package com.trip.tests;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

public class TripMainTest {

	static WebDriver driver;

	public static void main(String[] args) {

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();

		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(TripUtil.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(TripUtil.IMPLICIT_WAIT, TimeUnit.SECONDS);
		driver.get("https://www.makemytrip.com/");

		// flight icon
		WebElement flight = driver.findElement(By.xpath("//a[@href='//www.makemytrip.com/flights/']"));
		// Actions action = new Actions(driver);
		// action.moveToElement(flight).click().perform();

		WebDriverWait wait = new WebDriverWait(driver, 40);
		wait.until(ExpectedConditions.visibilityOf(flight));

		// flight.click();
		//
		// // from combobox
		// driver.findElement(By.xpath("//label[@for='fromCity']")).click();
		//
		// selectFromCombobox(driver, "DEL");
		//
		// // to combobox
		// // driver.findElement(By.xpath("//label[@for='toCity']")).click();
		//
		// selectFromCombobox(driver, "BLR");

		// select dates 1
		// selectDatesFromQL(driver);

		// select date 2
		driver.findElement(By.xpath("//label[@for='departure']")).click();
		selectDatesDC(driver);

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
		System.out.println("current date: " + df.format(date));

		// 2. separate day, month and year
		String dateStr = df.format(date);
		String splitDateStr[] = dateStr.split(" ");
		String day = splitDateStr[0];
		String month = splitDateStr[1];
		String year = splitDateStr[2];
		String monthYear = month + " " + year;

		// 3. get months displayed; 2 are displayed at a time; e.g. October and November
		WebElement elementMonths = driver.findElement(By.xpath("//div[@class='DayPicker-Months']"));
		List<WebElement> listMonths = elementMonths.findElements(By.xpath("//div[@class='DayPicker-Month']"));
		System.out.println("listMonths size: " + listMonths.size());

		// 4. check if the displayed month is equal to string var month

		String monthYearDisplayed = driver.findElement(By.xpath("//div[@class='DayPicker-Caption']//div")).getText();
		System.out.println("monthDisplayed: " + monthYearDisplayed);

		// 4.1 if displayed, select a day (not: this is always displayed)
		if (monthYear.equalsIgnoreCase(monthYearDisplayed)) {

		}

		// 4.2 if not displayed, click the next calendar window
		else {
			WebElement navBtnNext = driver
					.findElement(By.xpath("//span[@class='DayPicker-NavButton DayPicker-NavButton--next']"));
			if (navBtnNext.isDisplayed()) {

				navBtnNext.click();
			}
		}
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
}
