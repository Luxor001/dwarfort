needs(gold).
items(armor, 0).

!init.
!repaint.

+!init <- .wait(10);
	 ?needs(Resource);
	.print(Resource);
	.findall(P,carrier(P),Carriers);	
	.send(Carriers, tell, goCollect(Resource)).

+carrierReady(Name) <- +carrier(Name).

+carrierBack(Name) <-
	checkStorage;
	?needs(Resource);
	.send(Name, untell, goCollect(_));.send(Name, tell, goCollect(Resource)).

+orkOrdeIncoming <-	
	.print("ork orde coming!"); 
	-+needs(steel).
-orkOrdeIncoming <-

	.print("ork stopped!");  
-+needs(gold).

+!repaint <-
	paint_me;
	.wait(200);
	!repaint.

+storageKg(steel, Kg): Kg >= 100<-
buildArmor;
?items(armor, N);
-+items(armor, N+1);
.print("Forger built an Armor! Total Armors:",N+1).

+?passMeABeerPlz(Beer) <-
	Beer = true;.