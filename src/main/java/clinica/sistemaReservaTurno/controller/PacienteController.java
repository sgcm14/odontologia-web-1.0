package clinica.sistemaReservaTurno.controller;
import clinica.sistemaReservaTurno.entity.Odontologo;
import clinica.sistemaReservaTurno.entity.Paciente;
import clinica.sistemaReservaTurno.exception.ResourceConflictException;
import clinica.sistemaReservaTurno.exception.ResourceNotFoundException;
import clinica.sistemaReservaTurno.service.PacienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@Controller
@RestController //cambiamos pq no necesitamos tecnologia de vista.
@RequestMapping("/pacientes")
public class PacienteController {
    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<List<Paciente>> buscarTodos() {
        return ResponseEntity.ok(pacienteService.buscarTodos());
    }

    @PostMapping //nos permite crear o registrar un paciente
    public ResponseEntity<Paciente> registrarUnPaciente(@RequestBody Paciente paciente) throws ResourceConflictException {
        Optional<Paciente> pacienteEmailBuscado = pacienteService.buscarPorEmail(paciente.getEmail());
        Optional<Paciente> pacienteCedulaBuscado = pacienteService.buscarPorCedula(paciente.getCedula());
        String mensaje = "";

        if(pacienteEmailBuscado.isPresent()){
            mensaje+="Ya existe un paciente con email: " + paciente.getEmail() + ". ";
        }

        if(pacienteCedulaBuscado.isPresent()){
            mensaje+= "Ya existe un paciente con cedula: " + paciente.getCedula();
        }

        if(!mensaje.isEmpty()) {
            throw new ResourceConflictException(mensaje);
        }

        return ResponseEntity.ok(pacienteService.guardarPaciente(paciente));
    }

    @PutMapping
    public ResponseEntity<String> actualizarPaciente(@RequestBody Paciente paciente) throws ResourceNotFoundException, ResourceConflictException {
        //necesitamos primeramente validar si existe o  no
        Optional<Paciente> pacienteBuscado = pacienteService.buscarPorID(paciente.getId());
        if(!pacienteBuscado.isPresent()){
            throw new ResourceNotFoundException("No se encontró Paciente con id: " + paciente.getId());
        }

        Optional<Paciente>  pacienteEmailBuscado= pacienteService.buscarPorEmail(paciente.getEmail());
        Optional<Paciente> pacienteCedulaBuscado = pacienteService.buscarPorCedula(paciente.getCedula());
        String mensaje = "";

        if(pacienteEmailBuscado.isPresent() && pacienteEmailBuscado.get().getId() != paciente.getId()){
            mensaje += "Existe otro paciente con ese email: " + paciente.getEmail();
        }
        if(pacienteCedulaBuscado.isPresent() && pacienteCedulaBuscado.get().getId() != paciente.getId()){
            mensaje += "Existe otro paciente con esa cedula: " + paciente.getCedula();
        }

        if(!mensaje.isEmpty()){
            throw new ResourceConflictException(mensaje);
        }

        pacienteService.actualizarPaciente(paciente);
        return ResponseEntity.ok("Paciente actualizado");

    }

    @GetMapping("/{id}") //Buscar Paciente por Id
    public ResponseEntity<Paciente> buscarPacientePorId(@PathVariable Long id) throws ResourceNotFoundException {

        Optional<Paciente> pacienteBuscado = pacienteService.buscarPorID(id);
        if (pacienteBuscado.isPresent()) {
            return ResponseEntity.ok(pacienteBuscado.get());
        } else {
            //return ResponseEntity.notFound().build();
            //aca lanzamos la exception
            throw new ResourceNotFoundException("No se encontró paciente con id: " + id);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Paciente> buscarPorEmail(@PathVariable String email) throws ResourceNotFoundException {
        Optional<Paciente> pacienteBuscado = pacienteService.buscarPorEmail(email);
        if (pacienteBuscado.isPresent()) {
            return ResponseEntity.ok(pacienteBuscado.get());
        } else {
            // return ResponseEntity.notFound().build();
            //aca lanzamos la exception
            throw new ResourceNotFoundException("No existe email : " + email);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarPaciente(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Paciente> pacienteBuscado = pacienteService.buscarPorID(id);
        if (pacienteBuscado.isPresent()) {
            pacienteService.eliminarPaciente(id);
            return ResponseEntity.ok("paciente eliminado con exito");
        } else {
            //return ResponseEntity.badRequest().body("paciente no encontrado");
            //aca lanzamos la exception
            throw new ResourceNotFoundException("No existe paciente con id: " + id);
        }
    }
}
