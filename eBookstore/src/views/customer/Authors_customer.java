package views.customer;

import models.CartInfo;
import models.WindowMethods;
import models.dataBaseConnection;
import views.customer.Product_panels.Products_customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Authors_customer {
    private WindowMethods windowMethods = new WindowMethods();
    private JCheckBox name_asc, name_desc, surname_asc, surname_desc, country_asc, country_desc;
    private JButton back, filter;
    private JPanel up, center, down;
    private JLabel empty;
    private dataBaseConnection dataBase = null;//new dataBaseConnection();
    private DefaultTableModel tableModel = new DefaultTableModel();
    private JScrollPane listScroller;
    private String user, sort_asc, sort_desc;
    private Vector<Vector<String>> data = new Vector<Vector<String>>();
    private Vector<String> columnNames = new Vector<String>();
    private JTable table;
    private CartInfo cart;
    private JButton products;

    public void create(String data , CartInfo cart,dataBaseConnection dataBase) throws SQLException {
        this.cart = cart;
        this.dataBase = dataBase;
        windowMethods.window = new JFrame("Autorzy");
        windowMethods.settings();
        user = data;
        add_components();
        windowMethods.window.setVisible(true);
    }

    private void getAuthorList(int mode) throws SQLException {
        data.clear();
        dataBase.setStmt();
        //dataBase.getConn().setAutoCommit(true);
        ResultSet rs = null;
        if(sort_asc == "" && sort_desc == "") mode = 1;
        if(mode == 1) {
            rs = dataBase.getStmt().executeQuery(
                    "SELECT Imie, Nazwisko, Kraj_pochodzenia" +
                            " FROM Autor ORDER BY ID_autora"
            );
        }
        if(mode == 2){
            String query = "SELECT Imie, Nazwisko, Kraj_pochodzenia " +
                    "FROM Autor ORDER BY ";
            if(sort_asc != "" && sort_desc != ""){
                query += sort_asc + " ASC, ";
                query += sort_desc + " DESC";
            }
            else if(sort_asc != "") query += sort_asc + " ASC";
            else if(sort_desc != "") query += sort_desc + " DESC";

            rs = dataBase.getStmt().executeQuery(query);
        }
        while(rs.next()){
            Vector<String> vString = new Vector<String>();
            vString.add(rs.getString(1));
            vString.add(rs.getString(2));
            vString.add(rs.getString(3));
            data.add(vString);
        }
        rs.close();
        dataBase.getStmt().close();
    }
    private void createTable(int mode) throws SQLException {
        getAuthorList(mode);
        tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(600, 300));
        table.setFillsViewportHeight(true);
        table.changeSelection(0,0, false, false);
        listScroller = new JScrollPane(table);
        listScroller.setViewportView(table);
    }
    private void components() throws SQLException {
        name_asc = new JCheckBox("Imię - rosnąco");
        name_desc = new JCheckBox("Imię - malejąco");
        surname_asc = new JCheckBox("Nazwisko - rosnąco");
        surname_desc = new JCheckBox("Nazwisko - malejąco");
        country_asc = new JCheckBox("Kraj - rosnąco");
        country_desc = new JCheckBox("Kraj - malejąco");
        name_asc = windowMethods.setSortCheckBox(name_asc, name_desc);
        name_desc = windowMethods.setSortCheckBox(name_desc, name_asc);
        surname_asc = windowMethods.setSortCheckBox(surname_asc, surname_desc);
        surname_desc = windowMethods.setSortCheckBox(surname_desc, surname_asc);
        country_asc = windowMethods.setSortCheckBox(country_asc, country_desc);
        country_desc = windowMethods.setSortCheckBox(country_desc, country_asc);

        empty = new JLabel("");
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
        filter = new JButton("Sortuj");
        filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //sortuj
                sort_asc = "";
                sort_desc = "";
                if(name_asc.isSelected()) sort_asc += "Imie, ";
                if(surname_asc.isSelected()) sort_asc += "Nazwisko, ";
                if(country_asc.isSelected()) sort_asc += "Kraj_pochodzenia, ";
                if(sort_asc.length() != 0) sort_asc = sort_asc.substring(0, sort_asc.length() - 2);

                if(name_desc.isSelected()) sort_desc += "Imie, ";
                if(surname_desc.isSelected()) sort_desc += "Nazwisko, ";
                if(country_desc.isSelected()) sort_desc += "Kraj_pochodzenia, ";
                if(sort_desc.length() != 0) sort_desc = sort_desc.substring(0, sort_desc.length() - 2);

                try {
                    createTable(2);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                listScroller.revalidate();
                listScroller.repaint();
                windowMethods.window.repaint();
            }
        });
        products = new JButton("Zobacz produkty");
        products.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String autor = data.get(table.getSelectedRow()).get(0) + " " + data.get(table.getSelectedRow()).get(1);
                Products_customer pc = new Products_customer();

                try {
                    pc.create(user , dataBase , cart);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                pc.showAuthorsProducts(autor);
                windowMethods.exit();

            }
        });
        columnNames.add("Imię");
        columnNames.add("Nazwisko");
        columnNames.add("Kraj pochodzenia");
        createTable(1);

    }
    private void panels() throws SQLException {
        components();

        up = new JPanel();
        up.setLayout(new GridLayout(2,4));
        up.add(name_asc);
        up.add(name_desc);
        up.add(surname_asc);
        up.add(surname_desc);
        up.add(country_asc);
        up.add(country_desc);
        up.add(empty);
        up.add(filter);

        center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.add(listScroller);

        down = new JPanel();
        down.setLayout(new BorderLayout());
        down.add(back, BorderLayout.WEST);
        down.add(products,BorderLayout.EAST);
    }
    private void add_components() throws SQLException {
        panels();
        windowMethods.window.add(up, BorderLayout.NORTH);
        windowMethods.window.add(center, BorderLayout.CENTER);
        windowMethods.window.add(down, BorderLayout.SOUTH);
    }
}
