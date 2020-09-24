package forreal.DAO;

import forreal.Domein.OVChipkaart;
import forreal.Domein.Product;
import forreal.Domein.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    boolean save(Product product) throws SQLException;
    boolean update(Product product) throws SQLException;
    boolean delete(Product product) throws SQLException;
    List<Product> findByOVChipkaart(OVChipkaart kaart) throws SQLException;
    List<Product> findAll() throws SQLException;
}
