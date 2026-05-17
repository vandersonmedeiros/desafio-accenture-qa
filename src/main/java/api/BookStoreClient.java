package api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BookStoreClient {

    private static final String BASE_URL = "https://demoqa.com";

    public Response criarUsuario(String userName, String userPassword) {
        Map<String, String> requestPayload = new HashMap<>();
        requestPayload.put("userName", userName);
        requestPayload.put("password", userPassword);

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestPayload)
                .post(BASE_URL + "/Account/v1/User");
    }

    public Response gerarToken(String userName, String userPassword) {
        Map<String, String> requestPayload = new HashMap<>();
        requestPayload.put("userName", userName);
        requestPayload.put("password", userPassword);

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestPayload)
                .post(BASE_URL + "/Account/v1/GenerateToken");
    }

    public Response validarAutorizacao(String userName, String userPassword) {
        Map<String, String> requestPayload = new HashMap<>();
        requestPayload.put("userName", userName);
        requestPayload.put("password", userPassword);

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestPayload)
                .post(BASE_URL + "/Account/v1/Authorized");
    }

    public Response listarLivros() {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .get(BASE_URL + "/BookStore/v1/Books");
    }

    public Response alugarLivro(String userId, String bookIsbn, String accessToken) {
        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("userId", userId);
        requestPayload.put("collectionOfIsbns", Collections.singletonList(Collections.singletonMap("isbn", bookIsbn)));

        return RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(requestPayload)
                .post(BASE_URL + "/BookStore/v1/Books");
    }

    public Response consultarPerfil(String userId, String accessToken) {
        return RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .get(BASE_URL + "/Account/v1/User/" + userId);
    }
}
