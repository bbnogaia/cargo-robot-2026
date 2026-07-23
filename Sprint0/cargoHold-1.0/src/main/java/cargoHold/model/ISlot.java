package main.java.cargoHold.model;

public interface ISlot {
	
	// restituisce l'id dello slot
	String getId();
	
	// restituisce se lo slot è libero
	boolean getIsFree();
	
	// setta lo slot
	void setIsFree(boolean v);

}
