package kube.model;

// Import jackson classes
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

// Import java classes
import java.io.IOException;
import java.util.Map;

public class Serializer {

    /**********
     * KUBE SERIALIZER
     **********/

    public static class KubeSerializer extends JsonSerializer<Kube> {

        /**
         * Serialize the Kube kube to a json formatted string
         * 
         * @param kube               the Kube to serialize
         * @param jsonGenerator      the json generator
         * @param serializerProvider the serializer provider
         * @return void
         */
        @Override
        public void serialize(Kube kube, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException {

            Player[] players = { kube.getP1(), kube.getP2() };

            ModelColor[] kubeBase = new ModelColor[Kube.DEFAULT_BASE_SIZE];

            for (int i = 0; i < Kube.DEFAULT_BASE_SIZE; i++) {
                kubeBase[i] = kube.getK3().getCase(Kube.DEFAULT_BASE_SIZE - 1, i);
            }

            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField("phase", kube.getPhase());
            jsonGenerator.writeObjectField("kube_base", kubeBase);
            jsonGenerator.writeObjectField("players", players);
            jsonGenerator.writeObjectField("history", kube.getHistory());
            jsonGenerator.writeEndObject();
        }
    }

    /**********
     * PLAYER SERIALIZER
     **********/

    public static class PlayerSerializer extends JsonSerializer<Player> {

        /**
         * Serialize the player object into json format
         * 
         * @param player             the player object to serialize
         * @param jsonGenerator      the json generator
         * @param serializerProvider the serializer provider
         * @return void
         */
        @Override
        public void serialize(Player player, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException {

            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("name", player.getName());
            jsonGenerator.writeNumberField("id", player.getId());
            jsonGenerator.writeBooleanField("has_validate_building", player.getHasValidateBuilding());
            jsonGenerator.writeObjectField("initial_mountain", player.getInitialMountain());

            if (!player.getHasValidateBuilding()) {
                jsonGenerator.writeObjectField("mountain", player.getMountain());
                jsonGenerator.writeObjectFieldStart("available_to_build");
                // Serialize the availableToBuild hashmap
                for (Map.Entry<ModelColor, Integer> entry : player.getAvailableToBuild().entrySet()) {
                    jsonGenerator.writeNumberField(entry.getKey().toString(), entry.getValue());
                }
                jsonGenerator.writeEndObject();
            }

            jsonGenerator.writeEndObject();
        }
    }
}
