package com.rmuti.cpe.rangsiman.busatkorat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.rmuti.cpe.rangsiman.busatkorat.utill.DataBusStop;
import java.util.List;

/**
 * Created by Rangsiman on 4/14/2018.
 */

public class DataBusstopAdapter extends BaseAdapter {
    private Context mContext;
    private List<DataBusStop> mDataList;
    private String[] AlldataName_Thai;


    public DataBusstopAdapter(Context mContext, List<DataBusStop> mDataList) {
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
        return mDataList.get(i).getId_busstop();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        return null;
    }
}