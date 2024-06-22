package clinica.sistemaReservaTurno.service;
import clinica.sistemaReservaTurno.entity.Domicilio;
import clinica.sistemaReservaTurno.entity.Odontologo;
import clinica.sistemaReservaTurno.entity.Paciente;
import clinica.sistemaReservaTurno.entity.Turno;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TurnoServiceTest {

    @Autowired
    private TurnoService turnoService;

    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private OdontologoService odontologoService;


    @Test
    @Order(1)
    void guardarTurno() {
        Paciente pacienteGuardado = pacienteService.guardarPaciente(new Paciente("Jorgito", "Pereyra", "111111", LocalDate.of(2024, 6, 19), new Domicilio("Calle falsa", 123, "La Rioja", "Argentina"), "jorgito@digitalhouse.com"));
        Odontologo odontologoGuardado = odontologoService.guardarOdontologo(new Odontologo("MP10", "Ivan", "Bustamante"));

        Turno turno = new Turno(pacienteGuardado, odontologoGuardado,LocalDateTime.of(2024,06,15,06,44,00));

        Turno turnoGuardado = turnoService.guardarTurno(turno);

        assertEquals(1L, turnoGuardado.getId());
    }

    @Test
    @Order(2)
    void buscarTurnoPorID() {
        Long id= 1L;
        Optional<Turno> turnoBuscado = turnoService.buscarPorID(id);

        assertNotNull(turnoBuscado.get());
    }

    @Test
    @Order(3)
    public void actualizarTurnoTest(){
        Optional<Turno> turnoBuscado= turnoService.buscarPorID(1L);
        if(turnoBuscado.isPresent()){
            turnoBuscado.get().setFechaHoraCita(LocalDateTime.of(2024,07,15,06,44,00));
        }
        turnoService.actualizarTurno(turnoBuscado.get());
        assertEquals(LocalDateTime.of(2024, 07, 15, 06, 44, 00),turnoBuscado.get().getFechaHoraCita());
    }

    @Test
    @Order(4)
    public void buscarTodos() {
        List<Turno> turnos = turnoService.buscarTodos();
        assertEquals(1, turnos.size());
    }

    @Test
    @Order(5)
    void eliminarTurno() {
        turnoService.eliminarTurno(1L);
        Optional<Turno> turnoBuscado= turnoService.buscarPorID(1L);
        assertFalse(turnoBuscado.isPresent());
    }
}
