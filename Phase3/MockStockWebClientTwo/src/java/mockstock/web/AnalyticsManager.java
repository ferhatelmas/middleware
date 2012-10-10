package mockstock.web;

import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import mockstock.ejb.front.AnalyticsBeanRemote;

/**
 * bean for analytics to draw graphs, is related to a user, as his portfolio is
 * shown next to the market indexes
 *
 * @author Kenny Lienhard
 */
@ManagedBean
@SessionScoped
public class AnalyticsManager {

    private List applePrices;
    private List sunPrices;
    private List ibmPrices;
    @EJB
    AnalyticsBeanRemote analyticsRemote;

    /**
     * getter and setters, gettter gets the last 30 entries in stock product
     * price history according to the requested stock product
     *
     * @return
     */
    public List getApplePrices() {
        this.applePrices = analyticsRemote.getStockEvolutionRemote("Apple");
        return this.applePrices;
    }

    public void setApplePrices(List applePrices) {
        this.applePrices = applePrices;
    }

    public List getIbmPrices() {
        this.applePrices = analyticsRemote.getStockEvolutionRemote("IBM");
        return this.applePrices;
    }

    public void setIbmPrices(List ibmPrices) {
        this.ibmPrices = ibmPrices;
    }

    public List getSunPrices() {
        this.applePrices = analyticsRemote.getStockEvolutionRemote("Sun");
        return this.applePrices;
    }

    public void setSunPrices(List sunPrices) {
        this.sunPrices = sunPrices;
    }
}