package sachin.bws.modules;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TopNavigation {
	private String type="flat";
	private WebDriver driver;

	@FindBy(css="nav.top-fly-out-nav ul.navbar-nav")
	private List<WebElement> menus;

	public TopNavigation(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		for(WebElement e:menus){
			if(e.findElements(By.cssSelector("ul.bws-nav-child")).size()>0){
				type="fly-out";
				break;
			}
		}
	}

	/*
	 * This method returns type of top navigation
	 *
	 * */

	public String getType(){
		return type;
	}

}
