import com.cqblueprints.testing.cq.base.BaseActions;
import com.cqblueprints.testing.cq.base.TestBase;
import com.cqblueprints.testing.cq.factory.FactoryProducer;
import com.cqblueprints.testing.cq.pageobjects.AuthorPage;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

/**
 * Created by headwire on 4/10/2017.
 */

@RunWith(Parameterized.class)
public class MainTest extends TestBase{

    public static final String TEST_PAGE_NAME = "test-page-01";

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[1][0]);
    }

    public MainTest()
    {

    }

    @Test
    public void runTests() throws Exception
    {
        SiteAdminTest.testPageCreation();
        AuthorPageTest.testInlineEditedComponent();
        AuthorPageTest.testDialogConfiguredComponent();
        AuthorPageTest.testTouchDialogConfiguredComponent();
//        SiteAdminPage siteAdminPage = FactoryProducer.getPageFactory().getSiteAdminPage(driver, wait, environment.getVersion());
//        siteAdminPage.openSitesPage();
//        siteAdminPage.navigateToPage("/content/aem-selenium-basic-project");
//        Thread.sleep(1000);
//        siteAdminPage.createPageWithTemplate("/apps/aem-selenium-basic-project/templates/basic/thumbnail.png", TEST_PAGE_NAME);
//        driver.get(environment.getAuthorUrl()+"/editor.html"+"/content/aem-selenium-basic-project/"+TEST_PAGE_NAME+".html");
//
//        testTitle();

    }

    private static void testTitle() throws Exception
    {
        AuthorPage authorPage = FactoryProducer.getPageFactory().getAuthorPage(driver, wait, environment.getVersion());
        placeTitle(authorPage);

        authorPage.editComponent("title");
        authorPage.selectInlineEditor("Hello, World!");
        authorPage.closeInlineEditor();
    }

    private static void placeTitle(AuthorPage authorPage) throws Exception
    {
        authorPage.toggleSidePanel();
        authorPage.selectSidePanelTab("Components");
        Thread.sleep(1000);
        authorPage.dragComponentIntoParsys("Title - HTL", "par");

    }

    @After
    public void cleanup() {
        BaseActions.ACTIONS.deletePage("/content/aem-selenium-basic-project/"+TEST_PAGE_NAME, environment);
        //driver.quit();
    }
}
