package ldtk;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * This is the kernel that drives programs that are based on LDTK. Amongst other things it provides a source of time, a
 * single sprite batch, simple, convention-based asset management, and a managed camera class that automatically
 * resizes itself to fit the display. It also provides a state machine that handles updating and drawing of
 * client-provided states.
 * 
 * You would not typically override Kernel, but with work it could be made more open to modification.
 */
public class Kernel implements ApplicationListener {

	/**
	 * A time source available to all LDTK-based programs.
	 */
	public static Time time;
	
	/**
	 * A sprite batch available to all LDTK-based programs.
	 */
	public static SpriteBatch batch;

	/**
	 * The means by which client code obtains a Camera.
	 */
	public static Cameras cameras = new Cameras();

	/**
	 * The means by which client code obtains a Sound.
	 */
	public static Sounds sounds = new Sounds();

	/**
	 * The means by which client code obtains an Image.
	 */
	public static Images images = new Images();

	/**
	 * The means by which client code obtains a Font.
	 */
	public static Fonts fonts = new Fonts();

	/**
	 * The means by which client code can load and unload groups of assets.
	 */
	public static Assets assets = new Assets(images, fonts, sounds);
	
	private StateSelector stateSelector;
	private State currentState;
	
	/**
	 * Creates the LDTK kernel.
	 * 
	 * @param stateSelector an object that returns the desired state.
	 */
	public Kernel(StateSelector stateSelector) {
		this.stateSelector = stateSelector;
	}

	@Override
	public void create() {
		time = new Time();
		batch = createSpriteBatch();
		createCamera();
		assets.loadDefaults();
	}

	protected Camera createCamera() {
		float virtualWidth = 800;
		float virtualHeight = 600;
		return cameras.create("default", virtualWidth, virtualHeight);
	}

	protected SpriteBatch createSpriteBatch() {
		return new SpriteBatch();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {
		Kernel.time.delta = Gdx.graphics.getDeltaTime();
		Kernel.time.time += Kernel.time.delta;

	    State nextState = stateSelector.select();
	    if (currentState != nextState) {
	    	if (currentState != null) {
	    		currentState.exit();
	    	}
	    	if (nextState != null) {
	    		nextState.enter();
	    	}
	        currentState = nextState;
	    }
	    if (currentState != null) {
	    	currentState.update();
	    }

		Gdx.gl.glDisable(GL10.GL_SCISSOR_TEST);
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		Kernel.batch.begin();

		if (currentState != null) {
			currentState.draw();
		}
		
		Kernel.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		cameras.resize(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
