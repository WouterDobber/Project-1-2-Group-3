public class Thruster {
    private Vector maxForce;
    private Vector force = new Vector(0,0,0);

    public Thruster(Vector force){
        this.maxForce = force;
    }

    public Vector getForce(double mass){
        if(Math.abs(force.getY()) <= Math.abs(maxForce.getY()) && Math.abs(force.getX()) <= Math.abs(maxForce.getX())) { return force.divide(mass); }
        else { return maxForce.divide(mass); }
    }

    public void setForce(Vector force) {
        this.force = force;
    }

}
