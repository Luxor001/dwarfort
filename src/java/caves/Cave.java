package caves;

import java.util.ArrayList;

import jason.environment.grid.Area;

public abstract class Cave {
	public ArrayList<Area> areas = new ArrayList<Area>();
	public ArrayList<Area> tunnels = new ArrayList<Area>();
	public String agentAssigned;	
	public Cave(Area area) {
		this.areas.add(area);
	}
	public Cave(ArrayList<Area> areas) {
		this.areas = areas;
	}
	public Cave() {
		
	}
	
	public void assignAgent(String agent) {
		this.agentAssigned = agent;
	}
	public String getAgent() {
		return this.agentAssigned;
	}
}
