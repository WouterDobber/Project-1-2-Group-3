import java.util.ArrayList;
import java.util.Arrays;

class OpenLoop {

    static double G = -1.352;

    /**
     * Calculates half-time require to turn a given angle using controllers at max force
     *
     * @param changeInAngle
     * @param lander
     * @return time
     */
    public static double calculateHalfRotateTime(double changeInAngle, Lander lander){
        double angularAccel = (lander.getMaxControllerForce() * lander.getRadius()) / lander.getMomentumOfInertia();
        double t = Math.sqrt( Math.abs(changeInAngle) / angularAccel);
        return t;
    }

    /**
     * It calculates the angle at a certain time
     * @param lander
     * @param t
     * @param angularAcceleration
     * @return angle
     */
    public static double getAngleAfterT(Lander lander, double t, double angularAcceleration){
        double theta = lander.getAngle() + ( lander.getAngularVelocity() * t ) + ( angularAcceleration * Math.pow(t, 2) / 2 );
        return theta;
    }

    /**
     * Calculates the time taken to decent in y-axis.
     * t= (vf - v0 )/ a
     * @param lander
     * @param changeOfYDuringFlight
     * @param velocityChangeDuringLFight
     * @param accelerationYaxis
     * @return time
     */
    public static double decentYaxis(Lander lander, double changeOfYDuringFlight, double velocityChangeDuringLFight, double accelerationYaxis){
        double a= accelerationYaxis;                                                                             //update
        double t = (0 -( lander.getVelocity().getY() + velocityChangeDuringLFight)) / a;
        return t;
    }

    /**
     * Calculates given an initial velocity, calculates the time required to free fall 1/4 of the entire distance.
     * @param lander
     * @param initialVelocity
     * @return  time
     */
    public static double VerticalFreeFallTime(Lander lander, double initialVelocity){
        double t2= Math.sqrt(lander.getPosition().getY()/(2*-G)) ;

        double t3= 2* lander.getPosition().getY()-initialVelocity;
        double t= -initialVelocity + Math.sqrt(Math.pow(initialVelocity,2) - ((4*-G/2)* ( -(1/4)*lander.getPosition().getY())));
        t= t/-G;
        return t;
    }

    /**
     * Calculates the vertical acceleration
     * a= v0^2 / (2* y0)
     * @param lander
     * @param initialVelocity
     * @return acceleration
     */
    public static double VerticalAcceleration (Lander lander, double initialVelocity){
        double acceleration = Math.pow(initialVelocity,2) / (2*lander.getPosition().getY());
        return acceleration;
    }

    /** Time needed to reach 0 in y-axis.
     * time = v0 / a
     * @param lander
     * @param acceleration
     * @param initialVelocity
     * @return time
     */
    public static double VerticalTime (Lander lander, double acceleration, double initialVelocity){
        return initialVelocity/acceleration;
    }

    /**
     * u= ( v0^2 / 2*y0 )-G
     * @param lander
     * @param initialVelocity
     * @return
     */
    public static double VerticalMainThrusterForce(Lander lander, double initialVelocity){
        double u = (Math.pow(initialVelocity, 2) / (2* lander.getPosition().getY())) - G;
        return u;
    }


    public static double accelerationYaxis (Lander lander, double changeOfPositionDuringFlight,  double velocityChangeDuringLFight) {
        double a = Math.pow(lander.getVelocity().getY() + velocityChangeDuringLFight, 2) / (2 * (lander.getPosition().getY() + changeOfPositionDuringFlight));
        return a;
    }

        public static double changeInYDuringFreeFall(Lander lander, double time){ //it returns the distance the lander moves in free fall (y)
        double newPosition = lander.getVelocity().getY()*time + lander.getPosition().getY() - ( (G / 2) * Math.pow(time, 2)); //x = x0 + v0*t + 1/2 * a * t^2
        return lander.getPosition().getY() - newPosition;
    }

    public static double velocityAfterFreeFall(Lander lander, double time){
        return lander.getVelocity().getY() + (G * time);
    }

    /**
     * u= -G / cos(theta)
     * @param lander
     * @return force main thruster
     */
    public static double horizontalFlightForceMainThruster(Lander lander){
        // Input angle should be pi/4
        double u = -G / (Math.cos(lander.getAngle()));
        return u;
    }

    /**
     * a_x= u* sin(theta)
     * @param lander
     * @param mainThrusterForce
     * @return acceleration on x during horizontal flight
     */
    public static double horizontalFlightXAcc(Lander lander, double mainThrusterForce){
        // Input angle should be pi/4
        double xAcc = mainThrusterForce * Math.sin(lander.getAngle());
        return xAcc;
    }

