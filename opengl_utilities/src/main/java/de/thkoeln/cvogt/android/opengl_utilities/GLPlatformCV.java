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
 * Class with String constants for OpenGL ES programs
 * and corresponding constants and methods.
 */

public class GLPlatformCV {

    // TODO Hierhin alles Plattformnahe, auch Füllen der Puffer etc. (siehe Kommentare in OpenGLShape)

    /** Constant specifying the coloring / texturing type: undefined */
    public static final int COLORING_UNDEF = 0;

    /** Constant specifying the coloring / texturing type: uniform color */
    public static final int COLORING_UNIFORM = 1;

    /** Constant specifying the coloring / texturing type: color gradient = color interpolated from the vertex colors */
    public static final int COLORING_GRADIENT = 2;

    /** Constant specifying the coloring / texturing type: textured */
    public static final int COLORING_TEXTURED = 3;

    // uniformly colored shapes
    // TODO Das funktioniert noch nicht. Daher werden bis auf weiteres auch einfarbige Dreiecke mit dem OpenGL-Code für Farbgradienten gerendert.

    /*
    public static String vertexShaderUniformColor =
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 vPosition;" +   // vPosition liefert dem OpenGL-Rendering die Koordinaten der Eckpunkte des Shapes (Typ vec4 = vierdimensionaler Vektor),
                                        // wird in der Methode draw() mit Werten besetzt
        "void main() {" +
        "  gl_Position = uMVPMatrix * vPosition;" +  // gl_Position ist ein 'defined output', der der Hardware angibt, wo auf dem Display ein Eckpunkt dargestellt werden soll
        "}";

    public static String fragmentShaderUniformColor =
        "precision mediump float;" +
        "uniform vec4 vColor;" +     // vColor liefert dem OpenGL-Rendering die Farbe der Fläche (Typ vec4 = vierdimensionaler Vektor)
                                     // wird in der Methode draw() mit Werten besetzt
        "void main() {" +
        "  gl_FragColor = vColor;" +  // gl_FragColor ist ein 'defined output', der der Hardware die Farbwerte der Eckpunkte angibt
        "}";

     */

    /**
     * OpenGL ES code: vertex shader for uniformly colored shapes and shapes with color gradients.
     */

    public static String vertexShaderColorGradient =
            "attribute vec4 vPosition;" +   // vPosition liefert dem OpenGL-Rendering die Koordinaten der Eckpunkte des Shapes (Typ vec4 = vierdimensionaler Vektor),
                    // wird in der Methode draw() aus den Shape-Daten mit Werten besetzt.
                    "attribute vec4 aColor;" +	    // Farbwerte der Eckpunkte. Wertzuweisung wie bei vPosition in der Methode draw().
                    "varying vec4 vColor;" +		// Speicher zur Weitergabe von Farbwerten an den Fragment Shader
                    "uniform mat4 uMVPMatrix;" +    // MVP-Matrix (Typ mat4 = Matrix mit vier Zeilen und Spalten)
                    "void main() {" +
                    "  vColor = aColor;" +		    // Weitergabe der Farbwerte an den Fragment Shader
                    "  gl_Position = uMVPMatrix * vPosition;" +  // gl_Position ist ein 'defined output', der der Hardware angibt, wo auf dem Display ein Eckpunkt dargestellt werden soll.
                    // Hier: Eckpunkte des Würfels, transformiert durch die MVP-Matrix.
                    "}";

    /**
     * OpenGL ES code: fragment shader for uniformly colored shapes and shapes with color gradients.
     */

    public static String fragmentShaderColorGradient =
            "precision mediump float;" +
                    "varying vec4 vColor;" +        // vColor liefert dem OpenGL-Rendering Informationen zur Färbung des Shapes,
                    // wird in der Methode draw() mit Werten besetzt.
                    "void main() {" +
                    "  gl_FragColor = vColor;" +    // gl_FragColor ist ein 'defined output', der der Hardware Farbinformationen liefert.
                    "}";

    /**
     * OpenGL ES code: vertex shader for textured shapes.
     */

    public static String vertexShaderTextured =    // Vertex Shader: Informationen über die Eckpunkte des Dreiecks
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 aTexCoord;" +
                    "varying vec2 vTexCoord;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
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
        // debug output
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        Log.v("GLDEMO",">>> Compile Status: "+compileStatus[0]+" (0 = failed, 1 = ok)");
        if (compileStatus[0]==0)
            Log.v("GLDEMO",">>> Error: "+GLES20.glGetShaderInfoLog(shader));
        return shader;
    }

}
