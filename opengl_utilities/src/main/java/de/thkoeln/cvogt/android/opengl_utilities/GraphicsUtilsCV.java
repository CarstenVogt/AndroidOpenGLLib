// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 30.3.2022

package de.thkoeln.cvogt.android.opengl_utilities;

import android.graphics.Point;
import android.opengl.Matrix;
import android.util.Log;

/**
 * Class with some utility methods for 2D and 3D graphics programming.
 */

public class GraphicsUtilsCV {

    /** Method to calculate the distance between two points in 2D space.
     *
     * @param p1 First point
     * @param p2 Second point
     * @return Distance between the points
     */

    public static double distance(Point p1, Point p2) {
        return distance(p1.x,p1.y,p2.x,p2.y);
    }

    /** Method to calculate the distance between two points in 2D space.
     *
     * @param p1x First point: x position
     * @param p1y First point: y position
     * @param p2x Second point: x position
     * @param p2y Second point: y position
     * @return Distance between the points
     */

    public static double distance(double p1x, double p1y, double p2x, double p2y) {
        return Math.sqrt((p2x-p1x)*(p2x-p1x)+(p2y-p1y)*(p2y-p1y));
    }

    /** Method to calculate the length of a vector in 2D or 3D space.
     *
     * @param vector The vector.
     * @return The length of the vector or -1 if the parameter is not valid
     * (i.e. is null or has a length other than 2 or 3).
     */

    public static float vectorLength(float[] vector) {
        if (vector==null||vector.length<2||vector.length>3) return -1;
        if (vector.length==2)
            return (float)Math.sqrt(vector[0]*vector[0]+vector[1]*vector[1]);
          else
            return (float)Math.sqrt(vector[0]*vector[0]+vector[1]*vector[1]+vector[2]*vector[2]);
    }

    /** Method to determine the vector between two points in 3D space.
     *
     * @param p1 The first point: Array of length 3 with the (x,y,z) coordinates (in this order).
     * @param p2 The second point: Array of length 3 with the (x,y,z) coordinates (in this order).
     * @return The vector between the points or null if one of the parameters is not valid
     * (i.e. is null or has a length other than 3).
     */

    public static float[] vectorBetweenPoints(float[] p1, float[] p2) {
        if (p1==null||p1.length!=3||p2==null||p2.length!=3) return null;
        float result[] = new float[3];
        result[0] = p2[0]-p1[0];
        result[1] = p2[1]-p1[1];
        result[2] = p2[2]-p1[2];
        return result;
    }

    /** Method to calculate the distance between two points in 3D space.
     *
     * @param p1 The first point: Array of length 3 with the (x,y,z) coordinates (in this order).
     * @param p2 The second point: Array of length 3 with the (x,y,z) coordinates (in this order).
     * @return The distance between the points or -1 if one of the parameters is not valid
     * (i.e. is null or has a length other than 3).
     */

    public static float distance(float[] p1, float[] p2) {
        if (p1==null||p1.length!=3||p2==null||p2.length!=3) return -1;
        return vectorLength(vectorBetweenPoints(p1,p2));
    }

    /** Method that returns a random point in 3D space.
     * @param minX The minimum value for the x coordinate (inclusive).
     * @param maxX The maximum value for the x coordinate (exclusive).
     * @param minY The minimum value for the y coordinate (inclusive).
     * @param maxY The maximum value for the y coordinate (exclusive).
     * @param minZ The minimum value for the z coordinate (inclusive).
     * @param maxZ The maximum value for the z coordinate (exclusive).
     * @return The random point (float array of length 3 with the x, y, and z coordinates)
     * or null if one of the three parameter pairs defines no valid interval.
     */

    public static float[] randomPoint3D(float minX, float maxX, float minY, float maxY, float minZ, float maxZ) {
        if (minX>maxX||minY>maxY||minZ>maxZ) return null;
        float[] result = new float[3];
        result[0] = minX + (float)((maxX-minX)*Math.random());
        result[1] = minY + (float)((maxY-minY)*Math.random());
        result[2] = minZ + (float)((maxZ-minZ)*Math.random());
        return result;
    }

    /** Method to map multiple 2D points to 3D space.
     * The x and y coordinates will be copied, the z coordinate will be set to 0.
     * @param points2D The points in 2D space.
     * @return The points in 3D space (second dimension: index 0 - x coordinate, 1 - y coordinate, 2 - z coordinate = 0)
     * or null if points2D is null.
     */

    public static float[][] points2Dto3D(Point[] points2D) {
        if (points2D==null) return null;
        float[][] result = new float[points2D.length][3];
        for (int i=0; i<points2D.length; i++) {
            result[i][0] = points2D[i].x;
            result[i][1] = points2D[i].y;
            result[i][2] = 0;
        }
        return result;
    }

    /** Method to get the homogeneous coordinate representation of a point in 3D space.
     * @param coordinates The x, y, and z coordinates of the point (must be an array of length 3).
     * @return The homogeneous coordinate representation of the point,
     * i.e. a vector of length 4 with copies of the values of the parameter array in the first three positions
     * and a 1 in the last position.
     * Null if the parameter is not valid.
     */

    public static float[] homogeneousCoordsForPoint(float[] coordinates) {
        if (coordinates==null||coordinates.length!=3) return null;
        float result[] = new float[4];
        for (int i=0; i<3; i++) result[i] = coordinates[i];
        result[3] = 1;
        return result;
    }

