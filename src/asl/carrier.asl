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
		 .nth(I, ShuffledCaves, CaveSelected);
		 deletePersonalPercept(entranceReached);
		 deletePersonalPercept(artefactFound);
		 !reachEntrance(CaveSelected);
		 scanForArtefact(CaveSelected);
		 !goToCave(CaveSelected);
		 .wait(500);
	}.

+!goToCave(Cave) : artefactFound <- 
	true.

+!goToCave(Cave) : not artefactFound <-
	deployArtefact(Cave);
	!traverseTunnels(Cave, "Control-Cave").


+!traverseTunnels(Cave, Direction) <-
	traverseTunnel(Cave, Direction);
	.wait(100000);
	!traverseTunnels(Cave, Direction).

+!reachEntrance(Cave) : not entranceReached <-
	reachEntrance(Cave);
	.wait(500);
	!reachEntrance(Cave).
	
+!reachEntrance(Cave) : true <- true.

//+!reachEntrance(Cave) : entranceReached <- true.


