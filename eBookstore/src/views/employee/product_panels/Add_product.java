package views.employee.product_panels;

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

public class Add_product extends JFrame {
    private JFrame window;
    private JButton add, back, add_publisher, add_author, add_series;
    private JLabel name, price, year, storage, publisher, author, cover_type, pages_amount, size, series_title, min_players, max_players, min_age, est_time;
    private JTextField name2, price2, year2, storage2, publisher2, author2, cover_type2, pages_amount2, size2, series_title2, min_players2, max_players2, min_age2, est_time2;
    private JPanel center, down, general_pane, book_pane, game_pane, name_pane, price_pane, year_pane,storage_pane, publisher_pane, author_pane, cover_type_pane, pages_amount_pane, size_pane, series_title_pane, min_players_pane, max_players_pane, min_age_pane, est_time_pane;
    private String user, message = "W następujących polach wykryto błędy: ";
    private JTabbedPane menu;
    private Dimension dimension = new Dimension(250, 20);
    private dataBaseConnection dataBase = new dataBaseConnection();
    private DefaultListModel listModelPublisher = new DefaultListModel(), listModelAuthor = new DefaultListModel(), listModelSeries = new DefaultListModel();
    private JList publisherList, authorList, seriesList;
    private JScrollPane publisherListScroller, authorListScroller, seriesListScroller;
    private int error_counter;
    private boolean date_correctness, number_correctness;

//    private JTextField[] tab_JTextField = {name2, price2, year2, storage2, publisher2, cover_type2, pages_amount2, size2, series_title2, min_players2, max_players2, min_age2, est_time2};
//    private String[] tab_JTextFieldNames = {"NAZWA PRODUKTU", "CENA", "ROK WYDANIA", "STAN MAGAZYNU" ,"WYDAWNICTWO", "TYP OKŁADKI", "LICZBA STRON", "FORMAT KSIĄŻKI", "TYTUŁ SERII", "MINIMUM GRACZY", "MAKSIMUM GRACZY", "MINIMALNY WIEK GRACZA", "SZACOWANY CZAS GRY"};
//    private JLabel[] tab_JLabel = {name, price, year, storage, publisher, cover_type, pages_amount, size, series_title, min_players, max_players, min_age, est_time};
//    private String[] tab_JLabelNames = {"Nazwa produktu (max 30 znaków): ", "Cena produktu: ", "Rok wydania produktu (opcjonalnie): ", "Stan magazynu (max 999): " ,"Wydawnictwo (max 30 znaków): ", "Typ okładki: ", "Liczba stron: ", "Format książki: ", "Tytuł serii książek: ", "Minimalna liczba graczy: ", "Maksymalna liczba graczy: ", "Zalecany minimalny wiek (opcjonalnie): ", "Szacowany czas gry (opcjonalnie): "};
//    private JPanel[] tab_JPanel = {name_pane, price_pane, year_pane, storage_pane ,publisher_pane, cover_type_pane, pages_amount_pane, size_pane, series_title_pane, min_players_pane, max_players_pane, min_age_pane, est_time_pane};
//    private int[] game_iterator = {0,1,2,3,8,9,10,11};

