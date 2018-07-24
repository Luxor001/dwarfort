/* Initial beliefs */
thirstiness(0).
thirsty :- thirstiness(N) & N >= 10. 
strength_kg(10).
carrying(Resource, 0).
atCapacity :- carrying(_, Kg) & strength_kg(S) & S <= Kg.

!scanCave(0).

+!scanCave(AreaIndex): not caveScanned	<- 
	!scanArea(AreaIndex);
!scanCave(AreaIndex+1).
+!scanCave(AreaIndex): caveScanned & forgerNeeds(Resource) <-
	!collect(Resource).
+!scanCave(AreaIndex).

	+!scanArea(N)<- 
		?corner(N, X, Y);
		!goTo(X,Y, 50);		
		deletePersonalPercept(positionReached);
		!cycleArea(N);
		deletePersonalPercept(areaComplete).			
		+!cycleArea(N):
		not areaComplete <-
		     cycleArea(N);
			.wait(50);
		!cycleArea(N).
		
		+!cycleArea(N)
			: areaComplete
			<- deletePersonalPercept(goleft); deletePersonalPercept(goright); deletePersonalPercept(wall).
			
+!goTo(X, Y) : not positionReached <- 
 	goTo(X, Y); 	
	.wait(250);
	!goTo(X, Y).
+!goTo(X, Y) : positionReached.

+!goTo(X, Y, MS) : not positionReached <- 
 	goTo(X, Y); 	
	.wait(MS);
	!goTo(X, Y, MS).
+!goTo(X, Y, MS) : positionReached.
			 
// Collect some resource in my feets..
+!pickup(Resource) : not atCapacity <- 
	//.random(X); .wait(X*1800 + 200);	
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
	
//+!collect(Resource): thirstiness(N) & N<=5<-
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