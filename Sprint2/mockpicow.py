# fake_pico.py  -  pip install paho-mqtt
import sys
import threading
import time
import paho.mqtt.client as mqtt

# ---- CONFIG ----
BROKER      = "localhost"          # stesso broker del .qak (tcp://localhost:1883)
PORT        = 1883
TOPIC_SONAR = "sonar/distance"     # finto Pico -> qak
TOPIC_LED   = "led/blink"          # qak -> finto Pico
CLIENT_ID   = "pico_sonar_sim"
PERIODO_S   = 1

distanza = 5.0                     # cm, valore iniziale (modificabile da riga di comando o a runtime)
if len(sys.argv) > 1:
    distanza = float(sys.argv[1])

# ---------------- LED (solo stampa) ----------------
def on_message(client, userdata, msg):
    s = msg.payload.decode(errors="replace")
    # funziona sia con il formato qak msg(...,ledBlink(on),N) sia con il solo ledBlink(on)
    if "ledBlink(on)" in s:
        print(">> LED: lampeggio ON")
    elif "ledBlink(off)" in s:
        print(">> LED: lampeggio OFF")
    else:
        print(">> MQTT ricevuto [{}]: {}".format(msg.topic, s))

def on_connect(client, userdata, flags, rc, *args):
    print("Connesso al broker", BROKER, "- iscritto a", TOPIC_LED)
    client.subscribe(TOPIC_LED)    # ripetuto a ogni riconnessione

# ---------------- MQTT ----------------
try:
    client = mqtt.Client(mqtt.CallbackAPIVersion.VERSION2, CLIENT_ID)   # paho-mqtt 2.x
except AttributeError:
    client = mqtt.Client(CLIENT_ID)                                      # paho-mqtt 1.x

client.on_connect = on_connect
client.on_message = on_message
client.connect(BROKER, PORT, keepalive=60)
client.loop_start()                # gestisce ricezione e riconnessione in background

# ---------------- CAMBIO DISTANZA A RUNTIME ----------------
def leggi_input():
    global distanza
    while True:
        try:
            distanza = float(input())
            print("-- nuova distanza: {:.1f} cm".format(distanza))
        except ValueError:
            print("-- scrivi un numero (es. 5, 14.3, 999)")
        except EOFError:
            return

threading.Thread(target=leggi_input, daemon=True).start()
print("Invio {:.1f} cm ogni {} s. Scrivi un numero + Invio per cambiarla. Ctrl+C per fermare.".format(distanza, PERIODO_S))

# ---------------- MAIN ----------------
n = 0
try:
    while True:
        n += 1
        payload = "msg(sonarDistance,event,pico,none,distance({:.1f}),{})".format(distanza, n)
        client.publish(TOPIC_SONAR, payload)
        print(payload)
        time.sleep(PERIODO_S)
except KeyboardInterrupt:
    client.loop_stop()
    client.disconnect()
    print("Fermato.")