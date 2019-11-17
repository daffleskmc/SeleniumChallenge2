package com.trip.pages;

import com.trip.base.TripBase;
import com.trip.util.TripUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Date;
import java.util.List;

public class TripMainPage extends TripBase {

	//@FindBy(xpath = "//a[@href='https://www.undefined/flights/']")
	@FindBy(xpath = "//a[@href='https://www.makemytrip.com/flights/']")
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

	@FindBy(xpath = "//a[@class='primaryBtn font24 latoBlack widgetSearchBtn ']")
	WebElement searchBtn;

	public TripMainPage() {
		PageFactory.initElements(driver, this);
	}

	public void selectCities(String fromWhere, String toWhere) {
		TripUtil.waitForElementToBeVisible(driver, flights);

		// 1. click flights
		flights.click();

		// 2. click roundtrip
		roundTrip.click();

		// 3. select from city combobox
		fromLabel.click();

		// 4. select from city combobox
		selectCityFromComboBox(fromWhere);

		// 5. select to city combobox
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

	public void selectDates(int howManyDays) {
		// 1. get today's date
		Date date = new Date();
		String currentDate = TripUtil.formatDate(date);

		// 2. add number of days to today's date
		String returnDate = TripUtil.addDays(date, howManyDays);

		String beforeXpathDay = "//div[@aria-label='";
		String afterXpathDay = "' and @aria-disabled='false']";

		driver.findElement(By.xpath(beforeXpathDay + currentDate + afterXpathDay)).click();
		driver.findElement(By.xpath(beforeXpathDay + returnDate + afterXpathDay)).click();
	}

	public TripSearchResultPage clickSearch() {
		searchBtn.click();
		driver.manage().deleteAllCookies();

		return new TripSearchResultPage();
	}
}
