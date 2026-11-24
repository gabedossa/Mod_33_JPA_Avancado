package br.com.ebac.dao;

import br.com.ebac.domain.Persistente;

import java.util.List;

public interface IGenericDAO<T extends Persistente> {

    T cadastrar(T entity);

    T alterar(T entity);

    void excluir(T entity);

    T consultar(Long id);

    List<T> buscarTodos();
}
