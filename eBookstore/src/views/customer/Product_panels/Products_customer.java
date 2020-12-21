package views.customer.Product_panels;

import models.CartInfo;
import models.WindowMethods;
import models.dataBaseConnection;
import views.customer.Customer_panel;
import views.employee.manager.Manager_panel;
import views.employee.product_panels.Add_product;
import views.employee.supplier.Employee_panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Products_customer {
    private WindowMethods windowMethods = new WindowMethods();
    private JLabel empty;
    private JCheckBox name_asc, name_desc, price_asc, price_desc, year_asc, year_desc;
    private JPanel up, center, down;
    private JButton details, back, filter, addToCart;
    private dataBaseConnection dataBase = null;//new dataBaseConnection();
    private DefaultTableModel tableModel = new DefaultTableModel();
    private JScrollPane listScroller;
    private String sort_asc, sort_desc, user;
    private JTable table;
    private Vector<Vector<String>> data = new Vector<Vector<String>>();
    private Vector<String> columnNames = new Vector<String>();
    private CartInfo cart;

    public void create(String data , dataBaseConnection dataBase , CartInfo cart) throws SQLException {
        this.dataBase = dataBase;

        this.cart = cart;
        windowMethods.window = new JFrame("Produkty");
        windowMethods.settings();
        windowMethods.window.setLocation(200, 80);
        user = data;
        add_components();
        windowMethods.window.setVisible(true);

    }
    private void getProductList(int mode) throws SQLException {
        data.clear();
        dataBase.setStmt();
        ResultSet rs = null;
        if(sort_asc == "" && sort_desc == "") mode = 1;
        if(mode == 1) {
            rs = dataBase.getStmt().executeQuery(
                    "SELECT Nazwa, Cena, Rok_wydania, Stan_magazyn, co ,id_produktu  FROM Produkt ORDER BY ID_Produktu"
            );
        }
        if(mode == 2){
            String query = "SELECT Nazwa, Cena, Rok_wydania, Stan_magazyn, co,id_produktu FROM Produkt ORDER BY ";
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
            vString.add(Float.toString(rs.getFloat(2)));
            vString.add(rs.getString(3));
            vString.add(Integer.toString(rs.getInt(4)));
            String tmp = rs.getString(5);
            if(tmp.equals("g")){
                vString.add("gra planszowa");
            }
            else if(tmp.equals("k")){
                vString.add("książka");
            }
            vString.add(Integer.toString(rs.getInt(6)));
            data.add(vString);
        }
        rs.close();
        dataBase.getStmt().close();
    }
    public void createTable(int mode) throws SQLException {
        getProductList(mode);
        tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel);
        table.removeColumn(table.getColumnModel().getColumn(5));
        table.setPreferredScrollableViewportSize(new Dimension(600, 300));
        table.setFillsViewportHeight(true);
        table.changeSelection(0,0, false, false);
        listScroller = new JScrollPane(table);
        listScroller.setViewportView(table);
    }
    private void components() throws SQLException {
        name_asc = new JCheckBox("Nazwa - rosnąco");
        name_desc = new JCheckBox("Nazwa - malejąco");
        price_asc = new JCheckBox("Cena - rosnąco");
        price_desc = new JCheckBox("Cena - malejąco");
        year_asc = new JCheckBox("Rok wyd. - rosnąco");
        year_desc = new JCheckBox("Rok wyd. - malejąco");
        name_asc = windowMethods.setSortCheckBox(name_asc, name_desc);
        name_desc = windowMethods.setSortCheckBox(name_desc, name_asc);
        price_asc = windowMethods.setSortCheckBox(price_asc, price_desc);
        price_desc = windowMethods.setSortCheckBox(price_desc, price_asc);
        year_asc = windowMethods.setSortCheckBox(year_asc, year_desc);
        year_desc = windowMethods.setSortCheckBox(year_desc, year_asc);
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
        details = new JButton("Szczegóły");
        details.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dataBase.setStmt();
                    ResultSet rs = dataBase.getStmt().executeQuery(
                            "SELECT ID_produktu, co FROM Produkt WHERE ID_produktu = " + Integer.parseInt((String) table.getModel().getValueAt(table.getSelectedRow(), 5 ))

                    );
                    rs.next();
                    int id = rs.getInt(1);
                    String type = rs.getString(2);
                    rs.close();
                    dataBase.getStmt().close();
                    if(type.equals("k")){
                        Book_details bd = new Book_details();
                        bd.create(id , dataBase , cart);
                    }
                    else if(type.equals("g")){
                        Game_details gd = new Game_details();
                        gd.create(id , dataBase , cart);
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        addToCart = new JButton("Dodaj do koszyka");
        addToCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int id = Integer.parseInt((String) table.getModel().getValueAt(table.getSelectedRow(), 5 ));
                double price = Double.parseDouble(data.get(table.getSelectedRow()).get(1));
                //dataBase.newCartItem(cart, id, price);
                new BuyPanel(dataBase,id,price,cart  );
            }
        });
        filter = new JButton("Sortuj");
        filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //sortowanie
                sort_asc = "";
                sort_desc = "";
                if(name_asc.isSelected()) sort_asc += "Nazwa, ";
                if(price_asc.isSelected()) sort_asc += "Cena, ";
                if(year_asc.isSelected()) sort_asc += "Rok_wydania, ";
                if(sort_asc.length() != 0) sort_asc = sort_asc.substring(0, sort_asc.length()-2);
                //System.out.println(sort_asc);
                if(name_desc.isSelected()) sort_desc += "Nazwa, ";
                if(price_desc.isSelected()) sort_desc += "Cena, ";
                if(year_desc.isSelected()) sort_desc += "Rok_wydania, ";
                if(sort_desc.length() != 0) sort_desc = sort_desc.substring(0, sort_desc.length()-2);
                //System.out.println(sort_desc);
                try {
                    createTable(2);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                //printDebugData(table);
                listScroller.revalidate();
                listScroller.repaint();
                windowMethods.window.repaint();

            }
        });
        columnNames.add("Nazwa");
        columnNames.add("Cena");
        columnNames.add("Rok wydania");
        columnNames.add("Dostępność");
        columnNames.add("Typ produktu");
        columnNames.add("Id_produktu");

        createTable(1);
    }
    private void panels() throws SQLException {
        components();

        up = new JPanel();
        up.setLayout(new GridLayout(2,4));
        up.add(name_asc);
        up.add(name_desc);
        up.add(price_asc);
        up.add(price_desc);
        up.add(year_asc);
        up.add(year_desc);
        up.add(empty);
        up.add(filter);

        center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.add(listScroller);

        down = new JPanel();
        down.setLayout(new BorderLayout());
        down.add(back, BorderLayout.WEST);
        down.add(details, BorderLayout.CENTER);
        down.add(addToCart, BorderLayout.EAST);
    }
    private void add_components() throws SQLException {
        panels();
        windowMethods.window.add(up, BorderLayout.NORTH);
        windowMethods.window.add(center, BorderLayout.CENTER);
        windowMethods.window.add(down, BorderLayout.SOUTH);
    }
}
