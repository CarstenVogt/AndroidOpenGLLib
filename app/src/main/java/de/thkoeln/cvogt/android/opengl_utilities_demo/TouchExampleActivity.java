// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 17.3.2022

// Program to demonstrate the functionality of the OpenGL utility package de.thkoeln.cvogt.android.opengl_utilities

package de.thkoeln.cvogt.android.opengl_utilities_demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import de.thkoeln.cvogt.android.opengl_utilities.GLRendererCV;

public class TouchExampleActivity extends Activity {

    private MyTouchableSurfaceView glSurfaceView;

    private GLRendererCV renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderer = new GLRendererCV();
        glSurfaceView = new MyTouchableSurfaceView(this, renderer,false);
        setContentView(glSurfaceView);
        Toast t = Toast.makeText(this, "Touch please!", Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER,0,0);
        t.show();
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

}