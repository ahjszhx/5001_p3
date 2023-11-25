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
    private Shape shape;

    public DrawingAction(ActionType actionType, Shape shape) {
        this.actionType = actionType;
        this.shape = shape;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public Shape getShape() {
        return shape;
    }
}
