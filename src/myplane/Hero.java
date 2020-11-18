package myplane;

import javax.swing.*;
import java.awt.*;

//英雄机
public class Hero extends FlyObject {
    //private Integer life;//生命值
    private Integer doubleFire;//火力值
    private Image images;
    private int time;//能量
    private int type;//类型
    public Hero() {
        //初始位置是固定的
        super(80, 80, 150, 400, 3);
        this.doubleFire = 0;
        this.images = new ImageIcon("plane_img/plane_2.png").getImage();
        this.type=0;
        this.time = 200;
    }

    public void step() {
        System.out.println("跟随鼠标移动");
    }

    @Override
    public Image getImage() {
        if (isLife()) {
            return images;
        }
        if (isBomb()) {
            state = DEAD;
        }
        return null;
    }

    public boolean crash(FlyObject enemy) {
        int xCenter = x+width/2;//英雄机中心点x坐标
        int yCenter = y+height/2;//英雄机中心点y坐标
        int x1 = enemy.getX()-width/2;//敌人的x-hero宽度/2
        int x2 = enemy.getX()+enemy.getWidth()+width/2;//敌人的x+敌人的宽度+hero宽度/2
        int y1 = enemy.getY()-height/2 ;//敌人的y-hero高度/2
        int y2 = enemy.getY()+enemy.getHeight()+height/2;//敌人的y+敌人的高度+hero高度/2
        return xCenter>x1 && xCenter<x2 && yCenter>y1 && yCenter<y2;
    }


    public void setDoubleFire(Integer doubleFire) {
        this.doubleFire = doubleFire;
    }

    public Integer getDoubleFire() {
        return doubleFire;
    }


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void reset(){
        if(type==1){
            this.width=100;
            this.height=100;
            this.x=150;
            this.y=400;
            this.images = new ImageIcon("plane_img/plane_6.png").getImage();
        }
        if(type==2){
            this.width=100;
            this.height=100;
            this.x=150;
            this.y=400;
            this.images = new ImageIcon("plane_img/plane_4.png").getImage();
        }
    }
}
