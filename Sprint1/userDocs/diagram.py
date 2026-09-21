from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
import platform

if platform.system() == "Darwin":  # macOS
    # Percorso predefinito di Homebrew su Mac Apple Silicon (M1/M2/M3) e Intel
    os.environ['PATH'] += os.pathsep + '/opt/homebrew/bin' + os.pathsep + '/usr/local/bin'
elif platform.system() == "Windows":
    os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {
    'fontsize': '22',
}

nodeattr = {   
    'fontsize': '16',
    'bgcolor': 'lightyellow'
}

with Diagram('CargoSystemSprint0Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
    with Cluster('env'):
        sys = Custom('', './qakicons/system.png')

        # --- CONTESTO PRINCIPALE APPLICATIVO ---
        with Cluster('ctxcargosystem', graph_attr=nodeattr):
            ioportgui = Custom('ioportgui (Web-GUI)', './qakicons/symActorWithobjSmall.png')
            cargoservice = Custom('cargoservice', './qakicons/symActorWithobjSmall.png')
            holdcontroller = Custom('holdcontroller', './qakicons/symActorWithobjSmall.png')
            cargorobot = Custom('cargorobot', './qakicons/symActorWithobjSmall.png')
            sonarcontroller = Custom('sonarcontroller', './qakicons/symActorWithobjSmall.png')
            ledcontroller = Custom('ledcontroller (PicoW)', './qakicons/symActorWithobjSmall.png')
            marker = Custom('marker', './qakicons/symActorWithobjSmall.png')

        # --- CONTESTO ROBOT SMART (ESTERNO / COMMITTENTE) ---
        with Cluster('ctxrobotsmart', graph_attr=nodeattr):
            robotsmart = Custom('robotsmart (ext)', './qakicons/externalQActor.png')

        # --- INTERAZIONI E MESSAGGI ---

        # 1. IOPort <-> CargoService
        ioportgui >> Edge(
            color='magenta', style='solid', decorate='true',
            label='<loadrequest<font color="darkgreen"> loadaccept loadreject</font>>',
            fontcolor='magenta'
        ) >> cargoservice

        # 2. CargoService <-> HoldController
        cargoservice >> Edge(
            color='magenta', style='solid', decorate='true',
            label='<reserveSlot<font color="darkgreen"> slotReserved</font>>',
            fontcolor='magenta'
        ) >> holdcontroller

        # 3. CargoService <-> CargoRobot
        cargoservice >> Edge(
            color='magenta', style='solid', decorate='true',
            label='<movecontainer<font color="darkgreen"> movecompleted movefailed</font>>',
            fontcolor='magenta'
        ) >> cargorobot

        # 4. CargoRobot <-> RobotSmart (Servizio di navigazione A*)
        cargorobot >> Edge(
            color='magenta', style='solid', decorate='true',
            label='<moverobot<font color="darkgreen"> moverobotok moverobotfailed</font>>',
            fontcolor='magenta'
        ) >> robotsmart

        # 5. CargoService -> LedController
        cargoservice >> Edge(
            color='blue', style='solid', decorate='true',
            label='<updateled>', fontcolor='blue'
        ) >> ledcontroller

        # 6. CargoService <-> Marker (Slot5)
        cargoservice >> Edge(
            color='magenta', style='solid', decorate='true',
            label='<labelContainer<font color="darkgreen"> labelDone</font>>',
            fontcolor='magenta'
        ) >> marker

        # 7. SonarController -> Eventi
        sonarcontroller >> Edge(
            color='red', style='dotted', decorate='true',
            label='<sonardata / sonaralarm>', fontcolor='red'
        ) >> cargoservice