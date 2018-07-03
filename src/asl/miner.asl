/* Initial beliefs */

area_to_scan(1). // It's supermarket's "knowledge base"

/*
+!attendiOrdini : not ordiniArrivati <- .print("attendo"); !attendiOrdini.
*/

!scanCave.

+!scanCave: true
	<- ?area_to_scan(N);
	!scanArea(N).

+!scanArea(N)
	: not scanComplete
	<- !go_to_bottomright(N);
!scanArea(N). // ...that's all, do nothing, the "original" intention (the "context") can continue
	
+!go_to_bottomright(N):
 not bottomright <-
	gotocorner(N);
	.wait(1000);
!go_to_bottomright(N).
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