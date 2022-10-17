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
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import java.util.Date;

import de.thkoeln.cvogt.android.opengl_utilities.GLAnimatorFactoryCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLRendererCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLShapeCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLShapeFactoryCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLSurfaceViewCV;
import de.thkoeln.cvogt.android.opengl_utilities.GLTriangleCV;
import de.thkoeln.cvogt.android.opengl_utilities.TextureBitmapsCV;

public class BasicShapesActivity extends Activity {

    private GLSurfaceViewCV glSurfaceView;

    private GLRendererCV renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderer = new GLRendererCV();
        // glSurfaceView = new GLSurfaceViewCV(this, renderer,false);
        glSurfaceView = new GLSurfaceViewCV(this, renderer,false);
        coloredTriangles(glSurfaceView);
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
        mi.inflate(R.menu.menu_basicshapes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId()==R.id.itemColoredTriangles)
            coloredTriangles(glSurfaceView);
        if (item.getItemId()==R.id.itemColoredSquares)
            coloredSquares(glSurfaceView);
        if (item.getItemId()==R.id.itemTexturedSquares)
            texturedSquares(glSurfaceView);
        if (item.getItemId()==R.id.itemColoredCubes)
            coloredCubes(glSurfaceView);
        if (item.getItemId()==R.id.itemColoredCuboids)
            coloredCuboids(glSurfaceView);
        if (item.getItemId()==R.id.itemTexturedCubes)
            texturedCubes(glSurfaceView);
        if (item.getItemId()==R.id.itemColoredPolygons)
            coloredPolygons(glSurfaceView);
        if (item.getItemId()==R.id.itemPyramidsConesTetrahedra)
            pyramidsConesTetrahedra(glSurfaceView);
        if (item.getItemId()==R.id.itemPrismsFrustumsCylinders)
            prismsFrustumsCylinders(glSurfaceView);
        if (item.getItemId()==R.id.itemSpheres)
            spheres(glSurfaceView);
        setContentView(glSurfaceView);
        return true;
    }

    private void coloredTriangles(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        float[][] vertices1234 = { {-1,-1,0},{1,-1,0},{0,1,0} };
        GLShapeCV triangle1 = GLShapeFactoryCV.makeTriangle("Triangle 1", vertices1234,GLShapeFactoryCV.red);
        triangle1.setTrans(-1.5f,2.5f,0);
        GLAnimatorFactoryCV.addAnimatorRotX(triangle1, 360,8000,10, true);
        surfaceView.addShape(triangle1);
        GLShapeCV triangle2 = GLShapeFactoryCV.makeTriangle("Triangle 2", vertices1234,GLShapeFactoryCV.red,GLShapeFactoryCV.blue,20);
        triangle2.setTrans(1.5f,2.5f,0);
        GLAnimatorFactoryCV.addAnimatorRotX(triangle2, 360,8000,10, true);
        surfaceView.addShape(triangle2);
        float[][] colors34 = { GLShapeFactoryCV.darkgrey, GLShapeFactoryCV.darkgrey, GLShapeFactoryCV.lightgrey };
        float[][] lineColors3 = { GLShapeFactoryCV.blue, GLShapeFactoryCV.red, GLShapeFactoryCV.green };
        GLShapeCV triangle3 = GLShapeFactoryCV.makeTriangle("Triangle 3", vertices1234, colors34, lineColors3, 20);
        triangle3.setTrans(1.5f,0f,0);
        GLAnimatorFactoryCV.addAnimatorRotX(triangle3, 360,8000,10, true);
        surfaceView.addShape(triangle3);
        GLShapeCV triangle4 = GLShapeFactoryCV.makeTriangle("Triangle 4", vertices1234, colors34, null, 0);
        triangle4.setTrans(-1.5f,0f,0);
        GLAnimatorFactoryCV.addAnimatorRotX(triangle4, 360,8000,10, true);
        surfaceView.addShape(triangle4);
        float[][] vertices5 = { {-.5f,-.5f,0},{.5f,-1,0},{2.5f,.5f,0} };
        GLShapeCV triangle5 = GLShapeFactoryCV.makeTriangle("Triangle 5", vertices5, GLShapeFactoryCV.lightblue, GLShapeFactoryCV.blue, 15);
        triangle5.setTrans(-1f,-2.5f,0);
        GLAnimatorFactoryCV.addAnimatorRotX(triangle5, 360,8000,10, true);
        surfaceView.addShape(triangle5);
    }

    private void coloredSquares(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        GLShapeCV shape1 = GLShapeFactoryCV.makeSquare("Square 1", GLShapeFactoryCV.orange, GLShapeFactoryCV.blue);
        shape1.setScale(2,2,0).setTrans(-1.5f,2.5f,0);
        GLAnimatorFactoryCV.addAnimatorRotX(shape1, 360,8000,10, true);
        GLShapeCV shape2 = GLShapeFactoryCV.makeSquare("Square 2", GLShapeFactoryCV.red);
        shape2.setScale(2,2,0).setTrans(1.5f,1.5f,0);
        GLAnimatorFactoryCV.addAnimatorRotY(shape2, 360,4000,20, true);
        GLShapeCV shape3 = GLShapeFactoryCV.makeSquare("Square 3", GLShapeFactoryCV.yellow, GLShapeFactoryCV.purple);
        shape3.setScale(2,2,0).setTrans(0,-1.5f,0);
        GLAnimatorFactoryCV.addAnimatorRotZ(shape3, 360,2000,40, true);
        surfaceView.addShape(shape1);
        surfaceView.addShape(shape2);
        surfaceView.addShape(shape3);
    }

    private void texturedSquares(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        GLShapeCV shape1 = GLShapeFactoryCV.makeSquare("Square 1",TextureBitmapsCV.get("raster"));
        shape1.setScale(2,2,0).setTrans(-1.5f,2.5f,0);
        GLAnimatorFactoryCV.addAnimatorRotX(shape1, 360, 8000,10, true) ;
        GLShapeCV shape2 = GLShapeFactoryCV.makeSquare("Square 2", TextureBitmapsCV.get("dice05"));
        shape2.setScale(2,2,0).setTrans(1.5f,1.5f,0);
        GLAnimatorFactoryCV.addAnimatorRotY(shape2, 360,8000,10, true) ;
        GLShapeCV shape3 = GLShapeFactoryCV.makeSquare("Square 3", TextureBitmapsCV.get("logo_thk"));
        shape3.setScale(2,2,0).setTrans(0,-1,0);
        GLAnimatorFactoryCV.addAnimatorRotZ(shape3, 360,4000,10, true) ;
        surfaceView.addShape(shape1);
        surfaceView.addShape(shape2);
        surfaceView.addShape(shape3);
    }

    private void coloredCubes(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        GLShapeCV shape, shapeWireframe;
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
        shape.setScale(1.25f).setTrans(0.5f,2,1);
        GLAnimatorFactoryCV.addAnimatorRotY(shape, 360,8000,10, true) ;
        surfaceView.addShape(shape);
        colors = new float[6][];
        colors[0] = GLShapeFactoryCV.orange;
        colors[1] = GLShapeFactoryCV.blue;
        colors[2] = GLShapeFactoryCV.red;
        colors[3] = GLShapeFactoryCV.green;
        colors[4] = GLShapeFactoryCV.purple;
        colors[5] = GLShapeFactoryCV.yellow;
        shape = GLShapeFactoryCV.makeCube("Cube 2", colors);
        shape.setScale(1).setTrans(-1.5f,.5f,0);
        float[] axis2 = { 1, 1, 0 };
        GLAnimatorFactoryCV.addAnimatorRot(shape, 360, axis2,4000,20, true) ;
        surfaceView.addShape(shape);
        shape = GLShapeFactoryCV.makeCube("Cube 3", GLShapeFactoryCV.blue,GLShapeFactoryCV.white,10);
        shape.setScale(1.25f).setTrans(1.25f,-0.5f,0);
        float[] axis3 = { 1, 1, 1 };
        GLAnimatorFactoryCV.addAnimatorRot(shape, 360, axis3,3000,30, true) ;
        surfaceView.addShape(shape);
        shapeWireframe = GLShapeFactoryCV.makeCubeWireframe("CubeWire 4", GLShapeFactoryCV.green, 12);
        shapeWireframe.setTrans(-1.75f,-2.5f,0);
        GLAnimatorFactoryCV.addAnimatorTransX(shapeWireframe, 0,2000,20);
        GLAnimatorFactoryCV.addAnimatorRotY(shapeWireframe, 360,2000,20, false);
        surfaceView.addShape(shapeWireframe);
        shapeWireframe = GLShapeFactoryCV.makeCubeWireframe("CubeWire 5", GLShapeFactoryCV.red, 12);
        shapeWireframe.setTrans(0f,-2.5f,0);
        GLAnimatorFactoryCV.addAnimatorRotY(shapeWireframe, -360,2000,20, false);
        GLAnimatorFactoryCV.addAnimatorTransX(shapeWireframe, -1.5f,2000,20);
        surfaceView.addShape(shapeWireframe);
    }

    private void coloredCuboids(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        GLShapeCV shape1 = GLShapeFactoryCV.makeCuboid("Cube 1",4,3,1, GLShapeFactoryCV.blue,GLShapeFactoryCV.white,10);
        shape1.setTrans(1,-3,-3);
        GLAnimatorFactoryCV.addAnimatorScaleX(shape1,1.5f,3000,1,true);
        GLAnimatorFactoryCV.addAnimatorScaleY(shape1,1.5f,3000,1,true).setStartDelay(6000);
        GLAnimatorFactoryCV.addAnimatorScaleZ(shape1,4f,3000,1,true).setStartDelay(9000);
        GLShapeCV shape2 = GLShapeFactoryCV.makeCuboid("Cube 2",3,1.5f,1, GLShapeFactoryCV.orange);
        shape2.setTrans(-3f,-2,0);
        GLAnimatorFactoryCV.addAnimatorTrans(shape2, 6, -3.5f, -7, 3000, 6);
        GLAnimatorFactoryCV.addAnimatorRotY(shape2, 360, 3000,6, true) ;
        float colorFaces[][] = new float[12][];
        colorFaces[0] = GLShapeFactoryCV.orange;
        colorFaces[1] = GLShapeFactoryCV.blue;
        colorFaces[2] = GLShapeFactoryCV.red;
        colorFaces[3] = GLShapeFactoryCV.green;
        colorFaces[4] = GLShapeFactoryCV.white;
        colorFaces[5] = GLShapeFactoryCV.yellow;
        colorFaces[6] = GLShapeFactoryCV.red;
        colorFaces[7] = GLShapeFactoryCV.green;
        colorFaces[8] = GLShapeFactoryCV.white;
        colorFaces[9] = GLShapeFactoryCV.yellow;
        colorFaces[10] = GLShapeFactoryCV.magenta;
        colorFaces[11] = GLShapeFactoryCV.cyan;
        GLShapeCV shape3 = GLShapeFactoryCV.makeCuboid("Cube 3",1, 2, 1.5f, colorFaces,GLShapeFactoryCV.white,10);
        shape3.setTrans(0,1.5f,1);
        GLAnimatorFactoryCV.addAnimatorRotY(shape3, 360,2000,6, true) ;
        surfaceView.addShape(shape1);
        surfaceView.addShape(shape2);
        surfaceView.addShape(shape3);
    }

    private void texturedCubes(GLSurfaceViewCV surfaceView) {
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
        ObjectAnimator anim2 = GLAnimatorFactoryCV.addAnimatorRotY(shape, 360, 8000,10, true) ;
        // anim2.setInterpolator(new AccelerateDecelerateInterpolator());
        surfaceView.addShape(shape);
    }

    private void coloredPolygons(GLSurfaceViewCV surfaceView) {
        int duration = 5000;
        surfaceView.clearShapes();
        GLShapeCV shape1 = GLShapeFactoryCV.makePolygon("Polygon 1",4, GLShapeFactoryCV.blue);
        shape1.setScale(0.75f,0.75f,0).setTrans(-1f,2.5f,1);
        GLAnimatorFactoryCV.addAnimatorRotX(shape1, 360, duration,10, true) ;
        GLShapeCV shape2 = GLShapeFactoryCV.makePolygon("Polygon 2",5, GLShapeFactoryCV.orange);
        shape2.setScale(0.75f,0.75f,0).setTrans(1f,2.5f,1);
        GLAnimatorFactoryCV.addAnimatorRotZ(shape2, 360, duration,10, true) ;
        GLShapeCV shape3 = GLShapeFactoryCV.makePolygon("Polygon 3",6, GLShapeFactoryCV.green);
        shape3.setScale(0.75f,0.75f,0).setTrans(-1f,0,1);
        GLAnimatorFactoryCV.addAnimatorRotX(shape3, 360, duration,10, true) ;
        float colors[][] = new float[3][];
        colors[0] = GLShapeFactoryCV.red;
        colors[1] = GLShapeFactoryCV.green;
        colors[2] = GLShapeFactoryCV.blue;
        GLShapeCV shape4 = GLShapeFactoryCV.makePolygon("Polygon 4", 8, colors);
        shape4.setScale(0.75f,0.75f,0).setTrans(1f,0f,1);
        GLAnimatorFactoryCV.addAnimatorRotX(shape4, 360, duration,10, true) ;
        colors = new float[2][];
        colors[0] = GLShapeFactoryCV.lightgrey;
        colors[1] = GLShapeFactoryCV.darkgrey;
        GLShapeCV shape5 = GLShapeFactoryCV.makePolygon("Polygon 5", 64, colors);
        shape5.setTrans(-0.5f,-2.25f,1);
        GLAnimatorFactoryCV.addAnimatorRotY(shape5, 360, duration,10, true) ;
        surfaceView.addShape(shape1);
        surfaceView.addShape(shape2);
        surfaceView.addShape(shape3);
        surfaceView.addShape(shape4);
        surfaceView.addShape(shape5);
    }

    private void pyramidsConesTetrahedra(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        float[] baseColor = GLShapeFactoryCV.orange;
        float[][] facesColors = new float[4][];
        facesColors[0] = GLShapeFactoryCV.red;
        facesColors[1] = GLShapeFactoryCV.green;
        facesColors[2] = GLShapeFactoryCV.blue;
        facesColors[3] = GLShapeFactoryCV.yellow;
        GLShapeCV shape1 = GLShapeFactoryCV.makePyramid("Pyramid 1",4, 1.5f, baseColor, facesColors);
        shape1.setTrans(-1f,3f,0);
        GLAnimatorFactoryCV.addAnimatorRotX(shape1, 360, 8000,10, true) ;
        surfaceView.addShape(shape1);
        baseColor = GLShapeFactoryCV.green;
        facesColors = new float[2][];
        facesColors[0] = GLShapeFactoryCV.darkgreen;
        facesColors[1] = GLShapeFactoryCV.lightgreen;
        GLShapeCV shape2 = GLShapeFactoryCV.makePyramid("Pyramid 2",32, 2.55f, baseColor, facesColors);
        // shape2.setScale(0.2f,0.2f,0.2f).setTrans(1.25f,1.75f,0);
        shape2.setTrans(1.25f,1.75f,0);
        GLAnimatorFactoryCV.addAnimatorRotY(shape2, 360, 8000,10, true) ;
        surfaceView.addShape(shape2);
        baseColor = GLShapeFactoryCV.green;
        facesColors = new float[2][];
        facesColors[0] = GLShapeFactoryCV.orange;
        facesColors[1] = GLShapeFactoryCV.blue;
        GLShapeCV shape3 = GLShapeFactoryCV.makePyramid("Pyramid 3",8, 2, baseColor, facesColors);
        shape3.setScale(0.75f,1.25f,0.75f).setTrans(-1.25f,0,0);
        GLAnimatorFactoryCV.addAnimatorRotX(shape3, 360, 6000,10, true) ;
        surfaceView.addShape(shape3);
        facesColors = new float[4][];
        facesColors[0] = GLShapeFactoryCV.red;
        facesColors[1] = GLShapeFactoryCV.green;
        facesColors[2] = GLShapeFactoryCV.blue;
        facesColors[3] = GLShapeFactoryCV.yellow;
        GLShapeCV shape4 = GLShapeFactoryCV.makeRegularTetrahedron("Tetrahedron",facesColors);
        shape4.setScale(1.5f,1.5f,1.5f).setTrans(-1.25f,-3.25f,0);
        float[] axis4 = { 1, 1, 0 };
        GLAnimatorFactoryCV.addAnimatorRot(shape4, 360, axis4,6000,10, true) ;
        surfaceView.addShape(shape4);
        facesColors = new float[3][];
        facesColors[0] = GLShapeFactoryCV.green;
        facesColors[1] = GLShapeFactoryCV.blue;
        facesColors[2] = GLShapeFactoryCV.yellow;
        GLShapeCV shape5 = GLShapeFactoryCV.makePyramid("Pyramid 5",3,1.25f,GLShapeFactoryCV.red,facesColors);
        shape5.setTrans(1.5f,-3f,0);
        float[] axis5 = { 1, 1, 0 };
        GLAnimatorFactoryCV.addAnimatorRot(shape5, 360, axis5,6000,10, true) ;
        surfaceView.addShape(shape5);
    }

    private void prismsFrustumsCylinders(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        float[] baseColor = GLShapeFactoryCV.lightred;
        float[] topColor = GLShapeFactoryCV.lightblue;
        float[][] facesColors = new float[4][];
        facesColors[0] = GLShapeFactoryCV.red;
        facesColors[1] = GLShapeFactoryCV.green;
        facesColors[2] = GLShapeFactoryCV.blue;
        facesColors[3] = GLShapeFactoryCV.yellow;
        GLShapeCV shape1 = GLShapeFactoryCV.makePrism("Prism 1",6, 1, baseColor, topColor, facesColors);
        shape1.setScale(1f,1f,1f).setTrans(-0.25f,3f,0);
        GLAnimatorFactoryCV.addAnimatorRotY(shape1, 360, 8000,10, true) ;
        surfaceView.addShape(shape1);
        baseColor = GLShapeFactoryCV.white;
        topColor = GLShapeFactoryCV.white;
        facesColors = new float[2][];
        facesColors[0] = GLShapeFactoryCV.darkgrey;
        facesColors[1] = GLShapeFactoryCV.lightgrey;
        GLShapeCV shape2 = GLShapeFactoryCV.makePrism("Prism 2",32, 5f, baseColor, topColor, facesColors);
        shape2.setScale(0.5f,0.5f,0.5f).setTrans(1.25f,-0.25f,0);
        float[] axis2 = { 1, 1, 1 };
        ObjectAnimator anim = GLAnimatorFactoryCV.addAnimatorRot(shape2, 3600, axis2,24000,2, true);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        surfaceView.addShape(shape2);
        baseColor = GLShapeFactoryCV.red;
        topColor = GLShapeFactoryCV.green;
        facesColors = new float[2][];
        facesColors[0] = GLShapeFactoryCV.orange;
        facesColors[1] = GLShapeFactoryCV.blue;
        GLShapeCV shape3 = GLShapeFactoryCV.makePrism("Prism 3",8, 3, baseColor, topColor, facesColors);
        shape3.setScale(0.5f,0.5f,0.5f).setTrans(-0.5f,-3f,0);
        GLAnimatorFactoryCV.addAnimatorRotX(shape3, 360, 6000,10, true) ;
        surfaceView.addShape(shape3);
        baseColor = GLShapeFactoryCV.lightgrey;
        topColor = GLShapeFactoryCV.darkgrey;
        facesColors = new float[2][];
        facesColors[0] = GLShapeFactoryCV.blue;
        facesColors[1] = GLShapeFactoryCV.yellow;
        GLShapeCV shape4 = GLShapeFactoryCV.makeFrustum("Frustum",10, 0.25f, 3, baseColor, topColor, facesColors);
        shape4.setScale(0.8f,0.5f,0.8f).setTrans(-1.5f,0,0);
        GLAnimatorFactoryCV.addAnimatorRotX(shape4, 360, 6000,10, true) ;
        surfaceView.addShape(shape4);
    }

    private void spheres(GLSurfaceViewCV surfaceView) {
        surfaceView.clearShapes();
        //    GLShapeCV shape = GLShapeFactoryCV.makeSphere("Sphere", GLShapeFactoryCV.yellow);
        float[][] colors = new float[4][];
        colors[0] = GLShapeFactoryCV.red;
        colors[1] = GLShapeFactoryCV.green;
        colors[2] = GLShapeFactoryCV.blue;
        colors[3] = GLShapeFactoryCV.yellow;
        // GLTriangleCV triangles[] = null;
        for (int i=0; i<6; i++) {
            long start = (new Date()).getTime();
            GLShapeCV shape = GLShapeFactoryCV.makeSphere("Sphere "+i, i, colors);
            long duration = (new Date()).getTime() - start;
            Log.v("GLDEMO",shape.getId()+": "+duration+" ms "+shape.getTriangles().length+" triangles");
            shape.setScaleY(1).setTrans(-2.5f+2.5f*(i%3),4-2.5f*(i/3),-2);
            float[] axis = { 1, 1, 1 };
            GLAnimatorFactoryCV.addAnimatorRot(shape, 360, axis,3000, 4, true);
            surfaceView.addShape(shape);
            // triangles = shape.getTriangles();
        }
        /*
        long start = (new Date()).getTime();
        GLShapeCV shape = new GLShapeCV("",triangles);
        long duration = (new Date()).getTime() - start;
        Log.v("GLDEMO",shape.getId()+"with copied triangles: "+duration+" ms "+shape.getTriangles().length+" triangles");
        shape.setTrans(-2.5f,-2,-2);
        surfaceView.addShape(shape); */
        GLShapeCV hemisphere1 = GLShapeFactoryCV.makeHemisphere("Hemisphere1", 4, colors);
        hemisphere1.setTrans(0,-2,-2);
        float[] axis1 = {0, 1, 0};
        GLAnimatorFactoryCV.addAnimatorRot(hemisphere1,360,axis1, 3000,4,true);
        surfaceView.addShape(hemisphere1);
        GLShapeCV hemisphere2 = GLShapeFactoryCV.makeHemisphere("Hemisphere2", 4, colors);
        float[] axis2 = {1, 0, 0};
        hemisphere2.setTrans(0,-3,-2).setRotation(180,axis2);
        surfaceView.addShape(hemisphere2);
    }

}