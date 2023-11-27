package model;

import shape.Shape;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * The DrawingModel class represents the underlying model for managing shapes and application state.
 */
public class DrawingModel {

    private Stack<DrawingAction> undoStack;
    private Stack<DrawingAction> redoStack;
    private final PropertyChangeSupport notifier;
    private List<Shape> shapeList;

    /**
     * Constructs a DrawingModel with default values.
     */
    public DrawingModel() {
        this.notifier = new PropertyChangeSupport(this);
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        this.shapeList = new ArrayList<>();
    }

    /**
     * Adds a listener for property change events.
     *
     * @param listener The listener to be added.
     */
    public void addListener(PropertyChangeListener listener) {
        notifier.addPropertyChangeListener(listener);
    }

    /**
     * Notifies listeners of a change in the shape list.
     */
    private void updateList() {
        notifier.firePropertyChange("shapeList", null, shapeList);
    }

    /**
     * Adds a shape to the drawing model and creates an appropriate undo action.
     *
     * @param shape The shape to be added.
     */
    public void addShape(Shape shape) {
        DrawingAction action = new DrawingAction(DrawingAction.ActionType.ADD, shape);
        redoStack.clear();
        undoStack.push(action);
        shapeList.add(shape);
        updateList();
    }

    /**
     * Updates a shape in the drawing model and creates an appropriate undo action.
     *
     * @param updatedShape The updated shape.
     * @param prevShape    The previous state of the shape.
     */
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

    /**
     * Sets the server ID for a shape in the drawing model.
     *
     * @param serverId The server ID to be set.
     * @param innerId  The inner ID of the shape.
     */
    public void setShapeServerId(String serverId, String innerId) {
        for (Shape shape : shapeList) {
            if (shape.getInnerId().equals(innerId)) {
                shape.setUuid(serverId);
            }
        }
        updateList();
    }

    /**
     * Removes a shape from the drawing model based on its server ID or inner ID.
     *
     * @param serverId The server ID of the shape.
     * @param innerId  The inner ID of the shape.
     */
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

    /**
     * Creates a SaveToFile object representing the current state of the drawing model.
     *
     * @return The SaveToFile object.
     */
    public SaveToFile save() {
        return new SaveToFile(shapeList);
    }

    /**
     * Loads shapes from a SaveToFile object into the drawing model.
     *
     * @param fileRead The SaveToFile object containing shape data.
     */
    public void loadFromFile(SaveToFile fileRead) {
        this.shapeList = fileRead.shapeList;
        updateList();
    }

    /**
     * Loads shapes from the server into the drawing model.
     *
     * @param shapeList The list of shapes received from the server.
     */
    public void loadFromServer(List<Shape> shapeList) {
        for (Shape shape : shapeList) {
            if (shape.getStartPoint() != null && shape.getEndPoint() != null) {
                this.shapeList.add(shape);
            }
        }
        updateList();
    }

    /**
     * Initiates the undo operation in the drawing model.
     */
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

    /**
     * Initiates the redo operation in the drawing model.
     */
    public void redo() {
        if (!redoStack.isEmpty()) {
            DrawingAction action = redoStack.pop();
            undoStack.push(action);

            // Apply the action
            applyAction(action);
            updateList();
        }
    }

    /**
     * Checks if the undo stack is empty.
     *
     * @return True if the undo stack is empty, false otherwise.
     */
    public boolean isUndoStackEmpty() {
        return undoStack.empty();
    }

    /**
     * Checks if the redo stack is empty.
     *
     * @return True if the redo stack is empty, false otherwise.
     */
    public boolean isRedoStackEmpty() {
        return redoStack.empty();
    }

    /**
     * Clears all shapes from the drawing model.
     */
    public void clear() {
        undoStack.clear();
        redoStack.clear();
        shapeList.clear();
        updateList();
    }

    /**
     * Gets the list of shapes in the drawing model.
     *
     * @return The list of shapes.
     */
    public List<Shape> getShapeList() {
        return shapeList;
    }

    /**
     * Applies a drawing action to the drawing model.
     *
     * @param action The drawing action to be applied.
     */
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
                        realShape.setRotation(updatedShape.getRotation());
                    }
                }
                break;
            // Handle DELETE case if needed
        }
    }

    /**
     * Applies the reverse of a drawing action to the drawing model.
     *
     * @param action The drawing action to be reversed.
     */
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
                        realShape.setRotation(originalShape.getRotation());
                    }
                }
                break;
        }
    }
}