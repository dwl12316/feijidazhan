package myplane;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Boss extends FlyObject {
    private int speedX;
    private int speedY;
    private int type;
    private Image image;

    public Boss(int width,int height,int level) {
        super(width, height, 120, 40, 200+(level-1)*400);
        this.speedY = 1;
        this.speedX = 1;
        image = new ImageIcon("plane_img/Boss"+level+".png").getImage();
        this.type = level;
    }

    @Override
    public void step() {
        this.x += speedX;
        if (this.x >= 400 - width) {
            speedX = -1;
        }
        if (this.x <= 0) {
            speedX = 1;
        }
    }

    @Override
    public Image getImage() {
        if (isLife())
            return image;
        if (isBomb())
            state = DEAD;
        return null;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
