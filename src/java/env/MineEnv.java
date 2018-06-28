package env;

import java.util.ArrayList;
import java.util.logging.Logger;

import jason.asSyntax.Literal;
import jason.environment.Environment;
import jason.environment.grid.Area;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

public class MineEnv extends Environment{
	/*
    // action literals
    public static final Literal of = Literal.parseLiteral("open(fridge)");
    public static final Literal clf = Literal.parseLiteral("close(fridge)");
    public static final Literal gb = Literal.parseLiteral("get(beer)");
    public static final Literal hb = Literal.parseLiteral("hand_in(beer)");
    public static final Literal sb = Literal.parseLiteral("sip(beer)");

    // belief literals
    public static final Literal hob = Literal.parseLiteral("has(owner,beer)");
    public static final Literal af = Literal.parseLiteral("at(robot,fridge)");
    public static final Literal ao = Literal.parseLiteral("at(robot,owner)");

    static Logger logger = Logger.getLogger(HouseEnv.class.getName());

*/
    MineModel model; // the model of the grid

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

}
