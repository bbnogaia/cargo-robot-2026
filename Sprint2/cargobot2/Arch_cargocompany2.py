### conda install diagrams
from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '22',
}

nodeattr = {   
    'fontsize': '22',
    'bgcolor': 'lightyellow'
}

eventedgeattr = {
    'color': 'red',
    'style': 'dotted'
}
evattr = {
    'color': 'darkgreen',
    'style': 'dotted'
}
with Diagram('cargocompany2Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxcargocompany', graph_attr=nodeattr):
          cargohold=Custom('cargohold','./qakicons/symActorWithobjSmall.png')
          cargoservice=Custom('cargoservice','./qakicons/symActorWithobjSmall.png')
          sonar=Custom('sonar','./qakicons/symActorWithobjSmall.png')
          led=Custom('led','./qakicons/symActorWithobjSmall.png')
          mockwebgui=Custom('mockwebgui','./qakicons/symActorWithobjSmall.png')
     with Cluster('ctxrobotsmart', graph_attr=nodeattr):
          robotsmart=Custom('robotsmart(ext)','./qakicons/externalQActor.png')
     sys >> Edge( label='sonarDistance', **evattr, decorate='true', fontcolor='darkgreen') >> sonar
     cargoservice >> Edge(color='magenta', style='solid', decorate='true', label='<requestPosition<font color="darkgreen"> slotPosition positionFailed</font> &nbsp; requestSlot<font color="darkgreen"> slotReserved slotFailed</font> &nbsp; setSlot<font color="darkgreen"> setSlotChanged setSlotFailed</font> &nbsp; >',  fontcolor='magenta') >> cargohold
     cargoservice >> Edge(color='magenta', style='solid', decorate='true', label='<moverobot<font color="darkgreen"> moverobotdone moverobotfailed</font> &nbsp; >',  fontcolor='magenta') >> robotsmart
     mockwebgui >> Edge(color='magenta', style='solid', decorate='true', label='<requestToLoad<font color="darkgreen"> loadAccepted loadRejected loadRetryLater</font> &nbsp; >',  fontcolor='magenta') >> cargoservice
     sonar >> Edge(color='blue', style='solid',  decorate='true', label='<outOfService &nbsp; >',  fontcolor='blue') >> mockwebgui
     sonar >> Edge(color='blue', style='solid',  decorate='true', label='<containerArrived &nbsp; outOfService &nbsp; >',  fontcolor='blue') >> cargoservice
     cargoservice >> Edge(color='blue', style='solid',  decorate='true', label='<ledState &nbsp; >',  fontcolor='blue') >> led
diag
