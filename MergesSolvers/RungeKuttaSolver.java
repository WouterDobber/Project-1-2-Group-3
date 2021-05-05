import java.util.ArrayList;

public class RungeKuttaSolver {
    public static StateInterface RKuttaSolve(RateOfChange castedRate, ArrayList<CelestialBody> celestialBodies, double step) {
        // new ArrayList to add the celestial body states to after a step
        ArrayList<CelestialBody> updatedCelestialBodies = new ArrayList<CelestialBody>();
        for (int i = 0; i < celestialBodies.size(); i++) {

            Vector k1 = new Vector(celestialBodies.get(i).getVelocity().getX(), celestialBodies.get(i).getVelocity().getY(), celestialBodies.get(i).getVelocity().getZ());
            //Vector k1 = (Vector) celestialBodies.get(i).getVelocity().mul(step);
            Vector k2 = (Vector) castedRate.accelerations.get(i).mul(step / 2.0).add(k1).add(k1.divide(2.0));
            Vector k3 = (Vector) castedRate.accelerations.get(i).mul(step / 2.0).add(k1).add(k2.divide(2.0));
            Vector k4 = (Vector) castedRate.accelerations.get(i).mul(step).add(k1).add(k3);
            Vector change = (Vector) k1.add(k2.mul(2.0)).add(k3.mul(2.0)).add(k4);

            change = (Vector) change.divide(6.0).mul(step / 2.0);

            Vector3dInterface newPosition = celestialBodies.get(i).getLocation().add(change);
            Vector3dInterface newVelocity = celestialBodies.get(i).getVelocity().add(castedRate.accelerations.get(i).mul(step));
            updatedCelestialBodies.add(new CelestialBody(celestialBodies.get(i).getName(),
                    celestialBodies.get(i).getImage(), celestialBodies.get(i).getMass(), newPosition, newVelocity));

        }
        return (StateInterface) new State(updatedCelestialBodies);
    }
}

