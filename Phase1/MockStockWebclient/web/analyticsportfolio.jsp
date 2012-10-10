<%@page import="middleware.common.StockProduct"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ch.unil.trader.Trader"%>

<%! Trader trader;
    HashMap<Integer, StockProduct> stock = new HashMap<Integer, StockProduct>();
    double sun;
    double sunpercent;
    double apple;
    double applepercent;
    double ibm;
    double ibmpercent;
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<table id="portfolio">
    <tbody>
        <%
            //gets trader from session
            trader = (Trader) session.getAttribute("trader");
            //gets the trader's stock
            stock = trader.getMyStock();

            //gets quantity
            sun = stock.get(1).getStockQty();
            apple = stock.get(2).getStockQty();
            ibm = stock.get(3).getStockQty();

            //calculate the perentage of each StockProduct
            if (sun == 0.0 && apple == 0.0 && ibm == 0.0) {
                sunpercent = 100 / 3;
                applepercent = 100 / 3;
                ibmpercent = 100 / 3;
            } else {
                sunpercent = 100 * (sun / (sun + apple + ibm));
                applepercent = 100 * (apple / (sun + apple + ibm));
                ibmpercent = 100 * (ibm / (sun + apple + ibm));
            }
        %>
        <% if (sunpercent != 0.0) {%>
        <tr>          
            <th scope="row"><%= stock.get(1).getStockName()%></th>
            <td><%= sunpercent + "%"%></td>
        </tr>
        <% } if (applepercent != 0.0) {%>
        <tr>
            <th scope="row"><%= stock.get(2).getStockName()%></th>
            <td><%= applepercent + "%"%></td>
        </tr>
        <% } if (ibmpercent != 0.0) {%>
        <tr>
            <th scope="row"><%= stock.get(3).getStockName()%></th>
            <td><%= ibmpercent + "%"%></td>
        </tr>
        <% } %>
    </tbody>
</table>
<div id="portfolioholder"></div>