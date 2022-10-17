// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 29.5.2022

// Program to demonstrate the functionality of the OpenGL utility package de.thkoeln.cvogt.android.opengl_utilities
// To be downloaded from https://github.com/CarstenVogt/AndroidOpenGLLib

package de.thkoeln.cvogt.android.opengl_utilities_demo;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
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
import de.thkoeln.cvogt.android.opengl_utilities.GLSceneFactoryCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLShapeCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLShapeFactoryCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLSurfaceViewCV;
import de.thkoeln.cvogt.android.opengl_utilities.GraphicsUtilsCV;
import de.thkoeln.cvogt.android.opengl_utilities.TextureBitmapsCV;

public class AdvancedTechniquesActivity extends Activity {

    private GLSurfaceViewCV glSurfaceView;

    private GLRendererCV renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderer = new GLRendererCV();
        // glSurfaceView = new GLSurfaceViewCV(this, renderer,false);
        glSurfaceView = new GLSurfaceViewCV(this, renderer,false);
        combinedShapes(glSurfaceView);
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
        mi.inflate(R.menu.menu_advanced, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId()==R.id.itemCombinedShapes)
            combinedShapes(glSurfaceView);
        // if (item.getItemId()==R.id.itemConnectingShapes)
        //    connectingShapes(glSurfaceView);
        if (item.getItemId()==R.id.itemRotationStatic)
            startActivity(new Intent(this,RotationActivity.class));
        if (item.getItemId()==R.id.itemRotationAnimated)
            rotatingShapesAnimated(glSurfaceView);
        if (item.getItemId()==R.id.itemPitchRollYaw)
            pitchRollYaw(glSurfaceView);
        if (item.getItemId()==R.id.itemAlignment)
            alignment(glSurfaceView);
        if (item.getItemId()==R.id.itemCircleAroundAxis)
            circleAroundAxis(glSurfaceView);
        if (item.getItemId()==R.id.itemSpiralPaths)
            spiralPath(glSurfaceView);
        if (item.getItemId()==R.id.itemBezierCurves)
            bezierCurves(glSurfaceView);
        if (item.getItemId()==R.id.itemBezierCurvesAligned)
            bezierCurvesAligned(glSurfaceView);
        if (item.getItemId()==R.id.itemTouchReaction) {
            startActivity(new Intent(this, TouchExampleActivity.class));
            return true;
        }
        if (item.getItemId()==R.id.itemScenes)
            scenes(glSurfaceView);
        if (item.getItemId()==R.id.itemBlocksWorld)
            blocksWorld(glSurfaceView);
        setContentView(glSurfaceView);
        return true;
    }

    private void combinedShapes(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        // Make a spindle from two cones
        float[] baseColor = GLShapeFactoryCV.blue;
        float[][] facesColors = new float[1][];
        facesColors[0] = GLShapeFactoryCV.orange;
        GLShapeCV shape1a = GLShapeFactoryCV.makePyramid("Shape1a",64, 1, baseColor, facesColors);
        GLShapeCV shape1b = GLShapeFactoryCV.makePyramid("Shape1b",64, 1, baseColor, facesColors);
        GLShapeCV shape1 = GLShapeFactoryCV.joinShapes("Shape1",shape1a,shape1b,1,1,1,180, 0, 0,0,1,0,0,0.5f,0);
        shape1.setScaleX(0.8f).setScaleY(1.5f).setTransX(-2.25f).setTransY(.75f).setTransZ(-3.25f);
        GLAnimatorFactoryCV.addAnimatorRotX(shape1, 360, 5000,4, true);
        // Make a bipyramid from two single pyramids (see implementation of GLShapeFactory.makeBipyramid())
        facesColors = new float[4][];
        facesColors[0] = GLShapeFactoryCV.red;
        facesColors[1] = GLShapeFactoryCV.green;
        facesColors[2] = GLShapeFactoryCV.blue;
        facesColors[3] = GLShapeFactoryCV.yellow;
        // float[][] facesColors2 = new float[2][];
        // facesColors2[0] = GLShapeFactoryCV.darkgrey;
        // facesColors2[1] = GLShapeFactoryCV.lightgrey;
        GLShapeCV shape2 = GLShapeFactoryCV.makeBipyramid("Shape2",32,1,facesColors,facesColors);
        shape2.setScale(1f,1.5f,1f).setTransX(2).setTransY(4f).setTransZ(-2);
        GLAnimatorFactoryCV.addAnimatorRotX(shape2, 360, 5000,4, true);
        // Combine a prism and a cone
        baseColor = GLShapeFactoryCV.darkgrey;
        facesColors = new float[2][];
        facesColors[0] = GLShapeFactoryCV.blue;
        facesColors[1] = GLShapeFactoryCV.lightblue;
        float heightPrism = 2f, heightPyramid = 2f;
        GLShapeCV shape3a = GLShapeFactoryCV.makePrism("Prism",12,heightPrism,baseColor,baseColor,facesColors);
        GLShapeCV shape3b = GLShapeFactoryCV.makePyramid("Pyramid",12,heightPyramid,baseColor,facesColors);
        GLShapeCV shape3 = GLShapeFactoryCV.joinShapes("House",shape3a,shape3b,1f,1,1f,0,15,0,0,(heightPrism+heightPyramid)/2,0,0,heightPrism/2,0);
        shape3.setScaleY(0.7f).setTransX(2f).setTransY(0f).setTransZ(-3f);
        GLAnimatorFactoryCV.addAnimatorRotX(shape3, 360, 5000,4, true);
        // Make a stellated octahedron from two tetrahedrons
        GLShapeCV shape4a = GLShapeFactoryCV.makeRegularTetrahedron("t1",GLShapeFactoryCV.green);
        GLShapeCV shape4b = GLShapeFactoryCV.makeRegularTetrahedron("t2",GLShapeFactoryCV.red);
        GLShapeCV shape4 = GLShapeFactoryCV.joinShapes("StellatedTetrahedron",shape4a,shape4b,1,1,1,0,60,180,0,0,0,0,0,0);
        shape4.setTransX(-1.5f).setTransY(3.5f).setScale(1.2f);
        float[] axis4 = { 1, 1, 1 };
        GLAnimatorFactoryCV.addAnimatorRot(shape4, 360, axis4, 5000,4, true);
        // Combine two frustums
        facesColors = new float[2][];
        facesColors[0] = GLShapeFactoryCV.lightred;
        facesColors[1] = GLShapeFactoryCV.red;
        float height = 1.25f;
        GLShapeCV shape5a = GLShapeFactoryCV.makeFrustum("frustum1", 8,0.5f,height,GLShapeFactoryCV.lightgreen,GLShapeFactoryCV.white, facesColors);
        GLShapeCV shape5b = GLShapeFactoryCV.makeFrustum("frustum2", 8,0.5f,height,GLShapeFactoryCV.green,GLShapeFactoryCV.white, facesColors);
        GLShapeCV shape5 = GLShapeFactoryCV.joinShapes("BiFrustum",shape5a,shape5b,1,1,1,180, 0, 0,0,height,0,0,height/2,0);
        shape5.setTransX(-1.25f).setTransY(-2.75f).setScale(0.5f);
        float[] axis5 = { 1, 0, 1 };
        GLAnimatorFactoryCV.addAnimatorRot(shape5, 360, axis5,5000,4, true);
        // Combine a number of cubes
        GLShapeCV[] shapes = new GLShapeCV[5];
        for (int i=0;i<4;i++)
            shapes[i] = GLShapeFactoryCV.makeCube("Cube 3"+i, GLShapeFactoryCV.blue,GLShapeFactoryCV.white,10);
        shapes[4] = GLShapeFactoryCV.makeCube("Cube 34", GLShapeFactoryCV.white,GLShapeFactoryCV.blue,10);
        float[][] scaling = { {1,1,1}, {1,1,1}, {1,1,1}, {1,1,1}, {1,1,1} };
        float[][] rotation = { {0,0,0}, {0,0,0}, {0,0,0}, {0,0,0}, {0,0,0} };
        float[][] translation = { {-1,0,0}, {1,0,0}, {0,1,0}, {0,-1,0}, {0,0,-0.75f} };
        GLShapeCV shape6 = GLShapeFactoryCV.joinShapes("joinedShape",shapes,scaling,rotation,translation,0);
        GLAnimatorFactoryCV.addAnimatorRotX(shape6,360,5000,4,false);
        shape6.setTransX(1.5f).setTransY(-3).setScale(0.5f);
        // Show shapes
        surfaceView.addShape(shape1);
        surfaceView.addShape(shape2);
        surfaceView.addShape(shape3);
        surfaceView.addShape(shape4);
        surfaceView.addShape(shape5);
        surfaceView.addShape(shape6);
    }

    private void connectingShapes(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        float[][] colors1 = new float[6][];
        colors1[0] = GLShapeFactoryCV.orange;
        colors1[1] = GLShapeFactoryCV.blue;
        colors1[2] = GLShapeFactoryCV.red;
        colors1[3] = GLShapeFactoryCV.green;
        colors1[4] = GLShapeFactoryCV.purple;
        colors1[5] = GLShapeFactoryCV.yellow;
        float[][] colors2 = new float[1][];
        colors2[0] = GLShapeFactoryCV.lightgrey;
        float scaleFactor = 0.4f;
        // connection with direct placement
        float[] endPoint1 = {-3f,-3f,-2};
        float[] endPoint2 = {1.5f,0f,1};
        GLShapeCV cube1 = GLShapeFactoryCV.makeCube("Cube1", colors1);
        cube1.setScale(scaleFactor).setTrans(endPoint1);
        surfaceView.addShape(cube1);
        GLShapeCV cube2 = GLShapeFactoryCV.makeCube("Cube2", colors1);
        cube2.setScale(scaleFactor).setTrans(endPoint2);
        surfaceView.addShape(cube2);
        GLShapeCV connectingShapeA = GLShapeFactoryCV.makeCuboid("connectorA", 1, 1, 1, colors2);
        connectingShapeA.placeBetweenPoints(endPoint1,endPoint2);
        connectingShapeA.setScaleX(scaleFactor/4).setScaleZ(scaleFactor/4);
        surfaceView.addShape(connectingShapeA);
        // connection with animated placement
        float[] endPoint3 = {1.5f,3f,1};
        GLShapeCV cube3 = GLShapeFactoryCV.makeCube("Cube3", colors1);
        cube3.setScale(scaleFactor).setTrans(endPoint3);
        surfaceView.addShape(cube3);
        GLShapeCV connectingShapeB = GLShapeFactoryCV.makeCuboid("connectorB", 1, 1, 1, colors2);
        // connectingShapeB.placeBetweenPoints(endPoint2,endPoint3);
        // connectingShapeB.setScaleX(scaleFactor/4).setScaleZ(scaleFactor/4);
        GLAnimatorFactoryCV.addAnimatorPlaceBetween(connectingShapeB,endPoint2,endPoint3,scaleFactor/4,GraphicsUtilsCV.distance(endPoint2,endPoint3)/connectingShapeB.getIntrinsicSizeY(),scaleFactor/4,5000);
        surfaceView.addShape(connectingShapeB);
        GLShapeCV connectingShapeC = GLShapeFactoryCV.makeCuboid("connectorC", 1, 1, 1, colors2);
        // connectingShapeC.placeBetweenPoints(endPoint1,endPoint3);
        // connectingShapeC.setScaleX(scaleFactor/4).setScaleZ(scaleFactor/4);
        GLAnimatorFactoryCV.addAnimatorPlaceBetween(connectingShapeC,endPoint3,endPoint1,scaleFactor/4,GraphicsUtilsCV.distance(endPoint3,endPoint1)/connectingShapeC.getIntrinsicSizeY(),scaleFactor/4,5000);
        surfaceView.addShape(connectingShapeC);
        // connection with animated placement
        float[] endPoint4 = {-1.5f,3f,-1};
        GLShapeCV cube4 = GLShapeFactoryCV.makeCube("Cube4", colors1);
        cube4.setScale(scaleFactor);
        GLAnimatorFactoryCV.addAnimatorTrans(cube4,endPoint4,5000,0);
        surfaceView.addShape(cube4);
        GLShapeCV connectingShapeD = GLShapeFactoryCV.makeCuboid("connectorD", 1, 1, 1, colors2);
        // connectingShapeD.placeBetweenPoints(endPoint1,endPoint4);
        // connectingShapeD.setScaleX(scaleFactor/4).setScaleZ(scaleFactor/4);
        GLAnimatorFactoryCV.addAnimatorPlaceBetween(connectingShapeD,endPoint1,endPoint4,scaleFactor/4,GraphicsUtilsCV.distance(endPoint1,endPoint4)/connectingShapeD.getIntrinsicSizeY(),scaleFactor/4,5000);
        surfaceView.addShape(connectingShapeD);
        GLShapeCV connectingShapeE = GLShapeFactoryCV.makeCuboid("connectorE", 1, 1, 1, colors2);
       //  connectingShapeE.placeBetweenPoints(endPoint3,endPoint4);
        // connectingShapeE.setScaleX(scaleFactor/4).setScaleZ(scaleFactor/4);
        GLAnimatorFactoryCV.addAnimatorPlaceBetween(connectingShapeE,endPoint3,endPoint4,scaleFactor/4,GraphicsUtilsCV.distance(endPoint3,endPoint4)/connectingShapeE.getIntrinsicSizeY(),scaleFactor/4,5000);
        surfaceView.addShape(connectingShapeE);
    }

    private void rotatingShapesAnimated(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        float[][] colors = new float[6][];
        /*
        colors[0] = GLShapeFactoryCV.orange;
        colors[1] = GLShapeFactoryCV.blue;
        colors[2] = GLShapeFactoryCV.red;
        colors[3] = GLShapeFactoryCV.green;
        colors[4] = GLShapeFactoryCV.purple;
        colors[5] = GLShapeFactoryCV.yellow; */
        Bitmap[] textures = new Bitmap[6];
        textures[0] = TextureBitmapsCV.get("front");
        textures[1] = TextureBitmapsCV.get("right");
        textures[2] = TextureBitmapsCV.get("back");
        textures[3] = TextureBitmapsCV.get("left");
        textures[4] = TextureBitmapsCV.get("top");
        textures[5] = TextureBitmapsCV.get("bottom");
        GLShapeCV[][] shapes = new GLShapeCV[4][3];
        for (int i=0; i<4; i++)
            for (int j=0; j<3; j++) {
                // shapes[i][j] = GLShapeFactoryCV.makeCube("Cube" + i + "" + j, colors);
                shapes[i][j] = GLShapeFactoryCV.makeCube("Cube" + i + "" + j, textures);
                shapes[i][j].setTrans(1.7f * (j - 1), 1f + 2f * (1 - i), 0);
            }
        float angle = 360;
        int duration = 8000, repeatcount = 4;
        // rotation around the x axis
        GLAnimatorFactoryCV.addAnimatorRotX(shapes[0][0], angle, duration, repeatcount, true) ;
        // rotation around the y axis
        GLAnimatorFactoryCV.addAnimatorRotY(shapes[0][1], angle, duration, repeatcount, true) ;
        // rotation around the z axis
        GLAnimatorFactoryCV.addAnimatorRotZ(shapes[0][2], angle, duration, repeatcount, true) ;
        // simultaneous rotation around x and y axis
        float[] axis10 = { 1, 1, 0 };
        GLAnimatorFactoryCV.addAnimatorRot(shapes[1][0], angle, axis10, duration, repeatcount, true) ;
        // simultaneous rotation around x and z axis
        float[] axis11 = { 1, 0, 1 };
        GLAnimatorFactoryCV.addAnimatorRot(shapes[1][1], angle, axis11, duration, repeatcount, true) ;
        // simultaneous rotation around y and z axis
        float[] axis12 = { 0, 1, 1 };
        GLAnimatorFactoryCV.addAnimatorRot(shapes[1][2], angle, axis12, duration, repeatcount, true) ;
        // sequential rotation around x and y axis (world coordinate space)
        GLAnimatorFactoryCV.addAnimatorRotX(shapes[2][0], 315, duration, 0, false) ;
        ObjectAnimator anim = GLAnimatorFactoryCV.addAnimatorRotY(shapes[2][0], 315, duration, 0, false) ;
        anim.setStartDelay(duration);
        // sequential rotation around x and z axis (world coordinate space)
        GLAnimatorFactoryCV.addAnimatorRotX(shapes[2][1], 315, duration, 0, false) ;
        anim = GLAnimatorFactoryCV.addAnimatorRotZ(shapes[2][1], 315, duration, 0, false) ;
        anim.setStartDelay(duration+100);
        // sequential rotation around z and y axis (world coordinate space)
        GLAnimatorFactoryCV.addAnimatorRotZ(shapes[2][2], 315, duration, 0, false) ;
        anim = GLAnimatorFactoryCV.addAnimatorRotY(shapes[2][2], 315, duration, 0, false) ;
        anim.setStartDelay(duration+100);
        // sequential rotation around x and y axis (model coordinate space)
        GLAnimatorFactoryCV.addAnimatorRotXInModelSpace(shapes[3][0], -315, duration, 0, false) ;
        anim = GLAnimatorFactoryCV.addAnimatorRotYInModelSpace(shapes[3][0], 315, duration, 0, false) ;
        anim.setStartDelay(duration);
        // sequential rotation around x and z axis (model coordinate space)
        GLAnimatorFactoryCV.addAnimatorRotX(shapes[3][1], 315, duration, 0, false) ;
        anim = GLAnimatorFactoryCV.addAnimatorRotZInModelSpace(shapes[3][1], 315, duration, 0, false) ;
        anim.setStartDelay(duration+100);
        // sequential rotation around z and y axis (model coordinate space)
        GLAnimatorFactoryCV.addAnimatorRotZInModelSpace(shapes[3][2], 315, duration, 0, false) ;
        anim = GLAnimatorFactoryCV.addAnimatorRotYInModelSpace(shapes[3][2], 315, duration, 0, false) ;
        anim.setStartDelay(duration+100);
        // pitch, roll, and yaw
        /*
        GLAnimatorFactoryCV.addAnimatorPitch(shapes[3][0],45,duration,5);
        GLAnimatorFactoryCV.addAnimatorRoll(shapes[3][1],45,duration,5);
        GLAnimatorFactoryCV.addAnimatorYaw(shapes[3][2],45,duration,5);
        */
        for (int i=0; i<4; i++)
            for (int j=0; j<3; j++)
                surfaceView.addShape(shapes[i][j]);
    }

    private void pitchRollYaw(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        float[][] colors = new float[6][];
        colors[0] = GLShapeFactoryCV.orange;
        colors[1] = GLShapeFactoryCV.blue;
        colors[2] = GLShapeFactoryCV.red;
        colors[3] = GLShapeFactoryCV.green;
        colors[4] = GLShapeFactoryCV.purple;
        colors[5] = GLShapeFactoryCV.yellow;
        int duration = 3000;
        float length_xy = 0.5f, length_z = 2;
        GLShapeCV shapePitch = GLShapeFactoryCV.makeCuboid("pitch",length_xy,length_xy,length_z,colors);
        shapePitch.setTrans(-1.5f,2f,0);
        GLAnimatorFactoryCV.addAnimatorRotY(shapePitch,45,duration,0,false);
        GLAnimatorFactoryCV.addAnimatorPitch(shapePitch,45,duration,5).setStartDelay(duration);
        surfaceView.addShape(shapePitch);
        GLShapeCV shapeRoll = GLShapeFactoryCV.makeCuboid("roll",length_xy,length_xy,length_z,colors);
        shapeRoll.setTrans(0,0,0);
        GLAnimatorFactoryCV.addAnimatorRotY(shapeRoll,45,duration,0,false);
        GLAnimatorFactoryCV.addAnimatorRoll(shapeRoll,45,duration,5).setStartDelay(duration);
        surfaceView.addShape(shapeRoll);
        GLShapeCV shapeYaw = GLShapeFactoryCV.makeCuboid("yaw",length_xy,length_xy,length_z,colors);
        shapeYaw.setTrans(1.5f,-2f,0);
        GLAnimatorFactoryCV.addAnimatorRotX(shapeYaw,45,duration,0,false);
        GLAnimatorFactoryCV.addAnimatorYaw(shapeYaw,45,duration,5).setStartDelay(duration);
        surfaceView.addShape(shapeYaw);
    }

    private void alignment(GLSurfaceViewCV surfaceView) {

        surfaceView.clearShapes();

        float[] origin1 = { -7, 8, -12 };
        float[] target1 = { 13, 16, -12 };
        float[] midpoint = new float[3];
        float[] alignmentVector = new float[3];
        for (int i=0;i<3;i++) {
            midpoint[i] = (target1[i]+origin1[i])/2f;
            alignmentVector[i] = target1[i] - origin1[i];
        }
        GLShapeCV airplane1 = GLShapeFactoryCV.makeAirplane(GLShapeFactoryCV.lightblue,GLShapeFactoryCV.blue);
        airplane1.setTrans(origin1).setScale(0.5f);
        airplane1.alignWith(2,alignmentVector,true,-33);
        int initDelay = 500;
        int dur1 = 6000;
        ObjectAnimator anim = GLAnimatorFactoryCV.addAnimatorTrans(airplane1,target1,dur1,0);
        anim.setStartDelay(initDelay);
        anim.setInterpolator(new AccelerateInterpolator());
        surfaceView.addShape(airplane1);
        GLShapeCV line1 = GLShapeFactoryCV.makeCube("Line1",GLShapeFactoryCV.orange);
        line1.setTrans(midpoint).setScale(200,0.1f,0.1f);
        line1.alignWith(0,alignmentVector);
        surfaceView.addShape(line1);

        float[] origin2 = {2,-5,-3};
        float[] target2 = {-12,6,-10};
        for (int i=0;i<3;i++) {
            midpoint[i] = (target2[i]+origin2[i])/2f;
            alignmentVector[i] = -(target2[i]-origin2[i]);
        }
        GLShapeCV airplane2 = GLShapeFactoryCV.makeAirplane(GLShapeFactoryCV.lightred,GLShapeFactoryCV.red);
        airplane2.setTrans(origin2).setScale(0.5f);
        // airplane2.setRotationByEulerAngles(GLSceneFactoryCV.randDegree(),GLSceneFactoryCV.randDegree(),GLSceneFactoryCV.randDegree());
        int durAlign = 2000;
        int durTrans = 4000;
        float[] axis = {1,1,0};
        GLAnimatorFactoryCV.addAnimatorRot(airplane2,-180,axis,2000,0,false);
        anim = GLAnimatorFactoryCV.addAnimatorAlign(airplane2,2,alignmentVector,durAlign);
        anim.setStartDelay(3000);
        anim = GLAnimatorFactoryCV.addAnimatorTrans(airplane2,target2,durTrans,0);
        anim.setStartDelay(durAlign+3000);
        anim.setInterpolator(new AccelerateInterpolator());
        surfaceView.addShape(airplane2);
        GLShapeCV line2 = GLShapeFactoryCV.makeCube("Line2",GLShapeFactoryCV.lightgreen);
        midpoint[0] += 1;
        midpoint[1] -= 1;
        line2.setTrans(midpoint).setScale(250,0.1f,0.1f);
        line2.alignWith(0,alignmentVector);
        surfaceView.addShape(line2);
        GLShapeCV line3 = GLShapeFactoryCV.makeCube("Line3",GLShapeFactoryCV.lightgreen);
        midpoint[0] -= 2;
        line3.setTrans(midpoint).setScale(250,0.1f,0.1f);
        line3.alignWith(line2);
        surfaceView.addShape(line3);

        GLShapeCV airplanes[] = new GLShapeCV[4];
        float[] pos = {3,2,-4};
        for (int i=0;i<4;i++) {
            airplanes[i] = GLShapeFactoryCV.makeAirplane(GLShapeFactoryCV.lightgreen,GLShapeFactoryCV.darkgreen);
            airplanes[i].setTrans(pos).setScale(0.2f);
            pos[0] -= 2f;
            switch (i) {
                case 0: airplanes[i].setRotationByEulerAngles(0, 90, 0); break;
                case 1: airplanes[i].setRotationByEulerAngles(90, 0, 0); break;
                case 2: airplanes[i].setRotationByEulerAngles(-90, 0, 0); break;
                case 3: airplanes[i].setRotationByEulerAngles(0, 0, 135); break;
            }
        }
        for (int i=0;i<airplanes.length;i++) {
            if (i>0) {
                anim = GLAnimatorFactoryCV.addAnimatorAlign(airplanes[i], airplanes[0], 2000);
                anim.setStartDelay(8000);
            }
            anim = GLAnimatorFactoryCV.addAnimatorTransX(airplanes[i],7+(8-2*i),3000,0);
            anim.setStartDelay(10500+i*500);
            surfaceView.addShape(airplanes[i]);
        }

    }

    private void circleAroundAxis(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        // two points to define the rotation axis
        float[] axisPoint1 = {-2.5f,0f,-3};
        float[] axisPoint2 = {1f,3f,1};
        float[][] colors = new float[1][];
        colors[0] = GLShapeFactoryCV.white;
        int noCubesOnAxis = 10;
        // mark the axis by some aligned cubes
        float scaleFactor = 0.15f;
        surfaceView.addShapes(GLSceneFactoryCV.makeCubesInLine(noCubesOnAxis,scaleFactor,axisPoint1,axisPoint2,colors,true, 0));
        colors = new float[1][];
        colors[0] = GLShapeFactoryCV.white;
        GLShapeCV connectingShape = GLShapeFactoryCV.makePrism("axis", 32, 1, colors[0], colors[0], colors);
        connectingShape.placeBetweenPoints(axisPoint1,axisPoint2);
        connectingShape.setScaleX(0.01f).setScaleZ(0.01f);
        surfaceView.addShape(connectingShape);
        // endCube1.alignWith(connectingShape);
        // endCube2.alignWith(connectingShape);
        // The rotating shape
        colors = new float[6][];
        colors[0] = GLShapeFactoryCV.orange;
        colors[1] = GLShapeFactoryCV.yellow;
        colors[2] = GLShapeFactoryCV.blue;
        colors[3] = GLShapeFactoryCV.purple;
        colors[4] = GLShapeFactoryCV.red;
        colors[5] = GLShapeFactoryCV.green;
        GLShapeCV rotShape = GLShapeFactoryCV.makeCube("Rotating Shape",colors);
        rotShape.setTrans(-1,3,0).setScale(0.5f);
        // rotShape.alignWith(connectingShape);
        ObjectAnimator anim = GLAnimatorFactoryCV.addAnimatorArcPathAroundAxis(rotShape,axisPoint1,axisPoint2,2160,20000,0);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        surfaceView.addShape(rotShape);
        // Shapes rotating around an axis
        // The second axis
        float[] axis2Point1 = { -3.5f, -4f, -3 };
        float[] axis2Point2 = { 3.5f, -4f, -3 };
        colors = new float[1][];
        colors[0] = GLShapeFactoryCV.lightblue;
        GLShapeCV axis2 = GLShapeFactoryCV.makeCuboid("Axis",axis2Point2[0]-axis2Point1[0],0.05f,0.05f, colors);
        axis2.setTrans((axis2Point2[0]+axis2Point1[0])/2,axis2Point1[1],axis2Point1[2]);
        surfaceView.addShape(axis2);
        // The rotating shapes
        colors = new float[6][];
        colors[0] = GLShapeFactoryCV.orange;
        colors[1] = GLShapeFactoryCV.yellow;
        colors[2] = GLShapeFactoryCV.blue;
        colors[3] = GLShapeFactoryCV.purple;
        colors[4] = GLShapeFactoryCV.red;
        colors[5] = GLShapeFactoryCV.green;
        float angle = 2160;
        int duration = 12000;
        GLShapeCV rotShape1 = GLShapeFactoryCV.makeCube("Rotating Shape 1",colors);
        rotShape1.setTrans(-2,-3,-3).setScale(0.7f);
        anim = GLAnimatorFactoryCV.addAnimatorArcPathAroundAxis(rotShape1,axis2Point1,axis2Point2,angle,duration,0);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        surfaceView.addShape(rotShape1);
        GLShapeCV rotShape2 = GLShapeFactoryCV.makeCube("Rotating Shape 2",colors);
        rotShape2.setTrans(0,-3,-3).setScale(0.7f);
        anim = GLAnimatorFactoryCV.addAnimatorArcPathAroundAxis(rotShape2,axis2Point1,axis2Point2,angle,duration,0);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim = GLAnimatorFactoryCV.addAnimatorRotX(rotShape2,2160, duration,0,false);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        surfaceView.addShape(rotShape2);
        GLShapeCV rotShape3 = GLShapeFactoryCV.makeCube("Rotating Shape 3",colors);
        rotShape3.setTrans(2,-3,-3).setScale(0.7f);
        anim = GLAnimatorFactoryCV.addAnimatorArcPathAroundAxis(rotShape3,axis2Point1,axis2Point2,angle,duration,0);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim = GLAnimatorFactoryCV.addAnimatorRotX(rotShape3,-2160, duration,0,false);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        surfaceView.addShape(rotShape3);
    }

    private void spiralPath(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        // the first axis
        float[] axis1Point1 = { -5f, -3f, -2 };
        float[] axis1Point2 = { 5f, 6f, -2 };
        float colors[][] = new float[1][];
        colors[0] = GLShapeFactoryCV.lightblue;
        GLShapeCV axis1 = GLShapeFactoryCV.makeCuboid("Axis1",0.05f, axis1Point2[1]-axis1Point1[1],0.05f, colors);
        axis1.placeBetweenPoints(axis1Point1,axis1Point2);
        surfaceView.addShape(axis1);
        // the first shape
        colors = new float[6][];
        colors[0] = GLShapeFactoryCV.orange;
        colors[1] = GLShapeFactoryCV.blue;
        colors[2] = GLShapeFactoryCV.red;
        colors[3] = GLShapeFactoryCV.green;
        colors[4] = GLShapeFactoryCV.purple;
        colors[5] = GLShapeFactoryCV.yellow;
        GLShapeCV shape1 = GLShapeFactoryCV.makeSphere("Sphere1", colors);
        shape1.setTransX(-5).setTransY(-1).setTransZ(-2).setScale(0.4f);
        ObjectAnimator anim = GLAnimatorFactoryCV.addAnimatorSpiralPath(shape1,axis1Point1, axis1Point2, 5,8,12000,0);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        surfaceView.addShape(shape1);
        // the second shape
        GLShapeCV shape2 = GLShapeFactoryCV.makeSphere("Sphere2", colors);
        shape2.setTransX(-4).setTransY(0).setTransZ(-2).setScale(0.4f);
        anim = GLAnimatorFactoryCV.addAnimatorSpiralPath(shape2,axis1Point1, axis1Point2, 5,10,12000,0);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        surfaceView.addShape(shape2);
        // Shapes spiralling around an axis
        // The second axis
        float[] axis2Point1 = { -5f, -4.5f, -3 };
        float[] axis2Point2 = { 5f, -4.5f, -3 };
        colors = new float[1][];
        colors[0] = GLShapeFactoryCV.lightblue;
        GLShapeCV axis2 = GLShapeFactoryCV.makeCuboid("Axis",axis2Point2[0]-axis2Point1[0],0.05f,0.05f, colors);
        axis2.setTrans((axis2Point2[0]+axis2Point1[0])/2,axis2Point1[1],axis2Point1[2]);
        surfaceView.addShape(axis2);
        // The spiralling shapes
        colors = new float[6][];
        colors[0] = GLShapeFactoryCV.orange;
        colors[1] = GLShapeFactoryCV.yellow;
        colors[2] = GLShapeFactoryCV.blue;
        colors[3] = GLShapeFactoryCV.purple;
        colors[4] = GLShapeFactoryCV.red;
        colors[5] = GLShapeFactoryCV.green;
        float angle = 2160;
        int duration = 12000;
        GLShapeCV rotShape1 = GLShapeFactoryCV.makeCube("Spiralling Shape 1",colors);
        rotShape1.setTrans(-3,-3.5f,-3).setScale(0.7f);
        anim = GLAnimatorFactoryCV.addAnimatorSpiralPath(rotShape1,axis2Point1,axis2Point2,4,4,duration,0);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        surfaceView.addShape(rotShape1);
        GLShapeCV rotShape2 = GLShapeFactoryCV.makeCube("Spiralling Shape 2",colors);
        rotShape2.setTrans(-1,-3.5f,-3).setScale(0.7f);
        anim = GLAnimatorFactoryCV.addAnimatorSpiralPath(rotShape2,axis2Point1,axis2Point2,4,4,duration,0);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim = GLAnimatorFactoryCV.addAnimatorRotX(rotShape2,2160, duration,0,false);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        surfaceView.addShape(rotShape2);
        GLShapeCV rotShape3 = GLShapeFactoryCV.makeCube("Spiralling Shape 3",colors);
        rotShape3.setTrans(1,-3.5f,-3).setScale(0.7f);
        anim = GLAnimatorFactoryCV.addAnimatorSpiralPath(rotShape3,axis2Point1,axis2Point2,4,4,duration,0);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim = GLAnimatorFactoryCV.addAnimatorRotX(rotShape3,-2160, duration,0,false);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        surfaceView.addShape(rotShape3);
    }

    private void bezierCurves(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        float[][] colors = new float[4][];
        colors[0] = GLShapeFactoryCV.red;
        colors[1] = GLShapeFactoryCV.green;
        colors[2] = GLShapeFactoryCV.blue;
        colors[3] = GLShapeFactoryCV.yellow;
        float[] control = { 0.5f, 15, -7 };
        float[] target = { 4, 2, -7 };
        for (int i=0; i<8; i++) {
            GLShapeCV shape = GLShapeFactoryCV.makeSphere("", 4, colors);
            shape.setScale(0.5f);
            shape.setTrans(-4, 2f, -7);
            ObjectAnimator anim = GLAnimatorFactoryCV.addAnimatorBezierPath(shape, control, target, 5000, i*1000);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            surfaceView.addShape(shape);
        }
        colors[0] = GLShapeFactoryCV.orange;
        colors[1] = GLShapeFactoryCV.purple;
        colors[2] = GLShapeFactoryCV.white;
        colors[3] = GLShapeFactoryCV.magenta;
        float[] control21 = { -1, 8, -50 };
        float[] control22 = { 1, -30, 4 };
        float[] target2 = { 4, -2, -7 };
        for (int i=0; i<8; i++) {
            GLShapeCV shape = GLShapeFactoryCV.makeSphere("", 4, colors);
            shape.setTrans(-4, -2f, -5);
            shape.setScale(0.5f);
            ObjectAnimator anim = GLAnimatorFactoryCV.addAnimatorBezierPath(shape, control21, control22, target2, 6000, i*1000);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            surfaceView.addShape(shape);
        }
    }

    private void bezierCurvesAligned(GLSurfaceViewCV surfaceView) {

        surfaceView.clearShapes();

        float[][] colors = new float[6][];
        colors[0] = GLShapeFactoryCV.red;
        colors[1] = GLShapeFactoryCV.green;
        colors[2] = GLShapeFactoryCV.blue;
        colors[3] = GLShapeFactoryCV.white;
        colors[4] = GLShapeFactoryCV.orange;
        colors[5] = GLShapeFactoryCV.purple;

        float[] origin1 = {-4,-2,-2};
        float[] target1 = {4,-4,-2};
        float[] control11 = { 0, 9, -2 };

        GLShapeCV shape1 = GLShapeFactoryCV.makeCuboid("",1,3,1,colors);
        shape1.setTrans(origin1).setScale(0.5f);
        GLAnimatorFactoryCV.addAnimatorBezierPath(shape1, control11, target1, 1, 3000,0);
        surfaceView.addShape(shape1);

        float[] origin2 = {-3,3,-10};
        float[] target2 = {3,-3,0};
        float[] control21 = { 10, 1, 0 };
        float[] control22 = { -10, -1, -20 };

        GLShapeCV shape2 = GLShapeFactoryCV.makeCuboid("",1,3,1,colors);
        shape2.setTrans(origin2).setScale(0.5f);
        GLAnimatorFactoryCV.addAnimatorBezierPath(shape2, control21, control22, target2, 1, 6000,3500);
        surfaceView.addShape(shape2);

    }

    private void scenes(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        float[] endPoint1 = {-1.75f,3,1};
        float[] endPoint2 = {2f,-3,-1};
        float[][] colors = new float[6][];
        colors[0] = GLShapeFactoryCV.red;
        colors[1] = GLShapeFactoryCV.blue;
        colors[2] = GLShapeFactoryCV.green;
        colors[3] = GLShapeFactoryCV.yellow;
        colors[4] = GLShapeFactoryCV.purple;
        colors[5] = GLShapeFactoryCV.orange;
        int animationDuration = 3000;
        GLShapeCV[] shapes = GLSceneFactoryCV.makeCubesInLine(10,0.2f,endPoint1,endPoint2,colors,true, animationDuration-1000);
        for (GLShapeCV shape : shapes)
            shape.setTrans(GraphicsUtilsCV.randomPoint3D(-2,2,-3,3,-5, 1));
        surfaceView.addShapes(shapes);
        // float[] center = {0,-2,0};
        // float[] perpendicularVector = {1,4,2};
        float[] perpendicularVector = new float[3];
        for (int i=0;i<3;i++)
            perpendicularVector[i] = endPoint1[i]-endPoint2[i];
        float[] center = GraphicsUtilsCV.midpoint(endPoint1,endPoint2);
        shapes = GLSceneFactoryCV.makeSpheresOnCircle(12,0.2f,center,2f,perpendicularVector, colors,animationDuration);
        for (GLShapeCV shape : shapes)
            GLAnimatorFactoryCV.addAnimatorArcPathAroundAxis(shape,endPoint1,endPoint2,1800,animationDuration*5,animationDuration+100);
        surfaceView.addShapes(shapes);
    }

    private void shapesPotpourri(GLSurfaceViewCV surfaceView) {
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
        duration = 5000;;
        float[] baseColor = GLShapeFactoryCV.darkgrey;
        float[][] facesColors = new float[2][];
        facesColors[0] = GLShapeFactoryCV.blue;
        facesColors[1] = GLShapeFactoryCV.lightblue;
        float heightPrism = 1.8f, heightPyramid = 2.7f;
        GLShapeCV shape2a = GLShapeFactoryCV.makePrism("Prism",12,heightPrism,baseColor,baseColor,facesColors);
        GLShapeCV shape2b = GLShapeFactoryCV.makePyramid("Pyramid",12,heightPyramid,baseColor,facesColors);
        GLShapeCV shape2 = GLShapeFactoryCV.joinShapes("House",shape2a,shape2b,1,1,1,0,0,0,0,(heightPrism+heightPyramid)/2,0,0,heightPrism/2,0);
        shape2.setTrans(12,25,-40);
        ObjectAnimator anim2a = GLAnimatorFactoryCV.addAnimatorRotY(shape2, 140,(duration+1000)/4,4, true);
        anim2a.setStartDelay(startDelay);
        ObjectAnimator anim2b = GLAnimatorFactoryCV.addAnimatorTrans(shape2, 0,0,-2,duration,0);
        anim2b.setStartDelay(startDelay);
        ObjectAnimator anim2c = GLAnimatorFactoryCV.addAnimatorTransX(shape2, -3.5f,1000,0);
        anim2c.setStartDelay(startDelay+duration+1000);
        ObjectAnimator anim2d = GLAnimatorFactoryCV.addAnimatorTrans(shape2, 5,-4, 1, 1000,0);
        anim2d.setStartDelay(startDelay+duration+2000);
        anim2d.setInterpolator(new AccelerateInterpolator());
        surfaceView.addShape(shape2);
        startDelay += duration+3000;
        // DEMO 3: Six combined rotating cones
        duration = 5000;
        baseColor = GLShapeFactoryCV.darkgrey;
        facesColors = new float[1][];
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
        GLShapeCV shape3 = GLShapeFactoryCV.joinShapes("Shape3",shape3a,shape3b,1,1,1,180,0,0,0,apexHeight,0);
        shape3 = GLShapeFactoryCV.joinShapes("Shape3",shape3,shape3c,1,1,1,0,0,90,apexHeight/2,apexHeight/2,0);
        shape3 = GLShapeFactoryCV.joinShapes("Shape3",shape3,shape3d,1,1,1,0,0,270,-apexHeight/2,apexHeight/2,0);
        shape3 = GLShapeFactoryCV.joinShapes("Shape3",shape3,shape3e,1,1,1,90,0,0,0,apexHeight/2,-apexHeight/2);
        shape3 = GLShapeFactoryCV.joinShapes("Shape3",shape3,shape3f,1,1,1,270,0,0,0,apexHeight/2,apexHeight/2,0,apexHeight/2,0);
        shape3.setScale(0.3f).setTransX(-12).setTransY(25).setTransZ(-40);
        ObjectAnimator anim3a = GLAnimatorFactoryCV.addAnimatorRotY(shape3, 405, duration,0, false);
        anim3a.setStartDelay(startDelay);
        ObjectAnimator anim3b = GLAnimatorFactoryCV.addAnimatorTrans(shape3,2.5f,-2,-7,duration,0);
        anim3b.setStartDelay(startDelay);
        ObjectAnimator anim3c = GLAnimatorFactoryCV.addAnimatorRotZ(shape3, -30, 1000 ,0, false);
        anim3c.setStartDelay(startDelay+duration+150);
        float axis3[] = {0.57735f,1,0};
        ObjectAnimator anim3d = GLAnimatorFactoryCV.addAnimatorRot(shape3, 36000, axis3, duration,0, false);
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
    /*
    float[][] colors = new float[1][];
    colors[0] = GLShapeFactoryCV.red;
    GLShapeCV endCube1 = GLShapeFactoryCV.makeCube("EndCube1", colors);
    endCube1.setScale(0.25f).setTrans(axisPoint1);
    endCube1.setTrans(axisPoint1);
    anim = GLAnimatorFactoryCV.addAnimatorTransZ(endCube1,-3,2000,0);
    anim.setStartDelay(startDelay);
    surfaceView.addShape(endCube1);
    colors[0] = GLShapeFactoryCV.green;
    GLShapeCV endCube2 = GLShapeFactoryCV.makeCube("EndCube2", colors);
    endCube2.setScale(0.25f).setTrans(axisPoint2);
    endCube2.setTrans(axisPoint2);
    anim = GLAnimatorFactoryCV.addAnimatorTransZ(endCube2,-3,2000,0);
    anim.setStartDelay(startDelay);
    surfaceView.addShape(endCube2);
    colors = new float[1][];
    colors[0] = GLShapeFactoryCV.lightblue;
    GLShapeCV connectingShape = GLShapeFactoryCV.makePrism("axis", 32, 1, colors[0], colors[0], colors);
    connectingShape.placeBetweenPoints(axisPoint1,axisPoint2).setScaleX(0.01f).setScaleY(0.01f);
    anim = GLAnimatorFactoryCV.addAnimatorTransZ(connectingShape,-3,2000,0);
    anim.setStartDelay(startDelay);
    surfaceView.addShape(connectingShape);
    endCube1.alignWith(connectingShape);
    endCube2.alignWith(connectingShape);
     */
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

    private void blocksWorld(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
/*
        int xDim, yDim, zDim;
        xDim = yDim = zDim = 11;
        boolean[][][] positions = new boolean[xDim][yDim][zDim];
        for (int i=0; i<xDim; i++)
            positions[i][yDim/2][zDim/2] = true;
        for (int j=0; j<yDim; j++)
            positions[xDim/2][j][zDim/2] = true;
        for (int k=0; k<yDim; k++)
            positions[xDim/2][yDim/2][k] = true;
        GLShapeCV shapes[] = GLSceneFactoryCV.makeBlocksScene(positions,0.3f,GLShapeFactoryCV.white,GLShapeFactoryCV.red,10,0);
        float[] axis1 = { -5, 5, 5};
        float[] axis2 = { 5, -5, -5};
        for (GLShapeCV shape : shapes) {
            GLAnimatorFactoryCV.addAnimatorArcPathAroundAxis(shape,axis1,axis2,360,10000,0);
            surfaceView.addShape(shape);
        }
*/
        int yDim = 8;
        boolean positionsWithCubes[][][] = new boolean[yDim*2-1][yDim][yDim];
        for (int i=0;i<yDim*2-1;i++)
            positionsWithCubes[i][yDim - 1][0] = true;
        for (int i=0;i<yDim;i++) {
            positionsWithCubes[i][yDim-1-i][0] = true;
            positionsWithCubes[yDim*2-2-i][yDim-1-i][0] = true;
        }
        for (int i=0;i<yDim;i++) {
            positionsWithCubes[i][yDim-1][i] = true;
            positionsWithCubes[yDim*2-2-i][yDim-1][i] = true;
        }
        for (int i=0;i<yDim;i++)
            positionsWithCubes[yDim-1][i][i] = true;
        int numberOfBlocks = 0;
        for (int i=0;i<yDim*2-1;i++)
            for (int j=0;j<yDim;j++)
                for (int k=0;k<yDim;k++)
                    if (positionsWithCubes[i][j][k])
                        numberOfBlocks++;
        GLShapeCV shapeFromCubes = GLShapeFactoryCV.makeShapeFromCubes("ShapeInBlocksWorld", GLShapeFactoryCV.lightblue,GLShapeFactoryCV.black,5,positionsWithCubes);
        float[] axis = {1,1,0};
        shapeFromCubes.setTransX(0.5f).setTransZ(-yDim-2);
        GLAnimatorFactoryCV.addAnimatorRot(shapeFromCubes,3645,axis,40000,0,false);
        surfaceView.addShape(shapeFromCubes);
    }

    private void cubesInCubeform(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        int xDim, yDim, zDim;
        xDim = yDim = zDim = 40;
        boolean[][][] positions = new boolean[xDim][yDim][zDim];
        for (int i=0; i<xDim; i+=2)
            for (int j=0; j<yDim; j+=2)
                for (int k=0; k<zDim; k+=2)
                    positions[i][j][k] = true;
        GLShapeCV shapes[] = GLSceneFactoryCV.makeBlocksScene(positions,0.05f,GLShapeFactoryCV.white,GLShapeFactoryCV.red,10,0);
        // GLShapeCV shapes[] = GLSceneFactoryCV.makeBlocksScene(positions,0.5f,null,GLShapeFactoryCV.red,10);
        // GLShapeCV shapes[] = GLSceneFactoryCV.makeBlocksScene(positions,3.0f/xDim,GLShapeFactoryCV.white,GLShapeFactoryCV.black,10, 0);
        // Log.v("GLDEMO",shapes.length+" shapes");
        float[] axis1 = { -5, 5, 5};
        float[] axis2 = { 5, -5, -5};
        for (GLShapeCV shape : shapes) {
            GLAnimatorFactoryCV.addAnimatorArcPathAroundAxis(shape,axis1,axis2,360,10000,0);
            surfaceView.addShape(shape);
        }

    }

    // Folgende Methode zu Testzwecken (im Vergleich zu initCubeOfCubes()): Nur ein einziger Shape, das alle Würfel umfasst > draw() ist um Größenordnungen schneller

    private void cubesInCubeform_AlternativeTestVersion(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        // int xDim = 30, yDim = 30, zDim = 30;
        int xDim, yDim, zDim;
        xDim = yDim = zDim = 40;
        boolean[][][] positions = new boolean[xDim][yDim][zDim];
        for (int i=0; i<xDim; i+=2)
            for (int j=0; j<yDim; j+=2)
                for (int k=0; k<zDim; k+=2)
                    positions[i][j][k] = true;
        // GLShapeCV shapes[] = GLSceneFactoryCV.makeBlocksScene(positions,0.5f,GLShapeFactoryCV.white,null,10);
        // GLShapeCV shapes[] = GLSceneFactoryCV.makeBlocksScene(positions,0.5f,null,GLShapeFactoryCV.red,10);
        GLShapeCV shape = GLSceneFactoryCV.makeBlocksScene2(positions,3.0f/xDim,GLShapeFactoryCV.white,GLShapeFactoryCV.black,10, 0);
        // Log.v("GLDEMO",shape.getTriangles().length+" triangles");
        float[] axis1 = { -5, 5, 5};
        float[] axis2 = { 5, -5, -5};
        // shape = GLShapeFactoryCV.makeCube("",GLShapeFactoryCV.lightblue);
        // shape.setTrans(0,0,0).setScale(2).setRotAngle(0);
        GLAnimatorFactoryCV.addAnimatorRot(shape,360, axis1,2000,0,false);
        // GLAnimatorFactoryCV.addAnimatorTrans(shape,4,3,-3,10000,0);
        surfaceView.addShape(shape);
    }

}