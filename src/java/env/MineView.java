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
          
          /*  g.setColor(Color.black);
            this.drawString(g, x, y, this.defaultFont, "Fridge ("
                    + this.hmodel.availableBeers + ")");*/
            break;
        case MineModel.STEEL:                      	
            super.drawAgent(g, x, y, Color.darkGray, -1);
            break;
        /*case HouseModel.OWNER:
            if (lRobot.equals(this.hmodel.lOwner)) {
                super.drawAgent(g, x, y, Color.yellow, -1);
            }
            String o = "Owner";
            if (this.hmodel.sipCount > 0) {
                o += " (" + this.hmodel.sipCount + ")";
            }
            g.setColor(Color.black);
            this.drawString(g, x, y, this.defaultFont, o);
            break;*/
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