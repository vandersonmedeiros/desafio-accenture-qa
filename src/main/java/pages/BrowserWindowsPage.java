package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Set;

public class BrowserWindowsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private String mainWindowHandle;

    private final By windowButton = By.id("windowButton");
    private final By sampleHeading = By.id("sampleHeading");

    public BrowserWindowsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void abrirNovaJanela() {
        mainWindowHandle = driver.getWindowHandle();
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(windowButton));

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", button);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(button)).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }
    }

    public String obterTextoDaNovaJanela() {
        Set<String> allWindowHandles = driver.getWindowHandles();
        for (String windowHandle : allWindowHandles) {
            if (!windowHandle.equals(mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        return wait.until(ExpectedConditions.visibilityOfElementLocated(sampleHeading)).getText();
    }

    public void fecharNovaJanelaERetornar() {
        driver.close();
        driver.switchTo().window(mainWindowHandle);
    }
}