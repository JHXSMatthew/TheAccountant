package com.github.JHXSMatthew.records;

import com.github.JHXSMatthew.DataCache;
import com.github.JHXSMatthew.sql.MySQLConnection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Created by Matthew on 26/02/2017.
 */
public class RecordsManager {


    private static RecordsManager instance;
    private DataCache<String,Double> balanceCache;

    public RecordsManager(){
        getBalanceCache();
    }


    public Transaction createNewTransaction(Transaction transaction){
        MySQLConnection connection = new MySQLConnection();
        try {
            PreparedStatement statement = connection.connect()
                    .prepareStatement("INSERT INTO Records (date,amount,payer,ip,debitTo,memo) " +
                            "VALUES (?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,transaction.getDate());
            statement.setDouble(2,transaction.getAmount());
            statement.setString(3,transaction.getPayer());
            statement.setString(4,transaction.getIp());
            statement.setString(5,transaction.getDebitToString());
            statement.setString(6,transaction.getMemo());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()){
                transaction.setId(rs.getInt(1));
            }else{
                System.err.println("no id exception!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.disconnect();
        updateBalance(transaction.getPayer(),transaction.getAmount(),transaction.getDebitToArray());
        return transaction;
    }

    public Transfer createNewTransfer(Transfer transfer){
        MySQLConnection connection = new MySQLConnection();
        try{
            PreparedStatement statement = connection.connect()
                    .prepareStatement("INSERT INTO Records (date,amount,payer,payee,ip,memo) " +
                            "VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,transfer.getDate());
            statement.setDouble(2,transfer.getAmount());
            statement.setString(3,transfer.getPayer());
            statement.setString(4,transfer.getPayee());
            statement.setString(5,transfer.getIp());
            statement.setString(6,transfer.getMemo());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()){
                transfer.setId(rs.getInt(1));
            }else{
                System.err.println("no id exception!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        connection.disconnect();
        updateBalance(transfer.getPayer(),transfer.getAmount(),transfer.getPayee());
        return transfer;
    }

    public Record[] getRecords(String starting, String end){
        MySQLConnection connection = new MySQLConnection();

        try {
            PreparedStatement statement;
            if(starting == null && end == null) {
                statement = connection.connect().prepareStatement("SELECT * FROM Records ORDER BY id DESC LIMIT 10");
            }else{
                statement = connection.connect().prepareStatement("SELECT * FROM Records WHERE DATE_FORMAT(date,\"%Y-%m-%d\") BETWEEN ? AND ?");
                statement.setString(1,starting);
                statement.setString(2,end);
            }

            List<Record> list = new ArrayList<>();
            ResultSet set = statement.executeQuery();
            while(set.next()){
                if(set.getString("payee") == null ){
                    //transaction
                    list.add(new Transaction(
                            set.getInt("id")
                            ,set.getString("date")
                            ,set.getString("payer")
                            ,set.getDouble("amount")
                            ,set.getString("memo")
                            ,set.getString("ip")
                            ,set.getString("debitTo").split(",")));
                }else{
                    //transfer
                    list.add(new Transfer(
                            set.getInt("id")
                            ,set.getString("date")
                            ,set.getString("payer")
                            ,set.getDouble("amount")
                            ,set.getString("memo")
                            ,set.getString("payee")
                            ,set.getString("ip")
                    ));
                }
            }
            return list.toArray(new Record[list.size()]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.disconnect();
        return null;
    }

    public void updateBalance(String payer, double amount,Object obj){
        if(balanceCache.containsKey(payer))
            balanceCache.put(payer,balanceCache.get(payer) + amount);
        else
            balanceCache.put(payer,amount);

        if(obj instanceof String){
            //transfer
            if(balanceCache.containsKey((String) obj))
                balanceCache.put((String) obj,balanceCache.get((String) obj) - amount);
            else
                balanceCache.put((String) obj,-amount);
        }else{
            Arrays.stream((String[])obj).forEach(entity -> {
                if(balanceCache.containsKey(entity))
                    balanceCache.put(entity,balanceCache.get(entity) - amount/((String[])obj).length);
                else
                    balanceCache.put(entity,-amount/((String[])obj).length);
            });
            //transaction
        }
    }

    public DataCache<String, Double> getBalanceCache() {
        if(balanceCache == null || !balanceCache.isValid()){
            balanceCache = new DataCache<>();
            MySQLConnection connection = new MySQLConnection();
            try {
                PreparedStatement statement = connection.connect()
                        .prepareStatement("SELECT amount,payer,payee,debitTo FROM Records");
                ResultSet set = statement.executeQuery();
                while(set.next()){
                    String payer = set.getString("payer");
                    double amount = set.getDouble("amount");
                    Object obj = set.getString("payee");
                    if(set.getString("debitTo") != null)
                        obj = set.getString("debitTo").split(",");;

                    updateBalance(payer,amount,obj);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection.disconnect();
        }
        return balanceCache;
    }



    public void setBalanceCache(DataCache<String, Double> balanceCache) {
        this.balanceCache = balanceCache;
    }

    public static RecordsManager getInstance(){
        if(instance == null){
            instance = new RecordsManager();
        }
        return instance;
    }




}
