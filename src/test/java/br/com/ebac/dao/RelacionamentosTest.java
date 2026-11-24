package br.com.ebac.dao;

import br.com.ebac.domain.Acessorio;
import br.com.ebac.domain.Carro;
import br.com.ebac.domain.Marca;
import br.com.ebac.util.JPAUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RelacionamentosTest {

    private final MarcaDAO marcaDAO = new MarcaDAO();
    private final CarroDAO carroDAO = new CarroDAO();
    private final AcessorioDAO acessorioDAO = new AcessorioDAO();

    /** Limpa as tabelas respeitando as chaves estrangeiras. */
    @AfterEach
    void limpar() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("UPDATE Marca m SET m.carroChefe = null").executeUpdate();
            em.createNativeQuery("DELETE FROM carro_acessorio").executeUpdate();
            em.createQuery("DELETE FROM Carro").executeUpdate();
            em.createQuery("DELETE FROM Acessorio").executeUpdate();
            em.createQuery("DELETE FROM Marca").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @AfterAll
    static void fechar() {
        JPAUtil.close();
    }

    @Test
    void oneToMany_marcaPossuiVariosCarros() {
        Marca marca = marcaDAO.cadastrar(new Marca("Toyota", "Japão"));
        carroDAO.cadastrar(new Carro("Corolla", 2024, marca));
        carroDAO.cadastrar(new Carro("Hilux", 2023, marca));

        Marca consultada = marcaDAO.consultarComCarros(marca.getId());

        assertEquals(2, consultada.getCarros().size());
        assertTrue(consultada.getCarros().stream().allMatch(c -> c.getMarca().getId().equals(marca.getId())));
    }

    @Test
    void oneToOne_marcaTemUmCarroChefe() {
        Marca marca = marcaDAO.cadastrar(new Marca("Ford", "EUA"));
        Carro mustang = carroDAO.cadastrar(new Carro("Mustang", 2024, marca));

        marca.setCarroChefe(mustang);
        marcaDAO.alterar(marca);

        Marca consultada = marcaDAO.consultar(marca.getId());
        assertNotNull(consultada.getCarroChefe());
        assertEquals("Mustang", consultada.getCarroChefe().getModelo());
    }

    @Test
    void manyToMany_carroTemVariosAcessoriosEAcessorioEstaEmVariosCarros() {
        Marca marca = marcaDAO.cadastrar(new Marca("Volkswagen", "Alemanha"));
        Acessorio arCondicionado = acessorioDAO.cadastrar(new Acessorio("Ar-condicionado", new BigDecimal("4500.00")));
        Acessorio multimidia = acessorioDAO.cadastrar(new Acessorio("Central multimídia", new BigDecimal("2800.00")));

        Carro gol = new Carro("Gol", 2022, marca);
        gol.adicionarAcessorio(arCondicionado);
        gol.adicionarAcessorio(multimidia);
        carroDAO.cadastrar(gol);

        Carro polo = new Carro("Polo", 2024, marca);
        polo.adicionarAcessorio(arCondicionado);
        carroDAO.cadastrar(polo);

        assertEquals(2, carroDAO.consultarComAcessorios(gol.getId()).getAcessorios().size());
        assertEquals(1, carroDAO.consultarComAcessorios(polo.getId()).getAcessorios().size());

        try (EntityManager em = JPAUtil.getEntityManager()) {
            Long carrosComAr = em.createQuery(
                            "SELECT COUNT(c) FROM Carro c JOIN c.acessorios a WHERE a.id = :id", Long.class)
                    .setParameter("id", arCondicionado.getId())
                    .getSingleResult();
            assertEquals(2L, carrosComAr);
        }
    }

    @Test
    void manyToMany_removerAcessorioDoCarro() {
        Marca marca = marcaDAO.cadastrar(new Marca("Fiat", "Itália"));
        Acessorio teto = acessorioDAO.cadastrar(new Acessorio("Teto solar", new BigDecimal("6000.00")));

        Carro pulse = new Carro("Pulse", 2024, marca);
        pulse.adicionarAcessorio(teto);
        carroDAO.cadastrar(pulse);

        Carro carregado = carroDAO.consultarComAcessorios(pulse.getId());
        carregado.getAcessorios().clear();
        carroDAO.alterar(carregado);

        assertTrue(carroDAO.consultarComAcessorios(pulse.getId()).getAcessorios().isEmpty());
        assertNotNull(acessorioDAO.consultar(teto.getId()), "o acessório continua existindo, só sai da tabela de junção");
    }
}
