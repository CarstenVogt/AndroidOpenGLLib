// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 17.3.2022

package de.thkoeln.cvogt.android.opengl_utilities;

import android.graphics.Bitmap;
import android.opengl.Matrix;

/**
 * Class to specify triangles, the fundamental building units of OpenGL shapes.
 * Shapes, i.e. objects of class GLShapeCV are defined by a set of triangles, i.e. of objects of this class GLTriangleCV.
 * <BR>
 * The form of a triangle is specified by its three vertices as defined by the 'vertices' attribute.
 * <BR>
 * There are three ways to specify the color values of the individual pixels of a triangle:
 * <UL>
 * <LI>by a uniform color for the whole triangle,
 * <LI>by a color gradient where pixel colors are interpolated from the colors of the three vertices [NOT YET SUPPORTED] or
 * <LI>by a texture as defined by a bitmap image.
 * </UL>
 * The following attributes support these options and override each others in the given order:
 * <UL>
 * <LI>If the attribute 'uniformColor' is not null, it specifies the uniform color of the triangle.
 * <LI>If the attribute 'uniformColor' is null and the attribute 'vertexColors' is not, a color gradient is used as defined by the vertex colors [NOT YET SUPPORTED].
 * <LI>If the attributes 'uniformColor' and 'vertexColors' are both null, a bitmap will be applied as a texture,
 * as defined by the attributes 'textureBitmap' and 'uvCoordinates'.
 * </UL>
 * @see de.thkoeln.cvogt.android.opengl_utilities.GLShapeCV
 */

public class GLTriangleCV {

    /** The name / ID of the triangle. */

    private String id;

    /** The three vertices of the triangle:
        first index: vertex number (0,1,2), second index: 0 = x coordinate, 1 = y coordinate, 2 = z coordinate. */

    private float vertices[][];

    /** The uniform color of the triangle (four values: RGBA).
     *  If this attribute is null then the color is defined by a color gradient calculated from the three vertex colors (attribute 'vertexColors')
     *  or by a texture from a Bitmap object (attributes 'textureBitmap' and 'uvCoordinates'). */

    private float uniformColor[];

    /** The colors of the three vertices (vertices in the order as specified by 'vertices', color as RGBA).
        Only valid if 'uniformColor' is null.
        If this attribute and 'uniformColor' are null the triangle is textured as specified by 'textureBitmap' and 'uvCoordinates'. */

    private float vertexColors[][] = null;

    /** The bitmap to be used as texture. Only valid if 'uniformColor' and 'vertexColors' are null. */

    private Bitmap textureBitmap = null;     //

    /** The UV coordinates to map the texture bitmap onto the triangle. Only valid if 'uniformColor' and 'vertexColors' are null. */

    private float uvCoordinates[] = null;

    /**
     * All three vertices are set to (0.0f,0.0f,0.0f), the uniform color is set to white.
     * @param id The id of the triangle.
     */

    public GLTriangleCV(String id) {
        this.id = id;
        vertices = new float[3][3];
        uniformColor = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
    }

    /**
     * The uniform color is set to white.
     * @param id The id of the triangle.
     * @param vertices The vertices of the triangle (to be given in counter-clockwise order).
     */

    public GLTriangleCV(String id, float[][] vertices) {
        this(id,vertices,GLShapeFactoryCV.white);
    }

    /**
     * @param id The id of the triangle.
     * @param vertices The vertices of the triangle (to be given in counter-clockwise order).
     * @param color The uniform color of the triangle as an array of length 4 with the RGBA color values.
     */

    public GLTriangleCV(String id, float[][] vertices, float[] color) {
        this(id);
        setVertices(vertices);
        setUniformColor(color);
    }

    /**
     * The uniform color is set to white.
     * @param id The id of the triangle.
     * @param vertex0 The first vertex of the triangle (vertices to be given in counter-clockwise order).
     * @param vertex1 The second vertex of the triangle (vertices to be given in counter-clockwise order).
     * @param vertex2 The third vertex of the triangle (vertices to be given in counter-clockwise order).
     */

    public GLTriangleCV(String id, float[] vertex0, float[] vertex1, float[] vertex2) {
        this(id,vertex0,vertex1,vertex2,GLShapeFactoryCV.white);
    }

    /**
     * @param id The id of the triangle.
     * @param vertex0 The first vertex of the triangle (vertices to be given in counter-clockwise order).
     * @param vertex1 The second vertex of the triangle (vertices to be given in counter-clockwise order).
     * @param vertex2 The third vertex of the triangle (vertices to be given in counter-clockwise order).
     * @param color The uniform color of the triangle as an array of length 4 with the RGBA color values.
     */

