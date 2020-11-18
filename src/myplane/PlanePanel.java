package myplane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

//将images文件夹整个拷贝到当前项目得根目录中
//自定义画布
public class PlanePanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    //定义4种状态常量
    public static final int START = 1;//开始状态
    public static final int RUNNING = 2;//运行状态
    public static final int PAUSE = 3;//暂停状态
    public static final int GAMEOVER = 4;//结束状态
    public static final int LEVEL = 5;//关卡状态
    public static final int WIN = 6;//胜利状态
    private static Image winImage;
    private Image startImage;
    private Image pauseImage;
    private Image gameoverImage;
    private Image[] levelImage;//关卡前置图
    private Image[] bk;//背景
    private Image[] smallbullet;//飞机子弹集合
    //三种飞机的技能
    private Image laser;
    private Image bigimage1;
    private Image bigimage0;
    int frozen = 0;//冻结时间
    Music music = new Music();
    int level = 1;//关卡
    //游戏时间
    int time = 0;
    //金币
    int value = 0;
    boolean key = false;
    //保存背景图片得坐标
    Integer backgroundX = 0;
    Integer backgroundY = -5400;
    //英雄机
    Hero hero = new Hero();
    //Boss机
    Boss boss = new Boss(159, 89, level);
    ArrayList<Bullet> bossbullets = new ArrayList<Bullet>();
    //子弹集合
    ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    //小敌机 小蜜蜂 大敌机
    ArrayList<FlyObject> enemies = new ArrayList<FlyObject>();
    private int score = 0;
    //保存当前游戏状态
    int state = START;


    public PlanePanel() {
        //画背景图  new ImageIcon("文件路径") 相对路径：相对于当前工程的根目录开始寻找
        bk = new Image[3];
        levelImage = new Image[3];
        for (int i = 0; i < levelImage.length; i++) {
            levelImage[i] = new ImageIcon("plane_img/level" + (i + 1) + ".png").getImage();
        }
        for (int i = 0; i < bk.length; i++) {
            bk[i] = new ImageIcon("plane_img/background_" + (i + 1) + ".png").getImage();
        }
        startImage = new ImageIcon("images/interface_1.png").getImage();
        pauseImage = new ImageIcon("images/pause.png").getImage();
        gameoverImage = new ImageIcon("images/jeimian_2.png").getImage();
        smallbullet = new Image[3];
        for (int i = 0; i < smallbullet.length; i++) {
            smallbullet[i] = new ImageIcon("plane_img/smallbullet" + i + ".png").getImage();
        }
        laser = new ImageIcon("plane_img/bullet_11.png").getImage();
        winImage = new ImageIcon("plane_img/win.png").getImage();
        bigimage1 = new ImageIcon("plane_img/bigbullet1.png").getImage();
        bigimage0 = new ImageIcon("plane_img/bullet_5s.png").getImage();
    }

    //重写JPanel中得paint方法   Graphics g画笔
    public void paint(Graphics g) {
        super.paint(g);//父类得paint方法(画背景颜色等基础操作)
        //drawImage(要画得对象,要画得x坐标，y坐标,null)
        g.drawImage(bk[level - 1], backgroundX, backgroundY, null);
        //画英雄机
        g.drawImage(hero.getImage(), hero.getX(), hero.getY(), null);
        g.setColor(Color.RED);
        g.setFont(new Font("黑体", Font.ITALIC, 20));
        g.drawString("分数：" + score, 20, 30);
        g.drawString("生命：" + hero.getLife(), 20, 60);
        g.drawString("火力: " + hero.getDoubleFire(), 20, 90);
        g.drawString("金币: " + value, 20, 500);
        g.drawString("能量: " + hero.getTime(), 20, 530);
        g.drawString("关卡时间: " + time / 100, 250, 60);
        if (time >= 6000) {
            g.drawString("Boss生命:" + boss.getLife(), 250, 30);
        }
        //画子弹
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            g.drawImage(b.getImage(), b.getX(), b.getY(), null);
        }
        if (time >= 6000 && boss.isLife()) {
            g.drawImage(boss.getImage(), boss.getX(), boss.getY(), null);
            for (int i = 0; i < bossbullets.size(); i++) {
                Bullet bb = bossbullets.get(i);
                g.drawImage(bb.getImage(), bb.getX(), bb.getY(), null);
            }
        }
        for (int i = 0; i < enemies.size(); i++) {
            FlyObject e = enemies.get(i);
            if (e.isBomb()) {
                if (count % 8 == 0)
                    g.drawImage(e.getImage(), e.getX(), e.getY(), null);
            } else
                g.drawImage(e.getImage(), e.getX(), e.getY(), null);
        }
        switch (state) {
            case START:
                g.drawImage(startImage, 0, 0, null);
                break;
            case PAUSE:
                g.drawImage(pauseImage, 0, 0, null);
                break;
            case GAMEOVER:
                g.drawImage(gameoverImage, 0, 0, null);
                g.setColor(Color.yellow);
                g.setFont(new Font("黑体", Font.ITALIC, 30));
                g.drawString("最终分数为:" + score, 90, 110);
                break;
            case LEVEL:
                g.drawImage(levelImage[level - 1], 0, 0, null);
                break;
            case WIN:
                g.drawImage(winImage, 0, 0, null);
                g.setColor(Color.BLACK);
                g.setFont(new Font("黑体", Font.ITALIC, 30));
                g.drawString("最终分数为:" + score, 90, 220);
                break;
        }

    }

    //多线程实现移动
    //1.新建类 extends Thread 重写run
    //2.创建对象 start开启
    private int count = 0;//计数器

    public void move() {
        new Thread() {
            //匿名内部类：Thread类得子类
            @Override
            public void run() {
                while (true) {
                    if (state != RUNNING)
                        music.stop();
                    if (state == RUNNING) {
                        if (key) {
                            music.use();
                            key = false;
                        }
                        count++;
                        //背景图得移动：1.改坐标 2.刷新重画 3.睡眠
                        backgroundY++;
                        flymove();
                        //如果背景图得y坐标溢出屏幕，则从头开始
                        if (backgroundY >= 0) {
                            backgroundY = -5400;
                        }
                        //小敌机 大敌机 小蜜蜂 子弹移动
                        Into();
                        //入场
                        Bomb();
                        time++;
                    }
                    repaint();//继承来得
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void addAirplane() {
        enemies.add(new Airplane(level - 1, level));
    }

    public void addBigplane() {
        enemies.add(new BigPlane(level - 1, level));
    }

    public void addBee() {
        enemies.add(new Bee());
    }

    //三种英雄机的子弹添加
    public void addBullet() {
        if (hero.getType() == 0) {
            if (hero.getDoubleFire() != 0) {
                bullets.add(new Bullet(16, 16, hero.getX() + 24, hero.getY() - 10, smallbullet[0], 2, 0));
                bullets.add(new Bullet(16, 16, hero.getX() + 72, hero.getY() - 10, smallbullet[0], 2, 0));
                int x = hero.getDoubleFire();
                x--;
                hero.setDoubleFire(x);
            } else
                bullets.add(new Bullet(16, 16, hero.getX() + 45, hero.getY() - 10, smallbullet[0], 2, 0));
        } else if (hero.getType() == 1) {
            if (hero.getDoubleFire() != 0) {
                bullets.add(new Bullet(68, 18, hero.getX() - 20, hero.getY() - 10, smallbullet[1], 2, 2));
                bullets.add(new Bullet(68, 18, hero.getX() + 40, hero.getY() - 10, smallbullet[1], 2, 3));
                bullets.add(new Bullet(68, 18, hero.getX() + 20, hero.getY() - 10, smallbullet[1], 2, 0));
                int x = hero.getDoubleFire();
                x--;
                hero.setDoubleFire(x);
            } else {
                bullets.add(new Bullet(68, 18, hero.getX() + 20, hero.getY() - 10, smallbullet[1], 2, 0));
            }
        } else if (hero.getType() == 2) {
            if (hero.getDoubleFire() != 0) {
                bullets.add(new Bullet(10, 46, hero.getX() + 5, hero.getY() - 10, smallbullet[2], 2, 2));
                bullets.add(new Bullet(10, 46, hero.getX() + 25, hero.getY() - 10, smallbullet[2], 2, 2));
                bullets.add(new Bullet(10, 46, hero.getX() + 65, hero.getY() - 10, smallbullet[2], 2, 3));
                bullets.add(new Bullet(10, 46, hero.getX() + 45, hero.getY() - 10, smallbullet[2], 2, 0));
                bullets.add(new Bullet(10, 46, hero.getX() + 85, hero.getY() - 10, smallbullet[2], 2, 3));
                int x = hero.getDoubleFire();
                x--;
                hero.setDoubleFire(x);
            } else {
                bullets.add(new Bullet(10, 46, hero.getX() + 25, hero.getY() - 10, smallbullet[2], 2, 2));
                bullets.add(new Bullet(10, 46, hero.getX() + 65, hero.getY() - 10, smallbullet[2], 2, 3));
                bullets.add(new Bullet(10, 46, hero.getX() + 45, hero.getY() - 10, smallbullet[2], 2, 0));
            }
        }
    }

    //增加星星道具吃了增加能量
    public void addBall() {
        enemies.add(new ball());
    }

    //增加boss子弹根据三关的boss选择子弹
    public void addBossBullet() {
        Image a = new ImageIcon("plane_img/bullet_7.png").getImage();
        if (boss.getType() == 1) {
            if (count % 70 == 0) {
                bossbullets.add(new Bullet(19, 20, boss.getX() + 50, boss.getY() + 50, a, -2, 0));
            }
        } else if (boss.getType() == 2) {
            if (count % 150 == 0) {
                a = new ImageIcon("plane_img/fbossbullet.png").getImage();
                bossbullets.add(new Bullet(48, 48, (int) (Math.random() * 380), -36, a, -1, 1));
            }
            if (count % 90 == 0) {
                a = new ImageIcon("plane_img/bullet_5.png").getImage();
                bossbullets.add(new Bullet(34, 34, boss.getX() + 50, boss.getY() + 70, a, -2, 0));
            }
        } else if (boss.getType() == 3) {
            if (count % 300 == 0) {
                a = new ImageIcon("plane_img/bullet_4s.png").getImage();
                for (int i = 0; i <= 560; i += 80) {
                    bossbullets.add(new Bullet(80, 80, 0, i, a, -2, 4));
                    bossbullets.add(new Bullet(80, 80, 350, i, a, -2, 4));
                }
            }
            if (count % 90 == 0) {
                a = new ImageIcon("plane_img/bullet_8.png").getImage();
                bossbullets.add(new Bullet(18, 59, boss.getX() + 50, boss.getY() + 70, a, -2, 0));
            }
        }
    }


    //所有飞行物的移动
    public void flymove() {
        if (frozen <= 0) {
            boss.step();
            //boss子弹
            for (int i = 0; i < bossbullets.size(); i++) {
                Bullet bb = bossbullets.get(i);
                bb.step();
                if (bb.getY() > 600) {
                    bossbullets.remove(i--);
                }
            }
            //敌人
            for (int i = 0; i < enemies.size(); i++) {
                FlyObject e = enemies.get(i);
                e.step();
                if (e.getY() > 600) {
                    enemies.remove(i--);
                }
            }
        } else frozen--;

        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            b.step();
            if (b.getY() < -b.getHeight()) {
                bullets.remove(i--);
            }
        }
    }

    //入场
    public void Into() {
        if(frozen<=0) {
            if (time >= 6000 && boss.isLife()) {
                addBossBullet();
            } else {
                if (count % 50 == 0) {
                    addAirplane();
                }
                if (count % 240 == 0) {
                    addBigplane();
                }
            }
            if (count % 400 == 0) {
                addBee();
            }
            if (count % 600 == 0) {
                addBall();
            }
        }
        if (count % 20 == 0) {
            addBullet();
        }
    }

    //判断各种撞击的爆炸
    public void Bomb() {
        if (hero.getLife() <= 0) {
            hero.setStatebomb();
            state = GAMEOVER;
            return;
        }
        if (time >= 6000) {
            for (int i = 0; i < bossbullets.size(); i++) {
                if (hero.crash(bossbullets.get(i))&& bossbullets.get(i).isLife()) {
                    bossbullets.get(i).setStatebomb();
                    if (bossbullets.get(i).getBullettype() == 1) {
                        hero.setLife(hero.getLife() - 2);
                    }
                    else if(bossbullets.get(i).getBullettype()==0) {
                        hero.setLife(hero.getLife() - 1);
                    }
                    else if(bossbullets.get(i).getBullettype()==4){
                        hero.setLife(hero.getLife() - 2);
                    }
                }
            }
            if (hero.crash(boss)) {
                hero.setLife(hero.getLife() - 1);
                boss.setLife(boss.getLife() - 10);
                if (boss.getLife() <= 0) {
                    boss.setStatebomb();
                    score += 200;
                    value += 300;
                    time = 0;
                    if (level < 3) {
                        level++;
                        state = LEVEL;
                        boss = new Boss(159, 89, level);
                    } else
                        state = WIN;
                }
            }
        }
        for (int i = 0; i < enemies.size(); i++) {
            for (int j = 0; j < bullets.size(); j++) {
                Bullet bs = bullets.get(j);
                FlyObject enemy = enemies.get(i);
                if (bs.isLife() && bs.crash(boss) && boss.isLife() && time >= 6000) {
                    if (boss.getLife() <= 0) {
                        boss.setStatebomb();
                        score += 200;
                        value += 300;
                        time = 0;
                        if (level < 3) {
                            level++;
                            state = LEVEL;
                            boss = new Boss(159, 89, level);
                        } else
                            state = WIN;
                    } else {
                        bs.setStatebomb();
                        if (bs.getBullettype() != 1)
                            boss.setLife(boss.getLife() - (1 + hero.getType() * 5));
                        else
                            boss.setLife(boss.getLife() - (3 + hero.getType() * 5));
                    }
                }
                if (bs.isLife() && bs.crash(enemy) && enemy.isLife() && !(enemy instanceof ball)) {
                    if (bs.getBullettype() != 1) {
                        bs.setStatebomb();
                    }
                    if (enemy instanceof Bee) {
                        enemy.setStatebomb();
                        Bee b = (Bee) enemy;
                        if (b.getAwardType() == 0) {
                            hero.setLife(hero.getLife() + 1);
                        } else {
                            hero.setDoubleFire(hero.getDoubleFire() + 10);
                        }
                    } else {
                        if (bs.getBullettype() != 1) {
                            enemy.setLife(enemy.getLife() - (1 + hero.getType() * 5));
                            if (enemy.getLife() <= 0) {
                                enemy.setStatebomb();
                                if (enemies.get(i) instanceof Airplane) {
                                    score += 2;
                                    value += 2;
                                } else {
                                    score += 3;
                                    value += 3;
                                }
                            }
                        } else {
                            enemy.setLife(enemy.getLife() - (4 + hero.getType() * 5));
                            if (enemy.getLife() <= 0) {
                                enemy.setStatebomb();
                                if (enemy instanceof Airplane) {
                                    score += 2;
                                    value += 2;
                                } else {
                                    score += 3;
                                    value += 3;
                                }
                            }
                        }
                    }
                } else if (hero.crash(enemy)
                        && enemy.isLife() && !(enemy instanceof Bee)) {
                    enemy.setStatebomb();
                    if (!(enemy instanceof ball)) {
                        if (enemy instanceof Airplane) {
                            score += 2;
                            value += 2;
                        } else {
                            score += 3;
                            value += 3;
                        }
                        hero.setLife(hero.getLife() - 1);
                    } else {
                        hero.setTime(hero.getTime() + 25);
                        value += 5;
                    }
                }
            }
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        //鼠标点击
        if (state == START) {
            state = LEVEL;
        }
        //鼠标点击：结束状态转换为开始状态
        if (state == GAMEOVER) {
            //在下一轮游戏开始之前 保存游戏记录
            state = START;
            //开始下一次游戏之前 对象恢复到初始情况
            score = 0;
            time = 0;
            value = 0;
            level = 1;
            backgroundY = -5400;
            hero = new Hero();
            boss = new Boss(159, 89, 1);
            bullets.clear();//清空集合
            bossbullets.clear();
            enemies.clear();
        }
        if (state == PAUSE) {
            state = RUNNING;
            key = true;
        }
        if (state == WIN) {
            state = START;
            score = 0;
            time = 0;
            value = 0;
            level = 1;
            backgroundY = -5400;
            hero = new Hero();
            boss = new Boss(159, 89, 1);
            bullets.clear();//清空集合
            bossbullets.clear();
            enemies.clear();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //鼠标按下
        if (state == LEVEL) {
            state = RUNNING;
            bullets.clear();//清空集合
            bossbullets.clear();
            enemies.clear();
            key = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //鼠标释放
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //鼠标进入
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //鼠标离开
        if (state == RUNNING) {
            state = PAUSE;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //鼠标拖拽
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //鼠标移动：英雄机跟着鼠标移动
        if (state == RUNNING) {
            hero.setX(e.getX() - 45);
            hero.setY(e.getY() - 62);
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {


    }

    @Override
    public void keyReleased(KeyEvent e) {
        //购买飞机
        if (e.getKeyChar() == 'b' || e.getKeyChar() == 'B') {
            if (value >= 400 && value < 800 && hero.getType() < 1) {
                value -= 400;
                hero.setType(1);
                hero.reset();
            } else if (value >= 800 && hero.getType() < 2) {
                value -= 800;
                hero.setType(2);
                hero.reset();
            }
        }
        //释放技能
        if (e.getKeyChar() == 'G' || e.getKeyChar() == 'g') {
            if (hero.getTime() >= 50) {
                hero.setTime(hero.getTime() - 50);
                if (hero.getType() == 0) {
                    bullets.add(new Bullet(100, 100, 0, 500, bigimage0, 4, 1));
                    bullets.add(new Bullet(100, 100, 300, 500, bigimage0, 4, 1));
                }
                if (hero.getType() == 1) {
                    bullets.add(new Bullet(300, 79, 50, 500, bigimage1, 4, 1));
                    bullets.add(new Bullet(300, 79, 50, 700, bigimage1, 4, 1));
                }
                if (hero.getType() == 2) {
                    bullets.add(new Bullet(53, 172, 32, 100, laser, 4, 1));
                    bullets.add(new Bullet(53, 172, 32, 250, laser, 4, 1));
                    bullets.add(new Bullet(53, 172, 32, 400, laser, 4, 1));
                    bullets.add(new Bullet(53, 172, 172, 100, laser, 4, 1));
                    bullets.add(new Bullet(53, 172, 172, 250, laser, 4, 1));
                    bullets.add(new Bullet(53, 172, 172, 400, laser, 4, 1));
                    bullets.add(new Bullet(53, 172, 302, 100, laser, 4, 1));
                    bullets.add(new Bullet(53, 172, 302, 250, laser, 4, 1));
                    bullets.add(new Bullet(53, 172, 302, 400, laser, 4, 1));
                }
            }
        }
        //更改血量
        if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D') {
            hero.setLife(10000);
        }
        //冻结技能
        if(e.getKeyChar()=='f'||e.getKeyChar()=='F'){
            if(hero.getTime()>=100) {
                frozen = 200;
                hero.setTime(hero.getTime()-100);
            }
        }
    }
}
