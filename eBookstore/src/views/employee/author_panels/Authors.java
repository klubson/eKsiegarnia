package views.employee.author_panels;

import models.dataBaseConnection;
import views.employee.manager.Manager_panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Authors extends JFrame {
    private JFrame window;
    private JCheckBox id_asc, id_desc, name_asc, name_desc, surname_asc, surname_desc, country_asc, country_desc;
    private JButton back, add, edit, delete, filter;
    private JPanel up, center, down_center, down;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private DefaultTableModel tableModel = new DefaultTableModel();
    private JScrollPane listScroller;
    private String user, sort_asc, sort_desc;
    private Vector<Vector<String>> data = new Vector<Vector<String>>();
    private Vector<String> columnNames = new Vector<String>();
    private JTable table;

    public void create(String data) throws SQLException {
        window = new JFrame("Autorzy");
        settings();
        user = data;
        add_components();
        window.setVisible(true);
    }
    private void settings(){
        window.setSize(600, 600);
        window.setLocation(400, 80);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
    }
    private void getAuthorList(int mode) throws SQLException {
        data.clear();
        dataBase.setStmt();
        dataBase.getConn().setAutoCommit(true);
        ResultSet rs = null;
        if(sort_asc == "" && sort_desc == "") mode = 1;
        if(mode == 1) {
            rs = dataBase.getStmt().executeQuery(
                    "SELECT ID_autora, Imie, Nazwisko, Kraj_pochodzenia" +
                            " FROM Autor ORDER BY ID_autora"
            );
        }
        if(mode == 2){
            String query = "SELECT ID_autora, Imie, Nazwisko, Kraj_pochodzenia " +
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
            vString.add(Integer.toString(rs.getInt(1)));
            vString.add(rs.getString(2));
            vString.add(rs.getString(3));
            vString.add(rs.getString(4));
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
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Manager_panel mg = new Manager_panel();
                exit();
                try {
                    mg.create(user);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        add = new JButton("Dodaj autora");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //dodawanie autora
                Add_author aa = new Add_author();
                exit();
                aa.create(user);
            }
        });
        edit = new JButton("Edytuj");
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //edytowanie autora
                Edit_author ea = new Edit_author();
                exit();
                try {
                    ea.create(user, Integer.parseInt(data.get(table.getSelectedRow()).get(0)));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        delete = new JButton("Usuń");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //usuń
            }
        });
        filter = new JButton("Sortuj");
        filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //sortuj
                sort_asc = "";
                sort_desc = "";
                if(id_asc.isSelected()) sort_asc += "ID_autora, ";
                if(name_asc.isSelected()) sort_asc += "Imie, ";
                if(surname_asc.isSelected()) sort_asc += "Nazwisko, ";
                if(country_asc.isSelected()) sort_asc += "Kraj_pochodzenia, ";
                if(sort_asc.length() != 0) sort_asc = sort_asc.substring(0, sort_asc.length() - 2);

                if(id_desc.isSelected()) sort_desc += "ID_autora, ";
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
                window.repaint();
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
        name_asc = new JCheckBox("Imię - rosnąco");
        name_asc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(name_asc.isSelected()){
                    name_desc.setSelected(false);
                }
            }
        });
        name_desc = new JCheckBox("Imię - malejąco");
        name_desc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(name_desc.isSelected()){
                    name_asc.setSelected(false);
                }
            }
        });
        surname_asc = new JCheckBox("Nazwisko - rosnąco");
        surname_asc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(surname_asc.isSelected()){
                    surname_desc.setSelected(false);
                }
            }
        });
        surname_desc = new JCheckBox("Nazwisko - malejąco");
        surname_desc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(surname_desc.isSelected()){
                    surname_asc.setSelected(false);
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

        columnNames.add("ID autora");
        columnNames.add("Nazwisko");
        columnNames.add("Imię");
        columnNames.add("Kraj pochodzenia");
        createTable(1);

    }
    private void panels() throws SQLException {
        components();

        up = new JPanel();
        up.setLayout(new GridLayout(3,3));
        up.add(id_asc);
        up.add(id_desc);
        up.add(name_asc);
        up.add(name_desc);
        up.add(surname_asc);
        up.add(surname_desc);
        up.add(country_asc);
        up.add(country_desc);
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
}
