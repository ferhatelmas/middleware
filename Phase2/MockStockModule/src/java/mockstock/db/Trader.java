package mockstock.db;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import mockstock.db.enumeration.UserRight;
import mockstock.db.enumeration.UserStatus;

/**
 *
 * @author Kenny Lienhard
 */
/* User model in our db */
@Entity
@Table(name="TRADER")
public class Trader implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name="EMAIL")
    @NotNull
    private String email;
    
    @Column(name="PASSWORD")
    @NotNull
    private String password;
    
    @Column(name="FIRSTNAME")
    private String firstname;
    
    @Column(name="LASTNAME")
    private String lastname;
    
    @Enumerated(EnumType.STRING)
    @Column(name="USRGROUP")
    private UserRight usrgroup;
    
    @Enumerated(EnumType.STRING)
    @Column(name="STATUS")
    private UserStatus status;
    
    @OneToMany(mappedBy = "trader", fetch = FetchType.EAGER,
             cascade  = {CascadeType.PERSIST})
    private List<TraderTransaction> transactions;
    
    @OneToMany(mappedBy = "trader", fetch = FetchType.EAGER,
             cascade  = {CascadeType.PERSIST})
    private List<Portfolioproduct> portfolioproducts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public List<TraderTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TraderTransaction> transactions) {
        this.transactions = transactions;
    }

    public List<Portfolioproduct> getPortfolioproducts() {
        return portfolioproducts;
    }

    public void setPortfolioproducts(List<Portfolioproduct> portfolioproducts) {
        this.portfolioproducts = portfolioproducts;
    }

    public UserRight getUsrgroup() {
        return usrgroup;
    }

    public void setUsrgroup(UserRight usrgroup) {
        this.usrgroup = usrgroup;
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
        if (!(object instanceof Trader)) {
            return false;
        }
        Trader other = (Trader) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "unil.mockstock.db.Trader[ id=" + id + " ] " + email;
    }
    
}