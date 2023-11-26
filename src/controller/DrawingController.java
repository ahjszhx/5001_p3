package controller;
import model.DrawingModel;
import model.SaveToFile;
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

/**
 * The DrawingController class manages user interactions and communicates with the underlying DrawingModel.
 */
public class DrawingController{

    private DrawingModel model;

    /**
     * Constructs a DrawingController with the specified DrawingModel.
     *
     * @param model The DrawingModel for managing shapes and application state.
     */
    public DrawingController(DrawingModel model) {
        this.model = model;
    }

    /**
     * Adds a shape to the drawing model.
     *
     * @param shape The shape to be added.
     */
    public void addShape(Shape shape) {
        model.addShape(shape);
    }

    /**
     * Updates the shape in the drawing model.
     *
     * @param updatedShape The updated shape.
     * @param prevShape    The previous state of the shape.
     */
    public void updateShape(Shape updatedShape,Shape prevShape) {
        model.updateShape(updatedShape,prevShape);
    }

    /**
     * Sets the server ID for a shape in the drawing model.
     *
     * @param serverId The server ID to be set.
     * @param innerId  The inner ID of the shape.
     */
    public void setShapeServerId(String serverId,String innerId) {
        model.setShapeServerId(serverId,innerId);
    }

    /**
     * Removes a shape from the drawing model based on its server ID and inner ID.
     *
     * @param serverId The server ID of the shape.
     * @param innerId  The inner ID of the shape.
     */
    public void removeShapeFromServer(String serverId,String innerId) {
        model.removeShapeFromServer(serverId,innerId);
    }

    /**
     * Initiates the undo operation in the drawing model.
     */
    public void undo() {
        model.undo();
    }

    /**
     * Initiates the redo operation in the drawing model.
     */
    public void redo() {
        model.redo();
    }

    /**
     * Initiates the clear operation in the drawing model.
     */
    public void clear() {
        model.clear();
    }

    /**
     * Saves the contents of a component as a PNG image file.
     *
     * @param component The component to be saved.
     * @param filePath  The file path for saving the image.
     * @return True if the image is successfully saved, false otherwise.
     */
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

    /**
     * Saves the drawing model to a file in a serialized form.
     *
     * @param path The file path for saving the serialized data.
     * @throws IOException If an I/O error occurs during serialization.
     */
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

    /**
     * Opens a drawing file and loads its contents into the drawing model.
     *
     * @param path The file path of the drawing file.
     * @throws IOException            If an I/O error occurs during file reading.
     * @throws ClassNotFoundException If the serialized data class is not found during deserialization.
     */
    public void openFromFile(String path) throws IOException, ClassNotFoundException {

        FileInputStream fileIn = new FileInputStream(path);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        SaveToFile fileRead = (SaveToFile) in.readObject();
        in.close();
        fileIn.close();
        model.loadFromFile(fileRead);

    }

    /**
     * Loads shapes from the server into the drawing model.
     *
     * @param list The list of shapes received from the server.
     */
    public void loadFromServer(List<Shape> list){
        model.loadFromServer(list);
    }
}