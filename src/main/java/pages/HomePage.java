package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void acessarHomePage() {
        driver.get("https://demoqa.com/");
    }

    public void escolherOpcaoMenu(String nomeMenu) {
        By menuCard = By.xpath("//h5[text()='" + nomeMenu + "']/ancestor::div[contains(@class, 'top-card')]");
        WebElement card = wait.until(ExpectedConditions.elementToBeClickable(menuCard));

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", card);

        try {
            card.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
        }
    }

    public void escolherSubMenu(String nomeSubMenu) {
        By submenu = By.xpath("//span[text()='" + nomeSubMenu + "']");

        WebElement item = wait.until(ExpectedConditions.visibilityOfElementLocated(submenu));

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", item);

        try {
            item.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", item);
        }
    }
}