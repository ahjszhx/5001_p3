package view;

import controller.DrawingController;
import global.GlobalReference;
import http.DrawingAction;
import http.DrawingData;
import http.DrawingInfo;
import http.JsonParser;
import http.ResultData;
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

    private DrawingModel model;

    private DrawingController drawingController;

    private JButton undoButton;

    private JButton redoButton;

    private static Socket socket;

    private static String initResponse = "";

    private static String addResponse = "";

    private static String getResponse = "";

    private static List<Shape> serverShapes = new ArrayList<>();

    private static ResultData resultData;


    public DrawingView(DrawingModel model, DrawingController controller) {
        this.model = model;
        this.drawingController = controller;
        this.drawingAreaPanel = new DrawingPanel(GlobalReference.PANEL_WIDTH, GlobalReference.PANEL_HEIGHT, controller);
        initializeView();
        model.addListener(this);
        model.addListener(drawingAreaPanel);
        initializeSocket();
    }

    private void initializeView() {

        //SwingUtilities.invokeLater(() -> {

        mainFrame = new JFrame("Vector Drawing App");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //mainFrame.setSize(GlobalReference.VIEW_WIDTH, GlobalReference.VIEW_HEIGHT);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("OPERATION:"));

        JButton requestDrawings = createButton("getDrawings");
        requestDrawings.addActionListener(e -> {
            getDrawings();
            drawingController.loadFromServer(serverShapes);
        });
        topPanel.add(requestDrawings);

        JButton submit = createButton("addDrawing");
        submit.addActionListener(e -> {
            if (model.getShapeList().size() == 0) {
                JOptionPane.showMessageDialog(mainFrame, "Please draw a graph before submitting.", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else {
                addDrawing();
                JOptionPane.showMessageDialog(mainFrame, "Submit successfully, the id is" + resultData.getData().getId());
            }
            drawingController.setAddResult(addResponse);
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
            jfc.setSelectedFile(new File("new file.png"));
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

            topPanel.add(saveButton);
        });

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
            drawingAreaPanel.rotateSelectedShape();
            mainFrame.repaint();
        });
        leftPanel.add(rotationButton);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.PAGE_START);
        mainPanel.add(leftPanel, BorderLayout.LINE_START);
        mainPanel.add(drawingAreaPanel, BorderLayout.CENTER);

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
        //});
    }


    private static void initializeSocket() {
        String serverAddress = "cs5001-p3.dynv6.net";
        int serverPort = 8080;
        try {
            socket = new Socket(serverAddress, serverPort);
            String loginCommand = "{\"action\": \"login\", \"data\": {\"token\": \"49468b90-9ee2-4f75-81ae-a4d228ff2268\"}}";
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(loginCommand);
            initResponse = reader.readLine();
            System.out.println("init response=>" + initResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDrawing() {
        DrawingData drawingData = new DrawingData(model.getShapeList().get(model.getShapeList().size() - 1));
        DrawingAction drawingAction = new DrawingAction("addDrawing", drawingData);
        String requestBody = drawingAction.toJson();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(requestBody);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(requestBody);
            addResponse = reader.readLine();
            System.out.println("addResponse=>" + addResponse);
            resultData = JsonParser.parseAddResult(addResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getDrawings() {
        try {
            String getDrawingsCommand = "{\"action\": \"getDrawings\"}";
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Send the request to the server
            writer.println(getDrawingsCommand);
            getResponse = reader.readLine();
            System.out.println("getResponse=>: " + getResponse);
            List<DrawingInfo> drawingInfoList = JsonParser.parseInfos(getResponse);
            System.out.println("Number of drawings: " + drawingInfoList.size());
            for (DrawingInfo drawingInfo : drawingInfoList) {
                try {
                    Shape shape = drawingInfo.getShapeInstance();
                    if (shape != null) {
                        serverShapes.add(shape);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("serverShapes=>" + serverShapes.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
