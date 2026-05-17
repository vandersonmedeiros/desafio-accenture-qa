package api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BookStoreClient {

    private final String baseUrl = "https://demoqa.com";

    public Response criarUsuario(String username, String password) {
        Map<String, String> payload = new HashMap<>();
        payload.put("userName", username);
        payload.put("password", password);

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post(baseUrl + "/Account/v1/User");
    }

    public Response gerarToken(String username, String password) {
        Map<String, String> payload = new HashMap<>();
        payload.put("userName", username);
        payload.put("password", password);

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post(baseUrl + "/Account/v1/GenerateToken");
    }

    public Response validarAutorizacao(String username, String password) {
        Map<String, String> payload = new HashMap<>();
        payload.put("userName", username);
        payload.put("password", password);

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post(baseUrl + "/Account/v1/Authorized");
    }

    public Response listarLivros() {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .get(baseUrl + "/BookStore/v1/Books");
    }

    public Response alugarLivro(String userId, String isbn, String token) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);
        payload.put("collectionOfIsbns", Collections.singletonList(Collections.singletonMap("isbn", isbn)));

        return RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(payload)
                .post(baseUrl + "/BookStore/v1/Books");
    }

    public Response consultarPerfil(String userId, String token) {
        return RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .get(baseUrl + "/Account/v1/User/" + userId);
    }
}
