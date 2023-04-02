package PERTEMUAN_1;

import javax.swing.*;
import java.awt.*;

class MyFrame extends JFrame{
    int tinggi,lebar;
    MyFrame(int tinggi,int lebar){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.tinggi = tinggi;
        this.lebar = lebar;
    }
    public void paint(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        g2D.setPaint(Color.RED);
        g2D.fillRect(100,100,this.tinggi,this.lebar);
    }
}
public class First {
    public static void main(String[] args) {
        MyFrame frame1 = new MyFrame(100,100);
        MyFrame frame2 = new MyFrame(50,50);
        MyFrame frame3 = new MyFrame(250,50);

    }
}
