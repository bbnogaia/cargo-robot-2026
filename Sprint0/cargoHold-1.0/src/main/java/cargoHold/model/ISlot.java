package main.java.cargoHold.model;

public interface ISlot {
	
	// restituisce l'id dello slot
    String getId();

    // restituisce lo stato dello slot
    SlotStatus getSlotStatus();

    // imposta lo stato dello slot
    void setSlotStatus(SlotStatus status);

}
