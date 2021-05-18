import java.util.ArrayList;

public class VerletSolver {
    public static StateInterface VerletSolve(RateOfChange castedRate, ArrayList<CelestialBody> celestialBodies, double step) {
        // new ArrayList to add the celestial body states to after a step
        ArrayList<CelestialBody> updatedCelestialBodies = new ArrayList<CelestialBody>();
        /*
            * Processes the shot using the Velocity Verlet Method.
            * This method updates the position and velocity in five steps:
            * 1 The current acceleration is calculated using the current position and velocity.
            * 2 The next position is calculated using the current position, velocity and acceleration.
            * 3 The intermediate velocity is calculated using the current velocity and acceleration.
            * 4 The next acceleration is calculated using the next position and intermediate velocity.
            * 5 The next velocity is calculated using the intermediate velocity and the next acceleration.
            * Sources used:   http://www.physics.udel.edu/~bnikolic/teaching/phys660/numerical_ode/node5.html
            *                 https://www2.icp.uni-stuttgart.de/~icp/mediawiki/images/5/54/Skript_sim_methods_I.pdf
            *                 https://www.algorithm-archive.org/contents/verlet_integration/verlet_integration.html
        */
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
