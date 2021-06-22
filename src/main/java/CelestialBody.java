import java.awt.*;

public class CelestialBody {
	private String name;
	private double mass;
	private double radius;
	private Image pic;
	private Vector3dInterface location;
	private Vector3dInterface velocity;
	private double mdot;
	private Vector dir;

	/**
	 * Constructor
	 *
	 * @param name the name of the planet
	 * @param pic  the picture of the planet
	 * @param mass mass of planet
	 * @param x    initial x-coordinate of planet
	 * @param y    initial y-coordinate of planet
	 * @param z    initial z-coordinate of planet
	 * @param vX   initial x-velocity of planet
	 * @param vY   initial x-velocity of planet
	 * @param vZ   initial x-velocity of planet
	 */
	public CelestialBody(String name, Image pic, double mass, double x, double y, double z, double vX, double vY, double vZ) {
		this.name = name;
		this.pic = pic;
		this.mass = mass;
		this.location = new Vector(x, y, z);
		this.velocity = new Vector(vX, vY, vZ);
	}
	
	/**
	 * Constructor
	 *
	 * @param name the name of the planet
	 * @param mass mass of planet
	 * @param x    initial x-coordinate of planet
	 * @param y    initial y-coordinate of planet
	 * @param z    initial z-coordinate of planet
	 * @param vX   initial x-velocity of planet
	 * @param vY   initial x-velocity of planet
	 * @param vZ   initial x-velocity of planet
	 */
	public CelestialBody(String name, double mass, double x, double y, double z, double vX, double vY, double vZ) {
		this.name = name;
		this.mass = mass;
		this.location = new Vector(x*1000, y*1000, z*1000);
		this.velocity = new Vector(vX*1000, vY*1000, vZ*1000);
	}
	/**
	 *
	 * @param name
	 * @param pic
	 * @param mass
	 * @param position
	 * @param velocity
	 */
	public CelestialBody(String name, Image pic, double mass, Vector3dInterface position, Vector3dInterface velocity) {
		this.name = name;
		this.mass = mass;
		this.pic = pic;
		this.radius = radius;
		this.location = position;
		this.velocity = velocity;
	}

	/**
	 * It returns a copy of the celestial body 
	 *
	 */
	public CelestialBody duplicate (){
		return new CelestialBody(name, pic, mass, location, velocity);
	}

	/**
	 *
	 * @return name of planet
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 * @return mass of planet
	 */
	public double getMass() {
		return mass;
	}
	
	public void addMass(double newMass){
		mass+= newMass;
	}
	
	public boolean canThrust(double usedMass){
		if (mass - usedMass >= 84000){
			return true;	
		}
		else {	
			return false;
		}
	}
	
	public void setMdot(double mdot1){
		mdot = mdot1;
	}
	
	public double getMdot(){
		return mdot;
	}
	
	public Vector getDir(){
		return dir;	
	}
	
	public void setDir(Vector direction){
		dir = direction;	
	}

	/**
	 *
	 * @return Vector containing x,y,z position coordinates of the celestial body
	 */
	public Vector3dInterface getLocation() {
		return location;
	}

	/**
	 *
	 * @return Vector containing x,y,z velocities of the celestial body
	 */
	public Vector3dInterface getVelocity() {
		return velocity;
	}

	/**
	 * Sets the location of the body to the given vector
	 *
	 * @param value Vector containing position coordinates
	 */
	public void setLocation(Vector3dInterface value) {
		location = value;
	}

	/**
	 * Sets the velocity of the body to the given vector
	 *
	 * @param value Vector containing x,y,z velocities
	 */
	public void setVelocity(Vector3dInterface value) {
		velocity = value;
	}

	/**
	 * Sets the radius of the celestial body
	 *
	 * @param radius new radius of the body
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}

	/**
	 *
	 * @return the radius
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 *
	 * @return String containing name, position and location of body
	 */
	public String toString() {
		String result = name + " position: " + location + " velocity: " + velocity;
		return result;
	}

	/**
	 *
	 * @return an Image of the body
	 */
	public Image getImage() {
		return this.pic;
	}
}