package test;

import static org.junit.Assert.*;

import model.DrawingModel;
import model.SaveAsFile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import shape.Shape;
import shape.ShapeFactory;

import java.awt.*;

public class DrawingModelTest {

    private DrawingModel drawingModel;

    private Shape originShape1;

    private Shape updatedShape1;

    private Shape originShape2;

    @Before
    public void setUp() {
        Point startPoint1 = new Point(1, 1);
        Point endPoint1 = new Point(100, 100);
        Color borderColor1 = Color.BLACK;
        Color fillColor1 = Color.RED;
        float borderWidth1 = 2.0f;
        originShape1 = ShapeFactory.createShape("Rectangle",startPoint1,endPoint1,borderColor1,fillColor1,borderWidth1);

        Point startPoint2 = new Point(2, 2);
        Point endPoint2 = new Point(102, 102);
        Color borderColor2 = Color.BLACK;
        Color fillColor2 = Color.RED;
        float borderWidth2 = 2.0f;
        updatedShape1 = ShapeFactory.createShape("Rectangle",startPoint2,endPoint2,borderColor2,fillColor2,borderWidth2);
        updatedShape1.setInnerId(originShape1.getInnerId());


        Point startPoint3 = new Point(3, 3);
        Point endPoint3 = new Point(100, 100);
        Color borderColor3 = Color.BLACK;
        Color fillColor3 = Color.RED;
        float borderWidth3 = 2.0f;
        originShape2 = ShapeFactory.createShape("Ellipse",startPoint3,endPoint3,borderColor3,fillColor3,borderWidth3);
        drawingModel = new DrawingModel();
    }

    @Test
    public void testAddShape() {
        drawingModel.addShape(originShape1);
        assertTrue(drawingModel.getShapeList().contains(originShape1));
    }

    @Test
    public void testUpdateShape() {

        drawingModel.addShape(originShape1);

        drawingModel.updateShape(updatedShape1, originShape1);

        assertTrue(drawingModel.getShapeList().contains(updatedShape1));
        assertFalse(drawingModel.getShapeList().contains(originShape1));
    }

    @Test
    public void testUndoRedo() {


        drawingModel.addShape(originShape1);
        drawingModel.addShape(originShape2);

        // Test Undo
        drawingModel.undo();
        assertFalse(drawingModel.getShapeList().contains(originShape2));
        assertFalse(drawingModel.isUndoStackEmpty());
        assertFalse(drawingModel.isRedoStackEmpty());

        // Test Redo
        drawingModel.redo();
        assertTrue(drawingModel.getShapeList().contains(originShape2));
        assertFalse(drawingModel.isUndoStackEmpty());
        assertTrue(drawingModel.isRedoStackEmpty());
    }

    @Test
    public void testClear() {
        drawingModel.addShape(originShape1);
        drawingModel.addShape(originShape2);
        drawingModel.clear();
        assertTrue(drawingModel.getShapeList().isEmpty());
    }

    @Test
    public void testSaveAndReload() {
        drawingModel.addShape(originShape1);
        drawingModel.addShape(originShape2);
        SaveAsFile saved = drawingModel.save();
        drawingModel.loadFromFile(saved);
        Assert.assertEquals(drawingModel.getShapeList(), saved.getShapeList());
    }
}
