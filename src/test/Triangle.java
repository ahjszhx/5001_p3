package test;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

public class Triangle extends JFrame {

    private Point startPoint;
    private Point endPoint;
    private Color borderColor;
    private Color fillColor;

    public Triangle(Point startPoint, Point endPoint, Color borderColor, Color fillColor) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.borderColor = borderColor;
        this.fillColor = fillColor;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        //super.paint(g);

        Graphics2D g2d = (Graphics2D) g;

        // 设置边框颜色
        g2d.setColor(borderColor);

        // 计算三角形的第三个顶点，确保线段相等
        int thirdX = startPoint.x + (endPoint.x - startPoint.x) / 2;
        Point thirdPoint = new Point(thirdX, endPoint.y);

        // 绘制三角形的三条边
        g2d.draw(new Line2D.Double(startPoint, endPoint));
        g2d.draw(new Line2D.Double(endPoint, thirdPoint));
        g2d.draw(new Line2D.Double(thirdPoint, startPoint));

        // 创建一个Polygon对象，包含三个顶点
        int[] xPoints = {startPoint.x, endPoint.x, thirdPoint.x};
        int[] yPoints = {startPoint.y, endPoint.y, thirdPoint.y};
        int nPoints = 3;

        Polygon triangle = new Polygon(xPoints, yPoints, nPoints);

        // 设置填充颜色
        g2d.setColor(fillColor);

        // 填充三角形
        g2d.fill(triangle);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Point startPoint = new Point(50, 50);
            Point endPoint = new Point(150, 150);
            Color borderColor = Color.RED;
            Color fillColor = Color.YELLOW;

            new Triangle(startPoint, endPoint, borderColor, fillColor);
        });
    }
}