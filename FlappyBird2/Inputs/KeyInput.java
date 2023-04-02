package FlappyBird2.Inputs;

import FlappyBird2.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput implements KeyListener {
    GamePanel gamePanel;

    public KeyInput(GamePanel gamepanel){
        this.gamePanel = gamepanel;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                gamePanel.Control(true);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
