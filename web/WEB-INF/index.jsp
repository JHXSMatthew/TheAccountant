<%@ page import="java.util.HashMap" %>
<%@ page import="com.github.JHXSMatthew.DataCache" %><%--
  Created by IntelliJ IDEA.
  User: Matthew
  Date: 26/02/2017
  Time: 2:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title> The Accountant</title>
  <!-- Latest compiled and minified CSS -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.js"></script>
  <script src="${request.contextPath}/javascript/accountant.js"></script>
</head>
<body>
<div class="container">
  <div class = "page-header">
    <h1>The Accountant </h1>
    <p class="lead">The bill separating software.</p>
  </div>

  <div id="alerts">

  </div>

 <!-- transactions-->
  <div class="row">
    <div class="col-sm-5">
      <div class="panel panel-primary">
        <div class="panel-heading">
          <h3 class="panel-title"> New transaction</h3>
        </div>
        <div class="panel-body">
          <div class="form-group row">
            <label class="col-sm-2 col-form-label">Payer</label>
            <div class="col-sm-6">
              <select class="form-control" id="transaction_payer">
                <%
                  String[] entities = (String[])request.getAttribute("entities");
                  DataCache<String,Double> balance = (DataCache<String,Double>) request.getAttribute("balance");
                  for(String s : entities){
                    out.println( "<option value="+s +">"+ s + "</option>");
                  }
                %>
              </select>
            </div>
          </div>
          <div class="form-group row">
            <label class="col-sm-2 col-form-label">amount</label>
            <div class="col-sm-6">
              <div class="input-group">
                <span class="input-group-addon">$</span>
                <input class="form-control" type="number" step="0.01" placeholder="100" min="0" id="transaction_paid">
              </div>
            </div>
          </div>
          <div class="form-group row">
            <label class="col-sm-2 col-form-label">Memo</label>
            <div class="col-sm-6">
              <div class="input-group">
                <input class="form-control" type="text" placeholder="memo" id="transaction_memo">
              </div>
            </div>
          </div>
          <div class="form-group row">
            <label class="col-sm-2 col-form-label">Debit to</label>
            <div class="col-sm-8">
              <div class="form-check form-check-inline" id="debit_inline">
                  <%
                    int i = 0;
                    for(String s : entities){
                      out.println(" <label class=\"form-check-label\">");
                      out.println( "<input class=transaction_checkBox type=checkbox id=transaction_cb_" + s + " value=" + s + ">"+ s );
                      out.println(" </label>");
                      i++;
                    }
                  %>
              </div>
            </div>
          </div>
          <span class="col-md-offset-5">
                  <button type="button" class="btn btn-primary" id="transaction_submit">submit</button>
                </span>
        </div>
      </div>
    </div>

    <!-- transfer  -->
    <div class="col-sm-5">
      <div class="panel panel-primary">
        <div class="panel-heading">
          <h3 class="panel-title">New transfer</h3>
        </div>
        <div class="panel-body">
          <div class="form-group row">
            <label class="col-sm-2 col-form-label">Payer</label>
            <div class="col-sm-6">
              <select class="form-control" id="transfer_payer">
                <%
                  for(String s : entities){
                    out.println( "<option value="+s +">"+ s + "</option>");
                  }
                %>
              </select>
            </div>
          </div>
          <div class="form-group row">
            <label class="col-sm-2 col-form-label">Payee</label>
            <div class="col-sm-6">
              <select class="form-control" id="transfer_payee">

              </select>
            </div>
          </div>
          <div class="form-group row">
            <label class="col-sm-2 col-form-label">amount</label>
            <div class="col-sm-6">
              <div class="input-group">
                <span class="input-group-addon">$</span>
                <input class="form-control" type="number" step="0.01" placeholder="100" min="0" id="transfer_paid">
              </div>
            </div>
          </div>
          <div class="form-group row">
            <label class="col-sm-2 col-form-label">Memo</label>
            <div class="col-sm-6">
              <div class="input-group">
                <input class="form-control" type="text" placeholder="memo" id="transfer_memo">
              </div>
            </div>
          </div>
          <span class="col-md-offset-5">
                  <button type="button" class="btn btn-primary" id="transfer_submit">submit</button>
                </span>
        </div>
      </div>
    </div>

    <!-- balance panel -->

    <div class="col-md-2">
      <div class="panel panel-primary">
        <div class="panel-heading">
          <h3 class="panel-title">Balance</h3>
        </div>
        <div class="panel-body">

          <%
            for(String s : entities){
          %>
          <div class="row">
          <%
              out.println(" <label class=\"col-md-6\">" +s + " </label>");
              double m = balance.get(s);
              String type = "info";
              if(m < 0)
                type = "danger";
              out.println(" <span class=\"label label-"+ type +" col-md-4\" id=balance_amount_"+ s +"> "
                      +  ((int)(balance.get(s)*100))/100.00 +" </span>");
          %>
          </div>
          <%
            }
          %>
        </div>
      </div>
    </div>



  </div>


  <!-- records panel -->
  <div class="row">
    <div class="col-sm-12">
      <div class="panel panel-primary">
        <div class="panel-heading">
          <h3 class="panel-title">Records</h3>
        </div>
        <div class="panel-body">
          <div class="form-group">
                  <span>
                    <label class="col-2 col-form-label">Start: </label>
                    <input type="date" value="2017-02-25" id="record_date_start">
                  </span>
            <label  class="col-2 col-form-label">End: </label>
            <!-- TODO: The current date -->
            <input type="date" value="2017-08-19" id="record_date_end">
            </span>
            <button type="button" class="btn btn-primary" id="record_fetch">Fetch</button>
          </div>

          <div class="form-group">
            <table class="table table-striped" id="records_table">
              <thead>
              <tr id="table_columns">
                <th>#</th>
                <th>Date</th>
                <%
                  for(String s : entities) {
                    out.println("<th class=entityName_set>" + s + "</th>");
                  }
                %>
                <th>memo</th>
              </tr>
              </thead>
              <tbody id="records_table_body">

              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>

</div>
</body>
</html>
