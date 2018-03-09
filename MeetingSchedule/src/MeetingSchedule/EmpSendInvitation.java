/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MeetingSchedule;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author maxkim
 */
public class EmpSendInvitation extends javax.swing.JFrame {

    // username is received from the EmpMain jFrame
    String username;
    // used for the table to display schedule info
    DefaultTableModel empTableModel, inviTableModel, timeTableModel,
            roomTableModel;

    /**
     * Creates new form EmpSendInvitation
     */
    public EmpSendInvitation() {
        initComponents();
    }

    // overloaded-constructor will be executed 
    public EmpSendInvitation(String username) {
        this.username = username; // save the username received from the EmpMain jFrame
        initComponents();
        myInitComponents();

        // Display every employee in the table
        displayEmps();
    }

    private void displayEmps() {

        int meetingId;

        // initilize the table
        empTableModel = (DefaultTableModel) jTableEmp.getModel();

        // connect to the database
        DBconnector db = new DBconnector();

        // connect to database 
        Connection connection = db.connectToDB();

        // Retrieve rows rows from the `assignments` table
        String query = getEmpsQuery();

        Statement st;
        ResultSet rs;

        Object[] row = new Object[1];
        try {
            st = connection.createStatement();
            // execute the given SQL statement and get the result
            rs = st.executeQuery(query);

            // Loops until the last row of the rows retrrieved is reached
            while (rs.next()) {
                // retrives the value of the designated column in the current row of this Result Set Object
                row[0] = rs.getString("name");
                empTableModel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // This method returns a query that returns a set of the name of all the employees except for the user himself
    private String getEmpsQuery() {
        String query = "SELECT name "
                + "FROM employees "
                + "WHERE name != '" + username + "';";
        return query;
    }

    // This method prvents from terminating an application when a JFrame is closed
    private void myInitComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    // Execute The SQL Query and refresh the table
    public void executeSQLQuery(String query, String message) {
        DBconnector db = new DBconnector();
        Connection con = db.connectToDB();
        Statement st;
        try {
            st = con.createStatement();
            // execute the query
            if ((st.executeUpdate(query)) == 1) {
                // refresh jtable data so the new data created is displayed as well
                //model.setRowCount(0);
                //Show_EmpSch_In_JTable2();

                // Display the message
                // JOptionPane.showMessageDialog(null, message);
            } else {
                //JOptionPane.showMessageDialog(null, message + " failed.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getGrpSchQuery(String name, String date) {
        String query = "INSERT INTO groupSchedule (date, startTime, endTime) "
                + "SELECT es.date, es.startTime, es.endTime "
                + "FROM empSchedule AS es "
                + "WHERE es.date = '" + date + "'";
        return query;
    }

    // This method returns a date which is selected from the user in the calendar
    public String getDateFromCal() {
        // Change the date format so it's easier to query date in database
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
        // get date from the calendar
        // if no date is selected, the current date will be selected
        String date = dFormat.format(jDateChooser1.getDate());
        return date;
    }

    private String getDeleteGrpSchQry() {
        String query = "DELETE "
                + "FROM groupSchedule;";
        return query;
    }
    
    // This method saves the schedules, which includes the meetings as well, of the selected employees in the `groupSchedule` table
    private void saveInGroupSch(String[] names) {
        String query, date;
        // Delete all the previous data in the `groupSchedule` table
        query = getDeleteGrpSchQry();
        executeSQLQuery(query, "previous data deleted in `groupSchedule`");
        
        // save the new data
        
        date = getDateFromCal();
        for (int i = 0; i < names.length; i++) {
            query = getGrpSchQuery(names[i], date);
            executeSQLQuery(query, "schedule inserted into groupSchedule");
        }
    }

    // This method returns a query for selecting all the avilable time slots using the `allHours` and `groupSchedule` table
    private String getAvaTimeSlotsQry() {
        String query = "SELECT date, startTime, endTime "
                + "FROM allHours AS ah "
                + "WHERE NOT EXISTS "
                + "(SELECT * "
                + "FROM groupSchedule AS gs "
                + "WHERE ah.date = gs.date AND "
                + "(ah.startTime <= gs.startTime AND "
                + "gs.startTime <= ah.endTime OR "
                + "gs.startTime <= ah.startTime AND "
                + "ah.startTime <= gs.endTime));";
        return query;
    }

    private void ShowAvaTimeSlots() {

        //DefaultTableModel model = (DefaultTableModel) jTableEmpSch.getModel();
        timeTableModel = (DefaultTableModel) jTableTime.getModel();
        
        String dateSelected = getDateFromCal();

        DBconnector db = new DBconnector();

        // connect to database 
        Connection connection = db.connectToDB();
        String query = getAvaTimeSlotsQry();
        Statement st;
        ResultSet rs;

        int i = 0;
        Object[] row = new Object[2];
        try {
            st = connection.createStatement();
            // execute the given SQL statement and get the result
            rs = st.executeQuery(query);

            // Loops until the last row from the rows retrrieved is reached
            while (rs.next()) {
                // retrives the value of the designated column in the current row of this Result Set Object
                row[0] = rs.getString("startTime");
                row[1] = rs.getString("endTime");
                timeTableModel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void deleteSchAndHrs() {
        String query = "DELETE "
                + "FROM groupSchedule;";
        executeSQLQuery(query, "");
                query = "DELETE "
                + "FROM allHours;";
        executeSQLQuery(query, "");
    }
    
    private void makeAllHours() {
        String query, date;
        date = getDateFromCal();
        
        // Delete the previous data saved in the table
        query = "DELETE "
                + "FROM allHours;";
        executeSQLQuery(query, "allHours records deleted");
                
        
        // insert new hours with the selected date
        for (int i = 0; i < 24; i++) {
            query = "INSERT INTO allHours (date, startTime, endTime) "
                 + "VALUES ('" + date + "', '" + i + ":00:00', '" + (i+1) + ":00:00');";
            executeSQLQuery(query, "allHours records created");
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

        jLabelEmpList = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableTime = new javax.swing.JTable();
        jLabelInvList = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableEmp = new javax.swing.JTable();
        jButtonAdd = new javax.swing.JButton();
        jButtonRemove = new javax.swing.JButton();
        jButtonAvaTime = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableRoom = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableInv = new javax.swing.JTable();
        jLabelAvailRoom = new javax.swing.JLabel();
        jLabelAvailTime = new javax.swing.JLabel();
        jButtonSendInvi = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabelEmpList.setBackground(new java.awt.Color(204, 255, 204));
        jLabelEmpList.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLabelEmpList.setText("Employee List");

        jTableTime.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "startTime", "endTime"
            }
        ));
        jScrollPane1.setViewportView(jTableTime);

        jLabelInvList.setBackground(new java.awt.Color(204, 255, 204));
        jLabelInvList.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLabelInvList.setText("Invitee List");

        jTableEmp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name"
            }
        ));
        jScrollPane2.setViewportView(jTableEmp);

        jButtonAdd.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButtonAdd.setText("---->");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jButtonRemove.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButtonRemove.setText("<----");
        jButtonRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveActionPerformed(evt);
            }
        });

        jButtonAvaTime.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButtonAvaTime.setText("Available Time Slots");
        jButtonAvaTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAvaTimeActionPerformed(evt);
            }
        });

        jTableRoom.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Room"
            }
        ));
        jScrollPane3.setViewportView(jTableRoom);

        jTableInv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name"
            }
        ));
        jScrollPane4.setViewportView(jTableInv);

        jLabelAvailRoom.setBackground(new java.awt.Color(204, 255, 204));
        jLabelAvailRoom.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLabelAvailRoom.setText("Available Rooms");

        jLabelAvailTime.setBackground(new java.awt.Color(204, 255, 204));
        jLabelAvailTime.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLabelAvailTime.setText("Available Time Slots");

        jButtonSendInvi.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButtonSendInvi.setText("Send Invitation");
        jButtonSendInvi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSendInviActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelInvList)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(99, 99, 99)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelAvailTime)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelAvailRoom))
                        .addGap(64, 64, 64))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonSendInvi, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(142, 142, 142))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(253, 253, 253)
                        .addComponent(jButtonAvaTime, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabelEmpList)))
                .addContainerGap(544, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(846, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelEmpList, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelInvList, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(79, 79, 79)
                                .addComponent(jButtonAdd)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonRemove))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabelAvailTime, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabelAvailRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonSendInvi, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonAvaTime, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(404, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(70, 70, 70)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(502, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // This method moves the selected user in the Employee List into the Invitee List
    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        Object[] selectedUser = new Object[1];
        // get the number of the row the user clicked on in the table
        int i = jTableEmp.getSelectedRow();
        empTableModel = (DefaultTableModel) jTableEmp.getModel();
        inviTableModel = (DefaultTableModel) jTableInv.getModel();

        // Move the selected user from Employee List into Invitee List
        selectedUser[0] = empTableModel.getValueAt(i, 0).toString();
        inviTableModel.addRow(selectedUser);
        empTableModel.removeRow(i);
    }//GEN-LAST:event_jButtonAddActionPerformed

    // This method moves the selected user in the Invitee List into the Employee List
    private void jButtonRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveActionPerformed
        Object[] selectedUser = new Object[1];
        // get the number of the row the user clicked on in the table
        int i = jTableInv.getSelectedRow();
        empTableModel = (DefaultTableModel) jTableEmp.getModel();
        inviTableModel = (DefaultTableModel) jTableInv.getModel();

        // Move the selected user from Employee List into Invitee List
        selectedUser[0] = inviTableModel.getValueAt(i, 0).toString();
        empTableModel.addRow(selectedUser);
        inviTableModel.removeRow(i);
    }//GEN-LAST:event_jButtonRemoveActionPerformed

    // This method finds available time slots for the group of the selected employees
    private void jButtonAvaTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAvaTimeActionPerformed
        
        // get the names of the selected employees
        int invTabSize = inviTableModel.getRowCount();

        String[] selectedNames = new String[invTabSize + 1];
        selectedNames[1] = username; // the owner of the meeting is automatically included

        for (int i = 1; i < invTabSize + 1; i++) {
            selectedNames[i] = inviTableModel.getValueAt(i-1, 0).toString();
        }
        
        // make the `allHours` table
        makeAllHours();
        
        // save the schedules, including the meetings, of the selected employees in the groupSchedule table
        saveInGroupSch(selectedNames);

        // Display the available time slots in the table
        ShowAvaTimeSlots();

        // Delete all the records in the `groupSchedule` nad the `allHours` tables
        deleteSchAndHrs();
    }//GEN-LAST:event_jButtonAvaTimeActionPerformed

    private void jButtonSendInviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSendInviActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonSendInviActionPerformed

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
            java.util.logging.Logger.getLogger(EmpSendInvitation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmpSendInvitation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmpSendInvitation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmpSendInvitation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmpSendInvitation().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonAvaTime;
    private javax.swing.JButton jButtonRemove;
    private javax.swing.JButton jButtonSendInvi;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabelAvailRoom;
    private javax.swing.JLabel jLabelAvailTime;
    private javax.swing.JLabel jLabelEmpList;
    private javax.swing.JLabel jLabelInvList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTableEmp;
    private javax.swing.JTable jTableInv;
    private javax.swing.JTable jTableRoom;
    private javax.swing.JTable jTableTime;
    // End of variables declaration//GEN-END:variables
}
