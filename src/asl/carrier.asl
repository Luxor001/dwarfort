// Agent carrier in project dwarfort

/* Initial beliefs and rules */

//cave(Caves) :-	.findall(cave(Index, EntranceX, EntranceY), cave(Index, EntranceX, EntranceY), Caves).
//cavesEntrances(Index, X, Y) :- .findall(caves(Index, U, P), place(P, Room), Players).
cavesEntrances(Caves) :- .findall(cave(I, X, Y), cave(I, X, Y), Caves).
artefact(Prova) :- true.
/* Initial goals */

!start.

+!start: true <- 
	?cavesEntrances(Caves);
	.length(Caves, Length);
	+num_caves(Length);
	.my_name(Name);
	.send(forger, tell, carrierReady(Name)).

+goCollect(Resource) <- 
	?num_caves(N);
	?cavesEntrances(Caves);
	.shuffle(Caves, ShuffledCaves);
	for(.range(I, 0, N)) {
		 .nth(I, ShuffledCaves, Cave);
		 !reachEntrance(Cave);
		 scanForArtefact;
		 !goToCave(Cave);
		// ?artefact(Prova);
		 //goToCave(Cave)
		 .wait(500);
	}
	.print("devo andare a prendere", Resource).

+!goToCave(Cave) : artefactFound <-
true.

+!goToCave(Cave) : not artefactFound <-
deployArtefact(Cave);
.wait(10000);
//reachCave
true.
/*+!goToCave(Cave) <- 
	!reachEntrance(Cave);
	//scanForArtefact
	.print("Entrata raggiunta");
	.wait(1000);
	//goTroughTunnels
		//?geTunnelsOf(Cave);
	.print("reached").
*/

+!reachEntrance(Cave) : not entranceReached <-
	reachEntrance(Cave);
	.wait(500);
	!reachEntrance(Cave).
	
+!reachEntrance(Cave) : true <- true.

//+!reachEntrance(Cave) : entranceReached <- true.


