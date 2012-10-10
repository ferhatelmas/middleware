package mockstock.ejb.front;

import java.util.HashMap;
import java.util.List;
import javax.ejb.Remote;
import middleware.common.StockProduct;
import mockstock.db.*;

/**
 *
 * @author felmas
 */
@Remote
public interface TraderTransactionRemote {

    public void createTransactionRemote(TraderTransaction transaction, StockProduct transactionStock);
    
    public List<TraderTransaction> getSortedTransactionListRemote(String email);
    
    public List<TraderTransaction> getSortedTransactionListAllRemote(String email);
    
    public Portfolioproduct getPortfolioproductRemote(String stockname, String email);

    public boolean productExistsInPortfolioRemote(String stockName, String email);

    public Stockproduct getStockproductRemote(String stockName);

    public void updatePortfolioproductRemote(Portfolioproduct pp);
    
    public void createPortfolioproductRemote(Portfolioproduct pp);
    
    public SPHistory findLatestSPHistoryForSPRemote(String stockName);
    
    public HashMap<Integer, StockProduct> getAllStockProductsRemote();
    
    public HashMap<Integer, String> getAllStockDifferencesRemote();

    public List<Portfolioproduct> getPortfolioproductsRemote(String email);
    
    public Trader updateSettingsRemote(Trader editedTrader);
}
