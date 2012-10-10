package mockstock.ejb.front;

import java.util.List;
import javax.ejb.Remote;
import mockstock.db.SPHistory;

/**
 *
 * @author felmas
 */
@Remote
public interface AnalyticsBeanRemote {

    public List<SPHistory> getStockEvolutionRemote(String stockName);
}
