package myplane;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

//所有飞行物的父类
public abstract class FlyObject {
    //protected 对父类和所有子类可见
    protected Integer width;//宽度
    protected Integer height;//高度
    protected Integer x;//x坐标
    protected Integer y;//y坐标
    protected int life;
    //飞行物状态：活着 爆炸 死了得
    public static final int LIFE = 0;
    public static final int BOMB = 1;
    public static final int DEAD = 2;
    //飞行物得当前状态 默认初始状态是活着
    protected Integer state = LIFE;

    public void setStatebomb() {
        state = BOMB;
    }

    //飞行物移动

    public abstract void step();

    //根据飞行物得状态显示当前图片
    public abstract Image getImage();

    //判断是否活着
    public boolean isLife() {
        return state == LIFE;
    }

    //判断是否爆炸
    public boolean isBomb() {
        return state == BOMB;
    }

    //判断是否死了得
    public boolean isDead() {
        return state == DEAD;
    }

    // set/get方法 快捷键：alt+insert 选择setter getter
    //构造方法 快捷键：alt+insert  选择constructor

    public FlyObject(Integer width, Integer height, Integer x, Integer y, int life) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.life = life;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }


    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public int getLife() {
        if (life <= 0)
            return 0;
        else
            return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

}

//音乐类
class Music {
    File f;
    URI uri;
    URL url;
    AudioClip aau;

    Music() {
        try {
            f = new File("1.wav");
            uri = f.toURI();  //解析地址
            url = uri.toURL();
            aau = Applet.newAudioClip(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void use() {
        aau.loop();  //循环播放
    }

    public void stop() {
        aau.stop();
    }
}