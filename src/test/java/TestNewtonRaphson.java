import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;


class TestNewtonRaphson {
   @Test
    void multiply() {
        double [][] matrix = {{-1, -2, 2}, //[00,01,02]
                {2, 1, 1},   //[10,11,12]
                {3, 4, 5}};  //[20,21,22]

        Vector v= new Vector (1,1,0);
        Vector p= NewtonRaphson.multiply(matrix,v);
        assertEquals( p.toString(), new Vector (-3,3,7).toString());
    }
}
