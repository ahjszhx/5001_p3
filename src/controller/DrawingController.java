package controller;
import model.DrawingModel;
import model.SaveAsFile;
import shape.Shape;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;


public class DrawingController{

    private DrawingModel model;

    public DrawingController(DrawingModel model) {
        this.model = model;
    }

    public void addShape(Shape shape) {
        model.addShape(shape);
    }


    public void updateShape(Shape updatedShape,Shape prevShape) {
        model.updateShape(updatedShape,prevShape);
    }

    public void setAddResult(String shape){
        System.out.println(shape);
    }

    public void controlUndo() {
        model.undo();
    }


    public void controlRedo() {
        model.redo();
    }


    public void controlClear() {
        model.clear();
    }

    public boolean saveAsPNG(Component component, String filePath) {
        BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        component.paint(g2d);
        g2d.dispose();
        try {
            ImageIO.write(image, "png", new File(filePath));
            System.out.println("Image saved as " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean saveAsFile(String path) {
        try {
            if (!path.endsWith(".drawing")) {
                path = path + ".drawing";
            }
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(model.save());
            out.close();
            fileOut.close();
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }

    public void loadFromServer(List<Shape> list){
        model.loadFromServer(list);
    }

    public boolean openFromFile(String path) {
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            SaveAsFile fileRead = (SaveAsFile) in.readObject();
            in.close();
            fileIn.close();
            model.loadFromFile(fileRead);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}