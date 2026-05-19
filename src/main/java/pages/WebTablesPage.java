package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WebTablesPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By btnAdd = By.id("addNewRecordButton");
    private final By inputFirstName = By.id("firstName");
    private final By inputLastName = By.id("lastName");
    private final By inputEmail = By.id("userEmail");
    private final By inputAge = By.id("age");
    private final By inputSalary = By.id("salary");
    private final By inputDepartment = By.id("department");
    private final By btnSubmit = By.id("submit");
    private final By inputSearch = By.id("searchBox");
    private final By modalContent = By.cssSelector(".modal-content");

    public WebTablesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        removerAnuncios();
    }

    public void adicionarRegistro(String fName, String lName, String email, String age, String salary, String dep) {
        clickLocator(btnAdd);
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(modalContent));
        pause(300);
        
        preencher(inputFirstName, fName);
        preencher(inputLastName, lName);
        preencher(inputEmail, email);
        preencher(inputAge, age);
        preencher(inputSalary, salary);
        preencher(inputDepartment, dep);
        
        pause(300);
        
        WebElement submitBtn = this.wait.until(ExpectedConditions.elementToBeClickable(btnSubmit));
        clickElement(submitBtn);
        
        this.wait.until(ExpectedConditions.invisibilityOfElementLocated(modalContent));
        System.out.println("✓ Registro '" + fName + "' adicionado com sucesso");
        pause(300);
    }

    public void buscarPor(String texto) {
        preencher(inputSearch, texto);
        pause(400);
    }

    public void limparBusca() {
        try {
            WebElement search = this.wait.until(ExpectedConditions.visibilityOfElementLocated(inputSearch));
            ((JavascriptExecutor) this.driver).executeScript(
                "var setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                "setter.call(arguments[0], '');" +
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", 
                search
            );
            pause(400);
        } catch (Exception ignored) {}
    }

    public boolean isRegistroVisivel(String firstName) {
        buscarPor(firstName);
        By cellComNome = By.xpath("//table//tbody//tr//td[contains(text(), '" + firstName + "')]");
        boolean isDisplayed = false;
        
        try {
            WebElement elemento = this.wait.until(ExpectedConditions.visibilityOfElementLocated(cellComNome));
            System.out.println("✓ Registro '" + firstName + "' encontrado na tabela!");
            isDisplayed = elemento.isDisplayed();
        } catch (Exception e) {
            System.err.println("✗ Registro '" + firstName + "' NÃO encontrado");
        } finally {
            limparBusca();
        }
        
        return isDisplayed;
    }

    public void editarDepartamento(String firstName, String novoDepartamento) {
        buscarPor(firstName);
        try {
            By btnEdit = By.xpath("//table//tbody//tr[.//td[normalize-space()='" + firstName + "']]/td[last()]//span[contains(@id, 'edit') or @title='Edit']");
            try {
                clickLocator(btnEdit);
            } catch (Exception e1) {
                By btnEdit2 = By.xpath("(//table//tbody//tr[.//td[normalize-space()='" + firstName + "']]/td[last()]//span)[1]");
                clickLocator(btnEdit2);
            }
            
            this.wait.until(ExpectedConditions.visibilityOfElementLocated(modalContent));
            preencher(inputDepartment, novoDepartamento);
            
            WebElement submitBtn = this.wait.until(ExpectedConditions.elementToBeClickable(btnSubmit));
            clickElement(submitBtn);
            
            this.wait.until(ExpectedConditions.invisibilityOfElementLocated(modalContent));
            pause(400);
        } catch (Exception e) {
            throw new RuntimeException("Erro crítico ao tentar editar o registro '" + firstName + "': " + e.getMessage());
        } finally {
            limparBusca();
        }
    }

    public boolean verificarDepartamentoAtualizado(String firstName, String departamentoEsperado) {
        buscarPor(firstName);
        By cellComDep = By.xpath("//table//tbody//tr[.//td[contains(text(), '" + firstName + "')]]//td[contains(text(), '" + departamentoEsperado + "')]");
        boolean isDisplayed;
        
        try {
            isDisplayed = this.wait.until(ExpectedConditions.visibilityOfElementLocated(cellComDep)).isDisplayed();
        } catch (Exception e) {
            isDisplayed = false;
        } finally {
            limparBusca();
        }
        
        return isDisplayed;
    }

    public void deletarRegistro(String firstName) {
        buscarPor(firstName);
        try {
            By btnDelete = By.xpath("//table//tbody//tr[.//td[normalize-space()='" + firstName + "']]/td[last()]//span[contains(@id, 'delete') or @title='Delete']");
            try {
                clickLocator(btnDelete);
            } catch (Exception e1) {
                By btnDelete2 = By.xpath("(//table//tbody//tr[.//td[normalize-space()='" + firstName + "']]/td[last()]//span)[2]");
                clickLocator(btnDelete2);
            }
            pause(400);
        } catch (Exception e) {
            throw new RuntimeException("Erro crítico: Não foi possível deletar o colaborador '" + firstName + "'. Erro: " + e.getMessage());
        } finally {
            limparBusca();
        }
    }

    public void configurarPaginaParaExclusaoLote() {
        try {
            WebElement selectMenu = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".pagination-bottom select, select")));
            ((JavascriptExecutor) this.driver).executeScript(
                "arguments[0].value = '50';" +
                "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", 
                selectMenu
            );
            pause(500);
        } catch (Exception ignored) {}
    }

    public void deletarRegistroSemFiltrar(String firstName) {
        try {
            pause(200);
            By btnDelete = By.xpath("//table//tbody//tr[.//td[normalize-space()='" + firstName + "']]/td[last()]//span[contains(@id, 'delete') or @title='Delete']");
            WebElement element = this.wait.until(ExpectedConditions.elementToBeClickable(btnDelete));
            ((JavascriptExecutor) this.driver).executeScript("arguments[0].click();", element);
            pause(200);
        } catch (Exception e) {
            String registroExiste = (String) ((JavascriptExecutor) this.driver).executeScript(
                "const existe = Array.from(document.querySelectorAll('table tbody td')).some(td => td.textContent.trim() === '" + firstName + "'); " +
                "return existe ? 'SIM' : 'NAO';"
            );
            
            if ("NAO".equals(registroExiste)) {
                System.out.println("  Nota: Registro '" + firstName + "' não encontrado (já deletado?)");
                return;
            }
            
            try {
                By btnDelete2 = By.xpath("(//table//tbody//tr[.//td[normalize-space()='" + firstName + "']]/td[last()]//span)[2]");
                WebElement element2 = this.wait.until(ExpectedConditions.elementToBeClickable(btnDelete2));
                ((JavascriptExecutor) this.driver).executeScript("arguments[0].click();", element2);
                pause(200);
            } catch (Exception e2) {
                throw new RuntimeException("Erro ao deletar '" + firstName + "': " + e.getMessage());
            }
        }
    }

    private void preencher(By locator, String texto) {
        WebElement campo = this.wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        ((JavascriptExecutor) this.driver).executeScript(
            "var setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
            "setter.call(arguments[0], arguments[1]);" +
            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", 
            campo, texto
        );
    }

    private void clickLocator(By locator) {
        WebElement elemento = this.wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        clickElement(elemento);
    }

    private void clickElement(WebElement element) {
        removerAnuncios();
        ((JavascriptExecutor) this.driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        try {
            element.click();
        } catch (Exception e) {
            ((JavascriptExecutor) this.driver).executeScript("arguments[0].click();", element);
        }
    }

    private void removerAnuncios() {
        try {
            ((JavascriptExecutor) this.driver).executeScript("document.querySelectorAll('iframe, footer, header, [id^=\"google_ads\"], [id^=\"adplus\"], #fixedban').forEach(e => e.style.display = 'none');");
        } catch (Exception ignored) {}
    }

    private void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }
}