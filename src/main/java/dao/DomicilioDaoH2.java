package dao;

import model.Domicilio;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.List;

public class DomicilioDaoH2 implements iDao<Domicilio>{
    private static final String SQL_SELECT_ONE="SELECT * FROM DOMICILIOS WHERE ID=?";
    private static final String SQL_INSERT="INSERT INTO DOMICILIOS (CALLE, NUMERO, LOCALIDAD, PROVINCIA) VALUES(?,?,?,?)";

    private static final String SQL_DELETE = "DELETE FROM DOMICILIOS WHERE ID=?";

    private static final String SQL_UPDATE = "UPDATE DOMICILIOS SET CALLE = ?, NUMERO = ?, LOCALIDAD = ?, PROVINCIA = ? WHERE ID = ?";
    private static final Logger logger = Logger.getLogger(DomicilioDaoH2.class);

    @Override
    public Domicilio guardar(Domicilio domicilio) {

        logger.info("iniciando la operacion de guardado");
        Connection connection=null;
        try{
            connection= BD.getConnection();
            logger.info("Conexion a la BD PACIENTES establecida");
            PreparedStatement psInsert= connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);

            psInsert.setString(1, domicilio.getCalle());
            psInsert.setInt(2, domicilio.getNumero());
            psInsert.setString(3, domicilio.getLocalidad());
            psInsert.setString(4, domicilio.getProvincia());
            psInsert.execute(); //CUANDO SE EJECUTE SE VAN A GENERAR ID
            ResultSet rs= psInsert.getGeneratedKeys();
            while (rs.next()){
                domicilio.setId(rs.getInt(1));
            }
            logger.info("Domicilio guardado con ID: " + domicilio.getId());

        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            try{
                connection.close();
            }catch (SQLException ex){
                ex.getMessage();
            }
        }
        return domicilio;
    }

    @Override
    public Domicilio buscarPorID(Integer id) {
        logger.info("iniciando la busqueda de un domicilio por id: "+id);
        Connection connection = null;
        Domicilio domicilio = null;
        try {
            //1 Levantar el driver y Conectarnos
            connection = BD.getConnection();
            logger.info("Conexion a la BD DOMICILIOS establecida");

            //2 Crear una sentencia
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ONE);
            preparedStatement.setInt(1,id);

            //3 Ejecutar una sentencia SQL
            ResultSet result = preparedStatement.executeQuery();

            //4 Obtener resultados
            while (result.next()) {
                Integer idDomicilio = result.getInt(1);
                String calle = result.getString(2);
                Integer numero = result.getInt(3);
                String localidad = result.getString(4);
                String provincia = result.getString(5);
                domicilio = new Domicilio(idDomicilio, calle, numero, localidad, provincia);
            }
            /*if(domicilio!= null){
                logger.info("DOMICILIO encontrado: " + domicilio.toString());
            }else{
                logger.warn("No se encontró ningún DOMICILIO con el ID proporcionado: " + id);
            }*/

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            try{
                connection.close();
            }catch (SQLException ex){
                ex.getMessage();
            }
        }

        return domicilio;
    }

    @Override
    public Domicilio actualizar(Domicilio domicilio) {
        logger.info("Iniciando la operación de actualización de domicilio"+domicilio.getId());
        Connection connection = null;
        try {
            connection = BD.getConnection();
            logger.info("Conexión a la BD DOMICILIOS establecida");

           /* PreparedStatement psSelectOne = connection.prepareStatement(SQL_SELECT_ONE);
            psSelectOne.setInt(1, id);
            ResultSet rs = psSelectOne.executeQuery();

            if (rs.next()) {
                logger.info("Domicilio encontrado con ID: "+ id);
                PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE);
                psUpdate.setString(1, domicilio.getCalle());
                psUpdate.setInt(2, domicilio.getNumero());
                psUpdate.setString(3, domicilio.getLocalidad());
                psUpdate.setString(4, domicilio.getProvincia());
                psUpdate.setInt(5, id);
                psUpdate.executeUpdate();
                logger.info("Domicilio actualizado: " + id);
            }*/
            PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE);
            psUpdate.setString(1, domicilio.getCalle());
            psUpdate.setInt(2, domicilio.getNumero());
            psUpdate.setString(3, domicilio.getLocalidad());
            psUpdate.setString(4, domicilio.getProvincia());
            psUpdate.setInt(5, domicilio.getId());
            psUpdate.executeUpdate();
            logger.info("Domicilio actualizado: " + domicilio.getId());

        } catch (Exception e) {
            logger.warn(e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                logger.warn(e.getMessage());
            }
        }
        return domicilio;
    }


    @Override
    public void eliminar(Integer id) {
        logger.info("Iniciando la operación de eliminación de domicilio con id: " + id);
        Connection connection = null;
        try {
            connection = BD.getConnection();
            logger.info("Conexion a la BD DOMICILIOS establecida");

            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
            preparedStatement.setInt(1, id);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Domicilio eliminado con éxito."+ id);
            } else {
                logger.warn("No se encontró ningún DOMICILIO con el ID proporcionado: " + id);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            try{
                connection.close();
            }catch (SQLException ex){
                ex.getMessage();
            }
        }
    }

    @Override
    public List<Domicilio> buscarTodos() {
        return List.of();
    }
}
