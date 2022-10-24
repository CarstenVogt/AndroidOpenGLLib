// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 24.10.2022

package de.thkoeln.cvogt.android.opengl_utilities;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

/**
 * Class with static convenience methods to create specific shapes (i.e. objects of class <I>GLShapeCV</I>)
 * and triangles (i.e. objects of class <I>GLTriangleCV</I>) from which these shapes are built.
 * Currently, these geometric objects are supported:
 * <UL>
 * <P><LI>2D objects:
 * <UL>
 * <P><LI>Triangles (uniformly colored or with a color gradient)
 * <P><LI>Squares (uniformly colored, with two triangles of different colors, or textured)
 * <P><LI>Regular polygons (uniformly colored or with triangles of different colors)
 * </UL>
 * <P><LI>3D objects (geometry):
 * <UL>
 * <P><LI>Cubes (with different forms of coloring, especially with colored edges, or with texturing, also wireframe cubes)
 * <P><LI>Cuboids (with different forms of coloring, especially with colored edges, also wireframe cuboids)
 * <P><LI>Regular tetrahedra
 * <P><LI>Pyramids
 * <P><LI>Bipyramids
 * <P><LI>Prisms
 * <P><LI>Frustums
 * <P><LI>Cones
 * <P><LI>Spheres
 * <P><LI>Hemispheres
 * </UL>
 * <P><LI>3D objects (real world):
 * <UL>
 * <P><LI>Jet airplanes
 * <P><LI>Propeller airplanes
 * <P><LI>Birds
 * </UL>
 * </UL>
 * <P>
 * Moreover, the class provides some utility methods and color constants.
 * - especially the utility method <I>joinShapes()</I> to build more complex shapes from simpler shapes.
 * <BR>
 * @see de.thkoeln.cvogt.android.opengl_utilities.GLShapeCV
 * @see de.thkoeln.cvogt.android.opengl_utilities.GLTriangleCV
 */

public class GLShapeFactoryCV {

    /**
     * Make a triangle shape with a uniform face color
     * <BR>
     * @param id The ID of the triangle shape.
     * @param vertices The vertex coordinates of the triangle shape (in model coordinate space, i.e. to be used directly for the single <I>GLTriangleCV</I> object defining the triangle shape).
     * @param faceColor The RGBA color definition of the triangle face.
     * @return The new triangle shape. Null if one of the parameters is not valid.
     */
    public static GLShapeCV makeTriangle(String id, float vertices[][], float faceColor[]) {
        return makeTriangle(id,vertices,faceColor,null,0);
    }

    /**
     * Make a triangle shape with a uniform face color and colored edge lines.
     * <BR>
     * @param id The ID of the triangle shape.
     * @param vertices The vertex coordinates of the triangle shape (in model coordinate space, i.e. to be used directly for the single <I>GLTriangleCV</I> object defining the triangle shape).
     * @param faceColor The RGBA color definition of the triangle face.
     * @param lineColor The RGBA color definition of the triangle lines.
     * @param lineWidth The width of the edge lines.
     * @return The new triangle shape. Null if one of the parameters is not valid.
     */
    public static GLShapeCV makeTriangle(String id, float vertices[][], float faceColor[], float lineColor[], float lineWidth) {
        float[][] clrs = new float[1][];
        clrs[0] = faceColor;
        if (lineColor==null)
            return makeTriangle(id,vertices,clrs,null,0);
        float[][] lnclrs = new float[1][];
        lnclrs[0] = lineColor;
        return makeTriangle(id,vertices,clrs,lnclrs,lineWidth);
    }

    /**
     * Make a triangle shape with a color gradient and colored edge lines.
     * <BR>
     * @param id The ID of the triangle shape.
     * @param vertices The vertex coordinates of the triangle shape (in model coordinate space, i.e. to be used directly for the single <I>GLTriangleCV</I> object defining the triangle shape).
     * @param colors The color(s) of the triangle as an array of size[n][4] with n=1 or n=3.
     * If n=1 the triangle shape will be uniformly colored with colors[0] being the RGBA color definition.
     * If n=3 the triangle shape will have a color gradient with colors[i] being the RGBA color definition of vertex i.
     * @param lineColors The colors of the lines as an array of size[n][4] with n=1 or n=3. If null, the triangle shape will have no edge lines.
     * If n=1 colors[0] is the RGBA color definition for all three lines.
     * If n=1 colors[i] is the RGBA color definition for the ith line.
     * @param lineWidth The width of the edge lines.
     * @return The new triangle shape. Null if one of the parameters is not valid.
     */
    public static GLShapeCV makeTriangle(String id, float vertices[][], float colors[][], float lineColors[][], float lineWidth) {
        GLShapeCV triangleShape = null;
        try {
            GLTriangleCV[] innerTriangle = new GLTriangleCV[1];
            float[][] clrs = new float[3][];
            if (colors.length==3)
                clrs = colors;
            else for (int i=0;i<3;i++)
                clrs[i] = colors[0];
            innerTriangle[0] = new GLTriangleCV(id,vertices,clrs);
            if (lineColors!=null) {
                GLLineCV[] lines = new GLLineCV[3];
                lines[0] = new GLLineCV("Line 0", vertices[0], vertices[1], lineColors[0]);
                if (lineColors.length==3)
                    lines[1] = new GLLineCV("Line 1", vertices[1], vertices[2], lineColors[1]);
                else
                    lines[1] = new GLLineCV("Line 1", vertices[1], vertices[2], lineColors[0]);
                if (lineColors.length==3)
                    lines[2] = new GLLineCV("Line 2", vertices[2], vertices[0], lineColors[2]);
                else
                    lines[2] = new GLLineCV("Line 2", vertices[2], vertices[0], lineColors[0]);
                triangleShape = new GLShapeCV(id, innerTriangle, lines, lineWidth);
            }
            else
                triangleShape = new GLShapeCV(id, innerTriangle);
        } catch (Exception e) { return null; }
        return triangleShape;
    }

    /*
    public static GLShapeCV makeSquare(String id, float leftFrontUpperCorner_X, float leftFrontUpperCorner_Y, float sideLength, Bitmap texture) {
        GLTriangleCV[] triangles = trianglesForSquare(leftFrontUpperCorner_X, leftFrontUpperCorner_Y, sideLength);
        // Dreieck links oben
        float uvCoordinates0[] = {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
        };
        triangles[0].setTexture(texture,uvCoordinates0);
        // Dreieck rechts unten
        float uvCoordinates1[] = {
                1.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
        };
        triangles[1].setTexture(texture,uvCoordinates1);
        return new GLShapeCV(id,triangles);
    }

    /**
     * Make a square with a uniform color.
     * <BR>
     * The square will have an edge length of 1.0, lie in the x-y plane (i.e. with z = 0) parallel to the axes,
     * and its center will have the model coordinates (0.0,0.0,0.0),
     * such that its left upper vertex will have the model coordinates (-0.5f,0.5f,0.0f).
     * @param id The ID of the square.
     * @param color The color of the square. Must be a valid color definition according to the method isValidColorArray().
     * @return The new square. Null if the color parameter is not valid.
     */
    public static GLShapeCV makeSquare(String id, float[] color) {
        if (!isValidColorArray(color)) return null;
        return makeSquare(id,color,color);
    }

    /**
     * Make a square with two triangles of potentially different colors.
     * <BR>
     * The square will have an side length of 1.0, it will lie in the x-y plane (i.e. with z = 0) parallel to the axes,
     * and its center will have the model coordinates (0.0,0.0,0.0),
     * such that its left upper vertex will have the model coordinates (-0.5f,0.5f,0.0f).
     * @param id The ID of the square.
     * @param color1 The color of the left upper triangle. Must be a valid color definition according to the method isValidColorArray().
     * @param color2 The color of the right lower triangle. Must be a valid color definition according to the method isValidColorArray().
     * @return The new square. Null if a color parameter is not valid.
     */

    public static GLShapeCV makeSquare(String id, float[] color1, float[] color2) {
        if (!isValidColorArray(color1)||!isValidColorArray(color2)) return null;
        float sideLength = 1;
        float leftUpperCorner_X = -sideLength/2.0f;
        float leftUpperCorner_Y = sideLength/2.0f;
        GLTriangleCV[] triangles = trianglesForSquare(leftUpperCorner_X, leftUpperCorner_Y, sideLength);
        triangles[0].setUniformColor(color1);
        triangles[1].setUniformColor(color2);
        return new GLShapeCV(id,triangles);
    }

    /*
    public static GLShapeCV makeSquare(String id, float leftFrontUpperCorner_X, float leftFrontUpperCorner_Y, float sideLength, float[] color1, float[] color2) {
        float sideLenght
        GLTriangleCV[] triangles = trianglesForSquare(leftFrontUpperCorner_X, leftFrontUpperCorner_Y, sideLength);
        triangles[0].setUniformColor(color1);
        triangles[1].setUniformColor(color2);
        return new GLShapeCV(id,triangles);
    }
    */

    /**
     * Make a square with a texture.
     * <BR>
     * The square will have an side length of 1.0, it will lie in the x-y plane (i.e. with z = 0) parallel to the axes,
     * and its center will have the model coordinates (0.0,0.0,0.0),
     * such that its left upper vertex will have the model coordinates (-0.5f,0.5f,0.0f).
     * @param id The ID of the square.
     * @param texture The bitmap for the texture.
     * @return The new square. Null if texture is null.
     */

    public static GLShapeCV makeSquare(String id, Bitmap texture) {
        if (texture==null) return null;
        float sideLength = 1;
        float leftUpperCorner_X = -sideLength/2.0f;
        float leftUpperCorner_Y = sideLength/2.0f;
        GLTriangleCV[] triangles = trianglesForSquare(leftUpperCorner_X, leftUpperCorner_Y, sideLength);
        // Dreieck links oben
        float uvCoordinates0[] = {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
        };
        triangles[0].setTexture(texture,uvCoordinates0);
        // Dreieck rechts unten
        float uvCoordinates1[] = {
                1.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
        };
        triangles[1].setTexture(texture,uvCoordinates1);
        return new GLShapeCV(id,triangles);
    }

    /*
    public static GLShapeCV makeSquare(String id, float leftFrontUpperCorner_X, float leftFrontUpperCorner_Y, float sideLength, Bitmap texture) {
        GLTriangleCV[] triangles = trianglesForSquare(leftFrontUpperCorner_X, leftFrontUpperCorner_Y, sideLength);
        // Dreieck links oben
        float uvCoordinates0[] = {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
        };
        triangles[0].setTexture(texture,uvCoordinates0);
        // Dreieck rechts unten
        float uvCoordinates1[] = {
                1.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
        };
        triangles[1].setTexture(texture,uvCoordinates1);
        return new GLShapeCV(id,triangles);
    }

     */

    /**
     * Make a regular polygon with a uniform color.
     * <BR>
     * The polygon will lie in the x-y plane (i.e. with z = 0) with its center at model coordinates (0.0,0.0,0.0).
     * The circumcircle of the polygon will have a radius of 1.0.
     * The polygon will point upwards, i.e. its uppermost vertex will have the coordinates (0.0,1.0,0.0).
     * @param id The ID of the polygon.
     * @param noCorners The number of corners of the polygon (must be > 2).
     * @param color The color. Must be a valid color definition according to the method isValidColorArray().
     * @return The new polygon. Null if one of the parameters is not valid (see above).
     */

    public static GLShapeCV makePolygon(String id, int noCorners, float[] color) {
        if (noCorners<3||!isValidColorArray(color)) return null;
        float sideLength = 1;
        // float leftUpperCorner_X = -sideLength/2.0f;
        // float leftUpperCorner_Y = (float) ((sideLength/2)/Math.tan(Math.PI/2-(noCorners-2)/(2.0*noCorners)*Math.PI));
        float colors[][] = new float[1][];
        colors[0]=color;
        // GLTriangleCV[] triangles = trianglesForPolygon(leftUpperCorner_X, leftUpperCorner_Y, sideLength, noCorners, colors);
        GLTriangleCV[] triangles = trianglesForPolygon(noCorners, sideLength, colors);
        for (int i=0; i<noCorners; i++)
            triangles[i].setUniformColor(color);
        return new GLShapeCV(id,triangles);
    }

    /*
    public static GLShapeCV makePolygon(String id, float leftFrontUpperCorner_X, float leftFrontUpperCorner_Y, float sideLength, int numberOfCorners, float[] color) {
        float colors[][] = new float[1][];
        colors[0]=color;
        GLTriangleCV[] triangles = trianglesForPolygon(leftFrontUpperCorner_X, leftFrontUpperCorner_Y, sideLength, numberOfCorners, colors);
        for (int i=0; i<numberOfCorners; i++)
            triangles[i].setUniformColor(color);
        return new GLShapeCV(id,triangles);
    }
    */

    /**
     * Make a regular polygon with triangles of different colors.
     * <BR>
     * The polygon will lie in the x-y plane (i.e. with z = 0) with its center at model coordinates (0.0,0.0,0.0).
     * The circumcircle of the polygon will have a radius of 1.0.
     * The polygon will point upwards, i.e. its uppermost vertex will have the coordinates (0.0,1.0,0.0).
     * @param id The ID of the polygon.
     * @param noCorners The number of corners of the polygon (must be > 2).
     * @param colors The colors of the triangles. Must be a valid color definition according to the method isValidColorsArray().
     * @return The new polygon. Null if one of the parameters is not valid (see above).
     */

    public static GLShapeCV makePolygon(String id, int noCorners, float[][] colors) {
        if (noCorners<3||!isValidColorsArray(colors)) return null;
        float sideLength = 1;
        float leftUpperCorner_X = -sideLength/2.0f;
        float leftUpperCorner_Y = (float) ((sideLength/2)/Math.tan(Math.PI/2-(noCorners-2)/(2.0*noCorners)*Math.PI));
        // GLTriangleCV[] triangles = trianglesForPolygon(leftUpperCorner_X, leftUpperCorner_Y, sideLength, noCorners, colors);
        GLTriangleCV[] triangles = trianglesForPolygon(noCorners, sideLength, 0, colors);
        return new GLShapeCV(id,triangles);
    }

    /*
    public static GLShapeCV makePolygon(String id, float leftFrontUpperCorner_X, float leftFrontUpperCorner_Y, float sideLength, int numberOfCorners, float[][] colors) {
        GLTriangleCV[] triangles = trianglesForPolygon(leftFrontUpperCorner_X, leftFrontUpperCorner_Y, sideLength, numberOfCorners, colors);
        // for (int i=0; i<numberOfCorners; i++)
        //    triangles[i].setUniformColor(color);
        return new GLShapeCV(id,triangles);
    }
    */

    /**
     * Make a cube with colored faces, all faces having the same color.
     * <BR>
     * The cube will be "in level", i.e. its edges will be parallel to the respective axes of the underlying coordinate system.
     * The edges of the cube will have a length of 1.0.
     * The center of the cube will have the model coordinates (0.0,0.0,0.0)
     * such that its left upper front vertex will have the model coordinates (-0.5f,0.5f,0.5f) etc.
     * @param id The ID of the cube.
     * @param color The color of the faces (array of length 4).
     * @return The new cube. Null if 'color' is not a valid color array (see method isValidColorArray()).
     */

    public static GLShapeCV makeCube(String id, float color[]) {
        return makeCuboid(id,1,1,1,color);
/*
        if (!isValidColorArray(color)) return null;
        float edgeLength = 1.0f;
        float leftFrontUpperCorner_X = -edgeLength/2.0f;   // set values such that model coordinates (0,0,0) are the center of the cube
        float leftFrontUpperCorner_Y = edgeLength/2.0f;
        float leftFrontUpperCorner_Z = edgeLength/2.0f;
        float[][] colors = new float[1][];
        colors[0] = color;
        return new GLShapeCV(id,trianglesForColoredCube(leftFrontUpperCorner_X, leftFrontUpperCorner_Y, leftFrontUpperCorner_Z, edgeLength, colors));
 */
    }

    /**
     * Make a cube with colored faces.
     * <BR>
     * The cube will be "in level", i.e. its edges will be parallel to the respective axes of the underlying coordinate system.
     * The edges of the cube will have a length of 1.0.
     * The center of the cube will have the model coordinates (0.0,0.0,0.0)
     * such that its left upper front vertex will have the model coordinates (-0.5f,0.5f,0.5f) etc.
     * @param id The ID of the cube.
     * @param colors The colors of the faces. This must be a two-dimensional array with size n*4. For the first dimension, n may have these values:
     * <UL>
     * <LI>n=1: color[0] is the uniform color of all faces of the cube.
     * <LI>n=6: color[0-5] define the colors of the six faces of the cube (in the order front, right, back, left, top, bottom)
     * <LI>n=12: color[0-11] define the colors of the twelve triangles of the cube (in the order as specified by cubeTriangleIDs)
     * </UL>
     * @return The new cube. Null if 'colors' is not a valid color array (see method isValidColorsArray()) or has not a valid size (1, 6, or 12) in its first dimension or if one of the edge lengths is not larger than 0.
     */

    public static GLShapeCV makeCube(String id, float colors[][]) {
        return makeCuboid(id,1,1,1,colors);
        /*
        if (!isValidColorsArray(colors)
                ||(colors.length!=1&&colors.length!=6&&colors.length!=12)) return null;
        float edgeLength = 1.0f;
        float leftFrontUpperCorner_X = -edgeLength/2.0f;   // set values such that model coordinates (0,0,0) are the center of the cube
        float leftFrontUpperCorner_Y = edgeLength/2.0f;
        float leftFrontUpperCorner_Z = edgeLength/2.0f;
        return new GLShapeCV(id,trianglesForColoredCube(leftFrontUpperCorner_X, leftFrontUpperCorner_Y, leftFrontUpperCorner_Z, edgeLength, colors));
         */
    }

