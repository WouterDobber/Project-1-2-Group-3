import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;

public class TestSaturnCoordinates {
    static final double ACCURACY = 1e4; // play around with this value 
    private static double CONVERSION_UNIT_NASA= 0.001;


    @Test
    public void testSolverOneDayX() {
        Vector3dInterface[] trajectory = simulateOneDay();
        // 2020-Apr-02 00:00:00.0000
        double x1 = 6.335748551935359E+08; // reference implementation
        assertEquals(x1, trajectory[1].getX(), ACCURACY); // delta +-ACCURACY
    }

    @Test
    public void testSolverOneDayY() {
        Vector3dInterface[] trajectory = simulateOneDay();
        // 2020-Apr-02 00:00:00.0000
        double y1 = -1.357822481323319E+09; // reference implementation
        assertEquals(y1, trajectory[1].getY(), ACCURACY); // delta +-ACCURACY
    }

    @Test
    public void testSolverOneDayZ() {

        Vector3dInterface[] trajectory = simulateOneDay();
        // 2020-Apr-02 00:00:00.0000
        double z1 = -1.612884301640511E+06; // reference implementation
        assertEquals(z1, trajectory[1].getZ(), ACCURACY); // delta +-ACCURACY
    }

    @Test
    public void testSolverOneYearX() {

        Vector3dInterface[] trajectory = simulateOneYear();
        // 2021-Apr-01 00:00:00
        double x366 = 8.778981939996121E+08; // reference implementation
        assertEquals(x366, trajectory[365].getX(), ACCURACY); // delta +-ACCURACY

    }

    @Test
    public void testSolverOneYearY() {

        Vector3dInterface[] trajectory = simulateOneYear();
        // 2021-Apr-01 00:00:00
        double y366 = -1.204478262290766E+09; // reference implementation
        assertEquals(y366, trajectory[365].getY(), ACCURACY); // delta +-ACCURACY

    }

    @Test
    public void testSolverOneYearZ() {

        Vector3dInterface[] trajectory = simulateOneYear();
        // 2021-Apr-01 00:00:00
        double z366 = -1.400829719307184E+07; // reference implementation
        assertEquals(z366, trajectory[365].getZ(), ACCURACY); // delta +-ACCURACY
    }

    public static Vector3dInterface[] simulateOneDay() {
        double day = 24*60*60;
        double step = day;
        SolarSystem universe = new SolarSystem(); // create solarSystem

        ODEFunctionInterface function = new NewtonsFunction();
        SolarSystemSolver solver = new SolarSystemSolver();
        StateInterface state = (StateInterface) new State(universe.bodies);

        StateInterface[] s =  solver.solve( function, state, day, step);

        Vector3dInterface[] planet = new Vector3dInterface[s.length];
        for (int i = 0; i < s.length; i++) {
            State temporary = (State) s[i];
            planet[i] = temporary.celestialBodies.get(5).getLocation().mul(CONVERSION_UNIT_NASA); //5 is saturn
            // get vector position and add it to planet saturn positions 
        }
        return planet;

    }
    public static Vector3dInterface[] simulateOneYear() {
        double day = 24*60*60;
        double year = 365*day; //changed to 365
        double step = day;
        SolarSystem universe = new SolarSystem(); // create solarSystem

        ODEFunctionInterface function = new NewtonsFunction();
        SolarSystemSolver solver = new SolarSystemSolver();
        StateInterface state = (StateInterface) new State(universe.bodies);

        StateInterface[] s =  solver.solve( function, state, year, step);

        Vector3dInterface[] planet = new Vector3dInterface[s.length];
        for (int i = 0; i < s.length; i++) {
            State temporary = (State) s[i];
            planet[i] = temporary.celestialBodies.get(5).getLocation().mul(CONVERSION_UNIT_NASA); //5 is saturn
            // get vector position and add it to planet saturn positions 
        }
        return planet;
    }


}


