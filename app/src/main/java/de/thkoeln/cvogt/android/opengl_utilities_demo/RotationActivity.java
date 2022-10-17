// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 25.9.2022

package de.thkoeln.cvogt.android.opengl_utilities_demo;

/**
 * Activity that demonstrates the two different forms of rotation:
 * <BR>
 * 1.) Rotation with reference to the "model coordinate system" / "intrinsic coordinate system" of the rotated shape:
 * The rotation is defined by up to three "Euler angles" / "cardan angles" corresponding to the three dimensions
 * of the coordinate system. The shape is first rotated around its x axis and its model coordinate system is rotated accordingly.
 * Next, the shape is rotated around its current z axis (i.e. the z axis resulting from the previous rotation)
 * and finally around the y axis resulting from these two rotations.
 * Note that rotations are not commutative such that here the rotation order is fixed as X > Z > Y
 * (= pitch>roll>yaw of the airplane shown here).
 * <BR>
 * 2.) Rotation with reference to the "world coordinate system" / "extrinsic coordinate system":
 * Here, the rotation is defined by a rotation matrix and refers to the world coordinate system.
 * Hence, the coordinate system does not rotate together with the shape but remains fixed.
 * <BR>
 * Moreover, the activity illustrates that the implementation of the method Matrix.setRotateEulerM()
 * which should actually be used for case 1.) is faulty with respect to rotations around the y axis.
 * Therefore, for case 1.) a correct version of the Euler rotation method had to be implemented,
 * based on https://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToMatrix/index.htm.
 */

import android.app.Activity;
import android.content.Context;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import de.thkoeln.cvogt.android.opengl_utilities.GLRendererCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLShapeCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLShapeFactoryCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLSurfaceViewCV;
import de.thkoeln.cvogt.android.opengl_utilities.GraphicsUtilsCV;

public class RotationActivity extends Activity {

    private GLSurfaceViewCV glSurfaceView;

    private GLRendererCV renderer;

    private float rotX,rotY,rotZ; // the three rotation angles (in degree format)

    private GLShapeCV shape1, shape2, shape3; // the three shapes to be rotated

    private GLShapeCV axesShape1, axesShape3; // the rotation axes

