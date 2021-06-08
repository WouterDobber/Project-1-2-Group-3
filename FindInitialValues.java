public class FindInitialValues {

    /**
    * This calls Newton Raphson to find the best initial velocity
    * Variables to change/play around: h, finalTime, accuracy.
     */
    public static void main (String [] args){
        //starting velocity used as starting point, the position doesnt change.
        Vector p0= new Vector   ( -1.471886208478151E11, -2.861522074209465E10 ,8170057.668900404);
        Vector v0= new Vector  (27962.61762782645, -62349.24395947284 ,-666.7403073700751);

        double accuracy = 300000; // how close you want to get to titan
        double h= 1000; //STEP SIZE
        double finalTime = 31556952; //1 year in seconds
        Vector velocity = NewtonRaphson.calculate( v0,  p0,  finalTime,  h,  accuracy);

    }


}
