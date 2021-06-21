public class LandSystem extends CelestialBody {
    public LandSystem(String name, double mass, double x, double y, double vX, double vY){
        super(name,mass, x,y,0,vX,vY,0);
    }

    // setting thrusters
    Thruster leftThruster = new Thruster(new Vector(200,0,0));
    Thruster rightThruster = new Thruster(new Vector(-200,0,0));
    Thruster bottomThruster = new Thruster(new Vector(0,300,0));
    PID xPID = new PID(0, 5, 1, 3);
    PID yPID = new PID(0, 5, 1, 3.1);


    public void update(){
        double xError = xPID.update(this.getLocation().getX());

        if(xError > 0) {
            leftThruster.setForce(new Vector(xError, 0, 0));
            rightThruster.setForce(new Vector(0, 0, 0));
        } else if(xError < 0) {
            rightThruster.setForce(new Vector(xError, 0, 0));
            leftThruster.setForce(new Vector(0, 0, 0));
        } else {
            rightThruster.setForce(new Vector(0, 0, 0));
            leftThruster.setForce(new Vector(0, 0, 0));
        }
        double yError = yPID.update(this.getLocation().getY());

        bottomThruster.setForce(new Vector(0, Math.abs(yError), 0));
    }

    public void updatePosition(){
        Vector change = (Vector) this.getVelocity();
        Vector temp = new Vector(change.getX(),change.getY() ,change.getZ());
        this.setLocation(this.getLocation().add(temp));
    }

    public void updateVelocity(){
        double y = Math.abs(this.getLocation().getY());
        double gravity = 1.352;
        double gForce = gravity*this.getMass()/y;

        // applying gravitational force
        this.setVelocity(this.getVelocity().add(new Vector(0 , -gForce,0 )));

        // call the wind
        this.setVelocity(this.getVelocity().add(new Vector(Wind.wind(this.getLocation().getY(),10),0,0)));

        // updating velocity
        this.setVelocity(this.getVelocity().add(bottomThruster.getForce(this.getMass())));
        this.setVelocity(this.getVelocity().add(leftThruster.getForce(this.getMass())));
        this.setVelocity(this.getVelocity().add(rightThruster.getForce(this.getMass())));
    }


}