    /** Method to get the homogeneous coordinate representation of a vector in 3D space.
     * @param coordinates The x, y, and z coordinates of the vector (must be an array of length 3).
     * @return The homogeneous coordinate representation of the vector,
     * i.e. a vector of length 4 with copies of the values of the parameter array in the first three positions
     * and a 0 in the last position.
     * Null if the parameter is not valid.
     */

    public static float[] homogeneousCoordsForVector(float[] coordinates) {
        if (coordinates==null||coordinates.length!=3) return null;
        float result[] = new float[4];
        for (int i=0; i<3; i++) result[i] = coordinates[i];
        result[3] = 0;
        return result;
    }

    /** Method to get the x, y, and z coordinates from the homogeneous representation of a point or vector in 3D space.
     * @param homogeneous The homogeneous representation (must be an array of length 4).
     * @return The 3D coordinates of the point (array of length 3 with x in array position 0, y in pos. 1, z in pos. 2).
     * Null if the parameter is not valid.
     */

    public static float[] coordsFromHomogeneous(float[] homogeneous) {
        if (homogeneous==null||homogeneous.length!=4) return null;
        float result[] = new float[3];
        for (int i=0; i<3; i++) result[i] = homogeneous[i];
        return result;
    }

    /** Method to calculate the midpoint between two points in 3D space.
     * @param p1 The first point: Array of length 3 with the (x,y,z) coordinates (in this order).
     * @param p2 The second point: Array of length 3 with the (x,y,z) coordinates (in this order).
     * @return The midpoint between the two points or null if one of the parameters is not valid
     * (i.e. is null or has a length other than 3).
     */

    public static float[] midpoint(float[] p1, float[] p2) {
        if (p1==null||p1.length!=3||p2==null||p2.length!=3) return null;
        float[] result = new float[3];
        for (int i=0;i<3;i++) {
            result[i] = p1[i] + (p2[i] - p1[i]) / 2;
            // Log.v("GLDEMO",i+": "+p1[i]+" "+p2[i]+" "+result[i]);
        }
        return result;
    }

    /** Method to calculate a number of points in 3D space that lie equidistantly between two end points.
     * @param endpoint1 The first end point: Array of length 3 with the (x,y,z) coordinates (in this order).
     * @param endpoint2 The second end point: Array of length 3 with the (x,y,z) coordinates (in this order).
     * @param numberOfPoints The number of points to be calculated (>1).
     * @return The points as a two-dimensional array of size 'numberOfPoints' in the first dimension
     * and 3 in the second dimension (specifying the x, y, and z coordinates).
     * The first point (index 0) is endpoint1, the last point (index numberOfPoints-1) is endpoint2.
     * Null is returned if one of the end point parameters is not valid (i.e. null or not of length 3)
     * or numberOfPoints is not greater than 1.
     */

    public static float[][] pointsInLine(float[] endpoint1, float[] endpoint2, int numberOfPoints) {
        if (endpoint1==null||endpoint1.length!=3||endpoint2==null||endpoint2.length!=3||numberOfPoints<2) return null;
        float[][] result = new float[numberOfPoints][3];
        for (int i=0; i<numberOfPoints; i++)
            if (i<numberOfPoints-1)
                for (int j=0; j<3; j++) result[i][j] = endpoint1[j]+(endpoint2[j]-endpoint1[j])/(numberOfPoints-1)*i;
            else
                result[i] = endpoint2.clone();
        return result;
    }

    /** Method to normalize a vector in 3D space.
     *
     * @param vector The vector to normalize.
     * @return true if the operation was successful.
     * false if the parameter is not valid (i.e. is null or has a length other than 3) or if the length of the vector is zero or very close to zero.
     */

    public static boolean normalize(float[] vector) {
        if (vector==null||vector.length!=3) return false;
        float length = vectorLength(vector);
        if (length<1E-9f) return false;
        vector[0] /= length;
        vector[1] /= length;
        vector[2] /= length;
        return true;
    }

    /** Method to get a normalized copy of a vector in 3D space.
     *
     * @param vector The vector to normalize.
     * @return A normalized copy of the vector, i.e. a vector of length 1 with the same direction.
     * null if the parameter is not valid (i.e. is null or has a length other than 3) or if the length of the vector is zero or very close to zero.
     */

    public static float[] getNormalizedCopy(float[] vector) {
        if (vector==null||vector.length!=3) return null;
        float[] result = new float[3];
        float length = vectorLength(vector);
        if (length<1E-9f) return null;
        result[0] = vector[0]/length;
        result[1] = vector[1]/length;
        result[2] = vector[2]/length;
        return result;
    }

    /** Method to calculate the dot product of two vectors in 3D space.
     * @param vec1 The first vector.
     * @param vec2 The second vector.
     * @return The dot product or -1 if one of the parameters is not valid
     * (i.e. is null or has a length other than 3).
     */

    public static float dotProduct(float[] vec1, float[] vec2) {
        if (vec1==null||vec1.length!=3||vec2==null||vec2.length!=3) return -1;
        return vec1[0]*vec2[0]+vec1[1]*vec2[1]+vec1[2]*vec2[2];
    }

