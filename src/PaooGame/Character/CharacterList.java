package PaooGame.Character;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.sql.*;
public class CharacterList {
    //CharacterList e folosit pentru catalogarea tuturor personajelor de pe harta
    //charList e o lista pentru fiecare character si poate fi modificata corespunzator, fiind o lista
    //lastEnemy a trigger-ul pentru muzica diferita pt ultimul inamic
    public List<Character> charList;
    Boolean lastEnemy = false;
    public CharacterList(){

        charList= new ArrayList<>();
    }
    public Boolean contains(Integer x, Integer y){
        //cauta daca exista un personaj pe o anumita pozitie pe harta
        for(Character item: charList)
        {
            //if coordX==x && coordY==y true, else false
            if((item.coordX==x)&&(item.coordY==y)) return true;
        }
        return false;
    }
    public Boolean containsEnemy(Integer x, Integer y){
        //la fel ca contains dar numai pentru inamici
        for(Character item: charList)
        {
            //if coordX==x && coordY==y si daca personajul este inamic, true
            //otherwise false
            //folosit pentru coliziune inamici in A*
            if(((item.coordX==x)&&(item.coordY==y))&&item.enemy) return true;
        }
        return false;
    }
    public Character find(Integer x, Integer y){
        //returneaza personajul e pe o anumita pozitie
        for(Character item: charList)
        {
            if((Objects.equals(item.coordX, x))&&(Objects.equals(item.coordY, y))) return item;
        }
        return null;
    }

