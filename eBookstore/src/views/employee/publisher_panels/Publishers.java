package views.employee.publisher_panels;

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

public class Publishers {
    private WindowMethods windowMethods = new WindowMethods();
    private JCheckBox id_asc, id_desc, name_asc, name_desc, country_asc, country_desc;
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
        windowMethods.window = new JFrame("Wydawnictwa");
        windowMethods.settings();
        user = data;
        add_components();
        isManager = mode;
        windowMethods.window.setVisible(true);
        //printDebugData(table);
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
        table.removeColumn(table.getColumnModel().getColumn(0));
        listScroller = new JScrollPane(table);
        listScroller.setViewportView(table);
    }
    private void components() throws SQLException {
        id_asc = new JCheckBox("ID - rosnąco");
        id_desc = new JCheckBox("ID - malejąco");
        name_asc = new JCheckBox("Nazwa - rosnąco");
        name_desc = new JCheckBox("Nazwa - malejąco");
        country_asc = new JCheckBox("Kraj - rosnąco");
        country_desc = new JCheckBox("Kraj - malejąco");
        id_asc = windowMethods.setSortCheckBox(id_asc, id_desc);
        id_desc = windowMethods.setSortCheckBox(id_desc, id_asc);
        name_asc = windowMethods.setSortCheckBox(name_asc, name_desc);
        name_desc = windowMethods.setSortCheckBox(name_desc, name_asc);
        country_asc = windowMethods.setSortCheckBox(country_asc, country_desc);
        country_desc = windowMethods.setSortCheckBox(country_desc, country_asc);
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
        add = new JButton("Dodaj wydawnictwo");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Add_publisher ap = new Add_publisher();
                windowMethods.exit();
                ap.create(user, isManager);
            }
        });
        edit = new JButton("Edytuj wydawnictwo");
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(table.getSelectedRow() != -1){
                    Edit_publisher ep = new Edit_publisher();
                    windowMethods.exit();
                    try {
                        ep.create(user, Integer.parseInt(data.get(table.getSelectedRow()).get(0)), isManager);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }

            }
        });
        delete = new JButton("Usuń wydawnictwo");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(table.getSelectedRow() != -1){
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
                                JOptionPane.showMessageDialog(windowMethods.window, "Nie można usunąć wydawnictwa kiedy jest powiązane z produktami! Najpierw" +
                                        " usuń odpowiednie produkty!", "Błąd", JOptionPane.ERROR_MESSAGE);
                            }
                            else{
                                dataBase.getConn().setAutoCommit(true);
                                int changes = dataBase.getStmt().executeUpdate(
                                        "DELETE FROM Wydawnictwo WHERE ID_Wydawnictwa = " +
                                                data.get(table.getSelectedRow()).get(0)
                                );
                                System.out.println("Usunięto "+ changes + " rekord");
                                JOptionPane.showMessageDialog(windowMethods.window, "Wydawnictwo usunięte pomyślnie!");
                                Publishers ps = new Publishers();
                                windowMethods.exit();
                                ps.create(user, isManager);
                            }
                        }
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
                    getPublisherList(2);
                    tableModel.setDataVector(data,columnNames);
                    table.removeColumn(table.getColumnModel().getColumn(0));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                //printDebugData(table);
                listScroller.revalidate();
                listScroller.repaint();
                windowMethods.window.repaint();
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
        //up.add(id_asc);
        //up.add(id_desc);
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
        windowMethods.window.add(up, BorderLayout.NORTH);
        windowMethods.window.add(center, BorderLayout.CENTER);
        windowMethods.window.add(down, BorderLayout.SOUTH);
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
