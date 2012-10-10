/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.Timer;
import market.StockListModel;
import mockstock.db.TraderTransaction;
import mockstock.db.enumeration.TransactionType;
import mockstock.ejb.front.TraderTransactionRemote;

/**
 *
 * @author felmas
 */
public final class HistoryGUI extends javax.swing.JFrame implements ActionListener {

    private TraderTransactionRemote traderBean;
    private String email;
    private UserGUI parent;
    private StockListModel model;
    private Timer timer;
    
    public HistoryGUI(TraderTransactionRemote traderBean, String email, UserGUI parent) {
        this.traderBean = traderBean;
        this.email = email;
        this.parent = parent;
        initComponents();
        this.setTitle("Transactions");
        
        this.model = new StockListModel();
        this.transactions.setModel(model);
        
        this.timer = new Timer(1000, this);
        this.timer.setInitialDelay(0);
        this.timer.start();
        setTransactions();
    }

    public void setTransactions() {
        model.clearNormal();
        for(TraderTransaction t : traderBean.getSortedTransactionListRemote(email)) {
           String s = email + " has ";
           if(t.getType() == TransactionType.BUY) {
               s += "bought "; 
           } else {
               s += "sold ";
           }
           s += t.getQuantitiy();
           s += " " + t.getSphistory().getStockproduct().getName() + " at " + t.getActiondate();
           model.addFront(s);
       }
       transactions.updateUI();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        transactions.updateUI();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        transactions = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        transactions.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(transactions);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.parent.closeHistory();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList transactions;
    // End of variables declaration//GEN-END:variables
}