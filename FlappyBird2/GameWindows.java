package FlappyBird2;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class GameWindows {

    InputStream BirdNormal = getClass().getResourceAsStream("Source/burung.png");
    private BufferedImage icon;
    private BufferedImage importImage(){
        try{
            icon = ImageIO.read(BirdNormal);
        }catch(IOException e) {
            e.printStackTrace();
        }finally {
            return icon;
        }
    }
    public GameWindows(GamePanel gamepanel){
        JFrame jframe = new JFrame();
        jframe.setSize(300,520);
        jframe.setVisible(true);
        jframe.add(gamepanel);
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setTitle("Flappy Bird");
        jframe.setIconImage(importImage());
        jframe.setResizable(false);

    }
}
