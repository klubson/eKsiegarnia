package views.employee.author_panels;

import models.WindowMethods;
import models.dataBaseConnection;
import views.employee.manager.Manager_panel;
import views.employee.publisher_panels.Publishers;
import views.employee.supplier.Employee_panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Authors {
    private WindowMethods windowMethods = new WindowMethods();
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
    private boolean isManager;

    public void create(String data, boolean mode) throws SQLException {
        windowMethods.window = new JFrame("Autorzy");
        windowMethods.settings();
        user = data;
        isManager = mode;
        add_components();
        windowMethods.window.setVisible(true);
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
        id_asc = new JCheckBox("ID - rosnąco");
        id_desc = new JCheckBox("ID - malejąco");
        name_asc = new JCheckBox("Imię - rosnąco");
        name_desc = new JCheckBox("Imię - malejąco");
        surname_asc = new JCheckBox("Nazwisko - rosnąco");
        surname_desc = new JCheckBox("Nazwisko - malejąco");
        country_asc = new JCheckBox("Kraj - rosnąco");
        country_desc = new JCheckBox("Kraj - malejąco");
        id_asc = windowMethods.setSortCheckBox(id_asc, id_desc);
        id_desc = windowMethods.setSortCheckBox(id_desc, id_asc);
        name_asc = windowMethods.setSortCheckBox(name_asc, name_desc);
        name_desc = windowMethods.setSortCheckBox(name_desc, name_asc);
        surname_asc = windowMethods.setSortCheckBox(surname_asc, surname_desc);
        surname_desc = windowMethods.setSortCheckBox(surname_desc, surname_asc);
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
        add = new JButton("Dodaj autora");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //dodawanie autora
                Add_author aa = new Add_author();
                windowMethods.exit();
                aa.create(user, isManager);
            }
        });
        edit = new JButton("Edytuj");
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //edytowanie autora
                Edit_author ea = new Edit_author();
                windowMethods.exit();
                try {
                    ea.create(user, Integer.parseInt(data.get(table.getSelectedRow()).get(0)), isManager);
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
                try {
                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog (null,
                            "Czy na pewno chcesz usunąć autora " +
                                    data.get(table.getSelectedRow()).get(1) + " " + data.get(table.getSelectedRow()).get(2) +"?",
                            "Ostrzeżenie!",dialogButton);
                    if(dialogResult == JOptionPane.YES_OPTION){
                        dataBase.setStmt();
                        ResultSet rs = dataBase.getStmt().executeQuery(
                                "SELECT Autor_ID_Autora FROM Autor_produktu WHERE Autor_ID_Autora = " + data.get(table.getSelectedRow()).get(0)
                        );
                        if(rs.next()){
                            JOptionPane.showMessageDialog(windowMethods.window, "Nie można usunąć autora kiedy jest powiązany z produktami! Najpierw" +
                                    " usuń odpowiednie produkty!", "Błąd", JOptionPane.ERROR_MESSAGE);
                        }
                        else{
                            dataBase.getConn().setAutoCommit(true);
                            int changes = dataBase.getStmt().executeUpdate(
                                    "DELETE FROM Autor WHERE ID_Autora = " +
                                            data.get(table.getSelectedRow()).get(0)
                            );
                            System.out.println("Usunięto "+ changes + " rekord");
                            JOptionPane.showMessageDialog(windowMethods.window, "Autor usunięty pomyślnie!");
                            Authors as = new Authors();
                            windowMethods.exit();
                            as.create(user, isManager);
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
                windowMethods.window.repaint();
            }
        });
        columnNames.add("ID autora");
        columnNames.add("Imię");
        columnNames.add("Nazwisko");
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
        windowMethods.window.add(up, BorderLayout.NORTH);
        windowMethods.window.add(center, BorderLayout.CENTER);
        windowMethods.window.add(down, BorderLayout.SOUTH);
    }
}
