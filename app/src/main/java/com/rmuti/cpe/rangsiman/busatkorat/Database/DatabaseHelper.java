package com.rmuti.cpe.rangsiman.busatkorat.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;

import com.rmuti.cpe.rangsiman.busatkorat.R;
import com.rmuti.cpe.rangsiman.busatkorat.utill.DataBusStop;
import com.rmuti.cpe.rangsiman.busatkorat.utill.DataList;
import com.rmuti.cpe.rangsiman.busatkorat.utill.DataRoute;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 26/8/2560.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DBName = "BusDatabase.sqlite";
    public static final String DBLocation = "data/data/com.rmuti.cpe.rangsiman.busatkorat/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private byte[] img_front_bus;
    private byte[] ic_bus;
    private byte[] img_busstop;

    public DatabaseHelper(Context context) {
        super(context, DBName, null, 1);
        this.mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBName).getPath();
        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public List<DataList> getBusListData() {
        DataList data = null;
        List<DataList> dataList = new ArrayList<>();
        openDatabase();
        String SQL_Query = mContext.getString(R.string.SQL_query_BusListData);
        Cursor cursor = mDatabase.rawQuery(SQL_Query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            img_front_bus = cursor.getBlob(2);
            Bitmap img_bitmap = BitmapFactory.decodeByteArray(img_front_bus, 0, img_front_bus.length);


            ic_bus = cursor.getBlob(3);
            Bitmap ic_bitmap = BitmapFactory.decodeByteArray(ic_bus, 0, ic_bus.length);

            data = new DataList(cursor.getInt(0), cursor.getString(1), img_bitmap, ic_bitmap,cursor.getString(4),cursor.getInt(5),cursor.getString(6),cursor.getString(7));
            dataList.add(data);
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();
        return dataList;
    }


    public List<DataList> getBusListDataOnIndex(String str) {
        DataList data = null;
        List<DataList> dataList = new ArrayList<>();
        openDatabase();
        String SQL_Query = mContext.getString(R.string.SQL_query_BusListData);


        String[] ArrBus = str.split(",");
        String CaseSQL = "ORDER BY CASE id_bus";
        for (int i=0;i<ArrBus.length;i++){
            CaseSQL += " WHEN " + ArrBus[i] + " THEN " + i;
        }


        CaseSQL += " END";


        Cursor cursor = mDatabase.rawQuery(SQL_Query + " WHERE id_bus IN (" + str +")" + CaseSQL , null);




        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            img_front_bus = cursor.getBlob(2);
            Bitmap img_bitmap = BitmapFactory.decodeByteArray(img_front_bus, 0, img_front_bus.length);


            ic_bus = cursor.getBlob(3);
            Bitmap ic_bitmap = BitmapFactory.decodeByteArray(ic_bus, 0, ic_bus.length);

            data = new DataList(cursor.getInt(0), cursor.getString(1), img_bitmap, ic_bitmap,cursor.getString(4),cursor.getInt(5),cursor.getString(6),cursor.getString(7));
            dataList.add(data);
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();
        return dataList;
    }


    public List<DataBusStop> getBusStopListData() {
        DataBusStop data = null;
        List<DataBusStop> dataBusList = new ArrayList<>();
        openDatabase();
        String SQL_Query = mContext.getString(R.string.SQL_query_BusStopListData);
        Cursor cursor = mDatabase.rawQuery(SQL_Query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            data = new DataBusStop(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3));
            dataBusList.add(data);
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();
        return dataBusList;
    }

    public List<DataRoute> getRouteListData() {
        DataRoute data = null;
        List<DataRoute> dataRouteList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Route", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            data = new DataRoute(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2));
            dataRouteList.add(data);
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();
        return dataRouteList;
    }

    public String[] getBusstopFromRoute(int index){

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            Cursor cursor = db.rawQuery("SELECT id_busstop FROM Route WHERE id_bus = " + index, null);

            if(cursor != null)
            {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];
                    /***
                     *  [x] = Name
                     */
                    int i= 0;
                    do {
                        arrData[i] = cursor.getString(0);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();

            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    // Select All Data
    public String[] getBusstopNameData() {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = mContext.getString(R.string.SQL_query_nameBusStop);
            Cursor cursor = db.rawQuery(strSQL, null);

            if(cursor != null)
            {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];
                    /***
                     *  [x] = Name
                     */
                    int i= 0;
                    do {
                        arrData[i] = cursor.getString(0);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();

            return arrData;

        } catch (Exception e) {
            return null;
        }

    }


}

