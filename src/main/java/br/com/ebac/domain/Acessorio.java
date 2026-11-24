package br.com.ebac.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "acessorio")
public class Acessorio implements Persistente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, unique = true, length = 50)
    private String nome;

    @Column(name = "preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    /** Lado inverso do MANY TO MANY; o dono é Carro.acessorios. */
    @ManyToMany(mappedBy = "acessorios")
    private Set<Carro> carros = new HashSet<>();

    public Acessorio() {
    }

    public Acessorio(String nome, BigDecimal preco) {
        this.nome = nome;
        this.preco = preco;
    }

    @Override
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public Set<Carro> getCarros() { return carros; }
    public void setCarros(Set<Carro> carros) { this.carros = carros; }
}
