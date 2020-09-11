package forreal.DAO;

import forreal.Domein.Adres;
import forreal.Domein.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface AdresDAO {
    Boolean save(Adres adres) throws SQLException;
    Boolean update(Adres adres) throws SQLException;
    Boolean delete(Adres adres) throws SQLException;
    Adres findByReiziger(Reiziger reiziger) throws SQLException;
    List<Adres> findAll() throws SQLException;
}
