public class Trajectory {
/*Class used for the genetic algorithm. 
 * 
 */
	private double fitness;
	private Vector[][] initVelocity;
	private Vector launchPosition;
	private Vector normalized;
	private Vector velocity;

	public Trajectory() {
		this.fitness = 1;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public Vector[][] getVelocity() {
		return initVelocity;
	}

	public void setVelocity(Vector[][] velocity) {
		initVelocity = velocity;
	}

	public Vector getLaunchPos() {
		return launchPosition;
	}

	public void setLaunchPos(Vector launch) {
		launchPosition = launch;
	}

	public Vector getNormalized() {
		return normalized;
	}

	public void setNormalized(Vector normalized) {
		this.normalized = normalized;
	}

	public void setFinalVelocity(Vector velocity) {
		this.velocity = velocity;
	}

	public Vector getFinalVelocity() {
		return velocity;
	}

}