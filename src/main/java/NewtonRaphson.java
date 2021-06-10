import java.util.Arrays;
public class NewtonRaphson {
    public static Probe p = new Probe();
    public static final boolean  DEBUG = false;

    /**
     * Method calculates the best initial velocity to use when launching the rocket
     * @param v0
     * @param position
     * @param finalTime
     * @param h
     * @param accuracy
     */
    public static Vector calculate(Vector v0, Vector position, double finalTime, double h, double accuracy){
        Vector distanceProbeToTitan = p.closestVectorDistanceProbeTitan(position,v0, finalTime, h);
        Vector newVelocity= new Vector (0,0,0);
        Vector oldVelocity= v0;
        double euclideanDistanceProbeTitan= p.closestDistanceProbeTitan(position,v0, finalTime, h);

        final double delta = 1e-10;

        while (euclideanDistanceProbeTitan > accuracy){
            if (DEBUG) { System.out.println("DISTANCE  " + euclideanDistanceProbeTitan);}

            double jacobianMatrix [][] = JacobianMatrix(oldVelocity,delta, position, finalTime, h);

            if (DEBUG) { System.out.println("Jacobian  matrix " + Arrays.deepToString(jacobianMatrix));}
            jacobianMatrix= inverse(jacobianMatrix);

            if (DEBUG) {System.out.println("Jacobian inverted matrix " + Arrays.deepToString(jacobianMatrix));}

            Vector velocity= (Vector) oldVelocity.sub(multiply(jacobianMatrix ,distanceProbeToTitan) ); // new_v= old_v - Jinverse * g(old_v)
            newVelocity.setX(velocity.getX()); newVelocity.setY(velocity.getY()); newVelocity.setZ(velocity.getZ());

            Vector distance =  p.closestVectorDistanceProbeTitan(position,newVelocity, finalTime, h);//vector
            distanceProbeToTitan.setX(distance.getX()); distanceProbeToTitan.setY(distance.getY()); distanceProbeToTitan.setZ(distance.getZ());

            if (DEBUG) {System.out.println("New velocity " + newVelocity);}

            euclideanDistanceProbeTitan= p.closestDistanceProbeTitan(position,newVelocity, finalTime, h);
            oldVelocity.setX(newVelocity.getX()); oldVelocity.setZ(newVelocity.getZ()); oldVelocity.setY(newVelocity.getY());
        }

        System.out.println(newVelocity);
        return (newVelocity);
    }

    /**
     * Method to invert a 3x3 matrix using its determinant and cofactor matrix
     * @param matrix a 3x3 matrix
     * @return the inverted matrix
     */
    public static double [][] inverse (double [][] matrix){
        double[][] inverse = new double[3][3];

        double determinant = matrix[0][0] * (matrix[1][1]*matrix[2][2]-matrix[1][2]*matrix[2][1]) - matrix[0][1] * (matrix[1][0]*matrix[2][2]-matrix[1][2]*matrix[2][0]) + matrix[0][2] * (matrix[1][0]*matrix[2][1] - matrix[1][1]*matrix[2][0]);

        for(int i=0; i<matrix.length; i++){
            for(int j=0; j<matrix.length; j++){
                if(i==0){
                    if(j==0){ inverse[j][i] = (matrix[i+1][j+1]*matrix[i+2][j+2] - matrix[i+2][j+1]*matrix[i+1][j+2])/determinant; }
                    if(j==1){ inverse[j][i] = (matrix[i+1][j-1]*matrix[i+2][j+1] - matrix[i+2][j-1]*matrix[i+1][j+1])*-1/determinant; }
                    if(j==2){ inverse[j][i] = (matrix[i+1][j-2]*matrix[i+2][j-1] - matrix[i+2][j-2]*matrix[i+1][j-1])/determinant; }
                }
                if(i==1){
                    if(j==0){ inverse[j][i] = (matrix[i-1][j+1]*matrix[i+1][j+2] - matrix[i+1][j+1]*matrix[i-1][j+2])*-1/determinant; }
                    if(j==1){ inverse[j][i] = (matrix[i-1][j-1]*matrix[i+1][j+1] - matrix[i+1][j-1]*matrix[i-1][j+1])/determinant; }
                    if(j==2){ inverse[j][i] = (matrix[i-1][j-2]*matrix[i+1][j-1] - matrix[i+1][j-2]*matrix[i-1][j-1])*-1/determinant; }
                }
                if(i==2){
                    if(j==0){ inverse[j][i] = (matrix[i-2][j+1]*matrix[i-1][j+2] - matrix[i-1][j+1]*matrix[i-2][j+2])/determinant; }
                    if(j==1){ inverse[j][i] = (matrix[i-2][j-1]*matrix[i-1][j+1] - matrix[i-1][j-1]*matrix[i-2][j+1])*-1/determinant; }
                    if(j==2){ inverse[j][i] = (matrix[i-2][j-2]*matrix[i-1][j-1] - matrix[i-1][j-2]*matrix[i-2][j-1])/determinant; }
                }
            }
        }
        return inverse;
    }

