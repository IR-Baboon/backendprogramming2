package forreal.DAO;

import forreal.Domein.Reiziger;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface ReizigersDAO {
    Boolean save(Reiziger reiziger) throws SQLException;
    Boolean update(Reiziger reiziger) throws SQLException;
    Boolean delete(Reiziger reiziger) throws SQLException;
    Reiziger findById(int id) throws SQLException;
    List<Reiziger> findByGbdatum(String datum) throws SQLException;
    List<Reiziger> findAll() throws SQLException;
}
