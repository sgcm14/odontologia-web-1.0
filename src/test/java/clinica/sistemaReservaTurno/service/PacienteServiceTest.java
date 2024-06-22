package clinica.sistemaReservaTurno.service;
import clinica.sistemaReservaTurno.entity.Domicilio;
import clinica.sistemaReservaTurno.entity.Paciente;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PacienteServiceTest {
    @Autowired
    private PacienteService pacienteService;

    @Test
    @Order(1)
    public void guardarPaciente(){
        Paciente paciente= new Paciente("Jorgito","Pereyra","111111", LocalDate.of(2024,6,19),new Domicilio("Calle falsa",123,"La Rioja","Argentina"),"jorgito@digitalhouse.com");
        Paciente pacienteGuardado= pacienteService.guardarPaciente(paciente);
        assertEquals(1L,pacienteGuardado.getId());
    }

    @Test
    @Order(2)
    public void buscarPacientePorId(){
        Long id= 1L;
        Optional<Paciente> pacienteBuscado= pacienteService.buscarPorID(id);
        assertNotNull(pacienteBuscado.get());
    }

    @Test
    @Order(3)
    public void actualizarPacienteTest(){
        Optional<Paciente> pacienteBuscado= pacienteService.buscarPorID(1L);
        if(pacienteBuscado.isPresent()){
            pacienteBuscado.get().setApellido("Perez");
        }
        pacienteService.actualizarPaciente(pacienteBuscado.get());
        assertEquals("Perez",pacienteBuscado.get().getApellido());
    }

    @Test
    @Order(4)
    public void buscarTodos(){
        List<Paciente> pacientes= pacienteService.buscarTodos();
        assertEquals(1,pacientes.size());
    }

    @Test
    @Order(5)
    public void eliminarPaciente(){
        pacienteService.eliminarPaciente(1L);
        Optional<Paciente> pacienteBuscado= pacienteService.buscarPorID(1L);
        assertFalse(pacienteBuscado.isPresent());
    }
}
