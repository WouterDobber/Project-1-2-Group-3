
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
		StateInterface[] array = new StateInterface[(int) (tf / h) + 1];
		array[0] = y0;
		int i = 1;
		while (i < array.length) {
			array[i] = step(f, i*h, array[i - 1], h);
			i++;
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
		RateInterface acceleration = castedF.call(t, castedY);
		StateInterface resultFromChange = castedY.addMul(h, acceleration);
		return resultFromChange;
	}

}
