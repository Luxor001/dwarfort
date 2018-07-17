package env;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Optional;

import artefacts.Artefact;
import artefacts.CarrierArtefact;
import caves.Cave;
import caves.ControlCave;
import caves.MineCave;
import jason.environment.grid.Area;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;
import jason.functions.Random;
import jason.util.Pair;

public class MineModel extends GridWorldModel{
    // constants used by the view component (HouseView) to draw environment
    // "objects"
    public static final int GOLD = 16;
    public static final int STEEL = 32;
    public static final int BLACKBOARD = 64;
    
    public static final HashMap<String, Integer> agentIdByName = new HashMap<>();
    public static final HashMap<Integer, String> agentTypebyId = new HashMap<>();
    
    public HashMap<Location, CarrierArtefact> artefactsOnMap = new HashMap<>();
	protected final static int GRIDSIZE=40;
	ArrayList<MineCave> mineCaves = new ArrayList<MineCave>();
	ControlCave controlCave;

    protected MineModel() {
		super(GRIDSIZE, GRIDSIZE, 13);

		System.out.println("number of agents"+ this.getNbOfAgs());
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
    	//impostazione miners
    	this.mineCaves.forEach(mineCave -> {
    		int index = mineCaves.indexOf(mineCave);
    		// tralasciamo alcune caves se non ci sono abbastanza minatori..
    		if(index < this.getNbOfAgs()) {    			
	    		String agentName = "miner" + (index + 1);
	    		mineCave.assignAgent(agentName);
	    		this.setAgPos(index, this.mineCaves.get(index).areas.get(0).center());
	    		MineModel.agentTypebyId.put(index, "miner");
    			MineModel.agentIdByName.put(agentName, index);
    		}
    	});

		//impostazione forger
		MineModel.agentTypebyId.put(12, "forger");
		MineModel.agentIdByName.put("forger", 12);
		this.setAgPos(12, new Location(20, 20));
		
    	//impostazione carriers
    	for(int i=mineCaves.size(); i < mineCaves.size() + 6;) {
    		Location random = this.getRandomLocationInArea(this.controlCave.areas.get(0));
    		if(this.isFree(random)) {
    			this.setAgPos(i, random);	
    			MineModel.agentTypebyId.put(i, "carrier");
    			MineModel.agentIdByName.put("carrier"+(i - 5) , i);
    			i++;
    		}    		
    	}
    	
    }
    
