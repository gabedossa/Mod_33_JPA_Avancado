package br.com.ebac.dao;

import br.com.ebac.domain.Persistente;
import br.com.ebac.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.function.Function;

public abstract class GenericDAO<T extends Persistente> implements IGenericDAO<T> {

    protected final Class<T> persistenteClass;

    protected GenericDAO(Class<T> persistenteClass) {
        this.persistenteClass = persistenteClass;
    }

    @Override
    public T cadastrar(T entity) {
        return emTransacao(em -> {
            em.persist(entity);
            return entity;
        });
    }

    @Override
    public T alterar(T entity) {
        return emTransacao(em -> em.merge(entity));
    }

    @Override
    public void excluir(T entity) {
        emTransacao(em -> {
            T gerenciado = em.find(persistenteClass, entity.getId());
            if (gerenciado != null) {
                em.remove(gerenciado);
            }
            return null;
        });
    }

    @Override
    public T consultar(Long id) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.find(persistenteClass, id);
        }
    }

    @Override
    public List<T> buscarTodos() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            String jpql = "SELECT e FROM " + persistenteClass.getSimpleName() + " e";
            return em.createQuery(jpql, persistenteClass).getResultList();
        }
    }

    protected <R> R emTransacao(Function<EntityManager, R> acao) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                R resultado = acao.apply(em);
                tx.commit();
                return resultado;
            } catch (RuntimeException e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw e;
            }
        }
    }
}
