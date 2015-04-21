package com.headwire.testing.cq.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.headwire.testing.cq.pageobjects.BaseAuthorPage;
import com.headwire.testing.cq.pageobjects.AuthorPage61;
import com.headwire.testing.cq.pageobjects.DAMPage60;
import com.headwire.testing.cq.pageobjects.InboxPage60;
import com.headwire.testing.cq.pageobjects.LoginPage60;
import com.headwire.testing.cq.pageobjects.PublishPage60;
import com.headwire.testing.cq.pageobjects.SiteAdminPage60;
import com.headwire.testing.cq.pageobjects.ToolsPage60;
import com.headwire.testing.cq.pageobjects.WelcomePage60;

public class PageFactory extends AbstractPageFactory {

	@Override
	public AuthorPage getAuthorPage(WebDriver driver, WebDriverWait wait,
			String version) {
		if (version.equals("6.0")) {
			return new BaseAuthorPage(driver, wait);
		} else if (version.equals("6.1")) {
			return new AuthorPage61(driver, wait);
		}
		return new BaseAuthorPage(driver, wait);
	}

	@Override
	public DAMPage getDAMPage(WebDriver driver, WebDriverWait wait, String version) {
		if (version.equals("6.0")) {
			return new DAMPage60(driver, wait);
		} 
		return new DAMPage60(driver, wait);
	}

	@Override
	public InboxPage getInboxPage(WebDriver driver, WebDriverWait wait, String version) {
		if (version.equals("6.0")) {
			return new InboxPage60(driver, wait);
		} 
		return new InboxPage60(driver, wait);
	}

	@Override
	public LoginPage getLoginPage(WebDriver driver, WebDriverWait wait, String version) {
		if (version.equals("6.0")) {
			return new LoginPage60(driver, wait);
		} 
		return new LoginPage60(driver, wait);
	}

	@Override
	public PublishPage getPublishPage(String pagePath, String version) {
		if (version.equals("6.0")) {
			return new PublishPage60(pagePath);
		} 
		return new PublishPage60(pagePath);
	}

	@Override
	public SiteAdminPage getSiteAdminPage(WebDriver driver, WebDriverWait wait, String version) {
		if (version.equals("6.0")) {
			return new SiteAdminPage60(driver, wait);
		} 
		return new SiteAdminPage60(driver, wait);
	}

	@Override
	public ToolsPage getToolsPage(WebDriver driver, WebDriverWait wait, String version) {
		if (version.equals("6.0")) {
			return new ToolsPage60(driver, wait);
		} 
		return new ToolsPage60(driver, wait);
	}

	@Override
	public WelcomePage getWelcomePage(WebDriver driver, WebDriverWait wait,
			String version) {
		if (version.equals("6.0")) {
			return new WelcomePage60(driver, wait);
		}
		return new WelcomePage60(driver, wait);
	}


}
