package com.example.zoo;

import com.example.zoo.Zoo;

import android.os.Message;
import android.util.Log;

public class ZooRunnable implements Runnable {
	
	
	
	@Override
	public void run() {
		
		
					
			if (Zoo.run_thread) {
				long sec = System.nanoTime()/(1000 * 1000 * 1000);
				
				//Log.d(Zoo.TAG, Long.toString(sec));
				
				Message msg = Zoo.handler.obtainMessage();
				msg.what = 1;
				msg.obj = sec;
				Zoo.handler.sendMessage(msg);
				
				Zoo.handler.postDelayed(this, Zoo.time_update_msec);
				
				} else {
					//return;
				}
		} 				
	

}
