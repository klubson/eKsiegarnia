import views.Start_window;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Start_window start = new Start_window();
        start.create();
    }
}
