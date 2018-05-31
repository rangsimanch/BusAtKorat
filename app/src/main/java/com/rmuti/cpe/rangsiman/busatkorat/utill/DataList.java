package com.rmuti.cpe.rangsiman.busatkorat.utill;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 26/8/2560.
 */

public class DataList {
    private int id_bus;
    private String name_bus;
    private Bitmap img_front_bus;
    private Bitmap ic_bus;
    private String name_busstop;
    private int price;
    private String time_start;
    private String time_stop;

    public DataList(int id_bus, String name_bus, Bitmap img_front_bus, Bitmap ic_bus,String name_busstop,int price,String time_start, String time_stop){
        this.id_bus = id_bus;
        this.name_bus = name_bus;
        this.img_front_bus = img_front_bus;
        this.ic_bus = ic_bus;
        this.name_busstop = name_busstop;
        this.time_start = time_start;
        this.time_stop = time_stop;
        this.price = price;
    }


    public String getName_busstop() {
        return name_busstop;
    }

    public void setName_busstop(String name_busstop) {
        this.name_busstop = name_busstop;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_stop() {
        return time_stop;
    }

    public void setTime_stop(String time_stop) {
        this.time_stop = time_stop;
    }

    public int getId_bus() {
        return id_bus;
    }

    public void setId_bus(int id_bus) {
        this.id_bus = id_bus;
    }

    public String getName_bus() {
        return name_bus;
    }

    public void setName_bus(String name_bus_thai) {
        this.name_bus = name_bus_thai;
    }

    public Bitmap getImg_front_bus() {

        return img_front_bus;
    }

    public void setImg_front_bus(Bitmap img_front_bus) {
        this.img_front_bus = img_front_bus;
    }

    public Bitmap getIc_bus() {
        return ic_bus;
    }

    public void setIc_bus(Bitmap ic_bus) {

        this.ic_bus = ic_bus;
    }

    public String getBusstop() {
        return name_busstop;
    }

    public void setBusstop(String busstop_thai) {
        this.name_busstop = busstop_thai;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
