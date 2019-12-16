package com.example.srcismeterjuly;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;


public class ReadMeter extends Activity implements OnClickListener {


	byte[] printData = {0};
	public Cursor cur, curEmp;
	private static final int CAMERA_REQUEST = 1;
	private static final int RESULT_OK = -1;
	private static final int FIND_OK = 3;
	private static final int RESULT_REQUEST = 2;
	private int RECORD_REQUEST_CODE = 101;
	private DBClass db = new DBClass(this);
	Button bn_notread, bn_readagain, bn_reprint, bn_find;
	ImageButton bn_next, bn_prev, bn_first, bn_last;
	Button bnCap, bnOk;
	CheckBox ckAvg;
	public TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11, tv12,
			tvdupamt, tvpwaflag, tvrevym, tvdebmonth;
	EditText etRead;
	Uri imageFileUri;

	private String chRoute;
	private double lat;
	private double lon;
	private double dupamt, dupnet, dupvat, debtamt, debtnet, debtvat, tmpdupamt;
	private double distempamt, distempnet, distempvat;
	private LocationManager locationManager;

	private String wwcode;
	private String Mtrrdroute;
	private int Mtrseq;
	private String usertype;// ประเภทผู้ใช้น้ำ
	private String CustCode; //ชื่อผู้ใช้น้ำ
	private String DisCntCode;// ประเภทส่วนลด
	private String MeterNo, MeterSize, MtRmkCode; // ขนาดมิเตอร์
	private String controlmtr;
	private int ReadFlag;
	private double LstMtrCnt; // เลขมิเตอร์ก่อนจด
	private double PrsMtrCnt; //เลขมิเตอร์หลังจด
	private double Est; //ค่าประมาณการ
	private double PrsWtUsg; // หน่วยที่ใช้น้ำ PrsMtrCnt - LstMtrCnt
	private int AvgWtUsg;
	private double Smcnt;//หน่วยน้ำมาตรย่อย
	private double BigPrsWtUsg; //รวมมาตรย่อย
	private double NoofHouse;// บ้านเคหะ
	private String PwaFlag;//ส่วนลดประชารัฐ
	private double OldMtrUsg; // หน่วยน้ำค้างมิเตอร์ก่อนเปลี่ยน
	private int debmonth;
	private double debamt;
	private double Unitdiscnt; //ส่วนลดหน่วยน้ำมาตรใหญ่
	private double Discntbaht; //ส่วนลดค่าน้ำทหาร
	private double LowPrice; // ค่าน้ำขั้นต่ำตามประเภทผู้ใช้น้ำ
	private String Invoicecnt;
	private double NorTrfwt;//ค่าใช้น้ำปกติ
	private double DiscntAmt;// ส่วนลด ดูจาก DisCntCode
	private double NetTrfWt; // ค่าใช้จ่ายจริง
	private double Vat; // ภาษี (NetTrfWt + SrvFee) * 0.7
	private double SrvFee; // ค่าน้ำขั้นต่ำ คิดตามขนาดมิเตอร์
	private double TotTrfwt; // ค่าน้ำสุทธิ NetTrfWt + Vat + SrvFee
	private String ChkDigit;// chkDigit ที่พิมพ์ใน Barcode
	private String Str_report = "Null";

	private String Comment;
	public String USERID;
	public String HLN;
	public String ComMentDec;
	public String BillSend;
	public boolean OkRead;
	public boolean OkPrint;
	public String Usgcalmthd = "2";
	public String CustStat;
	public String invflag;
	public String ServiceFlag = "1";
	private int mon1;
	private int mon2;
	private int mon3;

	private String revym;
	private String revym1;
	private String revym2;
	private String revym3;

	public static boolean SendAVGSt;
	public boolean SendOutSt;
	public boolean SendNotIn;

	private int MIN1 = 4;//หน่วยน้ำขั้นต่ำประเภท1
	private int MIN2 = 9;//หน่วยน้ำขั้นต่ำประเภท2
	private int MIN3 = 15;//หน่วยน้ำขั้นต่ำประเภท3

	TextView tvLocInfo;
	String bn_value;
	String billcode;
	String billmonth;
	int chkRead;

	private int iposition, rposition;
	public String printaddress = "";
	private static final int REQUEST_ENABLE_BT = 2;
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	public String printerName;

	private BluetoothAdapter mBluetoothAdapter = null;
	private bluetoothCommandService mCommandService = null;
	boolean Statust_Connect = false;

	@SuppressLint("MissingPermission")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_readmeter);

		//myDB = new DBClass(this);
		//db = myDB.getReadableDatabase();

		initWidget();
		chRoute = getIntent().getStringExtra("TRoute");
		USERID = getIntent().getStringExtra("userid");
		printerName = getIntent().getStringExtra("printername");

		if (printerName==null){
			printerName = "HONEYWELL";
		}else{
			printerName = getIntent().getStringExtra("printername");
		}
		Toast.makeText(this, "เครื่องพิมพ์ " + printerName, Toast.LENGTH_SHORT).show();
		showData(chRoute);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		Comment = "00";
		etRead.setText("");
		etRead.requestFocus();
		etRead.setSelectAllOnFocus(true);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(etRead, InputMethodManager.SHOW_IMPLICIT);
		tvLocInfo = (TextView) findViewById(R.id.tv_latlon);
		bn_next.setOnClickListener(this);
		bn_prev.setOnClickListener(this);
		bn_first.setOnClickListener(this);
		bn_last.setOnClickListener(this);
		//bnCap.setOnClickListener(this);
		bnOk.setOnClickListener(this);
		bn_readagain.setOnClickListener(this);
		bn_notread.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					PrsWtUsg = AvgWtUsg;
					PrsMtrCnt = Est + PrsWtUsg;
					Comment = "01";
					BillSend = "1";
					CustStat = "2"; //มาตรผิดปกติ กดเข้าอ่านไม่ได้
					HLN = "N";


					Log.d("กดเข้าอ่านไม่ได้", String.valueOf(PrsWtUsg));
					Intent readPlus = new Intent(getApplicationContext(), ReadMeterPlus.class);
					readPlus.putExtra("userid", USERID);
					readPlus.putExtra("TwwCode", wwcode);
					readPlus.putExtra("TCustCode", CustCode);
					readPlus.putExtra("TRoute", chRoute);
					readPlus.putExtra("MtrSeq", Mtrseq);
					readPlus.putExtra("TMeterno", MeterNo);
					readPlus.putExtra("TPrsmtrcnt", PrsMtrCnt);
					readPlus.putExtra("TPrswtusg", PrsWtUsg);
					readPlus.putExtra("THln", HLN);
					readPlus.putExtra("TSmcnt", Smcnt);
					readPlus.putExtra("TComment", Comment);
					readPlus.putExtra("SentAVG", SendAVGSt);
					readPlus.putExtra("Tmsg", false);
					readPlus.putExtra("Bmsg", false);
					readPlus.putExtra("OkRead", false);
					readPlus.putExtra("OkPrint", false);
					readPlus.putExtra("SendOutSt", true);

					startActivityForResult(readPlus, RESULT_REQUEST);

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		});

		ckAvg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (ckAvg.isChecked()) {
					CustStat = "3";
					SendAVGSt = true;
					bn_notread.setVisibility(View.INVISIBLE);
					Usgcalmthd = "3";
					etRead.requestFocus();
				} else {
					CustStat = "1";
					etRead.requestFocus();
					SendAVGSt = false;
					bn_notread.setVisibility(View.VISIBLE);
					Usgcalmthd = "2";
				}
			}
		});

		bn_reprint.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Str_report.equalsIgnoreCase("")) {
					showDialog("No data to Print");
				} else {
					Thread t = new Thread(new GotoPrint(Str_report, printaddress));
					t.start();
				}
			}
		});

		bn_find.setOnClickListener(new View.OnClickListener() {

			@Override

			public void onClick(View v) {
				// TODO Auto-generated method stub
				FindMeter();
			}
		});



//		//สร้าง locationManager เพื่อใช้ในการรับตำแหน่ง
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		//		//จัดการ Event ต่างๆ ของ Location
		LocationListener locationListener = new LocationListener() {

			//เมื่อหาตำแหน่งได้ จะทำงานที่ function นี้
			public void onLocationChanged(Location location) {

				//รับค่า Latitude และ Longitude จาก location
				lat = location.getLatitude();
				lon = location.getLongitude();

				//ทำการแสดงค่า Latitude และ Longitude ที่ได้รับมา
				//15.2006971,104.2983381
				if (lat==0.0) {
					lat = 15.2006971;
					lon = 104.2983381;
				}



				tvLocInfo.setText(lat + "," + lon);

			}

			public void onProviderDisabled(String provider) {
				Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(i);

			}

			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			public void onStatusChanged(String provider, int status,
										Bundle extras) {
				// TODO Auto-generated method stub

			}
		};

