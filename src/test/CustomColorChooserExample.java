package test;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class CustomColorChooserExample {


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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Custom Color Selector");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComboBox<String> colorComboBox = createColorComboBox();

        JButton chooseColorButton = new JButton("Choose Color");
        JLabel colorLabel = new JLabel("Selected Color:");
        JPanel colorPanel = new JPanel();

        colorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedColorName = (String) colorComboBox.getSelectedItem();
                Color selectedColor = colorNameMap.get(selectedColorName);
                colorPanel.setBackground(selectedColor);
            }
        });

        chooseColorButton.addActionListener(e -> {
            String selectedColorName = (String) colorComboBox.getSelectedItem();
            Color selectedColor = colorNameMap.get(selectedColorName);
            System.out.println("Selected Color: " + selectedColor);
        });

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(colorComboBox);
        panel.add(colorLabel);
        panel.add(colorPanel);
        panel.add(chooseColorButton);

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JComboBox<String> createColorComboBox() {
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        for (String colorName : colorNameMap.keySet()) {
            comboBoxModel.addElement(colorName);
        }

        JComboBox<String> colorComboBox = new JComboBox<>(comboBoxModel);
        colorComboBox.setSelectedIndex(0); // 设置默认选择

        return colorComboBox;
    }
}
