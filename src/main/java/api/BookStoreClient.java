package api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Map;

public class BookStoreClient {

    private static final String BASE_URL = "https://demoqa.com";

    private RequestSpecification baseRequest() {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON);
    }

    private Map<String, String> createAuthPayload(String userName, String userPassword) {
        return Map.of(
                "userName", userName,
                "password", userPassword
        );
    }

    public Response criarUsuario(String userName, String userPassword) {
        return baseRequest()
                .body(createAuthPayload(userName, userPassword))
                .post("/Account/v1/User");
    }

    public Response gerarToken(String userName, String userPassword) {
        return baseRequest()
                .body(createAuthPayload(userName, userPassword))
                .post("/Account/v1/GenerateToken");
    }

    public Response validarAutorizacao(String userName, String userPassword) {
        return baseRequest()
                .body(createAuthPayload(userName, userPassword))
                .post("/Account/v1/Authorized");
    }

    public Response listarLivros() {
        return baseRequest()
                .get("/BookStore/v1/Books");
    }

    public Response alugarLivro(String userId, String bookIsbn, String accessToken) {
        Map<String, Object> requestPayload = Map.of(
                "userId", userId,
                "collectionOfIsbns", List.of(Map.of("isbn", bookIsbn))
        );

        return baseRequest()
                .header("Authorization", "Bearer " + accessToken)
                .body(requestPayload)
                .post("/BookStore/v1/Books");
    }

    public Response consultarPerfil(String userId, String accessToken) {
        return baseRequest()
                .header("Authorization", "Bearer " + accessToken)
                .get("/Account/v1/User/" + userId);
    }
}