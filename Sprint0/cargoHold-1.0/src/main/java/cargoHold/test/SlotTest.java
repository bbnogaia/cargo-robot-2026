package main.java.cargoHold.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import main.java.cargoHold.model.ISlot;
import main.java.cargoHold.model.Slot;

public class SlotTest {
	
	private ISlot slot;
	
	@Before
    public void setUp() {
		 slot = new Slot("slot1");
    }
	
	@Test
    public void testGetId() {
        assertEquals("slot1", slot.getId());
    }
 
    @Test
    public void testDefaultIsFree() {
        // uno slot appena creato è libero di default
        assertTrue(slot.getIsFree());
    }
 
    @Test
    public void testSetIsOccupied() {
        slot.setIsFree(false);
        assertFalse(slot.getIsFree());
    }
    
    @Test
    public void testSetIsFree() {
        slot.setIsFree(true);
        assertTrue(slot.getIsFree());
    }

}
