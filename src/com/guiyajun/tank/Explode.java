package com.guiyajun.tank;
import java.awt.*;

public class Explode {
    
    public TankWarClient twc = null;
    
    private int[] diameter = {6, 14, 22, 30, 38, 38, 22};
    private int step = 0;
    private int x;
    private int y;
    private boolean aliveOfExplode = true;

    public Explode(int x, int y, TankWarClient twc) {
        this.x = x;
        this.y = y;
        this.twc = twc;
    }
    
    public void draw(Graphics g) {
        if (!aliveOfExplode) {
            return;
        }
        if (step == diameter.length) {
            aliveOfExplode = false;
            step = 0;
            return;
        }
        
        Color c = g.getColor();
        g.setColor(Color.RED);
        g.fillOval(x + Tank.TANK_WIDTH/2 - diameter[step]/2, y + Tank.TANK_WIDTH/2 - diameter[step]/2,
            diameter[step], diameter[step]);
        g.setColor(c);
        
        step ++;
    }
    
    public boolean isAliveOfExplode() {
        return aliveOfExplode;
    }

    public void setAliveOfExplode(boolean aliveOfExplode) {
        this.aliveOfExplode = aliveOfExplode;
    }
}
