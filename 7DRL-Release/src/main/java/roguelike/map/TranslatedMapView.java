package src.main.java.roguelike.map;

// TODO: Reimplement in scala
public class TranslatedMapView implements MapView {
	private MapView map;
	private int x, y;
	
	public TranslatedMapView(MapView m) {
		super();
		if (m == null)
			throw new NullPointerException();
		this.map = m;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public MapView getMap() {
		return map;
	}

	public void setMap(MapView map) {
		if (map == null)
			throw new NullPointerException();
		this.map = map;
	}

	@Override
	public int get(int x, int y) {
		return map.get(x + this.x, y + this.y);
	}

}
