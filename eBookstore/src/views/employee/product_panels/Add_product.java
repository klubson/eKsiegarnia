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

public class Add_product {
    private WindowMethods windowMethods = new WindowMethods();
    private JButton add, back, add_publisher, add_author, add_series;
    private JLabel name, price, year, storage, publisher, author, cover_type, pages_amount, size, series_title, min_players, max_players, min_age, est_time;
    private JTextField name2, price2, year2, storage2, publisher2, cover_type2, pages_amount2, size2, series_title2, min_players2, max_players2, min_age2, est_time2;
    private JPanel center, down, general_pane, book_pane, game_pane, name_pane, price_pane, year_pane,storage_pane, publisher_pane, author_pane, cover_type_pane, pages_amount_pane, size_pane, series_title_pane, min_players_pane, max_players_pane, min_age_pane, est_time_pane;
    private String user;
    private JTabbedPane menu;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private DefaultListModel listModelPublisher = new DefaultListModel(), listModelAuthor = new DefaultListModel(), listModelSeries = new DefaultListModel();
    private JList publisherList, authorList, seriesList;
    private JScrollPane publisherListScroller, authorListScroller, seriesListScroller;
    private boolean isManager;

    public void create(String data, boolean mode) throws SQLException {
        windowMethods.window = new JFrame("Dodaj produkt");
        windowMethods.settings();
        user = data;
        isManager = mode;
        add_components();
        windowMethods.window.setVisible(true);
    }
    private void labels(){
        name = new JLabel("Nazwa produktu (max 50 znaków): ");
        price = new JLabel("Cena produktu: ");
        year = new JLabel("Rok wydania produktu (opcjonalnie): ");
        storage = new JLabel("Stan magazynu (max 999): ");
        publisher = new JLabel("Wydawnictwo: ");
        author = new JLabel("Autorzy: ");
        cover_type = new JLabel("Typ okładki: ");
        pages_amount = new JLabel("Liczba stron: ");
        size = new JLabel("Format książki: ");
        series_title = new JLabel("Tytuł serii książek: ");
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
        cover_type2 = windowMethods.setJTextField(cover_type2, "TYP OKŁADKI");
        pages_amount2 = windowMethods.setJTextField(pages_amount2, "LICZBA STRON");
        size2 = windowMethods.setJTextField(size2, "FORMAT KSIĄŻKI");
        series_title2 = windowMethods.setJTextField(series_title2, "TYTUŁ SERII");
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
        add = new JButton("Dodaj produkt");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(menu.getSelectedIndex() == 0){
                    JOptionPane.showMessageDialog(windowMethods.window,"Aby dodać produkt, wybierz odpowiednią zakładkę, uzupełnij dane oraz kliknij przycisk 'Dodaj'", "Ostrzeżnie", JOptionPane.ERROR_MESSAGE);
                }
                else{
                    if(generalCheck()){
                        try {
                            dataBase.getConn().setAutoCommit(true);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        if(menu.getSelectedIndex() == 1){
                            if(bookCheck()){
                                try {
                                    //System.out.println(authorList.getSelectedValuesList());
                                    String tmp;
                                    if(seriesList.isSelectionEmpty()) tmp = null;
                                    else tmp = seriesList.getSelectedValue().toString();
                                    dataBase.newBook(name2.getText(), Float.parseFloat(price2.getText()), year2.getText(), Integer.parseInt(storage2.getText()), publisherList.getSelectedValue().toString(), cover_type2.getText(), Integer.parseInt(pages_amount2.getText()), size2.getText(), tmp);
                                    for(int i = 0; i < authorList.getSelectedValuesList().size(); i++){
                                        dataBase.setStmt();
                                        ResultSet rs = dataBase.getStmt().executeQuery(
                                                "SELECT a.ID_Autora FROM (SELECT s.Imie || ' ' || s.Nazwisko as Dane, " +
                                                        "s.ID_autora FROM Autor s) a WHERE a.Dane = '" + authorList.getSelectedValuesList().get(i) + "'"
                                        );
                                        rs.next();
                                        dataBase.getStmt().executeUpdate(
                                                "INSERT INTO Autor_produktu VALUES(" + rs.getInt(1) + ",PRODUKT_ID_PRODUKTU_SEQ.currval)"
                                        );
                                        rs.close();
                                        dataBase.getStmt().close();
                                    }
                                    JOptionPane.showMessageDialog(windowMethods.window, "Książka dodana pomyślnie");
                                    System.out.println("Dodano książkę");
                                    Products pr = new Products();
                                    windowMethods.exit();
                                    pr.create(user, isManager);
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                            }
                        }
                        else if (menu.getSelectedIndex() == 2){
                            if(gameCheck()){
                                try {
                                    dataBase.newGame(name2.getText(), Float.parseFloat(price2.getText()), year2.getText(), Integer.parseInt(storage2.getText()), publisherList.getSelectedValue().toString(), Integer.parseInt(min_players2.getText()), Integer.parseInt(max_players2.getText()), min_age2.getText(), est_time2.getText());
                                    for(int i = 0; i < authorList.getSelectedValuesList().size(); i++){
                                        dataBase.setStmt();
                                        ResultSet rs = dataBase.getStmt().executeQuery(
                                                "SELECT a.ID_Autora FROM (SELECT s.Imie || ' ' || s.Nazwisko as Dane, " +
                                                        "s.ID_autora FROM Autor s) a WHERE a.Dane = '" + authorList.getSelectedValuesList().get(i) + "'"
                                        );
                                        rs.next();
                                        dataBase.getStmt().executeUpdate(
                                                "INSERT INTO Autor_produktu VALUES(" + rs.getInt(1) + ",PRODUKT_ID_PRODUKTU_SEQ.currval)"
                                        );
                                        rs.close();
                                        dataBase.getStmt().close();
                                    }
                                    JOptionPane.showMessageDialog(windowMethods.window, "Gra planszowa dodana pomyślnie");
                                    System.out.println("Dodano grę planszową");
                                    Products pr = new Products();
                                    windowMethods.exit();
                                    pr.create(user, isManager);
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                            }
                        }

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
        add_series = new JButton("Nowa seria...");
        add_series.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Add_series as = new Add_series();
                windowMethods.exit();
                as.create(user, isManager);
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

        getSeriesList();
        seriesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        seriesList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        seriesList.setVisibleRowCount(-1);
        seriesListScroller = new JScrollPane(seriesList);
        seriesListScroller.setViewportView(seriesList);

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
    private void getSeriesList() throws SQLException {
        dataBase.setStmt();
        dataBase.getConn().setAutoCommit(true);
        ResultSet rs = dataBase.getStmt().executeQuery(
                "SELECT Tytul FROM Seria"
        );
        while (rs.next()){
            listModelSeries.addElement(rs.getString(1));
        }
        rs.close();
        dataBase.getStmt().close();
        seriesList = new JList(listModelSeries);
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
    private void book_pane(){
        book_pane = new JPanel();
        book_pane.setLayout(new BoxLayout(book_pane, BoxLayout.PAGE_AXIS));
        book_pane.add(cover_type_pane);
        book_pane.add(pages_amount_pane);
        book_pane.add(size_pane);
        book_pane.add(series_title_pane);
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
        //publisher_pane.add(publisher2);
        publisher_pane.add(publisherListScroller);
        publisher_pane.add(add_publisher);
        author_pane = new JPanel();
        author_pane.add(author);
        author_pane.add(authorListScroller);
        author_pane.add(add_author);
        cover_type_pane = new JPanel();
        cover_type_pane.add(cover_type);
        cover_type_pane.add(cover_type2);
        pages_amount_pane = new JPanel();
        pages_amount_pane.add(pages_amount);
        pages_amount_pane.add(pages_amount2);
        size_pane = new JPanel();
        size_pane.add(size);
        size_pane.add(size2);
        series_title_pane = new JPanel();
        series_title_pane.add(series_title);
        //series_title_pane.add(series_title2);
        series_title_pane.add(seriesListScroller);
        series_title_pane.add(add_series);
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
        book_pane();
        game_pane();

        menu = new JTabbedPane();
        menu.add("ogólne", general_pane);
        menu.add("książka", book_pane);
        menu.add("gra planszowa", game_pane);

        center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.add(menu);
        down = new JPanel();
        down.setLayout(new BorderLayout());
        down.add(back, BorderLayout.WEST);
        down.add(add, BorderLayout.EAST);

    }
    private void add_components() throws SQLException {
        panels();
        windowMethods.window.add(center, BorderLayout.CENTER);
        windowMethods.window.add(down, BorderLayout.SOUTH);
    }
    private boolean generalCheck(){
        DataVerification verify = new DataVerification();
        verify.fieldCheck(name2, 1 , 50, true, true);
        verify.sumCheck(price2);
        verify.dateCheck(year2, 0, 4);
        verify.numberCheck(storage2, 1, 3);
        verify.errorMessage();
        if(verify.error_counter == 0) return true;
        else return false;
    }
    private boolean bookCheck(){
        DataVerification verify = new DataVerification();
        verify.fieldCheck(cover_type2, 0, 10, false, false);
        verify.numberCheck(pages_amount2, 0, 4);
        verify.fieldCheck(size2, 0, 4, true, false);
        verify.errorMessage();
        if(verify.error_counter == 0) return true;
        else return false;
    }
    private boolean gameCheck(){
        DataVerification verify = new DataVerification();
        verify.numberCheck(min_players2, 1, 2);
        verify.numberCheck(max_players2, 1, 2);
        verify.numberCheck(min_age2, 0, 2);
        verify.numberCheck(est_time2, 0, 3);
        verify.errorMessage();
        if(verify.error_counter == 0) return true;
        else return false;
    }
}
