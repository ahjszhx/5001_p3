import controller.DrawingController;
import model.DrawingModel;
import view.DrawingView;


/**
 * The DrawingApp class serves as the entry point for the vector drawing application.
 */
public class DrawingApp  {

    /**
     * The main method initializes the model, controller, and view components of the application.
     *
     * @param args The command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        DrawingModel drawingModel = new DrawingModel();
        DrawingController drawingController = new DrawingController(drawingModel);
        new DrawingView(drawingModel, drawingController);
    }





}