    /**
     * Make a cube with colored faces, all faces having the same color, and with its edges marked by a color.
     * <BR>
     * The cube will be "in level", i.e. its edges will be parallel to the respective axes of the underlying coordinate system.
     * The edges of the cube will have a length of 1.0.
     * The center of the cube will have the model coordinates (0.0,0.0,0.0)
     * such that its left upper front vertex will have the model coordinates (-0.5f,0.5f,0.5f) etc.
     * @param id The ID of the cube.
     * @param colorFaces The color of the faces (array of length 4).
     * @param colorLines The color of the faces (array of length 4).
     * @param edgeLineWidth The width of the edge lines.
     * @return The new cube. Null if a color array is not valid (see method isValidColorArray()) or if one of the edge lengths or the lineWidth is not larger than 0.
     */

    public static GLShapeCV makeCube(String id, float colorFaces[], float colorLines[], float edgeLineWidth) {
        return makeCuboid(id,1,1,1,colorFaces,colorLines,edgeLineWidth);
    }

    /**
     * Make a cube with colored faces and with its edges marked by a color.
     * <BR>
     * The cube will be "in level", i.e. its edges will be parallel to the respective axes of the underlying coordinate system.
     * The edges of the cube will have a length of 1.0.
     * The center of the cube will have the model coordinates (0.0,0.0,0.0)
     * such that its left upper front vertex will have the model coordinates (-0.5f,0.5f,0.5f) etc.
     * @param id The ID of the cube.
     * @param colorsFaces The colors of the faces. This must be a two-dimensional array with size n*4. For the first dimension, n may have these values:
     * <UL>
     * <LI>n=1: color[0] is the uniform color of all faces of the cube.
     * <LI>n=6: color[0-5] define the colors of the six faces of the cube (in the order front, right, back, left, top, bottom)
     * <LI>n=12: color[0-11] define the colors of the twelve triangles of the cube (in the order as specified by cubeTriangleIDs)
     * </UL>
     * @return The new cube. Null if a color array is not valid (see methods isValidColor(s)Array()) or if one of the edge lengths or the lineWidth is not larger than 0.
     */

    public static GLShapeCV makeCube(String id, float colorsFaces[][], float colorLines[], float edgeLineWidth) {
        return makeCuboid(id,1,1,1,colorsFaces,colorLines,edgeLineWidth);
    }

    /**
     * Make a cube with textured faces.
     * <BR>
     * The cube will be "in level", i.e. its edges will be parallel to the respective axes of the underlying coordinate system.
     * The edges of the cube will have a length of 1.0.
     * The center of the cube will have the model coordinates (0.0,0.0,0.0)
     * such that its left upper front vertex will have the model coordinates (-0.5f,0.5f,0.5f) etc.
     * @param id The ID of the cube.
     * @param textures The textures for the cube. This must be an array of length 6 with the bitmaps for the cube faces in this order: front, right, back, left, top, bottom.
     * @return The new cube. Null if 'textures' is null or has a length other than 6.
     */

    public static GLShapeCV makeCube(String id, Bitmap textures[]) {
        if (textures==null||textures.length!=6) return null;
        float edgeLength = 1.0f;
        float leftFrontUpperCorner_X = -edgeLength/2.0f;   // set values such that model coordinates (0,0,0) are the center of the cube
        float leftFrontUpperCorner_Y = edgeLength/2.0f;
        float leftFrontUpperCorner_Z = edgeLength/2.0f;
        GLShapeCV shape = new GLShapeCV(id,trianglesForTexturedCube(leftFrontUpperCorner_X, leftFrontUpperCorner_Y, leftFrontUpperCorner_Z, edgeLength, textures));
        return shape;
    }

    /**
     * Make a "wireframe cube", i.e. a shape with lines that mark the edges of the cube.
     * <BR>
     * The cube will be "in level", i.e. its edges will be parallel to the respective axes of the underlying coordinate system.
     * The edges of the cube will have a length of 1.0.
     * The center of the cube will have the model coordinates (0.0,0.0,0.0)
     * such that its left upper front vertex will have the model coordinates (-0.5f,0.5f,0.5f) etc.
     * @param id The ID of the cuboid.
     * @param color The color of the lines. This must be an array of size 4.
     * @return The new cube. Null if 'color' is not a valid color array (see method isValidColorArray()) or the line width is not larger than 0.
     */

    public static GLShapeCV makeCubeWireframe(String id, float color[], float lineWidth) {
        return makeCuboidWireframe(id,1,1,1,color,lineWidth);
    }

    /**
     * Make a cuboid with colored faces, all faces having the same color.
     * <BR>
     * The cuboid will be "in level", i.e. its edges will be parallel to the respective axes of the underlying coordinate system.
     * The center of the cuboid will have the model coordinates (0.0,0.0,0.0)
     * such that its left upper front vertex will have the model coordinates (-0.5f*edgeLength_X,0.5f*edgeLength_Y,0.5f*edgeLength_Z) etc.
     * @param id The ID of the cuboid.
     * @param edgeLength_X The edge length in the x dimension.
     * @param edgeLength_Y The edge length in the y dimension.
     * @param edgeLength_Z The edge length in the z dimension.
     * @param color The color of the faces (array of length 4).
     * @return The new cuboid. Null if 'color' is not a valid color array (see method isValidColorArray()) or one of the edge lengths is not larger than 0.
     */

    public static GLShapeCV makeCuboid(String id, float edgeLength_X, float edgeLength_Y, float edgeLength_Z, float color[]) {
        float[][] colors = new float[1][];
        colors[0] = color.clone();
        return makeCuboid(id,edgeLength_X,edgeLength_Y,edgeLength_Z,colors);
    }

    /**
     * Make a cuboid with colored faces.
     * <BR>
     * The cuboid will be "in level", i.e. its edges will be parallel to the respective axes of the underlying coordinate system.
     * The center of the cuboid will have the model coordinates (0.0,0.0,0.0)
     * such that its left upper front vertex will have the model coordinates (-0.5f*edgeLength_X,0.5f*edgeLength_Y,0.5f*edgeLength_Z) etc.
     * @param id The ID of the cuboid.
     * @param edgeLength_X The edge length in the x dimension.
     * @param edgeLength_Y The edge length in the y dimension.
     * @param edgeLength_Z The edge length in the z dimension.
     * @param colors The colors of the faces. This must be a two-dimensional array with size n*4. For the first dimension, n may have these values:
     * <UL>
     * <LI>n=1: color[0] is the uniform color of all faces of the cuboid.
     * <LI>n=6: color[0-5] define the colors of the six faces of the cuboid (in the order front, right, back, left, top, bottom)
     * <LI>n=12: color[0-11] define the colors of the twelve triangles of the cuboid (in the order as specified by cubeTriangleIDs)
     * </UL>
     * @return The new cuboid. Null if 'colors' is not a valid color array (see method isValidColorsArray()) or has not a valid size (1, 6, or 12) in its first dimension or if one of the edge lengths is not larger than 0.
     */

    public static GLShapeCV makeCuboid(String id, float edgeLength_X, float edgeLength_Y, float edgeLength_Z, float colors[][]) {
        if (!isValidColorsArray(colors)
                ||(colors.length!=1&&colors.length!=6&&colors.length!=12)
                ||edgeLength_X<=0||edgeLength_Y<=0||edgeLength_Z<=0) return null;
        float leftFrontUpperCorner_X = -edgeLength_X/2.0f;   // set values such that model coordinates (0,0,0) are the center of the cube
        float leftFrontUpperCorner_Y = edgeLength_Y/2.0f;
        float leftFrontUpperCorner_Z = edgeLength_Z/2.0f;
        return new GLShapeCV(id,trianglesForColoredCuboid(leftFrontUpperCorner_X, leftFrontUpperCorner_Y, leftFrontUpperCorner_Z, edgeLength_X, edgeLength_Y, edgeLength_Z, colors));
    }

    /**
     * Make a cuboid with colored faces, all faces having the same color, and with its edges marked by a color.
     * <BR>
     * The cuboid will be "in level", i.e. its edges will be parallel to the respective axes of the underlying coordinate system.
     * The center of the cuboid will have the model coordinates (0.0,0.0,0.0)
     * such that its left upper front vertex will have the model coordinates (-0.5f*edgeLength_X,0.5f*edgeLength_Y,0.5f*edgeLength_Z) etc.
     * @param id The ID of the cuboid.
     * @param edgeLength_X The edge length in the x dimension.
     * @param edgeLength_Y The edge length in the y dimension.
     * @param edgeLength_Z The edge length in the z dimension.
     * @param colorFaces The color of the faces (array of length 4).
     * @param colorLines The color of the faces (array of length 4).
     * @param edgeLineWidth The width of the edge lines.
     * @return The new cuboid. Null if teh color definitions are not valid (see method isValidColorArray()) or one of the edge lengths or the edge line width is not larger than 0.
     */

    public static GLShapeCV makeCuboid(String id, float edgeLength_X, float edgeLength_Y, float edgeLength_Z, float colorFaces[], float colorLines[], float edgeLineWidth) {
        float[][] colorsFaces = new float[1][];
        colorsFaces[0] = colorFaces.clone();
        return makeCuboid(id, edgeLength_X, edgeLength_Y, edgeLength_Z, colorsFaces, colorLines, edgeLineWidth);
    }

    /**
     * Make a cuboid with colored faces and with its edges marked by a color.
     * <BR>
     * The cuboid will be "in level", i.e. its edges will be parallel to the respective axes of the underlying coordinate system.
     * The center of the cuboid will have the model coordinates (0.0,0.0,0.0)
     * such that its left upper front vertex will have the model coordinates (-0.5f*edgeLength_X,0.5f*edgeLength_Y,0.5f*edgeLength_Z) etc.
     * @param id The ID of the cuboid.
     * @param edgeLength_X The edge length in the x dimension.
     * @param edgeLength_Y The edge length in the y dimension.
     * @param edgeLength_Z The edge length in the z dimension.
     * @param colorsFaces The colors of the faces. This must be a two-dimensional array with size n*4. For the first dimension, n may have these values:
     * <UL>
     * <LI>n=1: color[0] is the uniform color of all faces of the cuboid.
     * <LI>n=6: color[0-5] define the colors of the six faces of the cuboid (in the order front, right, back, left, top, bottom)
     * <LI>n=12: color[0-11] define the colors of the twelve triangles of the cuboid (in the order as specified by cubeTriangleIDs)
     * </UL>
     * @param colorLines The color of the edge lines (array of length 4).
     * @param edgeLineWidth The width of the edge lines.
     * @return The new cuboid. Null if 'colorsFaces' or 'clorLines' is not a valid color array (see methods isValidColor(s)Array()) or one of the edge lengths or the edge line width is not larger than 0.
     */

    public static GLShapeCV makeCuboid(String id, float edgeLength_X, float edgeLength_Y, float edgeLength_Z, float colorsFaces[][], float colorLines[], float edgeLineWidth) {
        if (!isValidColorsArray(colorsFaces)||!isValidColorArray(colorLines)||edgeLength_X<=0||edgeLength_Y<=0||edgeLength_Z<=0||edgeLineWidth<=0) return null;
        float leftFrontUpperCorner_X = -edgeLength_X/2.0f;   // set values such that model coordinates (0,0,0) are the center of the cube
        float leftFrontUpperCorner_Y = edgeLength_Y/2.0f;
        float leftFrontUpperCorner_Z = edgeLength_Z/2.0f;
        return new GLShapeCV(id,trianglesForColoredCuboid(leftFrontUpperCorner_X, leftFrontUpperCorner_Y, leftFrontUpperCorner_Z, edgeLength_X, edgeLength_Y, edgeLength_Z, colorsFaces),linesForWireframeCuboid(leftFrontUpperCorner_X, leftFrontUpperCorner_Y, leftFrontUpperCorner_Z, edgeLength_X, edgeLength_Y, edgeLength_Z, colorLines),edgeLineWidth);
    }

    /**
     * Make a "wireframe cuboid", i.e. a shape with lines that mark the edges of the cuboid.
     * <BR>
     * The cuboid will be "in level", i.e. its edges will be parallel to the respective axes of the underlying coordinate system.
     * The center of the cuboid will have the model coordinates (0.0,0.0,0.0)
     * such that its left upper front vertex will have the model coordinates (-0.5f*edgeLength_X,0.5f*edgeLength_Y,0.5f*edgeLength_Z) etc.
     * @param id The ID of the cuboid.
     * @param edgeLength_X The edge length in the x dimension.
     * @param edgeLength_Y The edge length in the y dimension.
     * @param edgeLength_Z The edge length in the z dimension.
     * @param color The color of the lines. This must be an array of size 4.
     * @return The new cuboid. Null if 'color' is not a valid color array (see method isValidColorArray()) or one of the edge lengths or the line width is not larger than 0.
     */

    public static GLShapeCV makeCuboidWireframe(String id, float edgeLength_X, float edgeLength_Y, float edgeLength_Z, float color[], float lineWidth) {
        if (!isValidColorArray(color)||edgeLength_X<=0||edgeLength_Y<=0||edgeLength_Z<=0||lineWidth<=0) return null;
        float leftFrontUpperCorner_X = -edgeLength_X/2.0f;   // set values such that model coordinates (0,0,0) are the center of the cube
        float leftFrontUpperCorner_Y = edgeLength_Y/2.0f;
        float leftFrontUpperCorner_Z = edgeLength_Z/2.0f;
        return new GLShapeCV(id,null,linesForWireframeCuboid(leftFrontUpperCorner_X, leftFrontUpperCorner_Y, leftFrontUpperCorner_Z, edgeLength_X, edgeLength_Y, edgeLength_Z,color),lineWidth);
    }

    // TODO makeCuboid with textures

    /**
     * Make a regular-based pyramid or a cone with colored faces.
     * <BR>
     * The base will be a polygon parallel to the x-z plane with y = -apexHeight/2.0f
     * as constructed by trianglesForPolygon() (i.e. having a circumcircle with a radius of 1.0) and rotated by 90 deg around the x axis.
     * The center of the base polygon will have the model coordinates (0.0,-apexHeight/2,0.0).
     * <BR>
     * The apex of the pyramid will have the model coordinates (0.0, apexHeight/2.0f, 0.0),
     * i.e. will be positioned above the center of the base polygon.
     * @param id The ID of the pyramid.
     * @param noBaseCorners The number of corners of the base polygon (must be > 2). For higher values (e.g. 32 or 64) the pyramid will appear as a cone.
     * @param apexHeight The height of the apex (must be > 0).
     * @param baseColor The color of the base polygon. Must be a valid color definition according to the method isValidColorArray().
     * @param facesColors The colors of the pyramid faces. Must be a valid colors definition array according to method isValidColorsArray().
     * The colors are defined by an array on some length n and are assigned cyclically to the triangles, i.e. triangle no. i gets color no. i%n, % being the modulo operator.
     * @return The new pyramid. Null if one of the parameters is not valid (see above).
     */

    public static GLShapeCV makePyramid(String id, int noBaseCorners, float apexHeight, float[] baseColor, float[][] facesColors) {
        return makePyramid(id,noBaseCorners,apexHeight,baseColor,facesColors,true);
    }

    /**
     * Make a regular-based pyramid or a cone with colored faces.
     * <BR>
     * The base will be a polygon parallel to the x-z plane with y = -apexHeight/2.0f
     * as constructed by trianglesForPolygon() (i.e. having a circumcircle with a radius of 1.0) and rotated by 90 deg around the x axis.
     * The center of the base polygon will have the model coordinates (0.0,-apexHeight/2,0.0).
     * <BR>
     * The apex of the pyramid will have the model coordinates (0.0, apexHeight/2.0f, 0.0),
     * i.e. will be positioned above the center of the base polygon.
     * @param id The ID of the pyramid.
     * @param noBaseCorners The number of corners of the base polygon (must be > 2). For higher values (e.g. 32 or 64) the pyramid will appear as a cone.
     * @param apexHeight The height of the apex (must be > 0).
     * @param baseColor The color of the base polygon. Must be a valid color definition according to the method isValidColorArray().
     * @param facesColors The colors of the pyramid faces. Must be a valid colors definition array according to method isValidColorsArray().
     * The colors are defined by an array on some length n and are assigned cyclically to the triangles, i.e. triangle no. i gets color no. i%n, % being the modulo operator.
     * @param withBaseSide Specifies if the pyramid shall have a base plane or be "hollow".
     * @return The new pyramid. Null if one of the parameters is not valid (see above).
     */

    public static GLShapeCV makePyramid(String id, int noBaseCorners, float apexHeight, float[] baseColor, float[][] facesColors, boolean withBaseSide) {
        if (noBaseCorners<3) return null;
        if (apexHeight<=0.0) return null;
        if (withBaseSide&&!isValidColorArray(baseColor)) return null;
        if (!isValidColorsArray(facesColors)) return null;
        GLTriangleCV[] triangles;
        if (withBaseSide)
            triangles = new GLTriangleCV[2*noBaseCorners];
          else
            triangles = new GLTriangleCV[noBaseCorners];
        float color[][] = new float[1][];
        if (withBaseSide)
            color[0] = baseColor;
          else
            color[0] = GLShapeFactoryCV.white;   // dummy color, needed below für trianglesForPolygon()
        float baseEdgeLength = 1;
        float baseX = -baseEdgeLength/2.0f;
        float baseY = (float) (baseEdgeLength/2/Math.tan(Math.PI/2-(noBaseCorners-2)/(2.0*noBaseCorners)*Math.PI));
        // GLTriangleCV[] trianglesBase = trianglesForPolygon(baseX, baseY, baseEdgeLength,noBaseCorners,apexHeight/2.0f, color);
        GLTriangleCV[] trianglesBase = trianglesForPolygon(noBaseCorners,1, apexHeight/2.0f,color);
        for (int i=0; i<noBaseCorners; i++) {
            float vertices[][] = trianglesBase[i].getVertices();
            vertices[0][2] = -apexHeight/2.0f;
            triangles[i] = new GLTriangleCV("Side"+i,vertices,facesColors[i%facesColors.length]);
        }
        if (withBaseSide)
            for (int i=noBaseCorners; i<2*noBaseCorners; i++)
                triangles[i] = trianglesBase[i-noBaseCorners];
        for (GLTriangleCV triangle : triangles)
            triangle.transform(1,1,1,90, 0, 0,0,0,0);
        GLShapeCV shape = new GLShapeCV(id,triangles);
        return shape;
    }

