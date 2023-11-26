package http;

import shape.Shape;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerConnect {

    private static Socket socket;

    private static String initResponse = "";

    private static String addResponse = "";

    private static String getResponse = "";

    private static List<Shape> serverShapes = new ArrayList<>();

    private static ResultData resultData;

    public static void initializeSocket() {
        String serverAddress = "cs5001-p3.dynv6.net";
        int serverPort = 8080;
        try {
            socket = new Socket(serverAddress, serverPort);
            String loginCommand = "{\"action\": \"login\", \"data\": {\"token\": \"49468b90-9ee2-4f75-81ae-a4d228ff2268\"}}";
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(loginCommand);
            initResponse = reader.readLine();
            System.out.println("init response=>" + initResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addDrawing(DrawingData drawingData) {
        //DrawingData drawingData = new DrawingData(model.getShapeList().get(model.getShapeList().size() - 1));
        DrawingAction drawingAction = new DrawingAction("addDrawing", drawingData);
        String requestBody = drawingAction.toJson();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(requestBody);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(requestBody);
            addResponse = reader.readLine();
            System.out.println("addResponse=>" + addResponse);
            resultData = JsonParser.parseAddResult(addResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void getDrawings() {
        try {
            String getDrawingsCommand = "{\"action\": \"getDrawings\"}";
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Send the request to the server
            writer.println(getDrawingsCommand);
            getResponse = reader.readLine();
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

    public static String getInitResponse() {
        return initResponse;
    }

    public static void setInitResponse(String initResponse) {
        ServerConnect.initResponse = initResponse;
    }

    public static String getAddResponse() {
        return addResponse;
    }

    public static void setAddResponse(String addResponse) {
        ServerConnect.addResponse = addResponse;
    }

    public static String getGetResponse() {
        return getResponse;
    }

    public static void setGetResponse(String getResponse) {
        ServerConnect.getResponse = getResponse;
    }

    public static List<Shape> getServerShapes() {
        return serverShapes;
    }

    public static void setServerShapes(List<Shape> serverShapes) {
        ServerConnect.serverShapes = serverShapes;
    }

    public static ResultData getResultData() {
        return resultData;
    }

    public static void setResultData(ResultData resultData) {
        ServerConnect.resultData = resultData;
    }
}
