package http;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.StringWriter;

public class DeleteAction {

    private final String action = "deleteDrawing";
    private DeleteAction.Data data;

    public DeleteAction.Data getData() {
        return data;
    }

    public void setData(DeleteAction.Data data) {
        this.data = data;
    }

    public static class Data {
        private String id;

        public Data(String serverId) {
            this.id = serverId;
        }

        public JsonObject toJson() {
            JsonObjectBuilder builder = Json.createObjectBuilder()
                    .add("id", id);

            return builder.build();
        }
    }

    public String toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder()
                .add("action", action)
                .add("data", data.toJson());

        StringWriter stringWriter = new StringWriter();
        try (javax.json.JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.writeObject(builder.build());
        }
        return stringWriter.toString();
    }

}
