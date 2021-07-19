package cases;

import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.TestsData;
import utils.WDFactory;
import utils.WDType;

import java.io.ByteArrayInputStream;
import java.util.List;

@Epic("Домашка по Allure Report")
public class HomeWork12Test {
    protected static WebDriver driver;
    private final Logger log = LogManager.getLogger(HomeWork12Test.class);
    TestsData testsData = ConfigFactory.create(TestsData.class);

    @Before
    public void startUp(){
        driver = WDFactory.getDriver(WDType.CHROME);
        driver.manage().window().maximize();
        //Настройка не явных ожиданий
//        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @After
    public void shutDown(){
        if (driver!=null)
            driver.quit();
        log.info("Web Driver закрыт\n");
    }

    @Test
    @Story("Страница контакты")
    @Step("Проверка адреса на странице контакты")
    public void checkContactAddress(){
        gotoURL(testsData.otusURL());

        By contactsLink = By.cssSelector("a[href=\"/contacts/\"].header2_subheader-link");
        getElement(contactsLink).click();
        log.info("Переход на страницу Контакты");

        By address = By.xpath("//div[text()='Адрес']/following-sibling::div");
        String actualAddress = getElement(address).getText();
        Assert.assertEquals(testsData.expectedAddress(),actualAddress);
        log.info("Проверка адреса на странице Контакты");
    }

    @Test
    @Story("Главная страница")
    @Step("Проверка заголовка страницы")
    public void checkPageTitle(){
        gotoURL(testsData.otusURL());

        By contactsLink = By.cssSelector("a[href=\"/contacts/\"].header2_subheader-link");
        getElement(contactsLink).click();
        log.info("Переход на страницу Контакты");

        driver.manage().window().maximize();
        log.info("Браузер развернут на полный экран");

        Assert.assertEquals(testsData.contactsTitle(), driver.getTitle());
        log.info("Проверка Title страницы");
    }

    @Test
    @Story("Поиск номеров")
    @Step("Проверка поиска номеров")
    public void numberSearchTest(){
        gotoURL("https://msk.tele2.ru/shop/number");

        By searchNumber = By.cssSelector("input#searchNumber");
        getElement(searchNumber).clear();
        getElement(searchNumber).sendKeys("97");
        log.info("Поиск номеров по цифрам 97");

        List<WebElement> elements = driver.findElements(By.cssSelector("span.phone-number"));
        Assert.assertEquals(20,elements.size(),4.0);
        log.info("Проверка количества вернувшихся элементов");
    }

    @Test
    @Story("Страница FAQ")
    @Step("Проверка текста в разделе FAQ")
    public void checkFAQ(){
        gotoURL(testsData.otusURL());

        By faqLink = By.cssSelector("a[href=\"/faq/\"].header2_subheader-link");
        getElement(faqLink).click();
        log.info("Переход на страницу FAQ");

        By questionLink = By.xpath("//div[text()='Где посмотреть программу интересующего курса?']");
        getElement(questionLink).click();
        log.info("Нажатие на ссылку с вопросом");

        By answerText = By.xpath("//div[text()='Где посмотреть программу интересующего курса?']/following-sibling::div");
        String actual = getElement(answerText).getText();
        Assert.assertEquals(testsData.answerText(), actual);
        log.info("Проверка текста ответа");
    }

    @Test
    @Story("Подписка на новости")
    @Step("Проверка подписки на новости")
    public void checkSubscribe(){
        gotoURL(testsData.otusURL());

        By emailInput = By.xpath("//input[@class='input footer2__subscribe-input']");
        getElement(emailInput).sendKeys(testsData.testEmail());
        log.info("Ввод тестового email в поле");

        By submitButton = By.xpath("//button[@class='footer2__subscribe-button button button_blue button_as-input']");
        getElement(submitButton).click();
        log.info("Нажатие на кнопку Подписаться");

        By subscribeConfirm = By.xpath("//p[@class='subscribe-modal__success']");
        String actual = getElement(subscribeConfirm).getText();
        Assert.assertEquals(testsData.subscribeConfirm(), actual);
        Allure.addAttachment("Подписка на новости", new ByteArrayInputStream(((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES)));
        log.info("Проверка текста об успешной подписке");
    }

    private WebElement getElement(By locator){
        return new WebDriverWait(driver,3)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void gotoURL(String URL){
        driver.get(URL);
        log.info("Переход по адресу " + URL);
    }
}