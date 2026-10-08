package main.java.cargoHold.test;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import main.java.cargoHold.model.ISlot;
import main.java.cargoHold.model.Slot;
import main.java.cargoHold.model.SlotStatus;

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
		assertEquals(SlotStatus.FREE, slot.getSlotStatus());
	}

	@Test
	public void testSetOccupied() {
		slot.setSlotStatus(SlotStatus.OCCUPIED);
		assertEquals(SlotStatus.OCCUPIED, slot.getSlotStatus());
	}

	@Test
	public void testSetReserved() {
		slot.setSlotStatus(SlotStatus.RESERVED);
		assertEquals(SlotStatus.RESERVED, slot.getSlotStatus());
	}

	@Test
	public void testSetFree() {
		slot.setSlotStatus(SlotStatus.OCCUPIED);
		slot.setSlotStatus(SlotStatus.FREE);
		assertEquals(SlotStatus.FREE, slot.getSlotStatus());
	}

	@Test
	public void testConstructorWithStatus() {
		ISlot reserved = new Slot("slot2", SlotStatus.RESERVED);
		assertEquals(SlotStatus.RESERVED, reserved.getSlotStatus());
	}

}