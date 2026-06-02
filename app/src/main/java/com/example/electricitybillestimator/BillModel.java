package com.example.electricitybillestimator;

public class BillModel {

    String id, month;
    int unit;
    int rebate;

    double totalCharges;
    double finalCost;

    public BillModel() {

    }

    public BillModel(String id,
                     String month,
                     int unit,
                     int rebate,
                     double totalCharges,
                     double finalCost) {

        this.id = id;
        this.month = month;
        this.unit = unit;
        this.rebate = rebate;
        this.totalCharges = totalCharges;
        this.finalCost = finalCost;
    }

    public String getId() {
        return id;
    }

    public String getMonth() {
        return month;
    }

    public int getUnit() {
        return unit;
    }

    public int getRebate() {
        return rebate;
    }

    public double getTotalCharges() {
        return totalCharges;
    }

    public double getFinalCost() {
        return finalCost;
    }
}