package env;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;
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
	
    MineModel model;
	public static final String goTo = "goTo";
	public static final String deletePersonalPercept = "deletePersonalPercept";
	public static final String scanForArtefact = "scanForArtefact";
	public static final String deployArtefact = "deployArtefact";
	public static final String removeArtefact = "removeArtefact";
	public static final String pickup = "pickup";
	public static final String pickupFromStorage = "pickupFromStorage";
	public static final String dropResource = "dropResource";
	public static final String checkStorage = "checkStorage";
	public static final String clearPercepts = "clearPercepts";
	public static final String buildArmor = "buildArmor";
	public static final String paint_me = "paint_me";
	public static final String cycleArea = "cycleArea";

    @Override
    public void init(final String[] args) {
        this.model = new MineModel();

        if ((args.length == 1) && args[0].equals("gui")) {
            final MineView view = new MineView(this.model);
            this.model.setView(view);
        }
        initializePercepts();
        

		new Thread(()->{
			try {
				while(true) {
					Thread.sleep(10000);
					int random = new Random().nextInt(3);
					System.out.println("ork orde?" + random);
					//33% possibility of ork orde. hard times for the dwarfes.
					if(random == 0)
						addPercept("forger", Literal.parseLiteral("orkOrdeIncoming"));
					else 
						removePercept("forger", Literal.parseLiteral("orkOrdeIncoming"));					
				}
			} catch (InterruptedException e) {}
		}).start();
    }
    
    private void initializePercepts() {
    	for(int i = 0; i < this.model.controlCave.entrances.size(); i++) {
    		Location entrance = this.model.controlCave.entrances.get(i);
    		MineCave cave = this.model.mineCaves.get(i);
    		addPercept(Literal.parseLiteral("cave(" + i + ","+ cave.minerAssigned+","+entrance.x + "," + entrance.y+")"));
    		addPercept(Literal.parseLiteral("caveE(" + i + ","+ cave.minerAssigned+","+cave.entrance.x + "," + cave.entrance.y+")"));    		
    	}
    	addPercept(Literal.parseLiteral("controlCave(" + this.model.controlCave.areas.get(0).center().toString()+")"));
    	
    	this.model.mineCaves.forEach(cave -> {
    		for(int i= 0; i < cave.areas.size(); i++) {
    			Area area = cave.areas.get(i);
    			addPercept(cave.minerAssigned, Literal.parseLiteral(String.format("corner(%d,%d,%d)", i, area.br.x, area.br.y)));    		}
    	});    	    		
    }
    
    @Override
    public boolean executeAction(final String agent, final Structure action) {
    	String functor=action.getFunctor();
    	
    	if(functor.equals(goTo)) {
    		int X = Integer.parseInt(action.getTerm(0).toString());
    		int Y = Integer.parseInt(action.getTerm(1).toString());
    		boolean reached = this.model.moveTowards(agent, new Location(X, Y));
    		if(reached) 
    			this.addPercept(agent, Literal.parseLiteral("positionReached"));    
    		return true;
    	}
    	if(functor.equals(deletePersonalPercept)) {
    		removePercept(agent, Literal.parseLiteral(action.getTerm(0).toString()));
    		return true;
    	}
    	if(functor.equals(scanForArtefact)) {
    		int caveIndex = Integer.parseInt(action.getTerm(0).toString());
    		if(!this.model.scanForArtefact(agent, caveIndex))
        		this.addPercept(agent, Literal.parseLiteral(String.format("caveFound(cave(%s,%s,%s,%s))",action.getTerm(0), action.getTerm(1),action.getTerm(2),action.getTerm(3))));
    		return true;
    	}
    	if(functor.equals(deployArtefact)) {
    		int caveIndex = Integer.parseInt(action.getTerm(0).toString());
    		this.model.deployArtefactOnMyPosition(agent, caveIndex);
    		return true;
    	}
    	if(functor.equals(removeArtefact)) {
    		int caveIndex = Integer.parseInt(action.getTerm(0).toString());
    		this.model.removeArtefactOnMyPosition(agent, caveIndex);
    		return true;
    	}
    	if(functor.equals(pickup) || functor.equals(pickupFromStorage) || functor.equals(dropResource)) {
    		int resourceType = action.getTerm(0).toString().equals("gold") ? MineModel.GOLD : MineModel.STEEL;
    		NumberTerm kgCarrying = (NumberTerm)action.getTerm(1);
    		double carryingKg = 0;
			try {
				if(functor.equals(pickup))
					carryingKg = this.model.collect(agent, resourceType) + kgCarrying.solve();
				if(functor.equals(pickupFromStorage))
					carryingKg = this.model.pickupFromStorage(agent, resourceType, ((NumberTerm)action.getTerm(2)).solve());
				if(functor.equals(dropResource)) 
		    		this.model.dropResource(agent, resourceType, kgCarrying.solve());				
			} catch (Exception e) {}

			
	    	removePerceptsByUnif(agent, Literal.parseLiteral("carrying(Resource, Kg)"));
			addPercept(agent, Literal.parseLiteral("carrying("+action.getTerm(0).toString()+"," + carryingKg + ")"));
    		return true;
    	}
    	if(functor.equals(checkStorage)) {
    		addPercept(agent, Literal.parseLiteral("storageKg(gold,"+ this.model.getStorageAmount(agent, MineModel.GOLD) + ")"));
    		addPercept(agent, Literal.parseLiteral("storageKg(steel,"+this.model.getStorageAmount(agent, MineModel.STEEL) + ")"));
    		return true;
    	}
    	if(functor.equals(clearPercepts)) {
    		clearPercepts(agent);
    		initializePercepts();
    		return true;
    	}
    	if(functor.equals(buildArmor)) {
    		this.model.buildArmor();
    		return true;
    	}
    	if(functor.equals(paint_me)) {
    		this.model.setAgPos(MineModel.agentIdByName.get(agent), this.model.getAgentLocationByName(agent));
    		return true;
    	}

    	if(functor.equals(cycleArea)) {
    		int areaIndex = Integer.parseInt(action.getTerm(0).toString());
    		MineCave caveIAmIn = this.model.getCaveAssignedToAgent(agent);
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
		return false;
    }
}