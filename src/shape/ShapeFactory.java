package shape;

import java.awt.*;

public class ShapeFactory {


    public static Shape createShape(String shapeName, Point startPoint, Point endPoint, Color borderColor, Color fillColor, float borderWidth) {
        Shape shape = null;
        if (shapeName == null || shapeName.equals("Line")) {
            shape = new Line(startPoint, endPoint, borderColor, fillColor, borderWidth);
        } else if (shapeName.equals("Rectangle")) {
            shape = new Rectangle(startPoint, endPoint, borderColor, fillColor, borderWidth, false);
        } else if (shapeName.equals("Square")) {
            shape = new Rectangle(startPoint, endPoint, borderColor, fillColor, borderWidth, true);
        } else if (shapeName.equals("Ellipse")) {
            shape = new Ellipse(startPoint, endPoint, borderColor, fillColor, borderWidth, false);
        } else if (shapeName.equals("Circle")) {
            shape = new Ellipse(startPoint, endPoint, borderColor, fillColor, borderWidth, true);
        } else if (shapeName.equals("Triangle")) {
            shape = new Triangle(startPoint, endPoint, borderColor, fillColor, borderWidth);
        }

        return shape;
    }

}
