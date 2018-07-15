
//controlliamo se è rimasto posto in magazzino..
space_avaiable(M) :- // What does "drinking too much" implies?
	.date(YY,MM,DD) & // that in the same day...
	.count(consumed(YY,MM,DD,_,_,_,B), QtdB) & limit(B,Limit) & QtdB > Limit. //...owner consumed more beers than allowed
needs("gold").

/*!impartisciOrdini.
+!impartisciOrdini : true <- .send(miner, tell, ciao).*/

!init.

+!init <- !execDeferredPlan(10,give_orders).

+!execDeferredPlan(Waitms,Plan) <- 
	.wait(Waitms);
	!Plan.
	
+!give_orders <-
	 ?needs(Resource);
	.findall(P,carrier(P),Carriers);	
	for(.member(P, Carriers)){		
		.send(P, tell, goCollect(Resource));
	}.

+carrierReady(P) <- +carrier(P).

