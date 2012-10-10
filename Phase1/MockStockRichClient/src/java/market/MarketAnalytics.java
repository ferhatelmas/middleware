package market;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.Timer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import trader.Trader;

/**
 *
 * @author felmas
 */
/*
 * Class used to draw history charts
 */
public class MarketAnalytics extends javax.swing.JFrame implements ActionListener {

    private Timer timer;
    
    public MarketAnalytics() {
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
        this.rootPane.setLayout(new GridLayout(Trader.getStocks().size(), 1));
        HashMap<Integer, ArrayList<Double>> history = Trader.getHistory();
        
        for(int i : history.keySet()) {
            XYSeries series = new XYSeries("History");
            ArrayList<Double> data = history.get(i);
            for(int j=0; j<data.size(); j++) {
                series.add(j, data.get(j));
            }
            XYSeriesCollection c = new XYSeriesCollection(series);
            JFreeChart chart = ChartFactory.createXYBarChart(Trader.getStocks().get(i).getStockName(), 
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
