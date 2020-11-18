package myplane;

import javax.swing.*;
import java.awt.*;

public class ball extends FlyObject {
    private Integer speedX;//x方向速度
    private Integer speedY;//y方向速度
    private Image image;

    public ball() {//游戏中的星星
        super(60, 50, (int) (Math.random() * 400), -50, 0);
        this.speedX = 2;
        this.speedY = 3;
        this.image=new ImageIcon("plane_img/boom2.png").getImage();
    }

    @Override
    public void step() {
        this.x += speedX;
        this.y += speedY;
        //到达窗口右边框 向左下移动
        if (this.x >= 400 - width) {
            speedX = -1;
        }
        //到达窗口左边框 向右下移动
        if (this.x <= 0) {
            speedX = 1;
        }
    }

    @Override
    public Image getImage() {
        if(isLife()){
            return image;
        }
        return null;
    }
}
