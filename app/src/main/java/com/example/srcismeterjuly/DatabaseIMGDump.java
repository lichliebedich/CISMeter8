package com.example.srcismeterjuly;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DatabaseIMGDump {
	public DatabaseIMGDump(SQLiteDatabase db,String destXml) {
		mDb = db;
		mDestXmlFilename=destXml;

		try {
			// create a file on sdcard1 to export the
			// database contents to
			File myFile = new File(mDestXmlFilename);
			myFile.createNewFile();

			FileOutputStream fOut = new FileOutputStream(myFile);
			BufferedOutputStream bos = new BufferedOutputStream(fOut);

			mExporter = new Exporter(bos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void exportData() {

		try {
			// mExporter.startDbExport(mDb.getPath());

			// get the tables out of the given sqlite database
			String sql = "SELECT * FROM sqlite_master";

			Cursor cur = mDb.rawQuery(sql, new String[0]);
			cur.moveToFirst();

			String tableName;
			while (cur.getPosition() < cur.getCount()) {
				tableName = cur.getString(cur.getColumnIndex("name"));
				Log.d("table",tableName);
				// don't process these two tables since they are used
				// for metadata
				//if (!tableName.equals("android_metadata")
				//&& !tableName.equals("sqlite_sequence")) {
				if(tableName.equals("CISIMG")){

					Log.d(">>", tableName);
					exportTable(tableName);
				}
				//Log.d(">>", cur.getString(cur.getColumnIndex("custcode")));
				cur.moveToNext();
			}
			// mExporter.endDbExport();
			mExporter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void exportTable(String tableName) throws IOException {
		mExporter.startTable(tableName);

		// get everything from the table
		String sql = "SELECT * FROM CISIMG";
		Cursor cur = mDb.rawQuery(sql, new String[0]);
		int numcols = cur.getColumnCount();
		Log.d("จำนวนคอลัมภ์",String.valueOf(numcols));
		cur.moveToFirst();

		// move through the table, creating rows
		// and adding each column with name and value
		// to the row
		while (cur.getPosition() < cur.getCount()) {
			mExporter.startRow();
			String name;
			String val;
			byte[] valimg;
			//ไม่เอาคอลัมภ์แรกDATA
			for (int idx = 0; idx < numcols; idx++){
				if (idx==numcols-1){
					//Log.d(">>","ฟิลล์รูปภาพ");

					name = cur.getColumnName(idx);
					valimg = cur.getBlob(idx);
					String encodedImage = Base64.encodeToString(valimg, Base64.DEFAULT);
					// Log.d("", encodedImage.toString());
					mExporter.addColumn(name, encodedImage);
				}
				else{
					// Log.d("จำนวนคอลัมภ์",String.valueOf(idx));
					name = cur.getColumnName(idx);
					val = cur.getString(idx);

					mExporter.addColumn(name, val);
				}
			}//ปิดfor

			mExporter.endRow();
			cur.moveToNext();
		}//ปิดwhile

		cur.close();

		mExporter.endTable();
	}

	private String mDestXmlFilename = "/CIS/exportimg.xml";

	private SQLiteDatabase mDb;
	private Exporter mExporter;

	class Exporter {
		private static final String CLOSING_WITH_TICK = ">";
		private static final String START_DB = "<export-database name='";
		private static final String END_DB = "</>";
		private static final String START_TABLE = "<table name='";
		private static final String END_TABLE = "</table>";
		private static final String START_ROW = "<row>";
		private static final String END_ROW = "</row>";
		private static final String START_COL = "<";
		private static final String END_COL = "</col>";

		private BufferedOutputStream mbufferos;

		public Exporter() throws FileNotFoundException {
			this(new BufferedOutputStream(new FileOutputStream(mDestXmlFilename)));

		}

		public Exporter(BufferedOutputStream bos) {
			mbufferos = bos;
		}

		public void close() throws IOException {
			if (mbufferos != null) {
				mbufferos.close();
			}
		}

		public void startDbExport(String dbName) throws IOException {
			String stg = "<table>";
			mbufferos.write(stg.getBytes());
		}

		public void endDbExport() throws IOException {
			mbufferos.write(END_DB.getBytes());
		}

		public void startTable(String tableName) throws IOException {
			String stg = "<" + tableName + ">";
			mbufferos.write(stg.getBytes());
		}

		public void endTable() throws IOException {
			String stg = "</CISIMG>";
			mbufferos.write(stg.getBytes());
		}

		public void startRow() throws IOException {
			mbufferos.write(START_ROW.getBytes());
		}

		public void endRow() throws IOException {
			mbufferos.write(END_ROW.getBytes());
		}

		public void addColumn(String name, String val) throws IOException {

			String stg = START_COL + name + CLOSING_WITH_TICK + val + "</"+name+">";
			mbufferos.write(stg.getBytes());
		}

		public void addColumnIMG(String name, byte[] val) throws IOException {

			String stg = START_COL + name + CLOSING_WITH_TICK + val + "</"+name+">";
			mbufferos.write(stg.getBytes());
		}
	}
}
