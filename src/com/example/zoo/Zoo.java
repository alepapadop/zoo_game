package com.example.zoo;

import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

@SuppressLint("NewApi")
public class Zoo extends Activity {
	
	public static String TAG = "zoo_debug";
	public static Handler handler = new ZooHandler();
	
	
	public static TextView game_view;
	public static TextView play_view;
	public static TextView eat_view;
	public static TextView bath_view;
	
	public static long eat_time_step = 30;
	public static long play_time_step = 30;
	public static long bath_time_step = 30;
	
	public static long game_last_time;
	public static long eat_last_time;
	public static long play_last_time;
	public static long bath_last_time;	
	
	public static long eat_current_time;
	public static long play_current_time;
	public static long bath_current_time;
	
	public static String happy = "Happy";
	public static String normal = "Normal";
	public static String angry = "Angry";
	public static String game_over = "Game Over";
	
	public static final String PREFS_NAME = "MyPrefsFile";
	
	public static long app_start_time = 0;
	
	public static int time_update_sec = 1;
	public static int time_update_msec = 1000;
	
	public static long time_sec = 0;

	public static volatile boolean run_thread = true;
	
	public static  ZooRunnable zoo_runnable;
	
	public static long system_time = 0;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zoo);
		
		run_thread = true;
		zoo_runnable = new ZooRunnable();
		
		app_start_time = System.currentTimeMillis();
				
		game_view = (TextView) findViewById(R.id.game);
		eat_view = (TextView) findViewById(R.id.eat);
		play_view = (TextView) findViewById(R.id.play);
		bath_view = (TextView) findViewById(R.id.bath);				
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    eat_last_time = settings.getLong("eat_last_time", app_start_time);
	    play_last_time = settings.getLong("play_last_time", app_start_time);
	    bath_last_time = settings.getLong("bath_last_time", app_start_time);
	    game_last_time = settings.getLong("game_last_time", app_start_time);
	    time_sec = settings.getLong("time_sec", 0);
	    
	    
	    eat_current_time = ((app_start_time - eat_last_time)/1000) + time_sec;
	    play_current_time = ((app_start_time - play_last_time)/1000) + time_sec;
	    bath_current_time = ((app_start_time - bath_last_time)/1000) + time_sec;
	    time_sec = time_sec + ((app_start_time - game_last_time) / 1000);
	    	    
	    //Log.d(Zoo.TAG, "eat_current read val: " + Long.toString(eat_current_time));
	    //Log.d(Zoo.TAG, "eat_last read val: " + Long.toString(eat_last_time));
	    //Log.d(Zoo.TAG, "app_start read val: " + Long.toString(app_start_time));
	    //Log.d(Zoo.TAG, "time_sec read val: " + Long.toString(time_sec));
		
		startZooRunnable();
		
	}
	
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
				
	    savedInstanceState.putLong("time_sec", time_sec);
	    savedInstanceState.putLong("bath_current_time", bath_current_time);
	    savedInstanceState.putLong("play_current_time", play_current_time);
	    savedInstanceState.putLong("eat_current_time", eat_current_time);
	    savedInstanceState.putLong("bath_last_time", bath_last_time);
	    savedInstanceState.putLong("play_last_time", play_last_time);
	    savedInstanceState.putLong("eat_last_time", eat_last_time);
	    savedInstanceState.putLong("time_sec", time_sec);
	    	    	   
	    super.onSaveInstanceState(savedInstanceState);
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	   
	    super.onRestoreInstanceState(savedInstanceState);
	   
	    time_sec = savedInstanceState.getLong("time_sec");
	    bath_current_time = savedInstanceState.getLong("bath_current_time");
	    play_current_time = savedInstanceState.getLong("play_current_time");
	    eat_current_time = savedInstanceState.getLong("eat_current_time");
	    bath_last_time = savedInstanceState.getLong("bath_last_time");
	    eat_last_time = savedInstanceState.getLong("eat_last_time");
	    play_last_time = savedInstanceState.getLong("play_last_time");
	    	    
	}
	
	
	@Override
	protected void onStop(){
	      super.onStop(); 	       	   
	      
	      SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putLong("eat_last_time", eat_last_time);
	      editor.putLong("play_last_time", play_last_time);
	      editor.putLong("bath_last_time", bath_last_time);
	      editor.putLong("game_last_time", game_last_time);
	      editor.putLong("time_sec", time_sec);
	      
	      editor.commit();
	      
	      run_thread = false;
	   
	}
	
	private void startZooRunnable() {
		
		handler.postDelayed(zoo_runnable,100);	
	}
	
	public void callbackBath(View view) {
		
		bath_current_time = 0;
		bath_last_time = system_time;
	
	}	

	public void callbackEat(View view) {
		
		eat_current_time = 0;
		eat_last_time = system_time;
		
	}
	
	public void callbackPlay(View view) {
		
		play_current_time = 0;
		play_last_time = system_time;

	}
	
	public void callbackRestart(View view) {
		
		callbackBath(null);
		callbackEat(null);
		callbackPlay(null);
		
		game_last_time = system_time;
		time_sec = 0;
		if (run_thread == false) {
			run_thread = true;
			zoo_runnable = new ZooRunnable();
			startZooRunnable();
		}
		
				
	}
	
	public static String LastTimeToStatus(long current_time, long time_step) {
		
		String ret_val = game_over;
		
		if (current_time < time_step) {
			ret_val = happy;
		} else if (current_time >= time_step && current_time < 2 * time_step) {
			ret_val = normal;
		} else if (current_time >= 2 * time_step && current_time < 3 * time_step) {
			ret_val = angry;
		} else if (current_time > 3 * time_step) {
			ret_val = game_over;
		}
		
		return ret_val;
		
	}
	
	public static int CheckIfGameIsOver(long current_time, long time_step) {
		
		if (current_time > 3 * time_step) {
			return 1;
		}
		
		return 0;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.zoo, menu);
		return true;
	}
	
	public static void printLastTimeInfo() {
		Log.d(Zoo.TAG, "------------LAST--------------");
		Log.d(Zoo.TAG, Long.toString(eat_last_time));
		Log.d(Zoo.TAG, Long.toString(play_last_time));
		Log.d(Zoo.TAG, Long.toString(bath_last_time));
	}
	
	public static void printCurrentTimeInfo() {
		Log.d(Zoo.TAG, "-------------CURRENT-------------");
		Log.d(Zoo.TAG, Long.toString(eat_current_time));
		Log.d(Zoo.TAG, Long.toString(play_current_time));
		Log.d(Zoo.TAG, Long.toString(bath_current_time));
	}
	
	@SuppressLint("NewApi")
	public static String ConvertSecToTime(long sec) {

		long millis = sec * 1000L;
		long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        sb.append(days);
        sb.append(" Days ");
        sb.append(hours);
        sb.append(" Hours ");
        sb.append(minutes);
        sb.append(" Minutes ");
        sb.append(seconds);
        sb.append(" Seconds");

        return(sb.toString());
	}
	
}
