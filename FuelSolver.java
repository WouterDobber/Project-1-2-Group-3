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
	
	public Vector solve(double mdot){
		
		double usedFuel = mdot;
		
		//System.out.println(body1.getDir().toString());
		
			//System.out.println("Used Fuel: " + usedFuel);
		if (body1.canThrust(usedFuel)){
				//System.out.println("Enough fuel!");
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
				//System.out.println("Not enough fuel!");
			return new Vector(0, 0, 0);	
		}
		
	}
	
	public double energy(){
		return getKinetic() - getPotential();
	}
	
	public double getKinetic(){
		double Kin = 0.5 * body1.getMass() * (body1.getVelocity().norm()*body1.getVelocity().norm());
		return Kin;
	}
	
	public double getPotential(){
		Vector distance = (Vector) body2.getLocation().sub(body1.getLocation());
		double r = distance.norm();
		
		double Pot = (-G * body2.getMass() * body1.getMass())/r;	
		return Pot;
	}
}