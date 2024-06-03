package kube;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import kube.model.*;

public class PlayerTest {

    @Test
    public void addToAdditionalsTest() {

        Player player = new Player(1);
        player.setIsMountainValidated(true);
        player.addToAdditionals(ModelColor.RED);
        player.addToAdditionals(ModelColor.BLUE);
        player.addToAdditionals(ModelColor.GREEN);

        assertEquals(3, player.getAdditionals().size());

        assertEquals(ModelColor.RED, player.getAdditionals().get(0));
        assertEquals(ModelColor.BLUE, player.getAdditionals().get(1));
        assertEquals(ModelColor.GREEN, player.getAdditionals().get(2));
    }

    @Test
    public void removeFromAdditionalsTest() {

        Player player = new Player(1);
        player.setIsMountainValidated(true);
        player.addToAdditionals(ModelColor.RED);
        player.addToAdditionals(ModelColor.BLUE);
        player.addToAdditionals(ModelColor.GREEN);

        assertEquals(ModelColor.BLUE, player.removeFromAdditionals(1));
        assertEquals(ModelColor.GREEN, player.removeFromAdditionals(1));

        assertEquals(1, player.getAdditionals().size());

        assertEquals(ModelColor.RED, player.removeFromAdditionals(0));
        assertEquals(0, player.getAdditionals().size());
    }

    @Test
    public void addToMountainFromAvailableToBuildTest() {

        Kube kube = new Kube();
        kube.setCurrentPlayer(kube.getP1());
        Player player = kube.getCurrentPlayer();

        kube.fillBag(0);
        kube.distributeCubesToPlayers();
        // Distribution for player one: {RED=4, BLUE=2, WHITE=2, YELLOW=7, BLACK=3,
        // NATURAL=2, GREEN=1}

        assertTrue(player.addToMountainFromAvailableToBuild(0, 0, ModelColor.RED));
        assertEquals(ModelColor.RED, player.getMountain().getCase(0, 0));
        assertEquals(3, player.getAvailableToBuild().get(ModelColor.RED).intValue());

        assertTrue(player.addToMountainFromAvailableToBuild(1, 0, ModelColor.RED));
        assertEquals(ModelColor.RED, player.getMountain().getCase(1, 0));
        assertEquals(2, player.getAvailableToBuild().get(ModelColor.RED).intValue());

        assertTrue(player.addToMountainFromAvailableToBuild(1, 1, ModelColor.RED));
        assertEquals(ModelColor.RED, player.getMountain().getCase(1, 1));
        assertEquals(1, player.getAvailableToBuild().get(ModelColor.RED).intValue());

        assertTrue(player.addToMountainFromAvailableToBuild(2, 0, ModelColor.RED));
        assertEquals(ModelColor.RED, player.getMountain().getCase(2, 0));
        assertEquals(0, player.getAvailableToBuild().get(ModelColor.RED).intValue());

        assertFalse(player.addToMountainFromAvailableToBuild(2, 1, ModelColor.RED));
        assertEquals(ModelColor.EMPTY, player.getMountain().getCase(2, 1));
        assertEquals(0, player.getAvailableToBuild().get(ModelColor.RED).intValue());

        assertTrue(player.addToMountainFromAvailableToBuild(0, 0, ModelColor.BLUE));
        assertEquals(ModelColor.BLUE, player.getMountain().getCase(0, 0));
        assertEquals(1, player.getAvailableToBuild().get(ModelColor.BLUE).intValue());
        assertEquals(1, player.getAvailableToBuild().get(ModelColor.RED).intValue());
    }

    @Test
    public void removeFromMountainTest() {

        Kube kube = new Kube();
        kube.setCurrentPlayer(kube.getP1());
        Player player = kube.getCurrentPlayer();

        kube.fillBag(0);
        kube.distributeCubesToPlayers();
        // Distribution for player one: {RED=4, BLUE=2, WHITE=2, YELLOW=7, BLACK=3,
        // NATURAL=2, GREEN=1}

        player.addToMountainFromAvailableToBuild(0, 0, ModelColor.RED);
        player.addToMountainFromAvailableToBuild(1, 0, ModelColor.RED);
        player.addToMountainFromAvailableToBuild(1, 1, ModelColor.RED);
        player.addToMountainFromAvailableToBuild(2, 0, ModelColor.RED);

        player.setIsMountainValidated(true);

        assertEquals(ModelColor.RED, player.removeFromMountain(0, 0));
        assertEquals(ModelColor.EMPTY, player.getMountain().getCase(0, 0));

        assertEquals(ModelColor.RED, player.removeFromMountain(1, 0));
        assertEquals(ModelColor.EMPTY, player.getMountain().getCase(1, 0));

        assertEquals(ModelColor.RED, player.removeFromMountain(1, 1));
        assertEquals(ModelColor.EMPTY, player.getMountain().getCase(1, 1));

        assertEquals(ModelColor.RED, player.removeFromMountain(2, 0));
        assertEquals(ModelColor.EMPTY, player.getMountain().getCase(2, 0));

        assertEquals(ModelColor.EMPTY, player.removeFromMountain(2, 1));
        assertEquals(ModelColor.EMPTY, player.getMountain().getCase(2, 1));
    }

