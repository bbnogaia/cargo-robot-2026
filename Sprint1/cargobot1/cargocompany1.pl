%====================================================================================
% cargocompany1 description   
%====================================================================================
request( requestToLoad, requestToLoad(NOARGS) ).
reply( loadAnswer, loadAnswer(ANSWER) ).  %%for requestToLoad
request( moverobot, moverobot(TARGETX,TARGETY,STEPTIME) ).
reply( moverobotdone, moverobotdone(ARG) ).  %%for moverobot
reply( moverobotfailed, moverobotfailed(PLANDONE,PLANTODO) ).  %%for moverobot
dispatch( containerArrived, containerArrived(ARG) ).
%====================================================================================
context(ctxcargocompany, "localhost",  "TCP", "8040").
context(ctxrobotsmart, "127.0.0.1",  "TCP", "8020").
 qactor( robotsmart, ctxrobotsmart, "external").
  qactor( cargoservice, ctxcargocompany, "it.unibo.cargoservice.Cargoservice").
 static(cargoservice).
  qactor( sonar, ctxcargocompany, "it.unibo.sonar.Sonar").
 static(sonar).
  qactor( webgui_ioport, ctxcargocompany, "it.unibo.webgui_ioport.Webgui_ioport").
 static(webgui_ioport).
