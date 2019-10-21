package com.guiyajun.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class EnemyTank extends Tank {
    
    public static Color COLOROFMISSILE = Color.BLACK;
    public static Random random = new Random();
    
    public int turnDirectionFrequency = 20;
    public int step = random.nextInt(turnDirectionFrequency) + 3;
    public int fireFrequency = 50;
    public int fireCountdown = random.nextInt(fireFrequency);
    
    public EnemyTank(int x, int y, boolean friendly, TankWarClient twc) {
        super(x, y, friendly, twc);
    }

    @Override
    public void draw(Graphics g) {
        if (!this.isAlive()) {
            return;
        }
        
        Color c = g.getColor();
        g.setColor(Color.LIGHT_GRAY);
        g.fillOval(x, y, TANK_WIDTH, TANK_HEIGHT);
        g.setColor(Color.DARK_GRAY);
        
        if (step == 0) {
            getDirection();
            getBarrelDirection();
            step = random.nextInt(turnDirectionFrequency) + turnDirectionFrequency;
        } else {
            step --;
        }
        
        if(fireCountdown == 0) {
            twc.missilesOfEnemyTanks.add(fire(COLOROFMISSILE, dirOfBarrel));
            fireCountdown= random.nextInt(fireFrequency) + fireFrequency;
        } else {
            fireCountdown --;
        }
        
        move();
        collisionDetection();
        drawBarrel(g);
        
        g.setColor(c);
    }
    
    @Override
    void getDirection() {   // 获取坦克的方向
        Direction[] dirOfEnemyTank = Direction.values();
        int indexOfDirection = random.nextInt(dirOfEnemyTank.length);
        dir = dirOfEnemyTank[indexOfDirection];
    }
}