//		//ระบุรูปแบบของตำแหน่งที่เราต้องการ และเชื่อมกับ locationListener
//		//LocationManager.GPS_PROVIDER รับตำแหน่งจาก GPS
//		//LocationManager.NETWORK_PROVIDER รับตำแหน่งจาก Network พวก Wifi EDGE GPRS 3G

		//locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0, 0, locationListener);
		locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {
			// Check Permissions Now
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
					RECORD_REQUEST_CODE);
		} else {
			// permission has been granted, continue as usual
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					0, 0, locationListener);
		}


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
			locationManager.requestLocationUpdates(strProvider, 1000, 10, locationListener);
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
			tvLocInfo.setText(lat + "," + lon);
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

	private void FindMeter() {
		// TODO Auto-generated method stub
		Intent findMeter = new Intent(getApplicationContext(), PageFindMeter.class);
		findMeter.putExtra("tRoute", chRoute);
		startActivityForResult(findMeter, FIND_OK);
	}

	private void initWidget() {

		//bnCap = (Button)findViewById(R.id.bn_cap);
		bnOk = (Button) findViewById(R.id.bn_ok);
		bn_next = (ImageButton) findViewById(R.id.bn_moveN);
		bn_prev = (ImageButton) findViewById(R.id.bn_moveP);
		bn_first = (ImageButton) findViewById(R.id.bn_moveF);
		bn_last = (ImageButton) findViewById(R.id.bn_moveL);
		bn_notread = (Button) findViewById(R.id.bn_notread);
		bn_readagain = (Button) findViewById(R.id.bn_readagain);
		bn_reprint = (Button) findViewById(R.id.bn_printagain);
		bn_find = (Button) findViewById(R.id.bn_findmeter);

		tv1 = (TextView) findViewById(R.id.tx_route2);
		tv2 = (TextView) findViewById(R.id.tx_seq);
		tv3 = (TextView) findViewById(R.id.tx_custcode);
		tv4 = (TextView) findViewById(R.id.tx_custname);
		tv5 = (TextView) findViewById(R.id.tx_custaddr);
		tv6 = (TextView) findViewById(R.id.tx_disccode);
		tv7 = (TextView) findViewById(R.id.tx_usetype);
		tv8 = (TextView) findViewById(R.id.tx_meterno2);
		tv9 = (TextView) findViewById(R.id.tx_mtrmkcode);
		tv10 = (TextView) findViewById(R.id.tx_readflag);
		tv11 = (TextView) findViewById(R.id.tx_lstcnt);
		tv12 = (TextView) findViewById(R.id.tx_metersize);
		tvrevym = (TextView) findViewById(R.id.txt_revym);
		tvdebmonth = (TextView) findViewById(R.id.txt_debmonth);
		ckAvg = (CheckBox) findViewById(R.id.ck_avg);
		tvdupamt = (TextView) findViewById(R.id.tx_dupamt);
		tvpwaflag = (TextView) findViewById(R.id.tx_pwaflag);
		chkRead = 1;
		etRead = (EditText) findViewById(R.id.et_readmeter);
		etRead.setRawInputType(Configuration.KEYBOARD_12KEY);
		etRead.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
						(keyCode == KeyEvent.KEYCODE_ENTER)) {
					calMeter();
					return true;
				}
				return false;
			}
		});
	}

	public void showData(String route) {
		// TODO Auto-generated method stub
		try {
			if (iposition >= 0) {

				cur = db.select_CISData(route);

				int i = cur.getCount();

				if (i > 0) {
					if (cur != null) {
						if (cur.moveToFirst()) {
							cur.moveToPosition(iposition);
							setData(cur);
						}

					}
				} else {
					showDialog("สายการจดหน่วยนี้อ่านข้อมูลครบหมดแล้ว");
					return;
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void setData(Cursor c) {
		int cgetposition;
		if (c.isLast()) {
			Toast.makeText(this, "ข้อมูลสุดท้าย !! ", Toast.LENGTH_SHORT).show();
		}
		cgetposition = c.getPosition();
		iposition = c.getPosition();
		Log.d("Position", String.valueOf(cgetposition));
		dupnet = 0.0;
		dupvat = 0.0;
		dupamt = 0.0;
		debtnet = 0.0;
		debtvat = 0.0;
		debtamt = 0.0;
		tmpdupamt = 0.0;
		distempamt = 0.0;
		distempnet = 0.0;
		distempvat = 0.0;

		tv1.setText(c.getString(c.getColumnIndex("mtrrdroute")));
		tv2.setText(c.getString(c.getColumnIndex("mtrseq")));
		tv3.setText(c.getString(c.getColumnIndex("custcode")));
		tv4.setText(c.getString(c.getColumnIndex("custname")));
		tv5.setText(c.getString(c.getColumnIndex("custaddr")));
		tv6.setText(c.getString(c.getColumnIndex("discntcode")));
		tv7.setText(c.getString(c.getColumnIndex("usertype")));
		tv8.setText(c.getString(c.getColumnIndex("meterno")));
		tv9.setText(c.getString(c.getColumnIndex("mtrmkcode")));
		tv11.setText(c.getString(c.getColumnIndex("lstmtrcnt")));
		tv12.setText(c.getString(c.getColumnIndex("metersize")));
		tvrevym.setText(c.getString(c.getColumnIndex("revym")));
		tvdebmonth.setText(c.getString(c.getColumnIndex("debmonth")));

		wwcode = c.getString(c.getColumnIndex("wwcode"));
		CustCode = c.getString(c.getColumnIndex("custcode"));
		Mtrrdroute = c.getString(c.getColumnIndex("mtrrdroute"));
		Mtrseq = c.getInt(c.getColumnIndex("mtrseq"));
		MeterSize = c.getString(c.getColumnIndex("metersize"));
		Invoicecnt = c.getString(c.getColumnIndex("invoicecnt"));
		MeterNo = c.getString(c.getColumnIndex("meterno"));
		ReadFlag = c.getInt(c.getColumnIndex("readflag"));
		Smcnt = c.getDouble(c.getColumnIndex(("smcnt")));
		CustStat = "1";
		switch (MeterSize) {
			case "01":
				tv12.setText("1/2 นิ้ว");
				break;
			case "02":
				tv12.setText("3/4 นิ้ว");
				break;
			case "03":
				tv12.setText("1 นิ้ว");
				break;
			case "04":
				tv12.setText("1 1/2 นิ้ว");
				break;
			case "05":
				tv12.setText("2 นิ้ว");
				break;
			case "06":
				tv12.setText("2 1/2 นิ้ว");
				break;
			case "07":
				tv12.setText("3 นิ้ว");
				break;
			case "08":
				tv12.setText("4 นิ้ว");
				break;
			case "09":
				tv12.setText("6 นิ้ว");
				break;
			case "10":
				tv12.setText("8และมากกว่า ");
				break;
		}

		MtRmkCode = c.getString(c.getColumnIndex("mtrmkcode"));
		switch (MtRmkCode) {
			case "01":
				tv9.setText("เค็นท์");
				break;
			case "02":
				tv9.setText("อาซาฮี");
				break;
			case "03":
				tv9.setText("อาชิโต้ไก");
				break;
			case "04":
				tv9.setText("เอส.ที.ซี.อี.");
				break;
			case "07":
				tv9.setText("แอสเตอร์");
				break;
			case "10":
				tv9.setText("ออสเซล");
				break;
			case "11":
				tv9.setText("แมคคาลินา");
				break;
			case "14":
				tv9.setText("ไทยอาชิ, ไทยอิชิ");
				break;
			case "15":
				tv9.setText("ไวลท์แม็ค");
				break;
			case "17":
				tv9.setText("สลิมเบอร์เกอร์");
				break;
		}

		ckAvg.setChecked(false);
		bn_notread.setVisibility(View.VISIBLE);
		SendAVGSt = false;
		usertype = c.getString(c.getColumnIndex("usertype"));
		LstMtrCnt = c.getDouble(c.getColumnIndex("lstmtrcnt"));
		controlmtr = c.getString(c.getColumnIndex("controlmtr"));
		Est = c.getDouble(c.getColumnIndex(("meterest")));
		debmonth = c.getInt(c.getColumnIndex("debmonth"));
		debamt = c.getDouble(c.getColumnIndex("debamt"));
		NoofHouse = c.getInt(c.getColumnIndex("noofhouse"));
		PwaFlag = c.getString(c.getColumnIndex("pwa_flag")); //ส่วนลดประชารัฐ


		DisCntCode = c.getString(c.getColumnIndex("discntcode")).trim();
		if (DisCntCode.equalsIgnoreCase("")) {
			DisCntCode = "0";
		}
		OldMtrUsg = c.getDouble(c.getColumnIndex(("remwtusg")));
		invflag = c.getString(c.getColumnIndex("invflag"));
		dupamt = c.getDouble(c.getColumnIndex(("dupamt")));
		tmpdupamt = c.getDouble(c.getColumnIndex(("tempdupamt")));
		tvdupamt.setText(String.valueOf(tmpdupamt));

		if (invflag.equalsIgnoreCase("1")) {
			tvpwaflag.setText("หักผ่านบัญชี");
		} else {
			tvpwaflag.setText("");
		}

		AvgWtUsg = cur.getInt(cur.getColumnIndex("avgwtuse"));
		ServiceFlag = cur.getString(c.getColumnIndex("service_flag"));

		ChkDigit = "00";

		if (ReadFlag == 1) {
			tv10.setText("อ่านแล้ว");
		} else {
			tv10.setText("ยังไม่อ่าน");
		}
		revym = c.getString(c.getColumnIndex("revym"));
		mon1 = c.getInt(c.getColumnIndex("mon1"));
		mon2 = c.getInt(c.getColumnIndex("mon2"));
		mon3 = c.getInt(c.getColumnIndex("mon3"));

		etRead.setText("");
		etRead.requestFocus();
		etRead.setSelectAllOnFocus(true);
		//InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		//imm.showSoftInput(etRead, InputMethodManager.SHOW_IMPLICIT);

	}

	public void Nextdata() {


		if (cur.moveToNext()) {
			setData(cur);
		}

	}

	public void Previousdata() {

		if (cur.moveToPrevious()) {
			setData(cur);
		}
	}

	public void Firstdata() {

		if (cur.moveToFirst()) {
			setData(cur);
		}
	}

	public void Lastdata() {
		if (cur.moveToLast()) {

			setData(cur);
		}
	}

	private void calMeter() {
		// TODO Auto-generated method stub
		Comment = "00";
		SendOutSt = false;

		if (cur != null) {
			AvgWtUsg = cur.getInt(cur.getColumnIndex("avgwtuse"));
			Smcnt = cur.getInt(cur.getColumnIndex("smcnt"));
		}

		String text = etRead.getText().toString().trim();

		if (text.equalsIgnoreCase("") || text == null) {
			Toast.makeText(this, "กรุณาใส่เลขมาตร !! ", Toast.LENGTH_SHORT).show();
		} else {
			PrsMtrCnt = Double.parseDouble(text);
			Intent readPlus = new Intent(getApplicationContext(), ReadMeterPlus.class);

			if (ckAvg.isChecked()) {
				PrsMtrCnt = Double.parseDouble(text);
				PrsWtUsg = AvgWtUsg;
			} else {
				PrsMtrCnt = Double.parseDouble(text);
				getPrsWtUsg();
			}

			PrsMtrCnt = Double.parseDouble(text);

			if (PrsWtUsg < 0) {

				Toast.makeText(this, "หน่วยน้ำติดลบ กรุณาตรวจสอบ", Toast.LENGTH_SHORT).show();
				showNotification();
				return;
			} else {
				Log.d("3e", "3e");
				HLN = "N";
				Double Percent, Npercent, LoLimit, HiLimit;

				if (AvgWtUsg == 0) {
					Log.d("1", "1");
					if (PrsWtUsg > 0) {
						HLN = "H";
					} else {
						HLN = "N";
					}
				} else if (AvgWtUsg > 0) {
					Log.d("2", "2");
					Log.d("AVG >0 ==>", String.valueOf(AvgWtUsg));
					if (PrsWtUsg <= 20) {
						Npercent = 0.5;
						Log.d("ค่า SMCNT ==>", String.valueOf(Smcnt));
						LoLimit = AvgWtUsg - (Npercent * AvgWtUsg);
						HiLimit = AvgWtUsg + (Npercent * AvgWtUsg);
						Log.d("ค่า LO ==>", String.valueOf(LoLimit));
						Log.d("ค่า HI ==>", String.valueOf(HiLimit));
						if ((PrsWtUsg - Smcnt) >= LoLimit && (PrsWtUsg - Smcnt) <= HiLimit) {
							HLN = "N";

						} else if ((PrsWtUsg - Smcnt) < LoLimit) {
							HLN = "L";

						} else if ((PrsWtUsg - Smcnt) > HiLimit) {
							HLN = "H";

						}
					} else if (PrsWtUsg <= 50) {
						Log.d("3", "3");
						Npercent = 0.35;

						LoLimit = AvgWtUsg - (Npercent * AvgWtUsg);
						HiLimit = AvgWtUsg + (Npercent * AvgWtUsg);

						if ((PrsWtUsg - Smcnt) >= LoLimit && (PrsWtUsg - Smcnt) <= HiLimit) {
							HLN = "N";

						} else if ((PrsWtUsg - Smcnt) < LoLimit) {
							HLN = "L";

						} else if ((PrsWtUsg - Smcnt) > HiLimit) {
							HLN = "H";

						}
					} else if (PrsWtUsg <= 100) {
						Log.d("4", "4");
						Percent = Math.abs((PrsWtUsg - Smcnt) - AvgWtUsg);

						if (Percent <= 10) {
							HLN = "N";

						} else if ((PrsWtUsg - Smcnt) < AvgWtUsg) {
							HLN = "L";

						} else if ((PrsWtUsg - Smcnt) > AvgWtUsg) {
							HLN = "H";

						}
					} else if (PrsWtUsg > 100) {
						Percent = Math.abs((PrsWtUsg - Smcnt) - AvgWtUsg) / (PrsWtUsg - Smcnt);
//									 Toast.makeText(this,"avg "+ String.valueOf(AvgWtUsg),Toast.LENGTH_LONG).show();
//						            Toast.makeText(this,"น้ำสูง "+ String.valueOf(PrsWtUsg),Toast.LENGTH_LONG).show();
//						            Toast.makeText(this,"Percent "+ String.valueOf(Percent),Toast.LENGTH_LONG).show();
						if (Percent < 0.2) {
							//Toast.makeText(this,"เข้าเคส "+ String.valueOf(Percent),Toast.LENGTH_LONG).show();
							HLN = "N";

						} else if ((PrsWtUsg - Smcnt) < AvgWtUsg) {
							HLN = "L";

						} else if ((PrsWtUsg - Smcnt) > AvgWtUsg) {
							HLN = "H";

						}
					}
				}


				Log.d("5", "5");

				//เพิ่มเงื่อนไขสำหรับตรวจสอบค่าน้ำสูงเกิน 25/09/2557 เพิ่มน้ำสูงตามช่วงเฉลี่ย (20/11/27)
				switch (usertype.substring(0, 1)) {
					case "1":

						if (PrsWtUsg > (AvgWtUsg * 3) && TotTrfwt >= 3000) {
							Comment = "27";//ใช้น้ำมากผิดปกติ (27)
							HLN = "H";
							readPlus.putExtra("Tmsg", true);
							readPlus.putExtra("Bmsg", false);

						} else {
							readPlus.putExtra("Tmsg", false);
							readPlus.putExtra("Bmsg", true);

						}
						break;
					case "2":
						if (PrsWtUsg > (AvgWtUsg * 3) && TotTrfwt >= 50000) {
							Comment = "27";//ใช้น้ำมากผิดปกติ (27)
							HLN = "H";
							readPlus.putExtra("Tmsg", true);
							readPlus.putExtra("Bmsg", false);

						} else {
							readPlus.putExtra("Tmsg", false);
							readPlus.putExtra("Bmsg", true);

						}
						break;
					case "3":
						if (PrsWtUsg > (AvgWtUsg * 2) && TotTrfwt >= 100000) {
							Comment = "27";//ใช้น้ำมากผิดปกติ (27)
							HLN = "H";
							readPlus.putExtra("Tmsg", true);
							readPlus.putExtra("Bmsg", false);

						} else {
							readPlus.putExtra("Tmsg", false);
							readPlus.putExtra("Bmsg", true);

						}
						break;
				}

				if (MeterSize.equalsIgnoreCase("01") && PrsWtUsg >= 1500) {
					Log.d("Meterno1", MeterSize);
					Comment = "27";//ใช้น้ำมากผิดปกติ (27)
					HLN = "H";
					readPlus.putExtra("Tmsg", true);
					readPlus.putExtra("Bmsg", false);
				} else if (MeterSize.equalsIgnoreCase("02") && PrsWtUsg >= 3000) {
					Log.d("Meterno1", MeterSize);
					Comment = "27";//ใช้น้ำมากผิดปกติ (27)
					HLN = "H";
					readPlus.putExtra("Tmsg", true);
					readPlus.putExtra("Bmsg", false);
				} else if (MeterSize.equalsIgnoreCase("03") && PrsWtUsg >= 4000) {
					Log.d("Meterno1", MeterSize);
					Comment = "27";//ใช้น้ำมากผิดปกติ (27)
					HLN = "H";
					readPlus.putExtra("Tmsg", true);
					readPlus.putExtra("Bmsg", false);
				}

				if (USERID.equalsIgnoreCase("8899")) {
					Toast.makeText(this, "รหัสพนักงาน QC 8899", Toast.LENGTH_SHORT).show();
					readPlus.putExtra("Tmsg", false);
					readPlus.putExtra("Bmsg", true);
				}

				if (HLN.equalsIgnoreCase("L")) {

					Comment = "26"; //ใช้น้ำน้อยผิดปกติ (26);
				} else if (HLN.equalsIgnoreCase("H")) {
					Comment = "27";//ใช้น้ำมากผิดปกติ (27);
				} else if (HLN.equalsIgnoreCase("N")) {

					Comment = "00";
				}

				if (PrsMtrCnt < LstMtrCnt) {//' จดหลัง น้อยกว่า จดก่อน
					//showNotification();
					Comment = "19";//"เลขที่อ่านได้น้อยกว่าเลขมาตรครั้งก่อน (19)"
					Toast.makeText(this, "มาตรถอยหลัง..!! โปรดตรวจสอบ", Toast.LENGTH_SHORT).show();
					//readPlus.putExtra("Tmsg", true);
					//readPlus.putExtra("Bmsg", false);
					SendOutSt = true;
				}

				readPlus.putExtra("userid", USERID);
				readPlus.putExtra("TwwCode", wwcode);
				readPlus.putExtra("MtrSeq", Mtrseq);
				readPlus.putExtra("TCustCode", CustCode);
				readPlus.putExtra("TRoute", chRoute);
				readPlus.putExtra("TMeterno", MeterNo);
				readPlus.putExtra("TPrsmtrcnt", PrsMtrCnt);
				readPlus.putExtra("TPrswtusg", PrsWtUsg);
				readPlus.putExtra("THln", HLN);
				readPlus.putExtra("TSmcnt", Smcnt);
				readPlus.putExtra("TComment", Comment);
				readPlus.putExtra("SentAVG", Boolean.toString(SendAVGSt));
				readPlus.putExtra("SendOutSt", SendOutSt);
				readPlus.putExtra("OkRead", false);
				readPlus.putExtra("OkPrint", false);
				readPlus.putExtra("SendNotIn", false);

				startActivityForResult(readPlus, RESULT_REQUEST);

			}
		}

	}

	//Set SoundUPDown
	private void showNotification() {

		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
		r.play();
	}

	public void getPrsWtUsg() {

		if (LstMtrCnt == Est) {
			// การคำนวณปกติ
			PrsWtUsg = PrsMtrCnt - LstMtrCnt + OldMtrUsg;

			if (PrsMtrCnt < LstMtrCnt) {// มิเตอร์หมุนครบรอบ
				int nLstMtrCnt;
				nLstMtrCnt = (int) LstMtrCnt;
				if (String.valueOf(nLstMtrCnt).length() == 7) {
					//1000000 ขนาดมิเตอร์เป็น 1 เลขที่สูงสุดที่หมุนได้คือ 999999
					//ถ้าจะให้ดีต้องมีการ Comment ด้วยว่ามิเตอร์หมุนครบรอบ
					PrsWtUsg = (PrsMtrCnt + 10000000) - LstMtrCnt + OldMtrUsg;
				} else if (String.valueOf(nLstMtrCnt).length() == 6) {
					//1000000 ขนาดมิเตอร์เป็น 1 เลขที่สูงสุดที่หมุนได้คือ 999999
					//ถ้าจะให้ดีต้องมีการ Comment ด้วยว่ามิเตอร์หมุนครบรอบ
					PrsWtUsg = (PrsMtrCnt + 1000000) - LstMtrCnt + OldMtrUsg;
				} else if (String.valueOf(nLstMtrCnt).length() == 5) {
					//100000 ขนาดมิเตอร์เป็น 1 เลขที่สูงสุดที่หมุนได้คือ 99999
					//ถ้าจะให้ดีต้องมีการ Comment ด้วยว่ามิเตอร์หมุนครบรอบ
					PrsWtUsg = (PrsMtrCnt + 100000) - LstMtrCnt + OldMtrUsg;
				} else {
					//10000 ขนาดมิเตอร์เป็น 1 เลขที่สูงสุดที่หมุนได้คือ 9999
					//ถ้าจะให้ดีต้องมีการ Comment ด้วยว่ามิเตอร์หมุนครบรอบ
					PrsWtUsg = (PrsMtrCnt + 10000) - LstMtrCnt + OldMtrUsg;
				}

			} else if (PrsMtrCnt == LstMtrCnt) { //'มิเตอร์ไม่หมุน
				PrsWtUsg = 0 + OldMtrUsg;
			}
		} else if (LstMtrCnt < Est) {
			//' ตรวจสอบบ้านปิด
			if (PrsMtrCnt >= Est) {
				PrsWtUsg = PrsMtrCnt - Est + OldMtrUsg;
			} else if (PrsMtrCnt < Est) {
				PrsWtUsg = 0 + OldMtrUsg;
			}

		}

	}

	public void getPriceAll() {
		double tmpUsg;

		switch (controlmtr) {
			case "0"://ปกติ
				CalPriceNormal();
				break;
			case "1"://มาตรใหญ่
				CalBigmeter();
				break;
			case "2"://มาตรย่อย
				CalPriceNormal();
				break;
			case "3"://มาตรเคหะ
				tmpUsg = PrsWtUsg;
				CalHousing();
				PrsWtUsg = tmpUsg;
				break;
			case "4"://รัฐบาลรับภาระ 50 หน่วย
				tmpUsg = PrsWtUsg;
				// CalHousing();
				PrsWtUsg = tmpUsg;
				break;
			case "5"://รัฐบาลรับภาระ 30 หน่วย
				tmpUsg = PrsWtUsg;
				// CalHousing();
				PrsWtUsg = tmpUsg;
				break;
			case "6"://มาตรใหญ่ทหาร
				//CalBigMeterSold();
				break;
			default:
				break;
		}
	}

	private void CalPriceNormal() {
		// TODO Auto-generated method stub
		int count51, count52, count53, count54, count42;
		boolean isDisCntAmt = true;
		switch (usertype.substring(0, 1)) {
			case "1":
				if (PrsWtUsg >= 11) {

					count51 = db.getCountDbst51();
					Cursor mCursor51 = db.curDbst51();
					mCursor51.moveToFirst();
					for (int i = 0; i <= count51 - 1; i++) {
						if (PrsWtUsg >= mCursor51.getDouble(mCursor51.getColumnIndex("lowusgran"))
								&& PrsWtUsg <= mCursor51.getDouble(mCursor51.getColumnIndex("highusgran"))) {
							NorTrfwt = mCursor51.getDouble(mCursor51.getColumnIndex("acwttrfamt"));
							NorTrfwt += mCursor51.getDouble(mCursor51.getColumnIndex("wttrfrt"))
									* (PrsWtUsg - (mCursor51.getDouble(mCursor51.getColumnIndex("lowusgran")) - 1));
							break;
						}

						mCursor51.moveToNext();
					}
					LowPrice = 0;
					DiscntAmt = 0;
				} else {
					if (PrsWtUsg == 0) {
						NorTrfwt = 0;
						LowPrice = 0;
						DiscntAmt = 0;
						isDisCntAmt = false;
					} else {
						count51 = db.getCountDbst51();
						Cursor mCursor51 = db.curDbst51();
						mCursor51.moveToFirst();
						for (int i = 0; i <= count51 - 1; i++) {
							if (PrsWtUsg >= mCursor51.getDouble(mCursor51.getColumnIndex("lowusgran"))
									&& PrsWtUsg <= mCursor51.getDouble(mCursor51.getColumnIndex("highusgran"))) {
								NorTrfwt = mCursor51.getDouble(mCursor51.getColumnIndex("acwttrfamt"));
								NorTrfwt += mCursor51.getDouble(mCursor51.getColumnIndex("wttrfrt"))
										* (PrsWtUsg);
								break;
							}

							mCursor51.moveToNext();
						}
						LowPrice = 0;
						DiscntAmt = 0;
					}

				}
				break;
			case "2":
				if (usertype.substring(0, 2).equalsIgnoreCase("28") || usertype.substring(0, 2).equalsIgnoreCase("29")) {
					Log.d("ประเภท29", usertype);
					//ประเภท2829
					if (PrsWtUsg >= 11) {
						count54 = db.getCountDbst54();
						Cursor mCursor54 = db.curDbst54();
						mCursor54.moveToFirst();
						for (int i = 0; i <= count54 - 1; i++) {
							if (PrsWtUsg >= mCursor54.getDouble(mCursor54.getColumnIndex("lowusgran"))
									&& PrsWtUsg <= mCursor54.getDouble(mCursor54.getColumnIndex("highusgran"))) {
								NorTrfwt = mCursor54.getDouble(mCursor54.getColumnIndex("acwttrfamt"));
								NorTrfwt += mCursor54.getDouble(mCursor54.getColumnIndex("wttrfrt"))
										* (PrsWtUsg - (mCursor54.getDouble(mCursor54.getColumnIndex("lowusgran")) - 1));
								Log.d("NorTrfwt", String.valueOf(NorTrfwt));
								break;
							}
							mCursor54.moveToNext();
						}
						DiscntAmt = 0;
					} else {
						if (PrsWtUsg == 10) {
							count54 = db.getCountDbst54();
							Cursor mCursor54 = db.curDbst54();
							mCursor54.moveToFirst();
							for (int i = 0; i <= count54 - 1; i++) {
								if (PrsWtUsg >= mCursor54.getDouble(mCursor54.getColumnIndex("lowusgran"))
										&& PrsWtUsg <= mCursor54.getDouble(mCursor54.getColumnIndex("highusgran"))) {
									NorTrfwt = mCursor54.getDouble(mCursor54.getColumnIndex("acwttrfamt"));
									NorTrfwt += mCursor54.getDouble(mCursor54.getColumnIndex("wttrfrt"))
											* (PrsWtUsg);
									break;
								}

								mCursor54.moveToNext();
							}
							LowPrice = 0;
							DiscntAmt = 0;
						} else {
							NorTrfwt = 150;
							LowPrice = 150;
							DiscntAmt = 0;
						}
					}
				} else {

					if (PrsWtUsg >= 11) {
						count52 = db.getCountDbst52();
						Cursor mCursor52 = db.curDbst52();
						mCursor52.moveToFirst();
						for (int i = 0; i <= count52 - 1; i++) {
							if (PrsWtUsg >= mCursor52.getDouble(mCursor52.getColumnIndex("lowusgran"))
									&& PrsWtUsg <= mCursor52.getDouble(mCursor52.getColumnIndex("highusgran"))) {
								NorTrfwt = mCursor52.getDouble(mCursor52.getColumnIndex("acwttrfamt"));
								NorTrfwt += mCursor52.getDouble(mCursor52.getColumnIndex("wttrfrt"))
										* (PrsWtUsg - (mCursor52.getDouble(mCursor52.getColumnIndex("lowusgran")) - 1));
								Log.d("NorTrfwt", String.valueOf(NorTrfwt));
								break;
							}
							mCursor52.moveToNext();
						}
						DiscntAmt = 0;
					} else {
						if (PrsWtUsg == 10) {
							count52 = db.getCountDbst52();
							Cursor mCursor52 = db.curDbst52();
							mCursor52.moveToFirst();
							for (int i = 0; i <= count52 - 1; i++) {
								if (PrsWtUsg >= mCursor52.getDouble(mCursor52.getColumnIndex("lowusgran"))
										&& PrsWtUsg <= mCursor52.getDouble(mCursor52.getColumnIndex("highusgran"))) {
									NorTrfwt = mCursor52.getDouble(mCursor52.getColumnIndex("acwttrfamt"));
									NorTrfwt += mCursor52.getDouble(mCursor52.getColumnIndex("wttrfrt"))
											* (PrsWtUsg);
									break;
								}

								mCursor52.moveToNext();
							}
							LowPrice = 0;
							DiscntAmt = 0;
						} else {
							NorTrfwt = 150;
							LowPrice = 150;
							DiscntAmt = 0;
						}
					}
				}
				break;
			case "3":
				if (PrsWtUsg > MIN3) {

					count53 = db.getCountDbst53();
					Cursor mCursor53 = db.curDbst53();
					mCursor53.moveToFirst();
					for (int i = 0; i <= count53 - 1; i++) {
						if (PrsWtUsg >= mCursor53.getDouble(mCursor53.getColumnIndex("lowusgran"))
								&& PrsWtUsg <= mCursor53.getDouble(mCursor53.getColumnIndex("highusgran"))) {
							NorTrfwt = mCursor53.getDouble(mCursor53.getColumnIndex("acwttrfamt"));
							NorTrfwt += mCursor53.getDouble(mCursor53.getColumnIndex("wttrfrt"))
									* (PrsWtUsg - (mCursor53.getDouble(mCursor53.getColumnIndex("lowusgran")) - 1));
							Log.d("OK", String.valueOf(NorTrfwt));
							break;
						}
						mCursor53.moveToNext();
					}
					DiscntAmt = 0;
				} else {
					NorTrfwt = 300;
					LowPrice = 300;
					DiscntAmt = 0;
				}
				break;
		}


		if (DisCntCode.equalsIgnoreCase("1")) {
			if (usertype.substring(0, 1).equalsIgnoreCase("1") && PrsWtUsg > 100) {
				isDisCntAmt = false;
			}
		} else if (DisCntCode.equals("0")) {
			isDisCntAmt = false;
		} else if (DisCntCode.equalsIgnoreCase("A03")){//ลบ.ม.ละ 15 บาท ขั้นต่ำ 0
			isDisCntAmt = false;
			NorTrfwt = PrsWtUsg * 15;
		} else if (DisCntCode.equalsIgnoreCase("A01")){//ลบ.ม.ละ 21 บาท ขั้นต่ำ 0
			isDisCntAmt = false;
			NorTrfwt = PrsWtUsg * 21;
		} else if (DisCntCode.equalsIgnoreCase("A02")) {//ลบ.ม.ละ 19 บาท ขั้นต่ำ 0
			isDisCntAmt = false;
			NorTrfwt = PrsWtUsg * 19;
		}


		if (isDisCntAmt) {
			DiscntAmt = CalDiscount(NorTrfwt, PrsWtUsg);
		}

		if (DiscntAmt < 0) {
			Toast.makeText(this, "ส่วนลดติดลบ กรุณาตรวจสอบ", Toast.LENGTH_SHORT).show();
			return;
		}

		Log.d(",DiscntAmt",String.valueOf(DiscntAmt));

		//ค่าบริการ
		count42 = db.getCountDbst53();
		Cursor mCursor42 = db.curDbst42();
		mCursor42.moveToFirst();
		for (int i = 0; i < count42 - 1; i++) {
			if (MeterSize.equals(mCursor42.getString(mCursor42.getColumnIndex("metersize")))) {
				SrvFee = mCursor42.getDouble(mCursor42.getColumnIndex("srvfee"));
				Log.d("Service:", String.valueOf(SrvFee));
				break;
			}
			mCursor42.moveToNext();
		}

		Log.d("Disc:", String.valueOf(DisCntCode));
		if (DisCntCode.equals("C") || DisCntCode.equals("K") || DisCntCode.equals("Z")) {
			DiscntAmt = 0;
			NetTrfWt = NorTrfwt;
		} else if (DisCntCode.equals("J")) {
			NetTrfWt = NorTrfwt;
		} else if (DisCntCode.equalsIgnoreCase("4")) {

			NetTrfWt = NorTrfwt - DiscntAmt;

			if (NetTrfWt < 0) {
				NetTrfWt = 0;
			}

			if (PrsWtUsg <= 20) {
				//if (NetTrfWt < 0){
					NetTrfWt = 0;
					DiscntAmt = NorTrfwt;
				//}
			}
		} else {
			NetTrfWt = NorTrfwt - DiscntAmt;
		}

		//ตรวจสอบ ServiceFlag 1=คิดค่าบริการ ,0 = ไม่คิดค่าบริการ

		if (ServiceFlag.equalsIgnoreCase("0")) {
			SrvFee = 0.0;
			Toast.makeText(this, "ไม่คิดค่าบริการ", Toast.LENGTH_LONG).show();
		}

		if (tmpdupamt > 0) {
			Vat = (NetTrfWt + SrvFee) * 0.07;
			Vat = (double) Math.round(Vat * 100) / 100;


			DecimalFormat form = new DecimalFormat("0.00");
			debtvat = Double.parseDouble(form.format(debtvat));
			Vat = Double.parseDouble(form.format(Vat));

			TotTrfwt = NetTrfWt + SrvFee + Vat;
			Log.d("==>TotTrfwt:", String.valueOf(TotTrfwt));

			if (tmpdupamt > TotTrfwt) //รับซ้ำมากกว่าค่าน้ำปัจจุบัน
			{
				//Log.d("ค่าน้ำรับซ้ำมากกว่าค่าน้ำปัจจุบัน", String.valueOf(tmpdupamt));
				debtnet = (NetTrfWt + SrvFee);
				debtvat = (NetTrfWt + SrvFee) * 0.07;
				debtamt = debtnet + debtvat;
				dupamt = tmpdupamt - debtamt;
				dupvat = (dupamt * 7) / 107;
				dupnet = dupamt - dupvat;
				Vat = 0.0;
				TotTrfwt = 0.0;

			} else if (tmpdupamt == TotTrfwt) { //รับซ้ำเท่ากับค่าน้ำปัจจุบัน
				//Toast.makeText(this, "ยอดเงินรวม : " + Vat, Toast.LENGTH_SHORT).show();
				//Log.d("ค่าน้ำรับซ้ำเท่ากับค่าน้ำปัจจุบัน", String.valueOf(tmpdupamt));
				debtnet = (NetTrfWt + SrvFee);
				debtvat = (NetTrfWt + SrvFee) * 0.07;
				debtamt = debtnet + debtvat;

				Vat = 0.0;
				TotTrfwt = 0.0;
				dupnet = 0.0;
				dupvat = 0.0;
				dupamt = 0.0;

			} else { //รับซ้ำน้อยกว่าค่าน้ำปัจจะบัน
				//Log.d("ค่าน้ำรับซ้ำน้อยกว่าค่าน้ำปัจจุบัน", String.valueOf(tmpdupamt));
				debtnet = (NetTrfWt + SrvFee);
				debtvat = (NetTrfWt + SrvFee) * 0.07;
				debtamt = debtnet + debtvat;

				TotTrfwt = debtamt - tmpdupamt;

				Vat = ((TotTrfwt) * 7) / 107;
				Vat = (double) Math.round(Vat * 100) / 100;
				DecimalFormat formt = new DecimalFormat("0.00");
				Vat = Double.parseDouble(formt.format(Vat));

				dupnet = 0.0;
				dupvat = 0.0;
				dupamt = 0.0;
			}
		} else {
			//คำนวณแบบปกติ
			DecimalFormat form = new DecimalFormat("0.00");
			Vat = (NetTrfWt + SrvFee) * 0.07;
			Vat = (double) Math.round(Vat * 100) / 100;
			Vat = Double.parseDouble(form.format(Vat));
			TotTrfwt = NetTrfWt + SrvFee + Vat;

			debtamt = TotTrfwt;
			debtnet = (NetTrfWt + SrvFee);
			debtvat = (NetTrfWt + SrvFee) * 0.07;
			debtvat = Vat;
//				showDialog(String.valueOf(debtamt));
//				showDialog(String.valueOf(debtnet));
//				showDialog(String.valueOf(debtvat));
		}

		if (PwaFlag.equalsIgnoreCase("4")) {
			String TempDisPwaPro = "";
			TempDisPwaPro = db.getDisPwaPro();
			if (TotTrfwt >= Double.parseDouble(TempDisPwaPro)) {
				Log.d("เข้าเคสมากกว่า ", PwaFlag);
				debtamt = TotTrfwt;
				debtnet = (NetTrfWt + SrvFee);
				debtvat = Vat;
				TotTrfwt = debtamt - Double.parseDouble(TempDisPwaPro);
				Vat = (TotTrfwt * 7) / 107;
				Vat = (double) Math.round(Vat * 100) / 100;
				DecimalFormat formt = new DecimalFormat("0.00");
				Vat = Double.parseDouble(formt.format(Vat));
				distempamt = Double.parseDouble(TempDisPwaPro);
				distempvat = (distempamt * 7) / 107;
				distempnet = distempamt - distempvat;
			} else {
				Log.d("เข้าเคส=====> น้อยกว่า ", PwaFlag);
				debtamt = TotTrfwt;
				if (DisCntCode.equalsIgnoreCase("4")) {
					NetTrfWt = (NetTrfWt + SrvFee);
					debtnet = NetTrfWt;
				} else {
					debtnet = (NetTrfWt + SrvFee);
				}
				debtvat = Vat;
				TotTrfwt = 0.00;
				Vat = 0.00;
				distempamt = debtamt;
				distempnet = (distempamt * 100) / 107;
				distempvat = distempamt - distempnet;
			}
		}

		Log.d("Net:", String.format("%.2f", NetTrfWt));
		Log.d("Service:", String.format("%.2f", SrvFee));
		Log.d("vat:", String.format("%.2f", Vat));
		Log.d("TotTrfwt:", String.format("%.2f", TotTrfwt));

	}

	private void CalHousingA() {
		// TODO Auto-generated method stub
		Log.d("CalHouing", "housing");

		double tmpUsg, PrsWtUsg2, wttr = 0;
		int count51, count52, count53, count54, count42;
		boolean isDisCntAmt = true;

		tmpUsg = PrsWtUsg;
		//หน่วยน้ำ หาร ด้วย จำนวนบ้านเคหะ
		PrsWtUsg2 = PrsWtUsg / NoofHouse;
		PrsWtUsg2 = Math.round(PrsWtUsg2);


		switch (usertype.substring(0, 1)) {
			case "1":
				if (PrsWtUsg >= 11) {

					count51 = db.getCountDbst51();
					Cursor mCursor51 = db.curDbst51();
					mCursor51.moveToFirst();
					for (int i = 0; i <= count51 - 1; i++) {
						if (PrsWtUsg >= mCursor51.getDouble(mCursor51.getColumnIndex("lowusgran"))
								&& PrsWtUsg <= mCursor51.getDouble(mCursor51.getColumnIndex("highusgran"))) {
							NorTrfwt = mCursor51.getDouble(mCursor51.getColumnIndex("acwttrfamt"));
							wttr = mCursor51.getDouble(mCursor51.getColumnIndex("wttrfrt"));
							NorTrfwt += mCursor51.getDouble(mCursor51.getColumnIndex("wttrfrt"))
									* (PrsWtUsg - (mCursor51.getDouble(mCursor51.getColumnIndex("lowusgran")) - 1));
							break;
						}

						mCursor51.moveToNext();
					}
					LowPrice = 0;
					DiscntAmt = 0;
				} else {
					if (PrsWtUsg == 0) {
						NorTrfwt = 0;
						LowPrice = 0;
						DiscntAmt = 0;
						isDisCntAmt = false;
					} else {
						count51 = db.getCountDbst51();
						Cursor mCursor51 = db.curDbst51();
						mCursor51.moveToFirst();
						for (int i = 0; i <= count51 - 1; i++) {
							if (PrsWtUsg >= mCursor51.getDouble(mCursor51.getColumnIndex("lowusgran"))
									&& PrsWtUsg <= mCursor51.getDouble(mCursor51.getColumnIndex("highusgran"))) {
								NorTrfwt = mCursor51.getDouble(mCursor51.getColumnIndex("acwttrfamt"));
								wttr = mCursor51.getDouble(mCursor51.getColumnIndex("wttrfrt"));
								NorTrfwt += mCursor51.getDouble(mCursor51.getColumnIndex("wttrfrt"))
										* (PrsWtUsg);
								break;
							}

							mCursor51.moveToNext();
						}
						LowPrice = 0;
						DiscntAmt = 0;
					}

				}
				break;
			case "2":
				if (usertype.substring(0, 2).equalsIgnoreCase("28") || usertype.substring(0, 2).equalsIgnoreCase("29")) {
					Log.d("ประเภท29", usertype);
					//ประเภท2829
					if (PrsWtUsg >= 11) {
						count54 = db.getCountDbst54();
						Cursor mCursor54 = db.curDbst54();
						mCursor54.moveToFirst();
						for (int i = 0; i <= count54 - 1; i++) {
							if (PrsWtUsg >= mCursor54.getDouble(mCursor54.getColumnIndex("lowusgran"))
									&& PrsWtUsg <= mCursor54.getDouble(mCursor54.getColumnIndex("highusgran"))) {
								NorTrfwt = mCursor54.getDouble(mCursor54.getColumnIndex("acwttrfamt"));
								wttr = mCursor54.getDouble(mCursor54.getColumnIndex("wttrfrt"));
								NorTrfwt += mCursor54.getDouble(mCursor54.getColumnIndex("wttrfrt"))
										* (PrsWtUsg - (mCursor54.getDouble(mCursor54.getColumnIndex("lowusgran")) - 1));
								Log.d("NorTrfwt", String.valueOf(NorTrfwt));
								break;
							}
							mCursor54.moveToNext();
						}
						DiscntAmt = 0;
					} else {
						if (PrsWtUsg == 10) {
							count52 = db.getCountDbst52();
							Cursor mCursor52 = db.curDbst52();
							mCursor52.moveToFirst();
							for (int i = 0; i <= count52 - 1; i++) {
								if (PrsWtUsg >= mCursor52.getDouble(mCursor52.getColumnIndex("lowusgran"))
										&& PrsWtUsg <= mCursor52.getDouble(mCursor52.getColumnIndex("highusgran"))) {
									NorTrfwt = mCursor52.getDouble(mCursor52.getColumnIndex("acwttrfamt"));
									wttr = mCursor52.getDouble(mCursor52.getColumnIndex("wttrfrt"));
									NorTrfwt += mCursor52.getDouble(mCursor52.getColumnIndex("wttrfrt"))
											* (PrsWtUsg);
									break;
								}

								mCursor52.moveToNext();
							}
							LowPrice = 0;
							DiscntAmt = 0;
						} else {
							NorTrfwt = 150;
							LowPrice = 150;
							DiscntAmt = 0;
						}
					}
				} else {

					if (PrsWtUsg >= 11) {
						count52 = db.getCountDbst52();
						Cursor mCursor52 = db.curDbst52();
						mCursor52.moveToFirst();
						for (int i = 0; i <= count52 - 1; i++) {
							if (PrsWtUsg >= mCursor52.getDouble(mCursor52.getColumnIndex("lowusgran"))
									&& PrsWtUsg <= mCursor52.getDouble(mCursor52.getColumnIndex("highusgran"))) {
								NorTrfwt = mCursor52.getDouble(mCursor52.getColumnIndex("acwttrfamt"));
								wttr = mCursor52.getDouble(mCursor52.getColumnIndex("wttrfrt"));
								NorTrfwt += mCursor52.getDouble(mCursor52.getColumnIndex("wttrfrt"))
										* (PrsWtUsg - (mCursor52.getDouble(mCursor52.getColumnIndex("lowusgran")) - 1));
								Log.d("NorTrfwt", String.valueOf(NorTrfwt));
								break;
							}
							mCursor52.moveToNext();
						}
						DiscntAmt = 0;
					} else {
						if (PrsWtUsg == 10) {
							count52 = db.getCountDbst52();
							Cursor mCursor52 = db.curDbst52();
							mCursor52.moveToFirst();
							for (int i = 0; i <= count52 - 1; i++) {
								if (PrsWtUsg >= mCursor52.getDouble(mCursor52.getColumnIndex("lowusgran"))
										&& PrsWtUsg <= mCursor52.getDouble(mCursor52.getColumnIndex("highusgran"))) {
									NorTrfwt = mCursor52.getDouble(mCursor52.getColumnIndex("acwttrfamt"));
									wttr = mCursor52.getDouble(mCursor52.getColumnIndex("wttrfrt"));
									NorTrfwt += mCursor52.getDouble(mCursor52.getColumnIndex("wttrfrt"))
											* (PrsWtUsg);
									break;
								}

								mCursor52.moveToNext();
							}
							LowPrice = 0;
							DiscntAmt = 0;
						} else {
							NorTrfwt = 150;
							LowPrice = 150;
							DiscntAmt = 0;
						}
					}
				}
				break;
			case "3":
				if (PrsWtUsg > MIN3) {

					count53 = db.getCountDbst53();
					Cursor mCursor53 = db.curDbst53();
					mCursor53.moveToFirst();
					for (int i = 0; i <= count53 - 1; i++) {
						if (PrsWtUsg >= mCursor53.getDouble(mCursor53.getColumnIndex("lowusgran"))
								&& PrsWtUsg <= mCursor53.getDouble(mCursor53.getColumnIndex("highusgran"))) {
							NorTrfwt = mCursor53.getDouble(mCursor53.getColumnIndex("acwttrfamt"));
							wttr = mCursor53.getDouble(mCursor53.getColumnIndex("wttrfrt"));
							NorTrfwt += mCursor53.getDouble(mCursor53.getColumnIndex("wttrfrt"))
									* (PrsWtUsg - (mCursor53.getDouble(mCursor53.getColumnIndex("lowusgran")) - 1));
							Log.d("OK", String.valueOf(NorTrfwt));
							break;
						}
						mCursor53.moveToNext();
					}
					DiscntAmt = 0;
				} else {
					NorTrfwt = 300;
					LowPrice = 300;
					DiscntAmt = 0;
				}
				break;
		}


		if (DisCntCode.equalsIgnoreCase("1")) {
			if (usertype.substring(0, 1).equalsIgnoreCase("1") && PrsWtUsg > 100) {
				isDisCntAmt = false;
			}
		} else if (DisCntCode.equals("0")) {
			isDisCntAmt = false;
		} else if (DisCntCode.equalsIgnoreCase("A03")){//ลบ.ม.ละ 15 บาท ขั้นต่ำ 0
			isDisCntAmt = false;
			NorTrfwt = PrsWtUsg * 15;
		} else if (DisCntCode.equalsIgnoreCase("A01")){//ลบ.ม.ละ 21 บาท ขั้นต่ำ 0
			isDisCntAmt = false;
			NorTrfwt = PrsWtUsg * 21;
		} else if (DisCntCode.equalsIgnoreCase("A02")) {//ลบ.ม.ละ 19 บาท ขั้นต่ำ 0
			isDisCntAmt = false;
			NorTrfwt = PrsWtUsg * 19;
		}

		if (isDisCntAmt) {
			DiscntAmt = CalDiscount(NorTrfwt, PrsWtUsg);
		}

		if (DiscntAmt < 0) {
			Toast.makeText(this, "ส่วนลดติดลบ กรุณาตรวจสอบ", Toast.LENGTH_SHORT).show();
			return;
		}

		count42 = db.getCountDbst53();
		Cursor mCursor42 = db.curDbst42();
		mCursor42.moveToFirst();
		for (int i = 0; i < count42 - 1; i++) {
			if (MeterSize.equals(mCursor42.getString(mCursor42.getColumnIndex("metersize")))) {
				SrvFee = mCursor42.getDouble(mCursor42.getColumnIndex("srvfee"));
				Log.d("Service:", String.valueOf(SrvFee));
				break;
			}
			mCursor42.moveToNext();
		}

		//ตรวจสอบ ServiceFlag 1=คิดค่าบริการ ,0 = ไม่คิดค่าบริการ

		if (ServiceFlag.equalsIgnoreCase("0")) {
			SrvFee = 0.0;
			Toast.makeText(this, "ไม่คิดค่าบริการ", Toast.LENGTH_LONG).show();
		}

		Log.d("==>Disc:", String.valueOf(DisCntCode));
		if (DisCntCode.equalsIgnoreCase("C") || DisCntCode.equalsIgnoreCase("K") || DisCntCode.equalsIgnoreCase("Z")) {
			DiscntAmt = 0;
			NetTrfWt = wttr * tmpUsg;
		} else if (DisCntCode.equalsIgnoreCase("J")) {
			NetTrfWt = wttr * tmpUsg;
		} else {
			NetTrfWt = wttr * tmpUsg;
			Log.d("==>Net:", String.valueOf(NetTrfWt));
		}

		//ศรีสะเกษไม่มีส่วนลดเคหะ 22/02/2562
				if (NoofHouse > 1) //คำนวณแบบบ้านการเคหะ
				{
					DiscntAmt = NorTrfwt - NetTrfWt;
				}


		Vat = (NetTrfWt + SrvFee) * 0.07;
		Vat = (double) Math.round(Vat * 100) / 100;

		//Toast.makeText(this, "ยอดเงินรวม : " + Vat, Toast.LENGTH_SHORT).show();

		DecimalFormat form = new DecimalFormat("0.00");
		Vat = Double.parseDouble(form.format(Vat));
		Log.d("Net:", String.format("%.2f", NetTrfWt));
		Log.d("Service:", String.format("%.2f", SrvFee));
		Log.d("vat:", String.format("%.2f", Vat));


		if (DisCntCode.equalsIgnoreCase(" ")) {
			TotTrfwt = NetTrfWt + SrvFee + Vat;
		} else {
			TotTrfwt = NetTrfWt + SrvFee + Vat;
		}
		//Log.d("Tottrfwt:",String.valueOf(TotTrfwt));
		// Toast.makeText(this, "ยอดเงินรวม : " + String.format("%.2f", TotTrfwt), Toast.LENGTH_SHORT).show();
		if (PwaFlag.equalsIgnoreCase("4")) {
			String TempDisPwaPro = "";
			TempDisPwaPro = db.getDisPwaPro();
			if (TotTrfwt >= Double.parseDouble(TempDisPwaPro)) {
				Log.d("เข้าเคส=====> มากกว่า ", PwaFlag);
				debtamt = TotTrfwt;
				debtnet = (NetTrfWt + SrvFee);
				debtvat = Vat;
				TotTrfwt = debtamt - Double.parseDouble(TempDisPwaPro);
				Vat = (TotTrfwt * 7) / 107;
				Vat = (double) Math.round(Vat * 100) / 100;
				DecimalFormat formt = new DecimalFormat("0.00");
				Vat = Double.parseDouble(formt.format(Vat));
				distempamt = Double.parseDouble(TempDisPwaPro);
				distempvat = (distempamt * 7) / 107;
				distempnet = distempamt - distempvat;
			} else {
				Log.d("เข้าเคส=====> น้อยกว่า ", PwaFlag);
				debtamt = TotTrfwt;
				if (DisCntCode.equalsIgnoreCase("4")) {
					NetTrfWt = (NetTrfWt + SrvFee);
					debtnet = NetTrfWt;
				} else {
					debtnet = (NetTrfWt + SrvFee);
				}
				debtvat = Vat;
				TotTrfwt = 0.00;
				Vat = 0.00;
				distempamt = debtamt;
				distempnet = (distempamt * 100) / 107;
				distempvat = distempamt - distempnet;
			}
		}

		Log.d("Net:", String.format("%.2f", NetTrfWt));
		Log.d("Service:", String.format("%.2f", SrvFee));
		Log.d("vat:", String.format("%.2f", Vat));
		Log.d("TotTrfwt:", String.format("%.2f", TotTrfwt));

	}

	private void CalHousing() {

		Log.d("CalHouing", "housing");

		double tmpUsg, PrsWtUsg2, wttr = 0;
		int count51, count52, count53, count54, count42;
		boolean isDisCntAmt = true;

		tmpUsg = PrsWtUsg;

		NorTrfwt = tmpUsg * 10.20;


		if (DisCntCode.equalsIgnoreCase("1")) {
			if (usertype.substring(0, 1).equalsIgnoreCase("1") && PrsWtUsg > 100) {
				isDisCntAmt = false;
			}
		} else if (DisCntCode.equals("0")) {
			isDisCntAmt = false;
		} else if (DisCntCode.equalsIgnoreCase("A03")){//ลบ.ม.ละ 15 บาท ขั้นต่ำ 0
			isDisCntAmt = false;
			NorTrfwt = PrsWtUsg * 15;
		} else if (DisCntCode.equalsIgnoreCase("A01")){//ลบ.ม.ละ 21 บาท ขั้นต่ำ 0
			isDisCntAmt = false;
			NorTrfwt = PrsWtUsg * 21;
		} else if (DisCntCode.equalsIgnoreCase("A02")) {//ลบ.ม.ละ 19 บาท ขั้นต่ำ 0
			isDisCntAmt = false;
			NorTrfwt = PrsWtUsg * 19;
		}

		if (isDisCntAmt) {
			DiscntAmt = CalDiscount(NorTrfwt, PrsWtUsg);
		}

		if (DiscntAmt < 0) {
			Toast.makeText(this, "ส่วนลดติดลบ กรุณาตรวจสอบ", Toast.LENGTH_SHORT).show();
			return;
		}

		count42 = db.getCountDbst53();
		Cursor mCursor42 = db.curDbst42();
		mCursor42.moveToFirst();
		for (int i = 0; i < count42 - 1; i++) {
			if (MeterSize.equals(mCursor42.getString(mCursor42.getColumnIndex("metersize")))) {
				SrvFee = mCursor42.getDouble(mCursor42.getColumnIndex("srvfee"));
				Log.d("Service:", String.valueOf(SrvFee));
				break;
			}
			mCursor42.moveToNext();
		}

		//ตรวจสอบ ServiceFlag 1=คิดค่าบริการ ,0 = ไม่คิดค่าบริการ

		if (ServiceFlag.equalsIgnoreCase("0")) {
			SrvFee = 0.0;
			Toast.makeText(this, "ไม่คิดค่าบริการ", Toast.LENGTH_LONG).show();
		}

		Log.d("Disc:", String.valueOf(DisCntCode));
		if (DisCntCode.equals("C") || DisCntCode.equals("K") || DisCntCode.equals("Z")) {
			DiscntAmt = 0;
			NetTrfWt = NorTrfwt;
		} else if (DisCntCode.equals("J")) {
			NetTrfWt = NorTrfwt;
		} else if (DisCntCode.equalsIgnoreCase("4")) {

			NetTrfWt = NorTrfwt - DiscntAmt;

			if (NetTrfWt < 0) {
				NetTrfWt = 0;
			}

			if (PrsWtUsg <= 20) {
				NetTrfWt = 0;
				DiscntAmt = NorTrfwt;
			}
		} else {
			NetTrfWt = NorTrfwt - DiscntAmt;
			Log.d("==>Net:", String.valueOf(NetTrfWt));
		}



		Vat = (NetTrfWt + SrvFee) * 0.07;
		Vat = (double) Math.round(Vat * 100) / 100;

		//Toast.makeText(this, "ยอดเงินรวม : " + Vat, Toast.LENGTH_SHORT).show();

		DecimalFormat form = new DecimalFormat("0.00");
		Vat = Double.parseDouble(form.format(Vat));
		Log.d("Net:", String.format("%.2f", NetTrfWt));
		Log.d("Service:", String.format("%.2f", SrvFee));
		Log.d("vat:", String.format("%.2f", Vat));


		if (DisCntCode.equalsIgnoreCase(" ")) {
			TotTrfwt = NetTrfWt + SrvFee + Vat;
		} else {
			TotTrfwt = NetTrfWt + SrvFee + Vat;
		}
		//Log.d("Tottrfwt:",String.valueOf(TotTrfwt));
		// Toast.makeText(this, "ยอดเงินรวม : " + String.format("%.2f", TotTrfwt), Toast.LENGTH_SHORT).show();
		if (PwaFlag.equalsIgnoreCase("4")) {
			String TempDisPwaPro = "";
			TempDisPwaPro = db.getDisPwaPro();
			if (TotTrfwt >= Double.parseDouble(TempDisPwaPro)) {
				Log.d("เข้าเคส=====> มากกว่า ", PwaFlag);
				debtamt = TotTrfwt;
				debtnet = (NetTrfWt + SrvFee);
				debtvat = Vat;
				TotTrfwt = debtamt - Double.parseDouble(TempDisPwaPro);
				Vat = (TotTrfwt * 7) / 107;
				Vat = (double) Math.round(Vat * 100) / 100;
				DecimalFormat formt = new DecimalFormat("0.00");
				Vat = Double.parseDouble(formt.format(Vat));
				distempamt = Double.parseDouble(TempDisPwaPro);
				distempvat = (distempamt * 7) / 107;
				distempnet = distempamt - distempvat;
			} else {
				Log.d("เข้าเคส=====> น้อยกว่า ", PwaFlag);
				debtamt = TotTrfwt;
				if (DisCntCode.equalsIgnoreCase("4")) {
					NetTrfWt = (NetTrfWt + SrvFee);
					debtnet = NetTrfWt;
				} else {
					debtnet = (NetTrfWt + SrvFee);
				}
				debtvat = Vat;
				TotTrfwt = 0.00;
				Vat = 0.00;
				distempamt = debtamt;
				distempnet = (distempamt * 100) / 107;
				distempvat = distempamt - distempnet;
			}
		}

		Log.d("Net:", String.format("%.2f", NetTrfWt));
		Log.d("Service:", String.format("%.2f", SrvFee));
		Log.d("vat:", String.format("%.2f", Vat));
		Log.d("TotTrfwt:", String.format("%.2f", TotTrfwt));

	}

	private void CalBigmeter() {

		// TODO Auto-generated method stub
		int count51, count52, count53, count54, count42;


		switch (usertype.substring(0, 1)) {
			case "1":
				if (PrsWtUsg >= 11) {

					count51 = db.getCountDbst51();
					Cursor mCursor51 = db.curDbst51();
					mCursor51.moveToFirst();
					for (int i = 0; i <= count51 - 1; i++) {
						if (PrsWtUsg >= mCursor51.getDouble(mCursor51.getColumnIndex("lowusgran"))
								&& PrsWtUsg <= mCursor51.getDouble(mCursor51.getColumnIndex("highusgran"))) {
							NorTrfwt = mCursor51.getDouble(mCursor51.getColumnIndex("acwttrfamt"));
							NorTrfwt += mCursor51.getDouble(mCursor51.getColumnIndex("wttrfrt"))
									* (PrsWtUsg - (mCursor51.getDouble(mCursor51.getColumnIndex("lowusgran")) - 1));
							break;
						}

						mCursor51.moveToNext();
					}
					LowPrice = 0;
					DiscntAmt = 0;
				} else {
					if (PrsWtUsg == 0) {
						NorTrfwt = 0;
						LowPrice = 0;
						DiscntAmt = 0;
					} else {
						count51 = db.getCountDbst51();
						Cursor mCursor51 = db.curDbst51();
						mCursor51.moveToFirst();
						for (int i = 0; i <= count51 - 1; i++) {
							if (PrsWtUsg >= mCursor51.getDouble(mCursor51.getColumnIndex("lowusgran"))
									&& PrsWtUsg <= mCursor51.getDouble(mCursor51.getColumnIndex("highusgran"))) {
								NorTrfwt = mCursor51.getDouble(mCursor51.getColumnIndex("acwttrfamt"));
								NorTrfwt += mCursor51.getDouble(mCursor51.getColumnIndex("wttrfrt"))
										* (PrsWtUsg);
								break;
							}

							mCursor51.moveToNext();
						}
						LowPrice = 0;
						DiscntAmt = 0;
					}

				}
				break;
			case "2":
				if (usertype.substring(0, 2).equalsIgnoreCase("28") || usertype.substring(0, 2).equalsIgnoreCase("29")) {
					Log.d("ประเภท29", usertype);
					//ประเภท2829
					if (PrsWtUsg >= 11) {
						count54 = db.getCountDbst54();
						Cursor mCursor54 = db.curDbst54();
						mCursor54.moveToFirst();
						for (int i = 0; i <= count54 - 1; i++) {
							if (PrsWtUsg >= mCursor54.getDouble(mCursor54.getColumnIndex("lowusgran"))
									&& PrsWtUsg <= mCursor54.getDouble(mCursor54.getColumnIndex("highusgran"))) {
								NorTrfwt = mCursor54.getDouble(mCursor54.getColumnIndex("acwttrfamt"));
								NorTrfwt += mCursor54.getDouble(mCursor54.getColumnIndex("wttrfrt"))
										* (PrsWtUsg - (mCursor54.getDouble(mCursor54.getColumnIndex("lowusgran")) - 1));
								Log.d("NorTrfwt", String.valueOf(NorTrfwt));
								break;
							}
							mCursor54.moveToNext();
						}
						DiscntAmt = 0;
					} else {
						if (PrsWtUsg == 10) {
							count52 = db.getCountDbst52();
							Cursor mCursor52 = db.curDbst52();
							mCursor52.moveToFirst();
							for (int i = 0; i <= count52 - 1; i++) {
								if (PrsWtUsg >= mCursor52.getDouble(mCursor52.getColumnIndex("lowusgran"))
										&& PrsWtUsg <= mCursor52.getDouble(mCursor52.getColumnIndex("highusgran"))) {
									NorTrfwt = mCursor52.getDouble(mCursor52.getColumnIndex("acwttrfamt"));
									NorTrfwt += mCursor52.getDouble(mCursor52.getColumnIndex("wttrfrt"))
											* (PrsWtUsg);
									break;
								}

								mCursor52.moveToNext();
							}
							LowPrice = 0;
							DiscntAmt = 0;
						} else {
							NorTrfwt = 150;
							LowPrice = 150;
							DiscntAmt = 0;
						}
					}

				} else {
					Log.d("ประเภท2ปกติ", usertype);
					if (PrsWtUsg >= 11) {
						count52 = db.getCountDbst52();
						Cursor mCursor52 = db.curDbst52();
						mCursor52.moveToFirst();
						for (int i = 0; i <= count52 - 1; i++) {
							if (PrsWtUsg >= mCursor52.getDouble(mCursor52.getColumnIndex("lowusgran"))
									&& PrsWtUsg <= mCursor52.getDouble(mCursor52.getColumnIndex("highusgran"))) {
								NorTrfwt = mCursor52.getDouble(mCursor52.getColumnIndex("acwttrfamt"));
								NorTrfwt += mCursor52.getDouble(mCursor52.getColumnIndex("wttrfrt"))
										* (PrsWtUsg - (mCursor52.getDouble(mCursor52.getColumnIndex("lowusgran")) - 1));
								Log.d("NorTrfwt", String.valueOf(NorTrfwt));
								break;
							}
							mCursor52.moveToNext();
						}
						DiscntAmt = 0;
					} else {
						if (PrsWtUsg == 10) {
							count52 = db.getCountDbst52();
							Cursor mCursor52 = db.curDbst52();
							mCursor52.moveToFirst();
							for (int i = 0; i <= count52 - 1; i++) {
								if (PrsWtUsg >= mCursor52.getDouble(mCursor52.getColumnIndex("lowusgran"))
										&& PrsWtUsg <= mCursor52.getDouble(mCursor52.getColumnIndex("highusgran"))) {
									NorTrfwt = mCursor52.getDouble(mCursor52.getColumnIndex("acwttrfamt"));
									NorTrfwt += mCursor52.getDouble(mCursor52.getColumnIndex("wttrfrt"))
											* (PrsWtUsg);
									break;
								}

								mCursor52.moveToNext();
							}
							LowPrice = 0;
							DiscntAmt = 0;
						} else {
							NorTrfwt = 150;
							LowPrice = 150;
							DiscntAmt = 0;
						}
					}
				}
				break;
			case "3":
				if (PrsWtUsg > MIN3) {

					count53 = db.getCountDbst53();
					Cursor mCursor53 = db.curDbst53();
					mCursor53.moveToFirst();
					for (int i = 0; i <= count53 - 1; i++) {
						if (PrsWtUsg >= mCursor53.getDouble(mCursor53.getColumnIndex("lowusgran"))
								&& PrsWtUsg <= mCursor53.getDouble(mCursor53.getColumnIndex("highusgran"))) {
							NorTrfwt = mCursor53.getDouble(mCursor53.getColumnIndex("acwttrfamt"));
							NorTrfwt += mCursor53.getDouble(mCursor53.getColumnIndex("wttrfrt"))
									* (PrsWtUsg - (mCursor53.getDouble(mCursor53.getColumnIndex("lowusgran")) - 1));
							Log.d("OK", String.valueOf(NorTrfwt));
							break;
						}
						mCursor53.moveToNext();
					}
					DiscntAmt = 0;
				} else {
					NorTrfwt = 300;
					LowPrice = 300;
					DiscntAmt = 0;
				}
				break;
		}

		boolean isDisCntAmt = true;

		if (DisCntCode.equalsIgnoreCase("1")) {
			if (usertype.substring(0, 1).equalsIgnoreCase("1") && PrsWtUsg > 100) {
				isDisCntAmt = false;
			}
		} else if (DisCntCode.equals("0")) {
			isDisCntAmt = false;
		} else if (DisCntCode.equalsIgnoreCase("A03")){//ลบ.ม.ละ 15 บาท ขั้นต่ำ 0
			isDisCntAmt = false;
			NorTrfwt = PrsWtUsg * 15;
		} else if (DisCntCode.equalsIgnoreCase("A01")){//ลบ.ม.ละ 21 บาท ขั้นต่ำ 0
			isDisCntAmt = false;
			NorTrfwt = PrsWtUsg * 21;
		} else if (DisCntCode.equalsIgnoreCase("A02")) {//ลบ.ม.ละ 19 บาท ขั้นต่ำ 0
			isDisCntAmt = false;
			NorTrfwt = PrsWtUsg * 19;
		}

		if (isDisCntAmt) {
			DiscntAmt = CalDiscount(NorTrfwt, PrsWtUsg);
		}

		if (DiscntAmt < 0) {
			Toast.makeText(this, "ส่วนลดติดลบ กรุณาตรวจสอบ", Toast.LENGTH_SHORT).show();
			return;
		}

		count42 = db.getCountDbst53();
		Cursor mCursor42 = db.curDbst42();
		mCursor42.moveToFirst();
		for (int i = 0; i < count42 - 1; i++) {
			if (MeterSize.equals(mCursor42.getString(mCursor42.getColumnIndex("metersize")))) {
				SrvFee = mCursor42.getDouble(mCursor42.getColumnIndex("srvfee"));
				Log.d("Service:", String.valueOf(SrvFee));
				break;
			}
			mCursor42.moveToNext();
		}

		//ตรวจสอบ ServiceFlag 1=คิดค่าบริการ ,0 = ไม่คิดค่าบริการ

		if (ServiceFlag.equalsIgnoreCase("0")) {
			SrvFee = 0.0;
			Toast.makeText(this, "ไม่คิดค่าบริการ", Toast.LENGTH_LONG).show();
		}

		Log.d("==>Disc:", String.valueOf(DisCntCode));
		if (DisCntCode.equals("C") || DisCntCode.equals("K") || DisCntCode.equals("Z")) {
			DiscntAmt = 0;
			NetTrfWt = NorTrfwt;
		} else if (DisCntCode.equals("J")) {
			NetTrfWt = NorTrfwt;
		} else if (DisCntCode.equalsIgnoreCase("4")) {

			NetTrfWt = NorTrfwt - DiscntAmt;

			if (NetTrfWt < 0) {
				NetTrfWt = 0;
			}

			if (PrsWtUsg <= 20) {
				NetTrfWt = 0;
				DiscntAmt = NorTrfwt;
			}
		} else {
			NetTrfWt = NorTrfwt - DiscntAmt;
			Log.d("==>Net:", String.valueOf(NetTrfWt));
		}

		Vat = (NetTrfWt + SrvFee) * 0.07;
		Vat = (double) Math.round(Vat * 100) / 100;

		DecimalFormat form = new DecimalFormat("0.00");
		Vat = Double.parseDouble(form.format(Vat));
		Log.d("Net:", String.format("%.2f", NetTrfWt));
		Log.d("Service:", String.format("%.2f", SrvFee));
		Log.d("vat:", String.format("%.2f", Vat));


		if (DisCntCode == " ") {
			TotTrfwt = NetTrfWt + SrvFee + Vat;
		} else {
			TotTrfwt = NetTrfWt + SrvFee + Vat;
		}

		if (tmpdupamt > 0) {
			Vat = (NetTrfWt + SrvFee) * 0.07;
			Vat = (double) Math.round(Vat * 100) / 100;


			DecimalFormat formt = new DecimalFormat("0.00");
			debtvat = Double.parseDouble(formt.format(debtvat));
			Vat = Double.parseDouble(formt.format(Vat));

			TotTrfwt = NetTrfWt + SrvFee + Vat;
			Log.d("==>TotTrfwt:", String.valueOf(TotTrfwt));

			if (tmpdupamt > TotTrfwt) //รับซ้ำมากกว่าค่าน้ำปัจจุบัน
			{
				//Log.d("ค่าน้ำรับซ้ำมากกว่าค่าน้ำปัจจุบัน", String.valueOf(tmpdupamt));
				debtnet = (NetTrfWt + SrvFee);
				debtvat = (NetTrfWt + SrvFee) * 0.07;
				debtamt = debtnet + debtvat;
				dupamt = tmpdupamt - debtamt;
				dupvat = (dupamt * 7) / 107;
				dupnet = dupamt - dupvat;
				Vat = 0.0;
				TotTrfwt = 0.0;

			} else if (tmpdupamt == TotTrfwt) { //รับซ้ำเท่ากับค่าน้ำปัจจุบัน
				//Toast.makeText(this, "ยอดเงินรวม : " + Vat, Toast.LENGTH_SHORT).show();
				//Log.d("ค่าน้ำรับซ้ำเท่ากับค่าน้ำปัจจุบัน", String.valueOf(tmpdupamt));
				debtnet = (NetTrfWt + SrvFee);
				debtvat = (NetTrfWt + SrvFee) * 0.07;
				debtamt = debtnet + debtvat;

				Vat = 0.0;
				TotTrfwt = 0.0;
				dupnet = 0.0;
				dupvat = 0.0;
				dupamt = 0.0;

			} else { //รับซ้ำน้อยกว่าค่าน้ำปัจจะบัน
				//Log.d("ค่าน้ำรับซ้ำน้อยกว่าค่าน้ำปัจจุบัน", String.valueOf(tmpdupamt));
				debtnet = (NetTrfWt + SrvFee);
				debtvat = (NetTrfWt + SrvFee) * 0.07;
				debtamt = debtnet + debtvat;

				TotTrfwt = debtamt - tmpdupamt;

				Vat = ((TotTrfwt) * 7) / 107;
				Vat = (double) Math.round(Vat * 100) / 100;
				DecimalFormat formt1 = new DecimalFormat("0.00");
				Vat = Double.parseDouble(formt1.format(Vat));

				dupnet = 0.0;
				dupvat = 0.0;
				dupamt = 0.0;
			}
		} else {
			//คำนวณแบบปกติ
			DecimalFormat forma = new DecimalFormat("0.00");
			Vat = (NetTrfWt + SrvFee) * 0.07;
			Vat = (double) Math.round(Vat * 100) / 100;
			Vat = Double.parseDouble(forma.format(Vat));
			TotTrfwt = NetTrfWt + SrvFee + Vat;

			debtamt = TotTrfwt;
			debtnet = (NetTrfWt + SrvFee);
			debtvat = (NetTrfWt + SrvFee) * 0.07;
			debtvat = Vat;

		}


		if (PwaFlag.equalsIgnoreCase("4")) {
			String TempDisPwaPro = "";
			TempDisPwaPro = db.getDisPwaPro();
			if (TotTrfwt >= Double.parseDouble(TempDisPwaPro)) {
				Log.d("เข้าเคส=====> มากกว่า ", PwaFlag);
				debtamt = TotTrfwt;
				debtnet = (NetTrfWt + SrvFee);
				debtvat = Vat;
				TotTrfwt = debtamt - Double.parseDouble(TempDisPwaPro);
				Vat = (TotTrfwt * 7) / 107;
				Vat = (double) Math.round(Vat * 100) / 100;
				DecimalFormat formt = new DecimalFormat("0.00");
				Vat = Double.parseDouble(formt.format(Vat));
				distempamt = Double.parseDouble(TempDisPwaPro);
				distempvat = (distempamt * 7) / 107;
				distempnet = distempamt - distempvat;
			} else {
				Log.d("เข้าเคส=====> น้อยกว่า ", PwaFlag);
				debtamt = TotTrfwt;
				if (DisCntCode.equalsIgnoreCase("4")) {
					NetTrfWt = (NetTrfWt + SrvFee);
					debtnet = NetTrfWt;
				} else {
					debtnet = (NetTrfWt + SrvFee);
				}
				debtvat = Vat;
				TotTrfwt = 0.00;
				Vat = 0.00;
				distempamt = debtamt;
				distempnet = (distempamt * 100) / 107;
				distempvat = distempamt - distempnet;
			}
		}

		Log.d("Net:", String.format("%.2f", NetTrfWt));
		Log.d("Service:", String.format("%.2f", SrvFee));
		Log.d("vat:", String.format("%.2f", Vat));
		Log.d("TotTrfwt:", String.format("%.2f", TotTrfwt));

	}

	private double CalDiscount(double price, double used) {
		// TODO Auto-generated method stub

		String disccode, discType = "";
		double discUnit=0, discBaht =0, discNom = 0, discPcen =0, discNum = 0;

		if (DisCntCode.equalsIgnoreCase("0") || DisCntCode.equalsIgnoreCase(" ")) {
			return 0;
		}

		double TmpCal = 0;
		double count06;
		count06 = db.getCountDbst06();
		Cursor mCursor06 = db.curDbst06();
		mCursor06.moveToFirst();
		for (int i = 0; i < count06 - 1; i++) {
			disccode = mCursor06.getString(mCursor06.getColumnIndex("DISCNTCODE")).trim();

//			if (Integer.parseInt(DisCntCode.trim()) == Integer.parseInt(disccode)) {
			if (DisCntCode.trim().equalsIgnoreCase(disccode)) {
				discUnit = mCursor06.getDouble(mCursor06.getColumnIndex("DISCNTUNIT"));
				discBaht = mCursor06.getDouble(mCursor06.getColumnIndex("DISCNTBAHT"));
				discNom = mCursor06.getDouble(mCursor06.getColumnIndex("DISCNTDNOM"));
				discPcen = mCursor06.getDouble(mCursor06.getColumnIndex("DISCNTPCEN"));
				discNum = mCursor06.getDouble(mCursor06.getColumnIndex("DISCNTNUMR"));
				discType = mCursor06.getString(mCursor06.getColumnIndex("DISCNTTYPE"));
				break;
			}
			mCursor06.moveToNext();
		}


			switch (Integer.parseInt(discType.trim())) {
				//ลด 1/3 ของค่าน้ำ
				case 2:
					Log.d("price", String.valueOf(price));
					Log.d("prswtusg", String.valueOf(used));
					TmpCal = price * (discNum / discNom);
					Log.d("Discntamt", String.valueOf(TmpCal));
					if (price - TmpCal < LowPrice) {
						TmpCal = price - LowPrice;
					}
					return TmpCal;
				//ลด 10% สำหรับผู้มีอุปการะคุณ
				case 6:
					TmpCal = (price * discPcen) / 100;
					if (NorTrfwt - TmpCal < LowPrice) {
						TmpCal = NorTrfwt - LowPrice;
					}
					return TmpCal;
				//ต้นสังกัดจ่ายแทน 20 หน่วย
				case 7:
					Log.d("test", "test");
					TmpCal = discBaht;
					return TmpCal;
				default:
					return 0;
			}


	}

	private void ReadDataAgain() {
		// TODO Auto-generated method stub

		try {
			//bn_readagain.setVisibility(View.INVISIBLE);
			bn_value = bn_readagain.getText().toString();
			if (bn_value.equalsIgnoreCase("จดซ้ำ")) {
				chkRead = 2;
				bn_readagain.setText("กลับ");
				rposition = iposition;
				cur = db.select_CISDataRead(chRoute);
				if (cur != null) {
					if (cur.moveToFirst()) {

						setData(cur);
					}
				}
			} else if (bn_value.equalsIgnoreCase("กลับ")) {
				chkRead = 1;
				bn_readagain.setText("จดซ้ำ");
				cur = db.select_CISData(chRoute);
				if (cur != null) {

					if (cur.moveToFirst()) {
						cur.moveToPosition(rposition);
						setData(cur);
					}
				}
			}


		} catch (Exception e) {
			// TODO: handle exception
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		Toast.makeText(this, String.valueOf(requestCode) + ":" + String.valueOf(resultCode), Toast.LENGTH_SHORT).show();
//		Log.d("OK1", String.valueOf(requestCode));
//		Log.d("OK1", String.valueOf(resultCode));
//		Log.d("OK1", String.valueOf(RESULT_OK));
		try {
			if (resultCode != RESULT_OK) {
				return;
			}

			switch (requestCode) {
				case RESULT_REQUEST:

					Comment = data.getStringExtra("Comment");
					ComMentDec = data.getStringExtra("ComMentDec");
					OkPrint = data.getBooleanExtra("OkPrint", true);
					BillSend = data.getStringExtra("BillSend");
					Smcnt = data.getDoubleExtra("Smallusg", Smcnt);

					if (Smcnt > 0) {
						PrsWtUsg = PrsWtUsg - Smcnt;
					}

					getPriceAll();

					ChkDigit();
					Log.d("ChkDigit", ChkDigit);
					UpdateMtRrdData(CustCode);
					BackupAfterRead();
					if (OkPrint) {

						if (printerName.equalsIgnoreCase("HONEYWELL")) {
							Str_report = printBillHoneyWell();
						} else {
							Str_report = PrintBill();
						}

						try {
							Thread t = new Thread(new GotoPrint(Str_report, printaddress));
							t.start();
						} catch (Exception e) {
							// TODO: handle exception
							showDialog("ติดต่อเครื่องพิมพ์ใบแจ้งหนี้ไม่ได้  ?? " + e.getMessage());
							return;
						}

					}
					//Toast.makeText(this, String.valueOf(chkRead), Toast.LENGTH_LONG).show();
					if (chkRead == 2) {
						if (cur != null) {
							if (cur.moveToNext()) {

								setData(cur);
								break;
							}
						}
					} else {
						showData(chRoute);
						break;
					}
				case FIND_OK:
					String tFind = "";
					int idfind = 0;
					tFind = data.getStringExtra("tFind");
					idfind = data.getIntExtra("idfind", 0);
					switch (idfind) {
						case 1:
							cur = db.select_FindCust(tFind);
							if (cur != null) {
								if (cur.moveToFirst()) {

									setData(cur);
								}
							} else {
								showDialog("ไม่พบข้อมูล");
							}
							break;
						case 2:
							cur = db.select_FindMeter(tFind, Mtrrdroute);
							if (cur != null) {
								if (cur.moveToFirst()) {

									setData(cur);
								}
							} else {
								showDialog("ไม่พบข้อมูล");
							}
							break;
						case 3:
							cur = db.select_FindAddr(tFind, Mtrrdroute);
							if (cur != null) {
								if (cur.moveToFirst()) {

									setData(cur);
								}
							} else {
								showDialog("ไม่พบข้อมูล");
							}
							break;
						case 4:
							cur = db.select_FindSeq(tFind, Mtrrdroute);
							if (cur != null) {
								if (cur.moveToFirst()) {

									setData(cur);
								}
							} else {
								showDialog("ไม่พบข้อมูล");
							}
							break;
					}

					break;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return;
		}

	}

	private void BackupAfterRead() {
		// TODO Auto-generated method stub
		try {
			final Calendar c = Calendar.getInstance();
			Time time = new Time();
			time.setToNow();


			int day = c.get(Calendar.DATE);
			int month = c.get(Calendar.MONTH);  //0 = JAN / 11 = DEC
			int year = c.get(Calendar.YEAR);

			month = month + 1; //Set int to correct month numeral, i.e 0 = JAN therefore set to 1.

			String MONTH$;
			if (month <= 9) {
				MONTH$ = "0" + month;
			} else {
				MONTH$ = "" + month;
			}    //Set month to MM


			String YYYYDDMM = "" + day + MONTH$ + year;    //Put date ints into string DD/MM/YYYY

			File sd = Environment.getExternalStorageDirectory();
			String path = sd + "/CIS/AUTOBK/" + "CISData" + USERID + YYYYDDMM + ".xml";
			Log.d("BackupAfterread", "STARTxml");
			DatabaseDump databaseDump = new DatabaseDump(db.getReadableDatabase(), path);
			databaseDump.exportData();
			Log.d("BackupAfterread", "Endxml");

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	//ใบแจ้งหนี้
	@SuppressLint("DefaultLocale")
	public String PrintBill() {

		Cursor curConstant, curCISDBP;
		curConstant = db.select_ConstantData();
		if (curConstant != null) {
			if (curConstant.moveToFirst()) {

			}
		}


		final Calendar ca = Calendar.getInstance();
		int day = ca.get(Calendar.DATE);
		int month = ca.get(Calendar.MONTH);  //0 = JAN / 11 = DEC
		int year = ca.get(Calendar.YEAR);


		Time time = new Time();
		time.setToNow();

		month = month + 1; //Set int to correct month numeral, i.e 0 = JAN therefore set to 1.
		String MONTH$;
		if (month <= 9) {
			MONTH$ = "0" + month;
		} else {
			MONTH$ = "" + month;
		}    //Set month to MM


		String formattedDate = day + "/" + MONTH$ + "/" + (year + 543) + " " +
				+time.hour + ":" + String.format("%02d", time.minute);
		//วันที่ครบกำหนดชำระ()
		Date m = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(m);
		Log.d("A", "A");
		String Strtoprint = "",
				StrP_Head = "",
				StrP_wwName = "",
				StrP_DiswwName = curConstant.getString(curConstant.getColumnIndex("wwnamet")),
				StrP_Ba = curConstant.getString(curConstant.getColumnIndex("ba")),
				StrP_Npay = "",
				StrP_DisNpay = "โปรดชำระเงินในวันถัดไป",
				StrP_Tel = "";
		String StrP_DisTel = curConstant.getString(curConstant.getColumnIndex("wwtel")),
				StrP_Invoicecnt = "",
				StrP_DisInvoicecnt = cur.getString(cur.getColumnIndex("invoicecnt")),
				StrP_Custcode = "",
				StrP_DisCustcode = cur.getString(cur.getColumnIndex("custcode")),
				StrP_wwCode = "",
				StrP_DiswwCode = cur.getString(cur.getColumnIndex("wwcode")) + "-" + ChkDigit;


		if (debmonth > 0) {
			StrP_DiswwCode = cur.getString(cur.getColumnIndex("wwcode"));
		}

		Log.d("b", "b");
		String StrP_dueDate = "",
				StrP_DisdueDate = formattedDate,
				StrP_LastPay = "";

		if (debmonth == 0) {
			cal.add(Calendar.DATE, 7);
			int day2 = cal.get(Calendar.DATE);
			if (day >= 25) {
				Log.d(String.valueOf(day2), "วันสุดท้ายจ่ายค่าน้ำ");
				String MONTH1$;
				month = month + 1;
				if (month <= 9) {
					MONTH1$ = "0" + month;
				} else {
					MONTH1$ = "" + month;
				}    //Set month to MM
				StrP_LastPay = "" + String.format("%02d", day2)+ "/" + MONTH1$ + "/" + ((year + 543) - 2500);
			} else {
				StrP_LastPay = "" + String.format("%02d", day2) + "/" + MONTH$ + "/" + ((year + 543) - 2500);
			}

			Log.d("วันที่ครบชำระ", StrP_LastPay);
			if (invflag.equalsIgnoreCase("1")) {
				StrP_LastPay = "-";
			}
		} else {
			cal.add(Calendar.DATE, 3); // 10 is the days you want to add or subtract
			int day3 = cal.get(Calendar.DATE);
			StrP_LastPay = "" + day3 + "/" + MONTH$ + "/" + ((year + 543) - 2500);

			if (invflag.equalsIgnoreCase("1")) {
				StrP_LastPay = "-";
			}
		}

		String StrP_mtrrdroute = "",
				StrP_Dismtrrdroute = cur.getString(cur.getColumnIndex("mtrrdroute")) +
						"." + cur.getString(cur.getColumnIndex("mtrseq"));
		String StrP_Custname = "",
				StrP_DisCustname = cur.getString(cur.getColumnIndex("custname"));
		if (StrP_DisCustname.length() > 39) {
			StrP_DisCustname = StrP_DisCustname.substring(0, 39);
		}
		String StrP_CustAddress = "",
				StrP_DisCustAddress = cur.getString(cur.getColumnIndex("custaddr"));
		if (StrP_DisCustAddress.length() > 39) {
			StrP_DisCustAddress = StrP_DisCustAddress.substring(0, 39);
		}

		String StrP_Lstmtrrddt = "",
				StrP_DisLstmtrrddt = cur.getString(cur.getColumnIndex("lstmtrddt")),
				strP_DisLstmtrrddt = StrP_DisLstmtrrddt.substring(4, 6) + "/" +
						StrP_DisLstmtrrddt.substring(2, 4) + "/" +
						StrP_DisLstmtrrddt.substring(0, 2),
				StrP_Prsmtrrddt = "";
		Log.d("วันที่อ่านครั้งก่อน", strP_DisLstmtrrddt);
		String StrP_DisPrsmtrrddt = day + "/" + MONTH$ + "/" + ((year + 543) - 2500),
				StrP_Lstmtrcnt = "",
				StrP_DisLstmtrcnt = "";

		//เลขในมาตร ครั้งก่อน
		if (PrsMtrCnt >= Est) {
			StrP_DisLstmtrcnt = cur.getString(cur.getColumnIndex("meterest"));
		} else {
			StrP_DisLstmtrcnt = cur.getString(cur.getColumnIndex("lstmtrcnt"));
		}
		Log.d("วันที่อ่านครั้งนี้", StrP_DisPrsmtrrddt);
		String StrP_Prsmtrcnt = "", StrP_DisPrsmtrcnt = "";
		//เลขในมาตร ครั้งนี้
		DecimalFormat df1 = new DecimalFormat("######");
		if (Smcnt > 0) {
			StrP_DisPrsmtrcnt = "(" + String.valueOf(PrsMtrCnt) + "-" + String.valueOf(Smcnt) + ")";
		} else {
			StrP_DisPrsmtrcnt = df1.format(PrsMtrCnt);
		}
		String StrP_Prswtusg = "",
				StrP_DisPrswtusg = "";
		if (PrsWtUsg > 0) {
			DecimalFormat df2 = new DecimalFormat("#,###");
			StrP_DisPrswtusg = df2.format(PrsWtUsg * 1000);
		} else {
			StrP_DisPrswtusg = String.valueOf(PrsWtUsg);
		}
		if (CustStat.equalsIgnoreCase("3")) {
			StrP_DisPrswtusg += " (เฉลี่ย)";
		}
		Log.d("F", "F");
		String StrP_Type = "",

				StrP_DisType = "T" + usertype.substring(0, 1) + "(" + MONTH$ + "/" + ((year + 543) - 2500) + ")";
		if (usertype.substring(0, 1).equalsIgnoreCase("1") && PrsWtUsg > 100) {
			StrP_DisType = "T1(" + MONTH$ + "/" + ((year + 543) - 2500) + ")";
		} else if (usertype.equalsIgnoreCase("29") && PrsWtUsg <= 100) {
			String OldType;
			OldType = cur.getString(cur.getColumnIndex("oldtype"));
			if (OldType.length() == 3) {
				if (OldType.substring(0, 1).equalsIgnoreCase("1")) {
					StrP_DisType = "T1(" + MONTH$ + "/" + ((year + 543) - 2500) + ")";
				}
			}
		}
		DecimalFormat df2 = new DecimalFormat( "#,###,###,##0.00" );
		String StrP_wtnor = "",
				StrP_Diswtnor = String.format("%.2f", NorTrfwt),
				StrP_DISCOUNT = "",
				StrP_DisDISCOUNT = String.format("%.2f", DiscntAmt),
				StrP_Service = "",
				StrP_DisService = String.format("%.2f", SrvFee),
				StrP_Net = "",
				StrP_DisNet = String.format("%.2f", TotTrfwt),
				StrP_Vat = "",
				StrP_DisVat = String.format("%.2f", Vat),
				StrP_Res = "",
				StrP_DisRes = String.valueOf(debmonth),
				StrP_AmtRes = "",
				StrP_DisAmtRes = String.format("%.2f", debamt),
				StrP_AmtTotal = "",
				//StrP_DisAmtTotal = df2.format(TotTrfwt),
                StrP_DisAmtTotal = String.format("%1$,.2f", TotTrfwt),
				StrP_LatLng = "",
				StrP_DisLatLng = "(lat,lon) " + lat + "," + lon,
				StrP_BarCodeA = "",
				StrP_BarCodeB = "",
				StrP_QRCodeA = "",
				StrP_QRCodeB = "",
				StrP_DisPwaPro = "",
				StrP_DisPwaPro2 = "",
				StrP_DisAddText = "";
		Log.d("ยอดเงินรวม",StrP_DisAmtTotal);
		if (DisCntCode.equalsIgnoreCase("4")) {
			StrP_DisDISCOUNT = "0.00";
		}

		//ข้อความแจ้งเตือน
		StrP_DisPwaPro = "ได้รับส่วนลดโครงการประชารัฐร่วมใจ";
		StrP_DisPwaPro2 = "คนไทยประหยัดน้ำ " + String.valueOf(distempamt) + " บาท";
		StrP_DisAddText = "แจ้งเตือนก่อนงดจ่ายค่าน้ำ";


		if (invflag.equalsIgnoreCase("1")) {
			StrP_BarCodeA = "หักบัญชีธนาคารภายในวันที่ 25 ของทุกเดือน ";
			StrP_BarCodeB = "โปรดตรวจสอบยอดเงินในบัญชีของท่านด้วย";
			if (debmonth > 0) {
				StrP_DisAmtTotal = String.format("%.2f", TotTrfwt + debamt);
			}
		} else {
			if (debmonth == 0) {
				//ถ้าน้อยกว่าหรือเท่ากับ 0.00 ไม่แสดงบาร์โคด
				if (TotTrfwt > 0.0){

				StrP_BarCodeA = StrP_DisInvoicecnt + CustCode;
				StrP_BarCodeA += wwcode;
				StrP_BarCodeA += ChkDigit;
				Calendar cal2 = Calendar.getInstance();
				cal2.add(Calendar.DATE, 10);
				int day4 = cal2.get(Calendar.DATE);


				DecimalFormat df4 = new DecimalFormat("000000000");
				StrP_BarCodeA += df4.format(TotTrfwt * 100);
				StrP_BarCodeB = StrP_BarCodeA;
				Log.d("barcode", StrP_BarCodeA);
				StrP_QRCodeA += "099400016490411\n";
				StrP_QRCodeA += "1";
				StrP_QRCodeA += StrP_Ba;
				StrP_QRCodeA += CustCode;
				StrP_QRCodeA += String.format("%02d", day4) + MONTH$ + ((year + 543) - 2500);
				StrP_QRCodeA += "\n";
				StrP_QRCodeA += StrP_DisInvoicecnt;
				GetBillCode();
				StrP_QRCodeA += billcode;
				StrP_QRCodeA += "\n";
				StrP_QRCodeA += String.format("%.0f", TotTrfwt * 100);
				StrP_QRCodeB = "@1030,30:QR_BC,SECURITY45,XDIM3|/|" + StrP_QRCodeA + "|\r\n";
				Log.d("barcodeQR", StrP_QRCodeB);

				}
			} else if (debmonth == 1) {
				StrP_DisAmtTotal = String.format("%.2f", TotTrfwt + debamt);
				StrP_BarCodeA = "ท่านอยู่ระหว่างถูกระงับการใช้น้ำ (ตัดมาตร)";
				StrP_BarCodeB = "โปรดติดต่อชำระหนี้ค้าง ณ สำนักงานประปาพื้นที่";
				Log.d("LOG",StrP_BarCodeA);
			} else if (debmonth >= 2) {
				StrP_DisAmtTotal = String.format("%.2f", TotTrfwt + debamt);
			}
		}

		//ขยายเวลารับชำระที่ตัวแทนเอกชนได้อีก 3 วัน
		String StrP_Adddate = "";
		String StrP_Duedate2 = "";
		String StrP_Sdate = "";
		String StrP_DisSdate = StrP_LastPay;
		String StrP_Edate = "";
		String StrP_DisEdate = "";
		String StrP_PwaPro = "";
		String StrP_PwaPro2 = "";
		String StrP_AddText = "";
		String StrP_Mon1;
		String StrP_Mon2;
		String StrP_Mon3;

		if (debmonth == 0) {
			cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 14); //ปรับวันที่ระงับการใช้น้ำเป็น7วันหลังครบกำหนดรับชำระDEC58
			int day3 = cal.get(Calendar.DATE);
			if (day >= 17 & day < 18) {
				String MONTH2$;
				if (month <= 9) {
					MONTH2$ = "0" + month;
				} else {
					MONTH2$ = "" + month;
				}    //Set month to MM
				StrP_DisEdate = "" + day3 + "/" + MONTH2$ + "/" + ((year + 543) - 2500);
			} else if (day >= 18 & day < 25) {
				String MONTH2$;
				// Toast.makeText(this,"month2 :" + String.valueOf(month),Toast.LENGTH_LONG).show();
				month = month + 1;
				if (month <= 9) {
					MONTH2$ = "0" + month;
				} else {
					MONTH2$ = "" + month;
				}    //Set month to MM
				StrP_DisEdate = "" + day3 + "/" + MONTH2$ + "/" + ((year + 543) - 2500);
			} else if (day >= 25) {
				String MONTH3$;
				//Toast.makeText(this,"month3 :" + String.valueOf(month),Toast.LENGTH_LONG).show();
				month = month;
				if (month <= 9) {
					MONTH3$ = "0" + month;
				} else {
					MONTH3$ = "" + month;
				}    //Set month to MM
				StrP_DisEdate = "" + day3 + "/" + MONTH3$ + "/" + ((year + 543) - 2500);
			} else {
				StrP_DisEdate = "" + day3 + "/" + MONTH$ + "/" + ((year + 543) - 2500);
			}

			Log.d("วันที่ระงับการใช้น้ำ", StrP_DisEdate);
		} else {
			cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 3);
			int day3 = cal.get(Calendar.DATE);
			StrP_DisEdate = "" + day3 + "/" + MONTH$ + "/" + ((year + 543) - 2500);
			Log.d("วันที่ระงับการใช้น้ำ", StrP_DisEdate);
		}
		String StrP_DisDupamt = "";
		String StrP_Dupamt = "";

		//dupnet
		if (tmpdupamt > 0) {
			if (dupamt > 0) {   //ค่าน้ำรับซ้ำมากกว่าค่าน้ำเดือนปัจจุบัน
				//dupnet = Format(dupnet, "0.00")
				StrP_DisDupamt = "ปรับปรุงค่าน้ำรับซ้ำคงเหลือ  : ";

			} else {
				//ค่าน้ำรับซ้ำน้อยกว่าค่าน้ำเดือนปัจจุบัน
				//dupnet = Format(dupnet, "0.00")
				StrP_DisDupamt = "มีค่าน้ำรับซ้ำไว้  : ";


			}
		}


		String StrP_Barcode = "",
				StrP_Version = "",
				StrP_DisVersion = "V.0.0.1",
				StrP_Comment = "",
				StrP_DisComment = "ID." + USERID + "(" + Comment + ")",
				StrP_Stop = "";

		Log.d("PASS", "PASS1");
		new DecimalFormat("#.###");
		try {
			Log.d("PASS", "PASS2");
			Log.d("วันที่อ่านครัั้งก่อน", strP_DisLstmtrrddt);
			StrP_Head = "EZ{PRINT:\r\n";
			Log.d("PASS", "PASS3");
			//String VERSION =getString(R.string.version_app);
			StrP_Npay = "@20,210:AN12B,HM1,VM1|" + StrP_DisNpay + "|\r\n";
			StrP_wwName = "@140,290:AN12B,HM1,VM1|" + StrP_DiswwName + "|\r\n";
			StrP_Tel = "@170,290:AN12B,HM1,VM1|" + StrP_DisTel + "|\r\n";
			StrP_Invoicecnt = "@270,6:AN12B,HM1,VM1|" + StrP_DisInvoicecnt + "|\r\n";
			StrP_Custcode = "@270,190:AN12B,HM1,VM1|" + StrP_DisCustcode + "|\r\n";
			StrP_wwCode = "@270,400:AN12B,HM1,VM1|" + StrP_DiswwCode + "|\r\n";
			StrP_dueDate = "@340,6:AN12B,HM1,VM1,RIGHT|" + StrP_DisdueDate + "|\r\n";
			StrP_LastPay = "@340,220:AN12B,HM1,VM1|" + StrP_LastPay + "|\r\n";
			StrP_mtrrdroute = "@340,400:AN12B,HM1,VM1|" + StrP_Dismtrrdroute + "|\r\n";
			StrP_Custname = "@380,100:AN12B,HM1,VM1|" + StrP_DisCustname + "|\r\n";
			StrP_CustAddress = "@410,60:AN12B,HM1,VM1|" + StrP_DisCustAddress + "|\r\n";
			StrP_Lstmtrrddt = "@480,170:AN12B,HM1,VM1|" + strP_DisLstmtrrddt + "|\r\n";
			StrP_Prsmtrrddt = "@480,400:AN12B,HM1,VM1|" + StrP_DisPrsmtrrddt + "|\r\n";
			StrP_Lstmtrcnt = "@510,170:AN12B,HM1,VM1|" + StrP_DisLstmtrcnt + "|\r\n";
			StrP_Prsmtrcnt = "@510,400:AN12B,HM1,VM1|" + StrP_DisPrsmtrcnt + "|\r\n";
			StrP_Prswtusg = "@545,450:AN12B,HM1,VM1,RIGHT|" + StrP_DisPrswtusg + "|\r\n";
			StrP_Type = "@580,110:AN12B,HM1,VM1|" + StrP_DisType + "|\r\n";
			StrP_wtnor = "@580,450:AN12B,HM1,VM1,RIGHT|" + StrP_Diswtnor + "|\r\n";
			StrP_DISCOUNT = "@610,450:AN12B,HM1,VM1,RIGHT|" + StrP_DisDISCOUNT + "|\r\n";

			if (tmpdupamt > 0) {
				if (Vat > 0) {
					//ค่าน้ำรับซ้ำน้อยกว่าค่าน้ำเดือนปัจจุบัน
					StrP_Service = "@640,450:AN12B,HM1,VM1,RIGHT|" + StrP_DisService + "|\r\n";
					StrP_Net = "@705,450:AN12B,HM1,VM1,RIGHT|" + String.format("%.2f", debtamt) + "|\r\n";
					StrP_Vat = "@670,450:AN12B,HM1,VM1,RIGHT|" + StrP_DisVat + "|\r\n";
				} else {
					//ค่าน้ำรับซ้ำมากกว่าค่าน้ำเดือนปัจจุบัน
					StrP_Service = "@640,450:AN12B,HM1,VM1,RIGHT|" + StrP_DisService + "|\r\n";
					StrP_Net = "@705,450:AN12B,HM1,VM1,RIGHT|" + String.format("%.2f", debtamt) + "|\r\n";
					StrP_Vat = "@670,450:AN12B,HM1,VM1,RIGHT|" + String.format("%.2f", debtvat) + "|\r\n";
				}
			} else {

				StrP_Service = "@640,450:AN12B,HM1,VM1,RIGHT|" + StrP_DisService + "|\r\n";

				if (PwaFlag.equalsIgnoreCase("4")) {

					StrP_Net = "@705,450:AN12B,HM1,VM1,RIGHT|" + String.format("%.2f", debtamt) + "|\r\n";
				} else {
					StrP_Net = "@705,450:AN12B,HM1,VM1,RIGHT|" + StrP_DisNet + "|\r\n";
				}

				StrP_Vat = "@670,450:AN12B,HM1,VM1,RIGHT|" + StrP_DisVat + "|\r\n";
			}

			StrP_Res = "@740,160:AN12B,HM1,VM1|" + StrP_DisRes + "|\r\n";
			StrP_AmtRes = "@740,450:AN12B,HM1,VM1,RIGHT|" + StrP_DisAmtRes + "|\r\n";
			StrP_AmtTotal = "@770,450:AN12B,HM1,VM1,RIGHT|" + StrP_DisAmtTotal + "|\r\n";
			StrP_LatLng = "@850,10:AN10B,HM1,VM1|" + StrP_DisLatLng + "|\r\n";


			//ค้างชำระและจ่ายธนาคาร
			if (debmonth == 0) {
				if (invflag.equalsIgnoreCase("1")) {
					StrP_Sdate = "@860,430:AN12B,HM1,VM1|" + "-" + "|\r\n";
					StrP_Edate = "@890,430:AN12B,HM1,VM1|" + "-" + "|\r\n";
					StrP_Barcode = "@850,530:AN12B,ROT90,HM1,VM1|" + StrP_BarCodeA + "|\r\n";
					StrP_Barcode += "@850,565:AN12N,ROT90,HM1,VM1|" + StrP_BarCodeB + "|\r\n";

				} else {
					StrP_Sdate = "@860,430:AN12B,HM1,VM1|" + StrP_DisSdate + "|\r\n";
					StrP_Edate = "@890,430:AN12B,HM1,VM1|" + StrP_DisEdate + "|\r\n";
					StrP_Barcode = "@830,525:BC128,ROT90,HIGH 10,WIDE 2|" + StrP_BarCodeA + "|\r\n";
					StrP_Barcode += "@830,565:AN12N,ROT90,HM1,VM1|" + StrP_BarCodeB + "|\r\n";
					//StrP_Adddate= "@870,3:AN09B,HM1,VM1|" + StrP_DisAdddate + "|\r\n";

				}


			} else if (debmonth == 1) {
				StrP_Sdate = "@860,430:AN12B,HM1,VM1|" + "-" + "|\r\n";
				StrP_Edate = "@890,430:AN12B,HM1,VM1|" + StrP_DisEdate + "|\r\n";
				StrP_Barcode = "@840,530:AN12B,ROT90,HM1,VM1|" + StrP_BarCodeA + "|\r\n";
				StrP_Barcode += "@850,565:AN12N,ROT90,HM1,VM1|" + StrP_BarCodeB + "|\r\n";
                Log.d("LOG",StrP_Barcode);
			} else if (debmonth >= 2) {
				StrP_Sdate = "@870,430:AN12B,HM1,VM1|" + "-" + "|\r\n";
				StrP_Edate = "@905,430:AN12B,HM1,VM1|" + StrP_DisEdate + "|\r\n";
			}

			StrP_Version = "@940,150:AN12N,HM1,VM1|" + StrP_DisVersion + "|\r\n";
			StrP_Comment = "@940,290:AN12N,HM1,VM1|" + StrP_DisComment + "|\r\n";

			if (tmpdupamt > 0) {
				if (dupamt > 0) {
					//ค่าน้ำรับซ้ำมากกว่าค่าน้ำเดือนปัจจุบัน
					//dupnet = Format(dupnet, "0.00")
					StrP_Dupamt = "@800,3:AN09B,HM1,VM1| " + StrP_DisDupamt + String.format("%.2f", dupamt) + " บาท " + "|\r\n";
				} else {
					//ค่าน้ำรับซ้ำน้อยกว่าค่าน้ำเดือนปัจจุบัน
					//dupnet = Format(dupnet, "0.00")
					StrP_Dupamt = "@800,3:AN09B,HM1,VM1| " + StrP_DisDupamt + String.format("%.2f", tmpdupamt) + " บาท " + "|\r\n";

				}
			}


			if (PwaFlag.equalsIgnoreCase("4")) {
				StrP_PwaPro = "@800,20:AN09B,HM1,VM1| " + StrP_DisPwaPro + "|\r\n";
				StrP_PwaPro2 = "@800,25:AN09B,HM1,VM1| " + StrP_DisPwaPro2 + "|\r\n";
				StrP_Adddate = "";
				StrP_Duedate2 = "";
			}

			if (debmonth >= 2) {
				//เพิ่มตุลา2561ข้อความแจ้งเตือนค้างชำระเกิน 2 เดือน
				StrP_Adddate = "@830,60:AN12B,HM1,VM1| " + StrP_DisAddText + "|\r\n";
			}

			String StrP_Revym1, StrP_Revym2, StrP_Revym3, StrP_HeadRevym;

			StrP_HeadRevym = "@1010,460:AN08B,HM1,VM1| " + "(หน่วยเป็น ลบ.ม.)" + "|\r\n";

			if (revym.equalsIgnoreCase("6201")) {
				revym = "6113";
				revym1 = String.valueOf(Integer.parseInt(revym) - 1); //6112
				revym2 = String.valueOf(Integer.parseInt(revym) - 2); //6111
				revym3 = String.valueOf(Integer.parseInt(revym) - 3); //6110

			}else if (revym.equalsIgnoreCase("6202")) {
				revym1 = String.valueOf(Integer.parseInt(revym) - 1); //6201
				revym2 = "6112";
				revym3 = "6111";

			}else if (revym.equalsIgnoreCase("6203")) {
				revym1 = String.valueOf(Integer.parseInt(revym) - 1); //6202
				revym2 = String.valueOf(Integer.parseInt(revym) - 2); //6201
				revym3 = "6112"; //6110

			}else{
				revym1 = String.valueOf(Integer.parseInt(revym) - 1); //6103=>03/61
				revym2 = String.valueOf(Integer.parseInt(revym) - 2);
				revym3 = String.valueOf(Integer.parseInt(revym) - 3);
			}


			revym1 = revym1.substring(2, 4) + "/" + revym1.substring(0, 2);
			revym2 = revym2.substring(2, 4) + "/" + revym2.substring(0, 2);
			revym3 = revym3.substring(2, 4) + "/" + revym3.substring(0, 2);
			StrP_Revym1 = "@1030,220:AN12B,HM1,VM1| " + String.valueOf(revym1) + "|\r\n";
			StrP_Revym2 = "@1030,370:AN12B,HM1,VM1| " + String.valueOf(revym2) + "|\r\n";
			StrP_Revym3 = "@1030,510:AN12B,HM1,VM1| " + String.valueOf(revym3) + "|\r\n";

			StrP_Mon1 = "@1065,230:AN12B,HM1,VM1| " + String.valueOf(mon1) + "|\r\n";
			StrP_Mon2 = "@1065,380:AN12B,HM1,VM1| " + String.valueOf(mon2) + "|\r\n";
			StrP_Mon3 = "@1065,510:AN12B,HM1,VM1| " + String.valueOf(mon3) + "|\r\n";

			Log.d("PASS", StrP_LatLng);
			StrP_Stop = "}\r\n";
			Log.d("PRINT", StrP_DisCustcode);

            //ใบแจ้งหนี้รูปแบบใหม่หักผ่านบัญชี
            String strtoprintbill = "";
            curCISDBP = db.select_CISPData(StrP_DisCustcode);
			int i = curCISDBP.getCount();
            double meterusg;
            String Addrs="",Addrs1 = "", Addrs2 = "", Str_MeterUsg = "", Str_BranchOrder = "";
            String Str_PaidDate="",Str_PaidDate2="",Str_PaidDate3="";
			if (i>=1){


            if (curCISDBP != null) {
                try {

                    //20180925
                    Str_PaidDate = curCISDBP.getString(curCISDBP.getColumnIndex("paid_date"));
                    Log.d("PRINT", Str_PaidDate);
                    Str_PaidDate = curCISDBP.getString(curCISDBP.getColumnIndex("paid_date")).substring(4,6);
                    Log.d("PRINT1", Str_PaidDate);
                    Str_PaidDate2 = curCISDBP.getString(curCISDBP.getColumnIndex("paid_date")).substring(6,8);
                    Log.d("PRINT2", Str_PaidDate2);
                    Str_PaidDate3 = curCISDBP.getString(curCISDBP.getColumnIndex("paid_date")).substring(0,4);
                    Str_PaidDate3 = String.valueOf(Integer.parseInt(Str_PaidDate3) + 543);
                    Log.d("PRINT3", Str_PaidDate3);
                    GetMonthThai(Str_PaidDate);

                    Str_BranchOrder = curCISDBP.getString(curCISDBP.getColumnIndex("branch_order_p"));
                    if (Str_BranchOrder == null) {
                        Str_BranchOrder = "";
                    }
					Addrs = curConstant.getString(curConstant.getColumnIndex("org_addr"));
                    Addrs1 = curConstant.getString(curConstant.getColumnIndex("org_addr"));

                    if (Addrs1.length() >= 30) {
						Addrs1 = curConstant.getString(curConstant.getColumnIndex("org_addr")).substring(0, 30);
						Addrs2 = curConstant.getString(curConstant.getColumnIndex("org_addr")).substring(30, Addrs.length());

					} else {
                        Addrs1 = curConstant.getString(curConstant.getColumnIndex("org_addr"));
                        Addrs2 = "";
                    }


                    meterusg = curCISDBP.getDouble(curCISDBP.getColumnIndex("present_meter_usg"));
                    if (meterusg > 0) {
                        DecimalFormat df6 = new DecimalFormat("#,###");
                        Str_MeterUsg = df6.format(meterusg * 1000);
                    } else {
                        Str_MeterUsg = String.valueOf(meterusg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                Log.d("PRINT", "STRTOPRINTBILL");
                if (curCISDBP.moveToFirst()) {
                    strtoprintbill = "@1140,30:AN12B,HM1,VM1|การประปาส่วนภูมิภาค|";
                    strtoprintbill += "@1140,600:AN12B,HM1,VM1,RIGHT|ใบเสร็จรับเงิน/ใบกำกับภาษี|";
					strtoprintbill += "@1170,30:AN12B,HM1,VM1|" + curConstant.getString(curConstant.getColumnIndex("wwnamet")) + "|";
                    strtoprintbill += "@1170,600:AN12B,HM1,VM1,RIGHT|เลขประจำตัวผู้เสียภาษี|";
                    strtoprintbill += "@1200,30:AN10B,HM1,VM1|" + Addrs1 + "|";
                    strtoprintbill += "@1200,600:AN12B,HM1,VM1,RIGHT|" + curConstant.getString(curConstant.getColumnIndex("tax_code")) + "|";
                    strtoprintbill += "@1220,30:AN10B,HM1,VM1|" + Addrs2 + "|";
                    strtoprintbill += "@1225,600:AN12B,HM1,VM1,RIGHT|สาขาที่ " + curConstant.getString(curConstant.getColumnIndex("branch_order_con")) + "|";
                    strtoprintbill += "@1250,30:AN12B,HM1,VM1|โทรศัพท์ : " + curConstant.getString(curConstant.getColumnIndex("wwtel")) + "|";
                    strtoprintbill += "@1250,600:AN12B,HM1,VM1,RIGHT|เลขที่ : " + curCISDBP.getString(curCISDBP.getColumnIndex("water_bill_no")) + "|";
                    strtoprintbill += "@1290,600:AN12B,HM1,VM1,RIGHT|วันเดือนปี : " + Str_PaidDate2 + " " + billmonth + " " + Str_PaidDate3 + "|";
                    strtoprintbill += "@1320,30:AN12B,HM1,VM1|เลขที่ผู้ใช้น้ำ : " + curCISDBP.getString(curCISDBP.getColumnIndex("custcode")) + "|";
                    strtoprintbill += "@1350,30:AN12B,HM1,VM1|ชื่อผู้ใช้น้ำ : " + curCISDBP.getString(curCISDBP.getColumnIndex("custname")) + "|";
                    strtoprintbill += "@1390,30:AN10B,HM1,VM1|ที่อยู่ : " + curCISDBP.getString(curCISDBP.getColumnIndex("custaddr")) + "|";
                    strtoprintbill += "@1430,30:AN12B,HM1,VM1|เลขประจำตัวผู้เสียภาษี : " + curCISDBP.getString(curCISDBP.getColumnIndex("corporate_no"))
                            + " สาขาที่ " + Str_BranchOrder + "|";
                    strtoprintbill += "@1460,30:AN12B,HM1,VM1|จำนวนหน่วยน้ำใช้|";
                    strtoprintbill += "@1460,500:AN12B,HM1,VM1,RIGHT|" + Str_MeterUsg + " ลิตร|";
                    strtoprintbill += "@1490,30:AN12B,HM1,VM1|ค่าน้ำประจำเดือน|";
                    strtoprintbill += "@1490,500:AN12B,HM1,VM1,RIGHT|" + curCISDBP.getString(curCISDBP.getColumnIndex("debt_ym")).substring(4, 6) + "/"
                            + curCISDBP.getString(curCISDBP.getColumnIndex("debt_ym")).substring(0, 4) + "|"; //256109
                    strtoprintbill += "@1520,30:AN12B,HM1,VM1|ค่าน้ำ|";
                    strtoprintbill += "@1520,500:AN12B,HM1,VM1,RIGHT|" + String.format("%.2f", curCISDBP.getDouble(curCISDBP.getColumnIndex("normal_water_amt"))) + " บาท|";
                    strtoprintbill += "@1550,30:AN12B,HM1,VM1|ส่วนลด|";
                    strtoprintbill += "@1550,500:AN12B,HM1,VM1,RIGHT|" + String.format("%.2f", curCISDBP.getDouble(curCISDBP.getColumnIndex("discount_amt"))) + " บาท|";
                    strtoprintbill += "@1580,30:AN12B,HM1,VM1|ค่าบริการ|";
                    strtoprintbill += "@1580,500:AN12B,HM1,VM1,RIGHT|" + String.format("%.2f", curCISDBP.getDouble(curCISDBP.getColumnIndex("service_amt"))) + " บาท|";
                    strtoprintbill += "@1610,30:AN12B,HM1,VM1|รวมเงิน|";
                    strtoprintbill += "@1610,500:AN12B,HM1,VM1,RIGHT|" + String.format("%.2f", curCISDBP.getDouble(curCISDBP.getColumnIndex("net_water_amt2"))) + " บาท|";
                    strtoprintbill += "@1640,30:AN12B,HM1,VM1|ปรับปรุงค่าน้ำรับซ้ำ|";
                    strtoprintbill += "@1640,500:AN12B,HM1,VM1,RIGHT|" + String.format("%.2f", curCISDBP.getDouble(curCISDBP.getColumnIndex("duplicate_amt"))) + " บาท|";
                    strtoprintbill += "@1670,30:AN12B,HM1,VM1|ภาษีมูลค่าเพิ่ม 7%|";
                    strtoprintbill += "@1670,500:AN12B,HM1,VM1,RIGHT|" + String.format("%.2f", curCISDBP.getDouble(curCISDBP.getColumnIndex("vat_amt"))) + " บาท|";
                    strtoprintbill += "@1700,30:AN12B,HM1,VM1|รวมทั้งสิ้น|";
                    strtoprintbill += "@1700,500:AN12B,HM1,VM1,RIGHT|" + String.format("%.2f", curCISDBP.getDouble(curCISDBP.getColumnIndex("total_water_amt"))) + " บาท|";
                    strtoprintbill += "@1740,30:AN12B,HM1,VM1|หักบัญชี " + curCISDBP.getString(curCISDBP.getColumnIndex("channel_name")) + "|";
                    strtoprintbill += "@1780,130:AN12B,HM1,VM1|ผู้รับเงิน " + curCISDBP.getString(curCISDBP.getColumnIndex("receiver")) + " " + curCISDBP.getString(curCISDBP.getColumnIndex("receiver_code")) + "|";
                    strtoprintbill += "@1810,230:AN12B,HM1,VM1|" + curCISDBP.getString(curCISDBP.getColumnIndex("org_rev")) + "|";
                }
            }
			}
			Log.d("PRINT", strtoprintbill);
			Strtoprint =
					StrP_Head +
							StrP_wwName +
							StrP_Npay +
							StrP_Tel +
							StrP_Invoicecnt +
							StrP_Custcode +
							StrP_wwCode +
							StrP_dueDate +
							StrP_LastPay +
							StrP_mtrrdroute +
							StrP_Custname +
							StrP_CustAddress +
							StrP_Lstmtrrddt +
							StrP_Prsmtrrddt +
							StrP_Lstmtrcnt +
							StrP_Prsmtrcnt +
							StrP_Prswtusg +
							StrP_Type +
							StrP_wtnor +
							StrP_DISCOUNT +
							StrP_Service +
							StrP_Net +
							StrP_Vat +
							StrP_Res +
							StrP_AmtRes +
							StrP_AmtTotal +
							StrP_LatLng +
							StrP_Sdate +
							StrP_Edate +
							StrP_Version +
							StrP_Comment +
							StrP_Barcode +
							StrP_Adddate +
							StrP_Duedate2 +
							StrP_Dupamt +
							StrP_PwaPro +
							StrP_PwaPro2 +
							StrP_AddText +
							StrP_Revym1 +
							StrP_Revym2 +
							StrP_Revym3 +
							StrP_Mon1 +
							StrP_Mon2 +
							StrP_Mon3 +
							StrP_HeadRevym +
							StrP_QRCodeB +
							strtoprintbill +
							StrP_Stop;
		} catch (Exception e) {
			showDialog("report=" + e.toString());
		}

		return (Strtoprint);
	}

	//	FORMATPrintHONEYWELL
	@SuppressLint("DefaultLocale")
	public String printBillHoneyWell() {

		Cursor curConstant, curCISDBP;
		curConstant = db.select_ConstantData();
		if (curConstant != null) {
			if (curConstant.moveToFirst()) {

			}
		}

		final Calendar ca = Calendar.getInstance();
		int day = ca.get(Calendar.DATE);
		int month = ca.get(Calendar.MONTH);  //0 = JAN / 11 = DEC
		int year = ca.get(Calendar.YEAR);


		Time time = new Time();
		time.setToNow();

		month = month + 1; //Set int to correct month numeral, i.e 0 = JAN therefore set to 1.
		String MONTH$;
		if (month <= 9) {
			MONTH$ = "0" + month;
		} else {
			MONTH$ = "" + month;
		}    //Set month to MM


		String formattedDate = day + "/" + MONTH$ + "/" + (year + 543) + " " +
				+time.hour + ":" + String.format("%02d", time.minute);
		//วันที่ครบกำหนดชำระ()
		Date m = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(m);
		String Strtoprint = "",
				StrP_Head = "",
				StrP_wwName = "",
				StrP_DiswwName = curConstant.getString(curConstant.getColumnIndex("wwnamet")),
				StrP_Ba = curConstant.getString(curConstant.getColumnIndex("ba")),
				StrP_Npay = "",
				StrP_DisNpay = "โปรดชำระเงินในวันถัดไป",
				StrP_Tel = "";
		String StrP_DisTel = curConstant.getString(curConstant.getColumnIndex("wwtel")),
				StrP_Invoicecnt = "",
				StrP_DisInvoicecnt = cur.getString(cur.getColumnIndex("invoicecnt")),
				StrP_Custcode = "",
				StrP_DisCustcode = cur.getString(cur.getColumnIndex("custcode")),
				StrP_wwCode = "",
				StrP_DiswwCode = cur.getString(cur.getColumnIndex("wwcode")) + "-" + ChkDigit;

		if (debmonth > 0) {
			StrP_DiswwCode = cur.getString(cur.getColumnIndex("wwcode"));
		}

		String StrP_dueDate = "",
				StrP_DisdueDate = formattedDate,
				StrP_LastPay = "";

		if (debmonth == 0) {
			cal.add(Calendar.DATE, 7);
			int day2 = cal.get(Calendar.DATE);
			if (day >= 25) {
				Log.d(String.valueOf(day2), "วันสุดท้ายจ่ายค่าน้ำ");
				String MONTH1$;
				month = month + 1;
				if (month <= 9) {
					MONTH1$ = "0" + month;
				} else {
					MONTH1$ = "" + month;
				}    //Set month to MM
				StrP_LastPay = "" + String.format("%02d", day2) + "/" + MONTH1$ + "/" + ((year + 543) - 2500);
			} else {
				StrP_LastPay = "" + String.format("%02d", day2)+ "/" + MONTH$ + "/" + ((year + 543) - 2500);
			}

			Log.d("วันที่ครบชำระ", StrP_LastPay);
			if (invflag.equalsIgnoreCase("1")) {
				StrP_LastPay = "-";
			}
		} else {
			cal.add(Calendar.DATE, 3); // 10 is the days you want to add or subtract
			int day3 = cal.get(Calendar.DATE);
			StrP_LastPay = "" + day3 + "/" + MONTH$ + "/" + ((year + 543) - 2500);

			if (invflag.equalsIgnoreCase("1")) {
				StrP_LastPay = "-";
			}
		}

		String StrP_mtrrdroute = "",
				StrP_Dismtrrdroute = cur.getString(cur.getColumnIndex("mtrrdroute")) +
						"." + cur.getString(cur.getColumnIndex("mtrseq"));
		String StrP_Custname = "",
				StrP_DisCustname = cur.getString(cur.getColumnIndex("custname"));
		if (StrP_DisCustname.length() > 39) {
			StrP_DisCustname = StrP_DisCustname.substring(0, 39);
		}
		String StrP_CustAddress = "",
				StrP_DisCustAddress = cur.getString(cur.getColumnIndex("custaddr"));
		if (StrP_DisCustAddress.length() > 39) {
			StrP_DisCustAddress = StrP_DisCustAddress.substring(0, 39);
		}

		String StrP_Lstmtrrddt = "",
				StrP_DisLstmtrrddt = cur.getString(cur.getColumnIndex("lstmtrddt")),
				strP_DisLstmtrrddt = StrP_DisLstmtrrddt.substring(4, 6) + "/" +
						StrP_DisLstmtrrddt.substring(2, 4) + "/" +
						StrP_DisLstmtrrddt.substring(0, 2),
				StrP_Prsmtrrddt = "";
		Log.d("วันที่อ่านครั้งก่อน", strP_DisLstmtrrddt);
		String StrP_DisPrsmtrrddt = day + "/" + MONTH$ + "/" + ((year + 543) - 2500),
				StrP_Lstmtrcnt = "",
				StrP_DisLstmtrcnt = "";

		//เลขในมาตร ครั้งก่อน
		if (PrsMtrCnt >= Est) {
			StrP_DisLstmtrcnt = cur.getString(cur.getColumnIndex("meterest"));
		} else {
			StrP_DisLstmtrcnt = cur.getString(cur.getColumnIndex("lstmtrcnt"));
		}
		Log.d("วันที่อ่านครั้งนี้", StrP_DisPrsmtrrddt);
		String StrP_Prsmtrcnt = "", StrP_DisPrsmtrcnt = "";
		//เลขในมาตร ครั้งนี้
		DecimalFormat df1 = new DecimalFormat("######");
		if (Smcnt > 0) {
			StrP_DisPrsmtrcnt = "(" + String.valueOf(PrsMtrCnt) + "-" + String.valueOf(Smcnt) + ")";
		} else {
			StrP_DisPrsmtrcnt = df1.format(PrsMtrCnt);
		}
		String StrP_Prswtusg = "",
				StrP_DisPrswtusg = "";
		if (PrsWtUsg > 0) {
			DecimalFormat df2 = new DecimalFormat("#,###");
			StrP_DisPrswtusg = df2.format(PrsWtUsg * 1000);
		} else {
			StrP_DisPrswtusg = String.valueOf(PrsWtUsg);
		}
		if (CustStat.equalsIgnoreCase("3")) {
			StrP_DisPrswtusg += " (เฉลี่ย)";
		}
		Log.d("F", "F");
		String StrP_Type = "", StrP_DisType = "T" + usertype.substring(0, 1) + "(" + MONTH$ + "/" + ((year + 543) - 2500) + ")";
		if (usertype.substring(0, 1).equalsIgnoreCase("1") && PrsWtUsg > 100) {
			StrP_DisType = "T1(" + MONTH$ + "/" + ((year + 543) - 2500) + ")";
		} else if (usertype.equalsIgnoreCase("29") && PrsWtUsg <= 100) {
			String OldType;
			OldType = cur.getString(cur.getColumnIndex("oldtype"));
			if (OldType.length() == 3) {
				if (OldType.substring(0, 1).equalsIgnoreCase("1")) {
					StrP_DisType = "T1(" + MONTH$ + "/" + ((year + 543) - 2500) + ")";
				}
			}
		}

		DecimalFormat df2 = new DecimalFormat( "#,###,###,##0.00" );
		String StrP_wtnor = "",
				StrP_Diswtnor = df2.format(NorTrfwt),
				StrP_DISCOUNT = "",
				StrP_DisDISCOUNT = String.format("%.2f", DiscntAmt),
				StrP_Service = "",
				StrP_DisService = String.format("%.2f", SrvFee),
				StrP_Net = "",
				StrP_DisNet = String.format("%1$,.2f", TotTrfwt),
				StrP_Vat = "",
				StrP_DisVat = df2.format(Vat),
				StrP_Res = "",
				StrP_DisRes = String.valueOf(debmonth),
				StrP_AmtRes = "",
				StrP_DisAmtRes = df2.format(debamt),
				StrP_AmtTotal = "",
				StrP_DisAmtTotal = String.format("%1$,.2f", TotTrfwt),
				StrP_LatLng = "",
				StrP_DisLatLng = "(lat,lon) " + lat + "," + lon,
				StrP_BarCodeA = "",
				StrP_BarCodeB = "",
				StrP_QRCodeA = "",
				StrP_QRCodeB = "",
				StrP_DisPwaPro = "",
				StrP_DisPwaPro2 = "",
				StrP_DisAddText = "";
		Log.d("ยอดเงินรวม",StrP_DisAmtTotal);
		if (DisCntCode.equalsIgnoreCase("4")) {
			StrP_DisDISCOUNT = "0.00";
		}

		StrP_DisPwaPro = "ได้รับส่วนลดโครงการประชารัฐร่วมใจ";
		StrP_DisPwaPro2 = "คนไทยประหยัดน้ำ " + distempamt + " บาท";
		StrP_DisAddText = "แจ้งเตือนก่อนงดจ่ายค่าน้ำ";


        if (TotTrfwt > 0) {
	    //เริ่มต้นบาร์โคด
		if (invflag.equalsIgnoreCase("1")) {
			StrP_BarCodeA = "หักบัญชีธนาคารภายในวันที่ 25 ของทุกเดือน ";
			StrP_BarCodeB = "โปรดตรวจสอบยอดเงินในบัญชีของท่านด้วย";
			if (debmonth > 0) {
				DecimalFormat df6 = new DecimalFormat("#,###,###,##0.00");
				StrP_DisAmtTotal = df6.format(TotTrfwt + debamt);
				Log.d("ยอดเงินรวม",StrP_DisAmtTotal);
			}
		} else {//ไม่หักบัญชี

                if (debmonth == 0) {
                    StrP_BarCodeA = StrP_DisInvoicecnt + CustCode;
                    StrP_BarCodeA += wwcode;
                    StrP_BarCodeA += ChkDigit;
                    Calendar cal2 = Calendar.getInstance();
                    cal2.add(Calendar.DATE, 10);
                    int day4 = cal2.get(Calendar.DATE);


                    if (day >= 17 & day < 18) {
                        String MONTH2$;
                        if (month <= 9) {
                            MONTH2$ = "0" + month;
                        } else {
                            MONTH2$ = "" + month;
                        }    //Set month to MM
                        StrP_BarCodeA += day4 + MONTH2$ + ((year + 543) - 2500);
                    } else if (day >= 18 & day < 25) {
                        String MONTH2$;
                        // Toast.makeText(this,"month2 :" + String.valueOf(month),Toast.LENGTH_LONG).show();
                        month = month + 1;
                        if (month <= 9) {
                            MONTH2$ = "0" + month;
                        } else {
                            MONTH2$ = "" + month;
                        }    //Set month to MM
                        StrP_BarCodeA += day4 + MONTH2$ + ((year + 543) - 2500);
                    } else if (day >= 25) {
                        String MONTH3$;
                        //Toast.makeText(this,"month3 :" + String.valueOf(month),Toast.LENGTH_LONG).show();
                        month = month;
                        if (month <= 9) {
                            MONTH3$ = "0" + month;
                        } else {
                            MONTH3$ = "" + month;
                        }    //Set month to MM
                        StrP_BarCodeA += String.format("%02d", day4) + MONTH3$ + ((year + 543) - 2500);
                    } else {
                        StrP_BarCodeA += day4 + MONTH$ + ((year + 543) - 2500);
                    }
                    //String.format("%.2f", TotTrfwt),
                    //df4.format(TotTrfwt * 100);
                    //บารโคด
                    TotTrfwt = Math.round(TotTrfwt * 100.0) / 100.0;
                    Log.d("TotTrfwt11",Double.toString((TotTrfwt)));
                    DecimalFormat df4 = new DecimalFormat("000000000");
                    StrP_BarCodeA += df4.format(TotTrfwt * 100);
                    StrP_BarCodeB = StrP_BarCodeA;
                    Log.d("barcode", StrP_BarCodeA);
                    StrP_QRCodeA += "099400016490411\n";
                    StrP_QRCodeA += "1";
                    //StrP_QRCodeA += StrP_Ba;
                    StrP_QRCodeA += CustCode;
                    StrP_QRCodeA += String.format("%02d", day4) + MONTH$ + ((year + 543) - 2500);
                    StrP_QRCodeA += "\n";
                    StrP_QRCodeA += StrP_DisInvoicecnt;
                    GetBillCode();
                    StrP_QRCodeA += billcode;
                    StrP_QRCodeA += "\n";
                    StrP_QRCodeA += String.format("%.0f", TotTrfwt * 100);
                    StrP_QRCodeB = "@1030,30:QR_BC,SECURITY45,XDIM3|/|" + StrP_QRCodeA + "|\r\n";
                    Log.d("barcodeQR", StrP_QRCodeB);

                } else if (debmonth == 1) {
                    DecimalFormat df6 = new DecimalFormat("#,###,###,##0.00");
                    StrP_DisAmtTotal = df6.format(TotTrfwt + debamt);
                    StrP_BarCodeA = "ท่านอยู่ระหว่างถูกระงับการใช้น้ำ (ตัดมาตร)";
                    StrP_BarCodeB = "โปรดติดต่อชำระหนี้ค้าง ณ สำนักงานประปาพื้นที่";
                    Log.d("ยอดเงินรวม", StrP_BarCodeA);
                    Log.d("ยอดเงินรวม", StrP_DisAmtTotal);
                    Log.d("LOG", StrP_BarCodeA);
                } else if (debmonth >= 2) {
                    DecimalFormat df6 = new DecimalFormat("#,###,###,##0.00");
                    StrP_DisAmtTotal = df6.format(TotTrfwt + debamt);
                    Log.d("ยอดเงินรวม", StrP_DisAmtTotal);
                }
            }
        }

		//ขยายเวลารับชำระที่ตัวแทนเอกชนได้อีก 3 วัน
		String StrP_Adddate = "";
		String StrP_Duedate2 = "", StrP_Sdate = "",
				StrP_DisSdate = StrP_LastPay,
				StrP_Edate = "",
				StrP_DisEdate = "",
				StrP_PwaPro = "",
				StrP_PwaPro2 = "",
				StrP_AddText = "",
				StrP_Mon1,
				StrP_Mon2,
				StrP_Mon3;

		//แก้ไขวันที่อ่าน
        month = ca.get(Calendar.MONTH);  //0 = JAN / 11 = DEC
		month = month + 1; //Set int to correct month numeral, i.e 0 = JAN therefore set to 1.
		if (debmonth == 0) {

			cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 14); //ปรับวันที่ระงับการใช้น้ำเป็น7วันหลังครบกำหนดรับชำระDEC58
			int day3 = cal.get(Calendar.DATE);
			if (day >= 17 & day < 18) {
				String MONTH2$;

				if (month <= 9) {
					MONTH2$ = "0" + month;
				} else {
					MONTH2$ = "" + month;
				}
				StrP_DisEdate = "" + day3 + "/" + MONTH2$ + "/" + ((year + 543) - 2500);
			} else if (day >= 18 & day < 25) {
				String MONTH2$;
				month = month + 1;
				if (month <= 9) {
					MONTH2$ = "0" + month;
				} else {
					MONTH2$ = "" + month;
				}
				StrP_DisEdate = "" + day3 + "/" + MONTH2$ + "/" + ((year + 543) - 2500);

			} else if (day >= 25) {
				String MONTH3$;
                month = month + 1;
				if (month <= 9) {
					MONTH3$ = "0" + month;
				} else {
					MONTH3$ = "" + month;
				}
				StrP_DisEdate = "" + String.format("%02d",day3) + "/" + MONTH3$ + "/" + ((year + 543) - 2500);
			} else {

				StrP_DisEdate = "" + day3 + "/" + MONTH$ + "/" + ((year + 543) - 2500);
			}
			Log.d("วันที่ระงับการใช้น้ำ", StrP_DisEdate);
		} else {
			cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 3);
			int day3 = cal.get(Calendar.DATE);
			StrP_DisEdate = "" + day3 + "/" + MONTH$ + "/" + ((year + 543) - 2500);
			Log.d("วันที่ระงับการใช้น้ำ", StrP_DisEdate);
		}
		String StrP_DisDupamt = "";
		String StrP_Dupamt = "";

		//dupnet
		if (tmpdupamt > 0) {
			if (dupamt > 0) {   //ค่าน้ำรับซ้ำมากกว่าค่าน้ำเดือนปัจจุบัน
				//dupnet = Format(dupnet, "0.00")
				StrP_DisDupamt = "ปรับปรุงค่าน้ำที่ชำระไว้เกินคงเหลือ : ";

			} else {
				//ค่าน้ำรับซ้ำน้อยกว่าค่าน้ำเดือนปัจจุบัน
				//dupnet = Format(dupnet, "0.00")
				StrP_DisDupamt = "มีค่าน้ำที่ชำระไว้เกิน  : ";


			}
		}


		String StrP_Barcode = "",
				StrP_Version = "",
				StrP_DisVersion = "V.0.0.1",
				StrP_Comment = "",
				StrP_DisComment = "ID." + USERID + "(" + Comment + ")",
				StrP_Stop = "";

		Log.d("PASS", "PASS1");
		new DecimalFormat("#.###");
		try {
			Log.d("PASS", "PASS2");

			Log.d("วันที่อ่านครัั้งก่อน", strP_DisLstmtrrddt);
			StrP_Head = "EZ{PRINT:\r\n";
			Log.d("PASS", "PASS3");
			//String VERSION =getString(R.string.version_app);
			StrP_Npay = "@25,210:AN12B,HM1,VM1|" + StrP_DisNpay + "|\r\n";
			StrP_wwName = "@160,300:AN12B,HM1,VM1|" + StrP_DiswwName + "|\r\n";
			StrP_Tel = "@190,290:AN12B,HM1,VM1|" + StrP_DisTel + "|\r\n";
			StrP_Invoicecnt = "@300,5:AN12B,HM1,VM1|" + StrP_DisInvoicecnt + "|\r\n";
			StrP_Custcode = "@300,195:AN12B,HM1,VM1|" + StrP_DisCustcode + "|\r\n";
			StrP_wwCode = "@300,400:AN12B,HM1,VM1|" + StrP_DiswwCode + "|\r\n";
			StrP_dueDate = "@370,15:AN12B,HM1,VM1,RIGHT|" + StrP_DisdueDate + "|\r\n";
			StrP_LastPay = "@370,220:AN12B,HM1,VM1|" + StrP_LastPay + "|\r\n";
			StrP_mtrrdroute = "@370,400:AN12B,HM1,VM1|" + StrP_Dismtrrdroute + "|\r\n";
			StrP_Custname = "@400,100:AN12B,HM1,VM1|" + StrP_DisCustname + "|\r\n";
			StrP_CustAddress = "@430,60:AN12B,HM1,VM1|" + StrP_DisCustAddress + "|\r\n";
			StrP_Lstmtrrddt = "@500,170:AN12B,HM1,VM1|" + strP_DisLstmtrrddt + "|\r\n";
			StrP_Prsmtrrddt = "@500,400:AN12B,HM1,VM1|" + StrP_DisPrsmtrrddt + "|\r\n";
			StrP_Lstmtrcnt = "@530,170:AN12B,HM1,VM1|" + StrP_DisLstmtrcnt + "|\r\n";
			StrP_Prsmtrcnt = "@530,400:AN12B,HM1,VM1|" + StrP_DisPrsmtrcnt + "|\r\n";
			StrP_Prswtusg = "@570,450:AN12B,HM1,VM1,RIGHT|" + StrP_DisPrswtusg + "|\r\n";
			StrP_Type = "@600,110:AN12B,HM1,VM1|" + StrP_DisType + "|\r\n";
			StrP_wtnor = "@600,450:AN12B,HM1,VM1,RIGHT|" + StrP_Diswtnor + "|\r\n";
			StrP_DISCOUNT = "@635,450:AN12B,HM1,VM1,RIGHT|" + StrP_DisDISCOUNT + "|\r\n";

			if (tmpdupamt > 0) {
				if (Vat > 0) {
					//ค่าน้ำรับซ้ำน้อยกว่าค่าน้ำเดือนปัจจุบัน
					StrP_Service = "@665,450:AN12B,HM1,VM1,RIGHT|" + StrP_DisService + "|\r\n";
					StrP_Net = "@725,450:AN12B,HM1,VM1,RIGHT|" + String.format("%1$,.2f", debtamt) + "|\r\n";
					StrP_Vat = "@695,450:AN12B,HM1,VM1,RIGHT|" + StrP_DisVat + "|\r\n";
				} else {
					//ค่าน้ำรับซ้ำมากกว่าค่าน้ำเดือนปัจจุบัน
					StrP_Service = "@665,450:AN12B,HM1,VM1,RIGHT|" + StrP_DisService + "|\r\n";
					StrP_Net = "@725,450:AN12B,HM1,VM1,RIGHT|" + String.format("%1$,.2f", debtamt) + "|\r\n";
					StrP_Vat = "@695,450:AN12B,HM1,VM1,RIGHT|" + String.format("%.2f", debtvat) + "|\r\n";
				}
			} else {

				StrP_Service = "@665,450:AN12B,HM1,VM1,RIGHT|" + StrP_DisService + "|\r\n";

				if (PwaFlag.equalsIgnoreCase("4")) {

					StrP_Net = "@725,450:AN12B,HM1,VM1,RIGHT|" + String.format("%1$,.2f", debtamt) + "|\r\n";
				} else {
					StrP_Net = "@725,450:AN12B,HM1,VM1,RIGHT|" + StrP_DisNet + "|\r\n";
				}

				StrP_Vat = "@695,450:AN12B,HM1,VM1,RIGHT|" + StrP_DisVat + "|\r\n";
			}



			StrP_Res = "@755,160:AN12B,HM1,VM1|" + StrP_DisRes + "|\r\n";
			StrP_AmtRes = "@755,450:AN12B,HM1,VM1,RIGHT|" + StrP_DisAmtRes + "|\r\n";
			StrP_AmtTotal = "@785,450:AN12B,HM1,VM1,RIGHT|" + StrP_DisAmtTotal + "|\r\n";
			StrP_LatLng = "@980,10:AN10B,HM1,VM1|" + StrP_DisLatLng + "|\r\n";

			//ค้างชำระและจ่ายธนาคาร
			if (debmonth == 0) {
				if (invflag.equalsIgnoreCase("1")) {
					StrP_Sdate = "@880,450:AN12B,HM1,VM1|" + "-" + "|\r\n";
					StrP_Edate = "@915,450:AN12B,HM1,VM1|" + "-" + "|\r\n";
					StrP_Barcode = "@830,530:AN12B,ROT90,HM1,VM1|" + StrP_BarCodeA + "|\r\n";
					StrP_Barcode += "@820,570:AN12N,ROT90,HM1,VM1|" + StrP_BarCodeB + "|\r\n";

				} else {
					StrP_Sdate = "@880,460:AN12B,HM1,VM1|" + StrP_DisSdate + "|\r\n";
					StrP_Edate = "@915,460:AN12B,HM1,VM1|" + StrP_DisEdate + "|\r\n";
					StrP_Barcode = "@850,540:BC128,ROT90,HIGH 10,WIDE 2|" + StrP_BarCodeA + "|\r\n";
					StrP_Barcode += "@850,575:AN12N,ROT90,HM1,VM1|" + StrP_BarCodeB + "|\r\n";
					//StrP_Adddate= "@870,3:AN09B,HM1,VM1|" + StrP_DisAdddate + "|\r\n";

				}


			} else if (debmonth == 1) {
				StrP_Sdate = "@880,430:AN12B,HM1,VM1|" + "-" + "|\r\n";
				StrP_Edate = "@915,430:AN12B,HM1,VM1|" + StrP_DisEdate + "|\r\n";
				StrP_Barcode = "@840,530:AN12B,ROT90,HM1,VM1|" + StrP_BarCodeA + "|\r\n";
				StrP_Barcode += "@850,565:AN12N,ROT90,HM1,VM1|" + StrP_BarCodeB + "|\r\n";
			} else if (debmonth >= 2) {
				StrP_Sdate = "@870,430:AN12B,HM1,VM1|" + "-" + "|\r\n";
				StrP_Edate = "@905,430:AN12B,HM1,VM1|" + StrP_DisEdate + "|\r\n";
			}

			StrP_Version = "@955,420:AN12N,HM1,VM1|" + StrP_DisVersion + "|\r\n";
			StrP_Comment = "@955,490:AN12N,HM1,VM1|" + StrP_DisComment + "|\r\n";


			//ค่าน้ำที่ชำระไว้เกินปรับปรุงแก้ไขข้อมูล 22/11/62
			if (tmpdupamt > 0) {
				if (dupamt > 0) {
					//ค่าน้ำรับซ้ำมากกว่าค่าน้ำเดือนปัจจุบัน
					//dupnet = Format(dupnet, "0.00")
					StrP_Dupamt = "@830,20:AN09B,HM1,VM1| " + StrP_DisDupamt +
							String.format("%1$,.2f", dupamt) + " บาท " + "|\r\n";
				} else {
					//ค่าน้ำรับซ้ำน้อยกว่าค่าน้ำเดือนปัจจุบัน
					//dupnet = Format(dupnet, "0.00")
					StrP_Dupamt = "@830,20:AN09B,HM1,VM1| " + StrP_DisDupamt +
							String.format("%1$,.2f", tmpdupamt) + " บาท " + "|\r\n";

				}
			}


			if (PwaFlag.equalsIgnoreCase("4")) {
				StrP_PwaPro = "@830,20:AN09B,HM1,VM1| " + StrP_DisPwaPro + "|\r\n";
				StrP_PwaPro2 = "@860,25:AN09B,HM1,VM1| " + StrP_DisPwaPro2 + "|\r\n";
				StrP_Adddate = "";
				StrP_Duedate2 = "";
			}



			if (debmonth >= 2) {
				//เพิ่มตุลา2562ข้อความแจ้งเตือนค้างชำระเกิน 2 เดือน
				StrP_Adddate = "@1160,180:AN12B,HM1,VM1| " + StrP_DisAddText + "|\r\n";
			}

			String StrP_Revym1, StrP_Revym2, StrP_Revym3, StrP_HeadRevym;

			StrP_HeadRevym = "@1040,480:AN08B,HM1,VM1| " + "(หน่วยเป็น ลบ.ม.)" + "|\r\n";


			if (revym.equalsIgnoreCase("6301")) {
				revym = "6213";
				revym1 = String.valueOf(Integer.parseInt(revym) - 1); //6212
				revym2 = String.valueOf(Integer.parseInt(revym) - 2); //6211
				revym3 = String.valueOf(Integer.parseInt(revym) - 3); //6210

			}else if (revym.equalsIgnoreCase("6302")) {
				revym1 = String.valueOf(Integer.parseInt(revym) - 1); //6301
				revym2 = "6212";
				revym3 = "6211";

			}else if (revym.equalsIgnoreCase("6303")) {
				revym1 = String.valueOf(Integer.parseInt(revym) - 1); //6302
				revym2 = String.valueOf(Integer.parseInt(revym) - 2); //6301
				revym3 = "6212";

			}else{
				revym1 = String.valueOf(Integer.parseInt(revym) - 1); //6103=>03/61
				revym2 = String.valueOf(Integer.parseInt(revym) - 2);
				revym3 = String.valueOf(Integer.parseInt(revym) - 3);
			}


			revym1 = revym1.substring(2, 4) + "/" + revym1.substring(0, 2);
			revym2 = revym2.substring(2, 4) + "/" + revym2.substring(0, 2);
			revym3 = revym3.substring(2, 4) + "/" + revym3.substring(0, 2);
			StrP_Revym1 = "@1070,230:AN12B,HM1,VM1| " + revym1 + "|\r\n";
			StrP_Revym2 = "@1070,370:AN12B,HM1,VM1| " + revym2 + "|\r\n";
			StrP_Revym3 = "@1070,510:AN12B,HM1,VM1| " + revym3 + "|\r\n";

			StrP_Mon1 = "@1100,230:AN12B,HM1,VM1| " + mon1 + "|\r\n";
			StrP_Mon2 = "@1100,380:AN12B,HM1,VM1| " + mon2 + "|\r\n";
			StrP_Mon3 = "@1100,510:AN12B,HM1,VM1| " + mon3 + "|\r\n";

			Log.d("PASS", StrP_LatLng);
			StrP_Stop = "}\r\n";
			Log.d("PRINT", "StrP_DisCustcode");

			//ใบแจ้งหนี้รูปแบบใหม่หักผ่านบัญชี
			String strtoprintbill = "";
			curCISDBP = db.select_CISPData(StrP_DisCustcode);
			int i = curCISDBP.getCount();
			double meterusg;
			String Addrs="",Addrs1="", Addrs2="", Str_MeterUsg="",
					Str_BranchOrder="",AddrsA="",AddrsB="",AddrsC="";
			String Str_PaidDate="",Str_PaidDate2="",Str_PaidDate3="";

			if (i>=1){

			if (curCISDBP != null) {
				try {
					Log.d("PRINT", "DATAP");
					//20180925
					Str_PaidDate = curCISDBP.getString(curCISDBP.getColumnIndex("paid_date"));
					Log.d("PRINT", Str_PaidDate);
					Str_PaidDate = curCISDBP.getString(curCISDBP.getColumnIndex("paid_date")).substring(4,6);
					Log.d("PRINT1", Str_PaidDate);
					Str_PaidDate2 = curCISDBP.getString(curCISDBP.getColumnIndex("paid_date")).substring(6,8);
					Log.d("PRINT2", Str_PaidDate2);
					Str_PaidDate3 = curCISDBP.getString(curCISDBP.getColumnIndex("paid_date")).substring(0,4);
					Str_PaidDate3 = String.valueOf(Integer.parseInt(Str_PaidDate3) + 543);
					Log.d("PRINT3", Str_PaidDate3);
					GetMonthThai(Str_PaidDate);

					Str_BranchOrder = curCISDBP.getString(curCISDBP.getColumnIndex("branch_order_p"));
					Log.d("ORDER", curCISDBP.getString(curCISDBP.getColumnIndex("branch_order_p")));
					if (Str_BranchOrder == null) {
						Str_BranchOrder = "";
					}
					Addrs = curConstant.getString(curConstant.getColumnIndex("org_addr"));
					Addrs1 = curConstant.getString(curConstant.getColumnIndex("org_addr"));

					if (Addrs1.length() >= 30) {
						Addrs1 = curConstant.getString(curConstant.getColumnIndex("org_addr")).substring(0, 30);
						Addrs2 = curConstant.getString(curConstant.getColumnIndex("org_addr")).substring(30, Addrs.length());
					} else {
						Addrs1 = curConstant.getString(curConstant.getColumnIndex("org_addr"));
						Addrs2 = "";
					}

					AddrsA = curCISDBP.getString(curCISDBP.getColumnIndex("custaddr"));
					AddrsB = curCISDBP.getString(curCISDBP.getColumnIndex("custaddr"));
					AddrsC = curCISDBP.getString(curCISDBP.getColumnIndex("custaddr"));



					meterusg = curCISDBP.getDouble(curCISDBP.getColumnIndex("present_meter_usg"));
					if (meterusg > 0) {
						DecimalFormat df6 = new DecimalFormat("#,###");
						Str_MeterUsg = df6.format(meterusg * 1000);
					} else {
						Str_MeterUsg = String.valueOf(meterusg);
					}

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}

				if (curCISDBP.moveToFirst()) {
					strtoprintbill = "@1140,30:AN12B,HM1,VM1|การประปาส่วนภูมิภาค|";
					strtoprintbill += "@1140,600:AN12B,HM1,VM1,RIGHT|ใบเสร็จรับเงิน/ใบกำกับภาษี|";
					strtoprintbill += "@1170,30:AN12B,HM1,VM1|" + curConstant.getString(curConstant.getColumnIndex("wwnamet")) + "|";
					strtoprintbill += "@1170,600:AN12B,HM1,VM1,RIGHT|เลขประจำตัวผู้เสียภาษี|";
					strtoprintbill += "@1200,30:AN10B,HM1,VM1|" + Addrs1 + "|";
					strtoprintbill += "@1200,600:AN12B,HM1,VM1,RIGHT|" + curConstant.getString(curConstant.getColumnIndex("tax_code")) + "|";
					strtoprintbill += "@1220,30:AN10B,HM1,VM1|" + Addrs2 + "|";
					strtoprintbill += "@1225,600:AN12B,HM1,VM1,RIGHT|สาขาที่ " + curConstant.getString(curConstant.getColumnIndex("branch_order_con")) + "|";
					strtoprintbill += "@1250,30:AN12B,HM1,VM1|โทรศัพท์ : " + curConstant.getString(curConstant.getColumnIndex("wwtel")) + "|";
					strtoprintbill += "@1250,600:AN12B,HM1,VM1,RIGHT|เลขที่ : " + curCISDBP.getString(curCISDBP.getColumnIndex("water_bill_no")) + "|";
					strtoprintbill += "@1290,600:AN12B,HM1,VM1,RIGHT|วันเดือนปี : " + Str_PaidDate2 + " " + billmonth + " " + Str_PaidDate3 + "|";
					strtoprintbill += "@1320,30:AN12B,HM1,VM1|เลขที่ผู้ใช้น้ำ : " + curCISDBP.getString(curCISDBP.getColumnIndex("custcode")) + "|";
					strtoprintbill += "@1350,30:AN12B,HM1,VM1|ชื่อผู้ใช้น้ำ : " + curCISDBP.getString(curCISDBP.getColumnIndex("custname")) + "|";
					if (AddrsB.length() >= 46) {//แก้ไขให้อุบล041262
						AddrsB = curCISDBP.getString(curCISDBP.getColumnIndex("custaddr")).substring(0, 46);
						AddrsC = curCISDBP.getString(curCISDBP.getColumnIndex("custaddr")).substring(46, AddrsA.length());
						strtoprintbill += "@1380,30:AN12B,HM1,VM1|ที่อยู่ : " + AddrsB + "|";
						strtoprintbill += "@1400,100:AN12B,HM1,VM1|" + AddrsC + "|";

					} else {

						strtoprintbill += "@1380,30:AN12B,HM1,VM1|ที่อยู่ : " + AddrsB + "|";

					}

					//strtoprintbill += "@1390,30:AN12B,HM1,VM1|ที่อยู่ : " + curCISDBP.getString(curCISDBP.getColumnIndex("custaddr")) + "|";
					strtoprintbill += "@1430,30:AN12B,HM1,VM1|เลขประจำตัวผู้เสียภาษี : "
							+ curCISDBP.getString(curCISDBP.getColumnIndex("corporate_no"))
							+ " สาขาที่ " + Str_BranchOrder + "|";
					strtoprintbill += "@1460,30:AN12B,HM1,VM1|จำนวนหน่วยน้ำใช้|";
					strtoprintbill += "@1460,500:AN12B,HM1,VM1,RIGHT|" + Str_MeterUsg + " ลิตร|";
					strtoprintbill += "@1490,30:AN12B,HM1,VM1|ค่าน้ำประจำเดือน|";
					strtoprintbill += "@1490,500:AN12B,HM1,VM1,RIGHT|" + curCISDBP.getString(curCISDBP.getColumnIndex("debt_ym")).substring(4, 6) + "/"
							+ curCISDBP.getString(curCISDBP.getColumnIndex("debt_ym")).substring(0, 4) + "|"; //256109
					strtoprintbill += "@1520,30:AN12B,HM1,VM1|ค่าน้ำ|";
					strtoprintbill += "@1520,500:AN12B,HM1,VM1,RIGHT|"
							+ String.format("%.2f", curCISDBP.getDouble(curCISDBP.getColumnIndex("normal_water_amt"))) + " บาท|";
					strtoprintbill += "@1550,30:AN12B,HM1,VM1|ส่วนลด|";
					strtoprintbill += "@1550,500:AN12B,HM1,VM1,RIGHT|"
							+ String.format("%.2f", curCISDBP.getDouble(curCISDBP.getColumnIndex("discount_amt"))) + " บาท|";
					strtoprintbill += "@1580,30:AN12B,HM1,VM1|ค่าบริการ|";
					strtoprintbill += "@1580,500:AN12B,HM1,VM1,RIGHT|"
							+ String.format("%.2f", curCISDBP.getDouble(curCISDBP.getColumnIndex("service_amt"))) + " บาท|";
					strtoprintbill += "@1610,30:AN12B,HM1,VM1|รวมเงิน|";
					strtoprintbill += "@1610,500:AN12B,HM1,VM1,RIGHT|"
							+ String.format("%.2f", curCISDBP.getDouble(curCISDBP.getColumnIndex("net_water_amt2"))) + " บาท|";
					strtoprintbill += "@1640,30:AN12B,HM1,VM1|ปรับปรุงค่าน้ำรับซ้ำ|";
					strtoprintbill += "@1640,500:AN12B,HM1,VM1,RIGHT|"
							+ String.format("%.2f", curCISDBP.getDouble(curCISDBP.getColumnIndex("duplicate_amt"))) + " บาท|";
					strtoprintbill += "@1670,30:AN12B,HM1,VM1|ภาษีมูลค่าเพิ่ม 7%|";
					strtoprintbill += "@1670,500:AN12B,HM1,VM1,RIGHT|"
							+ String.format("%.2f", curCISDBP.getDouble(curCISDBP.getColumnIndex("vat_amt"))) + " บาท|";
					strtoprintbill += "@1700,30:AN12B,HM1,VM1|รวมทั้งสิ้น|";
					strtoprintbill += "@1700,500:AN12B,HM1,VM1,RIGHT|"
							+ String.format("%.2f", curCISDBP.getDouble(curCISDBP.getColumnIndex("total_water_amt"))) + " บาท|";
					strtoprintbill += "@1740,30:AN12B,HM1,VM1|หักบัญชี " + curCISDBP.getString(curCISDBP.getColumnIndex("channel_name")) + "|";
					strtoprintbill += "@1780,130:AN12B,HM1,VM1|ผู้รับเงิน " + curCISDBP.getString(curCISDBP.getColumnIndex("receiver")) + " " + curCISDBP.getString(curCISDBP.getColumnIndex("receiver_code")) + "|";
					strtoprintbill += "@1810,230:AN12B,HM1,VM1|" + curCISDBP.getString(curCISDBP.getColumnIndex("org_rev")) + "|";
				}
			}

			}else{
				if (wwcode.equalsIgnoreCase("1130")){//แก้ไข021262
					strtoprintbill += "@1200,190:AN12B,HM1,VM1|แค่มี Line ก็ง่ายเรื่องประปา|";
					strtoprintbill += "@1260,130:AN12B,HM1,VM1|@pwathailand ง่ายๆ! เพียง 3 ขั้นตอน|";
					strtoprintbill += "@1320,230:QR_BC,SECURITY45,XDIM4|http:///line.me//ti//p//@pwathailand|";
					strtoprintbill += "@1460,140:AN12B,HM1,VM1|1|";
					strtoprintbill += "@1460,420:AN12B,HM1,VM1|2|";
					strtoprintbill += "@1500,60:AN12B,HM1,VM1|เปิด Application|";
					strtoprintbill += "@1500,320:AN12B,HM1,VM1|ค้นหา @pwathailand|";
					strtoprintbill += "@1540,110:AN12B,HM1,VM1|Line|";
					strtoprintbill += "@1540,360:AN12B,HM1,VM1|กดปุ่ม 'เพิ่มเพื่อน'|";
					strtoprintbill += "@1590,20:AN12B,HM1,VM1|-----------------------------------------------------------------------|";
					strtoprintbill += "@1620,260:AN12B,HM1,VM1|3|";
					strtoprintbill += "@1660,100:AN12B,HM1,VM1|เลือกรับบริการต่างๆ จาก กปภ. ได้ทันที|";
					strtoprintbill += "@1690,20:AN12B,HM1,VM1|-----------------------------------------------------------------------|";
				}else{
					strtoprintbill += "@1200,230:AN12B,HM1,VM1|เรียนผู้ใช้น้ำเลขที่|";
					strtoprintbill += "@1260,240:AN12B,HM1,VM1|" + CustCode + "|";
					strtoprintbill += "@1320,200:AN12B,HM1,VM1|กปภ. " + StrP_DiswwName + "|";
					strtoprintbill += "@1380,100:AN12B,HM1,VM1|อยู่ระหว่างปรับปรุงการให้บริการโปรดระบุ|";
					strtoprintbill += "@1440,90:AN12B,HM1,VM1|เบอร์โทรศัพท์ เพื่อรับข้อมูล ข่าวสารทางSMS|";
					strtoprintbill += "@1500,160:AN12B,HM1,VM1|เบอร์โทร......................................|";
					strtoprintbill += "@1600,320:AN12B,HM1,VM1|ผู้ให้ข้อมูล|";
					strtoprintbill += "@1680,260:AN12B,HM1,VM1|.......................................|";
				}
			}

			Log.d("PRINTBILL", strtoprintbill);

			Strtoprint =
					StrP_Head +
							StrP_wwName +
							StrP_Npay +
							StrP_Tel +
							StrP_Invoicecnt +
							StrP_Custcode +
							StrP_wwCode +
							StrP_dueDate +
							StrP_LastPay +
							StrP_mtrrdroute +
							StrP_Custname +
							StrP_CustAddress +
							StrP_Lstmtrrddt +
							StrP_Prsmtrrddt +
							StrP_Lstmtrcnt +
							StrP_Prsmtrcnt +
							StrP_Prswtusg +
							StrP_Type +
							StrP_wtnor +
							StrP_DISCOUNT +
							StrP_Service +
							StrP_Net +
							StrP_Vat +
							StrP_Res +
							StrP_AmtRes +
							StrP_AmtTotal +
							StrP_LatLng +
							StrP_Sdate +
							StrP_Edate +
							StrP_Version +
							StrP_Comment +
							StrP_Barcode +
							StrP_Adddate +
							StrP_Duedate2 +
							StrP_Dupamt +
							StrP_PwaPro +
							StrP_PwaPro2 +
							StrP_AddText +
							StrP_Revym1 +
							StrP_Revym2 +
							StrP_Revym3 +
							StrP_Mon1 +
							StrP_Mon2 +
							StrP_Mon3 +
							StrP_HeadRevym;
							if (wwcode.equalsIgnoreCase("1130")){
								Strtoprint += strtoprintbill +
										StrP_Stop;
							}else{
								Strtoprint += StrP_QRCodeB+
										strtoprintbill +
										StrP_Stop;
							}

		} catch (Exception e) {
			showDialog("report=" + e.toString());
		}

		return (Strtoprint);
	}


	private void UpdateMtRrdData(String custId) {
		// TODO Auto-generated method stub
		try {
			db.updateMtRrdData(custId, OkPrint, CustStat, Comment, ComMentDec,
					NorTrfwt, SrvFee, Vat, TotTrfwt, PrsMtrCnt, PrsWtUsg, DiscntAmt, NetTrfWt,
					Usgcalmthd, USERID, ChkDigit, BillSend, String.valueOf(lat), String.valueOf(lon), HLN,
					dupamt, dupnet, dupvat, debtamt, debtnet, debtvat, distempamt, distempnet, distempvat);

		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(this, "เกิดข้อผิดพลาดในการบันทึกข้อมูล !!", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

	}

	private void ChkDigit() {
		// TODO Auto-generated method stub

		String a = "", b, c;
		String strDigit, Str;
		long Tdigit = 0;
		long itot = 0;

		Log.d("เริ่ม", "a11");
		try {

			Str = Invoicecnt.trim(); //ลขที่ใบแจ้งหนี้
			Tdigit += Long.parseLong(CalPrime(Str));
			Log.d("Invoicecnt", String.valueOf(Tdigit));

			Str = CustCode.trim(); //เลขที่ ป.ผู้ใช้น้ำ
			Tdigit += Long.parseLong(CalPrime(Str));
			Log.d("CustCode", String.valueOf(Tdigit));

			Str = wwcode.trim(); //เลขที่สำนักงานประปา
			Tdigit += Long.parseLong(CalPrime(Str));
			Log.d("wwcode", String.valueOf(Tdigit));


			String ittot;
			double inttot;
			//Log.d("test", String.valueOf(TotTrfwt));
			ittot = String.format("%.2f", TotTrfwt);//259.66

			//Log.d("test", ittot); //577.80
			inttot = Double.parseDouble(ittot);
			//Log.d("test1", String.valueOf(inttot)); //577.80
			inttot = inttot * 100000;
			//Log.d("test2", String.valueOf(inttot)); //577.80
			itot = Long.parseLong(String.format("%.0f", inttot));//57780000
			//Log.d("aaa", String.valueOf(itot)); //577


			Str = String.valueOf(itot);
			Tdigit += Long.parseLong(CalPrime(Str));
			Log.d("tottrfwt", String.valueOf(Tdigit));

			strDigit = String.valueOf(Tdigit);
			Log.d("a", strDigit);
			a = strDigit.substring(0, 1);
			Log.d("a", a);
			b = strDigit.substring(strDigit.length() - 1);
			Log.d("b", b);
			c = b + a;
			Log.d("c", c);
			Tdigit = Tdigit * Integer.parseInt(c);
			Log.d("Tdigit", String.valueOf(Tdigit));
			int i;
			i = String.valueOf(Tdigit).length() - 2;

			ChkDigit = String.valueOf(Tdigit).substring(i);
			Log.d("Cisdigit", ChkDigit);

		} catch (Exception e) {
			Log.d("bCIS", "bCIS");
			ChkDigit = "00";
			e.printStackTrace();
		}

	}

	private String CalPrime(String str) {
		// TODO Auto-generated method stub

		long digit = 0;
		int i = 0;

		for (i = 0; i <= str.length() - 1; i++) {

			switch (i) {
				case 0:
					digit += Long.parseLong(str.substring(i, i + 1)) * 1;
					break;
				case 1:
					digit += Long.parseLong(str.substring(i, i + 1)) * 2;
					break;
				case 2:
					digit += Long.parseLong(str.substring(i, i + 1)) * 3;
					break;
				case 3:
					digit += Long.parseLong(str.substring(i, i + 1)) * 5;
					break;
				case 4:
					digit += Long.parseLong(str.substring(i, i + 1)) * 7;
					break;
				case 5:
					digit += Long.parseLong(str.substring(i, i + 1)) * 11;
					break;
				case 6:
					digit += Long.parseLong(str.substring(i, i + 1)) * 13;
					break;
				case 7:
					digit += Long.parseLong(str.substring(i, i + 1)) * 17;
					break;
				case 8:
					digit += Long.parseLong(str.substring(i, i + 1)) * 19;
					break;
				case 9:
					digit += Long.parseLong(str.substring(i, i + 1)) * 23;
					break;
				case 10:
					digit += Long.parseLong(str.substring(i, i + 1)) * 29;
					break;
				case 11:
					digit += Long.parseLong(str.substring(i, i + 1)) * 31;
					break;
				case 12:
					digit += Long.parseLong(str.substring(i, i + 1)) * 41;
					break;
			}
		}

		return String.valueOf(digit);

	}

	class GotoPrint implements Runnable {

		String Re_Message;
		String Re_printaddress;

		private GotoPrint(String Message, String printaddress) {
			Re_Message = Message;
			Re_printaddress = printaddress;
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
				Log.d("Print", "mess: " + address);
				//Log.d("Print", "mess: "+ mBluetoothAdapter.getAddress());
				//BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("00:17:AC:11:60:02");
				BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
				mCommandService.connect_1(device);

				int nWaitTime = 8;

				while (mCommandService.getState() != bluetoothCommandService.STATE_CONNECTED) {
					Thread.sleep(1000);
					nWaitTime--;
					if (nWaitTime == 0) {
						//DisplayPrintingStatusMessage("Unable to connect!");
						throw (new Throwable("\nชื่อ: " + device.getName() + "\nรหัส: " + device.getAddress() + " ไม่สามารถเชื่อมต่อได้"));
					}
				}

				mCommandService.write(Re_Message);

//				}
			} catch (final Throwable e) {
				//B_printBill.setEnabled(true);
				runOnUiThread(new Runnable() {
					public void run() {
						showDialog("กรุณาตรวจสอบเครื่องพิมพ์ " + e.getMessage());
					}
				});

			} finally {
				if (mCommandService != null) {
					mCommandService.stop();
					if (mCommandService.getState() != bluetoothCommandService.STATE_NONE) {
						Statust_Connect = false;
					}
				}
			}

		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		} else {
			if (mCommandService == null)
				setupCommand();
		}
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

		tvLocInfo.setText(lat + "," + lon);
	}

	private void setupCommand() {
		mCommandService = new bluetoothCommandService(this, mHandler);
	}

	protected void onDestroy() {
		super.onDestroy();

		if (mCommandService != null)
			mCommandService.stop();
		db.close();

	}

	private final Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MESSAGE_STATE_CHANGE:
					switch (msg.arg1) {

						case bluetoothCommandService.STATE_CONNECTED:
	                   /* mTitle.setText(R.string.title_connected_to);
	                    mTitle.append(mConnectedDeviceName);*/
							Statust_Connect = true;
							break;

						case bluetoothCommandService.STATE_CONNECTING:
							// mTitle.setText(R.string.title_connecting);
							break;
						case bluetoothCommandService.STATE_LISTEN:

						case bluetoothCommandService.STATE_NONE:
							// mTitle.setText(R.string.title_not_connected);
							Statust_Connect = false;
							break;
					}
					break;
				case MESSAGE_DEVICE_NAME:
					msg.getData().getString(DEVICE_NAME);
					// Toast.makeText(getApplicationContext(), "Connected to "+ mConnectedDeviceName, Toast.LENGTH_SHORT).show();
					break;
				case MESSAGE_TOAST:
					Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
					break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.bn_ok:
				calMeter();
				break;
			case R.id.bn_moveN:
				Nextdata();
				break;
			case R.id.bn_moveP:
				Previousdata();
				break;
			case R.id.bn_moveF:
				Firstdata();
				break;
			case R.id.bn_moveL:
				Lastdata();
				break;
			case R.id.bn_readagain:
				ReadDataAgain();
				break;
		}
	}

	private void showDialog(String message) {

		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage(message);
		alt_bld.setTitle("คำอธิบาย");
		alt_bld.setPositiveButton("ตกลง", null);
		alt_bld.show();

	}

	private void GetBillCode() {

		// TODO Auto-generated method stub
		switch (wwcode) {
			case "1125":
				billcode = "00155";
				break;
			case "1126":
				billcode = "00156";
				break;
			case "1127":
				billcode = "00158";
				break;
			case "1128":
				billcode = "00159";
				break;
			case "1129":
				billcode = "00157";
				break;
			case "1131":
				billcode = "00161";
				break;
			case "1132":
				billcode = "00162";
				break;
			case "1136":
				billcode = "00163";
				break;
			case "1137":
				billcode = "00166";
				break;
			case "1138":
				billcode = "00164";
				break;
			case "1139":
				billcode = "00165";
				break;
			case "1140":
				billcode = "00167";
				break;
			case "1141":
				billcode = "00174";
				break;
			case "1142":
				billcode = "00175";
				break;
			case "1143":
				billcode = "00176";
				break;
			case "1246":
				billcode = "00266";
				break;
			case "1144":
				billcode = "00170";
				break;
			case "1145":
				billcode = "00172";
				break;
			case "1099":
				billcode = "00153";
				break;
			case "1130":
				billcode = "00160";
				break;
			case "1032":
				billcode = "00208";
				break;
			case "1048":
				billcode = "00233";
				break;
			default:
				break;
		}

	}

	private void GetMonthThai(String mm) {

		// TODO Auto-generated method stub
		switch (mm) {
			case "01":
				billmonth = "มกราคม";
				break;
			case "02":
				billmonth = "กุมภาพันธ์";
				break;
			case "03":
				billmonth = "มีนาคม";
				break;
			case "04":
				billmonth = "เมษายน";
				break;
			case "05":
				billmonth = "พฤษภาคม";
				break;
			case "06":
				billmonth = "มิถุนายน";
				break;
			case "07":
				billmonth = "กรกฎาคม";
				break;
			case "08":
				billmonth = "สิงหาคม";
				break;
			case "09":
				billmonth = "กันยายน";
				break;
			case "10":
				billmonth = "ตุลาคม";
				break;
			case "11":
				billmonth = "พฤศจิกายน";
				break;
			case "12":
				billmonth = "ธันวาคม";
				break;
			default:
				break;
		}

	}
}