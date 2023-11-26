package model;

import shape.Shape;

import java.io.Serializable;
import java.util.List;



public class SaveAsFile implements Serializable {


    List<Shape> shapeList;


    public SaveAsFile(List<Shape> shapeList) {
        this.shapeList = shapeList;
    }


    public List<Shape> getShapeList() {
        return shapeList;
    }


}
