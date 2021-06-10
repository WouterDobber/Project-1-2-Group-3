public class Force {
    private double magnitude;
    private Vector3dInterface direction;
    private double force;
    private final double AREA = 300; //need to CHANGE it
    private final double PRESSURE_CONSTANT = 1;  //need to CHANGE it
    private final double dragCoefficient = 0.47; //for a circle

    public Force(double magnitude,  Vector3dInterface direction){
        this.magnitude = magnitude;
        this. direction = direction;
        // https://www.wikihow.com/Calculate-Wind-Load
        // F = area * Presure wind * drag coefficient
        // if circle, drag coefficient = 0.47
        // pressure wind = constant * v^2
        double pressureWind = PRESSURE_CONSTANT * magnitude*magnitude;
        this.force = AREA * pressureWind * dragCoefficient;

    }

    public double getForce(){
        return force;
    }

    public double getMagnitude(){
        return magnitude;
    }

    public Vector3dInterface getDirection(){
        return direction;
    }
}
