/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MeetingSchedule;

import java.awt.Dimension;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author maxkim
 */
public class EmpCalendarNote extends javax.swing.JFrame {

    private String username; //username received from the EmpMain jFrame
    private DefaultTableModel model; // used for the table to display schedule info
    
    /**
     * Creates new form EmpCalendarNote
     */
    public EmpCalendarNote() {
        initComponents();
        
    }
    
    // Overloaded constructor to get the value of username from the EmpMain jFrame
    public EmpCalendarNote(String username) {
        this.username = username;
        initComponents();
        // prevent from terminating the application when a jFrame is closed
        myInitComponents(); 

        // initilize the date to the current date so the table will display the schedule
        // of employee's today's schedule 
        Date dateToday = new Date();
        jDateChooser1.setDate(dateToday);
        Show_EmpSch_In_JTable2();
        
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

    // Display Data in JTable.
    // Unlike Show_EmpSch_In_JTable(), this method directly accesses the database
    // and display the retrieved rows in the table instead of making a list of objects
    // that represent the rows retrieved.
    public void Show_EmpSch_In_JTable2() {

        //DefaultTableModel model = (DefaultTableModel) jTableEmpSch.getModel();
        model = (DefaultTableModel) jTableEmpSch.getModel();
        model.setRowCount(0);
        
        String dateSelected = getDateFromCal();

        DBconnector db = new DBconnector();

        // connect to database 
        Connection connection = db.connectToDB();
        // Retrieve rows that is associated with the user and the date selected from the calendar
        String query = "SELECT * FROM `empSchedule` WHERE `username` = '" + username + "' AND `date` = "
                + "'" + dateSelected + "';";
        Statement st;
        ResultSet rs;

        int i = 0;
        Object[] row = new Object[6];
        try {
            st = connection.createStatement();
            // execute the given SQL statement and get the result
            rs = st.executeQuery(query);

            // Loops until the last row from the rows retrrieved is reached
            while (rs.next()) {
                // retrives the value of the designated column in the current row of this Result Set Object
                row[0] = rs.getString("id");
                row[1] = rs.getString("date");
                row[2] = rs.getString("startTime");
                row[3] = rs.getString("endTime");
                row[4] = rs.getString("task");
                row[5] = rs.getString("visibility");
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Execute The SQL Query and refresh the table
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
                //model.setRowCount(0);
                //Show_EmpSch_In_JTable2();
                
                // Display the message
                JOptionPane.showMessageDialog(null, message);
            }
            else
            {
                JOptionPane.showMessageDialog(null, message + " failed.");   
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableEmpSch = new javax.swing.JTable();
        jPanelAddDeleteEdit = new javax.swing.JPanel();
        jLabelID = new javax.swing.JLabel();
        jLabelDate = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabelTask = new javax.swing.JLabel();
        jLabelVisibility = new javax.swing.JLabel();
        jTextFieldID = new javax.swing.JTextField();
        jTextFieldDate = new javax.swing.JTextField();
        jTextFieldStart = new javax.swing.JTextField();
        jTextFieldEnd = new javax.swing.JTextField();
        jTextFieldTask = new javax.swing.JTextField();
        jTextFieldVisibility = new javax.swing.JTextField();
        jButtonInsert = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonEdit = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jButtonRefresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTableEmpSch.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "date", "startTime", "endTime", "task", "visibility"
            }
        ));
        jTableEmpSch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableEmpSchMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableEmpSch);

        jLabelID.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabelID.setText("ID:");

        jLabelDate.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabelDate.setText("Date:");

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel3.setText("StartTime:");

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel4.setText("EndTime:");

        jLabelTask.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabelTask.setText("Task:");

        jLabelVisibility.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabelVisibility.setText("Visibility");

        jTextFieldDate.setText("YYYY-MM-DD");