    @Test
    public void removeFromMountainToAvailableToBuildTest() {

        Kube kube = new Kube();
        kube.setCurrentPlayer(kube.getP1());
        Player player = kube.getCurrentPlayer();

        kube.fillBag(0);
        kube.distributeCubesToPlayers();
        // Distribution for player one: {RED=4, BLUE=2, WHITE=2, YELLOW=7, BLACK=3,
        // NATURAL=2, GREEN=1}

        player.addToMountainFromAvailableToBuild(0, 0, ModelColor.RED);
        player.addToMountainFromAvailableToBuild(1, 0, ModelColor.RED);
        player.addToMountainFromAvailableToBuild(1, 1, ModelColor.RED);
        player.addToMountainFromAvailableToBuild(2, 0, ModelColor.RED);

        player.removeFromMountainToAvailableToBuild(0, 0);
        assertEquals(ModelColor.EMPTY, player.getMountain().getCase(0, 0));
        assertEquals(1, player.getAvailableToBuild().get(ModelColor.RED).intValue());

        player.removeFromMountainToAvailableToBuild(1, 0);
        assertEquals(ModelColor.EMPTY, player.getMountain().getCase(1, 0));
        assertEquals(2, player.getAvailableToBuild().get(ModelColor.RED).intValue());

        player.removeFromMountainToAvailableToBuild(1, 1);
        assertEquals(ModelColor.EMPTY, player.getMountain().getCase(1, 1));
        assertEquals(3, player.getAvailableToBuild().get(ModelColor.RED).intValue());

        player.removeFromMountainToAvailableToBuild(2, 0);
        assertEquals(ModelColor.EMPTY, player.getMountain().getCase(2, 0));
        assertEquals(4, player.getAvailableToBuild().get(ModelColor.RED).intValue());

        player.removeFromMountainToAvailableToBuild(2, 1);
        assertEquals(ModelColor.EMPTY, player.getMountain().getCase(2, 1));
        assertEquals(4, player.getAvailableToBuild().get(ModelColor.RED).intValue());
    }

    // @Test
    // public void serializationTest() {

    // ObjectMapper mapper = new ObjectMapper();

    // Player player = new Player(1);

    // player.setName("roger");

    // player.getMountain().setCase(1, 0, ModelColor.BLUE);
    // player.getMountain().setCase(1, 1, ModelColor.WHITE);
    // player.getMountain().setCase(2, 0, ModelColor.YELLOW);
    // player.getMountain().setCase(2, 1, ModelColor.WHITE);
    // player.getMountain().setCase(2, 2, ModelColor.BLUE);

    // player.setInitialMountain(player.getMountain().clone());

    // HashMap<ModelColor, Integer> cubes = new HashMap<>();

    // cubes.put(ModelColor.RED, 4);
    // cubes.put(ModelColor.BLUE, 3);
    // cubes.put(ModelColor.GREEN, 2);
    // cubes.put(ModelColor.YELLOW, 1);

    // player.setAvailableToBuild(cubes);

    // try {
    // String playerJson = mapper.writeValueAsString(player);
    // Player player2 = mapper.readValue(playerJson, Player.class);

    // assertEquals(player.getName(), player2.getName());
    // assertEquals(player.getId(), player2.getId());
    // assertEquals(player.getHasValidateBuilding(),
    // player2.getHasValidateBuilding());
    // assertTrue(areSameMountain(player.getInitialMountain(),
    // player2.getInitialMountain()));
    // assertTrue(areSameMountain(player.getMountain(), player2.getMountain()));

    // for (ModelColor color : player.getAvailableToBuild().keySet()) {
    // assertEquals(player.getAvailableToBuild().get(color),
    // player2.getAvailableToBuild().get(color));
    // }

    // } catch (JsonProcessingException e) {
    // e.printStackTrace();
    // }
    // }
    //
    // private boolean areSameMountain(Mountain m1, Mountain m2) {
    // if (m1.getBaseSize() != m2.getBaseSize()) {
    // return false;
    // }
    // for (int i = 0; i < m1.getBaseSize(); i++) {
    // for (int j = 0; j < m1.getBaseSize(); j++) {
    // if (m1.getCase(i, j) != m2.getCase(i, j)) {
    // return false;
    // }
    // }
    // }
    // return true;
    // }
}
