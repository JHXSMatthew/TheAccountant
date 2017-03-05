/**
 * Created by Matthew on 28/02/2017.
 */

function fillPayee(){
    var selected = $("#transfer_payer").children('option:selected').val();
    var payee = $("#transfer_payee");
    payee.empty();
    $("#transfer_payer").children().toArray().forEach(function(argument){
        if(argument.value != selected){
            var option = document.createElement("option");
            option.text = argument.value;
            option.value = argument.value;
            payee.append(option);
        }
    });
}

function fillDebitTo(prev){
    var select = $("#transaction_payer").children('option:selected').val();
    $("#transaction_cb_"+select).prop("checked",true);
    if(prev && prev !== select)
        $("#transaction_cb_"+prev).prop("checked",false);
}

function notify(context){
    showInfo(context,2);
}

function success(context){
    showInfo(context,1);
}

function warn(context){
    showInfo(context,0);
}

function broadcast(data){
    if(data.status == 0){
        warn(data.words);
    }else if(data.status == 1){
        success(data.words);
    }else {
        warn("Unknown error, please check connection with server.")
    }
}

function showInfo(context,type){
    $('#alerts').children().alert("close");

    if(type == 0){
        $('#alerts').append(
            '<div class="alert alert-danger alert-dismissible">' +
            '<button type="button" class="close" data-dismiss="alert">' +
            '&times;</button>  <strong>warn</strong> ' + context + '</div>');
    }else if(type == 1){
        $('#alerts').append(
            '<div class="alert alert-success alert-dismissible">' +
            '<button type="button" class="close" data-dismiss="alert">' +
            '&times;</button>  <strong>success</strong> ' + context + '</div>');
    }else {
        $('#alerts').append(
            '<div class="alert alert-info alert-dismissible">' +
            '<button type="button" class="close" data-dismiss="alert">' +
            '&times;</button> <strong>info</strong> ' + context + '</div>');
    }
}

function getEntityArray(){
    var es = [];
    $(".entityName_set").each(function (index,value) {
        es[index] = value.innerText;
    });
    return es;
}

var du = [];
function insertRecord(table,record) {
    var columns = [];
    if(du[record.id] == 1){
        return;
    }
    $(".entityName_set").each(function (index,value) {
        columns[index] = value.outerText;
    });
    var position = -1;

    if(table.rows && table.rows[0] && table.rows[0].cells && table.rows[0].cells[0]){
        if(record.id < table.rows[0].cells[0].innerHTML){
            position = 0;
        } else{
            position = record.id - table.rows[0].cells[0].innerHTML ;
            position = position*2;
        }
    }
    var row1 = table.insertRow(position);
    if(position != -1)
        position = position +1
    var row2 = table.insertRow(position);
    row1.insertCell(-1).innerHTML = record.id;
    row1.insertCell(-1).innerHTML = record.date;
    row2.insertCell(-1).innerHTML = "";
    row2.insertCell(-1).innerHTML = "";
    for(var i = 0 ; i < columns.length ; i ++){
        if(columns[i] === record.payer){
            row1.insertCell(-1).innerHTML = record.amount;
        }else{
            row1.insertCell(-1).innerHTML = "0";
        }

        if(record.type === "transfer"){
            if(columns[i] === record.payee) {
                row2.insertCell(-1).innerHTML = "-" + record.amount;
            }else{
                row2.insertCell(-1).innerHTML = "0";
            }
        }else if(record.type === "transaction"){
            for(var j = 0 ; j < record.debitTo.length ; j ++){
                if(columns[i] === record.debitTo[j]) {
                    row2.insertCell(-1).innerHTML = "-" + (record.amount / record.debitTo.length).toFixed(2);
                    break;
                }
                if(j == record.debitTo.length -1){
                    row2.insertCell(-1).innerHTML = "0";
                }
            }
        }
    }
    row1.insertCell(-1).innerHTML = record.memo;
    du[record.id] = 1;

}

function updateBalance(){
    $.post("",{
        type: "balance"
    },function (data) {
        var entities = getEntityArray();
        for(var k in entities)
            $("#balance_amount_" + entities[k]).text(data[entities[k]]);
    },"json");
}

function loadRecords(starting,end) {
    $.post("",{
        type: "records",
        start: starting,
        end: end,
    },function (data) {
        if(data){
            var table = document.getElementById("records_table_body");
            try{
                var array = JSON.parse(data.records);
                for(var k in array) {
                    insertRecord(table, array[k]);
                }
            }catch (err){

            }
            broadcast(data);
        }

    },"json");
}

$(document).ready(function () {
    fillPayee();
    fillDebitTo();
    loadRecords();
    $("#record_date_end").val(new Date().toISOString().split(['T'])[0]);
    var date = new Date();
    date.setDate(date.getDate() - 1);
    $("#record_date_start").val(date.toISOString().split(['T'])[0]);


    $("#transfer_payer").change(function () {
        fillPayee();
    });

    $("#transfer_submit").click(function () {
        var amount = $("#transfer_paid").val();
        if(!amount || amount < 0){
            warn("illegal amount entered!");
            return;
        }
        var memo = $("#transfer_memo").val();
        if(!memo)
            memo = "no memo";
       $.post("",{
           type: "transfer",
           payer: $("#transfer_payer").children('option:selected').val(),
           payee: $("#transfer_payee").children('option:selected').val(),
           amount: amount,
           memo: memo
        },function (data, status) {
           broadcast(data);
           $("#transfer_paid").val("");
           $("#transfer_memo").val("");
           loadRecords();
           updateBalance();

           },"json"
       );
    });


    var sel = $("#transaction_payer");
    sel.data("prev",sel.val());
    $("#transaction_payer").change(function () {
        fillDebitTo(sel.data("prev"));
        sel.data("prev",$(this).val());
    });
    $('#transaction_payer').focus(function () {
        if(!$("#transaction_cb_"+$(this).children('option:selected').val()).prop("checked")){
            $("#transaction_cb_"+$(this).children('option:selected').val()).prop("checked",true);
        }
    });

    $("#transaction_submit").click(function () {
        var amount = $("#transaction_paid").val();
        if(!amount || amount < 0){
            warn("illegal amount entered!");
            return;
        }
        var memo = $("#transaction_memo").val();
        if(!memo)
            memo = "no memo";

        var debits = [];
        $('.transaction_checkBox').each(function(){
            if($(this).prop('checked'))
                debits.push($(this).attr("value"));
        });
        if(debits==undefined || debits.length <= 0){
            warn("At least one person needed to be involved in for this transaction!");
            return;
        }

        $.post("",{
                type: "transaction",
                payer: $("#transaction_payer").children('option:selected').val(),
                debitTo: debits,
                amount: amount,
                memo: memo
            },function (data) {
                broadcast(data);

                $("#transaction_paid").val("");
                $("#transaction_memo").val("");
                $('.transaction_checkBox').each(function(){
                    $(this).prop('checked',false);
                });
                fillDebitTo();
                loadRecords();
                updateBalance();

            },"json"
        );
    })

    $("#record_fetch").click(function () {
        var start =$("#record_date_start").val();
        var end = $("#record_date_end").val();
        loadRecords(start,end);
    });


});


