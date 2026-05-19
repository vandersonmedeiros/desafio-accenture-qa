package stepdefinitions;

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

    @Given("que possuo os dados de um novo usuario")
    public void quePossuoAsCredenciaisDeUmNovoUsuario() {
        userName = "user_" + UUID.randomUUID().toString().substring(0, 8);
        userPassword = "Password123!";
    }

    @When("solicito o cadastro na plataforma")
    public void envioUmaRequisicaoParaCadastrarOUsuario() {
        apiResponse = bookStoreClient.criarUsuario(userName, userPassword);
        Assert.assertEquals("Falha ao criar usuário na API", 201, apiResponse.getStatusCode());

        userId = apiResponse.jsonPath().getString("userID");
        Assert.assertNotNull("O ID do usuário retornado não deveria ser nulo", userId);
    }

    @And("realizo a autenticacao da conta")
    public void solicitoAGeracaoDoTokenDeAcesso() {
        apiResponse = bookStoreClient.gerarToken(userName, userPassword);
        Assert.assertEquals("Falha ao gerar token de acesso", 200, apiResponse.getStatusCode());

        accessToken = apiResponse.jsonPath().getString("token");
        Assert.assertNotNull("O token de acesso retornado não deveria ser nulo", accessToken);
    }

    @And("verifico a autorizacao de acesso")
    public void validoAAutorizacaoDasCredenciaisNaAPI() {
        apiResponse = bookStoreClient.validarAutorizacao(userName, userPassword);
        Assert.assertEquals("Falha na validação de autorização", 200, apiResponse.getStatusCode());
    }

    @Then("o usuario deve ser autorizado com sucesso")
    public void oUsuarioDeveSerAutenticadoEAutorizadoComSucesso() {
        Assert.assertEquals("A API não autorizou o usuário corretamente", "true", apiResponse.getBody().asString());
    }

    @Given("que o sistema de livros esta operacional")
    public void queAApiDeLivrosEstaOperacional() {
        Assert.assertNotNull("O cliente da API de livros falhou na inicialização", bookStoreClient);
    }

    @When("consulto a lista de livros disponiveis")
    public void envioUmaRequisicaoParaListarOsLivros() {
        apiResponse = bookStoreClient.listarLivros();
    }

    @Then("o catalogo deve retornar os titulos cadastrados")
    public void oCatalogoDeveRetornarAListaDeLivrosCadastrados() {
        Assert.assertEquals("Falha ao obter catálogo de livros", 200, apiResponse.getStatusCode());
        Assert.assertNotNull("A lista de livros (books) não foi encontrada no JSON",
                apiResponse.jsonPath().getList("books"));
    }

    @Given("que sou um usuario autenticado na plataforma")
    public void quePossuoUmUsuarioAutenticadoNaPlataforma() {
        quePossuoAsCredenciaisDeUmNovoUsuario();
        envioUmaRequisicaoParaCadastrarOUsuario();
        solicitoAGeracaoDoTokenDeAcesso();
    }

    @And("seleciono o primeiro livro disponivel no catalogo")
    public void identificoOPrimeiroLivroDisponivelNoCatalogo() {
        apiResponse = bookStoreClient.listarLivros();
        Assert.assertEquals("Falha ao buscar livros para identificação", 200, apiResponse.getStatusCode());

        bookIsbn = apiResponse.jsonPath().getString("books[0].isbn");
        Assert.assertNotNull("Nenhum ISBN foi retornado no catálogo de livros", bookIsbn);
    }

    @When("realizo o aluguel do livro selecionado")
    public void envioUmaRequisicaoParaAlugarEsteLivro() {
        apiResponse = bookStoreClient.alugarLivro(userId, bookIsbn, accessToken);
        Assert.assertEquals("Falha ao processar o aluguel do livro", 201, apiResponse.getStatusCode());
    }

    @Then("o livro alugado deve constar no perfil do usuario")
    public void oLivroAlugadoDeveEstarRegistradoComSucessoNoPerfil() {
        apiResponse = bookStoreClient.consultarPerfil(userId, accessToken);
        Assert.assertEquals("Falha ao consultar os dados do perfil do usuário", 200, apiResponse.getStatusCode());

        String isbnNoPerfil = apiResponse.jsonPath().getString("books[0].isbn");
        Assert.assertEquals("O ISBN registrado no perfil não corresponde ao livro alugado", bookIsbn, isbnNoPerfil);
    }
}