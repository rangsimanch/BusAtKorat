package com.rmuti.cpe.rangsiman.busatkorat.utill;

public class DataRoute {
    private int id_route;
    private int id_bus;
    private int id_busstop;

    public DataRoute(int id_route,int id_bus,int id_busstop){
        this.id_route = id_route;
        this.id_bus = id_bus;
        this.id_busstop = id_busstop;
    }


    public int getId_route() {
        return id_route;
    }

    public void setId_route(int id_route) {
        this.id_route = id_route;
    }

    public int getId_bus() {
        return id_bus;
    }

    public void setId_bus(int id_bus) {
        this.id_bus = id_bus;
    }

    public int getId_busstop() {
        return id_busstop;
    }

    public void setId_busstop(int id_busstop) {
        this.id_busstop = id_busstop;
    }
}
