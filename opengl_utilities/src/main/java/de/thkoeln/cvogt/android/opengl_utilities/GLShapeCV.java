// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 17.3.2022

package de.thkoeln.cvogt.android.opengl_utilities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * Class to define shapes, i.e. 2D or 3D objects, that can be rendered by a renderer of class GLRendererCV on a view of class GLSurfaceViewCV.
 * Such shapes are defined by a collection of triangles of class GLTriangleCV.
 * They can be animated.
 * <BR>
 * An object of this class typically includes these things:
 * <UL>
 * <LI>A collection of colored or textured triangles. It is assumed that all triangles have the same coloring / texture type
 * (i.e. have all a uniform color [but possibly different colors], a gradient color or a texture from a bitmap file).
 * The vertex coordinates of these triangles are specified with respect to the "model coordinate system" ("local coordinate system") of the shape they belong to.
 * The center (0,0,0) of the model coordinate system is typically the geometric center of the 2D or 3D object defined by the shape.
 * <LI>A model matrix specifying the translation, rotation, and scaling operations that map the model coordinates to world coordinates,
 * i.e. that place the shape into the "real world".
 * <BR>
 * <LI>Animators to define how the object shall be animated.
 * <LI>An OpenGL program with the vertex shaders and fragment shaders to be executed by the graphics hardware.
 * <LI>A draw() method to be called by a renderer to display the shape.
 * </UL>
 *  @see de.thkoeln.cvogt.android.opengl_utilities.GLSurfaceViewCV
 *  @see de.thkoeln.cvogt.android.opengl_utilities.GLRendererCV
 *  @see de.thkoeln.cvogt.android.opengl_utilities.GLTriangleCV
 *  @see de.thkoeln.cvogt.android.opengl_utilities.GLShapeFactoryCV
 *  @see de.thkoeln.cvogt.android.opengl_utilities.GLAnimatorFactoryCV
 */

public class GLShapeCV {

    /** The ID of the shape. */

    private String id;

    /** The surface view to which the shape is currently attached. */

    private GLSurfaceViewCV surfaceView;

    /** The triangles building this shape. The vertex coordinates of these triangles are specified with respect to the "model coordinate system" ("local coordinate system") of this shape. */

    private GLTriangleCV[] triangles;

    /**
     * Coloring / texture type of the shape (values according to GLPlatformCV, constants COLORING_XXX).
     * This value will be derived from the triangles. It is assumed that all triangles have the same coloring / texture type.
     */

    private int coloringType;

    /**
     * The model matrix specifying the translation, rotation, and scaling operations that map the model coordinates of the triangle vertices to world coordinates.
     * It thus "places the shape into the real world".
     * The model matrix is automatically calculated from the scaling matrix (attribute scalingMatrix),
     * the rotation axis and angle (attributes rotAxis and rotAngle), and the translation matrix (attribute translationMatrix),
     */

    private float[] modelMatrix;

    /** The scaling matrix (a float array of length 16, as required by OpenGL).
     * The scaling factor for the x dimension is stored at position 0,
     * for the y dimension at position 5 and for the z dimension at position 10.
     * The factors can be written and read through the setScale and getScale methods.
     * Its initial value is the identity matrix.
     */

    private float[] scalingMatrix;

    /** The rotation axis.
     * This is an array of length 3 with the x, y, and z coordinates of the axis vector in position 0, 1, and 2.
     * Its initial value is (1,0,0). */

    private float[] rotAxis = null;

    /** The rotation angle (in degrees).
     * Its initial value is 0. */

    private float rotAngle;

    // TODO Attribut für die Rotationsmatrix,
    // automatische Aktualisierung der Rotationsmatrix, von rotAxis und rotAngle, wenn sich einer der Werte ändert.

    /** The translation matrix (a float array of length 16, as required by OpenGL).
     * The translation value for the x dimension is stored at position 12,
     * for the y dimension at position 13 and for the z dimension at position 14.
     * The values can be written and read through the setTrans and getTrans methods.
     * Its initial value is the identity matrix.
     */

    private float[] translationMatrix;

    /**
     * The animators to be applied to the shape.
     */

    private ArrayList<ObjectAnimator> animators;

    /** The ID of the OpenGL ES program to draw this shape. */

    private int openGLprogram;

    /** Information whether the OpenGL program has been compiled.
     * If not, the renderer will compile the program in its onDrawFrame() method,
     * i.e. call the makeOpenGLProgram() method of this shape.
     */

    private boolean isCompiled;

    /** The Open GL ES vertex shader codes. */

    private String vertexShaderCode;

    /** The Open GL ES fragment shader codes. */

    private String fragmentShaderCode;

