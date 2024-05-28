package kube;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import kube.model.History;

public class HistoryTest {

    @Test
    public void constructorTest() {

        History history = new History();
        assertEquals(0, history.getFirstPlayer());
        assertEquals(0, history.getDone().size());
        assertEquals(0, history.getUndone().size());
    }

    @Test
    public void addMoveTest() {

        History history = new History();
        assertEquals(0, history.getDone().size());
        history.addMove(null);
        assertEquals(1, history.getDone().size());
    }

    @Test
    public void undoMoveTest() {

        History history = new History();
        assertEquals(0, history.getDone().size());
        history.addMove(null);
        assertEquals(1, history.getDone().size());
        history.undoMove();
        assertEquals(0, history.getDone().size());
    }

    @Test
    public void redoMoveTest() {

        History history = new History();
        assertEquals(0, history.getDone().size());
        history.addMove(null);
        assertEquals(1, history.getDone().size());
        history.undoMove();
        assertEquals(0, history.getDone().size());
        history.redoMove();
        assertEquals(1, history.getDone().size());
    }

    @Test
    public void clearDonetest() {

        History history = new History();
        assertEquals(0, history.getDone().size());
        history.addMove(null);
        assertEquals(1, history.getDone().size());
        history.clearDone();
        assertEquals(0, history.getDone().size());
    }

    @Test
    public void clearUndoneTest() {

        History history = new History();
        assertEquals(0, history.getUndone().size());
        history.addMove(null);
        assertEquals(1, history.getDone().size());
        history.undoMove();
        assertEquals(0, history.getDone().size());
        assertEquals(1, history.getUndone().size());
        history.clearUndone();
        assertEquals(0, history.getUndone().size());
    }

    @Test
    public void clearTest() {

        History history = new History();
        assertEquals(0, history.getDone().size());
        assertEquals(0, history.getUndone().size());
        history.addMove(null);
        assertEquals(1, history.getDone().size());
        history.clear();
        assertEquals(0, history.getDone().size());
        assertEquals(0, history.getUndone().size());
    }

    // @Test
    // public void serializationTest() {

    //     ObjectMapper mapper = new ObjectMapper();

    //     History history = new History();

    //     MoveMW mw = new MoveMW(0, 0);
    //     history.addMove(mw);

    //     MoveAM am = new MoveAM(0, 0, ModelColor.RED);
    //     history.addMove(am);
        
    //     MoveMM mm = new MoveMM(0, 0, 0, 0, ModelColor.RED);
    //     history.addMove(mm);

    //     MoveAW aw = new MoveAW();
    //     history.addMove(aw);

    //     MoveAA aa = new MoveAA(ModelColor.RED);
    //     history.addMove(aa);

    //     MoveMA ma = new MoveMA(1, 1, ModelColor.BLACK);
    //     history.addMove(ma);

    //     history.undoMove();
    //     history.undoMove();

    //     try {
    //         String json = mapper.writeValueAsString(history);
    //         History history2 = mapper.readValue(json, History.class);

    //         assertEquals(history.getFirstPlayer(), history2.getFirstPlayer());

    //         for (int i = 0; i < history.getDone().size(); i++) {
    //             assertEquals(history.getDone().get(i), history2.getDone().get(i));
    //         }

    //         for (int i = 0; i < history.getUndone().size(); i++) {
    //             assertEquals(history.getUndone().get(i), history2.getUndone().get(i));
    //         }

    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }
}
