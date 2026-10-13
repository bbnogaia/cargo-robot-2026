package it.unibo.test;

import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;

public class CargoServiceTest {

    private static Interaction conn;

    @BeforeClass
    public static void setup() throws Exception {
        conn = ConnectionFactory.createClientSupport23(
                ProtocolType.tcp, "localhost", "8040");
    }

    @Test
    public void testRequestToLoadAccepted() throws Exception {
        String req = CommUtils.buildRequest(
                "mock",
                "requestToLoad",
                "requestToLoad(NOARGS)",
                "cargoservice"
        ).toString();

        System.out.println("Richiesta: " + req);

        String response = conn.request(req);

        System.out.println("Risposta: " + response);

        assertTrue("La risposta deve specificare uno slot",
                response.contains("slot"));
        assertFalse("La richiesta non dovrebbe essere rifiutata",
                response.contains("rejected"));
    }
    
    @Test
    public void testRequestToLoadRejected() throws Exception {
        String req = CommUtils.buildRequest(
                "mock",
                "requestToLoad",
                "requestToLoad(NOARGS)",
                "cargoservice"
        ).toString();

        System.out.println("Richiesta: " + req);

        String response = conn.request(req);

        System.out.println("Risposta: " + response);

        assertTrue("La risposta deve essere rifiutata",
                response.contains("rejected"));
        assertFalse("La richiesta non dovrebbe specificare uno slot",
                response.contains("slot"));
    }
}
