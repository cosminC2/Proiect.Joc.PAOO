package PaooGame.Misc;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
    public Boolean upPressed, downPressed, leftPressed, rightPressed;
    public Boolean spacePressed, enterPressed;
    public Boolean zPressed, xPressed, cPressed;
    public Boolean gPressed, hPressed;
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
        gPressed = false;
        hPressed = false;
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
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_W:
                upPressed = false;
                break;
            case KeyEvent.VK_A:
                leftPressed = false;
                break;
            case KeyEvent.VK_S:
                downPressed = false;
                break;
            case KeyEvent.VK_D:
                rightPressed = false;
                break;
            case KeyEvent.VK_SPACE:
                spacePressed = true;
                break;
            case KeyEvent.VK_ENTER:
                enterPressed = true;
                break;
            case KeyEvent.VK_Z:
                zPressed = true;
                break;
            case KeyEvent.VK_X:
                xPressed = true;
                break;
            case KeyEvent.VK_C:
                cPressed = true;
                break;
            case KeyEvent.VK_G:
                gPressed = true;
                break;
            case KeyEvent.VK_H:
                hPressed = true;
                break;
        }
    }
}
