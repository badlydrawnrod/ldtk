package ldtk;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Images {
	// All of the images.
	private Map<String, Image> sounds = new HashMap<String, Image>(); 

	/**
	 * Obtains an image by name.
	 * 
	 * @param name the name of the image.
	 * @return the image.
	 */
	public Image get(String name) {
		return sounds.get(name);
	}
	
	/**
	 * Adds a named image to the kernel.
	 * 
	 * @param name the name of the image being added.
	 * @param region the image's texture region.
	 */
	public void add(String name, TextureRegion region) {
		sounds.put(name, new Image(this, name, region));
	}

	/**
	 * Disposes of a sound.
	 * 
	 * @param name the sound that is being disposed.
	 */
	public void dispose(String name) {
		sounds.remove(name);
	}
}
