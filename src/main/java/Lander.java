public class Lander {
    Vector velocity;
    Vector position;
    double mass;
    double radius;
    double maxControllerForce;

    public Lander(Vector velocity, Vector position, double mass, double radius, double maxControllerForce) {
        this.velocity = velocity;
        this.position = position;
        this.mass = mass;
        this.radius = radius;
        this.maxControllerForce = maxControllerForce;
    }

    public String toString(){
        return "Velocity: " + velocity.toString() + "\n" + "Position: " + position.toString() + "\n" + "Angle: " + this.getAngle();
    }

    public Vector getVelocity() {
        return velocity;
    }

    public double getAngularVelocity(){
        return this.velocity.getZ();
    }

    public Vector getPosition() {
        return position;
    }

    public double getMass() {
        return mass;
    }

    public double getAngle() {
        return this.position.getZ();
    }

    public double getMaxControllerForce(){
        return this.maxControllerForce;
    }

    /**
     * I= 2/5 * m * r^2
     * @return momentum of inertia pf sphere
     */
    public double getMomentumOfInertia() {
        return (this.mass * Math.pow( this.radius, 2 )* 2)/5;
    }

    public double getRadius() {
        return this.radius;
    }

    public void setAngle(double angle) {
        this.position.setZ((angle));
    }

    public void setPositionX(double x) {
        this.position.setX(x);
    }
    public Lander copy(){
        return new Lander ( new Vector (velocity.getX(), velocity.getY(), velocity.getZ()),  new Vector (position.getX(), position.getY(), position.getZ()),  mass,  radius,  maxControllerForce);
    }
}
