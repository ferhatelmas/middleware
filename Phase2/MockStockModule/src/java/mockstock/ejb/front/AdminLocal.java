package mockstock.ejb.front;

import java.util.List;
import javax.ejb.Local;
import mockstock.db.Trader;

/**
 *
 * @author Kenny Lienhard
 */
@Local
public interface AdminLocal {

    public List<Trader> getTraderList();

    public Trader getTrader(Long id);

    public Trader updateTrader(Trader editedTrader);

    public void removeTrader(Trader trader); 
    
}
