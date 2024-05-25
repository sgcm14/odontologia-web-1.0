package service;

import dao.OdontologoDaoH2;
import dao.iDao;
import model.Odontologo;

import java.util.List;

public class OdontologoService {
    //relacion de asociacion directa con el DAO
    private iDao<Odontologo> odontologoiDao;

    public OdontologoService() {
        odontologoiDao= new OdontologoDaoH2();
    }

    public Odontologo guardarOdontologo(Odontologo odontologo){
        return odontologoiDao.guardar(odontologo);
    }

    public Odontologo buscarOdontologo(Integer id){
        return odontologoiDao.buscarPorID(id);
    }

    public List<Odontologo> buscarTodos(){
        return odontologoiDao.buscarTodos();
    }

    public void  eliminarOdontologo(Integer id){
        Odontologo odontologo = odontologoiDao.buscarPorID(id);
        if(odontologo != null){
            odontologoiDao.eliminar(id);
        }
    }

    public Odontologo actualizarOdontologo(Odontologo odontologo){
        return odontologoiDao.actualizar(odontologo);
    }

}
