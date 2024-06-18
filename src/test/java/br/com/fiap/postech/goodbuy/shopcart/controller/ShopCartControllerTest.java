package br.com.fiap.postech.goodbuy.shopcart.controller;

import br.com.fiap.postech.goodbuy.shopcart.entity.Item;
import br.com.fiap.postech.goodbuy.shopcart.helper.ShopCartHelper;
import br.com.fiap.postech.goodbuy.shopcart.security.SecurityHelper;
import br.com.fiap.postech.goodbuy.shopcart.service.ShopCartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ShopCartControllerTest {
    public static final String SHOPCART = "/shop-cart";

    public static final String ADD_ITEM = "/addItem";

    public static final String REMOVE_ITEM = "/removeItem";
    private static final String USER_LOGIN = "login_do_usuario";
    private MockMvc mockMvc;
    @Mock
    private ShopCartService shopCartService;
    @Mock
    private SecurityHelper securityHelper;

    private AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        ShopCartController shopCartController = new ShopCartController(shopCartService, securityHelper);
        mockMvc = MockMvcBuilders.standaloneSetup(shopCartController).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    public static String asJsonString(final Object object) throws Exception {
        return new ObjectMapper().writeValueAsString(object);
    }

    @Nested
    class AdicionarItemShopCart {
        @Test
        void devePermitirAdicionarItem_EmptyShopCart() throws Exception {
            // Arrange
            var shopCart = ShopCartHelper.getShopCart(false);
            when(shopCartService.addItem(anyString(), any(Item.class))).thenReturn(shopCart);
            when(securityHelper.getLoggedUser()).thenReturn(USER_LOGIN);
            // Act
            mockMvc.perform(
                            post(SHOPCART + ADD_ITEM).contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(shopCart.getItens().get(0))))
                    .andExpect(status().isOk());
            // Assert
            verify(shopCartService, times(1)).addItem(anyString(), any(Item.class));
            verify(securityHelper, times(1)).getLoggedUser();
        }

        @Test
        void deveGerarExcecao_QuandoRegistrarShopCart_RequisicaoXml() throws Exception {
            // Arrange
            var shopCart = ShopCartHelper.getShopCart(false);
            when(shopCartService.addItem(anyString(), any(Item.class))).thenReturn(shopCart);
            // Act
            mockMvc.perform(
                            post(SHOPCART + ADD_ITEM).contentType(MediaType.APPLICATION_XML)
                                    .content(asJsonString(shopCart.getItens().get(0))))
                    .andExpect(status().isUnsupportedMediaType());
            // Assert
            verify(shopCartService, never()).addItem(anyString(), any(Item.class));
        }
    }

    @Nested
    class BuscarShopCart {
        @Test
        void devePermitirBuscarShopCartPorId() throws Exception {
            // Arrange
            var shopCart = ShopCartHelper.getShopCart(true);
            when(shopCartService.get(anyString())).thenReturn(shopCart);
            when(securityHelper.getLoggedUser()).thenReturn(USER_LOGIN);
            // Act
            mockMvc.perform(get(SHOPCART, shopCart.getId().toString()))
                    .andExpect(status().isOk());
            // Assert
            verify(shopCartService, times(1)).get(anyString());
            verify(securityHelper, times(1)).getLoggedUser();
        }
    }

    @Nested
    class RemoverItemShopCart {
        @Test
        void devePermitirRemoverItem_EmptyShopCart() throws Exception {
            // Arrange
            var shopCart = ShopCartHelper.getShopCart(false);
            when(shopCartService.removeItem(anyString(), any(Item.class))).thenReturn(shopCart);
            when(securityHelper.getLoggedUser()).thenReturn(USER_LOGIN);
            // Act
            mockMvc.perform(
                            post(SHOPCART + REMOVE_ITEM).contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(shopCart.getItens().get(0))))
                    .andExpect(status().isOk());
            // Assert
            verify(shopCartService, times(1)).removeItem(anyString(), any(Item.class));
            verify(securityHelper, times(1)).getLoggedUser();
        }

        @Test
        void deveGerarExcecao_QuandoRegistrarShopCart_RequisicaoXml() throws Exception {
            // Arrange
            var shopCart = ShopCartHelper.getShopCart(false);
            when(shopCartService.removeItem(anyString(), any(Item.class))).thenReturn(shopCart);
            // Act
            mockMvc.perform(
                            post(SHOPCART + REMOVE_ITEM).contentType(MediaType.APPLICATION_XML)
                                    .content(asJsonString(shopCart.getItens().get(0))))
                    .andExpect(status().isUnsupportedMediaType());
            // Assert
            verify(shopCartService, never()).removeItem(anyString(), any(Item.class));
        }
    }
}