    // onCreate: display three shapes that can be rotated.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderer = new GLRendererCV();
        glSurfaceView = new GLSurfaceViewCV(this, renderer, false);
        glSurfaceView.clearShapes();
        /*
        Alternative version: rotate three coloured cuboids
        float[][] colors = new float[6][];
        colors[0] = GLShapeFactoryCV.white;
        colors[1] = GLShapeFactoryCV.blue;
        colors[2] = GLShapeFactoryCV.green;
        colors[3] = GLShapeFactoryCV.lightgrey;
        colors[4] = GLShapeFactoryCV.yellow;
        colors[5] = GLShapeFactoryCV.red;
        shape1 = GLShapeFactoryCV.makeCuboid("Shape 1",1,1,3,colors);
        shape2 = GLShapeFactoryCV.makeCuboid("Shape 2",1,1,3,colors);
        shape3 = GLShapeFactoryCV.makeCuboid("Shape 3",1,1,3,colors);
         */
        // Shape 1: Euler / cardan rotation in the model coordinate system (intrinsic coordinate system)
        // by a self-implemented formula - see method rotationMatrixFromEulerAngles_OwnFormula() below
        shape1 = GLShapeFactoryCV.makeAirplane(GLShapeFactoryCV.lightblue,GLShapeFactoryCV.blue);
        axesShape1 = GLShapeFactoryCV.makeAxes();
        // add the axes to the model coordinate system to the shape such that they will rotate together with the shape
        shape1 = GLShapeFactoryCV.joinShapes("",shape1,axesShape1,2f,2f,2f,0,0,0,0,0,0,0,0,0);
        shape1.setTrans(-1.5f,0,-1).setScale(0.25f);
        glSurfaceView.addShape(shape1);
        // Shape 2: Euler / cardan rotation by Matrix.setRotateEulerM() - faulty!
        shape2 = GLShapeFactoryCV.makeAirplane(GLShapeFactoryCV.lightred,GLShapeFactoryCV.red);
        shape2.setTrans(0f,-3f,-1).setScale(0.15f);
        glSurfaceView.addShape(shape2);
        // Shape 3: rotation by a rotation matrix
        // - rotation in the fixed world coordinate space (extrinsic coordinate system)
        shape3 = GLShapeFactoryCV.makeAirplane(GLShapeFactoryCV.lightyellow,GLShapeFactoryCV.orange);
        axesShape3 = GLShapeFactoryCV.makeAxes();
        shape3.setTrans(1.5f,0f,-1).setScale(0.25f);
        glSurfaceView.addShape(shape3);
        axesShape3.setTrans(1.5f,0f,-1).setScale(0.5f);
        glSurfaceView.addShape(axesShape3);
        // set the rotation matrices of the three shapes and render them on the display
        setMatricesAndRender();
        setContentView(glSurfaceView);
        /*
        Toast.makeText(this,
                "Left: Euler rotation - self-implemented formula\n" +
                "Right: Euler rotation - Matrix.setRotateEulerM()\n" +
                "Bottom: Rotation by a matrix ",
                Toast.LENGTH_LONG).show();
         */
    }

    // Auxiliary method to update the rotation matrices of the three shapes and to re-render them.
    // The three approaches used are:
    // - Shape 1 (left): Euler rotation in the model coordinate space by the self-implemented formula in GraphicsUtilsCV.rotationMatrixFromEulerAngles()
    // - Shape 2 (right): Euler rotation in the model coordinate space by Matrix.setRotateEulerM() (faulty for rotations around the y axis!)
    // - Shape 3 (bottom): rotation in the world coordinate space

    private void setMatricesAndRender() {
        // Shape 1
        shape1.setRotationMatrix(GraphicsUtilsCV.rotationMatrixFromEulerAngles(rotX,rotY,rotZ));
        glSurfaceView.addShape(shape1);
        // GraphicsUtilsCV.writeArrayToLog("GLDEMO",shape1.getRotationMatrix());
        // Shape 2
        float[] rotMat = new float[16];
        Matrix.setRotateEulerM(rotMat,0,rotX,rotY,rotZ);
        shape2.setRotationMatrix(rotMat,false);
        // GraphicsUtilsCV.writeArrayToLog("GLDEMO",shape2.getRotationMatrix());
        // Shape 3
        float rotMatX[] = new float[16];
        Matrix.setRotateM(rotMatX,0,-rotX,1,0,0);
        float rotMatY[] = new float[16];
        Matrix.setRotateM(rotMatY,0,-rotY,0,1,0);
        float rotMatZ[] = new float[16];
        Matrix.setRotateM(rotMatZ,0,-rotZ,0,0, 1);
        Matrix.multiplyMM(rotMat,0,rotMatY,0,rotMatX,0);
        Matrix.multiplyMM(rotMat,0,rotMatZ,0,rotMat,0);
        shape3.setRotationMatrix(rotMat);
        // GraphicsUtilsCV.writeArrayToLog("GLDEMO",shape3.getRotationMatrix());
        // Log.v("GLDEMO","---");
    }

    // Method to make an airplane pointing into the negative z direction, i.e. straight away from the camera

    /*
    private GLShapeCV makeAirplane(float[] faceColor, float[] lineColor) {
        final float lengthFuselage = 7;
        final float wingspan = 12;
        final float heightVerticalTail = 4;
        final float lengthVerticalTail = lengthFuselage/2.0f;
        float[][] colors = new float[2][];
        colors[0] = faceColor;
        colors[1] = lineColor;
        // nose
        float[][] colorFront = new float[1][];
        colorFront[0] = colors[0];
        GLShapeCV nose = GLShapeFactoryCV.makeHemisphere("Nose",4,colorFront);
        // cockpit
        float[][] colorCockpit = { GLShapeFactoryCV.lightgrey };
        GLShapeCV cockpit = GLShapeFactoryCV.makeHemisphere("Cockpit",4,colorCockpit);
        // fuselage
        GLShapeCV fuselage = GLShapeFactoryCV.makePrism("Fuselage",32,lengthFuselage,GLShapeFactoryCV.lightblue,GLShapeFactoryCV.lightblue,colors);
        // wings
        float[][] verticesWings = {{0,0,0},{-0.5f*wingspan,0,0.5f*lengthFuselage},{0.5f*wingspan,0,0.5f*lengthFuselage}};
        GLShapeCV wings = GLShapeFactoryCV.makeTriangle("Wings",verticesWings,colors[0],colors[1],10);
        // vertical tail
        float[][] verticesVerticalTail = {{0,0,0.5f*lengthFuselage},{0,0,0.5f*lengthFuselage-lengthVerticalTail},{0,heightVerticalTail,0.6f*lengthFuselage}};
        GLShapeCV verticalTail = GLShapeFactoryCV.makeTriangle("Vertical Tail",verticesVerticalTail,colors[0],colors[1],10);
        GLShapeCV[] airplaneParts = new GLShapeCV[5];
        airplaneParts[0] = nose;
        airplaneParts[1] = cockpit;
        airplaneParts[2] = fuselage;
        airplaneParts[3] = wings;
        airplaneParts[4] = verticalTail;
        float[][] rotationArray = new float[airplaneParts.length][3];
        float[][] translationArray = new float[airplaneParts.length][3];
        translationArray[0][2] = -0.5f*lengthFuselage-0.5f;
        translationArray[1][1] = 0.8f;
        translationArray[1][2] = -0.25f*lengthFuselage;
        rotationArray[0][0] = -90;
        rotationArray[2][0] = 90;
        GLShapeCV airplane = GLShapeFactoryCV.joinShapes("Airplane",airplaneParts,rotationArray,translationArray,0);
        return airplane;
    } */

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    // Starting from here: code for a menu through which two popup windows can be opened.
    // The first popup window shows an explanatory text to the user,
    // through the second window the user can enter values for the rotation angles

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = new MenuInflater(this);
        mi.inflate(R.menu.menu_rotation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menuItemExplain:
                // show popup window with an explanatory text
                final ExplanationPopupWindow pwe = new ExplanationPopupWindow();
                pwe.showAtLocation(glSurfaceView, Gravity.TOP, 0, 0);
                int breiteE = getWindowManager().getDefaultDisplay().getWidth() - 40;
                int hoeheE = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 660, getResources().getDisplayMetrics());
                pwe.update(0, 0, breiteE, hoeheE);
                break;
            case R.id.menuItemRotate:
                // show popup window with seekbars to enter the angles
                final ControlPopupWindow pwc = new ControlPopupWindow();
                pwc.showAtLocation(glSurfaceView, Gravity.TOP, 0, 0);
                int breiteC = getWindowManager().getDefaultDisplay().getWidth() - 40;
                int hoeheC = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics());
                pwc.update(0, 0, breiteC, hoeheC);
                break;
        }
        return true;
    }

    // popup window with seekbars to enter the angles

    class ControlPopupWindow extends PopupWindow {

        LinearLayout layout;

        SeekBar seekBarRotX,seekBarRotY,seekBarRotZ;
        TextView textRotX, textRotY, textRotZ ;

        ControlPopupWindow() {
            super(RotationActivity.this);
            LayoutInflater inflater = (LayoutInflater) RotationActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflater.inflate(R.layout.layout_popup_rotation, null, false);
            setContentView(layout);
            setFocusable(true);
            seekBarRotX = (SeekBar) layout.findViewById(R.id.seekBarRotX);
            seekBarRotX.setOnSeekBarChangeListener(new SeekbarsListener());
            textRotX = (TextView) layout.findViewById(R.id.textRotX);
            textRotX.setText("rotX:  0");
            seekBarRotY = (SeekBar) layout.findViewById(R.id.seekBarRotY);
            seekBarRotY.setOnSeekBarChangeListener(new SeekbarsListener());
            textRotY = (TextView) layout.findViewById(R.id.textRotY);
            textRotY.setText("rotY:  0");
            seekBarRotZ = (SeekBar) layout.findViewById(R.id.seekBarRotZ);
            seekBarRotZ.setOnSeekBarChangeListener(new SeekbarsListener());
            textRotZ = (TextView) layout.findViewById(R.id.textRotZ);
            textRotZ.setText("rotZ:  0");
        }

        private class SeekbarsListener implements SeekBar.OnSeekBarChangeListener {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float newValue = i*3.6f-180;
                if (seekBar==seekBarRotX) {
                    if (Math.abs(rotX-newValue)<10) return;
                    rotX = newValue;
                    textRotX.setText("rotX:  "+Math.round(rotX));
                }
                if (seekBar==seekBarRotY) {
                    if (Math.abs(rotY-newValue)<2) return;
                    rotY = i*3.6f-180;
                    textRotY.setText("rotY:  "+Math.round(rotY));
                }
                if (seekBar==seekBarRotZ) {
                    if (Math.abs(rotZ-newValue)<2) return;
                    rotZ = i*3.6f-180;
                    textRotZ.setText("rotZ:  "+Math.round(rotZ));
                }
                setMatricesAndRender();
                Button resetButton = ControlPopupWindow.this.layout.findViewById(R.id.resetButton);
                resetButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SeekBar sb = ControlPopupWindow.this.layout.findViewById(R.id.seekBarRotX);
                        sb.setProgress(50,true);
                        sb = ControlPopupWindow.this.layout.findViewById(R.id.seekBarRotY);
                        sb.setProgress(50,true);
                        sb = ControlPopupWindow.this.layout.findViewById(R.id.seekBarRotZ);
                        sb.setProgress(50,true);
                    }
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }

        }

    }

    // popup window to explain what the user sees

    class ExplanationPopupWindow extends PopupWindow {

        TextView explanationTextView;

        String text =
                "Activity that demonstrates the two different forms of rotation:\n\n" +
                "1.) Center left - blue airplane - rotation with reference to the \"model coordinate system\" / \"intrinsic coordinate system\" of the rotated shape:\n" +
                "The rotation is defined by up to three \"Euler angles\" / \"cardan angles\" corresponding to the three dimensions " +
                "of the coordinate system. The shape is first rotated around its x axis and its model coordinate system is rotated accordingly.\n" +
                "Next, the shape is rotated around its current z axis (i.e. the z axis resulting from the previous rotation) " +
                "and finally around the y axis resulting from these two rotations.\n" +
                "Note that rotations are not commutative such that here the rotation order is fixed as X > Z > Y " +
                "(= pitch>roll>yaw of the airplane).\n\n" +
                "2.) Center right - yellow airplane - rotation with reference to the \"world coordinate system\" / \"extrinsic coordinate system\":\n" +
                "Here, the rotation is defined by a rotation matrix and refers to the world coordinate system. " +
                "Hence, the coordinate system does not rotate together with the shape but remains fixed.\n\n" +
                "Moreover (bottom - red airplane), the activity illustrates that implementation of the method Matrix.setRotateEulerM() " +
                "which should actually be used for case 1.) is faulty with respect to rotations around the y axis. "+
                "Therefore, for case 1.) a correct version of the Euler rotation method had to be implemented, " +
                "based on https://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToMatrix/index.htm.";

        ExplanationPopupWindow() {
            super(RotationActivity.this);
            explanationTextView = new TextView(RotationActivity.this);
            explanationTextView.setPadding(40,40,40,40);
            explanationTextView.setBackgroundColor(0xFFFFFFFF);
            explanationTextView.setText(text);
            setContentView(explanationTextView);
            setFocusable(true);
        }

    }

}