import java.util.ArrayList;

public class EulerSolver {

    public static StateInterface EulerSolve(RateOfChange castedRate, ArrayList<CelestialBody> celestialBodies, double step){
        // new ArrayList to add the celestial body states to after a step
        ArrayList<CelestialBody> updatedCelestialBodies = new ArrayList<CelestialBody>();

        for (int i = 0; i < celestialBodies.size(); i++) {
            // v(t2) = v(t1) + timeStep * a(t1)				need to check which acceleration is taking
            //x(t2) = x(t1) + timeStep * v(t1)
            // velocity computed using acceleration.
            Vector3dInterface newVelocity = celestialBodies.get(i).getVelocity().addMul(step, castedRate.accelerations.get(i));
            // position computed using the newly calculated velocities

            Vector3dInterface newPosition = celestialBodies.get(i).getLocation().addMul(step, celestialBodies.get(i).getVelocity());
            updatedCelestialBodies.add(new CelestialBody(celestialBodies.get(i).getName(),
                    celestialBodies.get(i).getImage(), celestialBodies.get(i).getMass(), newPosition, newVelocity));

        }
        return (StateInterface) new State(updatedCelestialBodies);

    }


}
