package PaooGame.TitleWindow;

import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TitleWindow {
    private Boolean isOpen;
    private BufferedImage img;

    public TitleWindow(){
        isOpen=false;
        img= ImageLoader.LoadImage("/textures/PaooTitleScreen.png");
    }
    public void draw(GameWindow wnd){
        Graphics g = wnd.GetCanvas().getBufferStrategy().getDrawGraphics();

    }
}
