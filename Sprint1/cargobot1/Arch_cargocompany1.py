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
with Diagram('cargocompany1Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxcargocompany', graph_attr=nodeattr):
          cargohold=Custom('cargohold','./qakicons/symActorWithobjSmall.png')
          cargoservice=Custom('cargoservice','./qakicons/symActorWithobjSmall.png')
          mocksonar=Custom('mocksonar','./qakicons/symActorWithobjSmall.png')
          mockled=Custom('mockled','./qakicons/symActorWithobjSmall.png')
          mockwebgui=Custom('mockwebgui','./qakicons/symActorWithobjSmall.png')
     with Cluster('ctxrobotsmart', graph_attr=nodeattr):
          robotsmart=Custom('robotsmart(ext)','./qakicons/externalQActor.png')
     cargoservice >> Edge(color='magenta', style='solid', decorate='true', label='<requestPosition<font color="darkgreen"> slotPosition positionFailed</font> &nbsp; requestSlot<font color="darkgreen"> slotReserved slotFailed</font> &nbsp; setSlot<font color="darkgreen"> setSlotChanged setSlotFailed</font> &nbsp; >',  fontcolor='magenta') >> cargohold
     cargoservice >> Edge(color='magenta', style='solid', decorate='true', label='<moverobot<font color="darkgreen"> moverobotdone moverobotfailed</font> &nbsp; >',  fontcolor='magenta') >> robotsmart
     mockwebgui >> Edge(color='magenta', style='solid', decorate='true', label='<requestToLoad<font color="darkgreen"> loadAccepted loadRejected loadRetryLater</font> &nbsp; >',  fontcolor='magenta') >> cargoservice
     mocksonar >> Edge(color='blue', style='solid',  decorate='true', label='<containerArrived &nbsp; >',  fontcolor='blue') >> cargoservice
     cargoservice >> Edge(color='blue', style='solid',  decorate='true', label='<ledState &nbsp; >',  fontcolor='blue') >> mockled
diag