    public Character getAdjacentChar(String dir, GameCursor cursor){
        //verifica daca
        switch(dir)
        {
            case "UP":return find(cursor.getX(), cursor.getY()-1);
            case "DOWN":return find(cursor.getX(), cursor.getY()+1);
            case "LEFT":return find(cursor.getX() - 1, cursor.getY());
            case "RIGHT":return find(cursor.getX() + 1, cursor.getY());
            default: return null;
        }
    }
    public Boolean getLastEnemy(){return lastEnemy;}
    public void setLastEnemy(Boolean val){lastEnemy = val;}
    public void init(int lvl){
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            String projectRoot = System.getProperty("user.dir");
            String path = projectRoot + File.separator + "res" + File.separator + "gameData" + File.separator + "InitDataLv"+lvl+".db";
            c = DriverManager.getConnection("jdbc:sqlite:"+path);
            c.setAutoCommit(false);
            String sql = "SELECT * FROM Stats";
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while( rs.next())
            {
                int x = rs.getInt("CoordX");
                int y = rs.getInt("CoordY");
                String name = rs.getString("Name");
                String clasa = rs.getString("Class");
                int hp = rs.getInt("HP");
                int str = rs.getInt("Str");
                int spd = rs.getInt("Spd");
                int def = rs.getInt("Def");
                int res = rs.getInt("Res");
                int mov = rs.getInt("Mov");
                Boolean pdmg = false;
                if(rs.getInt("physDMG")==1) pdmg = true;
                Boolean hostile = false;
                if(rs.getInt("enemy")==1) hostile = true;
                add(x,y, hostile, name, clasa, hp, str, spd, def, res, mov, pdmg);
            }

            c.close();
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

    public void add(Integer x, Integer y, Boolean hostile, String name, String clasa, int hp, int str,int spd, int def, int res, int mov, Boolean pdmg){
        if(!contains(x,y)) charList.add(new Character(x, y, hostile, name, clasa, hp, str, spd, def, res, mov, pdmg));
    }
    public void endTurn(){
        for(Character item: charList)
        {
            if(item.getEnemy()==false) {
                item.setCanMove(true);
                item.setCanAttak(true);
            }

        }
    }
    public Boolean hasHeroes(){
        for(Character unit:charList){
            if(!unit.getEnemy())
                return true;
        }
        return false;
    }
    public Boolean hasEnemy(){
        for(Character unit:charList){
            if(unit.getEnemy())
                return true;
        }
        return false;
    }

    public Boolean lastEnemy(){
        int counter = 0;
        for(Character unit:charList){
            if(unit.getEnemy())
                counter++;
        }
        return counter == 1;
    }

    public Boolean isHeroDead(){
        for(Character unit:charList)
        {
            if(Objects.equals(unit.clasa, "Lord")) return false;
        }
        return true;
    }
    public void saveData(int lvl){
        Connection c = null;
        Statement stmt = null;
    try{
        //connect to the savestate database
        Class.forName("org.sqlite.JDBC");
        String projectRoot = System.getProperty("user.dir");
        String nameDB = "saveState"+lvl+".db";
        String path = projectRoot + File.separator + "res" + File.separator + "saveFiles" + File.separator + nameDB;
        c = DriverManager.getConnection("jdbc:sqlite:"+path);
        stmt = c.createStatement();
        //tocmai am invatat ca DriveManager.getConnection cu sqlite creeaza un fisier database cand acesta nu exista
        //deci inafara de trei batai de cap, bonus functionality hurray (nu mai trebuie sa verific daca fisierul exista)
        if(!c.getMetaData().getTables(null,null,"Stats", new String[]{"TABLE"}).next())
        {
            //tabelul Stats, in care va fi stocat save data-ul nu exista
            //asadar acesta este creat
            stmt.executeUpdate("CREATE TABLE Stats (" +
                    "Name TEXT, " +
                    "Class TEXT, " +
                    "MaxHP INTEGER, " +
                    "HP INTEGER, " +
                    "Str INTEGER, " +
                    "Spd INTEGER, " +
                    "Def INTEGER, " +
                    "Res INTEGER, " +
                    "Mov INTEGER, " +
                    "physDMG INTEGER, " +
                    "CoordX INTEGER, " +
                    "CoordY INTEGER, " +
                    "isAlive INTEGER, " +
                    "canMove INTEGER, " +
                    "canAttak INTEGER, " +
                    "enemy INTEGER" +
                    ")");
            System.out.println("Tabel Stats creat");
        }
        stmt.executeUpdate("DELETE FROM Stats");
        for(Character unit : charList)
        {
            int pdmg = 2;
            if(unit.physDMG) pdmg = 1;
            else pdmg = 2;
            int alive = 0;
            if(unit.isAlive) alive = 1;
            int atk = 0;
            if(unit.canAttak) atk = 1;
            int enemy = 0;
            if (unit.enemy) enemy = 1;
            int move = 0;
            if(unit.canMove) move = 1;
            stmt.executeUpdate("INSERT INTO Stats (Name, Class, MaxHP, HP, Str, Spd, Def, Res, Mov, physDMG, CoordX, CoordY, isAlive, canMove, canAttak, enemy) " +
                    "VALUES ('" +unit.name +
                    "', '" + unit.clasa +
                    "', " + unit.maxhp +
                    ", " + unit.hp +
                    ", " +unit.str +
                    ", " +unit.spd +
                    ", " +unit.def +
                    ", " +unit.res +
                    ", " +unit.mov +
                    ", " +pdmg +
                    ", " +unit.coordX +
                    ", " +unit.coordY +
                    ", " +alive +
                    ", " +move +
                    ", " +atk +
                    ", " +enemy +
                    ")");
        }
        c.close();
    }catch (Exception e){
        System.err.println( e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
    }

    }
    public void loadData(int lvl){
        Connection c = null;
        Statement stmt = null;
        try{
            //connect to the savestate database
            Class.forName("org.sqlite.JDBC");
            String projectRoot = System.getProperty("user.dir");
            String nameDB = "saveState"+lvl+".db";
            String path = projectRoot + File.separator + "res" + File.separator + "saveFiles" + File.separator + nameDB;
            c = DriverManager.getConnection("jdbc:sqlite:"+path);
            stmt = c.createStatement();
            c.setAutoCommit(false);
            String sql = "SELECT * FROM Stats";
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(c.getMetaData().getTables(null,null,"Stats", new String[]{"TABLE"}).next())
            {
                //tabelul exista
                //pot fi citite informatii din el
                List<Character> proxy = new ArrayList<>();
                Character tempChar;
                while( rs.next())
                {
                    int x = rs.getInt("CoordX");
                    int y = rs.getInt("CoordY");
                    String name = rs.getString("Name");
                    String clasa = rs.getString("Class");
                    int hp = rs.getInt("HP");
                    int str = rs.getInt("Str");
                    int spd = rs.getInt("Spd");
                    int def = rs.getInt("Def");
                    int res = rs.getInt("Res");
                    int mov = rs.getInt("Mov");
                    Boolean pdmg = rs.getInt("physDMG")==1;
                    Boolean hostile = rs.getInt("enemy")==1;
                    tempChar = new Character(x, y, hostile, name, clasa, hp, str, spd, def, res, mov, pdmg);
                    tempChar.maxhp = rs.getInt("MaxHP");
                    tempChar.coordX = rs.getInt("CoordX");
                    tempChar.coordY = rs.getInt("CoordY");
                    tempChar.isAlive = rs.getInt("IsAlive") == 1;
                    tempChar.canMove = rs.getInt("canMove") == 1;
                    tempChar.canAttak = rs.getInt("canAttak") == 1;
                    proxy.add(tempChar);
                }
                charList=proxy;
            }
            c.close();
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
