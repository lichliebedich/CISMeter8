package com.example.srcismeterjuly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class MainScreen extends Activity {
	
	Handler handler;
    Runnable runnable;
    long delay_time;
    long time = 800L;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
     setContentView(R.layout.mainscreen);
     handler = new Handler(); 
     
     runnable = new Runnable() { 
         public void run() { 
             Intent intent = new Intent(MainScreen.this,MainActivity.class);
             startActivity(intent);
             finish();
         } 
     };
 }
 
 public void onResume() {
     super.onResume();
     delay_time = time;
     handler.postDelayed(runnable, delay_time);
     time = System.currentTimeMillis();
 }
 
 public void onPause() {
     super.onPause();
     handler.removeCallbacks(runnable);
     time = delay_time - (System.currentTimeMillis() - time);
 }
 
 
}

