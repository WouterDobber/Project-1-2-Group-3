import java.util.ArrayList;

public class VerletSolver {
    public static StateInterface VerletSolve(RateOfChange castedRate, ArrayList<CelestialBody> celestialBodies, double step) {
        // new ArrayList to add the celestial body states to after a step
        ArrayList<CelestialBody> updatedCelestialBodies = new ArrayList<CelestialBody>();
        for(int i = 0; i < celestialBodies.size(); i++) {
            Vector3dInterface curPosition = celestialBodies.get(i).getLocation();
            Vector3dInterface curVelocity = celestialBodies.get(i).getVelocity();
            Vector3dInterface curAcceleration = castedRate.accelerations.get(i);

            Vector3dInterface nextPosition = curPosition.addMul(step, curVelocity).addMul(step * step / 2.0, curAcceleration);
            Vector3dInterface intermediateVelocity = curVelocity.addMul(step / 2.0, curAcceleration);
            // omit acceleration forces step as we always calculate it before calling update with force method
            updatedCelestialBodies.add(new CelestialBody(celestialBodies.get(i).getName(),
                    celestialBodies.get(i).getImage(), celestialBodies.get(i).getMass(), nextPosition, intermediateVelocity));
        }

        NewtonsFunction calculateAccelerations = new NewtonsFunction();
        StateInterface newState= (StateInterface) new State(updatedCelestialBodies);

        //new accelerations
        RateOfChange castedRate2 = (RateOfChange) calculateAccelerations.call(0, newState ); //t is not used in method, so sending 0


        // recoil that for now all celestial objects store "intermediate" velocity, so we have to update it once again
        // in order to get velocity as of end of h time jump
        for (int i = 0; i < updatedCelestialBodies.size(); i++) {
            Vector3dInterface curVelocity = updatedCelestialBodies.get(i).getVelocity();
            Vector3dInterface curAcceleration = castedRate2.accelerations.get(i);

            Vector3dInterface nextVelocity = curVelocity.addMul(step / 2.0, curAcceleration);
            updatedCelestialBodies.get(i).setVelocity(nextVelocity);
        }

        // the state after the step will be the new state, containing the updated
        // celestial bodies
        return (StateInterface) new State(updatedCelestialBodies);
    }

}
