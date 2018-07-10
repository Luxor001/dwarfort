// Agent carrier in project dwarfort

/* Initial beliefs and rules */

/* Initial goals */

!start.

+!start: true <- 
.my_name(Name);
.send(forger, tell, carrierReady(Name)).

+goCollect(Resource) <-
.print("devo andare a prendere", Resource).