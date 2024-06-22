package clinica.sistemaReservaTurno.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "turnos")
public class Turno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", referencedColumnName = "id", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "odontologo_id", referencedColumnName = "id", nullable = false)
    private Odontologo odontologo;
    @Column
    private LocalDateTime fechaHoraCita;

    public Turno() {
    }

    public Turno(Long id, Paciente paciente, Odontologo odontologo, LocalDateTime fechaHoraCita) {
        this.id = id;
        this.paciente = paciente;
        this.odontologo = odontologo;
        this.fechaHoraCita = fechaHoraCita;
    }

    public Turno(Paciente paciente, Odontologo odontologo, LocalDateTime fechaHoraCita) {
        this.paciente = paciente;
        this.odontologo = odontologo;
        this.fechaHoraCita = fechaHoraCita;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public void setOdontologo(Odontologo odontologo) {
        this.odontologo = odontologo;
    }

    public void setFechaHoraCita(LocalDateTime fechaHoraCita) {
        this.fechaHoraCita = fechaHoraCita;
    }

    public Long getId() {
        return id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Odontologo getOdontologo() {
        return odontologo;
    }

    public LocalDateTime getFechaHoraCita() {
        return fechaHoraCita;
    }
}
