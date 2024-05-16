package PaooGame.Character;

import java.util.Objects;

public class GameCursor {
    private Integer coordX;
    private Integer coordY;
    private Character SelectedChar;
    public GameCursor(){
        coordX = 1;
        coordY = 1;
    }
    public Integer getX(){ return coordX;}
    public Integer getY(){ return coordY;}
    public void setX(Integer x){coordX = x;}
    public void setY(Integer y){coordY = y;}
    public boolean isCoord(Integer x, Integer y)
    {
        return Objects.equals(coordX, x) && Objects.equals(coordY, y);
    }

    public Character getSelectedChar() {
        return SelectedChar;
    }

    public void setSelectedChar(Character selectedChar) {
        SelectedChar = selectedChar;
    }


}