    public GLTriangleCV(String id, float[] vertex0, float[] vertex1, float[] vertex2, float[] color) {
        this(id);
        setVertices(vertex0,vertex1,vertex2);
        setUniformColor(color);
    }

    /**
     * @param id The id of the triangle.
     * @param vertices The vertices of the triangle (to be given in counter-clockwise order).
     * @param color The vertex colors of the triangle as an array of size 3*4 with the RGBA color values of the three vertices.
     */

    public GLTriangleCV(String id, float[][] vertices, float[][] color) {
        this(id);
        setVertices(vertices);
        setVertexColors(color);
    }

    /**
     * @param id The id of the triangle.
     * @param vertices The vertices of the triangle (to be given in counter-clockwise order).
     * @param textureBitmap The bitmap to be used as texture.
     * @param uvCoordinates The uv coordinates to map the bitmap onto the triangle.
     */

    public GLTriangleCV(String id, float[][] vertices, Bitmap textureBitmap, float[] uvCoordinates) {
        this(id);
        setVertices(vertices);
        setTexture(textureBitmap,uvCoordinates);
    }

    /** Constructs a new triangle as a copy of this triangle with in-depth copies of all attributes.
     * @return The new triangle.
     */

    protected GLTriangleCV clone() {
        GLTriangleCV newTriangle = new GLTriangleCV(id);
        newTriangle.setVertices(this.vertices);
        newTriangle.setUniformColor(this.uniformColor);
        newTriangle.setVertexColors(this.vertexColors);
        newTriangle.setTexture(this.textureBitmap,this.uvCoordinates);
        return newTriangle;
    }

    public String getId() {
        return id;
    }

    /*
    public int getIdAsInt() {
        int idAsInt = -1;
        try {
           idAsInt = Integer.parseInt(id);
        } catch (NumberFormatException exc) {}
        return idAsInt;
    } */

    public void setId(String id) {
        this.id = id;
    }

    /* public void setIdFromInt(int idAsInt) {
        this.id = idAsInt+"";
    } */

    /**
     * Sets the vertices of the triangle as an in-depth copy of the parameter array.
     * @param vertices The vertices to be set.
     * @return false if the parameter array has not the correct size (3 in both dimensions).
     */

    public boolean setVertices(float[][] vertices) {
        if (vertices.length!=3) return false;
        for (int i=0; i<3; i++) {
            if (vertices[i].length!=3) return false;
            for (int j=0; j<3; j++)
                this.vertices[i][j] = vertices[i][j];
        }
        return true;
    }

    /**
     * Sets the vertices of the triangle as copies of the parameter array.
     * @param vertex0 The first vertex to be set.
     * @param vertex1 The second vertex to be set.
     * @param vertex2 The third vertex to be set.
     * @return false if the parameter array has not the correct size (3 in both dimensions).
     */

    public boolean setVertices(float[] vertex0, float[] vertex1, float[] vertex2) {
        if (vertex0==null||vertex0.length!=3) return false;
        vertices[0]=vertex0;
        if (vertex1==null||vertex1.length!=3) return false;
        vertices[1]=vertex1;
        if (vertex2==null||vertex2.length!=3) return false;
        vertices[2]=vertex2;
        return true;
    }

    /**
     * Sets the vertices of the triangle from a sequence of float values
     * in the order vertex 0: x,y,z; vertex 1: x,y,z; vertex 2: x,y,z.
     * @param vertexCoordinates The vertices to be set - must be exactly nine float values.
     * @return false if the parameter array has not the correct size (3 in both dimensions).
     */

    public boolean setVertices(float... vertexCoordinates) {
        if (vertexCoordinates.length!=9) return false;
        vertices[0][0] = (float) vertexCoordinates[0];
        vertices[0][1] = (float) vertexCoordinates[1];
        vertices[0][2] = (float) vertexCoordinates[2];
        vertices[1][0] = (float) vertexCoordinates[3];
        vertices[1][1] = (float) vertexCoordinates[4];
        vertices[1][2] = (float) vertexCoordinates[5];
        vertices[2][0] = (float) vertexCoordinates[6];
        vertices[2][1] = (float) vertexCoordinates[7];
        vertices[2][2] = (float) vertexCoordinates[8];
        return true;
    }

    /**
     * Get the vertices of the triangle.
     * @return The vertices of the triangle as an in-depth copy of the 'vertices' attribute.
     */

    public float[][] getVertices() {
        float verticesToReturn[][] = new float[3][];
        for (int i=0; i<3; i++)
          verticesToReturn[i] = vertices[i].clone();
        return verticesToReturn;
    }

