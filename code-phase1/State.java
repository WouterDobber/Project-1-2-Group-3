
import java.util.ArrayList;

public class State implements StateInterface {
	ArrayList<CelestialBody> celestialBodies = new ArrayList<CelestialBody>(); // of all planets at a given time
	double time;
	/**
	 * Initialise the current state of the system
	 * 
	 * @param celestialBodies list containing all the celestial bodies
	 */
	public State(ArrayList<CelestialBody> celestialBodies) {
		this.celestialBodies = celestialBodies;

	}
	
	/*Sets the time of a State 
	 * @param time 
	 */
	public void setTime(double time) {
		this.time = time;
	}
	
	/*Gets the time at a given State 
	 * @return time 
	 */
	public double getTime() {
		return this.time;
	}

	

	/**
	 * Update a state to a new state, using Euler's method
	 *
	 * @param step The time-step of the update
	 * @param rate The average rate-of-change over the time-step. Has dimensions of
	 *             [state]/[time].
	 * @return The new state after the update. Required to have the same class as
	 *         'this'.
	 */
	@Override
	public StateInterface addMul(double step, RateInterface rate) {
		// for each planet, calculate the velocity and the position, using the
		// acceleration and Euler's method
		// castedRate is an object containing the accelerations
		RateOfChange castedRate = (RateOfChange) rate;

		// new ArrayList to add the celestial body states to after a step
		ArrayList<CelestialBody> updatedCelestialBodies = new ArrayList<CelestialBody>();
		for (int i = 0; i < celestialBodies.size(); i++) {

			// v(t2) = v(t1) + timeStep * a(t1)				need to check and change which acceleration is taking
			//x(t2) = x(t1) + timeStep * v(t1)
			// velocity computed using acceleration.
			Vector3dInterface newVelocity = celestialBodies.get(i).getVelocity().addMul(step, castedRate.accelerations.get(i));
			// position computed using the newly calculated velocities
			Vector3dInterface newPosition = celestialBodies.get(i).getLocation().addMul(step, celestialBodies.get(i).getVelocity());
			updatedCelestialBodies.add(new CelestialBody(celestialBodies.get(i).getName(),
					celestialBodies.get(i).getImage(), celestialBodies.get(i).getMass(), newPosition, newVelocity));
		}
		// the state after the step will be the new state, containing the updated
		// celestial bodies
		return (StateInterface) new State(updatedCelestialBodies);
	}

	/**
	 *
	 * @return a String containing all the celestial body information. name,
	 *         location and velocity.
	 */
	@Override
	public String toString() {
		String result = " ";

		// for-loop adding all bodies information to the String
		for (int i = 0; i < celestialBodies.size(); i++) {
			result += celestialBodies.get(i).toString() + '\n';
		}
		return result;
	}

}
