package com.railway.ui.window.common.entity;

public class Station {
    private int id;
    private String name;

    public Station(int id, String name) {
        this.id = id;
        this.name= name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Station))
            return false;
        Station other = (Station) obj;
        return id == other.id;
    }
}
