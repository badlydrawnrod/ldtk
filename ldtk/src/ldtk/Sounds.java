package ldtk;

import java.util.HashMap;
import java.util.Map;


public class Sounds {
	// All of the sounds.
	private Map<String, Sound> sounds = new HashMap<String, Sound>(); 

	/**
	 * Obtains a sound by name.
	 * 
	 * @param name the name of the sound.
	 * @return the sound.
	 */
	public Sound get(String name) {
		return sounds.get(name);
	}
	
	/**
	 * Adds a named sound to the kernel.
	 * 
	 * @param name the name of the sound being added.
	 * @param gdxSound the underlying libGDX sound.
	 */
	public void add(String name, com.badlogic.gdx.audio.Sound gdxSound) {
		sounds.put(name, new Sound(this, name, gdxSound));
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
