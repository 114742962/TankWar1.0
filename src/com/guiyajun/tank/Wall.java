package com.guiyajun.tank;

import java.awt.*;

public class Wall {
    
    
    public int width;
    public int height;
    public TankWarClient twc = null;
    
    private int x;
    private int y;
    
    Wall(int x, int y, int width, int height, TankWarClient twc) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.twc = twc;
    }
    
    public void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.BLACK);
        g.fillRect(x, y, width, height);        
        g.setColor(c);
    }
    
    public Rectangle getRectOfWall() {
        return new Rectangle(x, y, width, height);
    }
}
