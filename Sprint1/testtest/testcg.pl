%====================================================================================
% testcg description   
%====================================================================================
request( requestToLoad, requestToLoad(NOARGS) ).
reply( loadAnswer, loadAnswer(ANSWER) ).  %%for requestToLoad
%====================================================================================
context(ctxcompany, "localhost",  "TCP", "8010").
 qactor( cargoservice, ctxcompany, "it.unibo.cargoservice.Cargoservice").
 static(cargoservice).
