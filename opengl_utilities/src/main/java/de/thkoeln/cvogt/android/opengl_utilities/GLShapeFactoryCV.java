// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 17.3.2022

package de.thkoeln.cvogt.android.opengl_utilities;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Class with static methods to easily create specific shapes and triangles for them.
 * <BR>
 * @see de.thkoeln.cvogt.android.opengl_utilities.GLShapeCV
 * @see de.thkoeln.cvogt.android.opengl_utilities.GLTriangleCV
 */

public class GLShapeFactoryCV {

    /**
     * Make a square with a uniform color.
     * <BR>
     * The square will have an edge length of 1.0, lie in the x-y plane (i.e. with z = 0) parallel to the axes,
     * and its center will have the model coordinates (0.0,0.0,0.0),
     * such the its left upper vertex will have the model coordinates (-0.5f,0.5f,0.0f).
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
     * such the its left upper vertex will have the model coordinates (-0.5f,0.5f,0.0f).
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
     * such the its left upper vertex will have the model coordinates (-0.5f,0.5f,0.0f).
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
     * The polygon will lie in the x-y plane (i.e. with z = 0).
     * Its center will have the model coordinates (0.0,0.0,0.0).
     * The uppermost edge of the base polygon will have length 1.0 and lie horizontally symmetrically above the polygon center,
     * i.e. its end points will have the x coordinates -0.5f and 0.5f and their y coordinates will be identical
     * (their exact values resulting from the number of edges which in turn results from its number of edges).
     * The triangles of the polygon will have the center (0.0,0.0,0.0) as a common vertex.
     * @param id The ID of the polygon.
     * @param noCorners The number of corners of the polygon (must be > 2).
     * @param color The color. Must be a valid color definition according to the method isValidColorArray().
     * @return The new polygon. Null if one of the parameters is not valid (see above).
     */

