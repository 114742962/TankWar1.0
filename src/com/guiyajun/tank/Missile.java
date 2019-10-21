package com.guiyajun.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;

import com.guiyajun.tank.Tank.Direction;

public class Missile {
    
    public static final int MISSILE_WIDTH = 8;        // 子弹宽度
    public static final int MISSILE_HEIGHT = 8;       // 子弹高度
    public static final int MISSILE_MOVESTEP = 8;     // 子弹移动速度
    public TankWarClient twc = null;
    
    private int x = 0;     // 子弹的X坐标
    private int y = 0;     // 子弹的Y坐标
    private Color colorOfMissile = Color.BLACK;
    private boolean aliveOfMissile = true;      
    
    Tank.Direction dirOfMissile = null;
    
    Missile(int x, int y, Tank.Direction dirOfBarrel, TankWarClient twc) {
        this.x = x;
        this.y = y;
        this.dirOfMissile = dirOfBarrel;
        this.twc = twc;
    }
    
    Missile(int x, int y, Tank.Direction dirOfBarrel, TankWarClient twc, Color c) {
        this(x, y, dirOfBarrel, twc);
        this.colorOfMissile = c;
    }
    
    public void draw(Graphics g) {      // 画出子弹
        if (!this.getAliveOfMissile()) {
            return;
        }
        
        Color c = g.getColor();
        g.setColor(colorOfMissile);
        g.fillOval(x, y, MISSILE_WIDTH, MISSILE_HEIGHT);        
        g.setColor(c);
        
        move();
    }
    
    public void move() {   // 根据方向使子弹移动
        switch (dirOfMissile) {
            case UP:
                y -= MISSILE_MOVESTEP;
                break;
            case DOWN:
                y += MISSILE_MOVESTEP;
                break;
            case LEFT:
                x -= MISSILE_MOVESTEP;
                break;
            case RIGHT:
                x += MISSILE_MOVESTEP;
                break;
            case UP_LEFT:
                x -= Math.round(MISSILE_MOVESTEP * Math.sqrt(2)/2);
                y -= Math.round(MISSILE_MOVESTEP * Math.sqrt(2)/2);
                break;
            case UP_RIGHT:
                x += Math.round(MISSILE_MOVESTEP * Math.sqrt(2)/2);
                y -= Math.round(MISSILE_MOVESTEP * Math.sqrt(2)/2);
                break;
            case DOWN_LEFT:
                x -= Math.round(MISSILE_MOVESTEP * Math.sqrt(2)/2);
                y += Math.round(MISSILE_MOVESTEP * Math.sqrt(2)/2);
                break;
            case DOWN_RIGHT:
                x += Math.round(MISSILE_MOVESTEP * Math.sqrt(2)/2);
                y += Math.round(MISSILE_MOVESTEP * Math.sqrt(2)/2);
                break;
            default:
                break;
        }
        
        collisionDetection();
    }
    
    boolean hitTank(Tank tank) {
        boolean hit = false;
        if (this.aliveOfMissile && this.getRectOfMissle().intersects(tank.getRectOfTank()) 
            && tank.isAlive()) {
            hit = true;
            this.setAliveOfMissile(false);
            
            if(tank.equals(twc.myTank)) {
                tank.reduceBloodOfTank(1);
            } else {
                tank.reduceBloodOfTank(4);
            }
            
            if (tank.getBloodOfTank() <= 0) {
                tank.setAliveOfTank(false);
            }
                
            twc.explodes.add(new Explode(tank.x, tank.y, twc));
        }
        
        return hit;
    }
    
    public void hitEnemyTanks(List<EnemyTank> enemyTanks) {
        for (int i=0; i<enemyTanks.size(); i++) {
            Tank et = enemyTanks.get(i);
            hitTank(et);
        }
    }
    
    public boolean getAliveOfMissile() {
        return aliveOfMissile;
    }
    
    public void setAliveOfMissile(boolean live) {
        this.aliveOfMissile = live;
    }

    public void collisionDetection() {  // 子弹碰撞检测
        if (twc != null) {
            // 子弹与围墙碰撞检测
            if (x < 0 || y < TankWarClient.SCORE_AREA || x > TankWarClient.GAME_WIDTH 
                || y > TankWarClient.GAME_HEIGHT) {
            
                aliveOfMissile = false;
                twc.missilesOfMyTank.remove(this);
            }
            
            // 子弹与内墙碰撞检测
            if (this.getRectOfMissle().intersects(twc.wallLeft.getRectOfWall()) 
                || this.getRectOfMissle().intersects(twc.wallRight.getRectOfWall())) {
                aliveOfMissile = false;
                twc.missilesOfMyTank.remove(this);
            }
            
            // 子弹与子弹碰撞
            for (int i=0; i<twc.missilesOfMyTank.size(); i++) {
                for (int j=0; j<twc.missilesOfEnemyTanks.size(); j++) {
                    Missile mofmy = twc.missilesOfMyTank.get(i);
                    Missile mofen = twc.missilesOfEnemyTanks.get(j);
                    
                    if (mofmy != null && mofen != null && mofmy.getAliveOfMissile() 
                        && mofen.getAliveOfMissile()) {
                        
                        if (mofmy.getRectOfMissle().intersects(mofen.getRectOfMissle())) {
                            mofmy.setAliveOfMissile(false);
                            mofen.setAliveOfMissile(false);
                        }
                    }
                }
            }
        }
    }
    
    public Rectangle getRectOfMissle() {    // 获取子弹的方框
        return new Rectangle(x, y, MISSILE_WIDTH, MISSILE_HEIGHT);
    }
}
