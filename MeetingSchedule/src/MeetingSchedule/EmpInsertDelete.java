/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MeetingSchedule;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author maxkim
 */
public class EmpInsertDelete extends javax.swing.JFrame {

    /**
     * Creates new form EmpInsertDelete
     */
    public EmpInsertDelete() {
        initComponents();
        Show_Emp_In_JTable();        
    }

    // Before displaying employees in JTable, first store each employee as an object
    // make them a list
    public ArrayList<EmpDB> getEmpList()
    {
 
        DBconnector db = new DBconnector();
        // each roomDB object has its number and size
        ArrayList<EmpDB> empList = new ArrayList<EmpDB>();
        // connect to database 
        Connection connection = db.connectToDB();
        String query = "SELECT * FROM `employees`";
        Statement st;
        ResultSet rs = db.getQueryResult(query);
        
        try {
            st = connection.createStatement();
            // get the SQL query result
            rs = st.executeQuery(query);
            EmpDB emp;
            while(rs.next()) {
                // store SQL result's first and second column 
                emp = new EmpDB(rs.getString("name"), rs.getString("pswd"));
                // add new rooms (number, size) to the list
                empList.add(emp);
            }    
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return empList;
    }   
    
        // Display Data in JTable
    public void Show_Emp_In_JTable()
    {
        // connect to database and get all the existing rooms
        ArrayList<EmpDB> list = getEmpList();
        DefaultTableModel model = (DefaultTableModel)jTableDisplayEmployees.getModel();
        // each row has room's attributes: size and number 
        Object[] row = new Object[2];
        for (int i = 0; i < list.size(); i++) 
        {
            row[0] = list.get(i).getName();
            row[1] = list.get(i).getPassword();
            model.addRow(row);
        }
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
                DefaultTableModel model = (DefaultTableModel)jTableDisplayEmployees.getModel();
                model.setRowCount(0);
                Show_Emp_In_JTable();
                
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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Passwowrd = new javax.swing.JLabel();
        TxtFdName = new javax.swing.JTextField();
        TxtFdPswd = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableDisplayEmployees = new javax.swing.JTable();
        jButtonInsert = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        LabelName = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Passwowrd.setText("Password:");

        TxtFdName.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N

        TxtFdPswd.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N

        jTableDisplayEmployees.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Password(Hashed)"
            }
        ));
        jTableDisplayEmployees.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDisplayEmployeesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableDisplayEmployees);

        jButtonInsert.setBackground(new java.awt.Color(204, 255, 204));
        jButtonInsert.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jButtonInsert.setText("Insert");
        jButtonInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInsertActionPerformed(evt);
            }
        });

        jButtonDelete.setBackground(new java.awt.Color(255, 153, 153));
        jButtonDelete.setText("Delete");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        LabelName.setText("Name:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(123, 123, 123))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(LabelName)
                            .addComponent(Passwowrd))
                        .addGap(32, 32, 32)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(TxtFdName, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                            .addComponent(TxtFdPswd))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jButtonDelete, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelName, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TxtFdName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Passwowrd, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TxtFdPswd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(90, 90, 90)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTableDisplayEmployeesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDisplayEmployeesMouseClicked
        // Display Selected Row in JTextFields
        int i = jTableDisplayEmployees.getSelectedRow();
        TableModel model = jTableDisplayEmployees.getModel();
        TxtFdName.setText(model.getValueAt(i, 0).toString());
        TxtFdPswd.setText(model.getValueAt(i, 1).toString());
    }//GEN-LAST:event_jTableDisplayEmployeesMouseClicked

    // Password is hashed using MD5
    private void jButtonInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInsertActionPerformed
        String query = "INSERT INTO `employees`(`name`, `pswd`) VALUES ('" + TxtFdName.getText() +
        "', MD5(" + TxtFdPswd.getText() + "))";
        executeSQLQuery(query, "Inserted");
    }//GEN-LAST:event_jButtonInsertActionPerformed
    
    // Password is hashed using MD5
    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        String query = "DELETE FROM `employees` WHERE `name` = '" + TxtFdName.getText() +
        "' AND `pswd` = MD5(" + TxtFdPswd.getText() + ");";
        executeSQLQuery(query, "Deleted");
    }//GEN-LAST:event_jButtonDeleteActionPerformed

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
            java.util.logging.Logger.getLogger(EmpInsertDelete.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmpInsertDelete.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmpInsertDelete.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmpInsertDelete.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmpInsertDelete().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelName;
    private javax.swing.JLabel Passwowrd;
    private javax.swing.JTextField TxtFdName;
    private javax.swing.JTextField TxtFdPswd;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonInsert;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableDisplayEmployees;
    // End of variables declaration//GEN-END:variables
}