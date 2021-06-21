/**This class was used for experiments in Phase 2, the relative error is computed for different step sizes. 
*
**/ 
public class AccuracyStepSizeSaturnExperiment {
    private static double CONVERSION_UNIT_NASA = 0.001;

    public static void main (String [] args){
        System.out.println( "Step size 60:       " + testStep(60)); //1 minute
        System.out.println( "Step size 60*10:       " + testStep(60*10)); //10 minutes
        System.out.println( "Step size 60*60:       " + testStep(60*60)); //1 hour
        System.out.println( "Step size 60*100:       " + testStep(60*100));
        System.out.println( "Step size 60*500:       " + testStep(60*500)); //about half of day
        System.out.println( "Step size 60*60*24:       " + testStep(60*60*24)); //1 day
        System.out.println( "Step size 60*60*24*7:       " + testStep(60*60*24*7)); //1 week
    }


    public static double testStep (double stepSize){
        Vector3dInterface nasasSaturnCoordinate = new Vector(8.778981939996121E+08, -1.204478262290766E+09, -1.400829719307184E+07);
        Vector3dInterface[] positionsSaturn = simulateOneYear(stepSize);
        Vector3dInterface positionAfterYear = positionsSaturn[positionsSaturn.length - 1];

        //ERROR CALCULATION FOR X, Y and Z COORDINATES
        double numeratorX =  positionAfterYear.getX() -  nasasSaturnCoordinate.getX();
        double denominatorX =  nasasSaturnCoordinate.getX();
        double errorX= Math.abs(numeratorX / denominatorX) *100;
        System.out.println("Error x:   " + errorX + " %");

        double numeratorY =  positionAfterYear.getY() -  nasasSaturnCoordinate.getY();
        double denominatorY =  nasasSaturnCoordinate.getY();
        double errorY= Math.abs(numeratorY / denominatorY) *100;
        System.out.println("Error y:   " + errorY + " %");

        double numeratorZ =  positionAfterYear.getZ() -  nasasSaturnCoordinate.getZ();
        double denominatorZ =  nasasSaturnCoordinate.getZ();
        double errorZ= Math.abs(numeratorZ / denominatorZ) *100;
        System.out.println("Error z:   " + errorZ + " %");

        //return euclidean distance
        return  nasasSaturnCoordinate.dist(positionAfterYear); //euclidean distance

    }


    public static Vector3dInterface[] simulateOneYear(double stepSize) {
        double day = 24*60*60;
        double year = 365*day; //changed to 365
        SolarSystem universe = new SolarSystem(); // create solarSystem

        ODEFunctionInterface function = new NewtonsFunction();
        SolarSystemSolver solver = new SolarSystemSolver();
        StateInterface state = (StateInterface) new State(universe.bodies);

        StateInterface[] s =  solver.solve( function, state, year, stepSize);

        Vector3dInterface[] planet = new Vector3dInterface[s.length];
        for (int i = 0; i < s.length; i++) {
            State temporary = (State) s[i];
            planet[i] = temporary.celestialBodies.get(5).getLocation().mul(CONVERSION_UNIT_NASA); //5 is saturn
            // get vector position and add it to planet saturn positions
        }
        return planet;
    }





}
