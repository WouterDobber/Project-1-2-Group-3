//PHYSICS ENGINE: note that vectors are treated as: x, y, theta or angle.
public class PhysicsEngine {

    public static  final double  GRAVITATIONAL_FORCE = -1.352;

    /**
     *
     * @param lander
     * @param bottomThursterForce
     * @param controllersDirection
     * @return new vector with updated accelerations in x, y and z (angular acceleration)
     */
    public static Vector calculateAccelerations (Lander lander,  double bottomThursterForce, double controllersDirection, double wind){
        //System.out.println("wind" + wind);
        double accelerationX = (bottomThursterForce * Math.sin(lander.getAngle())) + wind;
        double accelerationY = bottomThursterForce * Math.cos(lander.getAngle()) + GRAVITATIONAL_FORCE;
        double accelerationTheta = ( lander.getRadius() * lander.getMaxControllerForce() * controllersDirection ) / lander.getMomentumOfInertia();
        return new Vector(accelerationX, accelerationY, accelerationTheta);
    }

    /**
     * Updates the velocity and positons of lander (x, y and angle)
     * @param lander
     * @param step
     * @param acceleration
     * @return  new Lander
     */
    public static Lander eulerSolver (Lander lander,  double step, Vector acceleration){
        Vector newVelocity = (Vector) lander.getVelocity().addMul(step, acceleration);
        Vector newPosition = (Vector) lander.getPosition().addMul(step, lander.getVelocity());
        return new Lander(newVelocity, newPosition, lander.getMass(), lander.getRadius(), lander.getMaxControllerForce() );
    }

    /**
     * It does one step with Euler solver updating the accelerations
     * @param lander
     * @param step
     * @param bottomThrusterForce
     * @param controllersDirection
     * @return new lander
     */
    public static Lander step (Lander lander, double step, double bottomThrusterForce, double controllersDirection, double strengthOfWind ){
        Vector acceleration;
        if(strengthOfWind > 0 ){
            double wind = Wind.wind(lander.getPosition().getY(), strengthOfWind);
            acceleration = calculateAccelerations(lander, bottomThrusterForce, controllersDirection, wind);
        } else {
            acceleration = calculateAccelerations(lander, bottomThrusterForce, controllersDirection, 0);
        }
        return  eulerSolver(lander, step, acceleration);
    }

}
