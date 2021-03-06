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
        
        // set the value of the `unchecked` attribute to 'undecided'
        setChecked();
        
        // Display the new invitations the user has not decided to attend or not
        showInvitationsInTable(); 
    }

    // This method will display all the invitations the user has in the table
    private void showInvitationsInTable() {
        
        int meetingId;
        
        // initilize the table
        model = (DefaultTableModel)jTableInvitations.getModel();
        model.setRowCount(0);  // refresh the table
        
        // connect to the database
        DBconnector db = new DBconnector();

        // connect to database 
        Connection connection = db.connectToDB();
        
        // Retrieve rows rows from the `assignments` table
        String query = getInviteQuery();

        Statement st;
        ResultSet rs;

        int i = 0;
        Object[] row = new Object[9];
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
                row[8] = rs.getString("room");
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
                + "WHERE `inviteeID` = '" + username + "' "
                + "AND `acceptance` = 'unchecked';";
        return query;
    }
    
    // This method returns the new invitations the use has not decided to attend or not
    public String getInviteQuery() {
          String query;
          // Get the user's invitations and important information associated with the invitations, such as
          // the date, time, invitor, etc.
          query = "SELECT a.id, m.id as meetingID, m.topic, e.name AS invitorName, m.date, m.startTime, m.endTime, a.acceptance, m.room "
                  + "FROM assignments a "
                  + "INNER JOIN meetings m ON a.meetingID = m.id "
                  + "INNER JOIN employees e ON m.ownerID = e.username "
                  + "WHERE a.inviteeID = '" +  username + "' "
                  + "AND a.acceptance = 'undecided';";  // only undecided invitations will be shown
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
        // Refresh the table
        //model = (DefaultTableModel)jTableInvitations.getModel();
        //model.setRowCount(0);
        try{
            st = con.createStatement();
            // execute the query
            if((st.executeUpdate(query)) == 1)
            {
                // refresh jtable data so the new data created is displayed as well
                //model = (DefaultTableModel)jTableInvitations.getModel();
                //model.setRowCount(0);
                //showInvitationsInTable();
                
                // Display the message
                //JOptionPane.showMessageDialog(null, "Data " + message + " Successfully");
            }
            else
            {
                //JOptionPane.showMessageDialog(null, "Data " + message + " not Successfully");   
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
        jButtonDecline1 = new javax.swing.JButton();
        jButtonDecline2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTableInvitations.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "meetingID", "topic", "Invitor", "date", "startTime", "endTime", "acceptance", "room"
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
        jLabelInvitation.setText("New Invitations");

        jButtonDecline1.setBackground(new java.awt.Color(255, 153, 153));
        jButtonDecline1.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButtonDecline1.setText("Previous Invitations");
        jButtonDecline1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDecline1ActionPerformed(evt);
            }
        });

        jButtonDecline2.setBackground(new java.awt.Color(255, 153, 153));
        jButtonDecline2.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButtonDecline2.setText("Current Invitations");
        jButtonDecline2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDecline2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(348, 348, 348)
                        .addComponent(jLabelInvitation, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonDecline2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDecline1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(470, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonAccept, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDecline, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(135, 135, 135)
                .addComponent(jButtonAccept, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jButtonDecline, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabelInvitation, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonDecline2)
                    .addComponent(jButtonDecline1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void jTableInvitationsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableInvitationsMouseClicked
         

          
    }//GEN-LAST:event_jTableInvitationsMouseClicked

    private String getSchConflictQuery(String date, String st, String et) {
                String query = "SELECT * "
                + "FROM empSchedule "
                + "WHERE date = '" + date + "' "
                        + "AND username = '" + username + "' "
                + "AND (startTime <= '" + st + "' AND '" + st + "' <= endTime "
                        + "OR '" + st + "' <= startTime AND startTime <= '" + et + "');";
                return query;
    }
    
    // run the query and see if the result sets return any table
    private Boolean DoesConflictExist(String date, String st, String et) { 
        
        // get the query that displays all the schedule conflicts
        String queryScheduleConflicts = getSchConflictQuery(date, st, et);
                        
        // connect to the database
        DBconnector db = new DBconnector();
        Connection connection = db.connectToDB();
        Statement statement;
        ResultSet rs;

        try {
            statement = connection.createStatement();
            // execute the given SQL statement and get the result
            rs = statement.executeQuery(queryScheduleConflicts);

            // Loops until the last row of the rows retrrieved is reached
            while (rs.next()) {
                // if anything is returned, that means there is a conflict
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // if no data is returned, there is no conflict
        return false;
    }
    
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
        
        // Check if there is a conflict
        // If there is a conflict, exit the function.
        if ( (DoesConflictExist(date, startTime, endTime)) == true ) {
            JOptionPane.showMessageDialog(null, "There is a time conflict. Please check your schedule.");   
            return; 
        }
        
        // surround each string with single quotes for the SQL query 
        date = "'" + date + "'";
        startTime = "'" + startTime + "'";
        endTime = "'" + endTime + "'";
        username2 = "'" + username + "'";
                
        // insert the meeting into the `empSchedule` table so the user can see it in his calendar
        query = "INSERT INTO empSchedule (username, date, startTime, endTime, task, visibility) "
                + "VALUES ( " + username2 + ", " + date + ", " + startTime + ", " + endTime + ", "
                + "(SELECT m.topic "
                + "FROM meetings m "
                + "WHERE m.id = " + meetingID + "), 'public');";
        
        // execute the query
        executeSQLQuery(query, "");
        
        // change assignment table's acceptance field to 'accepted'
        query = "UPDATE assignments "
                + "SET acceptance = 'accepted' "
                + "WHERE meetingID = " + meetingID + " "
                + "AND inviteeID = " + username2 + ";";
        
        // execute the query
        executeSQLQuery(query, "");
        
        // show the updated table
        showInvitationsInTable();
        
        // display a message
        JOptionPane.showMessageDialog(null, "New Meeting Is Accepted");  
        
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
        //JOptionPane.showMessageDialog(null, query);   
        executeSQLQuery(query, "Employee Schedule deleted");

        // show the updated table
        showInvitationsInTable();

        // Show a message to let the user know the invitation is declined succesfully
        JOptionPane.showMessageDialog(null, "New Meeting Is Declined");
    }//GEN-LAST:event_jButtonDeclineActionPerformed

    // This method opens up the PreviousInvitations jFrame
    private void jButtonDecline1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDecline1ActionPerformed
        EmpCheckPrevInvitation prevInv = new EmpCheckPrevInvitation(username);
        prevInv.setVisible(true);
    }//GEN-LAST:event_jButtonDecline1ActionPerformed

    private void jButtonDecline2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDecline2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonDecline2ActionPerformed

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
    private javax.swing.JButton jButtonDecline1;
    private javax.swing.JButton jButtonDecline2;
    private javax.swing.JLabel jLabelInvitation;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableInvitations;
    // End of variables declaration//GEN-END:variables
}