    /**
     * Make a regular-based frustum (= lower part of a pyramid or a cone with the tip cut off).
     * <BR>
     * The base and the top will be polygons parallel to the x-z plane with y = -height/2.0f and y = height/2.0f, respectively,
     * as constructed by trianglesForPolygon() and rotated by 90 deg around the x axis.
     * The centers of the polygons will have the model coordinates (0.0,+/-height/2,0.0).
     * The base polygon will have a circumcircle with a radius of 1.0f,
     * the top polygon will have a circumcircle with a radius of 'radiusTopCircle'
     * @param id The ID of the frustum.
     * @param noCorners The number of corners of the polygons (must be > 2).
     * @param radiusTopCircle The radius of the circumcircle of the top polygon (must be > 0).
     * @param height The height of the frustum (must be > 0).
     * @param baseColor The color of the base polygon. Must be a valid color definition according to the method isValidColorArray().
     * @param topColor The color of the top polygon. Must be a valid color definition according to the method isValidColorArray().
     * @param facesColors The colors of the frustum faces. Must be a valid colors definition array according to method isValidColorsArray().
     * The colors are defined by an array on some length n and are assigned cyclically to the triangles, i.e. triangle no. i gets color no. i%n, % being the modulo operator.
     * @return The new frustum. Null if one of the parameters is not valid (see above).
     */

      public static GLShapeCV makeFrustum(String id, int noCorners, float radiusTopCircle, float height, float[] baseColor, float[] topColor, float[][] facesColors) {
        if (noCorners<3) return null;
        if (height<=0.0) return null;
        if (!isValidColorArray(baseColor)) return null;
        if (!isValidColorArray(topColor)) return null;
        if (!isValidColorsArray(facesColors)) return null;
        GLTriangleCV[] triangles = new GLTriangleCV[4*noCorners];
        float colors[][] = new float[1][];
        // triangles of the base polygon
        colors[0] = baseColor;
        GLTriangleCV[] trianglesPolygon = trianglesForPolygon(noCorners,1, height/2.0f, colors);
        for (int i=0; i<noCorners; i++)
            triangles[i] = trianglesPolygon[i];
        // triangles of the top polygon
        colors[0] = topColor;
        trianglesPolygon = trianglesForPolygon(noCorners,radiusTopCircle, -height/2.0f, colors);
        for (int i=0; i<noCorners; i++)
            triangles[3*noCorners+i] = trianglesPolygon[i];
        // triangles of the faces
        float[][] faceTriangleVertices = new float[3][];
        float[][] pointsBottomCircle = GraphicsUtilsCV.pointsOnCircle2D(0,0,1, noCorners);
        float[][] bottomVertices = new float[noCorners][3];
        for (int i=0;i<noCorners;i++) {
            bottomVertices[i][0] = pointsBottomCircle[i][0];  bottomVertices[i][1] = pointsBottomCircle[i][1];  bottomVertices[i][2] = height/2.0f;
        }
        float[][] pointsTopCircle = GraphicsUtilsCV.pointsOnCircle2D(0,0,radiusTopCircle,noCorners);
          float[][] topVertices = new float[noCorners][3];
          for (int i=0;i<noCorners;i++) {
              topVertices[i][0] = pointsTopCircle[i][0];  topVertices[i][1] = pointsTopCircle[i][1];  topVertices[i][2] = -height/2.0f;
          }
        for (int i=0; i<noCorners;i++) {
            faceTriangleVertices[0] = topVertices[i];
            faceTriangleVertices[1] = bottomVertices[i];
            faceTriangleVertices[2] = topVertices[(i+1)%noCorners];
            triangles[noCorners+2*i+1] = new GLTriangleCV("TriangleFace"+2*i,faceTriangleVertices,facesColors[(2*i)%facesColors.length]);
            faceTriangleVertices[0] = topVertices[(i+1)%noCorners];
            faceTriangleVertices[1] = bottomVertices[i];
            faceTriangleVertices[2] = bottomVertices[(i+1)%noCorners];
            triangles[noCorners+2*i] = new GLTriangleCV("TriangleFace"+2*i+1,faceTriangleVertices,facesColors[(2*i+1)%facesColors.length]);
        }
        for (GLTriangleCV triangle : triangles)
            triangle.transform(1,1,1,90, 0, 0,0,0,0);
        GLShapeCV shape = new GLShapeCV(id,triangles);
        return shape;

        /* alte Version
        if (noCorners<3) return null;
        if (height<=0.0) return null;
        if (!isValidColorArray(baseColor)) return null;
        if (!isValidColorArray(topColor)) return null;
        if (!isValidColorsArray(facesColors)) return null;
        GLTriangleCV[] triangles = new GLTriangleCV[4*noCorners];
        float colors[][] = new float[1][];
        // triangles of the base polygon
        colors[0] = baseColor;
        float baseEdgeLength = 1;
        float startEdgeX = -baseEdgeLength/2.0f;
        float startEdgeY = (float) ((baseEdgeLength/2.0)/Math.tan(Math.PI/2-(noCorners-2)/(2.0*noCorners)*Math.PI));
        GLTriangleCV[] trianglesPolygon = trianglesForPolygon_AlternativeVersion(startEdgeX,startEdgeY,baseEdgeLength,noCorners,height/2.0f, colors);
        for (int i=0; i<noCorners; i++)
            triangles[i] = trianglesPolygon[i];
        float[][] verticesBottom = polygonVertexCoordinates_AlternativeVersion(startEdgeX,startEdgeY,baseEdgeLength,noCorners, height/2.0f);
        // triangles of the top polygon
        colors[0] = topColor;
        startEdgeX = -topEdgeLength/2.0f;
        startEdgeY = (float) ((topEdgeLength/2.0)/Math.tan(Math.PI/2-(noCorners-2)/(2.0*noCorners)*Math.PI));
        trianglesPolygon = trianglesForPolygon_AlternativeVersion(startEdgeX,startEdgeY,topEdgeLength,noCorners,-height/2.0f, colors);
        for (int i=0; i<noCorners; i++)
            triangles[3*noCorners+i] = trianglesPolygon[i];
        // triangles of the faces
        float[][] verticesTop = polygonVertexCoordinates_AlternativeVersion(startEdgeX,startEdgeY,topEdgeLength,noCorners,-height/2.0f);
        float[][] triangleVertices = new float[3][];
        for (int i=0; i<noCorners;i++) {
            triangleVertices[0] = verticesTop[i];
            triangleVertices[1] = verticesBottom[i];
            triangleVertices[2] = verticesTop[(i+1)%noCorners];
            triangles[noCorners+2*i+1] = new GLTriangleCV("TriangleFace"+2*i,triangleVertices,facesColors[(2*i)%facesColors.length]);
            triangleVertices[0] = verticesTop[(i+1)%noCorners];
            triangleVertices[1] = verticesBottom[i];
            triangleVertices[2] = verticesBottom[(i+1)%noCorners];
            triangles[noCorners+2*i] = new GLTriangleCV("TriangleFace"+2*i+1,triangleVertices,facesColors[(2*i+1)%facesColors.length]);
        }
        for (GLTriangleCV triangle : triangles)
            triangle.transform(1,1,1,90, 0, 0,0,0,0);
        GLShapeCV shape = new GLShapeCV(id,triangles);
        return shape;

         */
    }

    /**
     * Make a regular tetrahedron of uniform color.
     * <BR>
     * See makeRegularTetrahedron(String id, float[][] colors) for a detailed explication.
     * @param id The ID of the tetrahedron.
     * @param color The uniform color of the tetrahedron. Must be a valid color definition according to method isValidColorArray().
     * @return The new tetrahedron. Null if the color parameter is not valid.
     */

    public static GLShapeCV makeRegularTetrahedron(String id, float[] color) {
        if (!isValidColorArray(color)) return null;
        float[][] colors = new float[4][];
        for (int i=0; i<4; i++)
            colors[i] = color;
        return makeRegularTetrahedron(id,colors);
    }

    /**
     * Make a regular tetrahedron with colored faces.
     * <BR>
     * The center of gravity of the tetrahedron will have the local coordinates (model coordinates) (0.0,0.0,0.0).
     * The tetrahedron will point upwards, i.e. into positive y direction, and its apex will lie on the y axis, i.e. its x and z coordinates will be 0.0.
     * Its base triangle will be in parallel to the x-z plane, the base edge of this triangle will be in parallel to the x axis
     * and the tip of the triangle will point into negative z direction.
     * The edges will have length 1.0.
     * <BR>
     * The vertex coordinates will therefore be:
     * <UL>
     * <LI>base line of the base triangle - left end: (-0.5,-1/4*sqrt(2/3),sqrt(3)/6)
     * <LI>base line of the base triangle - right end: (0.5,-1/4*sqrt(2/3),sqrt(3)/6)
     * <LI>tip of the base triangle: (0.0,-1/4*sqrt(2/3),-sqrt(3)/3)
     * <LI>apex of the tetrahedron: (0.0,3/4*sqrt(2/3),0.0)
     * </UL>
     * The order of the triangles of the tetrahedron will be:
     * triangle 0 = base triangle, triangle 1 = front triangle (as seen from the camera), triangle 2 = right triangle, triangle 3 = left triangle.
     * The IDs of the triangles will be: "Base", "Front", "Right", "Left".
     * <BR>
     * The radius of the circumsphere of the returned tetrahedron is Math.sqrt(3.0/8)
     * [see https://en.wikipedia.org/wiki/Tetrahedron#Angles_and_distances].
     * Hence, to get a regular tetrahedron with a circumsphere of radius 1.0
     * it must be scaled by a factor of 1/Math.sqrt(3.0/8).
     * @param id The ID of the tetrahedron.
     * @param colors The colors of the triangles of the tetrahedron. Must be a valid colors definition array according to method isValidColorsArray() and have length 4.
     * @return The new tetrahedron. Null if the colors parameter is not valid.
     */

    public static GLShapeCV makeRegularTetrahedron(String id, float[][] colors) {
        if (!isValidColorsArray(colors)) return null;
        float triangleHeight = (float) (0.5*Math.sqrt(3));
        float apexHeight = (float) Math.sqrt(2.0/3);
        GLTriangleCV[] triangles = new GLTriangleCV[4];
        float[] vertexBaseLeft = {-0.5f,-1f/4*apexHeight,triangleHeight/3},
                vertexBaseRight = {0.5f,-1f/4*apexHeight,triangleHeight/3},
                vertexBaseTip = {0.0f,-1f/4*apexHeight,-2*triangleHeight/3},
                vertexApex = {0.0f,3f/4*apexHeight,0.0f};
        // base triangle
        float[][] vertices = new float[3][];
        vertices[0] = vertexBaseLeft;
        vertices[1] = vertexBaseRight;
        vertices[2] = vertexBaseTip;
        triangles[0] = new GLTriangleCV("Base",vertices,colors[0]);
        // front triangle
        vertices[0] = vertexBaseLeft;
        vertices[1] = vertexBaseRight;
        vertices[2] = vertexApex;
        triangles[1] = new GLTriangleCV("Front",vertices,colors[1]);
        // right triangle
        vertices[0] = vertexBaseRight;
        vertices[1] = vertexBaseTip;
        vertices[2] = vertexApex;
        triangles[2] = new GLTriangleCV("Right",vertices,colors[2]);
        // left triangle
        vertices[0] = vertexBaseTip;
        vertices[1] = vertexBaseLeft;
        vertices[2] = vertexApex;
        triangles[3] = new GLTriangleCV("Left",vertices,colors[3]);
        return new GLShapeCV(id,triangles);
    }

    /**
     * Make a bipyramid from two pyramids, as constructed by makePyramid().
     * <BR>
     * The apexes of the bipyramid will point in opposite directions along the z axis
     * and have the model coordinates (0.0, 0.0, apexHeight) and  (0.0, 0.0, -apexHeight), respectively.
     * The bipyramid is symmetric to a polygon as constructed by trianglesForPolygon().
     * This polygon lies in the x-y plane (i.e. has z=0) and its center has model coordinates (0.0,0.0,0.0).
     * <BR>
     * @param id The ID of the pyramid.
     * @param noPolygonCorners The number of corners of the base polygon (must be > 2). Note that for higher numbers (e.g. 64) both pyramids will appear as cones.
     * @param apexHeight The height of the apex (must be > 0).
     * @param facesColors1 The colors of the faces of the first pyramid, i.e. the pyramid pointing into the negative z direction. Must be a valid colors definition array according to method isValidColorsArray().
     * The colors are defined by an array on some length n and are assigned cyclically to the triangles, i.e. triangle no. i gets color no. i%n, % being the modulo operator.
     * @param facesColors2 The colors of the faces of the second pyramid, i.e. the pyramid pointing into the positive z direction. Must be a valid colors definition array according to method isValidColorsArray().
     * The colors are defined by an array on some length n and are assigned cyclically to the triangles, i.e. triangle no. i gets color no. i%n, % being the modulo operator.
     * @return The new pyramid. Null if one of the parameters is not valid (see above).
     */

    public static GLShapeCV makeBipyramid(String id, int noPolygonCorners, float apexHeight, float[][] facesColors1, float[][] facesColors2) {
        if (noPolygonCorners<3) return null;
        if (apexHeight<=0.0) return null;
        if (!isValidColorsArray(facesColors1)) return null;
        if (!isValidColorsArray(facesColors2)) return null;
        GLShapeCV shape1 = GLShapeFactoryCV.makePyramid("",noPolygonCorners, apexHeight, GLShapeFactoryCV.white, facesColors1, false);
        GLShapeCV shape2 = GLShapeFactoryCV.makePyramid("",noPolygonCorners, apexHeight, GLShapeFactoryCV.white, facesColors2, false);
        GLShapeCV shape = GLShapeFactoryCV.joinShapes(id, shape1, shape2,1,1,1,180,0,0,
                0,-apexHeight,0,0,-apexHeight/2,0);
        return shape;
    }

    /**
     * Make a regular ("right") prism or a cylinder with colored faces.
     * <BR>
     * The base and the top will be polygons parallel to the x-z plane with y = -height/2.0f and y = height/2.0f, respectively,
     * as constructed by trianglesForPolygon() and rotated by 90 deg around the x axis.
     * The centers of the polygons will have the model coordinates (0.0,+/-height/2,0.0).
     * The polygons will have circumcircles with a radius of 1.0.
     * @param id The ID of the prism.
     * @param noCorners The number of corners of the polygons (must be > 2). Note that for higher numbers (e.g. 64) the prism will appear as a cylinder.
     * @param height The height of the prism (must be > 0).
     * @param baseColor The color of the base polygon. Must be a valid color definition according to the method isValidColorArray().
     * @param topColor The color of the top polygon. Must be a valid color definition according to the method isValidColorArray().
     * @param facesColors The colors of the prism faces. Must be a valid colors definition array according to method isValidColorsArray().
     * The colors are defined by an array on some length n and are assigned cyclically to the triangles, i.e. triangle no. i gets color no. i%n, % being the modulo operator.
     * @return The new prism. Null if one of the parameters is not valid (see above).
     */

    public static GLShapeCV makePrism(String id, int noCorners, float height, float[] baseColor, float[] topColor, float[][] facesColors) {
        return makeFrustum(id,noCorners,1,height,baseColor,topColor,facesColors);
/*
        if (noCorners<3) return null;
        if (height<=0.0) return null;
        if (!isValidColorArray(baseColor)) return null;
        if (!isValidColorArray(topColor)) return null;
        if (!isValidColorsArray(facesColors)) return null;
        GLTriangleCV[] triangles = new GLTriangleCV[4*noCorners];
        float colors[][] = new float[1][];
        // triangles of the base polygon
        colors[0] = baseColor;
        float polygonEdgeLength = 1;
        float startEdgeX = -polygonEdgeLength/2.0f;
        float startEdgeY = (float) ((polygonEdgeLength/2)/Math.tan(Math.PI/2-(noCorners-2)/(2.0*noCorners)*Math.PI));
        GLTriangleCV[] trianglesPolygon = trianglesForPolygon(startEdgeX,startEdgeY,polygonEdgeLength,noCorners,height/2.0f, colors);
        for (int i=0; i<noCorners; i++)
            triangles[i] = trianglesPolygon[i];
        // triangles of the faces
        float[][] verticesBottom = polygonVertexCoordinates(startEdgeX,startEdgeY,polygonEdgeLength,noCorners,height/2.0f);
        float[][] verticesTop = polygonVertexCoordinates(startEdgeX,startEdgeY,polygonEdgeLength,noCorners,-height/2.0f);
        float[][] triangleVertices = new float[3][];
        for (int i=0; i<noCorners;i++) {
            triangleVertices[0] = verticesTop[i];
            triangleVertices[1] = verticesBottom[i];
            triangleVertices[2] = verticesTop[(i+1)%noCorners];
            triangles[noCorners+2*i+1] = new GLTriangleCV("TriangleFace"+2*i,triangleVertices,facesColors[(2*i)%facesColors.length]);
            triangleVertices[0] = verticesTop[(i+1)%noCorners];
            triangleVertices[1] = verticesBottom[i];
            triangleVertices[2] = verticesBottom[(i+1)%noCorners];
            triangles[noCorners+2*i] = new GLTriangleCV("TriangleFace"+2*i+1,triangleVertices,facesColors[(2*i+1)%facesColors.length]);
        }
        // triangles of the top polygon
        colors[0] = topColor;
        trianglesPolygon = trianglesForPolygon(startEdgeX,startEdgeY, polygonEdgeLength,noCorners,-height/2.0f, colors);
        for (int i=0; i<noCorners; i++)
            triangles[3*noCorners+i] = trianglesPolygon[i];
        for (GLTriangleCV triangle : triangles)
            triangle.transform(1,1,1,90, 0, 0,0,0,0);
        GLShapeCV shape = new GLShapeCV(id,triangles);
        return shape; */
    }

