package br.com.fiap.postech.goodbuy.shopcart.repository;

import br.com.fiap.postech.goodbuy.shopcart.entity.ShopCart;
import br.com.fiap.postech.goodbuy.shopcart.helper.ShopCartHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ShopCartRepositoryTest {
    @Mock
    private ShopCartRepository shopCartRepository;

    AutoCloseable openMocks;
    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirCadastrarShopCart() {
        // Arrange
        var shopCart = ShopCartHelper.getShopCart(false);
        when(shopCartRepository.save(any(ShopCart.class))).thenReturn(shopCart);
        // Act
        var savedShopCart = shopCartRepository.save(shopCart);
        // Assert
        assertThat(savedShopCart).isNotNull().isEqualTo(shopCart);
        verify(shopCartRepository, times(1)).save(any(ShopCart.class));
    }

    @Test
    void devePermitirBuscarShopCart() {
        // Arrange
        var shopCart = ShopCartHelper.getShopCart(true);
        when(shopCartRepository.findById(shopCart.getId())).thenReturn(Optional.of(shopCart));
        // Act
        var shopCartOpcional = shopCartRepository.findById(shopCart.getId());
        // Assert
        assertThat(shopCartOpcional).isNotNull().containsSame(shopCart);
        shopCartOpcional.ifPresent(
                shopCartRecebido -> {
                    assertThat(shopCartRecebido).isInstanceOf(ShopCart.class).isNotNull();
                    assertThat(shopCartRecebido.getId()).isEqualTo(shopCart.getId());
                    assertThat(shopCartRecebido.getLogin()).isEqualTo(shopCart.getLogin());
                }
        );
        verify(shopCartRepository, times(1)).findById(shopCart.getId());
    }
    @Test
    void devePermitirRemoverShopCart() {
        //Arrange
        var id = UUID.randomUUID();
        doNothing().when(shopCartRepository).deleteById(id);
        //Act
        shopCartRepository.deleteById(id);
        //Assert
        verify(shopCartRepository, times(1)).deleteById(id);
    }
    @Test
    void devePermitirListarShopCarts() {
        // Arrange
        var shopCart1 = ShopCartHelper.getShopCart(true);
        var shopCart2 = ShopCartHelper.getShopCart(true);
        var listaShopCarts = Arrays.asList(
                shopCart1,
                shopCart2
        );
        when(shopCartRepository.findAll()).thenReturn(listaShopCarts);
        // Act
        var shopCartsListados = shopCartRepository.findAll();
        assertThat(shopCartsListados)
                .hasSize(2)
                .containsExactlyInAnyOrder(shopCart1, shopCart2);
        verify(shopCartRepository, times(1)).findAll();
    }
}