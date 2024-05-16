package PaooGame.Character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CharacterList {
    public List<Character> charList;
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
    public void init(){
        add(1,2);
        add(2,1);
        add(3,2);
        add(2,3);
        add(4,3);
        add(3,4);
        addEnemy(2,10);
        addEnemy(2,8);
        addEnemy(11,3);
        addEnemy(11,10);
        addEnemy(13,2);
        addEnemy(10,0);
        charList.get(4).spd-=6;
    }

    public void add(Integer x, Integer y){

        if(!contains(x,y)) charList.add(new Character(x, y));
    }
    public void addEnemy(Integer x, Integer y){

        if(!contains(x,y))
            charList.add(new Character(x, y, true));
        find(x,y).physDMG=false;
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
}
