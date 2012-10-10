<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="middleware.common.StockProduct"%>
<%@page import="java.util.HashMap"%>
<%@page import="ch.unil.trader.Trader"%>

<%! HashMap<Integer, StockProduct> stocks;
    int productID;
    int productQtty;
    String transactionType = null;
    Trader trader = null;
    double price;
%>

<%
    //checks if a buy or sell transaction was submitted
    if ((request.getParameter("id") != null) && (request.getParameter("quantity") != null)) {
        //gets the trader from the session    
        trader = (Trader) session.getAttribute("trader");
        if (trader != null) {
            try {
                this.productID = Integer.parseInt(request.getParameter("id"));
                this.productQtty = Integer.parseInt(request.getParameter("quantity"));
                this.transactionType = request.getParameter("submit");

                trader.update(productQtty, productID, transactionType);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());

            }
        }
    }

%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="header.html" %>
<!-- 
Main application for web client: provides an interface to do transactions 
and to get updates about the market prices.
-->
<div id="content">
    <div id="trader">
        <h2>Portfolio of <%= session.getAttribute("username")%></h2>
        <%
            trader = (Trader) session.getAttribute("trader");
            
            
            //stocks = Trader.getStocks();

            //Iterator it = stocks.entrySet().iterator();
            //while (it.hasNext()) {
                //Map.Entry<Integer, StockProduct> stock = (Map.Entry<Integer, StockProduct>) it.next();
                //it.remove(); // avoids a ConcurrentModificationException
%>

        <%
            //}
        %>
        <div class="stockproduct">
            <div class="stockinfo">
                <p class="stockname">Sun</p>
                <ul>
                    <li class="info"><span class="meta">Quantity</span>
                    <% if (trader.getMyStock().get(1) != null) {%>
                    <%= trader.getMyStockQuantity(1)%>
                    <%} else
                            out.println("0");%>
                    </li>
                    <li class="info"><span class="meta">Result</span>
                    <% if (trader.getMyStock().get(1) != null) {%>
                    <%= trader.getMyStockResult(1)%>
                    <%} else
                            out.println("0");%>
                    </li>
                </ul>
            </div>
            <form action="trader" method="post" accept-charset="utf-8">
                <input type="text" name="quantity" placeholder="New Quantity">
                <input type="hidden" name="id" value="1">
                <input type="submit" name="submit" value="Buy">
                <input type="submit" name="submit" value="Sell">
            </form>
        </div>
        <div class="stockproduct">
            <div class="stockinfo">
                <p class="stockname">Apple</p>
                <ul>
                    <li class="info"><span class="meta">Quantity</span>
                    <% if (trader.getMyStock().get(2) != null) {%>
                    <%= trader.getMyStockQuantity(2)%>
                    <%} else
                            out.println("0");%>
                    </li>
                    <li class="info"><span class="meta">Result</span>
                    <% if (trader.getMyStock().get(2) != null) {%>
                    <%= trader.getMyStockResult(2)%>
                    <%} else
                            out.println("0");%>
                    </li>
                </ul>
            </div>
            <form action="trader" method="post" accept-charset="utf-8">
                <input type="text" name="quantity" placeholder="New Quantity">
                <input type="hidden" name="id" value="2">
                <input type="submit" name="submit" value="Buy">
                <input type="submit" name="submit" value="Sell">
            </form>
        </div>
        <div class="stockproduct">
            <div class="stockinfo">
                <p class="stockname">IBM</p>
                <ul>
                    <li class="info"><span class="meta">Quantity</span>
                    <% if (trader.getMyStock().get(3) != null) {%>
                    <%= trader.getMyStockQuantity(3)%>
                    <%} else
                            out.println("0");%>
                    </li>
                    <li class="info"><span class="meta">Result</span>
                    <% if (trader.getMyStock().get(3) != null) {%>
                    <%= trader.getMyStockResult(3)%>
                    <%} else
                            out.println("0");%>
                    </li>
                </ul>
            </div>
            <form action="trader" method="post" accept-charset="utf-8">
                <input type="text" name="quantity" placeholder=" New Quantity">
                <input type="hidden" name="id" value="3">
                <input type="submit" name="submit" value="Buy">
                <input type="submit" name="submit" value="Sell">
            </form>
        </div>

        <div id="total">
            <ul>
                <li class="info"><span class="meta">Total result</span>
                <% if (trader.getTotalResult() != null) {%>
                <%= trader.getTotalResult()%>
                <%} else
                        out.println("0.0");%>
                </li>
            </ul>
        </div>
    </div>
    <aside id="market">
        <h2>Market News</h2>
        <a class="setstockprices">Set Stock Prices</a>
        <div id="marketprices">
        </div>
    </aside>
</div>
<%@ include file="footer.html" %>