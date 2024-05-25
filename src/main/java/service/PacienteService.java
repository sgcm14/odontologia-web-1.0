package service;

import dao.DomicilioDaoH2;
import dao.PacienteDAOH2;
import dao.iDao;
import model.Domicilio;
import model.Paciente;

import java.util.List;

public class PacienteService {
    //relacion de asociacion directa con el DAO
    private iDao<Paciente> pacienteiDao;
    private iDao<Domicilio> domicilioiDao;

    public PacienteService() {

        pacienteiDao= new PacienteDAOH2();
        domicilioiDao = new DomicilioDaoH2();
    }
    public Paciente guardarPaciente(Paciente paciente){
        Domicilio domicilio = paciente.getDomicilio();
        if(domicilio == null){
            domicilio = new Domicilio("",0,"","");
        }
        domicilio = domicilioiDao.guardar(domicilio);

        paciente.setDomicilio(domicilio);
        return pacienteiDao.guardar(paciente);
    }
    public Paciente buscarPaciente(Integer id){
        return pacienteiDao.buscarPorID(id);
    }
    public List<Paciente> buscarTodos(){
        return pacienteiDao.buscarTodos();
    }

    public void eliminarPaciente(Integer id) {

        Paciente paciente = pacienteiDao.buscarPorID(id);
        if (paciente != null) {

            Domicilio domicilio = paciente.getDomicilio();
            // Eliminar el paciente
            pacienteiDao.eliminar(id);
            // Verificar si el domicilio no está asociado a otros pacientes antes de eliminarlo
            if (domicilio != null) {
                /*List<Paciente> pacientesConMismoDomicilio = buscarTodos();
                boolean domicilioUsadoPorOtroPaciente = pacientesConMismoDomicilio.stream()
                        .anyMatch(p -> p.getDomicilio().getId().equals(domicilio.getId()));
                if (!domicilioUsadoPorOtroPaciente) {*/
                    domicilioiDao.eliminar(domicilio.getId());
               // }
            }
        }
    }

    public Paciente actualizarPaciente(Paciente paciente) {
        return pacienteiDao.actualizar(paciente);
    }

    public Domicilio buscarDomicilio(Integer id) {
        return domicilioiDao.buscarPorID(id);
    }

}
