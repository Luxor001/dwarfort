package caves;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import jason.environment.grid.Area;
import jason.environment.grid.Location;
import jason.util.Pair;

public class MineCave extends Cave{
	public ArrayList<Pair<Location, Integer>> items = new ArrayList<Pair<Location, Integer>>();
	public Location entrance;
	public String minerAssigned;
	public ArrayList<Area> tunnels = new ArrayList<Area>();
	public MineCave(Area area) {
		super(area);
	}
	public MineCave() {
		super();
	}
	
	public void assignMiner(String agent) {
		this.minerAssigned = agent;
	}
	public String getMinerAssigned() {
		return this.minerAssigned;
	}
}
