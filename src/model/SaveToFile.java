package model;

import shape.Shape;

import java.io.Serializable;
import java.util.List;



public class SaveToFile implements Serializable {

    private static final long serialVersionUID = 1L;

    List<Shape> shapeList;


    public SaveToFile(List<Shape> shapeList) {
        this.shapeList = shapeList;
    }


    public List<Shape> getShapeList() {
        return shapeList;
    }


}