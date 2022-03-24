// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 17.3.2022

// Program to demonstrate the functionality of the OpenGL untility package de.thkoeln.cvogt.android.opengl_utilities

package de.thkoeln.cvogt.android.opengl_utilities_demo;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;

import de.thkoeln.cvogt.android.opengl_utilities.GLAnimatorFactoryCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLRendererCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLShapeCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLShapeFactoryCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLSurfaceViewCV;

public class MyTouchableSurfaceView extends GLSurfaceViewCV {

    public MyTouchableSurfaceView(Context context, GLRendererCV renderer) {
        super(context, renderer);
    }

    public MyTouchableSurfaceView(Context context, GLRendererCV renderer, boolean renderOnlyWhenDirty) {
        super(context, renderer, renderOnlyWhenDirty);
    }

    private int counter = 0;

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN) {
            // Log.v("GLDEMO",">>> Touch ACTION_DOWN "+event.getX()+" "+event.getY());
            float[][] colors = new float[6][];
            colors[0] = GLShapeFactoryCV.orange;
            colors[1] = GLShapeFactoryCV.yellow;
            colors[2] = GLShapeFactoryCV.blue;
            colors[3] = GLShapeFactoryCV.purple;
            colors[4] = GLShapeFactoryCV.red;
            colors[5] = GLShapeFactoryCV.green;

            int displayHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

            if (event.getY()<displayHeight/2) {
                GLShapeCV shape = GLShapeFactoryCV.makeCube("Cube "+counter, colors);
                counter++;
                shape.setScale(0.9f, 0.9f, 0.9f);
                shape.setTrans(-4 + event.getX() / 120, 6 - event.getY() / 150, -1);
                int duration = 3000;
                shape.setRotAxis(1,0,0);
                GLAnimatorFactoryCV.addAnimatorRot(shape, 1800, duration, 0, false);
                ObjectAnimator anim = GLAnimatorFactoryCV.addAnimatorTransY(shape, -10, duration, 0);
                anim.addListener(new GLAnimatorFactoryCV.EndListenerRemove(shape,this));
                addShape(shape);
            } else {
                int duration = 5000;
                float[] baseColor = GLShapeFactoryCV.darkgrey;
                float [][] faceColor = new float[1][];
/*
                float apexHeight = 10f;
                faceColor[0] = GLShapeFactoryCV.red;
                GLShapeCV shape1 = GLShapeFactoryCV.makePyramid("Cone1",16, apexHeight, baseColor, faceColor);
                faceColor[0] = GLShapeFactoryCV.green;
                GLShapeCV shape2 = GLShapeFactoryCV.makePyramid("Cone2",16, apexHeight, baseColor, faceColor);
                faceColor[0] = GLShapeFactoryCV.blue;
                GLShapeCV shape3 = GLShapeFactoryCV.makePyramid("Cone3",16, apexHeight, baseColor, faceColor);
                faceColor[0] = GLShapeFactoryCV.yellow;
                GLShapeCV shape4 = GLShapeFactoryCV.makePyramid("Cone4",16, apexHeight, baseColor, faceColor);
                faceColor[0] = GLShapeFactoryCV.orange;
                GLShapeCV shape5 = GLShapeFactoryCV.makePyramid("Cone5",16, apexHeight, baseColor, faceColor);
                faceColor[0] = GLShapeFactoryCV.purple;
                GLShapeCV shape6 = GLShapeFactoryCV.makePyramid("Cone6",16, apexHeight, baseColor, faceColor);
                GLShapeCV shape = GLShapeFactoryCV.joinShapes("",shape1,shape2,1,1,1,180,0,0,0,0,-apexHeight);
                counter++;
                shape = GLShapeFactoryCV.joinShapes("",shape,shape3,1,1,1,0,90,0,apexHeight/3,0,-apexHeight/2);
                shape = GLShapeFactoryCV.joinShapes("",shape,shape4,1,1,1,0,270,0,-apexHeight/2,0,-apexHeight/2);
                shape = GLShapeFactoryCV.joinShapes("",shape,shape5,1,1,1,270,0,0,0,apexHeight/2,-apexHeight/2);
                shape = GLShapeFactoryCV.joinShapes("Cones "+counter,shape,shape6,1,1,1,90,0,0,0,-apexHeight/2,-apexHeight/2,0,0,-apexHeight/2);
*/
                baseColor = GLShapeFactoryCV.darkgrey;
                faceColor = new float[1][];
                float apexHeight = 10f;
                faceColor[0] = GLShapeFactoryCV.red;
                GLShapeCV shapea = GLShapeFactoryCV.makePyramid("Cone3a",16, apexHeight, baseColor, faceColor);
                faceColor[0] = GLShapeFactoryCV.green;
                GLShapeCV shapeb = GLShapeFactoryCV.makePyramid("Cone3b",16, apexHeight, baseColor, faceColor);
                faceColor[0] = GLShapeFactoryCV.blue;
                GLShapeCV shapec = GLShapeFactoryCV.makePyramid("Cone3c",16, apexHeight, baseColor, faceColor);
                faceColor[0] = GLShapeFactoryCV.yellow;
                GLShapeCV shaped = GLShapeFactoryCV.makePyramid("Cone3d",16, apexHeight, baseColor, faceColor);
                faceColor[0] = GLShapeFactoryCV.orange;
                GLShapeCV shapee = GLShapeFactoryCV.makePyramid("Cone3e",16, apexHeight, baseColor, faceColor);
                faceColor[0] = GLShapeFactoryCV.purple;
                GLShapeCV shapef = GLShapeFactoryCV.makePyramid("Cone3f",16, apexHeight, baseColor, faceColor);
                GLShapeCV shape = GLShapeFactoryCV.joinShapes("shape",shapea,shapeb,1,1,1,180,0,0,0,apexHeight,0);
                shape = GLShapeFactoryCV.joinShapes("shape",shape,shapec,1,1,1,0,0,90,apexHeight/2,apexHeight/2,0);
                shape = GLShapeFactoryCV.joinShapes("shape",shape,shaped,1,1,1,0,0,270,-apexHeight/2,apexHeight/2,0);
                shape = GLShapeFactoryCV.joinShapes("shape",shape,shapee,1,1,1,90,0,0,0,apexHeight/2,-apexHeight/2);
                shape = GLShapeFactoryCV.joinShapes("shape",shape,shapef,1,1,1,270,0,0,0,apexHeight/2,apexHeight/2,0,apexHeight/2,0);
                counter++;
                shape.setScale(0.12f).setTrans(-4 + event.getX() / 130, 6 - event.getY() / 150, -1);
                // GLAnimatorFactoryCV.addAnimatorRotX(shape, 405,duration,0, false);
                // GLAnimatorFactoryCV.addAnimatorRotY(shape, 405,duration,0, false);
                // GLAnimatorFactoryCV.addAnimatorTrans(shape,2.5f,-2,-5,duration,0);
                shape.setRotAxis(0,1,0);
                ObjectAnimator anim = GLAnimatorFactoryCV.addAnimatorRot(shape, 36000,duration,0, false);
                anim.setInterpolator(new AccelerateInterpolator());
                anim = GLAnimatorFactoryCV.addAnimatorTrans(shape,-20,50,-20,duration,0);
                anim.setInterpolator(new AccelerateInterpolator());
                anim.addListener(new GLAnimatorFactoryCV.EndListenerRemove(shape,this));
                addShape(shape);
            }
            /*
            int duration = 5000;
            int startDelay = 0;
            float[] baseColor = GLShapeFactoryCV.darkgrey;
            float[][] facesColors = new float[1][];
            float apexHeight = 10f;
            facesColors[0] = GLShapeFactoryCV.red;
            GLShapeCV shape3a = GLShapeFactoryCV.makePyramid("Cone3a",16, apexHeight, baseColor, facesColors);
            facesColors[0] = GLShapeFactoryCV.green;
            GLShapeCV shape3b = GLShapeFactoryCV.makePyramid("Cone3b",16, apexHeight, baseColor, facesColors);
            facesColors[0] = GLShapeFactoryCV.blue;
            GLShapeCV shape3c = GLShapeFactoryCV.makePyramid("Cone3c",16, apexHeight, baseColor, facesColors);
            facesColors[0] = GLShapeFactoryCV.yellow;
            GLShapeCV shape3d = GLShapeFactoryCV.makePyramid("Cone3d",16, apexHeight, baseColor, facesColors);
            facesColors[0] = GLShapeFactoryCV.orange;
            GLShapeCV shape3e = GLShapeFactoryCV.makePyramid("Cone3e",16, apexHeight, baseColor, facesColors);
            facesColors[0] = GLShapeFactoryCV.purple;
            GLShapeCV shape3f = GLShapeFactoryCV.makePyramid("Cone3f",16, apexHeight, baseColor, facesColors);
            GLShapeCV shape3 = GLShapeFactoryCV.joinShapes("Shape3",shape3a,shape3b,1,1,1,180,0,0,0,0,-apexHeight);
            shape3 = GLShapeFactoryCV.joinShapes("Shape3",shape3,shape3c,1,1,1,0,90,0,apexHeight/3,0,-apexHeight/2);
            shape3 = GLShapeFactoryCV.joinShapes("Shape3",shape3,shape3d,1,1,1,0,270,0,-apexHeight/2,0,-apexHeight/2);
            shape3 = GLShapeFactoryCV.joinShapes("Shape3",shape3,shape3e,1,1,1,270,0,0,0,apexHeight/2,-apexHeight/2);
            shape3 = GLShapeFactoryCV.joinShapes("Shape3",shape3,shape3f,1,1,1,90,0,0,0,-apexHeight/2,-apexHeight/2,0,0,-apexHeight/2);
            shape3.setScale(0.3f).setTransX(-12).setTransY(25).setTransZ(-40);
            ObjectAnimator anim3a = GLAnimatorFactoryCV.addAnimatorRotX(shape3, 405,duration,0, false);
            anim3a.setStartDelay(startDelay);
            ObjectAnimator anim3b = GLAnimatorFactoryCV.addAnimatorRotY(shape3, 405,duration,0, false);
            anim3b.setStartDelay(startDelay);
            ObjectAnimator anim3c = GLAnimatorFactoryCV.addAnimatorTrans(shape3,2.5f,-2,-5,duration,0);
            anim3c.setStartDelay(startDelay);
            ObjectAnimator anim3d = GLAnimatorFactoryCV.addAnimatorRotY(shape3, 36000,duration,0, false);
            anim3d.setInterpolator(new AccelerateInterpolator());
            anim3d.setStartDelay(startDelay+duration+300);
            ObjectAnimator anim3e = GLAnimatorFactoryCV.addAnimatorTrans(shape3,-20,50,-20,duration,0);
            anim3e.setStartDelay(startDelay+duration+1500);
            anim3e.setInterpolator(new AccelerateInterpolator());
            addShape(shape3);
        */
        }
        return true;
    }

}
