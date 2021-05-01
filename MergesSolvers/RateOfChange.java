
import java.util.ArrayList;

public class RateOfChange implements RateInterface {

	ArrayList<Vector> accelerations = new ArrayList<Vector>(); // accelerations of all planets at a given time

	/**
	 * Gives the accelerations of the celestial bodies at the current state
	 * 
	 * @param accelerations List of acceleration vectors
	 */
	public RateOfChange(ArrayList<Vector> accelerations) {
		this.accelerations = accelerations;
	}
}
