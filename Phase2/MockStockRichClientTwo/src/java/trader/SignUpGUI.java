package trader;

import javax.swing.JOptionPane;
import mockstock.db.Trader;
import mockstock.db.enumeration.UserRight;
import mockstock.db.enumeration.UserStatus;
import mockstock.ejb.front.TraderFacadeRemote;
import util.Utils;

/**
 *
 * @author felmas
 */
public class SignUpGUI extends javax.swing.JFrame {

    private TraderFacadeRemote traderBean;
    private JoinMarketGUI parent;
    /**
     * Creates new form SignUpGUI
     */
    public SignUpGUI(TraderFacadeRemote traderBean, JoinMarketGUI parent) {
        this.traderBean = traderBean;
        this.parent = parent;
        
        initComponents();
        this.nameEditBox.requestFocusInWindow();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scroll = new javax.swing.JScrollPane();
        rootPane = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        surnameLabel = new javax.swing.JLabel();
        emailLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        repasswordLabel = new javax.swing.JLabel();
        nameEditBox = new javax.swing.JTextField();
        surnameEditBox = new javax.swing.JTextField();
        emailEditBox = new javax.swing.JTextField();
        passwordEditBox = new javax.swing.JPasswordField();
        repasswordEditBox = new javax.swing.JPasswordField();
        signUpButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        guiMainLabel = new javax.swing.JLabel();
        infoLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(669, 332));
        setResizable(false);

        scroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.setRequestFocusEnabled(false);

        nameLabel.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        nameLabel.setText("Name");

        surnameLabel.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        surnameLabel.setText("Surname");

        emailLabel.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        emailLabel.setText("Email*");

        passwordLabel.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        passwordLabel.setText("Password*");

        repasswordLabel.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        repasswordLabel.setText("Repassword*");

