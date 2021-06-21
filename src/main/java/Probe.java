import java.awt.Image;

import javax.swing.ImageIcon;

public class Probe implements ProbeSimulatorInterface {

	public Trajectory traj = new Trajectory();

	public Trajectory getTrajectory() {
		return traj;
	}

	Image rocket = new ImageIcon("src/solarimages/rocket.png").getImage();

	/*
	 * Simulate the solar system, including a probe fired from Earth at 00:00h on 1
	 * April 2020.
	 *
	 * @param p0 the starting position of the probe, relative to the earth's
	 * position.
	 *  
	 * @param v0 the starting velocity of the probe, relative to the earth's
	 * velocity.
	 * 
	 * @param ts the times at which the states should be output, with ts[0] being
	 * the initial time.
	 * 
	 * @return an array of size ts.length giving the position of the probe at each
	 * time stated, taken relative to the Solar System barycentre.
	 */
	@Override
	public Vector3dInterface[] trajectory(Vector3dInterface p0, Vector3dInterface v0, double[] ts) {
		SolarSystem universe = new SolarSystem(); // create solarSystem

		// change initial velocity to be relative to velocity from earth, same with
		// position:
		Vector3dInterface velocity = v0.add(universe.bodies.get(1).getVelocity());
		Vector3dInterface position = p0.add(universe.bodies.get(1).getLocation());

		// create celestialBody probe
		CelestialBody probe = new CelestialBody("Probe", rocket, 15000, position, velocity);

		universe.add(probe); // add probe to set of celestialBodies

		// create classes needed to calculate the state of the solar system in ts time
		ODEFunctionInterface function = new NewtonsFunction();
		SolarSystemSolver solver = new SolarSystemSolver();
		StateInterface state = (StateInterface) new State(universe.bodies);

		StateInterface[] EntireStateOfSolarSystem = solver.solve(function, state, ts);

		Vector3dInterface[] positionsOfProbe = new Vector3dInterface[EntireStateOfSolarSystem.length];
		for (int i = 0; i < EntireStateOfSolarSystem.length; i++) {
			State temporary = (State) EntireStateOfSolarSystem[i];
			positionsOfProbe[i] = temporary.celestialBodies.get(11).getLocation();
			// get vector position and add it in positionsOfProbe
		}

		return positionsOfProbe;
	}

	/*
	 * Simulate the solar system with steps of an equal size. The final step may
	 * have a smaller size, if the step-size does not exactly divide the solution
	 * time range.
	 *
	 * @param tf the final time of the evolution.
	 * 
	 * @param h the size of step to be taken
	 * 
	 * @return an array of size round(tf/h)+1 giving the position of the probe at
	 * each time stated, taken relative to the Solar System barycentre
	 */
	@Override
	public Vector3dInterface[] trajectory(Vector3dInterface p0, Vector3dInterface v0, double tf, double h) {
		SolarSystem universe = new SolarSystem(); // create solarSystem

		// change initial velocity to be relative to velocity from earth, same with
		// position:
		Vector3dInterface velocity = v0.add(universe.bodies.get(1).getVelocity());
		Vector3dInterface position = p0.add(universe.bodies.get(1).getLocation());

		// create celestialBody probe
		CelestialBody probe = new CelestialBody("Probe", rocket, 15000, position, velocity);

		universe.add(probe); // add probe to set of celestialBodies

		// create classes needed to calculate the state of the solar system with steps
		// h, from 0 to tf
		ODEFunctionInterface function = new NewtonsFunction();
		SolarSystemSolver solver = new SolarSystemSolver();
		StateInterface state = new State(universe.bodies);

		// ODEFunctionInterface f, StateInterface y0, double tf, double h)

		StateInterface[] StateOfSolarSystem = solver.solve(function, state, tf, h);

		Vector3dInterface[] positionsOfProbe = new Vector3dInterface[StateOfSolarSystem.length];
		for (int i = 0; i < StateOfSolarSystem.length; i++) {
			State temporary = (State) StateOfSolarSystem[i];
			positionsOfProbe[i] = temporary.celestialBodies.get(11).getLocation();
			// get vector position and add it in positionsOfProbe
		}

		return positionsOfProbe;
	}

