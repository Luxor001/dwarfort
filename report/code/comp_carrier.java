+?minerNeedsBeer(Beer)[source(Miner)] <-
	.drop_intention(checkStorageForResource(_));
	?caveFound(cave(_, Miner, X, Y))
	!goTo(X,Y);
	.send("forger", askOne, passMeABeerPlz(_), Beer);
	?caveE(_,Miner, A, B);
	!goTo(A, B);
	?goCollect(Resource);
	!checkStorageForResource(Resource).	