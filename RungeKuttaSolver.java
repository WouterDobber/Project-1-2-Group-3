import java.util.ArrayList;

public class RungeKuttaSolver {
    public static StateInterface RKuttaSolve(RateOfChange castedRate, ArrayList<CelestialBody> celestialBodies, double step, double time, State state) {
        // new ArrayList to add the celestial body states to after a step

        ArrayList<CelestialBody> updatedCelestialBodies = new ArrayList<CelestialBody>();
        ODEFunctionInterface f = new NewtonsFunction();

        /*
     * Processes the shot using The Runge–Kutta Method.
     * Each of these slope estimates can be described verbally.
     * 1 ki1 is the slope at the beginning of the time step.
     * 2 ki2 is an estimate of the slope at the midpoint.
     * 3 ki3 is another estimate of the slope at the midpoint
     * 4 ki4 is an estimate of the slope at the endpoint
     * Sources used:   https://web.mit.edu/10.001/Web/Course_Notes/Differential_Equations_Notes/node5.html
     *                 https://academicjournals.org/article/article1380207159_Agbeboh%20et%20al.pdf
     *                 https://en.wikipedia.org/wiki/Runge%E2%80%93Kutta_methods
     *                 https://lpsa.swarthmore.edu/NumInt/NumIntFourth.html
     *
    */
        RateOfChange ki1 = (RateOfChange) f.call(time, state);
        RateOfChange ki2 = (RateOfChange) f.call(time + 0.5 * step, state.addMulReal(step * 0.5, ki1));
        RateOfChange ki3 = (RateOfChange) f.call(time + 0.5 * step, state.addMulReal(step * 0.5, ki2));
        RateOfChange ki4 = (RateOfChange) f.call(time + step, state.addMulReal(step, ki3));
        
        // The the next value of cange is determined by the present value of change plus the weighted avarege of four increments,
        // where each increment is the product of the size of the interval of time, and the estimated slope of step
        RateOfChange change = (ki1.addMul(2, ki2).addMul(2, ki3).addMul(1, ki4)).mul(1.0 / 6.0);
        
        //acceleration
        Vector3dInterface newPosition;
        Vector3dInterface newVelocity;
        
        //update
        return state.addMulReal(step, change);
    }
}

