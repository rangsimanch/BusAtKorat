package com.rmuti.cpe.rangsiman.busatkorat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.rmuti.cpe.rangsiman.busatkorat.R;
import com.rmuti.cpe.rangsiman.busatkorat.utill.DataList;
import com.rmuti.cpe.rangsiman.busatkorat.view.DataListItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Admin on 8/22/2017.
 */

public class DataListAdapter extends BaseAdapter {

    private Context mContext;
    private List<DataList> mDataList;
    private DataListItem dataListItem;

    private Calendar fromTime;
    private Calendar toTime;
    private Calendar currentTime;


    public DataListAdapter(Context mContext, List<DataList> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mDataList.get(i).getId_bus();
    }

    public boolean checkTime(String time_start,String time_stop) {
        try {

            SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
            Date ten = parser.parse(time_start);
            Date eighteen = parser.parse(time_stop);
            Calendar calendar = Calendar.getInstance();
            int Hr24 = calendar.get(Calendar.HOUR_OF_DAY);
            int Min = calendar.get(Calendar.MINUTE);

            String currentTime = Hr24 + ":" + Min;

            Date userDate = parser.parse(currentTime);
                if (userDate.after(ten) && userDate.before(eighteen)) {
                    return true;
                }
        } catch (Exception e) {
            return false;
        }
        return false;
    }



    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        DataListItem item;
        if (view != null){
            item = (DataListItem) view;
        }
        else
            item = new DataListItem(viewGroup.getContext());



        item.setTextName(mDataList.get(i).getName_bus());

        String service;
        String busstopStart;
        String busstopEnd;
        String Price;

        if(!mDataList.get(i).getBusstop().toString().equals("")) {
            busstopStart = mContext.getString(R.string.bus_start) + " " + mDataList.get(i).getBusstop().toString()
                    .substring(0, mDataList.get(i).getBusstop().toString().indexOf("-")).toString();

            busstopEnd = mContext.getString(R.string.bus_end) + " " + mDataList.get(i).getBusstop().toString()
                    .substring(mDataList.get(i).getBusstop().toString().lastIndexOf("-") + 2,
                            mDataList.get(i).getBusstop().toString().length()).toString();
        }

/*
        if(!mDataList.get(i).getBusstop().toString().equals("")) {
            busstopStart = "ต้นสาย :" + " " + mDataList.get(i).getBusstop().toString()
                    .substring(0, mDataList.get(i).getBusstop().toString().indexOf("-")).toString();

            busstopEnd = "ปลายสาย :" + " " + mDataList.get(i).getBusstop().toString()
                    .substring(mDataList.get(i).getBusstop().toString().lastIndexOf("-") + 2,
                            mDataList.get(i).getBusstop().toString().length()).toString();
        }
*/
        else {
            busstopEnd = "";
            busstopStart = "";
        }

        if(checkTime(mDataList.get(i).getTime_start(),mDataList.get(i).getTime_stop())){
            item.setTxtService(mContext.getString(R.string.timeopen));
            item.setTxtServiceColor("#008000");
        }
        else{
            item.setTxtService(mContext.getString(R.string.timeout));
            item.setTxtServiceColor("#ff0000");
        }

        item.setTextBusstopStart(busstopStart);
        item.setTextbusstopEnd(busstopEnd);
        item.setImageBus(mDataList.get(i).getIc_bus());


        return item;
    }
}
