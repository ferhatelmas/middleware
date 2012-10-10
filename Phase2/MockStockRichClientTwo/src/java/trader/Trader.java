package trader;

import java.util.ArrayList;
import java.util.HashMap;
import middleware.common.StockProduct;

/**
 *
 * @author felmas
 */
/*
 * User stock model
 */
public class Trader {
    
    // Stocks info to be used by all users
    private static HashMap<Integer, StockProduct> stocks = new HashMap<Integer, StockProduct>();
    private static HashMap<Integer, ArrayList<Double>> stocksHistory = new HashMap<Integer, ArrayList<Double>>();
    private static int msgCounter = 0;
    
    // Info of each user(name, stocks)
    private String userName;
    private HashMap<Integer, StockProduct> myStock;
    
    // Constructor
    // User name is set and if server already published stock, their info is added
    public Trader(String userName) {
        this.userName = userName;
        this.myStock = new HashMap<Integer, StockProduct>();
        
        for(int i : stocks.keySet()) {
            myStock.put(i, stocks.get(i).clone());
        }
    }
    
    
    
    // If server starts publishing new stock, it is also added into model
    public void addStock(StockProduct sp) {
        myStock.put(sp.getStockID(), sp);
    }
    
    // Main method called in each server update
    // Sets new prices and also append them into history for charts
    public static boolean updateStock(StockProduct stock) {
        
        if(Trader.stocks.containsKey(stock.getStockID())) {
            stocksHistory.get(stock.getStockID()).add(stock.getStockPrice());
            
            Trader.stocks.remove(stock.getStockID());
            Trader.stocks.put(stock.getStockID(), stock);
            return false;
        } else {
            ArrayList<Double> history = new ArrayList<Double>();
            history.add(stock.getStockPrice());
            stocksHistory.put(stock.getStockID(), history);
            
            Trader.stocks.put(stock.getStockID(), stock);
            return true;
        }
    }
    
    // Does the buy/sell transaction
    // Firstly done in local, then transaction is sent to the server
    public void update(int qtty, int stockID, String type){
        
        if(type.equals("buy")){
            double price = Trader.stocks.get(stockID).getStockPrice();
            myStock.get(stockID).setStockQty(myStock.get(stockID).getStockQty() + qtty);
            myStock.get(stockID).setStockPrice(price);
        }else if(type.equals("sell")){
            if (qtty <= myStock.get(stockID).getStockQty()){
                myStock.get(stockID).setStockQty(myStock.get(stockID).getStockQty() - qtty);
                double adjust = qtty * Trader.stocks.get(stockID).getStockPrice() - qtty * myStock.get(stockID).getStockPrice();
                myStock.get(stockID).setResult(myStock.get(stockID).getResult() + Math.round(adjust));
                myStock.get(stockID).setStockPrice(Trader.stocks.get(stockID).getStockPrice());
            }
        }
        
    }
    
    /*------- Getters ------*/
    
    public static HashMap<Integer, StockProduct> getStocks() {
        return Trader.stocks;
    }
    
    public static HashMap<Integer, ArrayList<Double>> getHistory() {
        return stocksHistory;
    }
    
    public String getUserName(){
        return this.userName;
    }
    
    public static int getStockCounter(){
        return Trader.msgCounter;
    }
    
    public HashMap<Integer, StockProduct> getMyStock() {
        return myStock;
    }
    
    // Returns name, qty and result of the stock as a string
    public String getText(int stockID){
        return myStock.get(stockID).getStockName() + 
                " (Qtty " + myStock.get(stockID).getStockQty() + ") " + 
                "Result(" + myStock.get(stockID).getResult() + ")";
    }
    
    // Returns sum of the results of all stocks
    public Double getTotalResult(){
        double total = new Double(0);
        for(int i : myStock.keySet()){
            total = total + myStock.get(i).getResult();}
        return total;
    }
    
    /* -------- Setters ------ */
    
    public static void setStockCounter(int cnt) {
        Trader.msgCounter = cnt;
    }
}
