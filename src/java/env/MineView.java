package env;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;

import jason.asSyntax.Literal;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;

public class MineView  extends GridWorldView {

	MineModel  model;
    public MineView(final MineModel model) {    	
		super(model, "Dwarfort", 800);
		this.model = model;
		this.setVisible(true);
        this.repaint();
	}

    /** draw application objects */
    @Override
    public void draw(final Graphics g, final int x, final int y,final int object) {
       switch (object) {
        case MineModel.GOLD:                      	
            super.drawAgent(g, x, y, Color.yellow, -1);
            break;
        case MineModel.STEEL:                      	
            super.drawAgent(g, x, y, Color.LIGHT_GRAY, -1);
            break;
        case MineModel.STORAGE:                      	
            super.drawAgent(g, x, y, Color.BLACK, -1);
            break;
        default:
            break;
       }
    }
    
    @Override
    public void drawAgent(final Graphics g, final int x, final int y, Color c,
            final int id) {
    	if(id != -1) {
	    	if(MineModel.agentTypebyId.get(id).equals("miner"))
	    		c = Color.BLUE;
	    	if(MineModel.agentTypebyId.get(id).equals("carrier"))
	    		c = Color.green;
	    	if(MineModel.agentTypebyId.get(id).equals("forger"))
	    		c = Color.RED;
	        super.drawAgent(g, x, y, c, id);
	        this.drawString(g, x, y, this.defaultFont, id+"");
    	}
    }
}