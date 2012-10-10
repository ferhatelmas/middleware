package market;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import javax.swing.Timer;
import middleware.common.StockProduct;
import trader.UserGUI;

/*
 * @author felmas
 */
/*
 * Market GUI that shows update events
 */
public class MarketGUI extends javax.swing.JFrame implements ActionListener {
    
    private StockListModel model;
    private Timer timer;
    private UserGUI parent;
    private MarketAnalytics child;
    private final List<String> stockNames;
    
    public MarketGUI(UserGUI parent, List<String> stockNames) {
        this.parent = parent;
        this.child = null;
        this.stockNames = stockNames;
        
        initComponents();
        this.setTitle("MockStock Market");
        
        this.model = new StockListModel();
        this.jListEvent.setModel(model);
        
        this.timer = new Timer(1000, this);
        this.timer.setInitialDelay(0);
        this.timer.start();
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelBody = new javax.swing.JPanel();
        jScrollPaneEvent = new javax.swing.JScrollPane();
        jListEvent = new javax.swing.JList();
        jButtonClear = new javax.swing.JButton();
        analyticsButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanelBody.setBorder(javax.swing.BorderFactory.createTitledBorder("Events list"));

        jScrollPaneEvent.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jListEvent.setBackground(java.awt.SystemColor.controlHighlight);
        jListEvent.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jListEvent.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPaneEvent.setViewportView(jListEvent);

        jButtonClear.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        jButtonClear.setText("Clear events list");
        jButtonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearActionPerformed(evt);
            }
        });

        analyticsButton.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        analyticsButton.setText("Show analytics");
        analyticsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analyticsButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanelBodyLayout = new org.jdesktop.layout.GroupLayout(jPanelBody);
        jPanelBody.setLayout(jPanelBodyLayout);
        jPanelBodyLayout.setHorizontalGroup(
            jPanelBodyLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelBodyLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPaneEvent, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelBodyLayout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(analyticsButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 158, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jButtonClear)))
                .addContainerGap())
        );
        jPanelBodyLayout.setVerticalGroup(
            jPanelBodyLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelBodyLayout.createSequentialGroup()
                .add(jScrollPaneEvent, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelBodyLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButtonClear)
                    .add(analyticsButton))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelBody, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelBody, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void actionPerformed(ActionEvent e) {
        jListEvent.updateUI();
    }
    
    public void addStockEvents(HashMap<Integer, StockProduct> stocks, HashMap<Integer, String> priceDiffs) {
        for(int i : stocks.keySet()) {
            addStockEvent(stocks.get(i), priceDiffs.get(i));
        }
    }
    
    private void addStockEvent(StockProduct stock, String diff) {
        model.addFront(stock.getStockName() + ":\tPrice:\t" + stock.getStockPrice() + "\tDiff:\t" + diff);  
    };
    
    public void updateUI() {
        jListEvent.updateUI();
    }
    
    public void closeChild() {
        child = null;
    }
        
    private void jButtonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearActionPerformed
        model.clear();
    }//GEN-LAST:event_jButtonClearActionPerformed

    private void analyticsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_analyticsButtonActionPerformed
        child = new MarketAnalytics(this, stockNames);
        child.setVisible(true);
    }//GEN-LAST:event_analyticsButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        parent.closeMarket();
        if(child != null) {
            child.setVisible(false);
            child.dispose();
            child = null;
        }
    }//GEN-LAST:event_formWindowClosing
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton analyticsButton;
    private javax.swing.JButton jButtonClear;
    private javax.swing.JList jListEvent;
    private javax.swing.JPanel jPanelBody;
    private javax.swing.JScrollPane jScrollPaneEvent;
    // End of variables declaration//GEN-END:variables

}
