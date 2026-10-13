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
                ProtocolType.tcp, "localhost", "8010");
    }

    @Test
    public void testRequestToLoadAccepted() throws Exception {
        String req = CommUtils.buildRequest(
                "mock",
                "requestToLoad",
                "requestToLoad",
                "cargoservice"
        ).toString();

        System.out.println("Richiesta: " + req);

        String response = conn.request(req);

        System.out.println("Risposta: " + response);

        assertTrue("La risposta deve essere loadAnswer",
                response.contains("slot"));
        assertFalse("La richiesta non dovrebbe essere rifiutata",
                response.contains("rejected"));
    }
}
