package views.employee.series_panels;

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

public class Series {
    private WindowMethods windowMethods = new WindowMethods();
    private JButton back, add, edit, delete, filter;
    private JCheckBox title_asc, title_desc, tomes_asc, tomes_desc;
    private JPanel up, center, down, down_center;
    private JLabel empty;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private DefaultTableModel tableModel = new DefaultTableModel();
    private JScrollPane listScroller;
    private String sort_asc, sort_desc, user;
    private Vector<Vector<String>> data = new Vector<Vector<String>>();
    private Vector<String> columnNames = new Vector<String>();
    private JTable table;
    private boolean isManager;

    public void create(String data, boolean mode) throws SQLException {
        windowMethods.window = new JFrame("Serie książek");
        windowMethods.settings();
        windowMethods.window.setSize(620, 600);
        user = data;
        isManager = mode;
        add_components();
        windowMethods.window.setVisible(true);
        //printDebugData(table);
    }
    private void getSeriesList(int mode) throws SQLException {
        data.clear();
        dataBase.setStmt();
        dataBase.getConn().setAutoCommit(true);
        ResultSet rs = null;
        if(sort_asc == "" && sort_desc == "") mode = 1;
        if(mode == 1) {
            rs = dataBase.getStmt().executeQuery(
                    "SELECT Tytul, Liczba_tomow FROM Seria ORDER BY Tytul"
            );
        }
        if(mode == 2){
            String query = "SELECT Tytul, Liczba_tomow FROM Seria ORDER BY ";
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
            vString.add(Integer.toString(rs.getInt(2)));
            data.add(vString);
        }
        rs.close();
        dataBase.getStmt().close();
    }
    private void createTable(int mode) throws SQLException {
        getSeriesList(mode);
        tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(600, 300));
        table.setFillsViewportHeight(true);
        table.changeSelection(0,0, false, false);
        listScroller = new JScrollPane(table);
        listScroller.setViewportView(table);
    }
    private void components() throws SQLException {
        empty = new JLabel("");
        title_asc = new JCheckBox("Tytuł - rosnąco");
        title_desc = new JCheckBox("Tytuł - malejąco");
        tomes_asc = new JCheckBox("Liczba tomów - rosnąco");
        tomes_desc = new JCheckBox("Liczba tomów - malejąco");
        title_asc = windowMethods.setSortCheckBox(title_asc, title_desc);
        title_desc = windowMethods.setSortCheckBox(title_desc, title_asc);
        tomes_asc = windowMethods.setSortCheckBox(tomes_asc, tomes_desc);
        tomes_desc = windowMethods.setSortCheckBox(tomes_desc, tomes_asc);
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isManager){
                    Manager_panel mp = new Manager_panel();
                    exit();
                    try {
                        mp.create(user);
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
        add = new JButton("Dodaj serię");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Add_series as = new Add_series();
                exit();
                as.create(user, isManager);
            }
        });
        edit = new JButton("Edytuj");
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Edit_series es = new Edit_series();
                exit();
                es.create(user, data.get(table.getSelectedRow()).get(0), isManager);
            }
        });
        delete = new JButton("Usuń");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog (null,
                        "Czy na pewno chcesz usunąć serię " +
                                data.get(table.getSelectedRow()).get(0) + "?",
                        "Ostrzeżenie!",dialogButton);
                if(dialogResult == JOptionPane.YES_OPTION){
                    try {
                        dataBase.setStmt();
                        dataBase.getConn().setAutoCommit(false);
                        int changes = dataBase.getStmt().executeUpdate(
                                "DELETE FROM Seria WHERE Tytul = '" +
                                        data.get(table.getSelectedRow()).get(0) + "'"
                        );
                        System.out.println("Usunięto "+ changes + " rekord");
                        JOptionPane.showMessageDialog(windowMethods.window, "Seria usunięta pomyślnie!");
                        dataBase.getStmt().close();
                        dataBase.getConn().setAutoCommit(true);
                        Series ss = new Series();
                        exit();
                        ss.create(user, isManager);
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
                if(title_asc.isSelected()) sort_asc += "Tytul, ";
                if(tomes_asc.isSelected()) sort_asc += "Liczba_tomow, ";
                if(sort_asc.length() != 0) sort_asc = sort_asc.substring(0, sort_asc.length()-2);
                //System.out.println(sort_asc);
                if(title_desc.isSelected()) sort_desc += "Tytul, ";
                if(tomes_desc.isSelected()) sort_desc += "Liczba_tomow, ";
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
        columnNames.add("Tytuł serii");
        columnNames.add("Liczba tomów");
        createTable(1);
    }
    private void panels() throws SQLException {
        components();
        up = new JPanel();
        up.setLayout(new GridLayout(2,3));
        up.add(title_asc);
        up.add(title_desc);
        up.add(tomes_asc);
        up.add(tomes_desc);
        up.add(empty);
        up.add(filter);

        center = new JPanel();
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
    private void exit(){
        windowMethods.window.setVisible(false);
        windowMethods.window.dispose();
    }
}
