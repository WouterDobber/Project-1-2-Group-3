import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;


class TestNewtonRaphson {
    @Test
    void inverse() {
        double[][] e = {{-1.0, -2, 0}, //[00,01,02]
                {2, 1, 0},   //[10,11,12]
                {3, 4.3333333, 0}};  //[20,21,22]
        double[][] d = NewtonRaphson.inverse(e);
        double [][] expected = {{-0.5, 1.0, -0.5},
                                {0.09090909090909091, -0.6363636363636364, 0.45454545454545453},
                                {0.22727272727272727, -0.09090909090909091, 0.13636363636363635}};
        assertEquals( expected, d); //doesnt work to compare them u_u
    }

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