    /** Buffer to pass the vertex coordinates to the graphics hardware. */

    private FloatBuffer vertexBuffer;

    /**
     * A one-dimensional array to hold the color values of the triangle vertices if the triangles are uniformly colored or colored with a gradient.
     * The array is initialized by the constructor from the color values of the triangles.
     */

    private float colorArray[];

    /** Buffer to pass the color values to the graphics hardware. */

    private FloatBuffer colorBuffer;

    /**
     * Bitmaps specifying the triangle textures if the triangles are textured.
     * The array is initialized by the constructor from the texture bitmaps of the triangles.
     */

    private Bitmap[] textureBitmaps;

    /** IDs of the textures. */

    private int[] texturenames;

    /**
     * Bitmaps specifying the uv coordinates for the triangle textures if the triangles a textured.
     * The array is initialized by the constructor from the uv coordinates of the triangles.
     */

    private float uvCoordinates[];

    /** Buffer to pass the uv coordinates to the graphics hardware. */

    private FloatBuffer uvBuffer;

    /**
     * The constructor will prepare the OpenGL code to be executed for this shape with the corresponding attribute values ('vertexBuffer' etc.).
     * The OpenGL code is not compiled by the constructor (which would not work that early)
     * but by a later separate call of makeOpenGLProgram() from the onSurfaceCreated() method of a corresponding renderer.
     * The constructor will also prepare the model matrix from the corresponding float attributes ('scaleX', ...).
     * @param id The ID of the shape.
     * @param triangles The triangles for the shape. Clones of these triangles will be assigned to the 'triangles' attribute.
     */

    public GLShapeCV(String id, GLTriangleCV triangles[]) {

        if (triangles==null) return;

        this.id = new String(id);

        // prepare the matrices, the rotation axis and the rotation angle

        modelMatrix = new float[16];
        scalingMatrix = new float[16];
        translationMatrix = new float[16];
        Matrix.setIdentityM(scalingMatrix,0);
        Matrix.setIdentityM(translationMatrix,0);

        rotAxis = new float[3];
        rotAxis[0] = 1;
        rotAngle = 0;

        // set the triangles building this shape

        this.triangles = new GLTriangleCV[triangles.length];
        for (int i=0;i<triangles.length;i++)
            if (triangles[i]!=null)
                this.triangles[i] = triangles[i].clone();

        setAttributesAndOpenGLProgramFromTriangles();

    }

    /**
     * Auxiliary method to set the OpenGL program to be executed and corresponding attributes
     * based on the triangles of this shape.
     * Must be called when the set of triangles is initialized (i.e. from an GLShapeCV constructor)
     * or changes (i.e. from the methods addTriangle(), addTriangles(), and moveCenterTo())
     */

