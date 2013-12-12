package ldtk.demo1;

import java.util.ArrayList;
import java.util.List;

import ldtk.Kernel;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;


public class World {

	public static final int BACKGROUND_LAYER = 0;
	public static final int MIDGROUND_LAYER = BACKGROUND_LAYER + 1;
	public static final int FOREGROUND_LAYER = MIDGROUND_LAYER + 1;
	public static final int NUM_LAYERS = FOREGROUND_LAYER + 1;
	
	private static final float SCROLL_SPEED = 120;
	private static final float TILE_WIDTH = 64;
	private static final float BOTTOM = -180;
	private static final float TOP = 180;

	private Landscape[] landscapes;
	private LandscapePolys[] landscapePolys;
	private float landscapeX;
	private Player player;
	private List<Shot> shots;

	public World() {
		landscapes = new Landscape[NUM_LAYERS];
		landscapes[BACKGROUND_LAYER] = new Landscape();
		landscapes[MIDGROUND_LAYER] = new Landscape();
		landscapes[FOREGROUND_LAYER] = new Landscape();
		landscapePolys = new LandscapePolys[] {
			LandscapePolys.createLowerGeometry(landscapes[FOREGROUND_LAYER].lower(), TILE_WIDTH, BOTTOM),	
			LandscapePolys.createLowerGeometry(landscapes[FOREGROUND_LAYER].upper(), TILE_WIDTH, TOP),	
		};
		
		landscapeX = 0;
		player = new Player(this);
		shots = new ArrayList<Shot>();
	}

	public Landscape[] landscapes() {
		return landscapes;
	}

	public void update() {
		updateLandscape();
		updatePlayer();
		updateShots();
		checkPlayerCollisions();
		checkShotsCollisions();
	}

	private void updateLandscape() {
		landscapeX += SCROLL_SPEED * Kernel.time.delta;
	}

	private void updatePlayer() {
		player.update();
	}

	private void updateShots() {
		for (int i = shots.size() - 1; i >= 0; i--) {
			Shot shot = shots.get(i);
			shot.update();
			if (!shot.isActive()) {
				shots.remove(i);
			}
		}
	}
	
	private void checkPlayerCollisions() {
		Rectangle playerBounds = player.bounds();
		Polygon[] playerPolys = player.polys();
		for (LandscapePolys landscapePoly : landscapePolys) {
			if (landscapePoly.hitAny(playerPolys, playerBounds)) {
				// TODO: collision response - the player hit the landscape.
				break;
			}
		}
	}

	private void checkShotsCollisions() {
		for (int i = shots.size() - 1; i >= 0; i--) {
			Shot shot = shots.get(i);
			checkShotCollision(shot);
			if (!shot.isActive()) {
				shots.remove(i);
			}
		}
	}
	
	private void checkShotCollision(Shot shot) {
		Rectangle shotBounds = shot.bounds();
		Polygon[] shotPolys = shot.polys();
		for (LandscapePolys landscapePoly : landscapePolys) {
			if (landscapePoly.hitAny(shotPolys, shotBounds)) {
				shot.deactivate();
				return;
			}
		}
	}
	
	public float landscapeX() {
		return this.landscapeX;
	}
	
	public Player player() {
		return player;
	}
	
	public List<Shot> shots() {
		return shots;
	}
	
	public void addShot() {
		Shot shot = new Shot(this);
		shot.moveTo(player.x(), player.y());
		shots.add(shot);
	}
}
