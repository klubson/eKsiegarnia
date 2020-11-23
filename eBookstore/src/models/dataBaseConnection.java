package models;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class dataBaseConnection {
    private static Connection conn;
    private static Statement stmt = null;
    private static CallableStatement cstmt;
    private static String connectionString = "jdbc:oracle:thin:@//admlab2.cs.put.poznan.pl:1521/"+
            "dblab02_students.cs.put.poznan.pl";
    public void connect(){
        if (conn == null){
            Properties connectionProps = new Properties();
            connectionProps.put("user", "inf141246");
            connectionProps.put("password", "inf141246");
            try {
                conn = DriverManager.getConnection(connectionString,
                        connectionProps);
                System.out.println("Połączono z bazą danych");
            } catch (SQLException ex) {
                Logger.getLogger(dataBaseConnection.class.getName()).log(Level.SEVERE,
                        "Nie udało się połączyć z bazą danych", ex);
                System.exit(-1);
            }
        }
    }
    public Connection getConn(){
        return conn;
    }
    public void setStmt() throws SQLException { stmt = conn.createStatement(); }
    public Statement getStmt(){
        return stmt;
    }
    public void setCstmt(String sql) throws SQLException { cstmt = conn.prepareCall(sql); }
    public CallableStatement getCstmt() {return cstmt;}
}
