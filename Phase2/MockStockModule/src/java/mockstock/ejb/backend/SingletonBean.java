package mockstock.ejb.backend;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import middleware.common.StockProduct;
import mockstock.db.SPHistory;
import mockstock.db.Stockproduct;

/**
 *
 * @author felmas
 */
@Startup
@Singleton(name="Singleton")
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class SingletonBean implements SingletonBeanLocal {

    @PersistenceContext(unitName="MockStockModulePU")
    EntityManager em;
    
    /* user login timeouts after 30 mins if there is no activity */
    private static long timeout = 1000 * 60 * 30;
    
    private HashMap<Integer, StockProduct> stocks;
    private HashMap<Integer, String> differences;
    private HashMap<String, Long> traders;
    
    public SingletonBean() {
        this.stocks = new HashMap<Integer, StockProduct>();
        this.differences = new HashMap<Integer, String>();
        this.traders = new HashMap<String, Long>();
    }
    
    /* Updates the price of an existing stock, 
     * even if stock is new, it is put into hashMap*/
    @Override
    @Lock(LockType.WRITE)
    public void updateStock(StockProduct newStockProduct) {
    
        if(stocks.containsKey(newStockProduct.getStockID())) {
            StockProduct oldStockProduct = stocks.get(newStockProduct.getStockID());
            double diff = newStockProduct.getStockPrice() - oldStockProduct.getStockPrice();
            oldStockProduct.setStockPrice(newStockProduct.getStockPrice());
            if(differences.containsKey(newStockProduct.getStockID())) {
                differences.remove(newStockProduct.getStockID());
            }
            String priceDiff;
            if (diff < 0) {
                priceDiff = "- " + String.valueOf(Math.abs(diff));
            } else {
                priceDiff = "+ " + String.valueOf(diff);
            }
            differences.put(newStockProduct.getStockID(), priceDiff);
            
        } else {
            stocks.put(newStockProduct.getStockID(), newStockProduct);
            differences.put(newStockProduct.getStockID(), "0.0");
        }
        updateSPHistory(newStockProduct);
    }
    
    /* checks whether user is connected 
     * unless email is in the map, user is not connected 
     * otherwise timestemp is checked and if it is larger than threshold
     * removes the email map, returns user not connected 
     * other case, user is connected 
     */
    @Override
    @Lock(LockType.WRITE)
    public synchronized boolean isTraderExists(String email) {
        if(traders.containsKey(email)) {
            if(System.currentTimeMillis() - traders.get(email) > timeout) {
                traders.remove(email);
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    /* intrinsically synchronized */
    private void updateSPHistory(StockProduct sp) {
        Stockproduct stockProductDB;
        
        if((stockProductDB=getStockProduct(sp)) == null) {
            stockProductDB = new Stockproduct();
            stockProductDB.setName(sp.getStockName());
            em.persist(stockProductDB);
        }
        SPHistory spHistory = new SPHistory();
        spHistory.setPrice(sp.getStockPrice());
        spHistory.setStockproduct(stockProductDB);
        spHistory.setHistoryDate(new Timestamp(System.currentTimeMillis()));
        em.persist(spHistory);
        System.out.println("SPHistory is added for " + sp.getStockName());
    }
    
    /* Returns one stock product from db queried by its name */
    private Stockproduct getStockProduct(StockProduct sp) {
        Query q = em.createQuery("SELECT sp FROM Stockproduct sp WHERE sp.name= :name");
        q.setParameter("name", sp.getStockName());
        List<Stockproduct> res = q.getResultList();
        return res.isEmpty() ? null : res.get(0);
    }
    
    /* Various GETTERS */
    
    @Override
    @Lock(LockType.READ)
    public Set<Integer> getAllStockProductIds() {
        return this.stocks.keySet();
    } 
    
    @Override
    @Lock(LockType.READ)
    public StockProduct getStockProductById(int id) {
        return this.stocks.get(id);
    }
    
    @Override
    @Lock(LockType.READ)
    public double getStockPriceById(int id) {
        return getStockProductById(id).getStockPrice();
    }
    
    @Override
    @Lock(LockType.READ)
    public HashMap<Integer, StockProduct> getAllStocks() {
        return this.stocks;
    }
    
    @Override
    @Lock(LockType.READ)
    public HashMap<Integer, String> getAllStockDifferences() {
        return this.differences;
    }
    
}
