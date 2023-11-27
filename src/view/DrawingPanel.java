package view;

import controller.DrawingController;
import shape.Shape;
import shape.ShapeFactory;
import shape.Triangle;

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


/**
 * The DrawingPanel class represents the panel where shapes are drawn and manipulated.
 */
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
    private String colorChosenModel;
    private double rotationAngle;

    /**
     * Constructs a DrawingPanel with the specified dimensions and controller.
     *
     * @param width      The width of the panel.
     * @param height     The height of the panel.
     * @param controller The controller for handling interactions.
     */
    public DrawingPanel(int width, int height, DrawingController controller) {
        this.width = width;
        this.height = height;
        this.controller = controller;
        this.setVisible(true);
        initialize();
    }

    /**
     * Initializes the drawing panel with default settings and event listeners.
     */
    private void initialize() {
        this.borderColor = Color.BLACK;
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.white);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Gets the current color chosen mode.
     *
     * @return The current color chosen mode.
     */
    public String getColorChosenMode() {
        return colorChosenModel;
    }

    /**
     * Sets the color chosen mode and updates the selected shape's initial state if applicable.
     *
     * @param colorChosenModel The new color chosen mode.
     */
    public void setColorChosenModel(String colorChosenModel) {
        this.colorChosenModel = colorChosenModel;
        if (selectedShape != null) {
            System.out.println(this.selectedShape.getInnerId() + ",I am selected");
            this.initialSelectedShapeState = selectedShape.clone();
        }
    }

    /**
     * Overrides the paintComponent method to draw shapes on the panel.
     *
     * @param g The Graphics object used for painting.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (Shape shape : shapeList) {
            //super.paintComponent(g);
            shape.drawShape(g2);
        }
    }

    /**
     * Handles mouse click events, selecting the topmost shape at the clicked point.
     *
     * @param e The MouseEvent object representing the click event.
     */
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

    /**
     * Handles mouse press events, initializing the starting point and selecting the topmost shape.
     *
     * @param e The MouseEvent object representing the press event.
     */
    //429,557   693,715   561 329     622,671
    @Override
    public void mousePressed(MouseEvent e) {
        this.startPoint = e.getPoint();
        System.out.println("startPoint=>"+e.getPoint().x+","+e.getPoint().getY());
        this.selectedShape = getTopmostSelectedShape(startPoint);
        if (selectedShape != null) {
            this.initialSelectedShapeState = selectedShape.clone();
        }
        this.lastMousePoint = e.getPoint();
    }


    /**
     * Handles mouse release events, updating the position of the selected shape or adding a new shape.
     *
     * @param e The MouseEvent object representing the release event.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        Point endPoint = e.getPoint();
        if (selectedShape != null) {
            // Calculate mouse movement
            int dx = e.getX() - lastMousePoint.x;
            int dy = e.getY() - lastMousePoint.y;

            // Move the selected shape
            selectedShape.move(dx, dy);

            // Update the last known mouse position

            // Repaint the panel
            repaint();
            lastMousePoint = e.getPoint();
            this.prevShape = initialSelectedShapeState.clone();
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

    /**
     * Handles mouse drag events, moving the selected shape.
     *
     * @param e The MouseEvent object representing the drag event.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (selectedShape != null) {
            int dx = e.getX() - lastMousePoint.x;
            int dy = e.getY() - lastMousePoint.y;

            // Move the selected shape
            selectedShape.move(dx, dy);

            // Update the last known mouse position
            lastMousePoint = e.getPoint();

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
        Object newValue = evt.getNewValue();
         shapeList = (List<Shape>) newValue;
            paintComponent(getGraphics());
        }
//        shapeList = (List<Shape>) evt.getNewValue();
//        paintComponent(getGraphics());
    }

    /**
     * Rotates the selected shape by the specified angle.
     */
    public void rotateSelectedShape() {
        if (selectedShape != null) {
            // 获取用户输入的旋转角度
            //String input = JOptionPane.showInputDialog(this, "Enter Rotation Angle:");
            if (rotationAngle != 0) {
                try {
                    Shape prevShape = selectedShape.clone();
                    // 旋转选中的图形
                    selectedShape.rotate(rotationAngle);
                    Shape currentShape = selectedShape.clone();
                    // 重新绘制界面
                    repaint();
                    // 更新图形状态
                    controller.updateShape(currentShape, prevShape);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
                }
            }
        }
    }

    /**
     * Gets the topmost selected shape at a given point.
     *
     * @param point The point at which to check for a selected shape.
     * @return The topmost selected shape, or null if no shape is found.
     */
    private Shape getTopmostSelectedShape(Point point) {
        List<Shape> reversedList = this.shapeList;
        Collections.reverse(reversedList);

        for (Shape shape : reversedList) {
            if(shape instanceof Triangle){
                System.out.println(shape.toJson()+","+shape.getStartPoint().x+","+shape.getStartPoint().getY());
            }
            if (shape.contains(point)) {
                return shape;
            }
        }
        return null;
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

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    public void setDrawMode(String drawMode) {
        this.drawMode = drawMode;
    }

    public Color getBorderColor() {
        return borderColor;
    }
}