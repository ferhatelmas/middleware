package trader;

import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import mockstock.db.Trader;
import mockstock.db.enumeration.UserRight;
import mockstock.db.enumeration.UserStatus;
import mockstock.ejb.front.TraderFacadeRemote;
import util.Utils;

/**
 *
 *  @author felmas
 */
/* Main entrance point for rich client
 * Check credentials and accordingly redirect users
 */
public class JoinMarketGUI extends javax.swing.JFrame {

    private TraderFacadeRemote traderFacade;
    private Trader trader;
    
    public JoinMarketGUI() {
        
        this.traderFacade = (TraderFacadeRemote)Utils.getBean(Utils.BEAN.TRADER_FACADE);
        this.trader = null;
        
        // GUI init
        initComponents();
        emailEditBox.requestFocusInWindow();
        this.setTitle("MockStock Rich Client Main");
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        emailLabel = new javax.swing.JLabel();
        emailEditBox = new javax.swing.JTextField();
        traderJoinButton = new javax.swing.JButton();
        passwordEditBox = new javax.swing.JPasswordField();
        passwordLabel = new javax.swing.JLabel();
        signUpButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MockStock Rich Client");
        setResizable(false);
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        emailLabel.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        emailLabel.setText("Email:");

        emailEditBox.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        emailEditBox.setMinimumSize(new java.awt.Dimension(10, 32));
        emailEditBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                emailEditBoxFocusGained(evt);
            }
        });
        emailEditBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                emailEditBoxKeyPressed(evt);
            }
        });

        traderJoinButton.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        traderJoinButton.setText("Join Market");
        traderJoinButton.setMaximumSize(new java.awt.Dimension(98, 32));
        traderJoinButton.setMinimumSize(new java.awt.Dimension(98, 32));
        traderJoinButton.setPreferredSize(new java.awt.Dimension(98, 32));
        traderJoinButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                traderJoinButtonActionPerformed(evt);
            }
        });

        passwordEditBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordEditBoxFocusGained(evt);
            }
        });
        passwordEditBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passwordEditBoxKeyPressed(evt);
            }
        });

        passwordLabel.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        passwordLabel.setText("Password:");

        signUpButton.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        signUpButton.setText("Sign up");
        signUpButton.setMaximumSize(new java.awt.Dimension(98, 32));
        signUpButton.setMinimumSize(new java.awt.Dimension(98, 32));
        signUpButton.setPreferredSize(new java.awt.Dimension(98, 32));
        signUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signUpButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(passwordLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 23, Short.MAX_VALUE)
                        .add(passwordEditBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 459, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(emailLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(emailEditBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 459, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(traderJoinButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 136, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(signUpButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 136, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .add(0, 24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(14, 14, 14)
                .add(signUpButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(emailEditBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(emailLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(passwordEditBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(passwordLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(8, 8, 8)
                .add(traderJoinButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void enterMarket() {
        
        String email = emailEditBox.getText();
        String hash = Utils.encryptPassword(new String(passwordEditBox.getPassword()));
        
        trader = traderFacade.checkLoginRemote(email, hash);
        if(trader == null) {
            JOptionPane.showMessageDialog(this, "Your credentials does't match to a valid user\n"
                    + "Please be sure you enter correctly!");
            emailEditBox.requestFocusInWindow();
        } else {
            if(trader.getStatus() == UserStatus.ACTIVATED) {
                if(trader.getUsrgroup() == UserRight.USER) {
                    UserGUI userGUI = new UserGUI(trader, this);
                    Utils.setLocation(userGUI);
                    userGUI.setVisible(true);
                } else {
                    AdminGUI adminGUI = new AdminGUI(traderFacade, trader, this);
                    Utils.setLocation(adminGUI);
                    adminGUI.setVisible(true);
                }
                this.setVisible(false);
                cleanFields();
            } else {
                JOptionPane.showMessageDialog(this, "Your account status isn't active\nPlease contact your admin!");
                emailEditBox.requestFocusInWindow();
            }
        }
    }
    
    private void cleanFields() {
        emailEditBox.setText("");
        passwordEditBox.setText("");
    }
    
    private boolean isFieldEmpty() {
        return emailEditBox == null || 
               emailEditBox.getText().isEmpty() ||
               passwordEditBox == null ||
               passwordEditBox.getPassword().length == 0;
    }
    
    private void traderJoinButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_traderJoinButtonActionPerformed
        if(!isFieldEmpty()) enterMarket();
        else JOptionPane.showMessageDialog(this, "Email and password cannot be empty!");    
    }//GEN-LAST:event_traderJoinButtonActionPerformed

    private void emailEditBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emailEditBoxKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if(!isFieldEmpty()) enterMarket(); 
            else JOptionPane.showMessageDialog(this, "Email and password cannot be empty!");    
        } 
    }//GEN-LAST:event_emailEditBoxKeyPressed

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        emailEditBox.requestFocusInWindow();
    }//GEN-LAST:event_formFocusGained

    private void emailEditBoxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_emailEditBoxFocusGained
        emailEditBox.selectAll();
    }//GEN-LAST:event_emailEditBoxFocusGained

    private void passwordEditBoxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordEditBoxFocusGained
        passwordEditBox.selectAll();
    }//GEN-LAST:event_passwordEditBoxFocusGained

    private void passwordEditBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordEditBoxKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if(!isFieldEmpty()) enterMarket(); 
            else JOptionPane.showMessageDialog(this, "Email and password cannot be empty!");
        }
    }//GEN-LAST:event_passwordEditBoxKeyPressed

    private void signUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signUpButtonActionPerformed
        SignUpGUI signUp = new SignUpGUI(traderFacade, this);
        Utils.setLocation(signUp);
        signUp.setVisible(true);
        this.setVisible(false);
        cleanFields();
    }//GEN-LAST:event_signUpButtonActionPerformed

    // Main
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                JoinMarketGUI mainWindow = new JoinMarketGUI(); 
                Utils.setLocation(mainWindow);
                mainWindow.setVisible(true);
            }
        });
           
    } 
 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField emailEditBox;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JPasswordField passwordEditBox;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JButton signUpButton;
    private javax.swing.JButton traderJoinButton;
    // End of variables declaration//GEN-END:variables
    
}
