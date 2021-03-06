// Agent carrier in project dwarfort
strength_kg(10).
carrying(Resource, 0).
atCapacity :- carrying(_, Kg) & strength_kg(S) & S <= Kg.
storageKg(gold, 0).
storageKg(steel, 0).

/* Initial beliefs and rules */
cavesEntrances(Entrances) :- .findall(cave(I, Miner, X, Y), cave(I, Miner, X, Y), Entrances).
/* Initial goals */

!start.
!repaint.

+!start: true <- 
	.my_name(Name);
	.send(forger, tell, carrierReady(Name)).
	
+goCollect(Resource) <-
	clearPercepts;
	.random(X); .wait(X*1000);
	?cavesEntrances(Caves);
	.shuffle(Caves, ShuffledCaves);
	!searchFreeCave(ShuffledCaves, 0).
	
+!searchFreeCave(ShuffledCaves, I): not caveFound(_) & I < .length(ShuffledCaves)<-
 	.nth(I, ShuffledCaves, cave(Index, Miner, X, Y));
	!goTo(X,Y);
	deletePersonalPercept(positionReached);
	scanForArtefact(Index, Miner, X, Y);
!searchFreeCave(ShuffledCaves, I+1).
+!searchFreeCave(ShuffledCaves, I): true.

+caveFound(cave(Index, Miner, X, Y)) <- 
	deployArtefact(Index); 
	!goToCave(cave(Index, Miner, X, Y)).
	
+!goToCave(cave(Index, Miner, _, _)) <-
	?caveE(Index, _, X, Y);
	!goTo(X, Y);
	deletePersonalPercept(positionReached);
	?cave(Index, MinerName, _, _);
	?goCollect(Resource);	
	.send(MinerName, achieve, goCollect(Resource));	
	!checkStorageForResource(Resource).
	
	
+?kgInStorage(ResourceType, Kg) <-
	-storageKg(ResourceType, _);
	checkStorage;
	?storageKg(ResourceType, Kg).
-storageKg <- true.
	
+!checkStorageForResource(Resource)<-	
	?strength_kg(Strength);
	?kgInStorage(Resource, Kg);
	//.print("Checking for resources!", Kg);
	if(Kg >= Strength) {
		pickupFromStorage(Resource, 0, Strength);
		!bringBackResource;
	}
	else {
		.wait(500);
		!checkStorageForResource(Resource);
	}.
	
+!dropBag <-
	?carrying(Resource, Kg);
	dropResource(Resource, Kg).

+!bringBackResource <-
	?caveFound(cave(Index,_, X, Y))
	!goTo(X,Y); //go to the entrance of the cave...
	deletePersonalPercept(positionReached);
	removeArtefact(Index); //remove the artefact of "cave occupied.."
	.wait(500);
	!dropBag;
	.my_name(Name);
	.send(forger, untell, carrierBack(Name));.send(forger, tell, carrierBack(Name)).

+!goTo(X, Y) : not positionReached <- 
 	goTo(X, Y); 	
	.wait(50);
	!goTo(X, Y).
+!goTo(X, Y).

+!repaint <-
	paint_me;
	.wait(1000);
	!repaint.
	
+?minerNeedsBeer(Beer)[source(Miner)]<-
	.print("miner needs a beer");
	.drop_intention(checkStorageForResource(_));
	?caveFound(cave(_, Miner, X, Y))
	!goTo(X,Y); //go to the entrance of the cave...
	deletePersonalPercept(positionReached);
	.send("forger", askOne, passMeABeerPlz(_), Beer);
	?caveE(_,Miner, A, B);
	!goTo(A, B);
	deletePersonalPercept(positionReached);
	?goCollect(Resource);
	!checkStorageForResource(Resource).	