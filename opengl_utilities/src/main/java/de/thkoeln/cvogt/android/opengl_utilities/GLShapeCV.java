// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 9.10.2022

package de.thkoeln.cvogt.android.opengl_utilities;

import android.animation.Animator;
import android.animation.AnimatorSet;
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
 * Class to define shapes, i.e. 2D or 3D objects, that can be rendered by a renderer of class <I>GLRendererCV</I> on a view of class <I>GLSurfaceViewCV</I>.
 * Such shapes are defined by a collection of triangles of class <I>GLTriangleCV</I> and/or a set of lines of class <I>GLLineCV</I>.
 * The shapes can be animated.
 * <P>
 * An object of this class typically includes these items:
 * <P>
 * <UL>
 * <LI>A collection of colored or textured triangles and/or a collection of colored lines between two endpoints each.
 * It is assumed that all triangles have the same coloring / texture type
 * (i.e. have all a uniform color [but possibly different colors for the different triangles], a gradient color or a texture from a bitmap file).
 * Note: The current version of this class displays lines only if the triangles are colored, i.e. not textured.
 * <P>
 * The vertex coordinates of these triangles and lines are specified with respect to the "model coordinate system" ("local coordinate system") of the shape they belong to.
 * The center (0,0,0) of the model coordinate system is typically the geometric center of the 2D or 3D object defined by the shape,
 * e.g. the center of a circle or a cube.
 * <P><LI>A model matrix specifying the translation, rotation, and scaling operations that map the model coordinates to world coordinates,
 * i.e. that place the shape into the "real world".
 * <P><LI>Animators to define how the object shall be animated.
 * <P><LI>An OpenGL program with the vertex shaders and fragment shaders to be executed by the graphics hardware.
 * <P><LI>A <I>draw()</I> method to be called by a renderer to display the shape.
 * </UL>
 *  @see GLSurfaceViewCV
 *  @see GLRendererCV
 *  @see GLTriangleCV
 *  @see GLLineCV
 *  @see GLShapeFactoryCV
 *  @see GLAnimatorFactoryCV
 *  @see GLSceneFactoryCV
 *  @see TextureBitmapsCV
 */

public class GLShapeCV {

    /** The ID of the shape. */

    private String id;

    /** The surface view to which the shape is currently attached. */

    private GLSurfaceViewCV surfaceView;

    /**
     * Coloring / texture type of the shape (values according to GLPlatformCV, constants COLORING_XXX).
     * This value will be derived from the triangles. It is currently assumed that all triangles have the same coloring / texture type.
     */

    private int coloringType;

    /** The triangles building this shape. The vertex coordinates of these triangles are specified with respect to the "model coordinate system" ("local coordinate system") of this shape.
     * <BR>
     * This attribute can be null. If so, the 'lines' attribute must not be null and the shape will consist of lines only. */

    private GLTriangleCV[] triangles;

    /**
     * Bitmaps specifying the triangle textures if the triangles are textured.
     * The array is initialized by the constructor from the texture bitmaps of the triangles.
     * Only valid if the triangles are textured, i.e. not colored.
     */

    private Bitmap[] textureBitmaps;

    /** IDs of the textures. Only valid if the triangles are textured, i.e. not colored. */

    private int[] textureNames;

    /**
     * Bitmaps specifying the uv coordinates for the triangle textures if the triangles a textured.
     * The array is initialized by the constructor from the uv coordinates of the triangles.
     * Only valid if the triangles are textured, i.e. not colored.
     */

    private float uvCoordinates[];

    /** The lines belonging to this shape. The vertex coordinates of these lines are specified with respect to the "model coordinate system" ("local coordinate system") of this shape.
     * <BR>
     * This attribute can be null. If so, the 'triangles' attribute must not be null and the shape will consist of triangles only.
     * <BR>
     * Note: The current version of this class displays lines only if the triangles are colored, i.e. not textured.
     */

    private GLLineCV[] lines;

    /**
     * The width of the lines.
     * Only valid if the shape has lines, i.e. the 'lines' attribute is not null.
     * Initialized by the constructor.
     */

    private float lineWidth;

    /**
     * The model matrix specifying the translation, rotation, and scaling operations that map the model coordinates of the triangle vertices to world coordinates.
     * It thus "places the shape into the real world".
     * The model matrix is automatically calculated from the scaling matrix (attribute scalingMatrix),
     * the rotation matrix (attribute rotationMatrix), and the translation matrix (attribute translationMatrix),
     */

    private float[] modelMatrix;

    /**
     * The scaling matrix (a float array of length 16, as required by OpenGL).
     * The scaling factor for the x dimension is stored at position 0,
     * for the y dimension at position 5 and for the z dimension at position 10.
     * The factors can be written and read through the setScale and getScale methods.
     * Its initial value is the identity matrix.
     */

    private float[] scalingMatrix;

    /**
     * The rotation matrix (a float array of length 16, as required by OpenGL).
     * Its initial value is the identity matrix.
     */

    private float[] rotationMatrix;

    /**
     * The translation matrix (a float array of length 16, as required by OpenGL).
     * The translation value for the x dimension is stored at position 12,
     * for the y dimension at position 13 and for the z dimension at position 14.
     * The values can be written and read through the setTrans and getTrans methods.
     * Its initial value is the identity matrix.
     */

    private float[] translationMatrix;

    /** The animators to be applied to the shape. Animation is based on the property animation technique provided by Android. */

    private ArrayList<Animator> animators;

    /** The ID of the OpenGL ES program to draw this shape. */

    private int openGLprogram;

    /**
     * Information whether the OpenGL program has been compiled.
     * If not, the renderer will compile the program in its onDrawFrame() method,
     * i.e. call the initOpenGLProgram() method of this shape.
     */

    private boolean isCompiled;

    /** The Open GL ES vertex shader codes. */

    private String vertexShaderCode;

    /** The Open GL ES fragment shader codes. */

    private String fragmentShaderCode;

    /**
     * Buffer to pass the vertex coordinates of the triangles to the graphics hardware.
     * Only valid if the shape has triangles, i.e. the 'triangles' attribute is not null.
     */

    private FloatBuffer triangleVerticesBuffer;

    /**
     * Buffer to pass the line coordinates of the lines to the graphics hardware.
     * Only valid if the shape has lines, i.e. the 'lines' attribute is not null.
     */

    private FloatBuffer lineEndsBuffer;

    /**
     * Buffer to pass the color values of the triangles to the graphics hardware.
     * The buffer is initialized by the method setModelMatrixAndBuffers() from the color values of the triangles
     * and used by the draw() method.
     * Only valid if the shape has triangles, i.e. the 'triangles' attribute is not null.
     */

    private FloatBuffer triangleColorsBuffer;

    /**
     * Buffer to pass the uv coordinates to the graphics hardware.
     * Only valid if the shape has textured triangles.
     */

    private FloatBuffer uvBuffer;

    /**
     * Buffer to pass the color values of the lines to the graphics hardware.
     * The buffer is initialized by the method setModelMatrixAndBuffers() from the color values of the triangles
     * and used by the draw() method.
     * Only valid if the shape has lines, i.e. the 'lines' attribute is not null.
     */

