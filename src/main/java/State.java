
import java.sql.Array;
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

	public State(ArrayList<CelestialBody> celestialBodies, double time) {
		this.celestialBodies = celestialBodies;
		this.time= time;
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

	public ArrayList<CelestialBody> getbodies(){
		return this.celestialBodies;
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
		RateOfChange castedRate = (RateOfChange) rate; //acceleration
		State result;

		//Euler
		//result = (State) EulerSolver.EulerSolve(castedRate,celestialBodies, step );

		//Runge Kutta
		//result = (State) RungeKuttaSolver.RKuttaSolve(castedRate,celestialBodies, step, this.time , this);


		//Verlet Solver
		result = (State) VerletSolver.VerletSolve(castedRate,celestialBodies, step );

		result.setTime(this.time+ step);
		return result;
	}

	public StateInterface addMulReal(double step, RateInterface r) {
		RateOfChange rate = (RateOfChange) r;

		ArrayList<Vector3dInterface> newVelocities = new ArrayList<>();
		ArrayList<Vector3dInterface> newPositions = new ArrayList<>();

		for (int i = 0; i < celestialBodies.size(); i++) {
			newPositions.add( (celestialBodies.get(i).getLocation().addMul(step,celestialBodies.get(i).getVelocity()))); //p(t+1)=p(t)+h*vel
			try {
				newVelocities.add((celestialBodies.get(i).
						getVelocity().
						addMul(step, rate.
								getRates().
								get(i)))); // vel(t+1)=p(t)+h*acc
			} catch (Exception e){
				System.out.println("exception catched");
			}

		}
		ArrayList<CelestialBody> updatedBodies = new ArrayList<>();
		for (int i =0; i<celestialBodies.size(); i++){
			updatedBodies.add( celestialBodies.get(i).duplicate());
			updatedBodies.get(i).setVelocity(newVelocities.get(i));
			updatedBodies.get(i).setLocation(newPositions.get(i));
		}

		return new State( updatedBodies, getTime()+ step);

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
