package kube;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import kube.model.History;
import kube.model.ModelColor;
import kube.model.action.move.*;

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

    @Test
    public void forSaveTest() {

        History history = new History();

        history.setFirstPlayer(1);
        MoveMW mw = new MoveMW(1, 1);
        history.addMove(mw);
        MoveMM mm = new MoveMM(2, 2, 2, 2, ModelColor.RED);
        history.addMove(mm);
        MoveAM am = new MoveAM(3, 3, ModelColor.BLUE);
        history.addMove(am);
        MoveAW aw = new MoveAW();
        history.addMove(aw);
        MoveMA ma = new MoveMA(5, 5, ModelColor.GREEN);
        history.addMove(ma);
        MoveAA aa = new MoveAA(ModelColor.YELLOW);
        history.addMove(aa);
        history.undoMove();
        history.undoMove();
        history.redoMove();

        String save = history.forSave();
        String[] lines = save.split("\n");

        assertEquals("1", lines[0]);
        assertEquals("[" + mw.forSave() + " " +
                mm.forSave() + " " +
                am.forSave() + " " +
                aw.forSave() + " " +
                ma.forSave() + "]", lines[1]);

        assertEquals("[" + aa.forSave() + "]", lines[2]);
    }

    @Test
    public void fromSaveTest() {

        History history = new History();

        history.setFirstPlayer(1);
        MoveMW mw = new MoveMW(1, 1);
        history.addMove(mw);
        MoveMM mm = new MoveMM(2, 2, 2, 2, ModelColor.RED);
        history.addMove(mm);
        MoveAM am = new MoveAM(3, 3, ModelColor.BLUE);
        history.addMove(am);
        MoveAW aw = new MoveAW();
        history.addMove(aw);
        MoveMA ma = new MoveMA(5, 5, ModelColor.GREEN);
        history.addMove(ma);
        MoveAA aa = new MoveAA(ModelColor.YELLOW);
        history.addMove(aa);
        history.undoMove();
        history.undoMove();
        history.redoMove();

        History newHistory = new History(history.forSave());

        assertEquals(history.getFirstPlayer(), newHistory.getFirstPlayer());
        assertEquals(history.getDone().size(), newHistory.getDone().size());
        for (int i = 0; i < history.getDone().size(); i++) {
            assertEquals(history.getDone().get(i).forSave(), newHistory.getDone().get(i).forSave());
        }
        assertEquals(history.getUndone().size(), newHistory.getUndone().size());
        for (int i = 0; i < history.getUndone().size(); i++) {
            assertEquals(history.getUndone().get(i).forSave(), newHistory.getUndone().get(i).forSave());
        }
    }
}
