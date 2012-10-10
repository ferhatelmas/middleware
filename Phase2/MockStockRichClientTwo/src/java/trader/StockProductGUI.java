package trader;

import java.awt.Component;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author felmas
 */
/*
 * This is GUI for only one Stock
 * One label, one textbox and two buttons
 */
public class StockProductGUI extends javax.swing.JPanel {

    private int id;
    
    public StockProductGUI(int id, String stockLabel, MouseListener ml) {
        this.id = id;
        initComponents();
        this.stockLabel.setText(stockLabel);
        this.addMouseListener(ml);
    }                                     

    public StockProductGUI() {
        initComponents();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        stockLabel = new javax.swing.JLabel();
        qtyTextBox = new javax.swing.JTextField();
        buyButton = new javax.swing.JButton();
        sellButton = new javax.swing.JButton();

        stockLabel.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        stockLabel.setText("jLabel1");
        stockLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        qtyTextBox.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        qtyTextBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                qtyTextBoxFocusGained(evt);
            }
        });

        buyButton.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        buyButton.setText("Buy");
        buyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buyButtonActionPerformed(evt);
            }
        });

        sellButton.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        sellButton.setText("Sell");
        sellButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sellButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(stockLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(qtyTextBox, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(sellButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(qtyTextBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(buyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(sellButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(stockLabel))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buyButtonActionPerformed
        MouseListener ml = this.getMouseListeners()[0];
        try {
            int qty = Integer.parseInt(qtyTextBox.getText());
            if(qty > 0) {
                ml.mouseClicked(new StockEvent("buy", qty, id, this,
                (Component)evt.getSource(), 0, 0, 0, 0, 0, 0, false)); // unimportant
            } else {
                JOptionPane.showMessageDialog(buyButton, "Please enter positive values!");
                getFocusAndSelectAll();
            }
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(buyButton, "Please enter numeric values!");
            getFocusAndSelectAll();
        }
    }//GEN-LAST:event_buyButtonActionPerformed

    private void sellButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sellButtonActionPerformed
        MouseListener ml = this.getMouseListeners()[0];
        try {
            int qty = Integer.parseInt(qtyTextBox.getText());
            if(qty > 0) {
                ml.mouseClicked(new StockEvent("sell", qty, id, this,
                (Component)evt.getSource(), 0, 0, 0, 0, 0, 0, false)); // unimportant
            } else {
                JOptionPane.showMessageDialog(buyButton, "Please enter positive values!");
                getFocusAndSelectAll();
            }
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(buyButton, "Please enter numeric values!");
            getFocusAndSelectAll();
        }
    }//GEN-LAST:event_sellButtonActionPerformed

    private void qtyTextBoxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_qtyTextBoxFocusGained
        qtyTextBox.selectAll();
    }//GEN-LAST:event_qtyTextBoxFocusGained

    private void getFocusAndSelectAll() {
        qtyTextBox.selectAll();
        qtyTextBox.requestFocusInWindow();
    }
    
    public JLabel getStockLabel() {
        return stockLabel;
    }
    
    public JTextField getQtyTextBox() {
        return qtyTextBox;
    }
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buyButton;
    private javax.swing.JTextField qtyTextBox;
    private javax.swing.JButton sellButton;
    private javax.swing.JLabel stockLabel;
    // End of variables declaration//GEN-END:variables
}
