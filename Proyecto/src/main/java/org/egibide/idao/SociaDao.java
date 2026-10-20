package org.egibide.idao;
import org.egibide.models.Socia;

import java.sql.SQLException;
import java.util.List;


public interface SociaDao {

    List<Socia> listar() throws SQLException;
    void insertar(Socia socia) throws SQLException;
    Socia buscarDocumento(String documento);
    void eliminar (String documento);
    void actualizar (Socia socia);
}
