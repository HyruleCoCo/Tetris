package mino;

import java.awt.*;

// ◻
// ◻
// ◻
// ◻

public class Line extends SuperMino{
    public Line(){
        create(new Color(0, 128, 128));
    }

    public void setXY(int x, int y){
        // ◻  b[1]
        // ◻ b[0]   center of rotation
        // ◻ b[2]
        // ◻ b[3]

        b[0].x = x;
        b[0].y = y;
        b[1].x = b[0].x;
        b[1].y = b[0].y - Block.SIZE;
        b[2].x = b[0].x;
        b[2].y = b[0].y + Block.SIZE;
        b[3].x = b[2].x;
        b[3].y = b[2].y + Block.SIZE;
    }

    public void getDirection1(){
        // ◻
        // ◻
        // ◻
        // ◻
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].y = b[0].y - Block.SIZE;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y + Block.SIZE;
        tempB[3].x = b[0].x;
        tempB[3].y = b[0].y + (Block.SIZE * 2);
        updateXY(1);
    }
    public void getDirection2(){
        // ◻ ◻ ◻ ◻
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x - Block.SIZE;
        tempB[1].y = b[0].y;
        tempB[2].x = b[0].x + Block.SIZE;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x + (Block.SIZE * 2);
        tempB[3].y = b[0].y;
        updateXY(2);
    }
    public void getDirection3(){
        // ◻
        // ◻
        // ◻
        // ◻
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].y = b[0].y - Block.SIZE;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y + Block.SIZE;
        tempB[3].x = b[0].x;
        tempB[3].y = b[0].y + (Block.SIZE * 2);
        updateXY(3);
    }
    public void getDirection4(){
        // ◻ ◻ ◻ ◻
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x - Block.SIZE;
        tempB[1].y = b[0].y;
        tempB[2].x = b[0].x + Block.SIZE;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x + (Block.SIZE * 2);
        tempB[3].y = b[0].y;
        updateXY(4);        
    }
}
