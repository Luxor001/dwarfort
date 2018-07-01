package env;

import java.util.ArrayList;
import java.util.logging.Logger;

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
    	System.out.println(ag);
    	if(action.getFunctor().equals("scanArea")) {
    		this.model.scanArea();
    		return true;
    	}
		return false;
    }
}