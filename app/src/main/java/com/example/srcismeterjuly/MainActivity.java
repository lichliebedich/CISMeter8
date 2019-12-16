package com.example.srcismeterjuly;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Set;

public class MainActivity extends Activity implements android.view.View.OnClickListener {


	ListView listView;
	DBClass myDB;
	public SQLiteCursor mCursor;
	private DBClass db = new DBClass(this);
	private long lastBackPressTime = 0;
	long date = System.currentTimeMillis();
	TextView textview;
	private LocationManager locationManager;
	ProgressDialog dialog;
	private double lat;
	private double lon;
	public String USERID;
	TextView tvLocInfo;
	private Toast ms;
	private String TAG = "PermissionDemo";
	private int RECORD_REQUEST_CODE = 101;
	public String printername = "", printerbrand = "", printaddress = "";
	public static final int REFRESH_SCREEN = 1;
	private static final int REQUEST_ENABLE_BT = 2;
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	private BluetoothAdapter mBluetoothAdapter = null;
	private bluetoothCommandService mCommandService = null;
	boolean Statust_Connect = false;
	//*************************************PRINT*****************************************/
	boolean Connectdevice = true;
	EditText etUserId;

	@SuppressLint({"SimpleDateFormat", "MissingPermission"})
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_login);

		if (!checkIfAlreadyhavePermission()) {
			requestForSpecificPermission();
		}


		View btnlogin = findViewById(R.id.btnlogin);
		btnlogin.setOnClickListener(this);
		View btnftpload = findViewById(R.id.btnftpload);
		btnftpload.setOnClickListener(this);
		View btnloadPro = findViewById(R.id.bnLoadPro);
		btnloadPro.setOnClickListener(this);
		View btnprint = findViewById(R.id.bnPrint);
		btnprint.setOnClickListener(this);

		isWriteStoragePermissionGranted();
		//setupPermissions();
		Check_and_Create_folder();
		turnGPSOn();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy h:mm a");
		String dateString = sdf.format(date);
		TextView tvlogin = (TextView) findViewById(R.id.tv_login);
		tvlogin.setText(dateString);


		tvLocInfo = (TextView) findViewById(R.id.txtlatlong);
		tvLocInfo.setText(lat + "," + lon);
		etUserId = (EditText) findViewById(R.id.et_userid);
		etUserId.requestFocus();

		etUserId.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
						(keyCode == KeyEvent.KEYCODE_ENTER)) {

					USERID = etUserId.getText().toString().trim();

					if (getlogin(USERID) || (USERID.equalsIgnoreCase("8899"))) {
						Intent readMenu = new Intent(getApplicationContext(), MainMenu.class);
						readMenu.putExtra("userid", USERID);
						startActivityForResult(readMenu, 999);
					} else {
						showDialog("รหัสของคุณไมถูกต้อง");
					}
					return true;
				}
				return false;
			}
		});


		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
			finish();
			return;
		}


		//สร้าง locationManager เพื่อใช้ในการรับตำแหน่ง
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		LocationListener locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				//Log.d("LOCATE", "LOCATE CHANGE");
				// TODO Auto-generated method stub

				//รับค่า Latitude และ Longitude จาก location
				lat = location.getLatitude();
				lon = location.getLongitude();
				//Log.d("lat", String.valueOf(lat));
				//ทำการแสดงค่า Latitude และ Longitude ที่ได้รับมา
				//15.2006971,104.2983381
				if (lat==0.0) {
					lat = 15.20069713;
					lon = 104.2983381;
				}
				tvLocInfo.setText(lat + "," + lon);
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(i);

			}


		};

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		//locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
		Log.d("lat", String.valueOf(lat));

	}
	private void requestForSpecificPermission() {
		ActivityCompat.requestPermissions(this, new String[]{
				Manifest.permission.ACCESS_FINE_LOCATION,
				Manifest.permission.ACCESS_COARSE_LOCATION,
				Manifest.permission.INTERNET,
				Manifest.permission.CAMERA,
				Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.BLUETOOTH,
				Manifest.permission.READ_PHONE_STATE,
				Manifest.permission.READ_CALL_LOG,
				Manifest.permission.READ_CONTACTS}, 101);

	}

	private boolean checkIfAlreadyhavePermission() {
		int result = ContextCompat.checkSelfPermission(this,
				Manifest.permission.GET_ACCOUNTS);
		return result == PackageManager.PERMISSION_GRANTED;
	}

	public  void isWriteStoragePermissionGranted() {
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
					== PackageManager.PERMISSION_GRANTED) {
				Log.v(TAG,"Permission is granted2");

			} else {

				Log.v(TAG,"Permission is revoked2");
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
						RECORD_REQUEST_CODE);

			}
		}
		else { //permission is automatically granted on sdk<23 upon installation
			Log.v(TAG,"Permission is granted2");

		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case 101:
				Log.d(TAG, "Access Fine Location");
				// Check if the only required permission has been granted
				if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// Camera permission has been granted, preview can be displayed
					Log.i(TAG, "permission has now been granted. Showing preview.");

				} else {
					Log.i(TAG, "permission was NOT granted.");


				}
				// END_INCLUDE(permission_result)
				break;

		}
	}



	@Override
	protected void onStart() {
		super.onStart();
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}else {
			if (mCommandService==null)
				setupCommand();
		}
	}
	
	public void  Check_and_Create_folder() {
		 File sd = Environment.getExternalStorageDirectory();
		 boolean P_Directory;
		 String p_root = sd + "/CIS";
		 String p_backup = sd + "/CIS/BACKUP";
		 String p_database = sd + "/CIS/DATABASE";
		 String p_download = sd+"/CIS/DOWNLOAD";
		 String p_upload = sd+"/CIS/UPLOAD";
		 String p_image = sd+"/CIS/IMAGE";
		 String p_autobk = sd + "/CIS/AUTOBK";
		 
		 
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
		 File P_PathUpload = new File(p_upload);
		 if (!P_PathUpload.exists()) {
				P_Directory =  P_PathUpload.mkdir();
				if (P_Directory){
					Log.d("","UploadPath");
				}		
		 }
		 File P_PathDownload = new File(p_download);
		 if (!P_PathDownload.exists()) {
				P_Directory =  P_PathDownload.mkdir();
				if (P_Directory){
					Log.d("","UploadPath");
				}		
		 }
		 File P_PathImage = new File(p_image);
		 if (!P_PathImage.exists()) {
				P_Directory =  P_PathImage.mkdir();
				if (P_Directory){
					Log.d("","ImagePath");
				}		
		 }
		 File P_AutoBk = new File(p_autobk);
		 if (!P_AutoBk.exists()) {
				P_Directory =  P_AutoBk.mkdir();
				if (P_Directory){
					Log.d("","AutoBkPath");
				}		
		 }

		 scanFile(p_root);
		 scanFile(p_backup);
		 scanFile(p_database);
		 scanFile(p_download);
		 scanFile(p_image);
		 scanFile(p_upload);
		 scanFile(p_autobk);

	 }
	
	private void scanFile(String path) {

        MediaScannerConnection.scanFile(MainActivity.this,
                new String[] { path }, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("TAG", "Finished scanning " + path);
                    }
                });
    }	
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
		if (mCommandService != null) {
			if (mCommandService.getState() == bluetoothCommandService.STATE_NONE) {
				mCommandService.start();
			}
		}

		//หาพิกัดจาก network
		Location networkLocation = myFindLocation(LocationManager.NETWORK_PROVIDER);
		if (networkLocation != null) {
			lat = networkLocation.getLatitude();
			lon = networkLocation.getLongitude();
		}

		//หาพิกัดจาก PGS Card
		Location gpsLocation = myFindLocation(LocationManager.GPS_PROVIDER);
		if (gpsLocation != null) {
			lat= gpsLocation.getLatitude();
			lon = gpsLocation.getLongitude();
		}

		showView();
	}

	public Location myFindLocation(String strProvider) {

		Location location = null;
		//เช็คการ์ด GPS
		if (locationManager.isProviderEnabled(strProvider)) {

			//8
			//Check permission ได้มาจากกด alt + enter เลือก add permission จากการส่งตรง locationManager.requestLocationUpdates(strProvider, 1000, 10, locationListener);
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return null;
			}
			//7
			locationManager.requestLocationUpdates(strProvider, 0, 0, locationListener);
			//9
			location = locationManager.getLastKnownLocation(strProvider);
		}
		return location;
	}

	//เมื่อมีการเปลี่ยนพิกัด class นี้จะทำงาน //4
	public LocationListener locationListener = new LocationListener() {
		@Override

		public void onLocationChanged(Location location) {
			//5
			lat = location.getLatitude();
			lon = location.getLongitude();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}
	};


	private void showView() {
		tvLocInfo.setText(lat + "," + lon);
	}
	
	private void setupCommand() {
        mCommandService = new bluetoothCommandService(this, mHandler);
	}
	protected void onDestroy() {
		super.onDestroy();
		
		if (mCommandService != null)
			mCommandService.stop();
	}
	
	private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30000);
            startActivity(discoverableIntent);
        }
    }

    private final Handler mHandler = new Handler() {
        
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                
                case bluetoothCommandService.STATE_CONNECTED:
                   /* mTitle.setText(R.string.title_connected_to);
                    mTitle.append(mConnectedDeviceName);*/
                    Statust_Connect=true;
                    break;
                    
                case bluetoothCommandService.STATE_CONNECTING:
                   // mTitle.setText(R.string.title_connecting);
                    break;
                case bluetoothCommandService.STATE_LISTEN:
                	
                case bluetoothCommandService.STATE_NONE:
                   // mTitle.setText(R.string.title_not_connected);
                    Statust_Connect=false;
                   break;
                }
                break;
            case MESSAGE_DEVICE_NAME:
                msg.getData().getString(DEVICE_NAME);
               // Toast.makeText(getApplicationContext(), "Connected to "+ mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.btnlogin:
			  USERID = etUserId.getText().toString().trim();
			  if (getlogin(USERID) || (USERID.equalsIgnoreCase("8899"))){
				  Intent readMenu = new Intent(getApplicationContext(),MainMenu.class);
				  readMenu.putExtra("userid", USERID);
				  startActivityForResult (readMenu, 999 );
			  }
			  else{
				  showDialog("รหัสของคุณไม่ถูกต้อง");
			  }
			  
			break;
		case R.id.btnftpload:
			Intent i1 = new Intent(this,ftpload.class);
			startActivity(i1);
			break;
		case R.id.bnLoadPro:
			LoadProgram();
			break;
			case R.id.bnPrint:
			TestPrint();
		}


	}

