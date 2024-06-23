package br.com.fiap.postech.goodbuy.shopcart.integration;

import br.com.fiap.postech.goodbuy.shopcart.entity.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
public class ItemIntegration {

    @Value("${REMOTE_BASE_URI:http://localhost:8081/goodbuy/item/{id}}")
    String baseURI;

    public Item getItem(String token, UUID id) {
        RestClient restClient = RestClient.create();
        Item item = restClient.get()
                .uri(baseURI, id)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new IllegalArgumentException("" + response.getStatusCode());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new IllegalArgumentException("" + response.getStatusCode());
                })
                .body(Item.class);
        if (item != null) {
            return item;
        } else {
            throw new IllegalArgumentException("Èah, deu ruim, não achei o Item");
        }
    }
}
