<%@page import="ch.unil.trader.Trader"%>
<%@page import="java.util.ArrayList"%>

<%! ArrayList<Double> prices;
    int index;
    int counter;
    double currentPrice;
    int priceInterval = 6; //interval in which the price is updated
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
        <title>MockStock Index Apple</title>
        <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
        <script src="js/raphael.js"></script>
        <script src="js/popup.js"></script>
        <script src="js/analytics.js"></script>
    </head>
    <body>
        <table id="data">
            <tfoot>
                <tr>
                    <% prices = Trader.getHistory().get(2);
                        /*
                        always lists the last 30 prices fo the StockProduct with 
                        its corresponding time interval (index * interval)
                         */
                        index = prices.size();
                        if (index > 30) {
                            index = 30;
                        }
                        for (int i = prices.size() - index; i < prices.size(); i++) {
                            counter = i * priceInterval;%>
                    <th><%= counter%></th>
                    <%}%>
                </tr>
            </tfoot>
            <tbody>
                <tr>
                    <%
                        for (int i = prices.size() - index; i < prices.size(); i++) {
                            currentPrice = prices.get(i);
                    %>
                    <td><%= currentPrice%></td>
                    <%}%>
                </tr>
            </tbody>
        </table>
        <div id="holder"></div>
    </body>
</html>