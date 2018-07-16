// Agent carrier in project dwarfort

/* Initial beliefs and rules */
cavesEntrances(Caves) :- .findall(cave(I, Miner, X, Y), cave(I, Miner, X, Y), Caves).
/* Initial goals */

!start.

+!start: true <- 
	?cavesEntrances(Caves);
	.length(Caves, Length);
	+num_caves(Length);
	.my_name(Name);
	.send(forger, tell, carrierReady(Name)).
	
+!searchFreeCave(ShuffledCaves, I): not caveFound(_) & I < .length(ShuffledCaves)<-
 	.nth(I, ShuffledCaves, CaveSelected);
	!reachEntrance(CaveSelected);
	deletePersonalPercept(entranceReached);
	scanForArtefact(CaveSelected);
!searchFreeCave(ShuffledCaves, I+1).
+!searchFreeCave(ShuffledCaves, I): true <- true.

+!goToCave(cave(Index, Miner, X, Y)) <-
	!traverseTunnels(cave(Index, Miner, X, Y), "Control-Cave");
	deletePersonalPercept(caveReached);
	.wait(5000);
	?cave(Index, MinerName, _, _);
	?goCollect(Resource);
	.send(MinerName, tell, forgerNeeds(Resource)).	

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


+goCollect(Resource) <- 
	.random(X); .wait(X*1000); // From time to time, I get bored...
	?num_caves(N); 
	?cavesEntrances(Caves);
	.shuffle(Caves, ShuffledCaves);
	!searchFreeCave(ShuffledCaves, 0).

+caveFound(Cave) <- 
	deployArtefact(Cave); 
	.print(Cave); 
	!!goToCave(Cave).