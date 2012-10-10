package mockstock.ejb.backend;

import java.util.HashMap;
import javax.ejb.Local;
import middleware.common.MSTweet;
import middleware.common.StockProduct;

/**
 *
 * @author felmas
 */
@Local
public interface SingletonBeanLocal {

    public void updateStock(StockProduct sp);

    public java.util.HashMap<Integer, StockProduct> getAllStocks();

    public double getStockPriceById(int id);

    public java.util.Set<Integer> getAllStockProductIds();

    public StockProduct getStockProductById(int id);

    public boolean isTraderExists(String email);

    public HashMap<Integer, String> getAllStockDifferences();
        
}