    /**
     * Method multiplies a 3x3 matrix with a vector 3x1
     * @param matrix
     * @param vector
     * @return vector
     */
    public static Vector multiply (double [][] matrix, Vector vector){
        double []resultMatrix = new double [3];
        double [] matrixVector = {vector.getX(), vector.getY(), vector.getZ()};
        double result=0;
        for (int i=0; i<3;i++){
            result=0;
            for (int j=0; j<3; j++){
                result +=  matrix[i][j]* matrixVector[j];
            }
            resultMatrix[i]= result;
        }
        return new Vector (resultMatrix[0], resultMatrix[1], resultMatrix[2]);
    }


    /**
    *Calculates the Jacobian Matrix (3x3) using the following formula:
    * gi/vj = (gi(vj + h) - gi(vj -h)) / 2*h
    * where i and j are determinated by row, column position of the matrix and can be x,y,z.
    * g() is the shortest distance (vector) of the probe with respect to titan
    * @param delta: change in velocity
    * @param position: initial position
    * @param finalTime
    * @param h: step size
    * @param velocity
    * @return jacobianMatrix (3x3)
     */
    public static double[][] JacobianMatrix (Vector velocity, double delta, Vector position, double finalTime, double h){ //3x3 matrix
        double [][] matrix = new double [3][3];

        Vector vxPlus = new Vector(velocity.getX() + delta, velocity.getY(),  velocity.getZ());
        Vector vyPlus = new Vector(velocity.getX(), velocity.getY() + delta,  velocity.getZ());
        Vector vzPlus = new Vector(velocity.getX(), velocity.getY(),  velocity.getZ() + delta);
        Vector vxMinus = new Vector(velocity.getX() - delta, velocity.getY(),  velocity.getZ());
        Vector vyMinus = new Vector(velocity.getX(), velocity.getY() - delta,  velocity.getZ());
        Vector vzMinus = new Vector(velocity.getX(), velocity.getY(),  velocity.getZ() - delta);

        //calculating distances
        Vector distanceVXplus = p.closestVectorDistanceProbeTitan(position,vxPlus, finalTime, h);
        Vector distanceVXminus = p.closestVectorDistanceProbeTitan(position,vxMinus, finalTime, h);
        Vector distanceVYplus = p.closestVectorDistanceProbeTitan(position,vyPlus, finalTime, h);
        Vector distanceVYminus = p.closestVectorDistanceProbeTitan(position,vyMinus, finalTime, h);
        Vector distanceVZplus = p.closestVectorDistanceProbeTitan(position,vzPlus, finalTime, h);
        Vector distanceVZminus = p.closestVectorDistanceProbeTitan(position,vzMinus, finalTime, h);

        //filling up values in matrix
        matrix[0][0]= (distanceVXplus.getX() - distanceVXminus.getX()) / (2*delta);
        matrix[0][1]= (distanceVYplus.getX() - distanceVYminus.getX()) / (2*delta);
        matrix[0][2]= (distanceVZplus.getX() - distanceVZminus.getX()) / (2*delta);
        matrix[1][0]= (distanceVXplus.getY() - distanceVXminus.getY()) / (2*delta);
        matrix[1][1]= (distanceVYplus.getY() - distanceVYminus.getY()) / (2*delta);
        matrix[1][2]= (distanceVZplus.getY() - distanceVZminus.getY()) / (2*delta);
        matrix[2][0]= (distanceVXplus.getZ() - distanceVXminus.getZ()) / (2*delta);
        matrix[2][1]= (distanceVYplus.getZ() - distanceVYminus.getZ()) / (2*delta);
        matrix[2][2]= (distanceVZplus.getZ() - distanceVZminus.getZ()) / (2*delta);
        return matrix;
    }
}
