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

public class Edit_book {
    private WindowMethods windowMethods = new WindowMethods();
    private JButton edit, back, add_publisher, add_author, add_series;
    private JLabel name, price, year, storage, publisher, author, cover_type, pages_amount, size, series_title;
    private JTextField name2, price2, year2, storage2, publisher2, cover_type2, pages_amount2, size2, series_title2;
    private JPanel center, down, general_pane ,book_pane, name_pane, price_pane, year_pane,storage_pane, publisher_pane, author_pane, cover_type_pane, pages_amount_pane, size_pane, series_title_pane;
    private String user;
    private int IDToEdit;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private DefaultListModel listModelPublisher = new DefaultListModel(), listModelAuthor = new DefaultListModel(), listModelSeries = new DefaultListModel();
    private JList publisherList, authorList, seriesList;
    private JScrollPane publisherListScroller, authorListScroller, seriesListScroller;
    private boolean isManager, ableToEdit = true;

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
                "SELECT Nazwa, Cena, Rok_wydania, Stan_magazyn, Wydawnictwo_ID_wydawnictwa, k_Typ_okladki, k_Liczba_stron, k_Format, Seria_tytul " +
                        "FROM Produkt JOIN Ksiazka USING(ID_produktu) WHERE ID_produktu = " + id
        );
        rs.next();
        name2.setText(rs.getString(1));
        price2.setText(Float.toString(rs.getFloat(2)));
        year2.setText(rs.getString(3));
        storage2.setText(Integer.toString(rs.getInt(4)));
        int publisherID = rs.getInt(5);
        cover_type2.setText(rs.getString(6));
        pages_amount2.setText(rs.getString(7));
        size2.setText(rs.getString(8));
        String title = rs.getString(9);
        rs.close();
        ArrayList<Integer> tmpArray = new ArrayList<Integer>();
        rs = dataBase.getStmt().executeQuery(
                "SELECT a.Imie, a.Nazwisko, a.ID_autora FROM Autor a JOIN Autor_produktu b ON a.ID_autora = b.Autor_ID_autora WHERE b.Produkt_ID_produktu = " + id
        );
        while (rs.next()){
            for(int i = 0; i < authorList.getModel().getSize(); i++){
                if(authorList.getModel().getElementAt(i).equals(rs.getString(1) + " " + rs.getString(2) + ", " + Integer.toString(rs.getInt(3)))){
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
        if(title == null);
        else{
            for (int i = 0; i < seriesList.getModel().getSize(); i++){
                if(seriesList.getModel().getElementAt(i).equals(title)) seriesList.setSelectedIndex(i);
            }
        }
        dataBase.getStmt().close();
    }
    private void labels(){
        name = new JLabel("Nazwa produktu (max 50 znaków): ");
        price = new JLabel("Cena produktu: ");
        year = new JLabel("Rok wydania produktu (opcjonalnie): ");
        storage = new JLabel("Stan magazynu (max 999): ");
        publisher = new JLabel("Wydawnictwo: ");
        author = new JLabel("Autorzy: ");
        cover_type = new JLabel("Typ okładki (opcjonalnie, max 10 znaków): ");
        pages_amount = new JLabel("Liczba stron (opcjonalnie): ");
        size = new JLabel("Format książki (opcjonalnie, max 4 znaki): ");
        series_title = new JLabel("Tytuł serii książek (opcjonalnie): ");
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
                        if(publisherList.isSelectionEmpty() || authorList.isSelectionEmpty()){
                            JOptionPane.showMessageDialog(windowMethods.window, "Nie wybrano wydawnictwa i/lub autora!", "Błąd", JOptionPane.ERROR_MESSAGE);
                        }
                        else{
                            ResultSet rs2 = dataBase.getStmt().executeQuery(
                                    "SELECT ID_produktu, Nazwa FROM Produkt WHERE Nazwa = '" + name2.getText() + "'"
                            );
                            if(rs2.next()){

                                if(rs2.getInt(1) != IDToEdit){
                                    JOptionPane.showMessageDialog(windowMethods.window, "Produkt o podanej nazwie już istnieje!", "Błąd!", JOptionPane.ERROR_MESSAGE);
                                    ableToEdit = false;
                                }
                                else ableToEdit = true;
                            }
                            rs2.close();
                            if(ableToEdit){
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
//                            if(authorList.isSelectionEmpty()){
//                                JOptionPane.showMessageDialog(windowMethods.window, "Nie wybrano autorów!", "Błąd", JOptionPane.ERROR_MESSAGE);
//                            }
                                //else{
                                dataBase.getStmt().executeUpdate(
                                        "DELETE FROM Autor_produktu WHERE Produkt_ID_produktu = " + IDToEdit
                                );
                                for(int i = 0; i <authorList.getSelectedValuesList().size(); i++){
                                    String currentAuthor = authorList.getSelectedValuesList().get(i).toString();
                                    int id = Integer.parseInt(currentAuthor.substring(currentAuthor.indexOf(',') + 2));
//                                    rs = dataBase.getStmt().executeQuery(
//                                            "SELECT a.ID_Autora FROM (SELECT s.Imie || ' ' || s.Nazwisko as Dane, " +
//                                                    "s.ID_autora FROM Autor s) a WHERE a.Dane = '" + authorList.getSelectedValuesList().get(i) + "'"
//                                    );
//                                    rs.next();
                                    dataBase.getStmt().executeUpdate(
                                            "INSERT INTO Autor_produktu VALUES(" + id + "," + IDToEdit + ")"
                                    );
                                    //rs.close();
                                }
                                String tmp;
                                if(seriesList.isSelectionEmpty()) tmp = "";
                                else tmp = seriesList.getSelectedValue().toString();
                                dataBase.getStmt().executeUpdate(
                                        "UPDATE Ksiazka SET k_Typ_okladki = '" + cover_type2.getText() +
                                                "', k_Liczba_stron = '" + pages_amount2.getText() +
                                                "', k_Format = '" + size2.getText() + "', Seria_Tytul = '" + tmp +
                                                "' WHERE ID_produktu = " + IDToEdit
                                );
                                System.out.println("Edytowano 1 rekord");
                                JOptionPane.showMessageDialog(windowMethods.window, "Produkt edytowany pomyślnie!");
                                dataBase.getStmt().close();
                                Products pr = new Products();
                                windowMethods.exit();
                                pr.create(user, isManager);
                            }
                            //}
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
        publisherList.setLayoutOrientation(JList.VERTICAL);
        publisherList.setVisibleRowCount(6);
        publisherListScroller = new JScrollPane(publisherList);
        publisherListScroller.setViewportView(publisherList);

        getAuthorList();
        authorList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        authorList.setLayoutOrientation(JList.VERTICAL);
        authorList.setVisibleRowCount(6);
        authorListScroller = new JScrollPane(authorList);
        authorListScroller.setViewportView(authorList);

        getSeriesList();
        seriesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        seriesList.setLayoutOrientation(JList.VERTICAL);
        seriesList.setVisibleRowCount(4);
        seriesListScroller = new JScrollPane(seriesList);
        seriesListScroller.setViewportView(seriesList);
    }
    private void getPublisherList() throws SQLException {
        dataBase.setStmt();
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
                "SELECT Imie, Nazwisko, ID_autora FROM Autor"
        );
        while (rs.next()){
            String aut = rs.getString(1) + " " + rs.getString(2) + ", " + Integer.toString(rs.getInt(3));
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
        series_title_pane.add(seriesListScroller);
        series_title_pane.add(add_series);
        general_pane();
        book_pane();

        center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.add(general_pane);
        center.add(book_pane);

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
        name2.setText(name2.getText().trim().replaceAll("\\s{2,}", " "));
        cover_type2.setText(cover_type2.getText().trim().replaceAll("\\s{2,}", " "));
        size2.setText(size2.getText().trim().replaceAll("\\s{2,}", " "));
        verify.fieldCheck(name2, 1, 50, true, true);
        verify.sumCheck(price2);
        verify.dateCheck(year2, 0, 4);
        verify.numberCheck(storage2, 1, 3);
        verify.fieldCheck(cover_type2, 0, 10, false, true);
        verify.numberCheck(pages_amount2, 0, 4);
        verify.fieldCheck(size2, 0, 4, true, false);
        verify.errorMessage();
        if (verify.error_counter == 0) return true;
        else return false;
    }
}