	/*
	 * Calculates the closest distance between the probe and titan in a given time.
	 * 
	 * @param tf: total time
	 * 
	 * @param h: steps
	 * 
	 * @param p0: initial position
	 * 
	 * @param v0: initial velocity
	 * 
	 * @return shortest distance
	 */
	public double closestDistanceProbeTitan(Vector3dInterface p0, Vector3dInterface v0, double tf, double h) {
		SolarSystem universe = new SolarSystem(); // create solarSystem

		// position and velocity dont need to be changed, because they are already
		// relative to Earth , when the coordinates are created in ProbeCoordinates.
		// create celestialBody probe
		CelestialBody probe = new CelestialBody("Probe", rocket, 15000, p0, v0);

		universe.add(probe); // add rocket to set of celestialBodies

		// create classes needed to calculate the state of the solar system with steps
		// h, from 0 to tf
		ODEFunctionInterface function = new NewtonsFunction();
		SolarSystemSolver solver = new SolarSystemSolver();
		StateInterface state = new State(universe.bodies);

		StateInterface[] StateOfSolarSystem = solver.solve(function, state, tf, h);
		State initialState = (State) StateOfSolarSystem[0];

		double min = initialState.celestialBodies.get(6).getLocation()
				.dist(initialState.celestialBodies.get(11).getLocation()); // 6= titan, probe = 11
		double euclideanDistance;
		double time = 0;

		for (int i = 1; i < StateOfSolarSystem.length; i++) {
			initialState = (State) StateOfSolarSystem[i];
			euclideanDistance = initialState.celestialBodies.get(6).getLocation()
					.dist(initialState.celestialBodies.get(11).getLocation());
			if (min > euclideanDistance) {
				min = euclideanDistance;
				time = initialState.getTime();
			}
		}
		
		// System.out.println("Time: " + time);

		return min;
	}

/*
	 * Calculates the closest distance between the probe and titan in a given time.
	 *
	 * @param tf: total time
	 *
	 * @param h: steps
	 *
	 * @param p0: initial position
	 *
	 * @param v0: initial velocity
	 *
	 * @return shortest distance vector's absolute value
	 */


	public Vector closestVectorDistanceProbeTitan(Vector3dInterface p0, Vector3dInterface v0, double tf, double h) {
		SolarSystem universe = new SolarSystem(); // create solarSystem
		Vector closestDistanceReached=null;

		// position and velocity dont need to be changed, because they are already
		// relative to Earth , when the coordinates are created in ProbeCoordinates.
		// create celestialBody probe
		CelestialBody probe = new CelestialBody("Probe", rocket, 15000, p0, v0);

		universe.add(probe); // add rocket to set of celestialBodies

		// create classes needed to calculate the state of the solar system with steps
		// h, from 0 to tf
		ODEFunctionInterface function = new NewtonsFunction();
		SolarSystemSolver solver = new SolarSystemSolver();
		StateInterface state = new State(universe.bodies);

		StateInterface[] StateOfSolarSystem = solver.solve(function, state, tf, h);
		State initialState = (State) StateOfSolarSystem[0];

		double min = initialState.celestialBodies.get(6).getLocation()
				.dist(initialState.celestialBodies.get(11).getLocation()); // 6= titan, probe = 11
		double euclideanDistance;
		closestDistanceReached= (Vector) initialState.celestialBodies.get(6).getLocation().sub(initialState.celestialBodies.get(11).getLocation());

		double time = 0;

		for (int i = 1; i < StateOfSolarSystem.length; i++) {
			initialState = (State) StateOfSolarSystem[i];
			euclideanDistance = initialState.celestialBodies.get(6).getLocation()
					.dist(initialState.celestialBodies.get(11).getLocation());
			if (min > euclideanDistance) {
				min = euclideanDistance;
				closestDistanceReached = (Vector) initialState.celestialBodies.get(6).getLocation().sub(initialState.celestialBodies.get(11).getLocation());
				closestDistanceReached = closestDistanceReached.absolute();
				time = initialState.getTime();
			}
		}
		// System.out.println("Time: " + time);

		return closestDistanceReached;
	}
}