        jTextFieldStart.setText("HH:MM:SS");
        jTextFieldStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldStartActionPerformed(evt);
            }
        });

        jTextFieldEnd.setText("HH:MM:SS");
        jTextFieldEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldEndActionPerformed(evt);
            }
        });

        jButtonInsert.setBackground(new java.awt.Color(204, 255, 204));
        jButtonInsert.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButtonInsert.setText("Insert");
        jButtonInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInsertActionPerformed(evt);
            }
        });

        jButtonDelete.setBackground(new java.awt.Color(255, 102, 102));
        jButtonDelete.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButtonDelete.setText("Delete");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jButtonEdit.setBackground(new java.awt.Color(255, 204, 153));
        jButtonEdit.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButtonEdit.setText("Edit");
        jButtonEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelAddDeleteEditLayout = new javax.swing.GroupLayout(jPanelAddDeleteEdit);
        jPanelAddDeleteEdit.setLayout(jPanelAddDeleteEditLayout);
        jPanelAddDeleteEditLayout.setHorizontalGroup(
            jPanelAddDeleteEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAddDeleteEditLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelAddDeleteEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelAddDeleteEditLayout.createSequentialGroup()
                        .addGroup(jPanelAddDeleteEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabelID)
                            .addComponent(jLabelDate))
                        .addGroup(jPanelAddDeleteEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelAddDeleteEditLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanelAddDeleteEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jTextFieldDate, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldID, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelAddDeleteEditLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldStart, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanelAddDeleteEditLayout.createSequentialGroup()
                        .addGroup(jPanelAddDeleteEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabelTask)
                            .addComponent(jLabelVisibility))
                        .addGap(24, 24, 24)
                        .addGroup(jPanelAddDeleteEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelAddDeleteEditLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButtonInsert)
                                .addGap(9, 9, 9))
                            .addGroup(jPanelAddDeleteEditLayout.createSequentialGroup()
                                .addGroup(jPanelAddDeleteEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextFieldVisibility, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                                    .addComponent(jTextFieldTask, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanelAddDeleteEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButtonEdit)
                                    .addComponent(jButtonDelete)))
                            .addGroup(jPanelAddDeleteEditLayout.createSequentialGroup()
                                .addComponent(jTextFieldEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanelAddDeleteEditLayout.setVerticalGroup(
            jPanelAddDeleteEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAddDeleteEditLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanelAddDeleteEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelID)
                    .addComponent(jTextFieldID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelAddDeleteEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDate)
                    .addComponent(jTextFieldDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonInsert))
                .addGap(18, 18, 18)
                .addGroup(jPanelAddDeleteEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanelAddDeleteEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldStart, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonEdit)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelAddDeleteEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanelAddDeleteEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelAddDeleteEditLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelAddDeleteEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelTask)
                            .addComponent(jTextFieldTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelAddDeleteEditLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jButtonDelete)))
                .addGap(18, 18, 18)
                .addGroup(jPanelAddDeleteEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldVisibility, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelVisibility))
                .addContainerGap(105, Short.MAX_VALUE))
        );

        jDateChooser1.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jDateChooser1InputMethodTextChanged(evt);
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
        });

        jButtonRefresh.setBackground(new java.awt.Color(204, 255, 255));
        jButtonRefresh.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButtonRefresh.setText("Refresh Schedule");
        jButtonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(111, 111, 111)
                                .addComponent(jButtonRefresh)))
                        .addGap(0, 78, Short.MAX_VALUE))
                    .addComponent(jPanelAddDeleteEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 742, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonRefresh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 123, Short.MAX_VALUE)
                        .addComponent(jPanelAddDeleteEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(25, 25, 25))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // This method edits a schedule
    private void jButtonEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditActionPerformed
        String id = jTextFieldID.getText();
        String date = jTextFieldDate.getText();
        String startTime = jTextFieldStart.getText();
        String endTime = jTextFieldEnd.getText();
        String task = jTextFieldTask.getText();
        String visibility = jTextFieldVisibility.getText();

        String query = "UPDATE `empSchedule` SET `date` = '" + date + "', `startTime` = '" + startTime + "', "
                + "`endTime` = '" + endTime + "', `task` = '" + task + "', `visibility` = "
                + "'" + visibility + "' WHERE `id` = " + id + ";";
        executeSQLQuery(query, "Edited");
        Show_EmpSch_In_JTable2(); // refreshed table will be shown
    }//GEN-LAST:event_jButtonEditActionPerformed

    // This method returns 'true' if there is a time conflict with the user's schedule
    private Boolean DoesConflictExist(String date, String st, String et) { 
        
        // get the query that displays all the meeting conflicts
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
    
    // This method inserts a new schedule 
    private void jButtonInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInsertActionPerformed
        String date = jTextFieldDate.getText();
        String startTime = jTextFieldStart.getText();
        String endTime = jTextFieldEnd.getText();
        String task = jTextFieldTask.getText();
        String visibility = jTextFieldVisibility.getText();

        // checek if there is a schedule time conflict
        // If there is a conflict, exit the function.
        if ((DoesConflictExist(date, startTime, endTime)) == true) {
            JOptionPane.showMessageDialog(null, "There is a time conflict. Please check your schedule.");
            return;
        }

        String query = "INSERT INTO `empSchedule`(`username`, `date`, `startTime`, `endTime`"
                + ", `task`, `visibility`) VALUES ( '" + username + "', '" + date + "', '"
                + startTime + "', '" + endTime + "', '" + task + "', '" + visibility + "');";
        executeSQLQuery(query, "Inserted");
        Show_EmpSch_In_JTable2(); // refreshed table will be shown

    }//GEN-LAST:event_jButtonInsertActionPerformed

    private String getSchConflictQuery(String date, String st, String et) {
        String query = "SELECT * "
                + "FROM empSchedule "
                + "WHERE date = '" + date + "' "
                + "AND username = '" + username + "' "
                + "AND (startTime <= '" + st + "' AND '" + st + "' <= endTime "
                + "OR '" + st + "' <= startTime AND startTime <= '" + et + "');";
        return query;
    }
    
    private void jTextFieldStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldStartActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldStartActionPerformed

    // This method is not used
    private void jDateChooser1InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jDateChooser1InputMethodTextChanged
        //Show_EmpSch_In_JTable(username);
    }//GEN-LAST:event_jDateChooser1InputMethodTextChanged

    // Every time the user selects a new date, this method will refresh the table
    // and display the schedule of the date the user selected
    private void jButtonRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshActionPerformed
      
        // Refresh the schedule everytime user selects a new date
        model.setRowCount(0);
        Show_EmpSch_In_JTable2();
    }//GEN-LAST:event_jButtonRefreshActionPerformed

    private void jTextFieldEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldEndActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldEndActionPerformed

    // This method deletes a schedule
    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        String id = jTextFieldID.getText();

        String query = "DELETE FROM `empSchedule` WHERE `id` = " + id + ";";
        executeSQLQuery(query, "Deleted");
        Show_EmpSch_In_JTable2(); // refreshed table will be shown

    }//GEN-LAST:event_jButtonDeleteActionPerformed

    // fill in the text fields when a row in the table is clicked
    private void jTableEmpSchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableEmpSchMouseClicked
            // Display Selected Row in JTextFields
        int i = jTableEmpSch.getSelectedRow();
        model = (DefaultTableModel) jTableEmpSch.getModel();
        // TxtFdID.setText(model.getValueAt(i, 0).toString());
        jTextFieldID.setText(model.getValueAt(i, 0).toString());
        jTextFieldDate.setText(model.getValueAt(i, 1).toString());
        jTextFieldStart.setText(model.getValueAt(i, 2).toString());
        jTextFieldEnd.setText(model.getValueAt(i, 3).toString());
        jTextFieldTask.setText(model.getValueAt(i, 4).toString());
        jTextFieldVisibility.setText(model.getValueAt(i, 5).toString());
    }//GEN-LAST:event_jTableEmpSchMouseClicked

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
            java.util.logging.Logger.getLogger(EmpCalendarNote.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmpCalendarNote.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmpCalendarNote.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmpCalendarNote.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmpCalendarNote().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonEdit;
    private javax.swing.JButton jButtonInsert;
    private javax.swing.JButton jButtonRefresh;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelDate;
    private javax.swing.JLabel jLabelID;
    private javax.swing.JLabel jLabelTask;
    private javax.swing.JLabel jLabelVisibility;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelAddDeleteEdit;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableEmpSch;
    private javax.swing.JTextField jTextFieldDate;
    private javax.swing.JTextField jTextFieldEnd;
    private javax.swing.JTextField jTextFieldID;
    private javax.swing.JTextField jTextFieldStart;
    private javax.swing.JTextField jTextFieldTask;
    private javax.swing.JTextField jTextFieldVisibility;
    // End of variables declaration//GEN-END:variables
}