    /** Method to calculate the cross product of two vectors in 3D space,
     *  i.e. a vector that is perpendicular to the plane spanned by the two vectors.
     * @param vec1 The first vector.
     * @param vec2 The second vector.
     * @return The cross product or null if one of the parameters is not valid
     * (i.e. is null or has a length other than 3).
     */

    public static float[] crossProduct(float[] vec1, float[] vec2) {
        if (vec1==null||vec1.length!=3||vec2==null||vec2.length!=3) return null;
        float result[] = new float[3];
        result[0] = vec1[1]*vec2[2]-vec1[2]*vec2[1];
        result[1] = vec1[2]*vec2[0]-vec1[0]*vec2[2];
        result[2] = vec1[0]*vec2[1]-vec1[1]*vec2[0];
        return result;
    }

    /** Method to check if two double values are nearly equal,
     * i.e. if their difference is smaller than 1E-6.
     * @param f1 The first value.
     * @param f2 The second value.
     * @return true iff they are nearly equal.
     */

    public static boolean valuesEqual(double f1, double f2) {
        return valuesEqual(f1,f2,1E-6);
    }

    /** Method to check if two double values are nearly equal,
     * i.e. if their difference is smaller than the parameter 'epsilon'.
     * @param f1 The first value.
     * @param f2 The second value.
     * @param f2 The difference threshold.
     * @return true iff their difference is smaller than epsilon.
     */

    public static boolean valuesEqual(double f1, double f2, double epsilon) {
        return Math.abs(f1-f2)<epsilon;
    }

    /** Method to check if a matrix is an identity matrix,
     * i.e. a square matrix with ones in its diagonal and zeros elsewhere.
     * @param matrix The matrix.
     * @return true iff the matrix is an identity matrix
     */

    public static boolean isIdentity(float[][] matrix) {
        if (matrix==null||matrix.length!=matrix[0].length) return false;
        for (int i=0; i<matrix.length; i++)
            if (!valuesEqual(matrix[i][i],1.0)) return false;
        for (int i=0; i<matrix.length; i++)
            for (int j=0; j<matrix[i].length; j++)
                if (i!=j && !valuesEqual(matrix[i][j],0.0)) return false;
        return true;
    }

    /** Method to check if a matrix is an 4x4 rotation matrix.
     * @param matrix The matrix.
     * @return true iff the matrix is a rotation matrix.
     */

    public static boolean is4x4RotationMatrix(float[][] matrix) {
        if (matrix==null||matrix.length!=4) return false;
        for (int i=0; i<matrix.length; i++)
            if (matrix[i].length!=4) return false;
        // for the following see https://math.stackexchange.com/questions/261617/find-the-rotation-axis-and-angle-of-a-matrix/1886992
        float[] m1 = arrayFromMatrix(matrix);
        float[] m2 = new float[16];
        Matrix.transposeM(m2,0,m1,0);
        float[] m3 = new float[16];
        Matrix.multiplyMM(m3,0,m2,0,m1,0);
        // hier eigentlich noch prüfen, ob die Determinante = 1 ist
        // (wenn -1, dann Spiegelung)
        return isIdentity(matrixFromArray(m3,4,4));
    }

    /** Method to transform a two-dimensional matrix into a one-dimensional array,
     * copying its columns one after the other
     * (needed e.g. to transform a scaling, rotation or transformation matrix
     * into the one-dimensional format required by OpenGL).
     * @param matrix The matrix to be transformed.
     * @return The corresponding array; null if the parameter is not valid.
     */

    public static float[] arrayFromMatrix(float[][] matrix) {
        if (matrix==null) return null;
        float[] array = new float[matrix.length*matrix[0].length];
        for (int i=0;i<array.length;i++)
            array[i] = matrix[i%matrix.length][i/matrix.length];
        return array;
    }

    /** Method to transform a one-dimensional array into a two-dimensional matrix,
     * i.e. to copy its lines one after the other to a one-dimensional array
     * (needed e.g. to transform the one-dimensional matrix format of OpenGL
     * into a two-dimensional Java array).
     * @param array The array to be transformed.
     * @param lines The number of lines of the resulting matrix.
     * @param columns The number of columns of the resulting matrix.
     * @return The corresponding array; null if one of the parameters is not valid.
     */

    public static float[][] matrixFromArray(float[] array, int lines, int columns) {
        if (array==null||lines<1||columns<1||array.length!=lines*columns) return null;
        if (array==null||lines<1||columns<1||array.length!=lines*columns) return null;
        float[][] matrix = new float[lines][columns];
        for (int i=0;i<array.length;i++)
            matrix[i%lines][i/lines] = array[i];
        return matrix;
    }

    /**
     * Method to calculate a rotation matrix from three Euler angles (= cardan angles)
     * Code taken and adapted from: https://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToMatrix/index.htm
     * Note: Euler rotations are not commutative! Current rotation order: X > Z > Y (THIS ORDER IS YET TO BE ADAPTED: X > Y > Z)
     * ALSO TO BE ADAPTED: POSITIVE / NEGATIVE ROTATION ANGLE (?) BUT: IS CURRENTLY THE SAME AS WITH THE OTHER TWO APPROACHES
     * @param eulerX The Euler angle in the x dimension.
     * @param eulerY The Euler angle in the y dimension.
     * @param eulerZ The Euler angle in the z dimension.
     * @return The 4x4 rotation matrix as a one-dimensional array of length 16.
     */

