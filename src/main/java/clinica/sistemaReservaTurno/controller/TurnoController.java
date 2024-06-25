package clinica.sistemaReservaTurno.controller;

import clinica.sistemaReservaTurno.entity.Odontologo;
import clinica.sistemaReservaTurno.entity.Paciente;
import clinica.sistemaReservaTurno.entity.Turno;
import clinica.sistemaReservaTurno.exception.ResourceNotFoundException;
import clinica.sistemaReservaTurno.service.OdontologoService;
import clinica.sistemaReservaTurno.service.PacienteService;
import clinica.sistemaReservaTurno.service.TurnoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/turnos")
@Tag(name = "Controller de Turnos", description = "Este endpoint nos permite operar solo con turnos")
public class TurnoController {

    @Autowired
    private TurnoService turnoService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private OdontologoService odontologoService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @GetMapping
    @Operation(summary = "Listar todos los turnos", description = "Devuelve una lista completa de los turnos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de turnos encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Turno.class)))
    })
    public ResponseEntity<List<Turno>> buscarTodos() {
        return ResponseEntity.ok(turnoService.buscarTodos());
    }

    @PostMapping //nos permite crear o registrar un turno
    @Operation(summary = "Registrar un nuevo turno", description = "Registra un nuevo turno y devuelve el objeto completo del turno registrado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno registrado con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Turno.class))),
            @ApiResponse(responseCode = "404", description = "Paciente u odontólogo no encontrado", content = @Content)
    })
    public ResponseEntity<Turno> registrarUnTurno(
            @RequestBody @Schema(description = "Detalles del turno a registrar", example = "{ \"paciente\": { \"id\": 1 }, \"odontologo\": { \"id\": 1 }, \"fechaHoraCita\": \"2023-12-25T10:00\" }")
            Turno turno) throws ResourceNotFoundException {
        // Validamos si el paciente y el odontologo existen
        Optional<Paciente> pacienteBuscado = pacienteService.buscarPorID(turno.getPaciente().getId());
        Optional<Odontologo> odontologoBuscado = odontologoService.buscarPorID(turno.getOdontologo().getId());
        if (!pacienteBuscado.isPresent() && !odontologoBuscado.isPresent()) {
            throw new ResourceNotFoundException("No se encontraron paciente con id: " + turno.getPaciente().getId() + " ni odontólogo con id: " + turno.getOdontologo().getId());
        } else if (!pacienteBuscado.isPresent()) {
            throw new ResourceNotFoundException("No se encontró paciente con id: " + turno.getPaciente().getId());
        } else if (!odontologoBuscado.isPresent()) {
            throw new ResourceNotFoundException("No se encontró odontólogo con id: " + turno.getOdontologo().getId());
        }

        // Si ambos existen, procesamos la fecha y guardamos el turno
        turno.setFechaHoraCita(LocalDateTime.parse(turno.getFechaHoraCita().toString(), formatter));
        return ResponseEntity.ok(turnoService.guardarTurno(turno));
    }

    @PutMapping
    @Operation(summary = "Actualizar un turno existente", description = "Actualiza los datos de un turno y devuelve un mensaje de confirmación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno actualizado con éxito", content = @Content),
            @ApiResponse(responseCode = "404", description = "Turno, paciente u odontólogo no encontrado", content = @Content)
    })
    public ResponseEntity<String> actualizarTurno(
            @RequestBody @Schema(description = "Detalles del turno a actualizar", example = "{ \"id\": 1, \"paciente\": { \"id\": 1 }, \"odontologo\": { \"id\": 1 }, \"fechaHoraCita\": \"2023-12-25T10:00\" }")
            Turno turno) throws ResourceNotFoundException {
        // Primero verificamos si el turno existe
        Optional<Turno> turnoBuscado = turnoService.buscarPorID(turno.getId());
        if (turnoBuscado.isPresent()) {
            // Verificamos si el paciente y el odontólogo existen
            Optional<Paciente> pacienteBuscado = pacienteService.buscarPorID(turno.getPaciente().getId());
            Optional<Odontologo> odontologoBuscado = odontologoService.buscarPorID(turno.getOdontologo().getId());
            if (!pacienteBuscado.isPresent() && !odontologoBuscado.isPresent()) {
                throw new ResourceNotFoundException("No se encontraron paciente con id: " + turno.getPaciente().getId() + " ni odontólogo con id: " + turno.getOdontologo().getId());
            } else if (!pacienteBuscado.isPresent()) {
                throw new ResourceNotFoundException("No se encontró paciente con id: " + turno.getPaciente().getId());
            } else if (!odontologoBuscado.isPresent()) {
                throw new ResourceNotFoundException("No se encontró odontólogo con id: " + turno.getOdontologo().getId());
            }

            // Si el turno, el paciente y el odontólogo existen, actualizamos los datos del turno
            turno.setFechaHoraCita(LocalDateTime.parse(turno.getFechaHoraCita().toString(), formatter));
            turnoService.actualizarTurno(turno);
            return ResponseEntity.ok("Turno actualizado");
        } else {
            //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Turno no encontrado");
            //aca lanzamos la exception
            throw new ResourceNotFoundException("No se encontró turno con id: " + turno.getId());
        }
    }

    @GetMapping("/{id}") //Buscar Turno por Id
    @Operation(summary = "Buscar turno por ID", description = "Devuelve el objeto completo del turno correspondiente al ID especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Turno.class))),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado", content = @Content)
    })
    public ResponseEntity<Turno> buscarTurnoPorId(
            @PathVariable @Schema(description = "ID del turno a buscar", example = "1")
            Long id) throws ResourceNotFoundException {

        Optional<Turno> turnoBuscado = turnoService.buscarPorID(id);
        if (turnoBuscado.isPresent()) {
            return ResponseEntity.ok(turnoBuscado.get());
        } else {
            //return ResponseEntity.notFound().build();
            //aca lanzamos la exception
            throw new ResourceNotFoundException("No se encontró turno con id: " + id);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    @Operation(summary = "Eliminar un turno", description = "Elimina el turno correspondiente al ID especificado y devuelve un mensaje de confirmación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno eliminado con éxito", content = @Content),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado", content = @Content)
    })
    public ResponseEntity<String> eliminarTurno(
            @PathVariable @Schema(description = "ID del turno a eliminar", example = "1")
            Long id) throws ResourceNotFoundException {
        Optional<Turno> turnoBuscado = turnoService.buscarPorID(id);
        if (turnoBuscado.isPresent()) {
            turnoService.eliminarTurno(id);
            return ResponseEntity.ok("turno eliminado con exito");
        } else {
            // return ResponseEntity.badRequest().body("turno no encontrado");
            //aca lanzamos la exception
            throw new ResourceNotFoundException("No existe turno con id: " + id);
        }
    }
}
