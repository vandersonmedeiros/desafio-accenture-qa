package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Set;

public class BrowserWindowsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By tabButton = By.id("tabButton");
    private final By sampleHeading = By.id("sampleHeading");

    public BrowserWindowsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navegar() {
        driver.get("https://demoqa.com/browser-windows");
    }

    public void abrirNovaAba() {
        wait.until(ExpectedConditions.elementToBeClickable(tabButton)).click();
    }

    public String obterTextoDaNovaAba() {
        String mainWindowHandle = driver.getWindowHandle();
        Set<String> allWindowHandles = driver.getWindowHandles();
        for (String windowHandle : allWindowHandles) {
            if (!windowHandle.equals(mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        String sampleHeadingText = wait.until(ExpectedConditions.visibilityOfElementLocated(sampleHeading)).getText();
        driver.close();
        driver.switchTo().window(mainWindowHandle);
        return sampleHeadingText;
    }
}
