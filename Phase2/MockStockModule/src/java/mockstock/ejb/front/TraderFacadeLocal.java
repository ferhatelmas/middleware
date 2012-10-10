package mockstock.ejb.front;

import javax.ejb.Local;
import mockstock.db.Trader;

/**
 *
 * @author Kenny Lienhard
 */
@Local
public interface TraderFacadeLocal {

    public Trader addTrader(Trader trader);

    public Trader checkLogin(String email, String password);

    public boolean verifyEmail(String emailsignup);   

    
}
