package javalin;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsContext;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* Server web della CargoCompany.
*
* STEP 1: serve la pagina della webgui (resources/index.html).            [fatto]
* STEP 2: endpoint WebSocket /ws verso la pagina.                          [fatto]
* STEP 3: osserva via CoAP l'attore webgui e inoltra alla pagina.          [fatto]
* STEP 4: requestToLoad dalla pagina -> webgui via TCP, reply alla pagina. [fatto]
*/
public class JavalinServer {

    private static final int PORT = 8081;
    private static final String STATIC_DIR = "resources";

    // Contesto qak in cui gira l'attore webgui
    private static final String CTX_HOST = "127.0.0.1";   // 127.0.0.1 invece di localhost: evita problemi IPv4/IPv6
    private static final int CTX_PORT = 8040;

    // Numero progressivo dei messaggi qak inviati dal server
    private static final AtomicInteger MSG_NUM = new AtomicInteger();

    // msg(ID,TYPE,SENDER,RECEIVER,PAYLOAD,NUM) -> serve per estrarre PAYLOAD
    private static final Pattern QAK_MSG = Pattern.compile("^msg\\(\\s*\\w+\\s*,\\s*\\w+\\s*,\\s*\\w+\\s*,\\s*\\w+\\s*,\\s*(.*)\\s*,\\s*\\d+\\s*\\)$");

    // Risorsa CoAP dell'attore webgui: coap://host:porta/contesto/attore
    private static final String WEBGUI_COAP_URI = "coap://localhost:8040/ctxcargocompany/webgui";

    // Pagine attualmente connesse alla WebSocket (piu' schede possono essere aperte)
    private static final Set<WsContext> CLIENTS = ConcurrentHashMap.newKeySet();

    // Tenuto come campo statico perche' l'osservazione resti attiva
    private static CoapClient coap;
    private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor();
    private static final int OBSERVE_RETRY_SECONDS = 3;

    public static void main(String[] args) {
        var app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.directory = STATIC_DIR;
                staticFiles.location = Location.EXTERNAL;
            });
            // Evita che Jetty chiuda le WebSocket ferme da un po' (default: pochi secondi di inattivita')
            config.jetty.modifyWebSocketServletFactory(factory ->
                    factory.setIdleTimeout(Duration.ofHours(1)));
        }).start(PORT);

        app.get("/", ctx -> ctx.redirect("/index.html"));

        // ---- STEP 2: WebSocket verso la pagina -----------------------------
        app.ws("/ws", ws -> {
            ws.onConnect(ctx -> {
                CLIENTS.add(ctx);
                System.out.println("Pagina connessa (" + CLIENTS.size() + ")");
            });
            ws.onClose(ctx -> {
                CLIENTS.remove(ctx);
                System.out.println("Pagina disconnessa (" + CLIENTS.size() + ")");
            });
            ws.onError(ctx -> CLIENTS.remove(ctx));
            ws.onMessage(ctx -> {
                String text = ctx.message().trim();
                if (text.equals("requestToLoad")) {
                    System.out.println("Dalla pagina: requestToLoad");
                    sendRequestToLoad(ctx);
                }
            });
        });

        // ---- STEP 3: osservazione CoAP dell'attore webgui ------------------
        startObserve();

        System.out.println("CargoCompany webgui su http://localhost:" + PORT);
    }

    // ---- STEP 3: osservazione CoAP dell'attore webgui ----------------------
    // Se fallisce (es. il sistema qak non e' ancora partito) riprova ogni pochi secondi.
    private static void startObserve() {
        if (coap != null) coap.shutdown();
        System.out.println("Osservo via CoAP " + WEBGUI_COAP_URI + " ...");

        // Californium 2.x (quella inclusa con qak): non serve nessuna inizializzazione
        coap = new CoapClient(WEBGUI_COAP_URI);
        coap.observe(new CoapHandler() {
            @Override
            public void onLoad(CoapResponse response) {
                String content = response.getResponseText();   // es. "outOfService(ofs)"
                System.out.println("Da webgui (CoAP): " + content);
                broadcast(content);
            }

            @Override
            public void onError() {
                System.err.println("Osservazione CoAP di webgui fallita: riprovo tra "
                        + OBSERVE_RETRY_SECONDS + " s");
                SCHEDULER.schedule(JavalinServer::startObserve, OBSERVE_RETRY_SECONDS, TimeUnit.SECONDS);
            }
        });
    }

    // ---- STEP 4: requestToLoad -> webgui via TCP ---------------------------
    // Gira in un thread separato: cargoservice puo' metterci diversi secondi a rispondere
    // e non vogliamo bloccare la WebSocket nel frattempo.
    private static void sendRequestToLoad(WsContext page) {
        new Thread(() -> {
            String request = "msg(requestToLoad,request,webserver,webgui,requestToLoad(NOARGS),"
                    + MSG_NUM.incrementAndGet() + ")";

            System.out.println("Connessione al contesto qak " + CTX_HOST + ":" + CTX_PORT + " ...");
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(CTX_HOST, CTX_PORT), 3000);
                System.out.println("Connesso al contesto qak");

                PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

                // Passo 9: invio della request al contesto qak (una riga terminata da \n)
                System.out.println("Invio a webgui: " + request);
                out.print(request + "\n");
                out.flush();

                // Attesa della reply sulla stessa connessione, es.
                // msg(loadAccepted,reply,webgui,webserver,loadAccepted(slot2),12)
                String reply = in.readLine();
                System.out.println("Reply da webgui: " + reply);
                if (reply == null) {
                    System.err.println("Il contesto ha chiuso la connessione senza rispondere");
                    return;
                }

                // Passo 13: alle pagine mando solo il payload, es. "loadAccepted(slot2)".
                // Uso broadcast: se la pagina si e' riconnessa nel frattempo, la risposta arriva lo stesso.
                broadcast(extractPayload(reply));

            } catch (Exception e) {
                System.err.println("Errore TCP verso " + CTX_HOST + ":" + CTX_PORT + " -> " + e);
                e.printStackTrace();
            }
        }).start();
    }

    private static String extractPayload(String qakMsg) {
        Matcher m = QAK_MSG.matcher(qakMsg.trim());
        return m.matches() ? m.group(1).trim() : qakMsg.trim();
    }

    // Invia un testo a tutte le pagine connesse
    private static void broadcast(String text) {
        for (WsContext ctx : CLIENTS) {
            if (ctx.session.isOpen()) {
                ctx.send(text);
            }
        }
    }
}