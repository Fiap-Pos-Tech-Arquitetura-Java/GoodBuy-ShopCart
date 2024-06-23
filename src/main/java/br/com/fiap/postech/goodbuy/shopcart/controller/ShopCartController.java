package br.com.fiap.postech.goodbuy.shopcart.controller;

import br.com.fiap.postech.goodbuy.shopcart.entity.Item;
import br.com.fiap.postech.goodbuy.shopcart.entity.ShopCart;
import br.com.fiap.postech.goodbuy.shopcart.security.SecurityHelper;
import br.com.fiap.postech.goodbuy.shopcart.service.ShopCartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop-cart")
@Service
public class ShopCartController {

    private final ShopCartService shopCartService;

    private final SecurityHelper securityHelper;

    @Autowired
    public ShopCartController(ShopCartService shopCartService, SecurityHelper securityHelper) {
        this.shopCartService = shopCartService;
        this.securityHelper = securityHelper;
    }

    @PostMapping(path = "/addItem", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("!hasRole('ADMIN')")
    @Operation(summary = "adiciona um item no carrinho de compras")
    public ShopCart addItem(@Valid @RequestBody Item item) {
        return shopCartService.addItem(securityHelper.getToken(), securityHelper.getLoggedUser(), item);
    }

    @PostMapping(path = "/removeItem", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("!hasRole('ADMIN')")
    @Operation(summary = "remove um item no carrinho de compras")
    public ShopCart removeItem(@Valid @RequestBody Item item) {
        return shopCartService.removeItem(securityHelper.getToken(), securityHelper.getLoggedUser(), item);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("!hasRole('ADMIN')")
    @Operation(summary = "lista todos os itens de um shop cart")
    public ShopCart get() {
        return shopCartService.get(securityHelper.getToken(), securityHelper.getLoggedUser());
    }

    @DeleteMapping
    @PreAuthorize("!hasRole('ADMIN')")
    @Operation(summary = "remove um shop cart")
    public void delete() {
        shopCartService.delete(securityHelper.getLoggedUser());
    }
}
