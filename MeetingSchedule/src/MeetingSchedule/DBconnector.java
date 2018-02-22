/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MeetingSchedule;

import java.sql.*;

/**
 *
 * @author maxkim
 */
public class DBconnector {

    private static final String DBHOST = "localhost";
    private static final String DBNAME = "CS580_Schedule";
    private static final String DBUSER = "root";
    private static final String DBPASSWORD = "maxkim";
    private static final String CONN_STRING
            = "jdbc:mysql://" + DBHOST + "/" + DBNAME + "?user=" + DBUSER
            + "&password=" + DBPASSWORD + "&useSSL=false";

    public static void main(String[] args) {
        /*
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(CONN_STRING, DBUSER, DBPASSWORD);
            System.out.println("Database successfully connected");
        }catch (SQLException e) {
            System.err.println(e);
        }
         */
        //sqlDisplayer("select * from rooms;");
    }

    public static void connectToDB() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(CONN_STRING, DBUSER, DBPASSWORD);
            System.out.println("Database successfully connected");
        }catch (SQLException e) {
            System.err.println(e);
        }
    }
    
    public static void sqlHandler(String sqlVar) {
        try {
            //Load the MySQL Connector / J classes
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            //Set connect string to local MySQL database, user is JohnCena
            String connString = "jdbc:mysql://" + DBHOST + "/" + DBNAME
                    + "?user=" + DBUSER + "&" + "password=" + DBPASSWORD + "&useSSL=false";

            Connection conn = DriverManager.getConnection(connString);
            System.out.println("Database successfully connected");

            //Get result set
            Statement stmt = conn.createStatement();
            String varSQL = sqlVar;
            stmt.executeUpdate(varSQL);

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Example of JDBC Using MySQL
    // Taken from Kroenke's Book
    public static void sqlDisplayer(String MySQLquery) {
        try {
            //Load the MySQL Connector / J classes
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            //Set connect string to local MySQL database, user is JohnCena
            String connString = "jdbc:mysql://" + DBHOST + "/" + DBNAME
                    + "?user=" + DBUSER + "&" + "password=" + DBPASSWORD + "&useSSL=false";

            Connection conn = DriverManager.getConnection(connString);
            System.out.println("Database successfully connected");

            // Get result set
            Statement stmt = conn.createStatement();
            String select = MySQLquery;
            ResultSet rs = stmt.executeQuery(select);

            //Get meta data on just opened result set
            ResultSetMetaData rsMeta = rs.getMetaData();

            // Display Column names as string
            String varColNames = "";
            int varColCount = rsMeta.getColumnCount();
            for (int col = 1; col <= varColCount; col++) {
                varColNames = varColNames + rsMeta.getColumnName(col) + " ";
            }
            System.out.println(varColNames);

            //Display column values
            while (rs.next()) {
                for (int col = 1; col <= varColCount; col++) {
                    System.out.print(rs.getString(col) + " ");
                }
                System.out.println();
            }

            //Clean up
            rs.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
