package kube;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;

import kube.model.Kube;


public class KubeTest {

    @Test
    public void testKube() {
        Kube kube = new Kube();
        assertEquals(6, kube.getP1().getMountain().getBaseSize());
        assertEquals(6, kube.getP2().getMountain().getBaseSize());
        assertEquals(9, kube.getK3().getBaseSize());
        assertEquals(0, kube.getHistoric().getDone().size());
        assertEquals(0, kube.getHistoric().getUndone().size());
        assertEquals(0, kube.getHistoric().getFirstPlayer());
        assertEquals(1, kube.getPhase());
        

    }
    
}
