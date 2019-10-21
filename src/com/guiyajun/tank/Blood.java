package com.guiyajun.tank;

import java.awt.*;

/**
 * Simple to Introduction  
 * @ProjectName:  [TankWar] 
 * @Package:      [com.guiyajun.tank.Blood.java]  
 * @ClassName:    [Blood]   
 * @Description:  [实现了加血包，坦克吃了可以恢复血量]   
 * @Author:       [桂亚君]   
 * @CreateDate:   [2019年10月21日 下午4:13:44]   
 * @UpdateUser:   [桂亚君]   
 * @UpdateDate:   [2019年10月21日 下午4:13:44]   
 * @UpdateRemark: [去掉了两种加血包的设计，只保留一种，并去掉相应的变量]  
 * @Version:      [v1.0]
 */
public class Blood {

    public static int EAT_BLOOD_WIDTH = 15;
    public static int EAT_BLOOD_HEIGHT = 15;
    
    // 定义一个客户端类的变量，目的是为了方便管理图像上的血块
    public TankWarClient twc = null;
    
    // 定义血块的坐标变量
    private int x;
    private int y;
    
    private boolean alive = true;
    
    /**
    * 创建一个新的实例 Blood.
    * @param x  Blood的x坐标
    * @param y  Blood的y坐标
    * @param twc    客户端的实例
     */
    public Blood(int x, int y, TankWarClient twc) {
        this.x = x;
        this.y = y;
        this.twc = twc;
    }
    
    /**
     * 
    * @Title: draw
    * @Description: TODO(加血包的绘制方法)
    * @param g    参数，可以理解为绘画用的笔 
    * @return void    返回类型
    * @throws
     */
    public void draw(Graphics g) {
        if (!this.isAlive()) {
            return;
        }
        
        Color c = g.getColor();
        g.setColor(Color.RED);
        g.fillRect(x, y, EAT_BLOOD_WIDTH, EAT_BLOOD_HEIGHT);
        g.setColor(c);
    }
    
    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    
    public Rectangle getRectOfBlood() {    // 获取子弹的方框
        return new Rectangle(x, y, EAT_BLOOD_WIDTH, EAT_BLOOD_HEIGHT);
    }
}