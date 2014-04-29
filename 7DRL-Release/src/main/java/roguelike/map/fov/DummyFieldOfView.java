package src.main.java.roguelike.map.fov;

import roguelike.map.MapView;

public class DummyFieldOfView extends FieldOfView {

	public DummyFieldOfView(MapView mapView, int radius) {
		super(mapView, radius);
	}

	@Override
	public void update() {
		for (int y = -getRadius(); y <= getRadius(); ++y)
		for (int x = -getRadius(); x <= getRadius(); ++x)
			setVisible(x, y);
	}
	
}
