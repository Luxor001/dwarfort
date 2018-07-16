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
	!searchFreeCave(ShuffledCaves, 0).

+caveFound(Cave) <- deployArtefact(Cave); .print(Cave); !!goToCave(Cave).
	
+!searchFreeCave(ShuffledCaves, I): not caveFound(_) & I < .length(ShuffledCaves)<-
 	.nth(I, ShuffledCaves, CaveSelected);
	!reachEntrance(CaveSelected);
	deletePersonalPercept(entranceReached);
	scanForArtefact(CaveSelected);
!searchFreeCave(ShuffledCaves, I+1).
+!searchFreeCave(ShuffledCaves, I): true <- true.

+!goToCave(Cave) <-
	!traverseTunnels(Cave, "Control-Cave");
	deletePersonalPercept(caveReached);
	?goCollect(Resource);
	.send(miner1, tell, forgerNeeds(Resource)).	

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

