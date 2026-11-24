package br.com.ebac.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "marca")
public class Marca implements Persistente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, unique = true, length = 50)
    private String nome;

    @Column(name = "pais_origem", length = 50)
    private String paisOrigem;

    /**
     * ONE TO MANY: uma marca possui vários carros.
     * O lado dono da relação é Carro (coluna marca_id), por isso o mappedBy.
     */
    @OneToMany(mappedBy = "marca")
    private List<Carro> carros = new ArrayList<>();

    /**
     * ONE TO ONE: cada marca tem um único carro-chefe, e um carro só pode ser carro-chefe de uma marca.
     * Gera a coluna carro_chefe_id (única) na tabela marca.
     */
    @OneToOne
    @JoinColumn(name = "carro_chefe_id", unique = true)
    private Carro carroChefe;

    public Marca() {
    }

    public Marca(String nome, String paisOrigem) {
        this.nome = nome;
        this.paisOrigem = paisOrigem;
    }

    @Override
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getPaisOrigem() { return paisOrigem; }
    public void setPaisOrigem(String paisOrigem) { this.paisOrigem = paisOrigem; }

    public List<Carro> getCarros() { return carros; }
    public void setCarros(List<Carro> carros) { this.carros = carros; }

    public Carro getCarroChefe() { return carroChefe; }
    public void setCarroChefe(Carro carroChefe) { this.carroChefe = carroChefe; }
}
