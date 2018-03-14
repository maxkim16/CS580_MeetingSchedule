/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MeetingSchedule;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author maxkim
 */
public class EmpCheckPrevInvitation extends javax.swing.JFrame {

    DefaultTableModel model; // used for the table to display schedule info
    String username; // received from the EmpMain jtable
    
    /**
     * Creates new form EmpCheckPrevInvitation
     */
    public EmpCheckPrevInvitation() {
        initComponents();
    }
    
        // overloaded-constructor will be executed 
    public EmpCheckPrevInvitation(String username) {
        this.username = username; // save the username received from the EmpMain jFrame
        initComponents();
        myInitComponents();
                
        // Display the new invitations the user has not decided to attend or not
        showInvitationsInTable(); 
    }

    // This method will display all the invitations that are either accepted or declined
    private void showInvitationsInTable() {

        int meetingId;

        // initilize the table
        model = (DefaultTableModel) jTableInvitations.getModel();

        // connect to the database
        DBconnector db = new DBconnector();

        // connect to database 
        Connection connection = db.connectToDB();

        // Retrieve rows rows from the `assignments` table
        String query = getInviteQuery();

        Statement st;
        ResultSet rs;

        int i = 0;
        Object[] row = new Object[8];
        try {
            st = connection.createStatement();
            // execute the given SQL statement and get the result
            rs = st.executeQuery(query);

            // Loops until the last row of the rows retrrieved is reached
            while (rs.next()) {
                // retrives the value of the designated column in the current row of this Result Set Object
                row[0] = rs.getString("id");
                row[1] = rs.getString("meetingID");
                row[2] = rs.getString("topic");
                row[3] = rs.getString("invitorName");
                row[4] = rs.getString("date");
                row[5] = rs.getString("startTime");
                row[6] = rs.getString("endTime");
                row[7] = rs.getString("acceptance");
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setChecked() {
        String query = getCheckedQuery();
        executeSQLQuery(query, "query");
    }

    public String getCheckedQuery() {
        String query;
        query = "UPDATE `assignments` "
                + "SET `acceptance` = 'undecided' "
                + "WHERE `inviteeID` = '" + username + "';";
        return query;
    }

    // This method returns the new invitations the user have either accepted or declined
    public String getInviteQuery() {
        String query;
        // Get the user's invitations and important information associated with the invitations, such as
        // the date, time, invitor, etc.
        query = "SELECT a.id, m.id as meetingID, m.topic, e.name AS invitorName, m.date, m.startTime, m.endTime, a.acceptance "
                + "FROM assignments a "
                + "INNER JOIN meetings m ON a.meetingID = m.id "
                + "INNER JOIN employees e ON m.ownerID = e.username "
                + "WHERE a.inviteeID = '" + username + "' "
                + "AND (a.acceptance = 'accepted' OR a.acceptance = 'declined');";  // only undecided invitations will be shown
        return query;
    }

    // This method returns the query that query that will decline the invitation
    public String getDeclineQuery(String id) {
        String query;
        query = "UPDATE `assignments` "
                + "SET `acceptance` = 'declined' "
                + "WHERE `id` = " + id + ";";
        return query;
    }

    // This method returns the query that will delete the user's schedule in assignment table
    public String getDeleteAssignmentQuery(String meetingID) {
        String query;

        query = "DELETE es "
                + "FROM empSchedule AS es "
                + "JOIN meetings as m ON es.date = m.date "
                + "AND es.startTime = m.startTime "
                + "AND es.endTime = m.endTime "
                + "AND es.task = m.topic "
                + "WHERE m.id = " + meetingID + ";";

        return query;
    }

    // Execute The SQL Query
    public void executeSQLQuery(String query, String message) {
        DBconnector db = new DBconnector();
        Connection con = db.connectToDB();
        Statement st;
        try {
            st = con.createStatement();
            // execute the query
            if ((st.executeUpdate(query)) == 1) {
                // refresh jtable data so the new data created is displayed as well
                model = (DefaultTableModel) jTableInvitations.getModel();
                model.setRowCount(0);
                showInvitationsInTable();

                // Display the message
                JOptionPane.showMessageDialog(null, "Data " + message + " Successfully");
            } else {
                JOptionPane.showMessageDialog(null, "Data " + message + " not Successfully");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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

        jButtonDecline1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableInvitations = new javax.swing.JTable();
        jLabelInvitation = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButtonDecline1.setBackground(new java.awt.Color(255, 153, 153));
        jButtonDecline1.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButtonDecline1.setText("Previous Invitations");
        jButtonDecline1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDecline1ActionPerformed(evt);
            }
        });

        jTableInvitations.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "meetingID", "topic", "Invitor", "date", "startTime", "endTime", "acceptance"
            }
        ));
        jTableInvitations.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableInvitationsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableInvitations);

        jLabelInvitation.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        jLabelInvitation.setText("Previous Invitations");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 755, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonDecline1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(348, 348, 348)
                        .addComponent(jLabelInvitation, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(201, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabelInvitation, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jButtonDecline1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonDecline1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDecline1ActionPerformed

    }//GEN-LAST:event_jButtonDecline1ActionPerformed

    private void jTableInvitationsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableInvitationsMouseClicked

    }//GEN-LAST:event_jTableInvitationsMouseClicked

    // This method prvents from terminating an application when a JFrame is closed
    private void myInitComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EmpCheckPrevInvitation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmpCheckPrevInvitation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmpCheckPrevInvitation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmpCheckPrevInvitation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmpCheckPrevInvitation().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDecline1;
    private javax.swing.JLabel jLabelInvitation;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableInvitations;
    // End of variables declaration//GEN-END:variables
}
