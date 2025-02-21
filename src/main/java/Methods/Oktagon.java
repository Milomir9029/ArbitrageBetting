package Methods;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;

public class Oktagon {
    public static MainMethods mainMethods;
    private static WebDriver driver;

    public Oktagon(WebDriver driver) {
        Oktagon.driver = driver;
    }

    public void open(WebDriver Driver) {
        driver = Driver;
        mainMethods = new MainMethods(driver);
        driver.get("https://www.oktagonbet.com/ibet-web-client/#/home/leaguesWithMatches");
    }

    static String getXpath(String result) {
        return String.format("//span[contains(text(),'%s')]/parent::div/following-sibling::odd//span[2]", result);
    }

    public void openLeagueList() {
        mainMethods.clickOn(By.className("sport-S"));
    }

    public void toggleLeague(int x) {
        try {
            Thread.sleep(1000);
            mainMethods.clickOn(By.xpath
                    ("//*[@id=\"top-view\"]/div[1]/div[1]/div/div[4]/div/div[3]/div[1]/div[2]/div[" + x + "]"));
            Thread.sleep(1000);
        }catch (TimeoutException | InterruptedException e){
            scrollDownByDistance(200);
            mainMethods.clickOn(By.xpath
                    ("//*[@id=\"top-view\"]/div[1]/div[1]/div/div[4]/div/div[3]/div[1]/div[2]/div[" + x + "]"));
        }
    }


    static float findOdd(String result) {
        float odd;
        try {
            odd = Float.parseFloat(mainMethods.getElementText(By.xpath((getXpath(result)))));
            System.out.println(odd);
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException | NumberFormatException e) {
            if (e instanceof StaleElementReferenceException) {
                System.out.println("Element is stale. Refinding...");
                odd = Float.parseFloat(mainMethods.getElementText(By.xpath((getXpath(result)))));
                System.out.println(odd);
            } else {
                odd = 0;
                System.out.println(odd);
            }

        }
        return odd;
    }

    public ArrayList<FootballMatch> getLeagueMatches() {
        int i = 1;
        ArrayList<FootballMatch> MatchList = new ArrayList<>();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        while (true) {
            try {
                mainMethods.clickOn(By.cssSelector(".special-button"));
                MatchList.add(getOdds());
                System.out.println(MatchList.get(i - 1));
                mainMethods.clickOn(By.cssSelector(".back-button-pages"));
                for (int y = 1; y <= i; y++) {
                    WebElement element = new WebDriverWait(driver, Duration.ofSeconds(5))
                            .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".special-button")));
                    js.executeScript("arguments[0].remove();", element);
                }
                i++;
            } catch (TimeoutException e) {
                //System.out.println(MatchList.size());
                break;
            }
        }
        return MatchList;

    }


    public static FootballMatch getOdds() {
        String[] MatchName = mainMethods.separateTeams(mainMethods.getElementText(By.xpath(
                "//*[@id=\"home-container\"]/div[1]/div/div/div/div/div[3]/div[2]/div/div[1]/div[1]")), " - ");
        String HomeName = MatchName[0];
        String AwayName = MatchName[1];
        String LeagueName = mainMethods.getElementText(By.cssSelector(".special-details"));
        String StartTime = mainMethods.getElementText(By.cssSelector(".special-details span"));
        float K1 = findOdd("1");
        float KX = findOdd("X");
        float K2 = findOdd("2");
        float Ug02 = findOdd("UG0-2");
        float Ug3p = findOdd("UG3+");
        float K1X = findOdd("1X");
        float KX2 = findOdd("X2");
        mainMethods.clickOn(By.xpath("//div[.=' GOL - GOL']"));
        float GG = findOdd("GG");
        float NG = findOdd("NG");
        return new FootballMatch(K1, KX, K2, Ug02, Ug3p, K1X, KX2, GG, NG,
                HomeName, AwayName, StartTime, LeagueName);
    }

    public void nextPage() {
        mainMethods.clickOn(By.cssSelector(".remaining-num-of-matches-item"));
    }

    public void scrollToBottom() {
        WebElement homeContainer = driver.findElement(By.id("leagues-container"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollTop = arguments[0].scrollHeight", homeContainer);
    }

    public void scrollDownByDistance(int distance) {
        WebElement homeContainer = driver.findElement(By.className("content-left"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollTop += arguments[1]", homeContainer, distance);
    }

    public void closePopup() {
        mainMethods.clickOn(By.xpath("//div[text()='Prihvatam']"));
        mainMethods.waitUntilInvisible(By.xpath("//div[text()='Prihvatam']"));

    }

    public void loadAllMatches() {
        int test = 5;
        scrollToBottom();
        while (true) {
            try {
                nextPage();
            } catch (TimeoutException | ElementClickInterceptedException e) {
                if (e instanceof ElementClickInterceptedException) {
                    closePopup();
                } else {
                    break;
                }
            }
        }
    }

    public ArrayList<FootballMatch> getAllMatches() {
        ArrayList<FootballMatch> MatchList = new ArrayList<>();
        int i = 1;
        closePopup();
        openLeagueList();
        toggleLeague(i);
        while (true) {
            try {
                //loadAllMatches();
                MatchList.addAll(getLeagueMatches());
                toggleLeague(i + 1);
                toggleLeague(i);
                i++;
            } catch (TimeoutException e) {

                System.out.println(MatchList.size());
                break;
            }
        }
        return MatchList;
    }
}