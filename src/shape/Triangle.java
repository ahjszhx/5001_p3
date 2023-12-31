package shape;

import global.GlobalReference;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * The Triangle class represents a triangle shape in the drawing application.
 * It extends the base Shape class and provides methods specific to drawing and manipulating triangles.
 */
public class Triangle extends Shape {

    private int x2;

    private int y2;

    private int x3;

    private int y3;

    private String borderColor;

    private String fillColor;

    public Triangle() {
        super();
    }

    /**
     * Parameterized constructor for creating a Triangle object with specified attributes.
     *
     * @param startPoint  The starting point of the triangle.
     * @param endPoint    The ending point of the triangle.
     * @param borderColor The color of the triangle border.
     * @param fillColor   The fill color of the triangle.
     * @param borderWidth The width of the triangle's border.
     */
    public Triangle(Point startPoint, Point endPoint, Color borderColor, Color fillColor, float borderWidth) {
        super(startPoint, endPoint, borderColor, fillColor, borderWidth);
    }

    /**
     * Moves the triangle by a specified delta in the x and y directions.
     *
     * @param deltaX The change in x direction.
     * @param deltaY The change in y direction.
     */
    @Override
    public void move(int deltaX, int deltaY) {
        this.startPoint.setLocation(this.startPoint.getX() + deltaX, this.startPoint.getY() + deltaY);
        this.endPoint.setLocation(this.endPoint.getX() + deltaX, this.endPoint.getY() + deltaY);
    }

    @Override
    public void rotate(double rotationAngle) {
        this.rotation += rotationAngle;
    }

    /**
     * Draws the triangle shape on the graphics context.
     *
     * @param g The graphics context to draw on.
     */
    @Override
    public void drawShape(Graphics2D g) {
        Graphics2D g2d = (Graphics2D) g.create();

        int height = (int) (Math.sqrt(3) / 2 * (endPoint.x - startPoint.x));

        int x1 = (int) startPoint.getX();
        int y1 = (int) startPoint.getY();
        int x2 = (int) endPoint.getX();
        int y2 = (int) startPoint.getY();

        int x3 = (int) (startPoint.getX() + endPoint.getX()) / 2;
        int y3 = (int) (startPoint.getY() - height);

        setWebProperties(x3, y3);
        // 计算旋转后的坐标
        double radians = Math.toRadians(rotation);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);

        int centerX = (x1 + x2 + x3) / 3;
        int centerY = (y1 + y2 + y3) / 3;

        int newX1 = (int) (cosTheta * (x1 - centerX) - sinTheta * (y1 - centerY) + centerX);
        int newY1 = (int) (sinTheta * (x1 - centerX) + cosTheta * (y1 - centerY) + centerY);

        int newX2 = (int) (cosTheta * (x2 - centerX) - sinTheta * (y2 - centerY) + centerX);
        int newY2 = (int) (sinTheta * (x2 - centerX) + cosTheta * (y2 - centerY) + centerY);

        int newX3 = (int) (cosTheta * (x3 - centerX) - sinTheta * (y3 - centerY) + centerX);
        int newY3 = (int) (sinTheta * (x3 - centerX) + cosTheta * (y3 - centerY) + centerY);

        // 创建Polygon对象，表示一个多边形
        Polygon triangle = new Polygon();
        triangle.addPoint(newX1, newY1);
        triangle.addPoint(newX2, newY2);
        triangle.addPoint(newX3, newY3);

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

    /**
     * Checks if a point is inside the triangle.
     *
     * @param point The point to check.
     * @return True if the point is inside the triangle, false otherwise.
     */
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

        // 将鼠标按下的坐标转换为整数
        int mouseX = (int) point.getX();
        int mouseY = (int) point.getY();

        System.out.println("contains=>"+triangle.contains(mouseX, mouseY));
        // 检查点是否在等边三角形中
        return triangle.contains(mouseX, mouseY);
    }

    /**
     * Sets web-specific properties for serialization.
     */
    private void setWebProperties(int x3, int y3) {
        this.x2 = endPoint.x;
        this.y2 = endPoint.y;
        this.x3 = x3;
        this.y3 = y3;
        this.borderColor = GlobalReference.COLOR_STR.get(super.borderColorModel);
        this.fillColor = GlobalReference.COLOR_STR.get(super.fillColorModel);
    }


    /**
     * Converts the Triangle object to a JSON representation.
     *
     * @return The JSON representation of the Triangle.
     */
    @Override
    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        if (x2 != 0) {
            builder.add("x2", x2);
        }
        if (y2 != 0) {
            builder.add("y2", y2);
        }
        if (x3 != 0) {
            builder.add("x3", x3);
        }
        if (y3 != 0) {
            builder.add("y3", y3);
        }
        if (rotation != 0) {
            builder.add("rotation", rotation);
        }
        if (borderWidth != 0) {
            builder.add("borderWidth", borderWidth);
        }
        if (fillColor != null) {
            builder.add("fillColor", fillColor);
        }
        return builder.build();
    }

    @Override
    protected Shape createShapeInstance() {
        return new Triangle();
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

}
