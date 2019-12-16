package com.example.srcismeterjuly;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class MainMenu extends Activity implements OnClickListener {

	public String USERID;
	public String printerName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_menu);
		
		printerName="HONEYWELL";
		USERID = getIntent().getStringExtra("userid");
		Button btnRoute = (Button) findViewById(R.id.btnRoute);
		btnRoute.setOnClickListener(this);
		Button btnImport = (Button) findViewById(R.id.btnImportData);
		btnImport.setOnClickListener(this);
		Button btnuploadonline = (Button)findViewById(R.id.btnuploadonline);
		btnuploadonline.setOnClickListener(this);
		Button btnmtr = (Button)findViewById(R.id.btnMtr);
		btnmtr.setOnClickListener(this);
		Button btnbackup = (Button)findViewById(R.id.btnbackupdb);
		btnbackup.setOnClickListener(this);
		TextView tvShowId = (TextView)findViewById(R.id.text_title);
		tvShowId.setText("[เข้าใช้งานด้วยรหัส : " + USERID + "]");

		final Switch swPrinter = (Switch)findViewById(R.id.swPrinter);
		swPrinter.setText("HONEYWELL");

		swPrinter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {

				if (bChecked) {
					printerName= "ONEIL";
				} else {
					printerName="HONEYWELL";
				}
				swPrinter.setText(printerName);
			}
		});


	}
	
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent i  = new Intent();				
		setResult(RESULT_OK,i);
		finish();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.btnRoute:
			Intent i = new Intent(this,RouteMeter.class);
			i.putExtra("userid", USERID);
			i.putExtra("printername",printerName);
			startActivity(i);
			break;
		case R.id.btnImportData:
			Intent i1 = new Intent(this,PageImportData.class);
			startActivity(i1);
			break;
		case R.id.btnuploadonline:
			Intent i2 = new Intent(this,PageUploadOnline.class);
			startActivity(i2);
			break;
		case R.id.btnMtr:
			Intent i3 = new Intent(this,PageShowRoute.class);
			startActivity(i3);
			break;
		case  R.id.btnbackupdb:
			Intent i4 = new Intent(this,PageBackupDB.class);
			startActivity(i4);
			break;
		}
	}
	
}
