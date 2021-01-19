package views.customer;

import models.CartInfo;
import models.WindowMethods;
import models.dataBaseConnection;
import views.customer.cartsPanels.PreviousCartPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ShoppingHistoryPanel {
    protected WindowMethods windowMethods = new WindowMethods();
    protected JButton back ,details;
    protected dataBaseConnection dataBase;
    protected DefaultTableModel tableModel = new DefaultTableModel();
    protected JTable table;
    protected JScrollPane listScroller;
    protected Vector<Vector<String>> data = new Vector<Vector<String>>();
    protected Vector<String> columnNames = new Vector<String>();
    protected CartInfo cart;
    protected JPanel  down , center;
    protected String user;

    public ShoppingHistoryPanel(String user, dataBaseConnection dataBase , CartInfo cart){
        this.dataBase = dataBase;
        this.cart = cart;
        this.user = user;
        windowMethods.window = new JFrame("Historia zakupów");
        windowMethods.settings();
        windowMethods.window.setLocation(200, 80);
        add_components();
        windowMethods.window.setVisible(true);
    }

    private void add_components() {
        panels();
        //windowMethods.window.add(up, BorderLayout.NORTH);
        windowMethods.window.add(center, BorderLayout.CENTER);
        windowMethods.window.add(down, BorderLayout.SOUTH);
    }

    private void panels() {
        components();

        center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.add(listScroller);

        down = new JPanel();
        down.setLayout(new BorderLayout());
        down.add(back, BorderLayout.WEST);
        down.add(details , BorderLayout.EAST);
    }

    private void components() {
        buttons();
        columnNames.add("Id koszyka");
        columnNames.add("Zapłacono");
        columnNames.add("Ilość przedmiotów");
        columnNames.add("Sposób płątności");
        createTable();

    }



    private void buttons() {
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer_panel cp = new Customer_panel();
                windowMethods.exit();
                try {
                    cp.createFromBack(user , dataBase , cart);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        details = new JButton("Szczegóły");
        details.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int querriedCartId = Integer.parseInt(data.get(table.getSelectedRow()).get(0));
                CartInfo querriedCart = new CartInfo(querriedCartId);
                new PreviousCartPanel(user , dataBase , querriedCart , cart);
                windowMethods.exit();

            }
        });
    }

    private void createTable() {
        try {
            getShoppingHistory();
        } catch (SQLException e) {
            System.out.println("Błąd SQLa przy tworzeniu tabeli w historii zakupów");
            e.printStackTrace();
        }
        tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel);
        table.removeColumn(table.getColumnModel().getColumn(0));
        table.setPreferredScrollableViewportSize(new Dimension(600, 300));
        table.setFillsViewportHeight(true);
        table.changeSelection(0,0, false, false);
        listScroller = new JScrollPane(table);
        listScroller.setViewportView(table);
    }

    private void getShoppingHistory() throws SQLException {
        data.clear();

        dataBase.setStmt();

        ResultSet rs ;

        rs = dataBase.getStmt().executeQuery(
                "SELECT nr_koszyka , calkowita_wartosc_zamowienia, suma, sposob_platnosci  FROM koszyk_zakupowy " +
                        "JOIN (SELECT sum(liczba) as suma, koszyk_zakupowy_nr_koszyka  FROM element_koszyka GROUP BY koszyk_zakupowy_nr_koszyka) " +
                        "ON nr_koszyka = koszyk_zakupowy_nr_koszyka " +
                        "WHERE klient_login = '"+ user +"'"
        );

        while(rs.next()){
            Vector<String> vString = new Vector<String>();

            vString.add(rs.getString(1));
            vString.add(rs.getString(2));
            vString.add(rs.getString(3));
            vString.add(rs.getString(4));

            data.add(vString);
        }
        rs.close();

        dataBase.getStmt().close();
    }
}
