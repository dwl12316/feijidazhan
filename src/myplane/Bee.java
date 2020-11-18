package myplane;

import javax.swing.*;
import java.awt.*;

//小蜜蜂
public class Bee extends FlyObject {
    private Integer speedX;//x方向速度
    private Integer speedY;//y方向速度
    private Integer awardType;//奖励类型  0表示加1命   1表示加40火力值
    private Image[] images;
    private int index = 1;


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
        if (isLife()) {
            return images[0];
        }
        if (isBomb()) {
            if (index == 4) {//放完最后一张图片
                state = DEAD;//死了得
            } else {
                Image image = images[index++];
                return image;
            }

        }
        return null;
    }

    public Bee() {
        //x坐标在[0，400]内随机正整数  400窗口宽度
        //y坐标是负的高度
        super(60, 50, (int) (Math.random() * 400), -50, 0);
        this.speedX = 1;
        this.speedY = 2;
        this.awardType = (int) (Math.random() * 2);
        images = new Image[5];
        for (int i = 0; i < images.length; i++) {
            images[i] = new ImageIcon("images/bee" + i + ".png").getImage();
        }
    }

    public void setSpeedX(Integer speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(Integer speedY) {
        this.speedY = speedY;
    }

    public void setAwardType(Integer awardType) {
        this.awardType = awardType;
    }

    public Integer getSpeedX() {
        return speedX;
    }

    public Integer getSpeedY() {
        return speedY;
    }

    public Integer getAwardType() {
        return awardType;
    }
}
