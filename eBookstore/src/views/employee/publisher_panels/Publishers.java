package views.employee.publisher_panels;

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

public class Publishers extends JFrame {
    private JFrame window;
    private JCheckBox name_asc, name_desc, id_asc, id_desc, country_asc, country_desc;
    private JPanel up, center, down, down_center;
    private JButton add, back, edit, filter, delete;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private DefaultTableModel tableModel = new DefaultTableModel();
    private JScrollPane listScroller;
    private String sort_asc, sort_desc, user;
    private Vector<Vector<String>> data = new Vector<Vector<String>>();
    private Vector<String> columnNames = new Vector<String>();
    private JTable table;
    private boolean isManager;


    public void create(String data, boolean mode) throws SQLException {
        window = new JFrame("Wydawnictwa");
        settings();
        user = data;
        add_components();
        isManager = mode;
        window.setVisible(true);
        //printDebugData(table);
    }
    private void settings(){
        window.setSize(600, 600);
        window.setLocation(400, 80);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
    }
    private void getPublisherList(int mode) throws SQLException {
        data.clear();
        dataBase.setStmt();
        dataBase.getConn().setAutoCommit(true);
        ResultSet rs = null;
        if(sort_asc == "" && sort_desc == "") mode = 1;
        if(mode == 1) {
            rs = dataBase.getStmt().executeQuery(
                    "SELECT ID_wydawnictwa, Nazwa, Kraj_pochodzenia FROM Wydawnictwo ORDER BY ID_wydawnictwa"
            );
        }
        if(mode == 2){
            String query = "SELECT ID_wydawnictwa, Nazwa, Kraj_pochodzenia FROM Wydawnictwo ORDER BY ";
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
            vString.add(rs.getString(3));
            data.add(vString);
        }
        rs.close();
        dataBase.getStmt().close();
    }
    private void createTable(int mode) throws SQLException {
        getPublisherList(mode);
        tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(600, 300));
        table.setFillsViewportHeight(true);
        table.changeSelection(0,0, false, false);
        listScroller = new JScrollPane(table);
        listScroller.setViewportView(table);
    }
    private void components() throws SQLException {
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isManager){
                    Manager_panel mg = new Manager_panel();
                    exit();
                    try {
                        mg.create(user);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                else{
                    Employee_panel ep = new Employee_panel();
                    exit();
                    try {
                        ep.create(user);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }

            }
        });
        add = new JButton("Dodaj wydawnictwo");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Add_publisher ap = new Add_publisher();
                exit();
                ap.create(user, isManager);
            }
        });
        edit = new JButton("Edytuj wydawnictwo");
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Edit_publisher ep = new Edit_publisher();
                exit();
                try {
                    ep.create(user, Integer.parseInt(data.get(table.getSelectedRow()).get(0)), isManager);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        delete = new JButton("Usuń wydawnictwo");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog (null,
                            "Czy na pewno chcesz usunąć wydawnictwo " +
                                    data.get(table.getSelectedRow()).get(1) + "?",
                            "Ostrzeżenie!",dialogButton);
                    if(dialogResult == JOptionPane.YES_OPTION){
                        dataBase.setStmt();
                        ResultSet rs = dataBase.getStmt().executeQuery(
                                "SELECT Wydawnictwo_ID_wydawnictwa FROM Produkt WHERE Wydawnictwo_ID_wydawnictwa = " + data.get(table.getSelectedRow()).get(0)
                        );
                        if(rs.next()){
                            JOptionPane.showMessageDialog(window, "Nie można usunąć wydawnictwa kiedy jest powiązane z produktami! Najpierw" +
                                    " usuń odpowiednie produkty!", "Błąd", JOptionPane.ERROR_MESSAGE);
                        }
                        else{
                            dataBase.getConn().setAutoCommit(true);
                            int changes = dataBase.getStmt().executeUpdate(
                                    "DELETE FROM Wydawnictwo WHERE ID_Wydawnictwa = " +
                                            data.get(table.getSelectedRow()).get(0)
                            );
                            System.out.println("Usunięto "+ changes + " rekord");
                            JOptionPane.showMessageDialog(window, "Wydawnictwo usunięte pomyślnie!");
                            Publishers ps = new Publishers();
                            exit();
                            ps.create(user, isManager);
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        filter = new JButton("Sortuj");
        filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sort_asc = "";
                sort_desc = "";
                if(id_asc.isSelected()) sort_asc += "ID_wydawnictwa, ";
                if(name_asc.isSelected()) sort_asc += "Nazwa, ";
                if(country_asc.isSelected()) sort_asc += "Kraj_pochodzenia, ";
                if(sort_asc.length() != 0) sort_asc = sort_asc.substring(0, sort_asc.length()-2);
                //System.out.println(sort_asc);
                if(id_desc.isSelected()) sort_desc += "ID_wydawnictwa, ";
                if(name_desc.isSelected()) sort_desc += "Nazwa, ";
                if(country_desc.isSelected()) sort_desc += "Kraj_pochodzenia, ";
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
                window.repaint();

            }
        });
        name_asc = new JCheckBox("Nazwa - rosnąco");
        name_asc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(name_asc.isSelected()){
                    name_desc.setSelected(false);
                }
            }
        });
        name_desc = new JCheckBox("Nazwa - malejąco");
        name_desc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(name_desc.isSelected()){
                    name_asc.setSelected(false);
                }
            }
        });
        id_asc = new JCheckBox("ID - rosnąco");
        id_asc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(id_asc.isSelected()){
                    id_desc.setSelected(false);
                }
            }
        });
        id_desc = new JCheckBox("ID - malejąco");
        id_desc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(id_desc.isSelected()){
                    id_asc.setSelected(false);
                }
            }
        });
        country_asc = new JCheckBox("Kraj - rosnąco");
        country_asc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(country_asc.isSelected()){
                    country_desc.setSelected(false);
                }
            }
        });
        country_desc = new JCheckBox("Kraj - malejąco");
        country_desc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(country_desc.isSelected()){
                    country_asc.setSelected(false);
                }
            }
        });
        columnNames.add("ID Wydawnictwa");
        columnNames.add("Nazwa");
        columnNames.add("Kraj pochodzenia");

        createTable(1);
    }
    private void panels() throws SQLException {
        components();

        up = new JPanel();
        up.setLayout(new GridLayout(2,4));
        up.add(id_asc);
        up.add(id_desc);
        up.add(name_asc);
        up.add(name_desc);
        up.add(country_asc);
        up.add(country_desc);
        up.add(new JLabel(""));
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
        window.add(up, BorderLayout.NORTH);
        window.add(center, BorderLayout.CENTER);
        window.add(down, BorderLayout.SOUTH);
    }
    private void exit(){
        window.setVisible(false);
        window.dispose();
    }
    private void printDebugData(JTable table) {
        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        javax.swing.table.TableModel model = table.getModel();

        System.out.println("Value of data: ");
        for (int i=0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j=0; j < numCols; j++) {
                System.out.print("  " + model.getValueAt(i, j));
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }

}
