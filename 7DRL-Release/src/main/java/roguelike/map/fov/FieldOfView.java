package src.main.java.roguelike.map.fov;

import roguelike.map.*;

public abstract class FieldOfView {
	private MapView mapView;
	private boolean[][] fov;
	private int radius;
	
	public FieldOfView(MapView mapView, int radius) {
		this.mapView = mapView;
		this.radius = radius;
		fov = new boolean[radius * 2 + 1][radius * 2 + 1];
	}
	
	public MapView getMapView() {
		return mapView;
	}

	public int getRadius() {
		return radius;
	}

	protected boolean inBounds(int x, int y) {
		if (x + radius < 0 || y + radius < 0)
			return false;
		if (x - radius > 0 || y - radius > 0)
			return false;
		return true;
	}
	
	protected void setVisible(int x, int y, boolean visible) {
		if (inBounds(x, y))
			fov[x + radius][y + radius] = visible;
	}
	
	protected void setVisible(int x, int y) {
		setVisible(x, y, true);
	}
	
	public boolean isVisible(int x, int y) {
		return inBounds(x, y) && fov[x + radius][y + radius];
	}
	
	protected boolean isTransparent(int x, int y) {
		return inBounds(x, y) && mapView.get(x, y) >= 0;
	}
	
	protected void clear() {
		for (int y = 0; y <= radius*2; ++y)
		for (int x = 0; x <= radius*2; ++x)
			fov[x][y] = false;
	}
	
	public abstract void update();
}
