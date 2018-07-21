/* Initial beliefs */
strength_kg(10).
carrying(Resource, 0).
atCapacity :- carrying(_, Kg) & strength_kg(S) & S <= Kg.

!scanCave(0).

+!scanCave(AreaIndex): not caveScanned	<- 
	!scanArea(AreaIndex);
!scanCave(AreaIndex+1).
+!scanCave(AreaIndex): caveScanned.

	+!scanArea(N) : true <- 
		!go_to_bottomright(N);		
		deletePersonalPercept(atcorner);
		!cycleArea(N);
		deletePersonalPercept(areaComplete).
			
		+!cycleArea(N):
		not areaComplete <-
		     cycleArea(N);
			.wait(50);
		!cycleArea(N).
		
		+!cycleArea(N) // if arrived at destination (P = "owner" | "fridge")...
			: areaComplete
			<- deletePersonalPercept(goleft); deletePersonalPercept(goright); deletePersonalPercept(wall).
								
		+!go_to_bottomright(N):
		 not atcorner <-
			gotocorner(N);
			.wait(50);
		!go_to_bottomright(N).	
+!go_to_bottomright(N).
			

+!goTo(X, Y) : not positionReached <- 
 	goTo(X, Y); 	
	.wait(250);
	!goTo(X, Y).
+!goTo(X, Y) : positionReached.
			 
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
	
+forgerNeeds(Resource)[source(Ag)] <-
	!collect(Resource, Ag).
	
+!collect(Resource, Ag)<-
	.print("My task is from ", Ag); 
	?resource(Resource, X, Y);
	!goTo(X, Y);
	deletePersonalPercept(positionReached);	
	!pickup(Resource);
	?storage(A, B);
	!goTo(A, B);
	deletePersonalPercept(positionReached);	
	!dropBag;
	!collect(Resource, Ag).