package com.example.zoo;

import android.os.Handler;

import com.example.zoo.Zoo;

import android.os.Message;

public class ZooHandler extends Handler{
	
	
	
	@Override 
    public void handleMessage(Message msg) {
		if (msg.what == 1) {
			
			
			//Zoo.system_time = System.currentTimeMillis();
			
			Zoo.system_time = Zoo.app_start_time + Zoo.time_update_msec;
			
			Zoo.time_sec += Zoo.time_update_sec;
			
			String eat_text;
			String play_text;
			String bath_text;
			

			Zoo.game_view.setText(Zoo.ConvertSecToTime((Zoo.time_sec)));
			
			eat_text = Zoo.LastTimeToStatus(Zoo.eat_current_time, Zoo.eat_time_step);
			play_text = Zoo.LastTimeToStatus(Zoo.play_current_time, Zoo.play_time_step);
			bath_text = Zoo.LastTimeToStatus(Zoo.bath_current_time, Zoo.bath_time_step);
			
			Zoo.eat_view.setText(eat_text);
			Zoo.play_view.setText(play_text);
			Zoo.bath_view.setText(bath_text);
			
			Zoo.eat_current_time += Zoo.time_update_sec;
			Zoo.play_current_time += Zoo.time_update_sec;
			Zoo.bath_current_time += Zoo.time_update_sec;
			
			//Zoo.play_last_time += Zoo.time_update_msec;
			//Zoo.eat_last_time += Zoo.time_update_msec;
			//Zoo.bath_last_time += Zoo.time_update_msec;
			
			if (Zoo.CheckIfGameIsOver(Zoo.eat_current_time, Zoo.eat_time_step) == 1) {
				
				Zoo.run_thread = false;
			}
			
			if (Zoo.CheckIfGameIsOver(Zoo.play_current_time, Zoo.play_time_step) == 1) {
				
				Zoo.run_thread = false;
			}
			
			if (Zoo.CheckIfGameIsOver(Zoo.bath_current_time, Zoo.bath_time_step) == 1) {
				
				Zoo.run_thread = false;
			}
			
			
			
		}
	}

}