    private FloatBuffer lineColorsBuffer;

    /**
     * The constructor will prepare the OpenGL code to be executed for this shape with the corresponding attribute values ('vertexBuffer' etc.).
     * The OpenGL code is not compiled by the constructor (which would not work that early)
     * but by a later separate call of initOpenGLProgram() from the onSurfaceCreated() method of a corresponding renderer.
     * The constructor will also prepare the model matrix from the corresponding scaling, translation, and rotation matrix attributes.
     * @param id The ID of the shape.
     * @param triangles The triangles for the shape. Clones of these triangles will be assigned to the 'triangles' attribute.
     */

    public GLShapeCV(String id, GLTriangleCV triangles[]) {
        this(id,triangles,null,0);
    }

    /**
     * The constructor will prepare the OpenGL code to be executed for this shape with the corresponding attribute values ('vertexBuffer' etc.).
     * The OpenGL code is not compiled by the constructor (which would not work that early)
     * but by a later separate call of initOpenGLProgram() from the onSurfaceCreated() method of a corresponding renderer.
     * The constructor will also prepare the model matrix from the corresponding scaling, translation, and rotation matrix attributes.
     * @param id The ID of the shape.
     * @param lines The lines for the shape. A clone of this array will be assigned to the 'lines' attribute. May be null if the 'triangles' parameter is not null.
     * @param lineWidth The width of the lines of the shape.
     */

    public GLShapeCV(String id, GLLineCV lines[], float lineWidth) {
        this(id,null,lines,lineWidth);
    }

    /**
     * The constructor will prepare the OpenGL code to be executed for this shape with the corresponding attribute values ('vertexBuffer' etc.).
     * The OpenGL code is not compiled by the constructor (which would not work that early)
     * but by a later separate call of initOpenGLProgram() from the onSurfaceCreated() method of a corresponding renderer.
     * The constructor will also prepare the model matrix from the corresponding scaling, translation, and rotation matrix attributes.
     * @param id The ID of the shape.
     * @param triangles The triangles for the shape. Clones of these triangles will be assigned to the 'triangles' attribute. May be null if the 'lines' parameter is not null.
     * @param lines The lines for the shape. A clone of this array will be assigned to the 'lines' attribute. May be null if the 'triangles' parameter is not null.
     * @param lineWidth The width of the lines of the shape.
     */

    public GLShapeCV(String id, GLTriangleCV triangles[], GLLineCV[] lines, float lineWidth) {

        this.id = new String(id);

        // prepare the matrices, the rotation axis and the rotation angle

        modelMatrix = new float[16];
        scalingMatrix = new float[16];
        rotationMatrix = new float[16];
        translationMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix,0);
        Matrix.setIdentityM(scalingMatrix,0);
        Matrix.setIdentityM(rotationMatrix,0);
        Matrix.setIdentityM(translationMatrix,0);

        // set the triangles building this shape

        if (triangles!=null) {
            this.triangles = new GLTriangleCV[triangles.length];
            for (int i = 0; i < triangles.length; i++)
                if (triangles[i] != null)
                    this.triangles[i] = triangles[i].clone();
        }

        // set the lines building this shape

        if (lines!=null) {
            this.lines = new GLLineCV[lines.length];
            for (int i = 0; i < lines.length; i++)
                if (lines[i] != null)
                    this.lines[i] = lines[i].clone();
        }

        this.lineWidth = lineWidth;

        // prepare the list of animators

        animators = new ArrayList<Animator>();

        // set the model matrix from the scaling, translation, and rotation attributes
        // and the buffers from with the coordinates and color values will be transferred to the hardware

