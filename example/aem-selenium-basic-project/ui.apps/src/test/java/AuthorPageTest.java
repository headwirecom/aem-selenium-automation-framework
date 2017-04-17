import com.cqblueprints.testing.cq.base.TestBase;
import com.cqblueprints.testing.cq.factory.FactoryProducer;
import com.cqblueprints.testing.cq.pageobjects.AuthorPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by headwire on 4/12/2017.
 */
public class AuthorPageTest extends TestBase {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorPageTest.class);

    /**
     * Tests the following methods of AuthorPage interface:
     *
     * <ul>
     *     <li>toggleSitePanel</li>
     *     <li>selectSidePanelTab</li>
     *     <li>dragComponentIntoParsys</li>
     *     <li>editComponent</li>
     *     <li>selectInlineEditor</li>
     *     <li>closeInlineEditor</li>
     *     <li>deleteComponent</li>
     *     <li>switchToDefaultContent</li>
     *     <li>switchToContent</li>
     * </ul>
     */
    public static void testInlineEditedComponent() throws Exception
    {
        AuthorPage authorPage = FactoryProducer.getPageFactory().getAuthorPage(driver, wait, environment.getVersion());
        authorPage.toggleSidePanel();
        authorPage.selectSidePanelTab("Components");
        Thread.sleep(1000);
        authorPage.dragComponentIntoParsys("Title (WCM)", "par");
        authorPage.editComponent("title");
        authorPage.selectInlineEditor("Hello, World!");
        authorPage.closeInlineEditor();

        authorPage.switchToContent();
        synchronized (authorPage)
        {
            authorPage.assertExists(By.xpath("//h1[text()='Hello, World!']"));
        }
//        authorPage.assertExists(By.xpath("//h1[text()='Hello, World!']"));
//        LOG.info("Sleep for 3 seconds so we can confirm the text before deleting it.");
//        Thread.sleep(3000);
        authorPage.switchToDefaultContent();
        authorPage.deleteComponent("title");
        authorPage.switchToContent();

        WebDriverWait shortWait = new WebDriverWait(driver, 1L);
        authorPage.assertNotExists(By.xpath("//h1[text()='Hello, World!']"), shortWait);
        authorPage.switchToDefaultContent();
    }

    /**
     * Tests the following methods of AuthorPage interface:
     *
     * <ul>
     *     <li>fillInDialogFieldByName</li>
     *     <li>selectDialogTab</li>
     *     <li>confirmDialog</li>
     * </ul>
     * @throws Exception
     */
    public static void testDialogConfiguredComponent() throws Exception
    {
        AuthorPage authorPage = FactoryProducer.getPageFactory().getAuthorPage(driver, wait, environment.getVersion());
        authorPage.switchToDefaultContent();
//        authorPage.toggleSidePanel();
        Thread.sleep(1000);
        authorPage.selectSidePanelTab("Components");
        Thread.sleep(1000);
        authorPage.dragComponentIntoParsys("Text Field", "par");
        String elementName = "text-element-name";
        String defaultText = "Default Text For Input";
        authorPage.switchToContent();
        authorPage.switchToDefaultContent();
        authorPage.editComponent("text");
        authorPage.fillInDialogFieldByName("./name", elementName);
        authorPage.selectDialogTab("Initial Values");
        authorPage.fillInDialogFieldByName("./defaultValue", defaultText);
        authorPage.switchToDefaultContent();
        authorPage.confirmDialog();
        authorPage.switchToContent();

        authorPage.assertExists(By.name(elementName));
        authorPage.assertExists(By.xpath("//input[@value='"+defaultText+"']"));

        authorPage.switchToDefaultContent();

        authorPage.deleteComponent("text");

        authorPage.switchToContent();
        WebDriverWait shortWait = new WebDriverWait(driver, 1L);
        authorPage.assertNotExists(By.xpath("//input[@value='"+defaultText+"']"), shortWait);
    }

    public static void testTouchDialogConfiguredComponent() throws Exception
    {
        AuthorPage authorPage = FactoryProducer.getPageFactory().getAuthorPage(driver, wait, environment.getVersion());
        authorPage.switchToDefaultContent();
        Thread.sleep(1000);
        authorPage.selectSidePanelTab("Components");
        Thread.sleep(1000);
        authorPage.dragComponentIntoParsys("Testing Component", "par");
        authorPage.editComponent("testing_component");
        authorPage.fillInDialogFieldByName("./firstText", "Hello");
        authorPage.selectDialogTab("Second Tab");
        authorPage.fillInDialogFieldByName("./secondText", "World");
        authorPage.switchToDefaultContent();
        authorPage.confirmDialog();
        authorPage.switchToContent();

        synchronized (authorPage)
        {
            authorPage.assertExists(By.xpath("//div[contains(@class, 'testing-component') and contains(normalize-space(), 'First Text: Hello Second Text: World')]"));
            Thread.sleep(500);
        }


        authorPage.switchToDefaultContent();

        authorPage.deleteComponent("testing_component");


        authorPage.switchToContent();
        WebDriverWait shortWait = new WebDriverWait(driver, 1L);

        authorPage.assertNotExists(By.xpath("//div[contains(@class, 'testing-component') and contains(normalize-space(), 'First Text: Hello Second Text: World')]"), shortWait);
    }
}
