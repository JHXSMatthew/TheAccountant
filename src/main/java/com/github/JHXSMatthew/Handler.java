package com.github.JHXSMatthew;

import com.github.JHXSMatthew.entities.EntityManager;
import com.github.JHXSMatthew.records.Record;
import com.github.JHXSMatthew.records.RecordsManager;
import com.github.JHXSMatthew.records.Transaction;
import com.github.JHXSMatthew.records.Transfer;
import com.github.JHXSMatthew.utils.DateUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Matthew on 26/02/2017.
 */
public class Handler extends HttpServlet{
    public Handler(){
        super();

    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        if(session.getAttribute("login") == null){
            req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
            return;
        }

        String[] entities = EntityManager.getInstance().getEntities();
        for(String s : entities){
            if(!RecordsManager.getInstance().getBalanceCache().containsKey(s)){
                RecordsManager.getInstance().getBalanceCache().put(s,0.0);
            }
        }
        req.setAttribute("entities", entities);
        req.setAttribute("balance", RecordsManager.getInstance().getBalanceCache());
        req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req,resp);

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if(session.getAttribute("login") == null){
            if(req.getParameter("password") != null){
                if(req.getParameter("password").equals(Config.password)) {
                    session.setAttribute("login", "true");
                    setResponsePair(resp,1,"done","res","ok");
                }
            }
        }else {
            RequestContainer container = new RequestContainer(req);
            switch (req.getParameter("type")) {
                case "transfer":
                    if (container.transferCheck()) {
                        Transfer t = new Transfer(-1,
                                DateUtils.dateToString(new Date()),
                                container.getPayer(),
                                container.getAmount(),
                                container.getMomo(),
                                container.getPayee(),
                                req.getRemoteAddr()
                        );

                        RecordsManager.getInstance().createNewTransfer(t);
                        setResponsePair(resp, 1, "Transfer has been recorded successfully!"
                                , "id", String.valueOf(t.getId())
                        );
                    } else {
                        setResponsePair(resp, 0, "Please check the input value of your transfers, " +
                                "server rejected your illegal request!");
                    }
                    break;
                case "transaction":
                    if (container.transactionCheck()) {
                        Transaction t = new Transaction(-1,
                                DateUtils.dateToString(new Date()),
                                container.getPayer(),
                                container.getAmount(),
                                container.getMomo(),
                                req.getRemoteAddr(),
                                container.getDebitTo()
                        );

                        RecordsManager.getInstance().createNewTransaction(t);
                        setResponsePair(resp, 1, "Transaction has been recorded successfully!"
                                , "id", String.valueOf(t.getId()));
                    } else {
                        setResponsePair(resp, 0, "Please check the input value of your transaction, " +
                                "server rejected your illegal request!");
                    }
                    break;
                case "records":
                    if (container.recordCheck()) {
                        Record[] records = RecordsManager.getInstance()
                                .getRecords(container.getStartingDate(), container.getEndingDate());
                        JSONArray array = new JSONArray();
                        Arrays.stream(records).forEach(record -> {
                            array.add(record.toJSON());
                        });
                        setResponsePair(resp, 1, "last " + records.length + " records have been successfully listed! "
                                , "records", array.toJSONString());
                    } else {
                        setResponsePair(resp, 0, "Please check the starting date and ending date, " +
                                "server rejected your illegal request!");
                    }

                    break;
                case "balance":
                    List<String> list = new ArrayList<>();
                    for (String s : EntityManager.getInstance().getEntities()) {
                        list.add(s);
                        list.add(String.valueOf((int) (100 * RecordsManager.getInstance().getBalanceCache().get(s)) / 100.00));
                    }
                    setResponsePair(resp, 0, "yes", list.toArray(new String[list.size()]));
                    break;
                default:
                    setResponsePair(resp, 0, "You request was broken....");
            }
        }
    }

    private void setResponsePair(HttpServletResponse resp, int status, String words,String ... pairsArguments){
        JSONObject obj = new JSONObject();
        obj.put("status",status);
        obj.put("words",words);
        if(pairsArguments != null && pairsArguments.length %2 == 0){
            int index = 0;
            while(index < pairsArguments.length)
                obj.put(pairsArguments[index++],pairsArguments[index++]);
        }
        try {
            resp.getWriter().write(obj.toJSONString());
            resp.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


/*
    private boolean recordCheck(){

    }
*/
}
