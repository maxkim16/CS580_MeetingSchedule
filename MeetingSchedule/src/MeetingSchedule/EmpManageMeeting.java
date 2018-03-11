/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MeetingSchedule;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author maxkim
 */
public class EmpManageMeeting extends javax.swing.JFrame {
    String username;
    DefaultTableModel modelMeetings, modelInvitees, modelRooms; 

    /**
     * Creates new form EmpManageMeeting
     */
    public EmpManageMeeting() {
        initComponents();
    }

    // overloaded-constructor will be executed 
    public EmpManageMeeting(String username) {
        this.username = username; // save the username received from the EmpMain jFrame
        initComponents();
        myInitComponents();

        // show every invitation the user received
        showMeetingsInTable();
    }
    
        
    public String getAvaRoomQuery(int size, String date, String st, String et) {
        
        String query = " SELECT id AS roomNumber "
                + "FROM rooms "
                + "WHERE rooms.size >= " + size + " "
                + "AND NOT EXISTS "
                + "(SELECT * "
                + "FROM meetings "
                + "WHERE rooms.id = meetings.room "
                + "AND meetings.date = '" + date + "' "
                + "AND meetings.startTime = '" + st + "' "
                + "AND meetings.endTime = '" + et + "' );";
        return query;
    }
    
    public String getInviteeQuery(String meetingID) {
        String query = "SELECT name "
                + "FROM employees INNER JOIN assignments "
                + "ON assignments.inviteeID = employees.username "
                + "INNER JOIN meetings "
                + "ON meetings.ID = assignments.meetingID " 
                + "WHERE meetings.ID = " + meetingID + ";";
        return query;
    }
    
    // This method returns the query that will retrieve the rows (invitations the user received)
    public String getMeetingsQuery() {
        String query;
        // Get the user's invitations and important information associated with the invitations, such as
        // the date, time, invitor, etc.
        query = "SELECT * "
                + "FROM meetings "
                + "WHERE ownerID = '" + username + "';";
        return query;
    }
    
    // This method returns the real names of invitees, not the usernames
    public String[] getConvertedNames(String query) {
        String[] names = null;
        return names;
    }

