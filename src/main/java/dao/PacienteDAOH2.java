package dao;

import model.Domicilio;
import model.Paciente;
import org.apache.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAOH2 implements iDao<Paciente> {

    private static final Logger logger= Logger.getLogger(PacienteDAOH2.class);
    private static final String SQL_INSERT="INSERT INTO PACIENTES (NOMBRE, APELLIDO, CEDULA, FECHA_INGRESO, DOMICILIO_ID) VALUES(?,?,?,?,?)";
    private static final String SQL_SELECT_ONE="SELECT * FROM PACIENTES WHERE ID=?";
    private static final String SQL_SEARCH_ALL = "SELECT * FROM PACIENTES";
    private static final String SQL_DELETE = "DELETE FROM PACIENTES WHERE ID=?";
    private static final String SQL_UPDATE = "UPDATE PACIENTES SET NOMBRE = ?, APELLIDO = ?, CEDULA = ?, FECHA_INGRESO = ?, DOMICILIO_ID = ? WHERE ID = ?";

    @Override
    public Paciente guardar(Paciente paciente) {
        logger.info("iniciando la operacion de guardado");
        Connection connection= null;
        try{
            connection= BD.getConnection();
            logger.info("Conexion a la BD PACIENTES establecida");
            PreparedStatement psInsert= connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            psInsert.setString(1, paciente.getNombre());
            psInsert.setString(2, paciente.getApellido());
            psInsert.setString(3, paciente.getCedula());
            psInsert.setDate(4, Date.valueOf(paciente.getFechaIngreso()));
            psInsert.setInt(5,paciente.getDomicilio().getId());
            psInsert.execute(); //CUANDO SE EJECUTE SE VAN A GENERAR ID
            ResultSet rs= psInsert.getGeneratedKeys();
            while (rs.next()){
                paciente.setId(rs.getInt(1));
            }
            logger.info("Paciente guardado:"+ paciente.getNombre());

        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        return paciente;
    }

    @Override
    public Paciente buscarPorID(Integer id) {
        logger.info("iniciando la busqueda de un paciente por id: "+id);
        Connection connection= null;
        Paciente paciente= null;
        Domicilio domicilio= null;
        try{
            connection= BD.getConnection();
            PreparedStatement psSelectOne= connection.prepareStatement(SQL_SELECT_ONE);
            psSelectOne.setInt(1,id);
            ResultSet rs= psSelectOne.executeQuery();
            DomicilioDaoH2 domAux= new DomicilioDaoH2();
            while(rs.next()){
                domicilio= domAux.buscarPorID(rs.getInt(6));
                paciente= new Paciente(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getDate(5).toLocalDate(),domicilio);
            }
            if(paciente!= null){
                logger.info("PACIENTE encontrado: " + paciente.toString());
            }else{
                logger.warn("No se encontró ningún PACIENTE con el ID proporcionado: " + id);
            }


        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        return paciente;
    }

    @Override
    public Paciente actualizar(Paciente paciente) {
        logger.info("Iniciando la operación de actualización de paciente");
        Connection connection = null;
        Paciente pacienteActualizado = null;
        try {
            connection = BD.getConnection();
            logger.info("Conexión a la BD PACIENTES establecida");

            PreparedStatement psSelectOne = connection.prepareStatement(SQL_SELECT_ONE);
            psSelectOne.setInt(1, paciente.getId());
            ResultSet rs = psSelectOne.executeQuery();
            if (rs.next()) {
                logger.info("Paciente encontrado con ID: "+ paciente.getId());
                // Actualizar domicilio
                Domicilio domicilio = paciente.getDomicilio();
                if (domicilio != null && domicilio.getId() != null) {
                    DomicilioDaoH2 domicilioDaoH2 = new DomicilioDaoH2();
                    domicilio.setId(rs.getInt(6));
                    domicilio = domicilioDaoH2.actualizar(domicilio);
                    paciente.setDomicilio(domicilio);
                }

                PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE);
                psUpdate.setString(1, paciente.getNombre());
                psUpdate.setString(2, paciente.getApellido());
                psUpdate.setString(3, paciente.getCedula());
                psUpdate.setDate(4, Date.valueOf(paciente.getFechaIngreso()));
                psUpdate.setInt(5, paciente.getDomicilio().getId());
                psUpdate.setInt(6, paciente.getId());
                psUpdate.executeUpdate();
                logger.info("Paciente actualizado:  ID: " + paciente.getId() +  ", NOMBRE: " + paciente.getNombre());

                pacienteActualizado = new Paciente(paciente.getId(),paciente.getNombre(),paciente.getApellido(), paciente.getCedula(), paciente.getFechaIngreso(), domicilio);
            }
            else {
                logger.info("Paciente no actualizado:  ID: " + paciente.getId() +  " no encontrado");
            }

        } catch (Exception e) {
            logger.warn(e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                logger.warn(e.getMessage());
            }
        }
        return pacienteActualizado;
    }

    @Override
    public void eliminar(Integer id) {
        logger.info("Iniciando la operación de eliminación de paciente con id: " + id);
        Connection connection = null;
        try {
            connection = BD.getConnection();

            // Obtener el domicilio_id del paciente antes de eliminarlo
            PreparedStatement psSelectOne = connection.prepareStatement(SQL_SELECT_ONE);
            psSelectOne.setInt(1, id);
            ResultSet rs = psSelectOne.executeQuery();
            Integer domicilioId = null;
            if (rs.next()) {
                domicilioId = rs.getInt("DOMICILIO_ID");
            }

            // Eliminar el paciente
            PreparedStatement psDelete = connection.prepareStatement(SQL_DELETE);
            psDelete.setInt(1, id);
            int affectedRows = psDelete.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Paciente eliminado con éxito.");

                // Verificar si el domicilio está asociado a otros pacientes antes de eliminarlo
                PreparedStatement psRevisarDomicilio = connection.prepareStatement("SELECT COUNT(*) FROM PACIENTES WHERE DOMICILIO_ID=?");
                psRevisarDomicilio.setInt(1, domicilioId);
                ResultSet rsRevisarDomicilio = psRevisarDomicilio.executeQuery();
                if (rsRevisarDomicilio.next() && rsRevisarDomicilio.getInt(1) == 0) {
                    DomicilioDaoH2 domicilioDaoH2 = new DomicilioDaoH2();
                    domicilioDaoH2.eliminar(domicilioId);
                }
            } else {
                logger.warn("No se encontró ningún PACIENTE con el ID proporcionado: " + id);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        } finally {
            try{
                connection.close();
            }catch (SQLException ex){
                ex.getMessage();
            }
        }
    }


    @Override
    public List<Paciente> buscarTodos() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Paciente> pacientes = new ArrayList<>();
        Domicilio domicilio = null;
        try {
            //1 Levantar el driver y Conectarnos
            connection = BD.getConnection();
            logger.info("Conexion a la BD PACIENTES establecida");

            //2 Crear una sentencia
            preparedStatement = connection.prepareStatement(SQL_SEARCH_ALL);

            //3 Ejecutar una sentencia SQL
            ResultSet result = preparedStatement.executeQuery();
            DomicilioDaoH2 domicilioDaoH2 = new DomicilioDaoH2();
            //4 Obtener resultados
            while (result.next()) {
                domicilio = domicilioDaoH2.buscarPorID(result.getInt(6));
                Integer idPaciente = result.getInt(1);
                String nombre = result.getString(2);
                String apellido = result.getString(3);
                String cedula = result.getString(4);
                LocalDate fecha = result.getDate(5).toLocalDate();
                pacientes.add(new Paciente(idPaciente, nombre, apellido, cedula, fecha, domicilio));
            }

            if(pacientes.size()>0){
                logger.info("Pacientes encontrados" + pacientes.toString());
            }else{
                logger.warn("La BD PACIENTES se encuentra vacia");
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
        return pacientes;

    }


}