    /**
     * Get the vertex coordinates of the triangle.
     * @return A one-dimensional float array with the vertex coordinates
     * in the order vertex 0: x,y,z; vertex 1: x,y,z; vertex 2: x,y,z.
     */

    public float[] getVertexCoordinates() {
        float coordinatesToReturn[] = new float[9];
        for (int vNumber=0; vNumber<3;vNumber++)
            for (int vCoord=0; vCoord<3; vCoord++)
                coordinatesToReturn[vNumber*3+vCoord] = vertices[vNumber][vCoord];
        return coordinatesToReturn;
    }

    /**
     * Get the coloring type of the triangle.
     * @return The coloring type of the triangle:
     * <UL>
     * <LI>GLPlatformCV.COLORING_UNIFORM if the triangle is uniformly colored, i.e. the 'uniformColor' attribute is not null.
     * <LI>GLPlatformCV.COLORING_GRADIENT if the triangle is colored with a color gradient, i.e. the 'uniformColor' attribute is null and the 'vertexColors' attribute is not null.
     * <LI>GLPlatformCV.COLORING_TEXTURE if the triangle has a texture, i.e. the 'uniformColor' and 'vertexColors' attributes are null and the 'textureBitmap' and 'uvCoordinates' attributes are not null.
     * <LI>GLPlatformCV.COLORING_UNDEF otherwise.
     * </UL>
     */

    public int getColoringType()  {     // coloring types as defined in GLPlatformCV
        if (uniformColor!=null) return GLPlatformCV.COLORING_UNIFORM;
        if (vertexColors!=null) return GLPlatformCV.COLORING_GRADIENT;
        if (textureBitmap!=null) return GLPlatformCV.COLORING_TEXTURED;
        return GLPlatformCV.COLORING_UNDEF;
    }

    /**
     * By calling this method the triangle will become uniformly colored (provided that the parameter has the correct format).
     * @param uniformColor A float array of length 4 with the RGBA color values.
     * @return true if the parameter has the correct format, false otherwise.
     */

    public boolean setUniformColor(float[] uniformColor) {
        if (uniformColor==null||uniformColor.length!=4) return false;
        this.uniformColor = new float[4];
        for (int i=0; i<4; i++)
            this.uniformColor[i] = uniformColor[i];
        return true;
    }

    /**
     * Get the uniform color of the triangle.
     * @return If the triangle is uniformly colored, the color of the triangle as an in-depth copy of the 'uniformColor' attribute. Null otherwise.
     */

    public float[] getUniformColor() {
        if (getColoringType()!= GLPlatformCV.COLORING_UNIFORM) return null;
        return uniformColor.clone();
    }

    /**
     * By calling this method the triangle will become colored with a colored gradient (provided that the parameter has the correct format).
     * In particular, the 'uniformColor' attribute is set to null.
     * @param vertexColors A two-dimensional 3*4 float array with the RGBA color values of the three vertices.
     * @return true if the parameter has the correct format, false otherwise.
     */

    public boolean setVertexColors(float[][] vertexColors) {
        if (vertexColors==null||vertexColors.length!=3) return false;
        for (int i=0; i<3; i++)
            if (vertexColors[i].length!=4) return false;
        uniformColor = null;
        this.vertexColors = new float[3][4];
        for (int i=0; i<3; i++)
            for (int j=0; j<4; j++)
                this.vertexColors[i][j] = vertexColors[i][j];
        return true;
    }

    /**
     * Get the color of a vertex.
     * @param vertexNo The number of the vertex.
     * @return null if vertexNo is not 0, 1, or 2 or/and the triangle has neither a uniform or a gradient color.
     * The color of the vertex if the triangle has a gradient color; the uniform color of the triangle if the triangle is uniformly colored (both as copies of the attribute values).
     */

    public float[] getVertexColor(int vertexNo) {
        if (vertexNo<0||vertexNo>2) return null;
        if (getColoringType()!= GLPlatformCV.COLORING_UNIFORM&&getColoringType()!= GLPlatformCV.COLORING_GRADIENT) return null;
        if (getColoringType()== GLPlatformCV.COLORING_UNIFORM) {
            return uniformColor.clone();
        }
        return vertexColors[vertexNo].clone();
    }

    /**
     * By calling this method the triangle will become textured with a bitmap (provided that the parameters have the correct format).
     * In particular, the 'uniformColor' and 'vertexColor' attributes are set to null.
     * @param textureBitmap The bitmap to be used as texture.
     * @param uvCoordinates A float array of length 6 with the uvCoordinates to be used for the mapping (in the ordering of the corresponding vertices).
     * @return true if both parameters are not 0 and the array parameter has the correct length, false otherwise.
     */

