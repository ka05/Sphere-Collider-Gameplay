package com.mcbain.wyatt.gameleveltest.models;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.mcbain.wyatt.gameleveltest.game.GameMediator;
import com.mcbain.wyatt.gameleveltest.util.PaintUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/**
 * Inflates the ball when hit by the ball
 */
public class Inflater extends Observable implements GameModel, RandomizedModel {
    private GameMediator mediator;
    private float inflateValue;
    private Rect bounds;
    private Paint circlePaint, textPaint;
    private ModelType type;
    private int circleX, circleY, radius;
    private int size, changeValue = 0, originalSize;
    private int xMax, yMax;
    private float xVelocity, yVelocity, frameTime;
    private String text;
    private List<Observer> observers;
    private boolean increasing = false, decreasing = false;
    private float xPosition, yPosition;

    /**
     * Default constructor
     *
     * @param size
     * @param color
     */
    public Inflater(int size, float inflateValue, String color) {
        this.type = ModelType.INFLATER;
        this.size = size;
        this.originalSize = size;
        this.inflateValue = inflateValue;
        this.observers = new ArrayList<Observer>();
        this.mediator = GameMediator.getInstance();
        this.circlePaint = PaintUtility.configurePaintForCircle(PaintUtility.darkenColor(color, 1.1f));
        this.textPaint = PaintUtility.configurePaintForText(PaintUtility.createComplementaryColor(color), 16f);
        this.initVelocity();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public synchronized void deleteObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer o: observers) {
            o.update(this, null);
        }
    }

    @Override
    public void notifyObservers(Object data) {
        for (Observer o: observers) {
            o.update(this, data);
        }
    }

    /**
     * Renders the inflater on the canvas
     * @param canvas
     */
    @Override
    public void render(Canvas canvas) {
        if (increasing) increaseBounds();
        if (decreasing) decreaseBounds();
        this.updatePositions();
        canvas.drawCircle(circleX, circleY, radius, circlePaint);
    }

    /**
     * Gets the bounds
     *
     * @return
     */
    @Override
    public Rect getBounds() {
        return bounds;
    }

    @Override
    public void setBounds(int x, int y) {
        this.bounds = new Rect(x, y, x + size, y + size);
        this.configureLocation();
    }

    /**
     * Gets the type
     *
     * @return
     */
    @Override
    public ModelType getType() {
        return type;
    }

    /**
     * Gets the size of the object
     * @return
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * Gets the inflater value
     *
     * @return
     */
    public float getInflateValue() {
        return inflateValue;
    }

    /**
     * Configures the location of the ball
     */
    private void configureLocation() {
        xPosition = bounds.left;
        yPosition = bounds.top;
        radius = size / 2;
        circleX = bounds.left + radius;
        circleY = bounds.top + radius;
        xMax = mediator.getXMax() - size;
        yMax = mediator.getYMax() - size;
    }

    private void initVelocity() {
        Random random = new Random();
        int xDir = (random.nextFloat() > 0.5)? 1 : -1;
        int yDir = (random.nextFloat() > 0.5)? 1 : -1;
        xVelocity = random.nextFloat() * xDir;
        yVelocity = random.nextFloat() * yDir;
        frameTime = random.nextFloat() / 2;
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

    private void setMax() {
        xMax = mediator.getXMax() - size;
        yMax = mediator.getYMax() - size;
    }

    private void updatePositions() {
        Random random = new Random();

        //Calc distance travelled in that time
        float xS = ((xVelocity * 20) * frameTime);
        float yS = ((yVelocity * 20) * frameTime);

        //Add to position negative due to sensor
        //readings being opposite to what we want!
        xPosition -= xS;
        yPosition -= yS;

        if (xPosition > xMax) {
            xVelocity *= -1.2;
        }
        else if (xPosition < 0) {
            xVelocity *= -.8;
        }
        if (yPosition > yMax) {
            yVelocity *= -1.2;
        }
        else if (yPosition < 0) {
            yVelocity *= -.8;
        }
        if (xPosition > xMax || xPosition < 0) {
            xS = ((xVelocity) * frameTime);
            xPosition -= xS;
        }
        else if (yPosition > yMax || yPosition < 0) {
            yS = ((yVelocity) * frameTime);
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
        this.setBounds(Math.round(xPosition), Math.round(yPosition));
        this.notifyObservers();
    }
}
