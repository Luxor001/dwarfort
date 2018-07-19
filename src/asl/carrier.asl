// Agent carrier in project dwarfort
strength_kg(50).
carrying_kg(0).
atCapacity :- carrying_kg(Kg) & strength_kg(S) & S <= Kg.

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
+!searchFreeCave(ShuffledCaves, I): true.

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
	.random(X); .wait(X*1000);
	?num_caves(N); 
	?cavesEntrances(Caves);
	.shuffle(Caves, ShuffledCaves);
	!searchFreeCave(ShuffledCaves, 0);
	!!checkStorageForResource(Resource).

+caveFound(Cave) <- 
	deployArtefact(Cave); 
	.print(Cave); 
	!!goToCave(Cave).
	
//+kgInStorage(Resource, Kg) <- .print("aa"); .random(Kg).

+!checkStorageForResource(Resource): not atCapacity <- 
	.wait(500);
	//?strength_kg(Strength);
	+kgInStorage(Resource, Kg);
	//checkStorage(Resource);
	.print("waiting..", Kg).
	//?carrying_kg(CarryingKg);
	//?strength_kg(Strength);
	//pickupFromStorage(Resource, CarryingKg, Strength).
+!checkStorageForResource(Resource): atCapacity.

+kgInStorage(ResourceType, Kg) <- 
	checkStorage(ResourceType); 
	.random(Kg).
