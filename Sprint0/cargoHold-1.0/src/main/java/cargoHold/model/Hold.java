package main.java.cargoHold.model;

import java.util.HashMap;
import java.util.Map;

public class Hold implements IHold {

    private Position homePosition, ioPortPosition;

    // Map ID_Slot -> Istanza ISlot
    private final Map<String, ISlot> slots = new HashMap<>();

    // Map ID_Slot -> Coordinate RobotSmart (X,Y)
    private final Map<String, Position> slotPositions = new HashMap<>();

    // File di persistenza locale
    private static final String STATE_FILE = "cargohold_state.txt";

    public Hold() {
        this(false); 
    }

    public Hold(boolean restoreState) {
        // 1. inizializzazione degli slot: da 1 a 5 come indicato nei requisiti (tutti liberi)
        for (int i = 1; i <= 5; i++) {
            String slotId = "slot" + i;
            slots.put(slotId, new Slot(slotId)); // stato iniziale: FREE
        }

        // 2. inizializzazione delle posizioni degli slot/componenti da requisiti (coordinate RobotSmart)
        slotPositions.put("slot1", new Position(1, 1));
        slotPositions.put("slot2", new Position(1, 4));
        slotPositions.put("slot3", new Position(3, 1));
        slotPositions.put("slot4", new Position(3, 4));
        slotPositions.put("slot5", new Position(2, 5));
        this.homePosition = new Position(0,0);
        this.ioPortPosition = new Position(4, 0);

        // 3. ripristino dello stato precedente all'avvio se restoreState è true
        if (restoreState) {
            loadState();
        } else {
            System.out.println("[Hold] Avvio pulito richiesto. Elimino lo stato precedente se esiste.");
            File file = new File(STATE_FILE);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    // HOME ====================

    @Override
    public Position getHomePosition() {
        return this.homePosition;
    }

    // IO-PORT ====================

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
        // il primo slot libero (FREE) tra slot1 e slot4
        for (int i = 1; i <= 4; i++) {
            ISlot slot = slots.get("slot" + i);
            if (slot != null && slot.getSlotStatus() == SlotStatus.FREE) {
                return slot;
            }
        }
        return null;
    }

    @Override
    public void saveState() {
        try (FileWriter writer = new FileWriter(STATE_FILE)) {
            for (Map.Entry<String, ISlot> entry : slots.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue().getSlotStatus() + "\n");
            }
            System.out.println("[Hold] Stato del magazzino salvato su " + STATE_FILE);
        } catch (Exception e) {
            System.err.println("[Hold] Errore durante il salvataggio dello stato: " + e.getMessage());
        }
    }

    private void loadState() {
        File file = new File(STATE_FILE);
        if (!file.exists()) {
            System.out.println("[Hold] Nessun file di stato precedente trovato. Avvio pulito.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String slotId = parts[0].trim();
                    String statusStr = parts[1].trim();
                    
                    ISlot slot = slots.get(slotId);
                    if (slot != null) {
                        SlotStatus status = SlotStatus.valueOf(statusStr);
                        slot.setSlotStatus(status);
                    }
                }
            }
            System.out.println("[Hold] Stato del magazzino ripristinato con successo da " + STATE_FILE);
        } catch (Exception e) {
            System.err.println("[Hold] Errore خلال il caricamento dello stato: " + e.getMessage());
        }
    }

}