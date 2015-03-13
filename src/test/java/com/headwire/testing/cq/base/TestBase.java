package com.headwire.testing.cq.base;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.ProxyServer;
import net.lightbody.bmp.proxy.http.BrowserMobHttpRequest;
import net.lightbody.bmp.proxy.http.RequestInterceptor;

import org.apache.commons.codec.binary.Base64;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

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
	
	@BeforeClass
	public static void setupProxy() throws FileNotFoundException, UnsupportedEncodingException {
		environment = TestEnvironmentLoader.INSTANCE.loadConfiguration("dev");
		String loginString = environment.getTestUser()+":"+environment.getTestPassword();
		final byte[] encodedCredentials = Base64.encodeBase64(loginString.getBytes());
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
	
	@Before
	public void setupEnvironment() throws MalformedURLException {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		Proxy proxy = server.seleniumProxy();
		capabilities.setCapability(CapabilityType.PROXY, proxy);
		driver = new FirefoxDriver(capabilities);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, 20);
	}
	
	@After
	public void tearDown() {
		driver.quit();
	}
	
	@AfterClass
	public static void stopProxy() {
		server.stop();
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
