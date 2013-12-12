package ldtk;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * An image, managed by the kernel.
 */
public class Image {
	
	private final Images images;
	private final String name;
	private final TextureRegion region;
	private final float width;
	private final float height;
	private final float halfWidth;
	private final float halfHeight;

	Image(Images images, String name, TextureRegion region) {
		this.images = images;
		this.name = name;
		this.region = region;
		this.width = region.getRegionWidth();
		this.height = region.getRegionHeight();
		this.halfWidth = width / 2.0f;
		this.halfHeight = height / 2.0f;
	}

	/**
	 * Draws this image at the given coordinates.
	 * 
	 * @param x the x coordinate of the middle of the image.
	 * @param y the y coordinate of the middle of the image.
	 */
	public void draw(float x, float y) {
		Kernel.batch.draw(region, x - halfWidth, y - halfHeight);
	}

	/**
	 * Draws this image at the given coordinates, rotated anti-clockwise around its centre by the given amount.
	 * 
	 * @param x the x coordinate of the middle of the image.
	 * @param y the y coordinate of the middle of the image.
	 * @param ccwDegrees the angle of rotation anti-clockwise around the centre in degrees.
	 */
	public void draw(float x, float y, float ccwDegrees) {
		Kernel.batch.draw(region, x - halfWidth, y - halfHeight, halfWidth, halfHeight, width, height, 1.0f, 1.0f,
				ccwDegrees);
	}
	
	/**
	 * Returns this image's texture region.
	 * 
	 * @return this image's texture region.
	 */
	public TextureRegion region() {
		return this.region;
	}
	
	/**
	 * Disposes of this image.
	 */
	public void dispose() {
		images.dispose(name);
	}
}
