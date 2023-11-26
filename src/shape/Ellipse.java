package shape;

import global.GlobalReference;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;


public class Ellipse extends Shape {

    private int width;

    private int height;

    private int rotation = 0;

    private String borderColor;

    private String fillColor;

    private boolean lockRatio;

    public Ellipse() {
    }


    public Ellipse(Point startPoint, Point endPoint, Color borderColor, Color fillColor, float borderWidth, boolean lockRatio) {
        super(startPoint, endPoint, borderColor, fillColor, borderWidth);
        this.lockRatio = lockRatio;
    }

    @Override
    public void move(int deltaX, int deltaY) {
        this.startPoint.setLocation(this.startPoint.getX() + deltaX, this.startPoint.getY() + deltaY);
        this.endPoint.setLocation(this.endPoint.getX() + deltaX, this.endPoint.getY() + deltaY);
    }

    @Override
    public void rotate(double rotationAngle) {
        // 计算椭圆的中心坐标
        double centerX = startPoint.getX() + (endPoint.getX() - startPoint.getX()) / 2.0;
        double centerY = startPoint.getY() + (endPoint.getY() - startPoint.getY()) / 2.0;

        // 将角度转换为弧度
        double radians = Math.toRadians(rotationAngle);

        // 创建旋转变换
        AffineTransform transform = AffineTransform.getRotateInstance(radians, centerX, centerY);

        // 更新椭圆的位置
        Point2D newStartPoint = transform.transform(new Point2D.Double(startPoint.getX(), startPoint.getY()), null);
        Point2D newEndPoint = transform.transform(new Point2D.Double(endPoint.getX(), endPoint.getY()), null);

        startPoint.setLocation(newStartPoint);
        endPoint.setLocation(newEndPoint);
    }


    @Override
    public boolean contains(Point point) {
        double centerX = (startPoint.getX() + endPoint.getX()) / 2.0;
        double centerY = (startPoint.getY() + endPoint.getY()) / 2.0;

        double a = Math.abs(endPoint.getX() - startPoint.getX()) / 2.0;
        double b = Math.abs(endPoint.getY() - startPoint.getY()) / 2.0;

        Ellipse2D.Double ellipse = new Ellipse2D.Double(centerX - a, centerY - b, 2 * a, 2 * b);

        return ellipse.contains(point.getX(), point.getY());
    }

    @Override
    public void drawShape(Graphics2D g) {
        int minX = Math.min(startPoint.x, endPoint.x);
        int maxX = Math.max(startPoint.x, endPoint.x);
        int minY = Math.min(startPoint.y, endPoint.y);
        int maxY = Math.max(startPoint.y, endPoint.y);
        setWebProperties(minX, maxX, minY, maxY);

        int width = maxX - minX;
        int height = maxY - minY;

        g.setPaint(fillColorModel);

        if (this.lockRatio) {
            int diameter = Math.min(width, height);
            g.fillOval(minX, minY, diameter, diameter);
        } else {
            g.fillOval(minX, minY, width, height);
        }

        g.setColor(borderColorModel);
        g.setStroke(new BasicStroke(borderWidth));

        if (this.lockRatio) {
            // 如果 ratio 为 true，画椭圆的边框
            int diameter = Math.min(width, height);
            g.drawOval(minX, minY, diameter, diameter);
        } else {
            g.drawOval(minX, minY, width, height);
            // 如果 ratio 为 false，画圆的边框
        }
    }


    private void setWebProperties(int minX, int maxX, int minY, int maxY) {
        this.width = maxX - minX;
        this.height = maxY - minY;
        this.borderColor = GlobalReference.COLOR_STR.get(super.borderColorModel);
        this.fillColor = GlobalReference.COLOR_STR.get(super.fillColorModel);
    }

    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder()
                .add("width", width)
                .add("height", height)
                .add("rotation", rotation)
                .add("borderColor", borderColor)
                .add("borderWidth",borderWidth)
                .add("fillColor", fillColor);
        return builder.build();
    }

    @Override
    public Ellipse clone() {
        Ellipse ellipse = new Ellipse();
        ellipse.setUuid(this.getUuid());
        ellipse.setInnerId(this.getInnerId());
        ellipse.setBorderColorModel(new Color(this.borderColorModel.getRGB()));
        ellipse.setFillColorModel(new Color(this.fillColorModel.getRGB()));
        ellipse.setBorderWidth(this.borderWidth);

        // 复制引用类型属性
        if (this.startPoint != null) {
            ellipse.setStartPoint(new Point(this.startPoint));
        }

        if (this.endPoint != null) {
            ellipse.setEndPoint(new Point(this.endPoint));
        }

        return ellipse;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }


}
