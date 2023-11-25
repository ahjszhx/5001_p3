package shape;

import global.GlobalReference;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.awt.*;

public class Triangle extends Shape{

    private int x2;

    private int y2;

    private int x3;

    private int y3;

    private int rotation = 0;

    private String borderColor;

    private String fillColor;

    public Triangle() {
        super();
    }

    public Triangle(Point startPoint, Point endPoint, Color borderColor, Color fillColor, float borderWidth) {
        super(startPoint, endPoint, borderColor, fillColor, borderWidth);
    }

    @Override
    public void move(int deltaX, int deltaY) {
        // 移动三角形的三个顶点
        this.startPoint.setLocation(this.startPoint.getX() + deltaX, this.startPoint.getY() + deltaY);
        this.endPoint.setLocation(this.endPoint.getX() + deltaX, this.endPoint.getY() + deltaY);
    }

    @Override
    public void rotate(double rotationAngle) {

    }

    @Override
    public void drawShape(Graphics2D g) {
        Graphics2D g2d = (Graphics2D) g.create();

        // 计算等边三角形的高度
        //int height = (int) (Math.sqrt(3) * (endPoint.x - startPoint.x) / 2);
        int height = (int) (Math.sqrt(3) / 2 * (endPoint.x - startPoint.x));

        // 计算三个顶点的坐标
        int x1 = (int) startPoint.getX();
        int y1 = (int) endPoint.getY();
        int x2 = (int) endPoint.getX();
        int y2 = (int) endPoint.getY();
        int x3 = (int) (startPoint.getX() + endPoint.getX()) / 2;
        int y3 = (int) (startPoint.getY() - height);

        // 创建Polygon对象，表示一个多边形
        Polygon triangle = new Polygon();
        triangle.addPoint(x1, y1);
        triangle.addPoint(x2, y2);
        triangle.addPoint(x3, y3);

        // 设置填充颜色
        g2d.setColor(fillColorModel);
        g2d.fill(triangle);

        // 设置边框颜色
        Color borderColor = getBorderColorModel();
        if (borderColor != null) {
            g2d.setColor(borderColor);
        } else {
            // 设置一个默认颜色，或者根据需求处理
            g2d.setColor(Color.BLACK);
        }

        // 设置边框宽度
        g2d.setStroke(new BasicStroke(Math.max(getBorderWidth(), 1.0f))); // 至少为1像素宽度

        // 设置线段连接样式
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

        // 绘制边框
        g2d.draw(triangle);

        // 释放资源
        g2d.dispose();
    }


    @Override
    public boolean contains(Point point) {
        Polygon triangle = new Polygon();
        triangle.addPoint((int) startPoint.getX(), (int) startPoint.getY());
        triangle.addPoint((int) endPoint.getX(), (int) endPoint.getY());

        // 计算等边三角形的高度
        int height = (int) (Math.sqrt(3) * (endPoint.getX() - startPoint.getX()) / 2);
        int x3 = (int) (startPoint.getX() + endPoint.getX()) / 2;
        int y3 = (int) (startPoint.getY() - height);

        triangle.addPoint(x3, y3);

        return triangle.contains(point.getX(), point.getY());
    }

    private void setWebProperties(int x3,int y3) {
        this.x2 = endPoint.x;
        this.y2 = endPoint.y;
        this.x3 = x3;
        this.y3 = y3;
        this.borderColor = GlobalReference.COLOR_STR.get(super.borderColorModel);
        this.fillColor = GlobalReference.COLOR_STR.get(super.fillColorModel);
    }


    @Override
    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder()
                .add("x2", x2)
                .add("y2", y2)
                .add("x3", x3)
                .add("y3", y3)
                .add("rotation",rotation)
                .add("borderWidth",borderWidth)
                .add("fillColor", fillColor);
        return builder.build();
    }

    @Override
    public Triangle clone() {
        Triangle triangle = new Triangle();
        triangle.setUuid(this.getUuid());
        triangle.setInnerId(this.getInnerId());
        triangle.setBorderColorModel(new Color(this.borderColorModel.getRGB()));
        triangle.setFillColorModel(new Color(this.fillColorModel.getRGB()));
        triangle.setBorderWidth(this.borderWidth);

        // 复制引用类型属性
        if (this.startPoint != null) {
            triangle.setStartPoint(new Point(this.startPoint));
        }

        if (this.endPoint != null) {
            triangle.setEndPoint(new Point(this.endPoint));
        }

        return triangle;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public int getX3() {
        return x3;
    }

    public void setX3(int x3) {
        this.x3 = x3;
    }

    public int getY3() {
        return y3;
    }

    public void setY3(int y3) {
        this.y3 = y3;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public String getFillColor() {
        return fillColor;
    }

    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    @Override
    public float getBorderWidth() {
        return borderWidth;
    }

    @Override
    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }
}
