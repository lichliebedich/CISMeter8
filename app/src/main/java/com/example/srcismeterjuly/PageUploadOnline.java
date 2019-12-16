package com.example.srcismeterjuly;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;

import it.sauronsoftware.ftp4j.FTPClient;

public class PageUploadOnline extends ActionBarActivity {
	DBClass myDB;
	private SQLiteDatabase db;
	Button btnupload;
	TextView txtstat;
	ProgressDialog dialog;

	// String SRFTPHOST  = "110.170.30.38"; ลิงค์เดิมเปลี่ยนวันที่ 18/12/2560
	String SRFTPHOST  = "203.151.43.168";
	String SRFTPUSER  = "ftpadmin";
	String SRFTPPASS  = "siamrajadmin";
    
	String USERID,WWCODE;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_uploadonline);
		
		myDB = new DBClass(this);
		db = myDB.getWritableDatabase(); 
		getEmpInfo();
		
		txtstat = (TextView)findViewById(R.id.tvstatus);		
		btnupload = (Button)findViewById(R.id.btnupload);
		btnupload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String title = "กำลังอัพโหลดข้อมูล";
				String msg = "กรุณารอสักครู่...";
				dialog = ProgressDialog.show(PageUploadOnline.this, title, msg);
				
				Thread thread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							//ใส่โค้ดทำงานตรงนี้
							Thread.sleep(5000);
					 		
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
						
							      month=month+1; //Set int to correct month numeral, i.e 0 = JAN therefore set to 1.
						
							      String MONTH$;
							      if (month<=9)  { MONTH$ = "0"+month   ;}
							      else {MONTH$ = ""+month;               }    //Set month to MM


							 String YYYYDDMM = ""+day+MONTH$+year;    //Put date ints into string DD/MM/YYYY
						     String FileDate = YYYYDDMM + "-" + time.format("%H%M");

						     
					         String UPDataPath = sd + "/CIS/UPLOAD/" + "CISData"+USERID+"-"+FileDate+".xml";
					         String UPImgPath = sd + "/CIS/UPLOAD/" + "CISImage"+USERID+"-"+FileDate+".xml";
					     	 Log.d("D","STARTxml");
					         DatabaseDump databaseDump = new DatabaseDump(myDB.getReadableDatabase(), UPDataPath);
					         databaseDump.exportData();
					         DatabaseIMGDump databaseIMGDump = new DatabaseIMGDump(myDB.getReadableDatabase(), UPImgPath);
					         databaseIMGDump.exportData();
					     	Log.d("E","Endxml");
					     	String FileUpPath ="/CIS/UPLOAD/" + "CISData"+USERID+"-"+FileDate+".xml";
					     	String FileImgPath = "/CIS/UPLOAD/" + "CISImage"+USERID+"-"+FileDate+".xml";
							File uploadFile = new File(Environment.getExternalStorageDirectory(),FileUpPath);		
							File uploadImg = new File(Environment.getExternalStorageDirectory(),FileImgPath);		
							Log.d("ftp","start");
							uploadfile(uploadFile);
							uploadfile(uploadImg);
							txtstat.setText("สถานะ :อัพโหลดข้อมูลเรียบร้อยแล้ว");
							
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							txtstat.setText("สถานะ :อัพโหลดข้อมูลไม่สำเร็จ");

						}
						dialog.dismiss();
					}
				});
				thread.start();
				
				Log.d("ftp","finish");
			}
		});
	}
	
	private void uploadfile(File upFile) {
		// TODO Auto-generated method stub
	
		FTPClient ftpclient = new FTPClient();
		

		String remoteFile = "/CIS/HHTOPC/"+ WWCODE+"/"+USERID;
		Log.d("FTP", remoteFile);  
		try {
			ftpclient.connect(SRFTPHOST,21);
			ftpclient.login(SRFTPUSER, SRFTPPASS);
			ftpclient.setType(FTPClient.TYPE_BINARY);

		    //ftpclient.createDirectory(remoteFile);
            ftpclient.changeDirectory(remoteFile);
            ftpclient.upload(upFile);
	             
		} catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();
	            try {
					txtstat.setText("สถานะ : อัพ");
	                ftpclient.disconnect(true);    
	            } catch (Exception e2) {
	                e2.printStackTrace();
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
				WWCODE =curEmp.getString(curEmp.getColumnIndex("wwcode")); 
				
			}
		}
	}

}
