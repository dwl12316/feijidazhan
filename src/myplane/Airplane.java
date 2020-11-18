package myplane;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

//小敌机
public class Airplane extends FlyObject {
    private Integer speed;
    private Image[] images;


    @Override
    public void step() {
        //向下移动
        this.y += speed;
    }

    private int index = 1;//表示第2张图片

    @Override
    public Image getImage() {
        //活着得时候显示images[0] 爆炸得时候显示images[1]切换到[4] 放完最后一张图片则消失不见
        if (isLife()) {
            return images[0];
        }
        if (isBomb()) {
            if (index == 5) {//放完最后一张图片
                state = DEAD;//死了
            } else {
                Image image = images[index++];
                return image;
            }

        }
        return null;
    }

    public Airplane(int add, int level) {
        //x坐标在[0，400]内随机正整数  400窗口宽度
        //y坐标是负的高度
        super(49, 36, (int) (Math.random() * 340), -36, 1 + add * 5);
        this.speed = 3;
        this.images = new Image[5];
        for (int i = 0; i < images.length; i++) {
            if (i == 0 && level == 2) {//根据关卡调飞机
                int x =(int)(10*Math.random())%2;
                images[i] = new ImageIcon("plane_img/b1plane" + x + ".png").getImage();
                this.width = 50;
                this.height = 50;
            } else if (i == 0 && level == 3) {
                int x =(int)(10*Math.random())%2;
                images[i] = new ImageIcon("plane_img/b2plane" + x + ".png").getImage();
                this.width = 50;
                this.height = 50;
            } else
                images[i] = new ImageIcon("images/airplane" + i + ".png").getImage();
        }
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getSpeed() {
        return speed;
    }
}
