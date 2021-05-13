import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class Test_NewtonsFunction {

    @Test
    void testCall() {
    CelestialBody a = new CelestialBody("a", null, 10, new Vector(0,0,0),  new Vector (5,5,5) );
    CelestialBody b = new CelestialBody("b", null, 5, new Vector(2,2,2),  new Vector (3,3,3) );
        double G = 6.67430E-11;

        ArrayList<CelestialBody> bodies = new ArrayList<>();
        bodies.add(a); bodies.add(b);

        State state = new State (bodies);
        NewtonsFunction function = new NewtonsFunction();
        RateOfChange result = (RateOfChange) function.call(0,state);
        assertEquals(G*(50/12), result.getRates().get(0)); //expected is a vector not a value , FIIIIIIIIIIIIX THISSSSSS 


    }
    
    //this is for another class that i wanted to test, can be ignored 
    @Test
    public void testAddMulRate (){
        ArrayList<Vector> addRates = new ArrayList<>();
        addRates.add(new Vector(2,3,4));
        RateOfChange rate = new RateOfChange(addRates);

        ArrayList<Vector> addRates2 = new ArrayList<>();
        addRates2.add(new Vector(0,1,0));
        RateOfChange rate2 = new RateOfChange(addRates2);


        RateOfChange result = rate.addMul(-1, rate2);
        assertEquals(result.accelerations.get(0).toString(), "(2.0,2.0,4.0)" );

    }



}
