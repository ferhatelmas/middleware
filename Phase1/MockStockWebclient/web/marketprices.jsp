<%@page import="java.util.ArrayList"%>
<%@page import="middleware.common.StockProduct"%>
<%@page import="javax.jms.JMSException"%>
<%@page import="javax.jms.TextMessage"%>
<%@page import="javax.jms.MessageListener"%>
<%@page import="javax.jms.Message"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@page import="ch.unil.trader.Trader"%>

<%!    Trader trader;
    HashMap<Integer, ArrayList<Double>> log;
    HashMap<Integer, StockProduct> stocks;
%>

<!-- 
    marketprices.jsp is called within market.jsp to display the price history, 
    the JavaScript file mockstock.js calls this page every n seconds
-->

<%@page contentType="text/html" pageEncoding="UTF-8"%>
        <table>
            <%
                stocks = Trader.getStocks();
                log = Trader.getHistory();

                Iterator it = stocks.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Integer, StockProduct> stock = (Map.Entry<Integer, StockProduct>) it.next();
                    double priceDifference = 0;
                    if (log.get(stock.getKey()).size() > 1) {
                        double oldPrice = log.get(stock.getKey()).get(log.get(stock.getKey()).size() - 2);
                        priceDifference = stock.getValue().getStockPrice() - oldPrice;
                    }
                    //it.remove(); // avoids a ConcurrentModificationException
            %>
            <tr class="first">
                <td> <%= stock.getValue().getStockName()%> </td>
                <td> <%= stock.getValue().getStockPrice()%> </td>
                <td> <% if (priceDifference < 0) {
                        out.println("- " + Double.toString(Math.abs(priceDifference)));
                    } else {
                        out.println("+ " + Double.toString(priceDifference));
                    }

                    %> </td>
            </tr>
            <%}%></table>