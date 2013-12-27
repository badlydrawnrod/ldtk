package ldtk;

import java.util.HashMap;
import java.util.Map;


public class Tunes {
	// All of the music.
	private Map<String, Tune> tunes = new HashMap<String, Tune>(); 

	/**
	 * Obtains a tune by name.
	 * 
	 * @param name the name of the sound.
	 * @return the sound.
	 */
	public Tune get(String name) {
		return tunes.get(name);
	}
	
	/**
	 * Adds a named tune to the kernel.
	 * 
	 * @param name the name of the tune being added.
	 * @param gdxSound the underlying libGDX music.
	 */
	public void add(String name, com.badlogic.gdx.audio.Music gdxMusic) {
		tunes.put(name, new Tune(this, name, gdxMusic));
	}

	/**
	 * Disposes of a tune.
	 * 
	 * @param name the tune that is being disposed.
	 */
	public void dispose(String name) {
		tunes.remove(name);
	}
}
