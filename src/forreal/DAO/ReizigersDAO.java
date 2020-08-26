package forreal.DAO;

import forreal.Domein.Reiziger;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface ReizigersDAO {
    public Boolean save(Reiziger reiziger) throws SQLException;
    public Boolean update(Reiziger reiziger);
    public Boolean delete(Reiziger reiziger);
    public Reiziger findById(int id);
    public List<Reiziger> findByGbdatum(String datum);
    public List<Reiziger> findAll();
}
