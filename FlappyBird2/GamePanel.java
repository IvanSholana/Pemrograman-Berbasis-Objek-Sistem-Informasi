package FlappyBird2;
import FlappyBird2.Inputs.KeyInput;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.util.Random;
import java.sql.*;


public class GamePanel extends JPanel {

    private BufferedImage backgroundImage,tanah,TiangAtas,TiangBawah,PesanAwal,PapanScore;
    private BufferedImage[] AnimationCharacter,img;
    private BufferedImage[] ScoreA = new BufferedImage[10];
    private int AniTick,AniIndex,AniSpeed = 10;
    private int ScoreIndexA = 0,ScoreIndexB = 0;
    private boolean Jump = Boolean.FALSE;
    private int falls;
    int xBird = 120;
    int YBird = 210;
    int XBackground = 10;

    int YTiangBawah = 200; // (Lebih besar lebih ke bawah)
    int YTiangAtas = -200; // (Lebih kecil lebih ke atas)
    Random rand = new Random();
    int TinggiTiang,TinggiTiang2;
    int XTiang = -55;
    private Boolean GameStart = false;
    // Background Hitam
    private Boolean backgroundhitam = false;
    private Boolean RestartGame = Boolean.FALSE;
    private Boolean PapanScoreKeluar = Boolean.FALSE;
    private int Score,HighScore;