    synchronized private void setAttributesAndOpenGLProgramFromTriangles() {
        // build the ModelMatrix 'modelMatrix' from the attribute values scaleX, scaleY, ...

        buildModelMatrixFromAttributes();

        // prepare the list of animators

        animators = new ArrayList<ObjectAnimator>();

        // determine the coloring type of its triangles and set the shaders accordingly,
        // assuming that all triangles have the same coloring type

        coloringType = triangles[0].getColoringType();

        switch (coloringType) {
            case GLPlatformCV.COLORING_UNIFORM:
                // TODO: Hier Code von GLPlatformCV.vertexShaderUniformColor zuweisen, sobald er funktioniert
            case GLPlatformCV.COLORING_GRADIENT:
                vertexShaderCode = GLPlatformCV.vertexShaderColorGradient;
                fragmentShaderCode = GLPlatformCV.fragmentShaderColorGradient;
                break;
            case GLPlatformCV.COLORING_TEXTURED:
                vertexShaderCode = GLPlatformCV.vertexShaderTextured;
                fragmentShaderCode = GLPlatformCV.fragmentShaderTextured;
                break;
            default: return;
        }

        // prepare the buffer with the vertex coordinates

        float[] coordinates = coordinateArrayFromTriangles();
        ByteBuffer bb = ByteBuffer.allocateDirect(coordinates.length*4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(coordinates);
        vertexBuffer.position(0);

        // set colors or textures

        switch (coloringType) {
            case GLPlatformCV.COLORING_UNIFORM:
                // TODO: Hier auf Basis des Codes von GLPlatformCV.vertexShaderUniform programmieren, sobald dieser funktioniert
            case GLPlatformCV.COLORING_GRADIENT:
                colorArray = colorArrayFromTriangles();
                ByteBuffer bbCol = ByteBuffer.allocateDirect(colorArray.length * 4);  // 4 Bytes per float
                bbCol.order(ByteOrder.nativeOrder());  // native byte order of the device
                colorBuffer = bbCol.asFloatBuffer();
                colorBuffer.put(colorArray);
                colorBuffer.position(0);  // set read index to the first buffer element
                break;
            case GLPlatformCV.COLORING_TEXTURED:
                int uvIndex = 0;
                uvCoordinates = new float[triangles.length*6];
                for (GLTriangleCV triangle : triangles) {
                    float[] uvCoordinatesTriangle = triangle.getUvCoordinates();
                    for (int i=0; i<6; i++)
                        uvCoordinates[uvIndex++] = uvCoordinatesTriangle[i];
                }
                ByteBuffer bbUV = ByteBuffer.allocateDirect(uvCoordinates.length * 4);
                bbUV.order(ByteOrder.nativeOrder());
                uvBuffer = bbUV.asFloatBuffer();
                uvBuffer.put(uvCoordinates);
                uvBuffer.position(0);
                textureBitmaps = new Bitmap[triangles.length];
                for (int i=0; i<triangles.length; i++)
                    textureBitmaps[i] = triangles[i].getTexture();
                texturenames = new int[textureBitmaps.length];
                break;
        }

    }

    /**
     * To compile and link the OpenGL program from the shader codes selected by the constructor.
     * This method will be called from the onSurfaceCreated() method of the renderer that shall render the shade.
     * An earlier call (esp. from the shape constructor) will lead to an OpenGL link and/or compile error.
     */
    // TODO Diese Methode auslagern nach GLPlatformCV

    synchronized public void makeOpenGLProgram() {

        // create OpenGL shaders

        int vertexShader = GLPlatformCV.loadShader(GLES20.GL_VERTEX_SHADER,this.vertexShaderCode);
        int fragmentShader = GLPlatformCV.loadShader(GLES20.GL_FRAGMENT_SHADER,this.fragmentShaderCode);

        // create textures for textured shapes

        if (coloringType== GLPlatformCV.COLORING_TEXTURED) {
            GLES20.glGenTextures(texturenames.length, texturenames, 0);
            for (int i = 0; i < textureBitmaps.length; i++) {
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[i]);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, textureBitmaps[i], 0);
            }
        }

        // create the program

        openGLprogram = GLES20.glCreateProgram();
        GLES20.glAttachShader(openGLprogram, vertexShader);
        GLES20.glAttachShader(openGLprogram, fragmentShader);
        GLES20.glLinkProgram(openGLprogram);

