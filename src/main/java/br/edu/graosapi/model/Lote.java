package br.edu.graosapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "lotes")
public class Lote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private String variedade;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal quantidadeKg = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produtor_id", nullable = false)
    private Produtor produtor;

    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getVariedade() { return variedade; }
    public void setVariedade(String variedade) { this.variedade = variedade; }
    public BigDecimal getQuantidadeKg() { return quantidadeKg; }
    public void setQuantidadeKg(BigDecimal quantidadeKg) { this.quantidadeKg = quantidadeKg; }
    public Produtor getProdutor() { return produtor; }
    public void setProdutor(Produtor produtor) { this.produtor = produtor; }
}
