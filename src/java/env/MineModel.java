package env;

import java.util.ArrayList;

import jason.environment.grid.Area;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

public class MineModel extends GridWorldModel{
    // constants used by the view component (HouseView) to draw environment
    // "objects"
    public static final int FRIDGE = 16;
    public static final int OWNER = 32;
	protected final static int GRIDSIZE=40;
	ArrayList<Area> world = new ArrayList<Area>();

    protected MineModel() {
		super(GRIDSIZE, GRIDSIZE, 3);
		
		setAgentsPositions();
		buildEnvironment();
    }
    
    public void setAgentsPositions() {
		this.setAgPos(0, new Location(1, 1));
		this.setAgPos(1, new Location(2, 2));
		this.setAgPos(2, new Location(3, 3));    	
    }
    
    private void buildEnvironment() {
    	// zona controllo
    	Area area = new Area(new Location(16, 18), new Location(24, 22));
    	this.world.add(area);
    	this.addWall(area.tl.x, area.tl.y, area.br.x, area.tl.y);    	
    	this.addWall(area.tl.x, area.tl.y, area.tl.x, area.br.y);
    	this.addWall(area.tl.x, area.br.y, area.br.x, area.br.y);
    	this.addWall(area.br.x, area.tl.y, area.br.x, area.br.y);    	

    	/*this.addWall(2, 2, 10, 2);
    	this.addWall(2, 10, 20, 20);*/
    	/*
    	// zona nord
    	this.world.add(new Area(new Location(14, 0), new Location(26, 6)));

    	// zona nordOvest
    	this.world.add(new Area(new Location(0, 0), new Location(10, 6)));
    	this.world.add(new Area(new Location(0, 6), new Location(6, 10)));
    	
    	// // zona Est
    	this.world.add(new Area(new Location(36, 8), new Location(40, 16)));
    	this.world.add(new Area(new Location(34, 16), new Location(40, 22)));*/
    	
    }
}
