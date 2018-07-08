package env;

import java.util.ArrayList;
import java.util.Optional;

import caves.Cave;
import caves.MineCave;
import jason.environment.grid.Area;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;
import jason.util.Pair;

public class MineModel extends GridWorldModel{
    // constants used by the view component (HouseView) to draw environment
    // "objects"
    public static final int GOLD = 16;
    public static final int STEEL = 32;
    public static final int BEER = 64;
	protected final static int GRIDSIZE=40;
	ArrayList<MineCave> mineCaves = new ArrayList<MineCave>();

    protected MineModel() {
		super(GRIDSIZE, GRIDSIZE, 3);
				
		buildEnvironment();
		setAgentsPositions();
    }
    
    enum StepDirection{
    	UP,
    	DOWN,
    	LEFT,
    	RIGHT
    }
    
    public void setAgentsPositions() {
    	this.mineCaves.forEach(mineCave -> {
    		int index = mineCaves.indexOf(mineCave);
    		// tralasciamo alcune caves se non ci sono abbastanza minatori..
    		if(index < this.getNbOfAgs()) {    			
	    		String agentName = "miner" + (index + 1);
	    		mineCave.assignAgent(agentName);
	    		this.setAgPos(index, this.mineCaves.get(index).areas.get(0).center());
    		}
    	});
		
		this.setAgPos(1, this.mineCaves.get(1).areas.get(0).center());
    }
    
