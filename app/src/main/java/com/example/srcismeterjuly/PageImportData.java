package com.example.srcismeterjuly;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;

public class PageImportData extends ActionBarActivity{
	public SQLiteCursor mCursor;
	private DBClass db;
	TextView tvempid,tvempfname,tvemplname,tvwwcode,tvstatus;
	ProgressDialog progressDialog = null; 
	//Thread thread;
	private Handler handler = new Handler();
	private static String DB_PATH = "/data/data/com.example.srcismeterjuly/databases/";
    private static String DB_NAME = "CIS";
	String USERID;
    private SQLiteDatabase myDataBase;
    String Import_PATH = "/CIS/DOWNLOAD/CISData.xml";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_importdata);
		
		db= new DBClass(this);
		tvempid = (TextView)findViewById(R.id.tvempid);
		tvempfname = (TextView)findViewById(R.id.tvempname);
		tvemplname = (TextView)findViewById(R.id.tvempsurname);
		tvwwcode =(TextView)findViewById(R.id.tvempwwcode);
		tvstatus =(TextView)findViewById(R.id.tv_status);
		tvstatus.setText("สถานะ: เตรียมพร้อม");
		Button bnimport =(Button)findViewById(R.id.btnImportData);
		bnimport.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				try {
					
			         File sd = Environment.getExternalStorageDirectory();
			         File data = Environment.getDataDirectory();
			         if (sd.canWrite()) {
				        	Log.d("C","BackupDBbegin2");
				            String currentDBPath = "//data//"+getPackageName()+"//databases//CIS";
				            String backupDBPath = "/CIS/DATABASE/CISDB.sqlite";
				           // String backupDBPath = path+"CIS.sqlite";
				            
				            File currentDB = new File(data, currentDBPath);
				            File backupDB = new File( sd, backupDBPath);
		               
				            if (currentDB.exists()) {
				            	Log.d("C","BackupDBOK");
				                FileChannel src = new FileInputStream(currentDB).getChannel();
				                FileChannel dst = new FileOutputStream(backupDB).getChannel();
				                dst.transferFrom(src, 0, src.size());
				                src.close();
				                dst.close();
				            }
			         }
			         final Calendar c = Calendar.getInstance();
					    Time time = new Time();
					    time.setToNow();

					    
					    
					      int day = c.get(Calendar.DATE);
					      int month = c.get(Calendar.MONTH);  //0 = JAN / 11 = DEC
					      int year = c.get(Calendar.YEAR);
				
					      month=month+1; 
				
					      String MONTH$;
					      if (month<=9)  { MONTH$ = "0"+month   ;}
					      else {MONTH$ = ""+month;               }    //Set month to MM


					 String YYYYDDMM = ""+day+MONTH$+year; 
				     String FileDate = YYYYDDMM + "-" + time.format("%H%M");

				     
			         String path = sd + "/CIS/BACKUP/" + "CISData-"+FileDate+".xml";
			         String pathIMG = sd + "/CIS/BACKUP/" + "CISImage-"+FileDate+".xml";
			     	 Log.d("D","STARTxml");
			         DatabaseDump databaseDump = new DatabaseDump(db.getReadableDatabase(), path);
			         databaseDump.exportData();
			         DatabaseIMGDump databaseIMGDump = new DatabaseIMGDump(db.getReadableDatabase(), pathIMG);
			         databaseIMGDump.exportData();
			     	 Log.d("E","Endxml");
			     	 
					 File ExStroage = Environment.getExternalStorageDirectory();
						
					 Import_PATH = ExStroage + Import_PATH;
					if (!isFileExsist(Import_PATH)) 
					{
							Log.e("blubb","File not exists");
							tvstatus.setText("สถานะ: ไม่พบไฟล์เส้นทาง CISData.xml");
					}else{
						
						importDB();
					
					}
									
					getEmpInfo();
				} catch (Exception e) {
					// TODO: handle exception
					return;
				}
			}
		});
				
		getEmpInfo();
		
	}

	public Boolean isFileExsist(String filepath) {
			
		try {
			File file = new File(filepath);
	        return file.exists();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		
	}
	
	public void importDB(){
		Toast.makeText(this, "กำลังนำเข้าเส้นทางอ่านมาตร .. กรุณารอสักครู่ ..", Toast.LENGTH_SHORT).show();
		tvstatus.setText("สถานะ: กำลังนำเข้าเส้นทางอ่านมาตร...");
		progressDialog = ProgressDialog.show(this, "","กำลังนำเข้าเส้นทางอ่านมาตร...", true);
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
					try {
				        //Open the database
						Thread.sleep(5000);
						handler.post(new Runnable() {
							public void run() {
							// Handler thread
								String myPath = DB_PATH + DB_NAME;
						        myDataBase = SQLiteDatabase.openDatabase(myPath, null,
										SQLiteDatabase.OPEN_READWRITE);
						        Log.d("ex", "1");
						      
						        db.onUpgrade(myDataBase, 1, 2);
						    	tvstatus.setText("สถานะ : นำเข้าเส้นทางเรียบร้อยแล้ว");
								showNotificationUP();
						        Log.d("ex", "2");
						        

							}
							});
						  	
					       
					} catch (Exception e) {
						// TODO: handle exception
						tvstatus.setText("ʶสถานะ : ไม่สามารถนำเข้าเส้นทางได้");
						e.printStackTrace();

					}
					progressDialog.dismiss();
			}
		};
		new Thread(runnable).start();
		 
		
	}
	

	public void getEmpInfo(){
		
		Cursor curEmp;
		curEmp= db.select_EmpData();
		
		if(curEmp != null)
		{
			if (curEmp.moveToFirst()) {
				tvempid.setText(curEmp.getString(curEmp.getColumnIndex("empid")));   
				tvempfname.setText(curEmp.getString(curEmp.getColumnIndex("empname")));   
				tvemplname.setText(curEmp.getString(curEmp.getColumnIndex("emplastname")));   
				tvwwcode.setText(curEmp.getString(curEmp.getColumnIndex("wwcode")));
			}
		}
	}
	private void showNotificationUP(){
		 
		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
		r.play();
    }
}
