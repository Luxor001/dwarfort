package caves;

import java.util.ArrayList;

import jason.environment.grid.Area;
import jason.environment.grid.Location;
import jason.util.Pair;

public class ControlCave extends Cave{
	public ArrayList<Location> entrances = new ArrayList<>();
	public ControlCave(Area area) {
		super(area);
	}
	public ControlCave() {
		super();
	}
}
