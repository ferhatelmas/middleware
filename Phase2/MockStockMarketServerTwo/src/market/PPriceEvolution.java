package market;

import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;
import middleware.common.StockProduct;
import middleware.communication.Publisher;
import middleware.exception.PublisherException;

/**
 * 
 * @author felmas
 */
/*
 *  We didn't modify this class much
 *  Just copied into server package and added publisher
 * 
 *  Added a method to utilize of publisher of this class
 *  This method sends user enter/exit protocol response
 */
public class PPriceEvolution extends TimerTask {
    
    final int MSGT_CHANGE_PRICE = 6;
    final double PROBA_EVO = 0.05;
    final double EXCEPTIONAL_EVO_RATE = 0.5;
    final double NORMAL_EVO_RATE = 0.1;
    final double PROBA_UP = 0.475;
    final double PROBA_DOWN = 0.575;
    final double PRICE_ROOF = 0.5;
    
    private StockProduct thisStock ;
    private ArrayList<StockProduct> stockList;
    
    private Publisher publisher; 
    
    public PPriceEvolution(ArrayList<StockProduct> stockList, String topicName) throws PublisherException {
        
        this.stockList = stockList;
        this.publisher = new Publisher(topicName);
        this.publisher.start();
    }
    @Override
    public void run() {
        boolean sendThis = false;
            double newPrice = 0;
            if (getStockList() != null){
                java.util.Iterator it = getStockList().iterator();
                while(it.hasNext()){
                    java.util.Random randomizer;
                    thisStock = (StockProduct)it.next();
                    double proba ;
                    randomizer = new Random();
                    proba=randomizer.nextDouble();
                    if (thisStock.getStockPrice().doubleValue()<=(new Double(0.5)).doubleValue()){
                        newPrice = Math.round(new Double(randomizer.nextDouble()).doubleValue()*100);
                        sendThis = true;
                    }else {
                        double coef=0;
                        if (randomizer.nextDouble()<=PROBA_EVO){
                            coef = EXCEPTIONAL_EVO_RATE;
                        } else {
                            coef = NORMAL_EVO_RATE;
                        }
                        if (proba >= PROBA_UP ) {//hausse
                            newPrice = Math.round((thisStock.getStockPrice().doubleValue()*(new Double(1).doubleValue()+new Double(randomizer.nextDouble()*coef).doubleValue())));
                            sendThis = true;
                        } else if ((proba <= PROBA_DOWN) && (thisStock.getStockPrice().doubleValue()>=(new Double(PRICE_ROOF)).doubleValue())){//Baisse seulement si la valeur du titre est supérieur é 0.5
                            newPrice = Math.round((thisStock.getStockPrice().doubleValue()*(new Double(1).doubleValue()-new Double(randomizer.nextDouble()*coef).doubleValue())));
                            if (newPrice <=PRICE_ROOF){
                                sendThis=false;
                            }else{
                                sendThis = true;
                            }
                        } else {
                            sendThis = false;
                        }
                    }
                    if (sendThis){
                        thisStock.setStockPrice(newPrice);
                        try {
                            publisher.publishStockProduct(thisStock);
                        } catch (PublisherException e) {
                            System.err.println("Stock " + thisStock.getStockName() + " couldn't be published: " + e.getMessage());
                        }
                    }
                    sendThis=false;
                }
            }
    }
    
    public void sendControlMessage(String msg) throws PublisherException {
        publisher.publishTextMessage(msg);
    }
    
    public ArrayList getStockList() {
        return stockList;
    }
    
    public void setStockList(ArrayList stockList) {
        this.stockList = stockList;
    }
    
}
