// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 5.10.2022

// Program to demonstrate the functionality of the OpenGL utility package de.thkoeln.cvogt.android.opengl_utilities
// To be downloaded from https://github.com/CarstenVogt/AndroidOpenGLLib

package de.thkoeln.cvogt.android.opengl_utilities_demo;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;

import javax.microedition.khronos.opengles.GL;

import de.thkoeln.cvogt.android.opengl_utilities.GLAnimatorFactoryCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLRendererCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLSceneFactoryCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLShapeCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLShapeFactoryCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLSurfaceViewCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLTriangleCV;

public class TestsActivity extends Activity {

    private GLSurfaceViewCV glSurfaceView;

    private GLRendererCV renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderer = new GLRendererCV();
        glSurfaceView = new GLSurfaceViewCV(this, renderer, false);
        makePropellerPlane(glSurfaceView);
        setContentView(glSurfaceView);
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

    public static void makePropellerPlane(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        GLShapeCV airplane = GLShapeFactoryCV.makePropellerAirplane("Propeller Plane",10000,30);
        airplane.setRotationByEulerAngles(10,45,0);
        airplane.setTransZ(-5);
        GLAnimatorFactoryCV.addAnimatorRotY(airplane,270,5000,2,true);
        surfaceView.addShape(airplane);
    }

    public static void makeAirplaneDynamic(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        final float lengthFuselage = 7;
        final float wingspan = 12;
        final float heightVerticalTail = 4;
        final float lengthVerticalTail = lengthFuselage/2.0f;
        float[][] colors = new float[2][];
        colors[0] = GLShapeFactoryCV.lightblue;
        colors[1] = GLShapeFactoryCV.blue;
        // nose
        float[][] colorFront = new float[1][];
        colorFront[0] = colors[0];
        GLShapeCV nose = GLShapeFactoryCV.makeHemisphere("Nose",3,colorFront);
        // cockpit
        float[][] colorCockpit = { GLShapeFactoryCV.lightgrey };
        GLShapeCV cockpit = GLShapeFactoryCV.makeHemisphere("Cockpit",3,colorCockpit);
        // fuselage
        GLShapeCV fuselage = GLShapeFactoryCV.makePrism("Fuselage",16,lengthFuselage,GLShapeFactoryCV.lightgrey,GLShapeFactoryCV.lightgrey,colors);
        // wings
        float[][] verticesWings = {{0,0,0},{-0.5f*wingspan,0,0.5f*lengthFuselage},{0.5f*wingspan,0,0.5f*lengthFuselage}};
        GLShapeCV wings = GLShapeFactoryCV.makeTriangle("Wings",verticesWings,colors[0],colors[1],10);
        // vertical tail
        float[][] verticesVerticalTail = {{0,0,0.5f*lengthFuselage},{0,0,0.5f*lengthFuselage-lengthVerticalTail},{0,heightVerticalTail,0.6f*lengthFuselage}};
        GLShapeCV verticalTail = GLShapeFactoryCV.makeTriangle("Vertical Tail",verticesVerticalTail,colors[0],colors[1],10);

        float yStart = 4, zStart = -15;
        nose.setTrans(-4,yStart,zStart);
        wings.setTrans(-2,yStart,zStart);
        fuselage.setTrans(0,yStart,zStart);
        cockpit.setTrans(2,yStart,zStart);
        verticalTail.setTrans(4,yStart,zStart);

        int duration = 5000;
        float zTarget = -7;

        /*
        GLAnimatorFactoryCV.addAnimatorTrans(nose,0,0,zTarget-0.5f*lengthFuselage-0.5f,duration,0);
        GLAnimatorFactoryCV.addAnimatorTrans(wings,0,0,zTarget,duration,0);
        GLAnimatorFactoryCV.addAnimatorTrans(fuselage,0,0,zTarget,duration,0);
        GLAnimatorFactoryCV.addAnimatorTrans(cockpit,0,1,zTarget-0.25f*lengthFuselage,duration,0);
        GLAnimatorFactoryCV.addAnimatorTrans(verticalTail,0,1,zTarget-0.25f*lengthFuselage,duration,0);
        GLAnimatorFactoryCV.addAnimatorRotX(nose,-90,duration,0,false);
        GLAnimatorFactoryCV.addAnimatorRotX(fuselage,90,duration,0,false);
        */

        GLAnimatorFactoryCV.addAnimatorTrans(nose,4f,0,zTarget,duration,0);
        GLAnimatorFactoryCV.addAnimatorTrans(wings,0,0,zTarget,duration,0);
        GLAnimatorFactoryCV.addAnimatorTrans(fuselage,0,0,zTarget,duration,0);
        GLAnimatorFactoryCV.addAnimatorTrans(cockpit,1.8f,1.2f,zTarget,duration,0);
        GLAnimatorFactoryCV.addAnimatorTrans(verticalTail,0f,1,zTarget,duration,0);
        GLAnimatorFactoryCV.addAnimatorRotZ(nose,-90,duration,0,false);
        GLAnimatorFactoryCV.addAnimatorRotY(wings,-90,duration,0,false);
        GLAnimatorFactoryCV.addAnimatorRotZ(fuselage,-90,duration,0,false);
        GLAnimatorFactoryCV.addAnimatorRotY(verticalTail,-90,duration,0,false);

        surfaceView.addShape(nose);
        surfaceView.addShape(wings);
        surfaceView.addShape(fuselage);
        surfaceView.addShape(cockpit);
        surfaceView.addShape(verticalTail);

        surfaceView.addShape(GLShapeFactoryCV.makeAxes().setTransZ(zTarget));

    }

