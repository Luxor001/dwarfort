package caves;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import jason.environment.grid.Area;
import jason.environment.grid.Location;
import jason.util.Pair;

public class MineCave extends Cave{
	public ArrayList<Pair<Location, Integer>> items = new ArrayList<Pair<Location, Integer>>();
	public MineCave(Area area) {
		super(area);
	}
	public MineCave(ArrayList<Area> areas) {
		super(areas);
	}	
	public MineCave() {
		super();
	}
}
