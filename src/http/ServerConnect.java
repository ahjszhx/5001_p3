package http;

import shape.Shape;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * The ServerConnect class handles the communication with the server for the vector drawing application.
 */
public class ServerConnect {

    private static final String SERVER_ADDRESS = "cs5001-p3.dynv6.net";

    private static final int SERVER_PORT = 8080;

    private static Socket socket;

    private static final List<Shape> serverShapes = new ArrayList<>();

    private static ResultData resultData;

    private static Receipt receipt;

    /**
     * Initializes the socket connection to the server.
     */
    public static void initializeSocket() {

        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            String loginCommand = "{\"action\": \"login\", \"data\": {\"token\": \"49468b90-9ee2-4f75-81ae-a4d228ff2268\"}}";
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(loginCommand);
            String initResponse = reader.readLine();
            System.out.println("init response=>" + initResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an addOrUpdateDrawing action to the server.
     *
     * @param serverAction The client action to be sent to the server.
     */
    public static void addOrUpdateDrawing(ClientAction serverAction) {
        String requestBody = serverAction.toJson();
        System.out.println("clientAction=>"+requestBody);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(requestBody);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(requestBody);
            String response = reader.readLine();
            if(serverAction.getAction().equals("addDrawing")){
                System.out.println("addResponse=>" + response);
                resultData = JsonParser.parseAddResult(response);
            }else {
                System.out.println("updateResponse=>" + response);
                receipt = JsonParser.parseReceipt(response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a deleteDrawing action to the server.
     *
     * @param serverId The ID of the drawing to be deleted on the server.
     */
    public static void deleteDrawing(String serverId) {
        DeleteAction deleteAction = new DeleteAction();
        deleteAction.setData(new DeleteAction.Data(serverId));
        String requestBody = deleteAction.toJson();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(requestBody);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(requestBody);
            String deleteResponse = reader.readLine();
            System.out.println("deleteResponse=>" + deleteResponse);
            receipt = JsonParser.parseReceipt(deleteResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a getDrawings action to the server and retrieves the drawing information.
     */
    public static void getDrawings() {
        try {
            String getDrawingsCommand = "{\"action\": \"getDrawings\"}";
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Send the request to the server
            writer.println(getDrawingsCommand);
            String getResponse = reader.readLine();
            System.out.println("getResponse=>: " + getResponse);
            List<DrawingInfo> drawingInfoList = JsonParser.parseInfos(getResponse);
            System.out.println("Number of drawings: " + drawingInfoList.size());
            for (DrawingInfo drawingInfo : drawingInfoList) {
                try {
                    Shape shape = drawingInfo.getShapeInstance();
                    if (shape != null) {
                        serverShapes.add(shape);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("serverShapes=>" + serverShapes.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the list of shapes received from the server.
     *
     * @return The list of shapes received from the server.
     */
    public static List<Shape> getServerShapes() {
        return serverShapes;
    }

    /**
     * Retrieves the result data from the last server operation.
     *
     * @return The result data from the last server operation.
     */
    public static ResultData getResultData() {
        return resultData;
    }

    /**
     * Retrieves the receipt from the last server operation.
     *
     * @return The receipt from the last server operation.
     */
    public static Receipt getReceipt() {
        return receipt;
    }
}
