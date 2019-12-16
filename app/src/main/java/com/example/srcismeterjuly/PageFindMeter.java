package com.example.srcismeterjuly;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class PageFindMeter extends Activity {
	TextView etfind;
	Button bnfind;
	DBClass myDB;
	private SQLiteDatabase db;
	ListView listView;
	Cursor mCursor,fCursor;
	String tRoute,tCustcode,tFind;
	RadioButton R0,R1,R2,R3;
	int idfind;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.page_findmeter);
	try {
		myDB = new DBClass(this);
		db = myDB.getWritableDatabase(); 
		idfind = 1;
		etfind = (TextView)findViewById(R.id.et_find);
		R0=(RadioButton)findViewById(R.id.radio0);
		R1=(RadioButton)findViewById(R.id.radio1);
		R2=(RadioButton)findViewById(R.id.radio2);
		R3=(RadioButton)findViewById(R.id.radio3);
		R0.setOnClickListener(radio_listener);
		R1.setOnClickListener(radio_listener);
		R2.setOnClickListener(radio_listener);
		R3.setOnClickListener(radio_listener);
		
		tRoute = getIntent().getStringExtra("tRoute");
		
		bnfind = (Button)findViewById(R.id.bn_findmeter);
		bnfind.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tFind = etfind.getText().toString().trim();
				Intent i  = new Intent();				
				i.putExtra("tFind", tFind);
				i.putExtra("idfind", idfind);
				setResult(-1,i);
				finish();
			}
		});
		
	
			
		
		listView = (ListView) findViewById(R.id.list);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Object item = parent.getItemAtPosition (position);
				etfind.setText(item.toString().substring(0, 11));
			}
		});
		
		mCursor= (SQLiteCursor)db.rawQuery("select mtrrdroute,custcode,meterno,custaddr " +
				"from CISDB where mtrrdroute = '" + tRoute +"'", null);
		ArrayList<String> dirArray = new ArrayList<String>();
		mCursor.moveToFirst();
		
		while ( !mCursor.isAfterLast()){
			
			
			
			dirArray.add(mCursor.getString(1) +" | "  + mCursor.getString(2)+" | "  + mCursor.getString(3).substring(0,15)+ "\n");
					
					mCursor.moveToNext();
					
		}
		
		ArrayAdapter<String> adapterDir = new ArrayAdapter<String>(this,R.layout.list_item2,dirArray);
		listView.setAdapter(adapterDir);
		myDB.close();
		db.close();
		
	} catch (Exception e) {
		// TODO: handle exception
		
	}	
		
		
	}
	 private OnClickListener radio_listener = new OnClickListener() {
	        public void onClick(View v) {
	            switch (v.getId()) {
	            case R.id.radio0:
					Log.d("0", "0");
					idfind=1;
	                break;
				case R.id.radio1:
					Log.d("1", "1");
					idfind=2;
	                break;
				case R.id.radio2:
					Log.d("2", "2");
					idfind=3;
	                break;
				case R.id.radio3:
					Log.d("3", "3");
					idfind=4;
	                break;
				default:
					break;
	            }
	        }
	    };
}
