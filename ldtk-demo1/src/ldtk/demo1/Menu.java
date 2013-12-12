package ldtk.demo1;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import ldtk.Camera;
import ldtk.Font;
import ldtk.Kernel;
import ldtk.State;

public class Menu extends State {

	private final App app;
	private Camera guiCam;
	private Font font;
	private boolean isSpacePressed;
	private boolean isEscapePressed;

	public Menu(App app) {
		this.app = app;
	}
	
	@Override
	public void enter() {
		guiCam = Kernel.cameras.create("guiCam");
		font = Kernel.fonts.get("fonts/consolas32");
	}

	@Override
	public void exit() {
		guiCam.dispose();
	}

	@Override
	public void update() {
		boolean wasSpacePressed = isSpacePressed;
		isSpacePressed = Gdx.input.isKeyPressed(Keys.SPACE);
		if ((wasSpacePressed && !isSpacePressed) || Gdx.input.justTouched()) {
			app.requestPlaying();
		}
		boolean wasEscapePressed = isEscapePressed;
		isEscapePressed = Gdx.input.isKeyPressed(Keys.ESCAPE);
		if (wasEscapePressed && !isEscapePressed) {
			Gdx.app.exit();
		}
	}

	@Override
	public void draw() {
		guiCam.activate();
		ApplicationType appType = Gdx.app.getType();
		String helloWorld = appType == ApplicationType.Android
				? "Tap to start"
				: "Press [space] to start";
		Rectangle bounds = font.bounds(helloWorld);
		font.draw(helloWorld, -bounds.width / 2, -bounds.height / 2, Color.WHITE);
		String bottomLeft = "bottom left";
		font.draw(bottomLeft, -guiCam.width() / 2, -guiCam.height() / 2, Color.WHITE);
		String topLeft = "top left";
		font.draw(topLeft, -guiCam.width() / 2, guiCam.height() / 2 - font.height(), Color.WHITE);
		String topRight = "top right";
		bounds = font.bounds(topRight);
		font.draw(topRight, guiCam.width() / 2 - bounds.width, guiCam.height() / 2 - bounds.height, Color.WHITE); 
		String bottomRight = "bottom right";
		bounds = font.bounds(bottomRight);
		font.draw(bottomRight, guiCam.width() / 2 - bounds.width, -guiCam.height() / 2, Color.WHITE);
	}
}