    public static float[] rotationMatrixFromEulerAngles(float eulerX, float eulerY, float eulerZ) {

        // Matrix.setRotateEulerM(rotationMatrix,0,eulerX,eulerY,eulerZ);
        // THE IMPLEMENTATION OF THE METHOD Matrix.setRotateEulerM() IS BUGGY (AS OF 18.9.22), DOES NOT ROTATE CORRECTLY AROUND THE Y AXIS.

        float cosX = (float) Math.cos(Math.PI*eulerX/180.0);
        float sinX = (float) Math.sin(Math.PI*eulerX/180.0);
        float cosY = (float) Math.cos(Math.PI*eulerY/180.0);
        float sinY = (float) Math.sin(Math.PI*eulerY/180.0);
        float cosZ = (float) Math.cos(Math.PI*eulerZ/180.0);
        float sinZ = (float) Math.sin(Math.PI*eulerZ/180.0);

        float[] rotationMatrix = new float[16];

        rotationMatrix[0] = cosY * cosZ;
        rotationMatrix[1] = sinY*sinX - cosY*sinZ*cosX;
        rotationMatrix[2] = cosY*sinZ*sinX + sinY*cosX;
        rotationMatrix[3] = 0.0f;
        rotationMatrix[4] = sinZ;
        rotationMatrix[5] = cosZ*cosX;
        rotationMatrix[6] = -cosZ*sinX;
        rotationMatrix[7] = 0.0f;
        rotationMatrix[8] = -sinY*cosZ;
        rotationMatrix[9] = sinY*sinZ*cosX + cosY*sinX;
        rotationMatrix[10] = -sinY*sinZ*sinX + cosY*cosX;
        rotationMatrix[11] = rotationMatrix[12] = rotationMatrix[13] = rotationMatrix[14] = 0.0f;
        rotationMatrix[15] = 1.0f;

        return rotationMatrix;

    }

    // PROPOSED SOLUTION IN https://issuetracker.google.com/issues/36923403, Aug 17, 2011 10:20PM
    // (but this is not what is needed here: seems to be rotation in the world coordinate space, not in the model coordinate space!)
    /*
    float sinX = (float) Math.sin(Math.PI*eulerX/180.0);
    float sinY = (float) Math.sin(Math.PI*eulerY/180.0);
    float sinZ = (float) Math.sin(Math.PI*eulerZ/180.0);
    float cosX = (float) Math.cos(Math.PI*eulerX/180.0);
    float cosY = (float) Math.cos(Math.PI*eulerY/180.0);
    float cosZ = (float) Math.cos(Math.PI*eulerZ/180.0);
    float cosXsinY = cosX * sinY;
    float sinXsinY = sinX * sinY;
    rotationMatrix[0] = cosY * cosZ;
    rotationMatrix[1] = -cosY * sinZ;
    rotationMatrix[2] = sinY;
    rotationMatrix[3] = 0.0f;
    rotationMatrix[4] = sinXsinY * cosZ + cosX * sinZ;
    rotationMatrix[5] = -sinXsinY * sinZ + cosX * cosZ;
    rotationMatrix[6] = -sinX * cosY;
    rotationMatrix[7] = 0.0f;
    rotationMatrix[8] = -cosXsinY * cosZ + sinX * sinZ;
    rotationMatrix[9] = cosXsinY * sinZ + sinX * cosZ;
    rotationMatrix[10] = cosX * cosY;
    rotationMatrix[11] = 0.0f;
    rotationMatrix[12] = 0.0f;
    rotationMatrix[13] = 0.0f;
    rotationMatrix[14] = 0.0f;
    rotationMatrix[15] = 1.0f; */

    /**
     * Given are a rotation axis and a rotation angle around this axis in 3d space.
     * Returned is the angle for a rotation around the x axis that is to be applied
     * together with corresponding rotations around the y axis and z axis
     * (see methods eulerAngleY() and eulerAngleZ()) to get the same rotation.
     * @param axis The rotation axis (as a vector with origin (0,0,0)).
     * @param angle The rotation angle (degrees).
     * @return The rotation angle around the x axis (degrees).
     */

    public static float eulerAngleX(float[] axis, float angle) {
        // calculation according to https://gamedev.stackexchange.com/questions/50963/how-to-extract-euler-angles-from-transformation-matrix
        float[] rotMatrix = new float[16];
        Matrix.setIdentityM(rotMatrix,0);
        Matrix.rotateM(rotMatrix,0,angle,axis[0],axis[1],axis[2]);
        /*
        Log.v("DEMO2","RotMatrix X:");
        for (int i=0; i<4; i++) {
            String out = "";
            for (int j=0; j<4; j++)
                out += rotMatrix[i*4+j]+" ";
            Log.v("DEMO2",out);
        }
         */
        return (float) -Math.toDegrees(Math.atan2(-rotMatrix[6],rotMatrix[5]));
    }

    /**
     * Given are a rotation axis and a rotation angle around this axis in 3d space.
     * Returned is the angle for a rotation around the y axis that is to be applied
     * together with corresponding rotations around the x axis and z axis
     * (see methods eulerAngleX() and eulerAngleZ()) to get the same rotation.
     * @param axis The rotation axis (as a vector with origin (0,0,0)).
     * @param angle The rotation angle (degrees).
     * @return The rotation angle around the y axis (degrees).
     */

