package com.mcbain.wyatt.gameleveltest.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;


import com.mcbain.wyatt.gameleveltest.R;
import com.mcbain.wyatt.gameleveltest.models.Background;
import com.mcbain.wyatt.gameleveltest.models.Ball;
import com.mcbain.wyatt.gameleveltest.models.Coin;
import com.mcbain.wyatt.gameleveltest.models.GameModel;
import com.mcbain.wyatt.gameleveltest.models.Inflater;
import com.mcbain.wyatt.gameleveltest.models.ModelType;
import com.mcbain.wyatt.gameleveltest.models.RandomizedModel;
import com.mcbain.wyatt.gameleveltest.util.RandomUtility;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Stub class to emulate getting objects from the database
 */
public class DatabaseEmulator {
    private Context context;
    private GameMediator gameMediator;
    private ConcurrentLinkedQueue models;
    private int xMax, yMax;

    /**
     * Default constructor
     *
     */
    public DatabaseEmulator() {
        gameMediator = GameMediator.getInstance();
        this.models = gameMediator.getModels();
        this.context = gameMediator.getContext();
        this.xMax = gameMediator.getXMax();
        this.yMax = gameMediator.getYMax();
    }

    public void getLevelObjects() {
        Random random = new Random();
        int numberOfIncreasers = 3;
        int numbeOfDecreasers = 5;
        int numberOfAttackers = 2;
        models.add(new Background(getBitmap(R.drawable.level1)));

//        while (numberOfStartingCoins != 0) {
//            Coin coin = new Coin(getBitmap(R.drawable.coin));
//            randomizeLocation(coin);
//            models.add(coin);
//            numberOfStartingCoins--;
//        }

        while (numberOfIncreasers != 0) {
            float inflateValue = (((float)random.nextInt((6 - 1) + 1) + 1) / 10) + 1;
            int size = Math.round(inflateValue * 40);
            Inflater inflater = new Inflater(size, inflateValue, "#fc8d4d");
            inflater = (Inflater)RandomUtility.randomizeLocation(inflater);
            models.add(inflater);
            numberOfIncreasers--;
        }
        models.add(new Ball(50, "#fc8d4d"));
    }

    private int getXFromPercent(float percent) {
        return Math.round(percent * xMax);
    }

    private int getYFromPercent(float percent) {
        return Math.round(percent * yMax);
    }

    private Bitmap getBitmap(int id) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap image = BitmapFactory.decodeResource(context.getResources(), id, options);
        return Bitmap.createBitmap(image);
    }

}
