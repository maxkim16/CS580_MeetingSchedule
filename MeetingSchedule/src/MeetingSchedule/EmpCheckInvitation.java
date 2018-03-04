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
import javax.swing.table.TableModel;

/**
 *
 * @author maxkim
 */
public class EmpCheckInvitation extends javax.swing.JFrame {

    DefaultTableModel model; // used for the table to display schedule info
    String username; // received from the EmpMain jtable
    
    /**
     * Creates new form EmpCheckInvitation
     */
    public EmpCheckInvitation() {
        initComponents();
    }
    
    // overloaded-constructor will be executed 
    public EmpCheckInvitation(String username) {
        this.username = username; // save the username received from the EmpMain jFrame
        initComponents();
        myInitComponents();
        showInvitationsInTable(); // show every invitation the user received
    }

    // This method will display all the invitations the user has in the table
    private void showInvitationsInTable() {
        
        int meetingId;
        
        // initilize the table
        model = (DefaultTableModel)jTableInvitations.getModel();
        
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
    
    // This method returns the query that will retrieve the rows (invitations the user received)
    public String getInviteQuery() {
          String query;
          // Get the user's invitations and important information associated with the invitations, such as
          // the date, time, invitor, etc.
          query = "SELECT a.id, m.id as meetingID, m.topic, e.name AS invitorName, m.date, m.startTime, m.endTime, a.acceptance "
                  + "FROM assignments a "
                  + "INNER JOIN meetings m ON a.meetingID = m.id "
                  + "INNER JOIN employees e ON m.ownerID = e.username "
                  + "WHERE a.inviteeID = '" +  username + "';";
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
    public void executeSQLQuery(String query, String message)
    {
        DBconnector db = new DBconnector();
        Connection con = db.connectToDB();
        Statement st;
        try{
            st = con.createStatement();
            // execute the query
            if((st.executeUpdate(query)) == 1)
            {
                // refresh jtable data so the new data created is displayed as well
                model = (DefaultTableModel)jTableInvitations.getModel();
                model.setRowCount(0);
                showInvitationsInTable();
                
                // Display the message
                JOptionPane.showMessageDialog(null, "Data " + message + " Successfully");
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Data " + message + " not Not Successfully");   
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    // This method prvents from terminating an application when a JFrame is closed
    private void myInitComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
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
        jTableInvitations = new javax.swing.JTable();
        jButtonAccept = new javax.swing.JButton();
        jButtonDecline = new javax.swing.JButton();
        jLabelInvitation = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        jButtonAccept.setBackground(new java.awt.Color(204, 255, 204));
        jButtonAccept.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButtonAccept.setText("Accept");
        jButtonAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAcceptActionPerformed(evt);
            }
        });

        jButtonDecline.setBackground(new java.awt.Color(255, 153, 153));
        jButtonDecline.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButtonDecline.setText("Decline");
        jButtonDecline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeclineActionPerformed(evt);
            }
        });

        jLabelInvitation.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        jLabelInvitation.setText("Invitation");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 755, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(69, 69, 69)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonAccept, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonDecline, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(348, 348, 348)
                        .addComponent(jLabelInvitation, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addComponent(jButtonAccept, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jButtonDecline, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabelInvitation, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void jTableInvitationsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableInvitationsMouseClicked
         

          
    }//GEN-LAST:event_jTableInvitationsMouseClicked

    // Make an invitation a new schedule once the user accepts the invitation
    private void jButtonAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAcceptActionPerformed
        
        String query, username2,date, startTime, endTime, meetingID, task, visibility;
        
        // get the number of the row the user clicked on in the table
        int i = jTableInvitations.getSelectedRow();
        model = (DefaultTableModel) jTableInvitations.getModel();
        
        // get the date, startTime, endTime of the row the user clicked on
        meetingID = model.getValueAt(i, 1).toString();
        date = model.getValueAt(i, 4).toString();
        startTime = model.getValueAt(i, 5).toString();
        endTime = model.getValueAt(i, 6).toString();
        
        
        // surround each string with single quotes for the SQL query 
        date = "'" + date + "'";
        startTime = "'" + startTime + "'";
        endTime = "'" + endTime + "'";
        username2 = "'" + username + "'";
                
        // insert the meeting into the assignment table so the user can see it in his calendar
        query = "INSERT INTO empSchedule (username, date, startTime, endTime, task, visibility) "
                + "VALUES ( " + username2 + ", " + date + ", " + startTime + ", " + endTime + ", "
                + "(SELECT m.topic "
                + "FROM meetings m "
                + "WHERE m.id = " + meetingID + "), 'visible');";
        
        // execute the query
        executeSQLQuery(query, "Inserted in to the calendar");
        
        // change assignment table's acceptance field to 'accepted
        query = "UPDATE assignments "
                + "SET acceptance = 'accepted' "
                + "WHERE meetingID = " + meetingID + " "
                + "AND inviteeID = " + username2 + ";";
        
        // execute the query
        executeSQLQuery(query, "");
        
    }//GEN-LAST:event_jButtonAcceptActionPerformed

    /*
    The user will decline the invitation.
    The system will first change the `accepted` attribute to `declined` and delete the
    assignment in the `empSchedule` relation
     */
    private void jButtonDeclineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeclineActionPerformed
        String query, assignmentID, meetingID;
        // get the number of the row the user clicked on in the table
        int i = jTableInvitations.getSelectedRow();
        model = (DefaultTableModel) jTableInvitations.getModel();
        // get the id of the row the user clicked on
        assignmentID = model.getValueAt(i, 0).toString();
        meetingID = model.getValueAt(i,1).toString();
        
        query = getDeclineQuery(assignmentID);
        // Decline the invitation
        executeSQLQuery(query, "Invitation Declined");
        
        query = getDeleteAssignmentQuery(meetingID);
        // Delete the assignment
                        JOptionPane.showMessageDialog(null, query);   
        executeSQLQuery(query, "Employee Schedule deleted");
    }//GEN-LAST:event_jButtonDeclineActionPerformed

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
            java.util.logging.Logger.getLogger(EmpCheckInvitation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmpCheckInvitation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmpCheckInvitation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmpCheckInvitation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmpCheckInvitation().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAccept;
    private javax.swing.JButton jButtonDecline;
    private javax.swing.JLabel jLabelInvitation;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableInvitations;
    // End of variables declaration//GEN-END:variables
}
