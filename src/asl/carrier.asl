// Agent carrier in project dwarfort

/* Initial beliefs and rules */
cavesEntrances(Caves) :- .findall(cave(I, X, Y), cave(I, X, Y), Caves). 
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
		 !goToCave(CaveSelected);
		 .wait(100);
	};
	.print("prova").

+!goToCave(Cave) : not artefactFound <-
	!reachEntrance(Cave);
	deletePersonalPercept(entranceReached);
	scanForArtefact(Cave);
	deployArtefact(Cave);
	!traverseTunnels(Cave, "Control-Cave");
	deletePersonalPercept(caveReached);
	?goCollect(Resource);
	.send(miner1, tell, forgerNeeds(Resource));
	.wait(100000).
+!goToCave(Cave) : artefactFound <- true.

+!reachEntrance(Cave) : not entranceReached <-
	reachEntrance(Cave);
	.wait(100);
	!reachEntrance(Cave).	
+!reachEntrance(Cave) : entranceReached <- true.

+!traverseTunnels(Cave, Direction): not caveReached <-
	traverseTunnel(Cave, Direction);
	.wait(50);
	!traverseTunnels(Cave, Direction).	
+!traverseTunnels(Cave, Direction): caveReached <- true.

