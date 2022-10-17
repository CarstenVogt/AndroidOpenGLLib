// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 17.3.2022

package de.thkoeln.cvogt.android.opengl_utilities;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.util.ArrayList;

/**
 * Class to define views on which shapes, i.e. objects of class <I>GLShapeCV</I>, can be rendered.
 * The rendering is done by a renderer, i.e. an object of class <I>GLRendererCV</I>, that is associated with the <I>GLSurfaceViewCV</I> object.
 * The shapes to be rendered are stored in an <I>ArrayList</I> attribute of the <I>GLSurfaceViewCV</I> object.
 * The renderer calls the <I>draw()</I> methods of these shapes.
 * Shaped can be dynamically removed from and added to this list.
 * <P>
 * For surface views that shall react to touches, define a subclass and implement the <I>onTouchEvent()</I> method.
 * <BR>
 * @see de.thkoeln.cvogt.android.opengl_utilities.GLShapeCV
 * @see de.thkoeln.cvogt.android.opengl_utilities.GLRendererCV
 */

public class GLSurfaceViewCV extends GLSurfaceView {

    /** The shapes to render. */

    private ArrayList<GLShapeCV> shapesToRender;

    /** The registered renderer. */

    private GLRendererCV renderer;

    /**
     * With this constructor, the render mode of the view will be set to GLSurfaceView.RENDERMODE_WHEN_DIRTY,
     * i.e. animations controlled by the associated thread will NOT become effective.
     * @param context The context in which the view is created.
     * @param renderer The renderer to be associated with the view.
     */

    public GLSurfaceViewCV(Context context, GLRendererCV renderer) {
        this(context,renderer,true);
    }

    /**
     * @param context The context in which the view is created.
     * @param renderer The renderer to be associated with the view.
     * @param renderOnlyWhenDirty If true the render mode of the view will be set to GLSurfaceView.RENDERMODE_WHEN_DIRTY,
     *                         i.e. if an animation shall be shown this parameter must be false.
     */

    public GLSurfaceViewCV(Context context, GLRendererCV renderer, boolean renderOnlyWhenDirty) {
        super(context);
        // OpenGL ES context: version 2.0
        setEGLContextClientVersion(2);
        // associate a renderer with the view - see extended setRenderer() method below
        setRenderer(renderer);
        if (renderOnlyWhenDirty)
            // Android documentation: "The renderer only renders when the surface is created, or when requestRender() is called."
          setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        // initialize renderer and shapesToRender
        this.renderer = renderer;
        shapesToRender = new ArrayList<>();
    }

    /**
     * Associate a renderer with the view.
     * Note that for renderers of class GLRendererCV THIS method and not the method of the base class GLSurfaceView must be called
     * because it sets the 'surfaceView' attribute of the renderer.
     * @param renderer The renderer to be associated with the view.
     */
    synchronized public void setRenderer(GLRendererCV renderer) {
        super.setRenderer(renderer);
        renderer.setSurfaceView(this);
        this.renderer = renderer;
    }

    /**
     * Get the associated renderer.
     * @return The renderer associated with the view.
     */
    synchronized public GLRendererCV getRenderer() {
        return renderer;
    }

    /**
     * Add another shape that shall be rendered. This will also start the animators defined for the shape.
     * @param shape The shape to be added.
     */

    synchronized public void addShape(GLShapeCV shape) {
        shapesToRender.add(shape);
        shape.setSurfaceView(this);
        shape.startAnimators();
    }

    /**
     * Add some shapes that shall be rendered. This will also start the animators defined for the shapes.
     * @param shapes The shape to be added.
     */

    synchronized public void addShapes(GLShapeCV[] shapes) {
        for (GLShapeCV shape: shapes)
            addShape(shape);
    }

    /**
     * Get the shapes to render.
     * @return A copy of 'shapesToRender', i.e. the list of shapes to be rendered.
     */

    synchronized public ArrayList<GLShapeCV> getShapesToRender() {
        return (ArrayList<GLShapeCV>) shapesToRender.clone();
    }

    /**
     * Remove a shape from the list of shapes to render.
     * @return A copy of 'shapesToRender', i.e. the list of shapes to be rendered.
     */

    synchronized public void removeShape(GLShapeCV shape) {
        shapesToRender.remove(shape);
    }

    /**
     * Empty the list of the shapes to be rendered.
     */

    synchronized public void clearShapes() {
        for (GLShapeCV shape: shapesToRender)
            shape.setSurfaceView(null);
        shapesToRender.clear();
    }

    /**
     * Display the x, y, and z axes (for testing purposes).
     */

    synchronized public void showAxes() {
        addShape(GLShapeFactoryCV.makeAxes());
    }

}
