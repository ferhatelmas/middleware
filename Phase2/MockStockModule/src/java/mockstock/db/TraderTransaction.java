package mockstock.db;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;
import mockstock.db.enumeration.TransactionType;

/**
 *
 * @author Kenny Lienhard
 */
/* Keeps buy/sell operations of users */
@Entity
@Table(name="TRADERTRANSACTION")
public class TraderTransaction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="ID", nullable=false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name="QUANTITY")
    private int quantitiy;
    
    @Column(name="ACTIONDATE")
    //@Temporal(TemporalType.TIMESTAMP)
    private Timestamp actiondate;
    
    @Enumerated(EnumType.STRING)
    @Column(name="TYPE")
    private TransactionType type;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trader")
    private Trader trader;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sphistory")
    private SPHistory sphistory;

    public Timestamp getActiondate() {
        return actiondate;
    }

    public void setActiondate(Timestamp actiondate) {
        this.actiondate = actiondate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantitiy() {
        return quantitiy;
    }

    public void setQuantitiy(int quantitiy) {
        this.quantitiy = quantitiy;
    }

    public SPHistory getSphistory() {
        return sphistory;
    }

    public void setSphistory(SPHistory sphistory) {
        this.sphistory = sphistory;
    }

    public Trader getTrader() {
        return trader;
    }

    public void setTrader(Trader trader) {
        this.trader = trader;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
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
        if (!(object instanceof TraderTransaction)) {
            return false;
        }
        TraderTransaction other = (TraderTransaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "unil.mockstock.db.Transaction[ id=" + id + " ]";
    }
}
