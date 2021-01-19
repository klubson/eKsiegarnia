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

    public boolean findLoggedUser(String login, String user) throws SQLException {
        setStmt();
        ResultSet rs = stmt.executeQuery("SELECT Login FROM Uzytkownik WHERE Login = " + "'" + login + "'");
        if(rs.next()) {
            //System.out.println("Znaleziono login: " + rs.getString(1));
            if(rs.getString(1).equals(user)){
                //System.out.println("taki sam login");
                rs.close();
                getStmt().close();
                return true;
            }
            else{
                rs.close();
                getStmt().close();
                return false;
            }
        }
        else {
            System.out.println("nie znaleziono");
            rs.close();
            getStmt().close();
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
    public void newEmployee(String login, String pass, String name, String surname, String phone, String hired, float salary, String jobType, String contractType) throws SQLException {
        setCstmt("{call nowyPracownik(?,?,?,?,?,?,?,?,?)}");
        cstmt.setString(1, login);
        cstmt.setString(2, pass);
        cstmt.setString(3, name);
        cstmt.setString(4, surname);
        cstmt.setString(5, phone);
        cstmt.setString(6, hired);
        cstmt.setFloat(7, salary);
        cstmt.setString(8, jobType);
        cstmt.setString(9, contractType);
        cstmt.execute();
        cstmt.close();
    }
    public void newPublisher(String name, String country) throws SQLException {
        setStmt();
        int changes = stmt.executeUpdate(
                "INSERT INTO Wydawnictwo(Nazwa, Kraj_pochodzenia) VALUES('" + name + "','" + country + "')"
        );
        System.out.println("Wstawiono " + changes + " krotkę");
        stmt.close();
    }
    public void newAuthor(String name, String surname, String country) throws SQLException {
        setStmt();
        int changes = stmt.executeUpdate(
                "INSERT INTO Autor(Imie, Nazwisko, Kraj_pochodzenia) VALUES('" + name + "','"+ surname + "','" + country + "')"
        );
        System.out.println("Wstawiono " + changes + " krotkę");
        stmt.close();
    }
    public void newBook(String name, float price, String year, int storage, String publisher, String cover, int pages, String size, String seriesTitle) throws SQLException {
        setCstmt("{call nowaKsiazka(?,?,?,?,?,?,?,?,?)}");
        cstmt.setString(1, name);
        cstmt.setFloat(2, price);
        cstmt.setString(3, year);
        cstmt.setInt(4, storage);
        cstmt.setString(5, publisher);
        cstmt.setString(6, cover);
        if(pages == -1) cstmt.setNull(7,java.sql.Types.INTEGER);
        else cstmt.setInt(7, pages);
        cstmt.setString(8, size);
        cstmt.setString(9, seriesTitle);
        cstmt.execute();
        cstmt.close();
    }
    public void newGame(String name, float price, String year, int storage, String publisher, int min_players, int max_players, String min_age, String est_time) throws SQLException {
        setCstmt("{call nowaGra(?,?,?,?,?,?,?,?,?)}");
        cstmt.setString(1, name);
        cstmt.setFloat(2, price);
        cstmt.setString(3, year);
        cstmt.setInt(4, storage);
        cstmt.setString(5, publisher);
        cstmt.setInt(6, min_players);
        cstmt.setInt(7, max_players);
        if (min_age.equals("")){
            cstmt.setNull(8, Types.NUMERIC);
        }
        else cstmt.setInt(8, Integer.parseInt(min_age));
        if (est_time.equals("")){
            cstmt.setNull(9, Types.NUMERIC);
        }
        else cstmt.setInt(9, Integer.parseInt(est_time));
        cstmt.execute();
        cstmt.close();
    }

    public int newCart(String login)   {
        int cartId = -1;
        try{
            setStmt();
            int changes = stmt.executeUpdate(
                    "INSERT INTO Koszyk_zakupowy(Klient_login, Nr_koszyka, Wartosc_zakupow , Sposob_platnosci , Koszt_wysylki , Calkowita_wartosc_zamowienia) " +
                            "VALUES('" + login + "', KOSZYK_ZAKUPOWY_NR_KOSZYKA_SEQ.nextval , 0 , 'BLIK' , 10.00, 10.00 )"
            );
            System.out.println("Wstawiono " + changes + " krotkę");
        }
        catch (SQLException e)
        {
            System.out.println("Błąd przy tworzeniu koszyka");
        }
        finally {
            if(stmt != null){
                try{
                    stmt.close();
                }
                catch (SQLException e)
                {
                    System.out.println("Błąd przy zamykaniu statement przy tworzeniu koszyka");
                }
            }
        }

        try{
            setStmt();
            ResultSet rs = stmt.executeQuery(
                    "SELECT KOSZYK_ZAKUPOWY_NR_KOSZYKA_SEQ.currval FROM dual"
            );
            rs.next();
            cartId = rs.getInt(1);
            System.out.println("id koszyka " + cartId );
        }
        catch (SQLException e)
        {
            System.out.println("Błąd przy pobieraniu id koszyka");
        }
        finally {
            if(stmt != null){
                try{
                    stmt.close();
                }
                catch (SQLException e)
                {
                    System.out.println("Błąd przy zamykaniu statement przy pobieraniu id koszyka");
                }
            }
        }
        return cartId;
    }

    public void newCartItem(CartInfo cart , int produktId , double productPrice , int amount)
    {
        try{
            setStmt();
            int changes = stmt.executeUpdate(
                    "INSERT INTO Element_koszyka " +
                            "VALUES("+ cart.getNextLP() + ", "+ produktId + ", " + cart.getCartId() + ", " + amount+", " +productPrice +" )"
            );
            System.out.println("Wstawiono " + changes + " krotkę");
            changes = stmt.executeUpdate(
                    "Update koszyk_zakupowy SET Wartosc_zakupow = Wartosc_zakupow + " + productPrice + ", Calkowita_wartosc_zamowienia = Calkowita_wartosc_zamowienia + " +
                            productPrice + " WHERE Nr_koszyka = "+ cart.getCartId()

            );
            System.out.println("Zmodyfikowano " + changes + " krotkę");
            changes = stmt.executeUpdate(
                    "Update produkt SET stan_magazyn = stan_magazyn - " + amount  + " WHERE id_produktu = "+ produktId

            );
            System.out.println("Zmodyfikowano " + changes + " krotkę");
        }
        catch (SQLException e)
        {
            System.out.println("Błąd przy dodawaniu pozycji do koszyka");
        }
        finally {
            if(stmt != null){
                try{
                    stmt.close();
                }
                catch (SQLException e)
                {
                    System.out.println("Błąd przy zamykaniu statement przy dodawaniu do koszyka");
                }
            }
        }
    }
}