private void TestPrint(){
	String strtoprint;
	try {
		Toast.makeText(this,"กำลังพิมพ์ใบแจ้งหนี้....",Toast.LENGTH_SHORT).show();
		strtoprint="EZ{PRINT:\r\n";
		strtoprint+="@150,300:AN12B,HM1,VM1|สาขาสยามราชธานีทดสอบ|";
		strtoprint+="@20,210:AN12B,HM1,VM1|โปรดชำระเงินในวันถัดไป|";
		strtoprint+="@180,290:AN12B,HM1,VM1|0-4524-3910-1  ต่อ 107|";
		strtoprint+="@285,3:AN12B,HM1,VM1|6180344074|";
		strtoprint+="@285,185:AN12B,HM1,VM1|3303669|";
		strtoprint+="@285,360:AN12B,HM1,VM1|5532011-64|";
		strtoprint+="@350,1:AN12B,HM1,VM1,RIGHT|18/10/2561 17:19|";
		strtoprint+="@350,220:AN12B,HM1,VM1|25/10/61|";
		strtoprint+="@350,360:AN12B,HM1,VM1|010001.4|";
		strtoprint+="@390,95:AN12B,HM1,VM1|นายทดสอบ อ่านมาตร|";
		strtoprint+="@420,50:AN12B,HM1,VM1|909 ม.99 ต.ขามใหญ่ อ.เมืองอุบลราชธานี จ|";
		strtoprint+="@490,170:AN12B,HM1,VM1|01/06/61|";
		strtoprint+="@490,340:AN12B,HM1,VM1|18/10/61|";
		strtoprint+="@520,170:AN12B,HM1,VM1|1861|";
		strtoprint+="@520,340:AN12B,HM1,VM1|2007|";
		strtoprint+="@550,420:AN12B,HM1,VM1,RIGHT|146,000|";
		strtoprint+="@585,115:AN12B,HM1,VM1|T111(10/61)|";
		strtoprint+="@585,420:AN12B,HM1,VM1,RIGHT|2,955.20|";
		strtoprint+="@620,420:AN12B,HM1,VM1,RIGHT|0.00|";
		strtoprint+="@650,420:AN12B,HM1,VM1,RIGHT|50.00|";
		strtoprint+="@710,420:AN12B,HM1,VM1,RIGHT|3,215.56|";
		strtoprint+="@680,420:AN12B,HM1,VM1,RIGHT|210.36|";
		strtoprint+="@740,160:AN12B,HM1,VM1|0|";
		strtoprint+="@740,420:AN12B,HM1,VM1,RIGHT|0.00|";
		strtoprint+="@770,420:AN12B,HM1,VM1,RIGHT|3,215.56|";
		strtoprint+="@910,30:AN12B,HM1,VM1|(lat,lon)0.0,0.0|";
		strtoprint+="@860,430:AN12B,HM1,VM1|25/10/61|";
		strtoprint+="@890,430:AN12B,HM1,VM1|1/10/61|";
		strtoprint+="@950,30:AN12N,HM1,VM1|V.0.0.1|";
		strtoprint+="@950,200:AN12N,HM1,VM1|ID.8899(01)|";
		strtoprint+="@850,515:BC128,ROT90,HIGH 10,WIDE 2|61803440743303669553201164281061000321556|";
		strtoprint+="@850,555:AN12N,ROT90,HM1,VM1|61803440743303669553201164281061000321556|";
		strtoprint+="@860,3:AN09B,HM1,VM1|**โปรดระวังมิจฉาชีพแอบอ้างเก็บเงินค่าน้ำประปา**|";
		strtoprint+="@1010,30:QR_BC,SECURITY45,XDIM3|/|099400016490411";
		strtoprint+="011853303669281061\r";
		strtoprint+="61803440745532011\r";
		strtoprint+="321556|";
		strtoprint+="@1140,30:AN12B,HM1,VM1|การประปาส่วนภูมิภาค|";
		strtoprint+="@1140,600:AN12B,HM1,VM1,RIGHT|ใบเสร็จรับเงิน/ใบกำกับภาษี|";
		strtoprint+="@1170,30:AN12B,HM1,VM1|สาขาสยามราชธานีทดสอบ|";
		strtoprint+="@1170,600:AN12B,HM1,VM1,RIGHT|เลขประจำตัวผู้เสียภาษี|";
		strtoprint+="@1200,30:AN10B,HM1,VM1|93/796 ม.7 ซ.มหาชัยวิลล่า ต.ท่าทราย|";
		strtoprint+="@1200,600:AN12B,HM1,VM1,RIGHT|0094000164904|";
		strtoprint+="@1220,30:AN10B,HM1,VM1|อ.เมืองสมุทรสาคร จ.สมทุรสาคร74000|";
		strtoprint+="@1220,600:AN12B,HM1,VM1,RIGHT|สาขาที่ 00069|";
		strtoprint+="@1250,30:AN12B,HM1,VM1|โทรศัพท์ : 034411844|";
		strtoprint+="@1250,600:AN12B,HM1,VM1,RIGHT|เลขที่ : WT1184/600337534|";
		strtoprint+="@1290,600:AN12B,HM1,VM1,RIGHT|วันเดือนปี : 19 ธันวาคม 2562|";
		strtoprint+="@1320,30:AN12B,HM1,VM1|เลขที่ผู้ใช้น้ำ : 11840387014|";
		strtoprint+="@1350,30:AN12B,HM1,VM1|ชื่อผู้ใช้น้ำ : ธนาคารเพื่อการเกษตรและสหกรณ์การเกษตร|";
		strtoprint+="@1390,30:AN10B,HM1,VM1|ที่อยู่ : 87/10 ม.2 ต.คลองมะเดื่อ อ.กระทุ่มแบน จ.สมุทรสาคร 74000|";
		strtoprint+="@1430,30:AN12B,HM1,VM1|เลขประจำตัวผู้เสียภาษี : 0994000164912  สาขาที่ 00675|";
		strtoprint+="@1460,30:AN12B,HM1,VM1|จำนวนหน่วยน้ำใช้|";
		strtoprint+="@1460,500:AN12B,HM1,VM1,RIGHT|131,000 ลิตร|";
		strtoprint+="@1490,30:AN12B,HM1,VM1|ค่าน้ำประจำเดือน|";
		strtoprint+="@1490,500:AN12B,HM1,VM1,RIGHT|05/2561|";
		strtoprint+="@1520,30:AN12B,HM1,VM1|ค่าน้ำ|";
		strtoprint+="@1520,500:AN12B,HM1,VM1,RIGHT|3,754.00 บาท|";
		strtoprint+="@1550,30:AN12B,HM1,VM1|ส่วนลด|";
		strtoprint+="@1550,500:AN12B,HM1,VM1,RIGHT|0.00 บาท|";
		strtoprint+="@1580,30:AN12B,HM1,VM1|ค่าบริการ|";
		strtoprint+="@1580,500:AN12B,HM1,VM1,RIGHT|50.00 บาท|";
		strtoprint+="@1610,30:AN12B,HM1,VM1|รวมเงิน|";
		strtoprint+="@1610,500:AN12B,HM1,VM1,RIGHT|3,804.00 บาท|";
		strtoprint+="@1640,30:AN12B,HM1,VM1|ปรับปรุงค่าน้ำรับซ้ำ|";
		strtoprint+="@1640,500:AN12B,HM1,VM1,RIGHT|0.00 บาท|";
		strtoprint+="@1670,30:AN12B,HM1,VM1|ภาษีมูลค่าเพิ่ม 7%|";
		strtoprint+="@1670,500:AN12B,HM1,VM1,RIGHT|266.28 บาท|";
		strtoprint+="@1700,30:AN12B,HM1,VM1|รวมทั้งสิ้น|";
		strtoprint+="@1700,500:AN12B,HM1,VM1,RIGHT|4,070.28 บาท|";
		strtoprint+="@1740,30:AN12B,HM1,VM1|หักบัญชี ธนาคารเพื่อการเกษตรและสหกรณ์การเกษตร|";
		strtoprint+="@1780,130:AN12B,HM1,VM1|ผู้รับเงิน น.ส. นพรัตน์ ลือบางใหญ่ 15749|";
		strtoprint+="@1810,230:AN12B,HM1,VM1|สำนักงานใหญ่|";
		strtoprint+="}";

		try {

			Thread t = new Thread(new GotoPrint(strtoprint));
			t.start();

		} catch (Exception e) {
			// TODO: handle exception
			showDialog("ติดต่อเครื่องพิมพ์ใบแจ้งหนี้ไม่ได้  ?? " + e.getMessage());

		}
	}catch(Exception e){
		Toast.makeText(this,"กรุณาเชื่อมต่อเครื่องพิมพ์ : " + e.getMessage(),Toast.LENGTH_LONG).show();
	}
}

	class GotoPrint implements Runnable{

		String Re_Message;
		String Re_printaddress;

		private GotoPrint(String Message){
			Re_Message = Message;
		}
		@Override
		public void run() {
			try {
				//------------------------------------------------------------------------
				// BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
				BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
				Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

				String testdevice = pairedDevices.toString();
				//String address = testdevice.substring(testdevice.indexOf("#")+1,testdevice.length());
				String address = testdevice.substring(1, 18);
				Log.d("Print", "mess: "+ address);
				//Log.d("Print", "mess: "+ mBluetoothAdapter.getAddress());
				//BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("00:17:AC:11:60:02");
				BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
				mCommandService.connect_1(device);

				int nWaitTime = 8;

				while (mCommandService.getState() != mCommandService.STATE_CONNECTED) {
					Thread.sleep(1000);
					nWaitTime--;
					if (nWaitTime==0){
						//DisplayPrintingStatusMessage("Unable to connect!");
						throw( new Throwable("\nชื่อ: " + device.getName()+ "\nรหัส: "+device.getAddress()+ " ไม่สามารถเชื่อมต่อได้"));
					}
				}

				mCommandService.write(Re_Message);


//				}
			}catch (final Throwable e) {
				//B_printBill.setEnabled(true);
				runOnUiThread(new Runnable() {
					public void run() {
						showDialog("กรุณาตรวจสอบเครื่องพิมพ์ "+e.getMessage());
					}
				});

			}

			finally {
				if (mCommandService!=null){
					mCommandService.stop();
					if (mCommandService.getState() != mCommandService.STATE_NONE){
						Statust_Connect=false;
					}
				}
			}

		}

	}

