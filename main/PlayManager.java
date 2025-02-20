package main;

import mino.*;

import java.awt.*;
import java.util.*;

public class PlayManager {
    // play area
    final int WIDTH = 360, HEIGHT = 600;
    public static int left_x, right_x, top_y, bottom_y;

    // tetronimos
    SuperMino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;
    SuperMino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;
    SuperMino holdMino;
    final int HOLDMINO_X;
    final int HOLDMINO_Y;
    SuperMino tempMino;
    public static ArrayList<Block> staticBlocks = new ArrayList<>();

    // other
    public static int dropInterval = 60; // tetronimo drops every 60 frames
    boolean gameOver = false;
    boolean pieceHeld = false;
    public static int holdPerDrop = 0;

    // score
    public int score = 0;
    public int lines = 0;
    public int level = 1;

    // effects
    boolean effectCounterOn;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<>();

    public PlayManager(){
        // play area frame
        left_x = (GamePanel.WIDTH / 2) - (WIDTH / 2); // 1280/2 - 360/2 = 460
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;

        MINO_START_X = left_x + (WIDTH/2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        NEXTMINO_X = right_x + 175;
        NEXTMINO_Y = top_y + 500;

        HOLDMINO_X = left_x - 220;
        HOLDMINO_Y = top_y + 100;

        // set starting tetronimo
        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
        holdMino = pickMino();
        holdMino.setXY(HOLDMINO_X, HOLDMINO_Y);
    }
    private SuperMino pickMino(){
        //pick a random tetronimo
        SuperMino mino = null;
        int i = new Random().nextInt(14);

        switch(i){
            case 0 -> mino = new L1();
            case 1 -> mino = new L2();
            case 2 -> mino = new Line();
            case 3 -> mino = new S();
            case 4 -> mino = new Square();
            case 5 -> mino = new T();
            case 6 -> mino = new Z(); 

            case 7 -> mino = new S();
            case 8 -> mino = new L2();
            case 9 -> mino = new Square();
            case 10 -> mino = new Z();
            case 11 -> mino = new Line();
            case 12 -> mino = new T();
            case 13 -> mino = new L1();
        }
        return mino;
    }
    public void update(){
        if(KeyHandler.cPressed){
            if(holdPerDrop == 0){
                if(!pieceHeld){
                    holdMino = currentMino;
                    currentMino = pickMino();
                    currentMino.setXY(MINO_START_X, MINO_START_Y);
                    holdMino.setXY(HOLDMINO_X, HOLDMINO_Y);
                }
                else{
                    tempMino = holdMino;
                    holdMino = currentMino;
                    currentMino = tempMino;
                    currentMino.setXY(MINO_START_X, MINO_START_Y);
                    holdMino.setXY(HOLDMINO_X, HOLDMINO_Y);
                }
                holdPerDrop++;
                pieceHeld = true;
            }
            KeyHandler.cPressed = false;
        }
        if(!currentMino.active){
            // put mino in static blocks if not active
            holdPerDrop = 1;// prevents hold scumming
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            // check for game over

            currentMino.deactivating = false;
            if(currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y){
                GamePanel.se.play(2, false);
                // current mino immediately collided with a block and couldnt move
                gameOver = true;
            }

            // replace current Mino
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);
            holdPerDrop = 0;
            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

            // check if a line is cleared after a tetronimo becomes inactive
            checkLineClear();
            
        }
        else{
            currentMino.update();
        }
        
    }
    
