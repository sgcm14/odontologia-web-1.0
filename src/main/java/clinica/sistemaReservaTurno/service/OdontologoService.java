package clinica.sistemaReservaTurno.service;
import clinica.sistemaReservaTurno.entity.Odontologo;
import clinica.sistemaReservaTurno.repository.OdontologoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OdontologoService {
    @Autowired
    private OdontologoRepository odontologoRepository;

    public List<Odontologo> buscarTodos() {
        return odontologoRepository.findAll();
    }

    public Odontologo guardarOdontologo(Odontologo odontologo) {
        return odontologoRepository.save(odontologo);
    }

    public void actualizarOdontologo(Odontologo odontologo) {
        odontologoRepository.save(odontologo);
    }

    public Optional<Odontologo> buscarPorID(Long id) {
        return odontologoRepository.findById(id);
    }

    public void eliminarOdontologo(Long id) {
        odontologoRepository.deleteById(id);
    }

}
