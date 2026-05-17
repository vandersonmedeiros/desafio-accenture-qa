package stepDefinitions;

import api.BookStoreClient;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.Assert;
import java.util.UUID;

public class ApiSteps {

    private final BookStoreClient bookStoreClient = new BookStoreClient();
    private Response apiResponse;
    private String userName;
    private String userPassword;
    private String userId;
    private String accessToken;
    private String bookIsbn;

    @Given("que possuo as credenciais de um novo usuario")
    public void quePossuoAsCredenciaisDeUmNovoUsuario() {
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8);
        userName = "user_" + randomSuffix;
        userPassword = "Password123!";
    }

    @When("envio uma requisicao para cadastrar o usuario")
    public void envioUmaRequisicaoParaCadastrarOUsuario() {
        apiResponse = bookStoreClient.criarUsuario(userName, userPassword);
        Assert.assertEquals(201, apiResponse.getStatusCode());
        userId = apiResponse.jsonPath().getString("userID");
        Assert.assertNotNull(userId);
    }

    @And("solicito a geracao do token de acesso")
    public void solicitoAGeracaoDoTokenDeAcesso() {
        apiResponse = bookStoreClient.gerarToken(userName, userPassword);
        Assert.assertEquals(200, apiResponse.getStatusCode());
        accessToken = apiResponse.jsonPath().getString("token");
        Assert.assertNotNull(accessToken);
    }

    @And("valido a autorizacao das credenciais na API")
    public void validoAAutorizacaoDasCredenciaisNaAPI() {
        apiResponse = bookStoreClient.validarAutorizacao(userName, userPassword);
        Assert.assertEquals(200, apiResponse.getStatusCode());
    }

    @Then("o usuario deve ser autenticado e autorizado com sucesso")
    public void oUsuarioDeveSerAutenticadoEAutorizadoComSucesso() {
        Assert.assertEquals("true", apiResponse.getBody().asString());
    }

    @Given("que a API de livros esta operacional")
    public void queAApiDeLivrosEstaOperacional() {
        Assert.assertNotNull(bookStoreClient);
    }

    @When("envio uma requisicao para listar os livros")
    public void envioUmaRequisicaoParaListarOsLivros() {
        apiResponse = bookStoreClient.listarLivros();
    }

    @Then("o catalogo deve retornar a lista de livros cadastrados")
    public void oCatalogoDeveRetornarAListaDeLivrosCadastrados() {
        Assert.assertEquals(200, apiResponse.getStatusCode());
        Assert.assertNotNull(apiResponse.jsonPath().getList("books"));
    }

    @Given("que possuo um usuario autenticado na plataforma")
    public void quePossuoUmUsuarioAutenticadoNaPlataforma() {
        quePossuoAsCredenciaisDeUmNovoUsuario();
        envioUmaRequisicaoParaCadastrarOUsuario();
        solicitoAGeracaoDoTokenDeAcesso();
    }

    @And("identifico o primeiro livro disponivel no catalogo")
    public void identificoOPrimeiroLivroDisponivelNoCatalogo() {
        apiResponse = bookStoreClient.listarLivros();
        Assert.assertEquals(200, apiResponse.getStatusCode());
        bookIsbn = apiResponse.jsonPath().getString("books[0].isbn");
        Assert.assertNotNull(bookIsbn);
    }

    @When("envio uma requisicao para alugar este livro")
    public void envioUmaRequisicaoParaAlugarEsteLivro() {
        apiResponse = bookStoreClient.alugarLivro(userId, bookIsbn, accessToken);
        Assert.assertEquals(201, apiResponse.getStatusCode());
    }

    @And("consulto o perfil do usuario")
    public void consultoOPerfilDoUsuario() {
        apiResponse = bookStoreClient.consultarPerfil(userId, accessToken);
        Assert.assertEquals(200, apiResponse.getStatusCode());
    }

    @Then("o livro alugado deve estar registrado com sucesso no perfil")
    public void oLivroAlugadoDeveEstarRegistradoComSucessoNoPerfil() {
        String isbnNoPerfil = apiResponse.jsonPath().getString("books[0].isbn");
        Assert.assertEquals(bookIsbn, isbnNoPerfil);
    }
}