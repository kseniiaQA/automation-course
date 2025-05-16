package courseplayw;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.microsoft.playwright.*;

import io.qameta.allure.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Epic("Тесты для the-internet.herokuapp.com")
@Feature("Работа с JavaScript-алертами")
public class AdvancedReportingTest {
    private static ExtentReports extent;
    private ExtentTest extentTest;
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeAll
    static void setUpClass() {
        ExtentSparkReporter reporter = new ExtentSparkReporter("target/extent-report.html");
        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    @AfterAll
    static void tearDownClass() {
        if (extent != null) {
            extent.flush(); // Сохраняем отчет
        }
    }

    @BeforeEach
    @Step("Инициализация браузера и контекста")
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();
        page = context.newPage();
    }


    @Test
    @Story("Проверка алертов")
    @Description("Тест взаимодействия с JS-алертами")
    @Severity(SeverityLevel.NORMAL)
    void testJavaScriptAlerts() {
        extentTest = extent.createTest("Тест JS-алертов");

        try {
            // Открываем страницу
            Allure.step("Открыть страницу с алертами", () -> {
                page.navigate("https://the-internet.herokuapp.com/javascript_alerts");
                extentTest.pass("Страница загружена");
            });

            // Подписываемся на диалоговое окно
            final String[] alertTextHolder = new String[1]; // Для хранения текста алерта

            page.onDialog(dialog -> {
                alertTextHolder[0] = dialog.message();
                dialog.accept(); // Подтверждаем алерт
                extentTest.info("Обработан диалог: " + alertTextHolder[0]);
            });

            // Кликаем по кнопке Alert
            Allure.step("Кликнуть на кнопку Alert", () -> {
                page.click("button[onclick='jsAlert()']");
                extentTest.info("Клик выполнен");
            });

            // Проверка текста алерта для Allure и AssertJ
            Allure.step("Проверить текст алерта", () -> {
                assertThat(alertTextHolder[0]).isEqualTo("I am a JS Alert");
                extentTest.pass("Текст алерта корректен");
            });

            // Скриншот для ExtentReports
            String screenshotPath = "target/alert-success.png";
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));
            extentTest.addScreenCaptureFromPath(screenshotPath);

        } catch (Exception e) {
            // Скриншот при ошибке
            String errorScreenshotPath = "target/alert-error.png";
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(errorScreenshotPath)));
            Allure.addAttachment("Ошибка", "image/png", errorScreenshotPath);
            extentTest.fail("Тест упал: " + e.getMessage());
            throw e; // пробрасываем исключение дальше, чтобы тест считался неуспешным
        }
    }
}