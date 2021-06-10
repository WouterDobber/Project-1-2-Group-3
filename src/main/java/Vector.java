
public class Vector implements Vector3dInterface {
	private double x;
	private double y;
	private double z;

	/**
	 * Initialising a vector
	 *
	 * @param x x-position, velocity, or acceleration
	 * @param y y-position, velocity, or acceleration
	 * @param z z-position, velocity, or acceleration
	 */
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Empty constructor
	 */
	public Vector() {
	}

	public Vector getClone() {
		return new Vector(x, y, z);
	}

	/**
	 *
	 * @return the x-value of the vector
	 */
	public double getX() {
		return x;
	}

	/**
	 *
	 * @return the y-value of the vector
	 */
	public double getY() {
		return y;
	}

	/**
	 *
	 * @return the z-value of the vector
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Set the x-value of the Vector
	 *
	 * @param x new x-value to be set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Set the y-value of the Vector
	 *
	 * @param y new y-value to be set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Set the z-value of the Vector
	 *
	 * @param z new z-value to be set
	 */
	public void setZ(double z) {
		this.z = z;
	}

	/**
	 *
	 * @return the Euclidian norm of a vectoro
	 */
	public double norm() { // used to be called getLength
		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Subtract two vectors from eachother
	 *
	 * @param other the vector that will be subtracted from the vector the method is
	 *              being called on
	 * @return the new, subtracted vector
	 */
	public Vector3dInterface sub(Vector3dInterface other) {
		return new Vector(x - other.getX(), y - other.getY(), z - other.getZ());
	}

	/**
	 * Add two vectors together
	 *
	 * @param other the vector that will be added to the vector the method is being
	 *              called on
	 * @return the new, added vector
	 */
	public Vector3dInterface add(Vector3dInterface other) {
		return new Vector(x + other.getX(), y + other.getY(), z + other.getZ());
	}

	/**
	 * Multiply a vector by a scalar for all x,y,z values
	 *
	 * @param scalar the value to multiply the coordinates by
	 * @return new vector of original multiplied by scalar
	 */
	public Vector3dInterface mul(double scalar) {
		return new Vector(scalar * x, scalar * y, scalar * z);
	}

	/**
	 * Divide a vector by a value for all x,y,z values
	 *
	 * @param amount the number to be divided by
	 * @return new divided vector
	 */
	public Vector divide(double amount) {
		return new Vector(x / amount, y / amount, z / amount);
	}

	/**
	 * @return the Euclidean distance between two vectors
	 */
	public double dist(Vector3dInterface other) {
		double newX = Math.pow(x - other.getX(), 2);

		double newY = Math.pow(y - other.getY(), 2);

		double newZ = Math.pow(z - other.getZ(), 2);

		return Math.sqrt(newX + newY + newZ);
	}

	/**
	 * Scalar x vector multiplication, followed by an addition
	 *
	 * @param scalar the double used in the multiplication step
	 * @param other  the vector used in the multiplication step
	 * @return the result of the multiplication step added to this vector, for
	 *         example:
	 *
	 *         Vector3d a = Vector(); double h = 2; Vector3d b = Vector(); ahb =
	 *         a.addMul(h, b);
	 *
	 *         ahb should now contain the result of this mathematical operation:
	 *         a+h*b
	 */
	public Vector3dInterface addMul(double scalar, Vector3dInterface other) {
		// create a new, empty vector
		Vector result = new Vector();

		// set the vector to the above described calculation
		result.setX((scalar * other.getX()) + x);
		result.setY((scalar * other.getY()) + y);
		result.setZ((scalar * other.getZ()) + z);
		return result;
	}

	/**
	 * Makes this vector have a magnitude of 1. When normalized, a vector keeps the
	 * same direction but its length is 1.0. Note that this function will change the
	 * current vector. If you want to keep the current vector unchanged, use
	 * normalized variable. If this vector is too small to be normalized it will be
	 * set to zero.
	 *
	 * @return normalized vector
	 */
	public Vector normalize() {
		return (Vector) mul(1.0 / norm());
	}

	/**
	 * @return A string in this format: Vector3d(-1.0, 2, -3.0) should print out
	 *         (-1.0,2.0,-3.0)
	 */
	public String toString() {
		return "(" + x + "," + y + "," + z + ")";
	}
	
		public Vector absolute() {
		Vector result = new Vector();
		// set the vector to the above described calculation
		result.setX(Math.abs(this.getX()));
		result.setY(Math.abs(this.getY()));
		result.setZ(Math.abs(this.getZ()));
		return result;
	}
	@Override
	public boolean equals(Vector x){
		if(this.getX()!=x.getX() || this.getY()!=x.getY() || this.getZ()!=x.getZ()){
			return false;
		}
		return true;
	}
}
