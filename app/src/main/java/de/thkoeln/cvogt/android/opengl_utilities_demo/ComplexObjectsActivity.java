// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 20.10.2022

// Program to demonstrate the functionality of the OpenGL utility package de.thkoeln.cvogt.android.opengl_utilities
// To be downloaded from https://github.com/CarstenVogt/AndroidOpenGLLib

package de.thkoeln.cvogt.android.opengl_utilities_demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import de.thkoeln.cvogt.android.opengl_utilities.GLAnimatorFactoryCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLRendererCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLShapeCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLShapeFactoryCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLSurfaceViewCV;
import de.thkoeln.cvogt.android.opengl_utilities.GraphicsUtilsCV;

public class ComplexObjectsActivity extends Activity {

    private GLSurfaceViewCV glSurfaceView;

    private GLRendererCV renderer;

    private GLShapeCV shape;

    private final float transZ_Base = -4;

    private float rotX, rotY, rotZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderer = new GLRendererCV();
        glSurfaceView = new GLSurfaceViewCV(this, renderer,false);
        setContentView(R.layout.activity_complexobjects);
        LinearLayout layout = findViewById(R.id.layoutcomplexobjects);
        // Toast.makeText(this,"More demos through the menu",Toast.LENGTH_LONG).show();
        SeekbarsListener lis = new SeekbarsListener();
        ((SeekBar)findViewById(R.id.seekBarRotX)).setOnSeekBarChangeListener(lis);
        ((SeekBar)findViewById(R.id.seekBarRotY)).setOnSeekBarChangeListener(lis);
        ((SeekBar)findViewById(R.id.seekBarRotZ)).setOnSeekBarChangeListener(lis);
        ((SeekBar)findViewById(R.id.seekBarTransZ)).setOnSeekBarChangeListener(lis);
        propellerAirplane(glSurfaceView);
        rotX=rotY=rotZ=0;
        ((SeekBar)findViewById(R.id.seekBarTransZ)).setProgress(0);
        layout.addView(glSurfaceView);
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = new MenuInflater(this);
        mi.inflate(R.menu.menu_objects, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.itemJet: jetAirplane(glSurfaceView); break;
            case R.id.itemPropeller: propellerAirplane(glSurfaceView); break;
            case R.id.itemBird: bird(glSurfaceView); break;
        }
        return true;
    }

    private void jetAirplane(GLSurfaceViewCV glSurfaceView) {
        glSurfaceView.clearShapes();
        shape = GLShapeFactoryCV.makeJetAirplane("Jet Plane",GLShapeFactoryCV.lightblue,GLShapeFactoryCV.blue);
        shape.setTransZ(transZ_Base);
        glSurfaceView.addShape(shape);
    }

    private void propellerAirplane(GLSurfaceViewCV glSurfaceView) {
        Toast.makeText(this,"Yet to be completed",Toast.LENGTH_LONG).show();
        glSurfaceView.clearShapes();
        shape = GLShapeFactoryCV.makePropellerAirplane("Propeller Plane",10000,30);
        shape.setTransZ(transZ_Base);
        glSurfaceView.addShape(shape);
    }

    private void bird(GLSurfaceViewCV glSurfaceView) {
        glSurfaceView.clearShapes();
        shape = GLShapeFactoryCV.makeBird("Bird",30000,15);
        shape.setTransZ(transZ_Base);
        glSurfaceView.addShape(shape);
    }

    private class SeekbarsListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if (seekBar==findViewById(R.id.seekBarTransZ)) {
                shape.setTransZ(transZ_Base-i/5);
                return;
            }
            float newRotValue = i*3.6f-180;
            if (seekBar==findViewById(R.id.seekBarRotX)) {
                if (Math.abs(rotX-newRotValue)<10) return;
                rotX = newRotValue;
                // textRotX.setText("rotX:  "+Math.round(rotX));
            }
            if (seekBar==findViewById(R.id.seekBarRotY)) {
                if (Math.abs(rotY-newRotValue)<2) return;
                rotY = i*3.6f-180;
                // textRotY.setText("rotY:  "+Math.round(rotY));
            }
            if (seekBar==findViewById(R.id.seekBarRotZ)) {
                if (Math.abs(rotZ-newRotValue)<2) return;
                rotZ = i*3.6f-180;
                // textRotZ.setText("rotZ:  "+Math.round(rotZ));
            }
            shape.setRotationMatrix(GraphicsUtilsCV.rotationMatrixFromEulerAngles(rotX,rotY,rotZ));
            /*
            Button resetButton = findViewById(R.id.resetButton);
            resetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SeekBar sb = findViewById(R.id.seekBarRotX);
                    sb.setProgress(50,true);
                    sb = findViewById(R.id.seekBarRotY);
                    sb.setProgress(50,true);
                    sb = findViewById(R.id.seekBarRotZ);
                    sb.setProgress(50,true);
                }
            });
            */
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }

    }

}