package kube;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

import kube.model.ModelColor;
import kube.model.action.move.*;

public class MoveTest {
    
    @Test
    public void isWhiteTest() {

        // MoveMW
        MoveMW mw = new MoveMW(0, 0);
        assertTrue(mw.isWhite());

        // MoveAM
        MoveAM am = new MoveAM(0, 0, ModelColor.RED);
        assertFalse(am.isWhite());
        
        // MoveMM
        MoveMM mm = new MoveMM(0, 0, 0, 0, ModelColor.RED);
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
        MoveAM am = new MoveAM(0, 0, ModelColor.RED);
        assertFalse(am.isClassicMove());
        
        // MoveMM
        MoveMM mm = new MoveMM(0, 0, 0, 0, ModelColor.RED);
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
        MoveAM am = new MoveAM(0, 0, ModelColor.RED);
        assertTrue(am.isFromAdditionals());
        
        // MoveMM
        MoveMM mm = new MoveMM(0, 0, 0, 0, ModelColor.RED);
        assertFalse(mm.isFromAdditionals());

        // MoveAW
        MoveAW aw = new MoveAW();
        assertTrue(aw.isFromAdditionals());
    }

    @Test
    public void isToAdditionalsTest(){
        MoveAA aa = new MoveAA(ModelColor.RED);
        assertTrue(aa.isToAdditionals());
        assertFalse(aa.isClassicMove());
        assertTrue(aa.isFromAdditionals());

        MoveMA ma = new MoveMA(new Point(1, 1), ModelColor.BLACK);
        assertTrue(ma.isToAdditionals());
        assertFalse(ma.isClassicMove());
        assertFalse(ma.isFromAdditionals());
    }

    @Test
    public void forSaveTest() {
        
        MoveMW mw = new MoveMW(0, 0);
        assertEquals("{MW;1;(0,0)}", mw.forSave());

        MoveMM mm = new MoveMM(0, 0, 0, 0, ModelColor.RED);
        assertEquals("{MM;3;(0,0);(0,0)}", mm.forSave());

        MoveAM am = new MoveAM(0, 0, ModelColor.RED);
        assertEquals("{AM;3;(0,0)}", am.forSave());

        MoveAW aw = new MoveAW();
        assertEquals("{AW}", aw.forSave());

        MoveMA ma = new MoveMA(new Point(1, 1), ModelColor.BLACK);
        assertEquals("{MA;7;(1,1)}", ma.forSave());

        MoveAA aa = new MoveAA(ModelColor.RED);
        assertEquals("{AA;3}", aa.forSave());
    }

    @Test
    public void fromSaveTest() {

        MoveMW mw = new MoveMW(0, 0);
        Move m = Move.fromSave(mw.forSave());
        assertTrue(m instanceof MoveMW);
        assertEquals(m, mw);

        MoveMM mm = new MoveMM(0, 0, 0, 0, ModelColor.RED);
        m = Move.fromSave(mm.forSave());
        assertTrue(m instanceof MoveMM);
        assertEquals(m, mm);

        MoveAM am = new MoveAM(0, 0, ModelColor.RED);
        m = Move.fromSave(am.forSave());
        assertTrue(m instanceof MoveAM);
        assertEquals(m, am);

        MoveAW aw = new MoveAW();
        m = Move.fromSave(aw.forSave());
        assertTrue(m instanceof MoveAW);
        assertEquals(m, aw);

        MoveMA ma = new MoveMA(1, 1, ModelColor.BLACK);
        m = Move.fromSave(ma.forSave());
        assertTrue(m instanceof MoveMA);
        assertEquals(m, ma);

        MoveAA aa = new MoveAA(ModelColor.RED);
        m = Move.fromSave(aa.forSave());
        assertTrue(m instanceof MoveAA);
        assertEquals(m, aa);
    }
}
