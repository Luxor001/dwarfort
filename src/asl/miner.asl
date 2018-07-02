/*!attendiOrdini.

+!attendiOrdini : not ordiniArrivati <- .print("attendo"); !attendiOrdini.
*/

!scanMine.

+!scanMine: true <- scanArea. 


/*+!at(robot,P) // if NOT arrived at destination (P = "owner" | "fridge")...
	: not at(robot,P)
	<- move_towards(P); !at(robot,P). // ...continue attempting to reach destination
	* 
	*/