package clinica.sistemaReservaTurno.repository;
import clinica.sistemaReservaTurno.entity.Turno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {
}
