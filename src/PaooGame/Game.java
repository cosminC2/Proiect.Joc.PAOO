package PaooGame;

import PaooGame.Character.Character;
import PaooGame.Character.CharacterList;
import PaooGame.Character.CharacterMenu;
import PaooGame.Character.GameCursor;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Assets;
import PaooGame.Misc.Sound.Sound;
import PaooGame.Tiles.Tile;
import PaooGame.Misc.AStar;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.*;

/*! \class Game
    \brief Clasa principala a intregului proiect. Implementeaza Game - Loop (Update -> Draw)

                ------------
                |           |
                |     ------------
    60 times/s  |     |  Update  |  -->{ actualizeaza variabile, stari, pozitii ale elementelor grafice etc.
        =       |     ------------
     16.7 ms    |           |
                |     ------------
                |     |   Draw   |  -->{ deseneaza totul pe ecran
                |     ------------
                |           |
                -------------
    Implementeaza interfata Runnable:

        public interface Runnable {
            public void run();
        }

    Interfata este utilizata pentru a crea un nou fir de executie avand ca argument clasa Game.
    Clasa Game trebuie sa aiba definita metoda "public void run()", metoda ce va fi apelata
    in noul thread(fir de executie). Mai multe explicatii veti primi la curs.

    In mod obisnuit aceasta clasa trebuie sa contina urmatoarele:
        - public Game();            //constructor
        - private void init();      //metoda privata de initializare
        - private void update();    //metoda privata de actualizare a elementelor jocului
        - private void draw();      //metoda privata de desenare a tablei de joc
        - public run();             //metoda publica ce va fi apelata de noul fir de executie
        - public synchronized void start(); //metoda publica de pornire a jocului
        - public synchronized void stop()   //metoda publica de oprire a jocului
 */
public class Game implements Runnable
{
    private GameWindow      wnd;        /*!< Fereastra in care se va desena tabla jocului*/
    private CharacterMenu cm;
    private CharacterList cl;
    private GameCursor cursor;
    private boolean         runState;   /*!< Flag ce starea firului de executie.*/
    private Thread          gameThread; /*!< Referinta catre thread-ul de update si draw al ferestrei*/
    private BufferStrategy  bs;         /*!< Referinta catre un mecanism cu care se organizeaza memoria complexa pentru un canvas.*/
    /// Sunt cateva tipuri de "complex buffer strategies", scopul fiind acela de a elimina fenomenul de
    /// flickering (palpaire) a ferestrei.
    /// Modul in care va fi implementata aceasta strategie in cadrul proiectului curent va fi triplu buffer-at

    ///                         |------------------------------------------------>|
    ///                         |                                                 |
    ///                 ****************          *****************        ***************
    ///                 *              *   Show   *               *        *             *
    /// [ Ecran ] <---- * Front Buffer *  <------ * Middle Buffer * <----- * Back Buffer * <---- Draw()
    ///                 *              *          *               *        *             *
    ///                 ****************          *****************        ***************

    private Graphics        g;          /*!< Referinta catre un context grafic.*/
    private Integer[][] mapTable;

    private Boolean gameProgressState;
    private Sound sound;
    private Sound soundEffects;
    private int level;
    private Tile tile; /*!< variabila membra temporara. Este folosita in aceasta etapa doar pentru a desena ceva pe ecran.*/

    /*! \fn public Game(String title, int width, int height)
        \brief Constructor de initializare al clasei Game.

        Acest constructor primeste ca parametri titlul ferestrei, latimea si inaltimea
        acesteia avand in vedere ca fereastra va fi construita/creata in cadrul clasei Game.

        \param title Titlul ferestrei.
        \param width Latimea ferestrei in pixeli.
        \param height Inaltimea ferestrei in pixeli.
     */
    public Game(String title, int width, int height, int lvl)
    {
            /// Obiectul GameWindow este creat insa fereastra nu este construita
            /// Acest lucru va fi realizat in metoda init() prin apelul
            /// functiei BuildGameWindow();
        wnd = new GameWindow(title, width, height);
        level = lvl;
            /// Resetarea flagului runState ce indica starea firului de executie (started/stoped)
        runState = false;
    }

