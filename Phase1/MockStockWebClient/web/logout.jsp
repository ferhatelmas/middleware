<%@page import="middleware.communication.Subscriber"%>

<!-- 
Logic to properly close the trader's subscriber and producer connection to JMS, 
redirect to index.jsp
-->

<%@page import="ch.unil.trader.Trader"%>
<%!    private Trader trader = null;
       private Subscriber subscriber = null;
%>
<%
    trader = (Trader) session.getAttribute("trader");
    subscriber = (Subscriber) session.getAttribute("subscriber");
    if (subscriber != null)
    subscriber.close();
    
    //removes user name from server, not yet implemented
    //trader.getProducer().sendTextMessage("remove:" + trader.getUserName());
    if (trader.getProducer() != null)
    trader.getProducer().close();
    response.sendRedirect("index.jsp");
%>