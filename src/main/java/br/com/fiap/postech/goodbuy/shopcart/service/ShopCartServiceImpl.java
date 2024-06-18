package br.com.fiap.postech.goodbuy.shopcart.service;

import br.com.fiap.postech.goodbuy.shopcart.entity.Item;
import br.com.fiap.postech.goodbuy.shopcart.entity.ShopCart;
import br.com.fiap.postech.goodbuy.shopcart.repository.ShopCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShopCartServiceImpl implements ShopCartService {
    private final ShopCartRepository shopCartRepository;

    @Autowired
    public ShopCartServiceImpl(ShopCartRepository shopCartRepository) {
        this.shopCartRepository = shopCartRepository;
    }

    @Override
    public ShopCart addItem(String login, Item item) {
        ShopCart shopCart = get(login);
        if (shopCart.getItens().contains(item)) {
            Item itemDb = shopCart.getItens().get(shopCart.getItens().indexOf(item));
            itemDb.setQuantidade(itemDb.getQuantidade() + item.getQuantidade());
        } else {
            shopCart.getItens().add(item);
        }
        shopCartRepository.save(shopCart);
        return shopCart;
    }

    @Override
    public ShopCart removeItem(String login, Item item) {
        ShopCart shopCart = get(login);
        if (shopCart.getItens().contains(item)) {
            Item itemDb = shopCart.getItens().get(shopCart.getItens().indexOf(item));
            itemDb.setQuantidade(itemDb.getQuantidade() - item.getQuantidade());
            if (itemDb.getQuantidade() < 0) {
                throw new IllegalArgumentException("A quantidade de um item não pode ser negativa.");
            } else if (itemDb.getQuantidade() == 0) {
                shopCart.getItens().remove(itemDb);
            }
        } else {
            throw new IllegalArgumentException("Item não encontrado no carrinho de compras.");
        }
        if (!shopCart.getItens().isEmpty()) {
            shopCartRepository.save(shopCart);
        } else {
            shopCartRepository.deleteById(shopCart.getId());
        }
        return shopCart;
    }

    @Override
    public ShopCart get(String login) {
        return shopCartRepository.findByLogin(login)
                .orElseGet(() -> new ShopCart(UUID.randomUUID(), login, new ArrayList<>()));
    }

    @Override
    public void delete(String login) {
        shopCartRepository.findByLogin(login)
                .ifPresent(shopCart -> shopCartRepository.deleteById(shopCart.getId()));
    }
}
