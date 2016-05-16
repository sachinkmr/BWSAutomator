package sachin.bws.site;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import sachin.bws.selenium.WebDriverBuilder;

public class TestNG {
	WebDriverBuilder builder;
	WebDriver driver;
	Site site;

	@BeforeSuite(alwaysRun = true)
	public void setUp() {
		site = new SiteBuilder("http://lipton-uat.unileversolutions.com").setBrandName("Lipton").setCrawling(true)
				.setCulture("en-US").setEnvironment("UAT").setTimeout(120).setUsername("wlnonproduser").setPassword("Pass@word11").build();
		builder = site.hasAuthentication() ? new WebDriverBuilder(site.getUsername(), site.getPassword())
				: new WebDriverBuilder();
		driver = builder.getDriver(site);
	}

	@Test
	public void test() {
		driver.get(site.getUrl());
	}

	@AfterSuite(alwaysRun = true)
	public void tearDown() {
//		builder.destroy();
		WebDriverBuilder.killServers();
	}

}