package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.BrowserWindowsPage;
import pages.HomePage;
import pages.PracticeFormPage;
import java.util.UUID;
import java.util.Random;

public class UiSteps {

    private WebDriver driver;
    private HomePage homePage;
    private PracticeFormPage practiceFormPage;
    private BrowserWindowsPage browserWindowsPage;

    @Before("@ui")
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        driver = new ChromeDriver(chromeOptions);
    }

    @After("@ui")
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("que acesso o site DemoQA na pagina inicial")
    public void queAcessoOSiteDemoQANaPaginaInicial() {
        homePage = new HomePage(driver);
        homePage.acessarHomePage();
    }

    @When("escolho a opcao {string} na pagina inicial")
    public void escolhoAOpcaoNaPaginaInicial(String nomeMenu) {
        homePage.escolherOpcaoMenu(nomeMenu);
    }

    @And("clico no submenu {string}")
    public void clicoNoSubmenu(String nomeSubMenu) {
        homePage.escolherSubMenu(nomeSubMenu);
    }

    @And("preencho todo o formulario com valores aleatorios")
    public void preenchoTodoOFormularioComValoresAleatorios() {
        practiceFormPage = new PracticeFormPage(driver);
        String randomName = "QA_" + UUID.randomUUID().toString().substring(0, 5);
        String randomLastName = "Test_" + UUID.randomUUID().toString().substring(0, 5);
        String randomPhone = String.format("%010d", new Random().nextInt(1000000000) + 1000000000L).substring(0, 10);

        practiceFormPage.preencherNomeCompleto(randomName, randomLastName);
        practiceFormPage.selecionarGeneroEMovel(randomPhone);
    }

    @And("realizo o upload do arquivo de texto obrigatorio")
    public void realizoOUploadDoArquivoDeTextoObrigatorio() {
        practiceFormPage.fazerUploadArquivoTexto();
    }

    @And("clico no botao de enviar o formulario")
    public void submtoOFormulario() {
        practiceFormPage.enviarFormulario();
    }

    @Then("garantir que um popup foi aberto apos o submit")
    public void garantirQueUmPopupFoiAbertoAposOSubmit() {
        Assert.assertTrue(practiceFormPage.modalSucessoExibido());
    }

    @And("fechar o popup")
    public void fecharOPopup() {
        practiceFormPage.fecharPopupModal();
    }

    @And("clico no botao new Window")
    public void clicoNoBotaoNewWindow() {
        browserWindowsPage = new BrowserWindowsPage(driver);
        browserWindowsPage.abrirNovaJanela();
    }

    @Then("certifico que uma nova janela foi aberta com a mensagem {string}")
    public void certificoQueUmaNovaJanelaFoiAbertaComAMensagem(String expectedMessage) {
        Assert.assertEquals(expectedMessage, browserWindowsPage.obterTextoDaNovaJanela());
    }

    @And("fecho a nova janela aberta")
    public void fechoANovaJanelaAberta() {
        browserWindowsPage.fecharNovaJanelaERetornar();
    }
}