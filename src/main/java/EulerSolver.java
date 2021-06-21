import java.util.ArrayList;

public class EulerSolver {
    /**
   * ODE Solver using Euler's first order method
   * wi+1= wi + h*f(ti, wi)
   * param castedRate
   * param celestialBodies
   * @param step size 
   * @return new state 
   */ 
    public static StateInterface EulerSolve(RateOfChange castedRate, ArrayList<CelestialBody> celestialBodies, double step){

        ArrayList<CelestialBody> updatedCelestialBodies = new ArrayList<CelestialBody>();

        for (int i = 0; i < celestialBodies.size(); i++) {
            Vector3dInterface newVelocity = celestialBodies.get(i).getVelocity().addMul(step, castedRate.accelerations.get(i));
            // position computed using the newly calculated velocities

            Vector3dInterface newPosition = celestialBodies.get(i).getLocation().addMul(step, celestialBodies.get(i).getVelocity());
            updatedCelestialBodies.add(new CelestialBody(celestialBodies.get(i).getName(),
                    celestialBodies.get(i).getImage(), celestialBodies.get(i).getMass(), newPosition, newVelocity));

        }
        return (StateInterface) new State(updatedCelestialBodies);

    }


}
