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
import pages.PracticeFormPage;

public class UiSteps {

    private WebDriver driver;
    private PracticeFormPage practiceFormPage;
    private BrowserWindowsPage browserWindowsPage;

    @Before("@ui")
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--window-size=1920,1080");
        driver = new ChromeDriver(chromeOptions);
    }

    @After("@ui")
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("que navego ate a pagina do formulario de pratica")
    public void queNavegoAteAPaginaDoFormularioDePratica() {
        practiceFormPage = new PracticeFormPage(driver);
        practiceFormPage.navegar();
    }

    @When("preencho os campos de nome {string} e sobrenome {string}")
    public void preenchoOsCamposDeNomeESobrenome(String firstName, String lastName) {
        practiceFormPage.preencherNomeCompleto(firstName, lastName);
    }

    @And("seleciono o genero masculino e insiro o telefone {string}")
    public void selecionoOGeneroMasculinoEInsiroOTelefone(String phoneNumber) {
        practiceFormPage.selecionarGeneroEMovel(phoneNumber);
    }

    @And("realizo o upload do arquivo de texto obrigatorio")
    public void realizoOUploadDoArquivoDeTextoObrigatorio() {
        practiceFormPage.fazerUploadArquivoTexto();
    }

    @And("clico no botao de enviar o formulario")
    public void clicoNoBotaoDeEnviarOFormulario() {
        practiceFormPage.enviarFormulario();
    }

    @Then("o popup de confirmacao deve ser exibido com os dados enviados")
    public void oPopupDeConfirmacaoDeveSerExibidoComOsDadosEnviados() {
        Assert.assertTrue(practiceFormPage.modalSucessoExibido());
    }

    @Given("que navego ate a pagina de janelas do navegador")
    public void queNavegoAteAPaginaDeJanelasDoNavegador() {
        browserWindowsPage = new BrowserWindowsPage(driver);
        browserWindowsPage.navegar();
    }

    @When("clico no botao para abrir uma nova aba")
    public void clicoNoBotaoParaAbrirUmaNovaAba() {
        browserWindowsPage.abrirNovaAba();
    }

    @Then("uma nova aba deve ser aberta com a mensagem {string}")
    public void umaNovaAbaDeveSerAbertaComAMensagem(String expectedMessage) {
        Assert.assertEquals(expectedMessage, browserWindowsPage.obterTextoDaNovaAba());
    }
}