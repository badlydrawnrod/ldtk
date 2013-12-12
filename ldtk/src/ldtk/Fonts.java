package ldtk;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.BitmapFont;


public class Fonts {
	// All of the fonts.
	private Map<String, Font> fonts = new HashMap<String, Font>(); 

	/**
	 * Obtains a font by name.
	 * 
	 * @param name the name of the font.
	 * @return the font.
	 */
	public Font get(String name) {
		return fonts.get(name);
	}
	
	/**
	 * Adds a named font to the kernel.
	 * 
	 * @param name the name of the font being added.
	 * @param font the font's underlying BitmapFont.
	 */
	public void add(String name, BitmapFont font) {
		fonts.put(name, new Font(this, name, font));
	}

	/**
	 * Disposes of a font.
	 * 
	 * @param name the font that is being disposed.
	 */
	public void dispose(String name) {
		fonts.remove(name);
	}
}
