package mockstock.ejb.front;

import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import mockstock.db.Trader;

/**
 *
 * @author Kenny Lienhard
 */
@Stateful(name = "Admin", mappedName = "Admin")
@Local(AdminLocal.class)
@Remote(AdminRemote.class)
public class AdminBean implements AdminRemote, AdminLocal {

    @PersistenceContext
    private EntityManager em;

    public AdminBean() {
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

    /* GETTER methods */
    
    @Override
    public List<Trader> getTraderList() {
        return em.createQuery("SELECT t FROM Trader t").getResultList();
    }
    
    @Override
    public List<Trader> getTraderListRemote() {
        return em.createQuery("SELECT t FROM Trader t").getResultList();
    }

    @Override
    public Trader getTrader(Long id) {
        return em.find(Trader.class, id);
    }
    
    @Override
    public Trader getTraderRemote(Long id) {
        return getTrader(id);
    }

    /* UPDATE methods */
    
    @Override
    public Trader updateTrader(Trader editedTrader) {
        return (Trader) this.mergeEntity(editedTrader);
    }

    @Override
    public Trader updateTraderRemote(Trader editedTrader) {
        return updateTrader(editedTrader);
    }
    
    /* REMOVE methods */
    
    @Override
    public void removeTrader(Trader trader) {
        this.removeEntity(trader);
    }

    @Override
    public void removeTraderRemote(Trader trader) {
        removeTrader(trader);
    }
    
}
