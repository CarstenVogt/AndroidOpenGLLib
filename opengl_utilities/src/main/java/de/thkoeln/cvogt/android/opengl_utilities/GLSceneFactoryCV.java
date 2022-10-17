// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 10.10.2022

package de.thkoeln.cvogt.android.opengl_utilities;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Class with static convenience methods to create a collection of 3D objects, i.e. objects of class <I>GLShapeCV</I>
 * and place them into 3D space, i.e. define their coordinates in this space.
 * <P>
 * Currently, these scenes are supported:
 * <UL>
 * <P><LI>A number of cubes that lie equidistantly between two given points
 * <P><LI>A number of spheres that lie equidistantly on a given circle
 * <P><LI>A number of cubes in a regular three-dimensional grid
 * </UL>
 * Moreover, the class provides a utility method randDegree() that returns a random degree value.
 * @see de.thkoeln.cvogt.android.opengl_utilities.GLShapeCV
 */

public class GLSceneFactoryCV {

    /**
     * Makes and places a number of cubes that lie equidistantly between two points.
     * @param numberOfCubes The number of cubes to be made (must be >1).
     * @param scalingFactor The scaling factor for the cubes (must be >0).
     * @param point1 The first point (must be an array of length 3).
     * @param point2 The second point (must be an array of length 3).
     * @param colors The color(s) of the cubes.
     * @param rotate Rotate the cubes to align their faces?
     * @param animationDuration If > 0 the placement is animated and takes 'animationDuration' seconds.
     * @return The cubes or null if one of the parameters is not valid.
     */

    public static GLShapeCV[] makeCubesInLine(int numberOfCubes, float scalingFactor, float point1[], float point2[], float[][] colors, boolean rotate, int animationDuration) {
        if (point1==null||point1.length!=3||point2==null||point2.length!=3||numberOfCubes<2||scalingFactor<=0) return null;
        GLShapeCV cubes[] = new GLShapeCV[numberOfCubes];
        float[] vector = new float[3];
        vector[0] = point2[0]-point1[0];
        vector[1] = point2[1]-point1[1];
        vector[2] = point2[2]-point1[2];
        float[] y_axis = {0,1,0};
        float[] rotAxis = GraphicsUtilsCV.crossProduct(y_axis,vector);
        float rotAngle = (float) Math.toDegrees(Math.acos(GraphicsUtilsCV.dotProduct(y_axis, GraphicsUtilsCV.getNormalizedCopy(vector))));
        float[][] positions = GraphicsUtilsCV.pointsInLine(point1,point2,numberOfCubes);
        for (int i=0; i<numberOfCubes; i++) {
            cubes[i] = GLShapeFactoryCV.makeCube("Cube"+i, colors);
            if (cubes[i]==null) return null;
            cubes[i].setScale(scalingFactor);
            if (animationDuration>0)
                GLAnimatorFactoryCV.addAnimatorTrans(cubes[i], positions[i], animationDuration, 0);
            else
                cubes[i].setTrans(positions[i]);
            if (rotate)
                cubes[i].setRotation(rotAngle,rotAxis);
        }
        /*
        float[] position = new float[3];
        for (int i=0; i<numberOfCubes; i++) {
            cubes[i] = GLShapeFactoryCV.makeCube("Cube"+i, colors);
            if (cubes[i]==null) return null;
            cubes[i].setScale(scalingFactor);
            if (i<numberOfCubes-1)
                for (int j=0; j<3; j++) position[j] = point1[j]+vector[j]/(numberOfCubes-1)*i;
              else
                position = point2.clone();
            if (animated)
                GLAnimatorFactoryCV.addAnimatorTrans(cubes[i], position, 2000, 0);
              else
                cubes[i].setTrans(position);
            if (rotate) {
                cubes[i].setRotAxis(rotAxis);
                cubes[i].setRotAngle(rotAngle);
            }
        }
        */
        return cubes;
    }

    /**
     * Makes and places a number of spheres that lie equidistantly on a circle.
     * @param numberOfSpheres The number of spheres to be made (must be >1).
     * @param scalingFactor The scaling factor for the spheres (must be >0).
     * @param center The center of the circle.
     * @param radius The radius of the circle.
     * @param perpendicularVector Vector that is perpendicular to the plane in which the circle shall lie
     *                            and thus defines the orientation of the 2D circle in 3D space.
     *                            If null the circle will lie in the x-y plane, i.e. will not be rotated.
     *                            If not null it must be an array of length 3.
     * @param colors The color(s) of the spheres.
     * @param animationDuration If > 0 the placement is animated and takes 'animationDuration' seconds.
     * @return The spheres or null if one of the parameters is not valid.
     */

    public static GLShapeCV[] makeSpheresOnCircle(int numberOfSpheres, float scalingFactor, float center[], float radius, float[] perpendicularVector, float[][] colors, int animationDuration) {
        if (center==null||center.length!=3||radius<=0||numberOfSpheres<2||scalingFactor<=0) return null;
        long start = (new Date()).getTime();
        GLShapeCV spheres[] = new GLShapeCV[numberOfSpheres];
        float[][] positions = GraphicsUtilsCV.pointsOnCircle3D(center,radius,perpendicularVector,numberOfSpheres);
        for (int i=0; i<numberOfSpheres; i++) {
            if (i==0)
                spheres[i] = GLShapeFactoryCV.makeSphere("Sphere"+i, colors);
            else
                spheres[i] = spheres[0].copy("Sphere"+i);
            if (spheres[i]==null) return null;
            spheres[i].setScale(scalingFactor);
            if (animationDuration>0)
                GLAnimatorFactoryCV.addAnimatorTrans(spheres[i], positions[i], animationDuration, 0);
            else
                spheres[i].setTrans(positions[i]);
        }
        long duration = (new Date()).getTime() - start;
        // Log.v("GLDEMO",numberOfSpheres+" spheres: "+duration+" ms");
        return spheres;
    }

