package mockstock.ejb.front;

import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import mockstock.db.SPHistory;

/**
 *
 * @author Kenny Lienhard
 */

@Stateless(name = "Analytics", mappedName = "Analytics")
@Local(AnalyticsBeanLocal.class)
@Remote(AnalyticsBeanRemote.class)
public class AnalyticsBean implements AnalyticsBeanRemote, AnalyticsBeanLocal {
    
    @PersistenceContext
    private EntityManager em;

    /* Returns price history of a stock product specified by its name */
    
    @Override
    public List<SPHistory> getStockEvolution(String stockName) {
        return em.createQuery("SELECT h FROM SPHistory h "
                + "JOIN h.stockproduct sp WHERE sp.name = :stockName ORDER BY h.historyDate DESC")
                .setParameter("stockName", stockName)
                .setMaxResults(30)
                .getResultList();
    }
    
    @Override
    public List<SPHistory> getStockEvolutionRemote(String stockName) {
        return getStockEvolution(stockName);
    }
}
