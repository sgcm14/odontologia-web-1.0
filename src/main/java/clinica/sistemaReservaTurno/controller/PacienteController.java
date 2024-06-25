package clinica.sistemaReservaTurno.controller;
import clinica.sistemaReservaTurno.entity.Odontologo;
import clinica.sistemaReservaTurno.entity.Paciente;
import clinica.sistemaReservaTurno.exception.ResourceConflictException;
import clinica.sistemaReservaTurno.exception.ResourceNotFoundException;
import clinica.sistemaReservaTurno.service.PacienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@Controller
@RestController //cambiamos pq no necesitamos tecnologia de vista.
@RequestMapping("/pacientes")
@Tag(name = "Controller de Pacientes", description = "Este endpoint nos permite operar solo con paciente")
public class PacienteController {
    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    @Operation(summary = "Listar todos los pacientes", description = "Devuelve una lista completa de los pacientes registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pacientes encontrada", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "No se encontraron pacientes", content = @Content)
    })
    public ResponseEntity<List<Paciente>> buscarTodos() {
        return ResponseEntity.ok(pacienteService.buscarTodos());
    }

    @PostMapping //nos permite crear o registrar un paciente
    @Operation(summary = "Registrar un nuevo paciente", description = "Registra un nuevo paciente y devuelve el objeto completo del paciente registrado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente registrado con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Conflicto de recurso, el paciente ya existe", content = @Content)
    })
    public ResponseEntity<Paciente> registrarUnPaciente(
            @RequestBody @Schema(description = "Detalles del paciente a registrar", example = "{ \"nombre\": \"Juan Perez\", \"apellido\": \"Perez\", \"cedula\": \"12345678\", \"fechaIngreso\": \"2024-07-10\", \"domicilio\": { \"calle\": \"Calle Falsa\", \"numero\": 123, \"localidad\": \"Ciudad\", \"provincia\": \"Provincia\" }, \"email\": \"juan.perez@example.com\" }")
            Paciente paciente) throws ResourceConflictException {
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
    @Operation(summary = "Actualizar un paciente existente", description = "Actualiza los datos de un paciente y devuelve un mensaje de confirmación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente actualizado con éxito", content = @Content),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflicto de recurso, datos del paciente ya existen", content = @Content)
    })
    public ResponseEntity<String> actualizarPaciente(
            @RequestBody @Schema(description = "Detalles del paciente a actualizar", example = "{ \"id\": 1, \"nombre\": \"Juan Perez\", \"apellido\": \"Perez\", \"cedula\": \"12345678\", \"fechaIngreso\": \"2024-08-10\", \"domicilio\": { \"calle\": \"Calle Falsa\", \"numero\": 123, \"localidad\": \"Ciudad\", \"provincia\": \"Provincia\" }, \"email\": \"juan.perez@example.com\" }")
            Paciente paciente) throws ResourceNotFoundException, ResourceConflictException {
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
    @Operation(summary = "Buscar paciente por ID", description = "Devuelve el objeto completo del paciente correspondiente al ID especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado", content = @Content)
    })
    public ResponseEntity<Paciente> buscarPacientePorId(
            @PathVariable @Schema(description = "ID del paciente a buscar", example = "1") Long id) throws ResourceNotFoundException {

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
    @Operation(summary = "Buscar paciente por Email", description = "Devuelve el objeto completo del paciente correspondiente al email especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado", content = @Content)
    })
    public ResponseEntity<Paciente> buscarPorEmail(
            @PathVariable @Schema(description = "Email del paciente a buscar", example = "juan.perez@example.com") String email) throws ResourceNotFoundException {
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
    @Operation(summary = "Eliminar un paciente", description = "Elimina el paciente correspondiente al ID especificado y devuelve un mensaje de confirmación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente eliminado con éxito", content = @Content),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado", content = @Content)
    })
    public ResponseEntity<String> eliminarPaciente(
            @PathVariable @Schema(description = "ID del paciente a eliminar", example = "1") Long id) throws ResourceNotFoundException {
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
