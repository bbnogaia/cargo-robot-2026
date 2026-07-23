package main.java.cargoHold.model;

import java.util.HashMap;
import java.util.Map;

public class Hold implements IHold {

	private String ioPortPosition;
    
    // Map ID_Slot -> Istanza ISlot
    private final Map<String, ISlot> slots = new HashMap<>();
    
    // Map ID_Slot -> Coordinate RobotSmart (X,Y)
    private final Map<String, String> slotPositions = new HashMap<>();

    public Hold() {
        // inizializzazione degli slot: da 1 a 5 come indicato nei requisiti
        for (int i = 1; i <= 5; i++) {
            String slotId = "slot" + i;
            slots.put(slotId, new Slot(slotId)); // liberi
        }
    }

    // IO-PORT ====================

    @Override
    public void setIOPortPosition(String p) {
        this.ioPortPosition = p;
    }

    @Override
    public String getIOPortPosition() {
        return this.ioPortPosition;
    }

    // SLOT ====================

    @Override
    public ISlot getSlot(String slotId) {
        return slots.get(slotId);
    }

    @Override
    public boolean hasFreeSlot() {
        // i requisiti indicano che gli slot da riservare sono slot1-4 (slot5 è per il marker)
        for (int i = 1; i <= 4; i++) {
            ISlot slot = slots.get("slot" + i);
            if (slot != null && slot.getIsFree()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setSlotPosition(String slotId, String p) {
        this.slotPositions.put(slotId, p);
    }

    @Override
    public String getSlotPosition(String slotId) {
        return this.slotPositions.get(slotId);
    }

    @Override
    public ISlot getFirstFreeSlot() {
        // il primo slot libero tra slot1 e slot4
        for (int i = 1; i <= 4; i++) {
            ISlot slot = slots.get("slot" + i);
            if (slot != null && slot.getIsFree()) {
                return slot;
            }
        }
        return null; 
    }

}
