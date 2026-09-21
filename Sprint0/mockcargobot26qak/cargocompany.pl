%====================================================================================
% cargocompany description   
%====================================================================================
request( requestToLoad, requestToLoad(NOARGS) ).
reply( loadAnswer, loadAnswer(ANSWER) ).  %%for requestToLoad
%====================================================================================
context(ctxcargocompany, "localhost",  "TCP", "8040").
 qactor( cargoservice, ctxcargocompany, "it.unibo.cargoservice.Cargoservice").
 static(cargoservice).
  qactor( webgui_ioport, ctxcargocompany, "it.unibo.webgui_ioport.Webgui_ioport").
 static(webgui_ioport).
