package com.cqblueprints.testing.cq.base;

import com.cqblueprints.testing.cq.base.DragAndDropHelper.Position;
import junit.framework.Assert;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides high level functions for performing complex interactions in AEM. 
 * 
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public enum BaseActions {

	ACTIONS;
	private BaseActions() {}

	/**
	 * Moves the mouse to the specified position and 
	 *
	 * @param  driver  The active WebDriver on the current Page Object
	 * @param  element The target element to move the mouse over
	 * @return      boolean indicated if the action was successful
	 */
	public boolean mouseOver(WebDriver driver, WebElement element) {
		try {
			Actions builder = new Actions(driver); 
			Actions hoverOverElement = builder.moveToElement(element);
			hoverOverElement.perform();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Performs a context click on the specified element 
	 *
	 * @param  driver  The active WebDriver on the current Page Object
	 * @param  element The target element to move the mouse over
	 * @return      boolean indicated if the action was successful
	 */
	public boolean contextClick(WebDriver driver, WebElement element) {
		try {
			Actions builder = new Actions(driver);
			Action contextClick = builder.contextClick(element).build();
			contextClick.perform();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Performs a double click on the specified element 
	 *
	 * @param  driver  The active WebDriver on the current Page Object
	 * @param  element The target element to move the mouse over
	 * @return      boolean indicated if the action was successful
	 */
	public boolean doubleClick(WebDriver driver, WebElement element) {
		try {
			Actions builder = new Actions(driver);
			Action doubleClick = builder.doubleClick(element).build();
			doubleClick.perform();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Performs a drag and drop on AuthorPage objects for an element inside 
	 * of the left content pane and places it inside the specified parsys 
	 *
	 * @param  driver  The active WebDriver on the current Page Object
	 * @param  wait The active WebDriverWait of the current Page Object
	 * @param  source The element inside of the left content pane to be dragged
	 * @param  target The parsys that the component will be placed in
	 *
	 */
	public void dragDrop(WebDriver driver, WebDriverWait wait, WebElement source, WebElement target) {
		if (driver.getCurrentUrl().contains("/editor.html")) {
			// 6.1 jQuery breaks this method
			/*InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("dragdrop_helper3.js");
			String jsString = "";
			try {
				jsString = IOUtils.toString(stream);
			} catch (IOException e) {
				Assert.fail("Drag and drop failed. Error: "+e.getMessage());
			}
			JavascriptExecutor js = (JavascriptExecutor)driver;  
			String targetEl = "jQuery('div[data-path=\""+target.getAttribute("data-path")+"/*\"]')";
			String sourceEl = "jQuery(\"h4:contains("+source.getText()+")\").filter(function() {return jQuery(this).text() == '"+source.getText()+"';})";
			String fullJSString = jsString+" "+sourceEl+".simulateDragDrop({ "
					+ "dropTarget: "+targetEl+"});";
			System.out.println(fullJSString);
			js.executeScript(fullJSString);*/ 
			// Native JS DnD
			DragAndDropHelper.html5_DragAndDrop(driver, source, target, Position.Center, Position.Center);
			target.click();
		} else {
			Actions builder = new Actions(driver);
			Action dragDrop = builder.clickAndHold(source)
					.moveToElement(target)
					.build();
			dragDrop.perform();	
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, 'ghost')]")));
			WebElement containerGhost = driver.findElement(By.xpath("//div[contains(@class, 'ghost')]"));
			containerGhost.click();
			driver.switchTo().defaultContent();
		}
	}
	
	/**
	 * Performs drag and drop for assets inside of the left content panel into
	 * the target parsys
	 *
	 * @param  driver  The active WebDriver on the current Page Object
	 * @param  wait The active WebDriverWait of the current Page Object
	 * @param  source The content asset to be dragged
	 * @param  target The parsys that the component will be placed in
	 *
	 * @return Boolean representing if the drag and drop operation was successful
	 */
	public boolean dragDropAsset(WebDriver driver, WebDriverWait wait, WebElement source, WebElement target) {
		DragAndDropHelper.html5_DragAndDrop(driver, source, target, Position.Center, Position.Center);
		target.click();
		return true;
	}
	
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
	public void hitEscape() {
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_ESCAPE);
		} catch (AWTException e) {
			Assert.fail("Failed to close suggestions");
		}
	}

	/**
	 * Presses the enter key on the keyboard
	 *
	 */
	public void hitEnter() {
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_ENTER);
		} catch (AWTException e) {
			Assert.fail("Failed to push enter");
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
	public void createPage(String pageName, String parentPath, String template, TestEnvironment env) {		
		try {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(env.getAuthorUrl()+"/bin/wcmcommand");
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("_charset_", "utf-8"));
		nvps.add(new BasicNameValuePair("cmd", "createPage"));
		nvps.add(new BasicNameValuePair("title", pageName));
		nvps.add(new BasicNameValuePair("parentPath", parentPath));
		nvps.add(new BasicNameValuePair("template", template));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		HttpClientContext postContext = getAuthenticationContext(env);
		httpclient.execute(httpPost, postContext);
		httpPost.releaseConnection();
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
	public void activatePage(String pagePath, TestEnvironment env) {		
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(env.getAuthorUrl()+"/bin/replicate.json");
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair("cmd", "activate"));
			nvps.add(new BasicNameValuePair("path", pagePath));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpClientContext postContext = getAuthenticationContext(env);
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
	public void deactivatePage(String pagePath, TestEnvironment env) {	
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(env.getAuthorUrl()+"/bin/replicate.json");
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair("cmd", "deactivate"));
			nvps.add(new BasicNameValuePair("path", pagePath));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpClientContext postContext = getAuthenticationContext(env);
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
	public void deletePage(String pagePath, TestEnvironment env) {
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(env.getAuthorUrl()+"/bin/wcmcommand");
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair("_charset_", "utf-8"));
			nvps.add(new BasicNameValuePair("cmd", "deletePage"));
			nvps.add(new BasicNameValuePair("force", "true"));
			nvps.add(new BasicNameValuePair("path", pagePath));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpClientContext postContext = getAuthenticationContext(env);
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
	 *
	 * @return  The HttpClientContext with authentication credentials to access instance
	 */
	public HttpClientContext getAuthenticationContext(TestEnvironment env) {
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(env.getTestUser(), env.getTestPassword()));
		HttpClientContext postContext = HttpClientContext.create();
		postContext.setCredentialsProvider(credentialsProvider);
		return postContext;
	}
}


