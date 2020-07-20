package com.railway.ui.window.entrance.pages.tickets;

import com.railway.ui.window.entrance.pages.passengers.Passenger;

public class Ticket {
    private Passenger owner;
    private String id;
    private String operationDate;
    private String purchased;
    private double cost;
    private double duration;

    private String ownerLastName;
    private String ownerFirstName;
    private String ownerSecondName;

    public Ticket(String id,
                  String operationDate,
                  String purchased,
                  double cost,
                  double duration,
                  Passenger owner) {

        this.id = id;
        this.operationDate = operationDate;
        this.purchased = purchased;

        this.cost = cost;
        this.duration = duration;

        this.owner = owner;
        this.ownerLastName = owner.getLastName();
        this.ownerFirstName = owner.getFirstName();
        this.ownerSecondName = owner.getSecondName();
    }

    public String getId() {
        return id;
    }

    public double getDuration() {
        return duration;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public String getOwnerFirstName() {
        return ownerFirstName;
    }

    public String getOwnerSecondName() {
        return ownerSecondName;
    }

    public double getCost() {
        return cost;
    }

    public String getPurchased() {
        return purchased;
    }

    public String getOperationDate() {
        return operationDate;
    }

    @Override
    public String toString() {
        final char sep = ' ';
        final char comma = ',';
        StringBuilder builder;
        builder = new StringBuilder();
        builder.append(id).append(sep);

        builder.append("owner:").append(sep);
        builder.append(owner.getLastName()).append(sep);
        builder.append(owner.getFirstName()).append(sep);
        builder.append(owner.getSecondName()).append(sep);

        builder.append(comma).append(sep);
        builder.append("purchased:").append(sep).append(purchased);

        builder.append(comma).append(sep);
        builder.append("purchase date:").append(sep).append(operationDate);
        return  builder.toString();
    }
}
