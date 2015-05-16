package com.mcbain.wyatt.gameleveltest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import com.mcbain.wyatt.gameleveltest.game.GameLoop;
import com.mcbain.wyatt.gameleveltest.game.GameMediator;
import com.mcbain.wyatt.gameleveltest.game.LevelManager;
import com.mcbain.wyatt.gameleveltest.models.GameModel;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Responsible for running the game off the main thread
 */
public class GameSurface extends SurfaceView {
    private SurfaceHolder holder;
    private LevelManager levelManager;
    private ConcurrentLinkedQueue models;
    private GameLoop gameLoop;

    /**
     * Default constructor
     * Creates a holder callback to change the surface
     *
     * @param context
     */
    public GameSurface(Context context) {
        super(context);

        this.gameLoop = new GameLoop(this);
        this.levelManager = new LevelManager();
        this.models = GameMediator.getInstance().getModels();
        this.holder = getHolder();

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoop.setRunning(true);
                gameLoop.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoop.setRunning(false);
                while (retry) {
                    try {
                        gameLoop.join();
                        retry = false;
                    } catch(InterruptedException ie) {}
                }
            }
        });
    }

    /**
     * Draws to the canvas
     * @param canvas
     */
    public void render(Canvas canvas) {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        canvas.drawColor(Color.WHITE);

        Iterator iterator = models.iterator();
        while (iterator.hasNext()) {
            GameModel model = (GameModel) iterator.next();
            model.render(canvas);
        }
    }
}
