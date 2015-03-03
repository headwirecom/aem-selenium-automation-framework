package com.headwire.testing.cq.base;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.headwire.testing.cq.factory.FactoryProducer;
import com.headwire.testing.cq.factory.LoginPage;

/**
 * Base class tests inherit from.&nbsp;Provides environment setup,
 * remote driver configuration, and login prior to tests running
 * 
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public class TestBase {
	public static WebDriver driver;
	public static WebDriverWait wait;
	public static TestEnvironment environment;
	
	@Before
	public void setupEnvironment() throws FileNotFoundException, MalformedURLException {
		environment = TestEnvironmentLoader.INSTANCE.loadConfiguration("dev");		
//		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
//		capabilities.setCapability(Platform.LINUX);
		driver = new FirefoxDriver();
//                new URL("http://192.168.42.17:4444/wd/hub"), 
//                capabilities);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get(environment.getAuthorUrl());
		wait = new WebDriverWait(driver, 20);	
		LoginPage loginPage = FactoryProducer.getPageFactory().getLoginPage(driver, wait, environment.getVersion());
		loginPage.loginAs(environment.getTestUser(), environment.getTestPassword());
	}
	
	@After
	public void tearDown() {
		driver.quit();
	}
}
