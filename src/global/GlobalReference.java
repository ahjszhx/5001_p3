package global;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalReference {

    public final static List<String> drawButtons = new ArrayList<>();
    public static final int PANEL_WIDTH = 900;
    public static final int PANEL_HEIGHT = 800;

    public static final int TOP_BUTTON_WIDTH = 150;
    public static final int TOP_BUTTON_HEIGHT = 30;

    public static final int LEFT_BUTTON_WIDTH = 150;
    public static final int LEFT_BUTTON_HEIGHT = 20;

    public final static Map<String, Color> STR_COLOR = new HashMap<String, Color>();

    public final static Map<Color, String> COLOR_STR = new HashMap<Color, String>();

    static {

        drawButtons.add("Line");
        drawButtons.add("Rectangle");
        drawButtons.add("Square");
        drawButtons.add("Triangle");
        drawButtons.add("Ellipse");
        drawButtons.add("Circle");

        STR_COLOR.put("red", Color.RED);
        STR_COLOR.put("green", Color.GREEN);
        STR_COLOR.put("blue", Color.BLUE);
        STR_COLOR.put("yellow", Color.YELLOW);
        STR_COLOR.put("black", Color.BLACK);
        STR_COLOR.put("white", Color.WHITE);
        STR_COLOR.put("cyan", Color.CYAN);
        STR_COLOR.put("magenta", Color.MAGENTA);
        STR_COLOR.put("orange", Color.ORANGE);
        STR_COLOR.put("pink", Color.PINK);
        STR_COLOR.put("gray", Color.GRAY);
        STR_COLOR.put("darkGray", Color.DARK_GRAY);
        STR_COLOR.put("lightGray", Color.LIGHT_GRAY);

        COLOR_STR.put(Color.RED, "red");
        COLOR_STR.put(Color.GREEN,"green");
        COLOR_STR.put(Color.BLUE, "blue");
        COLOR_STR.put(Color.YELLOW,"yellow");
        COLOR_STR.put(Color.BLACK, "black");
        COLOR_STR.put(Color.WHITE,"white");
        COLOR_STR.put(Color.CYAN, "cyan");
        COLOR_STR.put(Color.MAGENTA,"magenta");
        COLOR_STR.put(Color.ORANGE, "orange");
        COLOR_STR.put(Color.PINK,"pink");
        COLOR_STR.put(Color.GRAY, "gray");
        COLOR_STR.put(Color.DARK_GRAY,"darkGray");
        COLOR_STR.put(Color.LIGHT_GRAY, "lightGray");


    }

}
