package com.trip.tests;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.trip.util.TripUtil;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TripOnePageTest {

	static WebDriver driver;

	public static void main(String[] args) {

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();

		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(TripUtil.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(TripUtil.IMPLICIT_WAIT, TimeUnit.SECONDS);
		driver.get("https://www.makemytrip.com/");

		// 1. click flight icon
		WebElement flight = driver.findElement(By.xpath("//a[@href='//www.makemytrip.com/flights/']"));
		// Actions action = new Actions(driver);
		// action.moveToElement(flight).click().perform();

		WebDriverWait wait = new WebDriverWait(driver, 40);
		wait.until(ExpectedConditions.visibilityOf(flight));

		flight.click();

		// 2. click roundtrip
		driver.findElement(By.xpath("//li[@data-cy='roundTrip']")).click();

		// 3. select from combobox
		driver.findElement(By.xpath("//label[@for='fromCity']")).click();

		selectFromCombobox(driver, "DEL");

		// to combobox
		// driver.findElement(By.xpath("//label[@for='toCity']")).click();

		// 4. select to combobox
		selectFromCombobox(driver, "BLR");

		// 5. select date
		// driver.findElement(By.xpath("//label[@for='departure']")).click();
		selectDatesDC(driver);

		// 5.1 see if non stop checkbox is clickable first
		waitElementToBeClickable(driver, driver.findElement(By.xpath("//span[contains(text(), 'Non Stop')]")));

		// 5.2 your now in result page, scroll to bottom
		scrollToBottomOfPage(driver);

		// 6. print total number of departure flights
		totalDepartureFlights(driver);

		// 7. print total number of return flights
		totalReturnFlights(driver);
	}

	public static void selectFromCombobox(WebDriver driver, String city) {

		WebElement elementList = driver.findElement(By.xpath("//ul[@role='listbox']"));

		List<WebElement> list = elementList.findElements(By.tagName("li"));

		for (WebElement e : list) {

			String lines[] = e.getText().split("\\r?\\n");

			String cityCd = lines[2];
			if (cityCd.equalsIgnoreCase(city)) {

				e.click();
				break;
			}
		}
	}

	// mine
	public static void selectDatesDC(WebDriver driver) {

		// 1. get current date
		DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
		Date date = new Date();
		String currentDate = formatDate(date);
		System.out.println("departure date: " + currentDate);

		// 2. separate day, month and year
		String dateStr = df.format(date);
		String splitDateStr[] = dateStr.split(" ");
		String day = splitDateStr[0];
		String month = splitDateStr[1];
		String year = splitDateStr[2];
		String monthYear = month + year;

		// 3. add certain number of days
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 7);
		Date dateR = cal.getTime();
		String returnDate = formatDate(dateR);
		System.out.println("return date: " + returnDate);

		// 4. get months displayed; 2 are displayed at a time; e.g. October and November
		WebElement elementMonths = driver.findElement(By.xpath("//div[@class='DayPicker-Months']"));
		List<WebElement> listMonths = elementMonths.findElements(By.xpath("//div[@class='DayPicker-Month']"));
		// System.out.println("listMonths size: " + listMonths.size());

		// 5. check if the displayed month is equal to string var 'month' + 'year'
		String monthYearDisplayed = driver.findElement(By.xpath("//div[@class='DayPicker-Caption']//div")).getText();
		System.out.println("monthYear: " + monthYear + " and monthYearDisplayed: " + monthYearDisplayed);

		// 5.1 if displayed, select a day (note: days are always displayed)
		String beforeXpathDay = "//div[@aria-label='";
		String afterXpathDay = "' and @aria-disabled='false']";

		if (monthYear.equalsIgnoreCase(monthYearDisplayed)) {
			System.out.println("date to click: " + beforeXpathDay + currentDate + afterXpathDay);
			System.out.println("date to click: " + beforeXpathDay + returnDate + afterXpathDay);
			driver.findElement(By.xpath(beforeXpathDay + currentDate + afterXpathDay)).click();
			driver.findElement(By.xpath(beforeXpathDay + returnDate + afterXpathDay)).click();

		}

		// 5.2 if not displayed, click the next calendar window - do this next time
		else {
			System.out.println("im here at else ----- ");
			WebElement navBtnNext = driver
					.findElement(By.xpath("//span[@class='DayPicker-NavButton DayPicker-NavButton--next']"));
			if (navBtnNext.isDisplayed()) {

				navBtnNext.click();
			}
		}

		// 6. click search button
		driver.findElement(By.xpath("//a[@class='primaryBtn font24 latoBlack widgetSearchBtn ']")).click();
	}

	public static void selectDatesFromQL(WebDriver driver) {
		try {
			driver.findElement(By.xpath("//input[@id='hp-widget__sfrom']")).click();
			driver.findElement(By.xpath("//input[@id='hp-widget__sfrom']")).clear();
			// driver.findElement(By.xpath("//input[@id='hp-widget__sfrom']")).sendKeys("Goa");
			Thread.sleep(2000);

			List<WebElement> fromCities = driver.findElements(By.xpath("//ul[@id='ui-id-1']/li/div/p/span[1]"));
			System.out.println(fromCities.size() + "\n");
			for (int i = 0; i < fromCities.size(); i++) {
				WebElement element = fromCities.get(i);
				System.out.println(element.getAttribute("innerHTML"));
			}

			// driver.findElement(By.xpath("//ul[@id='ui-id-1']/li/div/p/span")).click();
			driver.findElement(By.xpath("//li[contains(@aria-label,'Top Cities : Goa, India ')]/div/p/span[1]"))
					.click();

			driver.findElement(By.xpath("//input[@id='hp-widget__sTo']")).click();
			driver.findElement(By.xpath("//input[@id='hp-widget__sTo']")).clear();
			// driver.findElement(By.xpath("//input[@id='hp-widget__sTo']")).sendKeys("Mumbai");
			Thread.sleep(2000);

			List<WebElement> toCities = driver.findElements(By.xpath("//ul[@id='ui-id-2']/li/div/p/span[1]"));
			System.out.println(toCities.size() + "\n");
			for (int i = 0; i < toCities.size(); i++) {
				WebElement element = toCities.get(i);
				System.out.println(element.getAttribute("innerHTML"));
			}

			// driver.findElement(By.xpath("//ul[@id='ui-id-2']/li/div/p/span")).click();
			driver.findElement(By.xpath("//ul[@id='ui-id-2']/li[3]/div/p/span[1]")).click();
			Thread.sleep(2000);

			driver.findElement(By.xpath("//input[@id='hp-widget__depart']")).click();
			Thread.sleep(2000);

			String date = "10-OCTOBER-2018";
			String splitter[] = date.split("-");
			String month_year = splitter[1];
			String day = splitter[0];
			System.out.println(month_year);
			System.out.println(day);

			selectDateFromQL(month_year, day);
			Thread.sleep(3000);

			driver.findElement(By.xpath("//button[@id='searchBtn']")).click();
			Thread.sleep(5000);
			Thread.sleep(2000);

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollBy(0,3000)");

			/*
			 * Robot robot = new Robot(); robot.keyPress(KeyEvent.VK_PAGE_DOWN);
			 * robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
			 */

			Thread.sleep(5000);

			driver.findElement(By.xpath("//div[@id='aln_AI_dep']/span[3]")).click();
			Thread.sleep(5000);

			List<WebElement> flights = driver
					.findElements(By.xpath("//div[@class='top_first_part clearfix']/div/span/span[2]/span[1]"));

			System.out.println("No. of Air India flight search results: ---" + flights.size());

		} catch (Exception e) {
			System.out.println("Exception is: ---" + e + "\n");
		}
	}

	public static void selectDateFromQL(String monthyear, String Selectday) throws InterruptedException {
		List<WebElement> elements = driver.findElements(By.xpath("//div[@class='ui-datepicker-title']/span[1]"));

		for (int i = 0; i < elements.size(); i++) {
			System.out.println(elements.get(i).getText());
			// Selecting the month
			if (elements.get(i).getText().equals(monthyear)) {
				// Selecting the date
				List<WebElement> days = driver.findElements(By.xpath(
						"//div[@class='ui-datepicker-inline ui-datepicker ui-widget ui-widget-content ui-helper-clearfix ui-corner-all ui-datepicker-multi ui-datepicker-multi-2']/div[2]/table/tbody/tr/td/a"));

				for (WebElement d : days) {
					System.out.println(d.getText());
					if (d.getText().equals(Selectday)) {
						d.click();
						Thread.sleep(10000);
						return;
					}
				}

			}

		}
		driver.findElement(By.xpath(
				"//div[@class='ui-datepicker-inline ui-datepicker ui-widget ui-widget-content ui-helper-clearfix ui-corner-all ui-datepicker-multi ui-datepicker-multi-2']/div[2]/div/a/span"))
				.click();
		selectDateFromQL(monthyear, Selectday);

	}

	public static String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", java.util.Locale.ENGLISH);
		sdf.applyPattern("EEE MMM dd yyyy");
		String sMyDate = sdf.format(date);
		return sMyDate;
	}

	public static String totalDepartureFlights(WebDriver driver) {

		WebElement element = driver.findElement(By.xpath("//div[@id='ow-domrt-jrny']/div[2]"));
		List<WebElement> departureList = element
				.findElements(By.xpath("//div[@id='ow-domrt-jrny']/div[2]/div[@class='fli-list splitVw-listing ']"));
		departureList.add(driver.findElement(
				By.xpath("//div[@id='ow-domrt-jrny']/div[2]/div[@class='fli-list splitVw-listing active']")));
		// List<WebElement> departureList = element
		// .findElements(By.xpath("//div[@class='splitVw-sctn
		// pull-left']/child::div[2]/child::div"));

		waitForPageLoad(driver, departureList);

		System.out.println("departureList: " + departureList.size());

		return "departureList: " + departureList.size();
	}

	public static String totalReturnFlights(WebDriver driver) {
		WebElement element = driver.findElement(By.xpath("//div[@id='rt-domrt-jrny']/div[2]"));
		List<WebElement> returnList = element
				.findElements(By.xpath("//div[@id='rt-domrt-jrny']/div[2]/div[@class='fli-list splitVw-listing ']"));
		returnList.add(driver.findElement(
				By.xpath("//div[@id='rt-domrt-jrny']/div[2]/div[@class='fli-list splitVw-listing active']")));
		// List<WebElement> departureList = element
		// .findElements(By.xpath("//div[@class='splitVw-sctn
		// pull-left']/child::div[2]/child::div"));

		waitForPageLoad(driver, returnList);

		System.out.println("returnList: " + returnList.size());
		return "returnList: " + returnList.size();
	}

	public static void waitForPageLoad(WebDriver driver, List<WebElement> e) {
		WebDriverWait wait = new WebDriverWait(driver, 40);
		wait.until(ExpectedConditions.visibilityOfAllElements(e));
	}

	public static void scrollToBottomOfPage(WebDriver driver) {
		// try 1
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");

		// try 2
		// Actions actions = new Actions(driver);
		// actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();

		// try 3
		// ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,400)", "");

		// try 4 - slow scroll
		// for (int second = 0;; second++) {
		// if (second >= 60) {
		// break;
		// }
		// ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,400)", ""); //
		// y value '400' can be altered
		// try {
		// Thread.sleep(3000);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
	}

	public static void waitElementToBeClickable(WebDriver driver, WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}
}
