package com.example.srcismeterjuly;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadMeterPlus extends Activity {
	private static final int CAMERA_REQUEST = 1;
	private String chRoute;
	public TextView tx1,tx2,tx3,tx4,tx5;
	public String USERID;
	public String[] TXCOMMENT =
			{"ปกติ (00)",
					"บ้านปิด หรือมาตรอยู่ภายใน (01)",
					"มีสิ่งกีดขวางทำให้อ่านตัวเลขไม่ได้ (02)",
					"มีสัตว์เลี้ยงดุร้าย (03)",
					"บ้านว่าง (04)",
					"ลวดตีมาตรขาด (05)",
					"ยังไม่มีลวดตีมาตร (06)",
					"ผู้ใช้น้ำที่ชำระเงินหักผ่านบัญชีธนาคาร (07)",
					"เลขมาตรขึ้นตลอด (08)",
					"หลังมาตรมีการใช้ปั้มสูบน้ำ (09)",
					"มีการลักใช้น้ำ (10)",
					"ยูเนี่ยนมาตรรั่ว (11)",
					"ท่อเมนรองรั่ว (12)",
					"ประตูน้ำรั่ว (13)",
					"หน้าปัดมาตรแตกแต่อ่านตัวเลขได้ (14)",
					"มาตรตายตัวเลขไม่หมุน (15)",
					"มาตรจมดินหรือซีเมนต์(อ่านตัวเลขได้) (16)",
					"มาตรจมน้ำ แต่อ่านตัวเลขได้ (17)",
					"มาตรครบรอบ (18)",
					"ตัวเลขที่อ่านได้มีจำนวนน้อยกว่าที่ได้อ่านไว้ในรอบการอ่านมาตรก่อนหน้านี้(19)",
					"มีการติดมาตรสลับบ้าน (20)",
					"มีการติดมาตรกลับด้าน (21)",
					"มาตรเดินถอยหลัง (22)",
					"มาตรที่มองไม่เห็นตัวเลขหน่วยน้ำ (23)",
					"มาตรเดินเร็วผิดปกติ (24)",
					"มาตรที่ตัวเลขขึ้นแบบก้าวกระโดด (25)",
					"ใช้น้ำน้อยผิดปกติ (26)",
					"ใช้น้ำมากผิดปกติ (27)",
					"มาตรหาย (28)",
					"อื่นๆ (99)"};

	private static final String[] TXCOMMENT2 =
			{"บ้านปิด หรือมาตรอยู่ภายใน (01)",
					"มีสิ่งกีดขวางทำให้อ่านตัวเลขไม่ได้ (02)",
					"มีสัตว์เลี้ยงดุร้าย (03)",
					"มาตรหาย (28)",
					"อื่นๆ (99)"};

	private static final String[] TXCOMMENT3 =
			{"มาตรครบรอบ (18)",
					"บ้านปิด หรือมาตรอยู่ภายใน (01)",
					"มีสิ่งกีดขวางทำให้อ่านตัวเลขไม่ได้ (02)",
					"มีสัตว์เลี้ยงดุร้าย (03)",
					"มาตรเดินถอยหลัง (22)",
					"มาตรหาย (28)",
					"อื่นๆ (99)"};

	private static final String[] TXCOMMENT4 =
			{"ปกติ (00)",
					"บ้านว่าง (04)",
					"ลวดตีมาตรขาด (05)",
					"ยังไม่มีลวดตีมาตร (06)",
					"ผู้ใช้น้ำที่ชำระเงินหักผ่านบัญชีธนาคาร (07)",
					"เลขมาตรขึ้นตลอด (08)",
					"หลังมาตรมีการใช้ปั้มสูบน้ำ (09)",
					"มีการลักใช้น้ำ (10)",
					"ยูเนี่ยนมาตรรั่ว (11)",
					"ท่อเมนรองรั่ว (12)",
					"ประตูน้ำรั่ว (13)",
					"หน้าปัดมาตรแตกแต่อ่านตัวเลขได้ (14)",
					"มาตรตายตัวเลขไม่หมุน (15)",
					"มาตรจมดินหรือซีเมนต์(อ่านตัวเลขได้) (16)",
					"มาตรจมน้ำ แต่อ่านตัวเลขได้ (17)",
					"มาตรครบรอบ (18)",
					"มีการติดมาตรสลับบ้าน (20)",
					"มีการติดมาตรกลับด้าน (21)",
					"มาตรเดินถอยหลัง (22)",
					"มาตรที่มองไม่เห็นตัวเลขหน่วยน้ำ (23)",
					"มาตรเดินเร็วผิดปกติ (24)",
					"มาตรที่ตัวเลขขึ้นแบบก้าวกระโดด (25)",
					"ใช้น้ำน้อยผิดปกติ (26)",
					"ใช้น้ำมากผิดปกติ (27)",
					"มาตรหาย (28)",
					"อื่นๆ (99)"};

	private DBClass db = new DBClass(this);
	private static final String[] TXBILL = {"ใส่กล่องใบแจ้งหนี้ (1)","ส่งให้ผู้ใช้น้ำ (2)","แปะไว้หน้าบ้าน (3)"};
	private TextView txComment;
	private String MeterNo,HLN,Comment,txtcomment;
	private double PrsMtrCnt,PrsWtUsg,Smcnt;
	private EditText etComment,et_smcnt;
	boolean isPrint,OkPrint,SendOutSt,OkRead,SendNotIn;
	boolean Tmsg,Bmsg=false;
	String CustCode,commentCode,commentDesc= "";
	String billStatus;
	String sComment;
	String wwCode;
	int MtrSeq;
	Uri uri,imageFileUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_readmore);

		tx1=(TextView)findViewById(R.id.tx_route2);
		tx2=(TextView)findViewById(R.id.tx_meterno2);
		tx3=(TextView)findViewById(R.id.tx_prsmtrcnt2);
		tx4=(TextView)findViewById(R.id.tx_prswtusg2);
		tx5=(TextView)findViewById(R.id.tx_hln2);
		txComment = (TextView)findViewById(R.id.tx_comment);
		etComment = (EditText)findViewById(R.id.et_comment);
		Button btnOK = (Button)findViewById(R.id.bn_ok);
		Button bnComment = (Button)findViewById(R.id.bn_comment);
		Button bncap2 = (Button)findViewById(R.id.bn_capture2);
		Button bnbill = (Button)findViewById(R.id.bn_billstatus);

		etComment.setEnabled(true);
		bnComment.setEnabled(true);
		bnComment.setText("COMMENT");

		USERID = getIntent().getStringExtra("userid");
		wwCode = getIntent().getStringExtra("TwwCode");
		MtrSeq = getIntent().getIntExtra("MtrSeq", 1);
		CustCode = getIntent().getStringExtra("TCustCode");
		chRoute = getIntent().getStringExtra("TRoute");
		MeterNo = getIntent().getStringExtra("TMeterno");
		PrsMtrCnt=getIntent().getDoubleExtra("TPrsmtrcnt",10);
		PrsWtUsg= getIntent().getDoubleExtra("TPrswtusg",PrsWtUsg);
		HLN =getIntent().getStringExtra("THln");
		Smcnt=getIntent().getDoubleExtra("TSmcnt",Smcnt);
		Comment=getIntent().getStringExtra("TComment");
		Boolean SendAVGSt = ReadMeter.SendAVGSt;
		Tmsg=getIntent().getBooleanExtra("Tmsg",true);
		Bmsg=getIntent().getBooleanExtra("Bmsg", true);
		SendOutSt=getIntent().getBooleanExtra("SendOutSt", true);
		OkRead = getIntent().getBooleanExtra("OkRead", true);
		OkPrint = getIntent().getBooleanExtra("OkPrint", true);
		SendOutSt = getIntent().getBooleanExtra("SendOutSt", true);
		SendNotIn = getIntent().getBooleanExtra("SendNotIn", true);

		if (SendOutSt){

		}else{
			if(Tmsg){
				etComment.setEnabled(false);
				bnComment.setEnabled(false);
				bnComment.setText("ห้ามเปลี่ยน");
			}
		}


		AlertDialog.Builder builder = new AlertDialog.Builder(ReadMeterPlus.this);


		switch (HLN){
			case "N":
				tx5.setText("ปกติ");
				break;
			case "H":
				tx5.setText("น้ำสูง");
				AlertDialog alertDialog1;
				builder.setMessage("HIGH น้ำสูง !!");
				builder.setPositiveButton("OK", null);
				alertDialog1 = builder.show();
				TextView messageText1= (TextView) alertDialog1.findViewById(android.R.id.message);
				messageText1.setGravity(Gravity.CENTER);
				showNotificationUP();
				break;
			case "L":
				tx5.setText("น้ำต่ำ");
				AlertDialog alertDialog2;
				builder.setMessage("LOW น้ำต่ำ !!");
				builder.setPositiveButton("OK", null);
				alertDialog2 = builder.show();
				TextView messageText2 = (TextView) alertDialog2.findViewById(android.R.id.message);
				messageText2.setGravity(Gravity.CENTER);
				showNotificationDown();
				break;
		}

		if (SendAVGSt){
			Comment = "15";
			chkComment(Comment);
			AlertDialog alertDialog;
			builder.setMessage("มาตรตายหรือตัวเลขไม่หมุน(เฉลี่ย)");
			builder.setPositiveButton("OK", null);
			alertDialog = builder.show();
			TextView messageText = (TextView) alertDialog.findViewById(android.R.id.message);
			messageText.setGravity(Gravity.CENTER);
			showNotificationUP();

		}

		int myCnt = (int) PrsMtrCnt;
		int myUsg = (int) PrsWtUsg;
		int mySmcnt =(int) Smcnt;

		tx1.setText(chRoute);
		tx2.setText(MeterNo);
		tx3.setText(String.valueOf(myCnt));
		tx4.setText(String.valueOf(myUsg));


		et_smcnt = (EditText)findViewById(R.id.et_smcnt);
		et_smcnt.setText(String.valueOf(mySmcnt));

		etComment = (EditText)findViewById(R.id.et_comment);
		etComment.setRawInputType(Configuration.KEYBOARD_12KEY);
		etComment.setText(Comment);

		etComment.requestFocus();
		etComment.setSelectAllOnFocus(true);
		etComment.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
						(keyCode == KeyEvent.KEYCODE_ENTER)) {

					txtcomment = etComment.getText().toString();
					chkComment(txtcomment);
					CalMeterSubmit();
					return true;
				}
				return false;
			}
		});

		chkComment(Comment);


		btnOK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				CalMeterSubmit();

			}


		});

		bnComment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder =
						new AlertDialog.Builder(ReadMeterPlus.this);

				builder.setTitle("เลือกหมายเหตุ");
				if(SendOutSt){
					if (SendNotIn){
						TXCOMMENT= TXCOMMENT2;
					}else{
						TXCOMMENT=TXCOMMENT3;
					}
				}else{
					TXCOMMENT=TXCOMMENT4;
				}
				builder.setItems(TXCOMMENT, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						String selected = TXCOMMENT[which];


						commentCode = selected.substring(selected.length()-3, selected.length()-1);
						commentDesc = selected.toString();
						sComment =commentDesc;

						etComment.setText(commentCode);
						txComment.setText(commentDesc);
						chkComment(commentCode);
					}

				});
				builder.setNegativeButton("ไม่เลือก", null);
				builder.create();

				builder.show();
			}
		});

		bncap2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CallCamera();
			}
		});

		bnbill.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder =
						new AlertDialog.Builder(ReadMeterPlus.this);
				builder.setTitle("เลือกสถานะการส่งใบแจ้งหนี้");
				builder.setItems(TXBILL, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stu
						String selected = TXBILL[which];


						billStatus = selected.substring(selected.length()-2, selected.length()-1);
						EditText etBill = (EditText)findViewById(R.id.et_billstatus);
						etBill.setText(billStatus);
					}
				});
				builder.setNegativeButton("ไม่เลือก", null);
				builder.create();

				builder.show();
			}
		});


	}

	public void CallCamera() {

		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String imageFileName = "C"+CustCode.toString() + timeStamp + ".jpg";
			File imagefile = new File(Environment.getExternalStorageDirectory(),"CIS/IMAGE/" + imageFileName);
			imageFileUri = Uri.fromFile(imagefile);

			if (imagefile !=null){

				intent2.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
				//intent.putExtra("crop", "true");
				//intent.putExtra("outputX", 600);
				//intent.putExtra("outputY", 600);
				//intent.putExtra("aspectX", 1);
				//intent.putExtra("aspectY", 1);
				//intent.putExtra("scale", true);
				//intent.putExtra("return-data", true);


				//intent.putExtra("crop", "true");
				intent.putExtra("outputX", 200);
				intent.putExtra("outputY", 200);
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("scale", true);
				//intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
				intent.putExtra("outputFormat",Bitmap.CompressFormat.JPEG.toString());
				startActivityForResult(intent, CAMERA_REQUEST);

			}


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void CalMeterSubmit() {
		// TODO Auto-generated method stub
		try {

			AlertDialog.Builder builder =
					new AlertDialog.Builder(ReadMeterPlus.this);
			builder.setMessage("PRINT ??");
			builder.setPositiveButton("พิมพ์",new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//Toast.makeText(getApplicationContext(), String.valueOf(isPrint), Toast.LENGTH_SHORT).show();
					if (!isPrint){
						//Toast.makeText(getApplicationContext(), "No Print!!", Toast.LENGTH_SHORT).show();
						OkPrint=false;
						EditText etComment = (EditText)findViewById(R.id.et_comment);
						Button bnComment =(Button)findViewById(R.id.bn_comment);
						etComment.setEnabled(false);
						bnComment.setEnabled(false);
						bnComment.setText("COMMENT");
						String strComment = etComment.getText().toString();
						Double SmallUsg = Double.parseDouble(et_smcnt.getText().toString());
						Intent i  = new Intent();
						i.putExtra("Comment", strComment);
						i.putExtra("ComMentDec", sComment);
						i.putExtra("OkPrint", OkPrint);
						i.putExtra("BillSend", billStatus);
						i.putExtra("Smallusg",SmallUsg);
						setResult(-1,i);
						finish();
					}else{
						OkPrint=true;
						EditText etComment = (EditText)findViewById(R.id.et_comment);
						Button bnComment =(Button)findViewById(R.id.bn_comment);
						etComment.setEnabled(false);
						bnComment.setEnabled(false);
						bnComment.setText("COMMENT");
						String strComment = etComment.getText().toString();
						Double SmallUsg = Double.parseDouble(et_smcnt.getText().toString());
						Intent i  = new Intent();
						i.putExtra("Comment", strComment);
						i.putExtra("ComMentDec", sComment);
						i.putExtra("OkPrint", OkPrint);
						i.putExtra("BillSend", billStatus);
						i.putExtra("Smallusg",SmallUsg);
						setResult(-1,i);
						finish();
					}


				}

			});
			builder.setNegativeButton("ไม่พิมพ์",new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					OkPrint=false;
					etComment.requestFocus();
					etComment.setSelectAllOnFocus(true);
				}
			});

			// อย่าลืมคำสั่ง show
			builder.show();

		} catch (Exception e) {
			// TODO: handle exception
		}


	}

	public String chkComment(String message){
		try {


			sComment = "00";
			sComment = message;
			//Toast.makeText(ReadMeterPlus.this, "เลือก " +sComment, Toast.LENGTH_SHORT).show();
			switch (sComment.toString()){
				case "":
					sComment = "";
					isPrint = true;
					break;
				case "00":
					sComment = "ปกติ (00)";
					isPrint = true;
					break;
				case "01":
					sComment = "บ้านปิด หรือมาตรอยู่ภายใน (01)";
					isPrint = true;
					break;
				case "02":
					sComment = "มีสิ่งกีดขวางทำให้อ่านตัวเลขไม่ได้ (02)";
					isPrint = true;
					break;
				case "03":
					sComment = "มีสัตว์เลี้ยงดุร้าย (03)";
					isPrint = true;
					break;
				case "04":
					sComment = "บ้านปิดไม่มีคนอยู่(ไม่มีการใช้น้ำ)(04)";
					isPrint = true;
					break;
				case "05":
					sComment = "ลวดตีมาตรขาด (05)";
					isPrint = true;
					break;
				case "06":
					sComment = "ยังไม่มีลวดตรีมาตร (06)";
					isPrint = true;
					break;
				case "07":
					sComment = "ผู้ใช้น้ำที่ชำระเงินหักผ่านบัญชีธนาคาร (07)";
					isPrint = true;
					break;
				case "08":
					sComment = "เลขมาตรขึ้นตลอด (08)";
					isPrint = true;
					break;
				case "09":
					sComment = "หลังมาตรมีการใช้ปั้มสูบน้ำ (09)";
					isPrint = true;
					break;
				case "10":
					sComment = "มีการลักใช้น้ำ (10)";
					isPrint = true;
					break;
				case "11":
					sComment = "ยูเนี่ยนมาตรรั่ว (11)";
					isPrint = true;
					break;
				case "12":
					sComment = "ท่อเมนรองรั่ว (12)";
					isPrint = true;
					break;
				case "13":
					sComment = "ประตูน้ำรั่ว (13)";
					isPrint = true;
					break;
				case "14":
					sComment = "หน้าปัดมาตรแตกแต่อ่านตัวเลขได้ (14)";
					isPrint = true;
					break;
				case "15":
					sComment = "มาตรตายหรือตัวเลขไม่หมุน(เฉลี่ย) (15)";
					isPrint = true;
					break;
				case "16":
					sComment = "มาตรอยู่ในดินหรือซีเมนต์ แต่อ่านตัวเลขได้ (16)";
					isPrint = true;
					break;
				case "17":
					sComment = "มาตรจมน้ำ แต่อ่านตัวเลขได้ (17)";
					isPrint = true;
					break;
				case "18":
					sComment = "มาตรครบรอบ (18)";
					isPrint = true;
					break;
				case "19":
					sComment = "ตัวเลขที่อ่านได้มีจำนวนน้อยกว่าที่ได้อ่านไว้ในรอบการอ่านมาตรก่อนหน้านี้ (19)";
					isPrint = false;
					break;
				case "20":
					sComment = "มีการติดมาตรสลับด้าน (20)";
					isPrint = false;
					break;
				case "21":
					sComment = "มีการติดมาตรกลับด้าน (21)";
					isPrint = false;
					break;
				case "22":
					sComment = "มาตรเดินถอยหลัง (22)";
					isPrint = false;
					break;
				case "23":
					sComment = "มาตรที่มองไม่เห็นตัวเลขหน่วยน้ำ (23)";
					isPrint = false;
					break;
				case "24":
					sComment = "มาตรเดินเร็วผิดปกติ (24)";
					isPrint = false;
					break;
				case "25":
					sComment = "มาตรที่ตัวเลขขึ้นแบบก้าวกระโดด (25)";
					isPrint = false;
					break;
				case "26":
					sComment = "ใช้น้ำน้อยผิดปกติ (26)";
					isPrint = false;
					break;
				case "27":
					sComment = "ใช้น้ำมากผิดปกติ (27)";
					isPrint = false;
					break;
				case "28":
					sComment = "มาตรหาย (28)";
					isPrint = false;
					break;
				case "99":
					sComment = "อื่นๆ (99)";
					isPrint = false;
					break;
				default:
					break;
			}
			//  Toast.makeText(ReadMeterPlus.this, "เลือก " +sComment, Toast.LENGTH_SHORT).show();
			txComment = (TextView)findViewById(R.id.tx_comment);
			txComment.setText(sComment);
			return sComment;
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(ReadMeterPlus.this, e.getMessage(), Toast.LENGTH_SHORT).show();
			return message;
		}

	}

	/**
	 * On activity result
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult ( requestCode, resultCode, data );
		if (resultCode != RESULT_OK)
			return;
		Log.d("TAG", "onActivityResult, requestCode: " + requestCode + ", resultCode: " + resultCode);

		switch (requestCode) {
			case CAMERA_REQUEST:

				Bundle extras = data.getExtras();

				if (extras != null) {
					Bitmap yourImage = extras.getParcelable("data");
					// convert bitmap to byte
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					//yourImage.compress(CompressFormat.PNG,0, stream);
					yourImage.compress(CompressFormat.JPEG, 85, stream);
					byte imageInByte[] = stream.toByteArray();
					//Log.e("output before conversion", imageInByte.toString());
					// Inserting Contacts

					Log.d("Insert: ", "Inserting ..");
					db.insertIMG(wwCode, chRoute, MtrSeq,CustCode, USERID, imageInByte);
					showDialog("บันทึกรูปเรียบร้อยแล้ว");
					Log.d("2", "out");
				}
				break;
		}
	}

	private void showDialog(String message) {

		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage(message);
		alt_bld.setTitle("คำอธิบาย");
		alt_bld.setPositiveButton("ตกลง",null);
		alt_bld.show();

	}

	private void showNotificationUP(){

		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
		r.play();
	}
	private void showNotificationDown(){

		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
		r.play();
	}
}
