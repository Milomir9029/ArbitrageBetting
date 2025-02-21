package Methods;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import java.util.ArrayList;
import java.util.List;

public class MaxBet {
    public static MainMethods mainMethods;
    private static WebDriver driver;

    public MaxBet(WebDriver driver) {
        MaxBet.driver = driver;
    }

    public void open(WebDriver Driver) {
        driver = Driver;
        mainMethods = new MainMethods(driver);
        driver.get("https://www.maxbet.rs/leagues/S");
    }

    public void toggleLeague(int x) {
        mainMethods.clickOn(By.xpath("//ds-leagues-list/div[" + x + "]/ds-leagues-accordion/ion-item/ion-label/ion-item/div"));
    }
    public By findmatch(int x, int y){
        return By.xpath("//ds-leagues-list/div[" + x + "]/ds-leagues-accordion/div/ds-es-match-top[" + y + "]/ion-grid/ion-row/ion-row[2]/ion-badge");
    }

    public void openMatch(int x, int y) {
        List<WebElement> elements = driver.findElements(findmatch(x, y));
        if (!driver.findElements(findmatch(x, y)).isEmpty()) {
            mainMethods.clickOn(findmatch(x, y));
        } else {
            toggleLeague(x);
            mainMethods.clickOn(findmatch(x, y));
        }
    }

    public static float findOdd(String Result) {
        float odd = Float.parseFloat(mainMethods.getElementText(By.xpath("//ds-odd/button/span[2][contains(text(), '" + Result + "')]/parent::button//span[1]")));
        System.out.println(odd);
        return odd;
    }

    public static void changeOddView(String OddView) {
        mainMethods.clickOn(By.xpath("//ds-match-special-info/ion-header/div[3]/ion-row/button[" + OddView + "]"));
    }

    public void exitMatch() {
       // mainMethods.clickOn(By.xpath(
            //    "//ds-match-special/ion-content/ion-button"));
        driver.findElement(By.cssSelector(".icon-arrow-left-w")).click();

    }

