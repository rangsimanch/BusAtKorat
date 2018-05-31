package com.rmuti.cpe.rangsiman.busatkorat.utill;

/**
 * Created by Rangsiman on 3/25/2018.
 */

public class DataBusStop {
    private int id_busstop;
    private String name_busstop;
    private Double latitude_busstop;
    private Double longtitude_busstop;

    public DataBusStop(int id_busstop, String name_busstop, Double latitude_busstop, Double longtitude_busstop){
        this.id_busstop = id_busstop;
        this.name_busstop = name_busstop;
        this.latitude_busstop = latitude_busstop;
        this.longtitude_busstop = longtitude_busstop;
    }

    public int getId_busstop() {
        return id_busstop;
    }

    public void setId_busstop(int id_busstop) {
        this.id_busstop = id_busstop;
    }

    public String getName_busstop() {
        return name_busstop;
    }

    public void setName_busstop(String name_busstop_thai) {
        this.name_busstop = name_busstop_thai;
    }

    public Double getLatitude_busstop() {
        return latitude_busstop;
    }

    public void setLatitude_busstop(Double latitude_busstop) {
        this.latitude_busstop = latitude_busstop;
    }

    public Double getLongtitude_busstop() {
        return longtitude_busstop;
    }

    public void setLongtitude_busstop(Double longtitude_busstop) {
        this.longtitude_busstop = longtitude_busstop;
    }
}
