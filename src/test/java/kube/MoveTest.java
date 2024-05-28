package kube;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;

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

    // @Test
    // public void serializationTest() {

    //     ObjectMapper mapper = new ObjectMapper();

    //     MoveMW mw = new MoveMW(0, 0);
    //     MoveAM am = new MoveAM(0, 0, ModelColor.RED);
    //     MoveMM mm = new MoveMM(0, 0, 0, 0, ModelColor.RED);
    //     MoveAW aw = new MoveAW();
    //     MoveAA aa = new MoveAA(ModelColor.RED);
    //     MoveMA ma = new MoveMA(new Point(1, 1), ModelColor.BLACK);

    //     try {
    //         String mwJson = mapper.writeValueAsString(mw);
    //         MoveMW mw2 = mapper.readValue(mwJson, MoveMW.class);
    //         assertEquals(mw.getColor(), mw2.getColor());
    //         assertEquals(mw.getFrom(), mw2.getFrom());

    //         String amJson = mapper.writeValueAsString(am);
    //         MoveAM am2 = mapper.readValue(amJson, MoveAM.class);
    //         assertEquals(am.getColor(), am2.getColor());
    //         assertEquals(am.getFrom(), am2.getFrom());

    //         String mmJson = mapper.writeValueAsString(mm);
    //         MoveMM mm2 = mapper.readValue(mmJson, MoveMM.class);
    //         assertEquals(mm.getColor(), mm2.getColor());
    //         assertEquals(mm.getFrom(), mm2.getFrom());
    //         assertEquals(mm.getTo(), mm2.getTo());

    //         String awJson = mapper.writeValueAsString(aw);
    //         MoveAW aw2 = mapper.readValue(awJson, MoveAW.class);
    //         assertEquals(aw.getColor(), aw2.getColor());

    //         String aaJson = mapper.writeValueAsString(aa);
    //         MoveAA aa2 = mapper.readValue(aaJson, MoveAA.class);
    //         assertEquals(aa.getColor(), aa2.getColor());

    //         String maJson = mapper.writeValueAsString(ma);
    //         MoveMA ma2 = mapper.readValue(maJson, MoveMA.class);
    //         assertEquals(ma.getColor(), ma2.getColor());
    //         assertEquals(ma.getFrom(), ma2.getFrom());
    //     } catch (Exception e) {
    //         assertTrue(false);
    //         e.printStackTrace();
    //     }
    // }
}
