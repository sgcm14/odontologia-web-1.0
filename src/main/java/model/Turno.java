package model;

import java.time.LocalDateTime;

public class Turno {
    private Integer id;
    private Paciente paciente;
    private Odontologo odontologo;
    private LocalDateTime fechaHoraCita;

    public void setId(Integer id) {
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
}
