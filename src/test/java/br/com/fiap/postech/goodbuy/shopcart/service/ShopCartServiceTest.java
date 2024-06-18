package br.com.fiap.postech.goodbuy.shopcart.service;

import br.com.fiap.postech.goodbuy.shopcart.entity.Item;
import br.com.fiap.postech.goodbuy.shopcart.entity.ShopCart;
import br.com.fiap.postech.goodbuy.shopcart.helper.ItemHelper;
import br.com.fiap.postech.goodbuy.shopcart.helper.ShopCartHelper;
import br.com.fiap.postech.goodbuy.shopcart.repository.ShopCartRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ShopCartServiceTest {
    private ShopCartService shopCartService;

    @Mock
    private ShopCartRepository shopCartRepository;


    private AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        shopCartService = new ShopCartServiceImpl(shopCartRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Nested
    class AdicionarItemShopCart {
        @Test
        void devePermitirAdicionarItem_EmptyShopCart() {
            // Arrange
            var item = ItemHelper.getItem();
            when(shopCartRepository.findByLogin(anyString())).thenReturn(Optional.empty());
            when(shopCartRepository.save(any(ShopCart.class))).thenAnswer(r -> r.getArgument(0));
            // Act
            var shopCartSalvo = shopCartService.addItem("login", item);
            // Assert
            assertThat(shopCartSalvo)
                    .isInstanceOf(ShopCart.class)
                    .isNotNull();
            assertThat(shopCartSalvo.getId()).isNotNull();
            assertThat(shopCartSalvo.getItens()).isNotNull();
            assertThat(shopCartSalvo.getItens()).containsOnly(item);
            assertThat(shopCartSalvo.getItens().get(0).getQuantidade()).isEqualTo(item.getQuantidade());
            verify(shopCartRepository, times(1)).findByLogin(anyString());
            verify(shopCartRepository, times(1)).save(any(ShopCart.class));
        }

        @Test
        void devePermitirAdicionarItem_ItemJaEstaNoShopCart() {
            // Arrange
            var shopCart = ShopCartHelper.getShopCart(true);
            var quantidadeOriginal = shopCart.getItens().get(0).getQuantidade();
            var item = new Item(shopCart.getItens().get(0).getId(), 10L);
            when(shopCartRepository.findByLogin(anyString())).thenReturn(Optional.of(shopCart));
            when(shopCartRepository.save(any(ShopCart.class))).thenAnswer(r -> r.getArgument(0));
            // Act
            var shopCartSalvo = shopCartService.addItem("login", item);
            // Assert
            assertThat(shopCartSalvo)
                    .isInstanceOf(ShopCart.class)
                    .isNotNull();
            assertThat(shopCartSalvo.getId()).isNotNull();
            assertThat(shopCartSalvo.getItens()).isNotNull();
            assertThat(shopCartSalvo.getItens()).contains(item);
            assertThat(shopCartSalvo.getItens().get(0).getQuantidade()).isEqualTo(quantidadeOriginal + item.getQuantidade());
            verify(shopCartRepository, times(1)).findByLogin(anyString());
            verify(shopCartRepository, times(1)).save(any(ShopCart.class));
        }
    }

    @Nested
    class RemoverItemShopCart {
        @Test
        void devePermitirRemoverItem() {
            // Arrange
            var shopCart = ShopCartHelper.getShopCart(true);
            when(shopCartRepository.findByLogin(anyString())).thenReturn(Optional.of(shopCart));
            when(shopCartRepository.save(any(ShopCart.class))).thenAnswer(r -> r.getArgument(0));
            // Act
            var shopCartSalvo = shopCartService.removeItem("login", shopCart.getItens().stream().findFirst().orElseThrow());
            // Assert
            assertThat(shopCartSalvo)
                    .isInstanceOf(ShopCart.class)
                    .isNotNull();
            assertThat(shopCartSalvo.getId()).isNotNull();
            assertThat(shopCartSalvo.getItens()).isNotNull();
            verify(shopCartRepository, times(1)).findByLogin(anyString());
            verify(shopCartRepository, times(1)).save(any(ShopCart.class));
            verify(shopCartRepository, never()).deleteById(any(UUID.class));
        }

        @Test
        void devePermitirRemoverItem_EmptyShopCart() {
            // Arrange
            var shopCart = ShopCartHelper.getShopCart(true);
            shopCart.getItens().remove(0);
            when(shopCartRepository.findByLogin(anyString())).thenReturn(Optional.of(shopCart));
            when(shopCartRepository.save(any(ShopCart.class))).thenAnswer(r -> r.getArgument(0));
            // Act
            var shopCartSalvo = shopCartService.removeItem("login", shopCart.getItens().stream().findFirst().orElseThrow());
            // Assert
            assertThat(shopCartSalvo)
                    .isInstanceOf(ShopCart.class)
                    .isNotNull();
            assertThat(shopCartSalvo.getId()).isNotNull();
            assertThat(shopCartSalvo.getItens()).isNotNull();
            verify(shopCartRepository, times(1)).findByLogin(anyString());
            verify(shopCartRepository, never()).save(any(ShopCart.class));
            verify(shopCartRepository, times(1)).deleteById(any(UUID.class));
        }

        @Test
        void deveGerarExcecao_QuandoRemoverItem_QuantidadeASerRemovidaMaiorQueDisponivel() {
            // Arrange
            var shopCart = ShopCartHelper.getShopCart(true);
            var item = new Item(shopCart.getItens().get(0).getId(), 99L);
            when(shopCartRepository.findByLogin(anyString())).thenReturn(Optional.of(shopCart));
            when(shopCartRepository.save(any(ShopCart.class))).thenAnswer(r -> r.getArgument(0));
            // Act && Assert
            assertThatThrownBy(() -> shopCartService.removeItem("login", item))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("A quantidade de um item não pode ser negativa.");
            verify(shopCartRepository, times(1)).findByLogin(anyString());
            verify(shopCartRepository, never()).save(any(ShopCart.class));
        }

        @Test
        void deveGerarExcecao_QuandoRemoverItem_ItemNaoEstaNoCarrinho() {
            // Arrange
            var shopCart = ShopCartHelper.getShopCart(true);
            var item = ItemHelper.getItem();
            when(shopCartRepository.findByLogin(anyString())).thenReturn(Optional.of(shopCart));
            when(shopCartRepository.save(any(ShopCart.class))).thenAnswer(r -> r.getArgument(0));
            // Act && Assert
            assertThatThrownBy(() -> shopCartService.removeItem("login", item))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Item não encontrado no carrinho de compras.");
            verify(shopCartRepository, times(1)).findByLogin(anyString());
            verify(shopCartRepository, never()).save(any(ShopCart.class));
        }
    }

    @Nested
    class BuscarShopCart {
        @Test
        void devePermitirBuscarShopCartPorId() {
            // Arrange
            when(shopCartRepository.findByLogin(anyString())).thenReturn(Optional.empty());
            // Act
            var shopCartObtido = shopCartService.get("login");
            // Assert
            assertThat(shopCartObtido).isNotNull();
            assertThat(shopCartObtido.getItens()).isNotNull();
            assertThat(shopCartObtido.getItens()).hasSizeLessThanOrEqualTo(0);
            verify(shopCartRepository, times(1)).findByLogin(anyString());
        }
    }

    @Nested
    class RemoverShopCart {
        @Test
        void devePermitirRemoverShopCart_EmptyShopCart() {
            // Arrange
            when(shopCartRepository.findByLogin(anyString())).thenReturn(Optional.empty());
            // Act
            shopCartService.delete("login");
            // Assert
            verify(shopCartRepository, times(1)).findByLogin(anyString());
            verify(shopCartRepository, never()).deleteById(any(UUID.class));
        }

        @Test
        void devePermitirRemoverShopCart() {
            // Arrange
            var shopCart = ShopCartHelper.getShopCart(true);
            when(shopCartRepository.findByLogin(anyString())).thenReturn(Optional.of(shopCart));
            doNothing().when(shopCartRepository).deleteById(shopCart.getId());
            // Act
            shopCartService.delete("login");
            // Assert
            verify(shopCartRepository, times(1)).findByLogin(anyString());
            verify(shopCartRepository, times(1)).deleteById(any(UUID.class));
        }
    }
}
