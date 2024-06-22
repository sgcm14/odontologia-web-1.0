package clinica.sistemaReservaTurno.controller;
import clinica.sistemaReservaTurno.entity.Domicilio;
import clinica.sistemaReservaTurno.entity.Odontologo;
import clinica.sistemaReservaTurno.entity.Paciente;
import clinica.sistemaReservaTurno.entity.Turno;
import clinica.sistemaReservaTurno.service.OdontologoService;
import clinica.sistemaReservaTurno.service.PacienteService;
import clinica.sistemaReservaTurno.service.TurnoService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TurnoControllerTest {
    @Autowired
    private TurnoService turnoService;
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private OdontologoService odontologoService;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void listarTodosLosTurnos() throws Exception {
        Paciente pacienteGuardado = pacienteService.guardarPaciente(new Paciente("Jorgito", "Pereyra", "111111", LocalDate.of(2024, 6, 19), new Domicilio("Calle falsa", 123, "La Rioja", "Argentina"), "jorgito@digitalhouse.com"));

        Odontologo odontologoGuardado = odontologoService.guardarOdontologo(new Odontologo("MP10", "Ivan", "Bustamante"));

        Turno turnoGuardado = turnoService.guardarTurno(new Turno(pacienteGuardado, odontologoGuardado, LocalDateTime.of(2024, 06, 15, 06, 44, 00)));

        MvcResult respuesta = mockMvc.perform(MockMvcRequestBuilders.get("/turnos")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void guardarTurno() throws Exception {
        Domicilio domicilioPaciente = new Domicilio("Calle Falsa", 123, "Springfield", "USA");
        Paciente paciente = new Paciente("Juan", "Perez", "12345678", LocalDate.now(), domicilioPaciente, "juanperez1@example.com");

        pacienteService.guardarPaciente(paciente);

        Odontologo odontologo = new Odontologo("MP10", "Ivan", "Bustamante");

        odontologoService.guardarOdontologo(odontologo);

        Turno turno = new Turno(paciente, odontologo, LocalDateTime.of(2024, 06, 15, 06, 44, 00));


        MvcResult respuesta = mockMvc.perform(MockMvcRequestBuilders.post("/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(turno)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertTrue(respuesta.getResponse().getContentAsString().contains("\"id\""));
    }


    @Test
    public void actualizarTurno() throws Exception {
        Domicilio domicilioPaciente = new Domicilio("Calle Falsa", 123, "Springfield", "USA");
        Paciente paciente = new Paciente("Juan", "Perez", "12345678", LocalDate.now(), domicilioPaciente, "juanperez@example.com");

        pacienteService.guardarPaciente(paciente);

        Odontologo odontologo = new Odontologo("MP10", "Ivan", "Bustamante");

        odontologoService.guardarOdontologo(odontologo);

        LocalDateTime fechaHoraOriginal = LocalDateTime.of(2024, 06, 15, 06, 44, 00);
        Turno turnoInicial = new Turno(paciente, odontologo, fechaHoraOriginal);

        Turno turnoGuardado = turnoService.guardarTurno(turnoInicial);

        LocalDateTime nuevaFechaHora = LocalDateTime.of(2024, 06, 16, 10, 30, 00);

        turnoGuardado.setFechaHoraCita(nuevaFechaHora);


        MvcResult respuesta = mockMvc.perform(MockMvcRequestBuilders.put("/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(turnoGuardado)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertTrue(respuesta.getResponse().getContentAsString().contains("Turno actualizado"));

        Optional<Turno> turnoActualizado = turnoService.buscarPorID(turnoGuardado.getId());

        assertTrue(turnoActualizado.isPresent());
        assertTrue(turnoActualizado.get().getFechaHoraCita().isEqual(nuevaFechaHora));
    }

    @Test
    public void eliminarTurno() throws Exception {
        Domicilio domicilioPaciente = new Domicilio("Calle Falsa", 123, "Springfield", "USA");
        Paciente paciente = new Paciente("Juan", "Perez", "12345678", LocalDate.now(), domicilioPaciente, "juanperez2@example.com");

        pacienteService.guardarPaciente(paciente);

        Odontologo odontologo = new Odontologo("MP10", "Ivan", "Bustamante");

        odontologoService.guardarOdontologo(odontologo);


        LocalDateTime fechaHora = LocalDateTime.of(2024, 06, 15, 06, 44, 00);
        Turno turno = new Turno(paciente, odontologo, fechaHora);

        Turno turnoGuardado = turnoService.guardarTurno(turno);

        MvcResult respuesta = mockMvc.perform(MockMvcRequestBuilders.delete("/turnos/eliminar/{id}", turnoGuardado.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertTrue(respuesta.getResponse().getContentAsString().contains("turno eliminado con exito"));

        assertFalse(turnoService.buscarPorID(turnoGuardado.getId()).isPresent());
    }

    @Test
    public void buscarTurnoPorId() throws Exception {
        Domicilio domicilioPaciente = new Domicilio("Calle Falsa", 123, "Springfield", "USA");
        Paciente paciente = new Paciente("Juan", "Perez", "12345678", LocalDate.now(), domicilioPaciente, "juanperez5@example.com");

        pacienteService.guardarPaciente(paciente);

        Odontologo odontologo = new Odontologo("MP10", "Ivan", "Bustamante");

        odontologoService.guardarOdontologo(odontologo);

        LocalDateTime fechaHora = LocalDateTime.of(2024, 06, 15, 06, 44, 00);
        Turno turno = new Turno(paciente, odontologo, fechaHora);

        Turno turnoGuardado = turnoService.guardarTurno(turno);

        MvcResult respuesta = mockMvc.perform(MockMvcRequestBuilders.get("/turnos/{id}", turnoGuardado.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();


        Turno turnoEncontrado = objectMapper.readValue(respuesta.getResponse().getContentAsString(), Turno.class);

        assertEquals(turnoGuardado.getId(), turnoEncontrado.getId());
        assertEquals(turnoGuardado.getFechaHoraCita(), turnoEncontrado.getFechaHoraCita());
        assertEquals(turnoGuardado.getPaciente().getNombre(), turnoEncontrado.getPaciente().getNombre());
        assertEquals(turnoGuardado.getOdontologo().getNombre(), turnoEncontrado.getOdontologo().getNombre());
    }

}
