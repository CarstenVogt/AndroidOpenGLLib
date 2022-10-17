// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 17.3.2022

package de.thkoeln.cvogt.android.opengl_utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 *  Class with some predefined <I>Bitmap</I> objects that can be used for shape texturing.
 *  Currently these predefined bitmaps are available (the names to be used as parameter values for the static <I>get()</I> method that returns these bitmaps):
 *  <UL>
 *  <P><LI>"dice01": Face of a dice showing one point.
 *  <P><LI>"dice02": Face of a dice showing two points.
 *  <P><LI>"dice03": Face of a dice showing three points.
 *  <P><LI>"dice04": Face of a dice showing four points.
 *  <P><LI>"dice05": Face of a dice showing five points.
 *  <P><LI>"dice06": Face of a dice showing six points.
 *  <P><LI>"front":  The text "front".
 *  <P><LI>"back":   The text "back".
 *  <P><LI>"left":   The text "left".
 *  <P><LI>"right":  The text "right".
 *  <P><LI>"top":    The text "top".
 *  <P><LI>"bottom": The text "bottom".
 *  <P><LI>"raster": A two-dimensional raster of hexadecimal numbers.
 *  <P><LI>"logo-thk": The TH Köln logo.
 *  </UL>
 *  <P>
 *  Moreover, the class provides some utility methods.
 *  <P>
 *  N.B.: Before this class can be used, its static <I>init()</I> method must be called to initialize its <I>Context</I>.
 */

public class TextureBitmapsCV {

   /** HashMap mapping names to Bitmap objects that have been created from bmp files.
       The name is the file name without the .bmp extension.
       Available names: see comment for the get() method. */

   private static HashMap<String, Bitmap> textureBitmaps = new HashMap<>();

   /** The context to be used when creating a Bitmap object from a BMP file. */

   private static Context context;

    /** The init() method must be called when the program starts.
        It will initialize the context which is needed afterwards to create the Bitmap objects from the BMP files.
        @param c the context from which this method is called */

    public static void init(Context c) {
       context = c;
    }

    /** The get() method returns a Bitmap object for a name.
        If the Bitmap object has not already been stored in the internal HashMap 'textureBitmaps'
        it is created from a .bmp file and stored in 'textureBitmaps'.
        See top of page for a list of the currently supported names.
        @param name the name of the requested Bitmap object
        @return the corresponding Bitmap object or null if there is none available for 'name' */

    public static Bitmap get(String name) {
       Bitmap bitmap = textureBitmaps.get(name);
       // if bitmap has already been created, return it
       if (bitmap!=null)
           return bitmap;
       // create the bitmap and add it to the HashMap
       switch (name) {
           case "dice01": textureBitmaps.put("dice01", BitmapFactory.decodeResource(context.getResources(), R.raw.dice01)); break;
           case "dice02": textureBitmaps.put("dice02", BitmapFactory.decodeResource(context.getResources(), R.raw.dice02)); break;
           case "dice03": textureBitmaps.put("dice03", BitmapFactory.decodeResource(context.getResources(), R.raw.dice03)); break;
           case "dice04": textureBitmaps.put("dice04", BitmapFactory.decodeResource(context.getResources(), R.raw.dice04)); break;
           case "dice05": textureBitmaps.put("dice05", BitmapFactory.decodeResource(context.getResources(), R.raw.dice05)); break;
           case "dice06": textureBitmaps.put("dice06", BitmapFactory.decodeResource(context.getResources(), R.raw.dice06)); break;
           case "front": textureBitmaps.put("front", BitmapFactory.decodeResource(context.getResources(), R.raw.front)); break;
           case "back": textureBitmaps.put("back", BitmapFactory.decodeResource(context.getResources(), R.raw.back)); break;
           case "left": textureBitmaps.put("left", BitmapFactory.decodeResource(context.getResources(), R.raw.left)); break;
           case "right": textureBitmaps.put("right", BitmapFactory.decodeResource(context.getResources(), R.raw.right)); break;
           case "top": textureBitmaps.put("top", BitmapFactory.decodeResource(context.getResources(), R.raw.top)); break;
           case "bottom": textureBitmaps.put("bottom", BitmapFactory.decodeResource(context.getResources(), R.raw.bottom)); break;
           case "raster": textureBitmaps.put("raster", BitmapFactory.decodeResource(context.getResources(), R.raw.raster)); break;
           case "logo_thk": textureBitmaps.put("logo_thk", BitmapFactory.decodeResource(context.getResources(), R.raw.logo_thk)); break;
       }
       return textureBitmaps.get(name);
    }

    /**
     * Method to create a bitmap showing a string.
     * @param text String to be shown
     * @param textSize Text size (in pixels)
     * @param textColor Color of the text
     * @param backgroundColor Color of the background
     * @return A new Bitmap object showing the string.
     */

    public static Bitmap textToBitmap(String text, int textSize, int textColor, int backgroundColor) {
        // Create Paint object for the text
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        Typeface tf = Typeface.create("arial", Typeface.NORMAL);
        textPaint.setTypeface(tf);
        int textWidth = (int) textPaint.measureText(text);
        // Create Paint object for the background
        Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(backgroundColor);
        // Create bitmap
        Bitmap bitmap = Bitmap.createBitmap(textWidth,textSize, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        c.drawPaint(bgPaint);
        c.drawText(text,0,textSize-textPaint.descent(),textPaint);
        return bitmap;
    }

    /** Method to make pixels of a given bitmap with a specific color transparent.
     * Can e.g. be used to generate a bitmap with a transparent background.
     *
     * @param bitmap The bitmap for which pixels shall be made transparent
     * @param colorToMakeTransparent Color of the pixels to be made transparent
     * @return A new Bitmap object, generated from the give bitmap with transparent pixels.
     */

    public static Bitmap makeBitmapTransparent(Bitmap bitmap, int colorToMakeTransparent) {
        int width =  bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int noPixels = result.getHeight()*result.getWidth();
        int pixels[] = new int[noPixels];
        bitmap.getPixels(pixels,0,result.getWidth(),0,0,result.getWidth(),result.getHeight());
        result.setPixels(pixels,0,width,0,0,width,height);
        for(int i =0;i<noPixels;i++)
            if( pixels[i] == colorToMakeTransparent)
                pixels[i] = Color.alpha(Color.TRANSPARENT);
        result.setPixels(pixels,0,result.getWidth(),0,0,result.getWidth(),result.getHeight());
        return result;
    }

    /** Method that checks if a string contains letters with descents.
     * The string is assumed to contain ony letters and digits.
     * @param s The string to check
     * @return true if the string contains letters with descents, false otherwise
     */

    public static boolean hasDescents(String s) {
        char c;
        for (int i=0;i<s.length();i++) {
            c = s.charAt(i);
            if (c=='g'||c=='j'||c=='p'||c=='q'||c=='y')
                return true;
        }
        return false;
    }

    /** Method that checks if a string contains letters with ascents.
     * The string is assumed to contain ony letters and digits.
     * @param s The string to check
     * @return true if the string contains letters with ascents, false otherwise
     */

    public static boolean hasAscents(String s) {
        char c;
        for (int i=0;i<s.length();i++) {
            c = s.charAt(i);
            if (Character.isUpperCase(c)|| Character.isDigit(c))
                return true;
            if (c>='h'&&c<='l') return true;
            if (c=='b'||c=='d'||c=='f'||c=='t')
                return true;
        }
        return false;
    }

}
