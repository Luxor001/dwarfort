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
		 .wait(100);
	}.

+!goToCave(Cave) : artefactFound <- 
	true.

+!goToCave(Cave) : not artefactFound <-
	deployArtefact(Cave);
	!traverseTunnels(Cave, "Control-Cave");
	deletePersonalPercept(caveReached);
	.print("cave reached!");
	wait(50000).


+!reachEntrance(Cave) : not entranceReached <-
	reachEntrance(Cave);
	.wait(100);
	!reachEntrance(Cave).
	

+!reachEntrance(Cave) : entranceReached <- true.


+!traverseTunnels(Cave, Direction): not caveReached <-
	traverseTunnel(Cave, Direction);
	.wait(50);
	!traverseTunnels(Cave, Direction).
	
	
+!traverseTunnels(Cave, Direction): caveReached <-
	true.
	

//+!reachEntrance(Cave) : entranceReached <- true.


