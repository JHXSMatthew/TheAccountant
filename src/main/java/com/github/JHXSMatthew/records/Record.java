package com.github.JHXSMatthew.records;


import org.json.simple.JSONObject;

/**
 * Created by Matthew on 26/02/2017.
 */
public class Record {

    protected final static String TYPE_NAME = "record";
    private int id = -1;
    private double amount;
    private String date;
    private String payer;
    private String memo;
    private String ip;


    protected Record(int id, String date, String payer, double amount, String memo, String ip) {
        this.id = id;
        this.date = date;
        this.payer = payer;
        this.memo = memo;
        this.amount = amount;
        this.ip = ip;
    }

    protected Record(JSONObject obj) {
        this.id = (Integer) obj.get("id");
        this.date = obj.get("date").toString();
        this.payer = obj.get("payer").toString();
        this.memo = obj.get("memo").toString();
        this.amount = (Double) obj.get("amount");
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("amount", amount);
        obj.put("date", date);
        obj.put("payer", payer);
        obj.put("memo", memo);
        obj.put("type", TYPE_NAME);
        return obj;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


}
