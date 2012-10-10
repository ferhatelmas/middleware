package trader;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Set;
import middleware.common.StockProduct;
import middleware.communication.Producer;
import middleware.communication.Transaction;
import middleware.exception.ProducerException;

/**
 * @author felmas
 */
/*
 * This main GUI that a user can do buy/sell transactions
 */
public class UserGUI extends javax.swing.JFrame {
 
    // Stock model of the user
    private Trader trader;
    
    // Producer used to talk to the server for buy/sell
    private Producer producer;
    
    // Parent window to execute user enter/exit protocol
    private JoinMarketGUI parent;
    
    public UserGUI(JoinMarketGUI parent, String userName, Producer producer) {
        
        this.parent = parent;
        this.trader = new Trader(userName);
        this.producer = producer;
        
        initComponents();
        this.setTitle("Trader " + userName);
        displayMyStock();
        
        System.out.println("Trader " + userName + " join the market!");
    }
    
    // As soon as server publishes a new stock, it is added into protfolio
    public void fillTraderPortfolio(StockProduct sp) {
        trader.addStock(sp);
    }
    
    // Draws stocks -- Qty, Result, Buttons and TextBoxes 
    private void displayMyStock(){
        jLabel1.setText("Portofolio of " + trader.getUserName());
        drawStockContainer();
    }
    
    // Redraws one stock in stockContainer after buy/sell transaction
    private void updateStockContainer(StockProductGUI s, int id) {
        jLabel2.setText("Total result: " + trader.getTotalResult());
        s.getStockLabel().setText(trader.getText(id));
        
        for(Component c : stockContainer.getComponents()) {
            if(c instanceof StockProductGUI) {
                StockProductGUI spg = (StockProductGUI)c;
                spg.getQtyTextBox().setText("");
            }
        } 
        s.getQtyTextBox().requestFocusInWindow();
    }
    
    // Gets stock info from trader model
    // Sets layout and fills the container with stock info
    public void drawStockContainer() {
        
        jLabel2.setText("Total result: " + trader.getTotalResult());
        Set<Integer> keys = Trader.getStocks().keySet();
        if(keys.size() != Trader.getStockCounter()) return;
        
        stockContainer.removeAll();
        stockContainer.setLayout(new GridLayout(keys.size(), 1));
        
        for(int i : keys) {
            
            StockProductGUI spg = new StockProductGUI(i, trader.getText(i), new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    StockEvent se;
                    try {
                        se = (StockEvent)e;
                    } catch(ClassCastException ee) {
                        // StockEvent is being waited from buttons 
                        // but other mouseEvents can be fired which shouldn't be used
                        return; 
                    }
                    if("sell".equals(se.getType()) && se.getQty() > trader.getMyStock().get(se.getStockId()).getStockQty()) {
                        se.getSourceStock().getQtyTextBox().requestFocusInWindow();
                        se.getSourceStock().getQtyTextBox().selectAll();
                        return;
                    }
                    
                    trader.update(se.getQty(), se.getStockId(), se.getType());

                    Transaction t = new Transaction(trader.getUserName(), 
                                                    trader.getMyStock().get(se.getStockId()), 
                                                    se.getType(),
                                                    se.getQty());
                    try {
                        producer.sendObjectMessage(t);
                    } catch(ProducerException pe) {
                        System.err.println("Operation couldn't be sent to server: " + pe.getMessage());
                    }
                    updateStockContainer(se.getSourceStock(), se.getStockId());
                }
                @Override
                public void mousePressed(MouseEvent e) {}

                @Override
                public void mouseReleased(MouseEvent e) {}

                @Override
                public void mouseEntered(MouseEvent e) {}

                @Override
                public void mouseExited(MouseEvent e) {}
                
            });
            stockContainer.add(spg);
        }
        stockContainer.revalidate();
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane1 = new java.awt.ScrollPane();
        jpanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        stockContainer = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(javax.swing.UIManager.getDefaults().getColor("Button.light"));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jpanel3.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.light"));
        jpanel3.setForeground(javax.swing.UIManager.getDefaults().getColor("Button.light"));
        jpanel3.setRequestFocusEnabled(false);

        jLabel1.setFont(new java.awt.Font("Futura", 0, 24)); // NOI18N
        jLabel1.setText("Portfolio");

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/images.jpg"))); // NOI18N

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logoMockStock.jpg"))); // NOI18N

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logodop.gif"))); // NOI18N

        stockContainer.setAutoscrolls(true);
        stockContainer.setLayout(new java.awt.GridLayout(1, 0));
        jScrollPane1.setViewportView(stockContainer);

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        jLabel2.setText("jLabel2");

        org.jdesktop.layout.GroupLayout jpanel3Layout = new org.jdesktop.layout.GroupLayout(jpanel3);
        jpanel3.setLayout(jpanel3Layout);
        jpanel3Layout.setHorizontalGroup(
            jpanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 425, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jLabel7)
                .add(46, 46, 46))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jpanel3Layout.createSequentialGroup()
                .add(jpanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jpanel3Layout.createSequentialGroup()
                        .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 259, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(111, 111, 111)
                        .add(jLabel6)
                        .add(0, 225, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jpanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jpanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jpanel3Layout.createSequentialGroup()
                                .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(4, 4, 4)
                                .add(jSeparator1)))))
                .addContainerGap(21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jpanel3Layout.setVerticalGroup(
            jpanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jpanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 153, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(jpanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jpanel3Layout.createSequentialGroup()
                        .add(29, 29, 29)
                        .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jpanel3Layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel1)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 269, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jpanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jpanel3Layout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jpanel3Layout.createSequentialGroup()
                        .add(jLabel2)
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(jpanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jpanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        parent.removeTrader(trader.getUserName());
    }//GEN-LAST:event_formWindowClosing
                               
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel jpanel3;
    private java.awt.ScrollPane scrollPane1;
    private javax.swing.JPanel stockContainer;
    // End of variables declaration//GEN-END:variables
    
}
