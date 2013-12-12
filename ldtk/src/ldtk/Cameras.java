package ldtk;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;


public class Cameras {
	// All of the cameras.
	private Map<String, Camera> cameras = new HashMap<String, Camera>(); 

	// The currently active camera.
	private Camera active;

	/**
	 * Returns the currently active camera.
	 * 
	 * @return the currently active camera.
	 */
	public Camera active() {
		return active;
	}

	/**
	 * Activates the given camera.
	 * 
	 * @param camera the camera.
	 */
	public void activate(Camera camera) {
		this.active = camera;
	}

	/**
	 * Creates a camera with the same dimensions as the containing window / screen.
	 */
	public Camera create(String name) {
		return create(name, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	/**
	 * Creates a camera with the given dimensions.
	 * 
	 * @param width the camera's width.
	 * @param height the camera's height.
	 * @return the camera.
	 */
	public Camera create(String name, float width, float height) {
		return create(name, width, height, width / height, width / height);
	}

	/**
	 * Creates a camera with the given dimensions, allowing for a range of aspect ratios. 
	 * 
	 * @param width the camera's desired width.
	 * @param height the camera's desired height.
	 * @param minAspect the minimum acceptable aspect ratio.
	 * @param maxAspect the maximum acceptable aspect ratio.
	 * @return the camera.
	 */
	public Camera create(String name, float width, float height, float minAspect, float maxAspect) {
		Camera camera = new Camera(this, name, width, height, minAspect, maxAspect);
		cameras.put(name, camera);
		return camera;
	}

	/**
	 * Obtains a camera by name.
	 * 
	 * @param name the camera's name.
	 * @return the camera.
	 */
	public Camera get(String name) {
		return cameras.get(name);
	}

	/**
	 * Disposes of a camera.
	 * 
	 * @param camera the camera that is being disposed.
	 */
	public void dispose(String name) {
		cameras.remove(name);
	}

	/**
	 * Informs all cameras that the window (or screen) has been resized and that they should resize themselves next
	 * time they are activated.
	 * 
	 * @param width the new window width.
	 * @param height the new window height.
	 */
	public void resize(int width, int height) {
		for (Camera camera : cameras.values()) {
			camera.doResize(width, height);
		}
	}
}
