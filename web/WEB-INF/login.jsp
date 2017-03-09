<%--
  Created by IntelliJ IDEA.
  User: Matthew
  Date: 5/03/2017
  Time: 1:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

    <title>Sign in</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <link href="css/signin.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.js"></script>


</head>

<body>
<div class="container">
    <div id="alerts">

    </div>
    <form class="form-signin">
        <h2 class="form-signin-heading">Please sign in</h2>
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" id="inputPassword" class="form-control" placeholder="Password" required>
        <button class="btn btn-lg btn-primary btn-block" type="button" id="sub">Sign in</button>
    </form>
    <script>
        $("#sub").click(function () {
            $(this).attr("disabled", true);
            $.post("", {
                password: $("#inputPassword").val()
            }, function (data) {
                console.info(typeof(data));
                data = JSON.parse(data);
                if (data["res"] === "ok") {
                    location.href = "";
                } else {
                    $('#alerts').children().alert("close");
                    $('#alerts').append(
                        '<div class="alert alert-warning alert-dismissible">' +
                        '<button type="button" class="close" data-dismiss="alert">' +
                        '&times;</button> <strong>warn</strong> ' + "login failed! check you password or server down!" + '</div>');
                }
                $("#sub").removeAttr("disabled");
            });
        });

    </script>
</div> <!-- /container -->


</body>
<body>

</body>
</html>
