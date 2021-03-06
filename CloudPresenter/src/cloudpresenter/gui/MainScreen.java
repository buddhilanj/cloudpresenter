/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.gui;

import cloudpresenter.controller.DeviceManager;
import cloudpresenter.controller.GroupManager;
import cloudpresenter.controller.ImageStreamManager;
import cloudpresenter.gui.panels.DeviceShareSelect;
import cloudpresenter.gui.panels.GroupShareSelect;
import cloudpresenter.imagestream.ImageStream;
import cloudpresenter.imagestream.StreamImagesOnScreen;
import cloudpresenter.model.Device;
import cloudpresenter.model.Group;
import cloudpresenter.network.RecieveUDPMulticastPacket;
import cloudpresenter.network.RecieveUDPPacket;
import cloudpresenter.servers.ChatReciever;
import cloudpresenter.servers.FileRecieving;
import java.awt.Component;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author User
 */



public class MainScreen extends javax.swing.JFrame {
public static Device thisDevice;
public static RecieveUDPMulticastPacket rUDPms;
    /**
     * Creates new form MainScreen
     */
    public MainScreen() {
        initComponents();
        try {
            InetAddress local = InetAddress.getLocalHost();
            thisDevice = new Device(local.getHostAddress(), local.getHostName(), "device");
        } catch (UnknownHostException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        pnlChoice = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstGroups = new javax.swing.JList();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList2MouseClicked(evt);
            }
        });
        jList2.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList2ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jList2);

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout pnlChoiceLayout = new javax.swing.GroupLayout(pnlChoice);
        pnlChoice.setLayout(pnlChoiceLayout);
        pnlChoiceLayout.setHorizontalGroup(
            pnlChoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );
        pnlChoiceLayout.setVerticalGroup(
            pnlChoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 111, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnlChoice, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setText("Create New Group");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        lstGroups.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstGroups.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstGroupsValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(lstGroups);

        jList3.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(jList3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 181, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addComponent(jButton1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4))
                .addGap(129, 129, 129))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        new RecieveUDPPacket().start();
        rUDPms = new RecieveUDPMulticastPacket();
        rUDPms.start();
        new ChatReciever().start();
        new FileRecieving().start();
        Thread statcheck = new Thread(new Runnable() {

            public void run() {
                try {
                    while (true) {
                        updateDecives();
                        updateIncoming();
                        updateGroups();
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        statcheck.start();
    }//GEN-LAST:event_formWindowOpened

    private void jList2ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList2ValueChanged
    }//GEN-LAST:event_jList2ValueChanged

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        if (!jList1.isSelectionEmpty()) {
            Device device = DeviceManager.getDevice(jList1.getSelectedValue().toString());
//            JOptionPane.showMessageDialog(rootPane, device.getDeviceName());
//            ScreenSharingServer server = new ScreenSharingServer(device.getIpAddress());
//            server.start();

            Component[] comp = pnlChoice.getComponents();
            for (int i = 0; i < comp.length; i++) {
                pnlChoice.remove(comp[i]);
            }
            DeviceShareSelect jPanel3= new DeviceShareSelect(device);
            javax.swing.GroupLayout pnlChoiceLayout = new javax.swing.GroupLayout(pnlChoice);
            pnlChoice.setLayout(pnlChoiceLayout);
            pnlChoiceLayout.setHorizontalGroup(
                    pnlChoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
            pnlChoiceLayout.setVerticalGroup(
                    pnlChoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        }
    }//GEN-LAST:event_jList1ValueChanged

    private void jList2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList2MouseClicked
        if (!jList2.isSelectionEmpty()) {
            String selected = jList2.getSelectedValue().toString();
            try {
                ImageStream is = ImageStreamManager.getStreamByIP(InetAddress.getByName(selected));
                StreamImagesOnScreen sios = is.openDisplay();
                sios.showScreen();
                //new ViewImageUI(is).setVisible(true);
                //new ViewImageUI().setVisible(true);
            } catch (UnknownHostException ex) {
                Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jList2MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new CreateGroup().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void lstGroupsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstGroupsValueChanged
        if (!lstGroups.isSelectionEmpty()) {
            Group group = GroupManager.getGroup(lstGroups.getSelectedValue().toString());
//            JOptionPane.showMessageDialog(rootPane, device.getDeviceName());
//            ScreenSharingServer server = new ScreenSharingServer(device.getIpAddress());
//            server.start();

            Component[] comp = pnlChoice.getComponents();
            for (int i = 0; i < comp.length; i++) {
                pnlChoice.remove(comp[i]);
            }
            GroupShareSelect jPanel4= new GroupShareSelect(group);
            javax.swing.GroupLayout pnlChoiceLayout = new javax.swing.GroupLayout(pnlChoice);
            pnlChoice.setLayout(pnlChoiceLayout);
            pnlChoiceLayout.setHorizontalGroup(
                    pnlChoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
            pnlChoiceLayout.setVerticalGroup(
                    pnlChoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        }
    }//GEN-LAST:event_lstGroupsValueChanged

    public void updateDecives() {
        Vector<String> v = new Vector<String>();
        for (Device device : DeviceManager.getList()) {
            v.add(device.getDeviceName());
        }
        jList1.setModel(new DefaultComboBoxModel(v));
    }

    public void updateIncoming() {
        Vector<String> v = new Vector<String>();
        for (ImageStream stream : ImageStreamManager.getList()) {
            v.add(stream.getHostIP());
        }
        jList2.setModel(new DefaultComboBoxModel(v));
    }

    public void updateGroups() {
        Vector<String> v = new Vector<String>();
        for (Group group : GroupManager.getList()) {
            v.add(group.getGroupID());
        }
        lstGroups.setModel(new DefaultComboBoxModel(v));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainScreen().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JList jList3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JList lstGroups;
    private javax.swing.JPanel pnlChoice;
    // End of variables declaration//GEN-END:variables
}
