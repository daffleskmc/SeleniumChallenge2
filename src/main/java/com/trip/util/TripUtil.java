package com.trip.util;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TripUtil {

	public static long PAGE_LOAD_TIMEOUT = 30;

	public static long IMPLICIT_WAIT = 40;

	public static void waitForElementToBeVisible(WebDriver driver, WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, 40);
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public static void waitElementToBeClickable(WebDriver driver, WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	public static String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", java.util.Locale.ENGLISH);
		sdf.applyPattern("EEE MMM dd yyyy"); // e.g. Mon Nov 18 2019
		String sMyDate = sdf.format(date);
		return sMyDate;
	}

	public static String addDays(Date date, int howManyDays) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, howManyDays);
		Date dateR = cal.getTime();
		return formatDate(dateR);
	}

	public static void scrollToBottomOfPage(WebDriver driver) {
		try {
			long Height = Long.parseLong(
					((JavascriptExecutor) driver).executeScript("return document.body.scrollHeight").toString());

			while (true) {
				((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");

				Thread.sleep(3000);

				long newHeight = Long.parseLong(
						((JavascriptExecutor) driver).executeScript("return document.body.scrollHeight").toString());

				if (newHeight == Height) {
					break;
				}
				Height = newHeight;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	// Scrolls to top of page
	public static void scrollToTopOfPage(WebDriver driver) {

		((JavascriptExecutor) driver).executeScript("window.scrollTo(document.body.scrollHeight,0);");

	}

	public static void pause(Integer milliseconds) {
		try {
			TimeUnit.MILLISECONDS.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
