// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 27.5.2022

// Program to demonstrate the functionality of the OpenGL utility package de.thkoeln.cvogt.android.opengl_utilities
// To be downloaded from https://github.com/CarstenVogt/AndroidOpenGLLib

package de.thkoeln.cvogt.android.opengl_utilities_demo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import de.thkoeln.cvogt.android.opengl_utilities.*;

public class MainActivity extends ListActivity {

    String[] demos = {
            "Basic Shapes",
            "Advanced Techniques",
            "Applications",
            "--- Tests and Experiments" };

    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextureBitmapsCV.init(this);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,demos);
        setListAdapter(adapter);
        setTitle(R.string.app_name);
    }

    protected void onListItemClick(ListView liste, View datenElement, int position, long id) {

        super.onListItemClick(liste, datenElement, position, id);

        String auswahl = ((TextView)datenElement).getText().toString();

        Intent intent;

        switch (auswahl) {
            case "Basic Shapes":
                startActivity(new Intent(this, BasicShapesActivity.class)); break;
            case "Advanced Techniques":
                startActivity(new Intent(this, AdvancedTechniquesActivity.class)); break;
            case "Applications":
                startActivity(new Intent(this, ApplicationsActivity.class)); break;
            case "--- Tests and Experiments":
                startActivity(new Intent(this, TestsActivity.class)); break;
        }

    }

}

  /*
  class ControlPopupWindow extends PopupWindow {

    LinearLayout layout;

    SeekBar seekBarEyeX, seekBarEyeY, seekBarEyeZ, seekBarCenterX, seekBarCenterY, seekBarCenterZ;
    TextView textEyeX, textEyeY, textEyeZ, textCenterX, textCenterY, textCenterZ;

    ControlPopupWindow() {
      super(MainActivity.this);
      LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      layout = (LinearLayout) inflater.inflate(R.layout.layout_interactive_control, null, false);
      setContentView(layout);
      setFocusable(true);
      seekBarEyeX = (SeekBar) layout.findViewById(R.id.seekBarEyeX);
      seekBarEyeX.setOnSeekBarChangeListener(new SeekbarsListener());
      textEyeX = (TextView) layout.findViewById(R.id.textEyeX);
      textEyeX.setText("eyeX:  "+renderer.eyeX);
      seekBarEyeY = (SeekBar) layout.findViewById(R.id.seekBarEyeY);
      seekBarEyeY.setOnSeekBarChangeListener(new SeekbarsListener());
      textEyeY = (TextView) layout.findViewById(R.id.textEyeY);
      textEyeY.setText("eyeY:  "+renderer.eyeY);
      seekBarEyeZ = (SeekBar) layout.findViewById(R.id.seekBarEyeZ);
      seekBarEyeZ.setOnSeekBarChangeListener(new SeekbarsListener());
      textEyeZ = (TextView) layout.findViewById(R.id.textEyeZ);
      textEyeZ.setText("eyeZ:  "+renderer.eyeZ);
      seekBarCenterX = (SeekBar) layout.findViewById(R.id.seekBarCenterX);
      seekBarCenterX.setOnSeekBarChangeListener(new SeekbarsListener());
      textCenterX = (TextView) layout.findViewById(R.id.textCenterX);
      textCenterX.setText("centerX:  "+renderer.centerX);
      seekBarCenterY = (SeekBar) layout.findViewById(R.id.seekBarCenterY);
      seekBarCenterY.setOnSeekBarChangeListener(new SeekbarsListener());
      textCenterY = (TextView) layout.findViewById(R.id.textCenterY);
      textCenterY.setText("centerY:  "+renderer.centerY);
      seekBarCenterZ = (SeekBar) layout.findViewById(R.id.seekBarCenterZ);
      seekBarCenterZ.setOnSeekBarChangeListener(new SeekbarsListener());
      textCenterZ = (TextView) layout.findViewById(R.id.textCenterZ);
      textCenterZ.setText("centerZ:  "+renderer.centerZ);
    }

    private class SeekbarsListener implements SeekBar.OnSeekBarChangeListener {
      private final float faktor = 0.5f;
      @Override
      public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (seekBar==seekBarEyeX) {
          renderer.eyeX = RendererCubeCameraView.eyeXInit + ((i - 50)*faktor);
          textEyeX.setText("eyeX:  "+renderer.eyeX);
        }
        if (seekBar==seekBarEyeY) {
          renderer.eyeY = RendererCubeCameraView.eyeYInit + ((i - 50)*faktor);
          textEyeY.setText("eyeY:  "+renderer.eyeY);
        }
        if (seekBar==seekBarEyeZ) {
          renderer.eyeZ = RendererCubeCameraView.eyeZInit + ((i - 50)*faktor);
          textEyeZ.setText("eyeZ:  "+renderer.eyeZ);
        }
        if (seekBar==seekBarCenterX) {
          renderer.centerX = RendererCubeCameraView.centerXInit + ((i - 50)*faktor);
          textCenterX.setText("CenterX:  "+renderer.centerX);
        }
        if (seekBar==seekBarCenterY) {
          renderer.centerY = RendererCubeCameraView.centerYInit + ((i - 50)*faktor);
          textCenterY.setText("CenterY:  "+renderer.centerY);
        }
        if (seekBar==seekBarCenterZ) {
          renderer.centerZ = RendererCubeCameraView.centerZInit + ((i - 50)*faktor);
          textCenterZ.setText("CenterZ:  "+renderer.centerZ);
        }
        glSurfaceView.requestRender();
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) { }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) { }
    }

   */

    /*
  private void rotationAroundAxis2(GLSurfaceViewCV surfaceView) {
    float[][] colors = new float[12][];
    for (int i=0;i<6;i++) {
      colors[2*i] = GLShapeFactoryCV.darkgrey;
      colors[2*i+1] = GLShapeFactoryCV.lightgrey;
    }
    surfaceView.clearShapes();
    // two points to define the rotation axis
    float[] axisPoint1 = {-2f,0,-1};
    float[] axisPoint2 = {0f,0,3};
    GLShapeCV endCube1 = GLShapeFactoryCV.makeCube("EndCube1", colors);
    endCube1.setScale(0.25f).setTrans(axisPoint1);
    endCube1.setTrans(axisPoint1);
    surfaceView.addShape(endCube1);
    for (float incr=0;incr<=4;incr+=0.5f) {
      axisPoint2[2] = -2f + incr;
      // two cubes to mark the endpoints of the axis
      GLShapeCV endCube2 = GLShapeFactoryCV.makeCube("EndCube2", colors);
      endCube2.setScale(0.25f).setTrans(axisPoint2);
      endCube2.setTrans(axisPoint2);
      surfaceView.addShape(endCube2);
      // the axis
      colors = new float[1][];
      colors[0] = GLShapeFactoryCV.white;
      float axisLength = GraphicsUtilsCV.distance(axisPoint1, axisPoint2);
      float axisCenterX = axisPoint1[0] + (axisPoint2[0] - axisPoint1[0]) / 2;
      float axisCenterY = axisPoint1[1] + (axisPoint2[1] - axisPoint1[1]) / 2;
      float axisCenterZ = axisPoint1[2] + (axisPoint2[2] - axisPoint1[2]) / 2;
      GLShapeCV axisCuboid = GLShapeFactoryCV.makeCuboid("axis", axisLength, 0.1f, 0.1f, colors);
      axisCuboid.setTrans(axisCenterX, axisCenterY, axisCenterZ);
      if (axisPoint2[1] != axisPoint1[1]) {
        // axis does not lie in the x-z plane >> prism must be rotated around the z axis
        float rotAngleZ = (float) Math.toDegrees(Math.PI / 2 - Math.atan((axisPoint2[0] - axisPoint1[0]) / (axisPoint2[1] - axisPoint1[1])));
        // Log.v("DEMO2", ">Z " + (axisPoint2[0] - axisPoint1[0]) + " " + (axisPoint2[1] - axisPoint1[1]));
        // Log.v("DEMO2", ">Z " + Math.atan((axisPoint2[0] - axisPoint1[0]) / (axisPoint2[1] - axisPoint1[1])));
        // Log.v("DEMO2", ">Z " + rotAngleZ);
        axisCuboid.setRotAngleZ(rotAngleZ);
      }
      if (axisPoint2[2] != axisPoint1[2]) {
        // axis does not lie in the x-y plane >> prism must be rotated around the y axis
        float rotAngleY = (float) Math.toDegrees(Math.PI / 2 - Math.atan(Math.abs(axisPoint2[0] - axisPoint1[0]) / Math.abs(axisPoint2[2] - axisPoint1[2])));
        // Log.v("DEMO2", ">Y " + Math.abs(axisPoint2[0] - axisPoint1[0]) + " " + Math.abs(axisPoint2[2] - axisPoint1[2]));
        // Log.v("DEMO2",">Y "+Math.atan(2));
        // Log.v("DEMO2",">Y "+(Math.PI/2-Math.atan(0.5)));
        // Log.v("DEMO2",">Y "+Math.toDegrees(Math.atan(2)));
        // Log.v("DEMO2", ">Y " + (Math.PI / 2 - Math.atan(Math.abs(axisPoint2[0] - axisPoint1[0]) / Math.abs(axisPoint2[2] - axisPoint1[2]))));
        // Log.v("DEMO2", ">Y " + rotAngleY);
        if (axisPoint1[0] <= axisPoint2[0])
          axisCuboid.setRotAngleY(180 - rotAngleY); // korrekt, wenn X2>X1
        else
          axisCuboid.setRotAngleY(180 + rotAngleY); // korrekt, wenn X2>X1
      }
      surfaceView.addShape(axisCuboid);
    }
    // the rotating object
    colors = new float[6][];
    colors[0] = GLShapeFactoryCV.orange;
    colors[1] = GLShapeFactoryCV.blue;
    colors[2] = GLShapeFactoryCV.red;
    colors[3] = GLShapeFactoryCV.green;
    colors[4] = GLShapeFactoryCV.purple;
    colors[5] = GLShapeFactoryCV.yellow;
    GLShapeCV shape1 = GLShapeFactoryCV.makeCube("Cube1", colors);
    shape1.setTransY(-2.5f).setTransZ(-2.5f);
    GLAnimatorFactoryCV.addAnimatorArcPathAroundAxis(shape1,axisPoint1, axisPoint2, 270,false,8000,0);
    // ObjectAnimator anim = GLAnimatorFactoryCV.addAnimatorRotZ(shape1,-270, 4000, 0, false);
    surfaceView.addShape(shape1);
  }
    */

  /* ALT
  private void rotatingShapes(GLSurfaceViewCV surfaceView) {
    surfaceView.clearShapes();
    float[][] colors = new float[6][];
    colors[0] = GLShapeFactoryCV.orange;
    colors[1] = GLShapeFactoryCV.blue;
    colors[2] = GLShapeFactoryCV.red;
    colors[3] = GLShapeFactoryCV.green;
    colors[4] = GLShapeFactoryCV.purple;
    colors[5] = GLShapeFactoryCV.yellow;
    GLShapeCV[][] shapes = new GLShapeCV[3][3];
    for (int i=0; i<3; i++)
      for (int j=0; j<3; j++) {
        shapes[i][j] = GLShapeFactoryCV.makeCube("Cube" + i + "" + j, colors);
        shapes[i][j].setTrans(1.7f * (j - 1), 0.5f + 2f * (1 - i), 0);
        surfaceView.addShape(shapes[i][j]);
      }
    float angle = 315;
    int duration = 5000, repeatcount = 0;
    int startdelay = duration;
    // rotation around the x axis
    ObjectAnimator animX, animY, animZ;
    animX = GLAnimatorFactoryCV.addAnimatorRotX(shapes[0][0], angle, duration, repeatcount, false) ;
    animX.start();
    // rotation around the x and y axes
    animX = GLAnimatorFactoryCV.addAnimatorRotX(shapes[0][1], angle, duration, repeatcount, false) ;
    animX.start();
    animY = GLAnimatorFactoryCV.addAnimatorRotY(shapes[0][1], angle, duration, repeatcount, false) ;
    animY.setStartDelay(startdelay);
    animY.start();
    // rotation around the x, y and z axes
    animX = GLAnimatorFactoryCV.addAnimatorRotX(shapes[0][2], angle, duration, repeatcount, false) ;
    animX.start();
    animY = GLAnimatorFactoryCV.addAnimatorRotY(shapes[0][2], angle, duration, repeatcount, false) ;
    animY.setStartDelay(startdelay);
    animY.start();
    animZ = GLAnimatorFactoryCV.addAnimatorRotZ(shapes[0][2], angle, duration, repeatcount, false) ;
    animZ.setStartDelay(2*startdelay);
    animZ.start();
    // rotation around the y axis
    animY = GLAnimatorFactoryCV.addAnimatorRotY(shapes[1][0], angle, duration, repeatcount, false) ;
    animY.start();
    // rotation around the y and x axes
    animY = GLAnimatorFactoryCV.addAnimatorRotY(shapes[1][1], angle, duration, repeatcount, false) ;
    animY.start();
    animX = GLAnimatorFactoryCV.addAnimatorRotX(shapes[1][1], angle, duration, repeatcount, false) ;
    animX.setStartDelay(startdelay);
    animX.start();
    // rotation around the y, x and z axes
    animY = GLAnimatorFactoryCV.addAnimatorRotY(shapes[1][2], angle, duration, repeatcount, false) ;
    animY.start();
    animX = GLAnimatorFactoryCV.addAnimatorRotX(shapes[1][2], angle, duration, repeatcount, false) ;
    animX.setStartDelay(startdelay);
    animX.start();
    animZ = GLAnimatorFactoryCV.addAnimatorRotZ(shapes[1][2], angle, duration, repeatcount, false) ;
    animZ.setStartDelay(2*startdelay);
    animZ.start();
    // rotation around the z axis
    animZ = GLAnimatorFactoryCV.addAnimatorRotZ(shapes[2][0], angle, duration, repeatcount, false) ;
    animZ.start();
    // rotation around the z and x axes
    shapes[2][1].setRotAngleX(45);
    shapes[2][1].setRotAngleZ(45);
    animZ = GLAnimatorFactoryCV.addAnimatorRotZ(shapes[2][1], angle, duration, repeatcount, false) ;
    animZ.start();
    animX = GLAnimatorFactoryCV.addAnimatorRotX(shapes[2][1], angle, duration, repeatcount, false) ;
    animX.setStartDelay(startdelay);
    animX.start();
    // rotation around the x and z axes
    shapes[2][2].setRotAngleX(45);
    shapes[2][2].setRotAngleZ(45);
    animX = GLAnimatorFactoryCV.addAnimatorRotX(shapes[2][2], angle, duration, repeatcount, false) ;
    animX.start();
    animZ = GLAnimatorFactoryCV.addAnimatorRotZ(shapes[2][2], angle, duration, repeatcount, false) ;
    animZ.setStartDelay(startdelay);
    animZ.start();
    // rotation around the z, x and y axes
    animZ = GLAnimatorFactoryCV.makeAnimatorRotZ(shapes[2][2], angle, duration, repeatcount, false) ;
    animZ.start();
    animX = GLAnimatorFactoryCV.makeAnimatorRotX(shapes[2][2], angle, duration, repeatcount, false) ;
    animX.setStartDelay(startdelay);
    animX.start();
    animY = GLAnimatorFactoryCV.makeAnimatorRotY(shapes[2][2], angle, duration, repeatcount, false) ;
    animY.setStartDelay(2*startdelay);
    animY.start();
    surfaceView.clearShapes();
    float[][] colors = new float[6][];
    colors[0] = GLShapeFactoryCV.orange;
    colors[1] = GLShapeFactoryCV.blue;
    colors[2] = GLShapeFactoryCV.red;
    colors[3] = GLShapeFactoryCV.green;
    colors[4] = GLShapeFactoryCV.purple;
    colors[5] = GLShapeFactoryCV.yellow;
    GLShapeCV shape1 = GLShapeFactoryCV.makeCube("Cube1", colors);
    GLShapeCV shape2 = GLShapeFactoryCV.makeCube("Cube2", colors);
    GLShapeCV shape3 = GLShapeFactoryCV.makeCube("Cube3", colors);
    GLShapeCV shape4 = GLShapeFactoryCV.makeCube("Cube4", colors);
    shape1.setTransX(-2f);
    shape3.setTransX(2f);
    shape4.setTransY(-2);
    int startDelay = 3000;
    // rotation first z then x
    shape1.setRotAngleZ(45);
    ObjectAnimator animX = GLAnimatorFactoryCV.makeAnimatorRotX(shape1, 45, 2000, 0, false) ;
    animX.setStartDelay(startDelay);
    surfaceView.addShape(shape1);
    // rotation x and z simultaneously
    ObjectAnimator animX2 = GLAnimatorFactoryCV.makeAnimatorRotX(shape2, 45, 2000, 0, false) ;
    animX2.setStartDelay(startDelay);
    ObjectAnimator animZ2 = GLAnimatorFactoryCV.makeAnimatorRotZ(shape2, 45, 2000, 0, false) ;
    animZ2.setStartDelay(startDelay);
    surfaceView.addShape(shape2);
    // rotation first x then z
    shape3.setRotAngleX(45);
    ObjectAnimator animZ = GLAnimatorFactoryCV.makeAnimatorRotZ(shape3, 45, 2000, 0, false) ;
    animZ.setStartDelay(startDelay);
    surfaceView.addShape(shape3);
    // rotation x and z
    shape4.setRotAngleZ(45).setRotAngleX(45);
    surfaceView.addShape(shape4);
  }

*/