    private void buildEnvironment() {
    	buildWorld();
    	setItemsOnGrid();
    }
    private void buildWorld() {
    	// zona controllo
    	Area area = new Area(new Location(16, 18), new Location(24, 22));
    	//lato nord
    	this.addWall(area.tl.x, area.tl.y, area.tl.x+3, area.tl.y);  
    	this.addWall(area.tl.x+5, area.tl.y, area.br.x, area.tl.y); 
    	//lato ovest
    	this.addWall(area.tl.x, area.tl.y, area.tl.x, area.tl.y);	
    	this.addWall(area.tl.x, area.tl.y + 2, area.tl.x, area.br.y);
    	//lato sud
    	this.addWall(area.tl.x, area.br.y, area.tl.x + 3, area.br.y);
    	this.addWall(area.tl.x + 5, area.br.y, area.br.x, area.br.y);
    	//lato est
    	this.addWall(area.br.x, area.tl.y, area.br.x, area.tl.y + 1);
    	this.addWall(area.br.x, area.tl.y + 3, area.br.x, area.br.y);
    	this.controlCave = new ControlCave(new Area(new Location(area.tl.x + 1, area.tl.y + 1), new Location(area.br.x - 1, area.br.y - 1)));
    	
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
    	cave.entrance = new Location(area.br.x-1, area.tl.y+3);
    	
    	//cunicolo
    	this.addWall(area.br.x, area.tl.y+2, 19, area.tl.y+2);
    	Area tunnel = new Area(area.br.x, area.tl.y+3, 19, area.tl.y+3);
    	this.addWall(area.br.x, area.tl.y+4, 19, area.tl.y+4);
    	cave.tunnels.add(tunnel);
    	this.addWall(19, area.tl.y+4, 19, 18);
    	tunnel = new Area(20, area.tl.y+3, 20, 18);
    	this.addWall(21, area.tl.y+4, 21, 18);
    	cave.tunnels.add(tunnel);
    	this.controlCave.entrances.add(new Location(20, 18));

    	//blackboard/items
    	cave.blackBoard = new Location(5, 7);
    	cave.items.add(new Pair<Location, Integer>(new Location(8,1), GOLD));
    	cave.items.add(new Pair<Location, Integer>(new Location(1,4), STEEL));
    	this.mineCaves.add(cave);
    	
    	// NORD
    	cave = new MineCave();
    	area = new Area(new Location(14, 0), new Location(26, 6));   
    	this.addWall(area.tl.x, area.tl.y, area.br.x, area.tl.y);    	
    	this.addWall(area.tl.x, area.tl.y, area.tl.x, area.br.y);
    	this.addWall(area.tl.x, area.br.y, area.tl.x+5, area.br.y);
    	this.addWall(area.tl.x+7, area.br.y, area.br.x, area.br.y);
    	this.addWall(area.br.x, area.tl.y, area.br.x, area.br.y);
    	cave.areas.add(new Area(new Location(area.tl.x + 1, area.tl.y + 1), new Location(area.br.x - 1, area.br.y - 1)));
    	cave.entrance = new Location(area.tl.x+6, area.br.y-1);

    	//cunicolo
    	this.addWall(area.tl.x+5, 6, area.tl.x+5, 8);
    	tunnel = new Area(area.tl.x+6, 6, area.tl.x+6, 18);
    	this.addWall(area.tl.x+7, 6, area.tl.x+7, 18);
    	cave.tunnels.add(tunnel);
    	this.controlCave.entrances.add(new Location(area.tl.x+6, 18));
    	
    	//blackboard/items
    	cave.blackBoard = new Location(area.tl.x+4, 5);
    	cave.items.add(new Pair<Location, Integer>(new Location(15,1), GOLD));
    	cave.items.add(new Pair<Location, Integer>(new Location(25,4), STEEL));
    	this.mineCaves.add(cave);
    	
    	
    	// EST
    	cave = new MineCave();
    	area = new Area(new Location(35, 8), new Location(39, 16));
    	this.addWall(area.tl.x, area.tl.y, area.br.x, area.tl.y);    	
    	this.addWall(area.tl.x, area.tl.y, area.tl.x, area.br.y);
    	this.addWall(area.br.x, area.tl.y, area.br.x, area.br.y);
    	cave.areas.add(new Area(new Location(area.tl.x + 1, area.tl.y + 1), new Location(area.br.x - 1, area.br.y)));
    	area = new Area(new Location(33, 16), new Location(39, 22));
    	this.addWall(area.tl.x, area.tl.y, area.tl.x + 2, area.tl.y);    	
    	this.addWall(area.tl.x, area.tl.y, area.tl.x, area.tl.y + 3);
    	this.addWall(area.tl.x, area.tl.y + 5, area.tl.x, area.br.y);
    	this.addWall(area.tl.x, area.br.y, area.br.x, area.br.y);
    	this.addWall(area.br.x, area.tl.y, area.br.x, area.br.y);
    	cave.areas.add(new Area(new Location(area.tl.x + 1, area.tl.y + 1), new Location(area.br.x - 1, area.br.y - 1)));
    	cave.entrance = new Location(area.tl.x+1, area.tl.y+4);

    	//cunicolo
    	this.addWall(25, area.tl.y+3, area.tl.x - 1, area.tl.y+3);
    	tunnel = new Area(24, area.tl.y+4, area.tl.x, area.tl.y+4);
    	this.addWall(25, area.tl.y+5, area.tl.x - 1, area.tl.y+5);
    	cave.tunnels.add(tunnel);
    	this.controlCave.entrances.add(new Location(24, area.tl.y+4));

    	//blackboard/items
    	cave.blackBoard = new Location(area.tl.x+1, area.tl.y + 3);
    	cave.items.add(new Pair<Location, Integer>(new Location(area.br.x -1, area.br.y - 1), GOLD));
    	cave.items.add(new Pair<Location, Integer>(new Location(37,9), STEEL));
    	this.mineCaves.add(cave);
    	
    	
    	// SUDEST
    	cave = new MineCave();
    	area = new Area(new Location(30, 30), new Location(39, 39));
    	this.addWall(area.tl.x, area.tl.y, area.br.x, area.tl.y);    	
    	this.addWall(area.tl.x, area.tl.y + 2, area.tl.x, area.br.y);
    	this.addWall(area.tl.x, area.br.y, area.br.x, area.br.y);
    	this.addWall(area.br.x, area.tl.y, area.br.x, area.br.y);
    	cave.areas.add(new Area(new Location(area.tl.x + 1, area.tl.y + 1), new Location(area.br.x - 1, area.br.y - 1)));
    	cave.entrance = new Location(area.tl.x+1, area.tl.y+1);

    	//cunicolo
    	this.addWall(21, area.tl.y, area.tl.x - 1, area.tl.y);
    	tunnel = new Area(21, area.tl.y + 1, area.tl.x, area.tl.y + 1);
    	this.addWall(21, area.tl.y + 2, area.tl.x - 1, area.tl.y + 2);
    	cave.tunnels.add(tunnel);
    	this.addWall(19, 23, 19, area.tl.y+2);
    	tunnel = new Area(20, 23, 20, area.tl.y);
    	this.addWall(21, 23, 21, area.tl.y);
    	cave.tunnels.add(tunnel);    	
    	this.controlCave.entrances.add(new Location(20, 22));

    	//blackboard/items
    	cave.blackBoard = new Location(area.tl.x+1, area.tl.y + 2);
    	cave.items.add(new Pair<Location, Integer>(new Location(area.br.x -1, area.br.y - 1), GOLD));
    	cave.items.add(new Pair<Location, Integer>(new Location(38,32), STEEL));
    	this.mineCaves.add(cave);
    	
    	//SUD
    	cave = new MineCave();
    	area = new Area(new Location(16, 34), new Location(24, 39));
    	this.addWall(area.tl.x, area.tl.y, area.tl.x + 3, area.tl.y);    
    	this.addWall(area.tl.x + 5, area.tl.y, area.br.x, area.tl.y);    	
    	this.addWall(area.tl.x, area.tl.y, area.tl.x, area.br.y);
    	this.addWall(area.tl.x, area.br.y, area.br.x, area.br.y);
    	this.addWall(area.br.x, area.tl.y, area.br.x, area.br.y);
    	cave.areas.add(new Area(new Location(area.tl.x + 1, area.tl.y + 1), new Location(area.br.x - 1, area.br.y - 1)));
    	cave.entrance = new Location(area.tl.x+4, area.tl.y + 1);

    	//cunicolo
    	this.addWall(19, 23, 19, area.tl.y);
    	tunnel = new Area(20, 22, 20, area.tl.y);
    	this.addWall(21, area.tl.y - 2, 21, area.tl.y);
    	cave.tunnels.add(tunnel);
    	this.controlCave.entrances.add(new Location(20, 22));
    	
    	//blackboard/items
    	cave.blackBoard = new Location(19, area.tl.y + 1);
    	cave.items.add(new Pair<Location, Integer>(new Location(area.br.x -1, area.br.y - 1), GOLD));
    	cave.items.add(new Pair<Location, Integer>(new Location(17,35), STEEL));
    	this.mineCaves.add(cave);

    	//OVEST
    	cave = new MineCave();
    	area = new Area(new Location(0, 17), new Location(6, 25));
    	this.addWall(area.tl.x, area.tl.y, area.br.x, area.tl.y);    	
    	this.addWall(area.tl.x, area.tl.y, area.tl.x, area.br.y);
    	this.addWall(area.tl.x, area.br.y, area.br.x, area.br.y);
    	this.addWall(area.br.x, area.tl.y, area.br.x, area.tl.y + 1);
    	this.addWall(area.br.x, area.tl.y + 3, area.br.x, area.br.y);
    	cave.areas.add(new Area(new Location(area.tl.x + 1, area.tl.y + 1), new Location(area.br.x - 1, area.br.y - 1)));
    	cave.entrance = new Location(area.br.x - 1, area.tl.y+2);
    	
    	//cunicolo
    	this.addWall(area.br.x, area.tl.y + 1, 16, area.tl.y + 1);
    	tunnel = new Area(area.br.x,  area.tl.y + 2, 16,  area.tl.y + 2);
    	this.addWall(area.br.x, area.tl.y + 3, 16, area.tl.y + 3);
    	cave.tunnels.add(tunnel);
    	this.controlCave.entrances.add(new Location(16,  area.tl.y + 2));
    	
    	//blackboard/items
    	cave.blackBoard = new Location(5, area.tl.y + 1);
    	cave.items.add(new Pair<Location, Integer>(new Location(1, 22), GOLD));
    	cave.items.add(new Pair<Location, Integer>(new Location(1,20), STEEL));
    	this.mineCaves.add(cave);

    }
    private void setItemsOnGrid() {
    	this.mineCaves.forEach(mineCave -> {
    		if(mineCave.areas.size() != 0 && mineCave.blackBoard != null) {
	    		mineCave.items.forEach(item -> this.add(item.getSecond(), item.getFirst()));
	    		this.add(BLACKBOARD, mineCave.blackBoard);
    		}
    	});
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
        return r1.equals(dest);
    }    
    
    public synchronized void gotocorner(String agent, int area) {
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
    
    private synchronized int getAgentIdByName(String agent) {
    	/*if(agent.equals("miner"))
    		return 0;*/
    	return MineModel.agentIdByName.get(agent);
    }
    
    public MineCave getCaveOfAgent(String agent) {
    	return this.mineCaves.stream().filter(cave -> cave.getAgent().equals(agent)).findFirst().get();    	
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
    
    private Location getRandomLocationInArea(Area area) {
    	java.util.Random rnd = new java.util.Random();
    	int x = rnd.nextInt((area.br.x) - (area.tl.x)) + area.tl.x;
    	int y = rnd.nextInt((area.br.y) - (area.tl.y)) + area.tl.y;
    	return new Location(x,y);
    }
    
}