    public static float eulerAngleY(float[] axis, float angle) {
        // calculation according to https://gamedev.stackexchange.com/questions/50963/how-to-extract-euler-angles-from-transformation-matrix
        float[] rotMatrix = new float[16];
        Matrix.setIdentityM(rotMatrix,0);
        Matrix.rotateM(rotMatrix,0,angle,axis[0],axis[1],axis[2]);
        return (float) -Math.toDegrees(Math.atan2(-rotMatrix[8],rotMatrix[0]));
    }

    /**
     * Given are a rotation axis and a rotation angle around this axis in 3d space.
     * Returned is the angle for a rotation around the z axis that is to be applied
     * together with corresponding rotations around the x axis and y axis
     * (see methods eulerAngleX() and eulerAngleY()) to get the same rotation.
     * @param axis The rotation axis (as a vector with origin (0,0,0)).
     * @param angle The rotation angle (degrees).
     * @return The rotation angle around the z axis (degrees).
     */

    public static float eulerAngleZ(float[] axis, float angle) {
        // calculation according to https://gamedev.stackexchange.com/questions/50963/how-to-extract-euler-angles-from-transformation-matrix
        float[] rotMatrix = new float[16];
        Matrix.setIdentityM(rotMatrix,0);
        Matrix.rotateM(rotMatrix,0,angle,axis[0],axis[1],axis[2]);
        return (float) -Math.toDegrees(Math.asin(rotMatrix[4]));
    }

    /**
     * Given is a 4x4 matrix specifying a rotation in 3d space.
     * Returned is the corresponding rotation axis.
     * @param rotMatrix The rotation matrix.
     * @return The rotation angle (degrees)
     * or -1000 if rotMatrix is not a valid rotation matrix.
     */

    public static float rotAngleFrom4x4RotationMatrix(float[][] rotMatrix) {
        if (!is4x4RotationMatrix(rotMatrix)) return -1000;
        // get the rotation axis
        float[] rotAxis = rotAxisFrom4x4RotationMatrix(rotMatrix);
        // find a vector v that is perpendicular to the rotation axis
        float[] v = new float[3];
        if (!valuesEqual(rotAxis[0],0)) { v[0]=-(rotAxis[1]+rotAxis[2])/rotAxis[0]; v[1]=v[2]=1; }
        else if (!valuesEqual(rotAxis[1],0)) { v[0]=v[2]=1; v[1]=-rotAxis[2]/rotAxis[1]; }
        else { v[0]=v[1]=1; v[2]=0; }
        // rotate v by the rotation matrix
        float[] rotV = new float[3];
        rotV[0] = rotMatrix[0][0]*v[0]+rotMatrix[0][1]*v[1]+rotMatrix[0][2]*v[2];
        rotV[1] = rotMatrix[1][0]*v[0]+rotMatrix[1][1]*v[1]+rotMatrix[1][2]*v[2];
        rotV[2] = rotMatrix[2][0]*v[0]+rotMatrix[2][1]*v[1]+rotMatrix[2][2]*v[2];
        // calculate the angle between v and rotV
        float normedDotProduct = GraphicsUtilsCV.dotProduct(v,rotV)/(vectorLength(v)*vectorLength(rotV));
        if (normedDotProduct>1) normedDotProduct=1; // to handle rounding errors
        if (normedDotProduct<-1) normedDotProduct=-1;
        return (float) Math.toDegrees(Math.acos(normedDotProduct));
    }

    /**
     * Given is a 4x4 matrix specifying a rotation in 3d space.
     * Returned is the corresponding rotation axis.
     * @param rotMatrix The rotation matrix.
     * @return A float array of length 3 with the x, y, and z coordinates of the rotation axis in positions 0-2
     * or null if rotMatrix is not a valid rotation matrix.
     */

