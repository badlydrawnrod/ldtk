package ldtk;

/**
 * Classes implementing this interface return the desired state. If the state is different to the current state when
 * the kernel runs its update then the kernel calls the current state's exit() method, sets the current state to the
 * new state, then calls that state's enter() method.
 */
public interface StateSelector {
	/**
	 * Returns the state that will be in effect from the start of the next tick.
	 * 
	 * @return the state.
	 */
	public State select();
}
