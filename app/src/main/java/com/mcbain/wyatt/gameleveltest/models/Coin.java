package com.mcbain.wyatt.gameleveltest.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.mcbain.wyatt.gameleveltest.game.GameMediator;


/**
 * Responsible for the coin sprite and emulating animation
 */
public class Coin implements GameModel, RandomizedModel {
    private Context context;

    private final int COLS = 4;
    private final int ROWS = 2;
    private static final long SWITCH_SPEED = 7;

    private Rect bounds;
    private Bitmap image;
    private int width, height;
    private int currentXFrame, currentYFrame, frameCnt = 0;
    private ModelType type;
    private long lastDrawn, switchThreshold;

    /**
     * Default constructor
     * @param image the image
     */
    public Coin(Bitmap image) {
        this.type = ModelType.COIN;
        this.context = GameMediator.getInstance().getContext();
        this.image = image;
        this.width = image.getWidth() / COLS;
        this.height = image.getHeight() / ROWS;
        this.switchThreshold = 1000 / SWITCH_SPEED;
        this.lastDrawn = 0;
    }

    /**
     * Draws the bitmap on the canvas, in the subset of the current frame
     *
     * @param canvas the canvas to be drawn on
     */
    @Override
    public void render(Canvas canvas) {
        this.update();

        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        paint.setAntiAlias(true);
        paint.setDither(false);
        int translateX = width * currentXFrame;
        int translateY = height * currentYFrame;
        Rect subset = new Rect(translateX, translateY, translateX + width, translateY + height);
        canvas.drawBitmap(image, subset, bounds, paint);
    }

    /**
     * Gets the bounds of the frame
     *
     * @return the bounds
     */
    @Override
    public Rect getBounds() {
        return bounds;
    }

    /**
     * Gets the drawable type
     *
     * @return the type of drawable
     */
    @Override
    public ModelType getType() {
        return type;
    }

    /**
     * Sets the bounds of the drawable by creating a rect from left and top
     *
     * @param x left start
     * @param y top start
     */
    @Override
    public void setBounds(int x, int y) {
        int width = image.getWidth();
        int height = image.getHeight();
        this.bounds = new Rect(x, y, x + width, y + height);
    }

    /**
     * Gets the size of the frame
     * @return
     */
    @Override
    public int getSize() {
        return width;
    }

    /**
     * Updates the current frame position
     */
    private void update() {
        if (lastDrawn == 0 || (System.currentTimeMillis() - lastDrawn) > switchThreshold) {
            currentXFrame = ++frameCnt % COLS;
            currentYFrame = (frameCnt > 3) ? 1 : 0;
            if (frameCnt == 7) frameCnt = 0;
            lastDrawn = System.currentTimeMillis();
        }
    }

}
