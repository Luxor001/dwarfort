/* Initial beliefs */

area_to_scan(1). // It's supermarket's "knowledge base"	

/*
+!attendiOrdini : not ordiniArrivati <- .print("attendo"); !attendiOrdini.
*/

!scanCave.

+!scanCave: not caveScanned
	<- ?area_to_scan(N);
	!scanArea(N);
	.wait(1000);
	-+area_to_scan(N+1);
!scanCave.
	
	+!scanArea(N)
		: true
		<- !go_to_bottomright(N);
		-atcorner;
		!cycleArea(N);
		-areaComplete.		
			
		+!cycleArea(N):
		not areaComplete <-
		     cycleArea(N);
			.wait(100);
		!cycleArea(N).
		
		+!cycleArea(N) // if arrived at destination (P = "owner" | "fridge")...
			: areaComplete
			<- true. // ...that's all, do nothing, the "original" intention (the "context") can continue
					
		+!go_to_bottomright(N):
		 not atcorner <-
			gotocorner(N);
			.wait(100);
		!go_to_bottomright(N).
	
		+!go_to_bottomright(N) // if arrived at destination (P = "owner" | "fridge")...
			: atcorner
			<- true. // ...that's all, do nothing, the "original" intention (the "context") can continue

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