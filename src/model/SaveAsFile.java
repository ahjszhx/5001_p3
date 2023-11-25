package model;

import shape.Shape;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * A class to shift the model to a class which can be saved in files.
 * <p>
 * Because the in Model Class, here are some listeners, it is hard to change,
 * so I save the vector list, undo stack and redo stack into this class.
 * So that it can be saved and reloaded without any change for model's listeners.
 *
 * @author 210016568
 */
public class SaveAsFile implements Serializable {
    private static final long serialVersionUID = 4780759220397161457L;
    List<Shape> shapeList;
    Stack<ArrayList<Shape>> undoStack;
    Stack<ArrayList<Shape>> redoStack;


    public SaveAsFile(List<Shape> shapeList) {
        this.shapeList = shapeList;
    }


    public List<Shape> getShapeList() {
        return shapeList;
    }


    public Stack<ArrayList<Shape>> getUndoStack() {
        return undoStack;
    }


    public Stack<ArrayList<Shape>> getRedoStack() {
        return redoStack;
    }
}
