package br.com.fiap.postech.goodbuy.shopcart.service;

import br.com.fiap.postech.goodbuy.shopcart.entity.Item;
import br.com.fiap.postech.goodbuy.shopcart.entity.ShopCart;
import br.com.fiap.postech.goodbuy.shopcart.helper.ItemHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
public class ShopCartServiceIT {

    @Autowired
    private ShopCartService shopCartService;

    @Nested
    class AdicionarItemShopCart {
        @Test
        void devePermitirAdicionarItem_EmptyShopCart() {
            // Arrange
            var item = ItemHelper.getItem();
            // Act
            var shopCartSalvo = shopCartService.addItem("anderson.wagner", item);
            // Assert
            assertThat(shopCartSalvo)
                    .isInstanceOf(ShopCart.class)
                    .isNotNull();
            assertThat(shopCartSalvo.getId()).isNotNull();
            assertThat(shopCartSalvo.getItens()).isNotNull();
            assertThat(shopCartSalvo.getItens()).containsOnly(item);
            assertThat(shopCartSalvo.getItens().get(0).getQuantidade()).isEqualTo(item.getQuantidade());
            assertThat(shopCartSalvo.getId()).isNotNull();
        }
    }

    @Nested
    class RemoverItemShopCart {
        @Test
        void devePermitirRemoverItem_EmptyShopCart() {
            // Arrange
            var item = new Item(UUID.fromString("93b7e9d1-3632-4cae-8e66-4a22505e7470"), 5L);
            // Act
            var shopCartSalvo = shopCartService.removeItem("kaiby.santos", item);
            // Assert
            assertThat(shopCartSalvo)
                    .isInstanceOf(ShopCart.class)
                    .isNotNull();
            assertThat(shopCartSalvo.getId()).isNotNull();
            assertThat(shopCartSalvo.getItens()).isNotNull();
            assertThat(shopCartSalvo.getItens()).containsOnly(item);
            assertThat(shopCartSalvo.getItens().get(0).getQuantidade()).isEqualTo(10L);
        }
    }

    @Nested
    class BuscarPedido {
        @Test
        void devePermitirBuscarPedidoPorId() {
            // Arrange
            // Act
            var pedidoObtido = shopCartService.get("anderson.wagner");
            // Assert
            assertThat(pedidoObtido).isNotNull().isInstanceOf(ShopCart.class);
            assertThat(pedidoObtido.getId()).isNotNull();
        }
    }

    @Nested
    class RemoverPedido {
        @Test
        void devePermitirRemoverPedido() {
            // Arrange
            var login = "janaina.alvares";
            // Act
            shopCartService.delete(login);
            // Assert
            var pedidoObtido = shopCartService.get(login);
            assertThat(pedidoObtido.getId()).isNotNull();
            assertThat(pedidoObtido.getItens()).isNotNull();
            assertThat(pedidoObtido.getItens()).isEmpty();
        }
    }
}
