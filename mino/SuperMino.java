package mino;

import java.awt.*;

import javax.swing.*;

import main.*;


public class SuperMino {
    public Block b[] = new Block[4];
    public Block tempB[] = new Block[4];
    int autoDropCounter = 0;
    public int direction = 1;// there are 4 directions (1-> 2-> 3-> 4)
    boolean leftCollision, rightCollison, bottomCollision;
    public boolean active = true;
    public boolean deactivating;
    int deactiveCounter = 0;

    public void create(Color c){
        b[0] = new Block(c);
        b[1] = new Block(c);
        b[2] = new Block(c);
        b[3] = new Block(c);
        tempB[0] = new Block(c);
        tempB[1] = new Block(c);
        tempB[2] = new Block(c);
        tempB[3] = new Block(c);
    }

    public void setXY(int x, int y){

    }
    public void updateXY(int direction){
        checkRotationCollision();
        if(!leftCollision && !rightCollison && !bottomCollision){
            this.direction = direction;
            b[0].x = tempB[0].x;
            b[0].y = tempB[0].y;
            b[1].x = tempB[1].x;
            b[1].y = tempB[1].y;
            b[2].x = tempB[2].x;
            b[2].y = tempB[2].y;
            b[3].x = tempB[3].x;
            b[3].y = tempB[3].y;
        }
    }
    public void getDirection1(){
    }
    public void getDirection2(){        
    }
    public void getDirection3(){        
    }
    public void getDirection4(){        
    }
    public void checkMovementCollision(){
        leftCollision = false; 
        rightCollison = false;
        bottomCollision = false;

        staticCollisionCheck();

        // check fram collision
        // left wall
        for(int i = 0; i < b.length; i++){
            if(b[i].x == PlayManager.left_x){
                leftCollision = true;
            }
        }
        // right wall
        for(int i = 0; i < b.length; i++){
            if(b[i].x + Block.SIZE == PlayManager.right_x){
                rightCollison = true;
            }
        }
        // bottom wall
        for(int i = 0; i < b.length; i++){
            if(b[i].y + Block.SIZE == PlayManager.bottom_y){
                bottomCollision = true;
            }
        }

    }
    public void checkRotationCollision(){

        leftCollision = false; 
        rightCollison = false;
        bottomCollision = false;

        staticCollisionCheck();
        // check fram collision
        // left wall
        for(int i = 0; i < b.length; i++){
            if(tempB[i].x <= PlayManager.left_x){
                leftCollision = true;
            }
        }
        // right wall
        for(int i = 0; i < b.length; i++){
            if(tempB[i].x + Block.SIZE >= PlayManager.right_x){
                rightCollison = true;
            }
        }
        // bottom wall
        for(int i = 0; i < b.length; i++){
            if(tempB[i].y + Block.SIZE >= PlayManager.bottom_y){
                bottomCollision = true;
            }
        }
    }
    private void staticCollisionCheck(){
        for(int i = 0; i < PlayManager.staticBlocks.size(); i++){
            int targetX = PlayManager.staticBlocks.get(i).x;
            int targetY = PlayManager.staticBlocks.get(i).y;

            // check bottom collision
            for(int j = 0; j < b.length; j++){
                if(b[j].y + Block.SIZE == targetY && b[j].x == targetX){
                    bottomCollision = true;
                }
            }

            // check left collision
            for(int j = 0; j < b.length; j++){
                if(b[j].x - Block.SIZE == targetX && b[j].y == targetY){
                    leftCollision = true;
                }
            }

            // check right collision
            for(int j = 0; j < b.length; j++){
                if(b[j].x + Block.SIZE == targetX && b[j].y == targetY){
                    rightCollison = true;
                }
            }
        }
    }
    public void deactivating(){
        deactiveCounter++;
        // wait 15 frames until deactivating
        if(deactiveCounter == 15){
            deactiveCounter = 0;
            checkMovementCollision();
            if(bottomCollision){
                active = false;
            }
        }
    }
    public void update(){
        if(deactivating){
            deactivating();
        }

        // move the tetronimo
        if(KeyHandler.upPressed){

            switch(direction){
                case 1 -> getDirection2();
                case 2 -> getDirection3();
                case 3 -> getDirection4();
                case 4 -> getDirection1();                
            }

            KeyHandler.upPressed = false;
            GamePanel.se.play(3, false);
        }
        checkMovementCollision();
        if(!leftCollision){
            if(KeyHandler.leftPressed){
                b[0].x -= Block.SIZE;
                b[1].x -= Block.SIZE;
                b[2].x -= Block.SIZE;
                b[3].x -= Block.SIZE;
                KeyHandler.leftPressed = false;
            }
        }
        if(!rightCollison){
            if(KeyHandler.rightPressed){
                b[0].x += Block.SIZE;
                b[1].x += Block.SIZE;
                b[2].x += Block.SIZE;
                b[3].x += Block.SIZE;
                KeyHandler.rightPressed = false;
            }
        }
        if(bottomCollision){
            if(!deactivating){
                GamePanel.se.play(4, false);
            }
            deactivating = true;
        }
        else{
            if(KeyHandler.downPressed){
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;
                autoDropCounter = 0;

                KeyHandler.downPressed = false;
            }

            // lower the tetronimo
            autoDropCounter++;// increases every frame
            if(autoDropCounter == PlayManager.dropInterval){
                // lowers the tetronimos
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;
                autoDropCounter = 0;
            }
        }
        if(KeyHandler.spacePressed){
            while(!bottomCollision){
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;
                checkMovementCollision();
            }
            KeyHandler.spacePressed = false;
        }
    }
    public void draw(Graphics2D g2){
        int margin = 2;// 2 pixel gap between blocks
        g2.setColor(b[0].c);
        g2.fillRect(b[0].x + margin, b[0].y + margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        g2.fillRect(b[1].x + margin, b[1].y + margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        g2.fillRect(b[2].x + margin, b[2].y + margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        g2.fillRect(b[3].x + margin, b[3].y + margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
    }
}
