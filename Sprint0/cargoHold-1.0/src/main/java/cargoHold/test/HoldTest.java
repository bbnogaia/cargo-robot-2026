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
import main.java.cargoHold.model.Slot;

public class HoldTest {
	
	 private IHold hold;
	 
	    @Before
	    public void setUp() {
	        hold = new Hold();
	    }
	 
	    @Test
	    public void testIOPortPositionSetAndGet() {
	        hold.setIOPortPosition("(0,0)");
	        assertEquals("(0,0)", hold.getIOPortPosition());
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
	    public void testSetAndGetSlotPosition() {
	        hold.setSlotPosition("slot1", "(2,2)");
	        assertEquals("(2,2)", hold.getSlotPosition("slot1"));
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
