package shape;

import global.GlobalReference;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.awt.*;
import java.util.Locale;

public class Rectangle extends Shape {

    private int width;

    private int height;

    private int rotation = 0;

    private String borderColor;

    private String fillColor;

    private boolean lockRatio;

    public Rectangle() {
    }


    public Rectangle(Point startPoint, Point endPoint, Color borderColor, Color fillColor, float borderWidth, boolean lockRatio) {
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

    }

    @Override
    public void drawShape(Graphics2D g) {
        int minX = Math.min(startPoint.x, endPoint.x);
        int maxX = Math.max(startPoint.x, endPoint.x);
        int minY = Math.min(startPoint.y, endPoint.y);
        int maxY = Math.max(startPoint.y, endPoint.y);

        setWebProperties(minX, maxX, minY, maxY);

        g.setPaint(fillColorModel);

        if (lockRatio) {
            // 如果 lockRatio 为 true，画正方形
            int width = maxX - minX;
            int height = maxY - minY;
            int sideLength = Math.min(width, height);
            g.fillRect(minX, minY, sideLength, sideLength);
        } else {
            // 如果 lockRatio 为 false，画长方形
            g.fillRect(minX, minY, maxX - minX, maxY - minY);
        }

        g.setColor(borderColorModel);
        g.setStroke(new BasicStroke(borderWidth));

        if (lockRatio) {
            // 如果 lockRatio 为 true，画正方形的边框
            int width = maxX - minX;
            int height = maxY - minY;
            int sideLength = Math.min(width, height);
            g.drawRect(minX, minY, sideLength, sideLength);
        } else {
            // 如果 lockRatio 为 false，画长方形的边框
            g.drawRect(minX, minY, maxX - minX, maxY - minY);
        }
    }

    @Override
    public boolean contains(Point point) {
        return point.x >= startPoint.x && point.x <= endPoint.x &&
                point.y >= startPoint.y && point.y <= endPoint.y;
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
    public Rectangle clone() {
        Rectangle rectangle = new Rectangle();
        rectangle.setUuid(this.getUuid());
        rectangle.setInnerId(this.getInnerId());
        rectangle.setBorderColorModel(new Color(this.borderColorModel.getRGB()));
        rectangle.setFillColorModel(new Color(this.fillColorModel.getRGB()));
        rectangle.setBorderWidth(this.borderWidth);

        // 复制引用类型属性
        if (this.startPoint != null) {
            rectangle.setStartPoint(new Point(this.startPoint));
        }

        if (this.endPoint != null) {
            rectangle.setEndPoint(new Point(this.endPoint));
        }

        return rectangle;
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
