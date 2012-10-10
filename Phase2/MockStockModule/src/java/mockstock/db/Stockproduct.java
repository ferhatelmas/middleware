package mockstock.db;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Kenny Lienhard
 */
/* Stock product model */
@Entity
@Table(name="STOCKPRODUCT")
public class Stockproduct implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name="NAME")
    @NotNull
    private String name;
    
    @OneToMany(mappedBy = "stockproduct", fetch = FetchType.EAGER,
             cascade  = {CascadeType.PERSIST})
    private Collection<SPHistory> sphistory;
    
    @OneToMany(mappedBy = "stockproduct", fetch = FetchType.EAGER,
             cascade  = {CascadeType.PERSIST})
    private Collection<Portfolioproduct> portfolioproducts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<SPHistory> getSphistory() {
        return sphistory;
    }

    public void setSphistory(Collection<SPHistory> sphistory) {
        this.sphistory = sphistory;
    }

    public Collection<Portfolioproduct> getPortfolioproducts() {
        return portfolioproducts;
    }

    public void setPortfolioproducts(Collection<Portfolioproduct> portfolioproducts) {
        this.portfolioproducts = portfolioproducts;
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
        if (!(object instanceof Stockproduct)) {
            return false;
        }
        Stockproduct other = (Stockproduct) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "unil.mockstock.db.Stockproduct[ id=" + id + " ]";
    }
}
