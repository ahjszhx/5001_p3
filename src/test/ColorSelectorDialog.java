package test;

import view.DrawingPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class ColorSelectorDialog extends JDialog {

    private DrawingPanel drawingAreaPanel;
    private JPanel colorDisplayPanel;

    private static Map<String,Color> colorNameMap = new HashMap<String,Color>();
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
                    drawingAreaPanel.setBorderColor(selectedColor);
                    updateColorDisplay(selectedColor);
                    setVisible(false);
                }
            });
            colorPanel.add(colorButton);
        }

        setContentPane(colorPanel);
        setSize(300, 200);
        setLocationRelativeTo(getParent()); // 将对话框显示在 mainFrame 中央
    }

    private void updateColorDisplay(Color color) {
        colorDisplayPanel.setBackground(color);
        colorDisplayPanel.repaint(); // 更新组件
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test");
            frame.setSize(300, 200);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            DrawingPanel drawingPanel = new DrawingPanel(400, 300, null);
            ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(frame, drawingPanel);

            JButton openColorSelectorButton = new JButton("Open Color Selector");
            openColorSelectorButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    colorSelectorDialog.setVisible(true);
                }
            });

            JPanel mainPanel = new JPanel();
            mainPanel.add(openColorSelectorButton);
            mainPanel.add(drawingPanel);

            frame.add(mainPanel);
            frame.setVisible(true);
        });
    }
}
