package br.com.ebac.dao;

import br.com.ebac.domain.Marca;
import br.com.ebac.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class MarcaDAO extends GenericDAO<Marca> {

    public MarcaDAO() {
        super(Marca.class);
    }

    /** Carrega a marca já com a lista de carros (one-to-many é LAZY por padrão). */
    public Marca consultarComCarros(Long id) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                            "SELECT m FROM Marca m LEFT JOIN FETCH m.carros WHERE m.id = :id", Marca.class)
                    .setParameter("id", id)
                    .getSingleResult();
        }
    }
}
