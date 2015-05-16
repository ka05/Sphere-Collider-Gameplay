package com.mcbain.wyatt.gameleveltest.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.mcbain.wyatt.gameleveltest.game.GameMediator;
import com.mcbain.wyatt.gameleveltest.sensors.SensorHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Background model for the level
 * Updates when changes are made to the sensor
 * Notifies Observers when a change is made to the location
 */
public class Background implements GameModel {
    private GameMediator mediator;
    private Context context;

    private Rect viewBounds;
    private ModelType type;
    private List<Observer> observers;
    private List<BackgroundFrame> frames;

    private int xMax, yMax;

    /**
     * Default constructor
     *
     * @param image
     */
    public Background(Bitmap image) {
        this.type = ModelType.BACKGROUND;
        this.mediator = GameMediator.getInstance();
        this.context = mediator.getContext();
        this.xMax = mediator.getXMax();
        this.yMax = mediator.getYMax();
        this.viewBounds = new Rect(0, 0, xMax, yMax);
        this.observers = new ArrayList<Observer>();
        this.frames = new ArrayList<BackgroundFrame>();
        this.initializeFrames(image);
    }

    /**
     * Renders the frame on the canvas
     *
     * @param canvas
     */
    @Override
    public void render(Canvas canvas) {
        for (BackgroundFrame frame: frames) {
            Bitmap image = frame.getImage();
            Rect rect = frame.getBounds();
            canvas.drawBitmap(image, rect.left, rect.top, null);
        }
    }

    /**
     * Gets the bounds of the model
     *
     * @return
     */
    @Override
    public Rect getBounds() {
        return null;
    }


    /**
     * Gets the type of the model
     *
     * @return
     */
    @Override
    public ModelType getType() {
        return type;
    }


    /**
     * Initializes the frames into an array list
     * @param image
     */
    private void initializeFrames(Bitmap image) {
        BackgroundFrame frame;
        int x = 0, y = 0;

        while (x <= xMax) {
            while (y <= yMax) {
                frame = new BackgroundFrame(image, x, y);
                frames.add(frame);
                y += image.getHeight();
            }
            y = 0;
            x += image.getWidth();
        }
    }

    /**
     * Inner model for each frame
     */
    private class BackgroundFrame {
        private Rect bounds;
        private Bitmap image;

        /**
         * Default constructor
         * @param startX
         * @param startY
         */
        public BackgroundFrame(Bitmap image, int startX, int startY) {
            this.image = image;
            this.bounds = new Rect(startX, startY, startX + image.getWidth(), startY + image.getHeight());
        }

        /**
         * Gets the bounds
         * @return
         */
        public Rect getBounds() {
            return bounds;
        }

        /**
         * Sets the bounds
         * @param bounds
         */
        public void setBounds(Rect bounds) {
            this.bounds = bounds;
        }

        /**
         * Gets the image
         * @return
         */
        public Bitmap getImage() {
            return image;
        }

        /**
         * Sets the image
         * @param image
         */
        public void setImage(Bitmap image) {
            this.image = image;
        }
    }
}
