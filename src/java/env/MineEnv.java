package env;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.logging.Logger;

import artefacts.Artefact;
import artefacts.CarrierArtefact;
import caves.Cave;
import caves.MineCave;
import env.MineModel.StepDirection;
import jason.NoValueException;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.environment.Environment;
import jason.environment.grid.Area;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

public class MineEnv extends Environment{
	
    MineModel model; // the model of the grid
	public static final Literal ordiniArrivati = Literal.parseLiteral("ordiniArrivati");
	public static final Literal scanArea = Literal.parseLiteral("scanArea");
	public static final Literal gotocorner = Literal.parseLiteral("gotocorner");

    @Override
    public void init(final String[] args) {
        this.model = new MineModel();

        if ((args.length == 1) && args[0].equals("gui")) {
            final MineView view = new MineView(this.model);
            this.model.setView(view);
        }
        initializePercepts();
    }
    
    private void initializePercepts() {
    	for(int i = 0; i < this.model.controlCave.entrances.size(); i++) {
    		Location entrance = this.model.controlCave.entrances.get(i);
    		MineCave cave = this.model.mineCaves.get(i);
    		addPercept(Literal.parseLiteral("cave(" + i + ","+ cave.agentAssigned+","+entrance.x + "," + entrance.y+")"));
    		addPercept(Literal.parseLiteral("caveE(" + i + ","+ cave.agentAssigned+","+cave.entrance.x + "," + cave.entrance.y+")"));
    	}
    	addPercept(Literal.parseLiteral("controlCave(" + this.model.controlCave.areas.get(0).center().toString()+")"));
    }
    @Override
    public boolean executeAction(final String agent, final Structure action) {
    	if(action.getFunctor().equals("cycleArea")) {
    		int areaIndex = Integer.parseInt(action.getTerm(0).toString());
    		MineCave caveIAmIn = this.model.getCaveOfAgent(agent);
    		Location agentLocation =this.model.getAgentLocationByName(agent);  
    		if(!containsPercept(agent, Literal.parseLiteral("goleft")) && !containsPercept(agent, Literal.parseLiteral("goright")))
    			addPercept(agent, Literal.parseLiteral("goleft"));
    		   			
    		if(this.model.agentOverObject(agentLocation, caveIAmIn, MineModel.GOLD))
    			addPercept(agent, Literal.parseLiteral("resource(gold," + agentLocation.toString() + ")"));
    		
    		if(this.model.agentOverObject(agentLocation, caveIAmIn, MineModel.STEEL))
    			addPercept(agent, Literal.parseLiteral("resource(steel," + agentLocation.toString() + ")"));
    		
    		if(this.model.agentOverObject(agentLocation, caveIAmIn, MineModel.STORAGE))
    			addPercept(agent, Literal.parseLiteral("storage(" + agentLocation.toString() + ")"));
    		
    		
    		if(containsPercept(agent, Literal.parseLiteral("goleft"))) {
        		if(containsPercept(agent, Literal.parseLiteral("wall"))) {
        			this.model.moveAStep(agent,  StepDirection.RIGHT);
        			addPercept(agent, Literal.parseLiteral("goright"));
        			removePercept(agent, Literal.parseLiteral("goleft"));
        			removePercept(agent, Literal.parseLiteral("wall"));
        		}
        		else if(!this.model.isFreeOfObstacle(this.model.getLocationByStep(agent,  StepDirection.LEFT)) || 
        				!caveIAmIn.areas.get(areaIndex).contains(this.model.getLocationByStep(agent,  StepDirection.LEFT))) {
        			addPercept(agent, Literal.parseLiteral("wall")); 
        			if(agentLocation.y == caveIAmIn.areas.get(areaIndex).tl.y) {            			
        				addPercept(agent, Literal.parseLiteral("areaComplete"));
        				if(areaIndex == caveIAmIn.areas.size() -1 )
        					addPercept(agent, Literal.parseLiteral("caveScanned"));
        			}
        			else
        				this.model.moveAStep(agent,  StepDirection.UP); 
        		}
        		else
        			this.model.moveAStep(agent,  StepDirection.LEFT);
    		}
    		else if(containsPercept(agent, Literal.parseLiteral("goright"))) {
        		if(containsPercept(agent, Literal.parseLiteral("wall"))) {
        			this.model.moveAStep(agent,  StepDirection.LEFT);
        			addPercept(agent, Literal.parseLiteral("goleft"));    
        			removePercept(agent, Literal.parseLiteral("goright"));
        			removePercept(agent, Literal.parseLiteral("wall"));
        		}
        		else if(!this.model.isFreeOfObstacle(this.model.getLocationByStep(agent,  StepDirection.RIGHT)) || 
        				!caveIAmIn.areas.get(areaIndex).contains(this.model.getLocationByStep(agent,  StepDirection.RIGHT))) {
        			addPercept(agent, Literal.parseLiteral("wall")); 
        			if(agentLocation.y == caveIAmIn.areas.get(areaIndex).tl.y) {
        				addPercept(agent, Literal.parseLiteral("areaComplete"));
        				if(areaIndex == caveIAmIn.areas.size() -1 )
        					addPercept(agent, Literal.parseLiteral("caveScanned"));
        			}
        			else
            			this.model.moveAStep(agent,  StepDirection.UP);
        		}
        		else
        			this.model.moveAStep(agent,  StepDirection.RIGHT);
    		}
  		
  			System.out.flush();
    		
    		return true;
    	}
    	if(action.getFunctor().equals("gotocorner")) {
    		int areaIndex = Integer.parseInt(action.getTerm(0).toString());
    		this.model.gotocorner(agent, areaIndex);
    		if(this.model.isAtCorner(agent, areaIndex))
        		addPercept(agent, Literal.parseLiteral("atcorner"));
    		return true;
    	}
    	if(action.getFunctor().equals("goTo")) {
    		int X = Integer.parseInt(action.getTerm(0).toString());
    		int Y = Integer.parseInt(action.getTerm(1).toString());
    		boolean reached = this.model.moveTowards(agent, new Location(X, Y));
    		if(reached) 
    			this.addPercept(agent, Literal.parseLiteral("positionReached"));    
    		return true;
    	}
    	if(action.getFunctor().equals("deletePersonalPercept")) {
    		removePercept(agent, Literal.parseLiteral(action.getTerm(0).toString()));
    		return true;
    	}
    	if(action.getFunctor().equals("scanForArtefact")) {
    		// TODO refactor nel model!
    		Location agentLocation = this.model.getAgentLocationByName(agent);
    		if(this.model.artefactsOnMap.containsKey(agentLocation)) {
    			ArrayList<CarrierArtefact> artefacts = this.model.artefactsOnMap.get(agentLocation);
        		String term = action.getTerm(0).toString().replaceAll("[(.*?)]", "");
    			if(artefacts.stream().filter(artefact -> artefact.caveGoingTo.equals(term.split(",")[0])).count() != 0)
    				return true;
    		}
    		this.addPercept(agent, Literal.parseLiteral("caveFound(" + action.getTerm(0).toString() + ")"));
    		return true;
    	}
    	if(action.getFunctor().equals("deployArtefact")) {
    		//TODO refactor nel model!
    		Location agentLocation = this.model.getAgentLocationByName(agent);
    		String term = action.getTerm(0).toString().replaceAll("[(.*?)]", "");
    		CarrierArtefact artefact = new CarrierArtefact(agent,  term.split(",")[0]);
    		if(!this.model.artefactsOnMap.containsKey(agentLocation))
    			this.model.artefactsOnMap.put(agentLocation, new ArrayList<>());    		
    		this.model.artefactsOnMap.get(agentLocation).add(artefact);
    		return true;
    	}
    	if(action.getFunctor().equals("pickup") || action.getFunctor().equals("pickupFromStorage") || action.getFunctor().equals("dropResource")) {
    		int resourceType = action.getTerm(0).toString().equals("gold") ? MineModel.GOLD : MineModel.STEEL;
    		NumberTerm kgCarrying = (NumberTerm)action.getTerm(1);
    		double carryingKg = 0;
			try {
				if(action.getFunctor().equals("pickup"))
					carryingKg = this.model.collect(agent, resourceType) + kgCarrying.solve();
				if(action.getFunctor().equals("pickupFromStorage"))
					carryingKg = this.model.pickupFromStorage(agent, resourceType, ((NumberTerm)action.getTerm(2)).solve());
				if(action.getFunctor().equals("dropResource"))
		    		this.model.dropResource(agent, resourceType, kgCarrying.solve());
			} catch (Exception e) {}

			
	    	removePerceptsByUnif(agent, Literal.parseLiteral("carrying_kg(Kg)"));
			addPercept(agent, Literal.parseLiteral("carrying_kg(" + carryingKg + ")"));
    		return true;
    	}
    	if(action.getFunctor().equals("checkStorage")) {
    		int resourceType = action.getTerm(0).toString().equals("gold") ? MineModel.GOLD : MineModel.STEEL;
    		int caveIndex = Integer.parseInt(action.getTerm(1).toString());
    		int kgInStorage = this.model.getStorageAmount(caveIndex, resourceType);

	    	removePerceptsByUnif(agent, Literal.parseLiteral("storageKg(Kg)"));
			addPercept(agent, Literal.parseLiteral("storageKg("+action.getTerm(0).toString() + ","+kgInStorage + ")"));
    		return true;
    	}    		
    	if(action.getFunctor().equals("dropResource")) {
    		int resourceType = action.getTerm(0).toString().equals("gold") ? MineModel.GOLD : MineModel.STEEL;
    		NumberTerm kgCarrying =  ((NumberTerm)action.getTerm(1));
			try {
	    		this.model.dropResource(agent, resourceType, kgCarrying.solve());
		    	removePerceptsByUnif(agent, Literal.parseLiteral("carrying_kg(Kg)"));
				addPercept(agent, Literal.parseLiteral("carrying_kg(0)"));
			} catch (Exception e) {}
    		return true;
    	}
		return false;
    }
}