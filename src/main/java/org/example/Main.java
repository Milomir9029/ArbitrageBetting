package org.example;
import Methods.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

    static WebDriver MaxBetDriver = new ChromeDriver();
    static WebDriver OktagonDriver = new ChromeDriver();
    static MaxBet maxbet = new MaxBet(MaxBetDriver);
    static Oktagon oktagon = new Oktagon(OktagonDriver);

    //static Companies companies = new Companies(OktagonDriver);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

            try {
                executorService.submit(Main::MaxBetMatches);
                executorService.submit(Main::OktagonMatches);
            } finally {
                executorService.shutdown();
            }



    }

    private static ArrayList<FootballMatch> OktagonMatches() {
        oktagon.open(OktagonDriver);
        oktagon.getAllMatches();
        OktagonDriver.quit();
        return oktagon.getAllMatches();
    }
    private static void MaxBetMatches(){
    maxbet.open(MaxBetDriver);
        MaxBetDriver.manage().window().maximize();
        maxbet.toggleLeague(2);
        maxbet.openMatch(2, 1);
        maxbet.exitMatch();
        maxbet.toggleLeague(2);
        maxbet.openMatch(2, 2);
    //maxbet.toggleLeague(2);
        }
}