package com.trip.tests;

import com.trip.base.TripBase;
import com.trip.pages.TripMainPage;
import com.trip.pages.TripSearchResultPage;
import com.trip.util.TripUtil;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TripSearchResultPageTest extends TripBase {

    public static TripSearchResultPage resultPage;

    public static TripMainPage mainPage;

    @BeforeClass
    public void setup() {
        initialization();
        mainPage = new TripMainPage();
        mainPage.selectCities("DEL", "BLR");
        mainPage.selectDates(7);
        resultPage = mainPage.clickSearch();
    }

    @Test(priority = 1)
    public void validateNoOfFlightsWithoutFilters() {
        int cnt[] = resultPage.getNoOfFlightsNoFilter();

        System.out.println("Total departure flights: " + cnt[0]);
        System.out.println("Total return flights: " + cnt[1]);
    }

    @Test(priority = 2)
    public void validateNoOfNonStopFlights() {
        int cnt[] = resultPage.getNonStopFlights();

        System.out.println("\nTotal non stop departure flights: " + cnt[0]);
        System.out.println("Total non stop return flights: " + cnt[1]);
    }

    @Test(priority = 3)
    public void validateNoOfOneStopFlights() {
        int cnt[] = resultPage.getOneStopFlights();

        System.out.println("\nTotal 1 stop departure flights: " + cnt[0]);
        System.out.println("Total 1 stop return flights: " + cnt[1]);
    }

    @Test(priority = 4)
    public void validatePrice() {
        resultPage.resetFilter();

        TripUtil.scrollToBottomOfPage(driver);
        TripUtil.scrollToTopOfPage(driver);

        String depStr = resultPage.selectDepFlight(4);
        System.out.println("\nSelected departure flight price: " + depStr);

        TripUtil.pause(3000);

        String retStr = resultPage.selectRetFlight(3);
        System.out.println("\nSelected return flight price: " + retStr);

        // compare prices
        String actualPrices[] = resultPage.getActualPrices();

        Assert.assertEquals(actualPrices[0], depStr);
        Assert.assertEquals(actualPrices[1], retStr);


        // validate total
        int sumSelected = Integer.parseInt(depStr) + Integer.parseInt(retStr);
        int sumActual = resultPage.getActualTotalPrice();

        Assert.assertEquals(sumActual, sumSelected);
    }

    @AfterClass
    public void tearDown() {

    }
}
