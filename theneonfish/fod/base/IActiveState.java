package futuristicoffensiveanddefenseive.theneonfish.fod.base;

public interface IActiveState
{
	/**
	 * Gets the active state as a boolean.
	 * @return active state
	 */
	public boolean getActive();

	/**
	 * Sets the active state to a new value.
	 * @param active - new active state
	 */
	public void setActive(boolean active);

	/**
	 * Whether or not this block has a visual effect when it is on it's active state. Used for rendering.
	 * @return if the block has a visual effect in it's active state
	 */
	public boolean renderUpdate();

	public boolean lightUpdate();
}