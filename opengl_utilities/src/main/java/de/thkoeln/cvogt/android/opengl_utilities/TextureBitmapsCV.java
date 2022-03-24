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
 *  Class with Bitmap objects that can be used for texturing.
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
        Available names are: dice01, ..., dice06, raster, logo_thk.
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
