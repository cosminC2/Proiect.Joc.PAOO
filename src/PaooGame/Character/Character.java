package PaooGame.Character;

import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Assets;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class Stats{
    protected String name;
    protected String clasa;
    protected Integer hp, maxhp, str, spd, def, res, mov;
    protected Boolean physDMG;
    protected Boolean isAlive;
    public static Map<String, List<String>> WeaponType = Map.ofEntries(
            Map.entry("A", List.of("A-Paladin", "A-Cavalier", "A-Knight", "A-General", "Fighter")),
            Map.entry("L", List.of("L-Paladin", " L-Cavalier", " L-Knight", " L-General", "Solider")),
            Map.entry("S", List.of("S-Paladin", " S-Cavalier", " S-Knight", " S-General", "Lord", "Rogue")),
            Map.entry("N", List.of("Mage", " Monster", " Dragon"))
    );
//<<<<<<< HEAD
    public Stats(String name, String Class, int hp, int str,int spd, int def, int res, int mov, Boolean pdmg){
        this.name=name;
        this.clasa=Class;
        this.maxhp=hp;
        this.hp=hp;
        this.str=str;
        this.spd=spd;
        this.def=def;
        this.res=res;
        this.mov=mov;
        physDMG=pdmg;
    }
    public void takeDamage(Stats s){
        Integer dmgTaken;
        if(this.isAlive&&s.isAlive) {
            if (s.physDMG) {
                dmgTaken = (int)(s.str * WeaponTriangleMultiplier(s)) - def;
            } else dmgTaken = s.str - res;
            if(dmgTaken<0)dmgTaken=0;
            hp = hp - dmgTaken;
            if (hp <= 0) {
                hp = 0;
                isAlive = false;
                System.out.println("DEAD");
            }
        }
        else System.out.println("ONE OF THE PARTIES IS DEAD");
    }
    public double WeaponTriangleMultiplier(Stats s){
        //sincer, aici puteam adauga in Stats inca o variabila pentru tipul de arma
        //but I am so deep in this incat nu am chef, asadar voi face un switch de toti dracii in loc
        //nevermind am facut un Map cu ^^^
        String enemyWeap = null;
        String thisWeap = null;
        for(Map.Entry<String, List<String>> entry : WeaponType.entrySet())
        {
            if(entry.getValue().contains(s.clasa))
                enemyWeap=entry.getKey();
            if(entry.getValue().contains(this.clasa))
                thisWeap= entry.getKey();
        }
        if(Objects.equals(enemyWeap, thisWeap)) return 1;//daca ambele personaje au acelasi tip de arma, multiplierul este 1
        if(Objects.equals(enemyWeap, "N") || Objects.equals(thisWeap, "N")) return 1;//tipurile de arma neutre au multiplier nativ de 1
        switch(enemyWeap+thisWeap)
        {
            case "SA":
            case "LS":
            case "AL":
                //au fost considerate cazurile avantajoase
                //A = axe (topor)
                //S = sword (sabie)
                //L = lance (lance)
                //topoarele bat lancile (A > L)
                //sabiile bat topoarele ( S > A)
                //lancile bat sabiile ( T > S)
                return 1.3;
            default:
                return 0.7;
        }
    }

    public Integer getStat(String index){
        switch(index){
            case "HP": return hp;
            case "MAX HP": return maxhp;
            case "STR": return str;
            case "SPD": return spd;
            case "DEF": return def;
            case "RES": return res;
            case "MOV": return mov;
            default: return -99;
        }
    }

    public String getClasa() {return clasa;}

}
public class Character extends Stats{
    Integer coordX;
    Integer coordY;
    Boolean canMove;
    Boolean canAttak;
    Boolean isMoving;
    Boolean enemy;
    Boolean inCombat;
    Boolean displayStats;
    Image img;


    public Character(Integer x, Integer y, Boolean hostile, String name, String clasa, int hp, int str,int spd, int def, int res, int mov, Boolean pdmg)
    {
        super(name, clasa, hp, str,spd, def, res, mov, pdmg);
        coordX = x;
        coordY = y;
        enemy = hostile;
        canMove = true;
        canAttak = true;
        isMoving = false;
        inCombat=false;
        physDMG=pdmg;
        isAlive=true;
        displayStats=false;
        img= Assets.getCharacterImage(name);
    }
    public void set(Integer x, Integer y){
        coordX = x;
        coordY = y;
    }
//<<<<<<< HEAD
    public int getX(){return coordX;}
    public int getY(){return coordY;}
//=======
//>>>>>>> 1d4d000 (git commit)
    public Boolean getCanMove() {
        return canMove;
    }

