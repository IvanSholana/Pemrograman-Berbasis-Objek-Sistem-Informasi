package FlappyBird2;

import java.io.IOException;

public class Game implements Runnable {
    Thread GameThread;
    GamePanel gamepanel;
    public Game() {
        gamepanel = new GamePanel();
        GameWindows gamewindow = new GameWindows(gamepanel);
        gamepanel.requestFocus();
        StartGameLoop();
    }
    private void StartGameLoop(){
        GameThread = new Thread(this);
        GameThread.start();
    }
    private final int FPS_SET = 120; // --> Banyak FPS yang diinginkan
    public void run(){
        double TimePerFrame = 1000000000.0 / FPS_SET;
        long lastFrame = System.nanoTime();
        long now = System.nanoTime();

        int frames = 0;
        long lastCheck = System.currentTimeMillis();
        while(true){
            now = System.nanoTime();
            if(now - lastFrame >= TimePerFrame){
                gamepanel.repaint();
                lastFrame = now;
                frames++;
            }
            if(System.currentTimeMillis() - lastCheck >= 1000){
                lastCheck = System.currentTimeMillis();
                frames = 0;
            }
        }
    }
}
