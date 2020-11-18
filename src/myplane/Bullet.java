package myplane;

import javax.swing.*;
import java.awt.*;

//子弹类
public class Bullet extends FlyObject {
    private Integer speed;//速度
    private Image image;
    private int bullettype;//子弹类型
    int index=0;

    @Override
    public void step() {
        if (bullettype == 2) {//向右上移动
            this.y -= speed;
            this.x -= speed;
            
        }
        if (bullettype == 3) {//向左上移动
            this.y -= speed;
            this.x += speed;
        }
        if(bullettype==1||bullettype==0) {
            this.y -= speed;//向上或向下移动
        }
    }

    @Override
    public Image getImage() {
        //活着得时候显示bullet.png 其他状态下消失得
        if(bullettype!=4) {
            if (isLife()) {
                return image;
            }
            return null;//没有图片显示
        }
        else {//bullet为4时时激光
            if(isLife()||isBomb()){
                if(index<=50) {
                    index++;
                    return image;
                }else {
                    this.state=DEAD;
                }
            }
            return null;
        }
    }

    public Bullet(int w, int h, int x, int y, Image a, int speed, int bullettype) {
       //子弹的x和y无法确定 要看英雄机
        super(w, h, x, y,0);
        this.speed = speed;
        this.image = a;
        this.bullettype = bullettype;
    }

    public boolean crash(FlyObject a) {//碰撞
        if (x <= a.getX() + a.getWidth() && x >= a.getX() && y <= a.getY() + a.getHeight()  && y >= a.getY())
            return true;
        if (x + width <= a.getX() + a.getWidth() && x + width >= a.getX() && y <= a.getY() + a.getHeight() && y>= a.getY())
            return true;
        if (x <= a.getX() + a.getWidth() && x >= a.getX() && y + height <= a.getY() + a.getHeight() && y + height >= a.getY())
            return true;
        if (x + width <= a.getX() + a.getWidth() && x + width >= a.getX()  && y + height <= a.getY() + a.getHeight() && y + height >= a.getY())
            return true;
        if (a.getX() >= x  && a.getX()<= x + width && a.getY() >= y && a.getY() <= y + height )
            return true;
        if (a.getX() + a.getWidth() >= x  && a.getX() + a.getWidth() <= x + width && a.getY() >= y && a.getY() <= y + height)
            return true;
        if (a.getX() >= x  && a.getX()<= x + width && a.getY() + a.getHeight() >= y && a.getY() + a.getHeight() <= y + height )
            return true;
        if (a.getX() + a.getWidth() >= x && a.getX() + a.getWidth() <= x + width && a.getY() + a.getHeight() >= y && a.getY() + a.getHeight() <= y + height )
            return true;
        return false;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getSpeed() {
        return speed;
    }


    public int getBullettype() {
        return bullettype;
    }

    public void setBullettype(int bullettype) {
        this.bullettype = bullettype;
    }
}
