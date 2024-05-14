package kube;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;

import org.junit.Test;

import kube.model.Color;
import kube.model.action.move.MoveAA;
import kube.model.action.move.MoveAM;
import kube.model.action.move.MoveAW;
import kube.model.action.move.MoveMA;
import kube.model.action.move.MoveMM;
import kube.model.action.move.MoveMW;

public class MoveTest {
    
    @Test
    public void isWhiteTest() {

        // MoveMW
        MoveMW mw = new MoveMW(0, 0);
        assertTrue(mw.isWhite());

        // MoveAM
        MoveAM am = new MoveAM(0, 0, Color.RED);
        assertFalse(am.isWhite());
        
        // MoveMM
        MoveMM mm = new MoveMM(0, 0, 0, 0, Color.RED);
        assertFalse(mm.isWhite());

        // MoveAW
        MoveAW aw = new MoveAW();
        assertTrue(aw.isWhite());
    }

    @Test
    public void isClassicMoveTest() {

        // MoveMW
        MoveMW mw = new MoveMW(0, 0);
        assertFalse(mw.isClassicMove());

        // MoveAM
        MoveAM am = new MoveAM(0, 0, Color.RED);
        assertFalse(am.isClassicMove());
        
        // MoveMM
        MoveMM mm = new MoveMM(0, 0, 0, 0, Color.RED);
        assertTrue(mm.isClassicMove());

        // MoveAW
        MoveAW aw = new MoveAW();
        assertFalse(aw.isClassicMove());
    }

    @Test
    public void isFromAdditionalsTest() {

        // MoveMW
        MoveMW mw = new MoveMW(0, 0);
        assertFalse(mw.isFromAdditionals());

        // MoveAM
        MoveAM am = new MoveAM(0, 0, Color.RED);
        assertTrue(am.isFromAdditionals());
        
        // MoveMM
        MoveMM mm = new MoveMM(0, 0, 0, 0, Color.RED);
        assertFalse(mm.isFromAdditionals());

        // MoveAW
        MoveAW aw = new MoveAW();
        assertTrue(aw.isFromAdditionals());
    }

    @Test
    public void isToAdditionalsTest(){
        MoveAA aa = new MoveAA(Color.RED);
        assertTrue(aa.isToAdditionals());
        assertFalse(aa.isClassicMove());
        assertTrue(aa.isFromAdditionals());

        MoveMA ma = new MoveMA(new Point(1, 1), Color.BLACK);
        assertTrue(ma.isToAdditionals());
        assertFalse(ma.isClassicMove());
        assertFalse(ma.isFromAdditionals());
    }

}
