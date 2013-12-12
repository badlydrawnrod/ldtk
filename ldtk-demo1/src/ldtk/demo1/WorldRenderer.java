package ldtk.demo1;

import ldtk.Kernel;
import ldtk.Camera;
import ldtk.Image;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;

public class WorldRenderer {

	private static final float LERP = 1.0f;

	private World world;
	private Texture backgroundTexture;
	private Texture foregroundTexture;
	private Texture midgroundTexture;
	private LandscapeRenderer[] landscapeRenderers;
	private Camera gameCam;
	private float camX;


	public WorldRenderer(World world, Camera gameCam) {
		this.world = world;
		this.gameCam = gameCam;
		this.camX = 0.0f;
	}

	public void setup() {
		// Load the textures (CC0 licensed textures from http://opengameart.org) and make sure that they wrap.
		backgroundTexture = Kernel.images.get("textures/stones").region().getTexture();
		backgroundTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		midgroundTexture = Kernel.images.get("textures/building").region().getTexture();
		midgroundTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		foregroundTexture = Kernel.images.get("textures/grass").region().getTexture();
		foregroundTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

		// Create the landscape renderers.
		Landscape[] landscapes = world.landscapes();
		landscapeRenderers = new LandscapeRenderer[landscapes.length];
		landscapeRenderers[World.BACKGROUND_LAYER] = new LandscapeRenderer(landscapes[World.BACKGROUND_LAYER],
				backgroundTexture, Color.DARK_GRAY);
		landscapeRenderers[World.MIDGROUND_LAYER] = new LandscapeRenderer(landscapes[World.MIDGROUND_LAYER],
				midgroundTexture, Color.GRAY);
		landscapeRenderers[World.FOREGROUND_LAYER] = new LandscapeRenderer(landscapes[World.FOREGROUND_LAYER],
				foregroundTexture, Color.WHITE);
	}

	public void draw() {
		Image image = Kernel.images.get("atlases/png1/RobotScan3");
		gameCam.activate();

		camX += (world.landscapeX() - camX) * LERP;

		for (int i = 0, n = 1 << (landscapeRenderers.length - 1); i < landscapeRenderers.length - 1; i++, n >>= 1) {
			LandscapeRenderer landscapeRenderer = landscapeRenderers[i];
			gameCam.moveTo(camX / n, 0);
			landscapeRenderer.draw(gameCam);
		}

		gameCam.moveTo(camX, 0);
		image = Kernel.images.get("atlases/pack/PlayerShot01");
		for (Shot shot : world.shots()) {
			image.draw(shot.x(), shot.y());
		}

		image = Kernel.images.get("atlases/pack/RobotScan1");
		Player player = world.player();
		image.draw(player.x(), player.y());

		LandscapeRenderer landscapeRenderer = landscapeRenderers[World.FOREGROUND_LAYER];
		gameCam.moveTo(camX, 0);
		landscapeRenderer.draw(gameCam);
	}
}
