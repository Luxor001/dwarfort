/* Initial beliefs */

area_to_scan(0). // It's supermarket's "knowledge base"	
/*
+!attendiOrdini : not ordiniArrivati <- .print("attendo"); !attendiOrdini.
*/
!scanCave.

+!scanCave: not caveScanned
	<- ?area_to_scan(N);	
	!scanArea(N);
	-+area_to_scan(N+1);
!scanCave.

+!scanCave: caveScanned
	<- //.print("scansione finita");
	.wait(20000);
!scanCave.

	+!scanArea(N)
		: true
		<- 
		!go_to_bottomright(N);
		deletePersonalPercept(atcorner);
		!cycleArea(N);
		deletePersonalPercept(areaComplete).
			
		+!cycleArea(N):
		not areaComplete <-
		     cycleArea(N);
			.wait(40);
		!cycleArea(N).
		
		+!cycleArea(N) // if arrived at destination (P = "owner" | "fridge")...
			: areaComplete
			<- deletePersonalPercept(goleft); deletePersonalPercept(goright); deletePersonalPercept(wall).
								
		+!go_to_bottomright(N):
		 not atcorner <-
			gotocorner(N);
			.wait(40);
		!go_to_bottomright(N).
	
		+!go_to_bottomright(N).
			 // ...that's all, do nothing, the "original" intention (the "context") can continue
			 
				

/* 
<- ?last_order_id(N); // test-goal
		OrderId = N + 1; -+last_order_id(OrderId); // notice ATOMIC belief update
*/
	/*?position(X,Y,Z);
	moveTo(X,Y,Z);*/
//Usare scanMine(L) dove L è la location?

/*+!at(robot,P) // if NOT arrived at destination (P = "owner" | "fridge")...
	: not at(robot,P)
	<- move_towards(P); !at(robot,P). // ...continue attempting to reach destination
	* 
	*/