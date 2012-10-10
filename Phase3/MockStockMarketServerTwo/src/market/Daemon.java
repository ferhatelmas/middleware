package market;

import java.util.ArrayList;
import java.util.Timer;
import javax.jms.*;
import middleware.common.StockProduct;
import middleware.communication.Consumer;
import middleware.communication.Transaction;
import middleware.exception.PublisherException;
/**
 *
 * @author felmas
 */
public class Daemon extends Thread {

    // Time between updates in milliseconds
    private final int stockUpdateInterval = 6000;
    
    // JMS Resources
    private final String updateTopicName = "msUpdate";
    private final String controlQueueName = "msControl";
    
    // Consumer for trader enter/exit and buy/sell
    private Consumer controlConsumer;
    
    // Publishes new prices
    private PPriceEvolution service;
    
    // Update timer for prices
    private Timer timer;
    
    // User names currentln in the market
    private ArrayList<String> users;
    
    // Server start time
    private long startTime;
    
    public Daemon() {
        this.startTime = System.currentTimeMillis();
        
        this.users = new ArrayList<String>();
        
        try {
            this.service = new PPriceEvolution(getStockList(), updateTopicName);
            this.controlConsumer = new Consumer(controlQueueName, new MessageListener(){

                @Override
                public void onMessage(Message msg) {
                    
                    // User enter/exit part
                    // Server and clients talk a custom protocol
                    // ENTER request format   --> timestamp:username
                    // ENTER response formats --> timestamp:username:#ofStocks:Ok or timestamp:username:Fail
                    // EXIT request format    --> remove:username
                    // There is no response for remove, clients assume it is successful
                    // Since clients check every entrance, this assumption is safe
                    if(msg instanceof TextMessage) {
                        
                        String text;
                        try {
                            text = ((TextMessage)msg).getText();
                        } catch(JMSException e) {
                            System.err.println("Message couldn't be casted to TextMessage: " + e.getMessage());
                            return;
                        }
                        
                        // remove 
                        String name = text.split(":")[1];
                        if(text.startsWith("remove")) {
                            users.remove(name);
                            System.out.println("Trader " + name + " exited from market");
                        } 
                        
                        // enter/exit
                        else {

                            // this is a test whether message has arrived at topic before server start
                            // this test is necessary not to send wrong response clients
                            try {
                                if(msg.getJMSTimestamp() < startTime) return;
                            } catch(JMSException e){}
                            
                            if(users.contains(name)) {
                                try {
                                    service.sendControlMessage(text + ":Fail");
                                } catch(PublisherException e) {} 
                            } else {
                                try {
                                    service.sendControlMessage(text + ":" + service.getStockList().size() +":Ok");
                                    users.add(name);
                                    System.out.println("Trader " + name + " entered into market");
                                } catch(PublisherException e) {}
                            }   
                        }
                    } 
                    
                    // User buy/sell part
                    // Server only takes processed transaction and prints onto stardard output
                    else if(msg instanceof ObjectMessage) {
                    
                        try {
                            Transaction t = (Transaction)((ObjectMessage)msg).getObject();
                            
                            String operation;
                            if("buy".equals(t.getTransactionType())) operation = "bought ";
                            else operation = "sold ";
                            
                            System.out.println("Trader " + t.getAuthor() + " " + operation + t.getQty() + " " + t.getStockProduct().getStockName() + " stocks");
                        } catch(JMSException e) {
                            System.err.println("Message couldn't be casted to ObjectMessage: " + e.getMessage());
                        }
                    }
                }
            });
            this.controlConsumer.start();
        } catch(Exception e) {
            System.err.println("Server daemon couldn't be created: " + e.getMessage());
            System.exit(1);
        } 
        
        this.timer = new Timer();
        timer.scheduleAtFixedRate(service, 0, stockUpdateInterval);
    
    }
    
    // Returns stock list
    // Nice stock getter may be implemented in a real application
    private ArrayList<StockProduct> getStockList() {
        
        ArrayList<StockProduct> list = new ArrayList<StockProduct>();
        
        StockProduct thisStockPdt = new StockProduct("Sun");
        thisStockPdt.setStockID(1);
        list.add(thisStockPdt);
        thisStockPdt = new StockProduct("Apple");
        thisStockPdt.setStockID(2);       
        list.add(thisStockPdt);
        thisStockPdt = new StockProduct("IBM");
        thisStockPdt.setStockID(3);
        list.add(thisStockPdt);
        
        return list;
    }
    
    // Basically it does nothing
    // Only doesn't exit and wait for the timer fires
    @Override
    public void run() {
    }
    
    // Main applicaton for server
    // Creates a daemon and runs forever
    public static void main(String[] args) {    
        Daemon daemon = new Daemon();
        daemon.setDaemon(true);
        daemon.start();
    }
    
}
