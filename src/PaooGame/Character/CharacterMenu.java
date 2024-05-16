package PaooGame.Character;


import PaooGame.Graphics.ImageLoader;
import PaooGame.Graphics.SpriteSheet;
import java.awt.image.BufferedImage;
import java.awt.*;


public class CharacterMenu {

    private Boolean isOpen;
    private BufferedImage img;

    public CharacterMenu(){
        isOpen=false;

    }
    public void init(){
        img = ImageLoader.LoadImage("/textures/PaooMenuSprite.png");

    }
    public void draw(Graphics g, int x, int y){
        g.drawImage(img,x+48,y,49*2,57*2,null);
    }
    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }


}