    /**
     * Makes and places a number of cubes into a three-dimensional grid.
     * @param grid Specification where to place cubes.
     *             If position[i][j][k] is true, a cube will be placed centered at position (((-xSize+1)/2.0+i)*edgeLength,((-ySize+1)/2.0+j)*edgeLength,((-zSize+1)/2.0+k)*edgeLength)),
     *             xSize, ySize, and zSize being the sizes of the grid in the respective dimensions.
     * @param edgeLength The edge length of the cubes.
     * @param facesColor The color of the faces of the cubes. If null, the cube will have only lines, but no faces.
     * @param linesColor The color of the lines of the cubes. If null, the cube will have only faces, but no lines.
     * @param lineWidth The width of the lines of the cubes.
     * @param animationDuration If > 0 the placement is animated and takes 'animationDuration' seconds.
     * @return An array with the cubes or null if one of the parameters is not valid.
     */

    public static GLShapeCV[] makeBlocksScene(boolean[][][] grid, float edgeLength, float[] facesColor, float[] linesColor, int lineWidth, int animationDuration) {
        ArrayList<GLShapeCV> shapes = new ArrayList<>();
        if (grid==null||edgeLength<=0||lineWidth<=0) return null;
        for (int xPos=0; xPos<grid.length; xPos++)
            for (int yPos=0; yPos<grid[0].length; yPos++)
                for (int zPos=0; zPos<grid[0][0].length; zPos++)
                    if (grid[xPos][yPos][zPos]) {
                        GLShapeCV shape;
                        if (linesColor==null)
                            shape = GLShapeFactoryCV.makeCube("Cube_"+xPos+"_"+yPos+"_"+zPos,facesColor);
                        else if (facesColor==null)
                            shape = GLShapeFactoryCV.makeCubeWireframe("Cube_"+xPos+"_"+yPos+"_"+zPos,linesColor,lineWidth);
                        else
                            shape = GLShapeFactoryCV.makeCube("Cube_"+xPos+"_"+yPos+"_"+zPos,facesColor,linesColor,lineWidth);
                        shape.setScale(edgeLength);
                        float[] position = { ((-grid.length+1)/2.0f+xPos)*edgeLength, ((-grid[xPos].length+1)/2.0f+yPos)*edgeLength, ((-grid[xPos][yPos].length+1)/2.0f+zPos)*edgeLength };
                        if (animationDuration>0)
                            GLAnimatorFactoryCV.addAnimatorTrans(shape, position, animationDuration, 0);
                        else
                            shape.setTrans(position);
                        shapes.add(shape);
                    }
        GLShapeCV result[] = new GLShapeCV[shapes.size()];
        shapes.toArray(result);
        return result;
    }

    // A method for testing puporses (to be compared with makeBlocksScene()): a single shape comprising all cube > draw() is orders of magnitude faster

    public static GLShapeCV makeBlocksScene2(boolean[][][] grid, float edgeLength, float[] facesColor, float[] linesColor, int lineWidth, int animationDuration) {
        GLShapeCV result = null;
        float[][] color = new float[1][];
        ArrayList<GLTriangleCV> triangles = new ArrayList();
        ArrayList<GLLineCV> lines = new ArrayList();
        color[0] = GLShapeFactoryCV.white;
        if (grid==null||edgeLength<=0||lineWidth<=0) return null;
        for (int xPos=0; xPos<grid.length; xPos++)
            for (int yPos=0; yPos<grid[0].length; yPos++)
                for (int zPos=0; zPos<grid[0][0].length; zPos++)
                    if (grid[xPos][yPos][zPos]) {
                        triangles.addAll(Arrays.asList(GLShapeFactoryCV.trianglesForColoredCube(((-grid.length+1)/2.0f+xPos)*edgeLength, ((-grid[xPos].length+1)/2.0f+yPos)*edgeLength, ((-grid[xPos][yPos].length+1)/2.0f+zPos)*edgeLength,edgeLength,color)));
                        lines.addAll(Arrays.asList(GLShapeFactoryCV.linesForWireframeCuboid(((-grid.length+1)/2.0f+xPos)*edgeLength, ((-grid[xPos].length+1)/2.0f+yPos)*edgeLength, ((-grid[xPos][yPos].length+1)/2.0f+zPos)*edgeLength,edgeLength,edgeLength,edgeLength,GLShapeFactoryCV.red)));
                    }
        result = new GLShapeCV("",triangles.toArray(new GLTriangleCV[triangles.size()]),lines.toArray(new GLLineCV[lines.size()]),10);
        return result;
    }

    /**
     * @return A random float value between -360.0f (incl.) and 360.0f (excl.)
     */

    public static float randDegree() {
        return (float)(720*Math.random()-360);
    }

}
