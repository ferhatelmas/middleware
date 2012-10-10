package ch.unil.trader;

import java.util.ArrayList;
import java.util.HashMap;
import middleware.common.StockProduct;
import middleware.communication.Producer;
import middleware.communication.Transaction;
import middleware.exception.ProducerException;

/**
 * Trader class for Web UI
 * @author Adapted by Ferhat Elmas and Kenny Lienhard
 */
public class Trader {
    //available stocks: receives updates from the market

    private static HashMap<Integer, StockProduct> stocks = new HashMap<Integer, StockProduct>();
    //history stores all the prices from the market, is used to do analytics
    private static HashMap<Integer, ArrayList<Double>> stocksHistory = new HashMap<Integer, ArrayList<Double>>();
    private String userName;
    private Producer producer;
    //the personal stocks of a trader instance
    private HashMap<Integer, StockProduct> myStock;

    public Trader(String userName, Producer producer) {
        this.userName = userName;
        this.producer = producer;
        this.myStock = new HashMap<Integer, StockProduct>();

        for (int i : stocks.keySet()) {
            myStock.put(i, stocks.get(i).clone());
        }
    }

    public HashMap<Integer, StockProduct> getMyStock() {
        return myStock;
    }

    public void setMyStock(HashMap<Integer, StockProduct> myStock) {
        this.myStock = myStock;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public String getUserName() {
        return this.userName;
    }

    public void addStock(StockProduct sp) {
        myStock.put(sp.getStockID(), sp);
    }

    public static boolean updateStock(StockProduct stock) {
        if (stock == null) {
            System.out.println("I am null");
        }
        if (Trader.stocks.containsKey(stock.getStockID())) {
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

    public static HashMap<Integer, StockProduct> getStocks() {
        return Trader.stocks;
    }

    public static HashMap<Integer, ArrayList<Double>> getHistory() {
        return stocksHistory;
    }

    public void update(int qtty, int stockID, String type) throws Exception {
        type = type.toLowerCase();
        if (type.equals("buy") || type.equals("sell")) {
            boolean transactionConfirmed = true;
            //creates transaction object which is finally consumed by the market
            StockProduct transactionStockProduct = new StockProduct(stocks.get(stockID).getStockName());
            transactionStockProduct.setStockID(stockID);
            transactionStockProduct.setStockQty(qtty);
            transactionStockProduct.setStockPrice(this.stocks.get(stockID).getStockPrice());
            Transaction transaction = new Transaction(this.userName, transactionStockProduct, type, qtty);

            if (type.equals("buy")) {
                double price = this.stocks.get(stockID).getStockPrice();
                myStock.get(stockID).setStockQty(myStock.get(stockID).getStockQty() + qtty);
                myStock.get(stockID).setStockPrice(price);
            } else if (type.equals("sell")) {
                //checks if the trader has enough stocks to do the sale
                if (qtty <= myStock.get(stockID).getStockQty()) {
                    myStock.get(stockID).setStockQty(myStock.get(stockID).getStockQty() - qtty);
                    double adjust = ((qtty * this.stocks.get(stockID).getStockPrice()) - (qtty * myStock.get(stockID).getStockPrice()));
                    myStock.get(stockID).setResult(myStock.get(stockID).getResult() + Math.round(adjust));
                    myStock.get(stockID).setStockPrice(this.stocks.get(stockID).getStockPrice());
                } else {
                    transactionConfirmed = false;
                }
            }

            if (transactionConfirmed) {
                //produce message for the market
                try {
                    producer.sendObjectMessage(transaction);
                } catch (ProducerException pe) {
                    System.err.println("Operation couldn't be sent to server: " + pe.getMessage());
                }
            }

        }
    }

    public String getText(int stockID) {
        return ((StockProduct) myStock.get(stockID)).getStockName()
                + " (Qtty " + ((StockProduct) myStock.get(stockID)).getStockQty() + ") "
                + "Result(" + ((StockProduct) myStock.get(stockID)).getResult() + ")";
    }

    public int getMyStockQuantity(int stockID) {
        int quantity = myStock.get(stockID).getStockQty();
        return quantity;
    }

    public double getMyStockResult(int stockID) {
        double result = myStock.get(stockID).getResult();
        return result;
    }

    public Double getTotalResult() {
        double total = new Double(0);
        for (int i : myStock.keySet()) {
            total = total + myStock.get(i).getResult();
        }
        return total;
    }
}
