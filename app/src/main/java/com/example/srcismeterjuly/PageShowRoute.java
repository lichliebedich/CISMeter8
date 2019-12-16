package com.example.srcismeterjuly;


import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PageShowRoute extends ActionBarActivity implements OnClickListener {
	Cursor cur,curSumRoute;
	Button bn_next,bn_prev,bn_first,bn_last;
	private DBClass db = new DBClass(this);
	TextView tvwwcode,tvwwTname,tvRoute,tvamount,tvread;
	String wwTname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_meterroute);
		
		tvwwcode = (TextView)findViewById(R.id.tvwwcode);
		tvwwTname=(TextView)findViewById(R.id.tvwwTname);
		tvRoute=(TextView)findViewById(R.id.tvroute);
		tvamount=(TextView)findViewById(R.id.tvamount);
		tvread=(TextView)findViewById(R.id.tvread);
		bn_next = (Button)findViewById(R.id.bn_moveN);
		bn_prev = (Button)findViewById(R.id.bn_moveP);
		bn_first = (Button)findViewById(R.id.bn_moveF);
		bn_last = (Button)findViewById(R.id.bn_moveL);
	
		bn_next.setOnClickListener(this);
		bn_prev.setOnClickListener(this);
		bn_first.setOnClickListener(this);
		bn_last.setOnClickListener(this);
		
		showRoute();
	}
	private void showRoute() {
		// TODO Auto-generated method stub
		
		try {
			
			cur= db.select_ShowRoute();
			if(cur != null)
			{
				if (cur.moveToFirst()) {					    			
					setData(cur);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	
	}
	
	private void setData(Cursor curRoute) {
		// TODO Auto-generated method stub
		String tRoute,rCount;
		if(curRoute.isLast()){
			//Toast.makeText(this, "�������ش���� !! ", Toast.LENGTH_SHORT).show();
			showDialog("ข้อมูลสุดท้าย !!");
		}
				tvwwcode.setText(curRoute.getString(curRoute.getColumnIndex("wwcode")));
				tvRoute.setText(curRoute.getString(curRoute.getColumnIndex("mtrrdroute")));   
				tvamount.setText(curRoute.getString(curRoute.getColumnIndex("total")));   
				getTname();
				tRoute =curRoute.getString(curRoute.getColumnIndex("mtrrdroute"));
				curSumRoute = db.select_SumRoute(tRoute);
				if(curSumRoute!=null){
						rCount = curSumRoute.getString(curSumRoute.getColumnIndex("sumRoute"));
						tvread.setText(rCount);
				}
				
	}
	
	private void getTname() {
		String wwcode;
		wwcode = (tvwwcode.getText().toString());
		// TODO Auto-generated method stub
		switch (wwcode){
			case "1125":
				wwTname = "อุบลราชธานี";
				break;
			case "1126":
				wwTname = "พิบูลมังสาหาร";
				break;
			case "1127":
				wwTname = "เดชอุดม";
				break;
			case "1128":
				wwTname = "เขมราฐ";
				break;
			case "1129":
				wwTname = "อำนาจเจริญ";
				break;
			case "1131":
				wwTname = "เลิงนกทา";
				break;
			case "1132":
				wwTname = "มหาชนะชัย";
				break;
			case "1136":
				wwTname = "บุรีรัมย์";
				break;
			case "1137":
				wwTname = "สตึก";
				break;
			case "1138":
				wwTname = "ลำปลายมาศ";
				break;
			case "1139":
				wwTname = "นางรอง";
				break;
			case "1140":
				wwTname = "ละหานทราย";
				break;
			case "1141":
				wwTname = "สุรินทร์";
				break;
			case "1142":
				wwTname = "ศีขรภูมิ";
				break;
			case "1143":
				wwTname = "รัตนบุรี";
				break;
			case "1246":
				wwTname = "สังขะ";
				break;
			case "1144":
				wwTname = "ศรีสะเกษ";
				break;
			case "1145":
				wwTname = "กันทรลักษ์";
				break;
			case "5532030":
				wwTname = "มุกดาหาร";
				break;
			case "1130":
				wwTname = "ยโสธร";
			case "1048":
				wwTname = "ทดสอบ";
         break;
		 default:break;
		 }
		 tvwwTname.setText(wwTname);
	}	
	public void Nextdata (){

		
		if (cur.moveToNext())
		{
		setData(cur);	
		}
		
    }
	
	public void Previousdata (){

        if (cur.moveToPrevious())
        { 
        	setData(cur);
        }
    }
	
	public void Firstdata(){
		
		if (cur.moveToFirst())
		{
			setData(cur);
		}
	}
	
	public void Lastdata(){
		if (cur.moveToLast())
		{
			
			setData(cur);
		}
	}
	
	
	 private void showDialog(String message) {
			
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
			alt_bld.setMessage(message);
			alt_bld.setTitle("แจ้งเตือน");
			alt_bld.setPositiveButton("ตกลง",null);
			alt_bld.show();

		  }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case  R.id.bn_moveN:
			Nextdata();
			break;
		case  R.id.bn_moveP:
			Previousdata();
			break;
		case  R.id.bn_moveF:
			Firstdata();
			break;
		case R.id.bn_moveL:
			Lastdata();
			break;
		}
	}
	
}