    public static float[] rotAxisFrom4x4RotationMatrix(float[][] rotMatrix) {
        // if (!is4x4RotationMatrix(rotMatrix)) return null;
        // calculation according to https://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToAngle/index.htm
        // and https://en.wikipedia.org/wiki/Rotation_matrix#Conversion_from_rotation_matrix_to_axis%E2%80%93angle
        float[] result = new float[3];
        if (isIdentity(rotMatrix)) {
            // identity matrix > no rotation > return some arbitrary axis
            result[0] = 1;
            result[1] = result[2] = 0;
            return result;
        }
        float epsilon = 1E-12f;
        if (valuesEqual(rotMatrix[0][1],rotMatrix[1][0],epsilon)&&valuesEqual(rotMatrix[0][2],rotMatrix[2][0],epsilon)&&valuesEqual(rotMatrix[2][1],rotMatrix[1][2],epsilon)) {
            // Matrix is symmetric > rotation angle is n*180 degrees.
            // The following code is based on https://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToAngle/index.htm
		    float diag_x = (rotMatrix[0][0]+1)/2;
            float diag_y = (rotMatrix[1][1]+1)/2;
            float diag_z = (rotMatrix[2][2]+1)/2;
            float matr_01 = (rotMatrix[0][1]+rotMatrix[1][0])/4;
            float matr_02 = (rotMatrix[0][2]+rotMatrix[2][0])/4;
            float matr_12 = (rotMatrix[1][2]+rotMatrix[2][1])/4;
			if ((diag_x>diag_y)&&(diag_x>diag_z)) {
				if (diag_x<epsilon) {
					result[0] = 0;
					result[1] = (float) -Math.sqrt(0.5); // 0.7071;
					result[2] = (float) -Math.sqrt(0.5);
				} else {
					result[0] = (float) -Math.sqrt(diag_x);
					result[1] = -matr_01/result[0];
					result[2] = -matr_02/result[0];
				}
			} else if (diag_y>diag_z) {
				if (diag_y<epsilon) {
					result[0] = (float) -Math.sqrt(0.5);
					result[1] = 0;
					result[2] = (float) -Math.sqrt(0.5);
				} else {
					result[1] = (float) -Math.sqrt(diag_y);
					result[0] = -matr_01/result[1];
					result[2] = -matr_12/result[1];
				}
			} else {
				if (diag_z<epsilon) {
					result[0] = (float) -Math.sqrt(0.5);
					result[1] = (float) -Math.sqrt(0.5);
					result[2] = 0;
				} else {
					result[2] = (float) -Math.sqrt(diag_z);
					result[0] = -matr_02/result[2];
					result[1] = -matr_12/result[2];
				}
			}
            // for (int i=0;i<4;i++)
            //    result[i] = Math.abs(result[i]);
            return result;
        }
        result[0] = rotMatrix[1][2]-rotMatrix[2][1];
        result[1] = rotMatrix[2][0]-rotMatrix[0][2];
        result[2] = rotMatrix[0][1]-rotMatrix[1][0];
        float norm = vectorLength(result);
        for (int i=0;i<3;i++)
            result[i] /= norm;
        return result;
    }

    /**
     * Method to rotate a point by some angle around some axis in 3D space.
     * @param point The point to rotate.
     * @param axisPoint1 The first point defining the rotation axis.
     * @param axisPoint2 The second point defining the rotation axis.
     * @param angle The rotation angle (degrees).
     * @return A new point resulting from the rotation (the point passed as a parameter will remain unchanged)
     * or null if one of the parameters is not valid.
     */

    public static float[] rotateAroundAxis(float[] point, float[] axisPoint1, float axisPoint2[], float angle) {
        if (point==null||point.length!=3||axisPoint1==null||axisPoint1.length!=3||axisPoint2==null||axisPoint2.length!=3) return null;
        float[] rotMatrix = new float[16];
        Matrix.setIdentityM(rotMatrix,0);
        Matrix.rotateM(rotMatrix,0,angle,axisPoint2[0]-axisPoint1[0],axisPoint2[1]-axisPoint1[1],axisPoint2[2]-axisPoint1[2]);
        float[] temp = new float[4];
        temp[0] = point[0]-axisPoint1[0];
        temp[1] = point[1]-axisPoint1[1];
        temp[2] = point[2]-axisPoint1[2];
        temp[3] = 1;
        Matrix.multiplyMV(temp,0,rotMatrix,0,temp,0);
        float[] result = new float[3];
        for (int i=0;i<3;i++) result[i]=temp[i]+axisPoint1[i];
        return result;
    }

    /** Method to calculate a number of points in 2D space lying equidistantly on a circle around a center.
     * The first point will have the coordinates (0,radius), the following points will be calculated in counter-clockwise order.
     * @param centerX Center of the circle - X coordinate
     * @param centerY Center of the circle - Y coordinate
     * @param radius Radius of the circle
     * @param numberOfPoints Number of points to be placed on the circle
     * @return An array with the x-y coordinates of the points (x coordinate of point i in position [i][0], y coordinate in position [i][1])
     */

    public static float[][] pointsOnCircle2D(float centerX, float centerY, float radius, int numberOfPoints) {
        float result[][] = new float[numberOfPoints][2];
        for (int i=0;i<numberOfPoints;i++) {
            result[i][0] = -(float)(centerX+radius*Math.sin(2*Math.PI/numberOfPoints*i));
            result[i][1] = -(float)(centerY-radius*Math.cos(2*Math.PI/numberOfPoints*i));
            // Log.v("GLDEMO", result[i][0]+" "+result[i][1]);
        }
        return result;
    }

    /** Method to calculate a number of points in 3D space lying equidistantly on a circle.
     * @param center Center of the circle (array of length 3 - x, y, and z coordinate). Must be an array of length 3.
     * @param radius Radius of the circle. Must be greater than zero.
     * @param perpendicularVector Vector that is perpendicular to the plane in which the circle shall lie
     *                            and thus defines the orientation of the 2D circle in 3D space.
     *                            If null the circle will lie in the x-y plane, i.e. will not be rotated.
     *                            If not null it must be an array of length 3.
     * @param numberOfPoints Number of points to be placed on the circle. Must be greater than 1.
     * @return An array specifying the coordinates of the points (second dimension: 0 - x coordinate, 1 - y coordinate, 2 - z coordinate).
     * Null if one of the parameters is not valid.
     */

