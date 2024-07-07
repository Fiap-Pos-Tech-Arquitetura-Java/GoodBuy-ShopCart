package br.com.fiap.postech.goodbuy.shopcart.service;

import br.com.fiap.postech.goodbuy.shopcart.entity.Item;
import br.com.fiap.postech.goodbuy.shopcart.entity.ShopCart;
import br.com.fiap.postech.goodbuy.shopcart.integration.ItemIntegration;
import br.com.fiap.postech.goodbuy.shopcart.repository.ShopCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShopCartServiceImpl implements ShopCartService {
    private final ShopCartRepository shopCartRepository;
    private final ItemIntegration itemIntegration;

    @Autowired
    public ShopCartServiceImpl(ShopCartRepository shopCartRepository, ItemIntegration itemIntegration) {
        this.shopCartRepository = shopCartRepository;
        this.itemIntegration = itemIntegration;
    }

    @Override
    public ShopCart addItem(String token, String login, Item item) {
        ShopCart shopCart = getOrCreate(login);
        if (shopCart.getItens().contains(item)) {
            Item itemDb = shopCart.getItens().get(shopCart.getItens().indexOf(item));
            itemDb.setQuantidade(itemDb.getQuantidade() + item.getQuantidade());
        } else {
            shopCart.getItens().add(item);
        }
        shopCartRepository.save(shopCart);
        fill(token, shopCart);
        return shopCart;
    }

    @Override
    public ShopCart removeItem(String token, String login, Item item) {
        ShopCart shopCart = getOrCreate(login);
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
        fill(token, shopCart);
        return shopCart;
    }

    @Override
    public ShopCart get(String token, String login) {
        ShopCart shopCart = getOrCreate(login);
        fill(token, shopCart);
        return shopCart;
    }

    private ShopCart getOrCreate(String login) {
        return shopCartRepository.findByLogin(login)
                .orElseGet(() -> new ShopCart(UUID.randomUUID(), login, new ArrayList<>()));
    }

    private void fill(String token, ShopCart shopCart) {
        shopCart.getItens().forEach(item -> {
            var itemAux = itemIntegration.getItem(token, item.getId());
            item.setNome(itemAux.getNome());
            item.setPreco(itemAux.getPreco());
        });
    }

    @Override
    public void delete(String login) {
        shopCartRepository.findByLogin(login).ifPresent(shopCartRepository::delete);
    }
}
