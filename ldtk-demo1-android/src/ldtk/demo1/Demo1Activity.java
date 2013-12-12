package ldtk.demo1;

import ldtk.StateSelector;
import ldtk.Kernel;
import ldtk.demo1.App;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class Demo1Activity extends AndroidApplication {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = true;

		// To start an Android game just tell the kernel about your app so that it can switch between your game's
		// states, then start it all through initialize() just as you would any other LibGDX Android game.
		StateSelector gameStateSelector = new App();
		Kernel kernel = new Kernel(gameStateSelector);
		initialize(kernel, cfg);
    }
}