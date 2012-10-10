package mockstock.ejb.front;

import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import mockstock.db.Trader;

/**
 *
 * @author Kenny Lienhard
 */
@Stateless(name = "TraderFacade", mappedName = "TraderFacade")
@Local(TraderFacadeLocal.class)
@Remote(TraderFacadeRemote.class)
public class TraderFacadeBean implements TraderFacadeRemote, TraderFacadeLocal {

    @PersistenceContext
    private EntityManager em;

    public TraderFacadeBean() {
    }

    /* Basic entity operations */
    
    private Object mergeEntity(Object entity) {
        return em.merge(entity);
    }

    private Object persistEntity(Object entity) {
        em.persist(entity);
        return entity;
    }

    private Object refreshEntity(Object entity) {
        em.refresh(entity);
        return entity;
    }

    private void removeEntity(Object entity) {
        em.remove(em.merge(entity));
    }

    /* Adds new trader, which can be admin or normal user */
    @Override
    public Trader addTrader(Trader trader) {
        return (Trader) persistEntity(trader);
    }
    
    @Override
    public Trader addTraderRemote(Trader trader) {
        return addTrader(trader);
    }

    /* Check creadentials of the user which are email and password hash(MD5) */
    
    @Override
    public Trader checkLogin(String email, String password) {        
        Trader trader = null;
        try {
          List result =  (List) em.createQuery("SELECT t FROM Trader t "
                       + "WHERE t.email = :email AND t.password = :pwd")
                       .setParameter("email", email)
                       .setParameter("pwd", password)
                       .getResultList();
          if (result.size() == 1) {
              trader = (Trader) result.get(0);
          }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return trader;
    }

    @Override
    public Trader checkLoginRemote(String email, String password) {
        return checkLogin(email, password);
    }

    /* Checks whether there is already an email registered in our system */
    
    @Override
    public boolean verifyEmail(String email) {
        boolean emailExists = false;
        try {
            int i = em.createQuery("SELECT t FROM Trader t "
                    + "WHERE t.email = :email")
                    .setParameter("email", email)
                    .getResultList().size();
            if (i != 0) emailExists = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return emailExists;
    }

    @Override
    public boolean verifyEmailRemote(String emailsignup) {
        return verifyEmail(emailsignup);
    }

}
