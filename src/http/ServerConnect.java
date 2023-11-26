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

    private static String getResponse = "";

    private static String deleteResponse = "";

    private static List<Shape> serverShapes = new ArrayList<>();

    private static ResultData resultData;

    private static Receipt receipt;

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

    public static void deleteDrawing(String serverId) {
        DeleteAction deleteAction = new DeleteAction();
        deleteAction.setData(new DeleteAction.Data(serverId));
        String requestBody = deleteAction.toJson();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(requestBody);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(requestBody);
            deleteResponse = reader.readLine();
            System.out.println("deleteResponse=>" + deleteResponse);
            receipt = JsonParser.parseReceipt(deleteResponse);
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

    public static List<Shape> getServerShapes() {
        return serverShapes;
    }

    public static ResultData getResultData() {
        return resultData;
    }

    public static Receipt getReceipt() {
        return receipt;
    }
}
