package com.trip.base;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.trip.util.TripUtil;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TripBase {

	public static WebDriver driver;

	public static Properties props;

	public TripBase() {
		try {
			props = new Properties();
			FileInputStream fis = new FileInputStream(
					System.getProperty("user.dir") + "\\src\\main\\java\\com\\trip\\config\\config.properties");

			// FileInputStream fis = new FileInputStream(
			// "C:\\Users\\indeztruk\\eclipse-workspace\\SeleniumChallenge2\\src\\main\\java\\com\\trip\\config\\config.properties");
			props.load(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void initialization() {
		String browser = props.getProperty("browser");

		if (browser.equalsIgnoreCase("Chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("Firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		}

		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(TripUtil.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(TripUtil.IMPLICIT_WAIT, TimeUnit.SECONDS);
		driver.get(props.getProperty("url"));
	}

}
