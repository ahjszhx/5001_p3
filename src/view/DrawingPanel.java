package view;

import controller.DrawingController;
import shape.Shape;
import shape.ShapeFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DrawingPanel extends JPanel implements MouseListener, MouseMotionListener, PropertyChangeListener {


    private final int width;
    private final int height;
    private String drawMode;
    private Color borderColor;
    private float borderWidth;
    private Color fillColor;
    private List<Shape> shapeList = new ArrayList<>();
    private final DrawingController controller;
    private Point startPoint;
    private Shape selectedShape;
    private Shape initialSelectedShapeState;
    private Shape prevShape;
    private Point lastMousePoint;
    private String colorChosenMode;
    private double rotationAngle;


    public DrawingPanel(int width, int height, DrawingController controller) {
        this.width = width;
        this.height = height;
        this.controller = controller;
        this.setVisible(true);
        initialize();
    }


    private void initialize() {
        this.borderColor = Color.BLACK;
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.white);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public String getColorChosenMode() {
        return colorChosenMode;
    }

    public void setColorChosenMode(String colorChosenMode) {
        this.colorChosenMode = colorChosenMode;
        if (selectedShape != null) {
            System.out.println(this.selectedShape.getInnerId() + ",I am selected");
            this.initialSelectedShapeState = selectedShape.clone();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (Shape shape : shapeList) {
            //super.paintComponent(g);
            shape.drawShape(g2);
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        //this.startPoint = e.getPoint();

        this.selectedShape = getTopmostSelectedShape(startPoint);
        if (selectedShape != null) {
            System.out.println(this.selectedShape.getInnerId() + ",I am selected");
            this.initialSelectedShapeState = selectedShape.clone();
        }
        //this.lastMousePoint = e.getPoint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.startPoint = e.getPoint();
        this.selectedShape = getTopmostSelectedShape(startPoint);
        if (selectedShape != null) {
            this.initialSelectedShapeState = selectedShape.clone();
        }
        this.lastMousePoint = e.getPoint();
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        Point endPoint = e.getPoint();
        if (selectedShape != null) {
            // Calculate mouse movement
            int dx = e.getX() - lastMousePoint.x;
            int dy = e.getY() - lastMousePoint.y;


            // If there was significant movement, treat it as a drag and don't perform other operations
            // Move the selected shape
            selectedShape.move(dx, dy);

            // Update the last known mouse position
            lastMousePoint = e.getPoint();
            this.prevShape = initialSelectedShapeState.clone();
            // Repaint the panel
            repaint();
            if (rotationAngle == 0) {
                controller.updateShape(selectedShape, prevShape);
                selectedShape = null;
            }
        } else {
            Shape shape = ShapeFactory.createShape(this.drawMode, this.startPoint, endPoint, borderColor, fillColor, borderWidth);
            controller.addShape(shape);
        }
        // Reset the selected shape
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (selectedShape != null) {
            //prevShape =
            // Calculate mouse movement
            int dx = e.getX() - lastMousePoint.x;
            int dy = e.getY() - lastMousePoint.y;

            // Move the selected shape
            selectedShape.move(dx, dy);

            // Update the last known mouse position
            lastMousePoint = e.getPoint();

            // Repaint the panel
            //repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }


    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        shapeList = (List<Shape>) evt.getNewValue();
        paintComponent(getGraphics());
    }

    public void rotateSelectedShape() {
        if (selectedShape != null) {
            // 获取用户输入的旋转角度
            // 这里可以使用 JOptionPane.showInputDialog 或者其他方式获取输入
            //String input = JOptionPane.showInputDialog(this, "Enter Rotation Angle:");
            if (rotationAngle != 0) {
                try {
                    // 旋转选中的图形
                    selectedShape.rotate(rotationAngle);
                    // 重新绘制界面
                    repaint();
                    // 更新图形状态
                    controller.updateShape(selectedShape, initialSelectedShapeState);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
                }
            }
        }
    }


    private Shape getTopmostSelectedShape(Point point) {
        List<Shape> reversedList = this.shapeList;
        Collections.reverse(reversedList);

        for (Shape shape : reversedList) {
            if (shape.contains(point)) {
                return shape;
            }
        }
        return null;
    }

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    public void setDrawMode(String drawMode) {
        this.drawMode = drawMode;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        if (this.selectedShape != null) {
            selectedShape.setBorderColorModel(color);
        }
        this.changeShapeProperty();
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        if (this.selectedShape != null) {
            selectedShape.setFillColorModel(fillColor);
        }
        changeShapeProperty();
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        if (this.selectedShape != null) {
            selectedShape.setBorderWidth(borderWidth);
        }
        changeShapeProperty();
    }

    private void changeShapeProperty() {
        if (selectedShape != null) {
            // Calculate mouse movement
            this.prevShape = initialSelectedShapeState.clone();
            // Repaint the panel
            controller.updateShape(selectedShape, prevShape);
            selectedShape = null;
            repaint();
        }
    }

    public Shape getSelectedShape() {
        return selectedShape;
    }

    public void setSelectedShape(Shape selectedShape) {
        this.selectedShape = selectedShape;
    }
}