package mockstock.ejb.front;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import middleware.common.StockProduct;
import middleware.communication.Producer;
import middleware.communication.Transaction;
import middleware.exception.ProducerException;
import mockstock.db.*;
import mockstock.ejb.backend.SingletonBeanLocal;

/**
 *
 * @author Kenny Lienhard
 */
@Stateful(name = "TraderTransaction", mappedName = "TraderTransaction")
@Local(TraderTransactionLocal.class)
@Remote(TraderTransactionRemote.class)
public class TraderTransactionBean implements TraderTransactionRemote, TraderTransactionLocal {

    @PersistenceContext
    private EntityManager em;
    
    @EJB(beanName="Singleton")
    private SingletonBeanLocal singleton;

    public TraderTransactionBean() {
    }

    /* Creates one buy or sell transaction, puts into the db and notifies market via jms */
    
    @Override
    //public void createTransaction(int transQtty, StockProduct stockproduct, TransactionType transactionType, Trader trader) {
    public void createTransaction(TraderTransaction transaction, StockProduct transactionStock) {

            //creates transaction object which is finally consumed by the market
            Transaction marketTransaction = new Transaction(transaction.getTrader().getEmail(), transactionStock, transaction.getType().toString(), transaction.getQuantitiy());

            //} else if (transaction.getType() == TransactionType.SELL) {
                //checks if the trader has enough stocks to do the sale
//                if (transQtty <= myStock.get(stockID).getStockQty()) {
//                    myStock.get(stockID).setStockQty(myStock.get(stockID).getStockQty() - qtty);
//                    double adjust = ((qtty * this.stocks.get(stockID).getStockPrice()) - (qtty * myStock.get(stockID).getStockPrice()));
//                    myStock.get(stockID).setResult(myStock.get(stockID).getResult() + Math.round(adjust));
//                    myStock.get(stockID).setStockPrice(this.stocks.get(stockID).getStockPrice());
//                } else {
//                    transactionConfirmed = false;
//                }
            
                //try {
                //produce message for the market
                  Producer producer;
        try {
            producer = new Producer("msControl");
            producer.start();
            producer.sendObjectMessage(marketTransaction);
        } catch (ProducerException ex) {
            Logger.getLogger(TraderTransactionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
                

                em.persist(transaction);
                //} catch (ProducerException pe) {
                //System.err.println("Operation couldn't be sent to server: " + pe.getMessage());
                //}
            
        
    }
    
    @Override
    public void createTransactionRemote(TraderTransaction transaction, StockProduct transactionStock) {
        createTransaction(transaction, transactionStock);
    }

    /* Returns the latest price for a stock product registered by singleton */
    
    @Override
    public SPHistory findLatestSPHistoryForSP(String stockName) {
        SPHistory sphistory = null;
        try {
            sphistory = (SPHistory) em.createQuery("SELECT h FROM SPHistory h "
                    + "JOIN h.stockproduct sp WHERE sp.name = :name AND h.historyDate IN "
                    + "(SELECT MAX(hs.historyDate) FROM SPHistory hs JOIN hs.stockproduct spp WHERE spp.name = :name)")
                    .setParameter("name", stockName).getSingleResult(); 
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sphistory;
    }
    
    @Override
    public SPHistory findLatestSPHistoryForSPRemote(String stockName) {
        return findLatestSPHistoryForSP(stockName);
    }
    
    /* Returns transaction history of a user, newest at the top */
    
    @Override
    public List<TraderTransaction> getSortedTransactionList(String email) {
        return em.createQuery("SELECT t FROM TraderTransaction t "
                + "JOIN t.trader tra WHERE tra.email = :email ORDER BY t.actiondate DESC")
                .setParameter("email", email).setMaxResults(3).getResultList();
    }
    
    @Override
    public List<TraderTransaction> getSortedTransactionListRemote(String email) {
        return getSortedTransactionList(email);
    }
    
    @Override
    public List<TraderTransaction> getSortedTransactionListAll(String email) {
        System.out.println(em.createQuery("SELECT t FROM TraderTransaction t "
                + "JOIN t.trader tra WHERE tra.email = :email ORDER BY t.actiondate DESC")
                .setParameter("email", email).getResultList().size());
        return em.createQuery("SELECT t FROM TraderTransaction t "
                + "JOIN t.trader tra WHERE tra.email = :email ORDER BY t.actiondate DESC")
                .setParameter("email", email).getResultList();
    }
    
    @Override
    public List<TraderTransaction> getSortedTransactionListAllRemote(String email) {
        return getSortedTransactionListAll(email);
    }

    /* Checks whether a stock product is in the portfolio of a user */
    
    @Override
    public boolean productExistsInPortfolio(String stockname, String email) {
        boolean productExists = false;
        try {
            int i = em.createQuery("SELECT pp FROM Portfolioproduct pp "
                + "JOIN pp.stockproduct sp "
                    + "JOIN pp.trader t "
                    + "WHERE sp.name = :name AND t.email = :email")
                    .setParameter("name", stockname).setParameter("email", email).getResultList().size();
            if (i != 0) {
                productExists = true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return productExists;
    }

    
//    @Override
//    public boolean productExistsInPortfolio(String stockname) {
//        boolean productExists = false;
//        try {
//            int i = em.createQuery("SELECT pp FROM Portfolioproduct pp "
//                    + "JOIN pp.stockproduct sp WHERE sp.name = :name")
//                    .setParameter("name", stockname).getResultList().size();
//            if (i != 0) {
//                productExists = true;
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return productExists;
//    }
    
    @Override
    public boolean productExistsInPortfolioRemote(String stockname, String email) {
        return productExistsInPortfolio(stockname, email);
    }
    
//    @Override
//    public Portfolioproduct getPortfolioproduct(String stockname) {
//        return (Portfolioproduct) em.createQuery("SELECT pp FROM Portfolioproduct pp "
//                + "JOIN pp.stockproduct sp WHERE sp.name = :name")
//                .setParameter("name", stockname).getSingleResult();
//    }
    
    /* Returns the details of a portfolio product of a user */
    
    @Override
    public Portfolioproduct getPortfolioproductRemote(String stockname, String email) {
        return getPortfolioproduct(stockname, email);
    }

    @Override
    public Portfolioproduct getPortfolioproduct(String stockname, String email) {
        return (Portfolioproduct) em.createQuery("SELECT pp FROM Portfolioproduct pp "
                + "JOIN pp.stockproduct sp "
                    + "JOIN pp.trader t "
                    + "WHERE sp.name = :name AND t.email = :email")
                .setParameter("name", stockname).setParameter("email", email).getSingleResult();
    }

    /* Returns the details of a stock product */
    
    @Override
    public Stockproduct getStockproduct(String stockName) {
        return (Stockproduct) em.createQuery("SELECT sp FROM Stockproduct sp "
                + "WHERE sp.name = :name").setParameter("name", stockName).getSingleResult();
    }
    
    @Override
    public Stockproduct getStockproductRemote(String stockName) {
        return getStockproduct(stockName);
    }

    /* Creates a portfolio product for a user */
    
    @Override
    public void createPortfolioproduct(Portfolioproduct pp) {
        em.persist(pp);
    }
    
    @Override
    public void createPortfolioproductRemote(Portfolioproduct pp) {
        createPortfolioproduct(pp);
    }
    
    /* Returns the details of all stock products which are used gui init by clients*/
    
    @Override
    public HashMap<Integer, StockProduct> getAllStockProducts() {
        return singleton.getAllStocks();
    }
    
    @Override
    public HashMap<Integer, StockProduct> getAllStockProductsRemote() {
        return getAllStockProducts();
    }
    
    /* In addition to price data, singleton keeps price changes, 
     * this method returns these changes */
    
    @Override
    public HashMap<Integer, String> getAllStockDifferences() {
        return singleton.getAllStockDifferences();
    }
    
    @Override
    public HashMap<Integer, String> getAllStockDifferencesRemote() {
        return getAllStockDifferences();
    }
    
    /* Returns all portfolio detail of a user */
    
    @Override
    public List<Portfolioproduct> getPortfolioproducts(String email) {
        return em.createQuery("SELECT pp FROM Portfolioproduct pp "
                + "JOIN pp.trader t WHERE t.email = :email")
                .setParameter("email", email).getResultList();
    }

    @Override
    public List<Portfolioproduct> getPortfolioproductsRemote(String email) {
        return getPortfolioproducts(email);
    }
    
    /* Updates one portfolio product after buy/sell operation */
    
    @Override
    public void updatePortfolioproduct(Portfolioproduct pp) {
        em.merge(pp);
    }
    
    @Override
    public void updatePortfolioproductRemote(Portfolioproduct pp) {
        updatePortfolioproduct(pp);
    }
    
    /* Updates settings of the user such as status, admin or normal user */
    
    @Override
    public Trader updateSettings(Trader editedTrader) {
        return em.merge(editedTrader);
    }
    
    @Override
    public Trader updateSettingsRemote(Trader editedTrader) {
        return updateSettings(editedTrader);
    }
}