    private void checkLineClear(){
        int x = left_x;
        int y = top_y;
        int blockCount = 0, lineCount = 0;

        while(x < right_x && y < bottom_y){

            for(int i = 0; i < staticBlocks.size(); i++){
                if(staticBlocks.get(i).x == x && staticBlocks.get(i).y == y){
                    blockCount++;
                }
            }
            x+= Block.SIZE;

            if(x== right_x){

                if(blockCount == 12){

                    effectCounterOn = true;
                    effectY.add(y);
                    for(int i = staticBlocks.size() - 1; i > -1; i--){
                        if(staticBlocks.get(i).y == y){
                            staticBlocks.remove(i);
                        }
                    }
                    
                    lineCount++;
                    lines++;

                    if(lines % 10 == 0 && dropInterval > 1){
                        level++;
                        if(dropInterval > 10){
                            dropInterval -=10;
                        }
                        else{
                            dropInterval -=1;
                        }
                    }

                    for(int i = 0; i < staticBlocks.size(); i++){
                        if(staticBlocks.get(i).y < y){
                            staticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }

                blockCount = 0;
                x = left_x;
                y += Block.SIZE;
            }
            GamePanel.se.play(1, false);
            // add score
            if(lineCount > 0){
                int singleLineScore = 1 * level;
                score += singleLineScore * lineCount;
            }
        }
    }
    public void draw(Graphics2D g2){
        if(!KeyHandler.start){
            g2.setColor(Color.yellow);
            
            g2.setStroke(new BasicStroke(4F));// set how thick the lines will be, this is 4 pixels
            int x = left_x;
            int y = top_y + 200;
            g2.setFont(new Font("Times New Roman", Font.PLAIN, 30));
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.drawString("PRESS ENTER TO START", x, y);

            g2.setColor(Color.white);
            x = left_x - 350;
            y = bottom_y - 500;
            g2.drawRect(x, y, 325, 400);
            g2.drawString("CONTROLS", x + 75, y + 50); y+= 100;
            x+=10;
            g2.drawString("UP: ROTATE PIECE", x, y); y+= 50;
            g2.drawString("RIGHT: MOVE RIGHT", x, y); y+= 50;
            g2.drawString("LEFT: MOVE LEFT", x, y); y+= 50;
            g2.drawString("DOWN: SOFT DROP", x, y); y+= 50;
            g2.drawString("SPACE: HARD DROP", x, y); y+= 50;
            g2.drawString("ESCAPE: PAUSE", x, y); y+= 50;
        }
        else{
            // draw play area
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(4F));// set how thick the lines will be, this is 4 pixels
            g2.drawRect(left_x-4, top_y-4, WIDTH+8, HEIGHT+8);

            // draw next frame
            int x = right_x + 100;
            int y = bottom_y -200;
            g2.drawRect(x, y, 200, 200);
            g2.setFont(new Font("Times New Roman", Font.PLAIN, 30));
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.drawString("NEXT", x+60, y+50);
            
            // score frame
            g2.drawRect(x, top_y, 250, 300);
            x+=40;
            y = top_y + 90;
            g2.drawString("LEVEL: " + level, x, y); y += 70;
            g2.drawString("SCORE: " + score, x, y); y += 70;
            g2.drawString("LINES: " + lines, x, y); 

            // control frame
            x = left_x - 350;
            y = bottom_y - 380;
            g2.drawRect(x, y, 325, 415);
            g2.drawString("CONTROLS", x + 75, y + 50); y+= 100;
            x+=10;
            g2.drawString("UP: ROTATE PIECE", x, y); y+= 50;
            g2.drawString("RIGHT: MOVE RIGHT", x, y); y+= 50;
            g2.drawString("LEFT: MOVE LEFT", x, y); y+= 50;
            g2.drawString("DOWN: SOFT DROP", x, y); y+= 50;
            g2.drawString("SPACE: HARD DROP", x, y); y+= 50;
            g2.drawString("C: HOLD PIECE", x, y); y+= 50;
            g2.drawString("ESCAPE: PAUSE", x, y); 

            // draw hold frame
            x = left_x - 300;
            y = top_y;
            g2.drawRect(x, y, 200, 200);
            g2.drawString("HOLD", x+60, y+50);

            // draw current tetronimo
            if(currentMino != null){
                currentMino.draw(g2);
            }

            nextMino.draw(g2);
            if(pieceHeld){
            holdMino.draw(g2);
            }
            // draw static blocks
            for(int i = 0; i < staticBlocks.size(); i++){
                staticBlocks.get(i).draw(g2);
            }

            // draw clear effect
            if(effectCounterOn){
                effectCounter ++;

                g2.setColor(Color.white);
                for(int i = 0; i < effectY.size(); i++){
                    g2.fillRect(left_x, effectY.get(i), WIDTH, Block.SIZE);
                }

                // how many frames the effect will be visible for
                switch(dropInterval){
                    case 60:
                        if(effectCounter == 20){
                            effectCounterOn = false;
                            effectCounter = 0;
                            effectY.clear();
                        }
                        break;
                    case 50:
                        if(effectCounter == 17){
                            effectCounterOn = false;
                            effectCounter = 0;
                            effectY.clear();
                        }
                        break;
                    case 40:
                        if(effectCounter == 15){
                            effectCounterOn = false;
                            effectCounter = 0;
                            effectY.clear();
                        }
                        break;
                    case 30:
                        if(effectCounter == 13){
                            effectCounterOn = false;
                            effectCounter = 0;
                            effectY.clear();
                        }
                        break;
                    case 20:
                        if(effectCounter == 10){
                            effectCounterOn = false;
                            effectCounter = 0;
                            effectY.clear();
                        }
                    break;
                    case 10: 
                        if(effectCounter == 7){
                            effectCounterOn = false;
                            effectCounter = 0;
                            effectY.clear();
                        }
                        break;
                    case 9:
                        if(effectCounter == 5){
                            effectCounterOn = false;
                            effectCounter = 0;
                            effectY.clear();
                        }
                        break;
                    case 8:
                        if(effectCounter == 5){
                            effectCounterOn = false;
                            effectCounter = 0;
                            effectY.clear();
                        }
                        break;
                    case 7:
                        if(effectCounter == 5){
                            effectCounterOn = false;
                            effectCounter = 0;
                            effectY.clear();
                        }
                        break;
                    case 6:
                        if(effectCounter == 5){
                            effectCounterOn = false;
                            effectCounter = 0;
                            effectY.clear();
                        }
                        break;
                    case 5:
                        if(effectCounter == 5){
                            effectCounterOn = false;
                            effectCounter = 0;
                            effectY.clear();
                        }
                        break;
                    case 4:
                        if(effectCounter == 5){
                            effectCounterOn = false;
                            effectCounter = 0;
                            effectY.clear();
                        }
                        break;
                    case 3:
                        if(effectCounter == 5){
                            effectCounterOn = false;
                            effectCounter = 0;
                            effectY.clear();
                        }
                        break;
                    case 2:
                        if(effectCounter == 5){
                            effectCounterOn = false;
                            effectCounter = 0;
                            effectY.clear();
                        }
                        break;
                    case 1:
                        if(effectCounter == 5){
                            effectCounterOn = false;
                            effectCounter = 0;
                            effectY.clear();
                        }
                        break;

                }
            }

            // pause or game over screen
            g2.setFont(g2.getFont().deriveFont(50));
            if(gameOver){
                g2.setColor(Color.black);
                g2.setStroke(new BasicStroke(4F));// set how thick the lines will be, this is 4 pixels
                g2.fill3DRect(left_x-4, top_y-4, WIDTH+8, HEIGHT+8, false);
                
                x = right_x + 100;
                y = bottom_y -200;
                g2.fill3DRect(x+10, y+50,180, 140, false);
                
                g2.setColor(Color.yellow);
                g2.setFont(new Font("Times New Roman", Font.PLAIN, 30));
                g2.setFont(g2.getFont().deriveFont(50f));
                x = left_x + 25;
                y = top_y + 320;
                g2.drawString("GAME OVER", x, y); y+= 50;

            }
            else if(KeyHandler.pausePressed){
                g2.setColor(Color.black);
                g2.setStroke(new BasicStroke(4F));// set how thick the lines will be, this is 4 pixels
                g2.fill3DRect(left_x-4, top_y-4, WIDTH+8, HEIGHT+8, false);

                x = right_x + 100;
                y = bottom_y -200;
                g2.fill3DRect(x+10, y+50,180, 140, false);
                
                g2.setColor(Color.yellow);
                g2.setFont(new Font("Times New Roman", Font.PLAIN, 30));
                g2.setFont(g2.getFont().deriveFont(50f));
                x = left_x + 70;
                y = top_y + 320;
                g2.drawString("PAUSED", x, y); y+= 50;
            }
        }
    }

}
