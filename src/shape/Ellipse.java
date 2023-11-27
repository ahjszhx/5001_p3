package shape;

import global.GlobalReference;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class Ellipse extends Shape {

    private int width;

    private int height;

    //private int rotation = 0;

    private String borderColor;

    private String fillColor;

    private boolean lockRatio;

    private Ellipse2D ellipse = new Ellipse2D.Double();;

    public Ellipse() {
    }


    public Ellipse(Point startPoint, Point endPoint, Color borderColor, Color fillColor, float borderWidth, boolean lockRatio) {
        super(startPoint, endPoint, borderColor, fillColor, borderWidth);
        this.lockRatio = lockRatio;
        updateEllipse();
    }

    @Override
    public void move(int deltaX, int deltaY) {
        this.startPoint.setLocation(this.startPoint.getX() + deltaX, this.startPoint.getY() + deltaY);
        this.endPoint.setLocation(this.endPoint.getX() + deltaX, this.endPoint.getY() + deltaY);
        updateEllipse();
    }

    @Override
    public void rotate(double rotationAngle) {
        this.rotation += rotationAngle; // Update rotation
        updateEllipse();
    }

    @Override
    public void drawShape(Graphics2D g) {
        g.setPaint(fillColorModel);

        if (this.lockRatio) {
            int diameter = Math.min(width, height);
            ellipse.setFrame(startPoint.getX(), startPoint.getY(), diameter, diameter);
        } else {
            ellipse.setFrameFromDiagonal(startPoint, endPoint);
        }

        AffineTransform oldTransform = g.getTransform(); // Save the current transform
        g.rotate(rotation, ellipse.getCenterX(), ellipse.getCenterY()); // Apply rotation
        g.fill(ellipse);

        g.setColor(borderColorModel);
        g.setStroke(new BasicStroke(borderWidth));

        g.draw(ellipse);

        g.setTransform(oldTransform); // Restore the original transform
    }

    @Override
    public boolean contains(Point point) {
        return ellipse.contains(point);
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

    private void updateEllipse() {
        double minX = Math.min(startPoint.getX(), endPoint.getX());
        double minY = Math.min(startPoint.getY(), endPoint.getY());
        double maxX = Math.max(startPoint.getX(), endPoint.getX());
        double maxY = Math.max(startPoint.getY(), endPoint.getY());

        if (this.lockRatio) {
            double diameter = Math.min(maxX - minX, maxY - minY);
            ellipse = new Ellipse2D.Double(minX, minY, diameter, diameter);
        } else {
            ellipse = new Ellipse2D.Double(minX, minY, maxX - minX, maxY - minY);
        }

        setWebProperties((int) minX, (int) maxX, (int) minY, (int) maxY);
    }

    @Override
    public Ellipse clone() {
        Ellipse ellipse = new Ellipse();
        ellipse.setUuid(this.getUuid());
        ellipse.setInnerId(this.getInnerId());
        ellipse.setBorderColorModel(new Color(this.borderColorModel.getRGB()));
        ellipse.setFillColorModel(new Color(this.fillColorModel.getRGB()));
        ellipse.setBorderWidth(this.borderWidth);
        ellipse.setRotation(this.rotation);
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