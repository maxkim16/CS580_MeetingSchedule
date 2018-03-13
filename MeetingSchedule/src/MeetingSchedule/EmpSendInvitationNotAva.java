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
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author maxkim
 */
public class EmpSendInvitationNotAva extends javax.swing.JFrame {
    // username is received from the EmpMain jFrame
    String username;
    // used for the table to display schedule info
    DefaultTableModel empTableModel, inviTableModel, timeTableModel,
            roomTableModel, meetingConModel;
    
    /**
     * Creates new form EmpSendInvitationNotAva
     */
    public EmpSendInvitationNotAva() {
        initComponents();
    }

    // overloaded-constructor will be executed 
    public EmpSendInvitationNotAva(String username) {
        this.username = username; // save the username received from the EmpMain jFrame
        initComponents();
        myInitComponents();

        // Display every employee in the table
        displayEmps();
    }
    
    // Query for getting all the available rooms
    public String getAvaRoomQuery(String date, String st, String et, int size) {

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

    // Query for getting all the meeting conflicts
    private String getConflictQuery(String date, String st, String et) {
        String query = "SELECT e.name, m.topic AS meeting, a.acceptance AS status "
                + "FROM meetings AS m "
                + "INNER JOIN assignments AS a "
                + "ON m.id = a.meetingID "
                + "INNER JOIN employees AS e "
                + "ON a.inviteeID = e.username "
                + "WHERE m.date = '" + date + "' "
                + "AND (m.startTime <= '" + st + "'  AND '" + st + "' <= m.endTime "
                + "OR '" + st + "' <= m.startTime AND m.startTime <= '" + et + "');";
        return query;
    }
    
    // This method returns a query that returns a set of the name of all the employees except for the user himself
    private String getEmpsQuery() {
        String query = "SELECT name "
                + "FROM employees "
                + "WHERE name != '" + username + "';";
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
                
                st.close();
                con.close();
            } else {
                //JOptionPane.showMessageDialog(null, message + " failed.");
                st.close();
                con.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    

    private void displayMeetingConflicts(String query) {


        // initilize the table
        meetingConModel = (DefaultTableModel) jTableMeetingCon.getModel();
        
        // refresh the table
        meetingConModel.setRowCount(0);
        
        // connect to the database
        DBconnector db = new DBconnector();

        // connect to database 
        Connection connection = db.connectToDB();


        Statement st;
        ResultSet rs;

        Object[] row = new Object[3];
        try {
            st = connection.createStatement();
            // execute the given SQL statement and get the result
            rs = st.executeQuery(query);

            // Loops until the last row of the rows retrrieved is reached
            while (rs.next()) {
                // retrives the value of the designated column in the current row of this Result Set Object
                row[0] = rs.getString("name");
                row[1] = rs.getString("meeting");
                row[2] = rs.getString("status");
                meetingConModel.addRow(row);
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
    
    
    // show all the rooms that are available
    private void showAvaRoom(String query) {
        roomTableModel = (DefaultTableModel) jTableRoom.getModel();

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

            // Loops until the last row from the rows retrrieved is reached
            while (rs.next()) {
                // retrives the value of the designated column in the current row of this Result Set Object
                row[0] = rs.getString("roomNumber");
                roomTableModel.addRow(row);
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
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

        jLabelEmpList = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableEmp = new javax.swing.JTable();
        jButtonAdd = new javax.swing.JButton();
        jButtonRemove = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableInv = new javax.swing.JTable();
        jLabelInvList = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableRoom = new javax.swing.JTable();
        jButtonAvaRooms = new javax.swing.JButton();
        jLabelSt = new javax.swing.JLabel();
        jLabelEt = new javax.swing.JLabel();
        jTextFieldSt = new javax.swing.JTextField();
        jTextFieldEt = new javax.swing.JTextField();
        jLabelTopic = new javax.swing.JLabel();
        jTextFieldTopic = new javax.swing.JTextField();
        jButtonSendInvi = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableMeetingCon = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabelEmpList.setBackground(new java.awt.Color(204, 255, 204));
        jLabelEmpList.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLabelEmpList.setText("Employee List");

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

        jTableInv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name"
            }
        ));
        jScrollPane4.setViewportView(jTableInv);

        jLabelInvList.setBackground(new java.awt.Color(204, 255, 204));
        jLabelInvList.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLabelInvList.setText("Invitee List");

        jTableRoom.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Room"
            }
        ));
        jScrollPane3.setViewportView(jTableRoom);

        jButtonAvaRooms.setBackground(new java.awt.Color(255, 204, 204));
        jButtonAvaRooms.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButtonAvaRooms.setText("Find Available Rooms");
        jButtonAvaRooms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAvaRoomsActionPerformed(evt);
            }
        });

        jLabelSt.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabelSt.setText("StartTime:");

        jLabelEt.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabelEt.setText("EndTime:");

        jTextFieldSt.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextFieldSt.setText("00:00:00");
        jTextFieldSt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldStActionPerformed(evt);
            }
        });

        jTextFieldEt.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextFieldEt.setText("00:00:00");

        jLabelTopic.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabelTopic.setText("Topic:");

        jTextFieldTopic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldTopicActionPerformed(evt);
            }
        });

        jButtonSendInvi.setBackground(new java.awt.Color(153, 255, 153));
        jButtonSendInvi.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButtonSendInvi.setText("Send Invitation");
        jButtonSendInvi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSendInviActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "schedule", "public/private"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTableMeetingCon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "name", "meeting", "status"
            }
        ));
        jScrollPane5.setViewportView(jTableMeetingCon);

        jButton1.setBackground(new java.awt.Color(255, 255, 204));
        jButton1.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButton1.setText("Check Schedule Conflicts");

        jButton2.setBackground(new java.awt.Color(255, 255, 204));
        jButton2.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButton2.setText("Check Meeting Conflicts");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                            .addComponent(jLabelEmpList))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonAvaRooms, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(23, 23, 23)
                                        .addComponent(jLabelInvList))
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(465, 465, 465))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelEt, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jTextFieldSt, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jLabelSt, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jTextFieldEt, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelTopic)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldTopic, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonSendInvi, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabelEmpList, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelInvList, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(jButtonAdd)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonRemove))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonAvaRooms)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(91, 91, 91)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextFieldSt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(5, 5, 5)
                                        .addComponent(jTextFieldEt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(35, 35, 35))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabelEt)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabelSt)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabelTopic)
                                            .addComponent(jTextFieldTopic, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSendInvi, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(122, 122, 122)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void jButtonAvaRoomsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAvaRoomsActionPerformed

        String date,startTime, endTime, query;
        int rowSelected;
        int numOfInvitees;
        roomTableModel = (DefaultTableModel) jTableRoom.getModel();
        numOfInvitees = inviTableModel.getRowCount();

        // get date from the calendar
        date = getDateFromCal();

        // get the selected row and tis date in the time slot table'
        startTime = jTextFieldSt.getText();  // store the startTime
        endTime = jTextFieldEt.getText(); // store the endTime

        // execute the query and display
        query = getAvaRoomQuery(date, startTime, endTime, numOfInvitees);

        // show available rooms
        roomTableModel.setRowCount(0); // refresh the table
        showAvaRoom(query);

    }//GEN-LAST:event_jButtonAvaRoomsActionPerformed

    private void jTextFieldStActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldStActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldStActionPerformed

    private void jTextFieldTopicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldTopicActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldTopicActionPerformed

    // Send a new invitation
    private void jButtonSendInviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSendInviActionPerformed
        // Check conflicts before sending it
        
        // notify conflict exists. still send? yes - > send
        
        // no? don't send
    }//GEN-LAST:event_jButtonSendInviActionPerformed

    // Display the meeting conflicts
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // get the selected date, start time, and end time
        String date, st, et, queryConflicts; 
        date = getDateFromCal();
        st = jTextFieldSt.getText();
        et = jTextFieldEt.getText();
        
        // get the query that displays all the meeting conflicts
        queryConflicts = getConflictQuery(date, st, et);
        
        // Display all the conflicts
        displayMeetingConflicts(queryConflicts);
        
        
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(EmpSendInvitationNotAva.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmpSendInvitationNotAva.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmpSendInvitationNotAva.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmpSendInvitationNotAva.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmpSendInvitationNotAva().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonAvaRooms;
    private javax.swing.JButton jButtonRemove;
    private javax.swing.JButton jButtonSendInvi;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabelEmpList;
    private javax.swing.JLabel jLabelEt;
    private javax.swing.JLabel jLabelInvList;
    private javax.swing.JLabel jLabelSt;
    private javax.swing.JLabel jLabelTopic;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTableEmp;
    private javax.swing.JTable jTableInv;
    private javax.swing.JTable jTableMeetingCon;
    private javax.swing.JTable jTableRoom;
    private javax.swing.JTextField jTextFieldEt;
    private javax.swing.JTextField jTextFieldSt;
    private javax.swing.JTextField jTextFieldTopic;
    // End of variables declaration//GEN-END:variables
}