    public void create(String data) throws SQLException {
        window = new JFrame("Dodaj produkt");
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
//        for (int i = 0; i < tab_JLabel.length; i++){
//            tab_JLabel[i] = new JLabel(tab_JLabelNames[i]);
//        }
    }
    private void components() throws SQLException {
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Products pr = new Products();
                exit();
                try {
                    pr.create(user);
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
                    JOptionPane.showMessageDialog(window,"Aby dodać produkt, wybierz odpowiednią zakładkę, uzupełnij dane oraz kliknij przycisk 'Dodaj'", "Ostrzeżnie", JOptionPane.ERROR_MESSAGE);
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
                                    System.out.println(authorList.getSelectedValuesList());
                                    dataBase.newBook(name2.getText(), Float.parseFloat(price2.getText()), year2.getText(), Integer.parseInt(storage2.getText()), publisherList.getSelectedValue().toString(), cover_type2.getText(), Integer.parseInt(pages_amount2.getText()), size2.getText(), series_title2.getText());
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
                                    JOptionPane.showMessageDialog(window, "Książka dodana pomyślnie");
                                    Products pr = new Products();
                                    exit();
                                    pr.create(user);
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
                                    JOptionPane.showMessageDialog(window, "Gra planszowa dodana pomyślnie");
                                    Products pr = new Products();
                                    exit();
                                    pr.create(user);
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
                exit();
                ap.create(user);
            }
        });
        add_author = new JButton("Nowy autor...");
        add_author.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Add_author aa = new Add_author();
                exit();
                aa.create(user);
            }
        });
        add_series = new JButton("Nowa seria...");
        add_series.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Add_series as = new Add_series();
                exit();
                as.create(user);
            }
        });
        name2 = new JTextField();
        name2.setName("NAZWA PRODUKTU");
        name2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        name2.setPreferredSize(dimension);
        price2 = new JTextField();
        price2.setName("CENA");
        price2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        price2.setPreferredSize(dimension);
        year2 = new JTextField();
        year2.setName("ROK WYDANIA");
        year2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        year2.setPreferredSize(dimension);
        storage2 = new JTextField();
        storage2.setName("STAN MAGAZYNU");
        storage2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        storage2.setPreferredSize(dimension);
        publisher2 = new JTextField();
        publisher2.setName("WYDAWNICTWO");
        publisher2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        publisher2.setPreferredSize(dimension);
        cover_type2 = new JTextField();
        cover_type2.setName("TYP OKŁADKI");
        cover_type2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        cover_type2.setPreferredSize(dimension);
        pages_amount2 = new JTextField();
        pages_amount2.setName("ILOŚĆ STRON");
        pages_amount2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        pages_amount2.setPreferredSize(dimension);
        size2 = new JTextField();
        size2.setName("FORMAT KSIĄŻKI");
        size2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        size2.setPreferredSize(dimension);
        series_title2 = new JTextField();
        series_title2.setName("TYTUŁ SERII");
        series_title2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        series_title2.setPreferredSize(dimension);
        min_players2 = new JTextField();
        min_players2.setName("MINIMUM GRACZY");
        min_players2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        min_players2.setPreferredSize(dimension);
        max_players2 = new JTextField();
        max_players2.setName("MAKSIMUM GRACZY");
        max_players2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        max_players2.setPreferredSize(dimension);
        min_age2 = new JTextField();
        min_age2.setName("MINIMALNY WIEK GRACZA");
        min_age2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        min_age2.setPreferredSize(dimension);
        est_time2 = new JTextField();
        est_time2.setName("PRZEWIDYWANY CZAS ROZGRYWKI");
        est_time2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        est_time2.setPreferredSize(dimension);
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


