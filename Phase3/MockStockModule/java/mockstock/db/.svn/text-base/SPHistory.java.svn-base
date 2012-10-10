package mockstock.db;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import javax.persistence.*;

/**
 *
 * @author Kenny Lienhard
 */
/* Keeps price changes of stock products */
@Entity
@Table(name="SPHISTORY")
// THERE IS A BUG IN GLASSFISH CONCERNING NAMED QUERIES, see
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=357177
//@NamedQueries({
//@NamedQuery(name = "SPHistory.findLatestPriceForSP", 
//        query = "SELECT o FROM SPHistory o WHERE o.stockproduct = :stockproduct"
//        + " AND o.historydate = (SELECT MAX(d.historydate) FROM SPHistory d)")
//})
public class SPHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name="PRICE")
    private double price;
    
    @Column(name="HISTORYDATE")
    //@Temporal(TemporalType.TIMESTAMP)
    private Timestamp historyDate;
    
    @OneToMany(mappedBy = "sphistory", fetch = FetchType.EAGER,
             cascade  = {CascadeType.PERSIST})
    private Collection<TraderTransaction> transactions;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stockproduct")
    private Stockproduct stockproduct;

    public Timestamp getHistoryDate() {
        return historyDate;
    }

    public void setHistoryDate(Timestamp historyDate) {
        this.historyDate = historyDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Collection<TraderTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Collection<TraderTransaction> transactions) {
        this.transactions = transactions;
    }

    public Stockproduct getStockproduct() {
        return stockproduct;
    }

    public void setStockproduct(Stockproduct stockproduct) {
        this.stockproduct = stockproduct;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SPHistory)) {
            return false;
        }
        SPHistory other = (SPHistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "unil.mockstock.db.SPHistory[ id=" + id + " ]";
    }
}
