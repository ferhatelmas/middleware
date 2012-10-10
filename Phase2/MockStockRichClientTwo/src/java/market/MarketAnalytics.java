package market;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.Timer;
import mockstock.db.SPHistory;
import mockstock.ejb.front.AnalyticsBeanRemote;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import trader.Trader;
import util.Utils;

/**
 *
 * @author felmas
 */
/*
 * Class used to draw history charts
 */
public class MarketAnalytics extends javax.swing.JFrame implements ActionListener {

    private Timer timer;
    private MarketGUI parent;
    
    private final List<String> stockNames;
    
    private AnalyticsBeanRemote analyticsBean;
    
    public MarketAnalytics(MarketGUI parent, List<String> stockNames) {
        this.stockNames = stockNames;
        this.parent = parent;
        
        this.analyticsBean = (AnalyticsBeanRemote) Utils.getBean(Utils.BEAN.ANALYTICS);
        
        initComponents();
        this.setTitle("Market Analytics");
        this.setSize(500, 500);
        
        this.timer = new Timer(6000, this);
        this.timer.setInitialDelay(0);
        this.timer.start();
    }

    // Refreshes the charts
    @Override
    public void actionPerformed(ActionEvent e) {
        drawCharts();
    }
    
    // Sets layout of the frame
    // Gets history of stocks
    // Draws XY chart for each stock
    // Adds charts into frame
    private void drawCharts() {
        
        this.rootPane.removeAll();
        this.rootPane.setLayout(new GridLayout(stockNames.size(), 1));
        
        HashMap<Integer, ArrayList<Double>> history = Trader.getHistory();
        
        for(String stockName : stockNames) {
            XYSeries series = new XYSeries("History");
            List<SPHistory> data = analyticsBean.getStockEvolutionRemote(stockName);
            
            int len = data.size();
            for(int j=len-1; j>=0; j--) {
                series.add(j, data.get(len-j-1).getPrice());
            }
            XYSeriesCollection c = new XYSeriesCollection(series);
            JFreeChart chart = ChartFactory.createXYBarChart(stockName, 
                                          "Update", 
                                          false, 
                                          "Price", 
                                          c, 
                                          PlotOrientation.VERTICAL, 
                                          true, 
                                          true, 
                                          false);
            this.rootPane.add(new ChartPanel(chart));
        }
        this.rootPane.revalidate();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        parent.closeChild();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
