package forreal.DAO;

import forreal.Domein.Reiziger;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface ReizigersDAO {
    Boolean save(Reiziger reiziger) throws SQLException;
    Boolean update(Reiziger reiziger);
    Boolean delete(Reiziger reiziger);
    Reiziger findById(int id);
    List<Reiziger> findByGbdatum(String datum);
    List<Reiziger> findAll();
}
