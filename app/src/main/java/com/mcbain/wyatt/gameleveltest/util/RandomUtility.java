package com.mcbain.wyatt.gameleveltest.util;

import android.graphics.Rect;

import com.mcbain.wyatt.gameleveltest.game.GameMediator;
import com.mcbain.wyatt.gameleveltest.models.GameModel;
import com.mcbain.wyatt.gameleveltest.models.ModelType;
import com.mcbain.wyatt.gameleveltest.models.RandomizedModel;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by wyattmcbain on 5/16/15.
 */
public class RandomUtility {
    /**
     * Randomizes the starting location for a model
     * Checks to see if it intersects with any other models on the board
     *
     * @param model
     * @return
     */
    public static GameModel randomizeLocation(RandomizedModel model) {
        GameMediator mediator = GameMediator.getInstance();
        ConcurrentLinkedQueue models = mediator.getModels();
        int xMax = mediator.getXMax();
        int yMax = mediator.getYMax();
        Random rn = new Random();
        Rect rect = null;
        int size = model.getSize();
        int xBound = xMax - size;
        int yBound = yMax - size;
        boolean random = false;

        while(!random) {
            Iterator iterator = models.iterator();
            int x = rn.nextInt(xBound);
            int y = rn.nextInt(yBound);
            rect = new Rect(x, y, x + size, y + size);

            while (iterator.hasNext()) {
                GameModel test = (GameModel)iterator.next();
                if (test.getType() == ModelType.BACKGROUND || test.getType() == ModelType.BALL) {
                    random = true;
                    break;
                }
                if (Rect.intersects(rect, test.getBounds())) {
                    random = false;
                    break;
                }
                random = true;
            }
        }
        model.setBounds(rect.left, rect.top);
        return (GameModel)model;
    }
}