    private void alignment(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        // float[] origin2 = {2,-5,-3};
        // float[] target2 = {-12,6,-10};
        float[] origin2 = {-3,6,-10};
        float[] target2 = {4,-3,-2};
        float[] midpoint = new float[3];
        float[] alignmentVector = new float[3];
        for (int i=0;i<3;i++) {
            midpoint[i] = (target2[i]+origin2[i])/2f;
            alignmentVector[i] = -(target2[i]-origin2[i]);
        }
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
        GLShapeCV airplane2 = GLShapeFactoryCV.makeJetAirplane("Plane 2",GLShapeFactoryCV.lightred,GLShapeFactoryCV.red);
        airplane2.setTrans(origin2).setScale(0.5f);
        float[] axis = {1,0,1};
        GLAnimatorFactoryCV.addAnimatorRot(airplane2,-55,axis,2000,0,false);
        ObjectAnimator anim = GLAnimatorFactoryCV.addAnimatorAlign(airplane2,2,alignmentVector,2000);
        anim.setStartDelay(2500);
        anim = GLAnimatorFactoryCV.addAnimatorTrans(airplane2,target2,2000,0);
        anim.setStartDelay(5000);
        anim.setInterpolator(new AccelerateInterpolator());
        surfaceView.addShape(airplane2);
    }

    private void alignment3(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        float[] origin1 = {2,0,-3};
        float[] target1 = {-4,10,-10};
        float[] midpoint = new float[3];
        float[] alignmentVector = new float[3];
        for (int i=0;i<3;i++) {
            midpoint[i] = (target1[i]+origin1[i])/2f;
            alignmentVector[i] = -(target1[i] - origin1[i]);
        }
        GLShapeCV airplane1 = GLShapeFactoryCV.makeJetAirplane("Plane 1",GLShapeFactoryCV.lightblue,GLShapeFactoryCV.blue);
        airplane1.setTrans(origin1).setScale(0.5f);
        // airplane1.alignWith(2,alignmentVector,true,90);
        airplane1.alignWith(2,alignmentVector,false,0);
        int initDelay = 500;
        int durTrans = 6000;
        int durAlign = 2000;
        GLAnimatorFactoryCV.addAnimatorTrans(airplane1,target1,durTrans,0).setStartDelay(initDelay);
        surfaceView.addShape(airplane1);
        GLShapeCV line1 = GLShapeFactoryCV.makeCube("Line1",GLShapeFactoryCV.orange);
        line1.setTrans(midpoint).setScale(200,0.1f,0.1f);
        line1.alignWith(0,alignmentVector);
        surfaceView.addShape(line1);

        float[] origin2 = new float[3];
        float[] target2 = new float[3];
        for (int i=0;i<3;i++) {
            origin2[i] = origin1[i];
            if (i==1) origin2[i] -=5;
            target2[i] = target1[i];
            if (i==1) target2[i] -=5;
            midpoint[i] = (target2[i]+origin2[i])/2f;
            alignmentVector[i] = -(target2[i]-origin2[i]);
        }
        GLShapeCV airplane2 = GLShapeFactoryCV.makeJetAirplane("Plane 2",GLShapeFactoryCV.lightred,GLShapeFactoryCV.red);
        airplane2.setTrans(origin2).setScale(0.5f);
        airplane2.setRotationByEulerAngles(20,80,40);
        // airplane2.alignWith(2,alignmentVector,true,90);
        GLAnimatorFactoryCV.addAnimatorAlign(airplane2,2,alignmentVector,durAlign).setStartDelay(initDelay);
        GLAnimatorFactoryCV.addAnimatorTrans(airplane2,target2,durTrans,0).setStartDelay(initDelay+durAlign); // .setStartDelay(initDelay+dur1+durAlign+500);
        surfaceView.addShape(airplane2);
        GLShapeCV line2 = GLShapeFactoryCV.makeCube("Line2",GLShapeFactoryCV.lightgreen);
        line2.setTrans(midpoint).setScale(250,0.1f,0.1f);
        line2.alignWith(0,alignmentVector);
        surfaceView.addShape(line2);

    }

