/**
 Package with utility classes for programming OpenGL-ES-based applications under Android.
 <UL>
 <P>
 <LI>The fundamental classes are
 <UL>
 <P>
 <LI><I>GLSurfaceViewCV</I> to show 2D and 3D objects ("shapes") on the display
 <P>
 <LI><I>GLRendererCV</I> for renderers associated with GLSurfaceViewCV objects
 <P>
 <LI><I>GLShapeCV</I> for 2D and 3D objects to be displayed
 </UL>
 <P>
 <LI>Companion classes are, among others,
 <UL>
 <P>
 <LI><I>GLTriangleCV</I> for the triangles from which GLSurfaceViewCV objects are built
 <P>
 <LI><I>GLLineCV</I> for the lines from which GLSurfaceViewCV objects are built
 <P>
 <LI><I>GLShapeFactoryCV</I> with static methods to build GLSurfaceViewCV objects of specific forms
 <P>
 <LI><I>GLAnimatorFactoryCV</I> with static methods to build animators for GLSurfaceViewCV objects
 <P>
 <LI><I>GLSceneFactoryCV</I> with static methods to build scenes with multiple GLShapeCV objects
 </UL>
 </UL>
 <P>
 For an activity that uses this package (but is not part of it), the general usage pattern of these classes is this:
 <UL>
 <P>
 <LI>Once, at the beginning:
 <UL>
 <P>
 <LI>Generate a surface view (= object of class <I>GLSurfaceViewCV</I>).
 <P>
 <LI>Associate a renderer (= object of class <I>GLRendererCV</I>) with the surface view.
 <P>
 <LI>Generate new shapes (= objects of class <I>GLShapeCV</I>) and add them to the surface view.
 <P>
 <LI>Show the surface view on the display.
 </UL>
 <P>
 <LI>Dynamically at runtime:
 <UL>
 <P>
 <LI>Add new shapes to the surface view.
 <P>
 <LI>Remove shapes from the surface view.
 </UL>
 </UL>
 <P>
 The resulting overall program structure is this:
 <P>
 <UL>
 <P>
 <LI>An activity generates a surface view, associates a renderer, adds some shapes and displays the surfae view on the display (see above).
 <P>
 <LI>The generated surface view contains a list of the shapes to be displayed
 and an associated renderer that draws and animates these shapes on the display.
 <P>
 <LI>The renderer that is associated with the surface view
 defines the View Projection Matrix to be applied to all shapes,
 i.e. the matrix that specifies the properties of the camera looking at the scene with the shapes
 and the projection of the scene onto the 2D display.
 <P>
 For each shape, the renderer translates and links its OpenGL program.
 This is done once when the shape has been added to the surface view.
 [Implementation details: The renderer calls the initOpenGLProgram() method of the shape
 - either in the onSurfaceCreated() method of the renderer for all those shapes
 that have been added immediately after the creation of the surface view
 or in the onDrawFrame() method of the renderer for all those shapes
 that have been added dynamically at runtime.]
 <P>
 The renderer draws the shapes on the surface view, possibly multiple times for animated shapes.
 [Implementation details: This is done in the onDrawFrame() method of the renderer
 by calling the draw() methods of all shapes registered with the surface view.
 onDrawFrame() will be called repeatedly by the Android runtime,
 i.e. by the thread controlling the surface view.]
 <P>
 <LI>A shape provides this functionality:
 <P>
 <UL>
 <LI>In its constructor, it sets the primitives (lines and triangles, also points in a future implementation)
 of which the shape consists. It also sets the Model Matrix that places the shape into the 3D world of the surface view.
 [Implementation details: The placement into the 3D world is done by rotation, translation, and scaling operations
 as defined by the Model Matrix. The constructor also prepares the buffers to pass the coordinate and color values to the graphics hardware (GPU).]
 <P>
 <LI>
 In its initOpenGLProgram() method, the OpenGL program to be executed is generated.
 The method is called by the renderer once (see above).
 [Implementation details: The method selects the appropriate vertex and fragment shader,
 translates them and links them to a program.]
 <P>
 <LI>
 In its draw() method, the OpenGL program is executed to show the shape on the display.
 The method is called by the renderer as often as required (see above).
 [Implementation details: The method calls glUseProgram() to apply the OpenGL program generated in initOpenGLProgram(),
 transfers the current coordinate and color values the graphics hardware and subsequently makes the required glDrawXXX() calls.
 It also updates the MVP matrix that combines the general View Projection Matrix as defined by the renderer
 and the object-specific Model Matrix placing the shape into the 3D world of the surface view.
 The Model Matrix is specified by the current rotation, scaling, and translation values of the shape
 which will be altered dynamically and automatically by the animators associated with the shape.]
 </UL>
 </UL>
 <P>
 For programming examples and more information have a look at the demo code in main activity in the <I>app</I> module
 and read the documentation of the classes of this <I>opengl_utilities</I> package.
 <P>
 The code is available at https://github.com/CarstenVogt/AndroidOpenGLLib
 <P><HR><BR>
 This work is provided by
 Prof. Dr. Carsten Vogt, Technische Hochschule K&ouml;ln, Fakult&auml;t f&uuml;r Informations-, Medien- und Elektrotechnik, Germany
 <P>
 under GPLv3, the GNU General Public License 3,
 <A HREF="http://www.gnu.org/licenses/gpl-3.0.html">http://www.gnu.org/licenses/gpl-3.0.html</A>.
 @see de.thkoeln.cvogt.android.opengl_utilities.GLSurfaceViewCV
 @see de.thkoeln.cvogt.android.opengl_utilities.GLRendererCV
 @see de.thkoeln.cvogt.android.opengl_utilities.GLShapeCV
 @see de.thkoeln.cvogt.android.opengl_utilities.GLTriangleCV
 @see de.thkoeln.cvogt.android.opengl_utilities.GLLineCV
 @see de.thkoeln.cvogt.android.opengl_utilities.GLShapeFactoryCV
 @see de.thkoeln.cvogt.android.opengl_utilities.GLAnimatorFactoryCV
 @see de.thkoeln.cvogt.android.opengl_utilities.GLSceneFactoryCV
*/

package de.thkoeln.cvogt.android.opengl_utilities;

