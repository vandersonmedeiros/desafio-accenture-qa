package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.time.Duration;

public class PracticeFormPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By firstNameInput = By.id("firstName");
    private final By lastNameInput = By.id("lastName");
    private final By genderMaleRadio = By.xpath("//label[@for='gender-radio-1']");
    private final By userNumberInput = By.id("userNumber");
    private final By uploadPictureInput = By.id("uploadPicture");
    private final By submitButton = By.id("submit");
    private final By successModal = By.className("modal-content");

    public PracticeFormPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navegar() {
        driver.get("https://demoqa.com/automation-practice-form");
    }

    public void preencherNomeCompleto(String firstName, String lastName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput)).sendKeys(firstName);
        driver.findElement(lastNameInput).sendKeys(lastName);
    }

    public void selecionarGeneroEMovel(String telefone) {
        WebElement radio = wait.until(ExpectedConditions.elementToBeClickable(genderMaleRadio));
        try {
            radio.click();
        } catch (Exception e) {
            org.openqa.selenium.JavascriptExecutor executor = (org.openqa.selenium.JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", radio);
        }
        driver.findElement(userNumberInput).sendKeys(telefone);
    }

    public void fazerUploadArquivoTexto() {
        File arquivo = new File("src/test/resources/arquivo_dummy.txt");
        if (!arquivo.exists()) {
            try {
                arquivo.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        driver.findElement(uploadPictureInput).sendKeys(arquivo.getAbsolutePath());
    }

    public void enviarFormulario() {
        WebElement botao = driver.findElement(submitButton);
        driver.switchTo().defaultContent();
        try {
            botao.click();
        } catch (Exception e) {
            org.openqa.selenium.JavascriptExecutor executor = (org.openqa.selenium.JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", botao);
        }
    }

    public boolean modalSucessoExibido() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successModal)).isDisplayed();
    }
}