    public boolean setTexture(Bitmap textureBitmap, float uvCoordinates[]) {
        if (textureBitmap==null||uvCoordinates==null||uvCoordinates.length!=6) return false;
        uniformColor = null;
        vertexColors = null;
        this.textureBitmap = textureBitmap;
        this.uvCoordinates = new float[6];
        for (int i=0; i<6; i++)
            this.uvCoordinates[i] = uvCoordinates[i];
        return true;
    }

    /**
     * Get the uvCoordinates of the triangle if defined.
     * @return A copy of the 'uvCoordinates' array attribute if the triangle is textured, null otherwise.
     */

    public float[] getUvCoordinates() {
        if (getColoringType()!= GLPlatformCV.COLORING_TEXTURED) return null;
        return uvCoordinates.clone();
    }

    /**
     * Get the texture bitmap of the triangle if defined.
     * @return The texture bitmap if the triangle is textured , null otherwise.
     */

    public Bitmap getTexture() {
        if (getColoringType()!= GLPlatformCV.COLORING_TEXTURED) return null;
        return textureBitmap;
    }

    /**
     * Scale the triangle.
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
     * Rotate the triangle.
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
     * Translate the triangle.
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
     * Scale, rotate, and/or translate the triangle.
     * <BR>
     * N.B.: This method will affect the coordinates of the triangle in the model (!) coordinate system,
     * i.e. in the coordinate system of the shape to which the triangle belongs or is to be added.
     * By this, a triangle can be placed within this shape.
     * Similar methods of shapes (see class GLShapeCV: setTransX(), setRotAroundX(), ...)
     * do not modify the vertex coordinates of the shape's triangles
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
        for (int i=0; i<3; i++) {
            float[] vertex = new float[4];
            for (int j=0; j<3; j++)
                vertex[j] = this.vertices[i][j];
            vertex[3] = 1;
            Matrix.multiplyMV(vertex, 0, transformationMatrix, 0, vertex, 0);
            for (int j=0; j<3; j++)
                this.vertices[i][j] = vertex[j];
        }
    }

    /**
     * Method to normalize the vertices of all triangles,
     * i.e. to lengthen or shorten all vectors leading from the origin to the vertex to length 1.
     * Note that this is a very special method that is to be applied when constructing a sphere
     * (see method makeSphere() in class GLShapeFactory).
     */

    public void normalizeVertexVectors() {
      for (int i=0; i<3; i++)
          GraphicsUtilsCV.normalize(vertices[i]);
    }

    /* old version

    public void transform(float scaleX, float scaleY, float scaleZ, float rotAngle ,float rotX,float rotY,float rotZ, float transX, float transY, float transZ) {
        float[] transformationMatrix = new float[16];
        Matrix.setIdentityM(transformationMatrix,0);
        // scaling matrix
        float[] matrixScale = new float[16];
        Matrix.setIdentityM(matrixScale,0);
        Matrix.scaleM(matrixScale,0,scaleX, scaleY, scaleZ);
        // rotation matrix
        float[] matrixRotate = new float[16];
        Matrix.setIdentityM(matrixRotate,0);
        Matrix.rotateM(matrixRotate,0,rotAngle,rotX,rotY,rotZ);  // Rotation um die Achse (x, y, z) mit Winkel a Grad (Gradmaß).
        // translation matrix
        float[] matrixTranslate = new float[16];
        Matrix.setIdentityM(matrixTranslate,0);
        Matrix.translateM(matrixTranslate,0,transX,transY,transZ);
        // calculate the model matrix by multiplying the scaling, rotation, and translation matrices (in this order!)
        Matrix.multiplyMM(transformationMatrix, 0, matrixScale, 0, transformationMatrix, 0);
        if (rotX!=0||rotY!=0||rotZ!=0)
            Matrix.multiplyMM(transformationMatrix, 0, matrixRotate, 0, transformationMatrix, 0);
        Matrix.multiplyMM(transformationMatrix, 0, matrixTranslate, 0, transformationMatrix, 0);
        for (int i=0; i<3; i++) {
            float[] vertex = new float[4];
            for (int j=0; j<3; j++)
                vertex[j] = this.vertices[i][j];
            vertex[3] = 1;
            Matrix.multiplyMV(vertex, 0, transformationMatrix, 0, vertex, 0);
            for (int j=0; j<3; j++)
                this.vertices[i][j] = vertex[j];
        }
    }
  */

}

