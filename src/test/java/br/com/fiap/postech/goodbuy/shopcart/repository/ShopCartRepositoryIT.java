package br.com.fiap.postech.goodbuy.shopcart.repository;

import br.com.fiap.postech.goodbuy.shopcart.entity.ShopCart;
import br.com.fiap.postech.goodbuy.shopcart.helper.ShopCartHelper;
import jakarta.transaction.Transactional;
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
public class ShopCartRepositoryIT {
    private final ShopCartRepository shopCartRepository;

    @Autowired
    public ShopCartRepositoryIT(ShopCartRepository shopCartRepository) {
        this.shopCartRepository = shopCartRepository;
    }

    @Test
    void devePermitirCriarEstrutura() {
        var totalRegistros = shopCartRepository.count();
        assertThat(totalRegistros).isEqualTo(4);
    }

    @Test
    void devePermitirCadastrarShopCart() {
        // Arrange
        var shopCart = ShopCartHelper.getShopCart(true);
        // Act
        var shopCartCadastrado = shopCartRepository.save(shopCart);
        // Assert
        assertThat(shopCartCadastrado).isInstanceOf(ShopCart.class).isNotNull();
        assertThat(shopCartCadastrado.getId()).isEqualTo(shopCart.getId());
        assertThat(shopCartCadastrado.getLogin()).isEqualTo(shopCart.getLogin());
    }
    @Test
    void devePermitirBuscarShopCart() {
        // Arrange
        var id = UUID.fromString("dabb5525-d775-4347-be1f-876dea84ced3");
        var nome = "anderson.wagner";
        // Act
        var shopCartOpcional = shopCartRepository.findById(id);
        // Assert
        assertThat(shopCartOpcional).isPresent();
        shopCartOpcional.ifPresent(
                shopCartRecebido -> {
                    assertThat(shopCartRecebido).isInstanceOf(ShopCart.class).isNotNull();
                    assertThat(shopCartRecebido.getId()).isEqualTo(id);
                    assertThat(shopCartRecebido.getLogin()).isEqualTo(nome);
                }
        );
    }
    @Test
    void devePermitirRemoverShopCart() {
        // Arrange
        var id = UUID.fromString("f158f211-1900-4201-abb4-87ba2369acbb");
        // Act
        shopCartRepository.findById(id).orElseThrow();
        shopCartRepository.deleteById(id);
        // Assert
        var shopCartOpcional = shopCartRepository.findById(id);
        assertThat(shopCartOpcional).isEmpty();
    }
    @Test
    void devePermitirListarShopCarts() {
        // Arrange
        // Act
        var shopCartsListados = shopCartRepository.findAll();
        // Assert
        assertThat(shopCartsListados).hasSize(4);
    }
}