    public Boolean getCanAttak() {
        return canAttak;
    }

    public Boolean getMoving() {
        return isMoving;
    }

    public void setMoving(Boolean moving) {
        isMoving = moving;
    }
    public void setCanMove(Boolean canMove) {
        this.canMove = canMove;
    }

    public void setCanAttak(Boolean canAttak) {
        this.canAttak = canAttak;
    }

    public boolean getEnemy(){return enemy;}
    public boolean isAlive(){return isAlive;}
    public void enterCombat(Character opponent){
        if(!inCombat)
        {
            if(spd>=opponent.spd)
            {
                opponent.takeDamage(this);
                takeDamage(opponent);
                if(spd>opponent.spd+5)
                    opponent.takeDamage(this);
            }
            else {
                takeDamage(opponent);
                opponent.takeDamage(this);
                if(opponent.spd>spd+5)
                    takeDamage(opponent);
            }
        }
        //attacking marks the turn as done
        canAttak=false;
    }

    public void Draw(GameWindow wnd){
        Graphics g = wnd.GetCanvas().getBufferStrategy().getDrawGraphics();
        g.setFont(new Font("Serif", Font.BOLD, 26));
        g.drawImage(Assets.imgBase,16,wnd.GetWndHeight()-96,Assets.imgBase.getWidth(null)*2,Assets.imgBase.getHeight(null)*2,null);
        g.drawImage(img,(int)(wnd.GetWndWidth()*0.132),(int)(wnd.GetWndHeight()*0.8625),null);
        String health = hp+"";
        g.drawString(health,78, wnd.GetWndHeight()-24);
        g.drawString(name,26, wnd.GetWndHeight()-56);}

//    public void dispStats(){
//        System.out.println("HP: "+hp+" / "+ maxhp);
//        System.out.println("STR: "+str);
//        System.out.println("SPD: "+spd);
//        System.out.println("DEF: "+def);
//        System.out.println("RES: "+res);
//        displayStats=true;
//    }
        public void dispStats(GameWindow wnd){
        if(displayStats)
        {
            Graphics g = wnd.GetCanvas().getBufferStrategy().getDrawGraphics();
            Image imgMenuBase =Assets.imeMenuBase;
            g.setFont(new Font("Monospaced", Font.BOLD, 36));
            g.setColor(new Color(248,240,136));
            g.drawImage(imgMenuBase,0,0, wnd.GetWndWidth(),wnd.GetWndHeight(),null);
            String health = hp + "  " + maxhp;
            g.drawString(health, (int) (0.11*wnd.GetWndWidth()),(int) (0.92*wnd.GetWndHeight()));
            g.drawString(str+"",(int) (0.54*wnd.GetWndWidth()),(int) (0.33*wnd.GetWndHeight()));
            g.drawString(spd+"",(int) (0.54*wnd.GetWndWidth()),(int) (0.43*wnd.GetWndHeight()));
            g.drawString(def+"",(int) (0.54*wnd.GetWndWidth()),(int) (0.63*wnd.GetWndHeight()));
            g.drawString(res+"",(int) (0.54*wnd.GetWndWidth()),(int) (0.73*wnd.GetWndHeight()));
            g.drawString(clasa,(int) (0.11*wnd.GetWndWidth()),(int) (0.73*wnd.GetWndHeight()));
            g.setColor(Color.black);
            g.drawString(name,(int) (0.17*wnd.GetWndWidth()),(int) (0.58*wnd.GetWndHeight()));
            g.drawImage(img,(int) (0.067*wnd.GetWndWidth()),(int) (0.129*wnd.GetWndHeight()),(int)(2.84*img.getWidth(null)),(int)(2.9*img.getHeight(null)),null);
        }
        else {
            System.out.println("STATS MENU IS CLOSED");
        }
        }
    public Boolean getDisplayStats() {
        return displayStats;
    }

    public void setDisplayStats(Boolean displayStats) {
        this.displayStats = displayStats;
    }
}
