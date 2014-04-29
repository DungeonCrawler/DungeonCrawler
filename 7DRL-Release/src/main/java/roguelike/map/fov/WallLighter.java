package src.main.java.roguelike.map.fov;

public class WallLighter extends FieldOfView {
	private FieldOfView wrapped;

	public WallLighter(FieldOfView wrapped) {
		super(wrapped.getMapView(), wrapped.getRadius());
		this.wrapped = wrapped;
	}

	@Override
	public void update() {
		wrapped.update();
		clear();
		for (int y = -getRadius(); y <= getRadius(); ++y)
		for (int x = -getRadius(); x <= getRadius(); ++x) {
			if (wrapped.isVisible(x, y))
				setVisible(x, y);
			else if (!isTransparent(x, y)) {
				if (wrapped.isVisible(x + 1, y) || 
				    wrapped.isVisible(x - 1, y) || 
				    wrapped.isVisible(x, y + 1) || 
				    wrapped.isVisible(x, y - 1) || 
				    wrapped.isVisible(x + 1, y + 1) || 
				    wrapped.isVisible(x - 1, y - 1) || 
				    wrapped.isVisible(x - 1, y + 1) || 
				    wrapped.isVisible(x + 1, y - 1))
					setVisible(x, y);
			}
		}
	}

}
