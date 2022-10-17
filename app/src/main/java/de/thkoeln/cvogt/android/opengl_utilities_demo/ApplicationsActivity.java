// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 25.9.2022

// Program to demonstrate the functionality of the OpenGL utility package de.thkoeln.cvogt.android.opengl_utilities
// To be downloaded from https://github.com/CarstenVogt/AndroidOpenGLLib

package de.thkoeln.cvogt.android.opengl_utilities_demo;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

import de.thkoeln.cvogt.android.opengl_utilities.GLAnimatorFactoryCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLRendererCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLShapeCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLShapeFactoryCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLSurfaceViewCV;
import de.thkoeln.cvogt.android.opengl_utilities.TextureBitmapsCV;

public class ApplicationsActivity extends Activity {

    private GLSurfaceViewCV glSurfaceView;

    private GLRendererCV renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderer = new GLRendererCV();
        glSurfaceView = new GLSurfaceViewCV(this, renderer, false);
        thisAndThat(glSurfaceView);
        setContentView(glSurfaceView);
        Toast.makeText(this,"More demos through the menu",Toast.LENGTH_LONG).show();
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
        mi.inflate(R.menu.menu_applications, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId()==R.id.itemThisAndThat)
            thisAndThat(glSurfaceView);
        if (item.getItemId()==R.id.itemDrones)
            drones(glSurfaceView);
        if (item.getItemId()==R.id.itemAirplanesRow)
            airplanesRow(glSurfaceView);
        if (item.getItemId()==R.id.itemAirplanesLooping)
            airplanesLooping(glSurfaceView);
        if (item.getItemId()==R.id.itemBirdFlappingWings)
            birdWithFlappingWings(glSurfaceView);
        if (item.getItemId()==R.id.itemRandomSpheres)
            randomSpheres(glSurfaceView);
        if (item.getItemId()==R.id.itemCubeOfCubes)
            cubeOfCubes(glSurfaceView);
        if (item.getItemId()==R.id.itemTetrahedraPattern)
            tetrahedraPattern(glSurfaceView);
        setContentView(glSurfaceView);
        return true;
    }

    private void thisAndThat(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        // DEMO 1: Three rotating cubes
        int duration = 3000;
        // cube 1
        Bitmap[] textures = new Bitmap[6];
        for (int i=0; i<6; i++)
            textures[i] = TextureBitmapsCV.get("raster");
        GLShapeCV shape1a = GLShapeFactoryCV.makeCube("Cube1", textures);
        shape1a.setScale(1.5f,1.5f,1.5f).setTransZ(-9);
        GLAnimatorFactoryCV.addAnimatorRotX(shape1a, 1080,  3*duration, 0, true);
        GLAnimatorFactoryCV.addAnimatorTrans(shape1a, -2f, 0,-1, duration, 0);
        ObjectAnimator anim = GLAnimatorFactoryCV.addAnimatorTrans(shape1a, -4, -5,5, duration, 0);
        anim.setStartDelay(2*duration);
        surfaceView.addShape(shape1a);
        textures[0] = TextureBitmapsCV.get("dice04");
        textures[1] = TextureBitmapsCV.get("dice05");
        textures[2] = TextureBitmapsCV.get("dice03");
        textures[3] = TextureBitmapsCV.get("dice02");
        textures[4] = TextureBitmapsCV.get("dice06");
        textures[5] = TextureBitmapsCV.get("dice01");
        // cube 2
        GLShapeCV shape1b = GLShapeFactoryCV.makeCube("Cube2", textures);
        shape1b.setScale(1.5f,1.5f,1.5f).setTransZ(-11);
        anim = GLAnimatorFactoryCV.addAnimatorRotY(shape1b, 360, duration, 0, true);
        anim.setStartDelay(duration);
        anim = GLAnimatorFactoryCV.addAnimatorTrans(shape1b, 0, 0,-1, duration, 0);
        anim.setStartDelay(duration);
        anim = GLAnimatorFactoryCV.addAnimatorTrans(shape1b, 0,-5, 5, duration, 0);
        anim.setStartDelay(2*duration);
        surfaceView.addShape(shape1b);
        // cube 3
        for (int i=0; i<6; i++)
            textures[i] = TextureBitmapsCV.get("logo_thk");
        GLShapeCV shape1c = GLShapeFactoryCV.makeCube("Cube3", textures);
        shape1c.setScale(1.5f,1.5f,1.5f).setTransZ(-10);
        anim = GLAnimatorFactoryCV.addAnimatorRotZ(shape1c, 720, 2*duration, 0, true);
        anim.setStartDelay(duration/2);
        anim = GLAnimatorFactoryCV.addAnimatorTrans(shape1c, 2f, 0,-1, duration, 0);
        anim.setStartDelay(duration/2);
        anim = GLAnimatorFactoryCV.addAnimatorTrans(shape1c, 2,-5, 5, duration, 0);
        anim.setStartDelay(2*duration);
        surfaceView.addShape(shape1c);
        // DEMO 2: A prism with a pyramid on top
        int startDelay = 2*duration-1000;
        duration = 5000;
        float[] baseColor = GLShapeFactoryCV.darkgrey;
        float[][] facesColors = new float[2][];
        facesColors[0] = GLShapeFactoryCV.blue;
        facesColors[1] = GLShapeFactoryCV.lightblue;
/*
        float heightPrism = 1.8f, heightPyramid = 2.7f;
        GLShapeCV shape2a = GLShapeFactoryCV.makePrism("Prism",12,heightPrism,baseColor,baseColor,facesColors);
        GLShapeCV shape2b = GLShapeFactoryCV.makePyramid("Pyramid",12,heightPyramid,baseColor,facesColors);
        GLShapeCV shape2 = GLShapeFactoryCV.joinShapes("House",shape2a,shape2b,1,1,1,0,0,0,0,(heightPrism+heightPyramid)/2,0,0,heightPrism/2,0);
*/
        float heightPrism = 2f, heightPyramid = 2f;
        GLShapeCV shape2a = GLShapeFactoryCV.makePrism("Prism",12,heightPrism,baseColor,baseColor,facesColors);
        GLShapeCV shape2b = GLShapeFactoryCV.makePyramid("Pyramid",12,heightPyramid,baseColor,facesColors);
        GLShapeCV shape2 = GLShapeFactoryCV.joinShapes("House",shape2a,shape2b,1f,1,1f,0,15,0,0,(heightPrism+heightPyramid)/2,0,0,heightPrism/2,0);
        shape2.setScaleY(0.7f).setTransX(1.5f).setTransY(-0.5f).setTransZ(-1.5f);
        shape2.setTrans(8,12,-20);
        ObjectAnimator anim2a = GLAnimatorFactoryCV.addAnimatorRotY(shape2, 140,(duration+1000)/4,4, true);
        anim2a.setInterpolator(new AccelerateDecelerateInterpolator());
        anim2a.setStartDelay(startDelay);
        ObjectAnimator anim2b = GLAnimatorFactoryCV.addAnimatorTrans(shape2, 0,0,-2,duration,0);
        anim2b.setInterpolator(new AccelerateDecelerateInterpolator());
        anim2b.setStartDelay(startDelay);
        ObjectAnimator anim2c = GLAnimatorFactoryCV.addAnimatorTransX(shape2, -3.5f,1000,0);
        anim2c.setInterpolator(new AccelerateDecelerateInterpolator());
        anim2c.setStartDelay(startDelay+duration+1000);
        ObjectAnimator anim2d = GLAnimatorFactoryCV.addAnimatorTrans(shape2, 5,-4, 1, 1000,0);
        anim2d.setStartDelay(startDelay+duration+2000);
        anim2d.setInterpolator(new AccelerateInterpolator());
        surfaceView.addShape(shape2);
        startDelay += duration+3000;
        // DEMO 3: Six combined rotating cones = a kind of drone
        duration = 5000;
        baseColor = GLShapeFactoryCV.darkgrey;
        facesColors = new float[1][];
        float apexHeight = 4f;
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
        GLShapeCV shape3 = GLShapeFactoryCV.joinShapes("Shape3",shape3a,shape3b,1,1,1,180,0,0,0,apexHeight,0);
        shape3 = GLShapeFactoryCV.joinShapes("Shape3",shape3,shape3c,1,1,1,0,0,90,apexHeight/2,apexHeight/2,0);
        shape3 = GLShapeFactoryCV.joinShapes("Shape3",shape3,shape3d,1,1,1,0,0,270,-apexHeight/2,apexHeight/2,0);
        shape3 = GLShapeFactoryCV.joinShapes("Shape3",shape3,shape3e,1,1,1,90,0,0,0,apexHeight/2,-apexHeight/2);
        shape3 = GLShapeFactoryCV.joinShapes("Shape3",shape3,shape3f,1,1,1,270,0,0,0,apexHeight/2,apexHeight/2,0,apexHeight/2,0);
        shape3.setScale(0.5f).setTransX(-8).setTransY(12).setTransZ(-20);
        ObjectAnimator anim3a = GLAnimatorFactoryCV.addAnimatorRotY(shape3, 405, duration,0, false);
        anim3a.setStartDelay(startDelay);
        ObjectAnimator anim3b = GLAnimatorFactoryCV.addAnimatorTrans(shape3,2.5f,-2,-7,duration,0);
        anim3b.setStartDelay(startDelay);
        ObjectAnimator anim3c = GLAnimatorFactoryCV.addAnimatorRotZInModelSpace(shape3, 30, 1000 ,0, false);
        anim3c.setStartDelay(startDelay+duration+150);
        ObjectAnimator anim3d = GLAnimatorFactoryCV.addAnimatorRotYInModelSpace(shape3, 36000, duration,0, false);
        anim3d.setInterpolator(new AccelerateInterpolator());
        anim3d.setStartDelay(startDelay+duration+1300);
        ObjectAnimator anim3e = GLAnimatorFactoryCV.addAnimatorTrans(shape3,-20,50,-20,duration,0);
        anim3e.setStartDelay(startDelay+duration+2500);
        anim3e.setInterpolator(new AccelerateInterpolator());
        surfaceView.addShape(shape3);
        startDelay += 3*duration-6000;
        // DEMO 4: Shapes rotating around an axis
        float[] axisPoint1 = { -3f, -5.5f, -3 };
        float[] axisPoint2 = { 3f, -5.5f, -3 };
        // The axis
        float[][] colors = new float[1][];
        colors[0] = GLShapeFactoryCV.lightblue;
        GLShapeCV axis = GLShapeFactoryCV.makeCuboid("Axis",axisPoint2[0]-axisPoint1[0],0.05f,0.05f, colors);
        axis.setTrans((axisPoint2[0]+axisPoint1[0])/2,axisPoint1[1],axisPoint1[2]);
        surfaceView.addShape(axis);
        // The rotating shapes
        colors = new float[6][];
        colors[0] = GLShapeFactoryCV.orange;
        colors[1] = GLShapeFactoryCV.yellow;
        colors[2] = GLShapeFactoryCV.blue;
        colors[3] = GLShapeFactoryCV.purple;
        colors[4] = GLShapeFactoryCV.red;
        colors[5] = GLShapeFactoryCV.green;
        float angle = 1080;
        duration = 6000;
        GLShapeCV rotShape1 = GLShapeFactoryCV.makeCube("Rotating Shape 1",colors);
        rotShape1.setTrans(-2,-4,-3).setScale(0.75f);
        GLAnimatorFactoryCV.addAnimatorArcPathAroundAxis(rotShape1,axisPoint1,axisPoint2,angle,duration,startDelay);
        surfaceView.addShape(rotShape1);
        GLShapeCV rotShape2 = GLShapeFactoryCV.makeCube("Rotating Shape 2",colors);
        rotShape2.setTrans(0,-4,-3).setScale(0.75f);
        GLAnimatorFactoryCV.addAnimatorArcPathAroundAxis(rotShape2,axisPoint1,axisPoint2,angle,duration,startDelay);
        anim = GLAnimatorFactoryCV.addAnimatorRotX(rotShape2,2160, duration,0,false);
        anim.setStartDelay(startDelay);
        surfaceView.addShape(rotShape2);
        GLShapeCV rotShape3 = GLShapeFactoryCV.makeCube("Rotating Shape 3",colors);
        rotShape3.setTrans(2,-4,-3).setScale(0.75f);
        GLAnimatorFactoryCV.addAnimatorArcPathAroundAxis(rotShape3,axisPoint1,axisPoint2,angle,duration,startDelay);
        anim = GLAnimatorFactoryCV.addAnimatorRotX(rotShape3,-2160, duration,0,false);
        anim.setStartDelay(startDelay);
        surfaceView.addShape(rotShape3);
    }

    private void drones(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        float[] baseColor = GLShapeFactoryCV.darkgrey;
        float[][] facesColors = new float[1][];
        float apexHeight = 4f;
        facesColors[0] = GLShapeFactoryCV.red;
        GLShapeCV cone1 = GLShapeFactoryCV.makePyramid("Cone1", 16, apexHeight, baseColor, facesColors);
        facesColors[0] = GLShapeFactoryCV.green;
        GLShapeCV cone2 = GLShapeFactoryCV.makePyramid("Cone2", 16, apexHeight, baseColor, facesColors);
        facesColors[0] = GLShapeFactoryCV.blue;
        GLShapeCV cone3 = GLShapeFactoryCV.makePyramid("Cone3", 16, apexHeight, baseColor, facesColors);
        facesColors[0] = GLShapeFactoryCV.yellow;
        GLShapeCV cone4 = GLShapeFactoryCV.makePyramid("Cone4", 16, apexHeight, baseColor, facesColors);
        facesColors[0] = GLShapeFactoryCV.orange;
        GLShapeCV cone5 = GLShapeFactoryCV.makePyramid("Cone5", 16, apexHeight, baseColor, facesColors);
        facesColors[0] = GLShapeFactoryCV.purple;
        GLShapeCV cone6 = GLShapeFactoryCV.makePyramid("Cone6", 16, apexHeight, baseColor, facesColors);
        GLShapeCV drone = GLShapeFactoryCV.joinShapes("Drone", cone1, cone2, 1, 1, 1, 180, 0, 0, 0, apexHeight, 0);
        drone = GLShapeFactoryCV.joinShapes("Drone", drone, cone3, 1, 1, 1, 0, 0, 90, apexHeight / 2, apexHeight / 2, 0);
        drone = GLShapeFactoryCV.joinShapes("Drone", drone, cone4, 1, 1, 1, 0, 0, 270, -apexHeight / 2, apexHeight / 2, 0);
        drone = GLShapeFactoryCV.joinShapes("Drone", drone, cone5, 1, 1, 1, 90, 0, 0, 0, apexHeight / 2, -apexHeight / 2);
        drone = GLShapeFactoryCV.joinShapes("Drone", drone, cone6, 1, 1, 1, 270, 0, 0, 0, apexHeight / 2, apexHeight / 2, 0, apexHeight / 2, 0);
        final int numberOfDrones = 50;
        GLShapeCV[] drones = new GLShapeCV[numberOfDrones];
        int startDelay = 0;
        final int startIntervals = 500;
        for (int i=0;i<numberOfDrones;i++) {
            drones[i] = drone.copy("Drone"+i);
            int durationFirstPath = 3000;
            int durationTilt = 500;
            int durationSecondPath = 3000;
            drones[i].setScale(0.75f).setTransX(-8).setTransY(12).setTransZ(-30);
            ObjectAnimator animFirstPathRot = GLAnimatorFactoryCV.addAnimatorRotY(drones[i], 405, durationFirstPath, 0, false);
            animFirstPathRot.setStartDelay(startDelay);
            float targetXFirstPath = -6f+14*(float)Math.random();
            float targetYFirstPath = -10f+6*(float)Math.random();
            ObjectAnimator animFirstPathTrans = GLAnimatorFactoryCV.addAnimatorTrans(drones[i], targetXFirstPath, targetYFirstPath, -10, durationFirstPath, 0);
            animFirstPathTrans.setStartDelay(startDelay);
            ObjectAnimator animTilt = GLAnimatorFactoryCV.addAnimatorRotZInModelSpace(drones[i], 30, durationTilt, 0, false);
            animTilt.setStartDelay(startDelay + durationFirstPath);
            ObjectAnimator animSecondPathRot = GLAnimatorFactoryCV.addAnimatorRotYInModelSpace(drones[i], 36000, durationSecondPath, 0, false);
            animSecondPathRot.setInterpolator(new AccelerateInterpolator());
            animSecondPathRot.setStartDelay(startDelay + durationSecondPath + durationTilt);
            double random = Math.random();
            float targetXSecondPath = 20;
            if (random<0.333f) targetXSecondPath = -20;
             else if (random<0.667f) targetXSecondPath = 0;
             else targetXSecondPath = 20;
            ObjectAnimator animSecondPathTrans = GLAnimatorFactoryCV.addAnimatorTrans(drones[i], targetXSecondPath, 50, -20, durationSecondPath, 0);
            animSecondPathTrans.setStartDelay(startDelay + durationSecondPath + durationTilt);
            animSecondPathTrans.setInterpolator(new AccelerateInterpolator());
            surfaceView.addShape(drones[i]);
            startDelay += startIntervals;
        }
    }

    public void airplanesRow(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        final int numberOfPlanes = 12;
        for (int i = 0; i < numberOfPlanes; i++) {
            float[][] colors = new float[2][];
            switch (i % 3) {
                case 0:
                    colors[0] = GLShapeFactoryCV.lightblue;
                    colors[1] = GLShapeFactoryCV.blue;
                    break;
                case 1:
                    colors[0] = GLShapeFactoryCV.orange;
                    colors[1] = GLShapeFactoryCV.red;
                    break;
                case 2:
                    colors[0] = GLShapeFactoryCV.lightgreen;
                    colors[1] = GLShapeFactoryCV.blue;
                    break;
                case 3:
                    colors[0] = GLShapeFactoryCV.lightgrey;
                    colors[1] = GLShapeFactoryCV.grey;
                    break;
            }
            /*
            float lengthFuselage = 7;
            GLShapeCV fuselage = GLShapeFactoryCV.makePrism("Fuselage",32,lengthFuselage,GLShapeFactoryCV.lightblue,GLShapeFactoryCV.lightblue,colors);
            float lengthVerticalTail = lengthFuselage/2.0f, heightVerticalTail = 4;
            float[][] verticesVerticalTail = {{0,-0.5f*lengthFuselage,0},{0,-0.5f*lengthFuselage+lengthVerticalTail,0},{-heightVerticalTail,-0.6f*lengthFuselage,0}};
            GLShapeCV verticalTail = GLShapeFactoryCV.makeTriangle("Vertical Tail",verticesVerticalTail,colors[0],colors[1],10);
            float wingspan = 12;
            float[][] verticesWings = {{0,0.25f*lengthFuselage,0},{0,-0.5f*lengthFuselage,0.5f*wingspan},{0,-0.5f*lengthFuselage,-0.5f*wingspan}};
            GLShapeCV wings = GLShapeFactoryCV.makeTriangle("Wings",verticesWings,colors[0],colors[1],10);
            float[][] colorFront = new float[1][];
            colorFront[0] = colors[0];
            GLShapeCV front = GLShapeFactoryCV.makeHemisphere("Front",4,colorFront);
            float[][] colorCockpit = { GLShapeFactoryCV.white };
            GLShapeCV cockpit = GLShapeFactoryCV.makeHemisphere("Cockpit",4,colorCockpit);
            GLShapeCV[] airplaneParts = new GLShapeCV[5];
            airplaneParts[0] = fuselage;
            airplaneParts[1] = verticalTail;
            airplaneParts[2] = wings;
            airplaneParts[3] = front;
            airplaneParts[4] = cockpit;
            float[][] rotationArray = new float[airplaneParts.length][3];
            float[][] translationArray = new float[airplaneParts.length][3];
            translationArray[3][1] = 0.5f*lengthFuselage+0.5f;
            rotationArray[4][2] = 90;
            translationArray[4][0] = -1.2f;
            translationArray[4][1] = 0.25f*lengthFuselage;
            GLShapeCV airplane = GLShapeFactoryCV.joinShapes("Airplane",airplaneParts,rotationArray,translationArray,0);
            */
            GLShapeCV airplane = GLShapeFactoryCV.makeAirplane(colors[0], colors[1]);
            airplane.setScale(.4f);
            float[] origin = {-19, 16, -25};
            float[] target = {12, -12, -3};
            float[] vector = new float[3];
            for (int j=0;j<3;j++)
                vector[j] = target[j]-origin[j];
            airplane.alignWith(2,vector,true,0);
            // airplane.setRotationByEulerAngles(-50, 140, 0);
            airplane.setTrans(origin);
            ObjectAnimator anim = GLAnimatorFactoryCV.addAnimatorTrans(airplane, target, 10000, 0);
            anim.setStartDelay(i * 1000);
            ObjectAnimator anim2 = null;
            switch (3 * i / numberOfPlanes) {
                case 0:
                    anim2 = GLAnimatorFactoryCV.addAnimatorPitch(airplane, 30, 2000, 5);
                    break;
                case 1:
                    anim2 = GLAnimatorFactoryCV.addAnimatorRoll(airplane, 30, 2000, 5);
                    break;
                case 2:
                    anim2 = GLAnimatorFactoryCV.addAnimatorYaw(airplane, 30, 2000, 5);
                    break;
            }
            anim2.setStartDelay(i * 1000);
            surfaceView.addShape(airplane);
        }
    }

    public void airplanesLooping(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        // airplane 1
        GLShapeCV airplane = GLShapeFactoryCV.makeAirplane(GLShapeFactoryCV.lightblue,GLShapeFactoryCV.blue);
        airplane.setTrans(0,-3.5f,-6).setRotation(-45,0,1,0).setScale(0.25f);
        int duration = 20000;
        float[] axisPoint1 = {0,0,1};
        float[] axisPoint2 = {0,0,-1};
        GLAnimatorFactoryCV.addAnimatorArcPathAroundAxis(airplane, axisPoint1,axisPoint2,-1800,duration,0);
        GLAnimatorFactoryCV.addAnimatorRotXInModelSpace(airplane,-1800,duration,0,false);
        surfaceView.addShape(airplane);
        // airplane 2
        airplane = GLShapeFactoryCV.makeAirplane(GLShapeFactoryCV.lightblue,GLShapeFactoryCV.blue);
        airplane.setTrans(0,3.5f,-6).setRotation(45,0,1,0).setScale(0.25f);
        GLAnimatorFactoryCV.addAnimatorArcPathAroundAxis(airplane, axisPoint1,axisPoint2,-1800,duration,0) ;
        GLAnimatorFactoryCV.addAnimatorRotXInModelSpace(airplane,1800,duration,0,false);
        surfaceView.addShape(airplane);
        /*
        final int numberOfPlanes = 12;
        for (int i = 0; i < numberOfPlanes; i++) {
            float[][] colors = new float[2][];
            switch (i % 3) {
                case 0:
                    colors[0] = GLShapeFactoryCV.lightblue;
                    colors[1] = GLShapeFactoryCV.blue;
                    break;
                case 1:
                    colors[0] = GLShapeFactoryCV.orange;
                    colors[1] = GLShapeFactoryCV.red;
                    break;
                case 2:
                    colors[0] = GLShapeFactoryCV.lightgreen;
                    colors[1] = GLShapeFactoryCV.blue;
                    break;
                case 3:
                    colors[0] = GLShapeFactoryCV.lightgrey;
                    colors[1] = GLShapeFactoryCV.grey;
                    break;
            }
            GLShapeCV airplane = makeAirplane(colors[0], colors[1]);
            airplane.setScale(.4f);
            float[] origin = {-19, 16, -25};
            float[] target = {12, -12, -3};
            airplane.setRotationByEulerAngles(-50, 140, 0);
            airplane.setTrans(origin);
            ObjectAnimator anim = GLAnimatorFactoryCV.addAnimatorTrans(airplane, target, 10000, 0);
            anim.setStartDelay(i * 1000);
            ObjectAnimator anim2 = null;
            switch (3 * i / numberOfPlanes) {
                case 0:
                    anim2 = GLAnimatorFactoryCV.addAnimatorPitch(airplane, 30, 2000, 5);
                    break;
                case 1:
                    anim2 = GLAnimatorFactoryCV.addAnimatorRoll(airplane, 30, 2000, 5);
                    break;
                case 2:
                    anim2 = GLAnimatorFactoryCV.addAnimatorYaw(airplane, 30, 2000, 5);
                    break;
            }
            anim2.setStartDelay(i * 1000);

         */
            surfaceView.addShape(airplane);
    }

    public void birdWithFlappingWings(GLSurfaceViewCV surfaceView) {

        Toast.makeText(this,"Experimental implementation - rather inefficient",Toast.LENGTH_LONG).show();

        surfaceView.clearShapes();
        int numberOfParts = 9;
        GLShapeCV[] shapes = new GLShapeCV[numberOfParts];
        float[][] verticesWing1 = {{0,-1,0},{0,1,0},{-2,1,0}};
        shapes[0] = GLShapeFactoryCV.makeTriangle("Wing1",verticesWing1,GLShapeFactoryCV.blue);
        float[][] verticesWing2 = {{0,-1,0},{2,1,0},{0,1,0}};
        shapes[1] = GLShapeFactoryCV.makeTriangle("Wing2",verticesWing2,GLShapeFactoryCV.blue);
        shapes[2] = GLShapeFactoryCV.makeSphere("Body",3, GLShapeFactoryCV.lightblue);
        shapes[3] = GLShapeFactoryCV.makeSphere("Head", 3, GLShapeFactoryCV.lightgreen);
        shapes[4] = GLShapeFactoryCV.makeSphere("LeftEye", 3, GLShapeFactoryCV.red);
        shapes[5] = GLShapeFactoryCV.makeSphere("RightEye", 3, GLShapeFactoryCV.red);
        float[][] verticesBeak = {{0,-3f,0.3f},{0.5f,-1.5f,0.3f},{-0.5f,-1.5f,0.3f}};
        shapes[6] = GLShapeFactoryCV.makeTriangle("BeakUpper", verticesBeak, GLShapeFactoryCV.lightred);
        shapes[7] = GLShapeFactoryCV.makeTriangle("BeakLower", verticesBeak, GLShapeFactoryCV.lightred);
        float[][] verticesTail = {{0,2f,0},{0.5f,3.5f,0.75f},{-0.5f,3.5f,0.75f}};
        shapes[8] = GLShapeFactoryCV.makeTriangle("Tail", verticesTail, GLShapeFactoryCV.lightred);
        float[][] scalingArray = new float[numberOfParts][3];
        for (int i=0; i<numberOfParts; i++)
            for (int j=0;j<3;j++)
                scalingArray[i][j] = 1;
        scalingArray[2][0] = 0.5f;
        scalingArray[2][1] = 2;
        scalingArray[2][2] = 0.5f;
        scalingArray[3][0] = scalingArray[3][1] = scalingArray[3][2] = 0.5f;
        scalingArray[4][0] = scalingArray[4][1] = scalingArray[4][2] = 0.2f;
        scalingArray[5][0] = scalingArray[5][1] = scalingArray[5][2] = 0.2f;
        float[][] rotationArray = new float[numberOfParts][3];
        float[][] translationArray = new float[numberOfParts][3];
        translationArray[2][1] = 0.5f;
        translationArray[3][1] = -1.4f;
        translationArray[3][2] = 0.4f;
        translationArray[4][0] = -0.2f;
        translationArray[4][1] = -1.65f;
        translationArray[4][2] = 0.7f;
        translationArray[5][0] = 0.2f;
        translationArray[5][1] = -1.65f;
        translationArray[5][2] = 0.7f;
        GLShapeCV bird = GLShapeFactoryCV.joinShapes("Bird",shapes,scalingArray, rotationArray,translationArray,10);
        float[] origin = { -7, -6, -4 };
        float[] target = { 10, 12, -3 };
        bird.setRotationByEulerAngles(110, 40, -70);
        bird.setTrans(origin);
        final int duration = 15000;
        GLAnimatorFactoryCV.addAnimatorTrans(bird, target, duration, 0).setInterpolator(new AccelerateInterpolator());
        GLAnimatorFactoryCV.addAnimatorRotXInModelSpace(bird,50,duration,0,false);
        surfaceView.addShape(bird);
        // TODO Thread-Erzeugung und -Start in eine GLAnimatorFactory-Methode auslagern, dabei stärkere Parametrisierung
        (new Thread() {
            public void run() {
                int runTime = 0;
                float[] vertexWing1 = verticesWing1[2];
                float[] vertexWing2 = verticesWing2[1];
                float[] vertexBeak = verticesBeak[0];
                float[] vertexTail1 = verticesTail[1];
                float[] vertexTail2 = verticesTail[2];
                float stepWingsTail = 0.3f;
                float stepBeak = 0.1f;
                while (runTime<duration) {
                    vertexWing1[2]+=stepWingsTail;
                    bird.setTriangleVertex("Wing1",2,vertexWing1);
                    vertexWing2[2]+=stepWingsTail;
                    bird.setTriangleVertex("Wing2",1,vertexWing2);
                    vertexTail1[2]-=stepWingsTail;
                    bird.setTriangleVertex("Tail",1,vertexTail1);
                    vertexTail2[2]-=stepWingsTail;
                    bird.setTriangleVertex("Tail",2,vertexTail2);
                    if (vertexWing1[2]>1.25f||vertexWing1[2]<-1.25f) stepWingsTail=-stepWingsTail;
                    bird.setTriangleVertex("BeakLower",0,vertexBeak);
                    vertexBeak[2]+=stepBeak;
                    if (vertexBeak[2]>0.5f||vertexBeak[2]<=0.05) stepBeak=-stepBeak;
                    try {
                        Thread.currentThread().sleep(50);
                        runTime += 50;
                    } catch (Exception e) {}
                }
            }
        }).start();

        GLShapeCV bird2 = bird.copy("Bird 2");
        bird2.setTrans(-4,6,-10).setRotation(-90,1,0,0);
        // bird2.setTrans(-5,10,-10).setRotationByEulerAngles(0,45,0);
        float[] control1 = { 30, 7, -8 };
        float[] control2 = { -30, 4, -6 };
        float[] target2 = { 10, 1, -4 };
        final int duration2 = 10000;
        int startDelay = 13000;
        GLAnimatorFactoryCV.addAnimatorBezierPath(bird2,control1,control2,target2,1,duration2,startDelay);
        surfaceView.addShape(bird2);
        (new Thread() {
            public void run() {
                try {
                    Thread.currentThread().sleep(startDelay);
                } catch (InterruptedException e) {}
                int runTime = 0;
                float[] vertexWing1 = verticesWing1[2];
                float[] vertexWing2 = verticesWing2[1];
                float[] vertexBeak = verticesBeak[0];
                float[] vertexTail1 = verticesTail[1];
                float[] vertexTail2 = verticesTail[2];
                float stepWingsTail = 0.3f;
                float stepBeak = 0.1f;
                while (runTime<duration2) {
                    vertexWing1[2]+=stepWingsTail;
                    bird2.setTriangleVertex("Wing1",2,vertexWing1);
                    vertexWing2[2]+=stepWingsTail;
                    bird2.setTriangleVertex("Wing2",1,vertexWing2);
                    vertexTail1[2]-=stepWingsTail;
                    bird2.setTriangleVertex("Tail",1,vertexTail1);
                    vertexTail2[2]-=stepWingsTail;
                    bird2.setTriangleVertex("Tail",2,vertexTail2);
                    if (vertexWing1[2]>1.25f||vertexWing1[2]<-1.25f) stepWingsTail=-stepWingsTail;
                    bird2.setTriangleVertex("BeakLower",0,vertexBeak);
                    vertexBeak[2]+=stepBeak;
                    if (vertexBeak[2]>0.5f||vertexBeak[2]<=0.05) stepBeak=-stepBeak;
                    try {
                        Thread.currentThread().sleep(50);
                        runTime += 50;
                    } catch (Exception e) {}
                }
            }
        }).start();

    }

    public void randomSpheres(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        int xDim = 10, yDim = 15, zDim = 5;
        float probability = 0.1f;
        int durationScaleAnim = 2000;
        int maxStartDelay = 5000;
        int numberOfRotations = 40;
        float initScale = 0.1f;
        float targetScale = 0.4f;
        float[][] colors = new float[4][];
        colors[0] = GLShapeFactoryCV.red;
        colors[1] = GLShapeFactoryCV.green;
        colors[2] = GLShapeFactoryCV.blue;
        colors[3] = GLShapeFactoryCV.yellow;
        for (int i=0; i<xDim; i++)
            for (int j=0; j<yDim; j++)
                for (int k=0; k<zDim; k++)
                   if (Math.random()<probability){
                       GLShapeCV shape = GLShapeFactoryCV.makeSphere("",4,colors);
                       shape.setTrans(-xDim/2f+0.5f+i,-yDim/2f+.5f+j,-k-2).setScale(initScale);
                       int startDelay = (int)(Math.random()*maxStartDelay);
                       GLAnimatorFactoryCV.addAnimatorScale(shape,targetScale,durationScaleAnim,0,false).setStartDelay(startDelay);
                       ObjectAnimator animRot = GLAnimatorFactoryCV.addAnimatorRotY(shape,360*numberOfRotations,durationScaleAnim*numberOfRotations/8,0,false);
                       animRot.setStartDelay(startDelay+durationScaleAnim+500);
                       animRot.setInterpolator(new AccelerateDecelerateInterpolator());
                       GLAnimatorFactoryCV.addAnimatorScale(shape,0,durationScaleAnim,0,false).setStartDelay(2*startDelay+numberOfRotations/8*durationScaleAnim+500);
                       surfaceView.addShape(shape);
                }
    }

    public void cubeOfCubes(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        // long start = System.nanoTime();
        int numberOfCubes = 5;
        boolean[][][] positions = new boolean[2*numberOfCubes-1][2*numberOfCubes-1][2*numberOfCubes-1];
        for (int i=0; i<numberOfCubes; i++)
            for (int j=0; j<numberOfCubes; j++)
                for (int k=0; k<numberOfCubes; k++)
                    positions[2*i][2*j][2*k] = true;
        GLShapeCV cubes = GLShapeFactoryCV.makeShapeFromCubes("CubeOfCubes", GLShapeFactoryCV.darkgreen,GLShapeFactoryCV.white,10,positions);
        // long duration = System.nanoTime() - start;
        // Log.v("GLDEMO",">>> joinShapes: "+duration/1000000+" ms");
        cubes.setTransZ(-2*numberOfCubes-2);
        float axis3[] = {0.57735f, 1, 0};
        int duration = 140000;
        GLAnimatorFactoryCV.addAnimatorRot(cubes, 3600, axis3, duration, 0, false);
        surfaceView.addShape(cubes);
        GLShapeCV wireframe = GLShapeFactoryCV.makeCubeWireframe("Wireframe",GLShapeFactoryCV.red,10);
        wireframe.setScale(numberOfCubes*2).setTransZ(-2*numberOfCubes-2);
        GLAnimatorFactoryCV.addAnimatorRot(wireframe, 3600, axis3, duration, 0, false);
        surfaceView.addShape(wireframe);
    }

    private void tetrahedraPattern(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        float[][] facesColors = new float[4][];
        facesColors[0] = GLShapeFactoryCV.red;
        facesColors[1] = GLShapeFactoryCV.green;
        facesColors[2] = GLShapeFactoryCV.blue;
        facesColors[3] = GLShapeFactoryCV.yellow;
        GLShapeCV tetrahedra[] = new GLShapeCV[9];
        for (int i=0;i<6;i++)
            tetrahedra[i] = GLShapeFactoryCV.makeRegularTetrahedron("Tetrahedron"+i, facesColors);
        facesColors[0] = GLShapeFactoryCV.lightred;
        facesColors[1] = GLShapeFactoryCV.lightgreen;
        facesColors[2] = GLShapeFactoryCV.lightblue;
        facesColors[3] = GLShapeFactoryCV.lightyellow;
        for (int i=6;i<9;i++)
            tetrahedra[i] = GLShapeFactoryCV.makeRegularTetrahedron("Tetrahedron"+i, facesColors);
        float transZ_1 = -(float) Math.sqrt(3.0/4);
        float transZ_2 = -(float) (Math.sqrt(3.0/4)-Math.sqrt(1.0/3));
        float[][] scaling = { {1f,1f,1f}, {1f,1f,1f}, {1f,1f,1f}, {1f,1f,1f}, {1f,1f,1f}, {1f,1f,1f}, {1f,1f,1f}, {1f,1f,1f}, {1f,1f,1f} },
                rotation = { {0f,0f,0f}, {0f,0f,0f}, {0f,0f,0f}, {0f,0f,0f}, {0f,0f,0f}, {0f,0f,0f}, {0f,180f,0f}, {0f,180f,0f}, {0f,180f,0f} },
                translation = { {-1f,0f,0f}, {0f,0f,0f}, {1f,0f,0f}, {-0.5f,0f,transZ_1}, {0.5f,0f,transZ_1}, {0f,0f,2*transZ_1}, {-0.5f,0f,transZ_2}, {0.5f,0f,transZ_2}, {0f,0f,transZ_1+transZ_2} };
        GLShapeCV joinedTetrahedra = GLShapeFactoryCV.joinShapes("joinedTetrahedra",tetrahedra,scaling,rotation,translation,10);
        joinedTetrahedra.setTransX(0.5f);
        float[] axis = { 1, 1, 0 };
        GLAnimatorFactoryCV.addAnimatorRot(joinedTetrahedra,1860, axis, 20000,0,false);
        surfaceView.addShape(joinedTetrahedra);
    }

}