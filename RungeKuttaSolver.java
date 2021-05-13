import java.util.ArrayList;

public class RungeKuttaSolver {
    public static StateInterface RKuttaSolve(RateOfChange castedRate, ArrayList<CelestialBody> celestialBodies, double step, double time, State state) {
        // new ArrayList to add the celestial body states to after a step

        ArrayList<CelestialBody> updatedCelestialBodies = new ArrayList<CelestialBody>();
        ODEFunctionInterface f = new NewtonsFunction();

        RateOfChange ki1 = (RateOfChange) f.call(time, state);
        //System.out.println(ki1);
        RateOfChange ki2 = (RateOfChange) f.call(time + 0.5 * step, state.addMulReal(step * 0.5, ki1));
        RateOfChange ki3 = (RateOfChange) f.call(time + 0.5 * step, state.addMulReal(step * 0.5, ki2));
        RateOfChange ki4 = (RateOfChange) f.call(time + step, state.addMulReal(step, ki3));

        RateOfChange change = (ki1.addMul(2, ki2).addMul(2, ki3).addMul(1, ki4)).mul(1.0 / 6.0);
        //acceleration
        Vector3dInterface newPosition;
        Vector3dInterface newVelocity;

        return state.addMulReal(step, change);
    }
}


























      /**

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
       */


