package com.cqblueprints.testing.cq.base;

import junit.framework.Assert;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.ProxyServer;
import net.lightbody.bmp.proxy.http.BrowserMobHttpRequest;
import net.lightbody.bmp.proxy.http.RequestInterceptor;
import org.apache.commons.codec.binary.Base64;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


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
	public static final ProxyServer server = new ProxyServer(4444);
	public static String DRIVERS_PATH = "/drivers/";
	
	@BeforeClass
	public static void setupProxy() throws Exception {
		environment = TestEnvironmentLoader.INSTANCE.loadConfiguration("dev");
		if (environment.getProxyEnabled().equals("true")) {
			String loginString = environment.getTestUser()+":"+environment.getTestPassword();
			final byte[] encodedCredentials = Base64.encodeBase64(loginString.getBytes());
			String[] whitelist = {environment.getAuthorUrl()+".*"};
			server.start();
			server.addRequestInterceptor(new RequestInterceptor() {
				public void process(BrowserMobHttpRequest request, Har har) {
					request.getMethod().addHeader("Accept-Charset", "UTF-8");
					try {
						request.getMethod().addHeader("Authorization", "Basic "+new String(encodedCredentials,"UTF-8"));
					} catch (UnsupportedEncodingException e) {
						Assert.fail("Credentials encoding not supported");
					}
				}
			});
			server.newHar(environment.getAuthorUrl().replace("http://", ""));
		}
	}
	
	@Before
	public void setupEnvironment() throws Exception {
		setupBrowser();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, 20);
		if (!environment.getProxyEnabled().equals("true")) {
			login();
		}
	}
	
	public void login() {
		driver.get(environment.getAuthorUrl());
		driver.findElement(By.id("username")).sendKeys(environment.getTestUser());
		driver.findElement(By.id("password")).sendKeys(environment.getTestPassword());
		if (environment.getVersion().equals("5.6")) {
			driver.findElement(By.xpath("//*[@id='login']/button")).click();
		} else {
			driver.findElement(By.id("submit-button")).click();
		}
	}
	
	public void setupBrowser() throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		if (environment.getProxyEnabled().equals("true")) {
			Proxy proxy = server.seleniumProxy();
			capabilities.setCapability(CapabilityType.PROXY, proxy);
		}
		String browser = environment.getBrowser();
		if (browser.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver(capabilities);
		} else if (browser.equalsIgnoreCase("chrome")) {
			String fullPath = System.getProperty("user.dir");
			String driverPath = fullPath + DRIVERS_PATH;
			String os = System.getProperty("os.name"); 
			if (os.startsWith("Windows")) {
				driverPath = driverPath + "chromedriver.exe";
			} else if (os.startsWith("Mac")) {
				driverPath = driverPath + "chromedriver-mac";
			} else {
				driverPath = driverPath + "chromedriver-linux";
			}
			System.setProperty("webdriver.chrome.driver", driverPath);

			ChromeOptions options = new ChromeOptions();
			options.addArguments("disable-extensions");
			options.addArguments("disable-infobars");
			//capabilities.setCapability("chrome.switches", Arrays.asList("--disable-extensions"));
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);

			driver = new ChromeDriver(capabilities);
		} else if (browser.equalsIgnoreCase("ie")) {
			String os = System.getProperty("os.name"); 
			Assert.assertFalse("IE can only be tested on Windows", os.startsWith("Windows"));
			String fullPath = System.getProperty("user.dir");
			String driverPath = fullPath + DRIVERS_PATH;
			String arch = System.getProperty("os.arch");
			if (arch.equalsIgnoreCase("x86")) {
				driverPath = driverPath + "iedriver-x86.exe";
			} else {
				driverPath = driverPath + "iedriver.exe";
			}
			System.setProperty("webdriver.ie.driver", driverPath);
			driver = new InternetExplorerDriver(capabilities);
		} else if (browser.equalsIgnoreCase("http")) {
			driver = new HtmlUnitDriver(capabilities);
		} else {
			throw new Exception("Unsupported browser: "+browser+". Valid values are: firefox, chrome, ie, html");
		}
	}
	
	
	@AfterClass
	public static void stopProxy() throws Exception {
		if (environment.getProxyEnabled().equals("true")) {
			server.stop();
		}
	}
	
	class BasicAuthInterceptor implements RequestInterceptor {
		private String encodedCredentials;
		
		BasicAuthInterceptor(byte[] credentials) throws UnsupportedEncodingException {
			this.encodedCredentials = new String(credentials,"UTF-8");
		}
		
		public void process(BrowserMobHttpRequest request, Har har) {
			request.getMethod().addHeader("Accept-Charset", "UTF-8");
			request.getMethod().addHeader("Authorization", "Basic "+encodedCredentials);
		}
	}
}
