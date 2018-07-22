needs(gold, 150).

/*!impartisciOrdini.
+!impartisciOrdini : true <- .send(miner, tell, ciao).*/

!init.

+!init <- .wait(10);
	 ?needs(Resource, _);
	.findall(P,carrier(P),Carriers);	
	.send(Carriers, tell, goCollect(Resource)).

+carrierReady(Name) <- +carrier(Name).

+carrierBack(Name, Info) <-
	?needs(Resource, _);
	.send(Name, untell, goCollect(Resource));.send(Name, tell, goCollect(Resource)).

+orkOrdeIncoming <-
.print("ork order coming!");
-+needs(steel, 150).
-orkOrderIncoming <-
.print("ork order not coming!");

-+needs(gold, 150).