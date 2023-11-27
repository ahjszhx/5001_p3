package shape;

import global.GlobalReference;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.awt.*;

/**
 * The Line class represents a line shape in the drawing application.
 * It extends the base Shape class and implements methods specific to drawing and manipulating lines.
 */
public class Line extends Shape {

    private int x2;

    private int y2;

    private String lineColor;

    private int lineWidth;

    public Line() {
    }

    /**
     * Parameterized constructor for creating a Line object with specified attributes.
     *
     * @param startPoint    The starting point of the line.
     * @param endPoint      The ending point of the line.
     * @param borderColor   The color of the line.
     * @param fillColor     The fill color of the line (not applicable for lines).
     * @param borderWidth   The width of the line.
     */
    public Line(Point startPoint, Point endPoint, Color borderColor, Color fillColor, float borderWidth) {
        super(startPoint, endPoint, borderColor, fillColor, borderWidth);
    }

    /**
     * Moves the line by a specified delta in the x and y directions.
     *
     * @param deltaX The change in x direction.
     * @param deltaY The change in y direction.
     */
    @Override
    public void move(int deltaX, int deltaY) {
        this.startPoint.setLocation(this.startPoint.getX() + deltaX, this.startPoint.getY() + deltaY);
        this.endPoint.setLocation(this.endPoint.getX() + deltaX, this.endPoint.getY() + deltaY);
    }

    /**
     * Rotates the line by a specified angle.
     *
     * @param angle The rotation angle.
     */
    @Override
    public void rotate(double angle) {
        // Find the midpoint of the line
        double midX = (startPoint.getX() + endPoint.getX()) / 2;
        double midY = (startPoint.getY() + endPoint.getY()) / 2;

        // Translate so that the midpoint becomes the origin
        double startXTranslated = startPoint.getX() - midX;
        double startYTranslated = startPoint.getY() - midY;
        double endXTranslated = endPoint.getX() - midX;
        double endYTranslated = endPoint.getY() - midY;

        double radians = Math.toRadians(angle);

        double newX1 = startXTranslated * Math.cos(radians) - startYTranslated * Math.sin(radians);
        double newY1 = startXTranslated * Math.sin(radians) + startYTranslated * Math.cos(radians);

        double newX2 = endXTranslated * Math.cos(radians) - endYTranslated * Math.sin(radians);
        double newY2 = endXTranslated * Math.sin(radians) + endYTranslated * Math.cos(radians);

        newX1 += midX;
        newY1 += midY;
        newX2 += midX;
        newY2 += midY;

        // Set the new coordinates
        startPoint.setLocation(newX1, newY1);
        endPoint.setLocation(newX2, newY2);
    }


    /**
     * Draws the line shape on the graphics context.
     *
     * @param g The graphics context to draw on.
     */
    @Override
    public void drawShape(Graphics2D g) {
        super.drawShape(g);
        this.setWebProperties();
        g.setPaint(borderColorModel);
        g.drawLine((int) super.startPoint.getX(), (int) super.startPoint.getY(), (int) super.endPoint.getX(), (int) super.endPoint.getY());
    }

    /**
     * Checks if a point is on the line within a specified tolerance.
     *
     * @param point     The point to check.
     * @return True if the point is on the line, false otherwise.
     */
    @Override
    public boolean contains(Point point) {
        // Check if the point is on the line (within a tolerance)
        return isPointOnLine(point, this.startPoint, this.endPoint, 5);
    }

    /**
     * Checks if a point is on the line within a specified tolerance.
     *
     * @param point     The point to check.
     * @param lineStart The starting point of the line.
     * @param lineEnd   The ending point of the line.
     * @param tolerance The tolerance for the check.
     * @return True if the point is on the line, false otherwise.
     */
    public boolean isPointOnLine(Point point, Point lineStart, Point lineEnd, int tolerance) {
        double distance = pointToLineDistance(point, lineStart, lineEnd);
        return distance <= tolerance;
    }

    /**
     * Calculates the distance from a point to a line defined by two endpoints.
     *
     * @param point     The point.
     * @param lineStart The starting point of the line.
     * @param lineEnd   The ending point of the line.
     * @return The distance from the point to the line.
     */
    private double pointToLineDistance(Point point, Point lineStart, Point lineEnd) {
        double lineLength = lineStart.distance(lineEnd);
        if (lineLength == 0) {
            // 两个端点相同，线段长度为0，返回点到端点的距离
            return point.distance(lineStart);
        }

        double u = ((point.x - lineStart.x) * (lineEnd.x - lineStart.x) +
                (point.y - lineStart.y) * (lineEnd.y - lineStart.y)) / (lineLength * lineLength);

        if (u < 0) {
            // 最近点在线段的起点之前，返回点到起点的距离
            return point.distance(lineStart);
        } else if (u > 1) {
            // 最近点在线段的终点之后，返回点到终点的距离
            return point.distance(lineEnd);
        } else {
            // 最近点在线段上，计算点到线段的距离
            double intersectionX = lineStart.x + u * (lineEnd.x - lineStart.x);
            double intersectionY = lineStart.y + u * (lineEnd.y - lineStart.y);
            return point.distance(intersectionX, intersectionY);
        }
    }
    /**
     * Sets web-specific properties for serialization.
     */
    private void setWebProperties() {
        this.x2 = endPoint.x;
        this.y2 = endPoint.y;
        this.lineColor = GlobalReference.COLOR_STR.get(super.borderColorModel);
        this.lineWidth = (int) borderWidth;
    }

    /**
     * Converts the Line object to a JSON representation.
     *
     * @return The JSON representation of the Line.
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
        if (lineColor != null) {
            builder.add("lineColor", lineColor);
        }
        if (lineWidth != 0) {
            builder.add("lineWidth", lineWidth);
        }
        return builder.build();
    }

    @Override
    public Line clone() {
        Line clonedLine = new Line();
        clonedLine.setUuid(this.getUuid());
        clonedLine.setInnerId(this.getInnerId());
        clonedLine.setBorderColorModel(new Color(this.borderColorModel.getRGB()));
        clonedLine.setFillColorModel(new Color(this.fillColorModel.getRGB()));
        clonedLine.setBorderWidth(this.borderWidth);
        clonedLine.setRotation(this.rotation);
        // 复制引用类型属性
        if (this.startPoint != null) {
            clonedLine.setStartPoint(new Point(this.startPoint));
        }

        if (this.endPoint != null) {
            clonedLine.setEndPoint(new Point(this.endPoint));
        }

        return clonedLine;
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

    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }



}
