package br.com.fiap.postech.goodbuy.shopcart.controller;

import br.com.fiap.postech.goodbuy.shopcart.entity.Item;
import br.com.fiap.postech.goodbuy.shopcart.helper.ItemHelper;
import br.com.fiap.postech.goodbuy.shopcart.helper.UserHelper;
import br.com.fiap.postech.goodbuy.shopcart.integration.ItemIntegration;
import br.com.fiap.postech.goodbuy.shopcart.security.UserDetailsServiceImpl;
import br.com.fiap.postech.goodbuy.shopcart.security.enums.UserRole;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class ShopCartControllerIT {

    public static final String SHOPCART = "/goodbuy/shop-cart";

    public static final String ADD_ITEM = "/addItem";

    public static final String REMOVE_ITEM = "/removeItem";
    @LocalServerPort
    private int port;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private ItemIntegration itemIntegration;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Nested
    class AdicionarItemShopCart {
        @Test
        void devePermitirAdicionarItem_EmptyShopCart() {
            var user = UserHelper.getUser(true, "anderson.wagner", UserRole.USER);
            var userDetails = UserHelper.getUserDetails(user);
            when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
            var item = ItemHelper.getItem();
            var itemForIntegration = ItemHelper.getItemForIntegration(item);
            when(itemIntegration.getItem(anyString(), any(UUID.class))).thenReturn(itemForIntegration);

            given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, UserHelper.getToken(user))
                .body(item)
            .when()
                .post(SHOPCART + ADD_ITEM)
            .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("schemas/shop-cart.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoCadastrarShopCart_RequisicaoXml() {
            var user = UserHelper.getUser(true, "anderson.wagner", UserRole.USER);
            var userDetails = UserHelper.getUserDetails(user);
            when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

            given()
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .header(HttpHeaders.AUTHORIZATION, UserHelper.getToken(user))
            .when()
                .post(SHOPCART + ADD_ITEM)
            .then()
                .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoAdicionarItem_UserAdministrativo() {
            var user = UserHelper.getUser(true, "anderson.wagner", UserRole.ADMIN);
            var userDetails = UserHelper.getUserDetails(user);
            when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

            var item = ItemHelper.getItem();
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, UserHelper.getToken(user))
                    .body(item)
                    .when()
                    .post(SHOPCART + ADD_ITEM)
                    .then()
                    .statusCode(HttpStatus.FORBIDDEN.value())
                    .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"));
        }
    }

    @Nested
    class BuscarShopCart {
        @Test
        void devePermitirBuscarShopCart() {
            var user = UserHelper.getUser(true, "anderson.wagner", UserRole.USER);
            var userDetails = UserHelper.getUserDetails(user);
            when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

            given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, UserHelper.getToken(user))
            .when()
                .get(SHOPCART)
            .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("schemas/shop-cart.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoBuscarShopCart_UserAdministrativo() {
            var user = UserHelper.getUser(true, "anderson.wagner", UserRole.ADMIN);
            var userDetails = UserHelper.getUserDetails(user);
            when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, UserHelper.getToken(user))
                    .when()
                    .get(SHOPCART)
                    .then()
                    .statusCode(HttpStatus.FORBIDDEN.value())
                    .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"));
        }
    }


    @Nested
    class RemoverItemShopCart {
        @Test
        void devePermitirRemoverItem_EmptyShopCart() {
            var user = UserHelper.getUser(true, "usuario.comum", UserRole.USER);
            var userDetails = UserHelper.getUserDetails(user);
            when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
            var item = new Item(UUID.fromString("1e33307b-4c47-4407-8ebc-c60ef45d7f76"), 5L);
            var itemForIntegration = ItemHelper.getItemForIntegration(item);
            when(itemIntegration.getItem(anyString(), any(UUID.class))).thenReturn(itemForIntegration);

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, UserHelper.getToken(user))
                    .body(item)
                    .when()
                    .post(SHOPCART + REMOVE_ITEM)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(matchesJsonSchemaInClasspath("schemas/shop-cart.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoRemoverShopCart_RequisicaoXml() {
            var user = UserHelper.getUser(true, "anderson.wagner", UserRole.USER);
            var userDetails = UserHelper.getUserDetails(user);
            when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

            given()
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, UserHelper.getToken(user))
                    .when()
                    .post(SHOPCART + REMOVE_ITEM)
                    .then()
                    .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                    .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoRemoverShopCart_UserAdministrativo() {
            var user = UserHelper.getUser(true, "usuario.comum", UserRole.ADMIN);
            var userDetails = UserHelper.getUserDetails(user);
            when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

            var item = new Item(UUID.fromString("1e33307b-4c47-4407-8ebc-c60ef45d7f76"), 5L);
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, UserHelper.getToken(user))
                    .body(item)
                    .when()
                    .post(SHOPCART + REMOVE_ITEM)
                    .then()
                    .statusCode(HttpStatus.FORBIDDEN.value())
                    .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"));
        }
    }
}
