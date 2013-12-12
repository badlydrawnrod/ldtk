package ldtk.demo1;


import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

class LandscapePolys {

	private Polygon[] polys;
	private Rectangle bounds;
	private float tileWidth;

	public static LandscapePolys createLowerGeometry(float[] lower, float tileWidth, float landscapeBottom) {
		Polygon[] polys = new Polygon[lower.length - 1];
		for (int i = 0; i < polys.length; i++) {
			float x1 = i * tileWidth;
			float x2 = (i + 1) * tileWidth;
			float y1 = lower[i];
			float y2 = lower[i + 1];
			float[] vertices = new float[] { x1, y1, x1, landscapeBottom, x2, landscapeBottom, x2, y2 };
			polys[i] = new Polygon(vertices);
		}
		return new LandscapePolys(polys, tileWidth);
	}

	public static LandscapePolys createUpperGeometry(float[] upper, float tileWidth, float landscapeTop) {
		Polygon[] polys = new Polygon[upper.length - 1];
		for (int i = 0; i < polys.length; i++) {
			float x1 = i * tileWidth;
			float y1 = upper[i];
			float y2 = upper[i + 1];
			float x2 = (i + 1) * tileWidth;
			float[] vertices = new float[] { x1, landscapeTop, x1, y1, x2, y2, x2, landscapeTop };
			polys[i] = new Polygon(vertices);
		}
		return new LandscapePolys(polys, tileWidth);
	}

	private LandscapePolys(Polygon[] polys, float tileWidth) {
		this.polys = polys;
		this.tileWidth = tileWidth;
		bounds = new Rectangle(Polys.bounds(polys));
	}

	public Polygon[] polys() {
		return polys;
	}

	public boolean hitAny(Polygon[] others) {
		return Polys.hitAny(polys, others);
	}
	
	public boolean hitAny(Polygon[] others, Rectangle otherBounds) {
		int left = (int) (otherBounds.x / tileWidth);
		int right = (int) ((otherBounds.x + otherBounds.width + 0.5f) / tileWidth);
		if (right < 0 || left >= polys.length) {
			return false;
		}
		for (int i = Math.max(left, 0); i < Math.min(right, polys.length); i++) {
			if (Polys.hitAny(polys[i], others)) {
				return true;
			}
		}
		return false;
	}
	
	public Rectangle bounds() {
		return bounds;
	}
}