    /* ALT
     * Make a regular ("right") prism or a cylinder with colored faces.
     * <BR>
     * The base will be a polygon parallel to the x-y plane with z = height/2.0f as constructed by makePolygon() and trianglesForPolygon().
     * The center of the base polygon will have the model coordinates (0.0,0.0,height/2.0f).
     * the top will be a copy of the polygon translated by 'height' into the negative z direction.
     * The uppermost edges of the both polygons will have length 1.0 and lie horizontally symmetrically above the polygon center,
     * i.e. their end points will have the x coordinates -0.5f and 0.5f and their y coordinates will be identical
     * (their exact values resulting from the form of the polygon which in turn results from its number of edges).
     * @param id The ID of the prism.
     * @param noCorners The number of corners of the polygons (must be > 2). Note that for higher numbers (e.g. 64) the prism will appear as a cylinder.
     * @param height The height of the prism (must be > 0).
     * @param baseColor The color of the base polygon. Must be a valid color definition according to the method isValidColorArray().
     * @param topColor The color of the top polygon. Must be a valid color definition according to the method isValidColorArray().
     * @param facesColors The colors of the prism faces. Must be a valid colors definition array according to method isValidColorsArray().
     * The colors are defined by an array on some length n and are assigned cyclically to the triangles, i.e. triangle no. i gets color no. i%n, % being the modulo operator.
     * @return The new prism. Null if one of the parameters is not valid (see above).

    public static GLShapeCV makePrismAlt(String id, int noCorners, float height, float[] baseColor, float[] topColor, float[][] facesColors) {
        if (noCorners<3) return null;
        if (height<=0.0) return null;
        if (!isValidColorArray(baseColor)) return null;
        if (!isValidColorArray(topColor)) return null;
        if (!isValidColorsArray(facesColors)) return null;
        GLTriangleCV[] triangles = new GLTriangleCV[4*noCorners];
        float colors[][] = new float[1][];
        // triangles of the base polygon
        colors[0] = baseColor;
        float polygonEdgeLength = 1;
        float startEdgeX = -polygonEdgeLength/2.0f;
        float startEdgeY = (float) ((polygonEdgeLength/2)/Math.tan(Math.PI/2-(noCorners-2)/(2.0*noCorners)*Math.PI));
        GLTriangleCV[] trianglesPolygon = trianglesForPolygon(startEdgeX,startEdgeY,polygonEdgeLength,noCorners,height/2.0f, colors);
        for (int i=0; i<noCorners; i++)
            triangles[i] = trianglesPolygon[i];
        // triangles of the faces
        float[][] verticesBottom = polygonVertexCoordinates(startEdgeX,startEdgeY,polygonEdgeLength,noCorners,height/2.0f);
        float[][] verticesTop = polygonVertexCoordinates(startEdgeX,startEdgeY,polygonEdgeLength,noCorners,-height/2.0f);
        float[][] triangleVertices = new float[3][];
        for (int i=0; i<noCorners;i++) {
            triangleVertices[0] = verticesTop[i];
            triangleVertices[1] = verticesBottom[i];
            triangleVertices[2] = verticesTop[(i+1)%noCorners];
            triangles[noCorners+2*i+1] = new GLTriangleCV("TriangleFace"+2*i,triangleVertices,facesColors[(2*i)%facesColors.length]);
            triangleVertices[0] = verticesTop[(i+1)%noCorners];
            triangleVertices[1] = verticesBottom[i];
            triangleVertices[2] = verticesBottom[(i+1)%noCorners];
            triangles[noCorners+2*i] = new GLTriangleCV("TriangleFace"+2*i+1,triangleVertices,facesColors[(2*i+1)%facesColors.length]);
        }
        // triangles of the top polygon
        colors[0] = topColor;
        trianglesPolygon = trianglesForPolygon(startEdgeX,startEdgeY, polygonEdgeLength,noCorners,-height/2.0f, colors);
        for (int i=0; i<noCorners; i++)
            triangles[3*noCorners+i] = trianglesPolygon[i];
        GLShapeCV shape = new GLShapeCV(id,triangles);
        return shape;
    } */

    /**
     * Make a regular ("right") prism or a cylinder of a uniform color.
     * <BR>
     * The base and the top will be polygons parallel to the x-z plane with y = -height/2.0f and y = height/2.0f, respectively,
     * as constructed by trianglesForPolygon() and rotated by 90 deg around the x axis.
     * The centers of the polygons will have the model coordinates (0.0,+/-height/2,0.0).
     * The polygons will have circumcircles with a radius of 1.0.
     * @param id The ID of the prism.
     * @param noCorners The number of corners of the polygons (must be > 2). Note that for higher numbers (e.g. 64) the prism will appear as a cylinder.
     * @param height The height of the prism (must be > 0).
     * @param color The color of the prism. Must be a valid color definition according to the method isValidColorArray().
     * @return The new prism. Null if one of the parameters is not valid (see above).
     */

    public static GLShapeCV makePrism(String id, int noCorners, float height, float[] color) {
        float[][] colorArray = new float[1][];
        colorArray[0] = color;
        return makeFrustum(id, noCorners, 1, height, color, color, colorArray);
    }

    /**
     * Makes a sphere-like shape by iteratively transforming a double pyramid with 2*8 sides to approximate a sphere.
     * The transformation is done by splitting and adjusting the triangles of the pyramid iteratively to approximate a shape.
     * The center of the sphere will have the model coordinates (0.0,0.0,0.0) and its radius will be approximately 1.0.
     * @param id The ID of the shape.
     * @param color The color of the shape.
     * @return The new shape.
     */

    public static GLShapeCV makeSphere(String id, float[] color) {
        return makeSphere(id,4,color);
    }

    /**
     * Makes a sphere-like shape by iteratively transforming a double pyramid with 2*8 sides to approximate a sphere.
     * The transformation is done by splitting and adjusting the triangles of the pyramid iteratively to approximate a sphere.
     * The center of the sphere will have the model coordinates (0.0,0.0,0.0) and its radius will be approximately 1.0.
     * @param id The ID of the shape.
     * @param iterations The number of iterations to be run (3 or 4 will yield good results with reasonable execution times).
     * @param color The color of the shape.
     * @return The new shape.
     */

    public static GLShapeCV makeSphere(String id, int iterations, float[] color) {
        float[][] col = new float[1][];
        col[0] = color;
        return makeSphere(id,iterations,col);
    }

    /**
     * Makes a sphere-like shape by iteratively transforming a double pyramid with 2*8 sides to approximate a sphere.
     * The transformation is done by splitting and adjusting the triangles of the pyramid iteratively to approximate a sphere.
     * The center of the sphere will have the model coordinates (0.0,0.0,0.0) and its radius will be approximately 1.0.
     * @param id The ID of the shape.
     * @param colors The colors of the shape.
     * @return The new shape.
     */

    public static GLShapeCV makeSphere(String id, float[][] colors) {
        return makeSphere(id,4,colors);
    }

    /**
     * Makes a sphere-like shape by iteratively transforming a double pyramid with 2*8 sides to approximate a sphere.
     * The center of the sphere will have the model coordinates (0.0,0.0,0.0) and its radius will be approximately 1.0.
     * The transformation is done by splitting and adjusting the triangles of the pyramid iteratively to approximate a sphere.
     * @param id The ID of the shape.
     * @param iterations The number of iterations to be run (3 or 4 will yield good results with reasonable execution times).
     * @param colors The colors of the shape.
     * @return The new shape.
     */

    public static GLShapeCV makeSphere(String id, int iterations, float[][] colors) {
        // start with a double pyramid
        // which will be iteratively transformed into a sphere
        float[] baseColor = GLShapeFactoryCV.white;  // will not be visible
        int noBaseCorners = 8;  // base corners of the pyramid
//        float apexHeight = (float)(4*Math.sin(Math.PI/noBaseCorners));
        float colors2[][] = new float[colors.length][4];
        for (int i=0;i<colors2.length;i++)
            colors2[i] = colors[(i+1)%colors2.length];
        float apexHeight = 1;
        GLShapeCV startShape = GLShapeFactoryCV.makeBipyramid("Startshape",noBaseCorners,apexHeight,colors,colors);
        GLTriangleCV[] trianglesArray = startShape.getTriangles();
        ArrayList<GLTriangleCV> triangles = new ArrayList<GLTriangleCV>();
        for (GLTriangleCV triangle : trianglesArray)
            triangles.add(triangle);
        // explanation for the following see e.g. https://stackoverflow.com/questions/7687148/drawing-sphere-in-opengl-without-using-glusphere
        // split the triangles iteratively into four triangles each
        for (int i=0; i<iterations; i++) {
            ArrayList<GLTriangleCV> trianglesTmp = new ArrayList<GLTriangleCV>();
            for (GLTriangleCV triangle : triangles) {
                // split each triangle into four smaller triangles
                float[][] vertices = triangle.getVertices();
                float[] mid01 = GraphicsUtilsCV.midpoint(vertices[0], vertices[1]);
                float[] mid12 = GraphicsUtilsCV.midpoint(vertices[1], vertices[2]);
                float[] mid20 = GraphicsUtilsCV.midpoint(vertices[2], vertices[0]);
                // top
                trianglesTmp.add(new GLTriangleCV("", vertices[0], mid01, mid20, triangle.getUniformColor()));
                // left
                trianglesTmp.add(new GLTriangleCV("", mid01, vertices[1], mid12, triangle.getUniformColor()));
                // center
                trianglesTmp.add(new GLTriangleCV("", mid01, mid12, mid20, triangle.getUniformColor()));
                // right
                trianglesTmp.add(new GLTriangleCV("", mid20, mid12, vertices[2], triangle.getUniformColor()));
            }
            triangles = trianglesTmp;
        }
        trianglesArray = new GLTriangleCV[triangles.size()];
        int i=0;
        // normalize the vertex coordinates such that they lie equidistantly to the center
        for (GLTriangleCV triangle : triangles) {
            triangle.normalizeVertexVectors();
            trianglesArray[i++] = triangle;
        }
        GLShapeCV shape = new GLShapeCV(id,trianglesArray);
        return shape;
    }

    /**
     * Makes a hemisphere-like shape by iteratively transforming a double pyramid with 8 sides to approximate a hemisphere.
     * The radius at the base of the hemisphere and the radius of the "dome" will both be approximately 1.0.
     * The center of the hemisphere (i.e. the model coordinates (0.0,0.0,0.0)) will lie in the middle between this center
     * and the topmost vertex of the dome, i.e. all points within the base circle will have the y-coordinate -0.5 (in the model coordinate system)
     * and the topmost vertex will have the model coordinates (0.0, 0.5, 0.0).
     * N.B. In the current implementation, the hemisphere is hollow, i.e. has no base plane.
     * @param id The ID of the shape.
     * @param iterations The number of iterations to be run (3 or 4 will yield good results with reasonable execution times).
     * @param sideColors The colors of the "dome" of the shape.
     * @return The new shape.
     */

    public static GLShapeCV makeHemisphere(String id, int iterations, float[][] sideColors) {
        // start with a pyramid
        // which will be iteratively transformed into a hemisphere
        int noBaseCorners = 8;  // base corners of the pyramid
//        float apexHeight = (float)(4*Math.sin(Math.PI/noBaseCorners));
        float apexHeight = 1.0f;
        GLShapeCV startShape = GLShapeFactoryCV.makePyramid("Startshape",noBaseCorners,apexHeight,null,sideColors,false);
        startShape.moveCenterTo(0,-apexHeight/2,0);  // move the center to the base plane (otherwise normalizeVertexVectors() below will not work properly)
        GLTriangleCV[] trianglesArray = startShape.getTriangles();
        ArrayList<GLTriangleCV> triangles = new ArrayList<GLTriangleCV>();
        for (GLTriangleCV triangle : trianglesArray)
            triangles.add(triangle);
        // explanation for the following see e.g. https://stackoverflow.com/questions/7687148/drawing-sphere-in-opengl-without-using-glusphere
        // split the triangles iteratively into four triangles each
        for (int i=0; i<iterations; i++) {
            ArrayList<GLTriangleCV> trianglesTmp = new ArrayList<GLTriangleCV>();
            for (GLTriangleCV triangle : triangles) {
                // split each triangle into four smaller triangles
                float[][] vertices = triangle.getVertices();
                float[] mid01 = GraphicsUtilsCV.midpoint(vertices[0], vertices[1]);
                float[] mid12 = GraphicsUtilsCV.midpoint(vertices[1], vertices[2]);
                float[] mid20 = GraphicsUtilsCV.midpoint(vertices[2], vertices[0]);
                // top
                trianglesTmp.add(new GLTriangleCV("", vertices[0], mid01, mid20, triangle.getUniformColor()));
                // left
                trianglesTmp.add(new GLTriangleCV("", mid01, vertices[1], mid12, triangle.getUniformColor()));
                // center
                trianglesTmp.add(new GLTriangleCV("", mid01, mid12, mid20, triangle.getUniformColor()));
                // right
                trianglesTmp.add(new GLTriangleCV("", mid20, mid12, vertices[2], triangle.getUniformColor()));
            }
            triangles = trianglesTmp;
        }
        trianglesArray = new GLTriangleCV[triangles.size()];
        int i=0;
        // normalize the vertex coordinates such that they lie equidistantly to the center
        for (GLTriangleCV triangle : triangles) {
            triangle.normalizeVertexVectors();
            trianglesArray[i++] = triangle;
        }
        GLShapeCV shape = new GLShapeCV(id,trianglesArray);
        shape.moveCenterTo(0,apexHeight/2,0); // move the center back
        return shape;
    }

    /**
     * Makes a shape consisting of cubes. All cubes have the same coloring (faces and edge lines) and unit size, i.e. edge length 1
     * and will be placed into a three-dimensional raster.
     * @param id The ID of the shape.
     * @param faceColor The color of the faces of the cubes.
     * @param lineColor The color of the edge lines of the cubes.
     * @param lineWidth The width of the edge lines of the cubes.
     * @param positionsWithCubes A boolean array specifying the positions in the raster that shall hold a cube:
     * If positionsWithCubes[i][j][k] is true, a cube will be placed at raster position (i,j,k) (x,y,z coordinate).
     * The origin of the coordinate system of the shape, i.e. its point (0,0,0), lies in the center of this raster.
     * @return The new shape or null if a parameter is not valid.
     */

    public static GLShapeCV makeShapeFromCubes(String id, float[] faceColor, float[] lineColor, float lineWidth, boolean[][][] positionsWithCubes) {
        GLShapeCV result = null;
        try {
            int numberOfCubes = 0;
            for (int i=0;i<positionsWithCubes.length;i++)
                for (int j=0;j<positionsWithCubes[i].length;j++)
                    for (int k=0;k<positionsWithCubes[i][j].length;k++)
                        if (positionsWithCubes[i][j][k])
                            numberOfCubes++;
            GLShapeCV[] cubes = new GLShapeCV[numberOfCubes];
            float scaling[][] = new float[numberOfCubes][3];
            float rotation[][] = new float[numberOfCubes][3];
            float translation[][] = new float[numberOfCubes][3];
            for (int i=0;i<numberOfCubes;i++)
                for (int j=0;j<3;j++)
                    scaling[i][j] = 1;
            int cubeIndex = 0;
            for (int i=0;i<positionsWithCubes.length;i++)
                for (int j=0;j<positionsWithCubes[i].length;j++)
                    for (int k=0;k<positionsWithCubes[i][j].length;k++)
                        if (positionsWithCubes[i][j][k]) {
                            cubes[cubeIndex] = GLShapeFactoryCV.makeCube("cube_"+i+"_"+j+"_"+k,faceColor,lineColor,lineWidth);
                            translation[cubeIndex][0] = -positionsWithCubes.length/2+i;
                            translation[cubeIndex][1] = -positionsWithCubes[i].length/2+j;
                            translation[cubeIndex][2] = -positionsWithCubes[i][j].length/2+k;
                            cubeIndex++;
                        }
            result = joinShapes(id,cubes,scaling,rotation,translation,lineWidth);
        } catch (Exception e) { return null; }
        return result;
    }

    /*
    public static GLShapeCV makePyramid(String id, float baseX, float baseY, float baseEdgeLength, int noBaseCorners, float apexHeight, float[] baseColor, float[][] facesColors) {
        GLTriangleCV[] triangles = new GLTriangleCV[2*noBaseCorners];
        float colors[][] = new float[1][];
        colors[0] = baseColor;
        GLTriangleCV[] trianglesBase = trianglesForPolygon(baseX,baseY,baseEdgeLength,noBaseCorners,colors);
        for (int i=0; i<noBaseCorners; i++)
            triangles[i] = trianglesBase[i];
        for (int i=noBaseCorners; i<2*noBaseCorners; i++) {
            float vertices[][] = trianglesBase[i-noBaseCorners].getVertices();
            vertices[0][2] = apexHeight;
            triangles[i] = new GLTriangleCV("Side"+i,vertices,facesColors[(i-noBaseCorners)%facesColors.length]);
        }
        GLShapeCV shape = new GLShapeCV(id,triangles);
        return shape;
    }
*/


