package com.guiyajun.tank;
import java.awt.*;

public class Tank {
    
    public static final int TANK_WIDTH = 32;        // 坦克宽度
    public static final int TANK_HEIGHT = 32;       // 坦克高度
    public static final int TANK_MOVESTEP = 4;      // 坦克移动速度
    public TankWarClient twc = null;
    
    protected int x;                                  // 坦克X坐标
    protected int y;                                  // 坦克Y坐标
    protected int oldX;                                  // 坦克上一次X坐标
    protected int oldY;                                  // 坦克上一次Y坐标
    protected Direction dir = Direction.STOP;         // 初始化坦克的移动方向
    protected Direction dirOfBarrel = Direction.UP;
    protected boolean friendly = true;
    private int bloodOfTank = 4;
    private boolean aliveOfTank = true;
 
    public enum Direction {                                // 坦克方向
        UP,             // 上
        DOWN,           // 下
        LEFT,           // 左
        RIGHT,          // 右
        UP_LEFT,        // 左上
        UP_RIGHT,       // 右上
        DOWN_LEFT,      // 左下
        DOWN_RIGHT,     // 右下
        STOP            // 停止
    };
    
    public Tank(int x, int y, boolean friendly) {                     // 构造函数
        this.x = x;
        this.y = y;
        this.oldX = x;
        this.oldY = y;
        this.friendly = friendly;
    }
    
    public Tank(int x, int y, boolean friendly,TankWarClient twc) {                     // 构造函数
        this(x, y, friendly);
        this.twc = twc;
    }
    
    protected void move() {   // 根据方向使坦克移动
        this.oldX = x;
        this.oldY = y;
        
        switch (dir) {
            case UP:
                y -= TANK_MOVESTEP;
                break;
            case DOWN:
                y += TANK_MOVESTEP;
                break;
            case LEFT:
                x -= TANK_MOVESTEP;
                break;
            case RIGHT:
                x += TANK_MOVESTEP;
                break;
            case UP_LEFT:
                x -= Math.round(TANK_MOVESTEP * (Math.sqrt(2)/2));
                y -= Math.round(TANK_MOVESTEP * (Math.sqrt(2)/2));
                break;
            case UP_RIGHT:
                x += Math.round(TANK_MOVESTEP * (Math.sqrt(2)/2));
                y -= Math.round(TANK_MOVESTEP * (Math.sqrt(2)/2));
                break;
            case DOWN_LEFT:
                x -= Math.round(TANK_MOVESTEP * (Math.sqrt(2)/2));
                y += Math.round(TANK_MOVESTEP * (Math.sqrt(2)/2));
                break;
            case DOWN_RIGHT:
                x += Math.round(TANK_MOVESTEP * (Math.sqrt(2)/2));
                y += Math.round(TANK_MOVESTEP * (Math.sqrt(2)/2));
                break;
            case STOP:
                break;
            default:
                break;
        }
    }
    
    void getDirection() {}  // 获取坦克的方向
    
    void getBarrelDirection() {
        if (dir != Direction.STOP) {
            dirOfBarrel = dir;
        }
    }
    
    public void draw(Graphics g) {}   // 画出坦克和炮筒
    
    public void collisionDetection() {  // 坦克碰撞检测
        if (twc != null) {
            // 坦克与围墙碰撞检测
            if (x < 0) {
                stay();
            }
            else if (y < TankWarClient.SCORE_AREA) {
                stay();
            }
            else if (x > TankWarClient.GAME_WIDTH - Tank.TANK_WIDTH) {
                stay();
            }
            else if (y > TankWarClient.GAME_HEIGHT - Tank.TANK_HEIGHT) {
                stay();
            }
            
            // 坦克与内墙碰撞检测
            if (this.getRectOfTank().intersects(twc.wallLeft.getRectOfWall()) 
                || this.getRectOfTank().intersects(twc.wallRight.getRectOfWall())) {
                this.stay();
            }
            
            // 坦克与坦克的碰撞检测
            for (int i=0; i<twc.enemyTanks.size(); i++) {
                Tank tank = twc.enemyTanks.get(i);
                    
                if (this.isAlive() && tank.isAlive() && !this.equals(tank) && !this.equals(twc.myTank)) {
                    if (twc.myTank.getRectOfTank().intersects(this.getRectOfTank())
                        || tank.getRectOfTank().intersects(this.getRectOfTank())) {
                        this.stay();
                    }
                } else if (this.isAlive() && tank.isAlive() && this.equals(twc.myTank)) {
                    if (tank.getRectOfTank().intersects(twc.myTank.getRectOfTank())) {
                        twc.myTank.stay();
                    }
                }
            }
            
            // 坦克与加血包碰撞检测
            for(int i=0; i<twc.bloods.size(); i++) {
                for(int j=0; j<twc.enemyTanks.size(); j++) {
                    Blood blood = twc.bloods.get(i);
                    Tank tank = twc.enemyTanks.get(j);
                    if (blood.isAlive() && tank.isAlive() && blood.getRectOfBlood()
                        .intersects(tank.getRectOfTank())) {
                        blood.setAlive(false);
                        tank.setBloodOfTank(4);
                    } else if (blood.isAlive() && blood.getRectOfBlood().intersects(twc.myTank
                        .getRectOfTank())) {
                        blood.setAlive(false);
                        twc.myTank.setBloodOfTank(4);
                    }
                }
            }
        }
    }
    