    public void testDynamicViewMatrix(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        GLShapeCV shape = makeAirplaneDynamic(GLShapeFactoryCV.lightblue,GLShapeFactoryCV.blue);
        shape.setTrans(0,0,0).setRotation(225,0,1,0).setScale(0.3f);
        /*
        int duration = 3000;
        float[] axisPoint1 = {0,0,1};
        float[] axisPoint2 = {0,0,-1};
        GLAnimatorFactoryCV.addAnimatorArcPathAroundAxis(shape, axisPoint1,axisPoint2,-360,duration,0) ;
        GLAnimatorFactoryCV.addAnimatorRotXInModelSpace(shape,-360,duration,0,false);
         */
        surfaceView.addShape(shape);
        (new Thread() {
            float eyeZ = 5;
            public void run() {
                while (true) {
                    surfaceView.getRenderer().setViewMatrixValues(0,0,eyeZ,0,0,0);
                    eyeZ += 0.1;
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (Exception e) {}
                }
            }
        }).start();
    }

    private void testCombinedTriangles(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        int numberOfShapes = 4;
        GLShapeCV shapes[] = new GLShapeCV[numberOfShapes];
        float[][] vertices0 = { {-1,0,1}, {2,0,1}, {0,1,0} };
        shapes[0] = GLShapeFactoryCV.makeTriangle("Triangle0",vertices0,GLShapeFactoryCV.lightblue,GLShapeFactoryCV.blue,10);
        float[][] vertices1 = { {-1,0,-1}, {2,0,-1}, {0,1,0} };
        shapes[1] = GLShapeFactoryCV.makeTriangle("Triangle1",vertices1,GLShapeFactoryCV.lightblue,GLShapeFactoryCV.blue,10);
        float[][] vertices2 = { {-1,0,1.2f}, {0,0,1.2f}, {0,.9f,0.45f} };
        shapes[2] = GLShapeFactoryCV.makeTriangle("Triangle2",vertices2,GLShapeFactoryCV.lightred,GLShapeFactoryCV.red,10);
        float[][] vertices3 = { {-1,0,-1.2f}, {0,0,-1.2f}, {0,.9f,-0.45f} };
        shapes[3] = GLShapeFactoryCV.makeTriangle("Triangle3",vertices3,GLShapeFactoryCV.lightred,GLShapeFactoryCV.red,10);
        float[][] zeroArray = new float[numberOfShapes][3];
        GLShapeCV shape = GLShapeFactoryCV.joinShapes("Form",shapes,zeroArray,zeroArray,10);
        GLAnimatorFactoryCV.addAnimatorRotX(shape,360,4000,10,true);
        surfaceView.addShape(shape);
    }

