package mockstock.db;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Kenny Lienhard
 */
/* Portfolio data of users of each stock */
@Entity
@Table(name="PORTFOLIOPRODUCT")
public class Portfolioproduct implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trader")
    private Trader trader;
    
    @Column(name="QUANTITY")
    private int quantity;
    
    @Column(name="PRICE")
    private double price;
    
    @Column(name="STOCKRESULT")
    private double stockresult;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stockproduct")
    private Stockproduct stockproduct;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getStockresult() {
        return stockresult;
    }

    public void setStockresult(double stockresult) {
        this.stockresult = stockresult;
    }

    public Trader getTrader() {
        return trader;
    }

    public void setTrader(Trader trader) {
        this.trader = trader;
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
        if (!(object instanceof Portfolioproduct)) {
            return false;
        }
        Portfolioproduct other = (Portfolioproduct) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "unil.mockstock.db.Portfolioproduct[ id=" + id + " ]";
    }
    
}
