package com.example.srcismeterjuly;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DatabaseDump {
	public DatabaseDump(SQLiteDatabase db,String destXml) {
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

				if(tableName.equals("CISDB")){

					exportTable(tableName);
				}

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
		String sql = "select * from CISDB where readflag = 1";
		Cursor cur = mDb.rawQuery(sql, new String[0]);
		int numcols = cur.getColumnCount();

		cur.moveToFirst();

		while (cur.getPosition() < cur.getCount()) {
			mExporter.startRow();
			String name;
			String val;
			double val2;
			//ไม่เอาคอลัมภ์แรกDATA
			for (int idx = 1; idx < numcols; idx++) {
				name = cur.getColumnName(idx);
				val = cur.getString(idx);
				val2 = cur.getDouble(idx);

				if (idx>=36 && idx<=41){
					val = String.valueOf(val2);
				}
				mExporter.addColumn(name, val);
			}
			mExporter.endRow();
			cur.moveToNext();
		}

		cur.close();

		mExporter.endTable();
	}

	private String mDestXmlFilename = "/sdcard/export.xml";

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
			String stg = "</CISDB>";
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
	}
}
