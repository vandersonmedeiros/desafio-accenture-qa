package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProgressBarPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By btnStartStop = By.id("startStopButton");
    private final By progressBar = By.cssSelector(".progress-bar");
    private final By btnReset = By.id("resetButton");

    public ProgressBarPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        removerAnuncios();
    }

    public void clicarStartStop() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(btnStartStop));
        clickElement(button);
    }

    public int getValorProgresso() {
        WebElement pb = wait.until(ExpectedConditions.presenceOfElementLocated(progressBar));
        return Integer.parseInt(pb.getAttribute("aria-valuenow"));
    }

    public void pararAntesDos(int limite) {
        while (true) {
            if (getValorProgresso() >= (limite - 2)) {
                clicarStartStop();
                break;
            }
        }
    }

    public void aguardarChegarEm100() {
        wait.until(ExpectedConditions.attributeToBe(progressBar, "aria-valuenow", "100"));
    }

    public void resetarProgressBar() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(btnReset));
        clickElement(button);
    }

    private void clickElement(WebElement element) {
        removerAnuncios();
        executeJavaScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        try {
            element.click();
        } catch (Exception e) {
            executeJavaScript("arguments[0].click();", element);
        }
    }

    private void executeJavaScript(String script, WebElement element) {
        ((JavascriptExecutor) driver).executeScript(script, element);
    }

    private void executeJavaScript(String script) {
        ((JavascriptExecutor) driver).executeScript(script);
    }

    private void removerAnuncios() {
        try {
            executeJavaScript(
                    "document.querySelectorAll('iframe, footer, header, [id^=\"google_ads\"], [id^=\"adplus\"], #fixedban').forEach(e => e.style.display = 'none');");
        } catch (Exception ignored) {
        }
    }
}