    private void testJoinTetrahedra(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        float[][] facesColors = new float[4][];
        facesColors[0] = GLShapeFactoryCV.red;
        facesColors[1] = GLShapeFactoryCV.green;
        facesColors[2] = GLShapeFactoryCV.blue;
        facesColors[3] = GLShapeFactoryCV.yellow;
        /*
        int numberTetrahedra = 5;
        GLShapeCV tetrahedraA[] = new GLShapeCV[numberTetrahedra];
        float transY = 3, transZ = -numberTetrahedra+3;
        for (int i=0;i<numberTetrahedra;i++) {
            tetrahedraA[i] = GLShapeFactoryCV.makeRegularTetrahedron("TetrahedronA"+i, facesColors);
            tetrahedraA[i].setTrans(-numberTetrahedra/2.0f+i+.5f,transY,transZ);
            float[] axis = { 0, 1, 0 };
            if (i%2==1) {
                tetrahedraA[i].setRotation(60,axis);
                GLAnimatorFactoryCV.addAnimatorRotX(tetrahedraA[i],180,3000,0,false);
            }
            surfaceView.addShape(tetrahedraA[i]);
        }
        transY = 0;
        GLShapeCV tetrahedraB[] = new GLShapeCV[numberTetrahedra];
        for (int i=0;i<numberTetrahedra;i++) {
            tetrahedraB[i] = GLShapeFactoryCV.makeRegularTetrahedron("TetrahedronB"+i, facesColors);
            tetrahedraB[i].setTrans(-numberTetrahedra/2.0f+i+.5f,transY,transZ);
            float[] axis = { 0, 1, 0 };
            if (i%2==1) {
                tetrahedraB[i].setRotation(60,axis);
                GLAnimatorFactoryCV.addAnimatorRotX(tetrahedraB[i],180,3000,0,false);
                GLAnimatorFactoryCV.addAnimatorTransY(tetrahedraB[i],transY+(float)(0.5*Math.sqrt(2.0f/3)),3000,0);
            }
            surfaceView.addShape(tetrahedraB[i]);
        }
        transY = -3;
        /*
        GLShapeCV tetrahedraC[] = new GLShapeCV[numberTetrahedra];
        for (int i=0;i<numberTetrahedra;i++) {
            tetrahedraC[i] = GLShapeFactoryCV.makeRegularTetrahedron("TetrahedronC"+i, facesColors);
            float transX = -numberTetrahedra/2.0f+.5f*(i+1);
            tetrahedraC[i].setTrans(transX,transY,transZ);
            float[] axis = { 0, 1, 0 };
            if (i%2==1) {
                tetrahedraC[i].setRotation(60,axis);
                GLAnimatorFactoryCV.addAnimatorRotX(tetrahedraC[i],180,3000,0,false);
                GLAnimatorFactoryCV.addAnimatorTransY(tetrahedraC[i],transY+(float)(0.5*Math.sqrt(2.0f/3)),3000,0);
                // GLAnimatorFactoryCV.addAnimatorTransX(tetrahedraC[i],transX-0.5f,3000,0);
            }
            surfaceView.addShape(tetrahedraC[i]);
        } */
//        GLShapeCV axes = GLShapeFactoryCV.makeAxes();
//       axes.setTransY(-0.3876f);
//        surfaceView.addShape(axes);
        float transY = 0;
        GLShapeCV tetrahedraC[] = new GLShapeCV[9];
        for (int i=0;i<6;i++)
            tetrahedraC[i] = GLShapeFactoryCV.makeRegularTetrahedron("TetrahedronC"+i, facesColors);
        facesColors[0] = GLShapeFactoryCV.lightred;
        facesColors[1] = GLShapeFactoryCV.lightgreen;
        facesColors[2] = GLShapeFactoryCV.lightblue;
        facesColors[3] = GLShapeFactoryCV.lightyellow;
        for (int i=6;i<9;i++)
            tetrahedraC[i] = GLShapeFactoryCV.makeRegularTetrahedron("TetrahedronC"+i, facesColors);
        float transZ_1 = -(float) Math.sqrt(3.0/4);
        float transZ_2 = -(float) (Math.sqrt(3.0/4)-Math.sqrt(1.0/3));
        // transZ_2 = -.3f;
        float[][] scaling = { {1f,1f,1f}, {1f,1f,1f}, {1f,1f,1f}, {1f,1f,1f}, {1f,1f,1f}, {1f,1f,1f}, {1f,1f,1f}, {1f,1f,1f}, {1f,1f,1f} },
                rotation = { {0f,0f,0f}, {0f,0f,0f}, {0f,0f,0f}, {0f,0f,0f}, {0f,0f,0f}, {0f,0f,0f}, {0f,180f,0f}, {0f,180f,0f}, {0f,180f,0f} },
                translation = { {-1f,0f,0f}, {0f,0f,0f}, {1f,0f,0f}, {-0.5f,0f,transZ_1}, {0.5f,0f,transZ_1}, {0f,0f,2*transZ_1}, {-0.5f,0f,transZ_2}, {0.5f,0f,transZ_2}, {0f,0f,transZ_1+transZ_2} };
        GLShapeCV joinedTetrahedra = GLShapeFactoryCV.joinShapes("joined",tetrahedraC,scaling,rotation,translation,10);
        joinedTetrahedra.setTransY(transY);
        float[] axis = {0,1,1};
        GLAnimatorFactoryCV.addAnimatorRot(joinedTetrahedra,-90,  axis,4000,0,false);
        GLAnimatorFactoryCV.addAnimatorTransX(joinedTetrahedra,-2,4000,0);
        surfaceView.addShape(joinedTetrahedra);
    }

