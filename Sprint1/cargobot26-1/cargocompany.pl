%====================================================================================
% cargocompany description   
%====================================================================================
request( requestToLoad, requestToLoad(NOARGS) ).
reply( loadAnswer, loadAnswer(ANSWER) ).  %%for requestToLoad
request( moverobot, moverobot(TARGETX,TARGETY,STEPTIME) ).
reply( moverobotdone, moverobotdone(ARG) ).  %%for moverobot
reply( moverobotfailed, moverobotfailed(PLANDONE,PLANTODO) ).  %%for moverobot
%====================================================================================
context(ctxcargocompany, "localhost",  "TCP", "8040").
 qactor( cargoservice, ctxcargocompany, "it.unibo.cargoservice.Cargoservice").
 static(cargoservice).
  qactor( webgui_ioport, ctxcargocompany, "it.unibo.webgui_ioport.Webgui_ioport").
 static(webgui_ioport).
