package com.cqblueprints.testing;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ByteArrayOutputStream;
import org.apache.http.Header;
import org.apache.http.util.EntityUtils;

import junit.framework.Assert;

import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.cqblueprints.testing.DragAndDropHelper.Position;


/**
 * Provides high level functions for performing complex interactions in AEM. 
 * 
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public enum PageUtil {

	PageUtil;
	private PageUtil() {}

	/**
	 * Moves the mouse by the specified offset
	 *
	 * @param  x  The x offset
	 * @param  y  The y offset
	 */
	public void moveMouse(int x, int y) {
		try {
			Robot robot = new Robot();
			robot.mouseMove(x, y);
		} catch (AWTException e) {
			Assert.fail("Failed to move mouse");
		}
	}
	
	/**
	 * Presses the escape key on the keyboard
	 *
	 */
	public void escape() {
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_ESCAPE);
		} catch (AWTException e) {
			Assert.fail("Failed to close suggestions");
		}
	}
	
	/**
	 * Uses the CQ WCMCommands to create a page using a POST action
	 *
	 * @param  pageName   The name of the page that will be created
	 * @param  parentPath The path of the parent page to create under
	 * @param  template   The AEM template that will be used for the page
	 * @param  env        The environment settings to use
	 *
	 */
	public void createPage(String pageName, String parentPath, String template, String url, String user, String pass) {		
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url+"/bin/wcmcommand");
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair("_charset_", "utf-8"));
			nvps.add(new BasicNameValuePair("cmd", "createPage"));
			nvps.add(new BasicNameValuePair("title", pageName));
			nvps.add(new BasicNameValuePair("parentPath", parentPath));
			nvps.add(new BasicNameValuePair("template", template));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpClientContext postContext = getAuthenticationContext(user, pass);
			CloseableHttpResponse response = httpclient.execute(httpPost, postContext);
			httpPost.releaseConnection();
			String responseStr = EntityUtils.toString(response.getEntity());

			System.out.println("Response");
			System.out.println(responseStr);



		} catch (Exception e) {
			Assert.fail("Failed to create page. Error: "+e.getMessage());
		}
	}
	
	/**
	 * Uses the CQ WCMCommands to active a page using a POST action
	 *
	 * @param  pagePath   The path of the page to be activated 
	 * @param  env        The environment settings to use
	 *
	 */
	public void activatePage(String pagePath, String url, String user, String pass) {		
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url+"/bin/replicate.json");
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair("cmd", "activate"));
			nvps.add(new BasicNameValuePair("path", pagePath));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpClientContext postContext = getAuthenticationContext(user, pass);
			httpclient.execute(httpPost, postContext);
			httpPost.releaseConnection();
		} catch (Exception e) {
			Assert.fail("Failed to activate page. Error: "+e.getMessage());
		}
	}
	
	/**
	 * Uses the CQ WCMCommands to deactive a page using a POST action
	 *
	 * @param  pagePath   The path of the page to be activated 
	 * @param  env        The environment settings to use
	 *
	 */
	public void deactivatePage(String pagePath, String url, String user, String pass) {	
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url+"/bin/replicate.json");
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair("cmd", "deactivate"));
			nvps.add(new BasicNameValuePair("path", pagePath));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpClientContext postContext = getAuthenticationContext(user, pass);
			httpclient.execute(httpPost, postContext);
			httpPost.releaseConnection();
		} catch (Exception e) {
			Assert.fail("Failed to deactivate page. Error: "+e.getMessage());
		}
	}
	
	/**
	 * Uses the CQ WCMCommands to delete a page using a POST action
	 *
	 * @param  pagePath   The path of the page to be activated 
	 * @param  env        The environment settings to use
	 *
	 */
	public void deletePage(String pagePath, String url, String user, String pass) {
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url+"/bin/wcmcommand");
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair("_charset_", "utf-8"));
			nvps.add(new BasicNameValuePair("cmd", "deletePage"));
			nvps.add(new BasicNameValuePair("force", "true"));
			nvps.add(new BasicNameValuePair("path", pagePath));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpClientContext postContext = getAuthenticationContext(user, pass);
			httpclient.execute(httpPost, postContext);
			httpPost.releaseConnection();
		} catch (Exception e) {
			Assert.fail("Failed to delete page. Error: "+e.getMessage());
		}
	}
	
	/**
	 * Helper function to set the authentication context on POST actions
	 * 
	 * @param  env        The environment settings to use
	 */
	public HttpClientContext getAuthenticationContext(String user, String pass) {
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user+":"+pass));
		HttpClientContext postContext = HttpClientContext.create();
		postContext.setCredentialsProvider(credentialsProvider);
		return postContext;
	}
	
	public static void dragDrop(WebDriver driver, WebDriverWait wait, WebElement source, WebElement target) {
		DragAndDropHelper.html5_DragAndDrop(driver, source, target, Position.Center, Position.Center);
		target.click();
	}
	
	public static boolean doubleClick(WebDriver driver, WebElement element) {
		try {
			Actions builder = new Actions(driver);
			Action doubleClick = builder.doubleClick(element).build();
			doubleClick.perform();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}


