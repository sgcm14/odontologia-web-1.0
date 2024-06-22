package clinica.sistemaReservaTurno.service;
import clinica.sistemaReservaTurno.entity.Turno;
import clinica.sistemaReservaTurno.repository.TurnoRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;


@Service
public class TurnoService {
    @Autowired
    private TurnoRepository turnoRepository;

    public List<Turno> buscarTodos() {
        return turnoRepository.findAll();
    }

    public Turno guardarTurno(Turno turno) {
        return turnoRepository.save(turno);
    }

    public void actualizarTurno(Turno turno) {
        turnoRepository.save(turno);
    }

    public Optional<Turno> buscarPorID(Long id) {
        return turnoRepository.findById(id);
    }

    public void eliminarTurno(Long id) {
        turnoRepository.deleteById(id);
    }
}
