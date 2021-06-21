public class Lander {
    Vector velocity;
    Vector position;
    double mass;
    double radius;
    double maxControllerForce;

    /**
     * Constructor
     * @param velocity
     * @param position
     * @param mass
     * @param radius
     * @param maxControllerForce
     */
    public Lander(Vector velocity, Vector position, double mass, double radius, double maxControllerForce) {
        this.velocity = velocity;
        this.position = position;
        this.mass = mass;
        this.radius = radius;
        this.maxControllerForce = maxControllerForce;
    }

    /**
     * To string method
     * @return velocity + position + angle
     */
    public String toString(){
        return "Velocity: " + velocity.toString() + "\n" + "Position: " + position.toString() + "\n" + "Angle: " + this.getAngle();
    }

    /**
     *
     * @return velocity
     */
    public Vector getVelocity() {
        return velocity;
    }

    /**
     *
     * @return angular velocity
     */
    public double getAngularVelocity(){
        return this.velocity.getZ();
    }

    /**
     *
     * @return position
     */
    public Vector getPosition() {
        return position;
    }

    /**
     *
     * @return mass
     */
    public double getMass() {
        return mass;
    }

    /**
     *
     * @return angle
     */
    public double getAngle() {
        return this.position.getZ();
    }

    /**
     *
     * @return max controller force
     */
    public double getMaxControllerForce(){
        return this.maxControllerForce;
    }

    /**
     * I= 2/5 * m * r^2
     * @return momentum of inertia pf sphere
     */
    public double getMomentumOfInertia() {
        return (2.0/5.0)*this.mass * Math.pow( this.radius, 2 );
    }

    /**
     *
     * @return radius 
     */
    public double getRadius() {
        return this.radius;
    }

    /**
     * set angle
     * @param angle
     */
    public void setAngle(double angle) {
        this.position.setZ((angle));
    }

    /**
     * set position 
     * @param x
     */
    public void setPositionX(double x) {
        this.position.setX(x);
    }

    /**
     *
     * @return new lander copied
     */
    public Lander copy(){
        return new Lander ( new Vector (velocity.getX(), velocity.getY(), velocity.getZ()),  new Vector (position.getX(), position.getY(), position.getZ()),  mass,  radius,  maxControllerForce);
    }
}
