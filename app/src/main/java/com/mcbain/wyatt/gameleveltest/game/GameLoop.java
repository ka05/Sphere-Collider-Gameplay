package com.mcbain.wyatt.gameleveltest.game;

import android.graphics.Canvas;

import com.mcbain.wyatt.gameleveltest.view.GameSurface;


/**
 * Created by wyattmcbain on 4/22/15.
 */
public class GameLoop extends Thread {
    private GameSurface gameView;
    private boolean running = false;

    private static final long FPS = 60;

    public GameLoop(GameSurface gameView) {
        this.gameView = gameView;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        long tickPS = 1000 / FPS;
        long startTime, sleepTime;

        while(running) {
            Canvas c = null;
            startTime = System.currentTimeMillis();
            try {
                c = gameView.getHolder().lockCanvas();
                synchronized (gameView.getHolder()) {
                    gameView.render(c);
                }
            } finally {
                if (c != null) {
                    gameView.getHolder().unlockCanvasAndPost(c);
                }
            }
            sleepTime = tickPS - (System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0) sleep(sleepTime);
                else sleep(10);
            } catch (InterruptedException ie) {}
        }
    }
}
