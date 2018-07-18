/* Initial beliefs */
strength_kg(10).
carrying_kg(0).
atCapacity :- carrying_kg(Kg) & strength_kg(S) & S <= Kg.

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
+!collect(Resource) : not atCapacity <- 
	//.random(X); .wait(X*1800 + 200);
	.wait(200);
	.print("adsdas");
	?carrying_kg(CarryingKg);
	collect(Resource, CarryingKg);	
	!collect(Resource).
+!collect(Resource) : atCapacity.
	
+!dropResource(Resource) <-
	?carrying_kg(Kg);
	dropResource(Resource, Kg).
	
+forgerNeeds(Resource) : true<- 
	?resource(Resource, X, Y);
	!goTo(X, Y);
	deletePersonalPercept(positionReached);	
	!collect(Resource);
	?storage(A, B);
	!goTo(A, B);
	deletePersonalPercept(positionReached);	
	!dropResource(Resource);
	+forgerNeeds(Resource)[source(self)].