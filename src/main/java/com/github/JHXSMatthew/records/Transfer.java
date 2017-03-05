package com.github.JHXSMatthew.records;

import org.json.simple.JSONObject;

/**
 * Created by Matthew on 26/02/2017.
 */
public class Transfer extends Record {
    private String payee;
    protected final static String TYPE_NAME = "transfer";


    public Transfer(int number, String date, String payer, double amount,String memo,String payee,String ip) {
        super(number, date, payer,amount, memo,ip);
        this.payee = payee;
    }

    protected Transfer(JSONObject obj){
        super(obj);
        payee = obj.get("payee").toString();
    }
/*
    protected Transfer(Record record,String payee) {
        super(record.getNumber(), record.getDate(), record.getPayer(),record.getAmount(), record.getMemo());
        this.payee = payee;
    }
*/

    @Override
    public JSONObject toJSON(){
        JSONObject obj = super.toJSON();
        obj.put("payee",payee);
        obj.put("type",TYPE_NAME);

        return obj;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }
}
