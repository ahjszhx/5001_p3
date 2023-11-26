package model;

import shape.Shape;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DrawingModel {

    private Stack<DrawingAction> undoStack;
    private Stack<DrawingAction> redoStack;
    private final PropertyChangeSupport notifier;
    private List<Shape> shapeList;


    public DrawingModel() {
        this.notifier = new PropertyChangeSupport(this);
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        this.shapeList = new ArrayList<>();
    }


    public void addListener(PropertyChangeListener listener) {
        notifier.addPropertyChangeListener(listener);
    }


    private void update() {
        notifier.firePropertyChange("shapeList", null, shapeList);
    }


    public void addShape(Shape shape) {
        DrawingAction action = new DrawingAction(DrawingAction.ActionType.ADD, shape);
        redoStack.clear();
        undoStack.push(action);
        shapeList.add(shape);
        update();
    }


    public void updateShape(Shape updatedShape, Shape prevShape) {
        DrawingAction action = new DrawingAction(DrawingAction.ActionType.UPDATE, updatedShape,prevShape);
        redoStack.clear();
        undoStack.push(action);
        for (Shape shape : shapeList) {
            if(shape.getInnerId().equals(updatedShape.getInnerId())){
                shapeList.set(shapeList.indexOf(shape),updatedShape);
            }
        }
        update();
    }

    public SaveAsFile save() {
        return new SaveAsFile(shapeList);
    }

    public void loadFromFile(SaveAsFile fileRead) {
        this.shapeList = fileRead.shapeList;
        update();
    }

    public void loadFromServer(List<Shape> shapeList) {
        for (Shape shape : shapeList) {
            if (shape.getStartPoint() != null && shape.getEndPoint() != null) {
                this.shapeList.add(shape);
            }
        }
        update();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            DrawingAction action = undoStack.pop();
            DrawingAction redoAction = action.clone();
            redoStack.push(redoAction);
            applyReverseAction(action);
            update();
            // Apply the reverse of the action

        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            DrawingAction action = redoStack.pop();
            undoStack.push(action);

            // Apply the action
            applyAction(action);
            update();
        }
    }

    public boolean isUndoStackEmpty() {
        return undoStack.empty();
    }


    public boolean isRedoStackEmpty() {
        return redoStack.empty();
    }



    public void clear() {
        shapeList.clear();
        update();
    }

    public List<Shape> getShapeList() {
        return shapeList;
    }

    private void applyAction(DrawingAction action) {
        switch (action.getActionType()) {
            case ADD:
                shapeList.add(action.getCurrentShape());
                break;
            case UPDATE:
                // Update the shape in the list
                Shape updatedShape = action.getCurrentShape();
                for (Shape realShape : shapeList) {
                    if (updatedShape.getInnerId().equals(realShape.getInnerId())) {
                        realShape.setStartPoint(updatedShape.getStartPoint());
                        realShape.setEndPoint(updatedShape.getEndPoint());
                        realShape.setFillColorModel(updatedShape.getFillColorModel());
                        realShape.setBorderColorModel(updatedShape.getBorderColorModel());
                        realShape.setBorderWidth(updatedShape.getBorderWidth());
                    }
                }
                break;
            // Handle DELETE case if needed
        }
    }

    private void applyReverseAction(DrawingAction action) {
        switch (action.getActionType()) {
            case ADD:
                shapeList.remove(action.getCurrentShape());
                break;
            case UPDATE:
                // Reverse the update operation
                Shape originalShape = action.getPrevShape();
                for (Shape realShape : shapeList) {
                    if (originalShape.getInnerId().equals(realShape.getInnerId())) {
                        realShape.setStartPoint(originalShape.getStartPoint());
                        realShape.setEndPoint(originalShape.getEndPoint());
                        realShape.setFillColorModel(originalShape.getFillColorModel());
                        realShape.setBorderColorModel(originalShape.getBorderColorModel());
                        realShape.setBorderWidth(originalShape.getBorderWidth());
                    }
                }
                break;
            // Handle DELETE case if needed
        }
    }
}