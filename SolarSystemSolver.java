public class SolarSystemSolver implements ODESolverInterface {
/*
	 * Solve the differential equation by taking multiple steps.
	 *
	 * @param f the function defining the differential equation dy/dt=f(t,y)
	 * 
	 * @param y0 the starting state
	 * 
	 * @param ts the times at which the states should be output, with ts[0] being
	 * the initial time
	 * 
	 * @return an array of size ts.length with all intermediate states along the
	 * path ASSUMING that ts is in order
	 */
	@Override
	public StateInterface[] solve(ODEFunctionInterface f, StateInterface y0, double[] ts) {
		NewtonsFunction castedF = (NewtonsFunction) f;
		State castedY = (State) y0;
		StateInterface[] array = new StateInterface[ts.length];
		array[0] = y0;

		int i = 1;
		double difference;
		while (i < ts.length) {
			array[i] = step(f, ts[i], array[i - 1], ts[i] - ts[i - 1]);
			i++;
		}
		return array;
	}

	/*
	 * Solve the differential equation by taking multiple steps of equal size,
	 * starting at time 0. The final step may have a smaller size, if the step-size
	 * does not exactly divide the solution time range
	 *
	 * @param f the function defining the differential equation dy/dt=f(t,y)
	 * 
	 * @param y0 the starting state
	 * 
	 * @param tf the final time
	 * 
	 * @param h the size of step to be taken
	 * 
	 * @return an array of size round(tf/h)+1 including all intermediate states
	 * along the path
	 */
	@Override
	public StateInterface[] solve(ODEFunctionInterface f, StateInterface y0, double tf, double h) {
		NewtonsFunction castedF = (NewtonsFunction) f;
		State castedY = (State) y0;
		// tf/h gives number of elements in array
		StateInterface[] array;
		if (tf%h==0){
			array = new StateInterface[(int) (tf / h) + 1];
		} else{
			 array = new StateInterface[(int) (tf / h) + 2];
		}
		array[0] = y0;
		int i = 0;
		while (i*h < tf) {
			i++;
			array[i] = step(f, i*h, array[i - 1], h);
		}
		if (i*h != tf){
			array[i] = step(f, tf, array[i - 1], tf-((i-1)*h));
		}
		return array;
	}

	/*
	 * Update rule for one step.
	 *
	 * @param f the function defining the differential equation dy/dt=f(t,y)
	 * 
	 * @param t the time
	 * 
	 * @param y the state
	 * 
	 * @param h the step size
	 * 
	 * @return the new state after taking one step
	 */
	@Override
	public StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h) {
		NewtonsFunction castedF = (NewtonsFunction) f;
		State castedY = (State) y;
		castedY.setTime(t);
		
		double mdot = getMdot(t);
		Vector dir = getDir(t, castedY);
		
		castedY.celestialBodies.get(11).setMdot(mdot);
		castedY.celestialBodies.get(11).setDir(dir);
		
		RateInterface acceleration = castedF.call(t, castedY);
		StateInterface resultFromChange = castedY.addMul(h, acceleration);
		return resultFromChange;
	}
	
	public double getMdot(double t){
		if (t < 1300){		//INITIAL LAUNCH
			return 1300;	
		}
		if (t < 37544500){	//In between Thrusts
			return 0;
		}
		if (t < 37545150){	//Slowing down towards Titan
			return 1300;
		}  
		if (t < 50556950){	//In between thrusts
			return 0;	
		}
		if (t < 50557950){	//Thrust back towards Earth
			return 600;	
		}
		if (t < 85240740){	//In between thrusts
			return 0;
		}
		if (t < 85241740){	//Final thrust towards the Earth
			return 1500;
		}
		else{
			return 0;
		}
	}
	
	public Vector getDir(double t, State castedY){
		if (t < 1400){
			return new Vector(0.3740213351271397,-0.919598006453899,-0.00253403391218171).normalize();
		}
		if (t < 37545150){
			Vector direction = (Vector) castedY.celestialBodies.get(11).getVelocity().mul(-1);
			return direction.normalize();
		}
		
		if (t < 50557950){
			return new Vector(-0.5113102628844748,0.8414114719792569,0.008968419065337402).normalize(); //Direction from Probe > Earth at t = 50556960
		}
		if (t < 85883960){
			
			Vector direction = (Vector) castedY.celestialBodies.get(1).getLocation().sub(castedY.celestialBodies.get(11).getLocation());
			return direction.normalize();
		}
		
		
		else return new Vector(0, 0, 0);
	}
}