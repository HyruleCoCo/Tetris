package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    public static final int WIDTH = 1280, HEIGHT = 720;
    final int FPS = 60;
    Thread gameThread;
    PlayManager pm;
    public static Sound music = new Sound();
    public static Sound se = new Sound();

    public GamePanel(){
        // panel settings
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setLayout(null);

        // implement KeyListner
        this.addKeyListener(new KeyHandler());
        this.setFocusable(true);


        pm = new PlayManager();
    }

    public void launchGame(){
        gameThread = new Thread(this);// initializes the thread class
        gameThread.start();// calls for the run method

        music.play(0, true);
        music.loop();
    }

    @Override
    public void run() {
        // game Loop
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null){
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if(delta >= 1){
                update();
                repaint();// calls paintComponent
                delta--;
            }
        }
    }

    private void update(){
        if(!KeyHandler.pausePressed && !pm.gameOver && KeyHandler.start)
            pm.update();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D)g;
        pm.draw(g2);
    }

}
