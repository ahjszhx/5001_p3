package shape;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.awt.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public abstract class Shape implements Serializable {

    private String uuid;

    private String innerId;

    protected Point startPoint;

    protected Point endPoint;

    protected Color borderColorModel = Color.BLACK;

    protected Color fillColorModel = Color.BLUE;

    protected float borderWidth = 1;


    public Shape() {
    }

    public Shape(Point startPoint, Point endPoint, Color borderColor, Color fillColor, float borderWidth) {
        this.innerId = UUID.randomUUID().toString();
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.borderColorModel = borderColor;
        if (fillColor != null) {
            this.fillColorModel = fillColor;
        }
        if (borderColor != null) {
            this.borderWidth = borderWidth;
        }
    }

    public abstract void rotate(double rotationAngle);

    // Move the shape to a new position
    public abstract void move(int deltaX, int deltaY);


    // Check if a point is contained within the shape
    public abstract boolean contains(Point point);

    public void drawShape(Graphics2D g) {
        g.setColor(borderColorModel);
        g.setStroke(new BasicStroke(borderWidth));
        g.setPaint(fillColorModel);
    }

    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        return builder.build();
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public String getInnerId() {
        return innerId;
    }

    public void setInnerId(String innerId) {
        this.innerId = innerId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Color getBorderColorModel() {
        return borderColorModel;
    }

    public void setBorderColorModel(Color borderColorModel) {
        this.borderColorModel = borderColorModel;
    }

    public Color getFillColorModel() {
        return fillColorModel;
    }

    public void setFillColorModel(Color fillColorModel) {
        this.fillColorModel = fillColorModel;
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }

    public abstract Shape clone();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shape)) return false;
        Shape shape = (Shape) o;
        return Float.compare(shape.getBorderWidth(), getBorderWidth()) == 0 && Objects.equals(getInnerId(), shape.getInnerId()) && getStartPoint().equals(shape.getStartPoint()) && getEndPoint().equals(shape.getEndPoint()) && getBorderColorModel().equals(shape.getBorderColorModel()) && getFillColorModel().equals(shape.getFillColorModel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInnerId(), getStartPoint(), getEndPoint(), getBorderColorModel(), getFillColorModel(), getBorderWidth());
    }
}