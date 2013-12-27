package ldtk.demo1;

import ldtk.Kernel;
import ldtk.State;
import ldtk.Camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;

public class Playing extends State {

	private final App app;
	private Camera guiCam;
	private Camera gameCam;
	private float virtualWidth;
	private float virtualHeight;
	private WorldRenderer worldRenderer;
	private World world;
	private boolean isEscapePressed;
	private boolean isBackPressed;

	public Playing(App app) {
		this.app = app;
	}
	
	@Override
	public void enter() {
		Gdx.input.setCatchBackKey(true);
		virtualWidth = 640;
		virtualHeight = 360;
		guiCam = Kernel.cameras.create("guiCam", virtualWidth, virtualHeight);
		gameCam = Kernel.cameras.create("gameCam", virtualWidth, virtualHeight);
		world = new World();
		worldRenderer = new WorldRenderer(world, gameCam);
		worldRenderer.setup();
		Sound sound = Kernel.sounds.get("sounds/pickup");
		sound.play();
	}

	@Override
	public void exit() {
		Gdx.input.setCatchBackKey(false);
		gameCam.dispose();
		guiCam.dispose();
	}

	@Override
	public void update() {
		boolean wasEscapePressed = isEscapePressed;
		isEscapePressed = Gdx.input.isKeyPressed(Keys.ESCAPE);
		boolean wasBackPressed = isBackPressed;
		isBackPressed = Gdx.input.isKeyPressed(Keys.BACK);
		if ((wasEscapePressed && !isEscapePressed) || (wasBackPressed && !isBackPressed)) {
			app.requestMenu();
			return;
		}
		world.update();
	}

	@Override
	public void draw() {
		Gdx.gl.glDisable(GL10.GL_SCISSOR_TEST);
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		worldRenderer.draw();
		guiCam.activate();
	}
}
