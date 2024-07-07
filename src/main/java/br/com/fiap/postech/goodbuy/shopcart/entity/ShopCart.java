package br.com.fiap.postech.goodbuy.shopcart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_shop_cart")
public class ShopCart {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;
    @Column(name = "login", nullable = false)
    @JsonIgnore
    private String login;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_shop_cart", nullable = false)
    List<Item> itens;

    public ShopCart() {
        super();
    }

    public ShopCart(UUID id, String login, List<Item> itens) {
        this();
        this.id = id;
        this.login = login;
        this.itens = itens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShopCart shopCart)) return false;
        return Objects.equals(id, shopCart.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public UUID getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public List<Item> getItens() {
        return itens;
    }
}
