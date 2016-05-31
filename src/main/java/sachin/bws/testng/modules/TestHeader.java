package sachin.bws.testng.modules;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import sachin.bws.modules.Header;
import sachin.bws.selenium.WebDriverBuilder;
import sachin.bws.site.Site;
import sachin.bws.site.SiteBuilder;

public class TestHeader {
	private WebDriver driver;
	private WebDriverBuilder builder;
	private Site site;
	public String siteUrl;
	private Header header;

	@BeforeSuite(alwaysRun = true)
	public void setup() {
		site = new SiteBuilder("http://www.liptontea.ca/").setBrandName("Lipton").setCulture("en-US")
				.setEnvironment("UAT").setTimeout(60).build();
		builder = new WebDriverBuilder();
		driver = builder.getFirefoxDriver(site);
		siteUrl = site.getUrl();
		driver.navigate().to(siteUrl);
	}

	@BeforeClass(alwaysRun = true)
	public void initHeader() {
		header = new Header(driver);

	}

	@Test(description = "Verify that clicking on logo loads home page", groups = { "Header" },enabled=false)
	public void verifyLogo() {
		header.clickLogo();
		Assert.assertEquals(driver.getCurrentUrl(), siteUrl);
	}

	@Test(description = "Verify that if signup link is present in header, open it", groups = { "Header" })
	public void testSignUpLink() {
		header.openSignUpPage();
	}

	@AfterSuite(alwaysRun = true)
	public void tearDown() {
		builder.destroy();
		site = null;
		WebDriverBuilder.killServers();
	}
}
