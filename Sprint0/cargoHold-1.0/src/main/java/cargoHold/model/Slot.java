package main.java.cargoHold.model;

public class Slot implements ISlot{
	
	private String id;
	private boolean isFree;

		// costruttore principale
		public Slot(String id, boolean isFree) {
		        this.id = id;
		        this.isFree = isFree;
		}

		// costruttore di default (imposta lo slot come libero inizialmente)
		public Slot(String id) {
		        this(id, true);
		}

		@Override
		public String getId() {
		     return this.id;
		}
		
		@Override
		public boolean getIsFree() {
			return this.isFree;
			
		}

	    @Override
		public void setIsFree(boolean v) {
		        this.isFree = v;
		}

		@Override
		public String toString() {
		        return "Slot{" +
		                "id='" + id + '\'' +
		                ", isFree=" + isFree +
		                '}';
		 }
		
}
