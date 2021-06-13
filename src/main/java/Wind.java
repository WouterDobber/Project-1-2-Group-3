import java.util.Random;

public class Wind {
    /**
     * Method calculates the force of the wind based on a specific height
     *
     * @param height   of the lander, should be given in meters,
     * @param strength desired strength of the wind, given in %
     * @return the force of the wind on the lander
     */
    public static Force wind(double height, double strength) { //strength is a %
        height = height / 1000;  //dividing to transform it to km
        Random rand = new Random();  //based on random, this will define the strength of the wind

        //Adding randomness with gaussian distribution so that middle points have higher chances to come up, the value of strength wont increase, only remaind the same or decrease.
        double gauss = rand.nextGaussian();
        if (Math.abs(gauss) > 1) {
            gauss = 1;
        } else {
            gauss = Math.abs(gauss);
        }
        strength *= gauss;

        double velocityWind = 0; // magnitude
        Vector direction;

        if (height <= 150 && height > 120) {
            velocityWind = (100 * strength) / 100; //based on report, lander started recording from 150 but strongest one was in 120, so randomly choosen 100 to be strongest possible
        } else if (height == 120) {
            velocityWind = (120 * strength) / 100; //at a height of 120, the max wind is 120
        } else if (height < 120 && height > 100) {
            //call other method
            velocityWind = linearCalculateWind(120, 100, strength, height) / 100;
        } else if (height <= 100 && height >= 60) {
            velocityWind = 0; //no wind
        } else if (height < 60 && height > 0) {
            velocityWind = linearCalculateWind(60, 1, strength, height) / 100;
        } else { //height ==0
            velocityWind = (0.3 * strength) / 100;
        }

        //direction of the wind changes twice
        double verticalNoise = 0; //DEFINE THIS
        if (height > 6) { //west to east
            direction = new Vector(velocityWind, verticalNoise, 0);
        } else if (height < 6 && height > 0.7) { //east to west
            direction = new Vector(-velocityWind, verticalNoise, 0);
        } else { //west to east
            direction = new Vector(velocityWind, verticalNoise, 0);
        }

        //multiply both velocity and direction with random component
        return new Force(velocityWind, direction); //velocity*1000 to transform it to meters
    }

    public static double linearCalculateWind(int max, int min, double strength, double height) {
        double difference = max - min;
        double positionInRange = height - min; //my position
        return max * strength * (positionInRange / difference);
    }
}
