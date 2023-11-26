package controller;
import model.DrawingModel;
import model.SaveToFile;
import shape.Shape;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    public void setShapeServerId(String serverId,String innerId) {
        model.setShapeServerId(serverId,innerId);
    }

    public void removeShapeFromServer(String serverId,String innerId) {
        model.removeShapeFromServer(serverId,innerId);
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

    public void saveAsFile(String path) throws IOException{
        if (!path.endsWith(".drawing")) {
            path = path + ".drawing";
        }
        FileOutputStream fileOut = new FileOutputStream(path);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(model.save());
        out.close();
        fileOut.close();
    }


    public void openFromFile(String path) throws IOException, ClassNotFoundException {

        FileInputStream fileIn = new FileInputStream(path);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        SaveToFile fileRead = (SaveToFile) in.readObject();
        in.close();
        fileIn.close();
        model.loadFromFile(fileRead);

    }

    public void loadFromServer(List<Shape> list){
        model.loadFromServer(list);
    }
}