package br.com.fiap.postech.goodbuy.shopcart.entity;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_shop_cart_item")
public class Item {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;
    @Column(name = "quantidade", nullable = false)
    private Long quantidade;
    @Transient
    private String nome;
    @Transient
    private Double preco;

    public Item() {
        super();
    }

    public Item(UUID id, Long quantidade) {
        this.id = id;
        this.quantidade = quantidade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }
}
