package env;

import java.util.ArrayList;
import java.util.logging.Logger;

import caves.Cave;
import caves.MineCave;
import env.MineModel.StepDirection;
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
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
    		addPercept(Literal.parseLiteral("cave(" + i + ","+entrance.x + "," + entrance.y+")"));
    	}
    	this.model.controlCave.entrances.forEach(entrance -> {
    		
    	});
    }
    @Override
    public boolean executeAction(final String ag, final Structure action) {
    	if(action.getFunctor().equals("cycleArea")) {
    		int areaIndex = Integer.parseInt(action.getTerm(0).toString());
    		MineCave caveIAmIn = this.model.getCaveOfAgent(ag);
    		Location agentLocation =this.model.getAgentLocationByName(ag);  
    		if(!containsPercept(ag, Literal.parseLiteral("goleft")) && !containsPercept(ag, Literal.parseLiteral("goright")))
    			addPercept(ag, Literal.parseLiteral("goleft"));
    		   			
    		if(this.model.agentOverObject(agentLocation, caveIAmIn, MineModel.GOLD))
    			addPercept(ag, Literal.parseLiteral("gold(" + agentLocation.toString() + ")"));

    		if(this.model.agentOverObject(agentLocation, caveIAmIn, MineModel.STEEL))
    			addPercept(ag, Literal.parseLiteral("steel(" + agentLocation.toString() + ")"));
    		
    		if(this.model.agentOverObject(agentLocation, caveIAmIn, MineModel.BLACKBOARD))
    			addPercept(ag, Literal.parseLiteral("blackboard(" + agentLocation.toString() + ")"));
    		
    		if(containsPercept(ag, Literal.parseLiteral("goleft"))) {
        		if(containsPercept(ag, Literal.parseLiteral("wall"))) {
        			this.model.moveAStep(ag,  StepDirection.RIGHT);
        			addPercept(ag, Literal.parseLiteral("goright"));
        			removePercept(ag, Literal.parseLiteral("goleft"));
        			removePercept(ag, Literal.parseLiteral("wall"));
        		}
        		else if(!this.model.isFreeOfObstacle(this.model.getLocationByStep(ag,  StepDirection.LEFT)) || 
        				!caveIAmIn.areas.get(areaIndex).contains(this.model.getLocationByStep(ag,  StepDirection.LEFT))) {
        			addPercept(ag, Literal.parseLiteral("wall")); 
        			if(agentLocation.y == caveIAmIn.areas.get(areaIndex).tl.y) {            			
        				addPercept(ag, Literal.parseLiteral("areaComplete"));
        				if(areaIndex == caveIAmIn.areas.size() -1 )
        					addPercept(ag, Literal.parseLiteral("caveScanned"));
        			}
        			else
        				this.model.moveAStep(ag,  StepDirection.UP); 
        		}
        		else
        			this.model.moveAStep(ag,  StepDirection.LEFT);
    		}
    		else if(containsPercept(ag, Literal.parseLiteral("goright"))) {
        		if(containsPercept(ag, Literal.parseLiteral("wall"))) {
        			this.model.moveAStep(ag,  StepDirection.LEFT);
        			addPercept(ag, Literal.parseLiteral("goleft"));    
        			removePercept(ag, Literal.parseLiteral("goright"));
        			removePercept(ag, Literal.parseLiteral("wall"));
        		}
        		else if(!this.model.isFreeOfObstacle(this.model.getLocationByStep(ag,  StepDirection.RIGHT)) || 
        				!caveIAmIn.areas.get(areaIndex).contains(this.model.getLocationByStep(ag,  StepDirection.RIGHT))) {
        			addPercept(ag, Literal.parseLiteral("wall")); 
        			if(agentLocation.y == caveIAmIn.areas.get(areaIndex).tl.y) {
        				addPercept(ag, Literal.parseLiteral("areaComplete"));
        				if(areaIndex == caveIAmIn.areas.size() -1 )
        					addPercept(ag, Literal.parseLiteral("caveScanned"));
        			}
        			else
            			this.model.moveAStep(ag,  StepDirection.UP);
        		}
        		else
        			this.model.moveAStep(ag,  StepDirection.RIGHT);
    		}
  		
  			System.out.flush();
    		
    		return true;
    	}
    	if(action.getFunctor().equals("gotocorner")) {
    		int areaIndex = Integer.parseInt(action.getTerm(0).toString());
    		this.model.gotocorner(ag, areaIndex);
    		if(this.model.isAtCorner(ag, areaIndex))
        		addPercept(ag, Literal.parseLiteral("atcorner"));
    		return true;
    	}
    	if(action.getFunctor().equals("deletePersonalPercept")) {
    		removePercept(ag, Literal.parseLiteral(action.getTerm(0).toString()));
    		return true;
    	}
    	if(action.getFunctor().equals("reachEntrance")) {
    		System.out.print("prova");
    		String term = action.getTerm(0).toString().replaceAll("[(.*?)]", "");
    		int x = Integer.parseInt(term.split(",")[1]);
    		int y = Integer.parseInt(term.split(",")[2]);    		
    		this.model.moveTowards(ag, new Location(x, y));
    		return true;
    	}
		return false;
    }
}