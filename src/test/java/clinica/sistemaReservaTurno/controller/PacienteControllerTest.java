package clinica.sistemaReservaTurno.controller;
import clinica.sistemaReservaTurno.entity.Domicilio;
import clinica.sistemaReservaTurno.entity.Paciente;
import clinica.sistemaReservaTurno.service.PacienteService;

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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void listarTodosLosPacientes() throws Exception {
        Domicilio domicilio1 = new Domicilio("Calle Falsa", 123, "Springfield", "USA");
        Paciente paciente1 = new Paciente("Juan", "Perez", "12345678", LocalDate.now(), domicilio1, "juanperez@example.com");

        pacienteService.guardarPaciente(paciente1);

        Domicilio domicilio2 = new Domicilio("Av. Siempre Viva", 456, "Shelbyville", "USA");
        Paciente paciente2 = new Paciente("Ana", "Gomez", "87654321", LocalDate.now(), domicilio2, "anagomez@example.com");

        pacienteService.guardarPaciente(paciente2);

        MvcResult respuesta = mockMvc.perform(MockMvcRequestBuilders.get("/pacientes")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void guardarPaciente() throws Exception {
        Domicilio domicilio = new Domicilio("Calle Nueva", 789, "Capitol City", "USA");
        Paciente paciente = new Paciente("Carlos", "Lopez", "12378945", LocalDate.now(), domicilio, "carloslopez@example.com");


        MvcResult respuesta = mockMvc.perform(MockMvcRequestBuilders.post("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paciente)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void actualizarPaciente() throws Exception {
        Domicilio domicilio = new Domicilio("Calle Vieja", 321, "Old Town", "USA");
        Paciente pacienteExistente = new Paciente("Laura", "Martinez", "56789012", LocalDate.now(), domicilio, "laura.martinez@example.com");

        Paciente pacienteGuardado = pacienteService.guardarPaciente(pacienteExistente);

        Domicilio domicilioActualizado = new Domicilio("Calle Actualizada", 321, "New Town", "USA");
        Paciente pacienteActualizado = new Paciente(pacienteGuardado.getId(), "Laura", "Martinez", "56789012", LocalDate.now(), domicilioActualizado, "laura.martinez@example.com");


        MvcResult respuesta = mockMvc.perform(MockMvcRequestBuilders.put("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pacienteActualizado)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertTrue(respuesta.getResponse().getContentAsString().contains("paciente actualizado"));

        Paciente pacienteVerificado = pacienteService.buscarPorID(pacienteGuardado.getId()).orElse(null);
        assertTrue(pacienteVerificado != null && "Calle Actualizada".equals(pacienteVerificado.getDomicilio().getCalle()));
    }

    @Test
    public void buscarPacientePorId() throws Exception {
        Domicilio domicilio = new Domicilio("Calle Blanca", 123, "White City", "USA");
        Paciente paciente = new Paciente("Luis", "Ramirez", "45612378", LocalDate.now(), domicilio, "luis.ramirez@example.com");

        Paciente pacienteGuardado = pacienteService.guardarPaciente(paciente);

        MvcResult respuesta = mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{id}", pacienteGuardado.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Paciente pacienteEncontrado = objectMapper.readValue(respuesta.getResponse().getContentAsString(), Paciente.class);

        assertTrue(pacienteEncontrado != null);
        assertTrue(pacienteEncontrado.getId().equals(pacienteGuardado.getId()));
        assertTrue(pacienteEncontrado.getNombre().equals(pacienteGuardado.getNombre()));
        assertTrue(pacienteEncontrado.getApellido().equals(pacienteGuardado.getApellido()));
        assertTrue(pacienteEncontrado.getEmail().equals(pacienteGuardado.getEmail()));
    }

    @Test
    public void buscarPacientePorEmail() throws Exception {
        Domicilio domicilio = new Domicilio("Calle Roja", 321, "Red City", "USA");
        Paciente paciente = new Paciente("Marta", "Fernandez", "78912345", LocalDate.now(), domicilio, "marta.fernandez@example.com");

        Paciente pacienteGuardado = pacienteService.guardarPaciente(paciente);

        MvcResult respuesta = mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/email/{email}", pacienteGuardado.getEmail())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Paciente pacienteEncontrado = objectMapper.readValue(respuesta.getResponse().getContentAsString(), Paciente.class);

        assertTrue(pacienteEncontrado != null);
        assertTrue(pacienteEncontrado.getEmail().equals(pacienteGuardado.getEmail()));
    }

    @Test
    public void eliminarPaciente() throws Exception {
        Domicilio domicilio = new Domicilio("Calle Verde", 789, "Green City", "USA");
        Paciente paciente = new Paciente("Pedro", "Gonzalez", "32178945", LocalDate.now(), domicilio, "pedro.gonzalez@example.com");

        Paciente pacienteGuardado = pacienteService.guardarPaciente(paciente);

        MvcResult respuesta = mockMvc.perform(MockMvcRequestBuilders.delete("/pacientes/eliminar/{id}", pacienteGuardado.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertTrue(respuesta.getResponse().getContentAsString().contains("paciente eliminado con exito"));

        assertFalse(pacienteService.buscarPorID(pacienteGuardado.getId()).isPresent());
    }
}
