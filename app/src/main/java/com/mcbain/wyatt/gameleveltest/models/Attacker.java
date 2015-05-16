package com.mcbain.wyatt.gameleveltest.models;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.mcbain.wyatt.gameleveltest.game.GameMediator;

/**
 * Created by wyattmcbain on 5/14/15.
 */
public class Attacker implements GameModel {
    private GameMediator mediator;
    private Bitmap drawingArea;
    private int size, xCenter, yCenter, xStart, yStart;
    private Rect bounds;
    private Paint circlePaint;
    private ModelType type;

    /**
     * Default constructor
     * @param size
     * @param color
     */
    public Attacker(int size, String color) {
        this.type = ModelType.ATTACKER;
        this.size = size;
        this.mediator = GameMediator.getInstance();
        this.xCenter = mediator.getXMax() / 2;
        this.yCenter = mediator.getYMax() / 2;
        this.drawingArea = Bitmap.createBitmap(size, size, null);
        this.configurePaint(color);
        this.configureStartingPoint();
        this.drawCircleOnBitmap();
    }

    /**
     * Draws the ball on the canvas
     *
     * @param canvas
     */
    @Override
    public void render(Canvas canvas) {
        this.configureStartingPoint();
        this.drawCircleOnBitmap();
        canvas.drawBitmap(drawingArea, xStart, yStart, null);
    }

    /**
     * Gets the current counds
     *
     * @return
     */
    @Override
    public Rect getBounds() {
        return bounds;
    }


    /**
     * Gets the model type
     *
     * @return
     */
    @Override
    public ModelType getType() {
        return type;
    }

    /**
     * Increases ther size of the ball
     *
     * @param inc
     */
    public void increaseSize(int inc) {
        this.size += inc;
    }

    /**
     * Decreases the size of the ball
     *
     * @param dec
     */
    public void decreaseSize(int dec) {
        this.size -= dec;
    }

    /**
     * Configures the ball color
     *
     * @param color
     */
    private void configurePaint(String color) {
        circlePaint = new Paint();
        circlePaint.setColor(Color.parseColor(color));
    }

    /**
     * Configures the starting point of the ball
     */
    private void configureStartingPoint() {
        this.xStart = xCenter - (drawingArea.getWidth() / 2);
        this.yStart = yCenter - (drawingArea.getHeight() / 2);
        this.bounds = new Rect(xStart, yStart, xStart + drawingArea.getWidth(), yStart + drawingArea.getHeight());
    }

    /**
     * Draws the circle on the bitmap
     *
     */
    private void drawCircleOnBitmap() {
        int radius = this.size / 2;
        Canvas cv = new Canvas(drawingArea);
        cv.drawCircle(drawingArea.getWidth() / 2, drawingArea.getHeight() / 2, radius, circlePaint);
    }
}
