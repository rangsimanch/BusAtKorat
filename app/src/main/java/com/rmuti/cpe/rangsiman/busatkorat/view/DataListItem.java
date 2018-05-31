package com.rmuti.cpe.rangsiman.busatkorat.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.inthecheesefactory.thecheeselibrary.view.BaseCustomViewGroup;
import com.inthecheesefactory.thecheeselibrary.view.state.BundleSavedState;
import com.rmuti.cpe.rangsiman.busatkorat.R;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class DataListItem extends BaseCustomViewGroup {
    TextView textName;
    TextView textbusstopStart;
    TextView textbusstopEnd;
    ImageView imageBus;
    ImageView imageFrontBus;
    String txtPrice;
    TextView txtService;

    public DataListItem(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public DataListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public DataListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }


    @TargetApi(21)
    public DataListItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.list_item_bus, this);
    }

    private void initInstances() {
        // findViewById here
        textName = (TextView) getRootView().findViewById(R.id.textName);
        textbusstopStart = (TextView) getRootView().findViewById(R.id.textbusstopStart);
        textbusstopEnd = (TextView) getRootView().findViewById(R.id.textbusstopEnd);
        imageBus = (ImageView) getRootView().findViewById(R.id.imageBus);
        txtService = (TextView) getRootView().findViewById(R.id.txtservice);
    }

    public String getTxtService() {
        return txtService.getText().toString();
    }

    public void setTxtService(String txtService) {
        this.txtService.setText(txtService);
    }

    public void setTxtServiceColor(String Color) {
        this.txtService.setTextColor(android.graphics.Color.parseColor(Color));
    }

    public String getTextbusstopEnd() {
        return textbusstopEnd.getText().toString();
    }

    public void setTextbusstopEnd(String textBusstopEnd) {
        this.textbusstopEnd.setText(textBusstopEnd);
    }

    public String getTextName() {
        return textName.getText().toString();
    }

    public void setTextName(String textName) {
        this.textName.setText(textName);
    }

    public void setTextPrice(String textPrice) {
        this.txtPrice = txtPrice;
    }

    public String getTextPrice() {
        return txtPrice;
    }

    public String getTextBusstopStart() {
        return textbusstopStart.getText().toString();
    }

    public void setTextBusstopStart(String textBusstopStart) {
        this.textbusstopStart.setText(textBusstopStart);
    }

    public ImageView getImageBus() {
        return imageBus;
    }

    public void setImageBus(Bitmap imageBus) {
        this.imageBus.setImageBitmap(imageBus);
    }


    public void setImageFrontBus(Bitmap imageFrontBus) {
        this.imageFrontBus.setImageBitmap(imageFrontBus);
    }

    public ImageView getImageFrontBus() {
        return imageFrontBus;
    }

    private void initWithAttrs(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        /*
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StyleableName,
                defStyleAttr, defStyleRes);

        try {

        } finally {
            a.recycle();
        }
        */
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        BundleSavedState savedState = new BundleSavedState(superState);
        // Save Instance State(s) here to the 'savedState.getBundle()'
        // for example,
        // savedState.getBundle().putString("key", value);

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        BundleSavedState ss = (BundleSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        Bundle bundle = ss.getBundle();
        // Restore State from bundle here
    }

}
