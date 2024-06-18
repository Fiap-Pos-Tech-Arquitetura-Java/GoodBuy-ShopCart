package br.com.fiap.postech.goodbuy.shopcart.helper;

import br.com.fiap.postech.goodbuy.shopcart.entity.Item;

import java.util.UUID;

public class ItemHelper {
    public static Item getItem() {
        return new Item(
                UUID.randomUUID(),
                1L
        );
    }
}
