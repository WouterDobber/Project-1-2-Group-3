import java.util.*;

public class RungeKuttaSolver {

    public final static double G = 6.67408E-11;
    private double h;
    private ArrayList<CelestialBody> bodies;

    // tf must be positive
    public StateInterface[] solve(ArrayList<CelestialBody> bodies, StateInterface y0, double tf, double h) {
        this.h = h;
        this.bodies = bodies;
        //choosing the number of state we are going to go through
        int toBeAllocated = (int)(tf / h) + 1;
        if (tf % h != 0)
            toBeAllocated++;
        StateInterface[] ans = new StateInterface[toBeAllocated];

        // initial state
        ans[0] = y0;

        for (int i = 1; i < toBeAllocated; i++) {
            for (int j = 1; j < bodies.size(); j++) {
                // calculating acceleration for each celestial body via forces
                force(bodies.get(j));
            }

            // call Runge-Kutta update
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


    public void update() {
        for(int i=1; i < bodies.size(); i++) {
            //Runge kutta 4th order
            Vector k1 = new Vector(bodies.get(i).getVelocity().getX(), bodies.get(i).getVelocity().getY(), bodies.get(i).getVelocity().getZ());
            Vector k2 = (Vector) bodies.get(i).getAcceleration().mul(h / 2.0).add(k1).add(k1.divide(2.0));
            Vector k3 = (Vector) bodies.get(i).getAcceleration().mul(h / 2.0).add(k1).add(k2.divide(2.0));
            Vector k4 = (Vector) bodies.get(i).getAcceleration().mul(h).add(k1).add(k3);

            Vector change = (Vector) k1.add(k2.mul(2.0)).add(k3.mul(2.0)).add(k4);
            change = (Vector) change.divide(6.0).mul(h / 2.0);
            bodies.get(i).setLocation(bodies.get(i).getLocation().add(change));
            bodies.get(i).setVelocity(bodies.get(i).getVelocity().add(bodies.get(i).getAcceleration().mul(h)));
        }
    }
}
