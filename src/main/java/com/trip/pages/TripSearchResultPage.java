package com.trip.pages;

import com.trip.base.TripBase;
import com.trip.util.TripUtil;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class TripSearchResultPage extends TripBase {

    @FindBy(xpath = "//div[@class='splitVw-sctn pull-left']/child::div[2]/child::div")
    List<WebElement> departureFlightsList;

    @FindBy(xpath = "//div[@class='splitVw-sctn pull-right']/child::div[2]/child::div")
    List<WebElement> returnFlightsList;

    @FindBy(xpath = "//span[contains(text(), 'Non Stop')]")
    WebElement nonStop;

    @FindBy(xpath = "//span[contains(text(), '1 Stop')]")
    WebElement oneStop;

    @FindBy(xpath = "//a[contains(text(),'Reset')]")
    WebElement resetFilter;

    @FindBy(xpath = "//div[@class='splitVw-footer-left']//p[@class='actual-price']")
    WebElement depBottomPrice;

    @FindBy(xpath = "//div[@class='splitVw-footer-right']//p[@class='actual-price']")
    WebElement retBottomPrice;

    @FindBy(xpath = "//span[@class='splitVw-total-fare']")
    WebElement totalPrice;

    @FindBy(xpath = "//p[@class='disc-applied']")
    WebElement discount;

    public TripSearchResultPage() {
        PageFactory.initElements(driver, this);
        TripUtil.waitElementToBeClickable(driver, nonStop);
    }

    public int[] getNoOfFlightsNoFilter() {
        TripUtil.scrollToBottomOfPage(driver);
        TripUtil.scrollToTopOfPage(driver);

        int cnt[] = new int[2];

        cnt[0] = departureFlightsList.size();
        cnt[1] = returnFlightsList.size();

        return cnt;
    }

    public int[] getNonStopFlights() {
        resetFilter();
        TripUtil.waitElementToBeClickable(driver, nonStop);

        // click Non Stop checkbox
        nonStop.click();

        return getNoOfFlightsNoFilter();
    }

    public int[] getOneStopFlights() {
        resetFilter();
//        TripUtil.waitElementToBeClickable(driver, oneStop);
//        oneStop.click();

        // weird that this one works instead of waitElementToBeClickable()
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", oneStop);

        return getNoOfFlightsNoFilter();
    }

    public String selectDepFlight(int cnt) {
        // get price of selected flight
        String[] flightArr = departureFlightsList.get(cnt).getText().split("\n");
        String price = flightArr[7].replaceAll("[^0-9]+", "").trim();

        // click
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", departureFlightsList.get(cnt));

        return price;
    }

    public String selectRetFlight(int cnt) {

        // get price of selected flight
        String[] flightArr = returnFlightsList.get(cnt).getText().split("\n");
        String price = flightArr[7].replaceAll("[^0-9]+", "").trim();

        // click
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", returnFlightsList.get(cnt));

        return price;
    }

    public String[] getActualPrices() {
        String strPrices[] = new String[2];
        strPrices[0] = depBottomPrice.getText().replaceAll("[^0-9]+", "").trim();
        TripUtil.pause(3000);
        strPrices[1] = retBottomPrice.getText().replaceAll("[^0-9]+", "").trim();

        return strPrices;
    }

    public int getActualTotalPrice() {
        String sumActualStr = totalPrice.getText().replaceAll("[^0-9]+", "").trim();
        int sumActualInt = Integer.parseInt(sumActualStr);

        String disc = "";
        if (discount.isDisplayed()) {
            disc = discount.getText().replaceAll("[^0-9]+", "").trim();

            int discInt = Integer.parseInt(disc);

            sumActualInt = sumActualInt + discInt;
        }

        return sumActualInt;
    }

    public void resetFilter() {
        resetFilter.click();
    }
}

