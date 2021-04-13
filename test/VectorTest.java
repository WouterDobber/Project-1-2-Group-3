import static org.junit.jupiter.api.Assertions.*;

class VectorTest {
//100% coverage 

    @org.junit.jupiter.api.Test
    void getX() {
        Vector v= new Vector(1,2,3);
        assertEquals(1, v.getX());
    }

    @org.junit.jupiter.api.Test
    void getY() {
        Vector v= new Vector(1,2,3);
        assertEquals(2, v.getY());
    }

    @org.junit.jupiter.api.Test
    void getZ() {
        Vector v= new Vector(1,2,3);
        assertEquals(3, v.getZ());
    }

    @org.junit.jupiter.api.Test
    void setX() {
        Vector v= new Vector(0,0,0);
        v.setX(2);
        assertEquals(2,v.getX());
    }

    @org.junit.jupiter.api.Test
    void setY() {
        Vector v= new Vector(0,0,0);
        v.setY(2);
        assertEquals(2,v.getY());
    }

    @org.junit.jupiter.api.Test
    void setZ() {
        Vector v= new Vector(0,0,0);
        v.setZ(2);
        assertEquals(2,v.getZ());
    }

    @org.junit.jupiter.api.Test
    void norm() {
        Vector v= new Vector(1,2,3);
        assertEquals(Math.sqrt(14),v.norm());
    }

    @org.junit.jupiter.api.Test
    void sub() {
        Vector v= new Vector(1,2,3);
        Vector d= new Vector(1,2,0);
        Vector expected = new Vector (0,0,3);
        assertEquals(expected.toString(),v.sub(d).toString()); //making them string to be able to compare it

    }

    @org.junit.jupiter.api.Test
    void add() {
        Vector v= new Vector(1,2,3);
        Vector d= new Vector(1,2,0);
        Vector expected = new Vector (0,0,3);
        assertEquals(expected.toString(),v.sub(d).toString()); //making them string to be able to compare it
    }

    @org.junit.jupiter.api.Test
    void mul() {
        Vector v= new Vector(1,0,-1);
        Vector expected = new Vector (2,0,-2);
        assertEquals(expected.toString(), v.mul(2).toString()); //making them string to be able to compare it
    }

    @org.junit.jupiter.api.Test
    void divide() {
        Vector v= new Vector(2,0,-10);
        Vector expected = new Vector (1,0,-5);
        assertEquals(expected.toString(), v.divide(2).toString()); //making them string to be able to compare it
    }

    @org.junit.jupiter.api.Test
    void dist() {
        Vector v= new Vector(1,2,3);
        Vector d= new Vector(-1,5,0);
        Vector expected = new Vector (0,0,3);
        assertEquals(Math.sqrt(22),v.dist(d));
    }

    @org.junit.jupiter.api.Test
    void addMul() {
        Vector v= new Vector(1,2,3);
        Vector d= new Vector(-1,5,0);
        Vector expected = new Vector (-1,12,3);
        assertEquals(expected.toString(), v.addMul(2,d).toString());
    }

    @org.junit.jupiter.api.Test
    void normalize() {
        Vector v= new Vector(1,2,3);
        Vector expected = new Vector(1,2,3); //norm=14
        expected.setX(1.0/Math.sqrt(14));
        expected.setY(2.0/Math.sqrt(14));
        expected.setZ((3.0/Math.sqrt(14)));
        assertEquals(expected.toString(),v.normalize().toString());
    }

    @org.junit.jupiter.api.Test
    void testToString() {
        Vector v= new Vector(-1.0,2.0,-3.0);
        assertEquals("(-1.0,2.0,-3.0)",v.toString());
    }
}
