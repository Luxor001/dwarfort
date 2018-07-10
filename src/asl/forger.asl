/*!impartisciOrdini.
+!impartisciOrdini : true <- .send(miner, tell, ciao).*/

//controlliamo se è rimasto posto in magazzino..
space_avaiable(M) :- // What does "drinking too much" implies?
	.date(YY,MM,DD) & // that in the same day...
	.count(consumed(YY,MM,DD,_,_,_,B), QtdB) & limit(B,Limit) & QtdB > Limit. //...owner consumed more beers than allowed
needs("gold").

// utilizzare ciclo for?
+!order(M) : needs(g) & space_avaiable("gold") <- .print("prova").


+carrierReady[source(A)] <- .print("I received a 'carrierReady' from ",A).