package com.cqblueprints.testing.cq.factory;

import com.cqblueprints.testing.cq.pageobjects.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class AbstractPageFactory {
	public abstract AuthorPage getAuthorPage(WebDriver driver, WebDriverWait wait, String version);
	public abstract DAMPage getDAMPage(WebDriver driver, WebDriverWait wait, String version);
	public abstract InboxPage getInboxPage(WebDriver driver, WebDriverWait wait, String version);
	public abstract LoginPage getLoginPage(WebDriver driver, WebDriverWait wait, String version);
	public abstract PublishPage getPublishPage(WebDriver driver, String pagePath, String version);
	public abstract SiteAdminPage getSiteAdminPage(WebDriver driver, WebDriverWait wait, String version);
	public abstract ToolsPage getToolsPage(WebDriver driver, WebDriverWait wait, String version);
	public abstract WelcomePage getWelcomePage(WebDriver driver, WebDriverWait wait, String version);
}
