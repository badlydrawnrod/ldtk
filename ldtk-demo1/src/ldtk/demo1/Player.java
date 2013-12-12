package ldtk.demo1;

import ldtk.Camera;
import ldtk.Kernel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

class Player {

	private static final float MIN_X = -288.0f;
	private static final float MAX_X = 208.0f;
	private static final float MIN_Y = -180.0f;
	private static final float MAX_Y = 180.0f;

	private static final float VERTICAL_SPEED = 180;
	private static final float HORIZONTAL_SPEED = 240;
	private static final float NORMALIZED_SPEED = 240;
	
	private static final float SHOT_COOLDOWN_PERIOD = 0.05f;
	private static final float MAX_DIST = 45;

	private World world;
	private float playerX;
	private float playerY;
	private float mayFireTime;
	private Polygon[] polys;
	private boolean isBoundsDirty;
	private Rectangle bounds;
	private Vector3 initialTouchPoint;
	private Vector3 currentTouchPoint;

	public Player(World world) {
		this.world = world;
		playerX = 0.0f;
		playerY = 0.0f;
		bounds = new Rectangle();
		createPolys();
		updatePolys();
		initialTouchPoint = new Vector3();
		currentTouchPoint = new Vector3();
	}
	
	private void createPolys() {
		float playerWidth = 30;				// TODO: no magic.
		float playerHeight = 45;			// TODO: no magic.
		float halfWidth = playerWidth / 2;
		float halfHeight = playerHeight / 2;
		float[] vertices = new float[] {
			-halfWidth,  halfHeight,
			-halfWidth, -halfHeight,
			 halfWidth, -halfHeight,
			 halfWidth,  halfHeight,
		};
		Polygon playerPolygon = new Polygon(vertices);
		polys = new Polygon[] { playerPolygon };
	}

	public void update() {
		float dx = 0;
		float dy = 0;
		
		// Keyboard movement controls.
		if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
			dx += HORIZONTAL_SPEED;
		}
		if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) {
			dx -= HORIZONTAL_SPEED;
		}
		if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)) {
			dy += VERTICAL_SPEED;
		}
		if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) {
			dy -= VERTICAL_SPEED;
		}

		// Touch-screen movement controls.
		if (Gdx.input.justTouched()) {
			float touchX = Gdx.input.getX();
			float touchY = Gdx.input.getY();
			Camera guiCam = Kernel.cameras.get("guiCam");
			initialTouchPoint.set(touchX, touchY, 0);
			guiCam.camera().unproject(initialTouchPoint);
		}
		else if (Gdx.input.isTouched()) {
			float touchX = Gdx.input.getX();
			float touchY = Gdx.input.getY();
			Camera guiCam = Kernel.cameras.get("guiCam");
			currentTouchPoint.set(touchX, touchY, 0);
			guiCam.camera().unproject(currentTouchPoint);
			float diffX = currentTouchPoint.x - initialTouchPoint.x;
			float diffY = currentTouchPoint.y - initialTouchPoint.y;
			float angle = MathUtils.atan2(diffY, diffX);
			float dist = (float) Math.sqrt(diffX * diffX + diffY * diffY);
			dist /= MAX_DIST;
			dist = Math.min(Math.max(-1, dist), 1);
			dx = MathUtils.cos(angle) * dist * NORMALIZED_SPEED;
			dy = MathUtils.sin(angle) * dist * NORMALIZED_SPEED;
		}
		
		// Keyboard firing controls.
		if (Gdx.input.isKeyPressed(Keys.SPACE) && Kernel.time.time >= mayFireTime) {
			world.addShot();
			mayFireTime = Kernel.time.time + SHOT_COOLDOWN_PERIOD;
		}
		
		// TODO: Touch-screen firing controls.

		playerX += dx * Kernel.time.delta;
		playerY += dy * Kernel.time.delta;

		playerX = Math.min(MAX_X, Math.max(playerX, MIN_X));
		playerY = Math.min(MAX_Y, Math.max(playerY, MIN_Y));
		
		updatePolys();
	}
	
	private void updatePolys() {
		for (Polygon polygon : polys) {
			polygon.setPosition(x(), y());
		}
		isBoundsDirty = true;
	}
	
	public Polygon[] polys() {
		return polys;
	}
	
	public Rectangle bounds() {
		if (isBoundsDirty) {
			isBoundsDirty = false;
			bounds.set(Polys.bounds(polys));
		}
		return bounds;
	}

	public float x() {
		return world.landscapeX() + playerX;
	}

	public float y() {
		return playerY;
	}
}
