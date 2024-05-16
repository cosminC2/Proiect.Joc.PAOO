package PaooGame.Misc;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

public class KeyHandler implements KeyListener{
    public Boolean upPressed, downPressed, leftPressed, rightPressed;
    public Boolean spacePressed, enterPressed;
    public Boolean zPressed, xPressed, cPressed;
    public KeyHandler(){
        upPressed=false;
        downPressed=false;
        leftPressed=false;
        rightPressed=false;
        spacePressed=false;
        enterPressed=false;
        zPressed = false;
        xPressed = false;
        cPressed = false;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e){
        int keycode = e.getKeyCode();
        if(keycode==KeyEvent.VK_W)
            upPressed = true;
        else if(keycode==KeyEvent.VK_A)
            leftPressed = true;
        else if(keycode== KeyEvent.VK_S)
            downPressed = true;
        else if(keycode == KeyEvent.VK_D)
            rightPressed = true;
    }
    @Override
    public void keyReleased(KeyEvent e){
        int keycode = e.getKeyCode();
        if(keycode==KeyEvent.VK_W)
            upPressed = false;
        else if(keycode==KeyEvent.VK_A)
            leftPressed = false;
        else if(keycode== KeyEvent.VK_S)
            downPressed = false;
        else if(keycode == KeyEvent.VK_D)
            rightPressed = false;
        else if(keycode == KeyEvent.VK_SPACE)
            spacePressed = true;
        else if(keycode == KeyEvent.VK_ENTER)
            enterPressed = true;
        else if(keycode == KeyEvent.VK_Z)
            zPressed = true;
        else if(keycode == KeyEvent.VK_X)
            xPressed = true;
        else if(keycode == KeyEvent.VK_C)
            cPressed = true;
    }
}
