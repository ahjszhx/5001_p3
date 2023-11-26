package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class ColorSelectorDialog extends JDialog {

    private DrawingPanel drawingAreaPanel;
    private static final Map<String, Color> colorNameMap = new HashMap<>();

    static {
        colorNameMap.put("red", Color.RED);
        colorNameMap.put("green", Color.GREEN);
        colorNameMap.put("blue", Color.BLUE);
        colorNameMap.put("yellow", Color.YELLOW);
        colorNameMap.put("black", Color.BLACK);
        colorNameMap.put("white", Color.WHITE);
        colorNameMap.put("cyan", Color.CYAN);
        colorNameMap.put("magenta", Color.MAGENTA);
        colorNameMap.put("orange", Color.ORANGE);
        colorNameMap.put("pink", Color.PINK);
        colorNameMap.put("gray", Color.GRAY);
        colorNameMap.put("darkGray", Color.DARK_GRAY);
        colorNameMap.put("lightGray", Color.LIGHT_GRAY);
    }

    public ColorSelectorDialog(JFrame parent, DrawingPanel drawingAreaPanel) {
        super(parent, "Choose Color", true);
        this.drawingAreaPanel = drawingAreaPanel;
        initializeDialog();
    }

    private void initializeDialog() {
        JPanel colorPanel = new JPanel(new GridLayout(0, 4, 5, 5));

        for (String colorName : colorNameMap.keySet()) {
            JButton colorButton = new JButton();
            colorButton.setBackground(colorNameMap.get(colorName));
            colorButton.setPreferredSize(new Dimension(50, 50));
            colorButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Color selectedColor = colorNameMap.get(colorName);
                    if (drawingAreaPanel.getColorChosenMode().equals("borderColor")) {
                        drawingAreaPanel.setBorderColor(selectedColor);
                    } else if (drawingAreaPanel.getColorChosenMode().equals("fillColor")) {
                        drawingAreaPanel.setFillColor(selectedColor);
                    }
                    setVisible(false);
                }
            });
            colorPanel.add(colorButton);
        }

        setContentPane(colorPanel);
        setSize(300, 200);
        setLocationRelativeTo(getParent());
    }


}