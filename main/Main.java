package main;

import javax.swing.*;


public class Main{

    public static void main(String[] args) {
       
        JFrame window = new JFrame("Tetris");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);// window is in a set size

        // add gamepanel
        GamePanel gp = new GamePanel();
        window.add(gp);// adds game panel object to the window
        window.pack();// window adapts to panel size

        window.setLocationRelativeTo(null);// makes the window nativly set to be in the center of screen
        window.setVisible(true);

        gp.launchGame();
    }
}