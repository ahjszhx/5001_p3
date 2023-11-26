package view;

import controller.DrawingController;
import global.GlobalReference;
import http.DrawingAction;
import http.DrawingData;
import http.DrawingInfo;
import http.JsonParser;
import http.ResultData;
import http.ServerConnect;
import model.DrawingModel;
import shape.Shape;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DrawingView implements PropertyChangeListener {

    private JFrame mainFrame;

    private DrawingPanel drawingAreaPanel;

    private static DrawingModel model;

    private DrawingController drawingController;

    private JButton undoButton;

    private JButton redoButton;


    public DrawingView(DrawingModel model, DrawingController controller) {
        this.model = model;
        this.drawingController = controller;
        this.drawingAreaPanel = new DrawingPanel(GlobalReference.PANEL_WIDTH, GlobalReference.PANEL_HEIGHT, controller);
        initializeView();
        model.addListener(this);
        model.addListener(drawingAreaPanel);
        ServerConnect.initializeSocket();
    }

    private void initializeView() {

        mainFrame = new JFrame("Vector Drawing App");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //mainFrame.setSize(GlobalReference.VIEW_WIDTH, GlobalReference.VIEW_HEIGHT);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("OPERATION:"));

        JButton requestDrawings = createButton("getDrawings");
        requestDrawings.addActionListener(e -> {
            ServerConnect.getDrawings();
            drawingController.loadFromServer(ServerConnect.getServerShapes());
        });
        topPanel.add(requestDrawings);

        JButton submit = createButton("addDrawing");
        submit.addActionListener(e -> {
            if (model.getShapeList().size() == 0) {
                JOptionPane.showMessageDialog(mainFrame, "Please draw a graph before submitting.", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else {
                DrawingData drawingData = new DrawingData(model.getShapeList().get(model.getShapeList().size() - 1));
                ServerConnect.addDrawing(drawingData);
                JOptionPane.showMessageDialog(mainFrame, "Submit successfully, the id is" + ServerConnect.getResultData().getData().getId());
            }
            drawingController.setAddResult(ServerConnect.getAddResponse());
        });
        topPanel.add(submit);


        undoButton = createButton("Undo");
        undoButton.addActionListener(e -> {
            drawingController.controlUndo();
            mainFrame.repaint();
        });
        undoButton.setEnabled(false);
        topPanel.add(undoButton);

        redoButton = createButton("Redo");
        redoButton.addActionListener(e -> {
            drawingController.controlRedo();
            mainFrame.repaint();
        });
        redoButton.setEnabled(false);
        topPanel.add(redoButton);


        JButton saveButton = createButton("Save As PNG");
        saveButton.addActionListener(e -> {
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.setSelectedFile(new File(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"))+".png"));
            int statusCode = jfc.showDialog(mainFrame, "save");
            if (statusCode == JFileChooser.APPROVE_OPTION) {
                if (drawingController.saveAsPNG(drawingAreaPanel, jfc.getSelectedFile().getPath())) {
                    JOptionPane.showMessageDialog(mainFrame, "save successfully!");
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "save failed!");
                }
            } else if (statusCode == JFileChooser.ERROR_OPTION) {
                JOptionPane.showMessageDialog(mainFrame, "unknown error!");
            }
        });
        topPanel.add(saveButton);
        JButton saveAsFileButton = createButton("Save As File");
        saveAsFileButton.addActionListener(e -> {
            JFileChooser jfc = new JFileChooser();
            // only file can be chosen
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            // extension name limitation
            jfc.setFileFilter(new FileNameExtensionFilter("Save to file (.drawing)", "drawing"));
            jfc.setSelectedFile(new File(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")) + ".drawing"));
            int statusCode = jfc.showDialog(mainFrame, "save");
            if (statusCode == JFileChooser.APPROVE_OPTION) {
                if (drawingController.saveAsFile(jfc.getSelectedFile().getPath())) {
                    JOptionPane.showMessageDialog(mainFrame, "save successfully!");
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "save failed!");
                }
            } else if (statusCode == JFileChooser.ERROR_OPTION) {
                JOptionPane.showMessageDialog(mainFrame, "unknown error!");
            }
        });
        topPanel.add(saveAsFileButton);
        JButton openButton = createButton("Open From File");
        openButton.addActionListener(e -> {
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.setFileFilter(new FileNameExtensionFilter("Open drawing file (.drawing)", "drawing"));
            int statusCode = jfc.showDialog(mainFrame, "open");
            if (statusCode == JFileChooser.APPROVE_OPTION) {
                if (drawingController.openFromFile(jfc.getSelectedFile().getPath())) {
                    JOptionPane.showMessageDialog(mainFrame, "open successfully!");
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "open failed!");
                }
            } else if (statusCode == JFileChooser.ERROR_OPTION) {
                JOptionPane.showMessageDialog(mainFrame, "unknown error!");
            }
        });
        topPanel.add(openButton);
        JButton clearButton = createButton("Clear");
        clearButton.addActionListener(e -> {
            drawingController.controlClear();
            mainFrame.repaint();
        });
        topPanel.add(clearButton);

        ArrayList<JButton> shapeButtonList = new ArrayList<>();
        GridLayout gridLayout = new GridLayout(0, 1);
        gridLayout.setVgap(10); // 设置行间距
        JPanel leftPanel = new JPanel(gridLayout);
        ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(mainFrame, drawingAreaPanel);

        JButton borderColorSelectorButton = new JButton("Border Color");
        borderColorSelectorButton.addActionListener(e -> {
            drawingAreaPanel.setColorChosenMode("borderColor");
            colorSelectorDialog.setVisible(true);
        });
        leftPanel.add(borderColorSelectorButton);

        JButton fillColorSelectorButton = new JButton("Fill Color");
        fillColorSelectorButton.addActionListener(e -> {
            drawingAreaPanel.setColorChosenMode("fillColor");
            colorSelectorDialog.setVisible(true);
        });
        leftPanel.add(fillColorSelectorButton);

        JButton widthButton = createLeftButton("Border Width");
        widthButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(mainFrame, "Enter Border Width:", drawingAreaPanel.getBorderWidth());
            if (input != null && !input.isEmpty()) {
                try {
                    float borderWidth = Float.parseFloat(input);
                    drawingAreaPanel.setBorderWidth(borderWidth);
                    JOptionPane.showMessageDialog(mainFrame, "Line Width set to " + borderWidth);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(mainFrame, "Invalid input. Please enter a valid number.");
                }
            }
        });
        leftPanel.add(widthButton);
        for (String buttonName : GlobalReference.drawButtons) {
            JButton tempButton = createLeftButton(buttonName);
            tempButton.addActionListener(e -> {
                drawingAreaPanel.setDrawMode(buttonName);
                JButton clickedButton = (JButton) e.getSource();
                for (JButton button : shapeButtonList) {
                    button.setEnabled(true);
                }
                clickedButton.setEnabled(false);
            });
            leftPanel.add(tempButton);
            shapeButtonList.add(tempButton);

        }
        JTextField rotationTextField = new JTextField("Input Rotation Angle");
        leftPanel.add(rotationTextField, BorderLayout.SOUTH);
        rotationTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (rotationTextField.getText().equals("Input Rotation Angle")) {
                    rotationTextField.setText("");
                    rotationTextField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (rotationTextField.getText().isEmpty()) {
                    rotationTextField.setText("Input Rotation Angle");
                    rotationTextField.setForeground(Color.GRAY);
                }
                drawingAreaPanel.setRotationAngle(Double.parseDouble(rotationTextField.getText()));
            }
        });
        JButton rotationButton = createLeftButton("Rotate Shape");
        rotationButton.addActionListener(e -> {
            //drawingController.controlClear();
            drawingAreaPanel.setRotationAngle(0);
            //drawingAreaPanel.rotateSelectedShape();
            mainFrame.repaint();
        });
        leftPanel.add(rotationButton);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.PAGE_START);
        mainPanel.add(leftPanel, BorderLayout.LINE_START);
        mainPanel.add(drawingAreaPanel, BorderLayout.CENTER);

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        redoButton.setEnabled(!model.isRedoStackEmpty());
        undoButton.setEnabled(!model.isUndoStackEmpty());
    }


    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        // 设置按钮大小
        button.setPreferredSize(new Dimension(150, 30));
        return button;
    }

    private static JButton createLeftButton(String text) {
        JButton button = new JButton(text);
        // 设置按钮大小
        button.setPreferredSize(new Dimension(150, 20));
        return button;
    }

}
