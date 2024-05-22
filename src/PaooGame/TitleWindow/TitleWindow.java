package PaooGame.TitleWindow;

import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Assets;
import java.awt.*;

public class TitleWindow {
    private Boolean isOpen;

    public TitleWindow(){
        isOpen=false;
    }
    public static void draw(GameWindow wnd){
        Graphics g = wnd.GetCanvas().getBufferStrategy().getDrawGraphics();
        g.drawImage(Assets.mainMenu,wnd.GetWndWidth(),wnd.GetWndHeight(),null);

    }
}