    /**
     * Makes a shape of three white cuboids that mark the x, y, and z axes of the world coordinate system.
     * @return The new shape.
     */

    public static GLShapeCV makeAxes() {
        return makeAxes(GLShapeFactoryCV.white,GLShapeFactoryCV.white,GLShapeFactoryCV.white,0.1f);
    }

    /**
     * Makes a shape of three cuboids that mark the x, y, and z axes of the world coordinate system.
     * @param colorX The color of the x axis.
     * @param colorY The color of the y axis.
     * @param colorZ The color of the z axis.
     * @return The new shape.
     */

    public static GLShapeCV makeAxes(float[] colorX, float[] colorY, float[] colorZ, float width) {
        GLShapeCV shapeAxisX = GLShapeFactoryCV.makeCuboid("axis_x",5f,width,width,colorX);
        GLShapeCV shapeAxisY = GLShapeFactoryCV.makeCuboid("axis_y",width,5f,width,colorY);
        GLShapeCV shapeAxisZ = GLShapeFactoryCV.makeCuboid("axis_z",width,width,5f,colorZ);
        // GLShapeCV axes = joinShapes("axes",shapeAxisX,shapeAxisY,1,1,1,0,0,0,0,0,0);
        // return joinShapes("axes",axes,shapeAxisZ,1,1,1,0,0,0,0,0,0);
        GLShapeCV axes = joinShapes("axes",shapeAxisX,shapeAxisY);
        return joinShapes("axes",axes,shapeAxisZ);
    }

    /**
     * Makes a shape in the form of a jet airplane that points into the negative z direction, i.e. straight away from the camera.
     * @param id The ID of the new shape.
     * @param faceColor The color of the wings, the nose, the vertical tail and some parts of the fuselage.
     * @param lineColor The color of the lines around wings and vertical tail and of some parts of the fuselage.
     * @return The new shape.
     **/

    public static GLShapeCV makeJetAirplane(String id, float[] faceColor, float[] lineColor) {
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
        GLShapeCV nose = GLShapeFactoryCV.makeHemisphere("Nose",3,colorFront);
        // cockpit
        float[][] colorCockpit = { GLShapeFactoryCV.lightgrey };
        GLShapeCV cockpit = GLShapeFactoryCV.makeHemisphere("Cockpit",3,colorCockpit);
        // fuselage
        GLShapeCV fuselage = GLShapeFactoryCV.makePrism("Fuselage",16,lengthFuselage,lightgrey,lightgrey,colors);
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
        translationArray[1][1] = 1f;
        translationArray[1][2] = -0.25f*lengthFuselage;
        rotationArray[0][0] = -90;
        rotationArray[2][0] = 90;
        GLShapeCV airplane = GLShapeFactoryCV.joinShapes(id,airplaneParts,rotationArray,translationArray,0);
        return airplane;
    }

    /**
     * Makes a shape in the form of a propeller plane that points into the x direction.
     * The propeller of the airplane is optionally animated. The animation will start at once.
     * @param animDuration If >0 the propeller will be animated for 'animDuration' milliseconds.
     * @param animSpeed The speed of the animation, i.e. the number of animation steps per second.
     * @return The new shape.
     **/

    public static GLShapeCV makePropellerAirplane(String id, int animDuration, int animSpeed) {
        final float lengthFuselage = 7;
        final float propellerthickness = 0.1f;
        final float propellerlength = 2f;
        final float wingspan = 12;
        final float wingthickness = 0.15f;
        final float wingbreadth = 2;
        final float heightVerticalTail = 3f;
        final float lengthVerticalTail = lengthFuselage/4.0f;
        float[][] colors = new float[2][];
        colors[0] = GLShapeFactoryCV.lightblue;
        colors[1] = GLShapeFactoryCV.blue;
        // fuselage
        GLShapeCV fuselage = GLShapeFactoryCV.makePrism("Fuselage",16,lengthFuselage,GLShapeFactoryCV.lightgrey,GLShapeFactoryCV.lightgrey,colors);
        // nose
        float[][] colorFront = new float[1][];
        colorFront[0] = colors[0];
        GLShapeCV nose = GLShapeFactoryCV.makeHemisphere("Nose",3,colorFront);
        // propellers
        GLShapeCV propellerPt1 = GLShapeFactoryCV.makeCuboid("PropPart1",propellerthickness,propellerlength,propellerthickness,GLShapeFactoryCV.white);
        GLShapeCV propellerPt2 = GLShapeFactoryCV.makeCuboid("PropPart2",propellerthickness,propellerthickness,propellerlength,GLShapeFactoryCV.white);
        GLShapeCV propeller = GLShapeFactoryCV.joinShapes("Propeller",propellerPt1,propellerPt2);
        // wings
        GLShapeCV lowerWing = GLShapeFactoryCV.makeCuboid("LowerWing",wingbreadth,wingthickness,wingspan,GLShapeFactoryCV.lightblue);
        GLShapeCV upperWing = GLShapeFactoryCV.makeCuboid("UpperWing",wingbreadth,wingthickness,wingspan,GLShapeFactoryCV.lightblue);
        // struts between wings
        GLShapeCV wingStrutLeft = GLShapeFactoryCV.makePrism("WingStrutLeft",10,3,GLShapeFactoryCV.lightblue);
        GLShapeCV wingStrutRight = GLShapeFactoryCV.makePrism("WingStrutRight",10,3,GLShapeFactoryCV.lightblue);
        // tails
        GLShapeCV verticalTail = GLShapeFactoryCV.makeCuboid("Vertical Tail",lengthVerticalTail,heightVerticalTail,0.1f,GLShapeFactoryCV.lightblue);
        GLShapeCV horizontalTail = GLShapeFactoryCV.makeCuboid("Horizontal Tail",lengthVerticalTail,0.1f,2*heightVerticalTail,GLShapeFactoryCV.lightblue);
        // tail wheel
        GLShapeCV tailWheelPart1 = GLShapeFactoryCV.makePrism("",10,12,GLShapeFactoryCV.lightblue);
        GLShapeCV tailWheelPart2 = GLShapeFactoryCV.makePrism("",20,1,GLShapeFactoryCV.lightblue);
        GLShapeCV tailWheel = GLShapeFactoryCV.joinShapes("TailWheel",tailWheelPart1,tailWheelPart2,3,2f,3,90,0,0,0,-4,0);
        // right wheel
        GLShapeCV rightWheelPart1 = GLShapeFactoryCV.makePrism("",10,16,GLShapeFactoryCV.lightblue);
        GLShapeCV rightWheelPart2 = GLShapeFactoryCV.makePrism("",20,1,GLShapeFactoryCV.lightblue);
        GLShapeCV rightWheel = GLShapeFactoryCV.joinShapes("RightWheel",rightWheelPart1,rightWheelPart2,3,2f,3,-45,0,0,0,-8f,1f);
        // left wheel
        GLShapeCV leftWheelPart1 = GLShapeFactoryCV.makePrism("",10,16,GLShapeFactoryCV.lightblue);
        GLShapeCV leftWheelPart2 = GLShapeFactoryCV.makePrism("",20,1,GLShapeFactoryCV.lightblue);
        GLShapeCV leftWheel = GLShapeFactoryCV.joinShapes("LeftWheel",leftWheelPart1,leftWheelPart2,3,2f,3,45,0,0,0,-8f,-1f);
        // join all shapes
        GLShapeCV[] airplaneParts = new GLShapeCV[12];
        airplaneParts[0] = fuselage;
        airplaneParts[1] = nose;
        airplaneParts[2] = propeller;
        airplaneParts[3] = lowerWing;
        airplaneParts[4] = upperWing;
        airplaneParts[5] = wingStrutLeft;
        airplaneParts[6] = wingStrutRight;
        airplaneParts[7] = verticalTail;
        airplaneParts[8] = horizontalTail;
        airplaneParts[9] = tailWheel;
        airplaneParts[10] = rightWheel;
        airplaneParts[11] = leftWheel;
        float[][] scalingArray = new float[airplaneParts.length][];
        for (int i=0;i<airplaneParts.length;i++)
            scalingArray[i] = new float[]{1,1,1};
        scalingArray[5] = new float[]{0.1f,1,0.1f};
        scalingArray[6] = new float[]{0.1f,1,0.1f};
        scalingArray[9] = new float[]{0.15f,0.15f,0.15f};
        scalingArray[10] = new float[]{0.15f,0.15f,0.15f};
        scalingArray[11] = new float[]{0.15f,0.15f,0.15f};
        float[][] rotationArray = new float[airplaneParts.length][3];
        rotationArray[0][2] = 90;
        rotationArray[1][2] = -90;
        rotationArray[1][1] = 90;
        rotationArray[9][2] = -45;
        rotationArray[10][0] = -45;
        rotationArray[11][0] = 45;
        float[][] translationArray = new float[airplaneParts.length][3];
        translationArray[1][0] = 0.5f*lengthFuselage+0.5f;
        translationArray[2][0] = 0.5f*lengthFuselage+1f+propellerthickness;
        translationArray[3][0] = 1f;
        translationArray[3][1] = -1;
        translationArray[4][0] = 1f;
        translationArray[4][1] = 2;
        translationArray[5][0] = 1f;
        translationArray[5][1] = 0.5f;
        translationArray[5][2] = -wingspan/3f;
        translationArray[6][0] = 1f;
        translationArray[6][1] = 0.5f;
        translationArray[6][2] = wingspan/3f;
        translationArray[7][0] = -lengthFuselage/2.0f+0.25f;
        translationArray[7][1] = heightVerticalTail/2.0f;
        translationArray[8][0] = -lengthFuselage/2.0f+0.25f;
        translationArray[9][0] = -lengthFuselage/2.0f+0.5f;
        translationArray[9][1] = -1.25f;
        translationArray[10][1] = -1.75f;
        translationArray[10][2] = 1f;
        translationArray[11][1] = -1.75f;
        translationArray[11][2] = -1f;
        GLShapeCV airplane = GLShapeFactoryCV.joinShapes(id,airplaneParts,scalingArray,rotationArray,translationArray,0);
        int sleepTime = 1000/animSpeed;
        (new Thread() {
            public void run() {
                int angle = 0;
                long startTime = System.currentTimeMillis();
                int propellersOffset = (airplaneParts[0].getNumberOfTriangles()+airplaneParts[1].getNumberOfTriangles())*9;
                float[] propellerTriangleCoordinates = new float[airplaneParts[2].getNumberOfTriangles()*9];
                int index=0;
                for (GLTriangleCV propellerTriangle : airplaneParts[2].getTriangles()) {
                    float[] triangleCoords = propellerTriangle.getVertexCoordinates();
                    for (int j=0;j<9;j++)
                        propellerTriangleCoordinates[index++] = triangleCoords[j];
                }
                while ((System.currentTimeMillis()-startTime)<animDuration) {
                    try {
                        Thread.currentThread().sleep(sleepTime);
                    } catch (Exception e) {}
                    double sin = Math.sin(angle), cos = Math.cos(angle);
                    for (int i=0;i<propellerTriangleCoordinates.length;i++) {
                       switch (i%3) {
                           case 0: break;
                           case 1: airplane.setTriangleVertexBufferEntry(propellersOffset+i,(float)(propellerTriangleCoordinates[i]*cos-propellerTriangleCoordinates[i+1]*sin)); break;
                           case 2: airplane.setTriangleVertexBufferEntry(propellersOffset+i,(float)(propellerTriangleCoordinates[i-1]*sin+propellerTriangleCoordinates[i]*cos)); break;
                       }
                    }
                    angle = (angle+10)%360;
                }
            }
        }).start();
        return airplane;
    }

    /**
     * Makes a shape in the form of a bird that points with its beak into the x direction.
     * The bird is optionally animated, i.e. will move its wings, tail, and beak. The animation will start at once.
     * @param animDuration If >0 the bird will be animated for 'animDuration' milliseconds.
     * @param animSpeed The speed of the animation, i.e. the number of animation steps per second.
     * @return The new shape.
     **/

    public static GLShapeCV makeBird(String id, int animDuration, int animSpeed) {
        GLShapeCV bird;
        int numberOfParts = 9;
        GLShapeCV[] shapes = new GLShapeCV[numberOfParts];
        float[][] verticesWing1 = {{-1,0,0},{1,0,0},{0,0,-2}};
        shapes[0] = GLShapeFactoryCV.makeTriangle("Wing1",verticesWing1,GLShapeFactoryCV.blue);
        float[][] verticesWing2 = {{-1,0,0},{1,0,0},{0,0,2}};
        shapes[1] = GLShapeFactoryCV.makeTriangle("Wing2",verticesWing2,GLShapeFactoryCV.blue);
        shapes[2] = GLShapeFactoryCV.makeSphere("Body",3, GLShapeFactoryCV.lightblue);
        shapes[3] = GLShapeFactoryCV.makeSphere("Head", 3, GLShapeFactoryCV.lightgreen);
        shapes[4] = GLShapeFactoryCV.makeSphere("LeftEye", 3, GLShapeFactoryCV.red);
        shapes[5] = GLShapeFactoryCV.makeSphere("RightEye", 3, GLShapeFactoryCV.red);
        final float beakY = 0.3f;
        float[][] verticesBeak = {{3f,beakY,0},{2f,beakY,0.5f},{2f,beakY,-0.5f}};
        shapes[6] = GLShapeFactoryCV.makeTriangle("BeakUpper", verticesBeak, GLShapeFactoryCV.lightred);
        shapes[7] = GLShapeFactoryCV.makeTriangle("BeakLower", verticesBeak, GLShapeFactoryCV.lightred);
        final float tailtipY = 0.75f;
        float[][] verticesTail = {{-2f,0,0},{-3.5f,tailtipY,0.5f},{-3.5f,tailtipY,-0.5f}};
        shapes[8] = GLShapeFactoryCV.makeTriangle("Tail", verticesTail, GLShapeFactoryCV.lightred);
        float[][] scalingArray = new float[numberOfParts][3];
        for (int i=0; i<numberOfParts; i++)
            for (int j=0;j<3;j++)
                scalingArray[i][j] = 1;
        scalingArray[2][0] = 2;
        scalingArray[2][1] = scalingArray[2][2] = 0.5f;
        scalingArray[3][0] = scalingArray[3][1] = scalingArray[3][2] = 0.5f;
        scalingArray[4][0] = scalingArray[4][1] = scalingArray[4][2] = 0.2f;
        scalingArray[5][0] = scalingArray[5][1] = scalingArray[5][2] = 0.2f;
        float[][] rotationArray = new float[numberOfParts][3];
        float[][] translationArray = new float[numberOfParts][3];
        // translationArray[2][1] = 0.5f;
        translationArray[3][0] = 1.8f;
        translationArray[3][1] = 0.4f;
        translationArray[4][0] = 2f;
        translationArray[4][1] = 0.7f;
        translationArray[4][2] = -0.2f;
        translationArray[5][0] = 2f;
        translationArray[5][1] = 0.7f;
        translationArray[5][2] = 0.2f;
        bird = GLShapeFactoryCV.joinShapes(id,shapes,scalingArray, rotationArray,translationArray,10);
        // TODO Thread-Erzeugung und -Start in eine GLAnimatorFactory-Methode auslagern, dabei stärkere Parametrisierung
        (new Thread() {
            public void run() {
                long startTime = System.currentTimeMillis();
                float wingTailTipYAdd = 0;
                float stepWingsTail = 0.3f;
                float beakTipYAdd = 0;
                float stepBeak = 0.05f;
                int wings2Offset = shapes[0].getNumberOfTriangles()*9;
                int beakUpperOffset = 0;
                for (int i=0;i<6;i++)
                    beakUpperOffset += shapes[i].getNumberOfTriangles()*9;
                int beakLowerOffset = beakUpperOffset + shapes[6].getNumberOfTriangles()*9;
                int tailOffset = beakLowerOffset+shapes[7].getNumberOfTriangles()*9;
                int sleepTime = 100;
                if (animSpeed>0) sleepTime = 1000/animSpeed;
                while ((System.currentTimeMillis()-startTime)<animDuration) {
                    try {
                        Thread.currentThread().sleep(sleepTime);
                    } catch (Exception e) {}
                    bird.setTriangleVertexBufferEntry(7,wingTailTipYAdd);
                    bird.setTriangleVertexBufferEntry(wings2Offset+7,wingTailTipYAdd);
                    bird.setTriangleVertexBufferEntry(tailOffset+4,tailtipY+wingTailTipYAdd);
                    bird.setTriangleVertexBufferEntry(tailOffset+7,tailtipY+wingTailTipYAdd);
                    bird.setTriangleVertexBufferEntry(beakUpperOffset+1,beakY+beakTipYAdd);
                    bird.setTriangleVertexBufferEntry(beakLowerOffset+1,beakY-beakTipYAdd);
                    wingTailTipYAdd += stepWingsTail;
                    if (wingTailTipYAdd>1.25f||wingTailTipYAdd<-1.25f) stepWingsTail=-stepWingsTail;
                    beakTipYAdd += stepBeak;
                    if (beakTipYAdd<=0||beakTipYAdd>=0.3) stepBeak=-stepBeak;
                }
            }
        }).start();
        return bird;
    }

    /**
     * Joins two shapes, i.e. builds a new shape from the triangles and lines of two existing shapes.
     * The shapes must have the same coloring / texturing type.
     * <BR>
     * The triangles and lines of the two shaped are taken as they are, i.e. copies of them are directly added to the new shape.
     * @param id The ID of the new shape.
     * @param shape1 The first shape to be joined.
     * @param shape2 The second shape to be joined.
     * @return The new shape.
     */

    public static GLShapeCV joinShapes(String id, GLShapeCV shape1, GLShapeCV shape2) {
        return joinShapes(id, shape1, shape2, 1, 1, 1,0, 0, 0, 0, 0, 0);
    }

