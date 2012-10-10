package mockstock.ejb.front;

import javax.ejb.Remote;
import mockstock.db.Trader;

/**
 *
 * @author felmas
 */
@Remote
public interface TraderFacadeRemote {
    
    public Trader addTraderRemote(Trader trader);

    public Trader checkLoginRemote(String email, String password);

    public boolean verifyEmailRemote(String emailsignup);
    
}
