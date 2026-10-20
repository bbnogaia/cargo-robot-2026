package main.java.cargoHold.model;

public class Slot implements ISlot {

	private String id;
	private SlotStatus status;

	// costruttore principale
	public Slot(String id, SlotStatus status) {
		this.id = id;
		this.status = status;
	}

	// costruttore di default (lo slot è inizialmente libero)
	public Slot(String id) {
		this(id, SlotStatus.FREE);
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public SlotStatus getSlotStatus() {
		return this.status;
	}

	@Override
	public void setSlotStatus(SlotStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Slot{" +
				"id='" + id + '\'' +
				", status=" + status +
				'}';
	}
}