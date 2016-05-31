package sachin.bws.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class SignUpPage {
	private WebDriver driver;

	public SignUpPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
}
