package main.java.cargoHold.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import main.java.cargoHold.model.Hold;
import main.java.cargoHold.model.IHold;
import main.java.cargoHold.model.ISlot;
import main.java.cargoHold.model.Position;

public class HoldTest {

    private IHold hold;

    @Before
    public void setUp() {
        hold = new Hold();
    }

    @Test
    public void testIOPortPositionSetAndGet() {
        hold.setIOPortPosition(new Position(0, 0));
        assertEquals(new Position(0, 0), hold.getIOPortPosition());
    }

    @Test
    public void testGetSlot() {
        // slot inizializzati da 1 a 5
        ISlot s = hold.getSlot("slot1");
        assertNotNull(s);
        assertEquals("slot1", s.getId());
    }

    @Test
    public void testHasFreeSlotWhenAllFree() {
        // slot inizializzati sono liberi
        assertTrue(hold.hasFreeSlot());
    }

    @Test
    public void testHasFreeSlotWhenAllOccupied() {
        // i requisiti indicano che gli slot da riservare sono slot1-4 (slot5 è per il marker)
        for (int i = 1; i <= 4; i++) {
            hold.getSlot("slot" + i).setIsFree(false);
        }
        assertFalse(hold.hasFreeSlot());
    }

    @Test
    public void testSlotPositions() {
        // le posizioni degli slot sono inizializzate direttamente dalla Hold
        assertEquals(new Position(1, 1), hold.getSlotPosition("slot1"));
        assertEquals(new Position(1, 4), hold.getSlotPosition("slot2"));
        assertEquals(new Position(3, 1), hold.getSlotPosition("slot3"));
        assertEquals(new Position(3, 4), hold.getSlotPosition("slot4"));
        assertEquals(new Position(2, 5), hold.getSlotPosition("slot5"));
    }

    @Test
    public void testGetSlotPositionUnknownSlot() {
        assertNull(hold.getSlotPosition("slot99"));
    }

    @Test
    public void testGetFirstFreeSlot() {
        // assumiamo che la hold ritorni il primo slot libero
        hold.getSlot("slot1").setIsFree(false);
        ISlot free = hold.getFirstFreeSlot();
        assertNotNull(free);
        assertEquals("slot2", free.getId());
    }

    @Test
    public void testGetFirstFreeSlotWhenFull() {
        for (int i = 1; i <= 4; i++) {
            hold.getSlot("slot" + i).setIsFree(false);
        }
        assertNull(hold.getFirstFreeSlot());
    }

}