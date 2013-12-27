package ldtk;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Rectangle;

/**
 * A font, managed by the kernel.
 */
public class Font {

	private final Fonts fonts;
	private final String name;
	private final BitmapFont bitmapFont;
	private final Rectangle bounds;
	private final float height;

	Font(Fonts fonts, String name, BitmapFont bitmapFont) {
		this.fonts = fonts;
		this.name = name;
		this.bitmapFont = bitmapFont;
		this.bounds = new Rectangle();
		this.height = bitmapFont.getLineHeight();
		this.bounds.height = height;
	}

	/**
	 * Draws a string with this font.
	 * 
	 * @param text the string being drawn.
	 * @param x the x coordinate of the bottom left of the text.
	 * @param y the y coordinate of the bottom left of the text.
	 * @param color
	 */
	public void draw(String text, float x, float y, Color color) {
		bitmapFont.setColor(color);
		bitmapFont.draw(Kernel.batch, text, x, y + height);
	}

	/**
	 * Returns the size of the given text if drawn in this font.
	 * 
	 * @param text the string.
	 * @return the bounds of the text.
	 */
	public Rectangle bounds(String text) {
		TextBounds textBounds = bitmapFont.getBounds(text);
		bounds.width = textBounds.width;
		return bounds;
	}
	
	/**
	 * Returns the height of the font.
	 */
	public float height() {
		return height;
	}
	
	/**
	 * Disposes of this font.
	 */
	public void dispose() {
		fonts.dispose(name);
	}

	/**
	 * Returns the underlying BitmapFont.
	 * 
	 * @return the underlying BitmapFont.
	 */
	public BitmapFont bitmapFont() {
		return bitmapFont;
	}
}
