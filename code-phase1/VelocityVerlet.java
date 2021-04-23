import java.util.*;

public class VelocityVerlet {

    public final static double G = 6.67408E-11;
    private ArrayList<CelestialBody> bodies;
    private double h;

    // tf must be positive
    public StateInterface[] solve(ArrayList<CelestialBody> bodies, StateInterface y0, double tf, double h) {
        this.bodies = bodies;
        this.h = h;
        //choosing the number of state we are going to go through
        int toBeAllocated = (int)(tf / h) + 1;
        if (tf % h != 0)
            toBeAllocated++;
        StateInterface[] ans = new StateInterface[toBeAllocated];

        // initial state
        ans[0] = y0;

        for (int j = 1; j < bodies.size(); j++) {
            // calculating acceleration for each celestial body via forces
            force(bodies.get(j));
        }
        // later we dont have to call force method from here anymore since we call it in the middle of update method
        for (int i = 1; i < toBeAllocated; i++) {

            // call Velocity Verlet update
            update();

            ArrayList<CelestialBody> currentState = new ArrayList<>();
            for (int j = 0; j < bodies.size(); j++)
                currentState.add(bodies.get(j).getClone());
            ans[i] = new State(currentState);
        }
        return ans;
    }

    public void force(CelestialBody body){
        Vector totalForce = new Vector(0,0,0);

        for(int i=0; i < bodies.size(); i++) {
            if(body!= bodies.get(i)) {
                //Celestial body to compare with
                CelestialBody body2 = bodies.get(i);
                Vector vectorAB = (Vector) body2.getLocation().sub(body.getLocation());
                double r = vectorAB.norm();
                Vector normalizedAB = vectorAB.normalize();
                //The formula for gravitational forces
                double f = G*(body.getMass() * body2.getMass()) / (r * r);
                totalForce = (Vector) totalForce.add(normalizedAB.mul(f));

            }
        }

        body.setAcceleration(totalForce.divide(body.getMass()));
    }

    /*
    https://www2.icp.uni-stuttgart.de/~icp/mediawiki/images/5/54/Skript_sim_methods_I.pdf
    */
    public void update() {

        for(int i = 1; i < bodies.size(); i++) {
            Vector3dInterface curPosition = bodies.get(i).getLocation();
            Vector3dInterface curVelocity = bodies.get(i).getVelocity();
            Vector3dInterface curAcceleration = bodies.get(i).getAcceleration();

            Vector3dInterface nextPosition = curPosition.addMul(h, curVelocity).addMul(h * h / 2.0, curAcceleration);
            Vector3dInterface intermediateVelocity = curVelocity.addMul(h / 2.0, curAcceleration);
            // omit acceleration forces step as we always calculate it before calling update with force method
            bodies.get(i).setLocation(nextPosition);
            bodies.get(i).setVelocity(intermediateVelocity);
        }

        // updating acceleration
        for (int i = 1; i < bodies.size(); i++) {
            force(bodies.get(i));
        }

        // recoil that for now all celestial objects store "intermediate" velocity, so we have to update it once again
        // in order to get velocity as of end of h time jump
        for (int i = 1; i < bodies.size(); i++) {
            Vector3dInterface curVelocity = bodies.get(i).getVelocity();
            Vector3dInterface curAcceleration = bodies.get(i).getAcceleration();

            Vector3dInterface nextVelocity = curVelocity.addMul(h / 2.0, curAcceleration);
            bodies.get(i).setVelocity(nextVelocity);
        }
    }
}
