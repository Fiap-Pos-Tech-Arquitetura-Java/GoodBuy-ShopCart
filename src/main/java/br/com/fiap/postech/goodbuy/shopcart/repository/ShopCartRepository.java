package br.com.fiap.postech.goodbuy.shopcart.repository;

import br.com.fiap.postech.goodbuy.shopcart.entity.ShopCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShopCartRepository extends JpaRepository<ShopCart, UUID> {
    Optional<ShopCart> findByLogin(String login);
}
