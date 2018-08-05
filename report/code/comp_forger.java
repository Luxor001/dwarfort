+carrierBack(Name) <-
	checkStorage;
	?needs(Resource);
	.send(Name, untell, goCollect(_));.send(Name, tell, goCollect(Resource)).

//// MineEnv.java ////
if(action.getFunctor().equals("checkStorage")) {
	
	addPercept(agent, Literal.parseLiteral("storageKg(gold,"+ this.model.
	getStorageAmount(agent, MineModel.GOLD) + ")"));

	addPercept(agent, Literal.parseLiteral("storageKg(steel,"+this.model.
	getStorageAmount(agent, MineModel.STEEL) + ")"));
	return true;
}