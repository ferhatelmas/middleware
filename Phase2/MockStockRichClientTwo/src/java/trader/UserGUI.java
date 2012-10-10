package trader;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Timestamp;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import market.MarketGUI;
import market.PortfolioAnalytics;
import middleware.common.StockProduct;
import mockstock.db.Portfolioproduct;
import mockstock.db.SPHistory;
import mockstock.db.Trader;
import mockstock.db.TraderTransaction;
import mockstock.db.enumeration.TransactionType;
import mockstock.ejb.front.TraderTransactionRemote;
import util.Utils;

/**
 * @author felmas
 */
/*
 * This main GUI that a user can do buy/sell transactions
 */
public class UserGUI extends javax.swing.JFrame {
 
    private static final long stockUpdateInterval = 6000;
    private HashMap<Integer, StockProduct> stocks;
    private HashMap<Integer, String> priceDiffs;
    private Timer timer;
    
    private JoinMarketGUI parent;
    
    // Stock model of the user
    private Trader trader;
    private String userName;
   
    private TraderTransactionRemote traderBean;
    
    private MarketGUI market;
    private PortfolioAnalytics portfolio;
    private HistoryGUI historyGUI;
    
    public UserGUI(Trader trader, JoinMarketGUI parent) {
        this.market = null;
        this.portfolio = null;
        this.historyGUI = null;
        
        this.parent = parent;
        
        this.traderBean = (TraderTransactionRemote) Utils.getBean(Utils.BEAN.TRADER_TRANSACTION);
        this.trader = trader;
        /* backup calls */
        this.trader.setPortfolioproducts(traderBean.getPortfolioproductsRemote(trader.getEmail()));
        this.trader.setTransactions(traderBean.getSortedTransactionListRemote(trader.getEmail()));
        
        /* GUI init */
        initComponents();
        this.setTitle("Trader " + getUserName());
        
        /* Stocks init */
        stocks = traderBean.getAllStockProductsRemote();
        priceDiffs = traderBean.getAllStockDifferencesRemote();
        
        displayStocks();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                stocks = traderBean.getAllStockProductsRemote();
                priceDiffs = traderBean.getAllStockDifferencesRemote();
                
                if(market != null) {
                    market.addStockEvents(stocks, priceDiffs);
                }
            }
            
        }, stockUpdateInterval, stockUpdateInterval);
    }
    
    private String getUserName() {
        if(trader.getFirstname() != null && !trader.getFirstname().isEmpty()) {
            userName = trader.getFirstname();
        } else if(trader.getLastname() != null && !trader.getLastname().isEmpty()) {
            userName = trader.getLastname();
        } else {
            userName = trader.getEmail();
        }
        return userName;
    }
    
    private double getTotalResult() {
        double total = 0;
        for(Portfolioproduct p : trader.getPortfolioproducts()) {
            total += p.getStockresult();
        }
        return total;
    }
    
    // Draws stocks -- Qty, Result, Buttons and TextBoxes 
    private void displayStocks(){
        jLabel1.setText("Portfolio of " + this.userName);
        drawStockContainer();
    }
    
    // Redraws one stock in stockContainer after buy/sell transaction
    private void updateStockContainer(StockProductGUI s, int id) {
        jLabel2.setText("Total result: " + getTotalResult());
        s.getStockLabel().setText(getStockProductText(stocks.get(id).getStockName()));
        
        for(Component c : stockContainer.getComponents()) {
            if(c instanceof StockProductGUI) {
                StockProductGUI spg = (StockProductGUI)c;
                spg.getQtyTextBox().setText("");
            }
        } 
        s.getQtyTextBox().requestFocusInWindow();
    }
    
    private String getStockProductText(String name) {
        for(Portfolioproduct p : trader.getPortfolioproducts()) {
            if(p.getStockproduct().getName().equals(name)) {
                return "<html>" + p.getStockproduct().getName() +  
                        "<br>Quantity: " + p.getQuantity() + 
                        " Result: " + p.getStockresult() + "</html>";
            } 
        }
        return "<html>" + name + "<br>Quantity: 0.0 Result: 0.0</html>";  
    }
    
    // Gets stock info from trader model
    // Sets layout and fills the container with stock info
    public void drawStockContainer() {
        
        jLabel2.setText("Total result: " + getTotalResult());
        
        stockContainer.removeAll();
        stockContainer.setLayout(new GridLayout(stocks.size(), 1));
        
        for(int i : stocks.keySet()) {
            
            
            StockProductGUI spg = new StockProductGUI(i, getStockProductText(stocks.get(i).getStockName()), new MouseListener() {

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
                    
                    SPHistory history = traderBean.findLatestSPHistoryForSPRemote(stocks.get(se.getStockId()).getStockName());
                    
                    if("sell".equals(se.getType())) { 
                        if(!processPortfolioForSell(history, stocks.get(se.getStockId()), se.getQty())) {
                            se.getSourceStock().getQtyTextBox().requestFocusInWindow();
                            se.getSourceStock().getQtyTextBox().selectAll();
                            return;
                        }
                    } else {
                        processPortfolioForBuy(history, stocks.get(se.getStockId()), se.getQty());
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
    
    private void createTransaction(SPHistory history, StockProduct stock, TransactionType type, int qty) {
        TraderTransaction transaction = new TraderTransaction();
        transaction.setActiondate(new Timestamp(System.currentTimeMillis()));
        transaction.setQuantitiy(qty);
        transaction.setTrader(trader);
            
        transaction.setSphistory(history);
        transaction.setType(type);
                    
        traderBean.createTransactionRemote(transaction, stock);
        trader.getTransactions().add(transaction);
    }
    
    private void processPortfolioForBuy(SPHistory history, StockProduct stock, int qty) {
        createTransaction(history, stock, TransactionType.BUY, qty);
        
        if(traderBean.productExistsInPortfolioRemote(stock.getStockName(), trader.getEmail())) {
            trader.setPortfolioproducts(traderBean.getPortfolioproductsRemote(trader.getEmail()));
            for(Portfolioproduct p : trader.getPortfolioproducts()) {
                if(p.getStockproduct().getName().equals(stock.getStockName())) {
                    p.setPrice(history.getPrice());
                    p.setQuantity(p.getQuantity() + qty);
                    traderBean.updatePortfolioproductRemote(p);
                    break;
                }
            }
        } else {
            Portfolioproduct p = new Portfolioproduct();
            p.setPrice(history.getPrice());
            p.setQuantity(qty);
            p.setStockproduct(traderBean.getStockproductRemote(stock.getStockName()));
            p.setTrader(trader);
            traderBean.createPortfolioproductRemote(p);
        }
        trader.setPortfolioproducts(traderBean.getPortfolioproductsRemote(trader.getEmail()));
        if(portfolio != null) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    portfolio.drawPortfolio();
                }
            }).start();
        }
        
        if(historyGUI != null) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    historyGUI.setTransactions();
                }
            }).start();
        }
    }
    
    private boolean processPortfolioForSell(SPHistory history, StockProduct stock, int qty) {
        
        if (traderBean.productExistsInPortfolioRemote(stock.getStockName(), trader.getEmail())) {           
            Portfolioproduct p = traderBean.getPortfolioproductRemote(stock.getStockName(), trader.getEmail());
            if(p.getQuantity() >= qty) {
                createTransaction(history, stock, TransactionType.SELL, qty);
                p.setQuantity(p.getQuantity() - qty);

                double adjust = ((qty * history.getPrice()) - (qty * p.getPrice()));
                p.setStockresult(p.getStockresult() + Math.round(adjust));
                p.setPrice(history.getPrice());

                traderBean.updatePortfolioproductRemote(p);
                trader.setPortfolioproducts(traderBean.getPortfolioproductsRemote(trader.getEmail()));
                if(portfolio != null) {
                   new Thread(new Runnable() {

                    @Override
                    public void run() {
                        portfolio.drawPortfolio();
                    }
                    }).start();
                }
                
                if(historyGUI != null) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            historyGUI.setTransactions();
                        }
                    }).start();
                }
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "You can not sell more than what you have!");
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(this, "You can not sell stock product that you don't have!");
            return false;
        }
    }
    
    public void closeMarket() {
        this.market = null;
    }
    
    public void closePortfolio() {
        this.portfolio = null;
    }
    
    public void closeHistory() {
        this.historyGUI = null;
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
        portfolioButton = new javax.swing.JButton();
        marketButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

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

        portfolioButton.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        portfolioButton.setText("Portfolio Analytics");
        portfolioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                portfolioButtonActionPerformed(evt);
            }
        });

        marketButton.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        marketButton.setText("Market Analytics");
        marketButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                marketButtonActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        jButton1.setText("History");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jpanel3Layout = new org.jdesktop.layout.GroupLayout(jpanel3);
        jpanel3.setLayout(jpanel3Layout);
        jpanel3Layout.setHorizontalGroup(
            jpanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jpanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 425, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jpanel3Layout.createSequentialGroup()
                        .add(portfolioButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 190, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(marketButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 190, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 37, Short.MAX_VALUE)
                .add(jLabel7)
                .add(46, 46, 46))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jpanel3Layout.createSequentialGroup()
                .add(jpanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jpanel3Layout.createSequentialGroup()
                        .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 259, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(111, 111, 111)
                        .add(jLabel6)
                        .add(66, 66, 66)
                        .add(jButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .add(jpanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jpanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jpanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 153, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jpanel3Layout.createSequentialGroup()
                        .add(24, 24, 24)
                        .add(jButton1)))
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
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jpanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(portfolioButton)
                            .add(marketButton))))
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
        System.exit(0);
    }//GEN-LAST:event_formWindowClosing

    private void portfolioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_portfolioButtonActionPerformed
        if(portfolio == null) {
            
            final UserGUI u = this;
            new Thread(new Runnable() {

                @Override
                public void run() {
                     portfolio = new PortfolioAnalytics(traderBean, trader.getEmail(), u);
                     portfolio.setVisible(true);
                }
            }).start();
        }       
    }//GEN-LAST:event_portfolioButtonActionPerformed

    private void marketButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_marketButtonActionPerformed
        if(market == null) {
            List<String> stockNames = new ArrayList<String>(stocks.size());
            for(int i : stocks.keySet()) {
                stockNames.add(stocks.get(i).getStockName());
            }
            market = new MarketGUI(this, stockNames);
            market.setVisible(true);
        }
    }//GEN-LAST:event_marketButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(historyGUI == null) {
            
            final UserGUI u = this;
            new Thread(new Runnable() {

                @Override
                public void run() {
                     historyGUI = new HistoryGUI(traderBean, trader.getEmail(), u);
                     historyGUI.setVisible(true);
                }
            }).start();
        }  
    }//GEN-LAST:event_jButton1ActionPerformed
                               
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel jpanel3;
    private javax.swing.JButton marketButton;
    private javax.swing.JButton portfolioButton;
    private java.awt.ScrollPane scrollPane1;
    private javax.swing.JPanel stockContainer;
    // End of variables declaration//GEN-END:variables
    
}
