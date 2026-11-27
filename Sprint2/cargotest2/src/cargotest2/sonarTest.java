package main.java.sprint2.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
* Test sprint 2: MQTT, sonar, led.
*
* Il test si comporta come il Pico:
*  - manda le distanze su "sonar/distance"
*  - legge i comandi del led su "led/blink"
*
* Prima di lanciarlo: broker MQTT attivo, sistema qak avviato, Pico spento.
*/
public class Sprint2Test {

    private MqttClient client;

    // messaggi ricevuti su led/blink
    private List<String> ledMessages = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        client = new MqttClient("tcp://localhost:1883", "test-sprint2");
        client.connect();
        client.subscribe("led/blink", (topic, msg) -> {
            synchronized (ledMessages) {
                ledMessages.add(new String(msg.getPayload()));
            }
        });
    }

    @After
    public void tearDown() throws Exception {
        sendDistance(15.0, 1); // distanza normale: il sonar riparte da zero
        client.disconnect();
        client.close();
    }

    /**
     * Manda la stessa distanza una volta al secondo per "seconds" secondi (come il Pico).
     * Ritorna true se nel frattempo arriva il comando led cercato (es. "ledBlink(on)").
     */
    private boolean sendDistanceAndWaitLed(double distance, int seconds, String ledCommand) throws Exception {
        synchronized (ledMessages) {
            ledMessages.clear();
        }
        for (int i = 0; i < seconds; i++) {
            String payload = "msg(sonarDistance,event,pico,none,distance(" + distance + ")," + i + ")";
            client.publish("sonar/distance", payload.getBytes(), 0, false);
            Thread.sleep(1000);
            synchronized (ledMessages) {
                for (String m : ledMessages) {
                    if (m.contains(ledCommand)) return true;
                }
            }
        }
        return false;
    }

    private void sendDistance(double distance, int seconds) throws Exception {
        sendDistanceAndWaitLed(distance, seconds, "nessun-comando");
    }

    // ---------------------------------------------------------------------

    @Test
    public void testLedSiAccendeQuandoIlCaricoEAccettato() throws Exception {
        // nessun container (15cm): la mockwebgui chiede un carico, viene accettato -> led on
        assertTrue(sendDistanceAndWaitLed(15.0, 60, "ledBlink(on)"));
    }

    @Test
    public void testDistanzaNormaleNonSpegneIlLed() throws Exception {
        assertTrue(sendDistanceAndWaitLed(15.0, 60, "ledBlink(on)"));

        // 10 secondi a 15cm: nessun evento, il led resta acceso
        assertFalse(sendDistanceAndWaitLed(15.0, 10, "ledBlink(off)"));

        // attendo il timeout dei 30s per lasciare il sistema pulito
        sendDistanceAndWaitLed(15.0, 30, "ledBlink(off)");
    }

    @Test
    public void testSonarGuastoSpegneIlLed() throws Exception {
        assertTrue(sendDistanceAndWaitLed(15.0, 60, "ledBlink(on)"));

        // 999 = il Pico non riceve eco: dopo 3s il sonar emette outOfService -> led off
        assertTrue(sendDistanceAndWaitLed(999.0, 10, "ledBlink(off)"));
    }

    @Test
    public void testContainerGiaPresenteIlLedNonSiAccende() throws Exception {
        // container sempre presente (5cm): ogni richiesta riceve retryLater -> led mai acceso
        assertFalse(sendDistanceAndWaitLed(5.0, 45, "ledBlink(on)"));
    }
}

 