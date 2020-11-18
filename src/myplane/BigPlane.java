package myplane;

import javax.swing.*;
import javax.tools.Diagnostic;
import java.awt.*;

//大敌机
public class BigPlane extends FlyObject {
    private Integer speed;
    private Image[] images;
    private int life;
    private int index = 1;

    @Override
    public void step() {
        this.y += speed;//向下移动
    }

    @Override
    public Image getImage() {
        if (isLife()) {
            return images[0];
        }
        if (isBomb()) {
            if (index <= 4)
                return images[index++];
            else {
                state = DEAD;
            }
        }
        return null;
    }

    public BigPlane(int add, int level) {
        //x坐标在[0，400]内随机正整数  400窗口宽度
        //y坐标是负的高度
        super(49, 36, (int) (Math.random() * 340), -36, 3 + add * 12);
        this.speed = 2;
        images = new Image[5];
        for (int i = 0; i < images.length; i++) {
            if (i == 0 && level == 2) {
                images[i] = new ImageIcon("plane_img/bigplane0.png").getImage();
                width = 127;
                height = 48;
            } else if (i == 0 && level == 3) {
                images[i] = new ImageIcon("plane_img/bigplane4.png").getImage();
                width = 46;
                height = 90;
            } else
                images[i] = new ImageIcon("images/bigplane" + i + ".png").getImage();
        }
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getSpeed() {
        return speed;
    }


}
