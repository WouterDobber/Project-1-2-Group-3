
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

		/**EULER
		for (int i = 0; i < celestialBodies.size(); i++) {
			// v(t2) = v(t1) + timeStep * a(t1)				need to check which acceleration is taking
			//x(t2) = x(t1) + timeStep * v(t1)
			// velocity computed using acceleration.
			Vector3dInterface newVelocity = celestialBodies.get(i).getVelocity().addMul(step, castedRate.accelerations.get(i));
			// position computed using the newly calculated velocities

			Vector3dInterface newPosition = celestialBodies.get(i).getLocation().addMul(step, celestialBodies.get(i).getVelocity());
			updatedCelestialBodies.add(new CelestialBody(celestialBodies.get(i).getName(),
					celestialBodies.get(i).getImage(), celestialBodies.get(i).getMass(), newPosition, newVelocity));

		}
	*/

		/**Runge Kutta
		for(int i=0; i < celestialBodies.size(); i++) {

			Vector k1 = new Vector(celestialBodies.get(i).getVelocity().getX(), celestialBodies.get(i).getVelocity().getY(), celestialBodies.get(i).getVelocity().getZ());
			//Vector k1 = (Vector) celestialBodies.get(i).getVelocity().mul(step);
			Vector k2 = (Vector) castedRate.accelerations.get(i).mul(step / 2.0).add(k1).add(k1.divide(2.0));
			Vector k3 = (Vector) castedRate.accelerations.get(i).mul(step / 2.0).add(k1).add(k2.divide(2.0));
			Vector k4 = (Vector) castedRate.accelerations.get(i).mul(step).add(k1).add(k3);
			Vector change = (Vector) k1.add(k2.mul(2.0)).add(k3.mul(2.0)).add(k4);

			change = (Vector) change.divide(6.0).mul(step / 2.0);

			Vector3dInterface newPosition = celestialBodies.get(i).getLocation().add(change);
			Vector3dInterface newVelocity = celestialBodies.get(i).getVelocity().add(castedRate.accelerations.get(i).mul(step ));
			updatedCelestialBodies.add(new CelestialBody(celestialBodies.get(i).getName(),
					celestialBodies.get(i).getImage(), celestialBodies.get(i).getMass(), newPosition, newVelocity));

		}

		*/

		//Verlet Solver
		for(int i = 0; i < celestialBodies.size(); i++) {
			Vector3dInterface curPosition = celestialBodies.get(i).getLocation();
			Vector3dInterface curVelocity = celestialBodies.get(i).getVelocity();
			Vector3dInterface curAcceleration = castedRate.accelerations.get(i);

			Vector3dInterface nextPosition = curPosition.addMul(step, curVelocity).addMul(step * step / 2.0, curAcceleration);
			Vector3dInterface intermediateVelocity = curVelocity.addMul(step / 2.0, curAcceleration);
			// omit acceleration forces step as we always calculate it before calling update with force method
			updatedCelestialBodies.add(new CelestialBody(celestialBodies.get(i).getName(),
					celestialBodies.get(i).getImage(), celestialBodies.get(i).getMass(), nextPosition, intermediateVelocity));
		}

		NewtonsFunction calculateAccelerations = new NewtonsFunction();
		StateInterface newState= (StateInterface) new State(updatedCelestialBodies);

		//new accelerations
		RateOfChange castedRate2 = (RateOfChange) calculateAccelerations.call(0, newState ); //t is not used in method, so sending 0


		// recoil that for now all celestial objects store "intermediate" velocity, so we have to update it once again
		// in order to get velocity as of end of h time jump
		for (int i = 0; i < updatedCelestialBodies.size(); i++) {
			Vector3dInterface curVelocity = updatedCelestialBodies.get(i).getVelocity();
			Vector3dInterface curAcceleration = castedRate2.accelerations.get(i);

			Vector3dInterface nextVelocity = curVelocity.addMul(step / 2.0, curAcceleration);
			updatedCelestialBodies.get(i).setVelocity(nextVelocity);
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
