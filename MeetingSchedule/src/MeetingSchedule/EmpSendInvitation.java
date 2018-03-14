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

// SendInvitation/Available Time jFrmae
public class EmpSendInvitation extends javax.swing.JFrame {

    // username is received from the EmpMain jFrame
    String username;
    // used for the table to display schedule info
    DefaultTableModel empTableModel, inviTableModel, timeTableModel,
            roomTableModel;

    /**
     * Creates new form EmpSendInvitation2
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

    private String getGrpSchQuery(String name, String date) {
        String query = "INSERT INTO groupSchedule (date, startTime, endTime) "
                + "SELECT es.date, es.startTime, es.endTime "
                + "FROM empSchedule AS es "
                + "WHERE es.date = '" + date + "' "
                + "AND es.username = '" + name + "';";
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
                           //JOptionPane.showMessageDialog(null, "get group schedule" + query);

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
    
    // This method returns a query for selecting avaialble rooms
    private String getAvaRoomQry(String date, String st, String et, int size) {
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
                       // JOptionPane.showMessageDialog(null, query);
        return query;
    }

    private String getMakeMeetingQry(String date, String st, String et, String topic, String room) {
        String query = "INSERT INTO meetings(room, ownerID, date, startTime, endTime, topic) "
                + "VALUES ( " + room + ", '" + username + "', '" + date + "', '" + st + "', '"
                + et + "', '" + topic + "'); ";
        //JOptionPane.showMessageDialog(null, query);
        return query;
    }
    
    // This method sends the invitation to the selected invitees
    private void insertAssignment(String date, String st, String et, String room, String[] names, String topic) {
        String queryToGetMeetingID, queryToInsertAssignment;
        // This query returns the id of the meeting the invitor newly created, which the selected invitees will come to
        queryToGetMeetingID = "SELECT id "
                + "FROM meetings "
                + "WHERE date = '" + date + "' "
                + "AND startTime = '" + st + "' AND "
                + "endTime = '" + et + "' AND topic = '" + topic + "'";
        //JOptionPane.showMessageDialog(null, queryToGetMeetingID);
        // i is set to 1 because the invitor does not need to add the meeting to his assignment
        // instead, the meeting will be added directly into his schedule
        for (int i = 1; i < names.length; i++) {
            queryToInsertAssignment = "INSERT INTO assignments (meetingID, inviteeID, acceptance, invitorID, checked) "
                    + "VALUES "
                    + "((" + queryToGetMeetingID + "), '" + names[i] + "', 'unchecked', '" + username + "', 'unchecked');";
            //JOptionPane.showMessageDialog(null, queryToInsertAssignment);
            executeSQLQuery(queryToInsertAssignment, "Assignment Inserted Successfully");
        }
    }
    
    private void insertEmpSchedule(String date, String startTime, String endTime, String topic) {
        String query = "INSERT INTO empSchedule (username, date, startTime, endTime, task, visibility) "
                + "VALUES ('" + username + "', '" + date + "', '" + startTime + "', '" + endTime + "', '"
                + topic + "', 'public')";
                    //JOptionPane.showMessageDialog(null, query);
                    executeSQLQuery(query, "EmpSchedule Inserted Successfully");
    }

    private String getRoomQuery(int num) {
        String query = "SELECT id AS roomNumber "
                + "FROM rooms "
                + "WHERE size >= " + num + ";";
        return query;
    }
    
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void convertNameToUsername(String[] names) {
        //JOptionPane.showMessageDialog(null, "inside convert");
        String query;
        int size = names.length;

        DBconnector db = new DBconnector();
        Connection connection = db.connectToDB();
        Statement st;
        ResultSet rs;
        
        for (int i = 1; i < size; i++) {
            query = "SELECT username "
                    + "FROM employees "
                    + "WHERE name = '" + names[i] + "';";

            try {
                st = connection.createStatement();
                // execute the given SQL statement and get the result
                rs = st.executeQuery(query);
                if (rs.next()) {
                    names[i] = rs.getString("username");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        jLabelTopic = new javax.swing.JLabel();
        jTextFieldTopic = new javax.swing.JTextField();
        jButtonAvaRooms = new javax.swing.JButton();

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

        jButtonAvaTime.setBackground(new java.awt.Color(255, 255, 204));
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

        jButtonSendInvi.setBackground(new java.awt.Color(204, 255, 204));
        jButtonSendInvi.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButtonSendInvi.setText("Send Invitations");
        jButtonSendInvi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSendInviActionPerformed(evt);
            }
        });

        jLabelTopic.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabelTopic.setText("Topic:");

        jButtonAvaRooms.setBackground(new java.awt.Color(255, 255, 204));
        jButtonAvaRooms.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButtonAvaRooms.setText("Available Rooms");
        jButtonAvaRooms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAvaRoomsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(149, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelInvList)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(99, 99, 99)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelAvailTime)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelAvailRoom)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonAvaTime, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonAvaRooms, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(64, 64, 64))
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabelEmpList)
                .addContainerGap(885, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabelTopic)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jButtonSendInvi, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextFieldTopic, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(78, 78, 78))
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelInvList, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelEmpList, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonAvaRooms, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonAvaTime, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelTopic)
                            .addComponent(jTextFieldTopic, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonSendInvi, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(350, Short.MAX_VALUE))
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
        
        inviTableModel = (DefaultTableModel) jTableInv.getModel();
        timeTableModel = (DefaultTableModel) jTableTime.getModel();
        
        // get the names of the selected employees
        int invTabSize = inviTableModel.getRowCount();

        String[] selectedNames = new String[invTabSize + 1];
        selectedNames[0] = username; // the owner of the meeting is automatically included

        if (invTabSize >= 1) {
            for (int i = 1; i < invTabSize + 1; i++) {
                selectedNames[i] = inviTableModel.getValueAt(i - 1, 0).toString();
            }
        }
        
        // convert all the names in selectedNames into their usernames
        convertNameToUsername(selectedNames);
        
        //// TEST:  test invitees in the table
        //for (int i = 0; i < selectedNames.length; i++) {
        //       JOptionPane.showMessageDialog(null, "names(i): " + selectedNames[i]);
        //}
        
        // make the `allHours` table
        makeAllHours();
        
        // save the schedules, including the meetings, of the selected employees in the groupSchedule table
        saveInGroupSch(selectedNames);

        // Display the available time slots in the table
        timeTableModel.setRowCount(0); // refresh the table
        ShowAvaTimeSlots();
        
        // Delete all the records in the `groupSchedule` nad the `allHours` tables
        deleteSchAndHrs();
        

    }//GEN-LAST:event_jButtonAvaTimeActionPerformed

    // This method sends an invitation to the selected invitees
    private void jButtonSendInviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSendInviActionPerformed
        String query, startTime, endTime, room, topic, date;
        
         // get the names of the selected employees
        int invTabSize = inviTableModel.getRowCount();

        String[] selectedNames = new String[invTabSize + 1];
        selectedNames[0] = username; // the owner of the meeting is automatically included

        if (invTabSize >= 1) {
            for (int i = 1; i < invTabSize + 1; i++) {
                selectedNames[i] = inviTableModel.getValueAt(i - 1, 0).toString();

            }
        }
        
        // replace all the names in selectedNames with their usernames
        convertNameToUsername(selectedNames);
        
        // get the selected time, room number, and topic
        timeTableModel = (DefaultTableModel) jTableTime.getModel();
        roomTableModel = (DefaultTableModel) jTableRoom.getModel();
        int i = jTableTime.getSelectedRow();
        int j = jTableRoom.getSelectedRow();
        startTime = timeTableModel.getValueAt(i, 0).toString(); // store the startTime
        endTime = timeTableModel.getValueAt(i, 1).toString(); // store the endTime
        room = roomTableModel.getValueAt(j, 0).toString();
        topic = jTextFieldTopic.getText();
        date = getDateFromCal();
        
        // Create a meeting
        query = getMakeMeetingQry(date, startTime, endTime, topic, room);
        executeSQLQuery(query, "Meeting Inserted Successfully");
        
        // Invite the selected employees
        insertAssignment(date, startTime, endTime, room, selectedNames, topic);
        
        // Put the meeting in the schedule of the invitor automatically
        insertEmpSchedule(date, startTime, endTime, topic);
        
        // Display a message
        JOptionPane.showMessageDialog(null, "A New Meeting Invitation is Sent.");

    }//GEN-LAST:event_jButtonSendInviActionPerformed


    
    // This method shows available rooms
    private void jButtonAvaRoomsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAvaRoomsActionPerformed
        
        
        String date,startTime, endTime, query;
        int rowSelected;
        int numOfInvitees;
        timeTableModel = (DefaultTableModel) jTableTime.getModel();
        roomTableModel = (DefaultTableModel) jTableRoom.getModel();
        numOfInvitees = inviTableModel.getRowCount();
        
        // get date from the calendar 
        date = getDateFromCal();
        
        // get the selected row and tis date in the time slot table'
        rowSelected = jTableTime.getSelectedRow();
        startTime = timeTableModel.getValueAt(rowSelected, 0).toString(); // store the startTime
        endTime = timeTableModel.getValueAt(rowSelected, 1).toString(); // store the endTime
        
        // execute the query and display
        // numOfInvitees is added by 1 for the invitor who is creating the meeting
        query = getAvaRoomQry(date, startTime, endTime, numOfInvitees + 1);
        
        // show available rooms
        roomTableModel.setRowCount(0); // refresh the table
        showAvaRoom(query);
        
        // Display a message if there exists no available rooms
        if(jTableRoom.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "There are no available rooms at thie time.");
        }
        
    }//GEN-LAST:event_jButtonAvaRoomsActionPerformed

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
    private javax.swing.JButton jButtonAvaRooms;
    private javax.swing.JButton jButtonAvaTime;
    private javax.swing.JButton jButtonRemove;
    private javax.swing.JButton jButtonSendInvi;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabelAvailRoom;
    private javax.swing.JLabel jLabelAvailTime;
    private javax.swing.JLabel jLabelEmpList;
    private javax.swing.JLabel jLabelInvList;
    private javax.swing.JLabel jLabelTopic;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTableEmp;
    private javax.swing.JTable jTableInv;
    private javax.swing.JTable jTableRoom;
    private javax.swing.JTable jTableTime;
    private javax.swing.JTextField jTextFieldTopic;
    // End of variables declaration//GEN-END:variables


}
