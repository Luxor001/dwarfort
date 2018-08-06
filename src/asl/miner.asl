/* Initial beliefs */
thirstiness(0).
thirsty :- thirstiness(N) & N >= 10. 
strength_kg(10).
carrying(Resource, 0).
atCapacity :- carrying(_, Kg) & strength_kg(S) & S <= Kg.

direction(left).
my_position(X,Y).
!scanCave(0).

+!scanCave(AreaIndex): not caveScanned	<- 
	!scanArea(AreaIndex);
!scanCave(AreaIndex+1).
+!scanCave(AreaIndex): caveScanned & forgerNeeds(Resource) <-
	!collect(Resource).
+!scanCave(AreaIndex).

+!scanArea(N)<-
	?corner(N, X, Y);
	!goTo(X, Y, 50);	
	-+my_position(X, Y);
	deletePersonalPercept(positionReached);
	-+direction(left);
	!cycleArea(N);
	deletePersonalPercept(areaComplete).
				
+!cycleArea(N): not areaComplete & direction(Direction) <-	
	scanForWalls(Direction, N);
	scanForResources;
	?wall(WallExist)[source(percept)];
	?my_position(X,Y);
	if(WallExist){
		!goTo(X, Y, 0, up);	
		if(Direction == left) {		
			-+direction(right);
		}
		else{
			-+direction(left);				
		};
		-+wall(false)[source(percept)]
	}
	else{
		!goTo(X, Y, 0, Direction);		
	};
	.wait(100);
	!cycleArea(N).
+!cycleArea(N)	: areaComplete <-
	deletePersonalPercept(positionReached).
			
+!goTo(X, Y) : not positionReached <- 
 	goTo(X, Y); 	
	.wait(150);
	!goTo(X, Y).
+!goTo(X, Y).

+!goTo(X, Y, MS) : not positionReached <- 
 	goTo(X, Y); 	
	.wait(MS);
	!goTo(X, Y, MS).
+!goTo(X, Y, MS).

+!goTo(X, Y, MS, Directionstep) <-
	if(Directionstep == left){
 	 	goTo(X-1, Y);
		-+my_position(X-1, Y);
	};
	if(Directionstep == up){	
 	 	goTo(X, Y-1);
		-+my_position(X, Y-1);
	};
	if(Directionstep == right){
 	 	goTo(X+1, Y);
		-+my_position(X+1, Y);
	}.
			 
// Collect some resource in my feets..
+!pickup(Resource) : not atCapacity <- 
	.random(X); .wait(X*1800 + 200);	
	?carrying(Resource, CarryingKg);
	pickup(Resource, CarryingKg);	
	!pickup(Resource).
+!pickup(Resource) : atCapacity.
	
+!dropBag <-
	?carrying(Resource, Kg);
	dropResource(Resource, Kg).
	
// message from the carrier!
+!goCollect(Resource)[source(Ag)]<-
	.abolish(forgerNeeds(_));
	+forgerNeeds(Resource)[source(Ag)];
	.drop_intention(collect(_));
	!collect(Resource).
	
+!collect(Resource): not thirsty & caveScanned<-
	?resource(Resource, X, Y);
	!goTo(X, Y);
	deletePersonalPercept(positionReached);	
	!pickup(Resource);
	?storage(A, B);
	!goTo(A, B);
	deletePersonalPercept(positionReached);	
	!dropBag;
	?thirstiness(Thirstiness);
	-+thirstiness(Thirstiness+1);
	!collect(Resource).

+!collect(Resource): thirsty & caveScanned<-
?forgerNeeds(_)[source(Ag)];
.send(Ag, askOne, minerNeedsBeer(_), Beer);
-+thirstiness(0); //drink the beer
?forgerNeeds(Resource);
!collect(Resource).
	
+!collect(Resource).