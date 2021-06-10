import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TestEarthCoordinates { //Earth [Geocenter]

    static final double ACCURACY = 1E4; // 1 meter (might need to tweak that)
    private static double CONVERSION_UNIT_NASA = 0.001;
    static double step_Year = 60*60*24; //1 day
    static double step_day = 60*60*24; //1 day

    @Test
    public void testSolverOneDayX() {
        Vector3dInterface[] trajectory = simulateOneDay();
        // 2020-Apr-02
        double x1 = -1.467017349489421E+08; // reference implementation
        assertEquals(x1, trajectory[1].getX(), ACCURACY); // delta +-ACCURACY
    }

    @Test
    public void testSolverOneDayY() {
        Vector3dInterface[] trajectory = simulateOneDay();
        // 2020-Apr-02
        double y1 = -3.113778902611655E+07; // reference implementation
        assertEquals(y1, trajectory[1].getY(), ACCURACY); // delta +-ACCURACY
    }

    @Test
    public void testSolverOneDayZ() {

        Vector3dInterface[] trajectory = simulateOneDay();
        // 2020-Apr-02
        double z1 = 8.350045664343983E+03; // reference implementation
        assertEquals(z1, trajectory[1].getZ(), ACCURACY); // delta +-ACCURACY
    }

    @Test
    public void testSolverOneYearX() {

        Vector3dInterface[] trajectory = simulateOneYear();
        // 2021-Apr-01 00:00:00
        double x366 = -1.477129564888797E+08; // reference implementation
        assertEquals(x366, trajectory[365].getX(), ACCURACY); // delta +-ACCURACY

    }

    @Test
    public void testSolverOneYearY() {
        Vector3dInterface[] trajectory = simulateOneYear();
        // 2021-Apr-01 00:00:00
        double y366 = -2.821064238692064E+07; // reference implementation
        assertEquals(y366, trajectory[365].getY(), ACCURACY); // delta +-ACCURACY
    }


    @Test
    public void testSolverOneYearZ() {
        Vector3dInterface[] trajectory = simulateOneYear();
        // 2021-Apr-01 00:00:00
        double z366 = 2.033107664331608E+04; // reference implementation
        assertEquals(z366, trajectory[365].getZ(), ACCURACY); // delta +-ACCURACY
    }

    public static Vector3dInterface[] simulateOneDay() {
        double day = 24 * 60 * 60;
        SolarSystem universe = new SolarSystem(); // create solarSystem

        ODEFunctionInterface function = new NewtonsFunction();
        SolarSystemSolver solver = new SolarSystemSolver();
        StateInterface state = (StateInterface) new State(universe.bodies);

        StateInterface[] s = solver.solve(function, state, day, step_day);

        Vector3dInterface[] planet = new Vector3dInterface[s.length];
        for (int i = 0; i < s.length; i++) {
            State temporary = (State) s[i];
            planet[i] = temporary.celestialBodies.get(1).getLocation().mul(CONVERSION_UNIT_NASA); //1 is earth
            // get vector position and add it to planet saturn positions
        }

            return planet;

        }




    public static Vector3dInterface[] simulateOneYear() {
        double day = 24 * 60 * 60;
        double year = 365 * day; //changed to 365
        SolarSystem universe = new SolarSystem(); // create solarSystem

        ODEFunctionInterface function = new NewtonsFunction();
        SolarSystemSolver solver = new SolarSystemSolver();
        StateInterface state = (StateInterface) new State(universe.bodies);

        StateInterface[] s = solver.solve(function, state, year, step_Year);

        Vector3dInterface[] planet = new Vector3dInterface[s.length];
        for (int i = 0; i < s.length; i++) {
            State temporary = (State) s[i];
            planet[i] = temporary.celestialBodies.get(1).getLocation().mul(CONVERSION_UNIT_NASA); //1 is earth
            // get vector position and add it to planet saturn positions
        }
        return planet;
    }
}