    public void mysqlConnect(int score) {
            try {
                // Connect to the database
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/flappy", 
                        "root", 
                        "");

                // Create a statement
                Statement stmt = con.createStatement();

                // Execute the query
                ResultSet MaxScore = stmt.executeQuery("SELECT MAX(highscore) FROM score");
                
                //Insert data
                PreparedStatement pstmt = con.prepareStatement(
                        "INSERT INTO score values(?)"
                );
                pstmt.setInt(1, score);
                pstmt.executeUpdate();

                
                // Process the result set
                while (MaxScore.next()) {
                    this.HighScore = MaxScore.getInt(1);
                    System.out.println("highscore: " + this.HighScore);
                }

                // Close the resources
                MaxScore.close();
                stmt.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
    
    public GamePanel(){
        addKeyListener(new KeyInput(this));
        importImg();
        LoadAnimation();
        fall();
    }

    private void importImg() { // Memuat Gambar
        img = new BufferedImage[3];
        InputStream BirdNormal = getClass().getResourceAsStream("Source/yellowbird-midflap.png");
        InputStream BirdUp = getClass().getResourceAsStream("Source/yellowbird-upflap.png");
        InputStream BirdDown = getClass().getResourceAsStream("Source/yellowbird-downflap.png");
        InputStream Background = getClass().getResourceAsStream("Source/background-day.png");
        InputStream GambarTanah = getClass().getResourceAsStream("Source/base.png");
        InputStream Tiang_Atas = getClass().getResourceAsStream("Source/pipe-green-atas.png");
        InputStream Tiang_Bawah = getClass().getResourceAsStream("Source/pipe-green-bawah.png");
        InputStream Pesan_Awal = getClass().getResourceAsStream("Source/message.png");
        InputStream Papan_Score = getClass().getResourceAsStream("Source/Score.png");
        ////////////////////////////// SCORE INPUT IMAGE ////////////////////////////////////////
        InputStream[] skor = new InputStream[10];
        for(int a = 0; a < skor.length; a++){
            skor[a] = getClass().getResourceAsStream("Source/"+a+".png");
        }
        try{
            img[0] = ImageIO.read(BirdNormal);
            img[1] = ImageIO.read(BirdUp);
            img[2] = ImageIO.read(BirdDown);
            backgroundImage = ImageIO.read(Background);
            tanah = ImageIO.read(GambarTanah);
            TiangAtas = ImageIO.read(Tiang_Atas);
            TiangBawah = ImageIO.read(Tiang_Bawah);
            PesanAwal = ImageIO.read(Pesan_Awal);
            PapanScore = ImageIO.read(Papan_Score);
            for(int a = 0; a < skor.length; a++){
                ScoreA[a] = ImageIO.read(skor[a]);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void LoadAnimation(){
        AnimationCharacter = new BufferedImage[3];
        for(int a = 0; a < AnimationCharacter.length; a++){
            AnimationCharacter[a] = img[a];
        }
    }
    private void Animation(){
        AniTick++;
        if(AniTick >= AniSpeed){ // Mengatur kapan keseluruhan value berubah
            AniTick = 0;
            AniIndex++;
            XBackground-=10;
            if(GameStart){
                XTiang -= 10;
                tumbal2.x -= 10;
                tumbal3.x -= 10;
                tumbal4.x -= 10;
                tumbal5.x -= 10;
            }
            if(XBackground <= -284){ // Reset Posisi Background
                XBackground = 0;
            }
            if(XTiang <= -284-TiangAtas.getWidth()-55){ // Menentukkan kapan tinggi tiang berubah
                SetTinggiTiang();
                //////////////////////////
                XTiang = 284;
                tumbal2.x = 284;
                tumbal2.y = YTiangAtas - TinggiTiang;
                //////////////////////////
                tumbal3.x = XTiang+284+55;
                tumbal3.y = YTiangAtas - TinggiTiang + TinggiTiang2;
                /////////////////////////
                tumbal4.x = 284;
                tumbal4.y = YTiangBawah - TinggiTiang-5;
                /////////////////////////
                tumbal5.x = XTiang+284+55;
                tumbal5.y = YTiangBawah-TinggiTiang+TinggiTiang2;
            }
            if(Jump) {
                backgroundhitam = false;
                YBird -= 25;
                tumbal.y -= 25;
                falls = -30; // Membuat burung rotasi 30 derajat ke atas
                if(YBird <= 0){ // Membatasi posisi burung di tidak lebih dari panel atas
                    YBird = 0;
                    tumbal.y = 0;
                }
            }else{
                if(GameStart){
                    YBird += 10; // Membuat burung jatuh
                    tumbal.y += 10;
                    falls+=20; // Membuat burung berotasi 20 derajat tiap jatuh
                    if(falls >= 90){ // Menghentikan rotasi burung
                        falls = 90;
                    }
                    if(YBird >= 473-tanah.getHeight()){ // Membatasi posisi burung di atas tanah
                        YBird = 473-tanah.getHeight();
                    }
                }
            }
            Jump = Boolean.FALSE; // Membuat burung jatuh
            if(AniIndex >= AnimationCharacter.length){ // Prosedur untuk menset sprint animasi ke frame awal
                AniIndex = 0;
            }
            tumbal.y = YBird;
            tumbal.x = xBird+10;
            Poin();
        }
        Tabrakan();
    }
    private boolean Tabrakan(){
        if(tumbal.intersects(tumbal2)|| tumbal.intersects(tumbal3) || tumbal.intersects(tumbal4) || tumbal.intersects(tumbal5)){
            xBird-=1;
            backgroundhitam = Boolean.TRUE;
            RestartGame = Boolean.TRUE;
            Jump = Boolean.FALSE;
            PapanScoreKeluar = Boolean.TRUE;
            return true;
        }
        return false;
    }
    private void Poin(){
        if(GameStart && (XTiang == -255 || XTiang == 114 || XTiang == -246) && !backgroundhitam){
            if(ScoreIndexB > 8){
                ScoreIndexA++;
                ScoreIndexB = 0;
            }else{
                ScoreIndexB++;
            }
        }
    }
    public void Control(boolean Start) { // Mengontrol character lompat (tersambung keylistener keypressed)
        if(xBird == 120 && !Tabrakan() && !PapanScoreKeluar){
            Jump = Start;
            gamestart(true);
        }else{
            Restart();
        }
    }
    private void Restart(){
        gamestart(false);
        XTiang = -55;
        tumbal5.x = tumbal3.x = 300;
        xBird = 120;
        YBird = 210;
        Score = ScoreIndexA*10+ScoreIndexB;
        mysqlConnect(Score);
        ScoreIndexA = ScoreIndexB = 0;
        PapanScoreKeluar = Boolean.FALSE;
    }
    private AffineTransform fall(){ // Menentukkan posisi character dan rotasi character
        AffineTransform tr = new AffineTransform();
        tr.translate((int)xBird,(int)YBird);
        tr.rotate(Math.toRadians(falls),img[0].getWidth()/2,img[0].getHeight()/2);
        return tr;
    }

    private void SetTinggiTiang(){ // Menentukkan tinggi tiang
        TinggiTiang = rand.nextInt(60);
        TinggiTiang2 = rand.nextInt(10);
    }

    private void gamestart(boolean gameStart){
        this.GameStart = gameStart;
    }

    // BURUNG BAYANGAN
    Rectangle tumbal = new Rectangle(120,210,20,25);
    // TIANG ATAS BAYANGAN
    Rectangle tumbal2 = new Rectangle(XTiang,YTiangAtas-TinggiTiang,52,325);
    int tumbal3X = XTiang+284+55;
    int tumbal3Y = YTiangAtas-TinggiTiang+TinggiTiang2;
    Rectangle tumbal3 = new Rectangle(tumbal3X,tumbal3Y,52,325);
    // TIANG BAWAH BAYANGAN
    Rectangle tumbal4 = new Rectangle(XTiang,YTiangBawah-TinggiTiang-5,52,325);
    Rectangle tumbal5 = new Rectangle(XTiang+284+55,YTiangBawah-TinggiTiang+TinggiTiang2,52,325);

    public void paintComponent(Graphics g){ // --> Menampilkan suatu grafis ke dalam Panel atau menggambar dalam panel
        super.paintComponent(g); // Memanggil JPanel
        Graphics2D g2D = (Graphics2D) g;
        Animation(); // Prosedur untuk mengubah nilai parameter penting di bawah

        // Background
        g2D.drawImage(backgroundImage,XBackground,-20,null);
        g2D.drawImage(backgroundImage,XBackground+284,-20,null);

        // TIANG
        if(GameStart){
            g.setColor(new Color(128,0,0,0));
            g.fillRect(tumbal2.x, tumbal2.y, tumbal2.width, tumbal2.height); // TUMBAL TIANG ATAS
            g2D.drawImage(TiangAtas,XTiang,YTiangAtas-TinggiTiang,null);
            /////////////////////
            g.setColor(new Color(128,0,0,0)); // TUMBAL TIANG BAWAH
            g.fillRect(tumbal4.x,tumbal4.y,tumbal4.width,tumbal4.height); // TUMBAL TIANG BAWAH
            g2D.drawImage(TiangBawah,XTiang,YTiangBawah-TinggiTiang,null);
            /////////////////////
            g.setColor(new Color(128,0,0,0));
            g.fillRect(tumbal3.x, tumbal3.y, tumbal3.width, tumbal3.height); // TUMBAL TIANG ATAS
            g2D.drawImage(TiangAtas,tumbal5.x,YTiangAtas-TinggiTiang+TinggiTiang2,null);
            /////////////////////
            g.setColor(new Color(128,0,0,0)); // TUMBAL TIANG BAWAH
            g.fillRect(tumbal5.x,tumbal5.y,tumbal5.width,tumbal5.height); // TUMBAL TIANG BAWAH
            g2D.drawImage(TiangBawah,tumbal5.x,YTiangBawah-TinggiTiang+TinggiTiang2  ,null);
        }else{
            g2D.drawImage(TiangAtas,XTiang-65,YTiangAtas-TinggiTiang,null);
            g2D.drawImage(TiangBawah,XTiang-65,YTiangBawah-TinggiTiang,null);
            g2D.drawImage(TiangAtas,XTiang+284+65,YTiangAtas-TinggiTiang+TinggiTiang2,null);
            g2D.drawImage(TiangBawah,XTiang+284+65,YTiangBawah-TinggiTiang+TinggiTiang2  ,null);
        }

        // TANAH
        g2D.drawImage(tanah,XBackground,500-tanah.getHeight(),null);
        g2D.drawImage(tanah,XBackground+284,500-tanah.getHeight(),null);

        // Character
        if(GameStart){
            g.setColor(new Color(128,0,0,0));
            g.fillRect(tumbal.x,tumbal.y,tumbal.width, tumbal.height);
            g2D.drawImage(AnimationCharacter[AniIndex],fall(),null);
            if(backgroundhitam){
                g.setColor(new Color(0,0,0,50));
                g.fillRect(0,0,300,520);
            }
        }else{
            xBird = 120;
            YBird = 210;
            g.setColor(new Color(0,0,0,50));
            g.fillRect(0,0,300,520);
            g.setColor(new Color(128,0,0,0));
            g2D.drawImage(PesanAwal,45,40,null);
            g.fillRect(tumbal.x,tumbal.y,tumbal.width, tumbal.height);
            g2D.drawImage(AnimationCharacter[2],120,210,null);
            // BACKGROUND HITAM
            if(backgroundhitam){
                g.setColor(new Color(0,0,0,50));
                g.fillRect(0,0,300,520);
            }
        }
        if(PapanScoreKeluar){
            g2D.drawImage(PapanScore,100,190,null);
        }
        g2D.drawImage(ScoreA[ScoreIndexA],120,420,null);
        g2D.drawImage(ScoreA[ScoreIndexB],150,420,null);
    }

}
