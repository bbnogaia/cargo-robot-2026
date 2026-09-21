package main.java.cargoHold.model;

public interface IHold {
	
	// -- IO-PORT:
    // setta la posizione dell'IOPort in coordinate RobotSmart (X,Y)
    void setIOPortPosition(Position p);
    
    // restituisce la posizione dell'IOPort in coordinate RobotSmart (X,Y)
    Position getIOPortPosition();

    // -- SLOT:
    // restituisce lo slot identificato dal nome
    ISlot getSlot(String slotId);
    
    // verifica se esiste uno slot libero
    boolean hasFreeSlot();
    
    // restituisce la posizione dello slot in coordinate RobotSmart (X,Y)
    Position getSlotPosition(String slotId);
    
    // restituisce il primo slot libero
    ISlot getFirstFreeSlot();

}