        // debug information to see if the OpenGL code has been linked successfully

        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(openGLprogram, GLES20.GL_LINK_STATUS, linkStatus, 0);
        Log.v("GLDEMO",">>> Link Status: "+linkStatus[0]+" (0 = failed, 1 = ok)");
        if (linkStatus[0]==1)
            isCompiled = true;
          else
            Log.v("GLDEMO",">>> Error: "+GLES20.glGetProgramInfoLog(openGLprogram));

    }

    synchronized public String getId() {
        return id;
    }

    synchronized public boolean isCompiled() {
        return isCompiled;
    }

    synchronized public void setSurfaceView(GLSurfaceViewCV surfaceView) {
        this.surfaceView = surfaceView;
    }

    synchronized public GLSurfaceViewCV getSurfaceView() {
        return surfaceView;
    }

    /**
     * Get a list with copies of all triangles of the shape.
     * @return The list with the triangles.
     */

    synchronized public GLTriangleCV[] getTriangles() {
        GLTriangleCV[] trianglesCopy = new GLTriangleCV[triangles.length];
        for (int i=0; i<triangles.length; i++)
            trianglesCopy[i] = triangles[i].clone();
        return trianglesCopy;
    }

    /**
     * Adds a triangle to the shape.
     * @param newTriangle The triangle to be added.
     */

    synchronized public void addTriangle(GLTriangleCV newTriangle) {
        if (newTriangle==null) return;
        GLTriangleCV[] newTriangleArray = { newTriangle };
        addTriangles(newTriangleArray);
    }

    /**
     * Adds triangles to the shape.
     * @param newTriangles The triangles to be added.
     */

    synchronized public void addTriangles(GLTriangleCV[] newTriangles) {
        if (newTriangles==null||newTriangles.length==0) return;
        GLTriangleCV[] newTriangleArray = new GLTriangleCV[triangles.length+newTriangles.length];
        for (int i=0; i<triangles.length; i++)
            newTriangleArray[i] = triangles[i];
        for (int i=0; i<newTriangles.length; i++)
            newTriangleArray[triangles.length+i] = newTriangles[i];
        triangles = newTriangleArray;
        setAttributesAndOpenGLProgramFromTriangles();
    }

    /**
     * Changes the center of the shape, i.e. the origin (0,0,0) of its local coordinate system ("model coordinate system"),
     * by translating all coordinate values of its triangles by the same vector.
     * This will, in particular, affect future rotation operations on the shape as shapes are rotated around their origin.
     * <BR>
     * Note the difference between this method and the methods setScale(), setTrans() etc.:
     * This method changes the origin of the local coordinate system and hence modifies the coordinate values of its triangles
     * The other methods specify how the shape shall be placed into the real world,
     * i.e. how to calculate the model matrix (see method buildModelMatrixFromAttributes()),
     * but leaves the coordinate values stored with the triangles unchanged.
     * @param transX x component of the translation vector.
     * @param transY y component of the translation vector.
     * @param transZ z component of the translation vector.
     */

    synchronized public void moveCenterTo(float transX, float transY, float transZ) {
        // Log.v("DEMO2","moveCenterTo: "+transX+" "+transY+" "+transZ);
        for (GLTriangleCV triangle: triangles)
            triangle.translate(-transX,-transY,-transZ);
        setAttributesAndOpenGLProgramFromTriangles();
    }

    /**
     * Gets the maximum difference between the x coordinates of any two triangle vertices,
     * i.e. the extension of the enclosing cube in the x dimension.
     * The value refers to the local coordinate system (model coordinate system),
     * i.e. disregards the transformations specified by the model matrix attributes scaleX etc.
     * @return The extension of the shape in the x dimension.
     */

    synchronized public float getIntrinsicSizeX() {
        return getIntrinsicSize(0);
    }

    /**
     * Gets the maximum difference between the y coordinates of any two triangle vertices,
     * i.e. the extension of the enclosing cube in the y dimension.
     * The value refers to the local coordinate system (model coordinate system),
     * i.e. disregards the transformations specified by the model matrix attributes scaleY etc.
     * @return The extension of the shape in the y dimension.
     */

    synchronized public float getIntrinsicSizeY() {
        return getIntrinsicSize(1);
    }

    /**
     * Gets the maximum difference between the z coordinates of any two triangle vertices,
     * i.e. the extension of the enclosing cube in the z dimension.
     * The value refers to the local coordinate system (model coordinate system),
     * i.e. disregards the transformations specified by the model matrix attributes scaleZ etc.
     * @return The extension of the shape in the z dimension.
     */

    synchronized public float getIntrinsicSizeZ() {
        return getIntrinsicSize(2);
    }

    /**
     * Auxiliary method to be called by getIntrinsicSizeX(), ...Y(), and ...Z().
     * For more details, see the explanation of these methods.
     * @param dimension The dimension to be measured (0 = x, 1 = y, 2 = z).
     * @return The intrinsic size in the requested dimension.
     */

    synchronized private float getIntrinsicSize(int dimension) {
        if (dimension<0||dimension>2) return -1;
        if (triangles==null||triangles.length==0) return 0;
        float min, max;
        min = max = triangles[0].getVertices()[0][dimension];
        for (GLTriangleCV triangle : triangles) {
            float[][] vertices = triangle.getVertices();
            for (int i=0;i<3;i++) {
                if (vertices[i][dimension]<min)
                    min = vertices[i][dimension];
                if (vertices[i][dimension]>max)
                    max = vertices[i][dimension];
            }
        }
        return max-min;
    }

    /**
     * Sets the scaling factor in the x dimension and updates the model matrix accordingly.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     */

    synchronized public GLShapeCV setScaleX(float scaleX) {
        scalingMatrix[0] = scaleX;
        buildModelMatrixFromAttributes();
        return this;
    }

    synchronized public float getScaleX() {
        return scalingMatrix[0];
    }

    /**
     * Sets the scaling factor in the x dimension and updates the model matrix accordingly.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     */

    synchronized public GLShapeCV setScaleY(float scaleY) {
        scalingMatrix[5] = scaleY;
        buildModelMatrixFromAttributes();
        return this;
    }

    synchronized public float getScaleY() {
        return scalingMatrix[5];
    }

    /**
     * Sets the scaling factor in the z dimension and updates the model matrix accordingly.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     */

    synchronized public GLShapeCV setScaleZ(float scaleZ) {
        scalingMatrix[10] = scaleZ;
        buildModelMatrixFromAttributes();
        return this;
    }

    synchronized public float getScaleZ() {
        return scalingMatrix[10];
    }

    /**
     * Sets the attributes for the scaling factors and updates the model matrix accordingly.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     */

    synchronized public GLShapeCV setScale(double scaleX, double scaleY, double scaleZ) {
        scalingMatrix[0] = (float) scaleX;
        scalingMatrix[5] = (float) scaleY;
        scalingMatrix[10] = (float) scaleZ;
        buildModelMatrixFromAttributes();
        return this;
    }

    /**
     * Sets the scaling attributes scaleX, scaleY and scaleZ to the same value.
     * As the return value of this method is the shape itself, method calls of this kind can be daisy chained.
     */

    synchronized public GLShapeCV setScale(float scale) {
        return setScale(scale,scale,scale);
    }

    /**
     * Sets the rotation axis, i.e. the rotAxis attribute.
     * @param axis_x The x coordinate of the axis vector.
     * @param axis_y The y coordinate of the axis vector.
     * @param axis_z The z coordinate of the axis vector.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     */

    synchronized public GLShapeCV setRotAxis(float axis_x, float axis_y, float axis_z) {
        float[] axis = new float[3];
        axis[0] = axis_x;
        axis[1] = axis_y;
        axis[2] = axis_z;
        return setRotAxis(axis);
    }

    /**
     * Sets the rotation axis, i.e. the rotAxis attribute.
     * @param rotAxis The rotation axis to be set (a copy of this array will be assigned to the attribute).
     * @return The shape itself, such that calls of methods of this kind can be daisy chained
     * (null if the parameter was not valid, i.e. null or an array with length not equal 3).
     */

    synchronized public GLShapeCV setRotAxis(float[] rotAxis) {
        if (rotAxis==null||rotAxis.length!=3) return null;
        this.rotAxis = rotAxis.clone();
        buildModelMatrixFromAttributes();
        return this;
    }

    /**
     * Gets the rotation axis.
     * @return A copy of the rotAxis attribute.
     */

    synchronized public float[] getRotAxis() {
        if (rotAxis==null) return null;
        return rotAxis.clone();
    }

    /**
     * Sets the rotation angle.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     */

    synchronized public GLShapeCV setRotAngle(float rotAngle) {
        this.rotAngle = rotAngle;
        buildModelMatrixFromAttributes();
        return this;
    }

    synchronized public float getRotAngle() {
        return rotAngle;
    }

    /**
     * Sets the value of the 'transX' attribute and updates the model matrix accordingly.
     * @param transX The new value for the attribute.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     */

    synchronized public GLShapeCV setTransX(float transX) {
        translationMatrix[12] = transX;
        buildModelMatrixFromAttributes();
        return this;
    }

    synchronized public float getTransX() {
        return translationMatrix[12];
    }

    /**
     * Sets the value of the 'transY' attribute and updates the model matrix accordingly.
     * @param transY The new value for the attribute.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     */
    synchronized public GLShapeCV setTransY(float transY) {
        translationMatrix[13] = transY;
        buildModelMatrixFromAttributes();
        return this;
    }

    synchronized public float getTransY() {
        return translationMatrix[13];
    }

    /**
     * Sets the value of the 'transZ' attribute and updates the model matrix accordingly.
     * @param transZ The new value for the attribute.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     */
    synchronized public GLShapeCV setTransZ(float transZ) {
        translationMatrix[14] = transZ;
        buildModelMatrixFromAttributes();
        return this;
    }

    synchronized public float getTransZ() {
        return translationMatrix[14];
    }

    /**
     * Sets the values of the translation attributes and updates the model matrix accordingly.
     * @param transX The new value for the 'transX' attribute.
     * @param transY The new value for the 'transY' attribute.
     * @param transZ The new value for the 'transZ' attribute.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     */

    synchronized public GLShapeCV setTrans(float transX, float transY, float transZ) {
        translationMatrix[12] = transX;
        translationMatrix[13] = transY;
        translationMatrix[14] = transZ;
        buildModelMatrixFromAttributes();
        return this;
    }

    /**
     * Sets the values of the translation attributes and updates the model matrix accordingly.
     * @param trans The new values for the attributes (trans[0] = transX, trans[1] = transY, trans[2] = transZ).
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     */

    synchronized public GLShapeCV setTrans(float trans[]) {
        return setTrans(trans[0],trans[1],trans[2]);
    }

    /**
     * Gets the current values of the translation attributes.
     * @return An array of length 3 with transX at pos. 0, transY at pos. 1, transZ at pos. 2)
     */

    synchronized public float[] getTrans() {
        float[] result = new float[3];
        result[0] = translationMatrix[12];
        result[1] = translationMatrix[13];
        result[2] = translationMatrix[14];
        return result;
    }

    /** NOT AVAILABLE YET
     * Aligns the shape with another shape, i.e. sets the values of the rotAngleX/Y/Z attributes with the values of the other shapes.
     * @param shapeToAlignWith The other shape to align this shape with.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained (or null if the parameter is not valid).
     */

    synchronized public GLShapeCV alignWith(GLShapeCV shapeToAlignWith) {
        if (shapeToAlignWith==null) return null;
        // TODO Ausprogrammieren
        return this;
    }

    /** NOT AVAILABLE YET
     * Scales, rotates, and translates the shape such that it will "connect" two points in 3D space,
     * i.e. the "bottom" of the shape will be the first point and the "top" of the shape will be the second point.
     * Scaling will be done in the y dimension.
     * Rotation will rotate the shape from the direction defined by the y axis (0,1,0) to the direction defined by the vector between the two points.
     * The method is primarily applicable to prisms, cuboids, and pyramids.
     * @param point1 The first point.
     * @param point2 The second point.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained (or null if one of the parameters is not valid).
     */

    synchronized public GLShapeCV placeBetweenPoints(float[] point1, float[] point2) {
        if (point1==null||point1.length!=3||point2==null||point2.length!=3) return null;
/*
        // scaling in the y dimension
        setScale(1,GraphicsUtilsCV.distance(point1,point2)/getIntrinsicSizeY(),1);
        // rotation of the shape:
        // - current orientation is assumed to be (0,1,0)
        // - shape must be rotated around a vector that is the cross product of the vectors (0,1,0) and (axisPoint1-axisPoint2),
        //   i.e. a vector that is perpendicular to the plane spanned by these two vectors
        //   (Details e.g.: https://stackoverflow.com/questions/69669771/calculate-rotation-to-align-object-with-two-points-in-3d-space)
        float y_axis[] = {0,1,0};
        float vectorBetweenPoints[] = GraphicsUtilsCV.vectorBetweenPoints(point1,point2);
        if (Math.abs(vectorBetweenPoints[0])>10e-5||Math.abs(vectorBetweenPoints[1])>10e-5) {
            float rotAxisForShape[] = GraphicsUtilsCV.crossProduct(y_axis, vectorBetweenPoints);
            float rotAngleForShape = (float) Math.toDegrees(Math.acos(GraphicsUtilsCV.dotProduct(y_axis, GraphicsUtilsCV.getNormalizedCopy(GraphicsUtilsCV.vectorBetweenPoints(point1, point2)))));
            setRotAngleX(GraphicsUtilsCV.eulerAngleX(rotAxisForShape, rotAngleForShape));
            setRotAngleY(GraphicsUtilsCV.eulerAngleY(rotAxisForShape, rotAngleForShape));
            setRotAngleZ(GraphicsUtilsCV.eulerAngleZ(rotAxisForShape, rotAngleForShape));
        }
        setTrans(GraphicsUtilsCV.midpoint(point1,point2));
*/
        return this;
    }

    /**
     * Builds the model matrix (i.e. the 'modelMatrix' attribute) from the attributes scaling, rotation, and translation attributes of the shape.
     * For details, see the note in the introductory text on the order of transformation operations.
     */

    synchronized private void buildModelMatrixFromAttributes() {
        // rotation matrices
        /*
        if (rotMatrix==null)
          if (rotAxis==null) {
*/
            /*
            float[] matrixRotateX = new float[16];
            Matrix.setIdentityM(matrixRotateX, 0);
            Matrix.rotateM(matrixRotateX, 0, rotAngleX, 1, 0, 0);
            float[] matrixRotateY = new float[16];
            Matrix.setIdentityM(matrixRotateY, 0);
            Matrix.rotateM(matrixRotateY, 0, rotAngleY, 0, 1, 0);
            float[] matrixRotateZ = new float[16];
            Matrix.setIdentityM(matrixRotateZ, 0);
            Matrix.rotateM(matrixRotateZ, 0, rotAngleZ, 0, 0, 1);

            // calculate the model matrix by multiplying the scaling, rotation, and translation matrices (in this order!)
            Matrix.setIdentityM(modelMatrix,0);
            Matrix.multiplyMM(modelMatrix, 0, scalingMatrix, 0, modelMatrix, 0);
            Matrix.multiplyMM(modelMatrix, 0, matrixRotateY, 0, modelMatrix, 0);
            Matrix.multiplyMM(modelMatrix, 0, matrixRotateZ, 0, modelMatrix, 0);
            Matrix.multiplyMM(modelMatrix, 0, matrixRotateX, 0, modelMatrix, 0);

             */
            Matrix.setIdentityM(modelMatrix,0);
            Matrix.multiplyMM(modelMatrix, 0, scalingMatrix, 0, modelMatrix, 0);
            float[] rotationMatrix = new float[16];
            Matrix.setIdentityM(rotationMatrix,0);
            Matrix.rotateM(rotationMatrix,0,rotAngle,rotAxis[0],rotAxis[1],rotAxis[2]);
            Matrix.multiplyMM(modelMatrix, 0, rotationMatrix, 0, modelMatrix, 0);
            Matrix.multiplyMM(modelMatrix, 0, translationMatrix, 0, modelMatrix, 0);
/*
         }
          else {
              float[] matrixRotate = new float[16];
            // rotation matrix
            Matrix.setIdentityM(matrixRotate,0);
            Matrix.rotateM(matrixRotate,0,rotAngle,rotAxis[0],rotAxis[1],rotAxis[2]);
            // translation matrix
            float[] matrixTranslate = new float[16];
            Matrix.setIdentityM(matrixTranslate,0);
            Matrix.translateM(matrixTranslate,0,transX,transY,transZ);
            // calculate the model matrix by multiplying the scaling, rotation, and translation matrices (in this order!)
            Matrix.multiplyMM(modelMatrix, 0, matrixScale, 0, modelMatrix, 0);
            Matrix.multiplyMM(modelMatrix, 0, matrixRotate, 0, modelMatrix, 0);
            Matrix.multiplyMM(modelMatrix, 0, matrixTranslate, 0, modelMatrix, 0);
        }
        else {
            // translation matrix
            float[] matrixTranslate = new float[16];
            Matrix.setIdentityM(matrixTranslate,0);
            Matrix.translateM(matrixTranslate,0,transX,transY,transZ);
            // calculate the model matrix by multiplying the scaling, rotation, and translation matrices (in this order!)
            Matrix.multiplyMM(modelMatrix, 0, matrixScale, 0, modelMatrix, 0);
            Matrix.multiplyMM(modelMatrix, 0, rotMatrix, 0, modelMatrix, 0);
            Matrix.multiplyMM(modelMatrix, 0, matrixTranslate, 0, modelMatrix, 0);
        }

 */
    }

   /**
    * Add an animator to the list of animators.
    * <BR>
    * An update listener will be added to the animator which will let the renderer redraw the shape when required by the animator.
    * <BR>
    * The animator will not be started immediately but from the addShape() method of an GLSurfaceViewCV object, i.e. when the shape is added to a surface view.
    * <BR>
    * N.B.: If an animator is added to the shape after the shape has already been added to a surface view,
    * the animator must be started explicitly by calling animator.start().
    * @param animator The animator to be added.
    */

    synchronized public void addAnimator(ObjectAnimator animator) {
        animators.add(animator);
        /* It is not necessary to register an update listener
           because the renderer method onDrawFrame() will be called automatically
           when the animator has modified the shape's attributes.
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                try {
                    float[] viewProjectionMatrix = getSurfaceView().getRenderer().getViewProjectionMatrix();
                    // Log.v("DEMO",">>> onAnimationUpdate");
                    getSurfaceView().queueEvent(new Runnable() {
                        @Override
                        public void run() {
                                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
                                GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                                GLES20.glEnable(GLES20.GL_DEPTH_TEST);  // such that fragments in the front ...
                                GLES20.glDepthFunc(GLES20.GL_LESS);     // ... hide fragments in the back
                                GLES20.glDepthMask( true );
                                // draw the shapes based on the current view projection matrix
                                ArrayList<GLShapeCV> shapesToRender = surfaceView.getShapesToRender();
                                for (GLShapeCV shape : shapesToRender)
                                    shape.draw(viewProjectionMatrix);
                            // draw(viewProjectionMatrix);
                        }
                    });
                } catch (NullPointerException exc) { return; }
            }
        });
        */
    }

    /**
     * Starts the animators associated with the shape.
     * This method will called from the addShape() method of an GLSurfaceViewCV object, i.e. when the shape is added to a surface view.
     */

    synchronized public void startAnimators() {
        if (animators==null||animators.isEmpty()) return;
        // Log.v("DEMO","start animators: "+animators.size());
        AnimatorSet animset = new AnimatorSet();
        Animator animarray[] = new Animator[animators.size()];
        int i=0;
        for (Animator animator: animators)
            animarray[i++] = animator;
        animset.playTogether(animarray);
        animset.start();
    }

    /**
     * The method to be called by a renderer to draw the shape.
     * The view/projection matrix passed by the renderer is multiplied with the model matrix of the shape.
     * Moreover, the animators as specified by the 'animators' attribute (if any) are applied.
     * @param vpMatrix The view/projection matrix to be passed by the renderer.
     */

    synchronized public void draw(float[] vpMatrix) {

        // Log.v("GLDEMO",">>>>> draw "+id);

        // calculate the MVP matrix from the model matrix of the shape and the view/projection matrix from the renderes

        float[] mvpMatrix = new float[16];

        Matrix.multiplyMM(mvpMatrix, 0, vpMatrix, 0, modelMatrix, 0);

        // set some fundamental constants

        final int COORDS_PER_VERTEX = 3;  // coordinates (3 = three-dimensional space)
        final int COLORS_PER_VERTEX = 4;  // number of color values per vertex (4 = RGBA)

        final int vertexCount = triangles.length * 3;    // total number of vertices
        final int vertexStride = COORDS_PER_VERTEX * 4;  // total number of bytes to store the coordinates of a vertex

        // use the program defined in the constructor

        // TODO DAS FOLGENDE ALS METHODEN NACH GLPlatformCV
        GLES20.glUseProgram(openGLprogram);

        // pass the MVP matrix to the program

        int mMVPMatrixHandle = GLES20.glGetUniformLocation(openGLprogram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // get and activate a handle for the vPosition attribute of the vertex shader (coordinates of the vertices)

        int positionHandle = GLES20.glGetAttribLocation(openGLprogram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);

        // connect the 'vertexBuffer' attribute with the vertex coordinates with the vPosition attribute
        // = pass the coordinates to the graphics hardware

        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        switch (coloringType) {

            case GLPlatformCV.COLORING_UNIFORM:
                // TODO: Hier auf Basis des Codes von GLPlatformCV.vertexShaderUniform programmieren, sobald dieser funktioniert
                // Dann auch den Fall berücksichtigen, dass alle Triangles jeweils eine einheitliche Farbe haben, diese Farben aber unterschiedlich sind
                // colorHandle = GLES20.glGetUniformLocation(openGLprogram, "vColor");  // für einfarbige Würfel
                // GLES20.glUniform4fv(colorHandle, 1, colorArray, 0);
                // break;
            case GLPlatformCV.COLORING_GRADIENT:
                int colorHandle = GLES20.glGetAttribLocation(openGLprogram, "aColor");
                GLES20.glVertexAttribPointer(colorHandle, COLORS_PER_VERTEX, GLES20.GL_FLOAT, false, 16, colorBuffer);
                GLES20.glEnableVertexAttribArray(colorHandle);
                // draw the shape
                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
                // deactivate the attribute arrays
                GLES20.glDisableVertexAttribArray(positionHandle);
                GLES20.glDisableVertexAttribArray(colorHandle);
                break;

            case GLPlatformCV.COLORING_TEXTURED:
                int textureHandle = GLES20.glGetAttribLocation(openGLprogram, "aTexCoord" );
                // buffer for the uv coordinates
                ByteBuffer bbUV = ByteBuffer.allocateDirect(uvCoordinates.length * 4);
                bbUV.order(ByteOrder.nativeOrder());
                uvBuffer = bbUV.asFloatBuffer();
                uvBuffer.put(uvCoordinates);
                uvBuffer.position(0);
                GLES20.glVertexAttribPointer(textureHandle, 2, GLES20.GL_FLOAT, false, 8, uvBuffer);
                GLES20.glEnableVertexAttribArray(textureHandle);
                for (int i=0;i<triangles.length;i++) {   // draw the triangles one by one, setting the texture anew for each individual triangle
                    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[i]);
                    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 3*i, 3);
                }
                // disable the vertex array
                GLES20.glDisableVertexAttribArray(positionHandle);
                GLES20.glDisableVertexAttribArray(textureHandle);
                break;

        }

    }

    /**
     * Auxiliary method to get a one-dimensional float array with the vertex coordinates
     */

    synchronized private float[] coordinateArrayFromTriangles() {
        float coordinateArray[] = new float[triangles.length*9];
        for (int triangleNo = 0; triangleNo<this.triangles.length; triangleNo++) {
            float[] coordsCurrentTriangle = this.triangles[triangleNo].getVertexCoordinates();
            for (int i=0; i<9; i++)
                coordinateArray[triangleNo*9+i] = coordsCurrentTriangle[i];
        }
        return coordinateArray;
    }

    /**
     * Auxiliary method to get a one-dimensional float array with the vertex colors
     */

    synchronized private float[] colorArrayFromTriangles() {
        float colorArray[] = new float[triangles.length * 12];
        for (int i = 0; i < triangles.length; i++)      // all triangles
            for (int j = 0; j < 3; j++)    // all vertices of a triangle
                for (int k = 0; k < 4; k++) {   // RGBA values of a vertex
                    colorArray[i * 12 + j * 4 + k] = triangles[i].getVertexColor(j)[k];
                }
        return colorArray;
    }

}
