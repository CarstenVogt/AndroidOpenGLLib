// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 17.3.2022

package de.thkoeln.cvogt.android.opengl_utilities;

import android.graphics.Point;
import android.opengl.Matrix;
import android.util.Log;

import java.util.Arrays;

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

    /** Method to calculate the length of a vector in 3D space.
     *
     * @param vector The vector.
     * @return The length of the vector or -1 if the parameter is not valid
     * (i.e. is null or has a length other than 3).
     */

    public static float vectorLength(float[] vector) {
        if (vector==null||vector.length!=3) return -1;
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

    /** Method to calculate the midpoint between two points in 3D space.
     *
     * @param p1 The first point: Array of length 3 with the (x,y,z) coordinates (in this order).
     * @param p2 The second point: Array of length 3 with the (x,y,z) coordinates (in this order).
     * @return The midpoint between the two points or null if one of the parameters is not valid
     * (i.e. is null or has a length other than 3).
     */

    public static float[] midpoint(float[] p1, float[] p2) {
        if (p1==null||p1.length!=3||p2==null||p2.length!=3) return null;
        float[] result = new float[3];
        for (int i=0;i<3;i++)
          result[i] = p1[i]+(p2[i]-p1[i])/2;
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
        final double epsilon = 1E-6;
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

    public static boolean isRotationMatrix(float[][] matrix) {
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
     * copying its lines one after the other
     * (needed e.g. to transform a scaling, rotation or transformation matrix
     * into the one-dimensional format required by OpenGL).
     * @param matrix The matrix to be transformed.
     * @return The corresponding array; null if the parameter is not valid.
     */

    public static float[] arrayFromMatrix(float[][] matrix) {
        if (matrix==null) return null;
        int length = 0;
        for (int i=0; i<matrix.length; i++)
            if (matrix[i]!=null) length+=matrix[i].length;
            else return null;
        float[] array = new float[length];
        int lineStart = 0;
        for (int i=0; i<matrix.length; i++) {
            for (int j = 0; j<matrix[i].length; j++)
                array[lineStart+j] = matrix[i][j];
            lineStart += matrix[i].length;
        }
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
        float[][] matrix = new float[lines][columns];
        for (int i=0; i<lines; i++)
            for (int j=0; j<columns; j++)
                matrix[i][j] = array[i*columns+j];
        return matrix;
    }

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
     * Returned are the corresponding rotation axis and angle.
     * together with corresponding rotations around the y axis and z axis
     * (see methods eulerAngleY() and eulerAngleZ()) to get the same rotation.
     * @param rotMatrix The rotation matrix.
     * @return A float angle of length 4 with the rotation axis in positions 0-2 and the rotation angle (degrees) in position 3.
     * or null if rotMatrix is not a valid rotation matrix.
     */

    public static float[] rotAxisAndAngle(float[][] rotMatrix) {
        if (rotMatrix==null||rotMatrix.length!=4) return null;
        for (int i=0; i<4; i++)
            if (rotMatrix[i]==null||rotMatrix[i].length!=4) return null;
        // calculation according to https://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToAngle/index.htm
        // and https://en.wikipedia.org/wiki/Rotation_matrix#Conversion_from_rotation_matrix_to_axis%E2%80%93angle
        if (!isRotationMatrix(rotMatrix)) return null;
        float[] result = new float[4];
        if (isIdentity(rotMatrix)) {
            // identity matrix > no rotation > return angle 0.0 and some arbitrary axis
            result[0] = 1;
            result[1] = result[2] = result[3] = 0;
            return result;
        }
        if (valuesEqual(rotMatrix[0][1],rotMatrix[1][0])&&valuesEqual(rotMatrix[0][2],rotMatrix[2][0])&&valuesEqual(rotMatrix[2][1],rotMatrix[1][2])) {
            // matrix is symmetric > rotation angle is 180 degrees
            // the following code is based on https://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToAngle/index.htm
            // TODO Achse berechnen
/*
        // https://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToAngle/index.htm
		double xx = (m[0][0]+1)/2;
		double yy = (m[1][1]+1)/2;
		double zz = (m[2][2]+1)/2;
		double xy = (m[0][1]+m[1][0])/4;
		double xz = (m[0][2]+m[2][0])/4;
		double yz = (m[1][2]+m[2][1])/4;
		if ((xx > yy) && (xx > zz)) { // m[0][0] is the largest diagonal term
			if (xx< epsilon) {
				x = 0;
				y = 0.7071;
				z = 0.7071;
			} else {
				x = Math.sqrt(xx);
				y = xy/x;
				z = xz/x;
			}
		} else if (yy > zz) { // m[1][1] is the largest diagonal term
			if (yy< epsilon) {
				x = 0.7071;
				y = 0;
				z = 0.7071;
			} else {
				y = Math.sqrt(yy);
				x = xy/y;
				z = yz/y;
			}
		} else { // m[2][2] is the largest diagonal term so base result on this
			if (zz< epsilon) {
				x = 0.7071;
				y = 0.7071;
				z = 0;
			} else {
				z = Math.sqrt(zz);
				x = xz/z;
				y = yz/z;
			}
		}
 */
            result[3] = 180.0f;
            return result;
        }
        // rotation axis
        result[0] = rotMatrix[2][1]-rotMatrix[1][2];
        result[1] = rotMatrix[0][2]-rotMatrix[2][0];
        result[2] = rotMatrix[1][0]-rotMatrix[0][1];
        float norm = (float) Math.sqrt(result[0]*result[0]+result[1]*result[1]+result[2]*result[2]);
        for (int i=0;i<3;i++)
            result[i] /= norm;
        // rotation angle
        result[3] = (float) Math.acos((rotMatrix[0][0]+rotMatrix[1][1]+rotMatrix[2][2]-1)/2);
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

    /** Method to calculate a number of points in 2D space lying equidistantly on a circle around a center
     * @param centerX Center of the circle - X coordinate
     * @param centerY Center of the circle - Y coordinate
     * @param radius Radius of the circle
     * @param numberOfPoints Number of points to be placed on the circle
     * @return An array with 'numberOfPoints' Point objects specifying the coordinates of the points
     */

    public static Point[] pointsOnCircle(int centerX, int centerY, int radius, int numberOfPoints) {
        Point result[] = new Point[numberOfPoints];
        for (int i=0;i<numberOfPoints;i++) {
            int x = (int)(centerX+radius* Math.sin(2* Math.PI/numberOfPoints*i));
            int y = (int)(centerY-radius* Math.cos(2* Math.PI/numberOfPoints*i));
            result[i] = new Point(x,y);
        }
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