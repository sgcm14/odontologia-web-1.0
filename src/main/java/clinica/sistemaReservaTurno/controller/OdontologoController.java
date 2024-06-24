package clinica.sistemaReservaTurno.controller;
import clinica.sistemaReservaTurno.entity.Odontologo;
import clinica.sistemaReservaTurno.entity.Paciente;
import clinica.sistemaReservaTurno.exception.ResourceConflictException;
import clinica.sistemaReservaTurno.exception.ResourceNotFoundException;
import clinica.sistemaReservaTurno.service.OdontologoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/odontologos")
public class OdontologoController {

    @Autowired
    private OdontologoService odontologoService;

    @GetMapping
    public ResponseEntity<List<Odontologo>> buscarTodos() {
        return ResponseEntity.ok(odontologoService.buscarTodos());
    }

    @PostMapping //nos permite crear o registrar un odontologo
    public ResponseEntity<Odontologo> registrarUnOdontologo(@RequestBody Odontologo odontologo) throws ResourceConflictException {
        Optional<Odontologo> odontologoMatriculaBuscado = odontologoService.buscarPorMatricula(odontologo.getNumeroMatricula());
        if(odontologoMatriculaBuscado.isPresent()){
            throw new ResourceConflictException("Ya existe un odontologo con matricula " + odontologo.getNumeroMatricula());
        }
        return ResponseEntity.ok(odontologoService.guardarOdontologo(odontologo));
    }

    @PutMapping
    public ResponseEntity<String> actualizarOdontologo(@RequestBody Odontologo odontologo) throws ResourceNotFoundException, ResourceConflictException {
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
    public ResponseEntity<Odontologo> buscarOdontologoPorId(@PathVariable Long id) throws ResourceNotFoundException {

        Optional<Odontologo> odontologoBuscado = odontologoService.buscarPorID(id);
        if (odontologoBuscado.isPresent()) {
            return ResponseEntity.ok(odontologoBuscado.get());
        } else {
            throw new ResourceNotFoundException("No se encontró odontologo con id: " + id);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarOdontologo(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Odontologo> odontologoBuscado = odontologoService.buscarPorID(id);
        if (odontologoBuscado.isPresent()) {
            odontologoService.eliminarOdontologo(id);
            return ResponseEntity.ok("odontologo eliminado con exito");
        } else {
            throw new ResourceNotFoundException("No existe odontologo con id: " + id);
        }
    }
}
