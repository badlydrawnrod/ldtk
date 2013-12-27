package ldtk;

/**
 * Streaming music, managed by the kernel.
 */
public class Tune implements com.badlogic.gdx.audio.Music {
	
	private final Tunes tunes;
	private final String name;
	private final com.badlogic.gdx.audio.Music music;

	Tune(Tunes tunes, String name, com.badlogic.gdx.audio.Music music) {
		this.tunes = tunes;
		this.name = name;
		this.music = music;
	}

	/**
	 * Returns this sound's underlying libGDX Music.
	 * 
	 * @return the underlying libGDX music.
	 */
	public com.badlogic.gdx.audio.Music music() {
		return music;
	}

	@Override
	public void play() {
		music.play();
	}

	@Override
	public void pause() {
		music.pause();
	}

	@Override
	public void stop() {
		music.stop();
	}

	@Override
	public boolean isPlaying() {
		return music.isPlaying();
	}

	@Override
	public void setLooping(boolean isLooping) {
		music.setLooping(isLooping);
	}

	@Override
	public boolean isLooping() {
		return music.isLooping();
	}

	@Override
	public void setVolume(float volume) {
		music.setVolume(volume);
	}

	@Override
	public float getVolume() {
		return music.getVolume();
	}

	@Override
	public void setPan(float pan, float volume) {
		music.setPan(pan, volume);
	}

	@Override
	public float getPosition() {
		return music.getPosition();
	}

	@Override
	public void dispose() {
		tunes.dispose(name);
		music.dispose();
	}

	@Override
	public void setOnCompletionListener(OnCompletionListener listener) {
		music.setOnCompletionListener(listener);
	}
}
