package views.employee.product_panels;

import models.WindowMethods;
import models.dataBaseConnection;
import views.employee.manager.Manager_panel;
import views.employee.supplier.Employee_panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Products {
    private WindowMethods windowMethods = new WindowMethods();
    private JLabel empty;
    private JCheckBox id_asc, id_desc, name_asc, name_desc, price_asc, price_desc, year_asc, year_desc;
    private JPanel up, center, down, down_center;
    private JButton add, back, edit, filter, delete;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private DefaultTableModel tableModel = new DefaultTableModel();
    private JScrollPane listScroller;
    private String sort_asc, sort_desc, user, productType;
    private JTable table;
    private Vector<Vector<String>> data = new Vector<Vector<String>>();
    private Vector<String> columnNames = new Vector<String>();
    private boolean isManager;

    public void create(String data, boolean mode) throws SQLException {
        windowMethods.window = new JFrame("Produkty");
        windowMethods.settings();
        user = data;
        isManager = mode;
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
                    "SELECT ID_Produktu, Nazwa, Cena, Rok_wydania, Stan_magazyn, co, Wydawnictwo_ID_wydawnictwa FROM Produkt ORDER BY ID_Produktu"
            );
        }
        if(mode == 2){
            String query = "SELECT ID_Produktu, Nazwa, Cena, Rok_wydania, Stan_magazyn, co, Wydawnictwo_ID_wydawnictwa FROM Produkt ORDER BY ";
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
            vString.add(Integer.toString(rs.getInt(1)));
            vString.add(rs.getString(2));
            vString.add(Float.toString(rs.getFloat(3)));
            vString.add(rs.getString(4));
            vString.add(Integer.toString(rs.getInt(5)));
            String tmp = rs.getString(6);
            if(tmp.equals("g")){
                vString.add("gra planszowa");
            }
            else if(tmp.equals("k")){
                vString.add("książka");
            }
            vString.add(Integer.toString(rs.getInt(7)));
            data.add(vString);
        }
        rs.close();
        dataBase.getStmt().close();
    }
    private void createTable(int mode) throws SQLException {
        getProductList(mode);
        tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(600, 300));
        table.setFillsViewportHeight(true);
        table.changeSelection(0,0, false, false);
        table.removeColumn(table.getColumnModel().getColumn(0));
        listScroller = new JScrollPane(table);
        listScroller.setViewportView(table);
    }
    private void components() throws SQLException {
        id_asc = new JCheckBox("ID - rosnąco");
        id_desc = new JCheckBox("ID - malejąco");
        name_asc = new JCheckBox("Nazwa - rosnąco");
        name_desc = new JCheckBox("Nazwa - malejąco");
        price_asc = new JCheckBox("Cena - rosnąco");
        price_desc = new JCheckBox("Cena - malejąco");
        year_asc = new JCheckBox("Rok wyd. - rosnąco");
        year_desc = new JCheckBox("Rok wyd. - malejąco");
        id_asc = windowMethods.setSortCheckBox(id_asc, id_desc);
        id_desc = windowMethods.setSortCheckBox(id_desc, id_asc);
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
                if(isManager){
                    Manager_panel mg = new Manager_panel();
                    windowMethods.exit();
                    try {
                        mg.create(user);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                else{
                    Employee_panel ep = new Employee_panel();
                    windowMethods.exit();
                    try {
                        ep.create(user);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }

            }
        });
        add = new JButton("Dodaj produkt");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Add_product ap = new Add_product();
                windowMethods.exit();
                try {
                    ap.create(user, isManager);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        edit = new JButton("Edytuj");
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productType = data.get(table.getSelectedRow()).get(5);
                if(productType.equals("książka")){
                    Edit_book eb = new Edit_book();
                    windowMethods.exit();
                    try {
                        eb.create(user, isManager, Integer.parseInt(data.get(table.getSelectedRow()).get(0)));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                else if(productType.equals("gra planszowa")){
                    Edit_game eg = new Edit_game();
                    windowMethods.exit();
                    try {
                        eg.create(user, isManager, Integer.parseInt(data.get(table.getSelectedRow()).get(0)));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });
        delete = new JButton("Usuń");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //usuwanie produktu
                boolean ReadyToDelete = true;
                int id = Integer.parseInt(data.get(table.getSelectedRow()).get(0));
                String co = data.get(table.getSelectedRow()).get(5);
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog (null,
                        "Czy na pewno chcesz usunąć produkt " +
                                data.get(table.getSelectedRow()).get(1) +"?",
                        "Ostrzeżenie!",dialogButton);
                if(dialogResult == JOptionPane.YES_OPTION){
                    try {
                        dataBase.setStmt();
                        ResultSet rs = dataBase.getStmt().executeQuery(
                                "SELECT DISTINCT PRODUKT_ID_PRODUKTU FROM Element_koszyka ORDER BY produkt_id_produktu"
                        );
                        while (rs.next()){
                            if(rs.getInt(1) == id) ReadyToDelete = false;
                        }
                        rs.close();
                        if(ReadyToDelete){
                            dataBase.getStmt().executeUpdate(
                                    "DELETE FROM Autor_produktu WHERE Produkt_ID_produktu = " + id
                            );
                            if (co.equals("książka")){
                                dataBase.getStmt().executeUpdate(
                                        "DELETE FROM Ksiazka WHERE ID_produktu = " + id
                                );
                            }
                            else if(co.equals("gra planszowa")){
                                dataBase.getStmt().executeUpdate(
                                        "DELETE FROM Gra_planszowa WHERE ID_produktu = " + id
                                );
                            }
                            dataBase.getStmt().executeUpdate(
                                    "DELETE FROM Produkt WHERE ID_produktu = " + id
                            );
                            System.out.println("Usunięto 1 rekord");
                        }
                        else{
                            dataBase.getStmt().executeUpdate(
                                    "DELETE FROM Autor_produktu WHERE Produkt_ID_produktu = " + id
                            );
                            dataBase.getStmt().executeUpdate(
                                    "UPDATE Produkt SET Stan_magazyn = 0 WHERE ID_produktu = " + id
                            );
                            System.out.println("Usunięto 1 rekord");
                        }
                        dataBase.getStmt().close();
                        Products ps = new Products();
                        windowMethods.exit();
                        ps.create(user, isManager);

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });
        filter = new JButton("Sortuj");
        filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //sortowanie
                sort_asc = "";
                sort_desc = "";
                if(id_asc.isSelected()) sort_asc += "ID_produktu, ";
                if(name_asc.isSelected()) sort_asc += "Nazwa, ";
                if(price_asc.isSelected()) sort_asc += "Cena, ";
                if(year_asc.isSelected()) sort_asc += "Rok_wydania, ";
                if(sort_asc.length() != 0) sort_asc = sort_asc.substring(0, sort_asc.length()-2);
                //System.out.println(sort_asc);
                if(id_desc.isSelected()) sort_desc += "ID_produktu, ";
                if(name_desc.isSelected()) sort_desc += "Nazwa, ";
                if(price_desc.isSelected()) sort_desc += "Cena, ";
                if(year_desc.isSelected()) sort_desc += "Rok_wydania, ";
                if(sort_desc.length() != 0) sort_desc = sort_desc.substring(0, sort_desc.length()-2);
                //System.out.println(sort_desc);
                try {
                    getProductList(2);
                    tableModel.setDataVector(data,columnNames);
                    table.removeColumn(table.getColumnModel().getColumn(0));
                    //createTable(2);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                //printDebugData(table);
                listScroller.revalidate();
                listScroller.repaint();
                windowMethods.window.repaint();
            }
        });
        columnNames.add("ID produktu");
        columnNames.add("Nazwa");
        columnNames.add("Cena");
        columnNames.add("Rok wydania");
        columnNames.add("Stan magazynu");
        columnNames.add("Typ produktu");
        columnNames.add("ID wydawnictwa");
        createTable(1);
    }
    private void panels() throws SQLException {
        components();

        up = new JPanel();
        up.setLayout(new GridLayout(4,4));
        //up.add(id_asc);
        //up.add(id_desc);
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

        down_center = new JPanel();
        down_center.add(add);
        down_center.add(edit);

        down = new JPanel();
        down.setLayout(new BorderLayout());
        down.add(back, BorderLayout.WEST);
        down.add(down_center, BorderLayout.CENTER);
        down.add(delete, BorderLayout.EAST);
    }
    private void add_components() throws SQLException {
        panels();
        windowMethods.window.add(up, BorderLayout.NORTH);
        windowMethods.window.add(center, BorderLayout.CENTER);
        windowMethods.window.add(down, BorderLayout.SOUTH);
    }
}
