package caves;

import java.util.ArrayList;

import jason.environment.grid.Area;

public class MineCave extends Cave{	
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