        nameEditBox.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        nameEditBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nameEditBoxFocusGained(evt);
            }
        });

        surnameEditBox.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        surnameEditBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                surnameEditBoxFocusGained(evt);
            }
        });

        emailEditBox.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        emailEditBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                emailEditBoxFocusGained(evt);
            }
        });

        passwordEditBox.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        passwordEditBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordEditBoxFocusGained(evt);
            }
        });

        repasswordEditBox.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        repasswordEditBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                repasswordEditBoxFocusGained(evt);
            }
        });

        signUpButton.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        signUpButton.setText("Sign Up");
        signUpButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                signUpButtonMouseClicked(evt);
            }
        });

        cancelButton.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        cancelButton.setText("Cancel");
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelButtonMouseClicked(evt);
            }
        });

        guiMainLabel.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        guiMainLabel.setText("Sign Up For MockStock Market");

        infoLabel.setText("* Required");

        javax.swing.GroupLayout rootPaneLayout = new javax.swing.GroupLayout(rootPane);
        rootPane.setLayout(rootPaneLayout);
        rootPaneLayout.setHorizontalGroup(
            rootPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rootPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rootPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rootPaneLayout.createSequentialGroup()
                        .addComponent(guiMainLabel)
                        .addGap(0, 520, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rootPaneLayout.createSequentialGroup()
                        .addGroup(rootPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, rootPaneLayout.createSequentialGroup()
                                .addGroup(rootPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(passwordLabel)
                                    .addComponent(repasswordLabel)
                                    .addComponent(emailLabel)
                                    .addComponent(surnameLabel)
                                    .addComponent(nameLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(rootPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(nameEditBox)
                                    .addComponent(surnameEditBox)
                                    .addComponent(emailEditBox)
                                    .addComponent(passwordEditBox)
                                    .addComponent(repasswordEditBox)))
                            .addGroup(rootPaneLayout.createSequentialGroup()
                                .addComponent(infoLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(signUpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(162, 162, 162)))
                .addContainerGap())
        );
        rootPaneLayout.setVerticalGroup(
            rootPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rootPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(guiMainLabel)
                .addGap(12, 12, 12)
                .addGroup(rootPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameEditBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(rootPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(surnameLabel)
                    .addComponent(surnameEditBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(rootPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailLabel)
                    .addComponent(emailEditBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(rootPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordEditBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(rootPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(repasswordLabel)
                    .addComponent(repasswordEditBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(rootPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rootPaneLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(rootPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cancelButton)
                            .addComponent(signUpButton)))
                    .addGroup(rootPaneLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(infoLabel)))
                .addContainerGap(200, Short.MAX_VALUE))
        );

        scroll.setViewportView(rootPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 674, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nameEditBoxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameEditBoxFocusGained
        nameEditBox.selectAll();
    }//GEN-LAST:event_nameEditBoxFocusGained

    private void surnameEditBoxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_surnameEditBoxFocusGained
        surnameEditBox.selectAll();
    }//GEN-LAST:event_surnameEditBoxFocusGained

    private void emailEditBoxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_emailEditBoxFocusGained
        emailEditBox.selectAll();
    }//GEN-LAST:event_emailEditBoxFocusGained

    private void passwordEditBoxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordEditBoxFocusGained
        passwordEditBox.selectAll();
    }//GEN-LAST:event_passwordEditBoxFocusGained

    private void repasswordEditBoxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_repasswordEditBoxFocusGained
        repasswordEditBox.selectAll();
    }//GEN-LAST:event_repasswordEditBoxFocusGained

    private void signUpButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_signUpButtonMouseClicked
        String email = emailEditBox.getText();
        String password = new String(passwordEditBox.getPassword());
        String repassword = new String(repasswordEditBox.getPassword());
        
        if(Utils.isEmail(email)) {
            if(!traderBean.verifyEmailRemote(email)) {
                if(password != null && !password.isEmpty() && password.length() >= 6) {
                    if(password.equals(repassword)) {

                        Trader trader = new Trader();
                        trader.setEmail(email);
                        trader.setPassword(Utils.encryptPassword(password));
                        trader.setFirstname(nameEditBox.getText());
                        trader.setLastname(surnameEditBox.getText());

                        trader.setStatus(UserStatus.ACTIVATED);
                        trader.setUsrgroup(UserRight.USER);

                        traderBean.addTraderRemote(trader);
                        trader = traderBean.checkLoginRemote(email, Utils.encryptPassword(password));
                        JOptionPane.showMessageDialog(this, "You successfully signed up\nWelcome to MockStock Market!");
                        
                        UserGUI userGUI = new UserGUI(trader, parent);
                        Utils.setLocation(userGUI);
                        userGUI.setVisible(true);
                        this.dispose();
                    
                    } else {
                        JOptionPane.showMessageDialog(this, "Passwords don't match\nPlease be sure that you enter correctly!");
                        if(!passwordEditBox.requestFocusInWindow()) passwordEditBox.selectAll();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a valid password\nPassword cannot be less than 6 characters!");
                    if(!passwordEditBox.requestFocusInWindow()) passwordEditBox.selectAll();
                }
            } else {
                JOptionPane.showMessageDialog(this, "This email is already in use!");
                if(!emailEditBox.requestFocusInWindow()) emailEditBox.selectAll();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a valid email!");
            if(!emailEditBox.requestFocusInWindow()) emailEditBox.selectAll();
        }
    }//GEN-LAST:event_signUpButtonMouseClicked

    private void cancelButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelButtonMouseClicked
        this.parent.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_cancelButtonMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField emailEditBox;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JLabel guiMainLabel;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JTextField nameEditBox;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JPasswordField passwordEditBox;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JPasswordField repasswordEditBox;
    private javax.swing.JLabel repasswordLabel;
    private javax.swing.JPanel rootPane;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JButton signUpButton;
    private javax.swing.JTextField surnameEditBox;
    private javax.swing.JLabel surnameLabel;
    // End of variables declaration//GEN-END:variables
}
