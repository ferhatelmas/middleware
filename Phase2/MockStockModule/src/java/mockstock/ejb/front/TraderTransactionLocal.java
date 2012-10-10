package mockstock.ejb.front;


import java.util.HashMap;
import java.util.List;
import javax.ejb.Local;
import middleware.common.StockProduct;
import mockstock.db.*;

/**
 *
 * @author Kenny Lienhard
 */
@Local
public interface TraderTransactionLocal {

    public void createTransaction(TraderTransaction transaction, StockProduct transactionStock);
    
    public List<TraderTransaction> getSortedTransactionList(String email);
    
    public List<TraderTransaction> getSortedTransactionListAll(String email);
    
    public Portfolioproduct getPortfolioproduct(String stockname, String email);

    public boolean productExistsInPortfolio(String stockname, String email);

    public Stockproduct getStockproduct(String stockName);

    public void updatePortfolioproduct(Portfolioproduct pp);
    
    public void createPortfolioproduct(Portfolioproduct pp);
    
    public SPHistory findLatestSPHistoryForSP(String stockName);
    
    public HashMap<Integer, StockProduct> getAllStockProducts();
    
    public HashMap<Integer, String> getAllStockDifferences();

    public List<Portfolioproduct> getPortfolioproducts(String email);
    
    public Trader updateSettings(Trader editedTrader);
}
