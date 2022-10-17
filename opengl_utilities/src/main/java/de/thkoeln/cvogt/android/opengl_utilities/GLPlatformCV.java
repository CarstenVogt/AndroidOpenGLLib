// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 17.3.2022

package de.thkoeln.cvogt.android.opengl_utilities;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Class with <I>String</I> constants for OpenGL ES programs
 * and corresponding constants and methods.
 */

public class GLPlatformCV {

    // TODO Hierhin alles Plattformnahe, auch Füllen der Puffer etc. (siehe Kommentare in OpenGLShape)

    /** Constant specifying the coloring / texturing type: undefined */
    public static final int COLORING_UNDEF = 0;

    /** Constant specifying the coloring / texturing type: uniform color for the whole shape */
    public static final int COLORING_UNIFORM = 1;

    /** Constant specifying the coloring / texturing type: for shapes with triangles of different colors and possibly also with color gradients */
    public static final int COLORING_VARYING = 2;

    /** Constant specifying the coloring / texturing type: textured */
    public static final int COLORING_TEXTURED = 3;

    // uniformly colored shapes
    //
    // TODO Der folgende Code allein funktioniert so nicht. Daher werden bis auf weiteres auch einfarbige Shapes mit dem OpenGL-Code für Farbgradienten gerendert.
    // Zu Lösung die Klasse GLShapeCV entsprechend erweitern. Grundlegende Unterscheidung, die dabei zu treffen ist:
    // 1.) Shapes, die aus mehreren Dreiecken bestehen, von denen jedes "uniformly colored" ist und die alle dieselbe Farbe haben.
    //   > dann könnte dieser Code hier verwendet werden
    //   > das könnte evtl. zu einer Beschleunigung führen (aber sicher ist das nicht)
    //      - Messung (8.9.22 auf Samsung S21) in der App OpenGLAndroid, Klassen ShapeTriangleBasic vs. ShapeTriangleMulticolor vs. ShapeCubeMVPColor:
    //        > drawArrays()-Aufruf bei einem einheitlich gefärbten Dreieck, ohne MVP-Matrix: 20-60 Mikrosekunden
    //        > drawArrays()-Aufruf bei einem Dreieck mit Farbverlauf, ohne MVP-Matrix: 4-8 Milli(!!)sekunden
    //        > ABER(!!) drawArrays()-Aufruf bei Würfel mit 12 Dreiecken mit Farbverläufen, mit MVP-Matrix: 30-60 Mikrosekunden
    //                     [ähnliche Zeitwerte in dieser App OpenGLUtilities für Würfels mit 12 unterschiedlich, aber jeweils einheitlich gefärbten Dreiecken]
    // 2.) Shapes, die aus mehreren Dreiecken bestehen, von denen jedes "uniformly colored" ist, die aber unterschiedliche Farben haben.
    //   > wie sähe entsprechender Code aus??

    public static String vertexShaderUniformColor =
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 aPosition;" +   // aPosition liefert dem OpenGL-Rendering die Koordinaten der Eckpunkte des Shapes (Typ vec4 = vierdimensionaler Vektor),
        "void main() {" +
        "  gl_Position = uMVPMatrix * aPosition;" +  // gl_Position ist ein 'defined output', der der Hardware angibt, wo auf dem Display ein Eckpunkt dargestellt werden soll
        "}";

    public static String fragmentShaderUniformColor =
        "precision mediump float;" +
        "uniform vec4 uColor;" +     // vColor liefert dem OpenGL-Rendering die Farbe der Fläche (Typ vec4 = vierdimensionaler Vektor)
        "void main() {" +
        "  gl_FragColor = uColor;" +  // gl_FragColor ist ein 'defined output', der der Hardware die Farbwerte der Eckpunkte angibt
        "}";

    /**
     * OpenGL ES code: vertex shader for colored shapes (shapes with triangles of different colors and possibly color gradients, currently also for shapes with a uniform color).
     */

    public static String vertexShaderVaryingColor =
            "attribute vec4 aPosition;" +   // aPosition liefert dem OpenGL-Rendering die Koordinaten der Eckpunkte des Shapes (Typ vec4 = vierdimensionaler Vektor),
            "attribute vec4 aColor;" +	    // Farbwerte der Eckpunkte. Wertzuweisung wie bei aPosition in der Methode draw().
            "varying vec4 vColor;" +		// Speicher zur Weitergabe von Farbwerten an den Fragment Shader
            "uniform mat4 uMVPMatrix;" +    // MVP-Matrix (Typ mat4 = Matrix mit vier Zeilen und Spalten)
            "void main() {" +
            "  vColor = aColor;" +		    // Weitergabe der Farbwerte an den Fragment Shader
            "  gl_Position = uMVPMatrix * aPosition;" +  // gl_Position ist ein 'defined output', der der Hardware angibt, wo auf dem Display ein Eckpunkt dargestellt werden soll.
            // Hier: Eckpunkte des Würfels, transformiert durch die MVP-Matrix.
            "}";

    /**
     * OpenGL ES code: fragment shader for colored shapes (shapes with triangles of different colors and possibly color gradients, currently also for shapes with a uniform color).
     */

    public static String fragmentShaderVaryingColor =
            "precision mediump float;" +
                    "varying vec4 vColor;" +        // vColor liefert dem OpenGL-Rendering Informationen zur Färbung des Shapes,
                    "void main() {" +
                    "  gl_FragColor = vColor;" +    // gl_FragColor ist ein 'defined output', der der Hardware Farbinformationen liefert.
                    "}";

    /**
     * OpenGL ES code: vertex shader for textured shapes.
     */

    public static String vertexShaderTextured =    // Vertex Shader: Informationen über die Eckpunkte des Dreiecks
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 aPosition;" +
                    "attribute vec2 aTexCoord;" +
                    "varying vec2 vTexCoord;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * aPosition;" +
                    "  vTexCoord = aTexCoord;" +
                    "}";

    /**
     * OpenGL ES code: fragment shader for textured shapes.
     */

    public static String fragmentShaderTextured =   // Fragment Shader: Abbildung von Pixeln des Textur-Bilds auf die einzelnen "Fragments" = Pixel der Dreiecksfläche
            "precision mediump float;" +
                    "varying vec2 vTexCoord;" +
                    "uniform sampler2D sTexture;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D( sTexture, vTexCoord );" +
                    "}";

    /**
     * Auxiliary method to compile shader code
     */

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        /*
        // debug output
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0]==0)
            Log.v("GLDEMO",">>> Compile error: "+GLES20.glGetShaderInfoLog(shader));
          else
            Log.v("GLDEMO",">>> Compilation sucessful");
        */
        return shader;
    }

}