        setModelMatrixAndBuffers();

    }

    /**
     * Makes a deep copy of this shape, i.e. makes a new shape with copies of all the triangles and lines.
     * @param id The id of the new shape.
     * @return A reference to the new shape.
     */

    synchronized public GLShapeCV copy(String id) {
        return new GLShapeCV(id,triangles,lines,lineWidth);
    }

    /**
     * Auxiliary method to set the model matrix and the buffers based on the triangles and lines of this shape.
     * Must be called when the set of triangles is initialized (i.e. from an GLShapeCV constructor)
     * or changes (i.e. from the methods addTriangle(), addTriangles(), setTriangleVertex(), and moveCenterTo())
     */

    synchronized private void setModelMatrixAndBuffers() {

        // build the ModelMatrix 'modelMatrix' from the scaling, translation, and rotation matrix attributes.

        buildModelMatrix();

        // prepare the buffers with the vertex coordinates

        final int BYTES_PER_FLOAT = 4;

        if (triangles!=null) {
            float[] triangleCoordinates = coordinateArrayFromTriangles();
            ByteBuffer bbTri = ByteBuffer.allocateDirect(triangleCoordinates.length * BYTES_PER_FLOAT);
            bbTri.order(ByteOrder.nativeOrder());
            triangleVerticesBuffer = bbTri.asFloatBuffer();
            triangleVerticesBuffer.put(triangleCoordinates);
            triangleVerticesBuffer.position(0);
        }

        if (lines!=null) {
            float[] linesCoordinates = coordinateArrayFromLines();
            ByteBuffer bbLin = ByteBuffer.allocateDirect(linesCoordinates.length * BYTES_PER_FLOAT);
            bbLin.order(ByteOrder.nativeOrder());
            lineEndsBuffer = bbLin.asFloatBuffer();
            lineEndsBuffer.put(linesCoordinates);
            lineEndsBuffer.position(0);
        }

        // determine the coloring type:
        // - if there exist triangles: the coloring type of the first triangle (assuming that all triangles have the same coloring type)
        // - if there exist lines but no triangles: COLORING_UNIFORM
        // (note that the current version of this class draws no lines if there exist textured triangles.)

        if (triangles!=null)
            coloringType = triangles[0].getColoringType();
        else
            coloringType = GLPlatformCV.COLORING_UNIFORM;

        if (triangles!=null) {
            // set colors or textures for the triangles
            switch (coloringType) {
                case GLPlatformCV.COLORING_UNIFORM:
                    // TODO: Hier auf Basis des Codes von GLPlatformCV.vertexShaderUniform programmieren, sobald dieser funktioniert
                case GLPlatformCV.COLORING_VARYING:
                    float[] triangleColors = colorArrayFromTriangles();
                    ByteBuffer bbColTriangles = ByteBuffer.allocateDirect(triangleColors.length * BYTES_PER_FLOAT);
                    bbColTriangles.order(ByteOrder.nativeOrder());  // native byte order of the device
                    triangleColorsBuffer = bbColTriangles.asFloatBuffer();
                    triangleColorsBuffer.put(triangleColors);
                    triangleColorsBuffer.position(0);  // set read index to the first buffer element
                    break;
                case GLPlatformCV.COLORING_TEXTURED:
                    int uvIndex = 0;
                    uvCoordinates = new float[triangles.length * 6];
                    for (GLTriangleCV triangle : triangles) {
                        float[] uvCoordinatesTriangle = triangle.getUvCoordinates();
                        for (int i = 0; i < 6; i++)
                            uvCoordinates[uvIndex++] = uvCoordinatesTriangle[i];
                    }
                    ByteBuffer bbUV = ByteBuffer.allocateDirect(uvCoordinates.length * BYTES_PER_FLOAT);
                    bbUV.order(ByteOrder.nativeOrder());
                    uvBuffer = bbUV.asFloatBuffer();
                    uvBuffer.put(uvCoordinates);
                    uvBuffer.position(0);
                    textureBitmaps = new Bitmap[triangles.length];
                    for (int i = 0; i < triangles.length; i++)
                        textureBitmaps[i] = triangles[i].getTexture();
                    textureNames = new int[textureBitmaps.length];
                    break;
            }

        }

        if (lines!=null&&coloringType!=GLPlatformCV.COLORING_TEXTURED) {
            // set colors for the lines
            float[] lineColors = colorArrayFromLines();
            ByteBuffer bbColLines = ByteBuffer.allocateDirect(lineColors.length * BYTES_PER_FLOAT);
            bbColLines.order(ByteOrder.nativeOrder());
            lineColorsBuffer = bbColLines.asFloatBuffer();
            lineColorsBuffer.put(lineColors);
            // for (int i=0; i<lineColors.length/4; i++)
            //     Log.v("DEMO","--------- "+lineColors[i*4]+" "+lineColors[i*4+1]+" "+lineColors[i*4+2]+" "+lineColors[i*4+3]+" ");
            lineColorsBuffer.position(0);
        }

    }

    /**
     * To set the shaders and to compile and link the OpenGL program from these shader coders.
     * This method will be called from the onSurfaceCreated() method of the renderer that shall render the shade.
     * An earlier call (esp. from the shape constructor) will lead to an OpenGL link and/or compile error.
     * For textured shapes, always to be called together with initOpenGLProgram().
     */

    synchronized public void initOpenGLProgram() {

        switch (coloringType) {
            case GLPlatformCV.COLORING_UNIFORM:
                // TODO: Hier Code von GLPlatformCV.vertexShaderUniformColor zuweisen, sobald er funktioniert
                // vertexShaderCode = GLPlatformCV.vertexShaderUniformColor;
                // fragmentShaderCode = GLPlatformCV.fragmentShaderUniformColor;
                // break;
            case GLPlatformCV.COLORING_VARYING:
                vertexShaderCode = GLPlatformCV.vertexShaderVaryingColor;
                fragmentShaderCode = GLPlatformCV.fragmentShaderVaryingColor;
                break;
            case GLPlatformCV.COLORING_TEXTURED:
                vertexShaderCode = GLPlatformCV.vertexShaderTextured;
                fragmentShaderCode = GLPlatformCV.fragmentShaderTextured;
                break;
            default:
                return;
        }

        // create OpenGL shaders

        int vertexShader = GLPlatformCV.loadShader(GLES20.GL_VERTEX_SHADER,this.vertexShaderCode);
        int fragmentShader = GLPlatformCV.loadShader(GLES20.GL_FRAGMENT_SHADER,this.fragmentShaderCode);

        // create the program

        openGLprogram = GLES20.glCreateProgram();
        GLES20.glAttachShader(openGLprogram, vertexShader);
        GLES20.glAttachShader(openGLprogram, fragmentShader);
        GLES20.glLinkProgram(openGLprogram);

        // debug information to see if the OpenGL code has been linked successfully
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(openGLprogram, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0]==1) {
            // Log.v("GLDEMO", ">>> Linking successful");
            isCompiled = true;
        }
        else {
            // Log.v("GLDEMO2", ">>> Linking error: " + GLES20.glGetProgramInfoLog(openGLprogram));
        }

    }

    /**
     * To prepare the textures (only for textured shapes). Always to be called together with initOpenGLProgram().
     */

    synchronized public void prepareTextures() {

        if (coloringType==GLPlatformCV.COLORING_TEXTURED) {
            GLES20.glGenTextures(textureNames.length, textureNames, 0);
            for (int i = 0; i < textureBitmaps.length; i++) {
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[i]);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, textureBitmaps[i], 0);
            }
        }

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
     * Gets an array with copies of all triangles of the shape.
     * @return An array with copies of the triangles.
     */

    synchronized public GLTriangleCV[] getTriangles() {
        // long start = System.nanoTime();
        if (triangles==null) return null;
        GLTriangleCV[] trianglesCopy = new GLTriangleCV[triangles.length];
        for (int i=0; i<triangles.length; i++)
            trianglesCopy[i] = triangles[i].clone();
        // long duration = System.nanoTime() - start;
        // Log.v("GLDEMO",">>> getTriangles: "+duration+" ns");
        return trianglesCopy;
    }

    /**
     * Gets a reference to the array with the triangles of the shape.
     * @return The reference to the array with the triangles.
     */

    synchronized public GLTriangleCV[] getTrianglesNoCopy() {
        return triangles;
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
     * Adds copies of triangles to the shape.
     * @param newTriangles The triangles to be added.
     */

    synchronized public void addTriangles(GLTriangleCV[] newTriangles) {
        addTriangles(newTriangles,true);
    }

    /**
     * Adds triangles to the shape.
     * @param newTriangles The triangles to be added.
     * @param makeCopies Specifies if the triangles of 'newTriangles' shall be copied before adding them.
     */

    synchronized public void addTriangles(GLTriangleCV[] newTriangles, boolean makeCopies) {
        if (newTriangles==null||newTriangles.length==0) return;
        if (triangles==null) {
            if (makeCopies)
                triangles = (GLTriangleCV[]) newTriangles.clone();
              else
                triangles = newTriangles;
            setModelMatrixAndBuffers();
            return;
        }
        GLTriangleCV[] newTriangleAttribute = new GLTriangleCV[triangles.length+newTriangles.length];
        for (int i=0; i<triangles.length; i++)
            newTriangleAttribute[i] = triangles[i];
        for (int i=0; i<newTriangles.length; i++)
            if (makeCopies)
                newTriangleAttribute[triangles.length+i] = newTriangles[i].clone();
              else
                newTriangleAttribute[triangles.length+i] = newTriangles[i];
        triangles = newTriangleAttribute;
        setModelMatrixAndBuffers();
    }

    /**
     * Sets the values of a vertex of a triangle of the shape.
     * TODO In the current implementation, the model matrix and buffers are updated each time this method is called.
     * This is very inefficient if multiple triangle vertices are modified directly on after the other e.g. in a morphing animation.
     * The code should be adapted accordingly such that there will be only one update operation.
     * @param triangleID The ID of the triangle.
     * @param vertexNo The number of the triangle vertex (0, 1, or 2).
     * @param values the new coordinate values of the triangle (x, y, and z).
     * @return true if the operation was successful; false if there is no triangle with such ID or one of the two other parameters is not correct.
     */

    synchronized public boolean setTriangleVertex(String triangleID, int vertexNo, float[] values) {
        for (GLTriangleCV triangle : triangles)
            if (triangle.getId().equals(triangleID)) {
                if (!triangle.setVertex(vertexNo,values)) return false;
                setModelMatrixAndBuffers();
                return true;
            }
        return false;
    }

    /**
     * Gets an array with copies of all lines of the shape.
     * @return The array with the lines.
     */

    synchronized public GLLineCV[] getLines() {
        if (lines==null) return null;
        GLLineCV[] linesCopy = new GLLineCV[lines.length];
        for (int i=0; i<lines.length; i++)
            linesCopy[i] = lines[i].clone();
        return linesCopy;
    }

    /**
     * Adds a line to the shape.
     * If the shape had no lines before, the lineWidth is set to 10.
     * @param newLine The line to be added.
     */

    synchronized public void addLine(GLLineCV newLine) {
        if (newLine==null) return;
        GLLineCV newLines[] = { newLine };
        addLines(newLines);
    }

    /**
     * Adds lines to the shape.
     * If the shape had no lines before, the lineWidth is set to 10.
     * @param newLines The lines to be added
     */

    synchronized public void addLines(GLLineCV[] newLines) {
        if (newLines==null||newLines.length==0) return;
        if (lines==null) {
            lines = newLines;
            lineWidth = 10;
            setModelMatrixAndBuffers();
            return;
        }
        GLLineCV[] newLinesAttribute = new GLLineCV[lines.length+newLines.length];
        for (int i=0; i<lines.length; i++)
            newLinesAttribute[i] = lines[i];
        for (int i=0; i<newLines.length; i++)
            newLinesAttribute[lines.length+i] = newLines[i];
        lines = newLinesAttribute;
        setModelMatrixAndBuffers();
    }

    /**
     * @param lineWidth The new line width (must be larger than 0)
     */

    synchronized void setLineWidth(float lineWidth) {
        if (lineWidth<=0) return;
        this.lineWidth = lineWidth;
    }

    /**
     * @return The line width of the shape
     */

    synchronized float getLineWidth() {
        return this.lineWidth;
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
        // Log.v("GLDEMO","moveCenterTo: "+transX+" "+transY+" "+transZ);
        if (triangles!=null)
            for (GLTriangleCV triangle: triangles)
                triangle.translate(-transX,-transY,-transZ);
        if (lines!=null)
            for (GLLineCV line: lines)
                line.translate(-transX,-transY,-transZ);
        setModelMatrixAndBuffers();
    }

    /**
     * Gets the maximum difference between the x coordinates of any two triangle or line vertices,
     * i.e. the extension of the enclosing cube in the x dimension.
     * The value refers to the local coordinate system (model coordinate system),
     * i.e. disregards the transformations specified by the model matrix.
     * @return The extension of the shape in the x dimension.
     */

    synchronized public float getIntrinsicSizeX() {
        return getIntrinsicSize(0);
    }

    /**
     * Gets the maximum difference between the y coordinates of any two triangle or line vertices,
     * i.e. the extension of the enclosing cube in the y dimension.
     * The value refers to the local coordinate system (model coordinate system),
     * i.e. disregards the transformations specified by the model matrix attributes scaleY etc.
     * @return The extension of the shape in the y dimension.
     */

    synchronized public float getIntrinsicSizeY() {
        return getIntrinsicSize(1);
    }

    /**
     * Gets the maximum difference between the z coordinates of any two triangle or line vertices,
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
        float min=Float.MAX_VALUE, max=Float.MIN_VALUE;
        if (triangles!=null) {
            for (GLTriangleCV triangle : triangles) {
                float[][] vertices = triangle.getVertices();
                for (int i = 0; i < 3; i++) {
                    if (vertices[i][dimension] < min)
                        min = vertices[i][dimension];
                    if (vertices[i][dimension] > max)
                        max = vertices[i][dimension];
                }
            }
        }
        if (lines!=null) {
            for (int i=0;i<lines.length;i++) {
                if (lines[i].getPoint1()[dimension]<min)
                    min = lines[i].getPoint1()[dimension];
                if (lines[i].getPoint1()[dimension]>max)
                    max = lines[i].getPoint1()[dimension];
                if (lines[i].getPoint2()[dimension]<min)
                    min = lines[i].getPoint1()[dimension];
                if (lines[i].getPoint2()[dimension]>max)
                    max = lines[i].getPoint2()[dimension];
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
        buildModelMatrix();
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
        buildModelMatrix();
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
        buildModelMatrix();
        return this;
    }

    synchronized public float getScaleZ() {
        return scalingMatrix[10];
    }

    /**
     * Sets the attributes for the scaling factors and updates the model matrix accordingly.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     */

    synchronized public GLShapeCV setScale(float scaleX, float scaleY, float scaleZ) {
        scalingMatrix[0] = scaleX;
        scalingMatrix[5] = scaleY;
        scalingMatrix[10] = scaleZ;
        buildModelMatrix();
        // Log.v("GLDEMO","setScale: "+scaleX+" "+scaleY+" "+scaleZ);
        return this;
    }

    /**
     * Sets the scaling factors for all three dimensions to the same value and updates the model matrix accordingly.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     */

    synchronized public GLShapeCV setScale(float scale) {
        return setScale(scale,scale,scale);
    }

    /**
     * Sets the rotation based on three cardan angles / Euler angles, i.e. modifies the rotationMatrix attribute accordingly.
     * (Euler angles see e.g. https://en.wikipedia.org/wiki/Euler_angles, "Tate-Bryan angles" / "cardan angles".)
     * The rotation order is X > Z > Y. Note that this order is fixed because rotation operations are not commutative.
     * <BR>
     * The implementation is based on the code in
     * https://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToMatrix/index.htm.
     * @param eulerAngles Array of length 3 with the rotation angles at positions 0, 1, and 2
     *                    relative to the x-, y-, and z-axes of the model coordinate system (i.e. the coordinate system of the shape).
     * @return The shape itself, such that calls of methods of this kind can be daisy chained, or null if the parameter is not valid.
     */

    synchronized public GLShapeCV setRotationByEulerAngles(float[] eulerAngles) {
        if (eulerAngles==null||eulerAngles.length!=3)
            return null;
        return setRotationByEulerAngles(eulerAngles[0],eulerAngles[1],eulerAngles[2]);
    }

    /**
     * Sets the rotation based on three cardan angles / Euler angles, i.e. modifies the rotationMatrix attribute accordingly.
     * (Euler angles see e.g. https://en.wikipedia.org/wiki/Euler_angles, "Tate-Bryan angles" / "cardan angles".)
     * The rotation order is X > Z > Y. Note that this order is fixed because rotation operations are not commutative.
     * <BR>
     * The implementation is based on the code in
     * https://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToMatrix/index.htm.
     * @param eulerX rotation around the x axis in the model coordinate system (i.e. the coordinate system of the shape).
     * @param eulerY subsequent rotation around the y axis in the model coordinate system (i.e. the coordinate system of the shape).
     * @param eulerZ subsequent rotation around the z axis in the model coordinate system (i.e. the coordinate system of the shape).
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     */

    synchronized public GLShapeCV setRotationByEulerAngles(float eulerX, float eulerY, float eulerZ) {

        /* float cosX = (float) Math.cos(Math.PI*eulerX/180.0);
        float sinX = (float) Math.sin(Math.PI*eulerX/180.0);
        float cosY = (float) Math.cos(Math.PI*eulerY/180.0);
        float sinY = (float) Math.sin(Math.PI*eulerY/180.0);
        float cosZ = (float) Math.cos(Math.PI*eulerZ/180.0);
        float sinZ = (float) Math.sin(Math.PI*eulerZ/180.0);
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
        rotationMatrix[15] = 1.0f; */

        rotationMatrix = GraphicsUtilsCV.rotationMatrixFromEulerAngles(eulerX,eulerY,eulerZ);
        buildModelMatrix();
        return this;
    }

    /**
     * Sets the rotation angle and the rotation matrix, i.e. modifies the rotationMatrix attribute accordingly.
     * <BR>
     * https://registry.khronos.org/OpenGL-Refpages/gl2.1/xhtml/glRotate.xml:
     * "This rotation follows the right-hand rule, so if the vector (= rotation axis) points toward the user, the rotation will be counterclockwise."
     * @param rotAngle The rotation angle to be set.
     * @param rotAxis The rotation axis to be set.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     */

    synchronized public GLShapeCV setRotation(float rotAngle, float[] rotAxis) {
        return setRotation(rotAngle,rotAxis[0],rotAxis[1],rotAxis[2]);
    }

    /**
     * Sets the rotation angle and the rotation matrix, i.e. modifies the rotationMatrix attribute accordingly.
     * <BR>
     * https://registry.khronos.org/OpenGL-Refpages/gl2.1/xhtml/glRotate.xml:
     * "This rotation follows the right-hand rule, so if the vector (= rotation axis) points toward the user, the rotation will be counterclockwise."
     * @param rotAngle The rotation angle to be set.
     * @param rotAxisX x component of the rotation axis to be set.
     * @param rotAxisY y component of the rotation axis to be set.
     * @param rotAxisZ z component of the rotation axis to be set.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     */

    synchronized public GLShapeCV setRotation(float rotAngle, float rotAxisX, float rotAxisY, float rotAxisZ) {
        Matrix.setRotateM(rotationMatrix,0,rotAngle,rotAxisX,rotAxisY,rotAxisZ);
        // Log.v("GLDEMO",rotAngle+" "+rotAxis[0]+" "+rotAxis[1]+" "+rotAxis[2]);
        buildModelMatrix();
        return this;
    }

    /* NOT NEEDED ANYMORE
     * Sets the rotation angle and the rotation matrix, i.e. modifies the rotationMatrix attribute accordingly.
     * This method is primarily to be used by the rotation animator (see GLAnimatorFactorCV.addAnimatorRot())
     * which requires a method with single parameter.
     * <BR>
     * https://registry.khronos.org/OpenGL-Refpages/gl2.1/xhtml/glRotate.xml:
     * "This rotation follows the right-hand rule, so if the vector (= rotation axis) points toward the user, the rotation will be counterclockwise."
     * @param rotAngleAndAxis The rotation angle (in position 0) and axis (in positions 1-3).
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.

    synchronized public GLShapeCV setRotationForAnimator(float[] rotAngleAndAxis) {
        // Log.v("GLDEMO",">>> "+id+": setRotationForAnimator");
        float[] rotAxis = new float[3];
        rotAxis[0] = rotAngleAndAxis[1];
        rotAxis[1] = rotAngleAndAxis[2];
        rotAxis[2] = rotAngleAndAxis[3];
        return setRotation(rotAngleAndAxis[0],rotAxis);
    }

    /*
    synchronized public GLShapeCV setRotationForAnimatorVs2(float angle, float axisX, float axisY, float axisZ) {
        Log.v("GLDEMO",">>> "+id+": setRotationForAnimatorVs2");
        float[] rotAxis = new float[3];
        rotAxis[0] = axisX;
        rotAxis[1] = axisY;
        rotAxis[2] = axisZ;
        return setRotation(angle,rotAxis);
    }
     */

    /**
     * Sets the rotation matrix, i.e. the rotationMatrix attribute.
     * @param rotationMatrix The values for the rotation matrix as required by OpenGL (i.e. an array of length 16).
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     * (null if the parameter is not valid, i.e. null or an array with length not equal 16 or no rotation matrix).
     */

    synchronized public GLShapeCV setRotationMatrix(float[] rotationMatrix) {
        return setRotationMatrix(rotationMatrix,true);
    }

    /**
     * Sets the rotation matrix, i.e. the rotationMatrix attribute.
     * @param rotationMatrix The values for the rotation matrix as required by OpenGL (i.e. an array of length 16).
     * @param matrixCheck Specifies whether it shall be checked that rotationMatrix is indeed a rotation matrix.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     * (null if the rotationMatrix parameter is not valid, i.e. null or an array with length not equal 16 or, if matrixCheck is set, is no rotationMatrix).
     */

    synchronized public GLShapeCV setRotationMatrix(float[] rotationMatrix, boolean matrixCheck) {
        if (matrixCheck&&!GraphicsUtilsCV.is4x4RotationMatrix(GraphicsUtilsCV.matrixFromArray(rotationMatrix,4,4))) {
            Log.v("GLDEMO","Error "+id+": This is no rotation matrix!");
            return null;
        }
        this.rotationMatrix = rotationMatrix.clone();
        buildModelMatrix();
        return this;
    }

    /**
     * Gets the rotation axis.
     * @return The rotation axis calculated from the rotationMatrix attribute.
     */

    synchronized public float[] getRotAxis() {
        return GraphicsUtilsCV.rotAxisFrom4x4RotationMatrix(GraphicsUtilsCV.matrixFromArray(rotationMatrix,4,4));
    }

    /**
     * Gets the rotation angle.
     * @return The rotation angle calculated from the rotationMatrix attribute.
     */

    synchronized public float getRotAngle() {
        return GraphicsUtilsCV.rotAngleFrom4x4RotationMatrix(GraphicsUtilsCV.matrixFromArray(rotationMatrix,4,4));
    }

    /**
     * Gets a copy of the rotation matrix, i.e. the rotationMatrix attribute.
     * @return The copy of the rotation matrix.
     */

    synchronized public float[] getRotationMatrix() {
        return rotationMatrix.clone();
    }

    /**
     * Adds a rotation around the shape's own x axis to the current rotation,
     * i.e. takes the current orientation of the shape in world space and lets it flip ("pitch") up or down.
     * @param angle The angle by which the shape shall be flipped/pitched.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     */

    synchronized public GLShapeCV addRotationAroundOwnXAxis(float angle) {
        if (angle==0) return this;
        float[] xAxis = {1, 0, 0, 0};
        Matrix.multiplyMV(xAxis, 0, rotationMatrix, 0, xAxis, 0);
        float[] extraRotMatrix = new float[16];
        Matrix.setRotateM(extraRotMatrix, 0, angle, xAxis[0], xAxis[1], xAxis[2]);
        Matrix.multiplyMM(rotationMatrix, 0, extraRotMatrix, 0, rotationMatrix, 0);
        buildModelMatrix();
        return this;
    }

    /**
     * Adds a rotation around the shape's own y axis to the current rotation,
     * i.e. takes the current orientation of the shape in world space and lets it turn ("yaw") left or right.
     * @param angle The angle by which the shape shall be turned/yawed.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     */

    synchronized public GLShapeCV addRotationAroundOwnYAxis(float angle) {
        if (angle==0) return this;
        float[] yAxis = {0, 1, 0, 0};
        Matrix.multiplyMV(yAxis, 0, rotationMatrix, 0, yAxis, 0);
        float[] extraRotMatrix = new float[16];
        Matrix.setRotateM(extraRotMatrix, 0, angle, yAxis[0], yAxis[1], yAxis[2]);
        Matrix.multiplyMM(rotationMatrix, 0, extraRotMatrix, 0, rotationMatrix, 0);
        buildModelMatrix();
        return this;
    }

    /**
     * Adds a rotation around the shape's own y axis to the current rotation,
     * i.e. takes the current orientation of the shape in world space and lets it flip ("roll") left or right.
     * @param angle The angle by which the shape shall be flipped/rolled.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     */

    synchronized public GLShapeCV addRotationAroundOwnZAxis(float angle) {
        if (angle==0) return this;
        float[] zAxis = {0, 0, 1, 0};
        Matrix.multiplyMV(zAxis, 0, rotationMatrix, 0, zAxis, 0);
        float[] extraRotMatrix = new float[16];
        Matrix.setRotateM(extraRotMatrix, 0, angle, zAxis[0], zAxis[1], zAxis[2]);
        Matrix.multiplyMM(rotationMatrix, 0, extraRotMatrix, 0, rotationMatrix, 0);
        buildModelMatrix();
        return this;
    }

    /**
     * Sets the value of the 'transX' attribute and updates the model matrix accordingly.
     * @param transX The new value for the attribute.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     */

    synchronized public GLShapeCV setTransX(float transX) {
        translationMatrix[12] = transX;
        buildModelMatrix();
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
        buildModelMatrix();
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
        buildModelMatrix();
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
        buildModelMatrix();
        return this;
    }

    /**
     * Sets the values of the translation attributes and updates the model matrix accordingly.
     * @param trans The new values for the attributes (trans[0] = transX, trans[1] = transY, trans[2] = transZ).
     * @return The shape itself, such that calls of methods of this kind can be daisy chained.
     */

    synchronized public GLShapeCV setTrans(float trans[]) {
        // Log.v("GLDEMO","setTrans: "+trans[0]+" "+trans[1]+" "+trans[2]);
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

    /**
     * Method to align the shape with a given vector,
     * i.e. to rotate it such that its x, y, or z axis lies in parallel with the vector.
     * @param axisToAlign The axis of the shape that shall be aligned with the vector - 0 = x axis, 1 = y axis, 2 = z axis
     * @param vector The vector to align the shape with.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained (or null if one of the parameters is not valid).
     */

    synchronized public GLShapeCV alignWith(int axisToAlign, float[] vector) {
        return alignWith(axisToAlign,vector,false,0);
    }

    /**
     * Method to align the shape with a given vector,
     * i.e. to rotate it such that its x, y, or z axis lies in parallel with the vector.
     * It might be that after the alignment a.) the shape points into the "wrong" direction
     * and b.) an additional rotation around the aligned axis is required in order to balance the shape.
     * This can be handled by using the third and the fourth parameter.
     * @param axisToAlign The axis of the shape that shall be aligned with the vector - 0 = x axis, 1 = y axis, 2 = z axis
     * @param vector The vector to align the shape with.
     * @param flip Indicates whether the shape shall be turned by 180 degrees because it points into the "wrong" direction.
     * @param extraRot The angle for an additional rotation around the aligned axis.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained (or null if one of the parameters is not valid).
     */

    synchronized public GLShapeCV alignWith(int axisToAlign, float[] vector, boolean flip, float extraRot ) {
        if (vector==null||vector.length!=3||
                (vector[0]==0&&vector[1]==0&&vector[2]==0)
                || axisToAlign<0 || axisToAlign>2) return null;
        float[] axisToAlignVector = new float[3];
        axisToAlignVector[axisToAlign] = 1;
        float[] vectorNormalized = GraphicsUtilsCV.getNormalizedCopy(vector);
        float[] rotAxis = GraphicsUtilsCV.crossProduct(axisToAlignVector,vectorNormalized);
        float rotAngle = (float)(180*Math.acos(GraphicsUtilsCV.dotProduct(axisToAlignVector,vectorNormalized))/Math.PI);
        if (!GraphicsUtilsCV.valuesEqual(rotAngle,0,0.0001)) {
            float[] rotMatrix = new float[16];
            Matrix.setRotateM(rotMatrix, 0, rotAngle, rotAxis[0], rotAxis[1], rotAxis[2]);
            setRotationMatrix(rotMatrix);
        }
        if (flip)
            switch (axisToAlign) {
                case 0:
                case 2: addRotationAroundOwnYAxis(180); break;
                case 1: addRotationAroundOwnXAxis(180); break;
            }
        if (extraRot!=0)
            switch (axisToAlign) {
                case 0: addRotationAroundOwnXAxis(extraRot); break;
                case 1: addRotationAroundOwnYAxis(extraRot); break;
                case 2: addRotationAroundOwnZAxis(extraRot); break;
            }
        return this;
    }

    /**
     * Method to align the shape with a given vector,
     * i.e. to rotate it such that its x, y, or z axis lies in parallel with the vector.
     * @param axisToAlign The axis of the shape that shall be aligned with the vector - 0 = x axis, 1 = y axis, 2 = z axis
     * @param vector The vector to align the shape with.
     * @param extraRotX An extra rotation of the shape around its own x axis, applied after the alignment rotation.
     * @param extraRotY An extra rotation of the shape around its own y axis, applied after the alignment rotation and the extra x rotation.
     * @param extraRotZ An extra rotation of the shape around its own z axis, applied after the alignment rotation and the extra x and y rotations.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained (or null if one of the parameters is not valid).
     */

    synchronized public GLShapeCV alignWith(int axisToAlign, float[] vector, float extraRotX, float extraRotY, float extraRotZ ) {
        if (vector==null||vector.length!=3||
                (vector[0]==0&&vector[1]==0&&vector[2]==0)
                || axisToAlign<0 || axisToAlign>2) return null;
        float[] axisToAlignVector = new float[3];
        axisToAlignVector[axisToAlign] = 1;
        float[] vectorNormalized = GraphicsUtilsCV.getNormalizedCopy(vector);
        float[] rotAxis = GraphicsUtilsCV.crossProduct(axisToAlignVector,vectorNormalized);
        float rotAngle = (float)(180*Math.acos(GraphicsUtilsCV.dotProduct(axisToAlignVector,vectorNormalized)/Math.PI));
        if (!GraphicsUtilsCV.valuesEqual(rotAngle,0,0.0001)) {
            float[] rotMatrix = new float[16];
            Matrix.setRotateM(rotMatrix, 0, rotAngle, rotAxis[0], rotAxis[1], rotAxis[2]);
            setRotationMatrix(rotMatrix);
        }
        addRotationAroundOwnXAxis(extraRotX);
        addRotationAroundOwnYAxis(extraRotY);
        addRotationAroundOwnZAxis(extraRotZ);
        return this;
        /*
        if (extraRotX!=0) {
            float[] xAxis = {1, 0, 0, 0};
            Matrix.multiplyMV(xAxis, 0, rotMatrix, 0, xAxis, 0);
            float[] extraRotMatrixX = new float[16];
            Matrix.setRotateM(extraRotMatrixX, 0, extraRotX, xAxis[0], xAxis[1], xAxis[2]);
            Matrix.multiplyMM(rotMatrix, 0, extraRotMatrixX, 0, rotMatrix, 0);
        }
        if (extraRotY!=0) {
            float[] yAxis = {0, 1, 0, 0};
            Matrix.multiplyMV(yAxis, 0, rotMatrix, 0, yAxis, 0);
            float[] extraRotMatrixY = new float[16];
            Matrix.setRotateM(extraRotMatrixY, 0, extraRotY, yAxis[0], yAxis[1], yAxis[2]);
            Matrix.multiplyMM(rotMatrix, 0, extraRotMatrixY, 0, rotMatrix, 0);
        }
        if (extraRotZ!=0) {
            float[] zAxis = {0, 0, 1, 0};
            Matrix.multiplyMV(zAxis, 0, rotMatrix, 0, zAxis, 0);
            float[] extraRotMatrixZ = new float[16];
            Matrix.setRotateM(extraRotMatrixZ, 0, extraRotZ, zAxis[0], zAxis[1], zAxis[2]);
            Matrix.multiplyMM(rotMatrix, 0, extraRotMatrixZ, 0, rotMatrix, 0);
        }
        */
    }

    /**
     * Method to align the shape with another shape by copying the rotation matrix of that shape.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained (or null if the parameter is null).
     */

    synchronized public GLShapeCV alignWith(GLShapeCV shapeToAlignWith) {
        if (shapeToAlignWith==null) return null;
        setRotationMatrix(shapeToAlignWith.getRotationMatrix());
        return this;
    }

    /**
     * Scales, rotates, and translates the shape such that it will "connect" two points in 3D space,
     * i.e. the bottom of the shape will be mapped to the first point and the top of the shape to the second point.
     * Scaling will be done in the y dimension.
     * Rotation will rotate the shape from the direction defined by the y axis (0,1,0) to the direction defined by the vector between the two points.
     * The method is primarily applicable to prisms, cuboids, and pyramids.
     * @param point1 The first point.
     * @param point2 The second point.
     * @return The shape itself, such that calls of methods of this kind can be daisy chained (or null if one of the parameters is not valid).
     */

    synchronized public GLShapeCV placeBetweenPoints(float[] point1, float[] point2) {
        if (point1==null||point1.length!=3||point2==null||point2.length!=3) return null;
        // scaling in the y dimension
        setScaleY(GraphicsUtilsCV.distance(point1,point2)/getIntrinsicSizeY());
        // rotation of the shape:
        // - current orientation is assumed to be (0,1,0)
        // - shape must be rotated around a vector that is the cross product of the vectors (0,1,0) and (axisPoint1-axisPoint2),
        //   i.e. a vector that is perpendicular to the plane spanned by these two vectors
        //   (Details e.g.: https://stackoverflow.com/questions/69669771/calculate-rotation-to-align-object-with-two-points-in-3d-space)
        float y_axis[] = {0,1,0};
        float vectorBetweenPoints[] = GraphicsUtilsCV.vectorBetweenPoints(point1,point2);
        if (Math.abs(vectorBetweenPoints[0])>10e-5||Math.abs(vectorBetweenPoints[2])>10e-5) {
            // rotate only if the rotation axis is not parallel to the y axis
            float rotAxisForShape[] = GraphicsUtilsCV.crossProduct(y_axis, vectorBetweenPoints);
            float rotAngleForShape = (float) Math.toDegrees(Math.acos(GraphicsUtilsCV.dotProduct(y_axis, GraphicsUtilsCV.getNormalizedCopy(GraphicsUtilsCV.vectorBetweenPoints(point1, point2)))));
            setRotation(rotAngleForShape,rotAxisForShape);
        }
        // center of the shape = the point in the middle between the two given points
        setTrans(GraphicsUtilsCV.midpoint(point1,point2));
        return this;
    }

    /**
     * Builds the model matrix (i.e. the 'modelMatrix' attribute) from the scaling, rotation, and translation matrix attributes of the shape.
     * For details, see the note in the introductory text on the order of transformation operations.
     */

    synchronized private void buildModelMatrix() {
        Matrix.setIdentityM(modelMatrix,0);
        Matrix.multiplyMM(modelMatrix, 0, scalingMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(modelMatrix, 0, rotationMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(modelMatrix, 0, translationMatrix, 0, modelMatrix, 0);
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

    synchronized public void addAnimator(Animator animator) {
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
     * @param vpMatrix The view/projection matrix to be passed by the renderer.
     */

    synchronized public void draw(float[] vpMatrix) {    // Gesamtdauer für einen Würfel mit Kantenlinien: ca. 40-60 Mikrosek. (Zeitmessung 8.6.22)

        // Log.v("GLDEMO",">>>>> draw "+id);

        // set some fundamental constants

        final int COORDS_PER_VERTEX = 3;  // coordinates (3 = three-dimensional space)
        final int COLORS_PER_VERTEX = 4;  // number of color values per vertex (4 = RGBA)
        final int BYTES_PER_FLOAT = 4;
        final int triangleVertexCount = triangles!=null?triangles.length*3:0;    // total number of triangle vertices
        final int lineVertexCount = lines!=null?lines.length*2:0;    // total number of lines vertices

        // use the program defined in the constructor

        GLES20.glUseProgram(openGLprogram);    // ca. 2 Mikrosek. (Zeitmessung 8.6.22)

        // calculate the MVP matrix from the model matrix of the shape and the view/projection matrix from the renderes

        // float[] mvpMatrix = new float[16];

        float[] mvpMatrix = new float[16];     // diese und die nächste Zeile: ca. 2 Mikrosek. (Zeitmessung 8.6.22)

        Matrix.multiplyMM(mvpMatrix, 0, vpMatrix, 0, modelMatrix, 0);

        // pass the MVP matrix to the program        // die nächsten 4 Operationen: ca. 4-5 Mikrosek. (Zeitmessung 8.6.22)

        int mMVPMatrixHandle = GLES20.glGetUniformLocation(openGLprogram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // get and activate a handle for the aPosition attribute of the vertex shader (coordinates of the vertices)

        int positionHandle = GLES20.glGetAttribLocation(openGLprogram, "aPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);

        // draw the triangles

        // long start = System.nanoTime();

        if (triangles!=null&&triangles.length>0) {     // Zeichnen der 12 Dreiecke eines Würfels: ca. 8-10 Mikrosek. (Zeitmessung 8.6.22)
                                                       // zum Vergleich: Zeichen von 96000 Dreiecken: ca. 2 Millisek.
            // connect the 'vertexBuffer' attribute containing the triangle vertex coordinates with the aPosition attribute
            // = pass the triangle coordinates to the graphics hardware

            GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                    GLES20.GL_FLOAT, false,
                    0, triangleVerticesBuffer);

            switch (coloringType) {

                case GLPlatformCV.COLORING_UNIFORM:
                    // TODO: Hier auf Basis des Codes von GLPlatformCV.vertexShaderUniform programmieren, sobald dieser funktioniert
                    // Dann auch den Fall berücksichtigen, dass alle Triangles jeweils eine einheitliche Farbe haben, diese Farben aber unterschiedlich sind
                    // colorHandle = GLES20.glGetUniformLocation(openGLprogram, "vColor");  // für einfarbige Würfel
                    // GLES20.glUniform4fv(colorHandle, 1, colorArray, 0);
                    // break;
                case GLPlatformCV.COLORING_VARYING:
                    int colorHandle = GLES20.glGetAttribLocation(openGLprogram, "aColor");
                    GLES20.glVertexAttribPointer(colorHandle, COLORS_PER_VERTEX, GLES20.GL_FLOAT, false, COLORS_PER_VERTEX*BYTES_PER_FLOAT, triangleColorsBuffer);
                    GLES20.glEnableVertexAttribArray(colorHandle);
                    // draw the shape
                         // long start = System.nanoTime();
                    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, triangleVertexCount);
                         // long duration = System.nanoTime() - start;
                         // Log.v("GLDEMO",">>> "+triangles.length+" triangles "+(duration/1000)+" microsec");
                    // deactivate the attribute arrays
                    GLES20.glDisableVertexAttribArray(positionHandle);
                    GLES20.glDisableVertexAttribArray(colorHandle);
                    break;
                case GLPlatformCV.COLORING_TEXTURED:
                    int textureHandle = GLES20.glGetAttribLocation(openGLprogram, "aTexCoord");
                    // buffer for the uv coordinates
                    ByteBuffer bbUV = ByteBuffer.allocateDirect(uvCoordinates.length * BYTES_PER_FLOAT);
                    bbUV.order(ByteOrder.nativeOrder());
                    uvBuffer = bbUV.asFloatBuffer();
                    uvBuffer.put(uvCoordinates);
                    uvBuffer.position(0);
                    GLES20.glVertexAttribPointer(textureHandle, 2, GLES20.GL_FLOAT, false, 2*BYTES_PER_FLOAT, uvBuffer);
                    GLES20.glEnableVertexAttribArray(textureHandle);
                    for (int i = 0; i < triangles.length; i++) {   // draw the triangles one by one, setting the texture anew for each individual triangle
                        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[i]);
                        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 3 * i, 3);
                    }
                    // disable the vertex array
                    GLES20.glDisableVertexAttribArray(positionHandle);
                    GLES20.glDisableVertexAttribArray(textureHandle);
                    break;
            }

        }

        // long duration = System.nanoTime() - start;
        // Log.v("GLDEMO",">>> "+triangles.length+" triangles "+duration+" ns");

        // draw the lines

        // if (coloringType==GLPlatformCV.COLORING_TEXTURED) return;  // Current version of this class: Lines only for colored triangles.

        if (lines!=null) {        // Zeichnen der Kantenlinien eines Würfels: ca. 7-10 Mikrosek. (Zeitmessung 8.6.22)
            // positionHandle = GLES20.glGetAttribLocation(openGLprogram, "aPosition");
            GLES20.glDisableVertexAttribArray(positionHandle);
            GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                    GLES20.GL_FLOAT, false,
                    0, lineEndsBuffer);
            GLES20.glEnableVertexAttribArray(positionHandle);
            int colorHandle = GLES20.glGetAttribLocation(openGLprogram, "aColor");
            GLES20.glDisableVertexAttribArray(colorHandle);
            GLES20.glVertexAttribPointer(colorHandle, COLORS_PER_VERTEX, GLES20.GL_FLOAT, false, COLORS_PER_VERTEX*BYTES_PER_FLOAT, lineColorsBuffer);
            GLES20.glEnableVertexAttribArray(colorHandle);
            GLES20.glLineWidth(lineWidth);
            GLES20.glDrawArrays(GLES20.GL_LINES, 0, lineVertexCount);
            GLES20.glDisableVertexAttribArray(positionHandle);
            GLES20.glDisableVertexAttribArray(colorHandle);
        }

    }

    /** Auxiliary method to get a one-dimensional float array with the vertex coordinates of the triangles */

    synchronized private float[] coordinateArrayFromTriangles() {
        float coordinateArray[] = new float[triangles.length*9];
        for (int triangleNo = 0; triangleNo<this.triangles.length; triangleNo++) {
            float[] coordsCurrentTriangle = this.triangles[triangleNo].getVertexCoordinates();
            for (int i=0; i<9; i++)
                coordinateArray[triangleNo*9+i] = coordsCurrentTriangle[i];
        }
        return coordinateArray;
    }

    /** Auxiliary method to get a one-dimensional float array with the vertex coordinates of the lines */

    synchronized private float[] coordinateArrayFromLines() {
        float coordinateArray[] = new float[lines.length*6];
        for (int lineNo = 0; lineNo<this.lines.length; lineNo++) {
            for (int i = 0; i < 3; i++)
                coordinateArray[lineNo * 6 + i] = this.lines[lineNo].getPoint1()[i];
            for (int i = 0; i < 3; i++)
                coordinateArray[lineNo * 6 + 3 + i] = this.lines[lineNo].getPoint2()[i];
        }
        return coordinateArray;
    }

    /** Auxiliary method to get a one-dimensional float array with the vertex colors of the triangles */

    synchronized private float[] colorArrayFromTriangles() {
        float colorArray[] = new float[triangles.length * 12];
        for (int i = 0; i < triangles.length; i++)      // all triangles
            for (int j = 0; j < 3; j++)    // all vertices of a triangle
                for (int k = 0; k < 4; k++) {   // RGBA values of a vertex
                    colorArray[i * 12 + j * 4 + k] = triangles[i].getVertexColor(j)[k];
                }
        return colorArray;
    }

    /** Auxiliary method to get a one-dimensional float array with the colors of the lines */

    synchronized private float[] colorArrayFromLines() {
        float colorArray[] = new float[lines.length * 8];
        for (int i = 0; i < lines.length; i++) {      // all lines
            for (int j = 0; j < 4; j++) {   // RGBA values of a line
                colorArray[i * 8 + j] = lines[i].getColor()[j];  // first end point
                colorArray[i * 8 + 4 + j] = lines[i].getColor()[j];  // second end point
            }
        }
        return colorArray;
    }

    // TODO colorArrayFromLines()

}
