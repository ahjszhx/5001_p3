package http;

import shape.Shape;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Locale;

public class DrawingData {

    private String id;
    private String type;
    private int x;
    private int y;
    private Shape properties;

    public DrawingData(Shape shape) {
        if (shape.getUuid() != null) {
            this.id = shape.getUuid();
        }
        this.type = shape.getClass().getSimpleName().toLowerCase(Locale.ROOT);
        this.x = shape.getStartPoint().x;
        this.y = shape.getStartPoint().y;
        this.properties = shape;
    }


    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        if (id != null) {
            builder.add("id", id);
        }
        if (type != null) {
            builder.add("type", type);
        }
        if (x != 0) {
            builder.add("x", x);
        }
        if (y != 0) {
            builder.add("y", y);
        }
        if (properties != null) {
            builder.add("properties", properties.toJson());
        }

        return builder.build();
    }

}
