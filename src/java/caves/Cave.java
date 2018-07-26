package caves;

import java.util.ArrayList;
import java.util.HashMap;

import jason.environment.grid.Area;
import jason.environment.grid.Location;

public abstract class Cave {
	public ArrayList<Area> areas = new ArrayList<Area>();
	public HashMap<Integer, Integer> storage = new HashMap<Integer, Integer>();
	public Cave(Area area) {
		this.areas.add(area);
	}
	public Cave(ArrayList<Area> areas) {
		this.areas = areas;
	}
	public Cave() {
		
	}
}
