package PaooGame.Character;
import java.io.File;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.sql.*;
public class CharacterList {
    public List<Character> charList;
    Boolean lastEnemy = false;
    public CharacterList(){

        charList= new ArrayList<>();
    }
    public Boolean contains(Integer x, Integer y){
        for(Character item: charList)
        {
            //if coordX==x && coordY==y true, else false
            if((item.coordX==x)&&(item.coordY==y)) return true;
        }
        return false;
    }
    public Character find(Integer x, Integer y){
        for(Character item: charList)
        {
            if((Objects.equals(item.coordX, x))&&(Objects.equals(item.coordY, y))) return item;
        }
        return null;
    }

    public Character getAdjacentChar(String dir, GameCursor cursor){
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
}
