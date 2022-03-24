// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 22.3.2022

// Program to demonstrate the functionality of the OpenGL untility package de.thkoeln.cvogt.android.opengl_utilities

package de.thkoeln.cvogt.android.opengl_utilities_demo;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

import de.thkoeln.cvogt.android.opengl_utilities.*;

public class MainActivity extends Activity {

    private GLSurfaceViewCV glSurfaceView;

    private GLRendererCV renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextureBitmapsCV.init(this);
        renderer = new GLRendererCV();
        // glSurfaceView = new GLSurfaceViewCV(this, renderer,false);
        glSurfaceView = new GLSurfaceViewCV(this, renderer,false);
        // Für animierte Darstellungen: glSurfaceView = new GLSurfaceViewCV(this, renderer, false);
        initShapesPotpourri(glSurfaceView);
        //  initTest(glSurfaceView);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = new MenuInflater(this);
        mi.inflate(R.menu.menu_selectdemo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId()==R.id.itemColoredSquares)
            initShapesColoredSquares(glSurfaceView);
        if (item.getItemId()==R.id.itemTexturedSquares)
            initShapesTexturedSquares(glSurfaceView);
        if (item.getItemId()==R.id.itemColoredCubes)
            initShapesColoredCubes(glSurfaceView);
        if (item.getItemId()==R.id.itemColoredCuboids)
            initShapesColoredCuboids(glSurfaceView);
        if (item.getItemId()==R.id.itemTexturedCubes)
            initShapesTexturedCubes(glSurfaceView);
        if (item.getItemId()==R.id.itemColoredPolygons)
            initShapesColoredPolygons(glSurfaceView);
        if (item.getItemId()==R.id.itemColoredPyramids)
            initShapesColoredPyramids(glSurfaceView);
        if (item.getItemId()==R.id.itemColoredPrisms)
            initShapesColoredPrisms(glSurfaceView);
        if (item.getItemId()==R.id.itemSpheres)
            initShapesSpheres(glSurfaceView);
        if (item.getItemId()==R.id.itemCombinedShapes)
            initCombinedShapes(glSurfaceView);
        if (item.getItemId()==R.id.itemShapeRotation)
            initRotatingShapes(glSurfaceView);
        if (item.getItemId()==R.id.itemPaths)
            initShapesPath(glSurfaceView);
        if (item.getItemId()==R.id.itemRotationAxis)
            initRotationAroundAxis(glSurfaceView);
        if (item.getItemId()==R.id.itemPotpourri)
            initShapesPotpourri(glSurfaceView);
        if (item.getItemId()==R.id.itemTouchReaction) {
            startActivity(new Intent(this, TouchExampleActivity.class));
            return true;
        }
        if (item.getItemId()==R.id.itemTest)
            initTest(glSurfaceView);
        setContentView(glSurfaceView);
        return true;
    }

    private void initTest(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        surfaceView.showAxes();
        float[] baseColor = GLShapeFactoryCV.orange;
        float[][] facesColors = new float[6][];
        facesColors[0] = GLShapeFactoryCV.red;
        facesColors[1] = GLShapeFactoryCV.blue;
        facesColors[2] = GLShapeFactoryCV.green;
        facesColors[3] = GLShapeFactoryCV.yellow;
        facesColors[4] = GLShapeFactoryCV.purple;
        facesColors[5] = GLShapeFactoryCV.orange;
        float leftFrontUpperCorner_X = -1/2.0f;
        float leftFrontUpperCorner_Y = 1/2.0f;
        float leftFrontUpperCorner_Z = 1/2.0f;
        GLShapeCV shape = new GLShapeCV("Cube",GLShapeFactoryCV.trianglesForColoredCuboid(leftFrontUpperCorner_X, leftFrontUpperCorner_Y, leftFrontUpperCorner_Z, 1, 1, 1, facesColors));
        shape.setScale(2);
        float[] rotAxis = {1,0,0};
        shape.setRotAxis(rotAxis);
        shape.setRotAngle(70).setTransX(2).setTransY(2);
        // GLAnimatorFactoryCV.addAnimatorRot(shape,53,2000,0,true);
        surfaceView.addShape(shape);
    }

    private void initShapesColoredSquares(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        GLShapeCV shape1 = GLShapeFactoryCV.makeSquare("Square 1", GLShapeFactoryCV.orange, GLShapeFactoryCV.blue);
        shape1.setScale(2,2,0).setTrans(-1.5f,2.5f,0);
        shape1.setRotAxis(1,0,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape1, 360,8000,10, true);
        GLShapeCV shape2 = GLShapeFactoryCV.makeSquare("Square 2", GLShapeFactoryCV.red);
        shape2.setScale(2,2,0).setTrans(1.5f,1.5f,0);
        shape2.setRotAxis(0,1,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape2, 360,4000,20, true);
        GLShapeCV shape3 = GLShapeFactoryCV.makeSquare("Square 3", GLShapeFactoryCV.yellow, GLShapeFactoryCV.purple);
        shape3.setScale(2,2,0).setTrans(0,-1.5f,0);
        shape3.setRotAxis(0,0,1);
        GLAnimatorFactoryCV.addAnimatorRot(shape3, 360,2000,40, true);
        surfaceView.addShape(shape1);
        surfaceView.addShape(shape2);
        surfaceView.addShape(shape3);
    }

    private void initShapesTexturedSquares(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        GLShapeCV shape1 = GLShapeFactoryCV.makeSquare("Square 1",TextureBitmapsCV.get("raster"));
        shape1.setScale(2,2,0).setTrans(-1.5f,2.5f,0);
        shape1.setRotAxis(0,1,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape1, 360,8000,10, true) ;
        GLShapeCV shape2 = GLShapeFactoryCV.makeSquare("Square 2", TextureBitmapsCV.get("dice05"));
        shape2.setScale(2,2,0).setTrans(1.5f,1.5f,0);
        shape2.setRotAxis(1,0,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape2, 360,8000,10, true) ;
        GLShapeCV shape3 = GLShapeFactoryCV.makeSquare("Square 3", TextureBitmapsCV.get("logo_thk"));
        shape3.setScale(2,2,0).setTrans(0,-1,0);
        shape3.setRotAxis(0,1,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape3, 360,4000,10, true) ;
        surfaceView.addShape(shape1);
        surfaceView.addShape(shape2);
        surfaceView.addShape(shape3);
    }

    private void initShapesColoredCubes(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        GLShapeCV shape;
        float colors[][] = new float[12][];
        colors[0] = GLShapeFactoryCV.orange;
        colors[1] = GLShapeFactoryCV.blue;
        colors[2] = GLShapeFactoryCV.red;
        colors[3] = GLShapeFactoryCV.green;
        colors[4] = GLShapeFactoryCV.white;
        colors[5] = GLShapeFactoryCV.yellow;
        colors[6] = GLShapeFactoryCV.red;
        colors[7] = GLShapeFactoryCV.green;
        colors[8] = GLShapeFactoryCV.white;
        colors[9] = GLShapeFactoryCV.yellow;
        colors[10] = GLShapeFactoryCV.magenta;
        colors[11] = GLShapeFactoryCV.cyan;
        shape = GLShapeFactoryCV.makeCube("Cube 1",colors);
        shape.setScale(1.5f,1.5f,1.5f).setTrans(1f,2,1);
        shape.setRotAxis(0,1,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape, 360,8000,10, true) ;
        surfaceView.addShape(shape);
        colors = new float[6][];
        colors[0] = GLShapeFactoryCV.orange;
        colors[1] = GLShapeFactoryCV.blue;
        colors[2] = GLShapeFactoryCV.red;
        colors[3] = GLShapeFactoryCV.green;
        colors[4] = GLShapeFactoryCV.purple;
        colors[5] = GLShapeFactoryCV.yellow;
        shape = GLShapeFactoryCV.makeCube("Cube 2", colors);
        shape.setScale(1,1,1).setTrans(-1.5f,0,0);
        shape.setRotAxis(1,1,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape, 360,4000,20, true) ;
        surfaceView.addShape(shape);
        shape = GLShapeFactoryCV.makeCube("Cube 3", GLShapeFactoryCV.blue);
        shape.setScale(1.25f,1.25f,1.25f).setTrans(1.5f,-2.5f,0);
        shape.setRotAxis(1,1,1);
        GLAnimatorFactoryCV.addAnimatorRot(shape, 360,2000,40, true) ;
        surfaceView.addShape(shape);
    }

    private void initShapesColoredCuboids(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        GLShapeCV shape1 = GLShapeFactoryCV.makeCuboid("Cube 1",4,3,1, GLShapeFactoryCV.blue);
        shape1.setTrans(1,-3,-3);
        GLShapeCV shape2 = GLShapeFactoryCV.makeCuboid("Cube 2",3,1.5f,1, GLShapeFactoryCV.orange);
        shape2.setTrans(-3f,-2,0);
        GLAnimatorFactoryCV.addAnimatorTrans(shape2, 4, -3.5f, -7, 3000, 6);
        shape2.setRotAxis(0,1,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape2, 360,2000,6, true) ;
        float colors[][] = new float[12][];
        colors[0] = GLShapeFactoryCV.orange;
        colors[1] = GLShapeFactoryCV.blue;
        colors[2] = GLShapeFactoryCV.red;
        colors[3] = GLShapeFactoryCV.green;
        colors[4] = GLShapeFactoryCV.white;
        colors[5] = GLShapeFactoryCV.yellow;
        colors[6] = GLShapeFactoryCV.red;
        colors[7] = GLShapeFactoryCV.green;
        colors[8] = GLShapeFactoryCV.white;
        colors[9] = GLShapeFactoryCV.yellow;
        colors[10] = GLShapeFactoryCV.magenta;
        colors[11] = GLShapeFactoryCV.cyan;
        GLShapeCV shape3 = GLShapeFactoryCV.makeCuboid("Cube 3",1, 2, 1.5f, colors);
        shape3.setTrans(0,1.5f,1);
        shape3.setRotAxis(1,0,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape3, 360,8000,10, true) ;
        surfaceView.addShape(shape1);
        surfaceView.addShape(shape2);
        surfaceView.addShape(shape3);
    }

    private void initShapesTexturedCubes(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        GLShapeCV shape;
        Bitmap[] textures = new Bitmap[6];
        for (int i=0; i<6; i++)
            textures[i] = TextureBitmapsCV.get("raster");
        shape = GLShapeFactoryCV.makeCube("Cube 1", textures);
        shape.setScale(2.5f,2.5f,2.5f).setTrans(0,3, -2);
        // GLAnimatorFactoryCV.makeAnimatorRotX(shape,8000,10);
        surfaceView.addShape(shape);
        textures[0] = TextureBitmapsCV.get("dice04");
        textures[1] = TextureBitmapsCV.get("dice05");
        textures[2] = TextureBitmapsCV.get("dice03");
        textures[3] = TextureBitmapsCV.get("dice02");
        textures[4] = TextureBitmapsCV.get("dice06");
        textures[5] = TextureBitmapsCV.get("dice01");
        shape = GLShapeFactoryCV.makeCube("Cube 2", textures);
        shape.setScale(2.5f,2.5f,2.5f).setTrans(-4f,-2, -2);
        ObjectAnimator anim1 = GLAnimatorFactoryCV.addAnimatorTrans(shape, 4, 12, -18, 8000, 3);
        // anim1.setInterpolator(new AccelerateDecelerateInterpolator());
        shape.setRotAxis(0,1,0);
        ObjectAnimator anim2 = GLAnimatorFactoryCV.addAnimatorRot(shape, 360,8000,10, true) ;
        // anim2.setInterpolator(new AccelerateDecelerateInterpolator());
        surfaceView.addShape(shape);
    }

    private void initShapesColoredPolygons(GLSurfaceViewCV surfaceView) {
        int duration = 5000;
        surfaceView.clearShapes();
        GLShapeCV shape1 = GLShapeFactoryCV.makePolygon("Polygon 1",4, GLShapeFactoryCV.blue);
        shape1.setScale(1,1,0).setTrans(-1.5f,3f,1);
        shape1.setRotAxis(1,0,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape1, 360,duration,10, true) ;
        GLShapeCV shape2 = GLShapeFactoryCV.makePolygon("Polygon 2",5, GLShapeFactoryCV.orange);
        shape2.setScale(0.75f,0.75f,0).setTrans(1f,2.5f,1);
        shape2.setRotAxis(0,0,1);
        GLAnimatorFactoryCV.addAnimatorRot(shape2, 360,duration,10, true) ;
        GLShapeCV shape3 = GLShapeFactoryCV.makePolygon("Polygon 3",6, GLShapeFactoryCV.green);
        shape3.setScale(0.75f,0.75f,0).setTrans(-1.25f,1f,1);
        shape3.setRotAxis(1,0,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape3, 360,duration,10, true) ;
        float colors[][] = new float[3][];
        colors[0] = GLShapeFactoryCV.red;
        colors[1] = GLShapeFactoryCV.green;
        colors[2] = GLShapeFactoryCV.blue;
        GLShapeCV shape4 = GLShapeFactoryCV.makePolygon("Polygon 4", 8, colors);
        shape4.setScale(0.75f,0.75f,0).setTrans(1f,0f,1);
        shape4.setRotAxis(1,0,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape4, 360,duration,10, true) ;
        colors = new float[2][];
        colors[0] = GLShapeFactoryCV.lightgrey;
        colors[1] = GLShapeFactoryCV.darkgrey;
        GLShapeCV shape5 = GLShapeFactoryCV.makePolygon("Polygon 5", 64, colors);
        shape5.setScale(0.1f,0.1f,0).setTrans(-0.5f,-2.25f,1);
        shape5.setRotAxis(0,1,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape5, 360,duration,10, true) ;
        surfaceView.addShape(shape1);
        surfaceView.addShape(shape2);
        surfaceView.addShape(shape3);
        surfaceView.addShape(shape4);
        surfaceView.addShape(shape5);
    }

    private void initShapesColoredPyramids(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        float[] baseColor = GLShapeFactoryCV.orange;
        float[][] facesColors = new float[4][];
        facesColors[0] = GLShapeFactoryCV.red;
        facesColors[1] = GLShapeFactoryCV.green;
        facesColors[2] = GLShapeFactoryCV.blue;
        facesColors[3] = GLShapeFactoryCV.yellow;
        GLShapeCV shape1 = GLShapeFactoryCV.makePyramid("Pyramid 1",4, 1, baseColor, facesColors);
        Log.v("GLDEMO", "Pyramid 4 sides: " + shape1.getTriangles().length);
        shape1.setScale(1.5f,1.5f,1.5f).setTrans(-1.25f,3f,0);
        shape1.setRotAxis(1,0,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape1, 360,8000,10, true) ;
        surfaceView.addShape(shape1);
        baseColor = GLShapeFactoryCV.green;
        facesColors = new float[2][];
        facesColors[0] = GLShapeFactoryCV.darkgreen;
        facesColors[1] = GLShapeFactoryCV.lightgreen;
        GLShapeCV shape2 = GLShapeFactoryCV.makePyramid("Pyramid 2",32, 10f, baseColor, facesColors);
        shape2.setScale(0.2f,0.2f,0.2f).setTrans(1f,0.5f,0);
        shape1.setRotAxis(0,1,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape2, 360,8000,10, true) ;
        surfaceView.addShape(shape2);
        baseColor = GLShapeFactoryCV.green;
        facesColors = new float[2][];
        facesColors[0] = GLShapeFactoryCV.orange;
        facesColors[1] = GLShapeFactoryCV.blue;
        GLShapeCV shape3 = GLShapeFactoryCV.makePyramid("Pyramid 3",12, 3, baseColor, facesColors);
        shape3.setScale(0.5f,0.5f,0.5f).setTrans(-1f,-2f,0);
        shape3.setRotAxis(1,0,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape3, 360,6000,10, true) ;
        surfaceView.addShape(shape3);
        facesColors = new float[4][];
        facesColors[0] = GLShapeFactoryCV.red;
        facesColors[1] = GLShapeFactoryCV.green;
        facesColors[2] = GLShapeFactoryCV.blue;
        facesColors[3] = GLShapeFactoryCV.yellow;
        GLShapeCV shape4 = GLShapeFactoryCV.makeRegularTetrahedron("Tetrahedron",facesColors);
        shape4.setScale(1.5f,1.5f,1.5f).setTrans(1.5f,-2.5f,0);
        shape4.setRotAxis(1,1,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape4, 360,6000,10, true) ;
        surfaceView.addShape(shape4);
    }

    private void initShapesColoredPrisms(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        float[] baseColor = GLShapeFactoryCV.orange;
        float[] topColor = GLShapeFactoryCV.blue;
        float[][] facesColors = new float[4][];
        facesColors[0] = GLShapeFactoryCV.red;
        facesColors[1] = GLShapeFactoryCV.green;
        facesColors[2] = GLShapeFactoryCV.blue;
        facesColors[3] = GLShapeFactoryCV.yellow;
        GLShapeCV shape1 = GLShapeFactoryCV.makePrism("Prism 1",6, 1, baseColor, topColor, facesColors);
        shape1.setScale(1f,1f,1f).setTrans(-1.25f,3f,0);
        shape1.setRotAxis(1,0,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape1, 360,8000,10, true) ;
        surfaceView.addShape(shape1);
        baseColor = GLShapeFactoryCV.white;
        topColor = GLShapeFactoryCV.white;
        facesColors = new float[2][];
        facesColors[0] = GLShapeFactoryCV.darkgrey;
        facesColors[1] = GLShapeFactoryCV.lightgrey;
        GLShapeCV shape2 = GLShapeFactoryCV.makePrism("Prism 2",32, 20f, baseColor, topColor, facesColors);
        shape2.setScale(0.15f,0.15f,0.15f).setTrans(1f,0f,0);
        shape2.setRotAxis(1,1,1);
        ObjectAnimator anim = GLAnimatorFactoryCV.addAnimatorRot(shape2, 3600,16000,0, false);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        surfaceView.addShape(shape2);
        baseColor = GLShapeFactoryCV.green;
        topColor = GLShapeFactoryCV.green;
        facesColors = new float[2][];
        facesColors[0] = GLShapeFactoryCV.orange;
        facesColors[1] = GLShapeFactoryCV.blue;
        GLShapeCV shape3 = GLShapeFactoryCV.makePrism("Prism 3",12, 3, baseColor, topColor, facesColors);
        shape3.setScale(0.5f,0.5f,0.5f).setTrans(-1f,-3f,0);
        shape3.setRotAxis(1,0,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape3, 360,6000,10, true) ;
        surfaceView.addShape(shape3);
    }

    private void initShapesSpheres(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
//    GLShapeCV shape = GLShapeFactoryCV.makeSphere("Sphere", GLShapeFactoryCV.yellow);
        float[][] colors = new float[4][];
        colors[0] = GLShapeFactoryCV.red;
        colors[1] = GLShapeFactoryCV.green;
        colors[2] = GLShapeFactoryCV.blue;
        colors[3] = GLShapeFactoryCV.yellow;
        for (int i=0; i<6; i++) {
            GLShapeCV shape = GLShapeFactoryCV.makeSphere("Sphere", i, colors);
            shape.setScaleY(1).setTrans(-2.5f+2.5f*(i%3),3-2.5f*(i/3),-2);
            shape.setRotAxis(1,1,1);
            GLAnimatorFactoryCV.addAnimatorRot(shape, 360, 3000, 4, true);
            // shape.setRotAngleX(90);
            // shape2.setScale(.5,.5,.5).setRotAngleX(10).setRotAngleY(-40).setRotAngleZ(-40).setTrans(0f,0,.8f);
            surfaceView.addShape(shape);
        }
    }

    private void initCombinedShapes(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        // Make a spindle from two cones
        float[] baseColor = GLShapeFactoryCV.blue;
        float[][] facesColors = new float[1][];
        facesColors[0] = GLShapeFactoryCV.orange;
        float height = 20;
        GLShapeCV shape1a = GLShapeFactoryCV.makePyramid("Shape1a",64, 20f, baseColor, facesColors);
        GLShapeCV shape1b = GLShapeFactoryCV.makePyramid("Shape1b",64, 20f, baseColor, facesColors);
        GLShapeCV shape1 = GLShapeFactoryCV.joinShapes("Shape1",shape1a,shape1b,1,1,1,180, 0, 0,0,height,0,0,height/2,0);
        shape1.setScale(0.1f).setTransX(-1.5f).setTransY(-1).setTransZ(-1);
        shape1.setRotAxis(1,0,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape1, 360,5000,4, true);
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
        shape2.setScaleX(0.2f).setScaleY(1.5f).setScaleZ(0.2f).setTransX(1.5f).setTransY(3).setTransZ(-1);
        shape2.setRotAxis(1,0,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape2, 360,5000,4, true);
        // Combine a prism and a cone
        baseColor = GLShapeFactoryCV.darkgrey;
        facesColors = new float[2][];
        facesColors[0] = GLShapeFactoryCV.blue;
        facesColors[1] = GLShapeFactoryCV.lightblue;
        float heightPrism = 2f, heightPyramid = 3f;
        GLShapeCV shape3a = GLShapeFactoryCV.makePrism("Prism",12,heightPrism,baseColor,baseColor,facesColors);
        GLShapeCV shape3b = GLShapeFactoryCV.makePyramid("Pyramid",12,heightPyramid,baseColor,facesColors);
        GLShapeCV shape3 = GLShapeFactoryCV.joinShapes("House",shape3a,shape3b,1,1,1,0,0,0,0,(heightPrism+heightPyramid)/2,0,0,heightPrism/2,0);
        shape3.setScale(0.5f).setTransX(1.5f).setTransY(-2.5f).setTransZ(-1.5f);
        shape3.setRotAxis(1,0,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape3, 360,5000,4, true);
        // Make a stellated octahedron from two tetrahedrons
        GLShapeCV shape4a = GLShapeFactoryCV.makeRegularTetrahedron("t1",GLShapeFactoryCV.green);
        GLShapeCV shape4b = GLShapeFactoryCV.makeRegularTetrahedron("t2",GLShapeFactoryCV.red);
        GLShapeCV shape4 = GLShapeFactoryCV.joinShapes("StellatedTetrahedron",shape4a,shape4b,1,1,1,0,60,180,0,0,0,0,0,0);
        shape4.setTransX(-1.5f).setTransY(3).setScale(1.5f);
        shape4.setScale(1.5f);
        shape4.setRotAxis(1,1,1);
        GLAnimatorFactoryCV.addAnimatorRot(shape4, 360,5000,4, true);
        // Show shapes
        surfaceView.addShape(shape1);
        surfaceView.addShape(shape2);
        surfaceView.addShape(shape3);
        surfaceView.addShape(shape4);
    }

    private void initRotatingShapes(GLSurfaceViewCV surfaceView) {
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
            }
        float angle = 360;
        int duration = 4000, repeatcount = 4;
        // rotation around the x axis
        GLAnimatorFactoryCV.addAnimatorRotX(shapes[0][0], angle, duration, repeatcount, true) ;
        // rotation around the y axis
        GLAnimatorFactoryCV.addAnimatorRotY(shapes[0][1], angle, duration, repeatcount, true) ;
        // rotation around the z axis
        GLAnimatorFactoryCV.addAnimatorRotZ(shapes[0][2], angle, duration, repeatcount, true) ;
        // rotation around x and y axis
        shapes[1][0].setRotAxis(1,1,0);
        GLAnimatorFactoryCV.addAnimatorRot(shapes[1][0], angle, duration, repeatcount, true) ;
        // rotation around x and z axis
        shapes[1][1].setRotAxis(1,0,1);
        GLAnimatorFactoryCV.addAnimatorRot(shapes[1][1], angle, duration, repeatcount, true) ;
        // rotation around y and z axis
        shapes[1][2].setRotAxis(0,1,1);
        GLAnimatorFactoryCV.addAnimatorRot(shapes[1][2], angle, duration, repeatcount, true) ;
        // rotation around x, y and z axis
        shapes[2][0].setRotAxis(-1,1,1);
        GLAnimatorFactoryCV.addAnimatorRot(shapes[2][0], angle, duration, repeatcount, true) ;
        // rotation around x, y and z axis
        shapes[2][1].setRotAxis(1,1,1);
        GLAnimatorFactoryCV.addAnimatorRot(shapes[2][1], angle, duration, repeatcount, true) ;
        // rotation around x, y and z axis
        shapes[2][2].setRotAxis(1,-1,1);
        GLAnimatorFactoryCV.addAnimatorRot(shapes[2][2], angle, duration, repeatcount, true) ;
        for (int i=0; i<3; i++)
            for (int j=0; j<3; j++)
                surfaceView.addShape(shapes[i][j]);
    }

    private void initShapesPath(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        float[][] colors = new float[6][];
        colors[0] = GLShapeFactoryCV.orange;
        colors[1] = GLShapeFactoryCV.blue;
        colors[2] = GLShapeFactoryCV.red;
        colors[3] = GLShapeFactoryCV.green;
        colors[4] = GLShapeFactoryCV.purple;
        colors[5] = GLShapeFactoryCV.yellow;
        GLShapeCV shape1 = GLShapeFactoryCV.makeCube("Cube1", colors);
        shape1.setTransX(2).setTransZ(-2).setScale(0.7f);
        GLAnimatorFactoryCV.addAnimatorArcPathInVerticalPlane(shape1,0,0,270,true,6000,0);
        // ObjectAnimator anim = GLAnimatorFactoryCV.addAnimatorRotZ(shape1,-270, 4000, 0, false);
        surfaceView.addShape(shape1);
        GLShapeCV shape2 = GLShapeFactoryCV.makeCube("Cube2", colors);
        shape2.setTransY(-2).setTransZ(-3).setScale(0.7f);
        GLAnimatorFactoryCV.addAnimatorSpiralPath(shape2,0,-1,7,3,false,12000,0);
        surfaceView.addShape(shape2);
        // Shapes rotating around an axis
        // The axis
        float[] axisPoint1 = { -3.5f, -5f, -3 };
        float[] axisPoint2 = { 3.5f, -5f, -3 };
        colors = new float[1][];
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
        float angle = 2160;
        int duration = 12000;
        GLShapeCV rotShape1 = GLShapeFactoryCV.makeCube("Rotating Shape 1",colors);
        rotShape1.setTrans(-2,-4,-3).setScale(0.7f);
        GLAnimatorFactoryCV.addAnimatorArcPathAroundAxis(rotShape1,axisPoint1,axisPoint2,angle,false,duration,0);
        surfaceView.addShape(rotShape1);
        GLShapeCV rotShape2 = GLShapeFactoryCV.makeCube("Rotating Shape 2",colors);
        rotShape2.setTrans(0,-4,-3).setScale(0.7f);
        GLAnimatorFactoryCV.addAnimatorArcPathAroundAxis(rotShape2,axisPoint1,axisPoint2,angle,false,duration,0);
        shape2.setRotAxis(1,0,0);
        GLAnimatorFactoryCV.addAnimatorRot(rotShape2,2160,duration,0,false);
        surfaceView.addShape(rotShape2);
        GLShapeCV rotShape3 = GLShapeFactoryCV.makeCube("Rotating Shape 3",colors);
        rotShape3.setTrans(2,-4,-3).setScale(0.7f);
        GLAnimatorFactoryCV.addAnimatorArcPathAroundAxis(rotShape3,axisPoint1,axisPoint2,angle,false,duration,0);
        rotShape3.setRotAxis(1,0,0);
        GLAnimatorFactoryCV.addAnimatorRot(rotShape3,-2160,duration,0,false);
        surfaceView.addShape(rotShape3);
    }

    private void initRotationAroundAxis(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        // two points to define the rotation axis
        float[] axisPoint1 = {-1.5f,-1.5f,-1};
        float[] axisPoint2 = {1.5f,1.5f,1};
        float[][] colors = new float[1][];
        colors[0] = GLShapeFactoryCV.white;
        int noCubesOnAxis = 10;
        float scaleFactor = 0.15f;
        float diffX = axisPoint2[0]-axisPoint1[0],
                diffY = axisPoint2[1]-axisPoint1[1],
                diffZ = axisPoint2[2]-axisPoint1[2];
        for (int i=0; i<noCubesOnAxis; i++) {
            GLShapeCV cube = GLShapeFactoryCV.makeCube("Cube"+i, colors);
            cube.setScale(scaleFactor).setTrans(axisPoint1[0]+diffX/noCubesOnAxis*i,axisPoint1[1]+diffY/noCubesOnAxis*i,axisPoint1[2]+diffZ/noCubesOnAxis*i);
            surfaceView.addShape(cube);
        }
        GLShapeCV endCube = GLShapeFactoryCV.makeCube("EndCube", colors);
        endCube.setScale(scaleFactor).setTrans(axisPoint2);
        endCube.setTrans(axisPoint2);
        surfaceView.addShape(endCube);
    /*
    colors = new float[1][];
    colors[0] = GLShapeFactoryCV.white;
    GLShapeCV connectingShape = GLShapeFactoryCV.makePrism("axis", 32, 1, colors[0], colors[0], colors);
    connectingShape.placeBetweenPoints(axisPoint1,axisPoint2).setScaleX(0.01f).setScaleY(0.01f);
    surfaceView.addShape(connectingShape);
    endCube1.alignWith(connectingShape);
    endCube2.alignWith(connectingShape);
     */
        // The rotating shape
        colors = new float[6][];
        colors[0] = GLShapeFactoryCV.orange;
        colors[1] = GLShapeFactoryCV.yellow;
        colors[2] = GLShapeFactoryCV.blue;
        colors[3] = GLShapeFactoryCV.purple;
        colors[4] = GLShapeFactoryCV.red;
        colors[5] = GLShapeFactoryCV.green;
        GLShapeCV rotShape = GLShapeFactoryCV.makeCube("Rotating Shape",colors);
        rotShape.setTrans(-1,2,0).setScale(0.5f);
        // rotShape.alignWith(connectingShape);
        GLAnimatorFactoryCV.addAnimatorArcPathAroundAxis(rotShape,axisPoint1,axisPoint2,2160,false,20000,0);
        surfaceView.addShape(rotShape);
    }

    private void initShapesPotpourri(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        // DEMO 1: Three rotating cubes
        int duration = 3000;
        // cube 1
        Bitmap[] textures = new Bitmap[6];
        for (int i=0; i<6; i++)
            textures[i] = TextureBitmapsCV.get("raster");
        GLShapeCV shape1a = GLShapeFactoryCV.makeCube("Cube1", textures);
        shape1a.setScale(1.5f,1.5f,1.5f).setTransZ(-9);
        shape1a.setRotAxis(1,0,0);
        GLAnimatorFactoryCV.addAnimatorRot(shape1a, 1080, 3*duration, 0, true);
        GLAnimatorFactoryCV.addAnimatorTrans(shape1a, -2f, 0,-1, duration, 0);
        ObjectAnimator anim = GLAnimatorFactoryCV.addAnimatorTrans(shape1a, -4, -5,5, duration, 0);
        anim.setStartDelay(3*duration);
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
        shape1b.setRotAxis(0,1,0);
        anim = GLAnimatorFactoryCV.addAnimatorRot(shape1b, 360, duration, 0, true);
        anim.setStartDelay(2*duration);
        anim = GLAnimatorFactoryCV.addAnimatorTrans(shape1b, 0, 0,-1, duration, 0);
        anim.setStartDelay(2*duration);
        anim = GLAnimatorFactoryCV.addAnimatorTrans(shape1b, 0,-5, 5, duration, 0);
        anim.setStartDelay(3*duration);
        surfaceView.addShape(shape1b);
        // cube 3
        for (int i=0; i<6; i++)
            textures[i] = TextureBitmapsCV.get("logo_thk");
        GLShapeCV shape1c = GLShapeFactoryCV.makeCube("Cube3", textures);
        shape1c.setScale(1.5f,1.5f,1.5f).setTransZ(-10);
        shape1c.setRotAxis(0,0,1);
        anim = GLAnimatorFactoryCV.addAnimatorRot(shape1c, 720, 2*duration, 0, true);
        anim.setStartDelay(duration);
        anim = GLAnimatorFactoryCV.addAnimatorTrans(shape1c, 2f, 0,-1, duration, 0);
        anim.setStartDelay(duration);
        anim = GLAnimatorFactoryCV.addAnimatorTrans(shape1c, 2,-5, 5, duration, 0);
        anim.setStartDelay(3*duration);
        surfaceView.addShape(shape1c);
      /*
      int noShapes = 3;
      GLShapeCV shapes1[] = new GLShapeCV[noShapes];
      float colors[][] = new float[12][];
      colors[0] = GLShapeFactoryCV.orange;
      colors[1] = GLShapeFactoryCV.blue;
      colors[2] = GLShapeFactoryCV.red;
      colors[3] = GLShapeFactoryCV.green;
      colors[4] = GLShapeFactoryCV.white;
      colors[5] = GLShapeFactoryCV.yellow;
      colors[6] = GLShapeFactoryCV.red;
      colors[7] = GLShapeFactoryCV.green;
      colors[8] = GLShapeFactoryCV.white;
      colors[9] = GLShapeFactoryCV.yellow;
      colors[10] = GLShapeFactoryCV.magenta;
      colors[11] = GLShapeFactoryCV.cyan;
      for (int i=0;i<noShapes;i++) {
        shapes1[i] = GLShapeFactoryCV.makeCube("", colors);
        shapes1[i].setScale(1.5f,1.5f,1.5f).setTrans(0,0,-10);
      }
      int duration = 3000;
      ObjectAnimator animators[][] = new ObjectAnimator[noShapes][3];
      for (int i=0;i<noShapes;i++) {
        switch (i) {
          case 0: animators[i][0] = GLAnimatorFactoryCV.makeAnimatorRotX(shapes1[i], 360, duration, 0, true); break;
          case 1: animators[i][0] = GLAnimatorFactoryCV.makeAnimatorRotY(shapes1[i], 360, duration, 0, true); break;
          case 2: animators[i][0] = GLAnimatorFactoryCV.makeAnimatorRotZ(shapes1[i], 360, duration, 0, true); break;
        }
        animators[i][0].setStartDelay(i*duration/2);
        animators[i][1] = GLAnimatorFactoryCV.makeAnimatorTrans(shapes1[i], -2+2*i, 0,-1, duration, 0);
        animators[i][1].setStartDelay(i*duration/2);
        animators[i][2] = GLAnimatorFactoryCV.makeAnimatorTrans(shapes1[i], -4+4*i, -3,5, duration, 0);
        animators[i][2].setStartDelay(2*duration);
    }
    for (int i=0;i<noShapes;i++)
      surfaceView.addShape(shapes1[i]);
       */
        // DEMO 2: A prism with a pyramid on top
        int startDelay = 3*duration-1000;
        duration = 5000;;
        float[] baseColor = GLShapeFactoryCV.darkgrey;
        float[][] facesColors = new float[2][];
        facesColors[0] = GLShapeFactoryCV.blue;
        facesColors[1] = GLShapeFactoryCV.lightblue;
        float heightPrism = 2f, heightPyramid = 3f;
        GLShapeCV shape2a = GLShapeFactoryCV.makePrism("Prism",12,heightPrism,baseColor,baseColor,facesColors);
        GLShapeCV shape2b = GLShapeFactoryCV.makePyramid("Pyramid",12,heightPyramid,baseColor,facesColors);
        GLShapeCV shape2 = GLShapeFactoryCV.joinShapes("House",shape2a,shape2b,1,1,1,0,0,0,0,(heightPrism+heightPyramid)/2,0,0,heightPrism/2,0);
        shape2.setTrans(12,25,-40);
        shape2.setRotAxis(0,1,0);
        ObjectAnimator anim2a = GLAnimatorFactoryCV.addAnimatorRot(shape2, 140,(duration+1000)/4,4, true);
        anim2a.setStartDelay(startDelay);
        ObjectAnimator anim2b = GLAnimatorFactoryCV.addAnimatorTrans(shape2, 0,0,-1,duration,0);
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
        shape3.setRotAxis(0,1,0);
        ObjectAnimator anim3a = GLAnimatorFactoryCV.addAnimatorRot(shape3, 405,duration,0, false);
        anim3a.setStartDelay(startDelay);
        ObjectAnimator anim3b = GLAnimatorFactoryCV.addAnimatorTrans(shape3,2.5f,-2,-5,duration,0);
        anim3b.setStartDelay(startDelay);
        ObjectAnimator anim3c = GLAnimatorFactoryCV.addAnimatorRot(shape3, 36000,duration,0, false);
        anim3c.setInterpolator(new AccelerateInterpolator());
        anim3c.setStartDelay(startDelay+duration+300);
        ObjectAnimator anim3e = GLAnimatorFactoryCV.addAnimatorTrans(shape3,-20,50,-20,duration,0);
        anim3e.setStartDelay(startDelay+duration+1500);
        anim3e.setInterpolator(new AccelerateInterpolator());
        surfaceView.addShape(shape3);
        startDelay += 3*duration-6000;
        // DEMO 4: Shapes rotating around an axis
        float[] axisPoint1 = { -3f, -5f, -3 };
        float[] axisPoint2 = { 3f, -5f, -3 };
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
        rotShape1.setTrans(-2,-4,-3).setScale(0.3f);
        GLAnimatorFactoryCV.addAnimatorArcPathAroundAxis(rotShape1,axisPoint1,axisPoint2,angle,false,duration,startDelay);
        surfaceView.addShape(rotShape1);
        GLShapeCV rotShape2 = GLShapeFactoryCV.makeCube("Rotating Shape 2",colors);
        rotShape2.setTrans(0,-4,-3).setScale(0.3f);
        GLAnimatorFactoryCV.addAnimatorArcPathAroundAxis(rotShape2,axisPoint1,axisPoint2,angle,false,duration,startDelay);
        rotShape2.setRotAxis(1,0,0);
        anim = GLAnimatorFactoryCV.addAnimatorRot(rotShape2,2160,duration,0,false);
        anim.setStartDelay(startDelay);
        surfaceView.addShape(rotShape2);
        GLShapeCV rotShape3 = GLShapeFactoryCV.makeCube("Rotating Shape 3",colors);
        rotShape3.setTrans(2,-4,-3).setScale(0.3f);
        GLAnimatorFactoryCV.addAnimatorArcPathAroundAxis(rotShape3,axisPoint1,axisPoint2,angle,false,duration,startDelay);
        rotShape3.setRotAxis(1,0,0);
        anim = GLAnimatorFactoryCV.addAnimatorRot(rotShape3,-2160,duration,0,false);
        anim.setStartDelay(startDelay);
        surfaceView.addShape(rotShape3);
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
  private void initRotationAroundAxis2(GLSurfaceViewCV surfaceView) {
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
  private void initRotatingShapes(GLSurfaceViewCV surfaceView) {
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

}