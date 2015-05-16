package com.mcbain.wyatt.gameleveltest.util;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Color utility
 * Credit: http://stackoverflow.com/questions/4928772/android-color-darker
 */
public class PaintUtility {

    /**
     * Darkens a color
     *
     * @param color
     * @param factor
     * @return
     */
    public static String darkenColor(String color, float factor) {
        int oldColor = Color.parseColor(color);
        int a = Color.alpha(oldColor);
        int r = Color.red(oldColor);
        int g = Color.green(oldColor);
        int b = Color.blue(oldColor);

        return String.format("#%06X", 0xFFFFFF & Color.argb(a,
                Math.max((int)(r * factor), 0),
                Math.max((int)(g * factor), 0),
                Math.max((int)(b * factor), 0)));
    }

    /**
     * Lightens a color
     *
     * @param color
     * @param factor
     * @return
     */
    public static String lightenColor(String color, float factor) {
        int oldColor = Color.parseColor(color);
        int a = Color.alpha(oldColor);
        int r = Color.red(oldColor);
        int g = Color.green(oldColor);
        int b = Color.blue(oldColor);

        return String.format("#%06X", 0xFFFFFF & Color.argb(a,
                Math.max((int) (r * (1 - factor)), 0),
                Math.max((int) (g * (1 - factor)), 0),
                Math.max((int) (b * (1 - factor)), 0)));
    }

    /**
     * Creates a complementary color
     * @param color
     * @return
     */
    public static String createComplementaryColor(String color) {
        int oldColor = Color.parseColor(color);
        int r = Color.red(oldColor);
        int g = Color.green(oldColor);
        int b = Color.blue(oldColor);

        return String.format("#%06X", 0xFFFFFF & Color.rgb(
                255 - r,
                255 - g,
                255 - b
        ));
    }

    /**
     * Configures paint given a color string
     *
     * @param color
     * @return
     */
    public static Paint configurePaintForCircle(String color) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor(color));
        return paint;
    }

    /**
     * Configures paint for text
     * @param color
     * @param size
     * @return
     */
    public static Paint configurePaintForText(String color, float size) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor(color));
        paint.setTextSize(size);
        return paint;
    }
}
