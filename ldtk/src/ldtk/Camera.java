package ldtk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * An orthographic camera, managed by the Kernel, that resizes itself whenever the window or screen is resized. Only
 * one camera can be active at any one time.
 */
public class Camera {
	
	private final String name;
	private final OrthographicCamera camera;
	private final ViewportScaler scaler;
	private final VirtualViewport viewport;
	private final Rectangle scissorRect;
	private final Cameras cameras;
	private boolean isScissored;
	private final Vector2 position;
	private float angle;
	private boolean isAngleDirty;
	private boolean isPositionDirty;
	private float zoom;
	private boolean isSizeDirty;
	private float pixelWidth;
	private float pixelHeight;

	Camera(Cameras cameras, String name, float width, float height, float minAspect, float maxAspect) {
		this.cameras = cameras;
		this.name = name;
		this.camera = new OrthographicCamera();
		this.scaler = createScaler(width, height, minAspect, maxAspect);
		this.viewport = new VirtualViewport(width, height);
		this.isScissored = true;
		this.position = new Vector2();
		this.zoom = 1.0f;
		this.scissorRect = new Rectangle();
		this.isSizeDirty = true;
	}

	/**
	 * Disposes of the camera.
	 */
	public void dispose() {
		cameras.dispose(name);
	}

	/**
	 * Returns true if scissoring is enabled for this camera.
	 * 
	 * @return true if scissored.
	 */
	public boolean isScissored() {
		return isScissored;
	}
	
	/**
	 * Enables/disables the camera's scissoring.
	 * 
	 * @param isScissored true to enable scissoring, false to disable.
	 */
	public void setScissored(boolean isScissored) {
		this.isScissored = isScissored;
	}
	
	/**
	 * Enables scissoring on this camera.
	 */
	public void enableScissoring() {
		setScissored(true);
	}
	
	/**
	 * Disables scissoring on this camera.
	 */
	public void disableScissoring() {
		setScissored(false);
	}

	/**
	 * Returns this camera's virtual width.
	 * 
	 * @return the virtual width.
	 */
	public float width() {
		return viewport.virtualWidth() / zoom;
	}

	/**
	 * Returns this camera's virtual height.
	 * 
	 * @return the virtual height.
	 */
	public float height() {
		return viewport.virtualHeight() / zoom;
	}

	/**
	 * Returns the width of the window that this camera is in.
	 * 
	 * @return the window's width.
	 */
	public float windowWidth() {
		return viewport.width() / zoom;
	}

	/**
	 * Returns the height of the window that this camera is in.
	 * 
	 * @return the window's height.
	 */
	public float windowHeight() {
		return viewport.height() / zoom;
	}

	/**
	 * Returns this camera's angle (in degrees).
	 * 
	 * @return the camera's angle.
	 */
	public float angle() {
		return angle;
	}

	/**
	 * Sets this camera to the given angle (in degrees).
	 * 
	 * @param degrees the angle.
	 */
	public void setAngle(float degrees) {
		this.angle = degrees % 360;
		isAngleDirty = true;
		update();
	}

	/**
	 * Returns the position of the centre of this camera in world space.
	 * 
	 * @return the position.
	 */
	public Vector2 position() {
		return position;
	}

	/**
	 * Returns the x coordinate of this camera's position in world space.
	 * 
	 * @return the x coordinate.
	 */
	public float x() {
		return position.x;
	}

	/**
	 * Returns the y coordinate of this camera's position in world space.
	 * 
	 * @return the y coordinate.
	 */
	public float y() {
		return position.y;
	}

	/**
	 * Moves the camera to the given position in world space.
	 * 
	 * @param x the new x coordinate.
	 * @param y the new y coordinate.
	 */
	public void moveTo(float x, float y) {
		position.set(x, y);
		isPositionDirty = true;
		update();
	}

	/**
	 * Returns this camera's zoom.
	 * 
	 * @return the zoom.
	 */
	public float zoom() {
		return zoom;
	}

	/**
	 * Sets this camera's zoom to the given value.
	 * 
	 * @param zoom this camera's zoom.
	 */
	public void setZoom(float zoom) {
		this.zoom = zoom;
		update();
	}

	/**
	 * Activates this camera if it isn't already enabled. All drawing to the batch will be drawn to this camera.
	 */
	public void activate() {
		cameras.activate(this);
		update();
	}

	/**
	 * Returns this camera's underlying OrthographicCamera.
	 * @return the underlying OrthographicCamera.
	 */
	public OrthographicCamera camera() {
		return camera;
	}
	
	private void update() {
		if (this != cameras.active()) return;
		rescaleIfDirty();
		updatePositionIfDirty();
		updateAngleIfDirty();
		updateZoom();
		scissorIfDesired();
		camera.update();
		Kernel.batch.setProjectionMatrix(camera.combined);
		setClean();
	}

	private void setClean() {
		isPositionDirty = false;
		isAngleDirty = false;
		isSizeDirty = false;
	}

	private void updateAngleIfDirty() {
		if (isAngleDirty) {
			camera.up.set(0, 1, 0);
			camera.rotate(angle);
		}
	}

	private void updatePositionIfDirty() {
		if (isPositionDirty) {
			camera.position.x = position.x;
			camera.position.y = position.y;
		}
	}

	private void updateZoom() {
		camera.zoom = 1.0f / zoom;
	}

	private void rescaleIfDirty() {
		if (isSizeDirty) {
			// Rescale the virtual viewport to the new window size.
			resizeViewport(pixelWidth, pixelHeight);

			// Tell the camera to match the viewport. Centre it and extend
			// it to fit the window.
			camera.setToOrtho(false, viewport.width(), viewport.height());
			camera.position.set(0f, 0f, 0f);

			updateScissorRect(viewport);

			isPositionDirty = true;
			isAngleDirty = true;
		}
	}

	private void resizeViewport(float width, float height) {
		viewport.setSize(scaler.scale(width, height));
	}

	private void scissorIfDesired() {
		// Scissor the viewport if desired.
		if (isScissored) {
			Gdx.gl.glEnable(GL10.GL_SCISSOR_TEST);
			Gdx.gl.glScissor((int) scissorRect.x, (int) scissorRect.y, (int) (scissorRect.width + 0.5f),
					(int) (scissorRect.height + 0.5f));
		} else {
			Gdx.gl.glDisable(GL10.GL_SCISSOR_TEST);
		}
	}

	private ViewportScaler createScaler(float virtualWidth, float virtualHeight, float minAspect, float maxAspect) {
		return new ViewportScaler(virtualWidth, virtualHeight, minAspect, maxAspect);
	}

	private void updateScissorRect(VirtualViewport viewport) {
		float h = Gdx.graphics.getHeight();
		float virtualAspect = viewport.virtualAspect();;
		float w = h * virtualAspect;
		if (w > Gdx.graphics.getWidth()) {
			w = Gdx.graphics.getWidth();
			h = w / virtualAspect;
		}
		float scissorX = (Gdx.graphics.getWidth() - w) / 2;
		float scissorY = (Gdx.graphics.getHeight() - h) / 2;
		float scissorWidth = w;
		float scissorHeight = h;

		scissorRect.set(scissorX, scissorY, scissorWidth, scissorHeight);
	}

	void doResize(int width, int height) {
		pixelWidth = width;
		pixelHeight = height;
		isSizeDirty = true;
	}
}
