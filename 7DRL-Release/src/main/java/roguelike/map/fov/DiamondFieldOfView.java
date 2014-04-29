package src.main.java.roguelike.map.fov;

import java.util.LinkedList;
import java.util.Queue;

import roguelike.map.MapView;

public class DiamondFieldOfView extends FieldOfView {
	private Ray[][] rays;
	private Queue<Ray> perimeter = new LinkedList<Ray>();
	
	public DiamondFieldOfView(MapView mapView, int radius) {
		super(mapView, radius);
	}

	@Override
	public void update() {
		if (rays == null) rays = new Ray[getRadius() * 2 + 1][getRadius() * 2 + 1];
		
		getRay(0, 0).expand();
		
		while (!perimeter.isEmpty()) {
			Ray ray = perimeter.remove();
			ray.mergeInputs();
			if (!ray.ignore)
				ray.expand();
		}
		
		getFOVData();
		
		clearRays();
		perimeter.clear();
	}

	private void getFOVData() {
		final int r = getRadius();
		clear();
		for (int y = -r; y <= r; ++y)
		for (int x = -r; x <= r; ++x) {
			if (rays[x + r][y + r] != null && !rays[x + r][y + r].ignore && !rays[x + r][y + r].isObscure())
				setVisible(x, y);
		}
	}

	private void clearRays() {
		for (int y = 0; y <= getRadius()*2; ++y)
		for (int x = 0; x <= getRadius()*2; ++x)
				rays[x][y] = null;
	}
	
	private Ray getRay(int x, int y) {
		if (inBounds(x, y)) {
			final int radius = getRadius();
			if (rays[x + radius][y + radius] == null)
				rays[x + radius][y + radius] = new Ray(x, y);
			return rays[x + radius][y + radius];
		}
		else
			return null;
	}

	private class Ray {
		int x, y;
		int obscurityX = 0, obscurityY = 0;
		int errorX = 0, errorY = 0;
		Ray xInput = null, yInput = null;
		boolean added = false;
		boolean ignore = false;
		
		public Ray(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}
		
		private boolean isObscure() {
			return (errorX > 0 && errorX <= obscurityX) || (errorY > 0 && errorY <= obscurityY);
		}
		
		public void mergeInputs() {
			if (xInput != null) processXInput();
			if (yInput != null) processYInput();
			if (xInput == null) {
				if (yInput.isObscure()) ignore = true;
			}
			else if (yInput == null) {
				if (xInput.isObscure()) ignore = true;
			}
			else {
				if (xInput.isObscure() && yInput.isObscure()) ignore = true;
			}
			if (!ignore && !isTransparent(x, y)) {
				errorX = obscurityX = Math.abs(x);
				errorY = obscurityY = Math.abs(y);
			}
		}

		private void processXInput() {
			if (xInput.obscurityX == 0 && xInput.obscurityY == 0) return;
			if (xInput.errorX > 0 && this.obscurityX == 0) {
				this.errorX = xInput.errorX - xInput.obscurityY;
				this.errorY = xInput.errorY + xInput.obscurityY;
				this.obscurityX = xInput.obscurityX;
				this.obscurityY = xInput.obscurityY;
			}
			if (xInput.errorY <= 0 && xInput.obscurityY > 0 && xInput.errorX > 0) {
				this.errorX = xInput.errorX - xInput.obscurityY;
				this.errorY = xInput.errorY + xInput.obscurityY;
				this.obscurityX = xInput.obscurityX;
				this.obscurityY = xInput.obscurityY;
			}
		}

		private void processYInput() {
			if (yInput.obscurityX == 0 && yInput.obscurityY == 0) return;
			if (yInput.errorY > 0 && this.obscurityY == 0) {
				this.errorX = yInput.errorX + yInput.obscurityX;
				this.errorY = yInput.errorY - yInput.obscurityX;
				this.obscurityX = yInput.obscurityX;
				this.obscurityY = yInput.obscurityY;
			}
			if (yInput.errorX <= 0 && yInput.obscurityX > 0 && yInput.errorY > 0) {
				this.errorX = yInput.errorX + yInput.obscurityX;
				this.errorY = yInput.errorY - yInput.obscurityX;
				this.obscurityX = yInput.obscurityX;
				this.obscurityY = yInput.obscurityY;
			}
		}

		public void expand() {
			if (x >= 0)
				cast(getRay(x + 1, y));
			if (x <= 0)
				cast(getRay(x - 1, y));
			if (y >= 0)
				cast(getRay(x, y + 1));
			if (y <= 0)
				cast(getRay(x, y - 1));
		}
		
		private void cast(Ray newRay) {
			if (newRay != null) {
				if (this.x == newRay.x)
					newRay.yInput = this;
				else {
					assert this.y == newRay.y;
					newRay.xInput = this;
				}
				if (!newRay.added) {
					perimeter.add(newRay);
					newRay.added = true;
				}
			}
		}
	}
}
