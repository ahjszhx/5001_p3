package model;

import shape.Shape;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * A Serializable class representing a container for a list of Shape objects,
 * intended for saving to a file.
 */
public class SaveToFile implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    List<Shape> shapeList;

    /**
     * Constructs a SaveToFile object with the specified list of Shape objects.
     *
     * @param shapeList The list of Shape objects to be saved.
     */
    public SaveToFile(List<Shape> shapeList) {
        this.shapeList = shapeList;
    }

    /**
     * Gets the list of Shape objects stored in this SaveToFile instance.
     *
     * @return The list of Shape objects.
     */
    public List<Shape> getShapeList() {
        return shapeList;
    }


}