private void LoadProgram() {
		// TODO Auto-generated method stub
	// String SRFTPHOST  = "110.170.30.38"; ลิงค์เดิมเปลี่ยนวันที่ 18/12/2560
	final String SRFTPHOST  = "203.151.43.168";
	final String SRFTPUSER  = "ftpadmin";
	final String SRFTPPASS  = "siamrajadmin";
    final int FTPPORT = 21;
	

	if (isOnline()){
    	
        Log.d("internet status","Internet Access");
    	Toast.makeText(getBaseContext(), "เตรียมพร้อมดาวน์โหลด", Toast.LENGTH_SHORT).show();
    }else{
        Log.d("internet status","no Internet Access");
    	Toast.makeText(getBaseContext(), "ไม่สามารถเชื่อมต่ออินเตอร์เน็ตได้ กรุณาตรวจสอบ", Toast.LENGTH_SHORT).show();
    }
    
	String title = "Downloading Data";
	String msg = "Please Wait...";
	dialog = ProgressDialog.show(MainActivity.this, title, msg);
	
    Thread thread = new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				  Log.d("begin1", "OK");
				FTPClient ftpClient = new FTPClient();
                try {
                    ftpClient.connect(SRFTPHOST, FTPPORT);
                    ftpClient.login(SRFTPUSER, SRFTPPASS);
                    ftpClient.enterLocalPassiveMode();
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    String remoteFile2 = "/CIS/UPDATEPROGRAM/SRCISMeter.apk";
                    File downloadFile2 = new File(Environment.getExternalStorageDirectory(),"/CIS/DOWNLOAD/SRCISMeter.apk");
                    OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile2));
                    InputStream inputStream = ftpClient.retrieveFileStream(remoteFile2);
                    byte[] bytesArray = new byte[4096];
                    int bytesRead = -1;
                    Log.d("begin2", "OK");
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
			hRefresh.sendEmptyMessage(REFRESH_SCREEN);
			
		}

    });
    thread.start();
    
}
@SuppressLint("HandlerLeak")
Handler hRefresh = new Handler(){
	public void handleMessage(Message msg) {

		switch(msg.what){
		case REFRESH_SCREEN:
			Toast.makeText(getBaseContext(), "โหลดโปรแกรมเรียบร้อยแล้ว ..", Toast.LENGTH_SHORT).show();

			break;
		default:
			break;
		}
	}
};


