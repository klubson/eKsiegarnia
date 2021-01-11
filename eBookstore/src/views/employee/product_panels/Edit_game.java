package views.employee.product_panels;

import models.DataVerification;
import models.WindowMethods;
import models.dataBaseConnection;
import views.employee.author_panels.Add_author;
import views.employee.publisher_panels.Add_publisher;
import views.employee.series_panels.Add_series;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Edit_game {
    private WindowMethods windowMethods = new WindowMethods();
    private JButton edit, back, add_publisher, add_author;
    private JLabel name, price, year, storage, publisher, author, min_players, max_players, min_age, est_time;
    private JTextField name2, price2, year2, storage2, publisher2, min_players2, max_players2, min_age2, est_time2;
    private JPanel center, down, general_pane ,game_pane, name_pane, price_pane, year_pane,storage_pane, publisher_pane, author_pane, min_players_pane, max_players_pane, min_age_pane, est_time_pane;
    private String user;
    private int IDToEdit;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private DefaultListModel listModelPublisher = new DefaultListModel(), listModelAuthor = new DefaultListModel();
    private JList publisherList, authorList;
    private JScrollPane publisherListScroller, authorListScroller;
    private boolean isManager;

    public void create(String data, boolean mode, int product_ID) throws SQLException {
        windowMethods.window = new JFrame("Edytuj produkt");
        windowMethods.settings();
        user = data;
        isManager = mode;
        IDToEdit = product_ID;
        add_components();
        setProductData(product_ID);
        windowMethods.window.setVisible(true);
    }
    private void setProductData(int id) throws SQLException {
        dataBase.setStmt();
        ResultSet rs = dataBase.getStmt().executeQuery(
                "SELECT Nazwa, Cena, Rok_wydania, Stan_magazyn, Wydawnictwo_ID_wydawnictwa, g_Min_gracze, g_Max_gracze, g_Min_wiek, g_Czas_gry " +
                        "FROM Produkt JOIN Gra_planszowa USING(ID_produktu) WHERE ID_produktu = " + id
        );
        rs.next();
        name2.setText(rs.getString(1));
        price2.setText(Float.toString(rs.getFloat(2)));
        year2.setText(rs.getString(3));
        storage2.setText(Integer.toString(rs.getInt(4)));
        int publisherID = rs.getInt(5);
        min_players2.setText(Integer.toString(rs.getInt(6)));
        max_players2.setText(Integer.toString(rs.getInt(7)));
        min_age2.setText(Integer.toString(rs.getInt(8)));
        est_time2.setText(Integer.toString(rs.getInt(9)));
        rs.close();
        ArrayList<Integer> tmpArray = new ArrayList<Integer>();
        rs = dataBase.getStmt().executeQuery(
                "SELECT a.Imie, a.Nazwisko FROM Autor a JOIN Autor_produktu b ON a.ID_autora = b.Autor_ID_autora WHERE b.Produkt_ID_produktu = " + id
        );
        while (rs.next()){
            for(int i = 0; i < authorList.getModel().getSize(); i++){
                if(authorList.getModel().getElementAt(i).equals(rs.getString(1) + " " + rs.getString(2))){
                    tmpArray.add(i);
                }
            }
        }
        rs.close();
        if(tmpArray.size() != 0){
            int[] indices = new int[tmpArray.size()];
            for (int i = 0; i < tmpArray.size(); i++) indices[i] = tmpArray.get(i);
            authorList.setSelectedIndices(indices);
        }
        rs = dataBase.getStmt().executeQuery(
                "SELECT Nazwa FROM Wydawnictwo WHERE ID_wydawnictwa = " + publisherID
        );
        rs.next();
        for (int i = 0; i < publisherList.getModel().getSize(); i++){
            if(publisherList.getModel().getElementAt(i).equals(rs.getString(1))) publisherList.setSelectedIndex(i);
        }
        rs.close();
        dataBase.getStmt().close();
    }
    private void labels(){
        name = new JLabel("Nazwa produktu (max 50 znaków): ");
        price = new JLabel("Cena produktu: ");
        year = new JLabel("Rok wydania produktu (opcjonalnie): ");
        storage = new JLabel("Stan magazynu (max 999): ");
        publisher = new JLabel("Wydawnictwo: ");
        author = new JLabel("Autorzy: ");
        min_players = new JLabel("Minimalna liczba graczy: ");
        max_players = new JLabel("Maksymalna liczba graczy: ");
        min_age = new JLabel("Zalecany minimalny wiek (opcjonalnie): ");
        est_time = new JLabel("Szacowany czas gry (opcjonalnie): ");
    }
    private void components() throws SQLException {
        name2 = windowMethods.setJTextField(name2, "NAZWA");
        price2 = windowMethods.setJTextField(price2, "CENA");
        year2 = windowMethods.setJTextField(year2, "ROK WYDANIA");
        storage2 = windowMethods.setJTextField(storage2, "STAN MAGAZYNU");
        publisher2 = windowMethods.setJTextField(publisher2, "WYDAWNICTWO");
        min_players2 = windowMethods.setJTextField(min_players2, "MINIMUM GRACZY");
        max_players2 = windowMethods.setJTextField(max_players2, "MAKSIMUM GRACZY");
        min_age2 = windowMethods.setJTextField(min_age2, "MINIMALNY WIEK GRACZA");
        est_time2 = windowMethods.setJTextField(est_time2, "PRZEWIDYWANY CZAS ROZGRYWKI");

        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Products pr = new Products();
                windowMethods.exit();
                try {
                    pr.create(user, isManager);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        edit = new JButton("Edytuj");
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //edycja
                if(check()){
                    try {
                        dataBase.setStmt();
                        if(publisherList.isSelectionEmpty()){
                            JOptionPane.showMessageDialog(windowMethods.window, "Nie wybrano wydawnictwa!", "Błąd", JOptionPane.ERROR_MESSAGE);
                        }
                        else
                        {
                            ResultSet rs = dataBase.getStmt().executeQuery(
                                    "SELECT ID_wydawnictwa FROM Wydawnictwo WHERE Nazwa = '" +
                                            publisherList.getSelectedValue().toString() + "'"
                            );
                            rs.next();
                            dataBase.getStmt().executeUpdate(
                                    "UPDATE Produkt SET Nazwa = '" + name2.getText() + "', " +
                                            "Cena = " + Float.parseFloat(price2.getText()) + ", Rok_wydania = '" + year2.getText() +
                                            "', Stan_magazyn = " + Integer.parseInt(storage2.getText())
                                            + ", Wydawnictwo_ID_wydawnictwa = " + rs.getInt(1)
                                            + " WHERE ID_produktu = " + IDToEdit
                            );
                            rs.close();
                            if(authorList.isSelectionEmpty()){
                                JOptionPane.showMessageDialog(windowMethods.window, "Nie wybrano autorów!", "Błąd", JOptionPane.ERROR_MESSAGE);
                            }
                            else{
                                dataBase.getStmt().executeUpdate(
                                        "DELETE FROM Autor_produktu WHERE Produkt_ID_produktu = " + IDToEdit
                                );
                                for(int i = 0; i <authorList.getSelectedValuesList().size(); i++){
                                    rs = dataBase.getStmt().executeQuery(
                                            "SELECT a.ID_Autora FROM (SELECT s.Imie || ' ' || s.Nazwisko as Dane, " +
                                                    "s.ID_autora FROM Autor s) a WHERE a.Dane = '" + authorList.getSelectedValuesList().get(i) + "'"
                                    );
                                    rs.next();
                                    dataBase.getStmt().executeUpdate(
                                            "INSERT INTO Autor_produktu VALUES(" + rs.getInt(1) + "," + IDToEdit + ")"
                                    );
                                    rs.close();
                                }
                                int age, time;
                                if(min_age2.getText().equals("")){
                                    age = 0;
                                }
                                else age = Integer.parseInt(min_age2.getText());
                                if(est_time2.getText().equals("")){
                                    time = 0;
                                }
                                else time = Integer.parseInt(est_time2.getText());
                                dataBase.getStmt().executeUpdate(
                                        "UPDATE Gra_planszowa SET g_Min_gracze = " + Integer.parseInt(min_players2.getText()) +
                                                ", g_Max_gracze = " + Integer.parseInt(max_players2.getText()) +
                                                ", g_Min_wiek = " + age +
                                                ", g_Czas_gry = " + time +
                                                " WHERE ID_produktu = " + IDToEdit
                                );
                                System.out.println("Edytowano 1 rekord");
                                JOptionPane.showMessageDialog(windowMethods.window, "Produkt edytowany pomyślnie!");
                                dataBase.getStmt().close();
                                Products pr = new Products();
                                windowMethods.exit();
                                pr.create(user, isManager);
                            }
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });
        add_publisher = new JButton("Nowe wydawnictwo...");
        add_publisher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Add_publisher ap = new Add_publisher();
                windowMethods.exit();
                ap.create(user, isManager);
            }
        });
        add_author = new JButton("Nowy autor...");
        add_author.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Add_author aa = new Add_author();
                windowMethods.exit();
                aa.create(user, isManager);
            }
        });

        getPublisherList();
        publisherList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        publisherList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        publisherList.setVisibleRowCount(-1);
        publisherListScroller = new JScrollPane(publisherList);
        publisherListScroller.setViewportView(publisherList);

        getAuthorList();
        authorList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        authorList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        authorList.setVisibleRowCount(-1);
        authorListScroller = new JScrollPane(authorList);
        authorListScroller.setViewportView(authorList);
    }
    private void getPublisherList() throws SQLException {
        dataBase.setStmt();
        dataBase.getConn().setAutoCommit(true);
        ResultSet rs = dataBase.getStmt().executeQuery(
                "SELECT Nazwa FROM Wydawnictwo"
        );
        while(rs.next()){
            String pub = rs.getString(1);
            listModelPublisher.addElement(pub);
        }
        rs.close();
        dataBase.getStmt().close();
        publisherList = new JList(listModelPublisher);
    }
    private void getAuthorList() throws SQLException{
        dataBase.setStmt();
        dataBase.getConn().setAutoCommit(true);
        ResultSet rs = dataBase.getStmt().executeQuery(
                "SELECT Imie, Nazwisko FROM Autor"
        );
        while (rs.next()){
            String aut = rs.getString(1) + " " + rs.getString(2);
            listModelAuthor.addElement(aut);
        }
        rs.close();
        dataBase.getStmt().close();
        authorList = new JList(listModelAuthor);
    }
    private void general_pane(){
        general_pane = new JPanel();
        general_pane.setLayout(new BoxLayout(general_pane, BoxLayout.PAGE_AXIS));
        general_pane.add(name_pane);
        general_pane.add(price_pane);
        general_pane.add(year_pane);
        general_pane.add(storage_pane);
        general_pane.add(publisher_pane);
        general_pane.add(author_pane);
    }
    private void game_pane(){
        game_pane = new JPanel();
        game_pane.setLayout(new BoxLayout(game_pane, BoxLayout.PAGE_AXIS));
        game_pane.add(min_players_pane);
        game_pane.add(max_players_pane);
        game_pane.add(min_age_pane);
        game_pane.add(est_time_pane);
    }
    private void panels() throws SQLException {
        labels();
        components();

        name_pane = new JPanel();
        name_pane.add(name);
        name_pane.add(name2);
        price_pane = new JPanel();
        price_pane.add(price);
        price_pane.add(price2);
        year_pane = new JPanel();
        year_pane.add(year);
        year_pane.add(year2);
        storage_pane = new JPanel();
        storage_pane.add(storage);
        storage_pane.add(storage2);
        publisher_pane = new JPanel();
        publisher_pane.add(publisher);
        publisher_pane.add(publisherListScroller);
        publisher_pane.add(add_publisher);
        author_pane = new JPanel();
        author_pane.add(author);
        author_pane.add(authorListScroller);
        author_pane.add(add_author);
        min_players_pane = new JPanel();
        min_players_pane.add(min_players);
        min_players_pane.add(min_players2);
        max_players_pane = new JPanel();
        max_players_pane.add(max_players);
        max_players_pane.add(max_players2);
        min_age_pane = new JPanel();
        min_age_pane.add(min_age);
        min_age_pane.add(min_age2);
        est_time_pane = new JPanel();
        est_time_pane.add(est_time);
        est_time_pane.add(est_time2);
        general_pane();
        game_pane();

        center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.add(general_pane);
        center.add(game_pane);

        down = new JPanel();
        down.setLayout(new BorderLayout());
        down.add(back, BorderLayout.WEST);
        down.add(edit, BorderLayout.EAST);
    }
    private void add_components() throws SQLException {
        panels();
        windowMethods.window.add(center, BorderLayout.CENTER);
        windowMethods.window.add(down, BorderLayout.SOUTH);
    }
    private boolean check(){
        DataVerification verify = new DataVerification();
        verify.fieldCheck(name2, 1, 50, true, true);
        verify.sumCheck(price2);
        verify.dateCheck(year2, 0, 4);
        verify.numberCheck(storage2, 1, 3);
        verify.numberCheck(min_players2, 1, 2);
        verify.numberCheck(min_players2, 1, 2);
        verify.checkTwoNumbers(min_players2, max_players2);
        verify.numberCheck(min_age2, 0, 2);
        verify.numberCheck(est_time2, 0, 3);
        verify.errorTwoNumbers();
        verify.errorMessage();
        if (verify.error_counter == 0) return true;
        else return false;
    }
}
