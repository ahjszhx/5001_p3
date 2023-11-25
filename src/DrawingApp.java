import controller.DrawingController;
import model.DrawingModel;
import view.DrawingView;



public class DrawingApp  {




    public static void main(String[] args) {
        DrawingModel drawingModel = new DrawingModel();
        DrawingController drawingController = new DrawingController(drawingModel);
        new DrawingView(drawingModel, drawingController);
        //initializeSocket();
    }





}
