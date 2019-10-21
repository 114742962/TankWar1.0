package com.guiyajun.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class MyTank extends Tank {
    
    public static Color COLOROFMISSILE = Color.BLUE;
    
    private boolean beLeft = false;                                 // 按键状态，左
    private boolean beRight = false;                                // 按键状态，右
    private boolean beUp = false;                                   // 按键状态，左                    
    private boolean beDown = false;                                 // 按键状态，下
    
    public MyTank(int x, int y, boolean friendly, TankWarClient twc) {    // 将客户端的引用传入我方坦克
        super(x, y, friendly, twc);
    }
    
    @Override
    public void draw(Graphics g) {
        if(!this.isAlive()) {
            return;
        }
        
        // 定义坦克的格式
        Color c = g.getColor();
        g.setColor(Color.GREEN);
        g.fillOval(x, y, TANK_WIDTH, TANK_HEIGHT);
        g.setColor(Color.DARK_GRAY);
        
        move();
        collisionDetection();
        drawBarrel(g);
        g.setColor(Color.RED);
        drawBlood(g, getBloodOfTank());
//        bloodBar.draw(g, getBloodOfTank());
        g.setColor(c);
    }
    
    public void keyPressed(KeyEvent e) {    // 按键按下时修改按键的标记状态，并使坦克根据方向移动
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                beUp = true;
                break;
            case KeyEvent.VK_LEFT:
                beLeft = true;
                break;
            case KeyEvent.VK_RIGHT:
                beRight = true;
                break;                    
            case KeyEvent.VK_DOWN:
                beDown = true;
                break;
            default:
                break;
        }
        
        getDirection();     // 刷新坦克方向
    }
    
    public void keyReleased(KeyEvent e) {   // 按键释放时修改按键的标记状态，并使坦克根据方向移动
        int keyCode = e.getKeyCode();
        switch(keyCode) {
            case KeyEvent.VK_F2:
                myTankReset();
                break;
            case KeyEvent.VK_F8:
                twc.lauchFrame();
                break;            
            case KeyEvent.VK_F:
                twc.missilesOfMyTank.add(fire(COLOROFMISSILE, dirOfBarrel));
                break;
            case KeyEvent.VK_UP:
                beUp = false;
                break;
            case KeyEvent.VK_LEFT:
                beLeft = false;
                break;
            case KeyEvent.VK_RIGHT:
                beRight = false;
                break;                    
            case KeyEvent.VK_DOWN:
                beDown = false;
                break;
            case KeyEvent.VK_A:
                superFire(COLOROFMISSILE);
            default:
                break; 
        }
        
        getDirection();     // 刷新坦克方向
    }
    
    @Override
    void getDirection() {   // 获取坦克的方向
        if(beUp && !beDown && !beLeft && !beRight) {dir = Direction.UP;}
        else if (!beUp && beDown && !beLeft && !beRight) {dir = Direction.DOWN;}
        else if (!beUp && !beDown && beLeft && !beRight) {dir = Direction.LEFT;}
        else if (!beUp && !beDown && !beLeft && beRight) {dir = Direction.RIGHT;}
        else if (beUp && !beDown && beLeft && !beRight) {dir = Direction.UP_LEFT;}
        else if (beUp && !beDown && !beLeft && beRight) {dir = Direction.UP_RIGHT;}
        else if (!beUp && beDown && beLeft && !beRight) {dir = Direction.DOWN_LEFT;}
        else if (!beUp && beDown && !beLeft && beRight) {dir = Direction.DOWN_RIGHT;}
        else {dir = Direction.STOP;}
    }
    
    public void superFire(Color c) {
        if (!isAlive()) {
            return;
        }
        
        Direction[] dirs = Direction.values();
        
        for(int i=0; i<dirs.length -1; i++) {
            twc.missilesOfMyTank.add(fire(COLOROFMISSILE, dirs[i]));
        }
    }
    
    public void myTankReset() {
        twc.myTank = new MyTank(700, 400, true, twc);
    }
}
