package com.example.srcismeterjuly;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;



public class ftpload extends Activity {
	public String wwcode ="";
	public String emp_id="";
	ProgressDialog dialog;
	public static final int REFRESH_SCREEN = 1;
	TextView txtstat;

	// String SRFTPHOST  = "110.170.30.38"; ลิงค์เดิมเปลี่ยนวันที่ 18/12/2560
	String SRFTPHOST = "203.151.43.168";
	String SRFTPUSER  = "ftpadmin";
	String SRFTPPASS  = "siamrajadmin";
    int FTPPORT = 21;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_ftpload);

		
		final Spinner spin1 = (Spinner) findViewById(R.id.spinner1);
        final Button btn1 = (Button) findViewById(R.id.btn_find);
        txtstat = (TextView)findViewById(R.id.txtstat);

        
        txtstat.setText("เตรียมพร้อม");
        // Perform action on click
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	String title = "Downloading Data";
				String msg = "Please Wait...";
				dialog = ProgressDialog.show(ftpload.this, title, msg);
            	
            	TextView txtemp   = (EditText)findViewById(R.id.txtemp);
            	emp_id=txtemp.getText().toString();
        	    int tempwwcode = spin1.getSelectedItemPosition();
        	    switch (tempwwcode) {
					case 0 : wwcode = "0000000"; break;
					case 1 : wwcode = "1125"; break;//5532011อุบล
					case 2 : wwcode = "1126"; break;//5532012พิบูล
					case 3 : wwcode = "1127"; break;//5532013เดชอุดม
					case 4 : wwcode = "1128"; break;//5532014เขมราฐ
					case 5 : wwcode = "1129"; break;//5532015อำนาจเจริญ
					case 6 : wwcode = "1130"; break;//5532016ยโสธร
					case 7 : wwcode = "1131"; break;//5532017เลิงนกทา
					case 8 : wwcode = "1132"; break;//5532018มหาชนะชัย
					case 9 : wwcode = "1136"; break;//5532019บุรีรัมย์
					case 10 : wwcode = "1137"; break;//5532020สตึก
					case 11 : wwcode = "1138"; break;//5532021ลำปลายมาศ
					case 12 : wwcode = "1139"; break;//5532022นางรอง
					case 13 : wwcode = "1140"; break;//5532023ละหานทราย
					case 14 : wwcode = "1141"; break;//5532024สุรินทร์
					case 15 : wwcode = "1142"; break;//5532025ศีขรภูมิ
					case 16 : wwcode = "1143"; break;//5532026รัตนบุรี
					case 17 : wwcode = "1246"; break;//5532027สังขะ
					case 18 : wwcode = "1144"; break;//5532028ศรีสะเกษ
					case 19 : wwcode = "1145"; break;//5532029กัลทรลักษ์
					case 20 : wwcode = "1099"; break;//5532030มุกดาหาร
					case 21 : wwcode = "1039"; break;//5512017ขาณุ
					case 22 : wwcode = "1038"; break;//กำแพง
					case 23 : wwcode = "1040"; break;//ตาก
					case 24 : wwcode = "1041"; break;//แม่สอด
					case 25 : wwcode = "1032"; break;//นครสวรรค์
					case 26 : wwcode = "1048"; break;//พิษณุโลก
				default:
					break;
				}
        	    isInternetAvailable();
        	    if (isInternetAvailable() == false) {
        	    	txtstat.setText("ไม่สามารถเชื่อมต่ออินเตอร์เน็ตได้ กรุณาตรวจสอบ");
        	    	//Toast.makeText(getBaseContext(), "CONNECT FAILED", Toast.LENGTH_SHORT).show();
				}else{
					txtstat.setText("เชื่อมต่ออินเตอร์เน็ตเรียบร้อยแล้ว");
					//Toast.makeText(getBaseContext(), "CONNECT PASSED", Toast.LENGTH_SHORT).show();
				}
        	   
        	    Thread thread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							FTPClient ftpClient = new FTPClient();
			                try {
			                    ftpClient.connect(SRFTPHOST, FTPPORT);
			                    ftpClient.login(SRFTPUSER, SRFTPPASS);
			                    ftpClient.enterLocalPassiveMode();
			                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			                    String remoteFile2 = "/CIS/PCTOHH/"+ wwcode +"/"+ emp_id +"/CISData.xml";
			                    Log.d("str",remoteFile2);
			                    File downloadFile2 = new File(Environment.getExternalStorageDirectory()
										 ,"/CIS/DOWNLOAD/CISData.xml");
			                    OutputStream outputStream2 = new BufferedOutputStream
										(new FileOutputStream(downloadFile2));
			                    InputStream inputStream = ftpClient.retrieveFileStream(remoteFile2);
			                    byte[] bytesArray = new byte[4096];
			                    int bytesRead =-1;
			                    while ((bytesRead = inputStream.read(bytesArray)) != -1) {
			                        outputStream2.write(bytesArray, 0, bytesRead);
			                    }
			                    boolean success = ftpClient.completePendingCommand();
				                   if (success) {
				                        System.out.println("File #2 has been downloaded successfully.");
				                    }
				                    outputStream2.close();
				                    inputStream.close();
				                    
				                } catch (IOException ex) {
				                    System.out.println("Error: " + ex.getMessage());
				                    ex.printStackTrace();
				                }finally {
				                    try {
				                        if (ftpClient.isConnected()) {
				                            ftpClient.logout();
				                            ftpClient.disconnect();
				                        }
				                    } catch (IOException ex) {
				                        ex.printStackTrace();
				                    }
				                }
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						dialog.dismiss();
						if (isInternetAvailable() == false) {
							//txtstat.setText("ไม่สามารถเชื่อมต่ออินเทอร์เน็ตได้ กรุณาตรวจสอบ");
						}else{
								hRefresh.sendEmptyMessage(REFRESH_SCREEN);
						}
					}

					
					
        	    });
        	    thread.start();
            }
           });
	}
	
public boolean isInternetAvailable() {

		
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name

            if (ipAddr.equals("")) {
            	//Toast.makeText(getBaseContext(), "CONNECT FAILED", Toast.LENGTH_SHORT).show();
                return false;
            } else {
            	//Toast.makeText(getBaseContext(), "CONNECT PASSED", Toast.LENGTH_SHORT).show();
                return true;
            }

        } catch (Exception e) {
        	//Toast.makeText(getBaseContext(), "CONNECT FAILED", Toast.LENGTH_SHORT).show();
            return false;
        }

    }
@SuppressLint("HandlerLeak")
Handler hRefresh = new Handler(){
	public void handleMessage(Message msg) {

		switch(msg.what){
		case REFRESH_SCREEN:
			txtstat.setText("ดาวน์โหลดข้อมูลเรียบร้อยแล้ว");
			break;
		default:
			break;
		}
	}
};
}
