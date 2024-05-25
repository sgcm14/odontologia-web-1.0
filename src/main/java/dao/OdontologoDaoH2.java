package dao;

import model.Odontologo;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OdontologoDaoH2 implements iDao<Odontologo>{

    private static final Logger logger= Logger.getLogger(OdontologoDaoH2.class);

    private static final String SQL_INSERT="INSERT INTO ODONTOLOGOS (NUMERO_MATRICULA, NOMBRE, APELLIDO) VALUES(?,?,?)";

    private static final String SQL_SELECT_ONE="SELECT * FROM ODONTOLOGOS WHERE ID=?";
    private static final String SQL_SEARCH_ALL = "SELECT * FROM ODONTOLOGOS";

    private static final String SQL_DELETE = "DELETE FROM ODONTOLOGOS WHERE ID=?";
    private static final String SQL_UPDATE = "UPDATE ODONTOLOGOS SET NUMERO_MATRICULA = ?, NOMBRE = ?, APELLIDO = ? WHERE ID = ?";

    @Override
    public Odontologo guardar(Odontologo odontologo) {
        logger.info("iniciando la operacion de guardado");
        Connection connection=null;

        try{
            connection= BD.getConnection();
            logger.info("Conexion a la BD ODONTOLOGOS establecida");
            PreparedStatement psInsert= connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            psInsert.setInt(1, odontologo.getNumeroMatricula());
            psInsert.setString(2, odontologo.getNombre());
            psInsert.setString(3, odontologo.getApellido());
            psInsert.execute(); //CUANDO SE EJECUTE SE VAN A GENERAR ID
            ResultSet rs= psInsert.getGeneratedKeys();
            while (rs.next()){
                odontologo.setId(rs.getInt(1));
            }
            logger.info("Odontologo guardado:"+ odontologo.getNombre());

        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        return odontologo;
    }

    @Override
    public Odontologo buscarPorID(Integer id) {
        logger.info("iniciando la busqueda de un odontologo por id: "+id);
        Connection connection= null;
        Odontologo odontologo= null;
        try{
            connection= BD.getConnection();
            logger.info("Conexion a la BD ODONTOLOGOS establecida");
            PreparedStatement psSelectOne= connection.prepareStatement(SQL_SELECT_ONE);
            psSelectOne.setInt(1,id);
            ResultSet rs= psSelectOne.executeQuery();
            while(rs.next()){
                Integer idOdontologo = rs.getInt(1);
                Integer numeroMatricula = rs.getInt(2);
                String nombre= rs.getString(3);
                String apellido = rs.getString(4);
                odontologo = new Odontologo(idOdontologo,numeroMatricula,nombre,apellido);
            }

            if(odontologo!= null){
                logger.info("PACIENTE encontrado: " + odontologo.toString());
            }else{
                logger.warn("No se encontró ningún PACIENTE con el ID proporcionado: " + id);
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
        return odontologo;
    }

    @Override
    public Odontologo actualizar(Odontologo odontologo) {
        logger.info("Iniciando la operación de actualización de odontologo");
        Connection connection = null;
        try {
            connection = BD.getConnection();
            logger.info("Conexión a la BD ODONTOLOGOS establecida");

            PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE);
            psUpdate.setInt(1, odontologo.getNumeroMatricula());
            psUpdate.setString(2, odontologo.getNombre());
            psUpdate.setString(3, odontologo.getApellido());
            psUpdate.setInt(4, odontologo.getId());
            psUpdate.executeUpdate();
            logger.info("Odontologo actualizado:  ID: " + odontologo.getId() +  ", NOMBRE: " + odontologo.getNombre());

        } catch (Exception e) {
            logger.warn(e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                logger.warn(e.getMessage());
            }
        }
        return odontologo;
    }

    @Override
    public void eliminar(Integer id) {
        logger.info("Iniciando la operación de eliminación de odontologo con id: " + id);
        Connection connection = null;
        try {
            connection = BD.getConnection();
            logger.info("Conexion a la BD ODONTOLOGOS establecida");

            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
            preparedStatement.setInt(1, id);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Odontologo eliminado con éxito."+ id);
            } else {
                logger.warn("No se encontró ningún ODONTOLOGO con el ID proporcionado: " + id);
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

    public List<Odontologo> buscarTodos() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Odontologo> odontologos = new ArrayList<>();
        try {
            //1 Levantar el driver y Conectarnos
            connection = BD.getConnection();
            logger.info("Conexion a la BD Odontologos establecida");

            //2 Crear una sentencia
            preparedStatement = connection.prepareStatement(SQL_SEARCH_ALL);

            //3 Ejecutar una sentencia SQL
            ResultSet result = preparedStatement.executeQuery();

            //4 Obtener resultados
            while (result.next()) {
                Integer idOdontologo = result.getInt(1);
                Integer numeroMatricula = result.getInt(2);
                String nombre = result.getString(3);
                String apellido = result.getString(4);
                odontologos.add(new Odontologo(idOdontologo, numeroMatricula, nombre, apellido));
            }

            if(odontologos.size()>0){
                logger.info("Odontologos encontrados"+ odontologos.toString());
            }else{
                logger.warn("La BD odontologos se encuentra vacia");
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
        return odontologos;
    }
}
