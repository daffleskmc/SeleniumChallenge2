package com.trip.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.trip.base.TripBase;
import com.trip.pages.TripMainPage;

public class TripMainPageTest extends TripBase {

	TripMainPage main;

	public TripMainPageTest() {
		super();
	}

	@BeforeMethod
	public void setup() {
		initialization();
		main = new TripMainPage();
	}

	@Test
	public void searchFlightTest() {
		// main.searchFlight("Delhi", "Bangalore");
		main.selectCities("DEL", "BLR");
	}

	@AfterMethod
	public void tearDown() {

	}

}
