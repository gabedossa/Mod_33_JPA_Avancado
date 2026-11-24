package br.com.ebac.dao;

import br.com.ebac.domain.Carro;
import br.com.ebac.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class CarroDAO extends GenericDAO<Carro> {

    public CarroDAO() {
        super(Carro.class);
    }

    /** Carrega o carro já com os acessórios (many-to-many é LAZY por padrão). */
    public Carro consultarComAcessorios(Long id) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                            "SELECT c FROM Carro c LEFT JOIN FETCH c.acessorios WHERE c.id = :id", Carro.class)
                    .setParameter("id", id)
                    .getSingleResult();
        }
    }
}
