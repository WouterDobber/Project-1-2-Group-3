
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
		// for each planet, calculate the velocity and the position
		// castedRate is an object containing the accelerations
		RateOfChange castedRate = (RateOfChange) rate; //acceleration
		StateInterface result;

		//Euler
		result = EulerSolver.EulerSolve(castedRate,celestialBodies, step );

		//Runge Kutta
		//result = RungeKuttaSolver.RKuttaSolve(castedRate,celestialBodies, step );


		//Verlet Solver
		//result = VerletSolver.VerletSolve(castedRate,celestialBodies, step );
		return result;
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
