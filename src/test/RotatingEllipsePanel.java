package test;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

class RotatingEllipsePanel extends JPanel {

    private Ellipse2D.Double ellipse;
    private double rotationAngle;

    public RotatingEllipsePanel() {
        ellipse = new Ellipse2D.Double(50, 50, 100, 50); // Updated position to be inside the screen
        rotationAngle = 0;
    }

    public void rotateEllipse() {
        rotationAngle += Math.toRadians(30);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        double centerX = ellipse.getX() + ellipse.getWidth() / 2.0;
        double centerY = ellipse.getY() + ellipse.getHeight() / 2.0;

        AffineTransform transform = AffineTransform.getRotateInstance(rotationAngle, centerX, centerY);

        Shape rotatedEllipse = transform.createTransformedShape(ellipse);

        g2d.draw(rotatedEllipse);
    }
}