// Agent carrier in project dwarfort
strength_kg(10).
carrying_kg(0).
atCapacity :- carrying_kg(Kg) & strength_kg(S) & S <= Kg.
storageKg(gold, 0).
storageKg(steel, 0).

/* Initial beliefs and rules */
cavesEntrances(Entrances) :- .findall(cave(I, Miner, X, Y), cave(I, Miner, X, Y), Entrances).
/* Initial goals */

!start.

+!start: true <- 
	.my_name(Name);
	.send(forger, tell, carrierReady(Name)).
	
+!searchFreeCave(ShuffledCaves, I): not caveFound(_) & I < .length(ShuffledCaves)<-
 	.nth(I, ShuffledCaves, cave(Index, Miner, X, Y));
	!goTo(X,Y);
	deletePersonalPercept(positionReached);
	scanForArtefact(Index, Miner, X, Y);
!searchFreeCave(ShuffledCaves, I+1).
+!searchFreeCave(ShuffledCaves, I): true.

+!goToCave(cave(Index, Miner, _, _)) <-
	?caveE(Index, _, X, Y);
	!goTo(X, Y);
	deletePersonalPercept(positionReached);
	.wait(5000);
	?cave(Index, MinerName, _, _);
	?goCollect(Resource);
	.send(MinerName, tell, forgerNeeds(Resource));	
	!!checkStorageForResource(Resource).
	


+goCollect(Resource) <- 
	.random(X); .wait(X*1000);
	?cavesEntrances(Caves);
	.shuffle(Caves, ShuffledCaves);
	!searchFreeCave(ShuffledCaves, 0).

+caveFound(cave(Index, Miner, X, Y)) <- 
	deployArtefact(Index); 
	.print(cave(Index, Miner, X, Y)); 
	!!goToCave(cave(Index, Miner, X, Y)).
	
+kgInStorage(ResourceType, Kg) <-
	?caveFound(cave(CaveIndex, _, _, _));
	checkStorage(ResourceType, CaveIndex);
	?storageKg(ResourceType, Kg).
	

+!checkStorageForResource(Resource)<-
	?strength_kg(Strength);
	+kgInStorage(Resource, Kg);
	if(Kg >= Strength) {
		pickupFromStorage(Resource, 0, Strength);
		!!bringBackResource;
	}
	else {
		.wait(500);
		!checkStorageForResource(Resource);
	}.
	
+!dropResource(Resource) <-
	?carrying_kg(Kg);
	dropResource(Resource, Kg).

+!bringBackResource <-
	?caveFound(cave(Index,_, X, Y))
	!goTo(X,Y); //go to the entrance of the cave...
	deletePersonalPercept(positionReached);
	removeArtefact(Index); //remove the artefact of "cave occupied.."
	.wait(500);
	//!dropResource(A);
	.send(forger, tell, carrierBack("aa"));
	.print("reached").

+!goTo(X, Y) : not positionReached <- 
 	goTo(X, Y); 	
	.wait(50);
	!goTo(X, Y).
+!goTo(X, Y) : positionReached.