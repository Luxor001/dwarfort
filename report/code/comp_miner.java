+!collect(Resource): not thirsty & caveScanned <-
	?resource(Resource, X, Y);
	!goTo(X, Y);	
	!pickup(Resource);
	?storage(A, B);
	!goTo(A, B);
	!dropBag;
	?thirstiness(Thirstiness);
	-+thirstiness(Thirstiness+1);
	!collect(Resource).

+!pickup(Resource) : not atCapacity <- 
	.random(X); .wait(X*1800 + 200);	
	?carrying(Resource, CarryingKg);
	pickup(Resource, CarryingKg);	
	!pickup(Resource).
+!pickup(Resource) : atCapacity.