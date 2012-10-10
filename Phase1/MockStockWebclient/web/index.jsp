<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<!-- 
Login page to join the market and insert a trader name
-->
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
        <title>MockStock.</title>
        <link rel="stylesheet" media="screen" type="text/css" href="css/design.css" />
        <!--[if lt IE 9]>
                <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
                <![endif]-->
        <link rel="shortcut icon" href="img/fav.ico" type="image/x-icon"/>
    </head>
    <body id="mockstock">
        <div id="wrapper">
            <div id="login">
                <h1><span id="logo"></span>MockStock</h1>
                <form action="apply.jsp" method="post" accept-charset="utf-8">
                    <input type="text" name="name" placeholder="Trader Name">
                    <input type="submit" name="submit" value="Join Market">
                </form>
            </div>
        </div>
    </body>
</html>