package com.cqblueprints.testing.cq.base;

import com.cqblueprints.testing.cq.factory.FactoryProducer;
import com.cqblueprints.testing.cq.pageobjects.DAMPage;
import org.apache.http.client.ClientProtocolException;
import org.junit.Before;

import java.io.IOException;

public class DefaultDamAdminBase extends TestBase{
	public static DAMPage damPage;
	protected static final String DEFAULT_DAM_PAGE = "/content/dam/geometrixx";
	protected static final String AEM_6_DAM = "/assets.html";
	
	public static final String COMPONENTS_TAB_NAME = "Components";
	
	@Before
	public void startDriver() throws ClientProtocolException, IOException {
		driver.get(environment.getAuthorUrl()+AEM_6_DAM+DEFAULT_DAM_PAGE);
		damPage = FactoryProducer.getPageFactory().getDAMPage(driver, wait, environment.getVersion());
	}
	
	
}
