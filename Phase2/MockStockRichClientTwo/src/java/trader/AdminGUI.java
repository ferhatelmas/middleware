package trader;


import java.util.Vector;
import mockstock.db.Trader;
import mockstock.ejb.front.AdminRemote;
import mockstock.ejb.front.TraderFacadeRemote;
import util.Utils;

/**
 *
 * @author felmas
 */
/* Main admin window 
 * Show users that can be edit
 * User can add new user
 */
public class AdminGUI extends javax.swing.JFrame {

    private JoinMarketGUI parent;
    
    private Trader admin;
    
    private AdminRemote adminBean;
    private TraderFacadeRemote traderFacade;
    /**
     * Creates new form AdnibGUI
     */
    public AdminGUI(TraderFacadeRemote traderFacade, Trader admin, JoinMarketGUI parent) {
        this.admin = admin;
        this.parent = parent;
        
        this.adminBean = (AdminRemote) Utils.getBean(Utils.BEAN.ADMIN);
        this.traderFacade = traderFacade;
        
        initComponents();
        getAllTraders();
        this.setTitle("Admin Main View");
    }
    
    public AdminGUI(AdminRemote adminBean, TraderFacadeRemote traderFacade, Trader admin, JoinMarketGUI parent) {
        this.admin = admin;
        this.parent = parent;
        
        this.adminBean = adminBean;
        this.traderFacade = traderFacade;
        
        initComponents();
        getAllTraders();
    }
    
    private void getAllTraders() {
        Vector<Trader> traders = new Vector<Trader>();
        traders.addAll(adminBean.getTraderListRemote());
        this.traderList.setListData(traders);
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
        traderList = new javax.swing.JList();
        exitButton = new javax.swing.JButton();
        addUserButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        traderList.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        traderList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        traderList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                traderListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(traderList);

        exitButton.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        addUserButton.setFont(new java.awt.Font("DejaVu Sans", 0, 16)); // NOI18N
        addUserButton.setText("Add User");
        addUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUserButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 223, Short.MAX_VALUE)
                .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addUserButton)
                    .addComponent(exitButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        this.parent.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_exitButtonActionPerformed

    private void addUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUserButtonActionPerformed
        UserAddGUI userAddGUI = new UserAddGUI(adminBean, traderFacade, this.admin, this.parent);
        Utils.setLocation(userAddGUI);
        userAddGUI.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_addUserButtonActionPerformed

    private void traderListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_traderListMouseClicked
        if(evt.getClickCount() == 2) {
            Trader trader = (Trader) traderList.getModel().getElementAt(traderList.getSelectedIndex());
            UserDataGUI userDataGUI = new UserDataGUI(adminBean, traderFacade, this.admin, this.parent, trader);
            Utils.setLocation(userDataGUI);
            userDataGUI.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_traderListMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        System.exit(0);
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addUserButton;
    private javax.swing.JButton exitButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList traderList;
    // End of variables declaration//GEN-END:variables
}