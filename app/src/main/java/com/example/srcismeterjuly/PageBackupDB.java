package com.example.srcismeterjuly;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;

public class PageBackupDB extends Activity {
	ProgressDialog dialog;
	DBClass myDB;
	private SQLiteDatabase db;
	TextView tvStatus;
	String USERID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_backupdata);
		
		myDB = new DBClass(this);
		db = myDB.getWritableDatabase(); 
		getEmpInfo();
			
		tvStatus = (TextView)findViewById(R.id.tvstatus);
		tvStatus.setText("สถานะ : เตรียมพร้อม");
		Button btnBackup = (Button)findViewById(R.id.btnbackupdb);
		btnBackup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String title = "กำลังสำรองข้อมูล";
				String msg = "กรุณารอสักครู่...";
				dialog = ProgressDialog.show(PageBackupDB.this, title, msg);

				Thread thread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
							Log.d("C","BackupDB");
						 	 try {
						 		   Thread.sleep(5000);
							 		Check_and_Create_folder();
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

								     
							         String path = sd + "/CIS/BACKUP/" + "CISData"+USERID+"-"+FileDate+".xml";
							         String pathIMG = sd + "/CIS/BACKUP/" + "CISImage"+USERID+"-"+FileDate+".xml";
							     	 Log.d("D","STARTxml");
							         DatabaseDump databaseDump = new DatabaseDump(myDB.getReadableDatabase(), path);
							         databaseDump.exportData();
							         DatabaseIMGDump databaseIMGDump = new DatabaseIMGDump(myDB.getReadableDatabase(), pathIMG);
							         databaseIMGDump.exportData();
							     	Log.d("E","Endxml");
							    	
						    } catch (Exception e) {
						        e.printStackTrace();
						    }
					
						dialog.dismiss();
						}
				});
			
				thread.start();
				tvStatus.setText("สถานะ : สำรองข้อมูลเรียบร้อยแล้ว");
			}
		});
	}
	public void  Check_and_Create_folder() {	// เช็กว่ามี folder ที่ต้องการสร้างหรือไม่ ถ้าไม่มีให้สร้าง
		 File sd = Environment.getExternalStorageDirectory();
		 boolean P_Directory;
		 String p_root = sd + "/CIS";
		 String p_backup = sd + "/CIS/BACKUP";
		 String p_database = sd + "/CIS/DATABASE";
		 
		 File P_Path = new File(p_root);
		 if (!P_Path.exists()) {
			P_Directory = P_Path.mkdir();	
			if (P_Directory){
				Log.d("","Path");
			}
			}
		 File P_PathBackup = new File(p_backup);
		 if (!P_PathBackup.exists()) {
				P_Directory = P_PathBackup.mkdir();	
				if (P_Directory){
					Log.d("","BackupPath");
				}
			}
		 File P_PathDatabase = new File(p_database);
		 if (!P_PathDatabase.exists()) {
				P_Directory =  P_PathDatabase.mkdir();
				if (P_Directory){
					Log.d("","DatabasePath");
				}		
		 }

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
}
