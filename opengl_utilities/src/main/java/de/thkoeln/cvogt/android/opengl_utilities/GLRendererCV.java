// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 17.3.2022

package de.thkoeln.cvogt.android.opengl_utilities;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Class to define renderers. A renderer is attached to a surface view, i.e. an object of class <I>GLSurfaceViewCV</I>,
 * and calls the <I>draw()</I> methods of the shapes of this view.
 * <P>
 * A renderer also specifies and updates the View Projection Matrix to be applied to all shapes,
 *  i.e. the matrix that specifies the properties of the camera looking at the collection of shapes
 *  and the projection of the shapes onto the 2D display.
 * <BR>
 * @see de.thkoeln.cvogt.android.opengl_utilities.GLSurfaceViewCV
 * @see de.thkoeln.cvogt.android.opengl_utilities.GLShapeCV
 */

public class GLRendererCV implements GLSurfaceView.Renderer {

    /**
     * The associated surface view on which the renderer will draw the shapes. The renderer will get the shapes from this view to call their draw() methods.
     */

    private GLSurfaceViewCV surfaceView;

    /** Initial value for position of the camera: X value. */
    private static final float eyeXInit = 0f;

    /** Initial value for position of the camera: Y value. */
    private static final float eyeYInit = 0f;
    // private static final float eyeYInit = 3f;

    /** Initial value for position of the camera: Z value. */
    private static final float eyeZInit = 5f;

    /** Initial value for the focus of the camera: X value. */
    private static final float centerXInit = 0f;

    /** Initial value for the focus of the camera: Y value. */
    private static final float centerYInit = 0f;
    // private static final float centerYInit = -3f;

    /** Initial value for the focus of the camera: Z value. */
    private static final float centerZInit = 0f;

    /** Current position of the camera: X value. */
    private float eyeX = eyeXInit;

    /** Current position of the camera: Y value. */
    private float eyeY = eyeYInit;

    /** Current position of the camera: Z value. */
    private float eyeZ = eyeZInit;

    /** Current focus of the camera: X value. */
    private float centerX = centerXInit;

    /** Current focus of the camera: Y value. */
    private float centerY = centerYInit;

    /** Current focus of the camera: Z value. */
    private float centerZ = centerZInit;

    /** Frustum: near value. */
    private static final float frustumNear = 1;

    /** Frustum: far value. */
    private static final float frustumFar = 1000;

    /**
     * Projection matrix to project the 3D coordinates to the 2D display. Is set in onSurfaceChanged() based on the display geometry.
     */

    private final float[] projectionMatrix = new float[16];

    /**
     * View projection matrix to be passed to the draw methods of the shapes.
     * <BR>
     * The matrix is the product of the projection matrix and the view matrix,
     * the view matrix being calculated from the camera position (eyeX/Y/Z) and camera focus (centerX/Y/Z).
     * It initialized in onSurfaceChanged() and updated each time the camera position or focus changes.
     */

    private final float[] viewProjectionMatrix = new float[16];

    /**
     * @param surfaceView The surface view to which this renderer shall be attached.
     */

    public void setSurfaceView(GLSurfaceViewCV surfaceView) {
        this.surfaceView = surfaceView;
    }

    /**
     * Get a copy of the current view projection matrix.
     */

    public float[] getViewProjectionMatrix() {
        return viewProjectionMatrix.clone();
    }

    /**
     * Method called by the runtime system when the associated surface view has been initialized.
     * Colors the background black and compiles the OpenGL programs of the shapes to be displayed (i.e. the shapes attached to the associated surface view).
     * Prepares the textures for textured shapes.
     */

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        ArrayList<GLShapeCV> shapesToRender = surfaceView.getShapesToRender();
        for (GLShapeCV shape : shapesToRender) {
            shape.initOpenGLProgram();
            shape.prepareTextures();
        }
    }

    /**
     * Method called by the runtime system when the surface view shall been drawn, i.e. its shapes shall be rendered.
     */

    @Override
    synchronized public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);  // clear the buffers before drawing the shapes
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background color: black
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);  // such that fragments in the front ...
        GLES20.glDepthFunc(GLES20.GL_LESS);     // ... hide fragments in the back
        GLES20.glDepthMask( true );
        // draw the shapes based on the current view projection matrix
        ArrayList<GLShapeCV> shapesToRender = surfaceView.getShapesToRender();
        // start = (new Date()).getTime();
        // long start = System.nanoTime();
        for (GLShapeCV shape : shapesToRender) {
            if (!shape.isCompiled()) {
                shape.initOpenGLProgram();
                shape.prepareTextures();
            }
            shape.draw(viewProjectionMatrix);
        }
        // duration = (new Date()).getTime() - start;
        // long duration = System.nanoTime() - start;
        // Log.v("GLDEMO",">>> Draw "+duration+" ns");
    }

    /**
     * Sets the values for the view matrix.
     */

    synchronized public void setViewMatrixValues(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ) {
        this.eyeX = eyeX; this.eyeY = eyeY; this.eyeZ = eyeZ;
        this.centerX = centerX; this.centerY = centerY; this.centerZ = centerZ;
        updateViewProjectionMatrix();
    }

    /**
     * Calculates the current view matrix from the camera position (eyeX/Y/Z) and camera focus (centerX/Y/Z).
     * Updates the view projection matrix as the product of the projection matrix and the view matrix.
     * Is called initially from onSurfaceChanged() and updated each time the camera position or focus changes.
     */

    synchronized private void updateViewProjectionMatrix() {
        float[] viewMatrix = new float[16];
        Matrix.setLookAtM(viewMatrix, 0,
                eyeX, eyeY, eyeZ,
                centerX, centerY, centerZ,
                // vector specifying the "up direction" as seen from the camera
                0f, 1f, 0f);
        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }

    /**
     * Method called by the runtime system when the geometry of the display changes.
     * Sets the projection matrix.
     */

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(projectionMatrix, 0, -ratio, +ratio, -1, 1, frustumNear, frustumFar);
        updateViewProjectionMatrix();
    }

}
