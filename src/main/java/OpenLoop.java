import java.util.ArrayList;
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
     * Calculates given an initial velocity, calculates the time required to free fall 1/4 of the entire distance.
     * @param lander
     * @param initialVelocity
     * @return  time
     */
    public static double VerticalFreeFallTime(Lander lander, double initialVelocity){
        double t2= Math.sqrt( lander.getPosition().getY()/(-5*G)) ; //t= sqr(2h/g) and because is 1/10 we multiply it by it

        return t2;
    }


    /**
     * Calculates the angle of the lander based on a given time
     * @param initialVelocity
     * @param time
     * @param initialAngle
     * @param lander
     * @return angle
     */
    public static double angleGivenTime(double initialVelocity, double time, double initialAngle, Lander lander){
        double angularAccel = (lander.getMaxControllerForce() * lander.getRadius()) / lander.getMomentumOfInertia();
        double theta = initialAngle + initialVelocity*time + (angularAccel* (Math.pow(time,2)/2));
        return theta;
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
        double time = Math.sqrt(  ( -2 * lander.getPosition().getX() ) / acceleration); //THIS RESULTS IN NAN
        return time;
    }

    /**
     * Method calculates and returns the forces of the main thruster and controller during the vertical flight
     * @param lander
     * @param stepSize
     * @return forces during landing
     */
    public static ArrayList[] verticalFlight(Lander lander, double stepSize){
        ArrayList<Double> mainThruster = new ArrayList<Double>();
        ArrayList<Double> controllers = new ArrayList<Double>();
        double sectionStartTime = 0;
        double totalTime = 0;

        double timeFreeFall = VerticalFreeFallTime(lander, G);

        sectionStartTime = totalTime;
        totalTime += timeFreeFall;
        totalTime=  Math.round(totalTime);

        for (double i = sectionStartTime; i < totalTime; i += stepSize) {
            mainThruster.add(0.0);
            controllers.add(0.0);
        }

        // Controlled  decenent
        double initialVelocityY= G*totalTime;
        double changeInPosition = 0.5*G*  Math.pow(timeFreeFall, 2);
        double accelerationY = Math.pow(initialVelocityY,2) / (2*(lander.getPosition().getY() + changeInPosition));
        double timeForDecent = (initialVelocityY)/ -accelerationY;

        double forceThruster= ((Math.pow(initialVelocityY, 2) / (2* (lander.getPosition().getY()  + changeInPosition))) )- G ;
        sectionStartTime = totalTime;
        totalTime += timeForDecent;

        totalTime=  Math.round(totalTime);

        for (double i = sectionStartTime; i < totalTime; i += stepSize) {
            mainThruster.add(forceThruster);
            controllers.add(0.0);
        }
        return new ArrayList[]{mainThruster, controllers};

    }
    /**
     *  Method calculates and returns the forces of the main thruster and controller during the horizontal flight
     * @param lander
     * @param stepSize
     * @return Array with Forces of main thruster and controllers (direction or if is on or off)
     */
    public static ArrayList[] horizontalFlight(Lander lander, double stepSize) {
        ArrayList<Double> mainThruster = new ArrayList<Double>();
        ArrayList<Double> controllers = new ArrayList<Double>();
        double sectionStartTime = 0;
        double totalTime = 0;

        // First rotation
        // Acceleration
        double changeInAngel = (Math.PI / 4);
        double halfTimeToTurnTo45 = calculateHalfRotateTime(changeInAngel, lander);

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
        // double newAngle =  angleGivenTime(0,   totalTime, 0,  lander);
        lander.setAngle(Math.PI/4); //updating lander

        // 1 st horizontal flight
        // Acceloration
        double mainThrustForce = horizontalFlightForceMainThruster(lander);
        double xAcc = horizontalFlightXAcc(lander, mainThrustForce);
        double horizTime = horizontalFlightTimeToX0(lander, xAcc)+5;
        sectionStartTime = totalTime;
        totalTime += (horizTime / 2);

        for (double i = sectionStartTime; i <= totalTime; i += stepSize) {
            mainThruster.add(mainThrustForce);
            controllers.add(0.0);
        }

        lander.setPositionX(lander.getPosition().getX()/2); //update x
        // 90 degree roation to - 45
        double halfTimeToTurn90 = calculateHalfRotateTime(Math.PI / 2, lander);

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

        return new ArrayList[]{mainThruster, controllers};
    }

    /**
     * Method calcultes the coordinates needed by the lander to decendt
     * @return positions of lander as a vector with x and y coordinates and z= angle on inclination.
     */
    public static Lander[]   getCoordinatesForLander(Lander lander, double windStrength){
        double step =1;
        Lander verticalFlightLander = lander;
        //new Lander(new Vector(0,0,0), new Vector(0,60000,0), 6000, 10, 440);
        Lander copyOfTestFlight = verticalFlightLander.copy();

        ArrayList<Double>[] descentVertical = verticalFlight(verticalFlightLander, step);

        Lander[] res = new Lander[descentVertical[0].size()];


        for (int i =0; i<descentVertical[0].size(); i++){

            copyOfTestFlight = PhysicsEngine.step(copyOfTestFlight, step, descentVertical[0].get(i), descentVertical[1].get(i), windStrength);
            res[i] = copyOfTestFlight;

        }
       // System.out.println(copyOfTestFlight);
        return res;
    }

    /**
     *
     * @param windStrength
     * @return
     */
    public static Lander[]   getXCoordinatesForLander(double windStrength){
        double step =0.5;
        Lander verticalFlightLander = new Lander(new Vector(0,0,0), new Vector(-10000,120000,0), 6000, 5, 440);
        Lander copyOfTestFlight = verticalFlightLander.copy();

        ArrayList<Double>[] horizFLight = horizontalFlight(verticalFlightLander, step);

        Lander[] res = new Lander[horizFLight[0].size()];

        for (int i =0; i<horizFLight[0].size(); i++){

            copyOfTestFlight = PhysicsEngine.step(copyOfTestFlight, step, horizFLight[0].get(i), horizFLight[1].get(i), windStrength);
            res[i] = copyOfTestFlight;

        }
        return res;
    }


    /**
     * Method returns all positions for entire landing flight
     * @param windStrength
     * @return
     */
    public static Lander[]   getPosCoordinatesForFullFlight(double windStrength){
        Lander[] horizCoords = getXCoordinatesForLander(windStrength);
        Lander lander = new Lander(new Vector(0,0,0), new Vector(horizCoords[horizCoords.length-1].getPosition().getX(),horizCoords[horizCoords.length-1].getPosition().getY(),0), 6000, 10, 440);
        Lander[] vertCoords = getCoordinatesForLander(lander, windStrength);
        Lander[] fullFlightCoords = new Lander[horizCoords.length + vertCoords.length];
        int index = horizCoords.length;

        for (int i = 0; i < horizCoords.length; i++) {
            fullFlightCoords[i] = horizCoords[i];
        }
        for (int i = 0; i < vertCoords.length; i++) {
            fullFlightCoords[i + index] = vertCoords[i];
        }
        return fullFlightCoords;
    }


    /**
     * Method says if landing was successfull or not, considering a margin of error of 50m.
     * @param lander
     * @return true or false
     */
    public static boolean isSucessfulLanding(Lander lander){
        double error = 50;
        Vector goalPosition = new Vector(-2417,0,0);
        Vector goalVelocity = new Vector(0,0,0);
        boolean acurrate = true;

        if(Math.abs(lander.getPosition().getX() - goalPosition.getX() ) > error){
            acurrate = false;
        }
        if(Math.abs(lander.getPosition().getY() - goalPosition.getY() ) > error){
            acurrate = false;
        }
        if(Math.abs(lander.getPosition().getZ() - goalPosition.getZ() ) > error){
            acurrate = false;
        }
        if(Math.abs(lander.getVelocity().getX() - goalVelocity.getX() ) > error){
            acurrate = false;
        }
        if(Math.abs(lander.getVelocity().getY() - goalVelocity.getY() ) > error){
            acurrate = false;
        }
        if(Math.abs(lander.getVelocity().getZ() - goalVelocity.getZ() ) > error){
            acurrate = false;
        }
        return acurrate;

    }

}
