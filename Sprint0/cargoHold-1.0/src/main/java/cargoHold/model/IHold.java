package main.java.cargoHold.model;

public interface IHold {
	
	// -- IO-PORT:
    // setta la posizione dell'IOPort in coordinate RobotSmart (X,Y)
    void setIOPortPosition(String p);
    
    // restituisce la posizione dell'IOPort in coordinate RobotSmart (X,Y)
    String getIOPortPosition();

    // -- SLOT:
    // restituisce lo slot identificato dal nome
    ISlot getSlot(String slotId);
    
    // verifica se esiste uno slot libero
    boolean hasFreeSlot();
    
    // setta la posizione di uno slot in coordinate RobotSmart (X,Y)
    void setSlotPosition(String slotId, String p);
    
    // restituisce la posizione dello slot in coordinate RobotSmart (X,Y)
    String getSlotPosition(String slotId);
    
    // restituisce il primo slot libero
    ISlot getFirstFreeSlot();

}
