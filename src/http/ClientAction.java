package http;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.io.StringWriter;

public class ClientAction {

    private String action;
    private DrawingData data;

    public ClientAction(String action, DrawingData data) {
        this.action = action;
        this.data = data;
    }

    public String toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder()
                .add("action", action);

        if (data != null) {
            builder.add("data", data.toJson());
        }
        StringWriter stringWriter = new StringWriter();
        try (javax.json.JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.writeObject(builder.build());
        }
        return stringWriter.toString();
    }

    public String getAction() {
        return action;
    }
}
