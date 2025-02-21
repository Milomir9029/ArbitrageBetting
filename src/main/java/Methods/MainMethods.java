package Methods;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MainMethods {
    private WebDriver driver;

    public MainMethods(WebDriver driver) {
        this.driver = driver;
    }

    public void clickOn(By locator) {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(locator)).click();
    }


    public void scrollTo(By locator) {
        WebElement element = driver.findElement(locator);
        Actions actions = new Actions(driver);
        Dimension screenSize = driver.manage().window().getSize();
        int scrollDistance = screenSize.getHeight() / 3;
        for (int i = 0; i < scrollDistance; i++) {
            actions.sendKeys(Keys.PAGE_DOWN).perform();
        }
    }

    public String getElementText(By locator) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
        String text = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerText;", element);
        return text;
    }

    public void waitUntilInvisible(By locator) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[text()='Prihvatam']")));
    }

    private PropertyFile property = new PropertyFile();

    public void switchTabs(int tabnum) {
        java.util.Set<String> handles = driver.getWindowHandles();
        List<String> handleList = new ArrayList<>(handles);
        driver.switchTo().window(handleList.get(handleList.size() - tabnum));
    }

    public String[] separateTeams(String Match, String Separator) {
        return Match.split(Separator);
    }

    static boolean isElementPresentByClass(WebDriver driver, String className) {
        return !driver.findElements(By.className(className)).isEmpty();


    }
}