    /**
     * Joins two shapes, i.e. builds a new shape from the triangles and lines of two existing shapes.
     * The shapes must have the same coloring / texturing type.
     * <BR>
     * The triangles and lines of the first shape are taken as they are, i.e. copies of them are directly added to the new shape.
     * The triangles and lines of the second shape are transformed, i.e. copies of them are scaled, rotated and translated
     * by modifying their vertex coordinates accordingly and then added to the new shape.
     * Afterwards, all vertex coordinates refer to the local coordinate system ("model coordinate system") of the new shape,
     * which is originally the coordinate system of the first shape.
     * @param id The ID of the new shape.
     * @param shape1 The first shape to be joined.
     * @param shape2 The second shape to be joined.
     * @param shape2_scaleX The scaling factor for the second shape - x dimension.
     * @param shape2_scaleY The scaling factor for the second shape - y dimension.
     * @param shape2_scaleZ The scaling factor for the second shape - z dimension.
     * @param shape2_rotAngleX The rotation angle for the second shape around the x axis.
     * @param shape2_rotAngleY The rotation angle for the second shape around the y axis.
     * @param shape2_rotAngleZ The rotation angle for the second shape around the z axis.
     * @param shape2_transX The translation vector for the second shape - x dimension.
     * @param shape2_transY The translation vector for the second shape - y dimension.
     * @param shape2_transZ The translation vector for the second shape - z dimension.
     * @return The new shape.
     */

    public static GLShapeCV joinShapes(String id, GLShapeCV shape1, GLShapeCV shape2,
                                       float shape2_scaleX, float shape2_scaleY, float shape2_scaleZ,
                                       float shape2_rotAngleX, float shape2_rotAngleY, float shape2_rotAngleZ,
                                       float shape2_transX, float shape2_transY, float shape2_transZ) {
        return joinShapes(id, shape1, shape2, shape2_scaleX, shape2_scaleY, shape2_scaleZ,
                shape2_rotAngleX, shape2_rotAngleY, shape2_rotAngleZ, shape2_transX, shape2_transY, shape2_transZ, 0, 0, 0);
    }

    /**
     * Joins two shapes, i.e. builds a new shape from the triangles and lines of two existing shapes.
     * The shapes must have the same coloring / texturing type.
     * <BR>
     * The triangles and lines of the first shape are taken as they are, i.e. copies of them are directly added to the new shape.
     * Also the line width is copied.
     * The triangles and lines of the second shape are transformed, i.e. copies of them are scaled, rotated and translated
     * by modifying their vertex coordinates accordingly and then added to the new shape.
     * Afterwards, all vertex coordinates refer to the local coordinate system ("model coordinate system") of the new shape,
     * which is originally the coordinate system of the first shape.
     * Depending on the parameters moveCenter_X/Y/Z, however, the origin of this system, i.e. the point (0,0,0), is translated
     * - see explanation of the method GLShape.moveCenterTo().
     * N.B.: If more than two shapes are combined, i.e. if this method is called multiple times with the same shape as the first
     * and different shapes as the second parameter, moveCenter_X/Y/Z should be zero in all calls except the last one,
     * i.e. the origin should be modified only once and only as the final step.
     * @param id The ID of the new shape.
     * @param shape1 The first shape to be joined.
     * @param shape2 The second shape to be joined.
     * @param shape2_scaleX The scaling factor for the second shape - x dimension.
     * @param shape2_scaleY The scaling factor for the second shape - y dimension.
     * @param shape2_scaleZ The scaling factor for the second shape - z dimension.
     * @param shape2_rotAngleX The rotation angle for the second shape around the x axis.
     * @param shape2_rotAngleY The rotation angle for the second shape around the y axis.
     * @param shape2_rotAngleZ The rotation angle for the second shape around the z axis.
     * @param shape2_transX The translation vector for the second shape - x dimension.
     * @param shape2_transY The translation vector for the second shape - y dimension.
     * @param shape2_transZ The translation vector for the second shape - z dimension.
     * @param moveCenterTo_X The translation vector for the origin of the coordinate system - x dimension.
     * @param moveCenterTo_Y The translation vector for the origin of the coordinate system - y dimension.
     * @param moveCenterTo_Z The translation vector for the origin of the coordinate system - z dimension.
     * @return The new shape.
     */

    public static GLShapeCV joinShapes(String id, GLShapeCV shape1, GLShapeCV shape2,
                                       float shape2_scaleX, float shape2_scaleY, float shape2_scaleZ,
                                       float shape2_rotAngleX, float shape2_rotAngleY, float shape2_rotAngleZ,
                                       float shape2_transX, float shape2_transY, float shape2_transZ,
                                       float moveCenterTo_X, float moveCenterTo_Y, float moveCenterTo_Z) {
        GLShapeCV newShape = new GLShapeCV(id,shape1.getTriangles(),shape1.getLines(),shape1.getLineWidth());
        GLTriangleCV[] triangles2 = shape2.getTriangles();
        if (triangles2!=null) {
            for (GLTriangleCV triangle : triangles2)
                triangle.transform(shape2_scaleX, shape2_scaleY, shape2_scaleZ, shape2_rotAngleX, shape2_rotAngleY, shape2_rotAngleZ, shape2_transX, shape2_transY, shape2_transZ);
            newShape.addTriangles(triangles2);
        }
        GLLineCV[] lines2 = shape2.getLines();
        if (lines2!=null) {
            for (GLLineCV line : lines2)
                line.transform(shape2_scaleX, shape2_scaleY, shape2_scaleZ, shape2_rotAngleX, shape2_rotAngleY, shape2_rotAngleZ, shape2_transX, shape2_transY, shape2_transZ);
            newShape.addLines(lines2);
        }
        newShape.moveCenterTo(moveCenterTo_X,moveCenterTo_Y,moveCenterTo_Z);
        return newShape;
    }

    /**
     * Joins a collection of shapes, i.e. builds a new shape from the triangles and lines of the existing shapes.
     * All shapes must have the same coloring / texturing type.
     * <BR>
     * The triangles and lines of the two shaped are taken as they are, i.e. copies of them are directly added to the new shape.
     * @param id The ID  of the new shape.
     * @param shapes The shapes to be joined.
     * @param lineWidth The width of all lines in the new shape.
     * @return The new shape or null if a parameter is not valid.
     */

    public static GLShapeCV joinShapes(String id, GLShapeCV[] shapes, float lineWidth) {
        float scalingDummy[][] = new float[shapes.length][3];
        for (int i=0;i<scalingDummy.length;i++)
            for (int j=0;j<3;j++)
                scalingDummy[i][j] = 1;
        float nullMatrix[][] = new float[shapes.length][3];
        return joinShapes(id,shapes,scalingDummy,nullMatrix,nullMatrix,lineWidth);
    }

    /**
     * Joins a collection of shapes, i.e. builds a new shape from the triangles and lines of the existing shapes.
     * All shapes must have the same coloring / texturing type.
     * <BR>
     * The triangles and lines of all shapes are transformed, i.e. copies of them are rotated and translated (but not scaled)
     * by modifying their vertex coordinates accordingly and then added to the new shape.
     * By this the shapes are placed into the local coordinate system ("model coordinate system") of the new shape
     * and thus placed in relation to each others.
     * @param id The ID of the new shape.
     * @param shapes The shapes to be joined.
     * @param rotation The respective rotation angles for the shapes, specified and applied analogously to the scaling parameter.
     * @param translation The respective translation values for the shapes, specified and applied analogously to the scaling parameter.
     * @param lineWidth The width of all lines in the new shape.
     * @return The new shape or null if a parameter is not valid.
     */

    public static GLShapeCV joinShapes(String id, GLShapeCV[] shapes, float[][] rotation, float[][] translation, float lineWidth) {
        float scalingDummy[][] = new float[shapes.length][3];
        for (int i=0;i<scalingDummy.length;i++)
            for (int j=0;j<3;j++)
            scalingDummy[i][j] = 1;
        return joinShapes(id,shapes,scalingDummy,rotation,translation,lineWidth);
    }

    /**
     * Joins a collection of shapes, i.e. builds a new shape from the triangles and lines of the existing shapes.
     * All shapes must have the same coloring / texturing type.
     * <BR>
     * The triangles and lines of all shapes are transformed, i.e. copies of them are scaled, rotated and translated
     * by modifying their vertex coordinates accordingly and then added to the new shape.
     * By this the shapes are placed into the local coordinate system ("model coordinate system") of the new shape
     * and thus placed in relation to each others.
     * @param id The ID of the new shape.
     * @param shapes The shapes to be joined.
     * @param scaling The respective scaling factors for the shapes.
     * Must be a two-dimensional array of size n*3 where n is the number of shapes.
     * The coordinates of shape i are scaled by scaling[i][j] in the j-th dimension (with j=0,1,2 for the x/y/z dimension)
     * and thus transformed to the coordinate system of the new shape.
     * @param rotation The respective rotation angles for the shapes, specified and applied analogously to the scaling parameter.
     * @param translation The respective translation values for the shapes, specified and applied analogously to the scaling parameter.
     * @param lineWidth The width of all lines in the new shape.
     * @return The new shape or null if a parameter is not valid.
     */

    // TODO Doppelte und nicht sichtbare Dreiecke löschen
    // TODO Ausführung beschleunigen (zum Beispiel, aber nicht nur, die Dreiecke der einzelnen Shapes nicht kopieren, sondern direkt übernehmen)

    public static GLShapeCV joinShapes(String id, GLShapeCV[] shapes,
                                       float[][] scaling,
                                       float[][] rotation,
                                       float[][] translation,
                                       float lineWidth) {

        // long start = System.nanoTime();

        GLShapeCV joinedShape = null;
        try {
            joinedShape = new GLShapeCV(id,null);
            for (int i=0;i<shapes.length;i++) {
                GLTriangleCV[] triangles = shapes[i].getTriangles();
                if (triangles != null) {
                    for (GLTriangleCV triangle : triangles)
                        triangle.transform(scaling[i][0], scaling[i][1], scaling[i][2],
                                rotation[i][0], rotation[i][1], rotation[i][2],
                                translation[i][0], translation[i][1], translation[i][2]);
                    joinedShape.addTriangles(triangles);
                }
                GLLineCV[] lines = shapes[i].getLines();
                if (lines != null) {
                    for (GLLineCV line : lines)
                        line.transform(scaling[i][0], scaling[i][1], scaling[i][2],
                                rotation[i][0], rotation[i][1], rotation[i][2],
                                translation[i][0], translation[i][1], translation[i][2]);
                    joinedShape.addLines(lines);
                }
                // Log.v("GLDEMO","---- "+shapes[i].getId()+" "+joinedShape.getTriangles().length);
            }
            joinedShape.setLineWidth(lineWidth);
        } catch (Exception e) {
            Log.e("GLDEMO",e.getMessage());
            return null; }

        // long duration = System.nanoTime() - start;
        // Log.v("GLDEMO",">>> joinShapes: "+duration/1000000+" ms");

        return joinedShape;
    }

   /**
     * IDs for the two triangles of a square.
     * <UL>
     * <LI>SquareUpperLeft
     * <LI>SquareLowerRight
     * </UL>
     */

    public static String squareTriangleIDs[] = { "SquareUpperLeft", "SquareLowerRight" };

    /** Make two triangles for a square in the x-y plane (i.e with z=0).
     * The sides of the square will be parallel to the respective axes of the underlying coordinate system.
     * The IDs of the triangles will be taken from the array 'squareTriangleIDs'.
     * @param leftUpperCornerX x coordinate of the left upper corner.
     * @param leftUpperCornerY y coordinate of the left upper corner.
     * @param sideLength side length of the square.
     * @return An array with the two triangles (array component 0: upper left triangle, array component 1: lower right triangle). Null if sideLength<=0.
     */

    public static GLTriangleCV[] trianglesForSquare(float leftUpperCornerX, float leftUpperCornerY, float sideLength) {
        if (sideLength <= 0.0) return null;
        GLTriangleCV triangles[] = new GLTriangleCV[2];
        float vertices[][] = new float [3][];
        vertices[0] = new float[] { leftUpperCornerX, leftUpperCornerY, 0 };
        vertices[1] = new float[] { leftUpperCornerX, leftUpperCornerY - sideLength, 0 };
        vertices[2] = new float[] { leftUpperCornerX + sideLength, leftUpperCornerY, 0 };
        triangles[0] = new GLTriangleCV(squareTriangleIDs[0], vertices);
        vertices[0] = new float[] { leftUpperCornerX + sideLength, leftUpperCornerY, 0 };
        vertices[1] = new float[] { leftUpperCornerX, leftUpperCornerY - sideLength, 0 };
        vertices[2] = new float[] { leftUpperCornerX + sideLength, leftUpperCornerY - sideLength, 0 };
        triangles[1] = new GLTriangleCV(squareTriangleIDs[1], vertices);
        return triangles;
    }

    /**
     * Make the triangles for a regular polygon in the x-y plane (i.e with z=0) with its center at model coordinates (0,0,0).
     * The vertices will lie on the circumcircle of the polygon and hence their coordinates will be calculated from the radius of this circle.
     * The polygon will point upwards, i.e. its topmost vertex will have the model coordinates (0,radiusCircumcircle,0).
     * <BR>
     * The triangles will be created in counter-clockwise order from two neighboring vertices of the polygon and the center of the polygon,
     * starting with the topmost vertex. They will have the IDs Triangle00, Triangle01, ..., Triangle10, ...
     * in the order of their creation.
     * <BR>
     * The colors are specified by an array on some length n and are assigned cyclically to the triangles, i.e. triangle no. i gets color no. i%n, % being the modulo operator.
     * @param numberOfCorners The number of corners of the polygon (must be > 2).
     * @param colors The colors of the triangles (see explanation above). Must be a valid color definition according to the method isValidColorsArray().
     * @return The triangles of the new polygon. Null if one of the parameters is not valid (see above).
     */

    public static GLTriangleCV[] trianglesForPolygon(int numberOfCorners, float radiusCircumcircle, float colors[][]) {
        return trianglesForPolygon(numberOfCorners,radiusCircumcircle,0,colors);
    }

    /**
     * Make the triangles for a regular polygon.
     * The polygon will lie in a plane parallel to the x-y plane with a constant z coordinate 'z_const' and have its center at model coordinates (0,0,z_const).
     * The vertices will lie on the circumcircle of the polygon and hence their coordinates will be calculated from the radius of this circle.
     * The polygon will point upwards, i.e. its topmost vertex will have the model coordinates (0,radiusCircumcircle,z_const).
     * <BR>
     * The triangles will be created in counter-clockwise order from two neighboring vertices of the polygon and the center of the polygon,
     * starting with the topmost vertex. They will have the IDs Triangle00, Triangle01, ..., Triangle10, ...
     * in the order of their creation.
     * <BR>
     * The colors are specified by an array on some length n and are assigned cyclically to the triangles, i.e. triangle no. i gets color no. i%n, % being the modulo operator.
     * @param numberOfCorners The number of corners of the polygon (must be > 2).
     * @param z_const The common z coordinate of all vertices.
     * @param colors The colors of the triangles (see explanation above). Must be a valid color definition according to the method isValidColorsArray().
     * @return The triangles of the new polygon. Null if one of the parameters is not valid (see above).
     */

    public static GLTriangleCV[] trianglesForPolygon(int numberOfCorners, float radiusCircumcircle, float z_const, float colors[][]) {
        if (numberOfCorners < 3) return null;
        if (radiusCircumcircle <= 0.0) return null;
        if (!isValidColorsArray(colors)) return null;
        GLTriangleCV triangles[] = new GLTriangleCV[numberOfCorners];
        // 1.) calculate the coordinates of the polygon corners
        float[][] pointsOnCircle = GraphicsUtilsCV.pointsOnCircle2D(0,0,radiusCircumcircle,numberOfCorners);
        float center[] = { 0,0,z_const };
        for (int i = 0; i < triangles.length; i++) {
            float vertices[][] = new float[3][3];
            vertices[0] = center;
            vertices[1][0] = pointsOnCircle[i][0];
            vertices[1][1] = pointsOnCircle[i][1];
            vertices[1][2] = z_const;
            vertices[2][0] = pointsOnCircle[(i+1)%triangles.length][0];
            vertices[2][1] = pointsOnCircle[(i+1)%triangles.length][1];
            vertices[2][2] = z_const;
            String id;
            if (i<9)
                id = "Triangle0" + i;
            else
                id = "Triangle" + i;
            triangles[i] = new GLTriangleCV(id, vertices,colors[i%colors.length]);
            // Log.v("DEMO","Triangle "+i+": "+vertices[0][0]+","+vertices[0][1]+"  "+vertices[1][0]+","+vertices[1][1]+"  "+vertices[2][0]+","+vertices[2][1]);
        }
        return triangles;
    }

    /**
     * Make the triangles for a regular polygon in the x-y plane (i.e with z=0). [Alternative calculation method]
     * <BR>
     * The vertex coordinates are calculated from the three parameters 'topCornerX', 'topCornerY', and 'sideLength'
     * as described in the comment for the method polygonVertexCoordinates().
     * </UL>
     * The triangles will be created in clock-wise order from two neighboring vertices of the polygon and the center of the polygon.
     * <BR>
     * The triangles will have the IDs Triangle00, Triangle01, ..., Triangle10, ... in the order by which their vertices have been calculated (see above).
     * <BR>
     * The colors are defined by an array on some length n and are assigned cyclically to the triangles, i.e. triangle no. i gets color no. i%n, % being the modulo operator.
     * @param topCornerX See explanation above.
     * @param topCornerY See explanation above.
     * @param sideLength See explanation above. must be > 0.
     * @param numberOfCorners The number of corners of the polygon (must be > 2).
     * @param colors The colors of the triangles (see explanation above). Must be a valid color definition according to the method isValidColorsArray().
     * @return The triangles of the new polygon. Null if one of the parameters is not valid (see above).
     */

