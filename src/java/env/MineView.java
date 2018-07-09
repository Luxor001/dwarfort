package env;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

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
        //final Location lRobot = this.model.getAgPos(0);
       switch (object) {
        case MineModel.GOLD:                      	
            super.drawAgent(g, x, y, Color.yellow, -1);
            break;
        case MineModel.STEEL:                      	
            super.drawAgent(g, x, y, Color.LIGHT_GRAY, -1);
            break;
        case MineModel.BLACKBOARD:                      	
            super.drawAgent(g, x, y, Color.BLACK, -1);
            break;
        default:
            break;
       }
    }
    
    /*@Override
    public void drawAgent(final Graphics g, final int x, final int y, Color c,
            final int id) {
        final Location lRobot = this.hmodel.getAgPos(0);
        if (!lRobot.equals(this.hmodel.lOwner)
                && !lRobot.equals(this.hmodel.lFridge)) {
            c = Color.yellow;
            if (this.hmodel.carryingBeer) {
                c = Color.orange;
            }
            super.drawAgent(g, x, y, c, -1);
            g.setColor(Color.black);
            super.drawString(g, x, y, this.defaultFont, "Robot");
        }
    }*/
}