import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.testng.Assert;


import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestNewtonsFunction {

    NewtonsFunction function = new NewtonsFunction();



        @Test
    void testTwoBodiesXBothPositive() {
        CelestialBody a = new CelestialBody("a", null, 10, new Vector(32, 1, 21), new Vector(5, 5, 5));
        CelestialBody b = new CelestialBody("b", null, 5, new Vector(2, 10, 10), new Vector(3, 3, 3));
        double G = 6.67430E-11;

        ArrayList<CelestialBody> bodies = new ArrayList<>(); bodies.add(a); bodies.add(b);
        State state = new State(bodies);

        Vector distance = new Vector(32-2, 1-10, 21-10).normalize();

        double dist = b.getLocation().dist(a.getLocation());
        double f = G * 50 / (dist * dist);

        RateOfChange result = (RateOfChange) function.call(0, state);
        Vector expected = distance.mul(f).divide(b.getMass());

        assertTrue(expected.equals(result.getRates().get(1)));
        }

    @Test
    void testTwoBodiesPosAndNeg() {
        CelestialBody a = new CelestialBody("a", null, 10, new Vector(-32, -1, -21), new Vector(5, 5, 5));
        CelestialBody b = new CelestialBody("b", null, 5, new Vector(2, 10, 10), new Vector(3, 3, 3));
        double G = 6.67430E-11;

        ArrayList<CelestialBody> bodies = new ArrayList<>(); bodies.add(a); bodies.add(b);
        State state = new State(bodies);

        Vector distance = new Vector(-32-2, -1-10, -21-10).normalize();

        double dist = b.getLocation().dist(a.getLocation());
        double f = G * 50 / (dist * dist);

        RateOfChange result = (RateOfChange) function.call(0, state);
        Vector expected = distance.mul(f).divide(b.getMass());

        assertTrue(expected.equals(result.getRates().get(1)));
    }

    @Test
    void testThreeBodiesAllPositive() {
        CelestialBody a = new CelestialBody("a", null, 10, new Vector(32, 1, 21), new Vector(5, 5, 5));
        CelestialBody b = new CelestialBody("b", null, 5, new Vector(10, 10, 10), new Vector(3, 3, 3));
        CelestialBody c = new CelestialBody("c", null, 12, new Vector(2, 14, 1), new Vector(3, 3, 3));

        double G = 6.67430E-11;

        ArrayList<CelestialBody> bodies = new ArrayList<>(); bodies.add(a); bodies.add(b); bodies.add(c);
        State state = new State(bodies);


        Vector distance = new Vector(32-10, 1-10, 21-10).normalize();
        Vector distance2 = new Vector(2-10,14-10,1-10).normalize();

        double dist = Math.sqrt( (Math.pow(32-10,2) + Math.pow(1-10,2) + Math.pow(21-10,2)));

        double dist2 = Math.sqrt( (Math.pow(10-2, 2) + Math.pow(10-14, 2) + Math.pow(10-1,2)));
        System.out.println(dist + "             " + dist2);

        double f1 = G * 50 / (dist * dist); //50 = 5*10
        double f2 = G * 60 / (dist2 * dist2); //60 = 5*12

        RateOfChange result = (RateOfChange) function.call(0, state);
        Vector expected = distance.mul(f1).divide(b.getMass());
        expected = (Vector) expected.add(distance2.mul(f2).divide(b.getMass()));

        assertTrue(expected.equals(result.getRates().get(1)));
    }
    @Test
    void testTwoBodiesXBothNegative() {
        CelestialBody a = new CelestialBody("a", null, 10, new Vector(-32, -1, -21), new Vector(5, 5, 5));
        CelestialBody b = new CelestialBody("b", null, 5, new Vector(-2, -10, -10), new Vector(3, 3, 3));
        double G = 6.67430E-11;

        ArrayList<CelestialBody> bodies = new ArrayList<>(); bodies.add(a); bodies.add(b);
        State state = new State(bodies);

        Vector distance = new Vector(-32-(-2), -1-(-10), -21-(-10)).normalize();
        double dist = b.getLocation().dist(a.getLocation()); double f = G * 50 / (dist * dist);

        RateOfChange result = (RateOfChange) function.call(0, state);
        Vector expected = distance.mul(f).divide(b.getMass());

        assertTrue(expected.equals(result.getRates().get(1)));
    }

    @Test
    void testTwoBodiesZeroAndPos() {
        CelestialBody a = new CelestialBody("a", null, 10, new Vector(4,3,1),  new Vector (5,5,5) );
        CelestialBody b = new CelestialBody("b", null, 5, new Vector(0,0,0),  new Vector (3,3,3) );
        double G = 6.67430E-11;

        ArrayList<CelestialBody> bodies = new ArrayList<>();
        bodies.add(a); bodies.add(b);
        State state = new State (bodies);

        Vector distance = new Vector(0-4,0-3,0-1).normalize();

        double dist = a.getLocation().dist(b.getLocation());
        double f = G*50/(dist*dist);

        RateOfChange result = (RateOfChange) function.call(0,state);
        Vector expected = distance.mul(f).divide(a.getMass());

        assertTrue(expected.equals(result.getRates().get(0)));
    }

    @Test
    void testTwoBodiesZeroAndNeg() {
        CelestialBody a = new CelestialBody("a", null, 10, new Vector(-3, -6, -12), new Vector(5, 5, 5));
        CelestialBody b = new CelestialBody("b", null, 5, new Vector(0, 0, 0), new Vector(3, 3, 3));
        double G = 6.67430E-11;

        ArrayList<CelestialBody> bodies = new ArrayList<>(); bodies.add(a); bodies.add(b);
        State state = new State(bodies);

        Vector distance = new Vector(-3-0, -6-0, -12-0).normalize();

        double dist = b.getLocation().dist(a.getLocation());
        double f = G * 50 / (dist * dist);

        RateOfChange result = (RateOfChange) function.call(0, state);
        Vector expected = distance.mul(f).divide(b.getMass());

        assertTrue(expected.equals(result.getRates().get(1)));
    }

}
