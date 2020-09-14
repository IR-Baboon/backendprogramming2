package forreal.DAO;

import forreal.Domein.OVChipkaart;
import forreal.Domein.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface OVChipkaartDAO {
    boolean save(OVChipkaart kaart) throws SQLException;
    boolean update(OVChipkaart kaart) throws SQLException;
    boolean delete(OVChipkaart kaart) throws SQLException;
    List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException;
    List<OVChipkaart> findAll() throws SQLException;
}
