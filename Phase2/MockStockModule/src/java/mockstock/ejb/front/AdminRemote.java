package mockstock.ejb.front;

import java.util.List;
import javax.ejb.Remote;
import mockstock.db.Trader;

/**
 *
 * @author felmas
 */
@Remote
public interface AdminRemote {
    
    public List<Trader> getTraderListRemote();

    public Trader getTraderRemote(Long id);

    public Trader updateTraderRemote(Trader editedTrader);

    public void removeTraderRemote(Trader trader); 
     
}
