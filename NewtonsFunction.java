
import java.util.ArrayList;

/**
 * Class to implement Newton's Law of Gravitation
 */
public class NewtonsFunction implements ODEFunctionInterface {
	// G is the gravitational constant
	public final static double G = 6.67430E-11;

	/**
	 * Calculating the accelerations at the current state using Newton's Law of
	 * Gravitation F = G*m1m2(r^2) and F = ma to solve for a (acceleration)
	 *
	 * @param t time at which to evaluate function
	 * @param y state at which to evaluate function, will contain locations to
	 *          calculate distance (r)
	 * @return the accelerations at the current state, based on the masses and
	 *         distance of planets from each other.
	 */
	@Override
	public RateInterface call(double t, StateInterface y) {
		Vector totalForce;

		// Creating an empty Vector to add the total force of each body to, and an
		// ArrayList to store the accelerations
		ArrayList<Vector> accelerations = new ArrayList<Vector>();
		State castedY = (State) y;
		CelestialBody body1;
		CelestialBody body2;

		for (int i = 0; i < castedY.celestialBodies.size(); i++) {
			totalForce = new Vector(0, 0, 0);

			// calculating the total force for each body in the system, depends on all other
			// bodies
			// Hence, a for-loop going through all other bodies in the system
			body1 = castedY.celestialBodies.get(i);
			for (int j = 0; j < castedY.celestialBodies.size(); j++) {
				if (i != j) { // cant compare planet with itself
					// finding the distance between two bodies
					body2 = castedY.celestialBodies.get(j);
					Vector distance = (Vector) body2.getLocation().sub(body1.getLocation());

					double r = distance.norm();
					Vector normalizedAB = distance.normalize();
					// The formula for gravitational forces
					double f = G * (body1.getMass() * body2.getMass()) / (r * r);
					totalForce = (Vector) totalForce.add(normalizedAB.mul(f));
				}

			}
			// F = ma -> a = F/m
			// add this acceleration to the list of accelerations.
			accelerations.add(totalForce.divide(body1.getMass()));
		}

		return new RateOfChange(accelerations);
	}

}
