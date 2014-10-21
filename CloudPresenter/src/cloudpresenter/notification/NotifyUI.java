/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.notification;

import javax.swing.JFrame;
import javax.swing.JRootPane;
import net.sf.jcarrierpigeon.Notification;
import net.sf.jcarrierpigeon.NotificationQueue;
import net.sf.jcarrierpigeon.WindowPosition;


/**
 *
 * @author Hiyann
 */
public class NotifyUI extends javax.swing.JWindow{
    JFrame globalInterface;
    String globalType;
    
    public NotifyUI() {
        initComponents();
        setAlwaysOnTop(true);
        setSize(321, 100);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        
        Notification note = new Notification(this, WindowPosition.BOTTOMRIGHT, 25, 25, 5000);
        NotificationQueue queue = new NotificationQueue();
        queue.add(note);
    }
    
    public NotifyUI(String hedding, String discription,JFrame myInterface ,String type) {
        initComponents();
        setAlwaysOnTop(true);
        setSize(321, 100);
        globalInterface=myInterface;
        globalType=type;
        txtHeding.setText(hedding);
        txtDiscription.setText(discription);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        
        Notification note = new Notification(this, WindowPosition.BOTTOMRIGHT, 25, 25, 5000);
        NotificationQueue queue = new NotificationQueue();
        queue.add(note);
    }
    
    public void redirect(){

        globalInterface.setVisible(true);
        globalInterface.toFront();
//        if(globalType.equals("AdminMenuUI")){
//            AdminMenuUI myAdminMenuUI=(AdminMenuUI) globalInterface;
//            myAdminMenuUI.gotoBackup();
//            this.dispose();
//        }
//
//        if(globalType.equals("ManagementAccountantMenuUI")){
//            ManagementAccountantMenuUI myManagementAccountantMenuUI=(ManagementAccountantMenuUI) globalInterface;
//            myManagementAccountantMenuUI.gotoCompletedDeliveryUI();
//            this.dispose();
//        }
//
//        if(globalType.equals("TenderEvaluationPanelMenuUI")){
//            TenderEvaluationPanelMenuUI myTenderEvaluationPanelMenuUI=(TenderEvaluationPanelMenuUI) globalInterface;
//            myTenderEvaluationPanelMenuUI.gotoEvaluateBids();
//            this.dispose();
//        }
    }
    
    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {                                     
        redirect();
    }                                    

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {                                     
        redirect();
    }                                    

    private void txtDiscriptionMouseClicked(java.awt.event.MouseEvent evt) {                                            
        redirect();
    }                                           

    private void txtHedingMouseClicked(java.awt.event.MouseEvent evt) {                                       
        redirect();
    }
    
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtHeding = new javax.swing.JLabel();
        txtDiscription = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        //setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(211, 218, 224));
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });

        txtHeding.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtHeding.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtHedingMouseClicked(evt);
            }
        });

        txtDiscription.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDiscriptionMouseClicked(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudpresenter/notification/notification.png"))); // NOI18N
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDiscription, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                    .addComponent(txtHeding, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addComponent(txtHeding, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtDiscription, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }
    
    public static void main(String args[]) {
        
        
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new NotifyUI().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify                     
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel txtDiscription;
    private javax.swing.JLabel txtHeding;
    // End of variables declaration    
}