    public void deleteLeague(int x) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement parentElement = driver.findElement(By.tagName("ds-leagues-list"));
        WebElement firstChild = parentElement.findElement(By.xpath("./*[1]"));
        js.executeScript("arguments[0].remove();", firstChild);
    }

    public ArrayList<FootballMatch> getLeagueMatches(int league) {
        int i = 1;
        ArrayList<FootballMatch> MatchList = new ArrayList<>();
        while (true) {
            try {
            boolean isElementPresent = MainMethods.isElementPresentByClass(driver, "icon-arrow-up");
            if (!isElementPresent) {
                toggleLeague(league);
            }
            openMatch(league, i);
            try {
                MatchList.add(getOdds());
            } catch (Exception u) {
                //Thread.sleep(1000);
                openMatch(league, i);
                MatchList.add(getOdds());
            }
            System.out.println(MatchList.get(i - 1));
            exitMatch();
            i++;
            } catch (TimeoutException | IndexOutOfBoundsException e) {
                System.out.println(MatchList.size() + "breaks here");
                break;
            }
        }
        //toggleLeague(league);
        return MatchList;

    }
    public ArrayList<FootballMatch> getMatches(int league) throws InterruptedException {
        ArrayList<FootballMatch> MatchList = new ArrayList<>();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        toggleLeague(2);
        Thread.sleep(1000);
        WebElement League = driver.findElement(By.xpath("//ds-leagues/ion-content/ds-leagues-list/div["+league+"]"));
        List<WebElement> Matches = League.findElements(By.tagName("ds-es-match-top"));
        int matchNumber = 1;
        for (WebElement element : Matches){
            //openMatch(league, element);
            openMatch(league, matchNumber);
            MatchList.add(getOdds());
            exitMatch();
            System.out.println(MatchList.get(matchNumber - 1));
            matchNumber++;

        }
        return MatchList;
    }

    public ArrayList<FootballMatch> getAllMatches() {
        ArrayList<FootballMatch> MatchList = new ArrayList<>();
        int i = 2;
        while(true){
            try {
                MatchList.addAll(getLeagueMatches(i));
                System.out.println(MatchList.size());
                i++;
                scrollDownByLeague(i);
                Thread.sleep(1000);
                toggleLeague(i);
                Thread.sleep(1000);
            }catch (TimeoutException e){
                break;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return MatchList;
    }

    public FootballMatch getOdds() {
        String[] MatchName = mainMethods.separateTeams(mainMethods.getElementText(By.className("match-special-teams")), " - ");
        String HomeName = MatchName[0];
        String AwayName = MatchName[1];
        String[] Details = mainMethods.separateTeams(mainMethods.getElementText(By.className("match-special-details")), ", ");
        String LeagueName = Details[1];
        String StartTime = Details[0];
        float K1 = findOdd("1");
        float KX = findOdd("X");
        float K2 = findOdd("2");
        float Ug02 = findOdd("ug 0-2");
        float Ug3p = findOdd("ug 3+");
        float GG = findOdd("GG");
        float NG = findOdd("NG");
        //changeOddView("2");
        float K1X = 1;//findOdd("1X");
        float KX2 = 1;//findOdd("X2");
        return new FootballMatch(K1, KX, K2, Ug02, Ug3p, K1X, KX2, GG, NG,
                HomeName, AwayName, StartTime, LeagueName);
    }

    public void scrollDownByLeague(int x) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement scrollableElement = driver.findElement(By.xpath("/html/body/app-root/ng-component/ion-app/div/ds-main-layout/ds-main-layout-desktop/ion-row/ion-row/div[2]"));
        List<WebElement> nestedDivs = scrollableElement.findElements(By.tagName("ds-leagues-accordion"));
        WebElement nestedDiv = nestedDivs.get(x-1);
        js.executeScript("arguments[0].scrollIntoView(true);", nestedDiv);
    }
    public void scrollDownByMatch(int league, int match){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement League = driver.findElement(By.xpath("//ds-leagues/ion-content/ds-leagues-list/div["+league+"]"));
        List<WebElement> Matches = League.findElements(By.tagName("ds-es-match-top"));
        WebElement Match = Matches.get(match-1);
        js.executeScript("arguments[0].scrollIntoView(true);", Match);
    }
    public void scroll(int x){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement scrollableElement = driver.findElement(By.xpath("/html/body/app-root/ng-component/ion-app/div/ds-main-layout/ds-main-layout-desktop/ion-row/ion-row/div[2]"));
        List<WebElement> nestedDivs = scrollableElement.findElements(By.tagName("div"));
        WebElement nestedDiv = nestedDivs.get(x);
        js.executeScript("arguments[0].scrollTop += 500;", nestedDiv);
        js.executeScript("window.scrollBy(0, 500);");
    }
    public void scrollDown(By locator) {
        WebElement homeContainer = driver.findElement(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollTop += arguments[1]", homeContainer, 50000);
    }

    /*private static void scrollAllScrollableDivsBy500(WebElement element) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        // Get all nested div elements
        List<WebElement> nestedDivs = element.findElements(By.tagName("div"));

        for (int i = 0; i < 25; i++) {
            WebElement nestedDiv = nestedDivs.get(i);

            // Check if the nested div can be scrolled
            Boolean canScroll = (Boolean) jsExecutor.executeScript(
                    "return arguments[0].scrollHeight > arguments[0].clientHeight;", nestedDiv);

            // If the nested div can be scrolled, scroll it into view
            if (canScroll) {
                jsExecutor.executeScript("arguments[0].scrollIntoView(true);", nestedDiv);
                System.out.println("Scrolled the scrollable nested div at index " + i + " into view.");
            }

            // Print information about the nested div
            System.out.println("Nested div " + i + ":");
            System.out.println("  ScrollHeight: " + jsExecutor.executeScript("return arguments[0].scrollHeight;", nestedDiv));
            System.out.println("  ClientHeight: " + jsExecutor.executeScript("return arguments[0].clientHeight;", nestedDiv));
            System.out.println("  Can Scroll: " + canScroll);
            System.out.println();
        }
    }*/
    public void scroll2(){
        WebElement scrollableDiv = driver.findElement(By.xpath("/html/body/app-root/ng-component/ion-app/div/ds-main-layout/ds-main-layout-desktop/ion-row/ion-row/div[2]"));

        Actions actions = new Actions(driver);
        actions.moveToElement(scrollableDiv);
        actions.clickAndHold();
        actions.moveByOffset(0, 500); // Adjust the offset as needed
        actions.release();
        actions.perform();
    }
    public void maxbet1() {
        //driver.get("https://www.maxbet.rs/leagues/S");
        //driver.manage().window().setSize(new Dimension(1936, 1056));
        driver.findElement(By.cssSelector("#league_136866 .ion-no-margin .sc-ion-label-ios-h")).click();
        driver.findElement(By.cssSelector("#league_136867 .ion-no-margin .sc-ion-label-ios-h")).click();
        driver.findElement(By.cssSelector("#league_180457 .ion-no-margin .sc-ion-label-ios-h")).click();
        driver.findElement(By.cssSelector("#league_152506 .ion-no-margin .sc-ion-label-ios-h")).click();
        driver.findElement(By.cssSelector("ds-es-match-top:nth-child(19) .es-match-teams-item:nth-child(1)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector("div:nth-child(4) .odd-col:nth-child(7) .odd-btn"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".icon-arrow-left-w")).click();
        driver.findElement(By.cssSelector("#league_152506 .league-accordion--item-inner")).click();
        driver.findElement(By.cssSelector("#league_117827 .ion-no-margin .sc-ion-label-ios-h")).click();
        driver.close();
    }

}
