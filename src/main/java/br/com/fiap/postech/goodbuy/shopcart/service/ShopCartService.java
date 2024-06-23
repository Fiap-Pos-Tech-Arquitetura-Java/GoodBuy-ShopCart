package br.com.fiap.postech.goodbuy.shopcart.service;

import br.com.fiap.postech.goodbuy.shopcart.entity.Item;
import br.com.fiap.postech.goodbuy.shopcart.entity.ShopCart;

public interface ShopCartService {
    ShopCart addItem(String token, String login, Item item);

    ShopCart removeItem(String token, String login, Item item);

    ShopCart get(String token, String login);

    void delete(String login);
}
