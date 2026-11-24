package br.com.ebac.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "carro")
public class Carro implements Persistente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "modelo", nullable = false, length = 50)
    private String modelo;

    @Column(name = "ano", nullable = false)
    private Integer ano;

    /**
     * MANY TO ONE (lado dono do ONE TO MANY de Marca): vários carros pertencem a uma marca.
     * Gera a coluna marca_id na tabela carro.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "marca_id", nullable = false)
    private Marca marca;

    /**
     * MANY TO MANY: um carro tem vários acessórios e um acessório pode estar em vários carros.
     * Gera a tabela de junção carro_acessorio (carro_id, acessorio_id).
     */
    @ManyToMany
    @JoinTable(name = "carro_acessorio",
            joinColumns = @JoinColumn(name = "carro_id"),
            inverseJoinColumns = @JoinColumn(name = "acessorio_id"))
    private Set<Acessorio> acessorios = new HashSet<>();

    public Carro() {
    }

    public Carro(String modelo, Integer ano, Marca marca) {
        this.modelo = modelo;
        this.ano = ano;
        this.marca = marca;
    }

    /** Mantém os dois lados da relação many-to-many sincronizados. */
    public void adicionarAcessorio(Acessorio acessorio) {
        acessorios.add(acessorio);
        acessorio.getCarros().add(this);
    }

    public void removerAcessorio(Acessorio acessorio) {
        acessorios.remove(acessorio);
        acessorio.getCarros().remove(this);
    }

    @Override
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public Marca getMarca() { return marca; }
    public void setMarca(Marca marca) { this.marca = marca; }

    public Set<Acessorio> getAcessorios() { return acessorios; }
    public void setAcessorios(Set<Acessorio> acessorios) { this.acessorios = acessorios; }
}
