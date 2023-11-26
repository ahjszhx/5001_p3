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


    private void updateList() {
        notifier.firePropertyChange("shapeList", null, shapeList);
    }


    public void addShape(Shape shape) {
        DrawingAction action = new DrawingAction(DrawingAction.ActionType.ADD, shape);
        redoStack.clear();
        undoStack.push(action);
        shapeList.add(shape);
        updateList();
    }

    public void updateShape(Shape updatedShape, Shape prevShape) {
        DrawingAction action = new DrawingAction(DrawingAction.ActionType.UPDATE, updatedShape, prevShape);
        redoStack.clear();
        undoStack.push(action);
        for (Shape shape : shapeList) {
            if (shape.getInnerId().equals(updatedShape.getInnerId())) {
                shapeList.set(shapeList.indexOf(shape), updatedShape);
            }
        }
        updateList();
    }

    public void setShapeServerId(String serverId, String innerId) {
        for (Shape shape : shapeList) {
            if (shape.getInnerId().equals(innerId)) {
                shape.setUuid(serverId);
            }
        }
        updateList();
    }

    public void removeShapeFromServer(String serverId,String innerId) {
        int index = 0;
        for (Shape shape : shapeList) {
            if (shape.getUuid().equals(serverId)||shape.getInnerId().equals(innerId)) {
                index = shapeList.indexOf(shape);
            }
        }
        shapeList.remove(shapeList.get(index));
        updateList();
    }

    public SaveToFile save() {
        return new SaveToFile(shapeList);
    }

    public void loadFromFile(SaveToFile fileRead) {
        this.shapeList = fileRead.shapeList;
        updateList();
    }

    public void loadFromServer(List<Shape> shapeList) {
        for (Shape shape : shapeList) {
            if (shape.getStartPoint() != null && shape.getEndPoint() != null) {
                this.shapeList.add(shape);
            }
        }
        updateList();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            DrawingAction action = undoStack.pop();
            DrawingAction redoAction = action.clone();
            redoStack.push(redoAction);
            applyReverseAction(action);
            updateList();
            // Apply the reverse of the action

        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            DrawingAction action = redoStack.pop();
            undoStack.push(action);

            // Apply the action
            applyAction(action);
            updateList();
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
        updateList();
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