    public static float[][] pointsOnCircle3D(float[] center, float radius, float[] perpendicularVector, int numberOfPoints) {
        if (center==null||center.length!=3||radius<=0||numberOfPoints<2) return null;
        float[][] result = new float[numberOfPoints][3];
        // 1.) Construct a circle with the given radius around the origin (0,0,0) in the x-y plane
        for (int i=0;i<numberOfPoints;i++) {
            result[i][0] = (float)(radius* Math.sin(2* Math.PI/numberOfPoints*i));
            result[i][1] = (float)(radius* Math.cos(2* Math.PI/numberOfPoints*i));
            result[i][2] = 0;
        }
        // 2.) Rotate the circle to align it in 3D space
        if (perpendicularVector!=null) {
            if (perpendicularVector.length!=3) return null;
            if (!(GraphicsUtilsCV.valuesEqual(perpendicularVector[0],0)
                    && GraphicsUtilsCV.valuesEqual(perpendicularVector[1],0)
                    && GraphicsUtilsCV.valuesEqual(perpendicularVector[2],1))) {
                float[] z_axis = {0, 0, 1};
                float[] rotAxis = GraphicsUtilsCV.crossProduct(z_axis, perpendicularVector);
                float rotAngle = (float) Math.toDegrees(Math.acos(GraphicsUtilsCV.dotProduct(z_axis, GraphicsUtilsCV.getNormalizedCopy(perpendicularVector))));
                float[] rotMatrix = new float[16];
                Matrix.setIdentityM(rotMatrix, 0);
                Matrix.rotateM(rotMatrix, 0, rotAngle, rotAxis[0], rotAxis[1], rotAxis[2]);
                for (int i = 0; i < numberOfPoints; i++) {
                    float[] point = GraphicsUtilsCV.homogeneousCoordsForPoint(result[i]);
                    Matrix.multiplyMV(point, 0, rotMatrix, 0, point, 0);
                    result[i] = GraphicsUtilsCV.coordsFromHomogeneous(point);
                }
            }
        }
        // 3.) Translate the circle to its center
        for (int i=0;i<numberOfPoints;i++)
            for (int j=0; j<3; j++)
                result[i][j] += center[j];
        return result;
    }

    /** Method to calculate a number of points in 2D space lying equidistantly on an ellipse around a center.
     * The ellipse is implemented by a circle that is compressed vertically by a specified compression factor
     * and then rotated by a specified angle around its center.
     * @param centerX Center of the ellipse - X coordinate
     * @param centerY Center of the ellipse - Y coordinate
     * @param radius Radius of the ellipse
     * @param comprFactor; // Vertical compression of the circle (0<=comprFactor<=1), yielding the ellipse
     * @param rotAngle; // Rotation angle of the resulting ellipse (in radians)
     * @param numberOfPoints Number of points to be placed on the circle
     * @return An array with 'numberOfPoints' Point objects specifying the coordinates of the points
     */

    public static Point[] pointsOnEllipse(int centerX, int centerY, int radius, double comprFactor, double rotAngle, int numberOfPoints) {
        Point result[] = new Point[numberOfPoints];
        Point centerEll = new Point(centerX,centerY);
        Point startPoint = new Point(centerX+radius,centerY);
        for (int i=0;i<numberOfPoints;i++) {
            Point ptOnCircle = GraphicsUtilsCV.rotate(startPoint,centerEll,i*2* Math.PI/numberOfPoints);
            int compressedY = centerY + (int)(comprFactor*(centerY-ptOnCircle.y));
            Point ptOnEllipseUnrotated = new Point(ptOnCircle.x,compressedY);
            result[i] = GraphicsUtilsCV.rotate(ptOnEllipseUnrotated,centerEll,rotAngle);
        }
        return result;
    }

    /**
     * Method to rotate a point in 2D space around a center.
     *
     * @param toRotateX Point to be rotated: x coordinate
     * @param toRotateY Point to be rotated: y coordinate
     * @param centerX Center of the rotation: x coordinate
     * @param centerY Center of the rotation: y coordinate
     * @param angle Rotation angle (in radians)
     * @return The point after the rotation
     */

    public static Point rotate(int toRotateX, int toRotateY, int centerX, int centerY, double angle) {
        int normX = toRotateX - centerX;
        int normY = toRotateY - centerY;
        int rotX = (int) (normX * Math.cos(angle) - normY * Math.sin(angle)) + centerX;
        int rotY = (int) (normX * Math.sin(angle) + normY * Math.cos(angle)) + centerY;
        return new Point(rotX,rotY);
    }

    /** Method to rotate a point in 2D space around a center.
     *
     * @param toRotate Point to be rotated
     * @param center Center of the rotation
     * @param angle Rotation angle (in radians)
     * @return The point after the rotation
     */

    public static Point rotate(Point toRotate, Point center, double angle) {
        return rotate(toRotate.x,toRotate.y,center.x,center.y,angle);
    }

    /**
     * Writes the values of an array to the LocCat
     * @param tag
     * @param array
     */

    public static void writeArrayToLog(String tag, float[] array) {
        String line = "";
        for (int i=0; i<array.length; i++)
            line += array[i]+" ";
        Log.v(tag,line);
    }

    /**
     * Writes the values of a matrix to the LocCat
     * @param tag
     * @param matrix
     */

    public static void writeMatrixToLog(String tag, float[][] matrix) {
        for (int i=0; i<matrix.length; i++) {
            String line = "";
            for (int j=0; j<matrix[i].length; j++)
                line += matrix[i][j]+" ";
            Log.v(tag,line);
        }

    }

