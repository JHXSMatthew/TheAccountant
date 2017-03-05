package com.github.JHXSMatthew.records;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Arrays;


/**
 * Created by Matthew on 26/02/2017.
 */
public class Transaction extends Record {
    private String[] debitTo;
    protected final static String TYPE_NAME = "transaction";

    public Transaction(int number, String date, String payer,double amount, String memo,String ip,String... debitTo) {
        super(number, date, payer,amount, memo,ip);
        this.debitTo = debitTo;
    }

    protected Transaction(JSONObject obj){
        super(obj);
        debitTo = (String[])((JSONArray)(obj.get("debitTo")))
                .toArray(new String[((JSONArray)obj.get("debitTo")).size()]);
    }
/*
    protected Transaction(Record record,String... debitTo) {
        super(record.getNumber(), record.getDate(), record.getPayer(), record.getMemo());
        this.debitTo = debitTo;
    }
    */

    @Override
    public JSONObject toJSON(){
        JSONObject obj = super.toJSON();
        JSONArray array = new JSONArray();
        for(int i = 0 ; i < debitTo.length; i ++)
            array.add(debitTo[i]);

        obj.put("debitTo",array);
        obj.put("type",TYPE_NAME);

        return obj;
    }


    public String getDebitToString() {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(debitTo).forEach(str -> {
            builder
                    .append(str)
                    .append(",");
        });
        return builder.toString().substring(0,builder.length() - 1);
    }

    public String[] getDebitToArray() {
        return debitTo;
    }
}
