package shape;

import global.GlobalReference;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

/**
 * The Ellipse class represents an ellipse shape in the drawing application.
 * It extends the base Shape class and provides methods specific to drawing and manipulating ellipses.
 */
public class Ellipse extends Shape {

    private int width;

    private int height;

    private String borderColor;

    private String fillColor;

    private boolean lockRatio;

    private Ellipse2D ellipse = new Ellipse2D.Double();;

    public Ellipse() {
    }

    /**
     * Parameterized constructor for creating an Ellipse object with specified attributes.
     *
     * @param startPoint   The starting point of the ellipse.
     * @param endPoint     The ending point of the ellipse.
     * @param borderColor  The color of the ellipse border.
     * @param fillColor    The fill color of the ellipse.
     * @param borderWidth  The width of the ellipse's border.
     * @param lockRatio    Flag indicating whether to maintain the aspect ratio when resizing.
     */
    public Ellipse(Point startPoint, Point endPoint, Color borderColor, Color fillColor, float borderWidth, boolean lockRatio) {
        super(startPoint, endPoint, borderColor, fillColor, borderWidth);
        this.lockRatio = lockRatio;
        updateEllipse();
    }

    /**
     * Moves the ellipse by a specified delta in the x and y directions.
     *
     * @param deltaX The change in x direction.
     * @param deltaY The change in y direction.
     */
    @Override
    public void move(int deltaX, int deltaY) {
        this.startPoint.setLocation(this.startPoint.getX() + deltaX, this.startPoint.getY() + deltaY);
        this.endPoint.setLocation(this.endPoint.getX() + deltaX, this.endPoint.getY() + deltaY);
        updateEllipse();
    }

    /**
     * Rotates the ellipse by a specified angle.
     *
     * @param rotationAngle The angle by which to rotate the ellipse.
     */
    @Override
    public void rotate(double rotationAngle) {
        this.rotation += rotationAngle; // Update rotation
        System.out.println("Ellipse rotation=>"+this.rotation);
        updateEllipse();
    }

    /**
     * Updates the Ellipse2D object based on current attributes.
     */
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


    /**
     * Draws the ellipse shape on the graphics context.
     *
     * @param g The graphics context to draw on.
     */
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
        g.rotate(Math.toRadians(rotation), ellipse.getCenterX(), ellipse.getCenterY()); // Apply rotation
        g.fill(ellipse);

        g.setColor(borderColorModel);
        g.setStroke(new BasicStroke(borderWidth));

        g.draw(ellipse);

        g.setTransform(oldTransform); // Restore the original transform
    }

    /**
     * Checks if a point is inside the ellipse.
     *
     * @param point The point to check.
     * @return True if the point is inside the ellipse, false otherwise.
     */
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


    @Override
    protected Shape createShapeInstance() {
        return new Ellipse();
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