    /**
     * COPIED FROM THE PROJECT ON 2D PROPERTY ANIMATION
     *
     * Method to calculate the horizontal distance between two objects of class AnimatedGuiObjectCV.
     * Let oleft be the left object and oright the right object, i.e. the X coordinate of the center of oleft
     * is smaller than the X coordinate of the center of oright.
     * Then the value returned is the difference between the X coordinate of the leftmost corner of the
     * enclosing rectangle of oright (= its left bound in case of an unrotated object) and the
     * X coordinate of the rightmost corner of the enclosing rectangle of oleft (= its right bound
     * in case of an unrotated object).
     *
     * @param obj1 The first object
     * @param obj2 The first object
     * @return Horizontal distance between the objects, as defined above
     */

    // public static int distCentersHoriz(AnimatedGuiObjectCV obj1, AnimatedGuiObjectCV obj2) {

    /**
     * COPIED FROM THE PROJECT ON 2D PROPERTY ANIMATION
     *
     * Method to calculate the vertical distance between two objects of class AnimatedGuiObjectCV.
     * Let otop be the upper object and obottom the lower object, i.e. the Y coordinate of the center of otop
     * is smaller than the Y coordinate of the center of obottom.
     * Then the value returned is the difference between the Y coordinate of the upmost corner of the
     * enclosing rectangle of obottom (= its top bound in case of an unrotated object) and the
     * Y coordinate of the lowermost corner of the enclosing rectangle of otop (= its lower bound
     * in case of an unrotated object).
     *
     * @param obj1 The first object
     * @param obj2 The first object
     * @return Vertical distance between the objects, as defined above
     */

    // public static int distCentersVert(AnimatedGuiObjectCV obj1, AnimatedGuiObjectCV obj2) {

    /**
     * COPIED FROM THE PROJECT ON 2D PROPERTY ANIMATION
     *
     * Method to find the object with the leftmost bound in a collection of objects.
     * @param objects The collection of objects
     * @return The object with the leftmost bound
     */

    // public static AnimatedGuiObjectCV objectWithLeftmostBound(Collection<AnimatedGuiObjectCV> objects) {

    /**
     * COPIED FROM THE PROJECT ON 2D PROPERTY ANIMATION
     *
     * Method to find the object with the leftmost center in a collection of objects.
     * @param objects The collection of objects
     * @return The object with the leftmost center
     */

    // public static AnimatedGuiObjectCV objectWithLeftmostCenter(Collection<AnimatedGuiObjectCV> objects) {

    /**
     * COPIED FROM THE PROJECT ON 2D PROPERTY ANIMATION
     *
     * Method to find the object with the rightmost bound in a collection of objects.
     * @param objects The collection of objects
     * @return The object with the rightmost bound
     */

    // public static AnimatedGuiObjectCV objectWithRightmostBound(Collection<AnimatedGuiObjectCV> objects) {

    /**
     * COPIED FROM THE PROJECT ON 2D PROPERTY ANIMATION
     *
     * Method to find the object with the rightmost center in a collection of objects.
     * @param objects The collection of objects
     * @return The object with the rightmost center
     */

    // public static AnimatedGuiObjectCV objectWithRightmostCenter(Collection<AnimatedGuiObjectCV> objects) {

    /**
     * COPIED FROM THE PROJECT ON 2D PROPERTY ANIMATION
     *
     * Method to find the object with the topmost bound in a collection of objects.
     * @param objects The collection of objects
     * @return The object with the topmost bound
     */

    // public static AnimatedGuiObjectCV objectWithTopmostBound(Collection<AnimatedGuiObjectCV> objects) {

    /**
     * COPIED FROM THE PROJECT ON 2D PROPERTY ANIMATION
     *
     * Method to find the object with the topmost center in a collection of objects.
     * @param objects The collection of objects
     * @return The object with the topmost center
     */

    // public static AnimatedGuiObjectCV objectWithTopmostCenter(Collection<AnimatedGuiObjectCV> objects) {

    /**
     * COPIED FROM THE PROJECT ON 2D PROPERTY ANIMATION
     *
     * Method to find the object with the bottommost bound in a collection of objects.
     * @param objects The collection of objects
     * @return The object with the bottommost bound
     */

    // public static AnimatedGuiObjectCV objectWithBottommostBound(Collection<AnimatedGuiObjectCV> objects) {

    /**
     * COPIED FROM THE PROJECT ON 2D PROPERTY ANIMATION
     *
     * Method to find the object with the bottommost center in a collection of objects.
     * @param objects The collection of objects
     * @return The object with the bottommost center
     */

    // public static AnimatedGuiObjectCV objectWithBottommostCenter(Collection<AnimatedGuiObjectCV> objects) {

    /**
     * COPIED FROM THE PROJECT ON 2D PROPERTY ANIMATION
     *
     * Method to sort a collection of objects according to their horizontal positions (= x coordinates of their centers).
     * @param objects The collection of objects
     * @return An ArrayList with the objects sorted in ascending order according to their horizontal positions
     */

    // public static ArrayList<AnimatedGuiObjectCV> sortObjectsHorizCenters(Collection<AnimatedGuiObjectCV> objects) {

    /**
     * Method to sort a collection of objects according to their vertical positions (= y coordinates of their centers).
     * @param objects The collection of objects
     * @return An ArrayList with the objects sorted in ascending order according to their vertical positions
     */

    // public static ArrayList<AnimatedGuiObjectCV> sortObjectsVertCenters(Collection<AnimatedGuiObjectCV> objects) {

}