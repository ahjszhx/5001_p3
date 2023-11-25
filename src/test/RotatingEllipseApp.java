package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class RotatingEllipseApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Rotating Ellipse App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            RotatingEllipsePanel panel = new RotatingEllipsePanel();

            JButton rotateButton = new JButton("Rotate");
            rotateButton.addActionListener(e -> panel.rotateEllipse());

            JPanel controlPanel = new JPanel();
            controlPanel.add(rotateButton);

            frame.add(panel, BorderLayout.CENTER);
            frame.add(controlPanel, BorderLayout.SOUTH);

            frame.setSize(400, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