    private void buildEnvironment() {
    	buildWorld();
    	setItemsOnGrid();
    }
    private void buildWorld() {
    	// zona controllo
    	Area area = new Area(new Location(16, 18), new Location(24, 22));
    	this.addWall(area.tl.x, area.tl.y, area.tl.x+3, area.tl.y);  
    	this.addWall(area.tl.x+5, area.tl.y, area.br.x, area.tl.y);    	
    	this.addWall(area.tl.x, area.tl.y, area.tl.x, area.br.y);
    	this.addWall(area.tl.x, area.br.y, area.br.x, area.br.y);
    	this.addWall(area.br.x, area.tl.y, area.br.x, area.br.y);
    	
    	// zona nordOvest
    	MineCave cave = new MineCave();
    	area = new Area(new Location(0, 0), new Location(10, 6));   
    	cave.areas.add(new Area(new Location(area.tl.x + 1, area.tl.y + 1), new Location(area.br.x - 1, area.br.y - 1)));
    	this.addWall(area.tl.x, area.tl.y, area.br.x, area.tl.y);
    	this.addWall(area.tl.x, area.tl.y, area.tl.x, area.br.y);
    	this.addWall(area.tl.x+6, area.br.y, area.br.x, area.br.y);  
    	this.addWall(area.br.x, area.tl.y, area.br.x, area.br.y);
    	area = new Area(new Location(0, 6), new Location(6, 10));
    	cave.areas.add(new Area(new Location(area.tl.x + 1, area.tl.y), new Location(area.br.x - 1, area.br.y - 1)));
    	this.addWall(area.tl.x, area.tl.y, area.tl.x, area.br.y);
    	this.addWall(area.tl.x, area.tl.y, area.tl.x, area.br.y);
    	this.addWall(area.tl.x, area.br.y, area.br.x, area.br.y);    	
    	this.addWall(area.br.x, area.tl.y, area.br.x, area.tl.y+2);
    	this.addWall(area.br.x, area.tl.y+4, area.br.x, area.br.y);

    	cave.items.add(new Pair<Location, Integer>(new Location(8,1), GOLD));
    	cave.items.add(new Pair<Location, Integer>(new Location(1,4), STEEL));
    	
    	this.addWall(area.br.x, area.tl.y+2, 19, area.tl.y+2);
    	Area tunnel = new Area(area.br.x, area.tl.y+3, 19, area.tl.y+3);
    	this.addWall(area.br.x, area.tl.y+4, 19, area.tl.y+4);
    	cave.tunnels.add(tunnel);    	
    	this.mineCaves.add(cave);
    	
    	// zona nord
    	cave = new MineCave();
    	area = new Area(new Location(14, 0), new Location(26, 6));   
    	this.addWall(area.tl.x, area.tl.y, area.br.x, area.tl.y);    	
    	this.addWall(area.tl.x, area.tl.y, area.tl.x, area.br.y);
    	this.addWall(area.tl.x, area.br.y, area.tl.x+5, area.br.y);
    	this.addWall(area.tl.x+7, area.br.y, area.br.x, area.br.y);
    	this.addWall(area.br.x, area.tl.y, area.br.x, area.br.y);
    	cave.areas.add(new Area(new Location(area.tl.x + 1, area.tl.y + 1), new Location(area.br.x - 1, area.br.y - 1)));
    	
    	this.addWall(area.tl.x+5, 6, area.tl.x+5, 8);
    	this.addWall(area.tl.x+5, 10, area.tl.x+5, 18);
    	tunnel = new Area(new Location(area.tl.x+6, 6), new Location(area.tl.x+6, 18));
    	this.addWall(area.tl.x+7, 6, area.tl.x+7, 18);
    	cave.tunnels.add(tunnel);    	
    	this.mineCaves.add(cave);	

    	/*
    	//cunicolo nord-controllo
    	this.addWall(area.tl.x+5, area.br.y, area.tl.x+5, 18);    	
    	this.addWall(area.tl.x+7, area.br.y, area.tl.x+7, 18);
    	this.world.add(new Area(new Location(area.tl.x+6, 6), new Location(area.tl.x+6, 18)));
    	
    	// // zona Est
    	area = new Area(new Location(35, 8), new Location(39, 16));
    	this.world.add(area);
    	this.addWall(area.tl.x, area.tl.y, area.br.x, area.tl.y);    	
    	this.addWall(area.tl.x, area.tl.y, area.tl.x, area.br.y);
    	this.addWall(area.tl.x, area.br.y, area.br.x, area.br.y);
    	this.addWall(area.br.x, area.tl.y, area.br.x, area.br.y);
    	area = new Area(new Location(33, 16), new Location(39, 22));
    	this.world.add(area);
    	this.addWall(area.tl.x, area.tl.y, area.br.x, area.tl.y);    	
    	this.addWall(area.tl.x, area.tl.y, area.tl.x, area.br.y);
    	this.addWall(area.tl.x, area.br.y, area.br.x, area.br.y);
    	this.addWall(area.br.x, area.tl.y, area.br.x, area.br.y);
    	
    	
    	// // zona sudEst
    	area = new Area(new Location(26, 30), new Location(39, 39));
    	this.world.add(area);
    	this.addWall(area.tl.x, area.tl.y, area.br.x, area.tl.y);    	
    	this.addWall(area.tl.x, area.tl.y, area.tl.x, area.br.y);
    	this.addWall(area.tl.x, area.br.y, area.br.x, area.br.y);
    	this.addWall(area.br.x, area.tl.y, area.br.x, area.br.y);
    	
    	// // zona sud
    	area = new Area(new Location(18, 35), new Location(22, 39));
    	this.world.add(area);
    	this.addWall(area.tl.x, area.tl.y, area.br.x, area.tl.y);    	
    	this.addWall(area.tl.x, area.tl.y, area.tl.x, area.br.y);
    	this.addWall(area.tl.x, area.br.y, area.br.x, area.br.y);
    	this.addWall(area.br.x, area.tl.y, area.br.x, area.br.y);
    	area = new Area(new Location(12, 32), new Location(18, 39));
    	this.world.add(area);
    	this.addWall(area.tl.x, area.tl.y, area.br.x, area.tl.y);    	
    	this.addWall(area.tl.x, area.tl.y, area.tl.x, area.br.y);
    	this.addWall(area.tl.x, area.br.y, area.br.x, area.br.y);
    	this.addWall(area.br.x, area.tl.y, area.br.x, area.br.y);
    	
    	// // zona sudOvest
    	area = new Area(new Location(0, 34), new Location(8, 39));
    	this.world.add(area);
    	this.addWall(area.tl.x, area.tl.y, area.br.x, area.tl.y);    	
    	this.addWall(area.tl.x, area.tl.y, area.tl.x, area.br.y);
    	this.addWall(area.tl.x, area.br.y, area.br.x, area.br.y);
    	this.addWall(area.br.x, area.tl.y, area.br.x, area.br.y);
    	
    	// // zona Ovest
    	area = new Area(new Location(0, 17), new Location(6, 23));
    	this.world.add(area);
    	this.addWall(area.tl.x, area.tl.y, area.br.x, area.tl.y);    	
    	this.addWall(area.tl.x, area.tl.y, area.tl.x, area.br.y);
    	this.addWall(area.tl.x, area.br.y, area.br.x, area.br.y);
    	this.addWall(area.br.x, area.tl.y, area.br.x, area.br.y);*/
    }
    private void setItemsOnGrid() {
    	this.add(GOLD, new Location(8,1));
    	this.add(STEEL, new Location(1,4));    	
    }
    
