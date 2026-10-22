package it.unibo.test;

import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;

public class CargoHoldTest {

    private static Interaction conn;

    @BeforeClass
    public static void setup() throws Exception {
        conn = ConnectionFactory.createClientSupport23(
                ProtocolType.tcp, "localhost", "8040");
    }

    private void freeSlot(String slotId) throws Exception {
        String reset = CommUtils.buildRequest(
                "mock",
                "setSlot",
                "setSlot(" + slotId + ",free)",
                "cargohold"
        ).toString();

        conn.request(reset);
    }

    @Test
    public void testRequestSlotSuccess() throws Exception {
        String req = CommUtils.buildRequest(
                "mock",
                "requestSlot",
                "requestSlot(NOARGS)",
                "cargohold"
        ).toString();

        System.out.println("Richiesta slot: " + req);

        String response = conn.request(req);

        System.out.println("Risposta slot: " + response);

        assertTrue("La risposta deve essere slotReserved con l'ID dello slot assegnato",
                response.contains("slotReserved"));
        assertFalse("La richiesta dello slot non dovrebbe fallire se ci sono spazi liberi",
                response.contains("slotFailed"));

        String payload = response.substring(response.indexOf("slotReserved("));
        String slotId = payload.substring("slotReserved(".length(), payload.indexOf(")")).trim();
        freeSlot(slotId);
    }

    @Test
    public void testRequestPositionIOPort() throws Exception {
        String req = CommUtils.buildRequest(
                "mock",
                "requestPosition",
                "requestPosition(ioport)",
                "cargohold"
        ).toString();

        System.out.println("Richiesta posizione IOPort: " + req);

        String response = conn.request(req);

        System.out.println("Risposta posizione IOPort: " + response);

        assertTrue("La risposta deve contenere le coordinate (4,0) per la ioport",
                response.replace(" ", "").contains("slotPosition(4,0)"));
        assertFalse("La richiesta della posizione non deve fallire",
                response.contains("positionFailed"));
    }

    @Test
    public void testRequestPositionInvalid() throws Exception {
        String req = CommUtils.buildRequest(
                "mock",
                "requestPosition",
                "requestPosition(slotInesistente)",
                "cargohold"
        ).toString();

        System.out.println("Richiesta posizione non valida: " + req);

        String response = conn.request(req);

        System.out.println("Risposta posizione non valida: " + response);

        assertTrue("La risposta deve indicare positionFailed per uno slot inesistente",
                response.contains("positionFailed"));
        assertFalse("Non deve essere restituita alcuna posizione per uno slot inesistente",
                response.contains("slotPosition"));
    }

    @Test
    public void testSetSlotState() throws Exception {
        String req = CommUtils.buildRequest(
                "mock",
                "setSlot",
                "setSlot(slot1,occupied)",
                "cargohold"
        ).toString();

        System.out.println("Richiesta setSlot: " + req);

        String response = conn.request(req);

        System.out.println("Risposta setSlot: " + response);

        assertTrue("La risposta deve confermare il cambiamento dello stato con setSlotChanged",
                response.contains("setSlotChanged"));
        assertFalse("Il cambio di stato non deve fallire per uno slot valido",
                response.contains("setSlotFailed"));

        freeSlot("slot1");
    }
}