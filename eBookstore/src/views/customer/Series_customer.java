package views.customer;

import models.dataBaseConnection;
import views.employee.manager.Manager_panel;
import views.employee.series_panels.Add_series;
import views.employee.series_panels.Edit_series;
import views.employee.series_panels.Series;
import views.employee.supplier.Employee_panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Series_customer extends JFrame {
    private JFrame window;
    private JButton back, filter;
    private JCheckBox title_asc, title_desc, tomes_asc, tomes_desc;
    private JPanel up, center, down;
    private JLabel empty;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private DefaultTableModel tableModel = new DefaultTableModel();
    private JScrollPane listScroller;
    private String sort_asc, sort_desc, user;
    private Vector<Vector<String>> data = new Vector<Vector<String>>();
    private Vector<String> columnNames = new Vector<String>();
    private JTable table;

    public void create(String data) throws SQLException {
        window = new JFrame("Serie książek");
        settings();
        user = data;
        add_components();
        window.setVisible(true);
    }
    private void settings(){
        window.setSize(620, 600);
        window.setLocation(400, 80);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
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
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer_panel cp = new Customer_panel();
                exit();
                try {
                    cp.create(user);
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
                if(title_asc.isSelected()) sort_asc += "ID_wydawnictwa, ";
                if(tomes_asc.isSelected()) sort_asc += "Nazwa, ";
                if(sort_asc.length() != 0) sort_asc = sort_asc.substring(0, sort_asc.length()-2);
                //System.out.println(sort_asc);
                if(title_desc.isSelected()) sort_desc += "ID_wydawnictwa, ";
                if(tomes_desc.isSelected()) sort_desc += "Nazwa, ";
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
        title_asc = new JCheckBox("Tytuł - rosnąco");
        title_asc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(title_asc.isSelected()){
                    title_desc.setSelected(false);
                }
            }
        });
        title_desc = new JCheckBox("Tytuł - malejąco");
        title_desc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(title_desc.isSelected()){
                    title_asc.setSelected(false);
                }
            }
        });
        tomes_asc = new JCheckBox("Liczba tomów - rosnąco");
        tomes_asc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tomes_asc.isSelected()){
                    title_desc.setSelected(false);
                }
            }
        });
        tomes_desc = new JCheckBox("Liczba tomów - malejąco");
        tomes_desc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tomes_desc.isSelected()){
                    tomes_asc.setSelected(false);
                }
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
        down = new JPanel();
        down.setLayout(new BorderLayout());
        down.add(back, BorderLayout.WEST);
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
}
