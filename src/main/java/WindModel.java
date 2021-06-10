import java.util.Random;
public class WindModel {

    //NEED TO ADD RANDOMNESS (line 15) , check with katherina about vertical noise

    /** Method calculates the force of the wind based on a specific height
     * @param height of the lander, should be given in meters,
     * @param strength desired strength of the wind, given in %
     * @return the force of the wind on the lander
     */
    public static Force wind(double height, double strength){ //strength is a %
       height = height/1000;  //dividing to transform it to km
       Random rand = new Random();  //based on random, this will define the strength of the wind

        rand.nextGaussian();        //ASK ABOUT THIS

        double velocityWind ; // magnitude
       Vector direction;

        if (height==120 || height > 120 ){
            velocityWind= (120*strength) / 100; //at a height of 120, the max wind is 120
        } else if (height<120 && height>=60){
            velocityWind =0; //no wind
        } else if (height<60 && height>40 ){
            velocityWind= 20;
        } else {
            velocityWind = (0.3 *strength) / 100;
        }

        //direction of the wind changes twice
        double verticalNoise = 0; //DEFINE THIS
        if (height >6){ //west to east
            direction = new Vector (velocityWind*1000,verticalNoise,0);
        }else if ( height <6 && height>0.7) { //east to west
            direction = new Vector(velocityWind*1000, verticalNoise, 0);
        } else { //west to east
            direction = new Vector (velocityWind*1000,verticalNoise,0);
        }

        //multiply both velocity and direction with random component
        return new Force (velocityWind*1000, direction); //velocity*1000 to transform it to meters
    }

}
