package ldtk.demo1;

import ldtk.State;
import ldtk.StateSelector;

public class App implements StateSelector {

	private Playing playing;
	private Menu menu;
	private State state;

	public App() {
		playing = new Playing(this);
		menu = new Menu(this);
		state = menu;
	}

	@Override
	public State select() {
		return state;
	}
	
	public void requestPlaying() {
		if (state == menu) {
			state = playing;
		}
	}
	
	public void requestMenu() {
		if (state == playing) {
			state = menu;
		}
	}
}
