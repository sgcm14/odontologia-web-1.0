package clinica.sistemaReservaTurno.controller;
import clinica.sistemaReservaTurno.entity.Odontologo;
import clinica.sistemaReservaTurno.exception.ResourceConflictException;
import clinica.sistemaReservaTurno.exception.ResourceNotFoundException;
import clinica.sistemaReservaTurno.service.OdontologoService;

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

@RestController
@RequestMapping("/odontologos")
@Tag(name = "Controller de Odontologos", description = "Este endpoint nos permite operar solo con odontologos")
public class OdontologoController {

    @Autowired
    private OdontologoService odontologoService;

    @GetMapping
    @Operation(summary = "Listar todos los odontologos", description = "Devuelve una lista completa de los odontologos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de odontologos encontrada", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "No se encontraron odontologos", content = @Content)
    })
    public ResponseEntity<List<Odontologo>> buscarTodos() {
        return ResponseEntity.ok(odontologoService.buscarTodos());
    }

    @PostMapping //nos permite crear o registrar un odontologo
    @Operation(summary = "Registrar un nuevo odontologo", description = "Registra un nuevo odontologo y devuelve el objeto completo del odontologo registrado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Odontologo registrado con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Conflicto de recurso, el odontologo ya existe", content = @Content)
    })
    public ResponseEntity<Odontologo> registrarUnOdontologo(
            @RequestBody @Schema(description = "Detalles del odontologo a registrar", example = "{ \"nombre\": \"Carlos\", \"apellido\": \"Lopez\", \"numeroMatricula\": \"ABC123\" }")
            Odontologo odontologo) throws ResourceConflictException {
        Optional<Odontologo> odontologoMatriculaBuscado = odontologoService.buscarPorMatricula(odontologo.getNumeroMatricula());
        if(odontologoMatriculaBuscado.isPresent()){
            throw new ResourceConflictException("Ya existe un odontologo con matricula " + odontologo.getNumeroMatricula());
        }
        return ResponseEntity.ok(odontologoService.guardarOdontologo(odontologo));
    }

    @PutMapping
    @Operation(summary = "Actualizar un odontologo existente", description = "Actualiza los datos de un odontologo y devuelve un mensaje de confirmación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Odontologo actualizado con éxito", content = @Content),
            @ApiResponse(responseCode = "404", description = "Odontologo no encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflicto de recurso, datos del odontologo ya existen", content = @Content)
    })
    public ResponseEntity<String> actualizarOdontologo(
            @RequestBody @Schema(description = "Detalles del odontologo a actualizar", example = "{ \"id\": 1, \"nombre\": \"Carlos\", \"apellido\": \"Benedicto\", \"numeroMatricula\": \"ABC123\" }")
            Odontologo odontologo) throws ResourceNotFoundException, ResourceConflictException {
        //necesitamos primeramente validar si existe o  no
        Optional<Odontologo> odontologoBuscado = odontologoService.buscarPorID(odontologo.getId());
        if(!odontologoBuscado.isPresent()){
            throw new ResourceNotFoundException("No se encontró odontologo con id: " + odontologo.getId());
        }

        String mensaje = "";
        Optional<Odontologo> odontologoMatriculaBuscado = odontologoService.buscarPorMatricula(odontologo.getNumeroMatricula());

        if(odontologoMatriculaBuscado.isPresent() && odontologoMatriculaBuscado.get().getId() != odontologo.getId()){
            mensaje += "Existe otro odontologo con esa matricula: " + odontologo.getNumeroMatricula();
        }

        if(!mensaje.isEmpty()){
            throw new ResourceConflictException(mensaje);
        }

        odontologoService.actualizarOdontologo(odontologo);
        return ResponseEntity.ok("Odontologo actualizado");

    }

    @GetMapping("/{id}") //Buscar Odontologo por Id
    @Operation(summary = "Buscar odontologo por ID", description = "Devuelve el objeto completo del odontologo correspondiente al ID especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Odontologo encontrado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Odontologo no encontrado", content = @Content)
    })
    public ResponseEntity<Odontologo> buscarOdontologoPorId(
            @PathVariable @Schema(description = "ID del odontologo a buscar", example = "1") Long id) throws ResourceNotFoundException {

        Optional<Odontologo> odontologoBuscado = odontologoService.buscarPorID(id);
        if (odontologoBuscado.isPresent()) {
            return ResponseEntity.ok(odontologoBuscado.get());
        } else {
            throw new ResourceNotFoundException("No se encontró odontologo con id: " + id);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    @Operation(summary = "Eliminar un odontologo", description = "Elimina el odontologo correspondiente al ID especificado y devuelve un mensaje de confirmación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Odontologo eliminado con éxito", content = @Content),
            @ApiResponse(responseCode = "404", description = "Odontologo no encontrado", content = @Content)
    })
    public ResponseEntity<String> eliminarOdontologo(
            @PathVariable @Schema(description = "ID del odontologo a eliminar", example = "1") Long id) throws ResourceNotFoundException {
        Optional<Odontologo> odontologoBuscado = odontologoService.buscarPorID(id);
        if (odontologoBuscado.isPresent()) {
            odontologoService.eliminarOdontologo(id);
            return ResponseEntity.ok("odontologo eliminado con exito");
        } else {
            throw new ResourceNotFoundException("No existe odontologo con id: " + id);
        }
    }
}
