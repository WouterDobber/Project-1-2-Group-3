import java.util.ArrayList;

public class RateOfChange implements RateInterface {

	public ArrayList<Vector> accelerations = new ArrayList<Vector>(); // accelerations of all planets at a given time

	/**
	 * Gives the accelerations of the celestial bodies at the current state
	 * 
	 * @param accelerations List of acceleration vectors
	 */
	public RateOfChange(ArrayList<Vector> accelerations) {
		this.accelerations = accelerations;
	}

	/**
	 * Gives the RateOfChange of the celestial bodies at the current state after updating them by a multiplication and addition 
	 * 
	 * @param other 
	 * @param scalar 
	 */
	public RateOfChange addMul(double scalar, RateOfChange other) {
		ArrayList<Vector> addRates = new ArrayList<>();
		ArrayList<Vector> otherRates = other.getRates();

		for(int i = 0; i < otherRates.size(); i ++) {
			addRates.add ((Vector) this.accelerations.get(i).add(otherRates.get(i).mul(scalar)));
		}
		return new RateOfChange(addRates);
	}
	
	/**
	 * Gives the RateOfChange of the celestial bodies at the current state after updating them by a scalar  
	 *
	 * @param scalar 
	 */
	public RateOfChange mul(double scalar) {
		ArrayList<Vector> mulRates = new ArrayList<>();
		for(int i = 0; i < accelerations.size(); i ++) {
			mulRates.add((Vector) this.accelerations.get(i).mul(scalar));
		}
		return new RateOfChange(mulRates);
	}

	/**
	* Getter: Returns the acelerations 
	* @return accelerations 
	*/ 
	public ArrayList<Vector> getRates() {
		return accelerations;
	}

}