package kube.model;

// Import model class
import kube.model.action.move.Move;

// Import jackson classes
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

// Import java classes
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Deserializer {

    public static class KubeDeserializer extends JsonDeserializer<Kube> {

        /**
         * Deserialize the json formatted string to a Kube
         * 
         * @param jsonParser             the json parser
         * @param deserializationContext the deserialization context
         * @return the Kube kube
         */
        @Override
        public Kube deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {

            Kube kube = new Kube(true);

            while (!jsonParser.isClosed()) {
                JsonToken jsonToken = jsonParser.nextToken();

                if (JsonToken.FIELD_NAME.equals(jsonToken)) {

                    String fieldName = jsonParser.currentName();
                    jsonParser.nextToken();

                    switch (fieldName) {
                        case "phase":
                            kube.setPhase(jsonParser.getValueAsInt());
                            break;
                        case "kube_base":
                            // Convert the json formatted string to a ModelColor array
                            TypeReference<ModelColor[]> typeReference = new TypeReference<ModelColor[]>() {
                            };
                            ModelColor[] kubeBase = jsonParser.readValueAs(typeReference);
                            kube.setK3(new Mountain(kubeBase.length));
                            // Filling the base of the kube
                            for (int i = 0; i < kubeBase.length; i++) {
                                kube.getK3().setCase(kubeBase.length - 1, i, kubeBase[i]);
                            }
                            break;
                        case "players":
                            // Convert the json formatted string to a Player array
                            ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
                            JsonNode playersNode = mapper.readTree(jsonParser);
                            String playersJson = playersNode.toString();
                            // Split the json formatted string to get the two players
                            playersJson = playersJson.substring(1, playersJson.length() - 1);
                            String[] playerString = playersJson.split("\\},\\{");
                            playerString[0] = playerString[0] + "}";
                            playerString[1] = "{" + playerString[1];
                            // Filling the players of the kube
                            kube.setP1(mapper.readValue(playerString[0], Player.class));
                            kube.setP2(mapper.readValue(playerString[1], Player.class));
                            break;
                        case "history":
                            kube.setHistory(jsonParser.readValueAs(History.class));
                            // Setting the current player of the kube
                            if (kube.getHistory().getFirstPlayer() == kube.getP1().getId()) {
                                kube.setCurrentPlayer(kube.getP1());
                            } else {
                                kube.setCurrentPlayer(kube.getP2());
                            }
                            // Replaying the history
                            for (Move move : kube.getHistory().getDone()) {
                                kube.playMoveWithoutHistory(move);
                            }
                            break;
                    }
                }
            }
            return kube;
        }
    }

    public static class PlayerDeserializer extends JsonDeserializer<Player> {

        /**
         * Deserialize the player object from json format
         * 
         * @param jsonParser             the json parser
         * @param deserializationContext the deserialization context
         * @return the player object
         */
        @Override
        public Player deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {

            Player player = new Player(0);

            while (!jsonParser.isClosed()) {
                JsonToken jsonToken = jsonParser.nextToken();

                if (JsonToken.FIELD_NAME.equals(jsonToken)) {
                    String fieldName = jsonParser.currentName();
                    jsonParser.nextToken();

                    switch (fieldName) {
                        case "name":
                            player.setName(jsonParser.getValueAsString());
                            break;
                        case "id":
                            player.setId(jsonParser.getValueAsInt());
                            break;
                        case "has_validate_building":
                            // Deserialize the hasValidateBuilding attribute
                            player.setHasValidateBuilding(jsonParser.getValueAsBoolean());
                            if (player.getHasValidateBuilding()) {
                                player.initAvailableToBuild();
                            }
                            break;
                        case "initial_mountain":
                            // Deserialize the initialMountain attribute
                            player.setInitialMountain(jsonParser.readValueAs(Mountain.class));
                            if (player.getHasValidateBuilding()) {
                                player.setMountain(player.getInitialMountain().clone());
                            }
                            break;
                        case "mountain":
                            // Deserialize the mountain attribute
                            player.setMountain(jsonParser.readValueAs(Mountain.class));
                            break;
                        case "available_to_build":
                            // Convert the json object to a hashmap
                            TypeReference<HashMap<ModelColor, Integer>> typeRef = new TypeReference<HashMap<ModelColor, Integer>>() {
                            };
                            HashMap<ModelColor, Integer> availableToBuild = jsonParser.readValueAs(typeRef);
                            player.setAvailableToBuild(new HashMap<>());
                            // Filling the player availableToBuild hashmap
                            for (Map.Entry<ModelColor, Integer> entry : availableToBuild.entrySet()) {
                                player.getAvailableToBuild().put(entry.getKey(), entry.getValue());
                            }
                            break;
                    }
                }
            }
            return player;
        }
    }
}
