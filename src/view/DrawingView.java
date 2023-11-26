package view;

import controller.DrawingController;
import global.GlobalReference;
import http.ClientAction;
import http.DrawingData;
import http.ServerConnect;
import model.DrawingModel;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


/**
 * The DrawingView class represents the graphical user interface for the vector drawing application.
 */
public class DrawingView implements PropertyChangeListener {

    private JFrame mainFrame;

    private DrawingPanel drawingAreaPanel;

    private static DrawingModel model;

    private DrawingController drawingController;

    private JButton undoButton;

    private JButton redoButton;

    /**
     * Constructs a DrawingView with the specified DrawingModel and DrawingController.
     *
     * @param model      The DrawingModel for the application.
     * @param controller The DrawingController for managing user interactions.
     */
    public DrawingView(DrawingModel model, DrawingController controller) {
        this.model = model;
        this.drawingController = controller;
        this.drawingAreaPanel = new DrawingPanel(GlobalReference.PANEL_WIDTH, GlobalReference.PANEL_HEIGHT, controller);
        initializeView();
        model.addListener(this);
        model.addListener(drawingAreaPanel);
        ServerConnect.initializeSocket();
    }

    /**
     * Initializes the graphical user interface components.
     */
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
            if (drawingAreaPanel.getSelectedShape() == null) {
                JOptionPane.showMessageDialog(mainFrame, "Please select a shape before submitting.", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else {
                DrawingData drawingData = new DrawingData(drawingAreaPanel.getSelectedShape());
                ClientAction serverAction = new ClientAction("addDrawing",drawingData);
                ServerConnect.addOrUpdateDrawing(serverAction);
                if(ServerConnect.getResultData().getResult().equals("ok")){
                    drawingController.setShapeServerId( ServerConnect.getResultData().getData().getId(),drawingAreaPanel.getSelectedShape().getInnerId());
                    JOptionPane.showMessageDialog(mainFrame, "Submit successfully, the id is" + ServerConnect.getResultData().getData().getId());
                }
                else {
                    JOptionPane.showMessageDialog(mainFrame, "Submit failed");
                }
                drawingAreaPanel.setSelectedShape(null);
            }
        });
        topPanel.add(submit);

        JButton updateDrawing = createButton("updateDrawing");
        updateDrawing.addActionListener(e -> {
            if (drawingAreaPanel.getSelectedShape() == null) {
                JOptionPane.showMessageDialog(mainFrame, "Please select a shape before updating.", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else {
                String serverId = drawingAreaPanel.getSelectedShape().getUuid();
                if(serverId==null){
                    JOptionPane.showMessageDialog(mainFrame, "This shape has not been submitted and cannot be updated.", "WARNING", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    DrawingData drawingData = new DrawingData(drawingAreaPanel.getSelectedShape());
                    ClientAction serverAction = new ClientAction("updateDrawing",drawingData);
                    ServerConnect.addOrUpdateDrawing(serverAction);
                    if(ServerConnect.getResultData().getResult().equals("ok")){
                        JOptionPane.showMessageDialog(mainFrame, "Update successfully");
                    }
                    else {
                        JOptionPane.showMessageDialog(mainFrame, "Update failed");
                    }
                    drawingAreaPanel.setSelectedShape(null);
                }
            }
            //drawingController.setAddResult(ServerConnect.getAddResponse());
        });
        topPanel.add(updateDrawing);

        JButton deleteDrawing = createButton("deleteDrawing");
        deleteDrawing.addActionListener(e -> {
            if (drawingAreaPanel.getSelectedShape() == null) {
                JOptionPane.showMessageDialog(mainFrame, "Please select a shape before deleting.", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else {
                String serverId = drawingAreaPanel.getSelectedShape().getUuid();
                String innerId = drawingAreaPanel.getSelectedShape().getInnerId();
                if(serverId==null){
                    JOptionPane.showMessageDialog(mainFrame, "This shape has not been submitted and cannot be deleted.", "WARNING", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    ServerConnect.deleteDrawing(serverId);
                    if(ServerConnect.getReceipt().getResult().equals("ok")){
                        JOptionPane.showMessageDialog(mainFrame, "Delete successfully, the id is" + ServerConnect.getResultData().getData().getId());
                        drawingController.removeShapeFromServer(serverId,innerId);
                    }
                    else {
                        JOptionPane.showMessageDialog(mainFrame, "Delete failed");
                    }
                    drawingAreaPanel.setSelectedShape(null);
                }
            }
        });
        topPanel.add(deleteDrawing);


        undoButton = createButton("Undo");
        undoButton.addActionListener(e -> {
            drawingController.undo();
            mainFrame.repaint();
        });
        undoButton.setEnabled(false);
        topPanel.add(undoButton);

        redoButton = createButton("Redo");
        redoButton.addActionListener(e -> {
            drawingController.redo();
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
            int statusCode = jfc.showDialog(mainFrame, "save to file");
            if (statusCode == JFileChooser.APPROVE_OPTION) {
                try {
                    drawingController.saveAsFile(jfc.getSelectedFile().getPath());
                    JOptionPane.showMessageDialog(mainFrame, "save success!");
                } catch (IOException ioException){
                    JOptionPane.showMessageDialog(mainFrame, "save failed!");
                    ioException.printStackTrace();
                }

            } else if (statusCode == JFileChooser.ERROR_OPTION) {
                JOptionPane.showMessageDialog(mainFrame, "save failed!");
            }
        });
        topPanel.add(saveAsFileButton);
        JButton openButton = createButton("Open From File");
        openButton.addActionListener(e -> {
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.setFileFilter(new FileNameExtensionFilter("Open drawing file (.drawing)", "drawing"));
            int statusCode = jfc.showDialog(mainFrame, "open drawing file");
            if (statusCode == JFileChooser.APPROVE_OPTION) {
                try {
                    drawingController.openFromFile(jfc.getSelectedFile().getPath());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, "Open failed!");
                    ex.printStackTrace();
                }
            } else if (statusCode == JFileChooser.ERROR_OPTION) {
                JOptionPane.showMessageDialog(mainFrame, "Open failed!");
            }
        });
        topPanel.add(openButton);
        JButton clearButton = createButton("Clear");
        clearButton.addActionListener(e -> {
            drawingController.clear();
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
            drawingAreaPanel.setColorChosenModel("borderColor");
            colorSelectorDialog.setVisible(true);
        });
        leftPanel.add(borderColorSelectorButton);

        JButton fillColorSelectorButton = new JButton("Fill Color");
        fillColorSelectorButton.addActionListener(e -> {
            drawingAreaPanel.setColorChosenModel("fillColor");
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
                //
            }
        });
        JButton rotationButton = createLeftButton("Rotate Shape");
        rotationButton.addActionListener(e -> {
            if(rotationTextField.getText().isEmpty()){
                JOptionPane.showMessageDialog(mainFrame, "Please enter rotation degree.");
            }
            else {
                drawingAreaPanel.setRotationAngle(Double.parseDouble(rotationTextField.getText()));
            }
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
    }

    /**
     * Handles property change events, enabling or disabling undo and redo buttons based on the model state.
     *
     * @param evt The PropertyChangeEvent object representing the property change event.
     */
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