    private void testPyramid(GLSurfaceViewCV surfaceView) {
        float[][] colors = new float[4][];
        colors[0] = GLShapeFactoryCV.red;
        colors[1] = GLShapeFactoryCV.green;
        colors[2] = GLShapeFactoryCV.blue;
        colors[3] = GLShapeFactoryCV.yellow;
        GLShapeCV pyramid1 = GLShapeFactoryCV.makePyramid("Pyramid1", 64, 2, GLShapeFactoryCV.white, colors);
        pyramid1.setTransX(-1).setScale(1.5f);
        // hemisphere.moveCenterTo(0,.5f,0);
        // hemisphere1.setTransY((float)(4*Math.sin(Math.PI/10))/2).setTransZ(0).setScale(1);
        surfaceView.addShape(pyramid1);
        GLShapeCV pyramid2 = GLShapeFactoryCV.makeBipyramid("Pyramid2", 64, 2, colors, colors);
        pyramid2.setTransX(1).setScale(1.5f);
        // hemisphere.moveCenterTo(0,.5f,0);
        // hemisphere1.setTransY((float)(4*Math.sin(Math.PI/10))/2).setTransZ(0).setScale(1);
        float[] axis = {1, 0, 0};
        GLAnimatorFactoryCV.addAnimatorRot(pyramid2,180,axis, 2000,0,false);
        surfaceView.addShape(pyramid2);
        GLShapeCV coordAxes = GLShapeFactoryCV.makeAxes();
        surfaceView.addShape(coordAxes);
    }

    private void testHemispheres(GLSurfaceViewCV surfaceView) {
        float[][] colors = new float[4][];
        colors[0] = GLShapeFactoryCV.red;
        colors[1] = GLShapeFactoryCV.green;
        colors[2] = GLShapeFactoryCV.blue;
        colors[3] = GLShapeFactoryCV.yellow;
        GLShapeCV hemisphere1 = GLShapeFactoryCV.makeHemisphere("Hemisphere1", 4, colors);
        // hemisphere.moveCenterTo(0,.5f,0);
        // hemisphere1.setTransY((float)(4*Math.sin(Math.PI/10))/2).setTransZ(0).setScale(1);
        hemisphere1.setTransY(0.5f);
        float[] axis = {0, 1, 0};
        GLAnimatorFactoryCV.addAnimatorRot(hemisphere1,360,axis, 4000,2,false);
        surfaceView.addShape(hemisphere1);
        GLShapeCV hemisphere2 = GLShapeFactoryCV.makeHemisphere("Hemisphere2", 4, colors);
        // hemisphere.moveCenterTo(0,.5f,0);
        // hemisphere2.setTransY(-(float)(4*Math.sin(Math.PI/10))/2).setTransZ(0).setScale(1).setRotation(180,axis);
        float[] axis2 = {1, 0, 0};
        hemisphere2.setTransY(-0.5f).setTransZ(0).setScale(1).setRotation(180,axis2);
        surfaceView.addShape(hemisphere2);
        GLShapeCV coordAxes = GLShapeFactoryCV.makeAxes();
        surfaceView.addShape(coordAxes);
    }

