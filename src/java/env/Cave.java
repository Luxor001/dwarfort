package env;

import java.util.ArrayList;

import jason.environment.grid.Area;

public class Cave {
	public ArrayList<Area> areas = new ArrayList<Area>();
	public ArrayList<Area> tunnels = new ArrayList<Area>();
	public int tl;
	public int br;
	public Cave(Area area) {
		this.areas.add(area);
	}
	public Cave(ArrayList<Area> areas) {
		this.areas = areas;
	}
	public Cave() {
		
	}
}
