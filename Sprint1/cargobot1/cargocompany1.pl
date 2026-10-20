%====================================================================================
% cargocompany1 description   
%====================================================================================
request( requestToLoad, requestToLoad(NOARGS) ).
reply( loadAccepted, loadAccepted(ANSWER) ).  %%for requestToLoad
reply( loadRejected, loadRejected(ANSWER) ).  %%for requestToLoad
reply( loadRetryLater, loadRetryLater(ANSWER) ).  %%for requestToLoad
request( requestSlot, requestSlot(NOARGS) ).
reply( slotReserved, slotReserved(ANSWER) ).  %%for requestSlot
reply( slotFailed, slotFailed(REASON) ).  %%for requestSlot
request( setSlot, setSlot(SLOTID,STATE) ).
reply( setSlotChanged, setSlotChanged(NOARGS) ).  %%for setSlot
reply( setSlotFailed, setSlotFailed(REASON) ).  %%for setSlot
request( requestPosition, requestPosition(LOCATION) ).
reply( slotPosition, slotPosition(X,Y) ).  %%for requestPosition
reply( positionFailed, positionFailed(REASON) ).  %%for requestPosition
request( moverobot, moverobot(TARGETX,TARGETY,STEPTIME) ).
reply( moverobotdone, moverobotdone(ARG) ).  %%for moverobot
reply( moverobotfailed, moverobotfailed(PLANDONE,PLANTODO) ).  %%for moverobot
dispatch( containerArrived, containerArrived(ARG) ).
%====================================================================================
context(ctxcargocompany, "localhost",  "TCP", "8040").
context(ctxrobotsmart, "127.0.0.1",  "TCP", "8020").
 qactor( robotsmart, ctxrobotsmart, "external").
  qactor( cargohold, ctxcargocompany, "it.unibo.cargohold.Cargohold").
 static(cargohold).
  qactor( cargoservice, ctxcargocompany, "it.unibo.cargoservice.Cargoservice").
 static(cargoservice).
  qactor( mocksonar, ctxcargocompany, "it.unibo.mocksonar.Mocksonar").
 static(mocksonar).
  qactor( webgui_ioport, ctxcargocompany, "it.unibo.webgui_ioport.Webgui_ioport").
 static(webgui_ioport).
