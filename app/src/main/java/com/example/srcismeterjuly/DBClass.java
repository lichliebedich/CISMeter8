package com.example.srcismeterjuly;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class DBClass extends SQLiteOpenHelper {

	// Database Version
	public static final int DATABASE_VERSION =1;

	// Database Named
	public static final String DATABASE_NAME = "CIS";

	// Table Name
	private static final String TABLE_CISDB = "CISDB";
	private static final String TABLE_CONSTANT = "CONSTANT";
	private static final String TABLE_DBST42 = "DBST42";
	private static final String TABLE_DBST06 = "DBST06";
	private static final String TABLE_DBST51 = "DBST51";
	private static final String TABLE_DBST52 = "DBST52";
	private static final String TABLE_DBST53 = "DBST53";
	private static final String TABLE_DBST54 = "DBST54";
	private static final String TABLE_CISIMG = "CISIMG";
	private static final String TABLE_CISEMP = "CISEMP";
	private static final String TABLE_CISDBP = "CISDBP";

	// CISIMG Table Columns names
	private static final String COL_WWCODE = "wwcode";
	private static final String COL_ROUTE = "mtrrdroute";
	private static final String COL_SEQ = "mtrseq";
	private static final String COL_CUSTCODE = "custcode";
	private static final String COL_USERID = "userid";
	private static final String COL_DATE = "datepic";
	private static final String COL_IMAGE = "image";

	private static final String CREATE_INDEX_CISDB = "CREATE UNIQUE INDEX [CUST_ROUTE] ON [CISDB]("+
			"[mtrrdroute]  ASC,"+
			"[custcode]  ASC)";

	private static final String CREATE_TABLE_CISDB =  "CREATE TABLE CISDB (" +
			"precode TEXT NOT NULL,"+
			"wwcode TEXT (7) NOT NULL,"+
			"mtrrdroute TEXT (6) NOT NULL,"+
			"mtrseq INTEGER NULL,"+
			"custcode TEXT (7) NULL,"+
			"usertype TEXT (3) NULL,"+
			"oldtype TEXT (3) NULL,"+
			"custstat TEXT (1) NULL,"+
			"location TEXT (40) NULL,"+
			"custname TEXT (80) NULL,"+
			"custaddr TEXT (100) NULL,"+
			"mtrmkcode TEXT (2) NULL,"+
			"metersize TEXT (2) NULL,"+
			"meterno TEXT (20) NULL,"+ //แก้ไขจาก10เป็น20
			"prsmtrstat TEXT(1) NULL,"+
			"lstmtrddt TEXT(6) NULL,"+
			"lstmtrcnt INTEGER NULL,"+
			"revym TEXT (4) NULL,"+
			"novat TEXT (1) NULL,"+
			"avgwtuse INTEGER NULL,"+
			"discntcode TEXT (3) NULL,"+
			"invoicecnt TEXT (13) NULL,"+
			"invflag TEXT (1) NULL,"+
			"debmonth INTEGER NULL,"+
			"debamt INTEGER(15,2) NULL,"+
			"remwtusg INTEGER NULL,"+
			"noofhouse INTEGER NULL,"+
			"pwa_flag TEXT NULL,"+ //เพิ่มส่วนลดประชารัฐ
			"meterest INTEGER NULL,"+
			"smcnt INTEGER NULL,"+
			"mincharge TEXT(1) NULL,"+
			"lstwtusg INTEGER NULL,"+
			"subdiscn INTEGER NULL,"+
			"readflag TEXT (1) NULL,"+
			"newread TEXT (1) NULL,"+
			"prsmtrcnt INTEGER NULL,"+
			"nortrfwt INTEGER(10,2) NULL,"+
			"discntamt INTEGER(10,2) NULL,"+
			"NetTrfWt INTEGER(10,2) NuLL,"+
			"srvfee INTEGER(10,2) NULL,"+
			"vat INTEGER(10,2) NULL,"+
			"tottrfwt INTEGER(10,2) NULL,"+
			"comment TEXT (2) NULL,"+
			"commentdec TEXT (14) NULL,"+
			"billflag TEXT (1) NULL,"+
			"billsend TEXT (1) NULL,"+
			"hln TEXT(1) NULL,"+
			"prswtusg INTEGER NULL,"+
			"latitude TEXT(20) NULL,"+
			"lontitude TEXT(20) NULL,"+
			"prsmtrrddt TEXT (6) NULL,"+
			"time TEXT (4) NULL,"+
			"readcount INTEGER NULL,"+
			"printcount INTEGER Null,"+
			"usgcalmthd TEXT (1) Null,"+
			"userid TEXT (4) NULL,"+
			"chkdigit TEXT (2) NULL,"+
			"controlmtr TEXT (1) NULL,"+
			"bigmtrno TEXT(7) NULL,"+
			"tbseq INTEGER NULL,"+
			"readdate TEXT NULL,"+
			"dupamt INTEGER(7,2) NULL,"+
			"dupnet INTEGER(7,2) NULL,"+
			"dupvat INTEGER(7,2) NULL,"+
			"debtamt INTEGER(7,2) NULL,"+
			"debtnet INTEGER(7,2) NULL,"+
			"debtvat INTEGER(7,2) NULL,"+
			"tempdupamt INTEGER(7,2) NULL,"+
			"service_flag TEXT NULL,"+
			"corporate_no TEXT(16) NULL,"+
			"branch_order TEXT(5) NULL,"+
			"distempamt INTEGER(6,2) NULL,"+ //เพิ่มส่วนลดประชารัฐ
			"distempnet INTEGER(6,2) NULL,"+ //เพิ่มส่วนลดประชารัฐ
			"distempvat INTEGER(6,2) NULL,"+ //เพิ่มส่วนลดประชารัฐ
			"mon1 INTEGER(7,2) NULL,"+
			"mon2 INTEGER(7,2) NULL,"+
			"mon3 INTEGER(7,2) NULL"+
			")";
	private static final String CREATE_TABLE_CISDBP =  "CREATE TABLE CISDBP (" +
			"precode TEXT NOT NULL,"+
			"wwcode TEXT(4) NOT NULL,"+
			"custcode TEXT(11) NULL,"+
			"custname TEXT(80) NULL,"+
			"custaddr TEXT(100) NULL,"+
			"corporate_no TEXT(13) NULL,"+
			"branch_order_p TEXT(5) NULL,"+
			"water_bill_no TEXT(16) NULL,"+ //แก้ไขจาก10เป็น20
			"paid_date TEXT(8) NULL,"+
			"invoice_no TEXT(8) NULL,"+
			"present_meter_date TEXT(8) NULL,"+
			"present_meter_count INTEGER(7) NULL,"+
			"present_meter_usg INTEGER(7) NULL,"+
			"debt_ym TEXT(6) NULL,"+
			"normal_water_amt INTEGER(16,2) NULL,"+
			"discount_amt INTEGER(7,2) NULL,"+
			"net_water_amt INTEGER(16,2) NULL,"+
			"service_amt INTEGER(16,2) NULL,"+
			"net_water_amt2 INTEGER(16,2) NULL,"+
			"vat_amt INTEGER(16,2) NULL,"+
			"total_water_amt INTEGER(16,2) NULL,"+
			"duplicate_amt INTEGER(16,2) NULL,"+
			"paid_channel TEXT(2) NULL,"+
			"channel_name TEXT(50) NULL,"+
			"bank_acc TEXT(13) NULL,"+
			"account_page TEXT(5) NULL,"+
			"receiver_code TEXT(10) NULL,"+
			"receiver TEXT(50) NULL,"+
			"org_rev TEXT(50) NULL,"+
			"flag TEXT(5) NULL"+
			")";
	private static final String CREATE_TABLE_CONSTANT =  "CREATE TABLE CONSTANT (" +
			"precode TEXT NOT NULL,"+
			"wwcode TEXT (7) NOT NULL," +
			"wwnamet TEXT (65) NULL," +
			"wwtel TEXT (50) NULL," +
			"ba TEXT (4) NULL," +
			"mvamt1 INTEGER (10,2) NULL," +
			"mvamt2 INTEGER (10,2) NULL,"+
			"mvamt3 INTEGER (10,2) NULL,"+
			"dispwapro INTEGER (6,2) NULL,"+
			"tax_code TEXT(13) NULL," +
			"branch_order_con TEXT(5) NULL," +
			"org_addr TEXT(80) NULL" +
			")";
	private static final String CREATE_TABLE_DBST42 = "CREATE TABLE DBST42(" +
			"metersize TEXT (2) NOT NULL," +
			"srvfee INTEGER(10,2) NULL"+
			")";
	private static final String CREATE_TABLE_DBST06 = "CREATE TABLE DBST06("+
			"DISCNTCODE TEXT (3) NOT NULL,"+
			"DISCNTMEAN TEXT (60) NULL,"+
			"DISCNTTYPE TEXT (1) NULL,"+
			"DISCNTSRVF TEXT (1) NULL,"+
			"DISCNTPCEN FLOAT NULL,"+
			"DISCNTNUMR FLOAT NULL,"+
			"DISCNTDNOM FLOAT NULL,"+
			"DISCNTUNIT FLOAT NULL,"+
			"DISCNTBAHT FLOAT NULL"+
			")";
	private static final String CREATE_TABLE_DBST51 = "CREATE TABLE DBST51 (" +
			"wttrfrt FLOAT NULL,"+
			"acwttrfamt FLOAT NULL,"+
			"lowusgran FLOAT NULL,"+
			"highusgran FLOAT NULL "+
			")";
	private static final String CREATE_TABLE_DBST52 = "CREATE TABLE DBST52 ("+
			"wttrfrt FLOAT NULL,"+
			"acwttrfamt FLOAT NULL,"+
			"lowusgran FLOAT NULL,"+
			"highusgran FLOAT NULL "+
			")";
	private static final String CREATE_TABLE_DBST53 = "CREATE TABLE DBST53 ("+
			"wttrfrt FLOAT NULL,"+
			"acwttrfamt FLOAT NULL,"+
			"lowusgran FLOAT NULL,"+
			"highusgran FLOAT NULL "+
			")";
	private static final String CREATE_TABLE_DBST54 = "CREATE TABLE DBST54 ("+
			"wttrfrt FLOAT NULL,"+
			"acwttrfamt FLOAT NULL,"+
			"lowusgran FLOAT NULL,"+
			"highusgran FLOAT NULL "+
			")";
	private static final String CREATE_TABLE_CISIMG = "CREATE TABLE CISIMG("+
			"wwcode TEXT (7) NOT NULL,"+
			"mtrrdroute TEXT (6) NOT NULL,"+
			"mtrseq INTEGER NULL,"+
			"custcode TEXT (7) NULL,"+
			"userid TEXT(4) NOT NULL,"+
			"datepic TEXT(8) NOT NULL,"+
			"image BLOB NOT NULL"+
			")";
	private static final String CREATE_TABLE_CISEMP= "CREATE TABLE CISEMP("+
			"precode TEXT NOT NULL,"+
			"wwcode TEXT (7) NOT NULL,"+
			"empid TEXT (4) NOT NULL,"+
			"empname TEXT (50) NOT NULL,"+
			"emplastname TEXT (50) NULL,"+
			"empmobile TEXT (10) NULL,"+
			"empposition TEXT (2) NULL "+
			")";
	//private static final String CREATE_TABLE_CISTRAN = "";
	private SQLiteDatabase db;
	private static Context mContext;
	List<XMLParserObject> DBData = null;

	public DBClass(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		// creating required tables
		db.execSQL(CREATE_TABLE_CISDB);
		db.execSQL(CREATE_INDEX_CISDB);
		db.execSQL(CREATE_TABLE_CONSTANT);
		db.execSQL(CREATE_TABLE_DBST06);
		db.execSQL(CREATE_TABLE_DBST42);
		db.execSQL(CREATE_TABLE_DBST51);
		db.execSQL(CREATE_TABLE_DBST52);
		db.execSQL(CREATE_TABLE_DBST53);
		db.execSQL(CREATE_TABLE_DBST54);
		db.execSQL(CREATE_TABLE_CISIMG);
		db.execSQL(CREATE_TABLE_CISEMP);
		db.execSQL(CREATE_TABLE_CISDBP);


		// Insert data from XML
		XMLParser parser = new XMLParser();
		try {
			Log.d("xml","open");
			//CISData = parser.parse(mContext.getAssets().open("CISData.xml"));
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/CIS/DOWNLOAD/CISData.xml");

			// create an input stream to be read by the stream reader.
			FileInputStream fis = new FileInputStream(file);
			DBData = parser.parse(fis);
			for(XMLParserObject CISData : DBData) {
				if (CISData.getprecode()==("DATA")){
					Log.d("insertdata","OK");
					ContentValues values = new ContentValues();
					values.put("precode", CISData.getprecode());
					values.put("wwcode", CISData.getwwcode());
					values.put("mtrrdroute", CISData.getmtrrdroute());
					values.put("mtrseq", CISData.getmtrseq());
					values.put("custcode", CISData.getcustcode());
					values.put("usertype", CISData.getusertype());
					values.put("oldtype", CISData.getoldtype());
					values.put("custstat", CISData.getcuststat());
					values.put("location", CISData.getlocation());
					values.put("custname", CISData.getcustname());
					values.put("custaddr", CISData.getcustaddr());
					values.put("mtrmkcode", CISData.getmtrmkcode());
					values.put("metersize", CISData.getmetersize());
					values.put("meterno", CISData.getmeterno());
					values.put("prsmtrstat", CISData.getprsmtrstat());
					values.put("lstmtrddt", CISData.getlstmtrddt());
					values.put("lstmtrcnt", CISData.getlstmtrcnt());
					values.put("revym", CISData.getrevym());
					values.put("novat", CISData.getnovat());
					values.put("avgwtuse", CISData.getavgwtuse());
					values.put("discntcode", CISData.getdiscntcode());
					values.put("invoicecnt", CISData.getinvoicecnt());
					values.put("invflag", CISData.getinvflag());
					values.put("debmonth", CISData.getdebmonth());
					values.put("debamt", CISData.getdebamt());
					values.put("remwtusg", CISData.getremwtusg());
					values.put("noofhouse", CISData.getnoofhouse());
					values.put("pwa_flag",CISData.getpwaflag());
					values.put("meterest", CISData.getmeterest());
					values.put("smcnt", CISData.getsmcnt());
					values.put("mincharge", CISData.getmincharge());
					values.put("lstwtusg", CISData.getlstwtusg());
					values.put("subdiscn", CISData.getsubdiscn());
					values.put("readflag", CISData.getreadflag());
					values.put("newread", CISData.getnewread());
					values.put("prsmtrcnt", CISData.getprsmtrcnt());
					values.put("nortrfwt", CISData.getnortrfwt());
					values.put("discntamt", CISData.getdiscntamt());
					values.put("srvfee", CISData.getsrvfee());
					values.put("vat", CISData.getvat());
					values.put("tottrfwt", CISData.gettottrfwt());
					values.put("comment", CISData.getcomment());
					values.put("commentdec", CISData.getcommentdec());
					values.put("billflag", CISData.getbillflag());
					values.put("billsend", CISData.getbillsend());
					values.put("hln", CISData.gethln());
					values.put("prswtusg", CISData.getprswtusg());
					values.put("latitude", CISData.getlatitude());
					values.put("lontitude", CISData.getlontitude());
					values.put("prsmtrrddt", CISData.getprsmtrrddt());
					values.put("time", CISData.gettime());
					values.put("readcount", CISData.getreadcount());
					values.put("printcount", CISData.getprintcount());
					values.put("usgcalmthd", CISData.getusgcalmthd());
					values.put("userid", CISData.getuserid());
					values.put("chkdigit", CISData.getchkdigit());
					values.put("controlmtr", CISData.getcontrolmtr());
					values.put("bigmtrno", CISData.getbigmtrno());
					values.put("tbseq", CISData.gettbseq());
					values.put("dupamt", CISData.getdupamt());
					values.put("tempdupamt", CISData.getdupamt());
					values.put("service_flag", CISData.getserviceflag());
					values.put("corporate_no", CISData.getregisno());
					values.put("branch_order", CISData.getcustbrn());
					values.put("mon1", CISData.getmon1());
					values.put("mon2", CISData.getmon2());
					values.put("mon3", CISData.getmon3());
					Log.d("xmldup", String.valueOf(CISData.getmon1()));
					Log.d("xml","import");
					db.insert(TABLE_CISDB, null, values);
					Log.d("xml","successok");

				}else if(CISData.getprecode()=="CON"){
					ContentValues valuesConstant = new ContentValues();
					valuesConstant.put("precode", CISData.getprecode());
					valuesConstant.put("wwcode", CISData.getwwcode());
					valuesConstant.put("wwnamet",CISData.getwwnamet());
					valuesConstant.put("wwtel",CISData.getwwtel());
					valuesConstant.put("ba", CISData.getba());
					valuesConstant.put("mvamt1", CISData.getmvamt1());
					valuesConstant.put("mvamt2", CISData.getmvamt2());
					valuesConstant.put("mvamt3", CISData.getmvamt3());
					valuesConstant.put("dispwapro", CISData.getdispwapro());
					valuesConstant.put("tax_code",CISData.gettaxcode());
					valuesConstant.put("branch_order_con",CISData.getbranchorder2());
					valuesConstant.put("org_addr",CISData.getorgaddr());
					Log.d("xml",String.valueOf(CISData.getbranchorder2()));
					Log.d("xml","importconstant");
					db.insert(TABLE_CONSTANT, null, valuesConstant);
					Log.d("xml","ConstantOk");
				}else if (CISData.getwwcode()=="D51"){
					ContentValues valuesDbst = new ContentValues();
					valuesDbst.put("wttrfrt", CISData.getwttrfrt());
					valuesDbst.put("acwttrfamt", CISData.getacwttrfamt());
					valuesDbst.put("lowusgran", CISData.getlowusgran());
					valuesDbst.put("highusgran", CISData.gethighusgran());
					Log.d("xml","importdbst51");
					db.insert(TABLE_DBST51, null, valuesDbst);
					Log.d("xml","DBST51");
				}else if (CISData.getwwcode()=="D52"){
					ContentValues valuesDbst = new ContentValues();
					valuesDbst.put("wttrfrt", CISData.getwttrfrt());
					valuesDbst.put("acwttrfamt", CISData.getacwttrfamt());
					valuesDbst.put("lowusgran", CISData.getlowusgran());
					valuesDbst.put("highusgran", CISData.gethighusgran());
					Log.d("xml","importdbst52");
					db.insert(TABLE_DBST52, null, valuesDbst);
					Log.d("xml","DBST52");
				}else if (CISData.getwwcode()=="D53"){
					ContentValues valuesDbst = new ContentValues();
					valuesDbst.put("wttrfrt", CISData.getwttrfrt());
					valuesDbst.put("acwttrfamt", CISData.getacwttrfamt());
					valuesDbst.put("lowusgran", CISData.getlowusgran());
					valuesDbst.put("highusgran", CISData.gethighusgran());
					Log.d("xml","importdbst53");
					db.insert(TABLE_DBST53, null, valuesDbst);
					Log.d("xml","DBST53");
				}else if (CISData.getwwcode()=="D54"){
					ContentValues valuesDbst = new ContentValues();
					valuesDbst.put("wttrfrt", CISData.getwttrfrt());
					valuesDbst.put("acwttrfamt", CISData.getacwttrfamt());
					valuesDbst.put("lowusgran", CISData.getlowusgran());
					valuesDbst.put("highusgran", CISData.gethighusgran());
					Log.d("xml","importdbst54");
					db.insert(TABLE_DBST54, null, valuesDbst);
					Log.d("xml","DBST54");
				}else if (CISData.getwwcode()=="D06"){
					ContentValues valuesDbst = new ContentValues();
					valuesDbst.put("DISCNTCODE", CISData.getdiscntcode());
					valuesDbst.put("DISCNTMEAN", CISData.getdiscntmean());
					valuesDbst.put("DISCNTTYPE", CISData.getdiscnttype());
					valuesDbst.put("DISCNTSRVF", CISData.getdiscntsrvf());
					valuesDbst.put("DISCNTPCEN", CISData.getdiscntpcen());
					valuesDbst.put("DISCNTNUMR", CISData.getdiscntnumr());
					valuesDbst.put("DISCNTDNOM", CISData.getdiscntdnom());
					valuesDbst.put("DISCNTUNIT", CISData.getdiscntunit());
					valuesDbst.put("DISCNTBAHT", CISData.getdiscntbaht());
					Log.d("xml","importdbst06");
					db.insert(TABLE_DBST06, null, valuesDbst);
					Log.d("xml","DBST06");
				}else if (CISData.getwwcode()=="D42"){
					ContentValues valuesDbst = new ContentValues();
					valuesDbst.put("metersize", CISData.getmetersize());
					valuesDbst.put("srvfee", CISData.getsrvfee());;
					Log.d("xml","importdbst42");
					db.insert(TABLE_DBST42, null, valuesDbst);
					Log.d("xml","DBST42");
				}else if (CISData.getprecode()=="EMP"){
					ContentValues valuesEmp = new ContentValues();
					valuesEmp.put("precode", CISData.getprecode());
					valuesEmp.put("wwcode", CISData.getwwcode());
					valuesEmp.put("empid", CISData.getempid());
					valuesEmp.put("empname", CISData.getempname());
					valuesEmp.put("emplastname", CISData.getemplastname());
					valuesEmp.put("empmobile", "");
					valuesEmp.put("empposition", CISData.getempposition());
					Log.d("xml","importEmp");
					db.insert(TABLE_CISEMP, null, valuesEmp);
					Log.d("xml","EMP");
				}else if (CISData.getprecode()==("PDATA")){
					Log.d("insertPdata","OK");
					ContentValues values = new ContentValues();
					values.put("precode", "PDATA");
					values.put("wwcode", CISData.getwwcoded());
					values.put("custcode", CISData.getcustcoded());
					values.put("custname",CISData.getcustnamed());
					values.put("custaddr",CISData.getcustaddrd());
					values.put("corporate_no",CISData.getcorporateno());
					values.put("branch_order_p",CISData.getbranchorder());
					values.put("water_bill_no",CISData.getwaterbillno());
					values.put("paid_date",CISData.getpaiddate());
					values.put("invoice_no",CISData.getinvoiceno());
					values.put("present_meter_date",CISData.getPresentmeterdate());
					values.put("present_meter_count",CISData.getpresentmetercount());
					values.put("present_meter_usg",CISData.getpresentmeterusg());
					values.put("debt_ym",CISData.getdebtym());
					values.put("normal_water_amt",CISData.getnormalwateramt());
					values.put("discount_amt",CISData.getdiscntamt());
					values.put("net_water_amt",CISData.getnetwateramt());
					values.put("service_amt",CISData.getserviceamt());
					values.put("net_water_amt2",CISData.getnetwateramt2());
					values.put("vat_amt",CISData.getvatamt());
					values.put("total_water_amt",CISData.gettotalwateramt());
					values.put("duplicate_amt",CISData.getduplicateamt());
					values.put("paid_channel",CISData.getpaidchannel());
					values.put("channel_name",CISData.getchannelname());
					values.put("bank_acc",CISData.getbankacc());
					values.put("account_page",CISData.getaccountpage());
					values.put("receiver_code",CISData.getreceivercode());
					values.put("receiver",CISData.getreceiver());
					values.put("org_rev",CISData.getorgrev());
					values.put("flag",CISData.getflag());
					Log.d("xml","importPDATA");
					db.insert(TABLE_CISDBP,null,values);
					Log.d("xml","PDATA");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d("CREATE TABLE","Create Table Successfully.");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CISDB);
		db.execSQL("DROP INDEX IF EXISTS CUST_ROUTE");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONSTANT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DBST06);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DBST42);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DBST51);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DBST52);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DBST53);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DBST54);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CISEMP);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CISIMG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CISDBP);
		onCreate(db);
	}

	// Insert Data
	public long InsertData(String strWwcode, String strMtrrdroute, String strCustcode) {
		// TODO Auto-generated method stub

		try {
			SQLiteDatabase db;
			db = this.getWritableDatabase(); // Write Data


			// for API 11 and above
			SQLiteStatement insertCmd;
			String strSQL = "INSERT INTO " + TABLE_CISDB
					+ "(wwcode,mtrrdroute,custcode) VALUES (?,?,?)";

			insertCmd = db.compileStatement(strSQL);
			insertCmd.bindString(1, strWwcode);
			insertCmd.bindString(2, strMtrrdroute);
			insertCmd.bindString(3, strCustcode);
			return insertCmd.executeInsert();



		} catch (Exception e) {
			return -1;
		}

	}


	// Select Data
	public String[] SelectData() {
		// TODO Auto-generated method stub

		try {
			String arrData[] = null;

			SQLiteDatabase db;
			db = this.getReadableDatabase(); // Read Data

			Cursor cursor = db.query(TABLE_CISDB, new String[] { "*" },
					null,
					null, null, null, null, null);

			if(cursor != null)
			{
				if (cursor.moveToFirst()) {
					arrData = new String[cursor.getColumnCount()];
					arrData[0] = cursor.getString(0);
					arrData[1] = cursor.getString(1);
					arrData[2] = cursor.getString(2);
					arrData[3] = cursor.getString(3);
					arrData[4] = cursor.getString(8);
				}
			}
			cursor.close();
			db.close();
			return arrData;

		} catch (Exception e) {
			return null;
		}

	}

	public final Cursor select_CISData(String mRoute ) {

		db = getWritableDatabase();
		Cursor mCursor = db.rawQuery("select * from CISDB where mtrrdroute = '"
				+ mRoute + "' and readflag = '0' order by mtrseq", null);
		try {
			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			db.close();
		}
		return mCursor;
	}

	public final Cursor select_CISPData(String StrP_DisCustcode){

		db = getWritableDatabase();
		Cursor mCursor = db.rawQuery("select * from CISDBP where custcode = '"
				+ StrP_DisCustcode + "'", null);
		try {
			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			db.close();
		}
		return mCursor;

	}

	public final Cursor select_CISDataRead(String mRoute) {

		db = getWritableDatabase();
		Cursor mCursor = db.rawQuery("select * from CISDB where mtrrdroute = '"
				+ mRoute + "' and readflag = '1' order by mtrseq", null);
		try {
			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			db.close();
		}
		return mCursor;
	}

	public final Cursor select_FindCust(String StrFind) {

		db = getWritableDatabase();
		Cursor mCursor = db.rawQuery("select * from CISDB where custcode = '"
				+ StrFind + "'  order by mtrseq", null);
		try {
			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			db.close();
		}
		return mCursor;
	}

	public final Cursor select_FindMeter(String StrFind,String strRoute) {

		db = getWritableDatabase();
		Cursor mCursor = db.rawQuery("select * from CISDB where meterno like '%"
				+ StrFind + "%' and mtrrdroute = '" + strRoute +"'  order by mtrseq", null);
		try {
			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			db.close();
		}
		return mCursor;
	}

	public final Cursor select_FindAddr(String StrFind,String strRoute) {

		db = getWritableDatabase();
		Cursor mCursor = db.rawQuery("select * from CISDB where custaddr like '"
				+ StrFind + "%'  and mtrrdroute = '" + strRoute +"' order by mtrseq", null);
		try {
			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			db.close();
		}
		return mCursor;
	}

	public final Cursor select_FindSeq(String StrFind,String strRoute) {

		db = getWritableDatabase();
		Cursor mCursor = db.rawQuery("select * from CISDB where mtrseq = '"
				+ StrFind + "' and mtrrdroute = '" + strRoute + "' order by mtrseq", null);
		try {
			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			db.close();
		}
		return mCursor;
	}


	public Cursor select_ConstantData() {

		db = getWritableDatabase();
		Cursor mCursor = db.rawQuery("select * from CONSTANT", null);
		try {
			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			db.close();
		}
		return mCursor;
	}



	public Cursor curEmp(){


		db = getReadableDatabase();

		String SQL = "select * from msEmployee Order by empid";
		Cursor mCursor = db.rawQuery(SQL, null);
		try {
			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		} finally{
			db.close();
		}
		return mCursor;
	}

	public Cursor select_EmpData() {

		db = getWritableDatabase();
		Cursor mCursor = db.rawQuery("select * from CISEMP", null);
		try {
			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			db.close();
		}
		return mCursor;
	}

	public Cursor select_ShowRoute(){

		db = getWritableDatabase();
		Cursor mCursor = db.rawQuery("select wwcode,mtrrdroute,"
				+ "count(mtrrdroute) as total from CISDB group by wwcode,mtrrdroute",null);

		try {
			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			db.close();
		}
		return mCursor;

	}

	public Cursor select_SumRoute(String tRoute){
		String sql;
		db = getWritableDatabase();
		sql	=" SELECT COUNT(wwcode) AS sumRoute ";
		sql+=" FROM CISDB ";
		sql+=" WHERE mtrrdroute = '" + tRoute + "' ";
		sql+=" AND READFLAG = '1'";
		Cursor mCursor = db.rawQuery(sql,null);

		try {
			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			db.close();
		}
		return mCursor;

	}

	public Cursor curDbst42(){
		try {
			SQLiteDatabase db;
			db = this.getReadableDatabase();

			String SQL = "select * from Dbst42";
			Cursor cursor = db.rawQuery(SQL, null);

			return cursor;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	public int getCountDbst42(){
		try {
			SQLiteDatabase db;
			db = this.getReadableDatabase();

			String SQL = "select * from Dbst42 ";
			Cursor cursor = db.rawQuery(SQL, null);
			int x = cursor.getCount();
			db.close();
			return x;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}

	}

	public Cursor curDbst51(){
		try {
			SQLiteDatabase db;
			db = this.getReadableDatabase();

			String SQL = "select * from Dbst51 Order by LowUsgRan";
			Cursor cursor = db.rawQuery(SQL, null);

			return cursor;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	public int getCountDbst51(){
		try {
			SQLiteDatabase db;
			db = this.getReadableDatabase();

			String SQL = "select * from Dbst51 Order by LowUsgRan";
			Cursor cursor = db.rawQuery(SQL, null);
			int x = cursor.getCount();
			db.close();
			return x;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}

	}

	public Cursor curDbst52(){
		try {
			SQLiteDatabase db;
			db = this.getReadableDatabase();

			String SQL = "select * from Dbst52 Order by LowUsgRan";
			Cursor cursor = db.rawQuery(SQL, null);

			return cursor;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	public int getCountDbst52(){
		try {
			SQLiteDatabase db;
			db = this.getReadableDatabase();

			String SQL = "select * from Dbst52 Order by LowUsgRan";
			Cursor cursor = db.rawQuery(SQL, null);
			int x = cursor.getCount();
			db.close();
			return x;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}

	}

	public Cursor curDbst53(){
		try {
			SQLiteDatabase db;
			db = this.getReadableDatabase();

			String SQL = "select * from Dbst53 Order by LowUsgRan";
			Cursor cursor = db.rawQuery(SQL, null);

			return cursor;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	public int getCountDbst53(){
		try {
			SQLiteDatabase db;
			db = this.getReadableDatabase();

			String SQL = "select * from Dbst53 Order by LowUsgRan";
			Cursor cursor = db.rawQuery(SQL, null);
			int x = cursor.getCount();
			db.close();
			return x;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}

	}

	public Cursor curDbst54(){
		try {
			SQLiteDatabase db;
			db = this.getReadableDatabase();

			String SQL = "select * from Dbst54 Order by LowUsgRan";
			Cursor cursor = db.rawQuery(SQL, null);

			return cursor;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public int getCountDbst54(){
		try {
			SQLiteDatabase db;
			db = this.getReadableDatabase();

			String SQL = "select * from Dbst54 Order by LowUsgRan";
			Cursor cursor = db.rawQuery(SQL, null);
			int x = cursor.getCount();
			db.close();
			return x;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}

	}

	public Cursor curDbst06(){
		try {
			SQLiteDatabase db;
			db = this.getReadableDatabase();

			String SQL = "select * from Dbst06  order by discntcode";
			Cursor cursor = db.rawQuery(SQL, null);

			return cursor;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public int getCountDbst06(){
		try {
			SQLiteDatabase db;
			db = this.getReadableDatabase();

			String SQL = "select * from Dbst06";
			Cursor cursor = db.rawQuery(SQL, null);
			int x = cursor.getCount();
			db.close();
			return x;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}

	}

	public int getReadCount(String Cust_Code){

		SQLiteDatabase cdb;
		cdb = this.getReadableDatabase();
		String SQL = "select readcount from CISDB where custcode = '" + Cust_Code + "'" ;
		Cursor c = cdb.rawQuery(SQL, null);
		try {

			c.moveToFirst();
			int x = c.getInt(c.getColumnIndex("readcount"));

			c.close();
			return x;
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("Error",String.valueOf(e));

			c.close();
			return 0;

		}

	}

	public String getDisPwaPro(){
		String DisPwaPro="";
		SQLiteDatabase cdb;
		cdb = this.getReadableDatabase();
		String SQL = "select dispwapro from Constant " ;
		Cursor c = cdb.rawQuery(SQL, null);
		try {

			c.moveToFirst();
			DisPwaPro= c.getString(c.getColumnIndex("dispwapro"));
			c.close();

			return DisPwaPro;
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("Error",String.valueOf(e));

			c.close();
			return DisPwaPro;

		}

	}

	public void updateMtRrdData(String Cust_Code,boolean OkPrint,String CustStat,
								String ComMent,String ComMentDec,double NorTrfwt,double SrvFee,
								double Vat,double TotTrfwt,double PrsMtrCnt,double PrsWtUsg,double DiscntAmt,
								double NetTrfWt,String Usgcalmthd,String USER,String ChkDigit,String BillSend,
								String Latitude,String Lontitude,String HLN,double dupamt,
								double dupnet,double dupvat,double debtamt,double debtnet,double debtvat,
								double distempamt,double distempnet,double distempvat) {


		String sql;

		db = getWritableDatabase();

		String BillFlag;
		final Calendar c = Calendar.getInstance();
		Time time = new Time();
		time.setToNow();

		int rCount = 0;
		rCount =getReadCount(Cust_Code);
		rCount = rCount +1;
		Log.d("ReadCount", String.valueOf(rCount));

		int day = c.get(Calendar.DATE);
		int month = c.get(Calendar.MONTH);  //0 = JAN / 11 = DEC
		int year = c.get(Calendar.YEAR);

		month=month+1; //Set int to correct month numeral, i.e 0 = JAN therefore set to 1.

		String MONTH$;
		if (month<=9)  { MONTH$ = "0"+month   ;}
		else {MONTH$ = ""+month;               }    //Set month to MM


		//580605
		String YYYYDDMM = ""+((year+543)-2500)+String.format("%02d",day)+MONTH$;    //Put date ints into string DD/MM/YYYY

		System.out.println("Time==> "+String.format("%02d",time.minute));
		System.out.println("Current time => "+c.getTime());
		//--580605
		//--2/2/2558 17:19
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String formattedDate = df.format(c.getTime());
		String formattedDate2 = day+"/"+MONTH$+"/"+(year+543)+" "+time.hour +":"+ String.format("%02d",time.minute);

		if (OkPrint) {
			BillFlag = "Y" ;
		}else{
			BillFlag = "N";
		}

		if (CustStat.equalsIgnoreCase("")) {
			CustStat = "1";
		}
		sql = "";
		sql +=" UPDATE CISDB ";
		sql += " SET readflag = '1',";
		sql +=	"custstat = '" + CustStat + "',";
		sql += " nortrfwt = '" + String.format("%.2f",NorTrfwt) + "',";
		sql += " srvfee = '" + String.format("%.2f",SrvFee) + "',";
		sql += " vat = '" + String.format("%.2f",Vat) + "',";
		sql += " tottrfwt = '" + String.format("%.2f",TotTrfwt) + "',";
		sql += " prsmtrcnt = '" + PrsMtrCnt + "',";
		sql += " prswtusg = '" + PrsWtUsg + "',";
		sql += " discntamt = '" + String.format("%.2f",DiscntAmt) + "',";
		sql += " NetTrfWt = '" + String.format("%.2f",NetTrfWt) + "',";


		if (ComMent.length() > 2) {
			sql += " comment ='" + ComMent.substring(0, 2) + "',";
		}else{
			sql += " comment ='" + ComMent.toString() + "',";
		}

		if (ComMentDec.length() > 10 ){
			sql += " commentdec ='" + ComMentDec.substring(0, 10) + "',";
		}else{
			sql += " commentdec ='" + ComMentDec.toString() + "',";
		}

		BillSend = "1";
		sql += " billflag  = '" + BillFlag + "',";
		sql += " billsend = '" + BillSend + "',";
		sql += " hln = '" + HLN + "',";
		sql += " latitude  = '" + Latitude + "',";
		sql += " lontitude = '" + Lontitude + "',";
		sql += " prsmtrrddt = '" + YYYYDDMM + "',"; //ปีวันเดือน
		sql += " time = '" + time.hour + String.format("%02d",time.minute) + "',";
		sql += " readcount = '" + rCount +"',";
		sql += " printcount = '1',";
		sql += " usgcalmthd = '" + Usgcalmthd + "',";

		String tmp_prsmtrstat = "";

		if (ComMent.equalsIgnoreCase("01") || ComMent.equalsIgnoreCase("02") || ComMent.equalsIgnoreCase("03")) {
			tmp_prsmtrstat = "2";
		}else if (ComMent.equalsIgnoreCase("15")) {
			tmp_prsmtrstat = "3";
		}else if (ComMent.equalsIgnoreCase("28")) {
			tmp_prsmtrstat = "4";
		}else{
			tmp_prsmtrstat = "1";
		}

		sql += " prsmtrstat = '" + tmp_prsmtrstat + "',";
		sql += " userid = '" + USER + "',";
		sql += " chkdigit = '" + ChkDigit + "',";
		sql += " newread = '1',";
		sql += " readdate = '" + formattedDate2 + "',";
		sql += " dupamt = '" + dupamt + "',";
		sql += " dupnet = '" + dupnet + "',";
		sql += " dupvat = '" + dupvat + "',";
		sql += " debtamt = '" + debtamt + "',";
		sql += " debtnet = '" + debtnet + "',";
		sql += " debtvat = '" + debtvat + "',";
		sql += " distempamt = '" + distempamt + "',";
		sql += " distempnet = '" + distempnet + "',";
		sql += " distempvat = '" + distempvat + "'";
		sql += " WHERE custcode = '" + Cust_Code.trim() + "'";
		Log.d("",sql);
		db.execSQL(sql);
		db.close();
	}


	public void insertIMG(String wwcode,String route,int mtrseq,String custcode,
						  String userid,byte[] img) {



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

		//2015086ปีเดือนวัน
		SQLiteDatabase db = this.getWritableDatabase();
		String datepic = year+MONTH$+day;

		ContentValues values = new ContentValues();
		values.put(COL_WWCODE, wwcode);
		values.put(COL_ROUTE, route);
		values.put(COL_SEQ, mtrseq);
		values.put(COL_CUSTCODE, custcode);
		values.put(COL_USERID, userid);
		values.put(COL_DATE, datepic);
		values.put(COL_IMAGE,img);

		// Inserting Row
		db.insert(TABLE_CISIMG, null, values);
		db.close(); // Closing database connection
	}

}
