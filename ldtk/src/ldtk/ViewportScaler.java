package ldtk;

import com.badlogic.gdx.math.Vector2;

/**
 * The ViewportScaler is used to determine the dimensions that a viewport can take within a window of a given size
 * while still fitting within a range of aspect ratios.
 */
public class ViewportScaler {
	private final float virtualWidth;
	private final float virtualHeight;
	private final float minAspect;
	private final float maxAspect;
	private final Vector2 result;
	
	/**
	 * Creates a viewport scaler that will scale the viewport to accommodate the given dimensions. The aspect ratio
	 * is fixed to virtualWidth / virtualHeight.
	 * 
	 * @param virtualWidth the viewport's width in virtual pixels.
	 * @param virtualHeight the viewport's height in virtual pixels.
	 */
	public ViewportScaler(float virtualWidth, float virtualHeight) {
		this(virtualWidth, virtualHeight,
				virtualWidth / virtualHeight, 
				virtualWidth / virtualHeight);
	}
	
	/**
	 * Creates a viewport scaler that will scale the viewport to accommodate the given dimensions while allowing for
	 * a range of aspect ratios.
	 * 
	 * @param virtualWidth the viewport's width in virtual pixels.
	 * @param virtualHeight the viewport's height in virtual pixels.
	 * @param minAspect the minimum acceptable aspect ratio.
	 * @param maxAspect the maximum acceptable aspect ratio.
	 */
	public ViewportScaler(float virtualWidth, float virtualHeight, float minAspect, float maxAspect) {
		this.virtualWidth = virtualWidth;
		this.virtualHeight = virtualHeight;
		this.minAspect = minAspect;
		this.maxAspect = maxAspect;
		this.result = new Vector2();
	}
	
	/**
	 * Returns the dimensions that the viewport must take to fit in a screen of the given size.  
	 * @param screenWidth the screen's width.
	 * @param screenHeight the screen's height.
	 * @return the viewport's dimensions.
	 */
	public Vector2 scale(float screenWidth, float screenHeight) {
		float aspect = screenWidth / screenHeight;
		if (aspect <= maxAspect) {
			// Keep h constant, changing w. If aspect < minAspect then this results in letterboxing at the top and
			// bottom of the viewport.
			result.set(virtualHeight * Math.max(aspect,  minAspect), virtualHeight);
		}
		else {
			// Keep w constant, changing h. This results in letterboxing at the sides of the viewport.
			result.set(virtualWidth, virtualWidth / maxAspect);
		}
		return result;
	}
}
