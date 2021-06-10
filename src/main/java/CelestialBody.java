
import java.awt.*;

public class CelestialBody {
	private String name;
	private double mass;
	private double radius;
	private Image pic;
	private Vector3dInterface location;
	private Vector3dInterface velocity;

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
	public CelestialBody(String name, Image pic, double mass, double x, double y, double z, double vX, double vY,
						 double vZ) {
		this.name = name;
		this.pic = pic;
		this.radius = radius;
		this.mass = mass;
		this.location = new Vector(x, y, z);
		this.velocity = new Vector(vX, vY, vZ);
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
