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
	for(.range(I, 0, N - 1)) {
		 .nth(I, ShuffledCaves, Cave);
		 .print(Cave);
		 deletePersonalPercept(entranceReached);
		 deletePersonalPercept(artefactFound);
		 !reachEntrance(Cave);
		 scanForArtefact;
		 !goToCave(Cave);
		 .wait(500);
	}
	.print("devo andare a prendere", Resource).

+!goToCave(Cave) : artefactFound <-
.wait(5).
//.print("artefact found").

+!goToCave(Cave) : not artefactFound <-
.print("going for", Cave);
deployArtefact(Cave);
!traverseTunnels(Cave, "ControlCaveToMineCave").
/*+!goToCave(Cave) <- 
	!reachEntrance(Cave);
	//scanForArtefact
	.print("Entrata raggiunta");
	.wait(1000);
	//goTroughTunnels
		//?geTunnelsOf(Cave);
	.print("reached").
*/
+!traverseTunnels(Cave, Direction) <-
.wait(5);
!traverseTunnels(Cave, Direction).

+!reachEntrance(Cave) : not entranceReached <-
	reachEntrance(Cave);
	.wait(500);
	!reachEntrance(Cave).
	
+!reachEntrance(Cave) : true <- true.

//+!reachEntrance(Cave) : entranceReached <- true.


