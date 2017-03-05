package com.github.JHXSMatthew;

import com.github.JHXSMatthew.entities.EntityManager;
import com.github.JHXSMatthew.utils.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Matthew on 4/03/2017.
 */
public class RequestContainer {
    private HttpServletRequest req;

    public RequestContainer(HttpServletRequest request){
        this.req = request;
    }

    public String getPayer(){
        return req.getParameter("payer");
    }

    public String getPayee(){
        return req.getParameter("payee");
    }

    public double getAmount(){
        return Double.parseDouble(req.getParameter("amount"));
    }

    public String getMomo(){
        return req.getParameter("memo");
    }

    public String[] getDebitTo(){
        return req.getParameterValues("debitTo[]");
    }

    public String getStartingDate(){
        return req.getParameter("start");
    }

    public String getEndingDate(){
        return req.getParameter("end");
    }


    public boolean recordCheck(){
        try {
            return getStartingDate() != null && getEndingDate() != null
                    && DateUtils.stringToDate(getStartingDate()) != null
                    && DateUtils.stringToDate(getEndingDate()) != null
                    || getStartingDate() == null && getEndingDate() == null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean transferCheck(){
        String payer = req.getParameter("payer");
        String payee = req.getParameter("payee");
        try {
            Double.parseDouble(req.getParameter("amount"));
            return payer != null && payee != null &&
                    !payer.equals(payee) && EntityManager.getInstance().getNumberByName(payee) != -1 &&
                    EntityManager.getInstance().getNumberByName(payer) != -1;
        }catch (Exception e){
            return false;
        }
    }

    public boolean transactionCheck(){
        String payer = req.getParameter("payer");
        String[] debitTo = req.getParameterValues("debitTo[]");
        /*
        for(int i = 0 ; i < debitTo.length ; i ++)
            System.err.println(debitTo[i]);
        */
        try{
            Double.parseDouble(req.getParameter("amount"));
            return EntityManager.getInstance().getNumberByName(payer) != -1 && debitTo != null
                    && debitTo.length > 0 && Arrays.stream(debitTo).allMatch(str ->
                    EntityManager.getInstance().getNumberByName(str) != -1
            ) ;
        }catch (Exception e){
            return false;
        }
    }

    public boolean statsRequired(){
        return req.getParameter("balance") != null && req.getParameter("balance").equals("true");
    }

}

