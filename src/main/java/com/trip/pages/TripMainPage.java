package com.trip.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.trip.base.TripBase;

public class TripMainPage extends TripBase {

	@FindBy(xpath = "//a[@href='//www.makemytrip.com/flights/']")
	WebElement flights;

	@FindBy(xpath = "//li[@data-cy='roundTrip']")
	WebElement roundTrip;

	@FindBy(id = "fromCity")
	WebElement fromCity;

	@FindBy(id = "toCity")
	WebElement toCity;

	@FindBy(xpath = "//ul[@role='listbox']")
	static WebElement cityList;

	@FindBy(xpath = "//label[@for='fromCity']")
	WebElement fromLabel;

	@FindBy(xpath = "//label[@for='toCity']")
	WebElement toLabel;

	public TripMainPage() {
		PageFactory.initElements(driver, this);
	}

	public void selectCities(String fromWhere, String toWhere) {
		flights.click();
		roundTrip.click();

		// 1. select from city
		fromLabel.click();

		// 2. select from city combobox
		selectCityFromComboBox(fromWhere);

		// 3. select to city combobox
		selectCityFromComboBox(toWhere);

	}

	public static void selectCityFromComboBox(String city) {
		List<WebElement> listFromCity = cityList.findElements(By.tagName("li"));

		for (WebElement e : listFromCity) {
			String cityCdFullStr[] = e.getText().split("\\r?\\n");
			String cityCd = cityCdFullStr[2];
			if (cityCd.equalsIgnoreCase(city)) {
				e.click();
				break;
			}
		}
	}

	public void selectDates(String dateDeparture, String dateReturn) {

	}
}
