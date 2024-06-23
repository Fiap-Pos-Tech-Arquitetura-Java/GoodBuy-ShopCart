package br.com.fiap.postech.goodbuy.shopcart.service;

import br.com.fiap.postech.goodbuy.shopcart.entity.Item;
import br.com.fiap.postech.goodbuy.shopcart.entity.ShopCart;
import br.com.fiap.postech.goodbuy.shopcart.helper.ItemHelper;
import br.com.fiap.postech.goodbuy.shopcart.integration.ItemIntegration;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
public class ShopCartServiceIT {

    @Autowired
    private ShopCartService shopCartService;

    @MockBean
    private ItemIntegration itemIntegration;

    @Nested
    class AdicionarItemShopCart {
        @Test
        void devePermitirAdicionarItem_EmptyShopCart() {
            // Arrange
            var item = ItemHelper.getItem();
            var itemForIntegration = ItemHelper.getItemForIntegration(item);
            when(itemIntegration.getItem(anyString(), any(UUID.class))).thenReturn(itemForIntegration);
            // Act
            var shopCartSalvo = shopCartService.addItem("token","anderson.wagner", item);
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
            var itemForIntegration = ItemHelper.getItemForIntegration(item);
            when(itemIntegration.getItem(anyString(), any(UUID.class))).thenReturn(itemForIntegration);
            // Act
            var shopCartSalvo = shopCartService.removeItem("token","kaiby.santos", item);
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
    class BuscarShopCart {
        @Test
        void devePermitirBuscarShopCartPorId() {
            // Arrange
            // Act
            var pedidoObtido = shopCartService.get("token","anderson.wagner");
            // Assert
            assertThat(pedidoObtido).isNotNull().isInstanceOf(ShopCart.class);
            assertThat(pedidoObtido.getId()).isNotNull();
        }
    }

    @Nested
    class RemoverShopCart {
        @Test
        void devePermitirRemoverShopCart() {
            // Arrange
            var login = "janaina.alvares";
            // Act
            shopCartService.delete(login);
            // Assert
            var pedidoObtido = shopCartService.get("token", login);
            assertThat(pedidoObtido.getId()).isNotNull();
            assertThat(pedidoObtido.getItens()).isNotNull();
            assertThat(pedidoObtido.getItens()).isEmpty();
        }
    }
}
