package com.rmuti.cpe.rangsiman.busatkorat.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.rmuti.cpe.rangsiman.busatkorat.Database.DatabaseHelper;
import com.rmuti.cpe.rangsiman.busatkorat.R;
import com.rmuti.cpe.rangsiman.busatkorat.adapter.DataListAdapter;
import com.rmuti.cpe.rangsiman.busatkorat.utill.DataList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;


/**
 * Created by nuuneoi on 11/16/2014.
 */
public class DataFragment extends Fragment {

    private Calendar fromTime;
    private Calendar toTime;
    private Calendar currentTime;

    private ListView listView;
    private DataListAdapter listAdapter;
    private List<DataList> mDataList;
    private DatabaseHelper mDBHelper;
    private DataListAdapter item;
    private Switch sw_timefilter;
    public DataFragment() {
        super();
    }

    private boolean copyDatabase(Context context){
        try {
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBName);
            String outFileName = DatabaseHelper.DBLocation + DatabaseHelper.DBName;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff =  new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buff)) > 0){
                outputStream.write(buff, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            Log.v("DataFragment","DB Copied");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static DataFragment newInstance() {
        DataFragment fragment = new DataFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_data, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    @SuppressWarnings("UnusedParameters")
    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void
    initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        // Note: State of variable initialized here could not be saved
        //       in onSavedInstanceState

        listView = (ListView) rootView.findViewById(R.id.listView);
        mDBHelper = new DatabaseHelper(getContext());


        File database = getActivity().getDatabasePath(DatabaseHelper.DBName);
        if(false == database.exists()){
            mDBHelper.getReadableDatabase();
            if (copyDatabase(getContext())){
                Toast.makeText(getContext(), "Copy Success", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(), "Copy Fail", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        mDataList = mDBHelper.getBusListData();
        listAdapter = new DataListAdapter(getContext(),mDataList);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());

                View mView = getLayoutInflater().inflate(R.layout.popup_list, null);
                ImageView imgBus = (ImageView) mView.findViewById(R.id.imgBus);
                ImageView icBus = (ImageView) mView.findViewById(R.id.icBus);
                TextView txtName = (TextView) mView.findViewById(R.id.txtName);
                TextView txtBusstop = (TextView) mView.findViewById(R.id.txtBusstop);
                TextView txtPrice = (TextView) mView.findViewById(R.id.txtPrice);
                TextView txtTime = (TextView) mView.findViewById(R.id.txtTime);

                txtTime.setText( getContext().getString(R.string.time) + " " + mDataList.get(i).getTime_start() + " - " + mDataList.get(i).getTime_stop());
                imgBus.setImageBitmap(mDataList.get(i).getImg_front_bus());
                icBus.setImageBitmap(mDataList.get(i).getIc_bus());
                txtName.setText(mDataList.get(i).getName_bus());
                txtBusstop.setText(mDataList.get(i).getBusstop());
                if(mDataList.get(i).getId_bus() == 20){
                    txtPrice.setText(getContext().getString(R.string.price) + getContext().getString(R.string.price_special) + " 8 " + getContext().getString(R.string.baht));
                }
                else {
                    txtPrice.setText(getContext().getString(R.string.price) + " 8 " + getContext().getString(R.string.baht));
                }


                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance (Fragment level's variables) State here
    }

    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance (Fragment level's variables) State here
    }

    public boolean checkTime(String time_start,String time_stop) {
        try {

            String from = time_start;
            String until = time_stop;

            Date timefrom = new SimpleDateFormat("HH:mm").parse(from);
            fromTime = Calendar.getInstance();
            fromTime.setTime(timefrom);

            Date timeuntil = new SimpleDateFormat("HH:mm").parse(until);
            toTime = Calendar.getInstance();
            toTime.setTime(timeuntil);
            toTime.add(Calendar.DATE,1);

            Date getTime = Calendar.getInstance().getTime();
            String now = getTime.toString();
            Date nowTime = new SimpleDateFormat("HH:mm").parse(now);
            currentTime = Calendar.getInstance();
            currentTime.setTime(nowTime);
            currentTime.add(Calendar.DATE,1);

            Date x = currentTime.getTime();
            if(x.after(fromTime.getTime())&&x.before(toTime.getTime())){
                return true;
            }

        } catch (Exception e) {
            return false;
        }
        return false;
    }

}
