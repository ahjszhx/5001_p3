package shape;

import global.GlobalReference;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.awt.*;

public class Line extends Shape {

    private int x2;

    private int y2;

    private String lineColor;

    private int lineWidth;

    public Line() {
    }

    public Line(Point startPoint, Point endPoint, Color borderColor, Color fillColor, float borderWidth) {
        super(startPoint, endPoint, borderColor, fillColor, borderWidth);
    }

    @Override
    public void move(int deltaX, int deltaY) {
        this.startPoint.setLocation(this.startPoint.getX() + deltaX, this.startPoint.getY() + deltaY);
        this.endPoint.setLocation(this.endPoint.getX() + deltaX, this.endPoint.getY() + deltaY);
    }

    @Override
    public void rotate(double angle) {
        // 计算旋转后的新坐标
        double startX = startPoint.getX();
        double startY = startPoint.getY();
        double endX = endPoint.getX();
        double endY = endPoint.getY();

        // 将角度转换为弧度
        double radians = Math.toRadians(angle);

        // 计算旋转后的新坐标
        double newX1 = Math.cos(radians) * (startX - endX) - Math.sin(radians) * (startY - endY) + endX;
        double newY1 = Math.sin(radians) * (startX - endX) + Math.cos(radians) * (startY - endY) + endY;

        double newX2 = Math.cos(radians) * (endX - startX) - Math.sin(radians) * (endY - startY) + startX;
        double newY2 = Math.sin(radians) * (endX - startX) + Math.cos(radians) * (endY - startY) + startY;

        // 设置旋转后的新坐标
        startPoint.setLocation(newX1, newY1);
        endPoint.setLocation(newX2, newY2);
    }


    @Override
    public boolean contains(Point point) {
        // Check if the point is on the line (within a tolerance)
        return isPointOnLine(point, this.startPoint, this.endPoint, 5);
    }

    @Override
    public void drawShape(Graphics2D g) {
        super.drawShape(g);
        this.setWebProperties();
        g.setPaint(borderColorModel);
        g.drawLine((int) super.startPoint.getX(), (int) super.startPoint.getY(), (int) super.endPoint.getX(), (int) super.endPoint.getY());
    }

    private void setWebProperties() {
        this.x2 = endPoint.x;
        this.y2 = endPoint.y;
        this.lineColor = GlobalReference.COLOR_STR.get(super.borderColorModel);
        this.lineWidth = (int) borderWidth;
    }

    @Override
    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder()
                .add("x2", x2)
                .add("y2", y2)
                .add("lineColor", lineColor)
                .add("lineWidth", lineWidth);
        return builder.build();
    }

    public boolean isPointOnLine(Point point, Point lineStart, Point lineEnd, int tolerance) {
        double distance = pointToLineDistance(point, lineStart, lineEnd);
        return distance <= tolerance;
    }

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

    public String getLineColor() {
        return lineColor;
    }

    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Line)) return false;
        Line line = (Line) o;
        return getStartPoint() == line.getStartPoint() && getEndPoint() == line.getEndPoint() &&
                getX2() == line.getX2() && getY2() == line.getY2() && getLineWidth() == line.getLineWidth() && getLineColor().equals(line.getLineColor());
    }

    @Override
    public Line clone() {
        Line clonedLine = new Line();
        clonedLine.setUuid(this.getUuid());
        clonedLine.setInnerId(this.getInnerId());
        clonedLine.setBorderColorModel(new Color(this.borderColorModel.getRGB()));
        clonedLine.setFillColorModel(new Color(this.fillColorModel.getRGB()));
        clonedLine.setBorderWidth(this.borderWidth);

        // 复制引用类型属性
        if (this.startPoint != null) {
            clonedLine.setStartPoint(new Point(this.startPoint));
        }

        if (this.endPoint != null) {
            clonedLine.setEndPoint(new Point(this.endPoint));
        }

        return clonedLine;
    }
}
