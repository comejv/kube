package kube;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import kube.model.History;

public class HistoryTest {

    @Test
    public void constructorTest() {

        History history = new History();
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
}
