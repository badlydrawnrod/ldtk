package ldtk.demo1;

import ldtk.Kernel;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class Shot {

	private static final float MAX_X = 320.0f;
	private static final float HORIZONTAL_SPEED = 480;
	private static final float MAX_SPREAD = 25;
	
	private World world;
	private float x;
	private float y;
	private float spread;
	private boolean isActive;
	private Polygon[] polys;
	private boolean isBoundsDirty;
	private Rectangle bounds;
	
	public Shot(World world) {
		this.world = world;
		this.spread = MathUtils.random(-MAX_SPREAD, MAX_SPREAD);
		this.isActive = true;
		createPolys();
		updatePolys();
		bounds = new Rectangle();
	}
	
	private void createPolys() {
		// TODO: don't recreate the shot's collision geometry every time.
		float shotWidth = 9;				// TODO: no magic.
		float shotHeight = 9;				// TODO: no magic.
		float halfWidth = shotWidth / 2;
		float halfHeight = shotHeight / 2;
		float[] vertices = new float[] {
			-halfWidth,  halfHeight,
			-halfWidth, -halfHeight,
			 halfWidth, -halfHeight,
			 halfWidth,  halfHeight,
		};
		Polygon shotPolygon = new Polygon(vertices);
		polys = new Polygon[] { shotPolygon };
	}
	
	public Polygon[] polys() {
		return polys;
	}
	
	public void moveTo(float x, float y) {
		this.x = x - world.landscapeX();
		this.y = y;
	}
	
	public void update() {
		x += HORIZONTAL_SPEED * Kernel.time.delta;
		y += spread * Kernel.time.delta;
		updatePolys();
	}
	
	private void updatePolys() {
		for (Polygon polygon : polys) {
			polygon.setPosition(x(), y());
		}
		isBoundsDirty = true;
	}
	
	public float x() {
		return world.landscapeX() + x;
	}

	public float y() {
		return y;
	}
	
	public boolean isActive() {
		return isActive && x <= MAX_X;
	}
	
	public void deactivate() {
		isActive = false;
	}	
	
	public Rectangle bounds() {
		if (isBoundsDirty) {
			isBoundsDirty = false;
			bounds.set(Polys.bounds(polys));
		}
		return bounds;
	}
}