    public static GLTriangleCV[] trianglesForPolygon_AlternativeVersion(float topCornerX, float topCornerY, float sideLength, int numberOfCorners, float colors[][]) {
        return trianglesForPolygon_AlternativeVersion(topCornerX, topCornerY, sideLength, numberOfCorners, 0, colors);
    }

    /**
     * Make the triangles for a regular polygon in a plane with a specific z coordinate. [Alternative calculation method]
     * <BR>
     * The vertex coordinates are calculated from the three parameters 'topCornerX', 'topCornerY', and 'sideLength'
     * as described in the comment for the method polygonVertexCoordinates().
     * </UL>
     * The triangles will be created in clock-wise order from two neighboring vertices of the polygon and the center of the polygon.
     * <BR>
     * The triangles will have the IDs Triangle00, Triangle01, ..., Triangle10, ... in the order by which their vertices have been calculated (see above).
     * <BR>
     * The colors are defined by an array on some length n and are assigned cyclically to the triangles, i.e. triangle no. i gets color no. i%n, % being the modulo operator.
     * @param topCornerX See explanation above.
     * @param topCornerY See explanation above.
     * @param sideLength See explanation above. must be > 0.
     * @param numberOfCorners The number of corners of the polygon (must be > 2).
     * @param z The common z coordinate of all vertices.
     * @param colors The colors of the triangles (see explanation above). Must be a valid color definition according to the method isValidColorsArray().
     * @return The triangles of the new polygon. Null if one of the parameters is not valid (see above).
     */

    public static GLTriangleCV[] trianglesForPolygon_AlternativeVersion(float topCornerX, float topCornerY, float sideLength, int numberOfCorners, float z, float colors[][]) {
        if (sideLength <= 0.0) return null;
        if (numberOfCorners < 3) return null;
        if (!isValidColorsArray(colors)) return null;
        GLTriangleCV triangles[] = new GLTriangleCV[numberOfCorners];
        // 1.) calculate the coordinates of the polygon corners
        float corners[][] = polygonVertexCoordinates_AlternativeVersion(topCornerX,topCornerY,sideLength,numberOfCorners,z);
        // 2.) calculate the center of the polygon
        float xc = topCornerX + sideLength/2;
        float x1 = corners[1][0], y1 = corners[1][1], x2 = corners[2][0], y2 = corners[2][1];
        float yc = (2*xc*x1-2*xc*x2-x1*x1+x2*x2-y1*y1+y2*y2)/(2*y2-2*y1);
        float center[] = new float[3];
        center[0] = xc;
        center[1] = yc;
        center[2] = z;
        // 3.) create the triangles from the center and the polygon corners
        for (int i = 0; i < triangles.length; i++) {
            float vertices[][] = new float[3][];
            vertices[0] = center;
            vertices[1] = corners[(i+1)%triangles.length];
            vertices[2] = corners[i];
            String id;
            if (i<9)
                id = "Triangle0" + i;
            else
                id = "Triangle" + i;
            triangles[i] = new GLTriangleCV(id, vertices,colors[i%colors.length]);
            // Log.v("DEMO","Triangle "+i+": "+vertices[0][0]+","+vertices[0][1]+"  "+vertices[1][0]+","+vertices[1][1]+"  "+vertices[2][0]+","+vertices[2][1]);
        }
        return triangles;
    }

    /**
     * Calculate the vertex coordinates of a regular polygon in a plane with a specific z coordinate. [Alternative calculation method]
     * <BR>
     * The coordinates are calculated from the three parameters 'topCornerX', 'topCornerY', and 'sideLength':
     * <UL>
     * <LI>The first and second vertex have the coordinates (topCornerX,topCornerY,z) and (topCornerX+sideLength,topCornerY,z),
     * i.e. define the horizontal upper side of the polygon.
     * <LI>The coordinates of the third vertex are calculated by attaching a new side horizontally to the upper side
     * and then rotating it by the angle that is required to form a polygon of the given number of corners.
     * <LI>The following vertices are calculated in the same way.
     * </UL>
     * @param topCornerX See explanation above.
     * @param topCornerY See explanation above.
     * @param sideLength See explanation above. must be > 0.
     * @param numberOfCorners The number of corners of the polygon (must be > 2).
     * @param z The common z coordinate of all vertices.
     * @return The coordinates of the polygon vertices or null if one of the parameters is not valid (see above).
     * The index in the first dimension of this array is the number of the vertex,
     * vertex 0 being the top corner vertex (see above) and the other vertices following in clock-wise order.
     * In the second dimension, 0 denotes the x, 1 the y and 2 the z value of the vertex coordinate.
     */

    public static float[][] polygonVertexCoordinates_AlternativeVersion(float topCornerX, float topCornerY, float sideLength, int numberOfCorners, float z) {
        float innerAngle = (float) ((numberOfCorners - 2) * Math.PI / numberOfCorners);
                                 // sum of all inner angles: (n-2)*180 > one angle = ((n-2)*180)/n
        // 1.) calculate the coordinates of the horizontal upper side
        float corners[][] = new float[numberOfCorners][3];
        corners[0][0] = topCornerX;
        corners[0][1] = topCornerY;
        corners[0][2] = z;
        corners[1][0] = corners[0][0] + sideLength;
        corners[1][1] = corners[0][1];
        corners[1][2] = z;
        // 2.) attach new side horizontally to the corner that has been calculated last
        //     a rotate it such that the inner angle will be the inner angle of a regular polygon with the given number of corners.
        for (int i = 2; i < numberOfCorners; i++) {
            float rotAngle = (float) (2 * Math.PI - (i - 1) * (Math.PI - innerAngle));
            corners[i][0] = (float) (sideLength * Math.cos(rotAngle) + corners[i - 1][0]);
            corners[i][1] = (float) (sideLength * Math.sin(rotAngle) + corners[i - 1][1]);
            corners[i][2] = z;
        }
        return corners;
    }

    /*
    // Utility method to make the triangles for a regular polygon
    // with edge length and number of corners/vertices specified by parameters.
    // The top edge of the polygon has end points (topCornerX,topCornerY) and (topCornerX+sideLength,topCornerY).
    // All resulting triangles have (topCornerX,topCornerY) as a common vertex
    // - i.e. if v0 = (topCornerX,topCornerY), v1 = (topCornerX+sideLength,topCornerY), v2, v3, ... are the vertices/corners of the polygon
    // then its triangles are (v0,v2,v1), (v0,v3,v2), (v0,v4,v3), ...

    public static GLTriangleCV[] trianglesForPolygon_Vs2(float topCornerX, float topCornerY, float sideLength, int numberOfCorners) {
        if (numberOfCorners < 3) return null;
        GLTriangleCV triangles[] = new GLTriangleCV[numberOfCorners - 2];
        float angle = (float) ((numberOfCorners - 2) * Math.PI / numberOfCorners); // Winkelsumme n-Eck: (n-2)*180 Grad > ein Winkel = ((n-2)*180)/n
        // Zunächst die Koordinaten der Ecken berechnen:
        // 1.) Polygon hat oben eine waagerechte Seite der Länge 'sideLength'
        float corners[][] = new float[numberOfCorners][3];
        corners[0][0] = topCornerX;
        corners[0][1] = topCornerY;
        corners[0][2] = 0;
        corners[1][0] = corners[0][0] + sideLength;
        corners[1][1] = corners[0][1];
        corners[1][2] = 0;
        // 2.) Koordinaten der jeweils nächsten Ecke ergeben sich, indem man an der aktuellen Ecke eine waagerechte Strecke ansetzt
        //     und dann so rotiert, dass sich an dieser Ecke der Innenwinkel 'angle' ergibt.
        for (int i = 2; i < numberOfCorners; i++) {
            float rotAngle = (float) (2 * Math.PI - (i - 1) * (Math.PI - angle));
            corners[i][0] = (float) (sideLength * Math.cos(rotAngle) + corners[i - 1][0]);
            corners[i][1] = (float) (sideLength * Math.sin(rotAngle) + corners[i - 1][1]);
            corners[i][2] = 0;
        }
        // 3.) Aus den Eckpunkten die Dreiecke bilden
        for (int i = 0; i < triangles.length; i++) {
            float vertices[][] = new float[3][];
            vertices[0] = corners[0];
            vertices[1] = corners[i + 2];
            vertices[2] = corners[i + 1];
            triangles[i] = new GLTriangleCV("Triangle" + i, vertices);
        }
        return triangles;
    }
    */
    
    /**
     * IDs for the twelve triangles of a cube.
     * <UL>
     * <LI>CubeFront01: legs = top side and left side of the front square of the cube
     * <LI>CubeFront02: legs = bottom side and right side of the front square of the cube
     * <BR>
     * [In the following items, "top", "left" etc. denote the positions of the sides of a face when one looks at this face,
     *                          i.e. after the cube has been rotated such that this face has become front face of the cube.]
     * <LI>CubeRight01: legs = top side and left side of the right square of the cube
     * <LI>CubeRight02: legs = bottom side and right side of the right square of the cube
     * <LI>CubeBack01: legs = top side and left side of the back square of the cube
     * <LI>CubeBack02: legs = bottom side and right side of the back square of the cube
     * <LI>CubeLeft01, CubeLeft02, CubeTop01, CubeTop02, CubeBottom01, CubeBottom02 with the same meanings
     * </UL>
     */

    public static String[] cubeTriangleIDs = {
            "CubeFront01", "CubeFront02", "CubeRight01", "CubeRight02", "CubeBack01","CubeBack02",
            "CubeLeft01", "CubeLeft02", "CubeTop01", "CubeTop02", "CubeBottom01","CubeBottom02" };

    /**
     * Make the triangles for a colored cube.
     * <BR>
     * The vertex coordinates are calculated from the four parameters 'frontLeftUpperCorner_X', 'frontLeftUpperCorner_Y', 'frontLeftUpperCorner_Z', and 'edgeLength'.
     * The triangles will be defined in such a way that cube will be "in level", i.e. its edges will be parallel to the respective axes of the underlying coordinate system.
     * <BR>
     * @param frontLeftUpperCorner_X The x coordinate of the frontal left upper vertex of the cube.
     * @param frontLeftUpperCorner_Y The y coordinate of the frontal left upper vertex of the cube.
     * @param frontLeftUpperCorner_Z The z coordinate of the frontal left upper vertex of the cube.
     * @param edgeLength The edge length of the cube.
     * @param colors The colors of the triangles. This must be a two-dimensional array with size n*4. For the first dimension, n may have these values:
     * <UL>
     * <LI>n=1: color[0] is the uniform color of all faces of the cube.
     * <LI>n=6: color[0-5] define the colors of the six faces of the cube (in the order front, right, back, left, top, bottom)
     * <LI>n=12: color[0-11] define the colors of the twelve triangles of the cube (in the order as specified by cubeTriangleIDs)
     * </UL>
     * In the second dimension, all entries must be valid color definitions (see method isValidColorArray()).
     * @return The triangles for the cube in the order as defined by 'cubeTriangleIDs'. Null if the colors parameter is not valid (see above).
     */

    public static GLTriangleCV[] trianglesForColoredCube(float frontLeftUpperCorner_X, float frontLeftUpperCorner_Y, float frontLeftUpperCorner_Z, float edgeLength, float[][] colors) {
        return trianglesForColoredCuboid(frontLeftUpperCorner_X,frontLeftUpperCorner_Y,frontLeftUpperCorner_Z,edgeLength,edgeLength,edgeLength,colors);
/*
        if (!isValidColorsArray(colors)
                ||(colors.length!=1&&colors.length!=6&&colors.length!=12)) return null;
        GLTriangleCV triangles[] = new GLTriangleCV[12];
        int tIndex = 0;
        float vertices[][] = new float[3][];
        float colorsLocal[][] = new float[12][];
        int noColors = colors.length;
        switch (noColors) {
            case 1:  for (int i=0; i<12; i++)
                colorsLocal[i] = colors[0];
                break;
            case 6:  for (int i=0; i<12; i++)
                colorsLocal[i] = colors[i/2];
                break;
            case 12: colorsLocal = colors; break;
            default: return null;
        }
        // front face (triangle left/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // front face (triangle right/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // right face (triangle front/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength,  frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // right face (triangle back/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength};
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // back face (triangle right/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // back face (triangle left/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // left face (triangle back/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // left face (triangle front/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // top face (triangle left/front)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // top face (triangle right/back)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // bottom face (triangle left/front)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // bottom face (triangle right/back)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        return triangles;
 */
    }

    /**
     * Make the triangles for a colored cuboid.
     * <BR>
     * The vertex coordinates are calculated from the position of the frontal left upper vertex and the lengths of the three edges ot the cuboid.
     * The triangles will be defined in such a way that the cuboid will be "in level", i.e. its edges will be parallel to the respective axes of the underlying coordinate system.
     * <BR>
     * @param frontLeftUpperCorner_X The x coordinate of the frontal left upper vertex of the cuboid.
     * @param frontLeftUpperCorner_Y The y coordinate of the frontal left upper vertex of the cuboid.
     * @param frontLeftUpperCorner_Z The z coordinate of the frontal left upper vertex of the cuboid.
     * @param edgeLength_X The edge length of the cuboid in the x direction.
     * @param edgeLength_Y The edge length of the cuboid in the y direction.
     * @param edgeLength_Z The edge length of the cuboid in the z direction.
     * @param colors The colors of the triangles. This must be a two-dimensional array with size n*4. For the first dimension, n may have these values:
     * <UL>
     * <LI>n=1: color[0] is the uniform color of all faces of the cube.
     * <LI>n=6: color[0-5] define the colors of the six faces of the cube (in the order front, right, back, left, top, bottom)
     * <LI>n=12: color[0-11] define the colors of the twelve triangles of the cube (in the order as specified by cubeTriangleIDs)
     * </UL>
     * In the second dimension, all entries must be valid color definitions (see method isValidColorArray()).
     * @return The triangles for the cuboid in the order as defined by 'cubeTriangleIDs'. Null if the colors parameter is not valid (see above).
     */

    public static GLTriangleCV[] trianglesForColoredCuboid(float frontLeftUpperCorner_X, float frontLeftUpperCorner_Y, float frontLeftUpperCorner_Z, float edgeLength_X, float edgeLength_Y, float edgeLength_Z, float[][] colors) {
        if (!isValidColorsArray(colors)
                ||(colors.length!=1&&colors.length!=6&&colors.length!=12)) return null;
        GLTriangleCV triangles[] = new GLTriangleCV[12];
        int tIndex = 0;
        float vertices[][] = new float[3][];
        float colorsLocal[][] = new float[12][];
        int noColors = colors.length;
        switch (noColors) {
            case 1:  for (int i=0; i<12; i++)
                colorsLocal[i] = colors[0];
                break;
            case 6:  for (int i=0; i<12; i++)
                colorsLocal[i] = colors[i/2];
                break;
            case 12: colorsLocal = colors; break;
            default: return null;
        }
        // front face (triangle left/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // front face (triangle right/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // right face (triangle front/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength_X,  frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // right face (triangle back/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength_Z};
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // back face (triangle right/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // back face (triangle left/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // left face (triangle back/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // left face (triangle front/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // top face (triangle left/front)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // top face (triangle right/back)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // bottom face (triangle left/front)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // bottom face (triangle right/back)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colorsLocal[tIndex]);
        return triangles;
    }

    /**
     * Make the lines for a "wireframe cuboid", i.e. a shape with lines that mark the edges of the cuboid.
     * <BR>
     * The vertex coordinates are calculated from the position of the frontal left upper vertex and the lengths of the three edges ot the cuboid.
     * The lines will be defined in such a way that the cuboid will be "in level", i.e. its edges will be parallel to the respective axes of the underlying coordinate system.
     * <BR>
     * @param frontLeftUpperCorner_X The x coordinate of the frontal left upper vertex of the cuboid.
     * @param frontLeftUpperCorner_Y The y coordinate of the frontal left upper vertex of the cuboid.
     * @param frontLeftUpperCorner_Z The z coordinate of the frontal left upper vertex of the cuboid.
     * @param edgeLength_X The edge length of the cuboid in the x direction.
     * @param edgeLength_Y The edge length of the cuboid in the y direction.
     * @param edgeLength_Z The edge length of the cuboid in the z direction.
     * @return The lines for the cuboid. The indices of this array have these meanings:
     * <UL>
     * <LI>First index: line number (0,1,...)
     * <LI>Second index: 0 = first end point of the line, 1 = second end point.
     * <LI>Third index: 0 = x coordinate of the end point, 1 = y coordinate, 2 = z coordinate.
     * </UL>
     */