    private void testJoinTwoTetrahedra(GLSurfaceViewCV surfaceView) {
        // TODO Die Seitenflächen sind zwar schon parallel, aber der Abstand stimmt nicht
        surfaceView.clearShapes();
        float[][] facesColors = new float[4][];
        facesColors[0] = GLShapeFactoryCV.red;
        facesColors[1] = GLShapeFactoryCV.green;
        facesColors[2] = GLShapeFactoryCV.blue;
        facesColors[3] = GLShapeFactoryCV.yellow;
        float[] axis = { 1, 0, 0 };
        GLShapeCV shape1 = GLShapeFactoryCV.makeRegularTetrahedron("Tetrahedron1",facesColors);
//        shape1.setScale(1.5f,1.5f,1.5f).setTransX(-1.5f);
        // GLAnimatorFactoryCV.addAnimatorRot(shape1, 360, axis,6000,10, true) ;
//        surfaceView.addShape(shape1);
        GLShapeCV shape2 = GLShapeFactoryCV.makeRegularTetrahedron("Tetrahedron2",facesColors);
//        shape2.setScale(1.5f,1.5f,1.5f).setTransX(1.5f);
//        GLAnimatorFactoryCV.addAnimatorRot(shape2, 360, axis,6000,10, true) ;
        GLShapeCV joinedShape = GLShapeFactoryCV.joinShapes("joined",shape1,shape2,1,1,1,-38.2496f,180,0,0f,0f,1f);
        joinedShape.setScale(2.5f).setTransZ(-2);
        GLAnimatorFactoryCV.addAnimatorRot(joinedShape, 360, axis,10000,1, true) ;
        surfaceView.addShape(joinedShape);
        GLShapeCV axes = GLShapeFactoryCV.makeAxes();
//       axes.setTransY(-0.3876f);
//        surfaceView.addShape(axes);
    }

    private void testBlocksScene(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        int xDim = 30, yDim = 30, zDim = 30;
        boolean[][][] positions = new boolean[xDim][yDim][zDim];
        for (int i=0; i<xDim; i+=2)
            for (int j=0; j<yDim; j+=2)
                for (int k=0; k<zDim; k+=2)
                    positions[i][j][k] = true;

        // GLShapeCV shapes[] = GLSceneFactoryCV.makeBlocksScene(positions,0.5f,GLShapeFactoryCV.white,null,10);
        // GLShapeCV shapes[] = GLSceneFactoryCV.makeBlocksScene(positions,0.5f,null,GLShapeFactoryCV.red,10);
        GLShapeCV shapes[] = GLSceneFactoryCV.makeBlocksScene(positions,0.1f,GLShapeFactoryCV.white,GLShapeFactoryCV.black,10, 0);
        float[] axis1 = { -5, 5, 5};
        float[] axis2 = { 5, -5, -5};
        for (GLShapeCV shape : shapes) {
            GLAnimatorFactoryCV.addAnimatorArcPathAroundAxis(shape,axis1,axis2,360,10000,0);
            surfaceView.addShape(shape);
        }
        /*
        // GLShapeCV shape1 = GLShapeFactoryCV.makeCuboidWireframe("Shape1",2,2,2,GLShapeFactoryCV.red,10);
        // GLShapeCV shape2 = GLShapeFactoryCV.makeCuboidWireframe("Shape2",1,1,1,GLShapeFactoryCV.green,10);
        GLShapeCV shape1 = GLShapeFactoryCV.makeCuboid("Shape1",2,2,2,GLShapeFactoryCV.red,GLShapeFactoryCV.orange,10);
        // GLShapeCV shape2 = GLShapeFactoryCV.makeCube("Shape2",GLShapeFactoryCV.green,GLShapeFactoryCV.blue,20);
        GLShapeCV shape2 = GLShapeFactoryCV.makeCube("Shape2",GLShapeFactoryCV.green);
        GLShapeCV shape = GLShapeFactoryCV.joinShapes("Joined",shape1,shape2,1,1,1,0,0,0,-1,1,1);
        // surfaceView.addShape(shape1);
        // surfaceView.addShape(shape2);
        float[] axis = {1,1,0};
        GLAnimatorFactoryCV.addAnimatorRot(shape, 360, axis,6000,10, true) ;
        surfaceView.addShape(shape);
        */

        // float lineColors[][] = { GLShapeFactoryCV.red, GLShapeFactoryCV.orange, GLShapeFactoryCV.blue, GLShapeFactoryCV.green };
        // GLShapeCV shape = new GLShapeCV("TTT",null,GLShapeFactoryCV.linesForWireframeCuboid(-1, -1, -1, 2, 2, 2),lineColors,10);

        /* addLines(): Add two separate lines to a cube
        GLShapeCV shape = GLShapeFactoryCV.makeCube("Cube",GLShapeFactoryCV.lightblue);
        float[] axis = {1,1,0};
        float point1[] = {-0.5f,0.5f,0.6f};
        float point2[] = {0.5f,-0.5f,0.6f};
        float point3[] = {-0.5f,0.5f,-0.6f};
        float point4[] = {0.5f,-0.5f,-0.6f};
        GLLineCV newLine1 = new GLLineCV("",point1,point2,GLShapeFactoryCV.green);
        GLLineCV newLine2 = new GLLineCV("",point3,point4,GLShapeFactoryCV.red);
        GLLineCV newLines[] = { newLine1, newLine2};
        shape.addLines(newLines);
        GLAnimatorFactoryCV.addAnimatorRot(shape, 360, axis,6000,10, true) ;
        surfaceView.addShape(shape);
         */

        // GLShapeCV cross = GLShapeFactoryCV.makeAxes();
        // surfaceView.addShape(cross);

    }

