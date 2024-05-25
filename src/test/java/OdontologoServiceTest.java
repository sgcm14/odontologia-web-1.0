import dao.BD;
import model.Odontologo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.OdontologoService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OdontologoServiceTest {

    private OdontologoService odontologoService;
    private Odontologo odontologo;

    @BeforeEach
    public void config() {
        BD.crearTablas();
        odontologoService = new OdontologoService();
        Odontologo nuevoOdontologo1 = new Odontologo(00001, "Ana", "Gomez");
        Odontologo nuevoOdontologo2 = new Odontologo(00002, "Maria", "Gomez");
        Odontologo nuevoOdontologo3 = new Odontologo(00003, "Martha", "Gomez");
        Odontologo nuevoOdontologo4 = new Odontologo(00004, "Juan", "Gomez");
        Odontologo nuevoOdontologo5 = new Odontologo(00005, "Peter", "Gomez");
        odontologo = odontologoService.guardarOdontologo(nuevoOdontologo1);
        odontologoService.guardarOdontologo(nuevoOdontologo1);
        odontologoService.guardarOdontologo(nuevoOdontologo2);
        odontologoService.guardarOdontologo(nuevoOdontologo3);
        odontologoService.guardarOdontologo(nuevoOdontologo4);
        odontologoService.guardarOdontologo(nuevoOdontologo5);
    }

    @Test
    public void buscarUnOdontologo(){
        Odontologo buscado = odontologoService.buscarOdontologo(odontologo.getId());
        assertNotNull(buscado);
        assertEquals(odontologo.getNombre(), buscado.getNombre());
    }

    @Test
    void listarOdontologos() {
        List<Odontologo> odontologos = odontologoService.buscarTodos();
        assertTrue(odontologos.size()>0);
    }

    @Test
    void guardarOdontologo() {
        Odontologo nuevoOdontologo = new Odontologo(12345, "Diane", "Torres");
        Odontologo guardado = odontologoService.guardarOdontologo(nuevoOdontologo);
        assertNotNull(guardado);
        assertNotNull(guardado.getId());
    }

    @Test
    public void eliminarOdontologo() {

        Odontologo nuevoOdontologo = new Odontologo(12345, "Laura", "Tello");
        odontologo = odontologoService.guardarOdontologo(nuevoOdontologo);

        // Verificar que el odontologo se guardó correctamente
        Assertions.assertNotNull(odontologo.getId());

        // Eliminar el odontologo
        odontologoService.eliminarOdontologo(odontologo.getId());

        // Verificar que el odontologo fue eliminado
        Odontologo odontologoEliminado = odontologoService.buscarOdontologo(odontologo.getId());
        Assertions.assertNull(odontologoEliminado);

    }

    @Test
    public void actualizarOdontologo() {
        // elegir od a actualizar
        List<Odontologo> odontologos = odontologoService.buscarTodos();
        Integer indice = (int)Math.random()* odontologos.size();
        System.out.println(indice);
        Odontologo odontologoPorActualizar = odontologos.get(indice);

        odontologoPorActualizar.setNombre("Julia");
        odontologoService.actualizarOdontologo(odontologoPorActualizar);

        Odontologo odontologoActualizado = odontologoService.buscarOdontologo(odontologoPorActualizar.getId());
        assertNotNull(odontologoActualizado);
        assertEquals("Julia", odontologoActualizado.getNombre());
        assertEquals(odontologoPorActualizar.getId(), odontologoActualizado.getId());
    }

}