package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.BrowserWindowsPage;
import pages.HomePage;
import pages.PracticeFormPage;
import pages.ProgressBarPage;
import pages.SortablePage;
import pages.WebTablesPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class UiSteps {

    private WebDriver driver;
    private HomePage homePage;
    private PracticeFormPage practiceFormPage;
    private BrowserWindowsPage browserWindowsPage;
    private WebTablesPage webTablesPage;
    private ProgressBarPage progressBarPage;
    private SortablePage sortablePage;
    private final List<String> registrosDinamicosCriados = new ArrayList<>();

    @Before("@ui")
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(
                "--headless",
                "--window-size=1920,1080",
                "--no-sandbox",
                "--disable-dev-shm-usage"
        );
        this.driver = new ChromeDriver(chromeOptions);

        this.homePage = new HomePage(this.driver);
        this.practiceFormPage = new PracticeFormPage(this.driver);
        this.browserWindowsPage = new BrowserWindowsPage(this.driver);
        this.webTablesPage = new WebTablesPage(this.driver);
        this.progressBarPage = new ProgressBarPage(this.driver);
        this.sortablePage = new SortablePage(this.driver);
    }

    @After("@ui")
    public void tearDown() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }

    @Given("que acesso o site DemoQA na pagina inicial")
    public void queAcessoOSiteDemoQANaPaginaInicial() {
        this.homePage.acessarHomePage();
    }

    @Given("que estou na tela de {string} dentro de {string}")
    public void queEstouNaTelaDeDentroDe(String subMenu, String menu) {
        this.homePage.acessarHomePage();
        this.homePage.escolherOpcaoMenu(menu);
        this.homePage.escolherSubMenu(subMenu);
    }

    @When("escolho a opcao {string} na pagina inicial")
    public void escolhoAOpcaoNaPaginaInicial(String nomeMenu) {
        this.homePage.escolherOpcaoMenu(nomeMenu);
    }

    @And("clico no submenu {string}")
    public void clicoNoSubmenu(String nomeSubMenu) {
        this.homePage.escolherSubMenu(nomeSubMenu);
    }

    @And("submeto o formulario com dados validos e o anexo obrigatorio")
    public void submetoOFormularioComDadosValidosEOAnexoObrigatorio() {
        String randomName = "QA_" + UUID.randomUUID().toString().substring(0, 5);
        String randomLastName = "Test_" + UUID.randomUUID().toString().substring(0, 5);
        String randomPhone = String.format("%010d", new Random().nextInt(1000000000) + 1000000000L).substring(0, 10);

        this.practiceFormPage.preencherNomeCompleto(randomName, randomLastName);
        this.practiceFormPage.selecionarGeneroEMovel(randomPhone);
        this.practiceFormPage.fazerUploadArquivoTexto();
        this.practiceFormPage.enviarFormulario();
    }

    @Then("o sistema deve confirmar o recebimento das informacoes")
    public void oSistemaDeveConfirmarORecebimentoDasInformacoes() {
        Assert.assertTrue("O modal de sucesso não foi exibido após o envio", this.practiceFormPage.modalSucessoExibido());
        this.practiceFormPage.fecharPopupModal();
    }

    @And("aciono a abertura de uma nova janela")
    public void acionoAAberturaDeUmaNovaJanela() {
        this.browserWindowsPage.abrirNovaJanela();
    }

    @Then("a nova janela deve exibir a mensagem {string}")
    public void aNovaJanelaDeveExibirAMensagem(String expectedMessage) {
        Assert.assertEquals("O texto da nova janela nao corresponde ao esperado", expectedMessage, this.browserWindowsPage.obterTextoDaNovaJanela());
        this.browserWindowsPage.fecharNovaJanelaERetornar();
    }

    @When("cadastro um novo colaborador com os dados {string}, {string}, {string}, {string}, {string}, {string}")
    public void crioUmNovoRegistroComOsDados(String fName, String lName, String email, String age, String salary, String dep) {
        this.webTablesPage.adicionarRegistro(fName, lName, email, age, salary, dep);
    }

    @Then("o colaborador {string} deve constar na listagem")
    public void oRegistroDeDeveSerExibidoNaTabela(String fName) {
        Assert.assertTrue("O registro deveria estar visível na tabela", this.webTablesPage.isRegistroVisivel(fName));
    }

    @When("altero o departamento do colaborador {string} para {string}")
    public void editoORegistroDeAlterandoODepartamentoPara(String fName, String novoDep) {
        this.webTablesPage.editarDepartamento(fName, novoDep);
    }

    @Then("o colaborador {string} deve exibir o novo departamento {string}")
    public void oRegistroDeDeveExibirODepartamento(String fName, String depEsperado) {
        Assert.assertTrue("O departamento não foi updated corretamente", this.webTablesPage.verificarDepartamentoAtualizado(fName, depEsperado));
    }

    @When("removo o colaborador {string}")
    public void deletoORegistroDe(String fName) {
        this.webTablesPage.deletarRegistro(fName);
    }

    @Then("o colaborador {string} nao deve ser exibido na listagem")
    public void oRegistroDeNaoDeveMaisConstarNaTabela(String fName) {
        Assert.assertFalse("O registro ainda está na tabela após ser deletado", this.webTablesPage.isRegistroVisivel(fName));
    }

    @When("realizo o cadastro massivo dos seguintes colaboradores:")
    public void crioOsSeguintesRegistrosDinamica(DataTable dataTable) {
        List<Map<String, String>> registros = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> linha : registros) {
            String fName = linha.get("firstName");
            this.webTablesPage.adicionarRegistro(
                    fName,
                    linha.get("lastName"),
                    linha.get("email"),
                    linha.get("age"),
                    linha.get("salary"),
                    linha.get("department")
            );
            this.registrosDinamicosCriados.add(fName);
        }
    }

    @Then("removo todos os colaboradores cadastrados massivamente")
    public void deletoTodosOsRegistrosCriadosDinamicamente() {
        this.webTablesPage.configurarPaginaParaExclusaoLote();
        for (String nome : this.registrosDinamicosCriados) {
            this.webTablesPage.deletarRegistroSemFiltrar(nome);
        }
    }

    @And("a listagem deve retornar ao estado inicial")
    public void aTabelaDeveRetornarAoSeuEstadoOriginal() {
        for (String nomeDeletado : this.registrosDinamicosCriados) {
            Assert.assertFalse("O registro dinâmico " + nomeDeletado + " não foi apagado", this.webTablesPage.isRegistroVisivel(nomeDeletado));
        }
        this.registrosDinamicosCriados.clear();
    }

    @Then("o valor da barra deve iniciar em 0%")
    public void oValorDaBarraDeveIniciarEm0() {
        int valorAtual = this.progressBarPage.getValorProgresso();
        Assert.assertEquals("A barra não iniciou em 0%", 0, valorAtual);
    }

    @When("clico no botao Start para iniciar a barra")
    public void clicoNoBotaoStartParaIniciarABarra() {
        this.progressBarPage.clicarStartStop();
    }

    @And("pauso a barra de progresso antes dos 25%")
    public void pausoABarraDeProgressoAntesDos25() {
        this.progressBarPage.pararAntesDos(25);
    }

    @Then("o valor da barra deve ser menor ou igual a 25%")
    public void oValorDaBarraDeveSerMenorOuIgualA25() {
        int valorAtual = this.progressBarPage.getValorProgresso();
        Assert.assertTrue("O valor da barra ultrapassou 25%! Valor atual: " + valorAtual, valorAtual <= 25);
    }

    @And("aguardo a barra de progresso chegar a 100%")
    public void aguardoABarraDeProgressoChegarA100() {
        this.progressBarPage.aguardarChegarEm100();
    }

    @And("clico no botao para resetar a barra")
    public void clicoNoBotaoParaResetarABarra() {
        this.progressBarPage.resetarProgressBar();
    }

    @Then("o valor da barra deve retornar para 0%")
    public void oValorDaBarraDeveRetornarPara0() {
        int valorAtual = this.progressBarPage.getValorProgresso();
        Assert.assertEquals("A barra não foi resetada para 0%", 0, valorAtual);
    }

    @When("movo o ultimo elemento para a primeira posicao da lista")
    public void movoOUltimoElementoParaAPrimeiraPosicaoDaLista() {
        this.sortablePage.moverUltimoParaOPrimeiro();
    }

    @Then("o elemento deve ser exibido como o primeiro item da lista")
    public void oElementoDeveSerExibidoComoOPrimeiroItemDaLista() {
        Assert.assertEquals("O drag and drop falhou. O elemento 'Six' não está no topo.", "Six", this.sortablePage.obterTextoDoPrimeiroElemento());
    }
}