    /*! \fn private void init()
        \brief  Metoda construieste fereastra jocului, initializeaza aseturile, listenerul de tastatura etc.

        Fereastra jocului va fi construita prin apelul functiei BuildGameWindow();
        Sunt construite elementele grafice (assets): dale, player, elemente active si pasive.

     */
    private void InitGame()
    {
        wnd = new GameWindow("Schelet Proiect PAOO", 816, 624);
            /// Este construita fereastra grafica.
        wnd.BuildGameWindow();
            /// Se incarca toate elementele grafice (dale)

        Assets.Init();
    }
    private void InitData() throws FileNotFoundException {
        cl = new CharacterList();
        cl.init(level);
        cursor = new GameCursor();
        cm = new CharacterMenu();
        cm.init();
        gameProgressState = null;
        mapTable= new Integer[17][13];
        sound = new Sound();
        soundEffects = new Sound();
        cl.saveData(level);
        playMusic(0);
        //projectRoot + File.separator + "res" + File.separator + "gameData" + File.separator + "InitDataLv"+lvl+".db";
        System.out.println(new File("").getAbsolutePath());
        String mapPath = System.getProperty("user.dir") + File.separator + "res" + File.separator+ "gameData" + File.separator + "GridArrayLv" + level + ".txt";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("gameData/GridArrayLv1.txt")) {
            if(is !=null)
            {
                int jndex = 0;
                int index = 0;
                BufferedReader bfr = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = bfr.readLine()) != null) {
                    index = 0;
                    String[] nums = line.split(" ");
                    for(String num : nums){
                        Integer temp = Integer.parseInt(num);
                        if(index<13) {
                            mapTable[jndex][index] = temp;
                            index++;
                        }
                    }
                    jndex++;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /*! \fn public void run()
        \brief Functia ce va rula in thread-ul creat.

        Aceasta functie va actualiza starea jocului si va redesena tabla de joc (va actualiza fereastra grafica)
     */
    public void run()
    {
            /// Initializeaza obiectul game
        InitGame();
        try {
            InitData();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        long oldTime = System.nanoTime();   /*!< Retine timpul in nanosecunde aferent frame-ului anterior.*/
        long curentTime;                    /*!< Retine timpul curent de executie.*/
            /// Apelul functiilor Update() & Draw() trebuie realizat la fiecare 16.7 ms
            /// sau mai bine spus de 60 ori pe secunda.


        final int framesPerSecond   = 18; /*!< Constanta intreaga initializata cu numarul de frame-uri pe secunda.*/
        //FPS-ul a fost setat la 14 in mod intentionat, cum navigarea jocului (cum ar fi cursorul) depinde de framerate
        //acum inteleg de ce personajele din jocuri N64 (ex Golden Eye 007) se misca mai rapid la FPS mai mare
        final double timeFrame      = 1000000000 / framesPerSecond; /*!< Durata unui frame in nanosecunde.*/

            /// Atat timp timp cat threadul este pornit Update() & Draw()
        while (runState == true)
        {
                /// Se obtine timpul curent
            curentTime = System.nanoTime();
                /// Daca diferenta de timp dintre curentTime si oldTime mai mare decat 16.6 ms
            if((curentTime - oldTime) > timeFrame)
            {
                /// Actualizeaza pozitiile elementelor
                Update();
                /// Deseneaza elementele grafica in fereastra.
                Draw();
                oldTime = curentTime;
            }
        }
    }

    /*! \fn public synchronized void start()
        \brief Creaza si starteaza firul separat de executie (thread).

        Metoda trebuie sa fie declarata synchronized pentru ca apelul acesteia sa fie semaforizat.
     */
    public synchronized void StartGame()
    {
        if(runState == false)
        {
                /// Se actualizeaza flagul de stare a threadului
            runState = true;
                /// Se construieste threadul avand ca parametru obiectul Game. De retinut faptul ca Game class
                /// implementeaza interfata Runnable. Threadul creat va executa functia run() suprascrisa in clasa Game.
            gameThread = new Thread(this);
                /// Threadul creat este lansat in executie (va executa metoda run())
            gameThread.start();
        }
        else
        {
                /// Thread-ul este creat si pornit deja
            return;
        }
    }

    /*! \fn public synchronized void stop()
        \brief Opreste executie thread-ului.

        Metoda trebuie sa fie declarata synchronized pentru ca apelul acesteia sa fie semaforizat.
     */
    public synchronized void StopGame()
    {
        if(runState == true)
        {
                /// Actualizare stare thread
            runState = false;
                /// Metoda join() arunca exceptii motiv pentru care trebuie incadrata intr-un block try - catch.
            try
            {
                    /// Metoda join() pune un thread in asteptare panca cand un altul isi termina executie.
                    /// Totusi, in situatia de fata efectul apelului este de oprire a threadului.
                gameThread.join();
            }
            catch(InterruptedException ex)
            {
                    /// In situatia in care apare o exceptie pe ecran vor fi afisate informatii utile pentru depanare.
                ex.printStackTrace();
            }
        }
        else
        {
                /// Thread-ul este oprit deja.
            return;
        }
    }

    /*! \fn private void Update()
        \brief Actualizeaza starea elementelor din joc.

        Metoda este declarata privat deoarece trebuie apelata doar in metoda run()
     */
    private void Update()
    {
        gameProgressState = updateGameState();
        if(gameProgressState==null) {
            UpdateCursorMovement();
            PerformAction();
            PerformSaveAction();
            if(cl.lastEnemy()&&cl.getLastEnemy()!=null) cl.setLastEnemy(true);
            try {
                if (cl.getLastEnemy()) {
                    cl.setLastEnemy(null);
                    stopMusic();
                    playMusic(1);
                }
            }
            catch(NullPointerException e)
            {

            }
        }
        else {
            if(gameProgressState)
            {

                //WIN
            } else{
                //LOSE
                if(sound.getIndex()!=9) {
                    //conditie pentru a pune muzica doar o singura data
                    stopMusic();
                    playMusic(9);
                }
            }
        }


    }

    /*! \fn private void Draw()
        \brief Deseneaza elementele grafice in fereastra coresponzator starilor actualizate ale elementelor.

        Metoda este declarata privat deoarece trebuie apelata doar in metoda run()
     */
    private void Draw()
    {
            /// Returnez bufferStrategy pentru canvasul existent
        bs = wnd.GetCanvas().getBufferStrategy();
            /// Verific daca buffer strategy a fost construit sau nu
        if(bs == null)
        {
                /// Se executa doar la primul apel al metodei Draw()
            try
            {
                    /// Se construieste tripul buffer
                wnd.GetCanvas().createBufferStrategy(3);
                return;
            }
            catch (Exception e)
            {
                    /// Afisez informatii despre problema aparuta pentru depanare.
                e.printStackTrace();
            }
        }
            /// Se obtine contextul grafic curent in care se poate desena.
        g = bs.getDrawGraphics();
            /// Se sterge ce era
        g.clearRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());

            /// operatie de desenare
            // ...............

        if(gameProgressState==null) {
            for (int tileCoordX = 0; tileCoordX * Tile.TILE_WIDTH < wnd.GetWndWidth(); tileCoordX++) {
                for (int tileCoordY = 0; tileCoordY * Tile.TILE_HEIGHT < wnd.GetWndHeight(); tileCoordY++) {
                    Tile.tileList[mapTable[tileCoordX][tileCoordY]].Draw(g, tileCoordX * Tile.TILE_WIDTH, tileCoordY * Tile.TILE_HEIGHT);
                    if (isInFog(tileCoordX, tileCoordY))
                    //if(true)
                    {
                        if (cl.contains(tileCoordX, tileCoordY)) {
                            if (cl.find(tileCoordX, tileCoordY).getEnemy() == false) {
                                switch (cl.find(tileCoordX, tileCoordY).getClasa()) {
                                    case "Lord":
                                        Tile.tileList[155].Draw(g, tileCoordX * Tile.TILE_WIDTH, tileCoordY * Tile.TILE_HEIGHT);
                                        break;
                                    case "S-Paladin":
                                    case "L-Paladin":
                                    case "A-Paladin":
                                        Tile.tileList[153].Draw(g, tileCoordX * Tile.TILE_WIDTH, tileCoordY * Tile.TILE_HEIGHT);
                                        break;
                                    case "S-Cavalier":
                                    case "L-Cavalier":
                                    case "A-Cavalier":
                                        Tile.tileList[93].Draw(g, tileCoordX * Tile.TILE_WIDTH, tileCoordY * Tile.TILE_HEIGHT);
                                        break;
                                    case "S-Knight":
                                    case "L-Knight":
                                    case "A-Knight":
                                        Tile.tileList[61].Draw(g, tileCoordX * Tile.TILE_WIDTH, tileCoordY * Tile.TILE_HEIGHT);
                                        break;
                                    case "S-General":
                                    case "L-General":
                                    case "A-General":
                                        Tile.tileList[29].Draw(g, tileCoordX * Tile.TILE_WIDTH, tileCoordY * Tile.TILE_HEIGHT);
                                        break;
                                    case "Fighter":
                                        Tile.tileList[219].Draw(g, tileCoordX * Tile.TILE_WIDTH, tileCoordY * Tile.TILE_HEIGHT);
                                        break;
                                    case "Mage":
                                        Tile.tileList[185].Draw(g, tileCoordX * Tile.TILE_WIDTH, tileCoordY * Tile.TILE_HEIGHT);
                                        break;
                                    case "Rogue":
                                        Tile.tileList[186].Draw(g, tileCoordX * Tile.TILE_WIDTH, tileCoordY * Tile.TILE_HEIGHT);
                                        break;
                                }
                            } else {
                                switch (cl.find(tileCoordX, tileCoordY).getClasa()) {
                                    case "S-Paladin":
                                    case "L-Paladin":
                                    case "A-Paladin":
                                        Tile.tileList[154].Draw(g, tileCoordX * Tile.TILE_WIDTH, tileCoordY * Tile.TILE_HEIGHT);
                                        break;
                                    case "S-Cavalier":
                                    case "L-Cavalier":
                                    case "A-Cavalier":
                                        Tile.tileList[94].Draw(g, tileCoordX * Tile.TILE_WIDTH, tileCoordY * Tile.TILE_HEIGHT);
                                        break;
                                    case "S-Knight":
                                    case "L-Knight":
                                    case "A-Knight":
                                        Tile.tileList[62].Draw(g, tileCoordX * Tile.TILE_WIDTH, tileCoordY * Tile.TILE_HEIGHT);
                                        break;
                                    case "S-General":
                                    case "L-General":
                                    case "A-General":
                                        Tile.tileList[30].Draw(g, tileCoordX * Tile.TILE_WIDTH, tileCoordY * Tile.TILE_HEIGHT);
                                        break;
                                }
                            }

                        }
                    }
                    else
                        Tile.tileList[190].Draw(g, tileCoordX * Tile.TILE_WIDTH, tileCoordY * Tile.TILE_HEIGHT);
                    if (cursor.getSelectedChar() != null && cursor.getSelectedChar().getMoving()) {
                        int range = AStar.aStar(mapTable, cursor.getSelectedChar().getX(), cursor.getSelectedChar().getY(), tileCoordX, tileCoordY, cl);
                        if (range <= cursor.getSelectedChar().getStat("MOV") * 10) {
                            Tile.hoverTile.Draw(g, tileCoordX * Tile.TILE_WIDTH, tileCoordY * Tile.TILE_HEIGHT);
                        } else if (range <= (cursor.getSelectedChar().getStat("MOV") + 1) * 10) {
                            Tile.attackTile.Draw(g, tileCoordX * Tile.TILE_WIDTH, tileCoordY * Tile.TILE_HEIGHT);
                        }
                    }
                }
                if (cl.contains(cursor.getX(), cursor.getY())) {
                    //System.out.println("HOVER ON UNIT");
                    if (cl.find(cursor.getX(), cursor.getY()) != null)
                        cl.find(cursor.getX(), cursor.getY()).Draw(wnd);
                }

            }
            if (cm.getOpen()) {
                System.out.println("OPEN");
                cm.draw(g, cursor.getX() * Tile.TILE_WIDTH, cursor.getY() * Tile.TILE_HEIGHT);
            }
            if (cursor.getSelectedChar() != null) {
                System.out.println("Selected character");
                if (cursor.getSelectedChar().getDisplayStats()) {
                    System.out.println("DISPLAYING MENU");
                    cursor.getSelectedChar().dispStats(wnd);
                }
            }

            //g.drawRect(cursor.getX() * Tile.TILE_WIDTH, cursor.getY() * Tile.TILE_HEIGHT, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
            if (!(cursor.getSelectedChar() != null && cursor.getSelectedChar().getDisplayStats()))
                Tile.cursorTile.Draw(g, cursor.getX() * Tile.TILE_WIDTH, cursor.getY() * Tile.TILE_HEIGHT);
        }else{
            if(gameProgressState)
            {
                //win
                stopMusic();
                level++;
                gameProgressState=null;
            }else {
                //lose
                g.drawImage(Assets.gameOverScreen,0,0, wnd.GetWndWidth(),wnd.GetWndHeight(),null);
            }
        }

            // end operatie de desenare
            /// Se afiseaza pe ecran
        bs.show();

            /// Elibereaza resursele de memorie aferente contextului grafic curent (zonele de memorie ocupate de
            /// elementele grafice ce au fost desenate pe canvas).
        g.dispose();
    }

    public String CheckAdjacentEnemies(){
        if(cursor.getSelectedChar()!=null && cm.getOpen()==true)//I have a unit selected and the menu is open
        {
            //checking if tiles adjacent to cursor host any character
            if(cl.contains(cursor.getX()+1, cursor.getY()))//RIGHT
            {
                if(cl.find(cursor.getX()+1, cursor.getY()).getEnemy())//check if it's enemy
                {
                    System.out.println("ENEMY RIGHT");
                    return "RIGHT";
                }
            }
            if(cl.contains(cursor.getX()-1, cursor.getY()))//LEFT
            {
                if(cl.find(cursor.getX()-1, cursor.getY()).getEnemy())//check if it's enemy
                {
                    System.out.println("ENEMY LEFT");
                    return "LEFT";
                }
            }
            if(cl.contains(cursor.getX(), cursor.getY()+1))//DOWN
            {
                if(cl.find(cursor.getX(), cursor.getY()+1).getEnemy())//check if it's enemy
                {
                    System.out.println("ENEMY DOWN");
                    return "DOWN";
                }
            }
            if(cl.contains(cursor.getX(), cursor.getY()-1))//UP
            {
                if(cl.find(cursor.getX(), cursor.getY()-1).getEnemy())//check if it's enemy
                {
                    System.out.println("ENEMY UP");
                    return "UP";
                }
            }
        }
        return "NONE";
    }
    public void UpdateCursorMovement(){
        //dau update la cursor numai daca nu am vreun meniu deschis
        if(cm.getOpen()==false) {
            if (wnd.kh.downPressed == true) {
                if (cursor.getY() < (wnd.GetWndHeight()-1) / Tile.TILE_HEIGHT)
                    cursor.setY(cursor.getY() + 1);
                System.out.println("DOWN");
                playEffect(8);
            } else if (wnd.kh.leftPressed == true) {
                if (cursor.getX() > 0)
                    cursor.setX(cursor.getX() - 1);
                System.out.println("LEFT");
                playEffect(8);
            } else if (wnd.kh.upPressed == true) {
                if (cursor.getY() > 0)
                    cursor.setY(cursor.getY() - 1);
                System.out.println("UP");
                playEffect(8);
            } else if (wnd.kh.rightPressed == true) {
                if (cursor.getX() < wnd.GetWndWidth() / Tile.TILE_WIDTH - 1)
                    cursor.setX(cursor.getX() + 1);
                System.out.println("RIGHT");
                playEffect(8);
            }
            wnd.getWndFrame().requestFocusInWindow();
        }
    }
    public void PerformAction(){
        if(wnd.kh.spacePressed == true)//space has been pressed
        {
            wnd.kh.spacePressed = false;
            System.out.println("SPACE");
            playEffect(8);
            //this is retarded, but lets me wiggle with menu toggle later on
            if(cl.contains(cursor.getX(), cursor.getY()))
            {
                cursor.setSelectedChar(cl.find(cursor.getX(), cursor.getY()));
                if(!cursor.getSelectedChar().getEnemy()) {
                    if (cm.getOpen()) {
                        cm.setOpen(false);
                        cursor.setSelectedChar(null);
                    } else if (!cm.getOpen()) {
                        cm.setOpen(true);
                    }
                }

            }
            if(cursor.getSelectedChar()!=null)
            {
                if(cursor.getSelectedChar().getMoving()==true) {
                    System.out.println(AStar.aStar(mapTable,cursor.getSelectedChar().getX(),cursor.getSelectedChar().getY(), cursor.getX(),cursor.getY(), cl));
                    if (CheckMovement(cursor)) {
                        cursor.getSelectedChar().set(cursor.getX(), cursor.getY());
                        cursor.getSelectedChar().setMoving(false);
                        cursor.getSelectedChar().setCanMove(false);
                        cursor.setSelectedChar(null);
                    }
                }else {
                    if (cursor.getSelectedChar().getDisplayStats()) {
                        cursor.getSelectedChar().setDisplayStats(false);
                        ;
                    }
                    if (cursor.getSelectedChar().getEnemy()) {
                        if (cm.getOpen()) {
                            cm.setOpen(false);
                            cursor.setSelectedChar(null);
                        } else cm.setOpen(true);
                    }
                }
            }

        } else if(cm.getOpen() == true) {//one of the two (soon three) options has been selected: move and wait
            if (wnd.kh.zPressed == true) {
                System.out.println("Z");
                playEffect(8);
                if(cm.getOpen())//if menu is open
                {
                    wnd.kh.zPressed = false;
                    if(cursor.getSelectedChar()!=null&& !cursor.getSelectedChar().getEnemy())
                    {
                        if(cursor.getSelectedChar().getCanMove())
                        {
                            cursor.getSelectedChar().setMoving(true);
                            cm.setOpen(false);
                        }
                    }

                }
            } else if (wnd.kh.cPressed) {
                System.out.println("C");
                playEffect(8);
                wnd.kh.cPressed = false;
                //ATTACK
                if(CheckAdjacentEnemies()!="NONE"&&cursor.getSelectedChar().getCanAttak())
                {
                    //adjacent enemies exist
                    //closing menu to start combat
                    if(!cursor.getSelectedChar().getEnemy()) {
                        cm.setOpen(true);
                        cursor.getSelectedChar().enterCombat(cl.getAdjacentChar(CheckAdjacentEnemies(), cursor));
                        if (cl.getAdjacentChar(CheckAdjacentEnemies(), cursor).isAlive() == false) {
                            cl.charList.remove(cl.getAdjacentChar(CheckAdjacentEnemies(), cursor));
                            playEffect(4);
                        } else if (cursor.getSelectedChar().isAlive() == false) {
                            cl.charList.remove(cursor.getSelectedChar());
                            playEffect(3);
                        }
                        System.out.println(cursor.getSelectedChar().getStat("HP"));
                        cm.setOpen(false);
                        cursor.setSelectedChar(null);
                    }
                }
            } else if(wnd.kh.xPressed == true){
                wnd.kh.xPressed = false;
                System.out.println("X");
                playEffect(8);
                if(cursor.getSelectedChar().getDisplayStats())
                {
                    cursor.getSelectedChar().setDisplayStats(false);
                    ;
                }
                if(cm.getOpen()&&cursor.getSelectedChar()!=null)
                {
                    cursor.getSelectedChar().setDisplayStats(true);
                }
            }
        }
        if(wnd.kh.enterPressed == true)
        {
            wnd.kh.enterPressed=false;
            if((cursor.getSelectedChar()!=null && cursor.getSelectedChar().getMoving()==false)||cm.getOpen()==false) {
                cl.endTurn();
                playEffect(5);
                System.out.println("TURN ENDED");
            }
        }
    }

    public Boolean updateGameState(){
        if(!cl.hasEnemy()){
            //nu mai sunt inamici pe harta - true
            return true;
        }
        if(!cl.hasHeroes()||cl.isHeroDead()){
            //nu mai sunt eroi pe harta - false
            //personajul principal a murit - false
            return false;
        }
        //daca conditia de esec, dar nici conditia de castig nu sunt indeplinite
        //adica jocul este in plina desfasurare, se returneaza void
        //in alte cuvinte, Boolean-ul gameProgressState functioneaza ca un endgame check
        return null;
    }

    public void PerformSaveAction(){
        if(wnd.kh.gPressed==true)
        {
            wnd.kh.gPressed=false;
            System.out.println("G");
            cl.saveData(level);
            //quick save
            //parcurge cl
            //overwrite data din save file-ul existent
        }
        if(wnd.kh.hPressed==true)
        {
            wnd.kh.hPressed=false;
            System.out.println("H");
            cl.loadData(level);
            //quick load
            //parcurge save file-ul
            //adauga elementele intr-un characterlist proxy
            //sterge vechiul continut din cl, pune peste el cel din proxy
        }
    }
    public boolean CheckMovement(GameCursor cursor){
        int startX= cursor.getSelectedChar().getX();
        int startY= cursor.getSelectedChar().getY();
        int endX= cursor.getX();
        int endY= cursor.getY();
        if(AStar.aStar(mapTable,startX,startY,endX,endY, cl) <= (cursor.getSelectedChar().getStat("MOV")*10))
            return true;
        else return false;
        //algoritm A* dar in loc de a utiliza noduri, folosesc coordonate in grid
        //care tehnic vorbind este acelasi lucru, dar intr-o reprezentare diferita

    }

    public boolean isInFog(int x, int y){
        boolean ok = false;
        for(Character unit: cl.charList)
        {
            if(unit.getEnemy()==false)
            {
                int distance = Math.abs(x-unit.getX())+Math.abs(y-unit.getY());
                if(distance < unit.getStat("MOV")+1)
                    ok = true;
            }
        }
        return ok;
    }

    public void playMusic(Integer i){
        sound.setFile(i);
        sound.play();
        sound.loop();
    }
    public void stopMusic(){
        sound.stop();
    }
    public void playEffect(Integer i){
        soundEffects.setFile(i);
        soundEffects.play();
    }

    public int fn(int x, int y)
    {
        return (x*32)+y;
    }
}

