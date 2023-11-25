package model;

import shape.Shape;

import java.io.Serializable;

public class DrawingAction implements Serializable {

    public enum ActionType {
        ADD,
        UPDATE,
        DELETE
    }

    private ActionType actionType;
    private Shape currentShape;
    private Shape prevShape;

    public DrawingAction(ActionType actionType, Shape currentShape) {
        this.actionType = actionType;
        this.currentShape = currentShape;
    }

    public DrawingAction(ActionType actionType, Shape currentShape, Shape prevShape) {
        this.actionType = actionType;
        this.currentShape = currentShape;
        this.prevShape = prevShape;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public Shape getCurrentShape() {
        return currentShape;
    }

    public void setCurrentShape(Shape currentShape) {
        this.currentShape = currentShape;
    }

    public Shape getPrevShape() {
        return prevShape;
    }

    public void setPrevShape(Shape prevShape) {
        this.prevShape = prevShape;
    }
}
