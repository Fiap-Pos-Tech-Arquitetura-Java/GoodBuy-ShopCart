package br.com.fiap.postech.goodbuy.shopcart.helper;
import br.com.fiap.postech.goodbuy.shopcart.entity.Item;
import br.com.fiap.postech.goodbuy.shopcart.entity.ShopCart;

import java.util.ArrayList;
import java.util.UUID;

public class ShopCartHelper {
    public static ShopCart getShopCart(boolean geraId) {
        var itens = new ArrayList<Item>();
        itens.add(new Item(UUID.randomUUID(), 5L));
        itens.add(new Item(UUID.randomUUID(), 23L));
        return new ShopCart(
                geraId ? UUID.randomUUID() : null,
                "anderson.wagner",
                itens
        );
    }
}
