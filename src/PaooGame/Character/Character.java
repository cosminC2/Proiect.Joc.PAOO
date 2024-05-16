package PaooGame.Character;

import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.ImageLoader;

import java.awt.*;

class Stats{
    protected String name;
    protected String clasa;
    protected Integer hp, maxhp, str, spd, def, res;
    protected Boolean physDMG;
    protected Boolean isAlive;
    public Stats(){
        name="SSeth";
        clasa="Paladin";
        hp=30;
        maxhp=hp;
        str=14;
        spd=12;
        def=11;
        res=3;
        physDMG=false;
        isAlive=true;
    }
    public Stats(String name, String Class, int hp, int str,int spd, int def, int res, Boolean pdmg){
        this.name=name;
        this.clasa=Class;
        this.hp=hp;
        this.str=str;
        this.spd=spd;
        this.def=def;
        this.res=res;
        physDMG=pdmg;
    }
    public void takeDamage(Stats s){
        Integer dmgTaken;
        if(this.isAlive&&s.isAlive) {
            if (s.physDMG) {
                dmgTaken = s.str - def;
            } else dmgTaken = s.str - res;
            hp = hp - dmgTaken;
            if (hp <= 0) {
                hp = 0;
                isAlive = false;
                System.out.println("DEAD");
            }
        }
        else System.out.println("ONE OF THE PARTIES IS DEAD");
    }
    public Integer getStat(String index){
        switch(index){
            case "HP": return hp;
            case "MAX HP": return maxhp;
            case "STR": return str;
            case "SPD": return spd;
            case "DEF": return def;
            case "RES": return res;
            default: return -99;
        }
    }
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

    public Character(Integer x, Integer y){
        super();
        coordX = x;
        coordY = y;
        canMove = true;
        canAttak = true;
        isMoving = false;
        enemy = false;
        inCombat=false;
        isAlive=true;
        displayStats=false;
        img= ImageLoader.LoadImage("/textures/PaooPlaceholderCharSprite.png");
    }
    public Character(Integer x, Integer y, Boolean hostile){
        super();
        coordX = x;
        coordY = y;
        enemy = hostile;
        canMove = true;
        canAttak = true;
        isMoving = false;
        physDMG=false;
        isAlive=true;
        displayStats=false;
        if(enemy) {
            super.name = "Bad guy";
            super.clasa = "Bad Paladin";
            img = ImageLoader.LoadImage("/textures/PaooPlaceholderEnemySprite.png");
        }
    }
    public void set(Integer x, Integer y){
        coordX = x;
        coordY = y;
    }
    public int getX(){return coordX;}
    public int getY(){return coordY;}
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
        Image imgBase = ImageLoader.LoadImage("/textures/PaooHoverMenuBase.png");
        g.drawImage(imgBase,16,wnd.GetWndHeight()-96,imgBase.getWidth(null)*2,imgBase.getHeight(null)*2,null);
        g.drawImage(img,120,wnd.GetWndHeight()-88,null);
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
            Image imgMenuBase = ImageLoader.LoadImage("/textures/PaooCharMenuBase.png");
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
            g.drawImage(img,(int) (0.08*wnd.GetWndWidth()),(int) (0.1225*wnd.GetWndHeight()),3*img.getWidth(null),3*img.getHeight(null),null);
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
