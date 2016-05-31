package sachin.bws.modules;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.SkipException;

import sachin.bws.pages.SignUpPage;
import sachin.bws.selenium.WrapperMethods;

/*
 *
 * @author Sachin
 *
 * */
public class Header {
	@FindBy(className = "bws-logo")
	private WebElement logo;

	@FindBy(css = "a.bws-signup")
	private WebElement signup;

	@FindBy(css = "div.bws-socials ul li")
	private List<WebElement> socialIconsInHeader;

	@FindBy(css = "select#language")
	private WebElement languageSelector;

	@FindBy(css = "div.bws-country-selector-wrapper a")
	private WebElement countrySelector;

	@FindBy(css = "form#bws-site-search-nav input#keyword")
	private WebElement siteSearch;

	@FindBy(css = "form#bws-site-search-nav button#bws-s-search-btn")
	private WebElement siteSearchButton;

	private WebDriver driver;
	private TopNavigation topNav;

	public Header(WebDriver driver) {
		// this.driver = driver;
		PageFactory.initElements(driver, this);
		topNav = new TopNavigation(driver);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	public void clickLogo() {
		this.logo.click();
	}

	public TopNavigation getTopNav() {
		return topNav;
	}

	public SignUpPage openSignUpPage() {
		try{
			this.signup.click();
		}catch(NoSuchElementException ex){
			throw new SkipException(
					"Signup Functionality may not be present on this site. Signup link is not present in header.");
		}
		return new SignUpPage(driver);
	}
}
