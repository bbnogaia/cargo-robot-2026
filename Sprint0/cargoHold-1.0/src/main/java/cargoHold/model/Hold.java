package main.java.cargoHold.model;

import java.util.HashMap;
import java.util.Map;

public class Hold implements IHold {

    private Position ioPortPosition;

    // Map ID_Slot -> Istanza ISlot
    private final Map<String, ISlot> slots = new HashMap<>();

    // Map ID_Slot -> Coordinate RobotSmart (X,Y)
    private final Map<String, Position> slotPositions = new HashMap<>();

    public Hold() {
        // inizializzazione degli slot: da 1 a 5 come indicato nei requisiti (tutti liberi)
        for (int i = 1; i <= 5; i++) {
            String slotId = "slot" + i;
            slots.put(slotId, new Slot(slotId));
        }

        // inizializzazione delle posizioni degli slot da requisiti (coordinate RobotSmart)
        slotPositions.put("slot1", new Position(1, 1));
        slotPositions.put("slot2", new Position(1, 4));
        slotPositions.put("slot3", new Position(3, 1));
        slotPositions.put("slot4", new Position(3, 4));
        slotPositions.put("slot5", new Position(2, 5));
    }

    // IO-PORT ====================

    @Override
    public void setIOPortPosition(Position p) {
        this.ioPortPosition = p;
    }

    @Override
    public Position getIOPortPosition() {
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
        return getFirstFreeSlot() != null;
    }

    @Override
    public Position getSlotPosition(String slotId) {
        return slotPositions.get(slotId);
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