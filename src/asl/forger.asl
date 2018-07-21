
//controlliamo se è rimasto posto in magazzino..
space_avaiable(M) :- // What does "drinking too much" implies?
	.date(YY,MM,DD) & // that in the same day...
	.count(consumed(YY,MM,DD,_,_,_,B), QtdB) & limit(B,Limit) & QtdB > Limit. //...owner consumed more beers than allowed
needs(gold, 150).

/*!impartisciOrdini.
+!impartisciOrdini : true <- .send(miner, tell, ciao).*/

!init.

+!init <- !execDeferredPlan(10,give_orders).

+!execDeferredPlan(Waitms,Plan) <- 
	.wait(Waitms);
	!Plan.
	
+!give_orders <-
	 ?needs(Resource, _);
	.findall(P,carrier(P),Carriers);	
	.send(Carriers, tell, goCollect(Resource)).

+carrierReady(Name) <- +carrier(Name).

+carrierBack(Name, Info) <-
	?needs(Resource, _);
	.send(Name, untell, goCollect(Resource));.send(Name, tell, goCollect(Resource)).

+orkOrdeIncoming <- .print("Ork orde is incoming!");
-+needs(steel).