    public Missile fire(Color c, Direction dirOfBarrel) {
        if (!isAlive()) {
            return null;
        }
        
        Missile missileOfMyTank = null;
        
        switch(dirOfBarrel) {   
            case UP:
                missileOfMyTank = new Missile(x + TANK_WIDTH / 2 - Missile.MISSILE_WIDTH / 2, 
                    y - TANK_HEIGHT * 1 / 4 - Missile.MISSILE_HEIGHT / 2, dirOfBarrel, this.twc, c);
                break;
            case DOWN:
                missileOfMyTank = new Missile(x + TANK_WIDTH/2 - Missile.MISSILE_WIDTH/2, 
                    y + TANK_HEIGHT * 5 / 4 - Missile.MISSILE_HEIGHT/2, dirOfBarrel, this.twc, c);
                break;
            case LEFT:
                missileOfMyTank = new Missile(x - TANK_WIDTH * 1 / 4 - Missile.MISSILE_WIDTH/2, 
                    y + TANK_HEIGHT/2 - Missile.MISSILE_HEIGHT/2, dirOfBarrel, this.twc, c);
                break;
            case RIGHT:
                missileOfMyTank = new Missile(x + TANK_WIDTH * 5 / 4 - Missile.MISSILE_WIDTH/2, 
                    y + TANK_HEIGHT/2 - Missile.MISSILE_HEIGHT/2, dirOfBarrel, this.twc, c);
                break;
            case UP_LEFT:
                missileOfMyTank = new Missile(x, y, dirOfBarrel, this.twc, c);
                break;
            case UP_RIGHT:
                missileOfMyTank = new Missile(x + TANK_WIDTH, y - Missile.MISSILE_HEIGHT, dirOfBarrel, this.twc, c);
                break;
            case DOWN_LEFT:
                missileOfMyTank = new Missile(x - Missile.MISSILE_WIDTH / 2, y + TANK_HEIGHT - Missile
                    .MISSILE_HEIGHT/2,dirOfBarrel, this.twc, c);
                break;
            case DOWN_RIGHT:
                missileOfMyTank = new Missile(x + TANK_WIDTH, y + TANK_HEIGHT, dirOfBarrel, this.twc, c);
                break;
            default:
                break;
        }
        
        return missileOfMyTank;
    }
    
    public void drawBarrel(Graphics g) {  
        // 根据炮筒方向画出炮筒
        getBarrelDirection();
        switch(dirOfBarrel) {   
            case UP:
                g.drawLine(x + TANK_WIDTH/2, y + TANK_HEIGHT/2, x + TANK_WIDTH/2, y - TANK_HEIGHT * 1 / 4);
                break;
            case DOWN:
                g.drawLine(x + TANK_WIDTH/2, y + TANK_HEIGHT/2, x + TANK_WIDTH/2, y + TANK_HEIGHT * 5 / 4);
                break;
            case LEFT:
                g.drawLine(x + TANK_WIDTH/2, y + TANK_HEIGHT/2, x - TANK_WIDTH * 1 / 4, y + TANK_HEIGHT/2);
                break;
            case RIGHT:
                g.drawLine(x + TANK_WIDTH/2, y + TANK_HEIGHT/2, x + TANK_WIDTH * 5 / 4, y + TANK_HEIGHT/2);
                break;
            case UP_LEFT:
                g.drawLine(x + TANK_WIDTH/2, y + TANK_HEIGHT/2, x, y);
                break;
            case UP_RIGHT:
                g.drawLine(x + TANK_WIDTH/2, y + TANK_HEIGHT/2,x + TANK_WIDTH, y);
                break;
            case DOWN_LEFT:
                g.drawLine(x + TANK_WIDTH/2, y + TANK_HEIGHT/2, x, y + TANK_HEIGHT);
                break;
            case DOWN_RIGHT:
                g.drawLine(x + TANK_WIDTH/2, y + TANK_HEIGHT/2, x + TANK_WIDTH, y + TANK_HEIGHT);
                break;
            default:
                break;
        }
    }
    
    public void stay() {
        x = oldX;
        y = oldY;
    }
    
    public int getBloodOfTank() {
        return bloodOfTank;
    }

    public void reduceBloodOfTank(int numOfBlood) {
        this.bloodOfTank -= numOfBlood;
    }
    
    public void setBloodOfTank(int bloodOfTank) {
        this.bloodOfTank = bloodOfTank;
    }
    
    public boolean isAlive() {
        return aliveOfTank;
    }

    public void setAliveOfTank(boolean aliveOfTank) {
        this.aliveOfTank = aliveOfTank;
    }
    
    public Rectangle getRectOfTank() {  // 获取坦克的方框
        return new Rectangle(x, y, TANK_WIDTH, TANK_HEIGHT);
    }
    
    public void drawBlood(Graphics g, int bloodOfTank) {
        // 根据炮筒方向画出血量值
        getBarrelDirection();
        switch(dirOfBarrel) {   
            case UP:
                g.drawString(" " + bloodOfTank, x + 10, y + 27);
                break;
            case DOWN:
                g.drawString(" " + bloodOfTank, x + 10, y + 12);
                break;
            case LEFT:
                g.drawString(" " + bloodOfTank, x + 18, y + 20);
                break;
            case RIGHT:
                g.drawString(" " + bloodOfTank, x + 5, y + 20);
                break;
            case UP_LEFT:
                g.drawString(" " + bloodOfTank, x + 15, y + 25);
                break;
            case UP_RIGHT:
                g.drawString(" " + bloodOfTank, x + 5, y + 25);
                break;
            case DOWN_LEFT:
                g.drawString(" " + bloodOfTank, x + 15, y + 15);
                break;
            case DOWN_RIGHT:
                g.drawString(" " + bloodOfTank, x + 5, y + 18);
                break;
            default:
                break;
        }
    }
}
    




