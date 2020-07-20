package com.railway.ui.window.common.entity;

public class Route {
    private int routeId;
    private Station startStation;
    private Station finishStation;
    private String routeType;

    public Route(int id,
                 Station start,
                 Station finish,
                 String type) {
        this.routeId = id;
        this.startStation = start;
        this.finishStation = finish;
        this.routeType = type;
    }

    public int getRouteId() {
        return routeId;
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getFinishStation() {
        return finishStation;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public void setStartStation(Station startStation) {
        this.startStation = startStation;
    }

    public void setFinishStation(Station finishStation) {
        this.finishStation = finishStation;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    @Override
    public String toString() {
        String format = "%d: %s - %s";
        return String.format(format, routeId,
                startStation.getName(),
                finishStation.getName());
    }
}
