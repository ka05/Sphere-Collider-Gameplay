package com.mcbain.wyatt.gameleveltest.game;

import android.graphics.Rect;
import android.os.Handler;

import com.mcbain.wyatt.gameleveltest.models.Background;
import com.mcbain.wyatt.gameleveltest.models.Ball;
import com.mcbain.wyatt.gameleveltest.models.GameModel;
import com.mcbain.wyatt.gameleveltest.models.Inflater;
import com.mcbain.wyatt.gameleveltest.models.ModelType;
import com.mcbain.wyatt.gameleveltest.models.RandomizedModel;
import com.mcbain.wyatt.gameleveltest.util.RandomUtility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Responsible for getting the drawable objects from the database
 * Mapping the current bounds of the different objects on the board
 */
public class LevelManager implements Observer {
    private GameMediator gameMediator;
    private DatabaseEmulator dbEmulator;

    private ConcurrentLinkedQueue models;
    private List<GameModel> interactiveModels;
    private Ball gameBall;
    private Rect ballRect;
    private int xMax, yMax;
    private Handler handler;

    /**
     * Default constructor
     */
    public LevelManager() {
        this.gameMediator = GameMediator.getInstance();
        this.models = new ConcurrentLinkedQueue();
        this.gameMediator.setModels(models);
        this.handler = new Handler();
        this.dbEmulator = new DatabaseEmulator();
        this.dbEmulator.getLevelObjects();
        this.xMax = gameMediator.getXMax();
        this.yMax = gameMediator.getYMax();
        this.mapBounds();
    }

    @Override
    public void update(Observable observable, Object data) {
        ArrayList<GameModel> interactiveCopy = new ArrayList<GameModel>(interactiveModels);
        if (((GameModel)observable).getType() == ModelType.BALL) {
            ballRect = ((Ball)observable).getBounds();
            for (GameModel model : interactiveCopy) {
                if (model == gameBall) continue;
                if (Rect.intersects(ballRect, model.getBounds())) {
                    if (determineBallInteraction(model)) {
                        models.remove(model);
                        interactiveModels.remove(model);
                    }
                }
            }
        } else if (((GameModel)observable).getType() == ModelType.INFLATER) {
            Rect inflaterRect = ((Inflater)observable).getBounds();
            for (GameModel model: interactiveCopy) {
                if (model == observable) continue;
                if (Rect.intersects(inflaterRect, model.getBounds())) {
                    determineInflaterInteraction(((Inflater)observable), model);
                }
            }
        }
    }

    private boolean determineBallInteraction(GameModel model) {
        ModelType type = model.getType();
        switch(type) {
            case INFLATER:
                gameBall.increaseSize(((Inflater)model).getInflateValue());
                createNewInflater();
                return true;
            case COIN:
                return true;
            default:
                return false;
        }
    }

    private void determineInflaterInteraction(Inflater source, GameModel intersect) {
        ModelType type = intersect.getType();
        switch(type) {
            case INFLATER:
                Inflater collision = (Inflater)intersect;
                if (source.getInflateValue() > collision.getInflateValue()) {
                    source.increaseSize(collision.getInflateValue());
                    models.remove(collision);
                    interactiveModels.remove(collision);
                } else {
                    collision.increaseSize(collision.getInflateValue());
                    models.remove(source);
                    interactiveModels.remove(source);
                }
                createNewInflater();
                break;
            default:
                break;
        }
    }

    /**
     * Maps the bounds of the drawables to be searched through later
     */
    private void mapBounds() {
        interactiveModels = new ArrayList<GameModel>();
        Iterator iterator = models.iterator();
        while (iterator.hasNext()) {
            GameModel model = (GameModel)iterator.next();
            if (model.getType() == ModelType.BALL) {
                gameBall = (Ball)model;
                ((Ball)model).addObserver(this);
                gameMediator.getSensorHandler().addObserver((Ball)model);
            }
            else if(model.getType() == ModelType.INFLATER) {
                interactiveModels.add(model);
                ((Inflater)model).addObserver(this);
            }
            else if (model.getType() != ModelType.BACKGROUND) {
                interactiveModels.add(model);
            }
        }
    }

    private void createNewInflater() {
        Random random = new Random();
        float inflateValue = (((float)random.nextInt((6 - 1) + 1) + 1) / 10) + 1;
        int size = Math.round(inflateValue * 40);
        Inflater inflater = new Inflater(size, inflateValue, "#fc8d4d");
        inflater = (Inflater)RandomUtility.randomizeLocation(inflater);
        models.add(inflater);
        interactiveModels.add(inflater);
    }
}
