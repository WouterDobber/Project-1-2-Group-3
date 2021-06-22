import java.util.Random;

public class FuelSolver{
	
	public double maxThrust = 3000000;		// 30 MN
	public double exhaustVelocity = 20000; 	// in m/s
	public State castedY;
	public CelestialBody body1;		//Rocket
	public CelestialBody body2;		//Titan
	public final static double G = 6.67430E-11;

	public FuelSolver(StateInterface y){
		castedY = (State) y;
		body1 = castedY.celestialBodies.get(11);
		body2 = castedY.celestialBodies.get(6);
	}
	
   /**
	* @param mdot is the rate of mass burned over time. It is used to calculate the force and the amount of mass used at every interval
	* @return the acceleration vector, to be added to the probe's gravitational acceleration in the NewtonsFunction class
	*/
	
	public Vector solve(double mdot){
		
		double usedFuel = mdot;
		
		if (body1.canThrust(usedFuel)){
				
			//F = mdot * Ve
			double force = exhaustVelocity * mdot;
			
			Vector totalForce = (Vector) body1.getDir().mul(force);
			
			//Set the fuel so that it runs the calculations with half of the fuel consumed,
			//So that the force calculations are done with the average amount of fuel for this timestep
			body1.addMass(-0.5*usedFuel);
		
			Vector accel = totalForce.divide(body1.getMass());
			
			//Subtract the other half of the used fuel from the fuel mass
			body1.addMass(-0.5*usedFuel);
			
			return accel;
		}
		else{
			return new Vector(0, 0, 0);	
		}
		
	}
	
   /**
	*	@return the energy difference between the kinetic energy and the potential energy, used for finding a planet's orbit
	*/
	
	public double energy(){
		return getKinetic() - getPotential();
	}
	
   /**
	*	@return the probe's kinetic energy, based off it's mass and velocity
	*/
	
	public double getKinetic(){
		double Kin = 0.5 * body1.getMass() * (body1.getVelocity().norm()*body1.getVelocity().norm());
		return Kin;
	}
	
   /**
	*	@return the probe's potential energy, based off it's mass, the planet's mass and the distance between the two
	*/
	
	public double getPotential(){
		Vector distance = (Vector) body2.getLocation().sub(body1.getLocation());
		double r = distance.norm();
		
		double Pot = (-G * body2.getMass() * body1.getMass())/r;	
		return Pot;
	}
}