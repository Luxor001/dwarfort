package env;

import java.util.ArrayList;
import java.util.logging.Logger;

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
        // boot the agents' percepts
        this.initializePercepts();
    }
    
    public void initializePercepts() {
    	
    }

    @Override
    public boolean executeAction(final String ag, final Structure action) {
    	if(action.getFunctor().equals("cycleArea")) {
    		int areaIndex = Integer.parseInt(action.getTerm(0).toString());
    		if(!containsPercept(ag, Literal.parseLiteral("goleft")) && !containsPercept(ag, Literal.parseLiteral("goright"))) {
    			addPercept(ag, Literal.parseLiteral("goleft"));
    		}
    		
    		if(containsPercept(ag, Literal.parseLiteral("goleft"))) {
        		if(containsPercept(ag, Literal.parseLiteral("wall"))) {
        			this.model.moveAStep(ag,  StepDirection.RIGHT);
        			addPercept(ag, Literal.parseLiteral("goright"));
        			removePercept(ag, Literal.parseLiteral("goleft"));
        			removePercept(ag, Literal.parseLiteral("wall")); 
        			return true;
        		}
        		if(!this.model.isFreeOfObstacle(this.model.getLocationByStep(ag,  StepDirection.LEFT))) {
        			addPercept(ag, Literal.parseLiteral("wall"));
        			this.model.moveAStep(ag,  StepDirection.UP); 
        		}
        		else
        			this.model.moveAStep(ag,  StepDirection.LEFT);
        		return true;
    		}
    		if(containsPercept(ag, Literal.parseLiteral("goright"))) {
        		if(containsPercept(ag, Literal.parseLiteral("wall"))) {
        			this.model.moveAStep(ag,  StepDirection.LEFT);
        			addPercept(ag, Literal.parseLiteral("goleft"));    
        			removePercept(ag, Literal.parseLiteral("goright"));
        			removePercept(ag, Literal.parseLiteral("wall"));
        			return true;
        		}
        		if(!this.model.isFreeOfObstacle(this.model.getLocationByStep(ag,  StepDirection.RIGHT))) {
        			addPercept(ag, Literal.parseLiteral("wall"));      
        			this.model.moveAStep(ag,  StepDirection.UP); 
    				System.out.println("reached? "+ this.model.getAgentLocationByName(ag).y + " " + (this.model.getCaveOfAgent(ag).areas.get(areaIndex).tl.y - 1));
        			if(this.model.getAgentLocationByName(ag).y == this.model.getCaveOfAgent(ag).areas.get(areaIndex).tl.y - 1)
        				System.out.println("reached");
        		}
        		else
        			this.model.moveAStep(ag,  StepDirection.RIGHT);
        		return true;
    		}
    		
    		//this.model.cycleArea(ag, Integer.parseInt(action.getTerm(0).toString()), true);
    		return true;
    	}
    	if(action.getFunctor().equals("gotocorner")) {
    		this.model.gotocorner(ag, Integer.parseInt(action.getTerm(0).toString()));
    		if(this.model.isAtCorner(ag, Integer.parseInt(action.getTerm(0).toString())))
        		addPercept(ag,Literal.parseLiteral("atcorner"));
    		return true;
    	}
		return false;
    }
}