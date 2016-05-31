package sachin.bws.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WrapperMethods {
	public static boolean isPresent(WebDriver driver, WebElement element) {
		try {
			new WebDriverWait(driver, 2).until(ExpectedConditions.elementToBeClickable(element));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isPresent(WebDriver driver, WebElement element,int time) {
		try {
			new WebDriverWait(driver, time).until(ExpectedConditions.elementToBeClickable(element));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