    // This method will display all the invitations the user has in the table
    private void showInviteesInTable(String query) {
                
        // initilize the table
        modelInvitees = (DefaultTableModel)jTableInvitees.getModel();
        
        // connect to the database
        DBconnector db = new DBconnector();

        // connect to database 
        Connection connection = db.connectToDB();
               
        Statement st;
        ResultSet rs;

        int i = 0;
        Object[] row = new Object[1];
        try {
            st = connection.createStatement();
            // execute the given SQL statement and get the result
            rs = st.executeQuery(query);

            // Loops until the last row of the rows retrrieved is reached
            while (rs.next()) {
                // retrives the value of the designated column in the current row of this Result Set Object
                row[0] = rs.getString("name");
                modelInvitees.addRow(row);
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    // This method will display all the avaialble rooms
    private void showAvaRoomsInTable(String query) {
                
        // initilize the table
        modelRooms = (DefaultTableModel)jTableRooms.getModel();
        
        // connect to the database
        DBconnector db = new DBconnector();

        // connect to database 
        Connection connection = db.connectToDB();
               
        Statement st;
        ResultSet rs;

        int i = 0;
        Object[] row = new Object[1];
        try {
            st = connection.createStatement();
            // execute the given SQL statement and get the result
            rs = st.executeQuery(query);

            // Loops until the last row of the rows retrrieved is reached
            while (rs.next()) {
                // retrives the value of the designated column in the current row of this Result Set Object
                row[0] = rs.getString("roomNumber");
                modelRooms.addRow(row);
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // This method will display all the invitations the user has in the table
    private void showMeetingsInTable() {
        
        int meetingId;
        
        // initilize the table
        modelMeetings = (DefaultTableModel)jTableMeetings.getModel();
        
        // connect to the database
        DBconnector db = new DBconnector();

        // connect to database 
        Connection connection = db.connectToDB();
        
        // Retrieve rows rows from the `assignments` table
        String query = getMeetingsQuery();

        Statement st;
        ResultSet rs;

        int i = 0;
        Object[] row = new Object[6];
        try {
            st = connection.createStatement();
            // execute the given SQL statement and get the result
            rs = st.executeQuery(query);

            // Loops until the last row of the rows retrrieved is reached
            while (rs.next()) {
                // retrives the value of the designated column in the current row of this Result Set Object
                row[0] = rs.getString("id");
                row[1] = rs.getString("room");
                row[2] = rs.getString("date");
                row[3] = rs.getString("startTime");
                row[4] = rs.getString("endTime");
                row[5] = rs.getString("topic");
                modelMeetings.addRow(row);
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
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
        jTableMeetings = new javax.swing.JTable();
        jLabelMyMeeting = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableInvitees = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableRooms = new javax.swing.JTable();
        jTextFieldRoom = new javax.swing.JTextField();
        jTextFieldIInv = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButtonDisplayInv = new javax.swing.JButton();
        jButtonDeleteInv = new javax.swing.JButton();
        jButtonDisplayRooms = new javax.swing.JButton();
        jButtonDeleteRoom = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldIID = new javax.swing.JTextField();
        jButtonChangeRoom1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTableMeetings.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "room", "date", "st", "et", "topic"
            }
        ));
        jTableMeetings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMeetingsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableMeetings);

        jLabelMyMeeting.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabelMyMeeting.setText("My Meetings");

        jTableInvitees.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Invitees"
            }
        ));
        jTableInvitees.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableInviteesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableInvitees);

        jTableRooms.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Rooms"
            }
        ));
        jTableRooms.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableRoomsMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableRooms);

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel4.setText("Room:");

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel5.setText("Invitee");

        jButtonDisplayInv.setBackground(new java.awt.Color(204, 255, 204));
        jButtonDisplayInv.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButtonDisplayInv.setText("Display Invitees");
        jButtonDisplayInv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDisplayInvActionPerformed(evt);
            }
        });

        jButtonDeleteInv.setBackground(new java.awt.Color(255, 153, 153));
        jButtonDeleteInv.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButtonDeleteInv.setText("Delete");
        jButtonDeleteInv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteInvActionPerformed(evt);
            }
        });

        jButtonDisplayRooms.setBackground(new java.awt.Color(204, 255, 204));
        jButtonDisplayRooms.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButtonDisplayRooms.setText("Display Available Rooms");
        jButtonDisplayRooms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDisplayRoomsActionPerformed(evt);
            }
        });

        jButtonDeleteRoom.setBackground(new java.awt.Color(255, 153, 153));
        jButtonDeleteRoom.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButtonDeleteRoom.setText("Delete Room");
        jButtonDeleteRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteRoomActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel6.setText("ID:");

        jButtonChangeRoom1.setBackground(new java.awt.Color(204, 255, 204));
        jButtonChangeRoom1.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButtonChangeRoom1.setText("Edit");
        jButtonChangeRoom1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChangeRoom1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelMyMeeting)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jButtonDeleteRoom)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonDisplayInv)
                                .addGap(30, 30, 30)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextFieldIInv, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addComponent(jLabel6)
                                            .addGap(18, 18, 18)
                                            .addComponent(jTextFieldIID, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel4)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jTextFieldRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButtonDeleteInv)
                                    .addComponent(jButtonChangeRoom1)))
                            .addComponent(jButtonDisplayRooms))))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabelMyMeeting)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(54, 54, 54)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(jTextFieldIID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(jTextFieldRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButtonChangeRoom1))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextFieldIInv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5)
                                .addComponent(jButtonDeleteInv))
                            .addGap(197, 197, 197))
                        .addGroup(layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonDisplayInv)
                    .addComponent(jButtonDisplayRooms)
                    .addComponent(jButtonDeleteRoom))
                .addContainerGap(144, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTableMeetingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMeetingsMouseClicked

    }//GEN-LAST:event_jTableMeetingsMouseClicked

    private void jTableInviteesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableInviteesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableInviteesMouseClicked

    private void jTableRoomsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableRoomsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableRoomsMouseClicked

    // This method displays all the invitees who are invited to the meeting in the table
    private void jButtonDisplayInvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDisplayInvActionPerformed
        int selectedRow;
        String meetingID, query;
        String names[];
        modelMeetings = (DefaultTableModel) jTableMeetings.getModel();
        selectedRow = jTableMeetings.getSelectedRow();
        meetingID = modelMeetings.getValueAt(selectedRow, 0).toString();
        
        // get all the invitees
        query = getInviteeQuery(meetingID);
        
        // show the invitees in the table
        showInviteesInTable(query);
        
        
    }//GEN-LAST:event_jButtonDisplayInvActionPerformed

    private void jButtonDeleteInvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteInvActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonDeleteInvActionPerformed

    // Display the available rooms in the table
    private void jButtonDisplayRoomsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDisplayRoomsActionPerformed
        int numOfInvitees, selectedRow;
        String date, st, et, query;
        numOfInvitees = jTableInvitees.getRowCount();
        modelMeetings = (DefaultTableModel) jTableMeetings.getModel();
        selectedRow = jTableMeetings.getSelectedRow();
        date = modelMeetings.getValueAt(selectedRow, 2).toString();
        st = modelMeetings.getValueAt(selectedRow, 3).toString();
        et = modelMeetings.getValueAt(selectedRow, 4).toString();
        
        query = getAvaRoomQuery(numOfInvitees, date, st, et);

        showAvaRoomsInTable(query);
    }//GEN-LAST:event_jButtonDisplayRoomsActionPerformed

    private void jButtonDeleteRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteRoomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonDeleteRoomActionPerformed

    private void jButtonChangeRoom1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChangeRoom1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonChangeRoom1ActionPerformed

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
            java.util.logging.Logger.getLogger(EmpManageMeeting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmpManageMeeting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmpManageMeeting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmpManageMeeting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmpManageMeeting().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonChangeRoom1;
    private javax.swing.JButton jButtonDeleteInv;
    private javax.swing.JButton jButtonDeleteRoom;
    private javax.swing.JButton jButtonDisplayInv;
    private javax.swing.JButton jButtonDisplayRooms;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelMyMeeting;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTableInvitees;
    private javax.swing.JTable jTableMeetings;
    private javax.swing.JTable jTableRooms;
    private javax.swing.JTextField jTextFieldIID;
    private javax.swing.JTextField jTextFieldIInv;
    private javax.swing.JTextField jTextFieldRoom;
    // End of variables declaration//GEN-END:variables




}
