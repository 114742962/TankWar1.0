package com.guiyajun.tank;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;

public class TankWarClient extends Frame {
    
    public static final int GAME_WIDTH = 800;   // 游戏窗口的宽度
    public static final int GAME_HEIGHT = 600;   // 游戏窗口的高度
    public static final int SCORE_AREA = 60;
    
    public Wall wallLeft = new Wall(100, 150, 20, 350, this);
    public Wall wallRight = new Wall(600, 150, 20, 350, this);
    public List<EnemyTank> enemyTanks = new ArrayList<>();
    public List<Missile> missilesOfMyTank = new ArrayList<>();
    public List<Missile> missilesOfEnemyTanks = new ArrayList<>();
    public List<Explode> explodes = new ArrayList<>();
    public List<Blood> bloods = new ArrayList<>();
    
    Image backScreen = null;    // 定义一个虚拟屏幕，目的是双缓冲，先把图片画到虚拟屏幕上
    MyTank myTank = null;    // 为主坦克定义一个变量
    
    public static void main(String[] args) {
        TankWarClient twk = new TankWarClient(); // 创建一个客户端
        twk.lauchFrame();   // 加载客户端上的元素
    }
    
    public void lauchFrame() {
        // 设置客户端格式
        this.setTitle("TankWar");
        this.setLocation(40, 120);
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.setBackground(Color.LIGHT_GRAY);
        this.setLayout(null);
        this.setResizable(false);
        this.setVisible(true);
        
        new Thread(new PaintTank()).start(); // 启动画面刷新线程
        
        myTank = new MyTank(700, 400, true, this);     // 创建一个主坦克
        bloods.add(new Blood(400, 300, this));
        bloods.add(new Blood(700, 300, this));
        if(enemyTanks.size() <= 0) {
            for(int i=0; i<10; i++) {
                enemyTanks.add(new EnemyTank(150, 80 + (50 * i), false, this));
            }
        }
        
        this.addWindowListener(new WindowAdapter() {    // 加入window事件监听
            @Override
            public void windowClosing(WindowEvent e) {
                // 关闭游戏窗口
                System.exit(0);
            }
        });
        
        this.addKeyListener(new KeyAdapter() {  // 加入键盘事件监听
            @Override
            public void keyPressed(KeyEvent e) {    // 监听按下键
                myTank.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {   // 监听释放键
                myTank.keyReleased(e);
            }
        });
    }
    
    @Override
    public void paint(Graphics g) {     //画坦克，如果有子弹画出子弹
        if(enemyTanks.size() <= 0) {
            for(int i=0; i<10; i++) {
                enemyTanks.add(new EnemyTank(150, 80 + (50 * i), false, this));
            }
        }
        
        for (int j=0; j<enemyTanks.size(); j++) {
            EnemyTank et = enemyTanks.get(j);
            
            if (et.isAlive()) {
                et.draw(g);
            }
            else {
                enemyTanks.remove(et);
            }
        }
        
        for (int i=0; i<missilesOfMyTank.size(); i++) {
            Missile m = missilesOfMyTank.get(i);
            if (m != null) {
                m.hitEnemyTanks(enemyTanks);
                
                if (m.getAliveOfMissile()) {
                    m.draw(g);
                } else {
                    missilesOfMyTank.remove(m);
                }
            }
        }
        
        for (int i=0; i<missilesOfEnemyTanks.size(); i++) {
            Missile m = missilesOfEnemyTanks.get(i);
            m.hitTank(myTank);
            
            if (m.getAliveOfMissile()) {
                m.draw(g);
            }
            else {
                missilesOfEnemyTanks.remove(m);
            }
        }
        
        for (int i=0; i<explodes.size(); i++) {
            Explode ep = explodes.get(i);
            if (!ep.isAliveOfExplode()) {
                explodes.remove(ep);
            }
            ep.draw(g);
        }
        
        for (int i=0; i<bloods.size(); i++) {
            Blood blood = bloods.get(i);
            if (!blood.isAlive()) {
                bloods.remove(blood);
            }
            blood.draw(g);
        }
        
        myTank.draw(g);
        wallLeft.draw(g);
        wallRight.draw(g);
    }
    
    @Override
    public void update(Graphics g) {
        // 在一个虚拟屏幕上画图，画完后再显示到真实屏幕上，利用双缓冲解决界面闪烁问题
        if(backScreen == null) {
            backScreen = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        
        Graphics gOfBackScreen = backScreen.getGraphics();  //获取虚拟屏幕的画笔
        Color c = gOfBackScreen.getColor();     // 获取画笔的初始颜色
        gOfBackScreen.setColor(Color.PINK);
        gOfBackScreen.fillRect(0, 0, GAME_WIDTH, SCORE_AREA);
        gOfBackScreen.setColor(Color.ORANGE);
        gOfBackScreen.fillRect(0, SCORE_AREA, GAME_WIDTH, GAME_HEIGHT - SCORE_AREA);  // 重新画出游戏的背景，从而覆盖掉之前的画面
        gOfBackScreen.setColor(Color.BLACK);
        gOfBackScreen.drawString("BloodOfMyTank: " + myTank.getBloodOfTank(), 20, 50);
        gOfBackScreen.drawString("EnemyTanks: " + enemyTanks.size(), 150, 50);
        gOfBackScreen.drawString("Missiles: " + missilesOfMyTank.size(), 260, 50);
        gOfBackScreen.drawString("Explode: " + explodes.size(), 340, 50);
        gOfBackScreen.drawString("MissilesOfEnemy: " + missilesOfEnemyTanks.size(), 420, 50);
        
        paint(gOfBackScreen);   // 使用虚拟屏画笔画出屏幕上的元素
        gOfBackScreen.setColor(c);      // 还原画笔的初始颜色
        g.drawImage(backScreen, 0, 0, null);   // 使用真实屏幕画笔将虚拟屏画出，0，0是图片左上角的位置
    }
    
    private class PaintTank implements Runnable {      //定义一个内部类，实现刷新屏幕
        @Override
        public void run() {
            while(true) {
                repaint();      // 调用重画方法，重画方法会先调用update方法，再调用paint方法
                try {
                    Thread.sleep(30);   // 每隔30ms刷新一次
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
