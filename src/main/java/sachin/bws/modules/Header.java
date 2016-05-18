package sachin.bws.modules;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/*
 *
 * @author Sachin
 *
 * */
public class Header {
	@FindBy(id = "logo")
	WebElement logo;
	WebElement signup;
	WebElement languageSelector;
	WebElement countrySelector;
}