    private void cubeConsistingOfFourTetrahedra(GLSurfaceViewCV surfaceView) {
        // This method generates a cube from four tetrahedra.
        // Each tetrahedron has three edges of length 1 that correspond to three edges of the cube meeting at a corner of the cube.
        // Hence, the three angles of the tetrahedron's sides that meet at this corner are rectangular.
        // The further edges of a tetragon, i.e. the respective third edges of the tetrahedron's sides,
        // correspond to the diagonals of the cube's sides.
        surfaceView.clearShapes();
        GLTriangleCV[] trianglesForTetrahedron = new GLTriangleCV[4];
        // Specify the four sides of a tetrahedron
        float[][] vertices0 = {{0,0,0},{1,0,0},{0,0,-1}};
        trianglesForTetrahedron[0] = new GLTriangleCV("side0",vertices0,GLShapeFactoryCV.red);
        float[][] vertices1 =  {{0,0,0},{1,0,0},{0,1,0}};
        trianglesForTetrahedron[1] = new GLTriangleCV("side1",vertices1,GLShapeFactoryCV.yellow);
        float[][] vertices2 =  {{0,0,0},{0,1,0},{0,0,-1}};
        trianglesForTetrahedron[2] = new GLTriangleCV("side2",vertices2,GLShapeFactoryCV.green);
        float[][] vertices3 =  {{0,1,0},{1,0,0},{0,0,-1}};
        trianglesForTetrahedron[3] = new GLTriangleCV("side3",vertices3,GLShapeFactoryCV.blue);
        // Generate the four tetrahedra and combine them to a cube
        GLShapeCV tetrahedron1 = new GLShapeCV("tetrahedron1",trianglesForTetrahedron);
        GLShapeCV tetrahedron2 = new GLShapeCV("tetrahedron2",trianglesForTetrahedron);
        GLShapeCV cubePartA = GLShapeFactoryCV.joinShapes("CubePartA",tetrahedron1,tetrahedron2,1,1,1,
                0,180,0,1f,0,-1f);
        GLShapeCV tetrahedron3 = new GLShapeCV("tetrahedron3",trianglesForTetrahedron);
        GLShapeCV tetrahedron4 = new GLShapeCV("tetrahedron4",trianglesForTetrahedron);
        GLShapeCV cubePartB = GLShapeFactoryCV.joinShapes("CubePartB",tetrahedron3,tetrahedron4,1,1,1,
                0,180,0,1,0,-1f);
        GLShapeCV cube = GLShapeFactoryCV.joinShapes("Cube",cubePartA,cubePartB,1,1,1,
                180,0,0,0,1,-1);
        // show the animated cube
        float[] axis = {1,1,0};
        GLAnimatorFactoryCV.addAnimatorRot(cube,360,axis,4000,1,true);
        surfaceView.addShape(cube);

    }
    // Method to make an airplane pointing into the negative z direction, i.e. straight away from the camera

    private GLShapeCV makeAirplaneDynamic(float[] faceColor, float[] lineColor) {
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
    }

}