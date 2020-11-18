package myplane;

import javax.swing.*;
import java.awt.*;

public class PlaneFrame {
    public static void main(String[] args) {
        //创建窗口
        JFrame frame = new JFrame("雷霆战机");
        //设置窗口得大小
        frame.setSize(400, 600);
        frame.setResizable(false);
        //设置窗口得位置 居中
        frame.setLocationRelativeTo(null);
        //设置关闭操作 同时 结束程序
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //创建画布jpanel
        PlanePanel pan = new PlanePanel();
        pan.setBackground(Color.YELLOW);
        //将画布添加到窗口内
        frame.add(pan);
        //安装监听器
        frame.addMouseListener(pan);
        frame.addMouseMotionListener(pan);
        frame.addKeyListener(pan);
        //设置窗口可见
        frame.setVisible(true);
        ////多线程实现移动
        pan.move();
    }
}

