package com.mcbain.wyatt.gameleveltest;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.mcbain.wyatt.gameleveltest.game.GameMediator;
import com.mcbain.wyatt.gameleveltest.sensors.SensorHandler;
import com.mcbain.wyatt.gameleveltest.view.GameSurface;


public class GameLevelTest extends Activity {
    private GameSurface gameSurface;
    private SensorHandler sensorHandler;

    /**
     * Executes when the activity is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set to fullscreen and landscape
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        sensorHandler = new SensorHandler(this);
        GameMediator mediator = GameMediator.getInstance();
        mediator.setContext(this);
        mediator.setSensorHandler(sensorHandler);

        gameSurface = new GameSurface(this);
        setContentView(gameSurface);
    }

    /**
     * Activity override
     * Called when the activity resumes
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        sensorHandler.startSensorListener();
    }

    /**
     * Activity override
     * Called when the game stops
     */
    @Override
    protected void onStop()
    {
        // Unregister the listener
        sensorHandler.stopSensorListener();
        super.onStop();
    }

    /**
     * Executes when the options menu is created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_level_test, menu);
        return true;
    }

    /**
     * Eexecutes when the options menu item is selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Activity override
     * Called when the configuration changes
     *
     * @param newConfig the new configuration
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
