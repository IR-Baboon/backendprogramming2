package forreal;

import forreal.Domein.Adres;
import forreal.Domein.Reiziger;
import org.postgresql.core.SqlCommand;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static void packagingErrorHandler(SQLException error){
        error.printStackTrace();
    }

    public static Boolean errorHandler(SQLException error, Connection conn){
        if (conn != null) {
            try {
                System.err.println("[SQL Error!] Query could not be completed " + error.getMessage());
                conn.rollback();
                return false;
            } catch (SQLException excep) {
                System.err.println("[SQL Error!] failed operation rollback " + excep.getMessage());
                return false;
            }
        }
        return false;
    }
}
