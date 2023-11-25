package http;

import shape.Ellipse;
import shape.Line;
import shape.Rectangle;
import shape.Shape;
import shape.Triangle;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParser {

    public static List<DrawingInfo> parseInfos(String jsonString) {
        // 替换下面的 jsonString 变量为你的 JSON 字符串
        //String jsonString = "[{\"id\":\"0aed29ae-1099-4d95-813f-bd88ad456a45\",\"created\":\"timeString\",\"modified\":\"timeString\",\"isOwner\":true,\"type\":\"rectangle\",\"x\":10,\"y\":10,\"properties\":{\"width\":10,\"height\":10,\"lineColor\":\"red\"}}]";
        System.out.println("parseBegin=>" + jsonString);
        List<DrawingInfo> drawingInfos = new ArrayList<>();
        // 使用 JsonReader 解析 JSON 字符串
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonString))) {
            JsonArray jsonArray = jsonReader.readArray();
            // 遍历 JSON 数组
            for (JsonValue jsonValue : jsonArray) {
                //System.out.println(jsonValue.getValueType());
                if (jsonValue.getValueType() == JsonValue.ValueType.OBJECT) {
                    // 处理 JSON 对象
                    JsonObject jsonObject = (JsonObject) jsonValue;

                    try {
                        // 获取对象中的属性
                        String id = jsonObject.getString("id");
                        String created = jsonObject.getString("created");
                        String modified = jsonObject.getString("modified");
                        boolean isOwner = jsonObject.getBoolean("isOwner");
                        String type = jsonObject.getString("type");
                        int x = jsonObject.getInt("x");
                        int y = jsonObject.getInt("y");

                        // 获取 properties 对象
                        JsonObject properties = jsonObject.getJsonObject("properties");
                        Map<String, Object> drawProperties = new HashMap<String, Object>();

                        if (type.equals("rectangle") || type.equals("square")) {
                            if (properties.containsKey("width")) {
                                int width = properties.getInt("width");
                                drawProperties.put("width", width);
                            }
                            if (properties.containsKey("height")) {
                                int height = properties.getInt("height");
                                drawProperties.put("height", height);
                            }
                            if (properties.containsKey("rotation")) {
                                int rotation = properties.getInt("rotation");
                                drawProperties.put("rotation", rotation);
                            }
                            if (properties.containsKey("borderColor")) {
                                String borderColor = properties.getString("borderColor");
                                drawProperties.put("borderColor", borderColor);
                            }
                            if (properties.containsKey("borderWidth")) {
                                int borderWidth = properties.getInt("borderWidth");
                                drawProperties.put("borderWidth", borderWidth);
                            }
                            if (properties.containsKey("fillColor")) {
                                String fillColor = properties.getString("fillColor");
                                drawProperties.put("fillColor", fillColor);
                            }
                        } else if (type.equals("ellipse") || type.equals("circle")) {
                            if (properties.containsKey("width")) {
                                int width = properties.getInt("width");
                                drawProperties.put("width", width);
                            }

                            if (properties.containsKey("height")) {
                                int height = properties.getInt("height");
                                drawProperties.put("height", height);
                            }

                            if (properties.containsKey("rotation")) {
                                int rotation = properties.getInt("rotation");
                                drawProperties.put("rotation", rotation);
                            }

                            if (properties.containsKey("borderColor")) {
                                String borderColor = properties.getString("borderColor");
                                drawProperties.put("borderColor", borderColor);
                            }

                            if (properties.containsKey("borderWidth")) {
                                int borderWidth = properties.getInt("borderWidth");
                                drawProperties.put("borderWidth", borderWidth);
                            }

                            if (properties.containsKey("fillColor")) {
                                String fillColor = properties.getString("fillColor");
                                drawProperties.put("fillColor", fillColor);
                            }
                        } else if (type.equals("line")) {
                            int x2, y2, lineWidth;
                            String lineColor;

                            if (properties.containsKey("x2")) {
                                System.out.println("x2=>" + properties.getInt("x2"));
                                x2 = properties.getInt("x2");
                                drawProperties.put("x2", x2);
                            }

                            if (properties.containsKey("y2")) {
                                y2 = properties.getInt("y2");
                                drawProperties.put("y2", y2);
                            }

                            if (properties.containsKey("lineColor")) {
                                lineColor = properties.getString("lineColor");
                                drawProperties.put("lineColor", lineColor);
                            }

                            if (properties.containsKey("lineWidth")) {
                                lineWidth = properties.getInt("lineWidth");
                                drawProperties.put("lineWidth", lineWidth);
                            }
                        } else if (type.equals("triangle")) {
                            if (properties.containsKey("x2")) {
                                int x2 = properties.getInt("x2");
                                drawProperties.put("x2", x2);
                            }

                            if (properties.containsKey("y2")) {
                                int y2 = properties.getInt("y2");
                                drawProperties.put("y2", y2);
                            }

                            if (properties.containsKey("x3")) {
                                int x3 = properties.getInt("x3");
                                drawProperties.put("x3", x3);
                            }

                            if (properties.containsKey("y3")) {
                                int y3 = properties.getInt("y3");
                                drawProperties.put("y3", y3);
                            }

                            if (properties.containsKey("rotation")) {
                                int rotation = properties.getInt("rotation");
                                drawProperties.put("rotation", rotation);
                            }

                            if (properties.containsKey("borderColor")) {
                                String borderColor = properties.getString("borderColor");
                                drawProperties.put("borderColor", borderColor);
                            }

                            if (properties.containsKey("borderWidth")) {
                                int borderWidth = properties.getInt("borderWidth");
                                drawProperties.put("borderWidth", borderWidth);
                            }

                            if (properties.containsKey("fillColor")) {
                                String fillColor = properties.getString("fillColor");
                                drawProperties.put("fillColor", fillColor);
                            }
                        }
                        DrawingInfo drawingInfo = new DrawingInfo(id, created, modified, isOwner, type, x, y, drawProperties);
                        drawingInfos.add(drawingInfo);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //continue;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawingInfos;
    }


    public static ResultData parseAddResult(String jsonString) {
        ResultData resultData = new ResultData();
        //String jsonString = "{\"result\":\"ok\",\"data\":{\"id\":\"c4f433b6-3175-4d0b-a733-fbdc9b192b43\"}}";

        try (JsonReader reader = Json.createReader(new StringReader(jsonString))) {
            JsonObject jsonObject = reader.readObject();

            String result = jsonObject.getString("result");
            System.out.println("Result: " + result);
            resultData.setResult(result);
            JsonObject dataObject = jsonObject.getJsonObject("data");
            String id = dataObject.getString("id");
            ResultData.Data data = new ResultData.Data();
            data.setId(id);
            resultData.setData(data);
            System.out.println("ID: " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultData;
    }

}
