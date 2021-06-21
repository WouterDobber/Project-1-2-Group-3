import java.util.Random;

public class Wind {
    final static  double AREA = 78; //of the lander in m^2
    final static double PRESSURE_CONSTANT = 1.47*0.613;  //Titans athmosphere is 1.47 higher than earths
    final static double dragCoefficient = 0.47; //dependent on shape of lander: sphere
    /**
     * Method calculates the force of the wind based on a specific height, Assuming only possible one direction
     * for it in the x axis either right to left or left to right.
     *
     * @param height   of the lander, should be given in meters,
     * @param strength desired strength of the wind, given in %
     * @return the force of the wind on the lander, which is a double in Newton.
     */
    public static double wind(double height, double strength) { //strength is a %
        height = height / 1000;  //dividing to transform it to km
        Random rand = new Random();  //based on random, this will define the strength of the wind

        //Adding randomness with gaussian distribution so that the standard deviation can increase or decrease the desired wind by 20%
        double gauss = (rand.nextGaussian() /5 ) +1;
        if (gauss < 0) {
            gauss = 0;
        }
        strength *= gauss;

        double velocityWind = 0; // magnitude

        if (height <= 150 && height > 120) {
            velocityWind = (100 * strength) / 100; //based on report, lander started recording from 150 but strongest one was in 120, so randomly choosen 100 to be strongest possible
        } else if (height == 120) {
            velocityWind = (120 * strength) / 100; //at a height of 120, the max wind is 120
        } else if (height < 120 && height > 100) {
            //call other method
            velocityWind = linearCalculateWind(120, 100, strength, height) / 100;
        } else if (height <= 100 && height >= 60) {
            velocityWind = (0.3 * strength) / 100; //no wind
        } else if (height < 60 && height > 0) {
            velocityWind = linearCalculateWind(60, 1, strength, height) / 100;
        } else { //height ==0
            velocityWind = (0.3 * strength) / 100;
        }

        // https://www.wikihow.com/Calculate-Wind-Load
        // F = area * Presure wind * drag coefficient
        // if circle, drag coefficient = 0.47
        // pressure wind = constant * v^2
        double pressureWind = PRESSURE_CONSTANT * velocityWind*velocityWind; 
        double windForce = AREA * pressureWind * dragCoefficient;

        //direction of the wind changes twice
        double verticalNoise = 0; //DEFINE THIS
        if (height > 6) { //west to east
            windForce *= 1;
        } else if (height < 6 && height > 0.7) { //east to west
            windForce *= -1;
        } else { //west to east
            windForce *= 1;
        }

        return  windForce;
    }

    /**
     * Method generates the linear function to determine the wind speed based in the height
     * @param max
     * @param min
     * @param strength
     * @param height
     * @return wind speed
     */
    public static double linearCalculateWind(int max, int min, double strength, double height) {
        double difference = max - min;
        double positionInRange = height - min; //my position
        return max * strength * (positionInRange / difference);
    }

}
