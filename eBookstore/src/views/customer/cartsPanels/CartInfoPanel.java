package views.customer.cartsPanels;

import models.CartInfo;
import models.WindowMethods;
import models.dataBaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class CartInfoPanel {
    protected WindowMethods windowMethods = new WindowMethods();
    protected JButton back ;
    protected dataBaseConnection dataBase;
    protected DefaultTableModel tableModel = new DefaultTableModel();
    protected JTable table;
    protected JScrollPane listScroller;
    protected Vector<Vector<String>> data = new Vector<Vector<String>>();
    protected Vector<String> columnNames = new Vector<String>();
    protected CartInfo cart;
    protected JPanel up , down , center;
    protected String user;
    protected JLabel paymentMethod , deliveryCost , totalCost ;

    public CartInfoPanel(String user, dataBaseConnection dataBase , CartInfo cart){
        this.dataBase = dataBase;
        this.cart = cart;
        this.user = user;
        windowMethods.window = new JFrame();
        windowMethods.settings();
        windowMethods.window.setLocation(200, 80);
        add_components();
        windowMethods.window.setVisible(true);

    }

    protected void add_components(){
        panels();
        windowMethods.window.add(up, BorderLayout.NORTH);
        windowMethods.window.add(center, BorderLayout.CENTER);
        windowMethods.window.add(down, BorderLayout.SOUTH);
    }

    protected void panels() {
        components();

        up = new JPanel();
        up.setLayout(new BoxLayout(up ,BoxLayout.PAGE_AXIS));
        up.add(paymentMethod);
        up.add(deliveryCost);
        up.add(totalCost);

        center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.add(listScroller);

        down = new JPanel();
        down.setLayout(new BorderLayout());
        down.add(back, BorderLayout.WEST);

    }

    protected void components() {
        buttons();
        try {
            infoLabels();
        } catch (SQLException e) {
            System.out.println("Błąd przy pobieraniu informacji o koszyku");
            e.printStackTrace();
        }
        columnNames.add("Lp");
        columnNames.add("Nazwa");
        columnNames.add("Cena sztuki");
        columnNames.add("Sztuk");
        columnNames.add("Cena zbiorcza");
        columnNames.add("Typ produktu");
        columnNames.add("Id_produktu");

        createTable(1);
    }

    protected void buttons() {
        back = new JButton("Powrót");

    }

    protected void infoLabels() throws SQLException {
        paymentMethod  = new JLabel();
        deliveryCost = new JLabel();
        totalCost =  new JLabel();


        dataBase.setStmt();

        ResultSet rs ;

        rs = dataBase.getStmt().executeQuery(
                "Select  sposob_platnosci , koszt_wysylki, calkowita_wartosc_zamowienia  FROM koszyk_zakupowy WHERE nr_koszyka = " + cart.getCartId()
        );
        rs.next();

        paymentMethod.setText("Sposób płatności " + rs.getString(1));
        deliveryCost.setText("Koszt dostawy " + rs.getString(2));
        totalCost.setText("Łącznie do zapłaty " + rs.getString(3));

        rs.close();
        dataBase.getStmt().close();
    }

    protected void createTable(int mode) {
        try {
            getProductList(mode);
        } catch (SQLException e) {
            System.out.println("Błąd SQLa przy tworzeniu tabeli produktów w koszyku zakupowym");
            e.printStackTrace();
        }
        tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel);
        table.removeColumn(table.getColumnModel().getColumn(6));
        table.removeColumn(table.getColumnModel().getColumn(0));
        table.setPreferredScrollableViewportSize(new Dimension(600, 300));
        table.setFillsViewportHeight(true);
        table.changeSelection(0,0, false, false);
        listScroller = new JScrollPane(table);
        listScroller.setViewportView(table);
    }

    protected void getProductList(int mode) throws SQLException {
        data.clear();

        dataBase.setStmt();

        ResultSet rs ;

        rs = dataBase.getStmt().executeQuery(
                    "Select \"L.p.\" , nazwa , cena , liczba , wartosc_artykulu , co , id_produktu from element_koszyka JOIN produkt ON produkt_id_produktu = id_produktu" +
                            " WHERE koszyk_zakupowy_nr_koszyka = " + cart.getCartId() + " ORDER BY \"L.p.\""

            );


        while(rs.next()){
            Vector<String> vString = new Vector<String>();

            vString.add(rs.getString(1));
            vString.add(rs.getString(2));
            //vString.add(rs.getString(3));
            vString.add(String.valueOf(rs.getDouble(5)/rs.getDouble(4)));
            vString.add(rs.getString(4));
            vString.add(rs.getString(5));
            String tmp = rs.getString(6);
            if(tmp.equals("g")){
                vString.add("gra planszowa");
            }
            else if(tmp.equals("k")){
                vString.add("książka");
            }
            vString.add(rs.getString(7));
            data.add(vString);
        }
        rs.close();

        dataBase.getStmt().close();

    }



}
