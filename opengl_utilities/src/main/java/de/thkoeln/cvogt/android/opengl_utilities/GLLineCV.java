// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 2.6.2022

package de.thkoeln.cvogt.android.opengl_utilities;

import android.graphics.Bitmap;
import android.opengl.Matrix;

/**
 * Class to specify lines, i.e. fundamental building units of OpenGL shapes.
 * Shapes, i.e. objects of class <I>GLShapeCV</I> are defined by a set of triangles and/or lines, i.e. of objects of class <I>GLTriangleCV</I> and/or of this class <I>GLLineCV</I>.
 * <BR>
 * A line is specified by its two end vertices, its color, and its width.
 * @see GLShapeCV
 * @see GLTriangleCV
 */

public class GLLineCV {

    /** The name / ID of the line. */

    private String id;

    /** The first end point of the line: 0 = x coordinate, 1 = y coordinate, 2 = z coordinate. */

    private float point1[];

    /** The second end point of the line: 0 = x coordinate, 1 = y coordinate, 2 = z coordinate */

    private float point2[];

    /** The color of the line (four values: RGBA) */

    private float color[];

    /**
     * Both vertices are set to (0.0f,0.0f,0.0f), the color is set to black.
     * @param id The id of the line.
     */

    public GLLineCV(String id) {
        this(id,new float[3],new float[3]);
    }

    /**
     * The color is set to black.
     * @param id The id of the line.
     * @param point1 The first end point of the line (x, y, z coordinates).
     * @param point2 The second end point of the line (x, y, z coordinates).
     */

    public GLLineCV(String id, float[] point1, float[] point2) {
        this(id,point1,point2,GLShapeFactoryCV.black);
    }

    /**
     * @param id The id of the line.
     * @param point1 The first end point of the line (x, y, z coordinates).
     * @param point2 The second end point of the line (x, y, z coordinates).
     * @param color The color of the line as an array of length 4 with the RGBA color values.
     */

    public GLLineCV(String id, float[] point1, float[] point2, float[] color) {
        this.id = new String(id);
        this.point1 = point1.clone();
        this.point2 = point2.clone();
        this.color = color.clone();
    }

    /** Constructs a new line as a copy of this line with copies of all attributes.
     * @return The new line.
     */