    public static GLLineCV[] linesForWireframeCuboid(float frontLeftUpperCorner_X, float frontLeftUpperCorner_Y, float frontLeftUpperCorner_Z, float edgeLength_X, float edgeLength_Y, float edgeLength_Z, float[] color) {
        float vertices[][] = new float[8][3];
        GLLineCV lines[] = new GLLineCV[12];
        // vertices
        // front top left
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        // front top right
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        // front bottom left
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z };
        // front bottom right
        vertices[3] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z };
        // back top left
        vertices[4] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        // back top right
        vertices[5] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        // back bottom left
        vertices[6] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        // back bottom right
        vertices[7] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        // lines
        // front top
        lines[0] = new GLLineCV("LineFrontTop",vertices[0].clone(),vertices[1].clone(),color);
        lines[1] = new GLLineCV("LineFrontLeft",vertices[0].clone(),vertices[2].clone(),color);
        lines[2] = new GLLineCV("LineFrontBottom",vertices[2].clone(),vertices[3].clone(),color);
        lines[3] = new GLLineCV("LineFrontRight",vertices[3].clone(),vertices[1].clone(),color);
        lines[4] = new GLLineCV("LineRightBottom",vertices[3].clone(),vertices[7].clone(),color);
        lines[5] = new GLLineCV("LineRightBack",vertices[5].clone(),vertices[7].clone(),color);
        lines[6] = new GLLineCV("LineRightTop",vertices[1].clone(),vertices[5].clone(),color);
        lines[7] = new GLLineCV("LineBackBottom",vertices[6].clone(),vertices[7].clone(),color);
        lines[8] = new GLLineCV("LineBackLeft",vertices[4].clone(),vertices[6].clone(),color);
        lines[9] = new GLLineCV("LineBackTop",vertices[4].clone(),vertices[5].clone(),color);
        lines[10] = new GLLineCV("LineLeftBottom",vertices[2].clone(),vertices[6].clone(),color);
        lines[11] = new GLLineCV("LineLeftTop",vertices[0].clone(),vertices[4].clone(),color);
        /*
        lines[0][0] = vertices[0].clone();
        lines[0][1] = vertices[1].clone();
        // front left
        lines[1][0] = vertices[0].clone();
        lines[1][1] = vertices[2].clone();
        // front bottom
        lines[2][0] = vertices[2].clone();
        lines[2][1] = vertices[3].clone();
        // front right
        lines[3][0] = vertices[3].clone();
        lines[3][1] = vertices[1].clone();
        // right bottom
        lines[4][0] = vertices[3].clone();
        lines[4][1] = vertices[7].clone();
        // right back
        lines[5][0] = vertices[5].clone();
        lines[5][1] = vertices[7].clone();
        // right top
        lines[6][0] = vertices[1].clone();
        lines[6][1] = vertices[5].clone();
        // back bottom
        lines[7][0] = vertices[6].clone();
        lines[7][1] = vertices[7].clone();
        // back left
        lines[8][0] = vertices[4].clone();
        lines[8][1] = vertices[6].clone();
        // back top
        lines[9][0] = vertices[4].clone();
        lines[9][1] = vertices[5].clone();
        // left bottom
        lines[10][0] = vertices[2].clone();
        lines[10][1] = vertices[6].clone();
        // left top
        lines[11][0] = vertices[0].clone();
        lines[11][1] = vertices[4].clone();
*/
        return lines;
    }
    //
    // Utility method to make the triangles for a cube with color gradients.
    // NOCH UNVOLLSTÄNDIG, DA MIT FESTEN FARBWERTEN
    /*
    public static GLTriangleCV[] trianglesForCubeGradientColors(float frontLeftUpperCorner_X, float frontLeftUpperCorner_Y, float frontLeftUpperCorner_Z, float edgeLength) {
        GLTriangleCV triangles[] = new GLTriangleCV[12];
        int tIndex = 0;
        float vertices[][] = new float[3][];
        float colors[][] = new float[3][];
        // front face (triangle left/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        colors[0] = colors[1] = colors[2] = blue;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colors);
        tIndex++;
        // front face (triangle right/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        colors[0] = colors[1] = colors[2] = orange;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colors);
        tIndex++;
        // right face (triangle front/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength,  frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        colors[0] = colors[1] = colors[2] = red;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colors);
        tIndex++;
        // right face (triangle back/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength};
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        colors[0] = colors[1] = colors[2] = green;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colors);
        tIndex++;
        // back face (triangle right/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        colors[0] = colors[1] = colors[2] = white;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colors);
        tIndex++;
        // back face (triangle left/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        colors[0] = colors[1] = colors[2] = grey;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colors);
        tIndex++;
        // left face (triangle back/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        colors[0] = colors[1] = colors[2] = darkgrey;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colors);
        tIndex++;
        // left face (triangle front/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        colors[0] = colors[1] = colors[2] = lightgrey;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colors);
        tIndex++;
        // top face (triangle left/front)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        colors[0] = colors[1] = colors[2] = yellow;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colors);
        tIndex++;
        // top face (triangle right/back)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        colors[0] = colors[1] = colors[2] = purple;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colors);
        tIndex++;
        // bottom face (triangle left/front)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        colors[0] = colors[1] = colors[2] = cyan;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colors);
        tIndex++;
        // bottom face (triangle right/back)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        colors[0] = colors[1] = colors[2] = magenta;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,colors);
        return triangles;
    }
*/

    /**
     * Make the triangles for a textured cube.
     * <BR>
     * The vertex coordinates are calculated from the four parameters 'frontLeftUpperCorner_X', 'frontLeftUpperCorner_Y', 'frontLeftUpperCorner_Z', and 'edgeLength'.
     * The triangles will be defined in such a way that cube will be "in level", i.e. its edges will be parallel to the respective axes of the underlying coordinate system.
     * <BR>
     * @param frontLeftUpperCorner_X The x coordinate of the frontal left upper vertex of the cube.
     * @param frontLeftUpperCorner_Y The y coordinate of the frontal left upper vertex of the cube.
     * @param frontLeftUpperCorner_Z The z coordinate of the frontal left upper vertex of the cube.
     * @param edgeLength The edge length of the cube.
     * @param textures The textures for the triangles. This must be an array of length 6 with the bitmaps for the cube faces in this order: front, right, back, left, top, bottom.
     * @return The triangles for the cube in the order as defined by 'cubeTriangleIDs'. Null if the textures parameter is not valid (see above).
     */

    public static GLTriangleCV[] trianglesForTexturedCube(float frontLeftUpperCorner_X, float frontLeftUpperCorner_Y, float frontLeftUpperCorner_Z, float edgeLength, Bitmap[] textures) {
        if (textures==null||textures.length!=6) return null;
        GLTriangleCV triangles[] = new GLTriangleCV[12];
        int tIndex = 0;
        float vertices[][] = new float[3][];
        float uvCoord1[] = {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
        };
        float uvCoord2[] = {
                1.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
        };
        // front face (triangle left/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,textures[tIndex/2],uvCoord1);
        tIndex++;
        // front face (triangle right/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,textures[tIndex/2],uvCoord2);
        tIndex++;
        // right face (triangle front/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength,  frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,textures[tIndex/2],uvCoord1);
        tIndex++;
        // right face (triangle back/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength};
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,textures[tIndex/2],uvCoord2);
        tIndex++;
        // back face (triangle left/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,textures[tIndex/2],uvCoord1);
        tIndex++;
        // back face (triangle right/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,textures[tIndex/2],uvCoord2);
        tIndex++;
        // left face (triangle back/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,textures[tIndex/2],uvCoord1);
        tIndex++;
        // left face (triangle front/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,textures[tIndex/2],uvCoord2);
        tIndex++;
        // top face (triangle left/front)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,textures[tIndex/2],uvCoord1);
        tIndex++;
        // top face (triangle right/back)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,textures[tIndex/2],uvCoord2);
        tIndex++;
        // bottom face (triangle left/front)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,textures[tIndex/2],uvCoord1);
        tIndex++;
        // bottom face (triangle right/back)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleIDs[tIndex],vertices,textures[tIndex/2],uvCoord2);
        return triangles;
    }

    // TODO trianglesForCuboid with textures

    /** Color constant */
    public static final float[] white = { 1.0f, 1.0f, 1.0f, 1.0f };
    /** Color constant */
    public static final float[] lightgrey = { 0.8f, 0.8f, 0.8f, 1.0f };
    /** Color constant */
    public static final float[] grey = { 0.6f, 0.6f, 0.6f, 1.0f };
    /** Color constant */
    public static final float[] darkgrey = { 0.4f, 0.4f, 0.4f, 1.0f };
    /** Color constant */
    public static final float[] black = { 0.0f, 0.0f, 0.0f, 1.0f };
    /** Color constant */
    public static final float[] yellow = { 1.0f, 1.0f, 0.0f, 1.0f };
    /** Color constant */
    public static final float[] lightyellow = { 1.0f, 1.0f, 0.5f, 1.0f };
    /** Color constant */
    public static final float[] orange = { 1.0f, 0.647f, 0.0f, 1.0f };
    /** Color constant */
    public static final float[] red = { 1.0f, 0.0f, 0.0f, 1.0f };
    /** Color constant */
    public static final float[] lightred = { 1.0f, 0.28f, 0.3f, 1.0f };
    /** Color constant */
    public static final float[] blue = { 0.0f, 0.0f, 1.0f, 1.0f };
    /** Color constant */
    public static final float[] lightblue = { 0.678f, 0.847f, 0.902f, 1.0f };
    /** Color constant */
    public static final float[] green = { 0.0f, 1.0f, 0.0f, 1.0f };
    /** Color constant */
    public static final float[] lightgreen = { 0.565f, 0.933f, 0.565f, 1.0f };
    /** Color constant */
    public static final float[] darkgreen = { 0.0f, 0.3922f, 0.0f, 1.0f };
    /** Color constant */
    public static final float[] cyan = { 0.0f, 1.0f, 1.0f, 1.0f };
    /** Color constant */
    public static final float[] magenta = { 1.0f, 0.0f, 1.0f, 1.0f };
    /** Color constant */
    public static final float[] purple = { 0.5785f, 0.4392f, 0.8588f, 1.0f };

    /*
    private static final String[] colorNames = {
            "WHITE","YELLOW","SALMON","ORANGE","ORANGE2","RED","MAGENTA",
            "VIOLET2","VIOLETRED","LIGHTSKYBLUE","BLUE","CYAN","OLIVE","GREEN","SIENNA","BLACK" };

    private static final int[] colorValues =
            { 0xFFFFFFFF,0xFFFFFF00,0xFFFA8072,0xFFFF8C44,0xFFFFA500,0xFFFF0000,0xFFFF00FF
                    ,0xFFEE82EE,0xFFD02090,0xFF87CEFA,0xFF0000FF,0xFF00FFFF,0xFFDDDD00,0xFF00FF00,0xFFA0522D,0xFF000000 };
    */

    /**
     * Check if a float array validly specifies a color.
     * This is the case if the array has length 4 and contains only values between 0.0 and 1.0.
     * @param colorArray The array to be checked.
     * @return true if the parameter is not null and the validity check has succeeded, false otherwise.
     */

    public static boolean isValidColorArray(float[] colorArray) {
        if (colorArray==null||colorArray.length!=4) return false;
        for (int i=0; i<colorArray.length; i++)
            if (colorArray[i]<0.0||colorArray[i]>1.0) return false;
        return true;
    }

    /**
     * Check if a two-dimensional float array 'colorsArray' validly specifies a set of colors.
     * This is the case if for all indices i, colorsArray[i] is a float array of size 4 and contains values between 0.0 and 1.0.
     * @param colorsArray The array to be checked.
     * @return true if the parameter is not null and the validity check has succeeded, false otherwise.
     */

    public static boolean isValidColorsArray(float[][] colorsArray) {
        if (colorsArray==null) return false;
        for (int i=0; i<colorsArray.length; i++)
            if (!isValidColorArray(colorsArray[i])) return false;
        return true;
    }

    /*
    static float[][] doublesToFloat(double twoDimArray[][]) {
        float[][] result = new float[twoDimArray.length][twoDimArray[0].length];
        for (int i=0; i<twoDimArray.length; i++)
            for (int j=0; j<twoDimArray[0].length; j++)
                result[i][j] = (float) twoDimArray[i][j];
        return result;
    }
    */

    /*  The same as makePyramid but base polygon parallel to the x-y plane and apex pointing into negative z direction

        public static GLShapeCV makePyramidOld(String id, int noBaseCorners, float apexHeight, float[] baseColor, float[][] facesColors) {
        if (noBaseCorners<3) return null;
        if (apexHeight<=0.0) return null;
        if (!isValidColorArray(baseColor)) return null;
        if (!isValidColorsArray(facesColors)) return null;
        GLTriangleCV[] triangles = new GLTriangleCV[2*noBaseCorners];
        float colors[][] = new float[1][];
        colors[0] = baseColor;
        float baseEdgeLength = 1;
        float baseX = -baseEdgeLength/2.0f;
        float baseY = (float) (0.5/Math.tan(Math.PI/2-(noBaseCorners-2)/(2.0*noBaseCorners)*Math.PI));
        GLTriangleCV[] trianglesBase = trianglesForPolygon(baseX,baseY, baseEdgeLength,noBaseCorners,apexHeight/2.0f, colors);
        for (int i=0; i<noBaseCorners; i++)
            triangles[i] = trianglesBase[i];
        for (int i=noBaseCorners; i<2*noBaseCorners; i++) {
            float vertices[][] = trianglesBase[i-noBaseCorners].getVertices();
            vertices[0][2] = -apexHeight/2.0f;
            triangles[i] = new GLTriangleCV("Side"+i,vertices,facesColors[(i-noBaseCorners)%facesColors.length]);
        }
        GLShapeCV shape = new GLShapeCV(id,triangles);
        return shape;
    }

    */

    /*
    public static GLShapeCV makeBirdOld(int animDuration) {
        GLShapeCV bird;
        int numberOfParts = 9;
        GLShapeCV[] shapes = new GLShapeCV[numberOfParts];
        float[][] verticesWing1 = {{-1,0,0},{1,0,0},{0,0,-2}};
        shapes[0] = GLShapeFactoryCV.makeTriangle("Wing1",verticesWing1,GLShapeFactoryCV.blue);
        float[][] verticesWing2 = {{-1,0,0},{1,0,0},{0,0,2}};
        shapes[1] = GLShapeFactoryCV.makeTriangle("Wing2",verticesWing2,GLShapeFactoryCV.blue);
        shapes[2] = GLShapeFactoryCV.makeSphere("Body",3, GLShapeFactoryCV.lightblue);
        shapes[3] = GLShapeFactoryCV.makeSphere("Head", 3, GLShapeFactoryCV.lightgreen);
        shapes[4] = GLShapeFactoryCV.makeSphere("LeftEye", 3, GLShapeFactoryCV.red);
        shapes[5] = GLShapeFactoryCV.makeSphere("RightEye", 3, GLShapeFactoryCV.red);
        float[][] verticesBeak = {{3.5f,0.3f,0},{2f,0.3f,0.5f},{2f,0.3f,-0.5f}};
        shapes[6] = GLShapeFactoryCV.makeTriangle("BeakUpper", verticesBeak, GLShapeFactoryCV.lightred);
        shapes[7] = GLShapeFactoryCV.makeTriangle("BeakLower", verticesBeak, GLShapeFactoryCV.lightred);
        float[][] verticesTail = {{-2f,0,0},{-3.5f,0.75f,0.5f},{-3.5f,0.75f,-0.5f}};
        shapes[8] = GLShapeFactoryCV.makeTriangle("Tail", verticesTail, GLShapeFactoryCV.lightred);
        float[][] scalingArray = new float[numberOfParts][3];
        for (int i=0; i<numberOfParts; i++)
            for (int j=0;j<3;j++)
                scalingArray[i][j] = 1;
        scalingArray[2][0] = 2;
        scalingArray[2][1] = scalingArray[2][2] = 0.5f;
        scalingArray[3][0] = scalingArray[3][1] = scalingArray[3][2] = 0.5f;
        scalingArray[4][0] = scalingArray[4][1] = scalingArray[4][2] = 0.2f;
        scalingArray[5][0] = scalingArray[5][1] = scalingArray[5][2] = 0.2f;
        float[][] rotationArray = new float[numberOfParts][3];
        float[][] translationArray = new float[numberOfParts][3];
        // translationArray[2][1] = 0.5f;
        translationArray[3][0] = 1.8f;
        translationArray[3][1] = 0.4f;
        translationArray[4][0] = 2f;
        translationArray[4][1] = 0.7f;
        translationArray[4][2] = -0.2f;
        translationArray[5][0] = 2f;
        translationArray[5][1] = 0.7f;
        translationArray[5][2] = 0.2f;
        bird = GLShapeFactoryCV.joinShapes("Bird",shapes,scalingArray, rotationArray,translationArray,10);
        // TODO Thread-Erzeugung und -Start in eine GLAnimatorFactory-Methode auslagern, dabei stärkere Parametrisierung
        (new Thread() {
            public void run() {
                long startTime = System.currentTimeMillis();
                float stepWingsTail = 0.3f;
                float stepBeak = 0.05f;
                String[] animatedTriangles = { "Wing1", "Wing2", "BeakUpper", "BeakLower", "Tail", "Tail" };
                int[] vertexNos = { 2, 2, 0, 0, 1, 2 };
                float[][] vertexValues = new float[6][3];
                vertexValues[0] = verticesWing1[2].clone();
                vertexValues[1] = verticesWing2[2].clone();
                vertexValues[2] = verticesBeak[0].clone();
                vertexValues[3] = verticesBeak[0].clone();
                vertexValues[4] = verticesTail[1].clone();
                vertexValues[5] = verticesTail[2].clone();
                bird.setTriangleVertices(animatedTriangles,vertexNos,vertexValues);
                while ((System.currentTimeMillis()-startTime)<20000) {
                    try {
                        Thread.currentThread().sleep(50);
                    } catch (Exception e) {}
                    vertexValues[0][1]+=stepWingsTail;
                    vertexValues[1][1]+=stepWingsTail;
                    vertexValues[4][1]-=stepWingsTail;
                    vertexValues[5][1]-=stepWingsTail;
                    if (vertexValues[0][1]>1.25f||vertexValues[0][1]<-1.25f) stepWingsTail=-stepWingsTail;
                    vertexValues[2][1]+=stepBeak;
                    vertexValues[3][1]-=stepBeak;
                    if (vertexValues[2][1]>0.5f||vertexValues[2][1]<=0.05f) stepBeak=-stepBeak;
                    bird.setTriangleVertices(vertexValues);
                }
            }
        }).start();
        return bird;
    }
   */

}