//        for (int i = 0; i < tab_JTextField.length; i++){
//            tab_JTextField[i] = new JTextField();
//            tab_JTextField[i].setName(tab_JTextFieldNames[i]);
//            tab_JTextField[i].addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//
//                }
//            });
//            tab_JTextField[i].setPreferredSize(dimension);
//        }
    }
    private void getPublisherList() throws SQLException {
        dataBase.setStmt();
        dataBase.getConn().setAutoCommit(true);
        ResultSet rs = dataBase.getStmt().executeQuery(
                "SELECT Nazwa FROM Wydawnictwo"
        );
        while(rs.next()){
            System.out.println(rs.getString(1));
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
//        for (int i = 0; i < 8; i++){
//            book_pane.add(tab_JPanel[i]);
//        }
    }
    private void game_pane(){
        game_pane = new JPanel();
        game_pane.setLayout(new BoxLayout(game_pane, BoxLayout.PAGE_AXIS));
        game_pane.add(min_players_pane);
        game_pane.add(max_players_pane);
        game_pane.add(min_age_pane);
        game_pane.add(est_time_pane);
//        for (int i = 0; i < 8; i++){
//            game_pane.add(tab_JPanel[game_iterator[i]]);
//        }
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
//        for (int i = 0; i < tab_JPanel.length; i++){
//            tab_JPanel[i] = new JPanel();
//            tab_JPanel[i].add(tab_JLabel[i]);
//            tab_JPanel[i].add(tab_JTextField[i]);
//        }
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
        window.add(center, BorderLayout.CENTER);
        window.add(down, BorderLayout.SOUTH);
    }
    private boolean generalCheck(){
        error_counter = 0;
        fieldCheck(name2, 1, 50, true, true);
        priceCheck(price2);
        dateCheck(year2, 4, 4);
        numberCheck(storage2, 0, 3);
        if(error_counter != 0){
            JOptionPane.showMessageDialog(window, message, "Błąd!", JOptionPane.ERROR_MESSAGE);
            message = "W następujących polach wykryto błędy: ";
        }
        if (error_counter == 0) return true;
        else return false;
    }
    private boolean bookCheck(){
        error_counter = 0;
        fieldCheck(cover_type2, 0, 10, false, false);
        numberCheck(pages_amount2, 0, 4);
        fieldCheck(size2, 0, 4, true, false);
        if(error_counter != 0){
            JOptionPane.showMessageDialog(window, message, "Błąd!", JOptionPane.ERROR_MESSAGE);
            message = "W następujących polach wykryto błędy: ";
        }
        if (error_counter == 0) return true;
        else return false;
    }
    private boolean gameCheck(){
        error_counter = 0;
        numberCheck(min_players2, 1, 2);
        numberCheck(max_players2, 1,2);
        numberCheck(min_age2, 0, 2);
        numberCheck(est_time2, 0, 3);
        if(error_counter != 0){
            JOptionPane.showMessageDialog(window, message, "Błąd!", JOptionPane.ERROR_MESSAGE);
            message = "W następujących polach wykryto błędy: ";
        }
        if (error_counter == 0) return true;
        else return false;
    }
    private void fieldCheck(JTextField field, int min_size, int max_size, boolean digitsEnabled, boolean spaceEnabled){
        if(field.getText().length() < min_size || field.getText().length() > max_size){
            message += "\n" + field.getName();
            error_counter++;
        }
        else{
            if(digitsEnabled && spaceEnabled){
                for(int i = 0; i < field.getText().length(); i++){
                    if(!Character.isLetterOrDigit(field.getText().charAt(i)) && !Character.isSpaceChar(field.getText().charAt(i))){
                        message += "\n" + field.getName();
                        error_counter++;
                        break;
                    }
                }
            }
            else if(!digitsEnabled && spaceEnabled){
                for(int i = 0; i < field.getText().length(); i++){
                    if(!Character.isLetter(field.getText().charAt(i)) && !Character.isSpaceChar(field.getText().charAt(i))){
                        message += "\n" + field.getName();
                        error_counter++;
                        break;
                    }
                }
            }
            else if(digitsEnabled && !spaceEnabled){
                for(int i = 0; i < field.getText().length(); i++){
                    if(!Character.isLetterOrDigit(field.getText().charAt(i))){
                        message += "\n" + field.getName();
                        error_counter++;
                        break;
                    }
                }
            }
            else{
                for(int i = 0; i < field.getText().length(); i++){
                    if(!Character.isLetter(field.getText().charAt(i))){
                        message += "\n" + field.getName();
                        error_counter++;
                        break;
                    }
                }
            }
        }
    }
    private void priceCheck(JTextField field){
        if (!field.getText().matches("[0-9]+[.]?[0-9]{1,2}")){
            message += "\n" + field.getName();
            error_counter++;
        }
    }
    private void dateCheck(JTextField field, int min_size, int max_size) {
        date_correctness = true;
        if(field.getText().length() != min_size){
            if(field.getText().length() != 0){
                message += "\n" + field.getName();
                error_counter++;
            }
        }
        for (int i = 0; i < field.getText().length(); i++) {
            if (!Character.isDigit(field.getText().charAt(i))) {
                message += "\n" + field.getName();
                error_counter++;
            }
        }
    }
    private void numberCheck(JTextField field, int min_size, int max_size){
        number_correctness = true;
        if(field.getText().length() < min_size || field.getText().length() > max_size){
            message += "\n" + field.getName();
            error_counter++;
        }
        for (int i = 0; i < field.getText().length(); i++) {
            if (!Character.isDigit(field.getText().charAt(i))) {
                message += "\n" + field.getName();
                error_counter++;
            }
        }
    }
    private void exit(){
        window.setVisible(false);
        window.dispose();
    }
}