    /**
     * time = sqrt(|(-2* x0) / a|)
     * @param lander
     * @param acceleration
     * @return time needed to get to x=0
     */
    public static double horizontalFlightTimeToX0(Lander lander, double acceleration){
        double time = (Math.sqrt( Math.abs( ( -2 * lander.getPosition().getX() ) / acceleration))); //THIS RESULTS IN NAN
        return time;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static ArrayList[] fullFlight(Lander lander, double stepSize) {
        ArrayList<Double> mainThruster = new ArrayList<Double>();
        ArrayList<Double> controllers = new ArrayList<Double>();
        double sectionStartTime = 0;
        double totalTime = 0;
        double rotationTime =0;
        // First rotation
        // Acceleration
        double changeInAngel = (Math.PI / 4);
        double halfTimeToTurnTo45 = calculateHalfRotateTime(changeInAngel, lander);
        rotationTime+= 2*halfTimeToTurnTo45;



        for (double i = sectionStartTime; i < halfTimeToTurnTo45; i += stepSize) {
            mainThruster.add(0.0);
            controllers.add(1.0);
        }
        sectionStartTime = halfTimeToTurnTo45;
        totalTime = halfTimeToTurnTo45 * 2;
        // break
        for (double i = sectionStartTime; i < totalTime; i += stepSize) {
            mainThruster.add(0.0);
            controllers.add(-1.0);
        }
        lander.setAngle(Math.PI / 4); //updating lander

        // 1 st horizontal flight
        // Acceloration
        double mainThrustForce = horizontalFlightForceMainThruster(lander);
        double xAcc = horizontalFlightXAcc(lander, mainThrustForce);
        double horizTime = horizontalFlightTimeToX0(lander, xAcc) ;
        sectionStartTime = totalTime;
        totalTime += (horizTime / 2);

        for (double i = sectionStartTime; i < totalTime; i += stepSize) {

            mainThruster.add(mainThrustForce);
            controllers.add(0.0);
        }

        lander.setPositionX(lander.getPosition().getX()/2); //update x
        // 90 degree roation to - 45
        double halfTimeToTurn90 = calculateHalfRotateTime(Math.PI / 2, lander);
        rotationTime += 2*halfTimeToTurn90;

        sectionStartTime = totalTime;
        totalTime += halfTimeToTurn90;
        // acceleration
        for (double i = sectionStartTime; i < totalTime; i += stepSize) {
            mainThruster.add(0.0);
            controllers.add(-1.0);
        }
        // break
        sectionStartTime = totalTime;
        totalTime += halfTimeToTurn90;
        for (double i = sectionStartTime; i < totalTime; i += stepSize) {
            mainThruster.add(0.0);
            controllers.add(1.0);
        }
        lander.setAngle(-Math.PI/4); //update angle

        // 2nd horizonatal flight
        sectionStartTime = totalTime;
        totalTime += (horizTime / 2);

        for (double i = sectionStartTime; i < totalTime; i += stepSize) {
            mainThruster.add(mainThrustForce);
            controllers.add(0.0);
        }
        lander.setPositionX(0);

        // rotate to vertiacal

        // acceleration
        sectionStartTime = totalTime;
        totalTime += (halfTimeToTurnTo45);
        rotationTime += 2*halfTimeToTurnTo45;
        for (double i = sectionStartTime; i < totalTime; i += stepSize) {
            mainThruster.add(0.0);
            controllers.add(1.0);
        }

        // break
        sectionStartTime = totalTime;
        totalTime += (halfTimeToTurnTo45);
        for (double i = sectionStartTime; i < totalTime; i += stepSize) {
            mainThruster.add(0.0);
            controllers.add(-1.0);
        }

        lander.setAngle(0); //update angle

        // Free fall

        double timeFreeFall = VerticalFreeFallTime(lander, G);

        sectionStartTime = totalTime;
        totalTime += (timeFreeFall);

        for (double i = sectionStartTime; i < totalTime; i += stepSize) {
           mainThruster.add(0.0);
           controllers.add(0.0);
        }


        //double distancedTravelledDuringFreeFall = changeInYDuringFreeFall(lander, time180Roataion);
        //double velocityAfterFreeFall = velocityAfterFreeFall(lander, time180Roataion);


        // Controlled decent
        /**
        double accelerationY = accelerationYaxis(lander, 0, G*totalTime); // ay = u*cos(theta) -g;
        double timeForDecent = decentYaxis(lander, 0, G, accelerationY);
         double forceThruster = (accelerationY- G)/Math.cos(lander.getAngle());
        */
        double accelerationY= VerticalAcceleration(lander, G*rotationTime);
        double timeForDecent= VerticalTime(lander, accelerationY, G*timeFreeFall+ G*rotationTime);
        double forceThruster =  VerticalMainThrusterForce(lander, G*timeForDecent );
        sectionStartTime = totalTime;
        totalTime += timeForDecent;

        for (double i = sectionStartTime; i < totalTime; i += stepSize) {
            mainThruster.add(forceThruster);
            controllers.add(0.0);
        }

        return new ArrayList[]{mainThruster, controllers};
    }

    /**
     * Method calcultes the coordinates needed by the lander to decendt
     * @return positions of lander as a vector with x and y coordinates and z= angle on inclination. 
     */
    public static Vector[] getCoordinatesForLander(){
        Lander testFlight = new Lander(new Vector(0,0,0), new Vector(-10000,20000,0), 6000, 1000, 440);
        Lander copyOfTestFlight = testFlight.copy();

        ArrayList<Double>[] res = fullFlight(testFlight, 1);
        Vector [] positions = new Vector [res[0].size()];
        double step = 1;
        for (int i =0; i<res[0].size(); i++){
            copyOfTestFlight = PhysicsEngine.step(copyOfTestFlight, step, res[0].get(i), res[1].get(i));
            positions[i]= copyOfTestFlight.getPosition();
        }
        return positions;
    }
   


}
