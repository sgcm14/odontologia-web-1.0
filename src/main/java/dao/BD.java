package dao;

import org.apache.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;

public class BD {
 private static final Logger logger= Logger.getLogger(BD.class);
    private static final String SQL_DROP_CREATE_PACIENTES="DROP TABLE IF EXISTS PACIENTES; " +
            "CREATE TABLE PACIENTES (ID INT AUTO_INCREMENT PRIMARY KEY, NOMBRE VARCHAR(100) NOT NULL, APELLIDO VARCHAR(100) NOT NULL, " +
           "CEDULA VARCHAR(50) NOT NULL, FECHA_INGRESO DATE NOT NULL, DOMICILIO_ID INT NOT NULL ) "; //<--- fk
    private static final String SQL_DROP_CREATE_DOMICILIOS="DROP TABLE IF EXISTS DOMICILIOS; " +
            "CREATE TABLE DOMICILIOS (ID INT AUTO_INCREMENT PRIMARY KEY, CALLE VARCHAR(100)  NOT NULL, NUMERO INT NOT NULL, LOCALIDAD VARCHAR(100)  NOT NULL, " +
            "PROVINCIA VARCHAR(100)  NOT NULL)";
    private static final String SQL_PRUEBA="INSERT INTO PACIENTES (NOMBRE, APELLIDO, CEDULA, FECHA_INGRESO, DOMICILIO_ID) VALUES('Jorgito','Pereyra','123456','2024-05-15',1),('Juanita','Jimenez','11111111','2024-05-10',2); " +
            "INSERT INTO DOMICILIOS (CALLE, NUMERO, LOCALIDAD, PROVINCIA) VALUES ('Siempre viva',742,'Springfield','USA'), ('Siempre viva',742,'Springfield','USA')";

    private static final String SQL_DROP_CREATE_ODONTOLOGOS="DROP TABLE IF EXISTS ODONTOLOGOS; " +
            "CREATE TABLE ODONTOLOGOS (ID INT AUTO_INCREMENT PRIMARY KEY, NUMERO_MATRICULA INT NOT NULL, NOMBRE VARCHAR(100) NOT NULL, " +
            "APELLIDO VARCHAR(100) NOT NULL ) ";

    private static final String SQL_PRUEBA_ODONTOLOGO="INSERT INTO ODONTOLOGOS (NUMERO_MATRICULA, NOMBRE, APELLIDO) VALUES(123456,'Pepe','Perez'),(123456,'John','Doe'); ";

    private static final String SQL_DROP_CREATE_TURNO="DROP TABLE IF EXISTS TURNOS; " +
            "CREATE TABLE TURNOS (ID INT AUTO_INCREMENT PRIMARY KEY, PACIENTE_ID INT NOT NULL, ODONTOLOGO_ID INT NOT NULL, " +
            "FECHA_HORA_CITA TIMESTAMP NOT NULL ) ";
    //Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
    //private static final String SQL_PRUEBA_TURNO="INSERT INTO TURNOS (PACIENTE_ID, ODONTOLOGO_ID, FECHA_HORA_CITA) VALUES(1,2,2024-05-25 19:19:33); ";
    private static final String SQL_PRUEBA_TURNO = "INSERT INTO TURNOS (PACIENTE_ID, ODONTOLOGO_ID, FECHA_HORA_CITA) VALUES (?, ?, ?)";


    public static void crearTablas(){
        Connection connection= null;
        try{
            connection= getConnection();
            Statement statement= connection.createStatement();
            statement.execute(SQL_DROP_CREATE_DOMICILIOS);
            statement.execute(SQL_DROP_CREATE_PACIENTES);
            statement.execute(SQL_PRUEBA);

            statement.execute(SQL_DROP_CREATE_ODONTOLOGOS);
            statement.execute(SQL_PRUEBA_ODONTOLOGO);
            statement.execute(SQL_DROP_CREATE_TURNO);
            //statement.execute(SQL_PRUEBA_TURNO);
            // Uso de PreparedStatement para insertar el turno con el timestamp actual
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_PRUEBA_TURNO);
            preparedStatement.setInt(1, 1); // PACIENTE_ID
            preparedStatement.setInt(2, 2); // ODONTOLOGO_ID
            preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis())); // FECHA_HORA_CITA
            preparedStatement.executeUpdate();
            logger.info("tabla creada con exito");

        }catch (Exception e){
            logger.warn(e.getMessage());
        }

    }
    public static Connection getConnection() throws Exception{
        Class.forName("org.h2.Driver");
        return DriverManager.getConnection("jdbc:h2:~/C2ClinicaOdontologica","sa","sa");
    }
}
