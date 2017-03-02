package com.cqblueprints.testing.cq.factory;

import com.cqblueprints.testing.cq.pageobjects.*;
import com.cqblueprints.testing.cq.pageobjects.impl.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageFactory extends AbstractPageFactory {

	@Override
	public AuthorPage getAuthorPage(WebDriver driver, WebDriverWait wait,
			String version) {
		if (version.equals("6.0")) {
			return new BaseAuthorPage(driver, wait);
		} else if (version.equals("6.1")) {
			return new AuthorPage61(driver, wait);
		} else if (version.equals("6.3")) {
			return new AuthorPage63(driver, wait);
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
	public PublishPage getPublishPage(WebDriver driver, String pagePath, String version) {
		if (version.equals("6.0")) {
			return new PublishPage60(driver, pagePath);
		} 
		return new PublishPage60(driver, pagePath);
	}

	@Override
	public SiteAdminPage getSiteAdminPage(WebDriver driver, WebDriverWait wait, String version) {
		if (version.equals("6.0")) {
			return new SiteAdminPage60(driver, wait);
		}  else if (version.equals("6.3")) {
			return new SiteAdminPage63(driver, wait);
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
