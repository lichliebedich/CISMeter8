package com.example.srcismeterjuly;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class RouteMeter extends ActionBarActivity {
	ListView listView;
	DBClass myDB;
	private SQLiteDatabase db;
	private String txRoute,printerName;
	public SQLiteCursor mCursor ;
	private DBClass Strcon = new DBClass(this);
	public String USERID;
	Cursor curSumRoute;
	String tRoute,rCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_route);
		
		myDB = new DBClass(this);
		db = myDB.getWritableDatabase(); 
			
		USERID = getIntent().getStringExtra("userid");
		printerName = getIntent().getStringExtra("printername");
		listView = (ListView) findViewById(R.id.list);
		mCursor= (SQLiteCursor)db.rawQuery("select mtrrdroute,count(*) from CISDB " +
				"group by mtrrdroute", null);
		ArrayList<String> dirArray = new ArrayList<String>();
		mCursor.moveToFirst();
		
		while ( !mCursor.isAfterLast()){
			rCount="0";
			tRoute = mCursor.getString(0);
			curSumRoute = Strcon.select_SumRoute(tRoute);
			if(curSumRoute!=null){
					rCount = curSumRoute.getString(curSumRoute.getColumnIndex("sumRoute"));
			}else{
				rCount="0";
			}
			dirArray.add("สาย : " + mCursor.getString(0) + "  | จำนวน : " + rCount +" / " + mCursor.getString(1)+ "\n");
					
					mCursor.moveToNext();
					
		}
		
		ArrayAdapter<String> adapterDir = new ArrayAdapter<String>(this,R.layout.list_item,dirArray);
		listView.setAdapter(adapterDir);
		myDB.close();
		db.close();
				

                    
        final EditText etRoute = (EditText)findViewById(R.id.et_find);
        etRoute.setText("");
        etRoute.setRawInputType(Configuration.KEYBOARD_12KEY);		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Object item = parent.getItemAtPosition ( position );
				etRoute.setText(item.toString().substring(6, 12));
			}
		});
		
		Button selectbn = (Button)findViewById(R.id.bn_findmeter);
		selectbn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				txRoute = etRoute.getText().toString();
				if (USERID==null){
					getEmpInfo();
				}
				if (txRoute!=""){
					Intent chRoute = new Intent(getApplicationContext(),ReadMeter.class);
		        	chRoute.putExtra("TRoute", txRoute);
		        	chRoute.putExtra("userid", USERID);
		        	chRoute.putExtra("printername",printerName);
		        	//Toast.makeText(RouteMeter.this, printerName,Toast.LENGTH_LONG).show();
		        	startActivity(chRoute);
				}else{
					Toast.makeText(RouteMeter.this,"Not found Route!",
			   			 	Toast.LENGTH_LONG).show(); 
				}			
			}	
		});
		
		    Log.v("Example", "onCreate");
		    getIntent().setAction("Already created");
	}
	
public void getEmpInfo(){
		
		Cursor curEmp;
		curEmp= myDB.select_EmpData();
		
		if(curEmp != null)
		{
			if (curEmp.moveToFirst()) {
				USERID =curEmp.getString(curEmp.getColumnIndex("empid"));
							
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
	    Log.v("Example", "onResume");

	    String action = getIntent().getAction();
	    // Prevent endless loop by adding a unique action, don't restart if action is present
	    if(action == null || !action.equals("Already created")) {
	        Log.v("Example", "Force restart");
	        Intent intent = new Intent(this, RouteMeter.class);
	        startActivity(intent);
	        finish();
	    }
	    // Remove the unique action so the next time onResume is called it will restart
	    else
	        getIntent().setAction(null);

	    super.onResume();
	}
}
