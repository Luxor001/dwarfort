needs(gold, 150).
items(armor, 0).

!init.
!repaint.

+!init <- .wait(10);
	 ?needs(Resource, _);
	.print(Resource);
	.findall(P,carrier(P),Carriers);	
	.send(Carriers, tell, goCollect(Resource)).

+carrierReady(Name) <- +carrier(Name).

+carrierBack(Name) <-
	checkStorage;
	?needs(Resource, _);
	.send(Name, untell, goCollect(_));.send(Name, tell, goCollect(Resource)).

+orkOrdeIncoming <- -+needs(steel, 150).
-orkOrderIncoming <- -+needs(gold, 150).

+!repaint <-
	paint_me;
	.wait(200);
	!repaint.

+storageKg(steel, Kg): Kg >= 50<-
buildArmor;
?items(armor, N);
-+items(armor, N+1);
.print("Forger built an Armor! Total Armors:",N+1).

+?passMeABeerPlz(Beer) <-
	Beer = true;.