public boolean isOnline() {
    ConnectivityManager cm =
        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    return netInfo != null && netInfo.isConnectedOrConnecting();
}

public boolean getlogin(String userid){
		String strEmpid;
		Cursor curEmp;
		curEmp= db.select_EmpData();
		boolean success = true;
		if(curEmp != null)
		{
			if (curEmp.moveToFirst()) {
				strEmpid = curEmp.getString(curEmp.getColumnIndex("empid"));   
				Log.d("id",strEmpid);
				if (userid.equalsIgnoreCase(strEmpid)){
					success= true;
				}else{
					success= false;
				}
			}
		}
		return success;
	}




    private void showDialog(String message) {

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage(message);
        alt_bld.setTitle("แจ้งเตือน");
        alt_bld.setPositiveButton("ตกลง",null);
        alt_bld.show();

    }
	 
	 @Override
		public void onBackPressed() {
			// TODO Auto-generated method stub

		 
		 if(this.lastBackPressTime < System.currentTimeMillis() - 2000){
				ms =Toast.makeText(this, "กด BACK อีกครั้งเพื่อออกจากโปรแกรม",Toast.LENGTH_LONG);
				ms.show();
				this.lastBackPressTime = System.currentTimeMillis();
			}else{
				if(ms != null){
					ms.cancel();
				}
				super.onBackPressed();
			}
	}
		 
	
	 
	 /** Method to turn on GPS **/
		public void turnGPSOn(){
			try
			{
		
				String provider = Settings.Secure.getString(getContentResolver()
						, Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

				if(!provider.contains("gps")){ //if gps is disabled
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(intent);
				}
			}
			catch (Exception e) {

			}
		}


		
}
