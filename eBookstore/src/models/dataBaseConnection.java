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
    public void disconnect() throws SQLException {
        System.out.println("Rozłączono z bazą danych");
        conn.close();
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

    public boolean findUser(String login) throws SQLException {
        setStmt();
        ResultSet rs = stmt.executeQuery("SELECT Login FROM Klient WHERE Login = " + "'" + login + "'");
        if(rs.next()) {
            rs.close();
            return true;
        }
        else {
            rs.close();
            return false;
        }
    }
    public int checkPwd(String login, String pass) throws SQLException {
        setCstmt("{? = call checkPwd(?,?)}");
        cstmt.setString(2, login);
        cstmt.setString(3, pass);
        cstmt.registerOutParameter(1, Types.INTEGER);
        cstmt.execute();
        int result = cstmt.getInt(1);
        cstmt.close();
        return result;
    }
    public String getName(String login) throws SQLException {
        setStmt();
        ResultSet rs = stmt.executeQuery("SELECT Imie, Nazwisko FROM Uzytkownik WHERE Login = "
                + "'" + login + "'");
        rs.next();
        String data = rs.getString("Imie") + " " + rs.getString("Nazwisko");
        rs.close();
        return data;
    }
    public String getJob_type(String login) throws SQLException{
        setStmt();
        ResultSet rs = stmt.executeQuery("SELECT P_STANOWISKO FROM Pracownik WHERE Login = " +
                "'" + login + "'");
        rs.next();
        String job = rs.getString("P_STANOWISKO");
        rs.close();
        return job;
    }
    public void newClient(String login, String pass, String name, String surname, String phone, String e_mail, String address) throws SQLException {
        setCstmt("{call nowyKlient(?,?,?,?,?,?,?)}");
        cstmt.setString(1, login);
        cstmt.setString(2, pass);
        cstmt.setString(3, name);
        cstmt.setString(4, surname);
        cstmt.setString(5, phone);
        cstmt.setString(6, e_mail);
        cstmt.setString(7, address);
        cstmt.execute();
        cstmt.close();
    }


}
