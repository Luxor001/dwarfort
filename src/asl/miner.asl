/*!attendiOrdini.

+!attendiOrdini : not ordiniArrivati <- .print("attendo"); !attendiOrdini.
*/

!scanMine.

+!scanMine: not scancomplete 
	<- scanArea;
	.wait(1000); !scanMine. 
//Usare scanMine(L) dove L è la location?

/*+!at(robot,P) // if NOT arrived at destination (P = "owner" | "fridge")...
	: not at(robot,P)
	<- move_towards(P); !at(robot,P). // ...continue attempting to reach destination
	* 
	*/