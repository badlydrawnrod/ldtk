package ldtk.demo1;

import ldtk.StateSelector;
import ldtk.Kernel;
import ldtk.demo1.App;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Demo1 {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "LDTK - Demo 1";
		cfg.useGL20 = true;
		cfg.width = 800;
		cfg.height = 480;

		// To start a desktop game just tell the kernel about your app so that it can switch between your game's
		// states, then start it all through LwglApplication just as you would any other LibGDX desktop game.
		StateSelector gameStateSelector = new App();
		Kernel kernel = new Kernel(gameStateSelector);
		new LwjglApplication(kernel, cfg);
	}
}
