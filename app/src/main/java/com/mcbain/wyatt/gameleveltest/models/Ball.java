package com.mcbain.wyatt.gameleveltest.models;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.mcbain.wyatt.gameleveltest.game.GameMediator;
import com.mcbain.wyatt.gameleveltest.sensors.SensorHandler;
import com.mcbain.wyatt.gameleveltest.util.PaintUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


/**
 * Ball that will appear in the center of the screen
 */
public class Ball extends Observable implements GameModel, Observer {
    private GameMediator mediator;
    private int size, originalSize;
    private Rect bounds;
    private Paint circlePaint;
    private ModelType type;
    private List<Observer> observers;

    private float xPosition, pitch , xVelocity = 0.0f;
    private float yPosition, roll , yVelocity = 0.0f;
    private int changeValue = 0;
    private int xMax, yMax;
    private boolean increasing = false, decreasing = false;

    private final float FRAME_TIME = 0.45f;

    /**
     * Default constructor
     * @param size
     * @param color
     */
    public Ball(int size, String color) {
        this.type = ModelType.BALL;
        this.size = size;
        this.originalSize = size;
        this.mediator = GameMediator.getInstance();
        this.observers = new ArrayList<Observer>();
        this.circlePaint = PaintUtility.configurePaintForCircle(color);
        this.setMax();
        this.initializeStartingPoint();
        this.setBounds(Math.round(xPosition), Math.round(yPosition));
    }

    @Override
    public void update(Observable observable, Object data) {
        this.pitch = ((SensorHandler)observable).getPitch();
        this.roll = ((SensorHandler)observable).getRoll();
        if (increasing) increaseBounds();
        if (decreasing) decreaseBounds();
        this.updatePositions();
    }

    /**
     * Adds and observer to the observers
     *
     * @param observer
     */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Deletes an observer from the observers
     * @param observer
     */
    @Override
    public synchronized void deleteObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies observers with no data
     *
     */
    @Override
    public void notifyObservers() {
        for (Observer o: observers) {
            o.update(this, null);
        }
    }

    /**
     * Notifies observers with data
     *
     * @param data
     */
    @Override
    public void notifyObservers(Object data) {
        for (Observer o: observers) {
            o.update(this, data);
        }
    }

    /**
     * Draws the ball on the canvas
     *
     * @param canvas
     */
    @Override
    public void render(Canvas canvas) {
        this.setBounds(Math.round(xPosition), Math.round(yPosition));
        int radius = size / 2;
        int centerX = bounds.left + radius;
        int centerY = bounds.top + radius;
        canvas.drawCircle(centerX, centerY, radius, circlePaint);
        this.notifyObservers();
    }

    /**
     * Updates the starting location of the view port
     */
    private void updatePositions() {
        //Calculate new speed
        xVelocity += (pitch * FRAME_TIME);
        yVelocity += (roll * FRAME_TIME);

        //Calc distance travelled in that time
        float xS = ((xVelocity / 2) * FRAME_TIME);
        float yS = ((yVelocity / 2) * FRAME_TIME);

        //Add to position negative due to sensor
        //readings being opposite to what we want!
        xPosition -= xS;
        yPosition -= yS;

        if (xPosition > xMax || xPosition < 0) {
            xVelocity *= -0.5;
            xS = ((xVelocity / 2) * FRAME_TIME);
            yS = ((yVelocity / 2) * FRAME_TIME);
            xPosition -= xS;
            yPosition -= yS;
        }
        else if (yPosition > yMax || yPosition < 0) {
            yVelocity *= -0.5;
            xS = ((xVelocity / 2) * FRAME_TIME);
            yS = ((yVelocity / 2) * FRAME_TIME);
            xPosition -= xS;
            yPosition -= yS;
        }
        if (xPosition > xMax) {
            xPosition = xMax;
        } else if (xPosition < 0) {
            xPosition = 0;
        }
        if (yPosition > yMax) {
            yPosition = yMax;
        } else if (yPosition < 0) {
            yPosition = 0;
        }
    }

    /**
     * Gets the current bounds
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
     * Increases the size of the ball
     *
     */
    public void increaseSize(float inflateValue) {
        changeValue = Math.round(size * inflateValue) - size;
        increasing = true;
    }

    /**
     * Decreases the size of the ball
     *
     * @param dec
     */
    public void decreaseSize(float deflateValue) {
        changeValue = size - Math.round(size * deflateValue);
        decreasing = true;
    }

    /**
     * Initializes the starting point in the center of the screen
     */
    private void initializeStartingPoint() {
        int centerX = xMax / 2;
        int centerY = yMax / 2;
        xPosition = centerX - (size / 2);
        yPosition = centerY - (size / 2);
    }

    /**
     * Sets the bounds fo the ball
     * @param x
     * @param y
     */
    private void setBounds(int x, int y) {
        bounds = new Rect(x, y, x + size, y + size);
    }

    private void setMax() {
        xMax = mediator.getXMax() - size;
        yMax = mediator.getYMax() - size;
    }

    private void increaseBounds() {
        size++;
        changeValue--;
        if (changeValue == 0) increasing = false;
        setMax();
        this.setBounds(Math.round(xPosition), Math.round(yPosition));
    }

    private void decreaseBounds() {
        if (size-- < originalSize) {
            size = originalSize;
            decreasing = false;
        }
        changeValue--;
        if (changeValue == 0) decreasing = false;
        setMax();
        this.setBounds(Math.round(xPosition), Math.round(yPosition));
    }
}
