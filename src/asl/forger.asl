needs(gold, 150).

!init.
!repaint.

+!init <- .wait(10);
	 ?needs(Resource, _);
	.print(Resource);
	.findall(P,carrier(P),Carriers);	
	.send(Carriers, tell, goCollect(Resource)).

+carrierReady(Name) <- +carrier(Name).

+carrierBack(Name) <-
-storageKg(_,_);
	checkStorage;
	?needs(Resource, _);
	.send(Name, untell, goCollect(_));.send(Name, tell, goCollect(Resource)).

+orkOrdeIncoming <- -+needs(steel, 150).
-orkOrderIncoming <- -+needs(gold, 150).

+!repaint <-
	paint_me;
	.wait(200);
	!repaint.

+storageKg(Resource, Kg)<-
if(Resource == steel){
	.print(steel);
};
.print("asda", Kg, Resource).

+storageKg(Resource, Kg): storageKg(Resourcea, _) & Resourcea == steel<-
?items(armor, N); 
-+items(armor, N+1).
	