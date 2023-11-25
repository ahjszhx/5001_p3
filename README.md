# README

## implemented features and how to use it;

1、Drawing straight lines
2、Drawing rectangles
3、Drawing ellipses
4、Drawing triangles
5、Undo/redo
6、Different border/line color, border/line width, or fill colors (if applicable) for each shape
7、Rotation of the shapes
8、Support for drawing squares and circles. I added square and circle buttons that lock the aspect ratio of the ellipse and rectangle when drawn
9、Select a previously drawn object and change its location, color or size
10、Use networking to sharing drawings with other users using the protocol specified below



## JUnit

For the `JUnit`, I passed all the test I designed before. showed in image.

![images](https://github.com/ZhongliangGuo/CS5001/blob/main/CS5001-p4-vector-drawing/readme%20sources/tests.png)

### `testAddVector()`

```java
@Test
public void testAddVector() {
    Assert.assertTrue(model.isUndoStackEmpty());
    Assert.assertTrue(model.isRedoStackEmpty());
    model.addVector(vector1);
    Assert.assertFalse(model.isUndoStackEmpty());
    Assert.assertTrue(model.isRedoStackEmpty());
    model.addVector(vector2);
    Assert.assertEquals(model.getVectorList().toArray().length, 2);
    Assert.assertEquals(model.getVectorList().get(0), vector1);
    Assert.assertEquals(model.getVectorList().get(1), vector2);
}
```

I test the `addVector()` method by measuring the size of `vectorList`.

### `testUndoRedoClear()`

```java
@Test
public void testUndoRedoClear() {
    model = new Model();
    model.addVector(vector1);
    model.addVector(vector2);
    model.undo();
    Assert.assertEquals(model.getVectorList().toArray().length, 1);
    Assert.assertFalse(model.isUndoStackEmpty());
    Assert.assertFalse(model.isRedoStackEmpty());
    model.undo();
    Assert.assertEquals(model.getVectorList().toArray().length, 0);
    Assert.assertTrue(model.isUndoStackEmpty());
    Assert.assertFalse(model.isRedoStackEmpty());
    model.addVector(vector2);
    Assert.assertFalse(model.isUndoStackEmpty());
    Assert.assertTrue(model.isRedoStackEmpty());
    model.clear();
    Assert.assertTrue(model.getVectorList().isEmpty());
}
```

test the redo, undo and clear function by comparing the size too

### `testSaveAndReload()`

```java
@Test
public void testSaveAndReload() {
    model = new Model();
    model.addVector(vector1);
    model.addVector(vector2);
    SaveAsFile saved = model.save();
    model.reload(saved);
    Assert.assertEquals(model.getVectorList(), saved.getVectorList());
    Assert.assertEquals(model.getRedoStack(), saved.getRedoStack());
    Assert.assertEquals(model.getUndoStack(), saved.getUndoStack());
}
```

test the save and reload function for the model by comparing the vector list, undo stack and redo stack.

## How to run it

I have built the jar file to run it. In CMD, run as follow when cd the root path of this project.

```
java -jar CS5001-p4-vector-drawing.jar
```

Or also can compile all the .java file in every folder with java file,

```
javac *.java
```

then cd at `src\main` folder, run

```
java MVCMain.class
```

