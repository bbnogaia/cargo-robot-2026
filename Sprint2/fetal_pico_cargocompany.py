from machine import Pin, time_pulse_us, Timer
import network, time
from umqtt.simple import MQTTClient

# ---- CONFIG ----
WIFI_SSID = "FASTWEB-UVU6Vk"
WIFI_PASS = "s23Eht4qQ3"
BROKER    = "192.168.1.178"
TOPIC_SONAR = b"sonar/distance"   # Pico -> qak
TOPIC_LED   = b"led/blink"        # qak  -> Pico
CLIENT_ID = b"pico_sonar"
PERIODO_S = 1
N_MISURE  = 5
BLINK_MS  = 300                   # velocita' lampeggio
DEBUG     = False

trig = Pin(15, Pin.OUT)
echo = Pin(14, Pin.IN)
led  = Pin("LED", Pin.OUT)
trig.low()

# ---------------- LED ----------------
blink_timer = Timer()
blinking = False

def _toggle(t):
    led.toggle()

def led_blink_on():
    global blinking
    if not blinking:
        blink_timer.init(period=BLINK_MS, mode=Timer.PERIODIC, callback=_toggle)
        blinking = True
        print(">> LED: lampeggio ON")

def led_blink_off():
    global blinking
    blink_timer.deinit()
    led.off()
    if blinking:
        print(">> LED: lampeggio OFF")
    blinking = False

def on_msg(topic, msg):
    s = msg.decode()
    if DEBUG:
        print("MQTT ricevuto [{}]: {}".format(topic.decode(), s))
    # funziona sia con il formato qak msg(...,ledBlink(on),N) sia con il solo ledBlink(on)
    if "ledBlink(on)" in s:
        led_blink_on()
    elif "ledBlink(off)" in s:
        led_blink_off()

# ---------------- WIFI / MQTT ----------------
def connetti_wifi():
    wlan = network.WLAN(network.STA_IF)
    wlan.active(True)
    wlan.connect(WIFI_SSID, WIFI_PASS)
    print("Connessione Wi-Fi...")
    while not wlan.isconnected():
        led.toggle()
        time.sleep(0.3)
    led.off()
    print("Wi-Fi OK, IP:", wlan.ifconfig()[0])

def connetti_mqtt():
    c = MQTTClient(CLIENT_ID, BROKER, port=1883, keepalive=60)
    c.set_callback(on_msg)
    c.connect()
    c.subscribe(TOPIC_LED)
    print("Connesso al broker", BROKER, "- iscritto a", TOPIC_LED.decode())
    return c

# ---------------- SONAR ----------------
def misura_singola():
    trig.low();  time.sleep_us(2)
    trig.high(); time.sleep_us(10)
    trig.low()
    durata = time_pulse_us(echo, 1, 30000)
    if DEBUG:
        print("   grezzo:", durata)
    if durata < 0:
        return None
    cm = durata * 0.0343 / 2
    if cm < 2 or cm > 400:
        return None
    return cm

def distanza_cm():
    valori = []
    for _ in range(N_MISURE):
        v = misura_singola()
        if v is not None:
            valori.append(v)
        time.sleep_ms(60)
    if len(valori) < 3:
        return None
    valori.sort()
    return valori[len(valori) // 2]

# ---------------- MAIN ----------------
connetti_wifi()
client = connetti_mqtt()

n = 0
try:
    while True:
        try:
            client.check_msg()               # comandi LED dal qak
            d = distanza_cm()
            if d is None:
                d = 999.0
            n += 1
            payload = "msg(sonarDistance,event,pico,none,distance({:.1f}),{})".format(d, n)
            client.publish(TOPIC_SONAR, payload)
            print(payload)
            client.check_msg()
        except OSError as e:
            print("Errore MQTT:", e, "- riconnessione...")
            time.sleep(2)
            client = connetti_mqtt()
        time.sleep(PERIODO_S)
except KeyboardInterrupt:
    led_blink_off()
    client.disconnect()
    print("Fermato.")