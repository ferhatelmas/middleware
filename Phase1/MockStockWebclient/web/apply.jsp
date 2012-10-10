<%@page import="middleware.exception.ProducerException"%>
<!-- 
This logic handles the application of a new trader who wishes to join the market
-->
<%@page import="java.util.HashMap"%>
<%@page import="middleware.communication.Publisher"%>
<%@page import="java.util.ArrayList"%>
<%@page import="javax.jms.JMSException"%>
<%@page import="middleware.common.StockProduct"%>
<%@page import="javax.jms.ObjectMessage"%>
<%@page import="javax.jms.TextMessage"%>
<%@page import="middleware.common.Result"%>
<%@page import="javax.jms.Message"%>
<%@page import="middleware.exception.SubscriberException"%>
<%@page import="javax.xml.bind.Marshaller.Listener"%>
<%@page import="javax.jms.MessageListener"%>
<%@page import="middleware.communication.Subscriber"%>
<%@page import="middleware.communication.Producer"%>
<%@page import="ch.unil.trader.Trader"%>


<%! private final String controlQueueName = "msControl";
    private final String updateTopicName = "msUpdate";
    private Subscriber subscriber;
    private Producer producer;
    private Result queryResult;
    //private long time;
    private String query;
    private Trader trader;
%>

<%
    //get the new trader name
    String name = request.getParameter("name");
    this.queryResult = Result.OK;

    if (!name.trim().equals("")) {
        this.producer = new Producer(controlQueueName);
        this.producer.start();

        //Here we started implementing user name check on market
        //time = System.currentTimeMillis();
        //queryResult = Result.FAIL;
        //query = System.nanoTime() + ":" + name;

        this.trader = new Trader(name, producer);

        //Create a Subcriber and define onMessage behaviour
        subscriber = new Subscriber(updateTopicName, new MessageListener() {

            @Override
            public void onMessage(Message message) {
                if (message instanceof TextMessage) {
                    String result = null;

                    if (result.endsWith("Ok")) {
                        queryResult = Result.OK;
                    } else {
                        queryResult = Result.FAIL;
                    }
                } else if (message instanceof ObjectMessage) {
                    StockProduct sp = null;
                    try {
                        sp = (StockProduct) ((ObjectMessage) message).getObject();
                        /*
                        this method puts (or updates if already existing) the StockProduct 
                        with the new price in the list of StockProducts. If the StockProduct isn't 
                        initialized yet on the trader's personal StockProduct list, the object 
                        will be put also in this list
                         */
                        if (Trader.updateStock(sp)) {
                            trader.addStock(sp);
                        }
                    } catch (JMSException e) {
                        System.err.println("Stock list couldn't be read: " + e.getMessage());
                    }
                }
            }
        ;

        });
        this.subscriber.start();

        //check if user name already exists in market
        //try {
            //producer.sendTextMessage(query);
        //} catch (ProducerException e) {
            //session.setAttribute("error", "This user exists already.");
            //response.sendRedirect("errormessage.jsp");
        //}
        
        //if user name doesn't exists yet, join market
        //if (queryResult == Result.OK) {
            //Sets session attributes with corresponding objects
            session.setAttribute("username", name);
            session.setAttribute("trader", trader);
            response.sendRedirect("trader");
        //} else {
            //session.setAttribute("error", "There is already a trader with this name on the market.");
            //response.sendRedirect("errormessage.jsp");
        //}
    } else {
        session.setAttribute("error", "The name cannot be empty.");
        response.sendRedirect("errormessage.jsp");
    }
%>
