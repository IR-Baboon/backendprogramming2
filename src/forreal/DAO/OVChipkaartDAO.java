package forreal.DAO;

import forreal.Domein.Adres;
import forreal.Domein.OV_Chipkaart;
import forreal.Domein.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface OVChipkaartDAO {
    Boolean save(OV_Chipkaart kaart) throws SQLException;
    Boolean update(OV_Chipkaart kaart) throws SQLException;
    Boolean delete(OV_Chipkaart kaart) throws SQLException;
    List<OV_Chipkaart> findByReiziger(Reiziger reiziger) throws SQLException;
    List<OV_Chipkaart> findAll() throws SQLException;
}
