package trader;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.jms.*;
import javax.swing.JOptionPane;
import market.MarketGUI;
import middleware.common.Result;
import middleware.common.StockProduct;
import middleware.communication.Producer;
import middleware.communication.Subscriber;
import middleware.exception.ProducerException;
import middleware.exception.SubscriberException;

/**
 *
 *  @author felmas
 */
/*
 * Main class 
 * Creates market GUI and provides a GUI for traders
 */
public class JoinMarketGUI extends javax.swing.JFrame {

    // JMS Resources
    // Topic name for subscriber
    // Queue name for producer
    private final String controlQueueName = "msControl";
    private final String updateTopicName = "msUpdate";
    private Subscriber updateSubscriber;
    private Producer producer;
    
    // Trader names serviced by the client
    private ArrayList<String> userNames;
    
    // Variables to query trader name from server
    private boolean isWaitingForQuery;
    private Result queryResult;
    private String query;
    private long time;
    
    // Children of this GUI
    // One market and multiple traders
    private MarketGUI market;
    private ArrayList<UserGUI> users;
    
    public JoinMarketGUI() {
        
        // Subscriber creation
        try {
            this.updateSubscriber = new Subscriber(updateTopicName, new MessageListener() {

                @Override
                public void onMessage(Message message) {
                   
                    // User enter/exit response processing
                    if(message instanceof TextMessage) {
                        String result;
                        try {
                            result = ((TextMessage)message).getText();
                        } catch(JMSException e) {
                            System.err.println("User name couldn't be checked: " + e.getMessage());
                            queryResult = Result.FAIL;
                            return;
                        }

                        if(result.startsWith(query)) {
                            if(result.endsWith("Ok")) {
                                Trader.setStockCounter(Integer.parseInt(result.split(":")[2]));
                                queryResult = Result.OK;
                            }
                            else queryResult = Result.FAIL;
                            isWaitingForQuery = false;
                        } 
                    } 
                    // Regular stock update
                    else if(message instanceof ObjectMessage) {

                        StockProduct sp;
                        try {
                            sp = (StockProduct)((ObjectMessage)message).getObject();
                        } catch(JMSException e) {
                            System.err.println("New stock product couldn't be read: " + e.getMessage());
                            return;
                        } 
                        if(Trader.updateStock(sp)) {
                            for(UserGUI u : users) {
                                u.fillTraderPortfolio(sp.clone());
                            }
                            if(Trader.getStocks().size() == Trader.getStockCounter()) {
                                for(UserGUI u : users) {
                                    u.drawStockContainer();
                                }
                            }
                        }
                        if(market != null) market.addStockEvent(sp);
                    }
                };
            });
            this.updateSubscriber.start();
        } catch(SubscriberException e) {
            System.err.println("Subscriber couldn't be created: " + e.getMessage());
            System.exit(0);
        }
      
        // Producer creation
        try {
            this.producer = new Producer(controlQueueName);
            this.producer.start();
        } catch(ProducerException e) {
            System.err.println("Producer couldn't be created: " + e.getMessage());
            System.exit(0);
        }
            
        // Initialization of other variables
        this.userNames = new ArrayList<String>();
        this.users = new ArrayList<UserGUI>();
        
        this.isWaitingForQuery = false;
        this.queryResult = Result.OK;
        
        // GUI init
        initComponents();
        
        // Sets the location to the center of window
        setLocation();
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        nameTextField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MockStock Rich Client");
        setResizable(false);
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        nameTextField.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        nameTextField.setMinimumSize(new java.awt.Dimension(10, 32));
        nameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nameTextFieldFocusGained(evt);
            }
        });
        nameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nameTextFieldKeyPressed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        jButton1.setText("Join Market");
        jButton1.setMaximumSize(new java.awt.Dimension(98, 32));
        jButton1.setMinimumSize(new java.awt.Dimension(98, 32));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        jLabel1.setText("Trader name:");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(nameTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jLabel1)
                .add(nameTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    // Finds the center of the screen
    // Puts the window to the center
    // This method is taken from java.sun.com tutorials
    private void setLocation() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        
        int w = this.getSize().width;
        int h = this.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;

        this.setLocation(x, y);
    }
    
    // Removes all traders handled by the client from the market
    public void removeAllTraders() {
    
        for(String userName : userNames) {
            removeTrader(userName);
        }
    
    }
    
    // Removes one trader from the market
    public void removeTrader(String userName) {
    
        try {
            producer.sendTextMessage("remove:" + userName);
        } catch(ProducerException e) {
            System.err.println("Trader is removed from locale but message couldn't be sent to the server: " + e.getMessage());
        }
        this.userNames.remove(userName);
    
    }
    
    // Joins new trader into market
    // If this is the first trader, market is also started
    private void addNewTrader(String userName) {
        
        // There is already a user with this name
        if(queryResult == Result.FAIL && !isWaitingForQuery) {

            JOptionPane.showMessageDialog(this, userName + " is already in use\nPlese choose another name!");

        } else if(isWaitingForQuery) {
        
            JOptionPane.showMessageDialog(this, "Please be sure that server is running!");
            
        } else {

            // Market isn't started so start
            if(market == null) {
                market = new MarketGUI();
                market.setVisible(true);
            }
            
            // New trader joins
            UserGUI newUser = new UserGUI(this, userName, producer);
            users.add(newUser);
            newUser.setVisible(true);
            jPanel1.setEnabled(false);

            userNames.add(userName);
        }
        
    }
    
    // Checks whether requested user name is already in use
    private void enterMarket() {
        
        final String userName = nameTextField.getText().trim();
        
        if (!userName.equals("")) {
            
            if(userNames.contains(userName)) {
            
                JOptionPane.showMessageDialog(this, userName + " is already in use\nPlese choose another name!");
                
            } else {
                
                if(!sendQuery(userName)) return;
                
                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        
                        while(isWaitingForQuery && System.currentTimeMillis() - time < 3000);
                        addNewTrader(userName);
                        
                    }
                });
                t.start();        
                         
            }  
        }
        nameTextField.selectAll();
    }
    
    // User enter/exit protocol request creation
    // Asks server whether there is a user with this name
    private boolean sendQuery(String userName) {
        
        time = System.currentTimeMillis();
        queryResult = Result.FAIL;
        query = System.nanoTime() + ":" + userName;
        try {
            producer.sendTextMessage(query);
        } catch(ProducerException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(this, "User name couldn't be checked from server");    
            return false;
        }
        
        isWaitingForQuery = true; 
        return true;
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        enterMarket();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void nameTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameTextFieldKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) enterMarket();
    }//GEN-LAST:event_nameTextFieldKeyPressed

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        nameTextField.requestFocusInWindow();
    }//GEN-LAST:event_formFocusGained

    private void nameTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextFieldFocusGained
        nameTextField.selectAll();
    }//GEN-LAST:event_nameTextFieldFocusGained

    // Main
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                new JoinMarketGUI().setVisible(true);
            }
        });
           
    } 
 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField nameTextField;
    // End of variables declaration//GEN-END:variables
    
}
