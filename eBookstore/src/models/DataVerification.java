package models;

import javax.swing.*;
import java.util.Arrays;

public class DataVerification {
    private dataBaseConnection dataBase = new dataBaseConnection();
    public String message = "W następujących polach wykryto błędy: ";
    public boolean isSecondHigher = true;
    public int error_counter = 0;
    public boolean phone_correctness, pass_correctness, address_correctness;

    public void fieldCheck(JTextField field, int min_size, int max_size, boolean digitsEnabled, boolean spaceEnabled){
        if(field.getText().length() < min_size || field.getText().length() > max_size){
            message += "\nW pole " + field.getName() + " wprowadzono zbyt małą lub zbyt dużą liczbę znaków";
            error_counter++;
        }
        else{
            if(digitsEnabled && spaceEnabled){
                for(int i = 0; i < field.getText().length(); i++){
                    if(!Character.isLetterOrDigit(field.getText().charAt(i)) && !Character.isSpaceChar(field.getText().charAt(i))){
                        message += "\nPole " + field.getName() + " musi się składać wyłącznie z liter, cyfr lub znaku spacji";
                        error_counter++;
                        break;
                    }
                }
            }
            else if(!digitsEnabled && spaceEnabled){
                for(int i = 0; i < field.getText().length(); i++){
                    if(!Character.isLetter(field.getText().charAt(i)) && !Character.isSpaceChar(field.getText().charAt(i))){
                        message += "\nPole " + field.getName() + " musi się składać wyłącznie z liter lub znaku spacji";
                        error_counter++;
                        break;
                    }
                }
            }
            else if(digitsEnabled && !spaceEnabled){
                for(int i = 0; i < field.getText().length(); i++){
                    if(!Character.isLetterOrDigit(field.getText().charAt(i))){
                        message += "\nPole " + field.getName() + " musi się składać z liter lub cyfr";
                        error_counter++;
                        break;
                    }
                }
            }
            else{
                for(int i = 0; i < field.getText().length(); i++){
                    if(!Character.isLetter(field.getText().charAt(i))){
                        message += "\nPole " + field.getName() + " musi się składać wyłącznie z liter";
                        error_counter++;
                        break;
                    }
                }
            }
        }
    }
    public void passFieldCheck(JPasswordField field, int size){
        String tmp = String.copyValueOf(field.getPassword());
        if(tmp.length() == 0 || tmp.length() > size){
            message += "\nW pole " + field.getName() + " wprowadzono zbyt mało lub zbyt dużo znaków";
            error_counter++;
        }
        else if(tmp.contains(";")){
            message += "\nPole " + field.getName() + " zawiera niedozwolony znak ';'";
            error_counter++;
        }
        else{
            for(int i = 0; i < tmp.length(); i++){
                if(!Character.isLetterOrDigit(tmp.charAt(i))){
                    message += "\nPole " + field.getName() + " musi składać się wyłącznie z liter lub cyfr";
                    error_counter++;
                    break;
                }
            }
        }
    }
    public boolean samePass(JPasswordField pass1, JPasswordField pass2){
        if (Arrays.toString(pass1.getPassword()).equals(Arrays.toString(pass2.getPassword()))) pass_correctness = true;
        else pass_correctness = false;
        return pass_correctness;
    }
    public boolean phoneCheck(JTextField field) {
        phone_correctness = true;
        if(field.getText().length() != 9){
            if(field.getText().length() != 0) phone_correctness = false;
        }
        for (int i = 0; i < field.getText().length(); i++) {
            if (!Character.isDigit(field.getText().charAt(i))) {
                phone_correctness = false;
            }
        }
        return phone_correctness;
    }
    public void numberCheck(JTextField field, int min_size, int max_size){
        if(field.getText().length() < min_size || field.getText().length() > max_size){
            message += "\nW pole " + field.getName() + " wprowadzono zbyt mało lub zbyt dużo znaków!";
            error_counter++;
        }
        else{
            for (int i = 0; i < field.getText().length(); i++) {
                if (!Character.isDigit(field.getText().charAt(i))) {
                    message += "\nPole " + field.getName() + " musi składać się wyłącznie z cyfr!";
                    error_counter++;
                    break;
                }
            }
        }
    }
    public void checkTwoNumbers(JTextField field, JTextField field2){
        boolean dig1 = true, dig2 = true;
        for (int i = 0; i < field.getText().length(); i++) {
            if (!Character.isDigit(field.getText().charAt(i))) {
                dig1 = false;
                break;
            }
        }
        for (int i = 0; i < field.getText().length(); i++) {
            if (!Character.isDigit(field.getText().charAt(i))) {
                dig2 = false;
                break;
            }
        }
        if(field.getText().length() == 0) dig1 = false;
        if(field2.getText().length() == 0) dig2 = false;
        if(dig1 && dig2){
            if(Integer.parseInt(field.getText()) > Integer.parseInt(field2.getText())){
                isSecondHigher = false;
                error_counter++;
                message += "\nPole MIN.GRACZY musi zawierać mniejszą lub równą wartość od pola MAX.GRACZY. Pola mogą składać się wyłącznie z cyfr oraz nie mogą być puste!";
            }
        }
        else{
            error_counter++;
            message += "\nPole MIN.GRACZY lub MAX.GRACZY musi składać się wyłącznie z cyfr oraz nie może być puste!";
        }
    }
    public void sumCheck(JTextField field){
        if (!field.getText().matches("[0-9]+[.]?[0-9]{1,2}")){
            message += "\nPole " + field.getName() + " musi składać się wyłącznie z cyfr oraz kropki w przypadku liczby zmiennoprzecinkowej";
            error_counter++;
        }
    }
    public void dateCheck(JTextField field, int min_size, int max_size) {
        if(field.getText().length() != min_size){
            if(field.getText().length() != max_size){
                message += "\nPole " + field.getName() + " zawiera nieodpowiednią liczbę znaków (prawidłowa liczba to 0 lub 4)";
                error_counter++;
            }
        }
        else{
            for (int i = 0; i < field.getText().length(); i++) {
                if (!Character.isDigit(field.getText().charAt(i))) {
                    message += "\nPole " + field.getName() + " musi składać się wyłącznie z cyfr!";
                    error_counter++;
                    break;
                }
            }
        }
        if(!field.getText().isEmpty() ){
            if(Integer.parseInt(field.getText()) > 2021){
                message += "\nPole " + field.getName() + " zawiera rok nowszy niż obecny!";
                error_counter++;
            }
        }

    }
    public void emailCheck(JTextField field, int max_size){
        if(field.getText().length() == 0 || field.getText().length() > max_size) {
            message += "\nW pole " + field.getName() + " wprowadzono zbyt mało lub zbyt dużo znaków";
            error_counter++;
        }
        else{
            if(!field.getText().matches("[A-Za-z0-9]+[@][a-z0-9]{1,5}[.][a-z]{2,3}")) {
                message += "\nPole " + field.getName() + " nie spełnia założonego wzorca: osoba@domena.pl(.com lub inny)";
                error_counter++;
            }
        }
    }
    public void addressCheck(JTextField field, int max_size){
        address_correctness = true;
        if(field.getText().length() == 0 || field.getText().length() > max_size){
            address_correctness = false;
        }
        else{
            if(!field.getText().matches("[A-Za-zĆŚŁŹŻąęćłńóśźż ]{2,}[,][ ][A-Za-zĆŚŁŹŻąęćłńóśźż0-9]{2,}[ ][0-9]{1,3}[/]?[0-9]{0,2}")){
                address_correctness = false;
            }
        }
    }
    public void jobTypeCheck(JTextField field){
        if(!(field.getText().equals("magazynier") || field.getText().equals("kierownik"))){
            message += "\n" + field.getName();
            error_counter++;
        }
    }
    public void contractCheck(JTextField field){
        if(!field.getText().equals("praca")){
            message += "\n" + field.getName();
            error_counter++;
        }
    }
    public void errorMessage(){
        if(error_counter != 0){
            JOptionPane.showMessageDialog(null, message, "Błąd!", JOptionPane.ERROR_MESSAGE);
            message = "W następujących polach wykryto błędy: ";
        }
    }
    public void errorPhone(boolean var, JTextField number, JFrame window){
        if(!var) {
            JOptionPane.showMessageDialog(window, "Numer telefonu ma nieodpowiednią liczbę znaków lub nie składa się wyłącznie z cyfr!",
                    "Nieprawidłowy numer telefonu!", JOptionPane.ERROR_MESSAGE);
            number.setText("");
        }
    }
    public void errorPass(boolean var, JPasswordField pass1, JPasswordField pass2){
        if(!var){
            JOptionPane.showMessageDialog(null, "Hasła nie są identyczne!",
                    "Błąd hasła!", JOptionPane.ERROR_MESSAGE);
            pass1.setText("");
            pass2.setText("");
        }
    }
    public void errorTwoNumbers(){
        if(!isSecondHigher){
            JOptionPane.showMessageDialog(null, "Minimalna liczba graczy musi być mniejsza/równa maksymalnej liczbie graczy!",
                    "Błąd hasła!", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void errorAddress(){
        if(!address_correctness){
            JOptionPane.showMessageDialog(null, "Błąd w polu Adres! Liczba znaków musi być większa od 0 i nie większa od 50!\nPoprawny wzór to: <Nazwa miejscowości><przecinek><spacja><ulica><spacja><numer domu>(max. 3 cyfry)<ukośnik><numer mieszkania>(max. 2 cyfry).\nNiedozwolone są znaki takie jak '.', '-'.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}
