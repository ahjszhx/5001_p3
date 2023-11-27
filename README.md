# README

## implemented features and how to use it;

1、Drawing straight lines
2、Drawing rectangles
3、Drawing ellipses
4、Drawing triangles
5、Undo/redo: Including adding files, modifying file properties and other operations
6、Different border/line color, border/line width, or fill colors (if applicable) for each shape
8、Support for drawing squares and circles. I added square and circle buttons that lock the aspect ratio of the ellipse and rectangle when drawn
Operation Demonstration of features above:
![image](https://raw.githubusercontent.com/ahjszhx/5001_p3/main/basicOperations.gif)
7、Rotation of the line and rectangle/square/ellipse
Input the rotation angle, then mouse click to select the graphic, and then click the rotate button to achieve the rotation.
![image](https://raw.githubusercontent.com/ahjszhx/5001_p3/main/rotation.gif)
9、Select a previously drawn object and change its location, color or size
![image](https://raw.githubusercontent.com/ahjszhx/5001_p3/main/SaveAndLoad.gif)
Drag and drop to move the graphic, mouse click to select the graphic, you can modify the graphic colour and border attributes
![image](https://raw.githubusercontent.com/ahjszhx/5001_p3/main/changeLocationColor.gif)
10、Use networking to sharing drawings with other users using the protocol specified below
Click to update or delete a selected shapes
![image](https://raw.githubusercontent.com/ahjszhx/5001_p3/main/networking.gif)


## How to run it
1 cd into the src directory

cd ./src

2 compile all the .java file 

javac *.java

3 run program

java DrawingApp



## JUnit

### `testAddShape()` Test to see if adding the shape was successful
Test Result: Passed
@Test
public void testAddShape() {
    drawingModel.addShape(originShape1);
    assertTrue(drawingModel.getShapeList().contains(originShape1));
}


### `testUpdateShape()` Test that the graphic is updated successfully 
Test Result: Passed
```
@Test
public void testUpdateShape() {

    drawingModel.addShape(originShape1);

    drawingModel.updateShape(updatedShape1, originShape1);

    assertTrue(drawingModel.getShapeList().contains(updatedShape1));
    assertFalse(drawingModel.getShapeList().contains(originShape1));
}
```

### `testUndoRedo()` Test to see if you can undo and redo the shape after operations.
Test Result: Passed
```
@Test
public void testUndoRedo() {
    drawingModel.addShape(originShape1);
    drawingModel.addShape(originShape2);


    drawingModel.undo();
    assertFalse(drawingModel.getShapeList().contains(originShape2));
    assertFalse(drawingModel.isUndoStackEmpty());
    assertFalse(drawingModel.isRedoStackEmpty());

    drawingModel.redo();
    assertTrue(drawingModel.getShapeList().contains(originShape2));
    assertFalse(drawingModel.isUndoStackEmpty());
    assertTrue(drawingModel.isRedoStackEmpty());
}
```


### `testClear()` To test whether clearing the canvas can be executed successfully
Test Result: Passed
```
@Test
public void testClear() {
    drawingModel.addShape(originShape1);
    drawingModel.addShape(originShape2);
    drawingModel.clear();
    assertTrue(drawingModel.getShapeList().isEmpty());
}
```


### `testSaveAndReload()`  Test if reading shapes from a local file is available
Test Result: Passed
```
@Test
public void testSaveAndReload() {
    drawingModel.addShape(originShape1);
    drawingModel.addShape(originShape2);
    SaveAsFile saved = drawingModel.save();
    drawingModel.loadFromFile(saved);
    Assert.assertEquals(drawingModel.getShapeList(), saved.getShapeList());
}
```


