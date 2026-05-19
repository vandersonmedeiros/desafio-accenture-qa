package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SortablePage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions actions;

    private final By listItems = By.cssSelector("#demo-tabpane-list .list-group-item");

    public SortablePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);
        removerAnuncios();
    }

    public void moverUltimoParaOPrimeiro() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(listItems));
        List<WebElement> itens = driver.findElements(listItems);

        if (itens.size() >= 2) {
            WebElement primeiroItem = itens.get(0);
            WebElement ultimoItem = itens.get(itens.size() - 1);

            executeJavaScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", ultimoItem);
            pause(300);

            actions.clickAndHold(ultimoItem)
                    .moveToElement(primeiroItem)
                    .moveByOffset(0, -5)
                    .release()
                    .build()
                    .perform();

            pause(500);
        }
    }

    public String obterTextoDoPrimeiroElemento() {
        List<WebElement> itens = driver.findElements(listItems);
        return itens.get(0).getText();
    }

    private void removerAnuncios() {
        executeJavaScript(
                "document.querySelectorAll('iframe, footer, header, [id^=\"google_ads\"], [id^=\"adplus\"], #fixedban').forEach(e => e.style.display = 'none');");
    }

    private void executeJavaScript(String script) {
        try {
            ((JavascriptExecutor) driver).executeScript(script);
        } catch (Exception ignored) {
        }
    }

    private void executeJavaScript(String script, WebElement element) {
        ((JavascriptExecutor) driver).executeScript(script, element);
    }

    private void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}