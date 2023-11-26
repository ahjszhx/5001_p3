package shape;

import global.GlobalReference;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.awt.*;
import java.awt.geom.AffineTransform;

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
        this.rotation += rotationAngle;
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
            // If lockRatio is true, draw a rotated square
            int width = maxX - minX;
            int height = maxY - minY;
            int sideLength = Math.min(width, height);

            AffineTransform oldTransform = g.getTransform();
            g.rotate(Math.toRadians(rotation), minX + sideLength / 2, minY + sideLength / 2);
            g.fillRect(minX, minY, sideLength, sideLength);
            g.setTransform(oldTransform);
        } else {
            // If lockRatio is false, draw a rotated rectangle
            AffineTransform oldTransform = g.getTransform();
            g.rotate(Math.toRadians(rotation), (minX + maxX) / 2.0, (minY + maxY) / 2.0);
            g.fillRect(minX, minY, maxX - minX, maxY - minY);
            g.setTransform(oldTransform);
        }

        g.setColor(borderColorModel);
        g.setStroke(new BasicStroke(borderWidth));

        if (lockRatio) {
            int width = maxX - minX;
            int height = maxY - minY;
            int sideLength = Math.min(width, height);

            AffineTransform oldTransform = g.getTransform();
            g.rotate(Math.toRadians(rotation), minX + sideLength / 2, minY + sideLength / 2);
            g.drawRect(minX, minY, sideLength, sideLength);
            g.setTransform(oldTransform);
        } else {
            AffineTransform oldTransform = g.getTransform();
            g.rotate(Math.toRadians(rotation), (minX + maxX) / 2.0, (minY + maxY) / 2.0);
            g.drawRect(minX, minY, maxX - minX, maxY - minY);
            g.setTransform(oldTransform);
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
        JsonObjectBuilder builder = Json.createObjectBuilder();
        if (width != 0) {
            builder.add("width", width);
        }
        if (height != 0) {
            builder.add("height", height);
        }
        if (rotation != 0) {
            builder.add("rotation", rotation);
        }
        if (borderColor != null) {
            builder.add("borderColor", borderColor);
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
    public Rectangle clone() {
        Rectangle rectangle = new Rectangle();
        rectangle.setUuid(this.getUuid());
        rectangle.setInnerId(this.getInnerId());
        rectangle.setBorderColorModel(new Color(this.borderColorModel.getRGB()));
        rectangle.setFillColorModel(new Color(this.fillColorModel.getRGB()));
        rectangle.setBorderWidth(this.borderWidth);


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

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getRotation() {
        return rotation;
    }
}
