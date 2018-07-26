public class MineCave extends Cave{
	public ArrayList<Pair<Location, Integer>> items = new ArrayList<Pair<Location, Integer>>();
	public Location entrance;
	public String minerAssigned;
  public ArrayList<Area> tunnels = new ArrayList<Area>();
	public MineCave(Area area) {
		super(area);
	}    
}