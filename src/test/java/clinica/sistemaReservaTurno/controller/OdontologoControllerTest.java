package clinica.sistemaReservaTurno.controller;
import clinica.sistemaReservaTurno.entity.Odontologo;
import clinica.sistemaReservaTurno.service.OdontologoService;

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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class OdontologoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OdontologoService odontologoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void listarTodosLosOdontologos() throws Exception {
        odontologoService.guardarOdontologo(new Odontologo("MP10", "Ivan", "Bustamante"));
        odontologoService.guardarOdontologo(new Odontologo("MP20", "Juan", "Perez"));
        odontologoService.guardarOdontologo(new Odontologo("MP30", "Carlos", "Lopez"));

        MvcResult respuesta = mockMvc.perform(MockMvcRequestBuilders.get("/odontologos")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void guardarOdontologo() throws Exception {
        Odontologo odontologo = new Odontologo("MP05", "Diego", "Torres");

        MvcResult respuesta = mockMvc.perform(MockMvcRequestBuilders.post("/odontologos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(odontologo)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void actualizarOdontologo() throws Exception {

        Odontologo odontologoExistente = new Odontologo("MP30", "Carlos", "Lopez");

        Odontologo odontologoGuardado = odontologoService.guardarOdontologo(odontologoExistente);

        Odontologo odontologoActualizado = new Odontologo("MP30", "Carlos", "Lopez Actualizado");

        odontologoActualizado.setId(odontologoGuardado.getId());


        MvcResult respuesta = mockMvc.perform(MockMvcRequestBuilders.put("/odontologos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(odontologoActualizado)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertTrue(respuesta.getResponse().getContentAsString().contains("odontologo actualizado"));

        Odontologo odontologoVerificado = odontologoService.buscarPorID(odontologoGuardado.getId()).orElse(null);
        assertTrue(odontologoVerificado != null && "Lopez Actualizado".equals(odontologoVerificado.getApellido()));
    }

    @Test
    public void buscarOdontologoPorId() throws Exception {
        Odontologo odontologo = new Odontologo("MP50", "Laura", "Martinez");

        Odontologo odontologoGuardado = odontologoService.guardarOdontologo(odontologo);

        MvcResult respuesta = mockMvc.perform(MockMvcRequestBuilders.get("/odontologos/{id}", odontologoGuardado.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Odontologo odontologoEncontrado = objectMapper.readValue(respuesta.getResponse().getContentAsString(), Odontologo.class);

        assertTrue(odontologoEncontrado != null);
        assertTrue(odontologoEncontrado.getId().equals(odontologoGuardado.getId()));
        assertTrue(odontologoEncontrado.getNombre().equals(odontologoGuardado.getNombre()));
        assertTrue(odontologoEncontrado.getApellido().equals(odontologoGuardado.getApellido()));
        assertTrue(odontologoEncontrado.getNumeroMatricula().equals(odontologoGuardado.getNumeroMatricula()));
    }

    @Test
    public void eliminarOdontologo() throws Exception {
        Odontologo odontologo = new Odontologo("MP60", "Pedro", "Gonzalez");

        Odontologo odontologoGuardado = odontologoService.guardarOdontologo(odontologo);

        MvcResult respuesta = mockMvc.perform(MockMvcRequestBuilders.delete("/odontologos/eliminar/{id}", odontologoGuardado.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertTrue(respuesta.getResponse().getContentAsString().contains("odontologo eliminado con exito"));

        assertFalse(odontologoService.buscarPorID(odontologoGuardado.getId()).isPresent());
    }
}
