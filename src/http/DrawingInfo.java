package http;

import global.GlobalReference;
import shape.Ellipse;
import shape.Line;
import shape.Rectangle;
import shape.Shape;
import shape.Triangle;
import java.awt.*;
import java.util.Map;

public class DrawingInfo {

    private String id;


    private String created;


    private String modified;


    private boolean isOwner;


    private String type;


    private int x;


    private int y;


    private Map<String, Object> properties;


    public DrawingInfo(String id, String created, String modified, boolean isOwner, String type, int x, int y, Map<String, Object> properties) {
        this.id = id;
        this.created = created;
        this.modified = modified;
        this.isOwner = isOwner;
        this.type = type;
        this.x = x;
        this.y = y;
        this.properties = properties;
    }

    public Shape getShapeInstance() {
        Shape shape = null;
        //if (this.isOwner()) {
        if (type.equals("rectangle") || type.equals("square")) {
            Rectangle rectangle = new Rectangle();
            rectangle.setWidth((Integer) this.properties.get("width"));
            rectangle.setHeight((Integer) properties.get("height"));
            rectangle.setRotation((Integer) properties.get("rotation"));
            rectangle.setBorderColor((String) properties.get("borderColor"));
            if (properties.get("borderWidth") != null) {
                rectangle.setBorderWidth((Integer) properties.get("borderWidth"));
            }
            rectangle.setFillColor((String) properties.get("fillColor"));
            int endX = x + rectangle.getWidth();
            int endY = y + rectangle.getHeight();
            rectangle.setStartPoint(new Point(x, y));
            rectangle.setEndPoint(new Point(endX, endY));
            rectangle.setBorderColorModel(GlobalReference.STR_COLOR.get(String.valueOf(properties.get("borderColor"))));
            rectangle.setFillColorModel(GlobalReference.STR_COLOR.get(String.valueOf(properties.get("fillColor"))));
            shape = rectangle;
        } else if (type.equals("ellipse") || type.equals("circle")) {
            Ellipse ellipse = new Ellipse();
            ellipse.setWidth((Integer) properties.get("width"));
            ellipse.setHeight((Integer) properties.get("height"));
            ellipse.setRotation((Integer) properties.get("rotation"));
            ellipse.setBorderColor((String) properties.get("borderColor"));
            if (properties.get("borderWidth") != null) {
                ellipse.setBorderWidth((Integer) properties.get("borderWidth"));
            }
            ellipse.setFillColor((String) properties.get("fillColor"));
            int endX = x + ellipse.getWidth();
            int endY = y + ellipse.getHeight();
            ellipse.setStartPoint(new Point(x, y));
            ellipse.setEndPoint(new Point(endX, endY));
            ellipse.setBorderColorModel(GlobalReference.STR_COLOR.get(String.valueOf(properties.get("borderColor"))));
            ellipse.setFillColorModel(GlobalReference.STR_COLOR.get(String.valueOf(properties.get("fillColor"))));
            shape = ellipse;
        } else if (type.equals("line")) {
            Line line = new Line();
            line.setX2((Integer) properties.get("x2"));
            line.setY2((Integer) properties.get("y2"));
            line.setLineColor((String) properties.get("lineColor"));
            line.setLineWidth((Integer) properties.get("lineWidth"));
            line.setStartPoint(new Point(x, y));
            line.setEndPoint(new Point(line.getX2(), line.getY2()));
            shape = line;
        } else if (type.equals("triangle")) {
            Triangle triangle = new Triangle();
            triangle.setX2((Integer) properties.get("x2"));
            triangle.setY2((Integer) properties.get("y2"));
            triangle.setX3((Integer) properties.get("x3"));
            triangle.setY3((Integer) properties.get("y3"));
            triangle.setRotation((Integer) properties.get("rotation"));
            triangle.setBorderColor((String) properties.get("borderColor"));
            triangle.setBorderWidth((Integer) properties.get("borderWidth"));
            triangle.setFillColor((String) properties.get("fillColor"));
            int endX2 = triangle.getX2();
            int endY2 = triangle.getY2();
            int endX3 = triangle.getX3();
            int endY3 = triangle.getY3();
            int endX = 0;
            int endY = 0;
            // Calculate the centroid of the triangle
            endX = (endX + endX2 + endX3) / 3;
            endY = (endY + endY2 + endY3) / 3;
            triangle.setStartPoint(new Point(x, y));
            triangle.setEndPoint(new Point(endX, endY));
            triangle.setBorderColorModel(GlobalReference.STR_COLOR.get(String.valueOf(properties.get("borderColor"))));
            triangle.setFillColorModel(GlobalReference.STR_COLOR.get(String.valueOf(properties.get("fillColor"))));
            shape = triangle;
        }
        //}
        return shape;
    }


    // toString() method for debugging or printing
    @Override
    public String toString() {
        return "DrawingInfo{" +
                "id=" + id +
                ", created='" + created + '\'' +
                ", modified='" + modified + '\'' +
                ", isOwner=" + isOwner +
                ", type='" + type + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

}
