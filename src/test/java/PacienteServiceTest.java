import dao.BD;
import model.Domicilio;
import model.Paciente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.PacienteService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PacienteServiceTest {

   private PacienteService pacienteService;
   private Paciente paciente;

   @BeforeEach
   public void config() {
      BD.crearTablas();
      pacienteService = new PacienteService();
      LocalDate fecha = LocalDate.of(2024, 5, 18);
      Domicilio domicilio = new Domicilio("Calle Falsa", 123, "Springfield", "USA");
      Paciente pacienteNuevo = new Paciente("John", "Doe", "987654321", fecha, domicilio);
      paciente = pacienteService.guardarPaciente(pacienteNuevo);
   }
   @Test
    public void buscarUnPaciente(){
       /*PacienteService pacienteService= new PacienteService();
       Integer busqueda= 1;
       Paciente paciente= pacienteService.buscarPaciente(busqueda);
       Assertions.assertTrue(paciente!=null);*/
      Paciente buscado = pacienteService.buscarPaciente(paciente.getId());
      assertNotNull(buscado);
      assertEquals(paciente.getNombre(), buscado.getNombre());
   }
   @Test
   public  void listarPacientes() {
      List<Paciente> pacientes = pacienteService.buscarTodos();
      assertTrue(pacientes.size() > 0);
   }

   @Test
   public  void guardarPaciente() {
      LocalDate fecha = LocalDate.of(2024, 5, 18);
      Domicilio domicilio = new Domicilio("Las Manzanas",8,"Springfield","PERU");
      Paciente nuevoPaciente  = new Paciente("Amalia","Anto","798564", fecha,domicilio);
      Paciente guardado = pacienteService.guardarPaciente(nuevoPaciente);
      assertNotNull(guardado);
      assertNotNull(guardado.getId());
   }

   @Test
   public void eliminarPaciente() {
      // Guardar un nuevo paciente
      LocalDate fecha = LocalDate.of(2024, 5, 18);
      Domicilio domicilio = new Domicilio("Cerro colorado", 8, "Surco", "PERU");
      Paciente nuevoPaciente = new Paciente("Sammy", "Cantoral", "984512", fecha, domicilio);
      paciente = pacienteService.guardarPaciente(nuevoPaciente);

     // Verificar que el paciente se guardó correctamente
      Assertions.assertNotNull(paciente.getId());

      // Eliminar el paciente
      pacienteService.eliminarPaciente(paciente.getId());

      // Verificar que el paciente fue eliminado
      Paciente pacienteEliminado = pacienteService.buscarPaciente(paciente.getId());
      Assertions.assertNull(pacienteEliminado);

      // Verificar que el domicilio también fue eliminado
      Domicilio domicilioEliminado = pacienteService.buscarDomicilio(paciente.getDomicilio().getId());
      Assertions.assertNull(domicilioEliminado);
   }

   @Test
   public void actualizarPaciente() {
      paciente.setNombre("Jane");
      paciente.getDomicilio().setCalle("Nueva Calle");
      paciente.getDomicilio().setLocalidad("Piura");
      pacienteService.actualizarPaciente(paciente);

      Paciente actualizado = pacienteService.buscarPaciente(paciente.getId());
      assertNotNull(actualizado);
      assertEquals("Jane", actualizado.getNombre());
      assertEquals("Nueva Calle", actualizado.getDomicilio().getCalle());
   }
}
