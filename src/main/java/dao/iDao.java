package dao;

import java.util.List;

public interface iDao<T> {
    T guardar(T t);
    T buscarPorID(Integer id);
    T actualizar(T t);
    void eliminar(Integer id);
    List<T> buscarTodos();
}
