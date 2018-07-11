// Agent carrier in project dwarfort

/* Initial beliefs and rules */

//cave(Caves) :-	.findall(cave(Index, EntranceX, EntranceY), cave(Index, EntranceX, EntranceY), Caves).
//cavesEntrances(Index, X, Y) :- .findall(caves(Index, U, P), place(P, Room), Players).
cavesEntrances(Caves) :- .findall(cave(I, X, Y), cave(I, X, Y), Caves).
/* Initial goals */

!start.

+!start: true <- 
?cavesEntrances(Caves);
.length(Caves, Length);
+num_caves(Length);
.my_name(Name);
.send(forger, tell, carrierReady(Name)).

+goCollect(Resource) <- 
?num_caves(N);
?cavesEntrances(Caves);
for(.range(I, 0, N)) {
	 .nth(I, Caves, Cave);
	 .print(Cave);
	 !goToCave(I);
	 .wait(5000);
}
//!selectCave;
.print("devo andare a prendere", Resource).

+!goToCave(I) <- .print("going to ", I).
