import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class Test_RateOfChange {

    @Test
    void testAddMul(){
        double scalar = 10;
        ArrayList<Vector> a = new ArrayList<>();
        Vector one = new Vector(1,2,3);
        Vector two = new Vector(4,5,6);
        a.add(one); a.add(two);
        RateOfChange a1 = new RateOfChange(a);
        ArrayList<Vector> b = new ArrayList<>();
        Vector three = new Vector(-3,0,2);
        Vector four = new Vector(0,19,2);
        b.add(three); b.add(four);
        RateOfChange b1 = new RateOfChange(b);

        ArrayList<Vector> expected = new ArrayList<>();
        Vector exp1 = new Vector(1 + (-3*scalar), 2 + (0*scalar), 3 + (2*scalar));
        Vector exp2 = new Vector(4 + (0*scalar), 5 + (19*scalar), 6 + (2*scalar));



        assertEquals(exp1.getX(),a1.addMul(scalar,b1).getRates().get(0).getX());
        assertEquals(exp1.getY(),a1.addMul(scalar,b1).getRates().get(0).getY());
        assertEquals(exp1.getZ(),a1.addMul(scalar,b1).getRates().get(0).getZ());
        assertEquals(exp2.getX(),a1.addMul(scalar,b1).getRates().get(1).getX());
        assertEquals(exp2.getY(),a1.addMul(scalar,b1).getRates().get(1).getY());
        assertEquals(exp2.getZ(),a1.addMul(scalar,b1).getRates().get(1).getZ());
    }

    @Test
    void testReturnAccelerations(){
        SolarSystem ss = new SolarSystem();
        State state = new State(ss.bodies);
        NewtonsFunction f = new NewtonsFunction();
        RateOfChange rate = (RateOfChange) f.call(0,state);

        assertEquals(rate.accelerations, rate.getRates());
    }


}