    protected GLLineCV clone() {
        return new GLLineCV(id,point1.clone(),point2.clone(),color.clone());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the end points of the line as copies of the parameter arrays.
     * @param point1 The value of the first end point to be set (x, y, z coordinate).
     * @param point2 The value of the second end point to be set (x, y, z coordinate).
     * @return false if a parameter array is null or has not the correct size 3.
     */

    public boolean setPoints(float[] point1, float[] point2) {
        if (point1==null||point2==null||point1.length!=3||point2.length!=3) return false;
        this.point1 = point1.clone();
        this.point2 = point2.clone();
        return true;
    }

    /**
     * Get the first end point of the line.
     * @return The first end point of the line as a copy of the 'point1' attribute.
     */

    public float[] getPoint1() {
        return point1.clone();
    }

    /**
     * Get the second end point of the line.
     * @return The second end point of the line as a copy of the 'point2' attribute.
     */

    public float[] getPoint2() {
        return point2.clone();
    }

    /**
     * Sets the color of the line as a copy of the parameter array.
     * @param color The color to be set (RGBA).
     * @return false if a parameter array is null or has not the correct size 4.
     */

    public boolean setPoints(float[] color) {
        if (color==null||color.length!=4) return false;
        this.color = color.clone();
        return true;
    }

    /**
     * Get the color of the line.
     * @return The color of the line as a copy of the 'color' attribute.
     */

    public float[] getColor() {
        return color.clone();
    }

    /**
     * Scale the line.
     * <BR>
     * For a more detailed comment see method 'transform()'.
     * @param scaleX The scale factor for the x dimension.
     * @param scaleY The scale factor for the y dimension.
     * @param scaleZ The scale factor for the z dimension.
     */

    public void scale(float scaleX, float scaleY, float scaleZ) {
        transform(scaleX,scaleY,scaleZ,0,0,0,0,0,0);
    }

    /**
     * Rotate the line.
     * <BR>
     * For a more detailed comment see method 'transform()'.
     * @param rotAngleX The rotation angle to be applied - x axis.
     * @param rotAngleY The rotation angle to be applied - y axis.
     * @param rotAngleZ The rotation angle to be applied - z axis.
     */

    public void rotate(float rotAngleX, float rotAngleY, float rotAngleZ) {
        transform(1,1,1,rotAngleX,rotAngleY,rotAngleZ,0,0,0);
    }

    /**
     * Translate the line.
     * <BR>
     * For a more detailed comment see method 'transform()'.
     * @param transX The translation in the x direction.
     * @param transY The translation in the y direction.
     * @param transZ The translation in the z direction.
     */

    public void translate(float transX, float transY, float transZ) {
        transform(1,1,1,0,0,0,transX,transY,transZ);
        // OLD VERSION OF TRANSFORM: transform(1,1,1,0,0,0,1,transX,transY,transZ)
    }

    /**
     * Scale, rotate, and/or translate the line.
     * <BR>
     * N.B.: This method will affect the coordinates of the line in the model (!) coordinate system,
     * i.e. in the coordinate system of the shape to which the line belongs or is to be added.
     * By this, a line can be placed within this shape.
     * Similar methods of shapes (see class GLShapeCV: setTransX(), setRotAroundX(), ...)
     * do not modify the vertex coordinates of the shape's lines and triangles
     * but define a model matrix which places the whole shape into the "real world".
     * @param scaleX The scale factor for the x dimension.
     * @param scaleY The scale factor for the y dimension.
     * @param scaleZ The scale factor for the z dimension.
     * @param rotAngleX The rotation angle around the x axis.
     * @param rotAngleY The rotation angle around the y axis.
     * @param rotAngleZ The rotation angle around the z axis.
     * @param transX The translation in the x direction.
     * @param transY The translation in the y direction.
     * @param transZ The translation in the z direction.
     */

    public void transform(float scaleX, float scaleY, float scaleZ, float rotAngleX ,float rotAngleY ,float rotAngleZ, float transX, float transY, float transZ) {
        float[] transformationMatrix = new float[16];
        Matrix.setIdentityM(transformationMatrix,0);
        // scaling matrix
        float[] matrixScale = new float[16];
        Matrix.setIdentityM(matrixScale,0);
        Matrix.scaleM(matrixScale,0,scaleX, scaleY, scaleZ);
        // rotation matrix
        float[] matrixRotateX = new float[16];
        Matrix.setIdentityM(matrixRotateX,0);
        Matrix.rotateM(matrixRotateX,0,rotAngleX,1,0,0);
        float[] matrixRotateY = new float[16];
        Matrix.setIdentityM(matrixRotateY,0);
        Matrix.rotateM(matrixRotateY,0,rotAngleY,0,1,0);
        float[] matrixRotateZ = new float[16];
        Matrix.setIdentityM(matrixRotateZ,0);
        Matrix.rotateM(matrixRotateZ,0,rotAngleZ,0,0,1);
        // translation matrix
        float[] matrixTranslate = new float[16];
        Matrix.setIdentityM(matrixTranslate,0);
        Matrix.translateM(matrixTranslate,0,transX,transY,transZ);
        // calculate the model matrix by multiplying the scaling, rotation, and translation matrices (in this order!)
        Matrix.multiplyMM(transformationMatrix, 0, matrixScale, 0, transformationMatrix, 0);
        if (rotAngleY!=0)
        Matrix.multiplyMM(transformationMatrix, 0, matrixRotateY, 0, transformationMatrix, 0);
        if (rotAngleZ!=0)
        Matrix.multiplyMM(transformationMatrix, 0, matrixRotateZ, 0, transformationMatrix, 0);
        if (rotAngleX!=0)
        Matrix.multiplyMM(transformationMatrix, 0, matrixRotateX, 0, transformationMatrix, 0);
        Matrix.multiplyMM(transformationMatrix, 0, matrixTranslate, 0, transformationMatrix, 0);
        float[] vertex = new float[4];
        for (int i=0; i<3; i++)
            vertex[i] = this.point1[i];
        vertex[3] = 1;
        Matrix.multiplyMV(vertex, 0, transformationMatrix, 0, vertex, 0);
        for (int i=0; i<3; i++)
            this.point1[i] = vertex[i];
        for (int i=0; i<3; i++)
            vertex[i] = this.point2[i];
        vertex[3] = 1;
        Matrix.multiplyMV(vertex, 0, transformationMatrix, 0, vertex, 0);
        for (int i=0; i<3; i++)
            this.point2[i] = vertex[i];
    }

}

