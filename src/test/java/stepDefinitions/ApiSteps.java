package stepDefinitions;

import api.BookStoreClient;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.Assert;
import java.util.UUID;

public class ApiSteps {

    private BookStoreClient apiClient;
    private Response response;
    private String username;
    private String password;
    private String userId;
    private String token;
    private String isbn;

    @Given("que inicializo a base da API DemoQA")
    public void queInicializoABaseDaApiDemoQA() {
        apiClient = new BookStoreClient();
        String sufixoAleatorio = UUID.randomUUID().toString().substring(0, 8);
        username = "user_" + sufixoAleatorio;
        password = "Password123!";
    }

    @When("envio uma requisicao POST para criar um novo usuario")
    public void envioUmaRequisicaoPostParaCriarUmNovoUsuario() {
        response = apiClient.criarUsuario(username, password);
    }

    @Then("o usuario deve ser criado com sucesso retornando o ID")
    public void oUsuarioDeveSerCriadoComSucessoRetornandoOId() {
        Assert.assertEquals(201, response.getStatusCode());
        userId = response.jsonPath().getString("userID");
        Assert.assertNotNull(userId);
    }

    @When("envio uma requisicao POST para gerar o token de acesso")
    public void envioUmaRequisicaoPostParaGerarOTokenDeAcesso() {
        response = apiClient.gerarToken(username, password);
    }

    @Then("o token deve ser gerado com sucesso")
    public void oTokenDeveSerGeradoComSucesso() {
        Assert.assertEquals(200, response.getStatusCode());
        token = response.jsonPath().getString("token");
        Assert.assertEquals("Success", response.jsonPath().getString("status"));
        Assert.assertNotNull(token);
    }

    @When("envio uma requisicao POST para validar a autorizacao")
    public void envioUmaRequisicaoPostParaValidarAAutorizacao() {
        response = apiClient.validarAutorizacao(username, password);
    }

    @Then("a resposta de autorizacao deve ser verdadeira")
    public void aRespostaDeAutorizacaoDeveSerVeradeira() {
        Assert.assertEquals(200, response.getStatusCode());
        Assert.assertEquals("true", response.getBody().asString());
    }

    @When("envio uma requisicao GET para listar os livros disponiveis")
    public void envioUmaRequisicaoGetParaListarOsLivrosDisponiveis() {
        response = apiClient.listarLivros();
    }

    @Then("a lista de livros deve ser retornada com sucesso")
    public void aListaDeLivrosDeveSerRetornadaComSucesso() {
        Assert.assertEquals(200, response.getStatusCode());
        isbn = response.jsonPath().getString("books[0].isbn");
        Assert.assertNotNull(isbn);
    }

    @When("envio uma requisicao POST para alugar o primeiro livro da lista")
    public void envioUmaRequisicaoPostParaAlugarOPrimeiroLivroDaLista() {
        response = apiClient.alugarLivro(userId, isbn, token);
    }

    @Then("o livro deve ser associado ao usuario com sucesso")
    public void oLivroDeveSerAssociadoAoUsuarioComSucesso() {
        Assert.assertEquals(201, response.getStatusCode());
    }

    @When("envio uma requisicao GET para consultar o perfil do usuario")
    public void envioUmaRequisicaoGetParaConsultarOPerfilDoUsuario() {
        response = apiClient.consultarPerfil(userId, token);
    }

    @Then("o livro alugado deve constar no perfil do usuario")
    public void oLivroAlugadoDeveConstarNoPerfilDoUsuario() {
        Assert.assertEquals(200, response.getStatusCode());
        String isbnNoPerfil = response.jsonPath().getString("books[0].isbn");
        Assert.assertEquals(isbn, isbnNoPerfil);
    }
}
