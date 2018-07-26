public abstract class Cave {
	public ArrayList<Area> areas = new ArrayList<Area>();
    public HashMap<Integer, Integer> storage = new HashMap<Integer, Integer>();
	public Cave(Area area) {
		this.areas.add(area);
    }
}