    boolean moveTowards(String agent, final Location dest) {
        final Location r1 = this.getAgPos(getAgentIdByName(agent));
        // compute where to move
        if (r1.x < dest.x && this.isFreeOfObstacle(new Location(r1.x + 1, r1.y))) 
            r1.x++;
        else if (r1.x > dest.x && this.isFreeOfObstacle(new Location(r1.x - 1, r1.y))) 
            r1.x--;
        
        if (r1.y < dest.y && this.isFreeOfObstacle(new Location(r1.x, r1.y + 1))) 
            r1.y++;
        else if (r1.y > dest.y && this.isFreeOfObstacle(new Location(r1.x, r1.y - 1))) 
            r1.y--;
        
        this.setAgPos(getAgentIdByName(agent), r1); // actually move the robot in the grid        
        return true;
    }
    
    
    public synchronized void gotocorner(String agent, int area) {
    	Location agentLocation = getAgentLocationByName(agent);
    	Cave caveIAmIn = this.mineCaves.stream().filter(cave -> cave.getAgent().equals(agent)).findFirst().get();
    	Area areaToMoveTo = caveIAmIn.areas.get(area);
    	moveTowards(agent, areaToMoveTo.br);
}

    public synchronized boolean isAtCorner(String agent, int area) {
    	Location agentLocation = getAgentLocationByName(agent);
    	Cave caveIAmIn = this.mineCaves.stream().filter(cave -> cave.getAgent().equals(agent)).findFirst().get();
    	Area areaToMoveTo = caveIAmIn.areas.get(area);
    	return agentLocation.distance(areaToMoveTo.br) == 0;    	
    }
    
    public boolean moveAStep(String agent, StepDirection direction) {
    	Location newLocation = this.getLocationByStep(agent, direction);
        this.setAgPos(getAgentIdByName(agent), newLocation); // actually move the robot in the grid            	
		return true;    	
    }
    public synchronized void cycleArea(String agent, int area, boolean direction) {
    	Location agentLocation = getAgentLocationByName(agent);
    	Cave caveIAmIn = this.mineCaves.stream().filter(cave -> cave.getAgent().equals(agent)).findFirst().get();
    	Area currentarea = caveIAmIn.areas.get(area);
    	
    	if(agentLocation.x > currentarea.tl.x)
    		moveTowards(agent, new Location(agentLocation.x - 1, agentLocation.y));
    	if(agentLocation.x == currentarea.tl.x)
    		moveTowards(agent, new Location(agentLocation.x, agentLocation.y-1));
    	
    	this.setAgPos(Integer.parseInt(agent.substring(agent.length() -1 , agent.length())) - 1, new Location(agentLocation.x - 1, agentLocation.y));
    }
    
    public Location getAgentLocationByName(String agent) {
    	if(agent.equals("miner"))
    		return this.getAgPos(0);    	
    	return this.getAgPos(getAgentIdByName(agent));
    }
    public Location getLocationByStep(String agent, StepDirection step) {
    	Location agentLocation = getAgentLocationByName(agent);
    	if(step == StepDirection.UP)
    		return new Location(agentLocation.x, agentLocation.y - 1);
    	if(step == StepDirection.RIGHT)
    		return new Location(agentLocation.x + 1, agentLocation.y);
    	if(step == StepDirection.LEFT)
    		return new Location(agentLocation.x - 1, agentLocation.y);
    	if(step == StepDirection.DOWN)
    		return new Location(agentLocation.x, agentLocation.y -  1);
		return null;    	
    }
    
    private int getAgentIdByName(String agent) {
    	if(agent.equals("miner"))
    		return 0;
    	return Integer.parseInt(agent.substring(agent.length() -1 , agent.length())) - 1;    	
    }
    
    public MineCave getCaveOfAgent(String agent) {
    	return this.mineCaves.stream().filter(cave -> cave.getAgent().equals(agent)).findFirst().get();    	
    }
    
    public void scanAgentCell(String Agent) {
    	Location agentLocation = this.getAgentLocationByName(Agent);
    	int agente = this.getAgAtPos(new Location(8,1));
    	
    	System.out.println("prova" + agente);
    }
    
    public boolean agentOverObject(String agent, int object) {
    	MineCave cave = this.getCaveOfAgent(agent);    	
    	Location agentLocation = this.getAgentLocationByName(agent);
    	return this.agentOverObject(agentLocation, cave, object); 	
    }
    public boolean agentOverObject(Location agentLocation, MineCave agentCave, int object) {
    	Optional<Pair<Location, Integer>> itemInPlace = agentCave.items.stream().filter(item -> item.getFirst().distance(agentLocation) == 0).findFirst();
    	if(itemInPlace.isPresent() && itemInPlace.get().getSecond() == object)
    		return true;
		return false;    	
    }
    
}