    public static GLShapeCV makePolygon(String id, int noCorners, float[] color) {
        if (noCorners<3||!isValidColorArray(color)) return null;
        float sideLength = 1;
        float leftUpperCorner_X = -sideLength/2.0f;
        float leftUpperCorner_Y = (float) (0.5/Math.tan(Math.PI/2-(noCorners-2)/(2.0*noCorners)*Math.PI));
        float colors[][] = new float[1][];
        colors[0]=color;
        GLTriangleCV[] triangles = trianglesForPolygon(leftUpperCorner_X, leftUpperCorner_Y, sideLength, noCorners, colors);
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
     * The polygon will lie in the x-y plane (i.e. with z = 0).
     * Its center will have the model coordinates (0.0,0.0,0.0).
     * The uppermost edge of the base polygon will have length 1.0 and lie horizontally symmetrically above the polygon center,
     * i.e. its end points will have the x coordinates -0.5f and 0.5f and their y coordinates will be identical
     * (their exact values resulting from the number of edges which in turn results from its number of edges).
     * The triangles of the polygon will have the center (0.0,0.0,0.0) as a common vertex.
     * @param id The ID of the polygon.
     * @param noCorners The number of corners of the polygon (must be > 2).
     * @param colors The colors of the triangles. Must be a valid color definition according to the method isValidColorsArray().
     * @return The new polygon. Null if one of the parameters is not valid (see above).
     */

    public static GLShapeCV makePolygon(String id, int noCorners, float[][] colors) {
        if (noCorners<3||!isValidColorsArray(colors)) return null;
        float sideLength = 1;
        float leftUpperCorner_X = -sideLength/2.0f;
        float leftUpperCorner_Y = (float) (0.5/Math.tan(Math.PI/2-(noCorners-2)/(2.0*noCorners)*Math.PI));
        GLTriangleCV[] triangles = trianglesForPolygon(leftUpperCorner_X, leftUpperCorner_Y, sideLength, noCorners, colors);
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
     * such the its left upper front vertex will have the model coordinates (-0.5f,0.5f,0.5f) etc.
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
     * Make a cuboid with colored faces, all faces having the same color.
     * <BR>
     * The cuboid will be "in level", i.e. its edges will be parallel to the respective axes of the underlying coordinate system.
     * The center of the cuboid will have the model coordinates (0.0,0.0,0.0)
     * such the its left upper front vertex will have the model coordinates (-0.5f*edgeLength_X,0.5f*edgeLength_Y,0.5f*edgeLength_Z) etc.
     * @param id The ID of the cuboid.
     * @param edgeLength_X The edge length in the x dimension.
     * @param edgeLength_Y The edge length in the y dimension.
     * @param edgeLength_Z The edge length in the z dimension.
     * @param color The color of the faces (array of length 4).
     * @return The new cuboid. Null if 'color' is not a valid color array (see method isValidColorArray()).
     */

    public static GLShapeCV makeCuboid(String id, float edgeLength_X, float edgeLength_Y, float edgeLength_Z, float color[]) {
        if (!isValidColorArray(color)) return null;
        float leftFrontUpperCorner_X = -edgeLength_X/2.0f;   // set values such that model coordinates (0,0,0) are the center of the cube
        float leftFrontUpperCorner_Y = edgeLength_Y/2.0f;
        float leftFrontUpperCorner_Z = edgeLength_Z/2.0f;
        float[][] colors = new float[1][];
        colors[0] = color;
        return new GLShapeCV(id,trianglesForColoredCuboid(leftFrontUpperCorner_X, leftFrontUpperCorner_Y, leftFrontUpperCorner_Z, edgeLength_X, edgeLength_Y, edgeLength_Z, colors));
    }

    /**
     * Make a cube with colored faces.
     * <BR>
     * The cube will be "in level", i.e. its edges will be parallel to the respective axes of the underlying coordinate system.
     * The edges of the cube will have a length of 1.0.
     * The center of the cube will have the model coordinates (0.0,0.0,0.0)
     * such the its left upper front vertex will have the model coordinates (-0.5f,0.5f,0.5f) etc.
     * @param id The ID of the cube.
     * @param colors The colors of the faces. This must be a two-dimensional array with size n*4. For the first dimension, n may have these values:
     * <UL>
     * <LI>n=1: color[0] is the uniform color of all faces of the cube.
     * <LI>n=6: color[0-5] define the colors of the six faces of the cube (in the order front, right, back, left, top, bottom)
     * <LI>n=12: color[0-11] define the colors of the twelve triangles of the cube (in the order as specified by cubeTriangleNames)
     * </UL>
     * @return The new cube. Null if 'colors' is not a valid color array (see method isValidColorsArray()) or has not a valid size (1, 6, or 12) in its first dimension.
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
     * Make a cuboid with colored faces.
     * <BR>
     * The cuboid will be "in level", i.e. its edges will be parallel to the respective axes of the underlying coordinate system.
     * The center of the cuboid will have the model coordinates (0.0,0.0,0.0)
     * such the its left upper front vertex will have the model coordinates (-0.5f*edgeLength_X,0.5f*edgeLength_Y,0.5f*edgeLength_Z) etc.
     * @param id The ID of the cuboid.
     * @param edgeLength_X The edge length in the x dimension.
     * @param edgeLength_Y The edge length in the y dimension.
     * @param edgeLength_Z The edge length in the z dimension.
     * @param colors The colors of the faces. This must be a two-dimensional array with size n*4. For the first dimension, n may have these values:
     * <UL>
     * <LI>n=1: color[0] is the uniform color of all faces of the cuboid.
     * <LI>n=6: color[0-5] define the colors of the six faces of the cuboid (in the order front, right, back, left, top, bottom)
     * <LI>n=12: color[0-11] define the colors of the twelve triangles of the cuboid (in the order as specified by cubeTriangleNames)
     * </UL>
     * @return The new cuboid. Null if 'colors' is not a valid color array (see method isValidColorsArray()) or has not a valid size (1, 6, or 12) in its first dimension.
     */

    public static GLShapeCV makeCuboid(String id, float edgeLength_X, float edgeLength_Y, float edgeLength_Z, float colors[][]) {
        if (!isValidColorsArray(colors)
                ||(colors.length!=1&&colors.length!=6&&colors.length!=12)) return null;
        float leftFrontUpperCorner_X = -edgeLength_X/2.0f;   // set values such that model coordinates (0,0,0) are the center of the cube
        float leftFrontUpperCorner_Y = edgeLength_Y/2.0f;
        float leftFrontUpperCorner_Z = edgeLength_Z/2.0f;
        return new GLShapeCV(id,trianglesForColoredCuboid(leftFrontUpperCorner_X, leftFrontUpperCorner_Y, leftFrontUpperCorner_Z, edgeLength_X, edgeLength_Y, edgeLength_Z, colors));
    }

    /**
     * Make a cube with textured faces.
     * <BR>
     * The cube will be "in level", i.e. its edges will be parallel to the respective axes of the underlying coordinate system.
     * The edges of the cube will have a length of 1.0.
     * The center of the cube will have the model coordinates (0.0,0.0,0.0)
     * such the its left upper front vertex will have the model coordinates (-0.5f,0.5f,0.5f) etc.
     * @param id The ID of the cube.
     * @param textures The bitmaps for the face textures. This must be an array of length 6.
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

    // TODO makeCuboid with textures

    /**
     * Make a regular-based pyramid or a cone with colored faces.
     * <BR>
     * The base will be a polygon parallel to the x-z plane with y = apexHeight/2.0f as constructed by trianglesForPolygon()
     * and rotated by 90 deg around the x axis.
     * The center of the base polygon will have the model coordinates (0.0,-apexHeight/2,0.0).
     * The uppermost edge of the base polygon will have length 1.0 and lie symmetrically above the polygon center,
     * i.e. its end points will have the x coordinates -0.5f and 0.5f and their z coordinates will be identical
     * (their exact values resulting from the form of the polygon which in turn results from its number of edges).
     * <BR>
     * The apex of the pyramid will have the model coordinates (0.0, apexHeight/2.0f, 0.0),
     * i.e. will be positioned above the center of the base polygon.
     * @param id The ID of the pyramid.
     * @param noBaseCorners The number of corners of the base polygon (must be > 2). Note that for higher numbers (e.g. 64) the pyramid will appear as a cone.
     * @param apexHeight The height of the apex (must be > 0).
     * @param baseColor The color of the base polygon. Must be a valid color definition according to the method isValidColorArray().
     * @param facesColors The colors of the pyramid faces. Must be a valid colors definition array according to method isValidColorsArray().
     * The colors are defined by an array on some length n and are assigned cyclically to the triangles, i.e. triangle no. i gets color no. i%n, % being the modulo operator.
     * @return The new pyramid. Null if one of the parameters is not valid (see above).
     */

    public static GLShapeCV makePyramid(String id, int noBaseCorners, float apexHeight, float[] baseColor, float[][] facesColors) {
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
        for (GLTriangleCV triangle : triangles)
            triangle.transform(1,1,1,90, 0, 0,0,0,0);
        GLShapeCV shape = new GLShapeCV(id,triangles);
        return shape;
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
     * Its base triangle will be in parallel to the x-z plane, its base edge will be in parallel to the x axis
     * and its tip will point into negative z direction.
     * The edges will have length 1.0.
     * <BR>
     * The vertex coordinates will therefore be:
     * <UL>
     * <LI>base line of the base triangle - left end: (-0.5,-1/4*sqrt(2/3),sqrt(3)/6)
     * <LI>base line of the base triangle - right end: (0.5,-1/4*sqrt(2/3),sqrt(3)/6)
     * <LI>tip of the base triangle - right end: (0.0,-1/4*sqrt(2/3),-sqrt(3)/3)
     * <LI>apex: (0.0,3/4*sqrt(2/3),0.0)
     * </UL>
     * The order of the triangles of the tetrahedron will be:
     * triangle 0 = base triangle, triangle 1 = front triangle (as seen from the camera), triangle 2 = right triangle, triangle 3 = left triangle.
     * The names of the triangles will be: "Base", "Front", "Right", "Left".
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
     * Make a bipyramid as a combination of two pyramids, as constructed by makePyramid().
     * <BR>
     * The apexes of the bipyramid will point in opposite directions along the z axis
     * and have the model coordinates (0.0, 0.0, apexHeight) and  (0.0, 0.0, -apexHeight), respectively.
     * The bipyramid is symmetric to a polygon as constructed by trianglesForPolygon().
     * This polygon lies in the x-y plane (i.e. has z=0) and its center has model coordinates (0.0,0.0,0.0).
     * The uppermost edge of the base polygon will have length 1.0 and lie horizontally symmetrically above the polygon center,
     * i.e. its end points will have the x coordinates -0.5f and 0.5f and their y coordinates will be identical
     * (their exact values resulting from the form of the polygon which in turn results from its number of edges).
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
        GLShapeCV shape1 = GLShapeFactoryCV.makePyramid("",noPolygonCorners, apexHeight, GLShapeFactoryCV.white, facesColors1);
        GLShapeCV shape2 = GLShapeFactoryCV.makePyramid("",noPolygonCorners, apexHeight, GLShapeFactoryCV.white, facesColors2);
        GLShapeCV shape = GLShapeFactoryCV.joinShapes(id, shape1, shape2,1,1,1,180,0,0,
                                                                         0,-apexHeight,0,0,-apexHeight/2,0);
        return shape;
    }

    /**
     * Make a regular ("right") prism or a cylinder with colored faces.
     * <BR>
     * The base will be a polygon parallel to the x-z plane with y = -height/2.0f as constructed by makePolygon() and trianglesForPolygon()
     * and rotated by 90 degrees around the x axis.
     * The center of the base polygon will have the model coordinates (0.0,-height/2.0f,0.0).
     * the top will be a copy of the polygon translated by 'height' into the positive y direction.
     * The uppermost edges of both polygons will have length 1.0 and lie horizontally symmetrically above the polygon center,
     * i.e. their end points will have the x coordinates -0.5f and 0.5f and their z coordinates will be identical
     * (their exact values resulting from the form of the polygon which in turn results from its number of edges).
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
        float startEdgeY = (float) (0.5/Math.tan(Math.PI/2-(noCorners-2)/(2.0*noCorners)*Math.PI));
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
        return shape;
    }

    /**
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
     */

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
        float startEdgeY = (float) (0.5/Math.tan(Math.PI/2-(noCorners-2)/(2.0*noCorners)*Math.PI));
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
    }

    /**
     * Makes a sphere-like shape by iteratively transforming a double pyramid.
     * The transformation is done by splitting and adjusting the triangles of the pyramid iteratively to approximate a shape.
     * The center of the sphere will have the model coordinates (0.0,0.0,0.0) and its radius will be 1.0 (+/-epsilon).
     * @param id The id of the shape.
     * @param color The color of the shape.
     * @return The new shape.
     */

    public static GLShapeCV makeSphere(String id, float[] color) {
        return makeSphere(id,4,color);
    }

    /**
     * Makes a sphere-like shape by iteratively transforming a double pyramid.
     * The transformation is done by splitting and adjusting the triangles of the pyramid iteratively to approximate a shape.
     * The center of the sphere will have the model coordinates (0.0,0.0,0.0) and its radius will be 1.0 (+/-epsilon).
     * @param id The id of the shape.
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
     * Makes a sphere-like shape by iteratively transforming a double pyramid.
     * The transformation is done by splitting and adjusting the triangles of the pyramid iteratively to approximate a shape.
     * @param id The id of the shape.
     * @param colors The colors of the shape.
     * @return The new shape.
     */

    public static GLShapeCV makeSphere(String id, float[][] colors) {
        return makeSphere(id,4,colors);
    }

    /**
     * Makes a sphere-like shape by iteratively transforming a double pyramid.
     * The transformation is done by splitting and adjusting the triangles of the pyramid iteratively to approximate a shape.
     * @param id The id of the shape.
     * @param iterations The number of iterations to be run (3 or 4 will yield good results with reasonable execution times).
     * @param colors The colors of the shape.
     * @return The new shape.
     */

    public static GLShapeCV makeSphere(String id, int iterations, float[][] colors) {
        // start with an double pyramid
        // which will be iteratively transformed into a sphere
        float[] baseColor = GLShapeFactoryCV.white;  // will not be visible
        int noBaseCorners = 10;  // base corners of the pyramid
        float apexHeight = (float)(4*Math.sin(Math.PI/noBaseCorners));
        GLShapeCV startShape = GLShapeFactoryCV.makeBipyramid("Startshape",noBaseCorners,apexHeight,colors,colors);
        GLTriangleCV[] trianglesArray = startShape.getTriangles();
        ArrayList<GLTriangleCV> triangles = new ArrayList<GLTriangleCV>();
        for (GLTriangleCV triangle : trianglesArray)
            triangles.add(triangle);
        long time1 = (new Date()).getTime();
        long time2;
        // explanation for the following see e.g. https://stackoverflow.com/questions/7687148/drawing-sphere-in-opengl-without-using-glusphere
        // split the triangles iteratively into four triangles each
        for (int i=0; i<iterations; i++) {
            ArrayList<GLTriangleCV> trianglesTmp = new ArrayList<GLTriangleCV>();
            for (GLTriangleCV triangle : triangles) {
                // split each triangle into four smaller triangles
                time2 = (new Date()).getTime();
                // Log.v("DEMO2",">>>>>> "+(time2-time1)+"ms");
                float[][] vertices = triangle.getVertices();
                time2 = (new Date()).getTime();
                // Log.v("DEMO2",">>>>>> "+(time2-time1)+"ms");
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
        time2 = (new Date()).getTime();
        // Log.v("DEMO2",">>> "+iterations+" Iterationen, "+triangles.size()+" Dreiecke, "+(time2-time1)+"ms");
        return shape;
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
     * Makes a shape of three cuboids that mark the x, y, and z axes of the world coordinate system.
     * @return The new shape.
     */

    public static GLShapeCV makeAxes() {
        float small = 0.1f;
        GLShapeCV shapeAxisX = GLShapeFactoryCV.makeCuboid("axis_x",5f,small,small,GLShapeFactoryCV.white);
        GLShapeCV shapeAxisY = GLShapeFactoryCV.makeCuboid("axis_y",small,5f,small,GLShapeFactoryCV.white);
        GLShapeCV shapeAxisZ = GLShapeFactoryCV.makeCuboid("axis_z",small,small,5f,GLShapeFactoryCV.white);
        GLShapeCV axes = joinShapes("axes",shapeAxisX,shapeAxisY,1,1,1,0,0,0,0,0,0);
        return joinShapes("axes",axes,shapeAxisZ,1,1,1,0,0,0,0,0,0);
    }

    /**
     * Joins two shapes, i.e. builds a new shape from the triangles of two existing shapes.
     * <BR>
     * The triangles of the first shape are taken as they are, i.e. copies of them are directly added to the new shape.
     * The triangles of the second shape are transformed, i.e. copies of them are scaled, rotated and translated
     * by modifying their vertex coordinates accordingly and then added to the new shape.
     * Afterwards, all vertex coordinates refer to the local coordinate system ("model coordinate system") of the new shape,
     * which is originally the coordinate system of the first shape.
     * @param name The name of the new shape.
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

    public static GLShapeCV joinShapes(String name, GLShapeCV shape1, GLShapeCV shape2,
                                       float shape2_scaleX, float shape2_scaleY, float shape2_scaleZ,
                                       float shape2_rotAngleX, float shape2_rotAngleY, float shape2_rotAngleZ,
                                       float shape2_transX, float shape2_transY, float shape2_transZ) {
        return joinShapes(name, shape1, shape2, shape2_scaleX, shape2_scaleY, shape2_scaleZ,
                shape2_rotAngleX, shape2_rotAngleY, shape2_rotAngleZ, shape2_transX, shape2_transY, shape2_transZ, 0, 0, 0);
    }

    /**
     * Joins two shapes, i.e. builds a new shape from the triangles of two existing shapes.
     * <BR>
     * The triangles of the first shape are taken as they are, i.e. copies of them are directly added to the new shape.
     * The triangles of the second shape are transformed, i.e. copies of them are scaled, rotated and translated
     * by modifying their vertex coordinates accordingly and then added to the new shape.
     * Afterwards, all vertex coordinates refer to the local coordinate system ("model coordinate system") of the new shape,
     * which is originally the coordinate system of the first shape.
     * Depending on the parameters moveCenter_X/Y/Z, however, the origin of this system, i.e. the point (0,0,0), is translated
     * - see explanation of the method GLShape.moveCenterTo().
     * N.B.: If more than two shapes are combined, i.e. if this method is called multiple times with the same shape as the first
     * and different shapes as the second parameter, moveCenter_X/Y/Z should be zero in all calls except the last one,
     * i.e. the origin should be modified only once and only as the final step.
     * @param name The name of the new shape.
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

    public static GLShapeCV joinShapes(String name, GLShapeCV shape1, GLShapeCV shape2,
                                       float shape2_scaleX, float shape2_scaleY, float shape2_scaleZ,
                                       float shape2_rotAngleX, float shape2_rotAngleY, float shape2_rotAngleZ,
                                       float shape2_transX, float shape2_transY, float shape2_transZ,
                                       float moveCenterTo_X, float moveCenterTo_Y, float moveCenterTo_Z) {
        GLShapeCV newShape = new GLShapeCV(name,shape1.getTriangles());
        GLTriangleCV[] triangles2 = shape2.getTriangles();
        for (GLTriangleCV triangle : triangles2)
            triangle.transform(shape2_scaleX,shape2_scaleY,shape2_scaleZ,shape2_rotAngleX, shape2_rotAngleY, shape2_rotAngleZ,shape2_transX,shape2_transY,shape2_transZ);
        newShape.addTriangles(triangles2);
        newShape.moveCenterTo(moveCenterTo_X,moveCenterTo_Y,moveCenterTo_Z);
        return newShape;
    }

    /**
     * Names for the two triangles of a square.
     * <UL>
     * <LI>SquareUpperLeft
     * <LI>SquareLowerRight
     * </UL>
     */

    public static String squareTriangleNames[] = { "SquareUpperLeft", "SquareLowerRight" };

    /** Make two triangles for a square in the x-y plane (i.e with z=0).
     * The sides of the square will be parallel to the respective axes of the underlying coordinate system.
     * The names of the triangles will be taken from the array 'squareTriangleNames'.
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
        triangles[0] = new GLTriangleCV(squareTriangleNames[0], vertices);
        vertices[0] = new float[] { leftUpperCornerX + sideLength, leftUpperCornerY, 0 };
        vertices[1] = new float[] { leftUpperCornerX, leftUpperCornerY - sideLength, 0 };
        vertices[2] = new float[] { leftUpperCornerX + sideLength, leftUpperCornerY - sideLength, 0 };
        triangles[1] = new GLTriangleCV(squareTriangleNames[1], vertices);
        return triangles;
    }

    /**
     * Make the triangles for a regular polygon in the x-y plane (i.e with z=0).
     * For a detailed explanation see the method with the same name and an additional parameter for the z coordinate.
     */

    public static GLTriangleCV[] trianglesForPolygon(float topCornerX, float topCornerY, float sideLength, int numberOfCorners, float colors[][]) {
        return trianglesForPolygon(topCornerX, topCornerY, sideLength, numberOfCorners, 0, colors);
    }

    /**
     * Make the triangles for a regular polygon in a plane with a specific z coordinate.
     * <BR>
     * The vertex coordinates are calculated from the three parameters 'topCornerX', 'topCornerY', and 'sideLength'
     * as described in the comment for the method polygonVertexCoordinates().
     * </UL>
     * The triangles will be created in clock-wise order from two neighboring vertices of the polygon and the center of the polygon.
     * <BR>
     * The triangles will have the names Triangle00, Triangle01, ..., Triangle10, ... in the order by which their vertices have been calculated (see above).
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

    public static GLTriangleCV[] trianglesForPolygon(float topCornerX, float topCornerY, float sideLength, int numberOfCorners, float z, float colors[][]) {
        if (sideLength <= 0.0) return null;
        if (numberOfCorners < 3) return null;
        if (!isValidColorsArray(colors)) return null;
        GLTriangleCV triangles[] = new GLTriangleCV[numberOfCorners];
        // 1.) calculate the coordinates of the polygon corners
        float corners[][] = polygonVertexCoordinates(topCornerX,topCornerY,sideLength,numberOfCorners,z);
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
            String name;
            if (i<9)
                name = "Triangle0" + i;
             else
                name = "Triangle" + i;
            triangles[i] = new GLTriangleCV(name, vertices,colors[i%colors.length]);
            // Log.v("DEMO","Triangle "+i+": "+vertices[0][0]+","+vertices[0][1]+"  "+vertices[1][0]+","+vertices[1][1]+"  "+vertices[2][0]+","+vertices[2][1]);
        }
        return triangles;
    }

    /**
     * Calculate the vertex coordinates of a regular polygon in a plane with a specific z coordinate.
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

    public static float[][] polygonVertexCoordinates(float topCornerX, float topCornerY, float sideLength, int numberOfCorners, float z) {
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
     * Names for the twelve triangles of a cube.
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

    public static String[] cubeTriangleNames = {
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
     * <LI>n=12: color[0-11] define the colors of the twelve triangles of the cube (in the order as specified by cubeTriangleNames)
     * </UL>
     * In the second dimension, all entries must be valid color definitions (see method isValidColorArray()).
     * @return The triangles for the cube in the order as defined by 'cubeTriangleNames'. Null if the colors parameter is not valid (see above).
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
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // front face (triangle right/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // right face (triangle front/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength,  frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // right face (triangle back/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength};
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // back face (triangle right/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // back face (triangle left/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // left face (triangle back/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // left face (triangle front/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // top face (triangle left/front)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // top face (triangle right/back)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // bottom face (triangle left/front)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // bottom face (triangle right/back)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
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
     * <LI>n=12: color[0-11] define the colors of the twelve triangles of the cube (in the order as specified by cubeTriangleNames)
     * </UL>
     * In the second dimension, all entries must be valid color definitions (see method isValidColorArray()).
     * @return The triangles for the cuboid in the order as defined by 'cubeTriangleNames'. Null if the colors parameter is not valid (see above).
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
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // front face (triangle right/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // right face (triangle front/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength_X,  frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // right face (triangle back/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength_Z};
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // back face (triangle right/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // back face (triangle left/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // left face (triangle back/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // left face (triangle front/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // top face (triangle left/front)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // top face (triangle right/back)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // bottom face (triangle left/front)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        tIndex++;
        // bottom face (triangle right/back)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength_X, frontLeftUpperCorner_Y - edgeLength_Y, frontLeftUpperCorner_Z - edgeLength_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colorsLocal[tIndex]);
        return triangles;
    }

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
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colors);
        tIndex++;
        // front face (triangle right/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        colors[0] = colors[1] = colors[2] = orange;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colors);
        tIndex++;
        // right face (triangle front/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength,  frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        colors[0] = colors[1] = colors[2] = red;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colors);
        tIndex++;
        // right face (triangle back/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength};
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        colors[0] = colors[1] = colors[2] = green;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colors);
        tIndex++;
        // back face (triangle right/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        colors[0] = colors[1] = colors[2] = white;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colors);
        tIndex++;
        // back face (triangle left/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        colors[0] = colors[1] = colors[2] = grey;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colors);
        tIndex++;
        // left face (triangle back/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        colors[0] = colors[1] = colors[2] = darkgrey;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colors);
        tIndex++;
        // left face (triangle front/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        colors[0] = colors[1] = colors[2] = lightgrey;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colors);
        tIndex++;
        // top face (triangle left/front)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        colors[0] = colors[1] = colors[2] = yellow;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colors);
        tIndex++;
        // top face (triangle right/back)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        colors[0] = colors[1] = colors[2] = purple;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colors);
        tIndex++;
        // bottom face (triangle left/front)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        colors[0] = colors[1] = colors[2] = cyan;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colors);
        tIndex++;
        // bottom face (triangle right/back)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        colors[0] = colors[1] = colors[2] = magenta;
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,colors);
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
     * @return The triangles for the cube in the order as defined by 'cubeTriangleNames'. Null if the textures parameter is not valid (see above).
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
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,textures[tIndex/2],uvCoord1);
        tIndex++;
        // front face (triangle right/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,textures[tIndex/2],uvCoord2);
        tIndex++;
        // right face (triangle front/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength,  frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,textures[tIndex/2],uvCoord1);
        tIndex++;
        // right face (triangle back/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength};
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,textures[tIndex/2],uvCoord2);
        tIndex++;
        // back face (triangle left/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,textures[tIndex/2],uvCoord1);
        tIndex++;
        // back face (triangle right/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,textures[tIndex/2],uvCoord2);
        tIndex++;
        // left face (triangle back/top)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,textures[tIndex/2],uvCoord1);
        tIndex++;
        // left face (triangle front/bottom)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,textures[tIndex/2],uvCoord2);
        tIndex++;
        // top face (triangle left/front)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,textures[tIndex/2],uvCoord1);
        tIndex++;
        // top face (triangle right/back)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z - edgeLength };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,textures[tIndex/2],uvCoord2);
        tIndex++;
        // bottom face (triangle left/front)
        vertices[0] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,textures[tIndex/2],uvCoord1);
        tIndex++;
        // bottom face (triangle right/back)
        vertices[0] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z };
        vertices[1] = new float[] { frontLeftUpperCorner_X, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        vertices[2] = new float[] { frontLeftUpperCorner_X + edgeLength, frontLeftUpperCorner_Y - edgeLength, frontLeftUpperCorner_Z - edgeLength };
        triangles[tIndex] = new GLTriangleCV(cubeTriangleNames[tIndex],vertices,textures[tIndex/2],uvCoord2);
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

}
