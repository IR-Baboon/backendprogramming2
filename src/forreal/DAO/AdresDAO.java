package forreal.DAO;

import forreal.Domein.Adres;
import forreal.Domein.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface AdresDAO {
    Boolean save(Adres adres) throws SQLException;
    Boolean update(Adres adres);
    Boolean delete(Adres adres);
    Adres findById(int id);
    Adres findByReiziger(Reiziger reiziger);
    List<Adres> findAll();
}
